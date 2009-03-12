/**
 * Copyright 2005-2009 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or CDL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.test.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.data.CharacterSet;
import org.restlet.data.Encoding;
import org.restlet.data.Form;
import org.restlet.data.Language;
import org.restlet.data.MediaType;
import org.restlet.data.Metadata;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Preference;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.engine.application.TunnelFilter;
import org.restlet.engine.http.HttpConstants;
import org.restlet.util.Series;

/**
 * Tests cases for the tunnel filter.
 */
public class TunnelFilterTestCase extends TestCase {

    /** . */
    private static final String EFFECTED = "http://example.org/adf.asdf/af.html";

    /** . */
    private static final String START_REF_FOR_PATH_TEST = "http://www.example.com/abc/def/";

    /** . */
    private static final String UNEFFECTED = "http://example.org/abc.def/af.ab";

    private List<Preference<CharacterSet>> accCharsets;

    private List<Preference<Encoding>> accEncodings;

    private List<Preference<Language>> accLanguages;

    private List<Preference<MediaType>> accMediaTypes;

    private String lastCreatedReference;

    private Request request;

    private Response response;

    private TunnelFilter tunnelFilter;

    void assertCharSets(CharacterSet... characterSets) {
        assertEqualSet(this.accCharsets, characterSets);
    }

    void assertEncodings(Encoding... encodings) {
        assertEqualSet(this.accEncodings, encodings);
    }

    <A extends Metadata> A assertEqualSet(List<? extends Preference<A>> actual,
            A... expected) {
        if (actual.size() != expected.length) {
            System.out.println("Is:     " + actual);
            System.out.println("Should: " + Arrays.asList(expected));
        }
        assertEquals(actual.size(), expected.length);
        boolean contained = false;
        for (final Metadata exp : expected) {
            for (final Preference<? extends Metadata> act : actual) {
                if (exp.equals(act.getMetadata())) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                final String message = exp
                        + " should be in, but is missing in " + actual;
                fail(message);
            }
        }
        return null;
    }

    void assertLanguages(Language... languages) {
        assertEqualSet(this.accLanguages, languages);
    }

    void assertMediaTypes(MediaType... mediaTypes) {
        assertEqualSet(this.accMediaTypes, mediaTypes);
    }

    void assertMethod(Method method) {
        assertEquals(this.request.getMethod(), method);
    }

    void assertNotSameMethod(Method method) {
        assertNotSame(this.request.getMethod(), method);
    }

    /**
     * @param expectedCut
     * @param expectedExtensions
     */
    private void check(String expectedCut, String expectedExtensions) {
        final Reference resourceRef = this.request.getResourceRef();
        assertEquals(expectedCut, resourceRef.toString());

        final Reference originalRef = this.request.getOriginalRef();
        assertEquals(originalRef, new Reference(this.lastCreatedReference));
        assertEquals(expectedCut, resourceRef.toString());
        assertEquals(expectedExtensions, resourceRef.getExtensions());
    }

    /**
     * 
     * @param expectedSubPathCut
     *            if null, the same as subPathOrig
     * @param expectedExtension
     *            if null, then same as "" for this test
     */
    private void checkFromPath(String expectedSubPathCut,
            String expectedExtension) {
        if (expectedSubPathCut == null) {
            check(this.lastCreatedReference, expectedExtension);
        } else {
            check(START_REF_FOR_PATH_TEST + expectedSubPathCut,
                    expectedExtension);
        }
    }

    /**
     * @see #createGetFromPath(String)
     * @see #createRequest(Method, String)
     */
    void createGet(String reference) {
        createRequest(Method.GET, reference);
    }

    /**
     * 
     * @param subPathToCheck
     * @see #createGet(String)
     * @see #createRequest(Method, String)
     */
    private void createGetFromPath(String subPathToCheck) {
        createGet(START_REF_FOR_PATH_TEST + subPathToCheck);
    }

    /**
     * 
     */
    void createPost(String reference) {
        createRequest(Method.POST, reference);
    }

    /**
     * Creates a {@link Request} and put it into {@link #request}.<br>
     * To use the methods provided by the test case class use ever the provided
     * create methods to create a request.
     * 
     * @param method
     * @param reference
     * @see #createPost(String)
     * @see #createGet(String)
     * @see #createGetFromPath(String)
     */
    void createRequest(Method method, String reference) {
        this.request = new Request(method, reference);
        this.request.setOriginalRef(new Reference(reference));
        this.response = new Response(this.request);
        this.lastCreatedReference = reference;
        setPrefs();
    }

    private void extensionTunnelOff() {
        final Application application = this.tunnelFilter.getApplication();
        application.getTunnelService().setExtensionsTunnel(false);
    }

    /**
     * Call this method to filter the current request
     */
    private void filter() {
        this.tunnelFilter.beforeHandle(this.request, this.response);
        setPrefs();
    }

    private void setPrefs() {
        this.accMediaTypes = this.request.getClientInfo()
                .getAcceptedMediaTypes();
        this.accLanguages = this.request.getClientInfo().getAcceptedLanguages();
        this.accCharsets = this.request.getClientInfo()
                .getAcceptedCharacterSets();
        this.accEncodings = this.request.getClientInfo().getAcceptedEncodings();
    }

    @Override
    public void setUp() {
        final Application app = new Application(new Context());
        Application.setCurrent(app);
        this.tunnelFilter = new TunnelFilter(app.getContext());
        this.tunnelFilter.getApplication().getTunnelService()
                .setExtensionsTunnel(true);
    }

    public void testExtMappingOff1() {
        extensionTunnelOff();
        createGet(UNEFFECTED);
        this.accLanguages
                .add(new Preference<Language>(Language.valueOf("ajh")));
        this.accMediaTypes.add(new Preference<MediaType>(
                MediaType.APPLICATION_STUFFIT));
        filter();
        assertEquals(UNEFFECTED, this.request.getResourceRef().toString());
        assertLanguages(Language.valueOf("ajh"));
        assertMediaTypes(MediaType.APPLICATION_STUFFIT);
        assertCharSets();
        assertEncodings();
    }

    public void testExtMappingOff2() {
        extensionTunnelOff();
        createGet(EFFECTED);
        this.accLanguages
                .add(new Preference<Language>(Language.valueOf("ajh")));
        this.accMediaTypes.add(new Preference<MediaType>(
                MediaType.APPLICATION_STUFFIT));
        filter();
        assertEquals(EFFECTED, this.request.getResourceRef().toString());
        assertLanguages(Language.valueOf("ajh"));
        assertMediaTypes(MediaType.APPLICATION_STUFFIT);
        assertCharSets();
        assertEncodings();
    }

    public void testExtMappingOn() {
        createGet(UNEFFECTED);
        filter();
        check(UNEFFECTED, "ab");
        assertLanguages();
        assertCharSets();
        assertCharSets();
        assertMediaTypes();

        createGet(EFFECTED);
        filter();
        check("http://example.org/adf.asdf/af", null);
        assertMediaTypes(MediaType.TEXT_HTML);
        assertLanguages();
        assertCharSets();
        assertCharSets();

        createGetFromPath("afhhh");
        filter();
        checkFromPath(null, null);
        assertEqualSet(this.accMediaTypes);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.afsdf");
        filter();
        checkFromPath(null, "afsdf");
        assertMediaTypes();
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.afsdf.html");
        filter();
        checkFromPath("hksf.afsdf", "afsdf");
        assertMediaTypes(MediaType.TEXT_HTML);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.afsdf.html.txt");
        filter();
        checkFromPath("hksf.afsdf.html", "afsdf.html");
        assertMediaTypes(MediaType.TEXT_PLAIN);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.html.afsdf.txt");
        filter();
        checkFromPath("hksf.html.afsdf", "html.afsdf");
        assertMediaTypes(MediaType.TEXT_PLAIN);
        assertLanguages();
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.html.afsdf.txt.en.fr");
        filter();
        checkFromPath("hksf.html.afsdf.txt.en", "html.afsdf.txt.en");
        // Take care about the fact that only one extension per metadata "type"
        // is allowed: ie only one Language, one encoding, one media type, etc.
        // assertMediaTypes(MediaType.TEXT_PLAIN);
        assertMediaTypes();
        assertLanguages(Language.FRENCH);
        assertEncodings();
        assertCharSets();

        createGetFromPath("hksf.html.afsdf.txt.en");
        filter();
        checkFromPath("hksf.html.afsdf", "html.afsdf");
        assertMediaTypes(MediaType.TEXT_PLAIN);
        assertLanguages(Language.ENGLISH);
        assertEncodings();
        assertCharSets();

        createGet(START_REF_FOR_PATH_TEST);
        filter();
        checkFromPath(null, null);
        assertMediaTypes();
        assertLanguages();
        assertEncodings();
        assertCharSets();
    }

    public void testMethodTunnelingViaHeader() {
        tunnelFilter.getTunnelService().setMethodTunnel(true);
        Map<String, Object> attributesHeader = new HashMap<String, Object>();
        Series<Parameter> headers = new Form();
        headers.add(HttpConstants.HEADER_X_HTTP_METHOD_OVERRIDE, Method.GET
                .getName());
        headers.add(HttpConstants.HEADER_X_FORWARDED_FOR, "TEST");
        attributesHeader.put(HttpConstants.ATTRIBUTE_HEADERS, headers);

        createGet(UNEFFECTED);
        this.request.setAttributes(attributesHeader);
        filter();
        assertMethod(Method.GET);

        createPost(UNEFFECTED);
        filter();
        assertMethod(Method.POST);

        createPost(UNEFFECTED);
        tunnelFilter.getTunnelService().setMethodHeader(
                HttpConstants.HEADER_X_FORWARDED_FOR);
        this.request.setAttributes(attributesHeader);
        filter();
        assertNotSameMethod(Method.PUT);

        createPost(UNEFFECTED);
        tunnelFilter.getTunnelService().setMethodHeader(
                HttpConstants.HEADER_X_FORWARDED_FOR);
        tunnelFilter.getTunnelService().setHeaderTunnel(false);
        this.request.setAttributes(attributesHeader);
        filter();
        assertMethod(Method.POST);

    }

    public void testWithMatrixParam() {
        createGet(EFFECTED + ";abcdef");
        filter();
        check("http://example.org/adf.asdf/af;abcdef", null);
        assertMediaTypes(MediaType.TEXT_HTML);
        assertLanguages();
        assertCharSets();
        assertCharSets();
    }
}

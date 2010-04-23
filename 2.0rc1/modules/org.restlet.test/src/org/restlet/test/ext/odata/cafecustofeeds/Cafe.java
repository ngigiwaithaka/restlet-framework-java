/**
 * Copyright 2005-2010 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
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

package org.restlet.test.ext.odata.cafecustofeeds;


import java.util.List;
import org.restlet.test.ext.odata.cafecustofeeds.Contact;
import org.restlet.test.ext.odata.cafecustofeeds.Item;

/**
* Generated by the generator tool for the WCF Data Services extension for the Restlet framework.<br>
*
* @see <a href="http://localhost:8182/CafeCustoFeeds.svc/$metadata">Metadata of the target WCF Data Services</a>
*
*/
public class Cafe {

private String city;
private String companyName;
private String id;
private String name;
private int zipCode;
private Contact contact;
private List<Item> items;    /**
     * Constructor without parameter.
     * 
     */
    public Cafe() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param id
     *            The identifiant value of the entity.
     */
    public Cafe(String id) {
        this();
        this.id = id;
    }
    
   /**
    * Returns the value of the city attribute.
    *
    * @return The value of the city attribute.
    */
   public String getCity() {
      return city;
   }
   
   /**
    * Returns the value of the companyName attribute.
    *
    * @return The value of the companyName attribute.
    */
   public String getCompanyName() {
      return companyName;
   }
   
   /**
    * Returns the value of the id attribute.
    *
    * @return The value of the id attribute.
    */
   public String getId() {
      return id;
   }
   
   /**
    * Returns the value of the name attribute.
    *
    * @return The value of the name attribute.
    */
   public String getName() {
      return name;
   }
   
   /**
    * Returns the value of the zipCode attribute.
    *
    * @return The value of the zipCode attribute.
    */
   public int getZipCode() {
      return zipCode;
   }
   
   /**
    * Returns the value of the contact attribute.
    *
    * @return The value of the contact attribute.
    */
   public Contact getContact() {
      return contact;
   }
   
   /**
    * Returns the value of the items attribute.
    *
    * @return The value of the items attribute.
    */
   public List<Item> getItems() {
      return items;
   }
   

   /**
    * Sets the value of the city attribute.
    *
    * @param City
    *     The value of the city attribute.
    */
   public void setCity(String city) {
      this.city = city;
   }
   
   /**
    * Sets the value of the companyName attribute.
    *
    * @param CompanyName
    *     The value of the companyName attribute.
    */
   public void setCompanyName(String companyName) {
      this.companyName = companyName;
   }
   
   /**
    * Sets the value of the id attribute.
    *
    * @param ID
    *     The value of the id attribute.
    */
   public void setId(String id) {
      this.id = id;
   }
   
   /**
    * Sets the value of the name attribute.
    *
    * @param Name
    *     The value of the name attribute.
    */
   public void setName(String name) {
      this.name = name;
   }
   
   /**
    * Sets the value of the zipCode attribute.
    *
    * @param ZipCode
    *     The value of the zipCode attribute.
    */
   public void setZipCode(int zipCode) {
      this.zipCode = zipCode;
   }
   
   /**
    * Sets the value of the contact attribute.
    *
    * @param contact
    *     The value of the contact attribute.
    */
   public void setContact(Contact contact) {
      this.contact = contact;
   }

   /**
    * Sets the value of the items attribute.
    *
    * @param items
    *     The value of the items attribute.
    */
   public void setItems(List<Item> items) {
      this.items = items;
   }


}
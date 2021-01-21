//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2017.09.18 at 02:35:45 PM BST
//

package net.sf.mpxj.primavera.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ActivityFilterType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ActivityFilterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ActivityFilterId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ActivityFilterName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FilterCriteria" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD) @XmlType(name = "ActivityFilterType", propOrder =
{
   "activityFilterId",
   "activityFilterName",
   "filterCriteria"
}) public class ActivityFilterType
{

   @XmlElement(name = "ActivityFilterId") protected Integer activityFilterId;
   @XmlElement(name = "ActivityFilterName") protected String activityFilterName;
   @XmlElement(name = "FilterCriteria") protected String filterCriteria;

   /**
    * Gets the value of the activityFilterId property.
    *
    * @return
    *     possible object is
    *     {@link Integer }
    *
    */
   public Integer getActivityFilterId()
   {
      return activityFilterId;
   }

   /**
    * Sets the value of the activityFilterId property.
    *
    * @param value
    *     allowed object is
    *     {@link Integer }
    *
    */
   public void setActivityFilterId(Integer value)
   {
      this.activityFilterId = value;
   }

   /**
    * Gets the value of the activityFilterName property.
    *
    * @return
    *     possible object is
    *     {@link String }
    *
    */
   public String getActivityFilterName()
   {
      return activityFilterName;
   }

   /**
    * Sets the value of the activityFilterName property.
    *
    * @param value
    *     allowed object is
    *     {@link String }
    *
    */
   public void setActivityFilterName(String value)
   {
      this.activityFilterName = value;
   }

   /**
    * Gets the value of the filterCriteria property.
    *
    * @return
    *     possible object is
    *     {@link String }
    *
    */
   public String getFilterCriteria()
   {
      return filterCriteria;
   }

   /**
    * Sets the value of the filterCriteria property.
    *
    * @param value
    *     allowed object is
    *     {@link String }
    *
    */
   public void setFilterCriteria(String value)
   {
      this.filterCriteria = value;
   }

}

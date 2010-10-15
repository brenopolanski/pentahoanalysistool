package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="axis")
public class AxisObject {

        @XmlAttribute(name = "location", required = true)
		String location;
        @XmlAttribute(name = "nonempty", required = true)
		Boolean nonempty;
        @XmlElement(name = "dimensions", required = true)
        DimensionObject[] dimList;

		
		public String getLocation() {
			return location;
		}

		public void setLocation(String name) {
			this.location = name;
		}

		
		public Boolean getNonEmpty() {
			return nonempty;
		}

		public void setNonEmpty(Boolean nonempty) {
			this.nonempty = nonempty;
		}

		public DimensionObject[] getDims() {
			return dimList;
		}

		public void setDims(DimensionObject[] dims) {
			this.dimList = dims;
		}


	public void newAxis(String name, boolean nonempty, DimensionObject[] dims) {
		
		setLocation(name);
		setNonEmpty(nonempty);
		setDims(dims);
	}


}

package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("restriction")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="dimension")
public class DimensionObject {


        @XmlAttribute(name = "dimensionname", required = true)
		String dimensionname;


        @XmlElement(name = "levels", required = true)
		LevelObject[] members;

		public LevelObject[] getLevels() {
			return members;
		}

		public void setLevels(LevelObject[] levelObj) {
			this.members = levelObj;
		}

		public void setName(String name) {
			this.dimensionname = name;
		}

		public String getName() {
			return dimensionname;
		}

	public void newDimension(String name, LevelObject[] levelObj) {
		setName(name);
		setLevels(levelObj);

	}


}

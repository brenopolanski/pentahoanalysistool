package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DimensionObject {

	public static class Dimension {

		String dimensionname;

		LevelObject members;

		@XmlElement(name = "levels", required = true)
		public LevelObject getLevels() {
			return members;
		}

		public void setLevels(LevelObject members) {
			this.members = members;
		}

		public void setName(String name) {
			this.dimensionname = name;
		}

		@XmlAttribute(name = "dimensionname", required = true)
		public String getName() {
			return dimensionname;
		}

	}

	@XmlElement(name = "dimension", required = true)
	private List<Dimension> names = new ArrayList<Dimension>();

	public void newDimension(String name, LevelObject mob) {
		Dimension dim = new Dimension();
		dim.setName(name);
		dim.setLevels(mob);

		names.add(dim);
	}

	public List<Dimension> getDimensionList() {
		return names;
	}

}

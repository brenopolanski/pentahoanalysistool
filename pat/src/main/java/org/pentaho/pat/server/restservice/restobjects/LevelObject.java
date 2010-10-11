package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LevelObject {

	public static class Level {

		String name;

		String caption;

		MemberObject mob;

		@XmlElement(name = "members", required = true)
		public MemberObject getMob() {
			return mob;
		}

		public void setMob(MemberObject mob) {
			this.mob = mob;
		}

		@XmlAttribute(name = "levelcaption", required = true)
		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public void setName(String name) {
			this.name = name;
		}

		@XmlAttribute(name = "levelname", required = true)
		public String getName() {
			return name;
		}
	}

	@XmlElement(name = "level", required = true)
	private List<Level> names = new ArrayList<Level>();

	public void newLevel(String name, String caption, MemberObject mob) {
		Level dim = new Level();
		dim.setName(name);
		dim.setCaption(caption);
		dim.setMob(mob);
		names.add(dim);
	}

	public List<Level> getLevelList() {
		return names;
	}

}

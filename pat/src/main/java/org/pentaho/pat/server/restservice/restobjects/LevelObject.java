package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@SuppressWarnings("restriction")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="level")
public class LevelObject {

        @XmlAttribute(name = "levelname", required = true)
		String name;

		@XmlAttribute(name = "levelcaption", required = true)
		String caption;


        @XmlElement(name = "members", required = true)
		MemberObject[] mob;

		public MemberObject[] getMob() {
			return mob;
		}

		public void setMob(MemberObject[] mob) {
			this.mob = mob;
		}

		
		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public void setName(String name) {
			this.name = name;
		}

		
		public String getName() {
			return name;
		}
	

	public void newLevel(String name, String caption, MemberObject[] mob) {
		
		setName(name);
		setCaption(caption);
		setMob(mob);
	}

}

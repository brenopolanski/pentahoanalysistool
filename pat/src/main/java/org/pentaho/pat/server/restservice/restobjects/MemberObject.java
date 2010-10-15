package org.pentaho.pat.server.restservice.restobjects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.pentaho.pat.rpc.dto.enums.SelectionType;
import org.pentaho.pat.server.restservice.enums.Status;

@SuppressWarnings("restriction")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="level")
public class MemberObject {

        @XmlAttribute(name = "membername", required = true)
		String name;
	    @XmlAttribute(name = "membercaption", required = true)
		String caption;
	    @XmlAttribute(name = "status", required = true)
		Status status;
        @XmlAttribute(name = "type", required = true)
		SelectionType type;

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		
		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}


		public SelectionType getType() {
			return type;
		}

		public void setType(SelectionType type) {
			this.type = type;
		}

		public void setName(String name) {
			this.name = name;
		}


		public String getName() {
			return name;
		}


	public void newMember(String name, String caption, String status,
			SelectionType selectionType) {
		setName(name);
		setCaption(caption);
		setStatus(Status.valueOf(status));
		setType(selectionType);
	}

}

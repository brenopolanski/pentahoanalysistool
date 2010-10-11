package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.pentaho.pat.rpc.dto.enums.SelectionType;
import org.pentaho.pat.server.restservice.enums.Status;

@XmlRootElement
public class MemberObject {


	public static class Member {

		String name;

		String caption;

		Status status;

		SelectionType type;

		@XmlAttribute(name = "membercaption", required = true)
		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		@XmlAttribute(name = "status", required = true)
		public Status getStatus() {
			return status;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

		@XmlAttribute(name = "type", required = true)
		public SelectionType getType() {
			return type;
		}

		public void setType(SelectionType type) {
			this.type = type;
		}

		public void setName(String name) {
			this.name = name;
		}

		@XmlAttribute(name = "membername", required = true)
		public String getName() {
			return name;
		}
	}

	@XmlElement(name = "member", required = true)
	private List<Member> names = new ArrayList<Member>();

	public void newMember(String name, String caption, String status,
			SelectionType selectionType) {
		Member dim = new Member();
		dim.setName(name);
		dim.setCaption(caption);
		dim.setStatus(Status.valueOf(status));
		dim.setType(selectionType);
		names.add(dim);
	}

	public List<Member> getMemberList() {
		return names;
	}

}

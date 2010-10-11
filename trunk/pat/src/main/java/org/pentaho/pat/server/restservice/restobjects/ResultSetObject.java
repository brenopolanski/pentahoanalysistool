package org.pentaho.pat.server.restservice.restobjects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.pentaho.pat.rpc.dto.CellDataSet;
import org.pentaho.pat.rpc.dto.celltypes.AbstractBaseCell;

@SuppressWarnings("restriction")
@XmlRootElement
public class ResultSetObject {

	public static class Row {

		List<String> contents = new ArrayList<String>();

		String name;

		public void setContents(String name) {
			contents.add(name);
		}

		public void setName(String name) {
			this.name = name;
		}

		@XmlElement(name = "ROW", required = true)
		public List<String> getContents() {
			return contents;
		}

		@XmlElement(name = "CONTENT", required = true)
		public String getName() {
			return name;
		}
	}

	CellDataSet cds;

	@XmlElement(name = "result", required = true)
	private List<Row> result = new ArrayList<Row>();

	public ResultSetObject() {

	}

	public ResultSetObject(CellDataSet cds) {
		this.cds = cds;
		generate();
	}

	private void generate() {
		// AbstractBaseCell[][] headers = cds.getCellSetHeaders();
		AbstractBaseCell[][] body = cds.getCellSetBody();

		for (int i = 0; i < body.length; i++) {
			Row row = new Row();
			for (int j = 0; j < body[i].length; j++) {
				AbstractBaseCell cell = body[i][j];
				row.setContents(cell.getFormattedValue());
			}
			result.add(row);
		}
	}

}

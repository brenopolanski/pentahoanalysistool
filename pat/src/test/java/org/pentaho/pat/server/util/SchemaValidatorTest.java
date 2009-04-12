package org.pentaho.pat.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.pentaho.pat.server.services.impl.AbstractServiceTest;

public class SchemaValidatorTest extends AbstractServiceTest {

	private String schema = null;

	public void testValidateSchema() throws Exception {

		initTest();
		
		String result = SchemaValidator.validateAgainstXsd(this.schema);
		assertNull(result,result);

		result = SchemaValidator.validateAgainstXsd(this.schema+"ERROR STUFF"); //$NON-NLS-1$
		assertEquals("Invalid XML file : Content is not allowed in trailing section.",result); //$NON-NLS-1$
		
		result = SchemaValidator.validateAgainstXsd(
			this.schema.replace(
				"<Dimension name=\"Positions\">",  //$NON-NLS-1$
				"<Dimension name=\"Positions\" someRandomAttribute=\"whatever\">")); //$NON-NLS-1$
		assertEquals("Invalid XML file : cvc-complex-type.3.2.2: Attribute 'someRandomAttribute' is not allowed to appear in element 'Dimension'.", result); //$NON-NLS-1$
	
		finishTest();
	}

	private void initTest() {
		initTestContext();
		if (schema==null)
			try {
				this.schema=readFileAsString(new File(
					SchemaValidatorTest.class.getResource("SampleData.mondrian.xml").toURI())); //$NON-NLS-1$
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		if(schema==null)
			fail("Unable to read the test schema file."); //$NON-NLS-1$
	}

	private void finishTest() {
	}

	private static String readFileAsString(File file)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}

}

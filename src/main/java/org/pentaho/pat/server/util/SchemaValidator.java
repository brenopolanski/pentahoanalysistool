package org.pentaho.pat.server.util;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public abstract class SchemaValidator implements ErrorHandler {

	public static String validateAgainstXsd(String xml) {
		
		try {
			// build an XSD-aware SchemaFactory
			SchemaFactory factory = 
	            SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$

			// get the custom xsd schema describing the required format for my XML files.
			Schema schemaXSD = factory.newSchema( 
					new File ( SchemaValidator.class.getResource("mondrian.xsd").toURI() ) ); //$NON-NLS-1$
			
			// Create a Validator capable of validating XML files according to my custom schema.
			Validator validator = schemaXSD.newValidator();
	
			// Get a parser capable of parsing vanilla XML into a DOM tree
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	
			// parse the XML purely as XML and get a DOM tree represenation.
			Document document = parser.parse( new ByteArrayInputStream(xml.getBytes()) );
	
			// parse the XML DOM tree againts the stricter XSD schema
			validator.validate( new DOMSource( document ) );
			
			return null;
		}
		catch (SAXException e)
		{
			return "Invalid XML file : "+e.getMessage(); //$NON-NLS-1$
		}
		catch (Throwable t)
		{
			return t.getClass().getCanonicalName() + 
					" : " + t.getMessage(); //$NON-NLS-1$
		}
	}
}

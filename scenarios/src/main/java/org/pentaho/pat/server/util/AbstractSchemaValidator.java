/*
 * Copyright (C) 2009 Luc Boudreau
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along 
 * with this program; if not, write to the Free Software Foundation, Inc., 
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 *
 */
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

public abstract class AbstractSchemaValidator implements ErrorHandler {

    public static String validateAgainstXsd(final String xml) {

        try {
            // build an XSD-aware SchemaFactory
            final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema"); //$NON-NLS-1$

            // get the custom xsd schema describing the required format for my XML files.
            final Schema schemaXSD = factory.newSchema(new File(AbstractSchemaValidator.class.getResource(
                    "mondrian.xsd").toURI())); //$NON-NLS-1$

            // Create a Validator capable of validating XML files according to my custom schema.
            final Validator validator = schemaXSD.newValidator();

            // Get a parser capable of parsing vanilla XML into a DOM tree
            final DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // parse the XML purely as XML and get a DOM tree represenation.
            final Document document = parser.parse(new ByteArrayInputStream(xml.getBytes()));

            // parse the XML DOM tree againts the stricter XSD schema
            validator.validate(new DOMSource(document));

            return null;
        } catch (SAXException e) {
            return "Invalid XML file : " + e.getMessage(); //$NON-NLS-1$
        } catch (Throwable t) {
            return t.getClass().getCanonicalName() + " : " + t.getMessage(); //$NON-NLS-1$
        }
    }
}

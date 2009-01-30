package org.pentaho.pat.test;

import java.util.Properties;

import junit.framework.TestCase;

import org.olap4j.OlapException;
import org.pentaho.pat.server.services.SessionService;

public class TestContext extends TestCase {

	private static boolean IS_INIT_DONE = false;
	
	private static Properties testProps = new Properties();

	protected void initTestContext() {
		if (!IS_INIT_DONE)
		{
			try {
				// Load test context properties.
				testProps.loadFromXML( TestContext.class.getResourceAsStream("test.properties.xml") );
			
				IS_INIT_DONE = true;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected String getTestProperty(String key) 
	{
		if (!IS_INIT_DONE)
			throw new RuntimeException("You can't use the context properties unless you initialize a test context first.");
			
		return testProps.getProperty(key);
	}
	
	
	protected void createConnection(SessionService service, String userId, String sessionId)
	{
		if (!IS_INIT_DONE)
			throw new RuntimeException("You can't use the context properties unless you initialize a test context first.");
		
		String url = getTestProperty("olap4j.jdbc.url");
		url = url.replace("{mondrian.jdbc.url}", getTestProperty("mondrian.jdbc.url"));
		url = url.replace("{mondrian.catalog}", getTestProperty("mondrian.catalog"));
		url = url.replace("{mondrian.jdbc.driver}", getTestProperty("mondrian.jdbc.driver"));
		
		try {
			service.createConnection(userId, sessionId, getTestProperty("olap4j.driver"), url, null, null);
		} catch (OlapException e) {
			throw new RuntimeException(e);
		}
	}
	
}

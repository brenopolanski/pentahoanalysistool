package org.pentaho.pat.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
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
				
				// Load the test SQL structure
				String sql = slurp(TestContext.class.getResourceAsStream("sampledata.sql"));
				
				// First, create the in-memory instance
				Class.forName(getTestProperty("context.driver"));
				Connection c = DriverManager.getConnection(getTestProperty("context.database"), 
						getTestProperty("context.username"), getTestProperty("context.password"));
				
				Statement stm =  c.createStatement();
				stm.execute(sql);
				stm.close();
				c.close();
			
				IS_INIT_DONE = true;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	protected String getTestProperty(String key) 
	{
		return testProps.getProperty(key);
	}
	
	
	protected void createConnection(SessionService service, String userId, String sessionId)
	{
		if (!IS_INIT_DONE)
			throw new RuntimeException("You can't use the context properties unless you initialize a test context first.");
		
		try {
			service.createConnection(userId, sessionId, getTestProperty("olap4j.driver"), getTestProperty("mondrian.url"), null, null);
		} catch (OlapException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String slurp (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}

	
}

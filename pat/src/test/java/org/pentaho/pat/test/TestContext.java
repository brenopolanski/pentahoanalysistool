package org.pentaho.pat.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.TestCase;

import org.hsqldb.jdbc.jdbcDataSource;
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
				
				
				// Create the datasource
				jdbcDataSource ds = new jdbcDataSource();
				ds.setDatabase(getTestProperty("context.database"));
				ds.setUser(getTestProperty("context.username"));
				ds.setPassword(getTestProperty("context.password"));
				
				// Bind the datasource in the directory
				Context ctx = new InitialContext();
				ctx.bind(getTestProperty("context.jndi"), ds);
				
				// Create the schema
				Connection c = ds.getConnection();
				Statement stm =  c.createStatement();
				slurp(stm, TestContext.class.getResourceAsStream("sampledata.sql"));
				stm.executeBatch();
				stm.clearBatch();
				stm.close();
				c.commit();
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
	
	private void slurp (Statement stm, InputStream stream) throws Exception 
	{
		DataInputStream in = new DataInputStream(stream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    
	    String strLine;
	    
	    while ((strLine = br.readLine()) != null)   {
	    	//stm.addBatch(strLine);
	    	stm.execute(strLine);
	    }
	    
	    in.close();
	}

	
}

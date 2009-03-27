package org.pentaho.pat.server.data.impl;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class AbstractManagerTest extends TestCase {

    private final String[] contextFiles = new String[] { 
        "/src/main/webapp/WEB-INF/pat-applicationContext.xml",
        "/src/test/java/org/pentaho/pat/server/data/impl/applicationContextOverrides.xml"
    };

    private static Properties testProps = new Properties();
    protected static ApplicationContext applicationContext = null;
    private static boolean IS_INIT_DONE = false;

    protected void init() {

        if (!IS_INIT_DONE) {
            try {

                testProps.loadFromXML(AbstractManagerTest.class
                        .getResourceAsStream("test.properties.xml"));

                // Create the application datasource
                jdbcDataSource ds = new jdbcDataSource();
                ds.setDatabase(getTestProperty("context.application_database"));
                ds.setUser(getTestProperty("context.username"));
                ds.setPassword(getTestProperty("context.password"));
                
                // Bind the datasource in the directory
                Context ctx = new InitialContext();
                ctx.bind(getTestProperty("context.jndi"), ds);

                // Initialize the application context. This will also create the
                // default schema
                applicationContext = new FileSystemXmlApplicationContext(
                        contextFiles);

                // we now inject test data.
                Connection c = ds.getConnection();
                Statement stm = c.createStatement();
                slurp(stm, AbstractManagerTest.class.getResourceAsStream("pat.sql"));
                stm.executeBatch();
                stm.clearBatch();
                stm.close();
                c.commit();
                c.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected String getTestProperty(String key) {
        return testProps.getProperty(key);
    }

    private void slurp(Statement stm, InputStream stream) throws Exception {
        DataInputStream in = new DataInputStream(stream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;

        while ((strLine = br.readLine()) != null) {
            // stm.addBatch(strLine);
            stm.execute(strLine);
        }

        in.close();
    }
}

package org.pentaho.pat.server.services.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import junit.framework.TestCase;

import org.hsqldb.jdbc.jdbcDataSource;
import org.junit.Assert;
import org.olap4j.OlapException;
import org.pentaho.pat.server.services.SessionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public abstract class AbstractServiceTest extends TestCase {

    private static boolean IS_INIT_DONE = false;
    
    private static String connectionId;
    
    private static DataSource datasource = null;

    private final String[] contextFiles = new String[] { 
        "/src/main/webapp/WEB-INF/pat-applicationContext.xml", //$NON-NLS-1$
        "/src/main/webapp/WEB-INF/pat-sessionfactory.xml", //$NON-NLS-1$
        "/src/main/webapp/WEB-INF/pat-datasource.xml", //$NON-NLS-1$
        "/src/test/resources/org/pentaho/pat/server/services/impl/applicationContextOverrides.xml" //$NON-NLS-1$
    };

    protected static ApplicationContext applicationContext = null;

    private static Properties testProps = new Properties();

    protected void initTestContext() {
        if (!IS_INIT_DONE) {
            try {

                /*
                 * Step 1. Create a datasource for Mondrian tests.
                 */
                // Load test context properties.
                testProps.loadFromXML(AbstractServiceTest.class
                        .getResourceAsStream("test.properties.xml")); //$NON-NLS-1$

                // Create the mondrian datasource
                jdbcDataSource ds = new jdbcDataSource();
                ds.setDatabase(getTestProperty("context.database")); //$NON-NLS-1$
                ds.setUser(getTestProperty("context.username")); //$NON-NLS-1$
                ds.setPassword(getTestProperty("context.password")); //$NON-NLS-1$
                
                // Bind the datasource in the directory
                Context ctx = new InitialContext();
                ctx.bind(getTestProperty("context.jndi"), ds); //$NON-NLS-1$
                
                // Create the application datasource
                jdbcDataSource ds2 = new jdbcDataSource();
                ds2.setDatabase(getTestProperty("pat.database")); //$NON-NLS-1$
                ds2.setUser(getTestProperty("pat.username")); //$NON-NLS-1$
                ds2.setPassword(getTestProperty("pat.password")); //$NON-NLS-1$
                
                // Bind the datasource in the directory
                ctx.bind(getTestProperty("pat.jndi"), ds2); //$NON-NLS-1$
                
                // Initialize the application context. This will also create the
                // default schema
                applicationContext = new FileSystemXmlApplicationContext(
                        contextFiles);
                
                datasource=(DataSource)applicationContext.getBean("dataSource"); //$NON-NLS-1$

                // Create the mondrian schema
                Connection c = ds.getConnection();
                Statement stm = c.createStatement();
                slurp(stm, AbstractServiceTest.class
                        .getResourceAsStream("sampledata.sql")); //$NON-NLS-1$
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
        if (datasource!=null)
            initDatabase();
    }
    
    protected void initDatabase()
    {
        try 
        {
            /*
             * Step 1. Clear data.
             */
            Connection c = datasource.getConnection();
            Statement stm = c.createStatement();
            slurp(stm, AbstractServiceTest.class.getResourceAsStream("pat-delete.sql")); //$NON-NLS-1$
            stm.executeBatch();
            stm.clearBatch();
            stm.close();
            c.commit();
            c.close();

            /*
             * Step 2. Insert data
             */
            c = datasource.getConnection();
            stm = c.createStatement();
            slurp(stm, AbstractServiceTest.class.getResourceAsStream("pat-insert.sql")); //$NON-NLS-1$
            stm.executeBatch();
            stm.clearBatch();
            stm.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getTestProperty(String key) {
        return testProps.getProperty(key);
    }

    protected String createConnection(SessionService service, String userId,
            String sessionId) {
        if (!IS_INIT_DONE)
            throw new RuntimeException(
                    "You can't use the context properties unless you initialize a test context first."); //$NON-NLS-1$

        try {
            connectionId = service.createConnection(userId, sessionId,
                    getTestProperty("olap4j.driver"), //$NON-NLS-1$
                    getTestProperty("mondrian.url"), null, null); //$NON-NLS-1$
            return connectionId;
        } catch (OlapException e) {
            throw new RuntimeException(e);
        }
    }
    
    protected String[][] runOnDatasource(String sql) {
        try 
        {
            Connection c = datasource.getConnection();
            Statement stm = c.createStatement();
            ResultSet rst = stm.executeQuery(sql);
            
            int nbCols=rst.getMetaData().getColumnCount();
            List<String[]> rows = new ArrayList<String[]>();
            while(rst.next())
            {
                String[] currentRow = new String[nbCols];
                for (int colPos = 1; colPos<=nbCols;colPos++)
                    currentRow[colPos-1]=rst.getString(colPos);
                rows.add(currentRow);
            }
            
            String[][] resultArray = new String[rows.size()][nbCols];
            for(int rowPos = 0; rowPos < rows.size(); rowPos++) {
                resultArray[rowPos] = rows.get(rowPos);
            }
            
            rst.close();
            stm.close();
            c.close();
            
            return resultArray;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected void assertTwoDimensionArrayEquals(String[][] expected, String[][] actual)
    {
        Assert.assertEquals(expected.length, actual.length);
        for(int rowPos = 0; rowPos < expected.length; rowPos++) {
            Assert.assertArrayEquals(expected[rowPos], actual[rowPos]);
        }
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

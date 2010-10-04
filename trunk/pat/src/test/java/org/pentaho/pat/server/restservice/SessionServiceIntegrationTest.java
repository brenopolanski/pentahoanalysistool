package org.pentaho.pat.server.restservice;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.pat.server.data.pojo.ConnectionType;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.restservice.restobjects.SessionObject;
import org.pentaho.pat.server.services.SessionService;
import org.pentaho.pat.server.services.impl.AbstractServiceTest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.test.framework.AppDescriptor;
import com.sun.jersey.test.framework.WebAppDescriptor;

public class SessionServiceIntegrationTest extends AbstractServiceTest {

    private SessionService sessionService;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() {
        initTest();
        
        final SavedConnection sc2 = new SavedConnection();
        sc2.setName("Test");
        sc2.setType(ConnectionType.MONDRIAN);
        sc2.setDriverClassName("com.mysql.jdbc.Driver");
        sc2.setUrl("jdbc:mysql://localhost/sampledata");
        sc2.setUsername("root");
        sc2.setPassword(null);
        sc2.setSchemaData("<Schema name=\"SteelWheels\">\r\n\t<Cube name=\"SteelWheelsSales\" cache=\"true\" enabled=\"true\">\r\n\t\t<Table name=\"ORDERFACT\">\r\n\t\t</Table>\r\n\t\t<Dimension foreignKey=\"CUSTOMERNUMBER\" name=\"Markets\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Markets\" primaryKey=\"CUSTOMERNUMBER\" primaryKeyTable=\"\">\r\n\t\t\t\t<Table name=\"CUSTOMER_W_TER\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Territory\" column=\"TERRITORY\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Country\" column=\"COUNTRY\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"State Province\" column=\"STATE\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"City\" column=\"CITY\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension foreignKey=\"CUSTOMERNUMBER\" name=\"Customers\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Customers\" primaryKey=\"CUSTOMERNUMBER\">\r\n\t\t\t\t<Table name=\"CUSTOMER_W_TER\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Customer\" column=\"CUSTOMERNAME\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension foreignKey=\"PRODUCTCODE\" name=\"Product\">\r\n\t\t\t<Hierarchy name=\"\" hasAll=\"true\" allMemberName=\"All Products\" primaryKey=\"PRODUCTCODE\" primaryKeyTable=\"PRODUCTS\" caption=\"\">\r\n\t\t\t\t<Table name=\"PRODUCTS\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Line\" table=\"PRODUCTS\" column=\"PRODUCTLINE\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Vendor\" table=\"PRODUCTS\" column=\"PRODUCTVENDOR\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Product\" table=\"PRODUCTS\" column=\"PRODUCTNAME\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension type=\"TimeDimension\" foreignKey=\"TIME_ID\" name=\"Time\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Years\" primaryKey=\"TIME_ID\">\r\n\t\t\t\t<Table name=\"DIM_TIME\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Years\" column=\"YEAR_ID\" type=\"String\" uniqueMembers=\"true\" levelType=\"TimeYears\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Quarters\" column=\"QTR_NAME\" ordinalColumn=\"QTR_ID\" type=\"String\" uniqueMembers=\"false\" levelType=\"TimeQuarters\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Months\" column=\"MONTH_NAME\" ordinalColumn=\"MONTH_ID\" type=\"String\" uniqueMembers=\"false\" levelType=\"TimeMonths\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension foreignKey=\"STATUS\" name=\"Order Status\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Status Types\" primaryKey=\"STATUS\">\r\n\t\t\t\t<Level name=\"Type\" column=\"STATUS\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<!--     <Dimension name='Scenario' foreignKey='TIME_ID'>\r\n                  <Hierarchy primaryKey='TIME_ID' hasAll='true'>\r\n                    <InlineTable alias='foo'>\r\n                      <ColumnDefs>\r\n                        <ColumnDef name='foo' type='Numeric'/>\r\n                      </ColumnDefs>\r\n                      <Rows/>\r\n                    </InlineTable>\r\n                    <Level name='Scenario' column='foo'/>\r\n                  </Hierarchy>\r\n    </Dimension>-->\r\n\r\n\t\t<Measure name=\"Quantity\" column=\"QUANTITYORDERED\" formatString=\"#,###\" aggregator=\"sum\">\r\n\t\t</Measure>\r\n\t\t<Measure name=\"Sales\" column=\"TOTALPRICE\" formatString=\"#,###\" aggregator=\"sum\">\r\n\t\t</Measure>\r\n\t</Cube>\r\n</Schema>\r\n");
        sc2.setConnectOnStartup(true);
        
        this.sessionService.saveConnection("admin", sc2);
        
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    protected AppDescriptor configure() {
        ClientConfig cc = new DefaultClientConfig();
        // use the following jaxb context resolver
        // cc.getClasses().add(JAXBContextResolver.class);
        return new WebAppDescriptor.Builder("org.pentaho.pat.server.restservice").contextPath("rest").clientConfig(cc)
                .build();
    }

    /**
     * Test check GET on the "changes" resource in "application/xml" format.
     */
    @Test
    public void testGetOnLatestChangeXMLFormat() {

        ClientConfig config = new DefaultClientConfig();

        Client client = Client.create(config);

        client.addFilter(new HTTPBasicAuthFilter("admin", "admin"));

        client.addFilter(new LoggingFilter());
        WebResource r = client.resource("http://localhost:8080/rest/admin/session");
        Builder test = r.accept(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_XML_TYPE).type(
                MediaType.APPLICATION_FORM_URLENCODED);

        SessionObject blah = null;

        ClientResponse response = test.post(ClientResponse.class);

        try {

            blah = response.getEntity(SessionObject.class);

        } catch (Exception e) {

        }
        assertNotNull(blah.getSessionId());
    }
    
    private void initTest() {
        initTestContext();
        this.sessionService = (SessionService)applicationContext.getBean("sessionService"); //$NON-NLS-1$
    }
}

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
package org.pentaho.pat.server.restservice;

import org.apache.log4j.Logger;
import org.pentaho.pat.server.data.GroupManager;
import org.pentaho.pat.server.data.UserManager;
import org.pentaho.pat.server.data.pojo.ConnectionType;
import org.pentaho.pat.server.data.pojo.Group;
import org.pentaho.pat.server.data.pojo.SavedConnection;
import org.pentaho.pat.server.data.pojo.User;
import org.pentaho.pat.server.messages.Messages;
import org.springframework.beans.factory.InitializingBean;

public class TestDataLoader implements InitializingBean {

    private UserManager userManager = null;

    private GroupManager groupManager = null;

    private boolean loadDefaultData = false;

    private static final Logger LOG = Logger.getLogger(TestDataLoader.class);

    public void afterPropertiesSet() throws Exception {
        
            assert userManager != null;
            assert groupManager != null;
            // Test if data is already present
            if (userManager.getDefaultUsers().size() > 0) {
                return;
            }
            LOG.info(Messages.getString("Services.DefaultDataLoader.Loading")); //$NON-NLS-1$

            // Create default user
            final User admin = new User();
            admin.setUsername("admin"); //$NON-NLS-1$
            admin.setPassword("admin"); //$NON-NLS-1$
            admin.setEnabled(true);

            // Create default group
            final Group administrators = new Group();
            administrators.setName("Administrators"); //$NON-NLS-1$
            final Group users = new Group();
            users.setName("Users"); //$NON-NLS-1$

            // Create default connection
          
            
            
            final SavedConnection sc2 = new SavedConnection();
            sc2.setName("Test");
            sc2.setType(ConnectionType.MONDRIAN);
            sc2.setDriverClassName("com.mysql.jdbc.Driver");
            sc2.setUrl("jdbc:mysql://localhost/sampledata");
            sc2.setUsername("root");
            sc2.setPassword(null);
            sc2.setSchemaData("<Schema name=\"SteelWheels\">\r\n\t<Cube name=\"SteelWheelsSales\" cache=\"true\" enabled=\"true\">\r\n\t\t<Table name=\"ORDERFACT\">\r\n\t\t</Table>\r\n\t\t<Dimension foreignKey=\"CUSTOMERNUMBER\" name=\"Markets\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Markets\" primaryKey=\"CUSTOMERNUMBER\" primaryKeyTable=\"\">\r\n\t\t\t\t<Table name=\"CUSTOMER_W_TER\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Territory\" column=\"TERRITORY\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Country\" column=\"COUNTRY\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"State Province\" column=\"STATE\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"City\" column=\"CITY\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension foreignKey=\"CUSTOMERNUMBER\" name=\"Customers\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Customers\" primaryKey=\"CUSTOMERNUMBER\">\r\n\t\t\t\t<Table name=\"CUSTOMER_W_TER\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Customer\" column=\"CUSTOMERNAME\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension foreignKey=\"PRODUCTCODE\" name=\"Product\">\r\n\t\t\t<Hierarchy name=\"\" hasAll=\"true\" allMemberName=\"All Products\" primaryKey=\"PRODUCTCODE\" primaryKeyTable=\"PRODUCTS\" caption=\"\">\r\n\t\t\t\t<Table name=\"PRODUCTS\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Line\" table=\"PRODUCTS\" column=\"PRODUCTLINE\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Vendor\" table=\"PRODUCTS\" column=\"PRODUCTVENDOR\" type=\"String\" uniqueMembers=\"false\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Product\" table=\"PRODUCTS\" column=\"PRODUCTNAME\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension type=\"TimeDimension\" foreignKey=\"TIME_ID\" name=\"Time\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Years\" primaryKey=\"TIME_ID\">\r\n\t\t\t\t<Table name=\"DIM_TIME\">\r\n\t\t\t\t</Table>\r\n\t\t\t\t<Level name=\"Years\" column=\"YEAR_ID\" type=\"String\" uniqueMembers=\"true\" levelType=\"TimeYears\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Quarters\" column=\"QTR_NAME\" ordinalColumn=\"QTR_ID\" type=\"String\" uniqueMembers=\"false\" levelType=\"TimeQuarters\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t\t<Level name=\"Months\" column=\"MONTH_NAME\" ordinalColumn=\"MONTH_ID\" type=\"String\" uniqueMembers=\"false\" levelType=\"TimeMonths\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<Dimension foreignKey=\"STATUS\" name=\"Order Status\">\r\n\t\t\t<Hierarchy hasAll=\"true\" allMemberName=\"All Status Types\" primaryKey=\"STATUS\">\r\n\t\t\t\t<Level name=\"Type\" column=\"STATUS\" type=\"String\" uniqueMembers=\"true\" levelType=\"Regular\" hideMemberIf=\"Never\">\r\n\t\t\t\t</Level>\r\n\t\t\t</Hierarchy>\r\n\t\t</Dimension>\r\n\t\t<!--     <Dimension name='Scenario' foreignKey='TIME_ID'>\r\n                  <Hierarchy primaryKey='TIME_ID' hasAll='true'>\r\n                    <InlineTable alias='foo'>\r\n                      <ColumnDefs>\r\n                        <ColumnDef name='foo' type='Numeric'/>\r\n                      </ColumnDefs>\r\n                      <Rows/>\r\n                    </InlineTable>\r\n                    <Level name='Scenario' column='foo'/>\r\n                  </Hierarchy>\r\n    </Dimension>-->\r\n\r\n\t\t<Measure name=\"Quantity\" column=\"QUANTITYORDERED\" formatString=\"#,###\" aggregator=\"sum\">\r\n\t\t</Measure>\r\n\t\t<Measure name=\"Sales\" column=\"TOTALPRICE\" formatString=\"#,###\" aggregator=\"sum\">\r\n\t\t</Measure>\r\n\t</Cube>\r\n</Schema>\r\n");
            sc2.setConnectOnStartup(true);
           
            //admin.getSavedConnections().add(sc);
            admin.getSavedConnections().add(sc2);
            // Save
            groupManager.createDefaultGroup(administrators);
            groupManager.createDefaultGroup(users);
            userManager.createDefaultUser(admin);

            // Create default association
            administrators.getMembers().add(admin);
            users.getMembers().add(admin);
            admin.getGroups().add(administrators);
            admin.getGroups().add(users);

            groupManager.updateDefaultGroup(administrators);
            groupManager.updateDefaultGroup(users);

            LOG.info(Messages.getString("Services.DefaultDataLoader.Done")); //$NON-NLS-1$
        
    }

    public void setUserManager(final UserManager userManager) {
        this.userManager = userManager;
    }

    public void setLoadDefaultData(final boolean loadDefaultData) {
        this.loadDefaultData = loadDefaultData;
    }

    public void setGroupManager(final GroupManager groupManager) {
        this.groupManager = groupManager;
    }
}

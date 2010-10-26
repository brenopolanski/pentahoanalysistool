insert into pat_users ( username, password, enabled ) values ('admin', 'admin', 1);

insert into pat_groups ( name ) values ('Administrators');
insert into pat_groups ( name ) values ('Users');

insert into pat_connections ( id, name, driverClassName, password, type, url, connectonstartup , username, schemadata) values ('1111-1111-1111-1111','administrator_connection', 'org.hsqldb.jdbcDriver', '', 'aced00057372002f6f72672e70656e7461686f2e7061742e7365727665722e646174612e706f6a6f2e436f6e6e656374696f6e5479706500000000000000010200014c00046e616d657400124c6a6176612f6c616e672f537472696e673b78707400084d6f6e647269616e', 'jdbc:hsqldb:mem:pat_mondrian;default_schema=true;type=cached;write_delay=false','false', 'sa', '<?xml version="1.0"?><Schema name="SampleData"><!-- Shared dimensions -->  <Dimension name="Region">    <Hierarchy hasAll="true" allMemberName="All Regions">      <Table name="QUADRANT_ACTUALS"/>      <Level name="Region" column="REGION" uniqueMembers="true"/>    </Hierarchy>  </Dimension>  <Dimension name="Department">    <Hierarchy hasAll="true" allMemberName="All Departments">      <Table name="QUADRANT_ACTUALS"/>      <Level name="Department" column="DEPARTMENT" uniqueMembers="true"/>    </Hierarchy>  </Dimension>  <Dimension name="Positions">    <Hierarchy hasAll="true" allMemberName="All Positions">      <Table name="QUADRANT_ACTUALS"/>      <Level name="Positions" column="POSITIONTITLE" uniqueMembers="true"/>    </Hierarchy>  </Dimension>  <Cube name="Quadrant Analysis">    <Table name="QUADRANT_ACTUALS"/>    <DimensionUsage name="Region" source="Region"/>    <DimensionUsage name="Department" source="Department" />    <DimensionUsage name="Positions" source="Positions" />    <Measure name="Actual" column="ACTUAL" aggregator="sum" formatString="#,###.00"/>    <Measure name="Budget" column="BUDGET" aggregator="sum" formatString="#,###.00"/>    <Measure name="Variance" column="VARIANCE" aggregator="sum" formatString="#,###.00"/><!--    <CalculatedMember name="Variance Percent" dimension="Measures" formula="([Measures].[Variance]/[Measures].[Budget])*100" /> -->  </Cube></Schema>');

insert into pat_groups_users ( group_id, user_id ) values ('Administrators','admin');
insert into pat_groups_users ( group_id, user_id ) values ('Users','admin');

insert into pat_users_connections (user_id,connection_id) values ('admin','1111-1111-1111-1111');
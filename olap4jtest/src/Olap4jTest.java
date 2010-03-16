import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.olap4j.*;
import org.olap4j.layout.CellSetFormatter;
import org.olap4j.layout.RectangularCellSetFormatter;
import org.olap4j.layout.TraditionalCellSetFormatter;

public class Olap4jTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        
        String olap4jDriver = "org.olap4j.driver.xmla.XmlaOlap4jDriver"; //$NON-NLS-1$
//        String olap4jUrl = "jdbc:xmla:Server=http://localhost:8080/pentaho/Xmla?userid=joe&password=password";
//        String catalogName = "SampleData";
//        String mdx = "select NON EMPTY {[Measures].[Actual]} ON COLUMNS,   NON EMPTY Hierarchize(Union({[Region].[All Regions]}, [Region].[All Regions].Children)) ON ROWS from [Quadrant Analysis]";
//        String username = null;
//        String password = null;
        
//        MS AS 2008
//        String olap4jUrl = "jdbc:xmla:Server=http://192.168.150.130/xmla/msmdpump.dll";
//        String catalogName = "Adventure Works DW 2008";
//        String username = "paul";
//        String password = "asdf";
//        String mdx = "SELECT Measures.AllMembers ON COLUMNS,[Product].[Model Name].Children ON ROWS FROM [Adventure Works] ";
        
//        PALO
      String olap4jUrl = "jdbc:xmla:Server=http://localhost:4242/?userid=admin&password=admin";
      String catalogName = "Biker";
      String username = "admin";
      String password = "admin";
      String mdx = "SELECT NON EMPTY { [Measures].[Sales] } ON COLUMNS, NON EMPTY { [Channels].[All Channels], [Channels].[All Channels].Children } ON ROWS FROM [Orders] ";


        try {

            Class.forName(olap4jDriver);

            OlapConnection connection = null;
            if (username == null && password == null) {
                connection = (OlapConnection) DriverManager.getConnection(olap4jUrl);
            } else {
                connection = (OlapConnection) DriverManager
                .getConnection(olap4jUrl, username,password);
            }
            
            final OlapWrapper wrapper = connection;
            final OlapConnection olapConnection = wrapper.unwrap(OlapConnection.class);

            for (int s = 0 ; s < connection.getCatalogs().size() ; s++) {
                System.out.println("CATALOG: " + connection.getCatalogs().get(s).getName());
            }
            SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S" );
            df.setTimeZone( TimeZone.getDefault() );                  // nicht mehr unbedingt notwendig seit JDK 1.2
            // Formatierung zu String:
            System.out.println( "SET CATALOG = " + df.format( new Date() ) );        // z.B. '2001-01-26 19:03:56.731'
            OlapStatement stmt;
            try {
                if (catalogName != null) {
                    connection.setCatalog(catalogName);
                }
            } catch (SQLException e) {
                throw new OlapException("Error setting catalog for MDX statement: '" + catalogName + "'");
            }

            
            stmt = connection.createStatement();
            
            CellSet result = null;
            if (mdx != null && mdx.length() > 0) {
                System.out.println( "EXECUTE START = " + df.format( new Date() ) );        // z.B. '2001-01-26 19:03:56.731'
                result = stmt.executeOlapQuery(mdx);
                if (result != null ) {

                    for (Position axis_0 : result.getAxes().get(Axis.ROWS.axisOrdinal()).getPositions()) {
                        System.out.println("Axis 0:" + axis_0.toString());
                        boolean first = true;
                          for (Position axis_1 : result.getAxes().get(Axis.COLUMNS.axisOrdinal()).getPositions()) {
                              if(first) 
                                  System.out.print("Axis 1:" + axis_0.toString());

                              first = false;
                              Cell currentCell = result.getCell(axis_0, axis_1);
                              Object value = currentCell == null ?  "" : currentCell.getValue();
                              
                              System.out.print(value + " | ");
                          }
                      }
//                    CellSetFormatter formatter =
//                        new TraditionalCellSetFormatter();
//
//                    formatter.format(
//                        result,
//                        new PrintWriter(System.out, true));
//                    System.out.println( "EXECUTE END = " + df.format( new Date() ) );        // z.B. '2001-01-26 19:03:56.731'
//                    CellDataSet cs = OlapUtil.cellSet2Matrix(result);
//                    AbstractBaseCell[][] headers = cs.getCellSetHeaders();
//                    printResult(headers);
//                    printResult(cs.getCellSetBody());
//                    System.out.println("RESULT FINISH");
//                    System.out.println( "FINISH " + df.format( new Date() ) );        // z.B. '2001-01-26 19:03:56.731'
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void printResult(AbstractBaseCell[][] cellset) {
        for(int i = 0 ; i < cellset.length;i++) {
            String row = "";
            for(int k = 0 ; k < cellset[i].length;k++) {
                String value = cellset[i][k].getRawValue();
                if (value == null || value.equals("") || value.equals("null")) {
                    value = "\t\t";
                }
                row = row.concat(value).concat(" | ");
            }
            System.out.println(row);
            
        }
    }

}

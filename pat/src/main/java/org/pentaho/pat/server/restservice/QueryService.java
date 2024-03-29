package org.pentaho.pat.server.restservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.olap4j.query.Selection;
import org.pentaho.pat.rpc.dto.CubeItem;
import org.pentaho.pat.rpc.dto.MemberLabelItem;
import org.pentaho.pat.rpc.dto.QuerySaveModel;
import org.pentaho.pat.rpc.dto.enums.ObjectType;
import org.pentaho.pat.rpc.dto.enums.SelectionType;
import org.pentaho.pat.rpc.dto.query.IAxis;
import org.pentaho.pat.rpc.exceptions.RpcException;
import org.pentaho.pat.server.restservice.restobjects.AxisObject;
import org.pentaho.pat.server.restservice.restobjects.DimensionObject;
import org.pentaho.pat.server.restservice.restobjects.LevelObject;
import org.pentaho.pat.server.restservice.restobjects.MemberObject;
import org.pentaho.pat.server.restservice.restobjects.QueryObject;
import org.pentaho.pat.server.restservice.restobjects.ResultSetObject;
import org.pentaho.pat.server.servlet.DiscoveryServlet;
import org.pentaho.pat.server.servlet.QueryServlet;
import org.pentaho.pat.server.servlet.SessionServlet;
import org.springframework.context.annotation.Scope;

/**
 * The Query Service for the PAT Restful Interface, this is WIP DO NOT TAKE IT AS A FINSIHED API.
 * 
 * @author tom(at)wamonline.org.uk
 * @since 0.9.0
 */
@SuppressWarnings("restriction")
@Path("/{user}/query")
/*
 * to set the path on which the service will be accessed e.g. http://{serverIp}/{contextPath}/foo
 */
@Scope("request")
// to set the scope of service
public class QueryService {


    SessionServlet ss = new SessionServlet();

    QueryServlet qs = new QueryServlet();

    DiscoveryServlet ds = new DiscoveryServlet();

    /**
     * 
     * This method allows you to create a query object in one request, as per the idea of Tiemonster.<br>
     * To use this method you need to have a Session Object available.<br>
     * HTTP POST.<br>
     * 
     * <pre>
     * curl -XPOST -d "connectionId=792bcd2d-41c5-4af5-84b6-16fd65c4f7bb" -d "cubeName=SteelWheelsSales"  --basic -u "admin:admin" "http://localhost:8080/rest/53c72f85-7ff2-4a63-b4d2-dbd55a56255f/query"
     * </pre>
     * 
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws ServletException
     */
    @POST
    @Path("/{schema}/{cube}/{queryname}")
    @Produces( {"application/xml", "application/json"})
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized QueryObject createNewQuery(@FormParam("sessionid") String sessionId,
            @FormParam("connectionid") String connectionId, @PathParam("cube") String cube,
            @PathParam("user") String user, @PathParam("schema") String schema, @PathParam("queryname") String queryname)
            throws RpcException, ServletException {
        // ss.init();
        try {
            qs.init();
            ds.init();

            QueryObject qob = new QueryObject();
            qob.setQueryId(qs.createNewQuery(sessionId, connectionId, cube));

            AxisObject[] axis = new AxisObject[1];

            String[] dims = ds.getDimensions(sessionId, qob.getQueryId(), IAxis.Standard.valueOf("UNUSED"));
            DimensionObject[] dimObj = new DimensionObject[dims.length];
            for (int i = 0; i < dims.length; i++) {

                List<MemberLabelItem> levels = ds.getLevels(sessionId, qob.getQueryId(), dims[i]);
                LevelObject[] levelObj = new LevelObject[levels.size()];

                for (int j = 0; j < levels.size(); j++) {

                    List<MemberLabelItem> bers = ds.getLevelMembers(sessionId, qob.getQueryId(), levels.get(j)
                            .getName());

                    MemberObject[] memberObj = new MemberObject[bers.size()];

                    for (int k = 0; k < bers.size(); k++) {
                        MemberObject mob = new MemberObject();
                        mob.newMember(bers.get(k).getName(), bers.get(k).getCaption(), "NONE", null);
                        memberObj[k] = mob;

                    }
                    LevelObject lob = new LevelObject();
                    lob.newLevel(levels.get(j).getName(), levels.get(j).getCaption(), memberObj);
                    levelObj[j] = lob;
                }
                DimensionObject dob = new DimensionObject();
                dob.newDimension(dims[i], levelObj);
                dimObj[i] = dob;
            }

            AxisObject aop = new AxisObject();

            aop.newAxis("UNUSED", true, dimObj);
            axis[0] = aop;
            qob.setAxis(axis);

            return qob;
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

    /**
     * 
     * This method allows you to delete a query, as per the idea of Tiemonster.<br>
     * HTTP DELETE<br>
     * 
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws ServletException
     */
    @DELETE
    @Path("/{schema}/{cube}/{queryname}")
    @Consumes( {"application/xml", "application/json"})
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized void deleteQuery(@PathParam("queryname") String queryname,
            @FormParam("sessionId") String sessionId,
            @QueryParam("callback") @DefaultValue("jsoncallback") String jsoncallback) throws RpcException,
            ServletException {
        ss.init();
        qs.init();
        ds.init();

        List<QuerySaveModel> queries = this.qs.getActiveQueries(sessionId);
        String id = null;
        for (int q = 0; q < queries.size(); q++) {

            if (queries.get(q).getName().equals(queryname)) {
                id = queries.get(q).getQueryId();
                queries.remove(q);
                break;
            }
        }
        if (id != null) {
            this.qs.deleteQuery(sessionId, id);
        }
    }

    /**
     * 
     * This method allows you to overwrite a query object in one request, as per the idea of Tiemonster.<br>
     * HTTP PUT.<br>
     * 
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws ServletException
     */
    @PUT
    @Path("/{schema}/{cube}/{queryname}")
    @Consumes( {"application/xml", "application/json"})
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized void saveQuery(QueryObject qob, @PathParam("queryId") String queryId, @FormParam("sessionId") String sessionId,
            @FormParam("queryName") String queryName, @FormParam("connectionId") String connectionId,
            @FormParam("cubeCatalog") String catalog, @FormParam("cubeName") String cubeName,
            @FormParam("schema") String schema) throws RpcException,
            ServletException {
        ss.init();
        qs.init();
        ds.init();
        
        updateQuery(sessionId, qob);
        
        CubeItem ci = new CubeItem(cubeName, catalog, schema);

        this.qs.saveQuery(sessionId, queryId, queryName, connectionId, ci, cubeName);
    }

    /**
     * 
     * This method allows you to get a query object in one request, as per the idea of Tiemonster.<br>
     * HTTP GET.<br>
     * curl --basic -u "admin:admin"
     * "http://localhost:8080/rest/service/query?sessionId=idstring&connectionId=connectionstring&cubeName=cubestring"
     * 
     * @param sessionId
     * @param connectionId
     * @param cubeName
     * @param jsoncallback
     * @return
     * @throws RpcException
     * @throws RpcException
     * @throws ServletException
     * @throws ServletException
     */
    @GET
    @Path("/{schema}/{cube}/{queryname}")
    @Produces( {"application/xml", "application/json"})
    // it is to set the response type
    @Resource
    // to make it spring set the response type
    public synchronized QueryObject loadQuery(@QueryParam("queryname") String queryId,
            @QueryParam("sessionId") String sessionId) throws RpcException, ServletException {

        try {
            qs.init();
            ds.init();

            String[] queries = this.qs.getQueries(sessionId);

            if (Arrays.asList(queries).contains(queryId)) {
                QueryObject qob = new QueryObject();
                qob.setQueryId(queryId);

                AxisObject[] axis = new AxisObject[4];

                for (int z = 0; z < 4; z++) {
                    String axislabel = null;
                    switch (z) {
                    case 0:
                        axislabel = "UNUSED";
                        break;
                    case 1:
                        axislabel = "ROWS";
                        break;
                    case 2:
                        axislabel = "COLUMNS";
                        break;
                    case 3:
                        axislabel = "FILTER";
                        break;
                    }
                    String[] dims = ds.getDimensions(sessionId, qob.getQueryId(), IAxis.Standard.valueOf(axislabel));
                    DimensionObject[] dimObj = new DimensionObject[dims.length];
                    for (int i = 0; i < dims.length; i++) {

                        List<Selection> inclusions = ds.getInclusions(sessionId, queryId, dims[i]);
                        List<String> inclusionmembers = new ArrayList<String>();
                        for(int q=0; q< inclusions.size(); q++){

                            inclusionmembers.add(inclusions.get(q).getMember().getUniqueName());   
                        }
                        List<MemberLabelItem> levels = ds.getLevels(sessionId, qob.getQueryId(), dims[i]);
                        LevelObject[] levelObj = new LevelObject[levels.size()];

                        for (int j = 0; j < levels.size(); j++) {

                            List<MemberLabelItem> bers = ds.getLevelMembers(sessionId, qob.getQueryId(), levels.get(j)
                                    .getName());

                            MemberObject[] memberObj = new MemberObject[bers.size()];

                            
                            for (int k = 0; k < bers.size(); k++) {
                                MemberObject mob = new MemberObject();
                                MemberLabelItem mber = bers.get(k);
                                if(inclusionmembers.contains(mber.getName())){
                                    int ind = inclusionmembers.indexOf(mber.getName());
                                    mob.newMember(mber.getName(), mber.getCaption(), "INCLUSION", inclusions.get(ind).getOperator().toString());
                                }
                                else{
                                mob.newMember(bers.get(k).getName(), bers.get(k).getCaption(), "NONE", "MEMBER");
                                }
                                memberObj[k] = mob;

                            }
                            LevelObject lob = new LevelObject();
                            lob.newLevel(levels.get(j).getName(), levels.get(j).getCaption(), memberObj);
                            levelObj[j] = lob;
                        }
                        DimensionObject dob = new DimensionObject();
                        dob.newDimension(dims[i], levelObj);
                        dimObj[i] = dob;
                    }

                    AxisObject aop = new AxisObject();

                    aop.newAxis(axislabel, true, dimObj);
                    axis[z] = aop;
                    qob.setAxis(axis);
                }

                return qob;
            }
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        return null;

    }

    /**
     * curl --basic -u "admin:admin" -XPUT -H 'Content-Type: application/json' -d ''
     * http://localhost:8080/rest/admin/SteelWheels/SteelWheelsSales/tomsquery /run?sessionId=10 -v
     */
    @PUT
    // to be accessed using http get method
    @Path("/{schema}/{cube}/{queryname}/run")
    @Consumes( {"application/xml", "application/json"})
    @Produces( {"application/xml", "application/json"})
    public ResultSetObject run(QueryObject qob, @QueryParam("sessionid") String sessionId,
            @PathParam("cube") String cube, @PathParam("user") String user, @PathParam("schema") String schema,
            @PathParam("queryname") String queryname) throws RpcException, ServletException {

        updateQuery(sessionId, qob);

        ResultSetObject rso = new ResultSetObject(qs.executeQuery(sessionId, qob.getQueryId()));
        return rso;

    }

    private void updateQuery(String sessionId, QueryObject qob) throws RpcException, ServletException{
        qs.init();

        for (AxisObject obj : qob.getAxes()) {
            String loc = obj.getLocation();

            DimensionObject[] dimlist = obj.getDims();

            for (DimensionObject dim : dimlist) {
                qs.moveDimension(sessionId, qob.getQueryId(), IAxis.Standard.valueOf(loc), dim.getName());

                // Set memebers selection
                LevelObject[] levellist = dim.getLevels();
                if (levellist != null) {
                    for (LevelObject lob : levellist) {
                        if(lob.getMob()==null){
                            qs.createSelection(sessionId, qob.getQueryId(), lob.getName(), ObjectType.LEVEL,
                                    SelectionType.MEMBER);
                        }
                        else{
                            for (MemberObject mob : lob.getMob()) {
                        

                            if (mob.getType() != null) {
                                if (mob.getStatus().toString().equals("INCLUSION")) {
                                    qs.createSelection(sessionId, qob.getQueryId(), mob.getName(), ObjectType.MEMBER,
                                            mob.getType());
                                }
                            }
                            }
                        }
                    }
                }
            }
        }
    }
}
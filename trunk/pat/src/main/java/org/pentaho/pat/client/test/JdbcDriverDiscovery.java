/**
 * 
 */
package org.pentaho.pat.client.test;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.pat.client.test.NameValue;
import org.pentaho.pat.client.test.ResolverUtil;






/**
 * @author paul
 *
 */
public class JdbcDriverDiscovery {
	
	 
	 
	  public static List<NameValue> getDrivers(String location,boolean relative) {
		    //CacheInfo ci = cache.get(location);
		  List<NameValue> drivers;
		  drivers = new ArrayList<NameValue>();
		    try {
		     /* long timeMil = System.currentTimeMillis();
		          
		      if (ci!=null){
		        long inte =  timeMil - ci.lastCall;
		        if (inte < interval) {
		          return ci.cachedObj;
		        }
		      }
		    */
		    	
		      if (location != null && location.length() > 0) {
		        //File parent = relative?new File(getServletContext().getRealPath(location)):new File(location);
		    	File parent = new File(location);
		        FilenameFilter libs = new FilenameFilter() {

		          public boolean accept(File dir, String pathname) {
		            return pathname.endsWith("jar") || pathname.endsWith("zip"); //$NON-NLS-1$//$NON-NLS-2$
		          }

		        };

		        ResolverUtil<Driver> resolver = new ResolverUtil<Driver>();

		        if (parent.exists()) {
		          for (File lib : parent.listFiles(libs)) {
		            try {
		              resolver.loadImplementationsInJar("", lib.toURI().toURL(), //$NON-NLS-1$
		                  new ResolverUtil.IsA(Driver.class));
		            } catch (MalformedURLException e) {
		              e.printStackTrace();
		              continue;
		            }
		          }

		          drivers = new ArrayList<NameValue>();
		          for (Class<? extends Driver> cd : resolver.getClasses())
		            drivers.add(new NameValue(cd.getName(), cd.getName()));

		       /*   if(drivers.size() > 0) {
		            if(ci == null) {
		              ci = new CacheInfo();
		              ci.lastCall = timeMil;
		              cache.put(location, ci);
		            }
		            ci.cachedObj = drivers.toArray(new NameValue[drivers.size()]);
		             
		          } else {
		            throw new Exception("JdbcDriverDiscoveryService.ERROR_0002_NO_DRIVERS_FOUND_IN_JDBC_DRIVER_PATH"); //$NON-NLS-1$
		          } */
		          
		        } else {
		          throw new Exception("JdbcDriverDiscoveryService.ERROR_0002_NO_DRIVERS_FOUND_IN_JDBC_DRIVER_PATH"); //$NON-NLS-1$ 
		        }
		      } else {
		        throw new Exception("JdbcDriverDiscoveryService.ERROR_0001_NO_JDBC_DRIVER_PATH_SPECIFIED"); //$NON-NLS-1$
		      }
		    } catch (Exception e) {
		      
		    }
		    return drivers;
		  }


}

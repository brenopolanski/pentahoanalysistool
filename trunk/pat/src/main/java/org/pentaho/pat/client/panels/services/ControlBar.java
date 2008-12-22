package org.pentaho.pat.client.panels.services;

import org.pentaho.pat.client.util.GuidFactory;
import org.pentaho.pat.client.util.ServiceFactory;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;

public class ControlBar {
	
	private static Store store;
	private static MemoryProxy proxy;
	private static RecordDef recordDef;
	private static ArrayReader reader;
	public static final String AXIS_NONE = "none"; //$NON-NLS-1$
	public static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	public static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	public static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	public static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	public static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	public static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	public static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	
	public ControlBar() {
	
	}


	public void getCubes(final String boxText) {
		ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(),
				new AsyncCallback() {
					public void onSuccess(Object result1) {
						if (result1 != null) {
							store.removeAll();
							store.commitChanges();
							
							String[][] cubeNames = (String[][]) result1;
							for (int i = 0; i < cubeNames.length; i++) {
								store.add(recordDef.createRecord(cubeNames[i]));
							}
							store.commitChanges();
						}
						setCube(boxText);
										}

					public void onFailure(Throwable caught) {
					}
				});

	}

	
	public static String[][] getCubeData() {
		String[][] tom = { new String[] { "naught", "No Cubes" }};
		return tom;
	}

	public static void setCube(String boxText){
	
		ServiceFactory.getInstance().setCube(
				boxText,
				GuidFactory.getGuid(), new AsyncCallback() {
					public void onSuccess(Object result) {
						//TODO Populate the Dimensions Dialog
					}

					public void onFailure(Throwable caught) {
					}
				});

	}
	public static Store populateCubeList(){
		proxy = new MemoryProxy(getCubeData());
		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("number"),
				new StringFieldDef("name") });
		reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();
		return store;
	}
	
	
}

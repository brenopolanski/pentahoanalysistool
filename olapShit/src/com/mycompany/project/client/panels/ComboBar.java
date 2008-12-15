package com.mycompany.project.client.panels;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtext.client.data.ArrayReader;
import com.gwtext.client.data.FieldDef;
import com.gwtext.client.data.MemoryProxy;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.RecordDef;
import com.gwtext.client.data.Store;
import com.gwtext.client.data.StringFieldDef;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.menu.Menu;
import com.mycompany.project.client.listeners.ConnectionListener;
import com.mycompany.project.client.util.GuidFactory;
import com.mycompany.project.client.util.ServiceFactory;

public class ComboBar extends Toolbar implements ConnectionListener {
	public static final String AXIS_NONE = "none"; //$NON-NLS-1$
	public static final String AXIS_UNUSED = "UNUSED"; //$NON-NLS-1$
	public static final String AXIS_FILTER = "FILTER"; //$NON-NLS-1$
	public static final String AXIS_COLUMNS = "COLUMNS"; //$NON-NLS-1$
	public static final String AXIS_ROWS = "ROWS"; //$NON-NLS-1$
	public static final String AXIS_PAGES = "PAGES"; //$NON-NLS-1$
	public static final String AXIS_CHAPTERS = "CHAPTERS"; //$NON-NLS-1$
	public static final String AXIS_SECTIONS = "SECTIONS"; //$NON-NLS-1$
	static ComboBox cubeListBox;
	Store store;
	MemoryProxy proxy;
	RecordDef recordDef;

	public ComboBar() {
		super();

		init();
	}

	private void init() {
		Menu menu = new Menu();
		menu.setShadow(true);
		menu.setMinWidth(10);

		proxy = new MemoryProxy(getProxyData());
		recordDef = new RecordDef(new FieldDef[] { new StringFieldDef("noo"),
				new StringFieldDef("nam") });

		ArrayReader reader = new ArrayReader(recordDef);
		store = new Store(proxy, reader);
		store.load();
		cubeListBox = new ComboBox();
		cubeListBox.setFieldLabel("Cube");
		cubeListBox.setStore(store);
		cubeListBox.setDisplayField("nam");
		cubeListBox.setMode(ComboBox.LOCAL);
		cubeListBox.addListener(new ComboBoxListenerAdapter() {

			public void onSelect(ComboBox comboBox, Record record, int index) {
				System.out.println(comboBox.getValueAsString());
				ServiceFactory.getInstance().setCube(
						(String) comboBox.getValueAsString(),
						GuidFactory.getGuid(), new AsyncCallback() {
							public void onSuccess(Object result) {
								populateDimensions();
							}

							public void onFailure(Throwable caught) {
							}
						});
			}

		});

		this.addField(cubeListBox);
	}

	/**
	 * @return the cubeListBox
	 */
	public static String getCubeListBox() {
		return cubeListBox.getText();
	}

	public void getCubes() {
		ServiceFactory.getInstance().getCubes(GuidFactory.getGuid(),
				new AsyncCallback() {
					public void onSuccess(Object result1) {
						if (result1 != null) {
							store.removeAll();
							store.commitChanges();
							System.out.println(store.getCount());
							String[][] cubeNames = (String[][]) result1;
							for (int i = 0; i < cubeNames.length; i++) {
								store.add(recordDef.createRecord(cubeNames[i]));
							}
							store.commitChanges();
						}
						ServiceFactory.getInstance().setCube(
								ComboBar.getCubeListBox(),
								GuidFactory.getGuid(), new AsyncCallback() {
									public void onSuccess(Object result2) {
										populateDimensions();
									}

									public void onFailure(Throwable caught) {
									}
								});
					}

					public void onFailure(Throwable caught) {
					}
				});

	}

	public static void populateDimensions() {
		List axis = new ArrayList();
		axis.add(AXIS_NONE);
		axis.add(AXIS_ROWS);
		axis.add(AXIS_COLUMNS);
		axis.add(AXIS_FILTER);
		populateDimensions(axis);
	}

	public static void populateDimensions(List axis) {
		if (axis.contains(AXIS_NONE)) {
			ServiceFactory.getInstance().getDimensions(AXIS_NONE,
					GuidFactory.getGuid(), new AsyncCallback() {
						public void onSuccess(Object result) {

							String[] dimStrs = (String[]) result;

							DimPanel.tree = DimPanel.getDimTree(dimStrs);

						}

						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

					});
		}
	}

	public void onConnectionBroken(Widget sender) {
		// TODO Auto-generated method stub

	}

	public void onConnectionMade(Widget sender) {
		// TODO Auto-generated method stub
		getCubes();

	}

	private String[][] getProxyData() {
		String[][] tom = { new String[] { "naught", "No Cubes" }};
		return tom;
	}
}

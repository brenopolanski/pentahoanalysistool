/**
 * 
 */
package org.pentaho.pat.client.util;

/**
 * @author Paul Stoellberger
 * 
 */
public class State {

	public static enum Mode {
		BISERVERPUC("BISERVERPUC", false,false,false,false,false,false,false),  //$NON-NLS-1$
		STANDALONE("STANDALONE", true,true,true,false,true,true,true); //$NON-NLS-1$

		private String param;
		
		private boolean showConnections;
		private boolean manageConnections;
		private boolean showCubeMenu;
		private boolean showDimensionMenu;
		private boolean showOnlyTable;
		private boolean showMenu;
		private boolean showWelcomePanel;
		
		private Mode(final String param, final boolean showConnections, final boolean manageConnections, final boolean showCubeMenu, final boolean showOnlyTable, final boolean showMenu, final boolean showWelcomePanel, final boolean showDimensionMenu) {
			this.param = param;
			this.showConnections = showConnections;
			this.manageConnections = manageConnections;
			this.showCubeMenu = showCubeMenu;
			this.showOnlyTable = showOnlyTable;
			this.showMenu = showMenu;
			this.showWelcomePanel = showWelcomePanel;
			this.showDimensionMenu = showDimensionMenu;
		}
		
		public boolean isShowDimensionMenu() {
			return showDimensionMenu;
		}
		public boolean isShowWelcomePanel() {
			return showWelcomePanel;
		}
		public boolean isShowMenu() {
			return showMenu;
		}
		
		public boolean isShowOnlyTable() {
			return showOnlyTable;
		}
		public boolean isShowCubeMenu() {
			return showCubeMenu;
		}
		public boolean isManageConnections() {
			return manageConnections;
		}
		
		public boolean isShowConnections() {
			return showConnections;
		}

	

		public String getParam() {
			return param;
		}

		public static Mode getModeByParameter(final String param) {
			for (Mode s : values()) {
				if (s.getParam().equals(param)) {
					return s;
				}
			}
			return null;
		}
	}

	private Mode mode = null;

	private String sessionId = null;
	
	private boolean connected = false;

	public State() {

	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public String getSession() {
		return sessionId;
	}

	public void setSession(String session) {
		sessionId = session;
	}

	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
}

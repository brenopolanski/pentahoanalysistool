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
		BISERVERPUC("BISERVERPUC", false), 
		STANDALONE("STANDALONE", true);

		private String param;
		
		private boolean showToolbar;
		
		private Mode(final String param, final boolean showToolbar) {
			this.param = param;
			this.showToolbar = showToolbar;
		}
		
		public boolean isShowToolbar() {
			return showToolbar;
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

/*
 * Copyright (C) 2009 Paul Stoellberger
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

package org.pentaho.pat.client.util;

/**
 * @created Jul 10, 2009
 * @since 0.4.0
 * @author Paul Stoellberger
 * 
 */
public class State {

	/**
	 * 
	 * @author Paul Stoellberger
	 *
	 */
    public static enum Mode {
        BISERVERPUC("BISERVERPUC", false, false, false, false, false, false, false, false, false), //$NON-NLS-1$
        DEFAULT("DEFAULT", false, true, true, false, true, true, true, true, true), //$NON-NLS-1$
        STANDALONE("STANDALONE", true, true, true, false, true, true, true, true, true), //$NON-NLS-1$
        USER("USER", true, false, true, false, true, true, true, true, true), //$NON-NLS-1$
        BUSINESSUSER("BUSINESSUSER", false, false, true, false, true, true, true, false, true), //$NON-NLS-1$
        ONECUBE("ONECUBE", false, false, false, false, true, false, true, false, true), //$NON-NLS-1$
        OLAPTABLE("OLAPTABLE", false, false, false, true, false, false, false, false, false); //$NON-NLS-1$
        public static Mode getModeByParameter(final String param) {
            for (final Mode s : values()) {
                if (s.getParam().equals(param)) {
                    return s;
                }

            }
            return null;
        }

        private String param;

        private boolean showConnections;

        private boolean manageConnections;

        private boolean showCubeMenu;

        private boolean showDimensionMenu;

        private boolean showOnlyTable;

        private boolean showMenu;

        private boolean showWelcomePanel;

        private boolean allowMdxQuery;

        private boolean allowQmQuery;

        /**
         * 
         * @param param
         * @param showConnections
         * @param manageConnections
         * @param showCubeMenu
         * @param showOnlyTable
         * @param showMenu
         * @param showWelcomePanel
         * @param showDimensionMenu
         * @param allowMdxQuery
         * @param allowQmQuery
         */
        private Mode(final String param, final boolean showConnections, final boolean manageConnections,
                final boolean showCubeMenu, final boolean showOnlyTable, final boolean showMenu,
                final boolean showWelcomePanel, final boolean showDimensionMenu, final boolean allowMdxQuery,
                final boolean allowQmQuery) {
            this.param = param;
            this.showConnections = showConnections;
            this.manageConnections = manageConnections;
            this.showCubeMenu = showCubeMenu;
            this.showOnlyTable = showOnlyTable;
            this.showMenu = showMenu;
            this.showWelcomePanel = showWelcomePanel;
            this.showDimensionMenu = showDimensionMenu;
            this.allowMdxQuery = allowMdxQuery;
            this.allowQmQuery = allowQmQuery;
        }
        
        /**
         * 
         * @return
         */
        public String getParam() {
            return param;
        }
        
        /**
         * 
         * @return
         */
        public boolean isAllowMdxQuery() {
            return allowMdxQuery;
        }

        /**
         * 
         * @return
         */
        public boolean isAllowQmQuery() {
            return allowQmQuery;
        }

        /**
         * 
         * @return
         */
        public boolean isManageConnections() {
            return manageConnections;
        }

        /**
         * 
         * @return
         */
        public boolean isShowConnections() {
            return showConnections;
        }

        /**
         * 
         * @return
         */
        public boolean isShowCubeMenu() {
            return showCubeMenu;
        }

        /**
         * 
         * @return
         */
        public boolean isShowDimensionMenu() {
            return showDimensionMenu;
        }

        /**
         * 
         * @return
         */
        public boolean isShowMenu() {
            return showMenu;
        }

        /**
         * 
         * @return
         */
        public boolean isShowOnlyTable() {
            return showOnlyTable;
        }

        /**
         * 
         * @return
         */
        public boolean isShowWelcomePanel() {
            return showWelcomePanel;
        }
    }

    private Mode mode = null;

    private String sessionId = null;

    private boolean connected = false;

    /**
     * 
     * @return
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * 
     * @return
     */
    public String getSession() {
        return sessionId;
    }

    /**
     * 
     * @return
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * 
     * @param connected
     */
    public void setConnected(final boolean connected) {
        this.connected = connected;
    }

    /**
     * 
     * @param mode
     */
    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    /**
     * 
     * @param session
     */
    public void setSession(final String session) {
        sessionId = session;
    }
}

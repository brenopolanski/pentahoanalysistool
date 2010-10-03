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
package org.pentaho.pat.rpc.dto.query;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface IAxis extends Serializable {

    Standard FILTER = Standard.FILTER;

    Standard COLUMNS = Standard.COLUMNS;

    Standard ROWS = Standard.ROWS;

    Standard UNUSED = Standard.UNUSED;

    String name();

    boolean isFilter();

    int axisOrdinal();

    String getCaption();

    public enum Standard implements IAxis, IsSerializable, Serializable {
        FILTER, COLUMNS, ROWS, UNUSED;

        public int axisOrdinal() {
            return ordinal() - 1;
        }

        public boolean isFilter() {
            return this == FILTER;
        }

        public String getCaption() {
            return name();
        }
    }

    class Factory {
        private static final Standard[] STANDARD_VALUES = Standard.values();

        public static IAxis forOrdinal(final int ordinal) {
            if (ordinal < -1) {
                throw new IllegalArgumentException("Axis ordinal must be -1 or higher"); //$NON-NLS-1$
            }
            if (ordinal + 1 < STANDARD_VALUES.length) {
                return STANDARD_VALUES[ordinal + 1];
            }
            return new IAxis() {
                private static final long serialVersionUID = 1L;

                public String toString() {
                    return name();
                }

                public String name() {
                    return "AXIS(" + ordinal + ")"; //$NON-NLS-1$//$NON-NLS-2$
                }

                public boolean isFilter() {
                    return false;
                }

                public int axisOrdinal() {
                    return ordinal;
                }

                public String getCaption() {
                    return name();
                }
            };
        }
    }
}

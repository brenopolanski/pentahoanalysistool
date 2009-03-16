package org.pentaho.pat.rpc.beans;

import java.io.Serializable;

public interface Axis extends Serializable {

    Standard FILTER = Standard.FILTER;
    Standard COLUMNS = Standard.COLUMNS;
    Standard ROWS = Standard.ROWS;
    Standard UNUSED = Standard.UNUSED;

    String name();
    boolean isFilter();
    int axisOrdinal();
    String getCaption();

    public enum Standard implements Axis {
        FILTER,
        COLUMNS,
        ROWS,
        UNUSED;

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

        public static Axis forOrdinal(final int ordinal) {
            if (ordinal < -1) {
                throw new IllegalArgumentException(
                    "Axis ordinal must be -1 or higher");
            }
            if (ordinal + 1 < STANDARD_VALUES.length) {
                return STANDARD_VALUES[ordinal + 1];
            }
            return new Axis() 
            {
				private static final long serialVersionUID = 1L;

				public String toString() {
                    return name();
                }

                public String name() {
                    return "AXIS(" + ordinal + ")";
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

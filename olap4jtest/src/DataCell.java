
import java.io.Serializable;

/**
 * The Class CellInfo.
 * 
 * @author wseyler
 */
public class DataCell extends AbstractBaseCell implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The color value. */
    private String colorValue = null; // Color held as hex String

    private Number rawNumber = null;

    private MemberCell parentColMember = null;

    public MemberCell getParentColMember() {
        return parentColMember;
    }

    public void setParentColMember(final MemberCell parentColMember) {
        this.parentColMember = parentColMember;
    }

    public MemberCell getParentRowMember() {
        return parentRowMember;
    }

    public void setParentRowMember(final MemberCell parentRowMember) {
        this.parentRowMember = parentRowMember;
    }

    private MemberCell parentRowMember = null;

    public Number getRawNumber() {
        return rawNumber;
    }

    public void setRawNumber(final Number rawNumber) {
        this.rawNumber = rawNumber;
    }

    /**
     * 
     * Blank constructor for serialization purposes, don't use it.
     * 
     */
    public DataCell() {
        super();
    }

    /**
     * 
     * Construct a Data Cell containing olap data.
     * 
     * @param b
     * @param c
     */
    public DataCell(final boolean right, final boolean sameAsPrev) {
        super();
        this.right = right;
        this.sameAsPrev = sameAsPrev;
    }

    /**
     * Gets the color value.
     * 
     * @return the color value
     */
    public String getColorValue() {
        return colorValue;
    }

    /**
     * Sets the color value.
     * 
     * @param colorValue
     *            the new color value
     */
    public void setColorValue(final String colorValue) {
        this.colorValue = colorValue;
    }

}

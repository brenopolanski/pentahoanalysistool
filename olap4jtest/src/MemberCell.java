
import java.io.Serializable;
import java.util.List;



public class MemberCell extends AbstractBaseCell {
    private static final long serialVersionUID = 1L;

    private boolean lastRow = false;

    private boolean expanded = false;

    private String parentDimension = null;

    private String parentMember = null;

    private MemberCell rightOf = null;

    private String uniqueName;

    private int childMemberCount;

    private String rightOfDimension;

    private List<String> memberPath;

    private final static String CELLBUTTON = "cellButton"; //$NON-NLS-1$

    /**
     * 
     * Blank Constructor for Serializable niceness, don't use it.
     * 
     */
    public MemberCell() {
        super();
    }

    /**
     * 
     * Creates a member cell.
     * 
     * @param b
     * @param c
     */
    public MemberCell(final boolean right, final boolean sameAsPrev) {
        super();
        this.right = right;
        this.sameAsPrev = sameAsPrev;
    }

    /**
     * Returns true if this is the bottom row of the column headers(supposedly).
     * 
     * @return the lastRow
     */
    public boolean isLastRow() {
        return lastRow;
    }

    /**
     * 
     * Set true if this is the bottom row of the column headers.
     * 
     * @param lastRow
     *            the lastRow to set
     */
    public void setLastRow(final boolean lastRow) {
        this.lastRow = lastRow;
    }

    public void setParentDimension(final String parDim) {
        parentDimension = parDim;
    }

    public String getParentDimension() {
        return parentDimension;
    }

    /**
     *TODO JAVADOC
     * 
     * @return the expanded
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * 
     *TODO JAVADOC
     * 
     * @param expanded
     *            the expanded to set
     */
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }

    /**
     *TODO JAVADOC
     * 
     * @param parentMember
     */
    public void setParentMember(final String parentMember) {

        this.parentMember = parentMember;

    }

    public String getParentMember() {
        return parentMember;
    }

    /**
     *TODO JAVADOC
     * 
     * @param uniqueName
     */
    public void setUniquename(final String uniqueName) {

        this.uniqueName = uniqueName;

    }

    public String getUniqueName() {
        return uniqueName;
    }

    /**
     *TODO JAVADOC
     * 
     * @param childMemberCount
     */
    public void setChildMemberCount(final int childMemberCount) {
        this.childMemberCount = childMemberCount;
    }

    public int getChildMemberCount() {
        return childMemberCount;
    }

    /**
     *TODO JAVADOC
     * 
     * @param memberCell
     */
    public void setRightOf(final MemberCell memberCell) {
        this.rightOf = memberCell;

    }

    public MemberCell getRightOf() {
        return rightOf;
    }

    /**
     *TODO JAVADOC
     * 
     * @param name
     */
    public void setRightOfDimension(String name) {

        this.rightOfDimension = name;

    }

    public String getRightOfDimension() {
        return this.rightOfDimension;
    }

    public void setMemberPath(List<String> memberPath) {
        this.memberPath = memberPath;

    }

    public List<String> getMemberPath() {
        return memberPath;
    }

}

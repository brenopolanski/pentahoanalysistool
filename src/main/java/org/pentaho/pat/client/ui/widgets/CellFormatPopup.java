package org.pentaho.pat.client.ui.widgets;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class CellFormatPopup extends PopupPanel {

	public class CellFormatCommand implements Command {
		private transient String value = ""; //$NON-NLS-1$
		private final transient CellFormatType type;

		public CellFormatCommand(final CellFormatType type, final String value) {
			this.value = value;
			this.type = type;

		}

		public void execute() {
			// Widget widget = getSource();

			if (type == CellFormatType.FOREGROUND)
			{
				DOM.setStyleAttribute(sender.getElement(), "color", value); //$NON-NLS-1$
			}
			else if (type == CellFormatType.BACKGROUND)
			{
				DOM.setStyleAttribute(sender.getElement(), "background", value); //$NON-NLS-1$
			}
			else if (type == CellFormatType.DECORATION)
			{
				DOM.setStyleAttribute(sender.getElement(), "textDecoration", value); //$NON-NLS-1$
			}
			else if (type == CellFormatType.STYLE)
			{
				DOM.setStyleAttribute(sender.getElement(), "fontWeight", value); //$NON-NLS-1$
			}
			else if (type == CellFormatType.SIZE)
			{
				DOM.setStyleAttribute(sender.getElement(), "fontSize", value); //$NON-NLS-1$
			}
			else if (type == CellFormatType.FAMILY)
			{
				DOM.setStyleAttribute(sender.getElement(), "fontFamily", value); //$NON-NLS-1$
			}

			CellFormatPopup.this.hide();
		}

	}
	public static final String COLOR_RED = "red"; //$NON-NLS-1$
	public static final String COLOR_GREEN = "green"; //$NON-NLS-1$
	public static final String COLOR_YELLOW = "yellow"; //$NON-NLS-1$
	public static final String COLOR_BLUE = "blue"; //$NON-NLS-1$
	public static final String COLOR_WHITE = "white"; //$NON-NLS-1$

	public static final String COLOR_BLACK = "black"; //$NON-NLS-1$
	public static final String STYLE_BOLD = "bold"; //$NON-NLS-1$

	public static final String STYLE_NORMAL = "normal"; //$NON-NLS-1$
	public static final String DECORATION_OUTLINE = "outline"; //$NON-NLS-1$


	public static final String DECORATION_UNDERLINE = "underline"; //$NON-NLS-1$
	private transient Widget sender;
	private transient MenuBar menuBar;

	private transient String returnStyle = ""; //$NON-NLS-1$

	public CellFormatPopup(final int x, final int y, final Widget sender) {
		super(false, true);
		this.sender = sender;

		this.setPopupPosition(y, x);
		init();
	}

	public String getReturnStyle() {
		return returnStyle;
	}

	public Widget getSource() {
		return sender;
	}

	public void init() {
		menuBar = new MenuBar(true);
		menuBar.setAutoOpen(true);

		// com.google.gwt.user.client.Element rootNode =
		// DOM.getElementById("rootNode");
		// com.google.gwt.user.client.Element caption =
		// DOM.createElement("<P>");
		// com.google.gwt.user.client.DOM.setInnerText(caption, "MERDA!");
		// com.google.gwt.user.client.DOM.appendChild(rootNode, caption);

		final MenuBar bgMenu = new MenuBar(true);
		bgMenu.addItem(new MenuItem("Red", new CellFormatCommand(CellFormatType.BACKGROUND, COLOR_RED))); //$NON-NLS-1$
		bgMenu.addItem(new MenuItem("Green", new CellFormatCommand(CellFormatType.BACKGROUND, COLOR_GREEN))); //$NON-NLS-1$
		bgMenu.addItem(new MenuItem("Yellow", new CellFormatCommand(CellFormatType.BACKGROUND, COLOR_YELLOW))); //$NON-NLS-1$
		bgMenu.addItem(new MenuItem("Blue", new CellFormatCommand(CellFormatType.BACKGROUND, COLOR_BLUE))); //$NON-NLS-1$
		bgMenu.addItem(new MenuItem("White", new CellFormatCommand(CellFormatType.BACKGROUND, COLOR_WHITE))); //$NON-NLS-1$
		bgMenu.addItem(new MenuItem("Black", new CellFormatCommand(CellFormatType.BACKGROUND, COLOR_BLACK))); //$NON-NLS-1$

		final MenuBar fgMenu = new MenuBar(true);
		fgMenu.addItem(new MenuItem("Red", new CellFormatCommand(CellFormatType.FOREGROUND, COLOR_RED))); //$NON-NLS-1$
		fgMenu.addItem(new MenuItem("Green", new CellFormatCommand(CellFormatType.FOREGROUND, COLOR_GREEN))); //$NON-NLS-1$
		fgMenu.addItem(new MenuItem("Yellow", new CellFormatCommand(CellFormatType.FOREGROUND, COLOR_YELLOW))); //$NON-NLS-1$
		fgMenu.addItem(new MenuItem("Blue", new CellFormatCommand(CellFormatType.FOREGROUND, COLOR_BLUE))); //$NON-NLS-1$
		fgMenu.addItem(new MenuItem("White", new CellFormatCommand(CellFormatType.FOREGROUND, COLOR_WHITE))); //$NON-NLS-1$
		fgMenu.addItem(new MenuItem("Black", new CellFormatCommand(CellFormatType.FOREGROUND, COLOR_BLACK))); //$NON-NLS-1$

		final MenuBar styleMenu = new MenuBar(true);
		styleMenu.addItem(new MenuItem("Bold", new CellFormatCommand(CellFormatType.STYLE, STYLE_BOLD))); //$NON-NLS-1$
		styleMenu.addItem(new MenuItem("Normal", new CellFormatCommand(CellFormatType.STYLE, STYLE_NORMAL))); //$NON-NLS-1$
		styleMenu.addItem(new MenuItem("Underline", new CellFormatCommand(CellFormatType.DECORATION, DECORATION_UNDERLINE))); //$NON-NLS-1$
		styleMenu.addItem(new MenuItem("Outline", new CellFormatCommand(CellFormatType.DECORATION, DECORATION_OUTLINE))); //$NON-NLS-1$

		final MenuBar sizeMenu = new MenuBar(true);
		sizeMenu.addItem(new MenuItem("70%", new CellFormatCommand(CellFormatType.SIZE, "70%"))); //$NON-NLS-1$ //$NON-NLS-2$
		sizeMenu.addItem(new MenuItem("80%", new CellFormatCommand(CellFormatType.SIZE, "80%"))); //$NON-NLS-1$ //$NON-NLS-2$
		sizeMenu.addItem(new MenuItem("90%", new CellFormatCommand(CellFormatType.SIZE, "90%"))); //$NON-NLS-1$ //$NON-NLS-2$
		sizeMenu.addItem(new MenuItem("100%", new CellFormatCommand(CellFormatType.SIZE, "100%"))); //$NON-NLS-1$ //$NON-NLS-2$
		sizeMenu.addItem(new MenuItem("110%", new CellFormatCommand(CellFormatType.SIZE, "110%"))); //$NON-NLS-1$ //$NON-NLS-2$
		sizeMenu.addItem(new MenuItem("120%", new CellFormatCommand(CellFormatType.SIZE, "120%"))); //$NON-NLS-1$ //$NON-NLS-2$
		sizeMenu.addItem(new MenuItem("130%", new CellFormatCommand(CellFormatType.SIZE, "130%"))); //$NON-NLS-1$ //$NON-NLS-2$

		final MenuBar familyMenu = new MenuBar(true);
		familyMenu.addItem(new MenuItem("Monospace", new CellFormatCommand(CellFormatType.FAMILY, "monospace"))); //$NON-NLS-1$ //$NON-NLS-2$
		familyMenu.addItem(new MenuItem("Arial", new CellFormatCommand(CellFormatType.FAMILY, "arial"))); //$NON-NLS-1$ //$NON-NLS-2$
		familyMenu.addItem(new MenuItem("Sans-serif", new CellFormatCommand(CellFormatType.FAMILY, "sans-serif"))); //$NON-NLS-1$ //$NON-NLS-2$
		familyMenu.addItem(new MenuItem("Courier", new CellFormatCommand(CellFormatType.FAMILY, "courier"))); //$NON-NLS-1$ //$NON-NLS-2$

		menuBar.addItem(new MenuItem("Size", sizeMenu)); //$NON-NLS-1$
		menuBar.addItem(new MenuItem("Foreground", fgMenu)); //$NON-NLS-1$
		menuBar.addItem(new MenuItem("Background", bgMenu)); //$NON-NLS-1$
		menuBar.addItem(new MenuItem("Font style", styleMenu)); //$NON-NLS-1$
		menuBar.addItem(new MenuItem("Family", familyMenu)); //$NON-NLS-1$

		this.setWidget(menuBar);

	}

	public void setReturnStyle(final String returnStyle) {
		this.returnStyle = returnStyle;
	}

	public void setSource(final Widget sender) {
		this.sender = sender;
	}

}

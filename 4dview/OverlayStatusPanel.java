import java.awt.*;
import java.awt.event.*;

//**************************************************************
//*		O V E R L A Y  S T A T U S  P A N E L
//*		Created 7/25/02 for v1.59
//**************************************************************
class OverlayStatusPanel extends Panel
{
	//***** Layout format constants
	final	static	int			HORIZONTAL = 0;
	final	static	int			VERTICAL = 1;

	final	static	Color		DARK_RED = new Color(70,0,0);
	final	static	Color		DARK_GREEN = new Color(0,35,0);


					int			layout_format = HORIZONTAL;
					int			width = 0;
					int			height = 0;


	//*******************************************************
	//*		I N I T
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	public OverlayStatusPanel()
	{
		super();

		if (this.getSize().width >= this.getSize().height)
			layout_format = HORIZONTAL;
		else
			layout_format = VERTICAL;

		this.width = this.getSize().width;
		this.height = this.getSize().height;

		//***** Add listeners
		MyMouseAdapter mouse_adapter = new MyMouseAdapter();
		this.addMouseListener(mouse_adapter);

		return;

	}// init


	//*******************************************************
	//*		S E T  S I Z E
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	public void setSize(int w, int h)
	{
		super.setSize(w, h);

		if (w >= h)
			layout_format = HORIZONTAL;
		else
			layout_format = VERTICAL;

		this.width = w;
		this.height = h;

		repaint();

		return;

	}// end of setSize()


	//*******************************************************
	//*		S E T  S I Z E
	//*		Modified 8/28/02 for v1.61
	//*******************************************************
	public void setSize(Dimension d)
	{
		super.setSize(d);

		int w = d.width;
		int h = d.height;

		if (w >= h)
			layout_format = HORIZONTAL;
		else
			layout_format = VERTICAL;

		this.width = w;
		this.height = h;

		repaint();

		return;

	}// end of setSize()


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Modified 8/29/02 for v1.61
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		Dimension d = new Dimension(this.width, this.height);

		return(d);

	}// end of getPreferredSize()


	//*******************************************************
	//*		P A I N T
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		try
		{
			fillBackground(g);
			outlinePanel(g);
			drawStoplights(g);
		}
		catch (Exception e)
		{
		}

		return;

	}// end of paint


	//*******************************************************************
	//*		M Y  M O U S E  A D A P T E R
	//*		MyMouseAdapter is a mouse listener which shuttles mouse events
	//*		to the appropriate methods
	//*******************************************************************
	class MyMouseAdapter extends MouseAdapter
	{

		public void mousePressed(MouseEvent event)
		{
			try
			{
				doMousePressed(event);
			}
			catch (Exception e)
			{
				Settings.main_window.displayMessage("Exception thrown in OverlayStatusPanel.mousePressed()!");
			}
		}

		public void mouseReleased(MouseEvent event)
		{
		}

		public void mouseClicked(MouseEvent event)
		{
		}

	}// end of class MyMouseAdapter


	//*****************************************************************
	//*		D O  M O U S E  P R E S S E D
	//*		Modified 8/1/02 for v1.59
	//*****************************************************************
	public void doMousePressed(MouseEvent e) throws Exception
	{
		PutMsgDlog				pmd = null;
		String					msg = null;

		msg = new String("About the Overlay Status Panel: " +
						 "If the red (right) signal is active, changes have " +
						 "been made to the overlay but it has not been saved.  " +
						 "If the green (left) signal is active, saving is not required.");

		pmd = new PutMsgDlog(Settings.editing_window, true, msg);
		pmd.setVisible(true);

		return;

	}// end of doMousePressed()


	//*******************************************************
	//*		O U T L I N E  P A N E L
	//*		Modified 8/27/02 for v1.61
	//*******************************************************
	private void outlinePanel(Graphics incoming_graphics) throws Exception
	{
		Graphics		g = null;
		int				width = 0, height = 0;

		if (incoming_graphics == null)
			g = this.getGraphics();
		else
			g = incoming_graphics;


		width = this.getSize().width;
		height = this.getSize().height;

		g.setColor(Color.black);
		g.drawRect(1, 1, width - 2, height - 2);

		if (incoming_graphics == null)
			g.dispose();

		return;

	}// end of fillBackground()


	//*******************************************************
	//*		F I L L  B A C K G R O U N D
	//*		Modified 8/27/02 for v1.61
	//*******************************************************
	private void fillBackground(Graphics incoming_graphics) throws Exception
	{
		Graphics		g = null;
		int				width = 0, height = 0;

		if (incoming_graphics == null)
			g = this.getGraphics();
		else
			g = incoming_graphics;

		width = this.getSize().width;
		height = this.getSize().height;

		g.setColor(Color.lightGray);
		g.fillRect(0, 0, width-1, height-1);

		if (incoming_graphics == null)
			g.dispose();

		return;

	}// end of fillBackground()


	//*******************************************************
	//*		D R A W  S T O P L I G H T S
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	private void drawStoplights(Graphics incoming_graphics) throws Exception
	{
		Graphics		g = null;


		if (width <= 2 || height <= 2)
			return;

		if (incoming_graphics == null)
			g = this.getGraphics();
		else
			g = incoming_graphics;

		if (layout_format == HORIZONTAL)
			drawHorizontalStoplights(g);
		else
			drawVerticalStoplights(g);


		if (incoming_graphics == null)
			g.dispose();

		return;

	}// end of drawStoplights()


	//*******************************************************
	//*		D R A W  H O R I Z O N T A L  S T O P L I G H T S
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	private void drawHorizontalStoplights(Graphics g) throws Exception
	{
		int			x = 0, y = 0;
		Rectangle	red_rect = new Rectangle(0,0,0,0);
		Rectangle	green_rect = new Rectangle(0,0,0,0);


		if (g == null)
			throw new Exception("Null graphics object in drawHorizontalStoplights()");

		//***** Define the green stoplight
		green_rect.height = this.height - 2;
		green_rect.width = green_rect.height;
		green_rect.x = (this.width/4) - (green_rect.width/2);
		green_rect.y = 1;

		//***** Define the red stoplight
		red_rect.height = this.height - 2;
		red_rect.width = red_rect.height;
		red_rect.x = (int) ((this.width * 0.75) - (green_rect.width/2.0));
		red_rect.y = 1;

		doStoplightPainting(g, green_rect, red_rect);

		return;

	}// end of drawHorizintalStoplights()


	//*******************************************************
	//*		D R A W  V E R T I C A L  S T O P L I G H T S
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	private void drawVerticalStoplights(Graphics g) throws Exception
	{
		int			x = 0, y = 0;
		Rectangle	red_rect = new Rectangle(0,0,0,0);
		Rectangle	green_rect = new Rectangle(0,0,0,0);


		if (g == null)
			throw new Exception("Null graphics object in drawVerticalStoplights()");

		//***** Define the green stoplight
		green_rect.width = this.width - 2;
		green_rect.height = green_rect.width;
		green_rect.y = (this.height/4) - (green_rect.height/2);
		green_rect.x = 1;

		//***** Define the red stoplight
		red_rect.width = this.width - 2;
		red_rect.height = red_rect.width;
		red_rect.y = (int)((this.height * 0.75) - (green_rect.height/2.0));
		red_rect.x = 1;

		doStoplightPainting(g, green_rect, red_rect);

		return;

	}// end of drawVerticalStoplights()



	//*******************************************************
	//*		D O  S T O P L I G H T  P A I N T I N G
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	private void doStoplightPainting(Graphics g, Rectangle green_rect, Rectangle red_rect) throws Exception
	{

		if (g == null || green_rect == null || red_rect == null)
			throw new Exception("Invalid parameter in doStoplightPainting().");

		//***** Draw the green stoplight
		if (EditingSettings.overlay_status == EditingSettings.OVERLAY_SAVED)
			g.setColor(Color.green);
		else
			g.setColor(DARK_GREEN);
		g.fillOval(green_rect.x, green_rect.y, green_rect.width, green_rect.height);
		g.setColor(Color.black);
		g.drawOval(green_rect.x, green_rect.y, green_rect.width, green_rect.height);


		//***** Draw the red stoplight
		if (EditingSettings.overlay_status == EditingSettings.OVERLAY_SAVED)
			g.setColor(DARK_RED);
		else
			g.setColor(Color.red);
		g.fillOval(red_rect.x, red_rect.y, red_rect.width, red_rect.height);
		g.setColor(Color.black);
		g.drawOval(red_rect.x, red_rect.y, red_rect.width, red_rect.height);

		return;

	}// end of doStoplightPainting()

}// end of OverlayStatusPanel

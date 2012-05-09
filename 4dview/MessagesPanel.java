import java.awt.*;
import java.awt.event.*;

//***************************************************************
//***************************************************************
//*		M E S S A G E  P A N E L
//*		Added 12/9/02 for v1.70
//***************************************************************
//***************************************************************
class MessagesPanel extends Panel implements AdjustmentListener
{
	static final int		SCROLL_BAR_WIDTH = 15;

	MessagesCanvas			messages_canvas = null;
	Scrollbar				scroll_bar = null;
	int						scroll_value = 0;
	int						panel_width = 0, panel_height = 0;


	//*****************************************************************
	//*		I N I T
	//*		Added 12/9/02 for v1.70
	//*		Modified 12/10/02 for v1.70
	//*****************************************************************
	public	MessagesPanel()
	{

		super();

		setLayout(null);

		panel_width = 300;
		panel_height = 139;

		setSize(panel_width, panel_height);
		setForeground(Color.black);
		setBackground(Color.white);

		messages_canvas = new MessagesCanvas(this);
		messages_canvas.setSize(panel_width - SCROLL_BAR_WIDTH, panel_height);
		messages_canvas.setLocation(0,0);
		add(messages_canvas);

		scroll_bar = new Scrollbar(Scrollbar.VERTICAL);
		scroll_bar.addAdjustmentListener(this);
		scroll_bar.setSize(SCROLL_BAR_WIDTH, panel_height);
		scroll_bar.setBlockIncrement(5);
		scroll_bar.setValues(0, 0, 0, messages_canvas.getNumOffscreenMessages()); // value, visible_amount, min, max
		scroll_bar.setLocation(panel_width - SCROLL_BAR_WIDTH, 0);
		add(scroll_bar);

		return;

	}// init


	//**********************************************************************
	//*		P A I N T
	//*		Added 12/9/02 for v1.70
	//*		Modified 12/11/02 for v1.70
	//**********************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);


		return;

	}// end of paint()


	//*****************************************************************
	//*		D I S P L A Y  M E S S A G E
	//*		Added 12/9/02 for v1.70
	//*****************************************************************
	public void displayMessage(String msg) throws Exception
	{
		messages_canvas.displayMessage(msg);
		scroll_bar.setMaximum(messages_canvas.getNumOffscreenMessages());
		scroll_bar.setValue(messages_canvas.getNumOffscreenMessages());
		setScrollPosition(0, messages_canvas.getLineHeight() * messages_canvas.getNumOffscreenMessages());

		return;

	}// end of displayMessage()


	//*****************************************************************
	//*		C L E A R  M E S S A G E S
	//*		Added 12/9/02 for v1.70
	//*****************************************************************
	public void clearMessages() throws Exception
	{
		messages_canvas.clearMessages();
		scroll_bar.setMaximum(messages_canvas.getNumOffscreenMessages());
		scroll_bar.setValue(messages_canvas.getNumOffscreenMessages());
		setScrollPosition(0, messages_canvas.getLineHeight() * messages_canvas.getNumOffscreenMessages());

		return;

	}// end of clearMessage()


	//*****************************************************************
	//*		S E T  S I Z E
	//*		Added 12/9/02 for v1.70
	//*****************************************************************
	public void setSize(Dimension d)
	{
		setSize(d.width, d.height);
		return;

	}// end of setSize()


	//*****************************************************************
	//*		S E T  S I Z E
	//*		Added 12/9/02 for v1.70
	//*		Modified 12/11/02 for v1.70
	//*****************************************************************
	public void setSize(int w, int h)
	{
		super.setSize(w, h);

		panel_width = w;
		panel_height = h;

		if (messages_canvas != null)
			messages_canvas.setSize(w - SCROLL_BAR_WIDTH, messages_canvas.getSize().height);

		if (scroll_bar != null)
		{
			scroll_bar.setSize(SCROLL_BAR_WIDTH, h);
			scroll_bar.setLocation(panel_width - SCROLL_BAR_WIDTH, 0);
		}

		//Settings.main_window.displayMessage("w: " + w + " h: " + h);

		return;

	}// end of setSize()


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 12/9/02 for v1.70
	//*		Modified 12/11/02 for v1.70
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(panel_width, panel_height));

	}// end of getPreferredSize()


	//*****************************************************************
	//*		A D J U S T M E N T  V A L U E  C H A N G E D
	//*		Added 12/9/02 for v1.70
	//*****************************************************************
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		scroll_value = e.getValue();

		setScrollPosition(0, messages_canvas.getLineHeight() * scroll_value);

		return;

	}// end of adjustmentValueChanged()


	//*****************************************************************
	//*		S E T  S C R O L L  P O S I T I O N
	//*		Added 12/9/02 for v1.70
	//*****************************************************************
	public void setScrollPosition(int x, int y)
	{
		messages_canvas.setLocation(x, -y);

		return;

	}// end of setScrollPosition()


	//*****************************************************************
	//*		G E T  C A N V A S  W I D T H
	//*		Added 12/9/02 for v1.70
	//*****************************************************************
	public int getCanvasWidth()
	{
		return(panel_width - SCROLL_BAR_WIDTH);

	}// end of getCanvasWidth()


}// end of class MessagesPanel

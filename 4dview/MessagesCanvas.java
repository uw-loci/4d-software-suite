import java.awt.*;
import java.lang.*;
import java.util.*;



public class MessagesCanvas extends Canvas
{

	static final int	 TOP_MARGIN = 10;
	static final int	 LEFT_MARGIN = 5;

	//***** Begin Class Variables
	private	Vector					message_array = null; // holds message strings
	private	MessagesPanel			messages_panel = null;
	private	int						line_height = 0, lines_to_display = 0;

	//*****************************************************************
	//*		I N I T
	//*		Modified 12/9/02 for v1.70
	//*****************************************************************
	public	MessagesCanvas(MessagesPanel mp)
	{
		this.messages_panel = mp;
		this.message_array = new Vector(10, 0);
		this.line_height = 10;
		this.lines_to_display = 10;

		setSize(285, 139);
		setForeground(Color.black);
		setBackground(Color.white);
		setFont(new Font("Geneva", Font.PLAIN, 9));

		try
		{
			getFontInfo();
		}
		catch (Exception e){}

		restoreMessages();

	}// end of init()


	//*****************************************************************
	//*		U P D A T E
	//*****************************************************************
	public void update(Graphics g)
	{
		//***** Clear the text area
		g.setColor(getBackground());
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(getForeground());

		paint(g);

	}// end of update()


	//*****************************************************************
	//*		P A I N T
	//*****************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		//***** Restore the window's text
		restoreMessages();

		g.setColor(Color.black);
		g.drawRect(1, 1, this.getBounds().width - 2, this.getBounds().height - 2);


		return;

	}// end of paint()


	//********************************************************************************
	//*		G E T  F O N T  I N F O
	//*		Added 12/9/02 for v1.70
	//********************************************************************************
	private	void getFontInfo() throws Exception
	{
		Graphics 		g = null;
		FontMetrics		fm = null;

		g = this.getGraphics(); // get fresh graphics contest

		if (g != null)
		{

			//***** Deal with font issues
			fm = g.getFontMetrics();
			line_height = fm.getAscent() + fm.getDescent() + fm.getLeading();
			lines_to_display = messages_panel.getSize().height/line_height;


			g.dispose();
		}// if g != null

		return;

	}// end of getFontInfo()


	//********************************************************************************
	//*		R E S T O R E  M E S S A G E S
	//*		Added 12/9/02 for v1.70
	//********************************************************************************
	public	void	restoreMessages()
	{
		Graphics	g = null;
		int			i = 0;
		String		cur_message = null;


		try
		{
			g = this.getGraphics(); // get fresh graphics contest

			if (g != null)
			{
				getFontInfo();

				//***** Display each message
				for (i = 0; i < message_array.size(); i++)
				{
					cur_message = (String)this.message_array.elementAt(i);
					g.drawString(cur_message, LEFT_MARGIN, TOP_MARGIN + (i * line_height));
				}// for each message to display

				g.dispose();
			}// if g != null
		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while restoring messages!");
		}

		return;

	}// end of restoreMessages()


	//*******************************************************************
	//*		D I S P L A Y  M E S S A G E
	//*		Added 12/9/02 for v1.70
	//*******************************************************************
	public void displayMessage(String message_string)
	{

		//***** Add the newest message
		this.message_array.addElement(message_string);

		if (message_array.size() <= lines_to_display)
		{
			this.setSize(messages_panel.getCanvasWidth(), messages_panel.getSize().height);
			//messages_panel.setScrollPosition(0, 0);
		}// if we have fewer messages than will fit in the window
		else
		{
			this.setSize(messages_panel.getCanvasWidth(), message_array.size() * (line_height + 1));
			//this.messages_panel.setScrollPosition(0, this.getSize().height);
		}// if we have more messages than will fit in the window

		repaint();

		return;

	}//end of displayMessage()


	//*******************************************************************
	//*		C L E A R  M E S S A G E S
	//*		Added 12/9/02 for v1.70
	//*******************************************************************
	public void clearMessages()
	{
		//***** Reset the array holding the messages
		this.message_array.removeAllElements();

		//***** Resize the canvas to its original size
		this.setSize(messages_panel.getCanvasWidth(), messages_panel.getSize().height);

		//***** Redraw the canvas as blank
		repaint();

		//****** Reset the scrollbars in the message panel
		messages_panel.doLayout();

		return;

	}// end of clearMessages()


	//*******************************************************************
	//*		G E T  N U M  M E S S A G E S
	//*		Added 12/9/02 for v1.70
	//*******************************************************************
	public int getNumMessages()
	{
		if (message_array != null)
			return(message_array.size());
		else
			return(0);

	}// end of getNumMessages()


	//*******************************************************************
	//*		G E T  N U M  O F F S C R E E N  M E S S A G E S
	//*		Added 12/9/02 for v1.70
	//*******************************************************************
	public int getNumOffscreenMessages()
	{
		int offscreen_messages = 0;

		if (message_array != null)
			offscreen_messages = message_array.size() - lines_to_display;

		if (offscreen_messages < 0)
			offscreen_messages = 0;

		return(offscreen_messages);

	}// end of getNumOffscreenMessages()


	//*******************************************************************
	//*		G E T  L I N E  H E I G H T
	//*		Added 12/9/02 for v1.70
	//*******************************************************************
	public int getLineHeight()
	{
		return(line_height);

	}// end of getLineHeight()

}//End of TextCanvas




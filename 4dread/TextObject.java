import java.awt.*;
import java.awt.event.*;

//**************************************************************************
//*		T E X T  O B J E C T
//**************************************************************************
public class TextObject extends AnnotationObject
{


	//======= TEXT CODES
	final		static	int			END_OF_FILE = 0x04;
	final		static	int			PARAGRAPH_CHAR = 0xA6;
	final		static	int			RETURN_CHAR = 0x0D;
	final		static	int			LINE_FEED_CHAR = 0x0A;
	final		static	int			DELETE_CHAR = 0x08;
	final		static	int			TAB_CHAR = 0x09;
	final		static	int			RIGHT_ARROW = 1007;//0x1D;
	final		static	int			LEFT_ARROW = 1006;//0x1C;
	final		static	int			UP_ARROW = 1004;//0x1E;
	final		static	int			DOWN_ARROW = 1005;//0x1F;
	final		static	int			QUOTATION_MARK = 0x22;

	final		static	int			MARGIN = 2;

	protected	String				text_string = null;// the text itself
	protected	int					text_counter = 0; // how many characters?
	protected	Point				start_pt = null; // where did the mouse click occur?
	protected	Point				pen_pt = null; // where will the actual drawing begin?
	protected	Point				insert_pt;// used to draw flashing insert bar
	protected	String				font_name = null;
	protected	int					font_style = 0;
	protected	int					font_size = 0;
	protected	int					bottom_margin = 0; // how far to drop the bottom margin
										   			   // to include "g" and other descending chars

	protected	int					text_height = 0;
	protected	int					char_height = 0;

	//***** Creator method
	public TextObject()
	{
		super();

		this.object_type = TEXT_OBJECT;

		this.text_string = new String("");
		this.text_counter = 0;
		this.start_pt = new Point(0,0);
		this.pen_pt = new Point(0,0);
		this.insert_pt = new Point(0,0);

		this.font_name = EditingSettings.font_name;
		this.font_style = EditingSettings.font_style;
		this.font_size = EditingSettings.font_size;
		this.object_color = EditingSettings.text_color;

		this.bottom_margin = 0;
		this.text_height = 0;

	}// init


	public TextObject(int x, int y)
	{
		this();
		this.start_pt.x = x;
		this.start_pt.y = y;

		return;

	}

	//************************************************
	//*		D R A W  O B J E C T
	//************************************************
	public void drawObject(Graphics g)
	{
		Font					f = null;
		FontMetrics 			fm = null;
		int						text_width = 0;
		int						line_spacing = 0;
		int						char_width = 0;
		int						i = 0, new_width = 0, new_height = 0;
		String					char_string = null;
		Color					save_color = null;


		//***** Create a new font
		if (this.font_name == null)
			return;
		f = new Font(this.font_name, this.font_style, this.font_size);
		if (f == null)
			return;

		//***** Set the font to the created font
		g.setFont(f);

		//***** Get information about the font
		fm = g.getFontMetrics(f);
		if (fm == null)
			return;

		//***** Get the necessary information from the FontMetrics
		text_height = fm.getDescent() + fm.getAscent();
		char_height = (int)(fm.getHeight() * 0.6);
		text_width = fm.getMaxAdvance();
		line_spacing = fm.getLeading();
		this.bottom_margin = fm.getDescent();
		this.pen_pt.x = this.start_pt.x;
		this.pen_pt.y = this.start_pt.y;
		this.insert_pt.x = this.start_pt.x;
		this.insert_pt.y = this.start_pt.y;
		this.pen_pt.y += (int)text_height/2; // set to draw to the center of that point

		//***** Set up the drawing conditions
		save_color = g.getColor();
		g.setColor(this.object_color);

		//***** Reset the object's rect
		this.object_rect.x = this.pen_pt.x - MARGIN; // expand the boundary a bit
		this.object_rect.y = (this.pen_pt.y - fm.getAscent()) - MARGIN; // expand the boundary a bit
		this.object_rect.width = 0;
		this.object_rect.height = 0;

		//***** Determine new object rect
		this.getNewObjectRect();

		//***** Draw the characters, one by one
		for (i = 0; i < this.text_counter; i++)
		{
			//***** Draw non-paragraph characters
			if (text_string.charAt(i) != (char)PARAGRAPH_CHAR && text_string.charAt(i) != '|')
			{
				char_string = null; // set for garbage collection
				char_string = String.valueOf(text_string.charAt(i)); // holds one char
				char_width = fm.charWidth(text_string.charAt(i)); // find width
				g.drawString(char_string, this.pen_pt.x, this.pen_pt.y); // draw one char
				this.pen_pt.x += fm.charWidth(text_string.charAt(i)); //advance the pen point the char's width
				this.insert_pt.x += fm.charWidth(text_string.charAt(i)); //advance the insert point the char's width
			}// if it's not a paragraph sign

			//***** Move down a line
			else if (text_string.charAt(i) == (char)PARAGRAPH_CHAR || text_string.charAt(i) == '|')
			{
				//***** Change the pen point
				this.pen_pt.y += fm.getAscent() + 2;
				this.pen_pt.x = this.start_pt.x;

				//***** Change the insert point
				this.insert_pt.y += fm.getAscent() + 2;
				this.insert_pt.x = this.start_pt.x;
			}// move down one line

			//***** Determine new object rect
			this.getNewObjectRect();

		}// for each character

		g.setColor(save_color);

		this.dirty = false;

		return;

	}// end of draw_object()


	//*******************************************************************
	//*		G E T  I N S E R T  P O I N T
	//*******************************************************************
	public Point	getInsertPoint()
	{
		return(new Point(this.insert_pt));

	}// end of getInsertPoint()


	//*******************************************************************
	//*		G E T  C H A R  H E I G H T
	//*******************************************************************
	public int	getCharHeight()
	{
		return(this.char_height);

	}// end of getCharHeight()


	//*******************************************************************
	//*		F I N I S H  O B J E C T
	//*******************************************************************
	public void finishTextObject(Graphics g, int x, int y)
	{
		finished = true;
		drawObject(g); // draw the new one

		return;

	}// end of finishTextObject()


	//*******************************************************************
	//*		I S  V A L I D  O B J E C T
	//*		Retuns false if object fails tests for validity
	//*******************************************************************
	public boolean  isValidObject()
	{
		if (text_counter <= 0 && finished)
			return(false);

		return(true);

	}// end of isValidObject()


	//************************************************
	//*		G E T  N E W  O B J E C T  R E C T
	//************************************************
	public void getNewObjectRect()
	{

		int		new_width = 0;
		int		new_height = 0;

		new_width = (this.pen_pt.x - this.object_rect.x) + (MARGIN * 2);
		new_height = (this.pen_pt.y - this.object_rect.y) + (MARGIN * 2) + this.bottom_margin;
		if (new_width > this.object_rect.width)
			this.object_rect.width = new_width;
		if (new_height > this.object_rect.height)
			this.object_rect.height = new_height;

		return;

	}// end of getNewObjectRect()


	//************************************************
	//*		A D D  T E X T  C H A R A C T E R
	//************************************************
  /*
	public void addTextCharacter(EditingCanvas ec, int ch)
	{
		String  new_string = null;

		switch (ch)
		{
			case KeyEvent.VK_DELETE:
			case DELETE_CHAR:
				 try
				 {
			   	  ec.clearObject(this.object_rect);
			   	  this.text_counter -= 1;
			   	  if (this.text_counter < 1)
			   	  	this.text_counter = 0;
			   	  new_string = this.text_string.substring(0, this.text_counter);// remove the last char
			   	  this.text_string = new_string;
			   	 }// try
			   	 catch (Exception e)
			   	 {
			   	 	//Settings.main_window.displayMessage("Exception thrown while deleting object!");
			   	 	System.out.println("Exception thrown while deleting object!");
			   	 }
				 break;
		 	case KeyEvent.VK_ENTER:
		 	case RETURN_CHAR:
		 	//case LINE_FEED_CHAR:
		 		 this.text_string += (char)PARAGRAPH_CHAR; // use the paragraph symbol
		 		 this.text_counter += 1;
		 		 break;
		 	default:
		 		 if (ch >= 0x20 && ch <= 0x7E)
		 		 {
		 		 	this.text_string += (char)ch;
		 		 	this.text_counter += 1;
		 		 }// if it's a printable character
		 		 break;
		}// end of switch

		this.drawObject(ec.getGraphics());

		return;

	}// end of addTextCharacter()
  */



	//**************************************************************
	//*		C L I C K  I N  O B J E C T
	//*		Returns true if the click was in the object's boundary
	//**************************************************************
	public boolean clickInObject(int x, int y)
	{

		if (this.object_rect.contains(x, y))
			return(true);
		else
			return(false);

	}// end of clickInObject()


	//************************************************
	//*		D R A G  O B J E C T
	//************************************************
  /*
	public void dragObject(EditingCanvas ec, int x, int y)
	{
		int			x_offset = 0;
		int			y_offset = 0;
		Graphics	g = ec.getGraphics();


		if (g == null)
			return;

		ec.clearObject(this.object_rect); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		this.object_rect.x += x_offset;
		this.object_rect.y += y_offset;
		this.start_pt.x += x_offset;
		this.start_pt.y += y_offset;

		this.drawObject(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		g.dispose();

		return;

	}// end of dragObject()
  */


	//***********************************************
	//*		H A N D L E  C L I C K  O N  O B J E C T
	//***********************************************
	public void handleClickOnObject(int x, int y)
	{
		last_click.x = x;
		last_click.y = y;
		first_click.x = x;
		first_click.y = y;

		return;

	}// end of handleClickOnObject()


	//************************************************************
	//*		C H E C K  S E L E C T
	//* 	Checks to see if select status needs to be changed
	//*		Returns TRUE if status was changed
	//************************************************************
  /*
	public boolean checkSelect(EditingCanvas ec, int x, int y)
	{

		if (x == first_click.x && y == first_click.y)
		{
			this.selected = !this.selected;
			if (!this.selected)
			{
				Graphics 	g = ec.getGraphics();

				if (g != null)
				{
					ec.clearObject(this.object_rect);
					this.drawObject(g); // draw new one
					g.dispose();
				}// if we have a graphics context
			}// if we deselected

			return(true);

		}// if we changed status
		else
			return(false);

	}// end of check_change_select()
  */


	//************************************************
	//*		S E L E C T  O B J E C T
	//************************************************
	public void selectObject(Graphics g)
	{

		Color		save_color = null;

		if (g == null)
			return;

		if (this.selected)
		{
			save_color = g.getColor();

			//***** Draw the object rect outline
			g.setXORMode(Color.white); // set to outline mode
			g.drawRect(this.object_rect.x, this.object_rect.y,
					   this.object_rect.width, this.object_rect.height);
			g.setPaintMode(); // restore paint mode
			g.setColor(save_color); // restore color mode

		}// if we're selecting

		return;

	}// end of selectObject()


	//************************************************
	//*		F L A S H  O B J E C T
	//*		If on, the object flashes white
	//*		if !on, the object flashes black
	//********************************************
	public void flashObject(Graphics g, boolean on)
	{

		Color			save_color = null;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			g.setPaintMode();
			if (on)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);

			g.drawRect(this.object_rect.x, this.object_rect.y,
					   this.object_rect.width, this.object_rect.height);
			g.setPaintMode(); // restore paint mode
			g.setColor(save_color); // restore color mode

		}// if we're selecting

		return;

	}// end of flashObject()


	//************************************************
	//*   I S  I D E N T I C A L  O B J E C T
	//************************************************
	public boolean	isIdenticalObject(AnnotationObject test_obj)
	{
		boolean			is_identical = false;
		TextObject		text_obj = null;
		int				i = 0;

		is_identical = super.isIdenticalObject(test_obj);

		if (!is_identical)
			return(false);

		if (!(test_obj instanceof TextObject))
			return(false);

		text_obj = (TextObject)test_obj;

		if (text_string != text_obj.text_string ||
			start_pt.x != text_obj.start_pt.x ||
			start_pt.y != text_obj.start_pt.y)
				return(false);

		return(true);

	}// end of isIdenticalObject()


	//*********************************************************************
	//*		G E T  P R O X Y
	//*********************************************************************
	public AnnotationObjectProxy	getProxy()
	{
		return(new TextObjectProxy(this));
	}// end of getProxy()


  /*
	//*********************************************************************
	//*		G E T  O B J E C T  O P T I O N S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public void getObjectOptions(Frame parent) throws Exception
	{
		TextOptDlog		tod = null;

 		tod = new TextOptDlog(Settings.editing_window, this, true);
 		if (tod != null)
 			tod.setVisible(true);

 		return;

	}// end of getObjectOptions()
  */

}// end of class TextObject

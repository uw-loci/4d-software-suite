import java.awt.*;
import java.lang.*;
import java.io.*;
import java.util.*;

//**************************************************************************
//*		L I N E  O B J E C T
//**************************************************************************
public class LineObject extends AnnotationObject
{
	//***** Constants to show which point is being dragged if the object is being resized
	final static int		NONE = 0;
	final static int		START_POINT = 1;
	final static int		END_POINT = 2;

	protected	int			pen_width = 0;
	protected	Point		start_pt = null;
	protected	Point		end_pt = null;
	protected	int			editing_point = 0;

	//***** Creator method
	public LineObject()
	{
		super();
		this.setObjectType(LINE_OBJECT);
		this.pen_width = 0;
	 	this.start_pt = new Point(0,0);
	 	this.end_pt = new Point(0,0);
		this.object_color = EditingSettings.line_color;

		this.editing_point = NONE;

	}// end of LineObject constructor


	public LineObject(int x, int y)
	{
		this();
		this.start_pt = new Point(x,y);
		this.end_pt = new Point(x, y);


		return;
	}

	public LineObject(int start_x, int start_y, int end_x, int end_y)
	{
		this(start_x, start_y);
		this.end_pt = new Point(end_x, end_y);

		return;
	}

	//************************************************
	//*		D R A W  O B J E C T
	//************************************************
	public void drawObject(Graphics g)
	{

		if (g == null || start_pt == null || end_pt == null)
			return;

		if (this.getFinished())
		{
			g.setPaintMode();
			g.setColor(this.object_color);
		}
		else if (!this.getFinished())
		{
			g.setXORMode(Color.white);
		}

		g.drawLine(this.start_pt.x,
				   this.start_pt.y,
				   this.end_pt.x,
				   this.end_pt.y);

		g.setPaintMode();

		this.dirty = false;

		return;

	}// end of drawObject()


	//*******************************************************************
	//*		E D I T  O B J E C T
	//*******************************************************************
	public void editObject(Graphics g, int x, int y)
	{
		if (g == null)
			return;

		drawObject(g); // get rid of the old one

		end_pt.x = x;
		end_pt.y = y;
		getObjectRect(); // determine the object's bounding rectangle

		drawObject(g);

		return;

	}// end of editObject


	//*******************************************************************
	//*		F I N I S H  O B J E C T
	//*******************************************************************
	public void finishObject(Graphics g, int x, int y)
	{

		drawObject(g);// get rid of the old one

		end_pt.x = x;
		end_pt.y = y;

		getObjectRect(); // determine the object's bounding rectangle
		finished = true;
		drawObject(g); // draw the new one

		return;

	}// end of finishObject()



	//*******************************************************************
	//*		I S  V A L I D  O B J E C T
	//*		Retuns false if object fails tests for validity
	//*******************************************************************
	public boolean  isValidObject()
	{
		if (end_pt.x == start_pt.x || end_pt.y == start_pt.y)
			return(false);

		return(true);

	}// end of isValidObject()


	//**************************************************************
	//*		C L I C K  I N  O B J E C T
	//*		Returns true if the click was in the object's boundary
	//**************************************************************
	public boolean clickInObject(int x, int y)
	{
		Rectangle	check_rect = null;

		getLineRect();// determine the border rect

		if (this.object_rect.contains(x, y))
			return(true);

		if (this.selected)
		{
			if (clickInResizeRect(x, y))
				return(true);
		}

		return(false);

	}// end of clickInObject()


	//****************************************************************
	//*		C L I C K  I N  R E S I Z E  R E C T
	//*		Returns true if the click was in the object's resize rect
	//*		and the object was selected
	//*		Makes sure that the click was always in the end_pt
	//****************************************************************
	public boolean clickInResizeRect(int x, int y)
	{
		Rectangle	resize_rect1 = new Rectangle(0,0,0,0);
		Rectangle	resize_rect2 = new Rectangle(0,0,0,0);
		Point		temp_pt = new Point(0,0);


		//***** Define the first resize rect
		resize_rect1.x = this.start_pt.x - 2;
		resize_rect1.y = this.start_pt.y - 2;
		resize_rect1.width = RESIZE_RECT_WIDTH;
		resize_rect1.height = RESIZE_RECT_WIDTH;

		if (resize_rect1.contains(x, y) && this.selected)
		{
			this.editing_point = START_POINT;
			return(true);
		}

		//****** Define the second resize rect
		resize_rect2.x = this.end_pt.x - 2;
		resize_rect2.y = this.end_pt.y - 2;
		resize_rect2.width = RESIZE_RECT_WIDTH;
		resize_rect2.height = RESIZE_RECT_HEIGHT;

		if (resize_rect2.contains(x, y) && this.selected)
		{
			this.editing_point = END_POINT;
			return(true);
		}


		this.editing_point = NONE;
		return(false);

	}// end of clickInResizeRect()


	//************************************************
	//*		D R A G  O B J E C T
	//************************************************
  /*
	public void dragObject(EditingCanvas ec, int x, int y)
	{
		int				x_offset = 0;
		int				y_offset = 0;
		Graphics		g = ec.getGraphics();
		Rectangle		clear_rect = null;


		if (g == null)
			return;

		clear_rect = new Rectangle(this.object_rect);

		if (this.getSelected())
		{
			clear_rect.x -=2;
			clear_rect.y -=2;
			clear_rect.width += 4;
			clear_rect.height += 4;
		}
		else
		{
			clear_rect.x -=1;
			clear_rect.y -=1;
			clear_rect.width += 2;
			clear_rect.height += 2;
		}


		ec.clearObject(clear_rect);// get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		this.start_pt.x += x_offset;
		this.start_pt.y += y_offset;
		this.end_pt.x += x_offset;
		this.end_pt.y += y_offset;

		this.object_rect.x += x_offset;
		this.object_rect.y += y_offset;

		this.drawObject(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		g.dispose();//get rid of graphics context

		return;

	}// end of dragObject()
  */


	//************************************************
	//*		R E S I Z E  O B J E C T
	//************************************************
  /*
	public void resizeObject(EditingCanvas ec, int x, int y)
	{
		Rectangle		clear_rect = null;
		int				width = 0, height = 0;
		Graphics		g = ec.getGraphics();

		if (g == null)
			return;

		if (this.selected || this.finished)
		{
			clear_rect = new Rectangle(this.object_rect);
			clear_rect.width += 3;
			clear_rect.height += 3;
			ec.clearObject(clear_rect); // get rid of the old one
		}
		else
			this.drawObject(g);// get rid of the old one

		if (this.editing_point == START_POINT)
		{
			start_pt.x = x;
			start_pt.y = y;
		}
		else if (this.editing_point == END_POINT)
		{
			end_pt.x = x;
			end_pt.y = y;
		}

		this.getLineRect();// recalculate the bounding rect
		this.drawObject(g);// draw the new one

		g.dispose();// get rid of graphics context

		return;

	}// end of resize_object()
  */


	//************************************************
	//*		H A N D L E  C L I C K  O N  O B J E C T
	//********************************************
	public void handleClickOnObject(int x, int y)
	{

		this.last_click.x = x;
		this.last_click.y = y;
		this.first_click.x = x;
		this.first_click.y = y;

		return;

	}// end of handleClickOnObject()


	//************************************************************
	//*		C H E C K  S E L E C T
	//* 	Checks to see if select status needs to be changed
	//*		Returns true if status was changed
	//************************************************************
  /*
	public boolean checkSelect(EditingCanvas ec, int x, int y)
	{
		Rectangle		clear_rect = null;

		if (x == first_click.x && y == first_click.y)
		{
			Graphics		g = ec.getGraphics();

			this.selected = !this.selected;
			if (!this.selected)
			{
				clear_rect = new Rectangle(this.object_rect);
				clear_rect.x -=2;
				clear_rect.y -=2;
				clear_rect.width += 4;
				clear_rect.height += 4;

				ec.clearObject(clear_rect);
				this.drawObject(g); // draw new one
			}// if we deselected

			g.dispose();// get rid of graphics context

			return(true);

		}
		else
			return(false);

	}// end of checkSelect()
  */


	//************************************************
	//*		S E L E C T  O B J E C T
	//********************************************
	public void selectObject(Graphics g)
	{

		Rectangle	resize_rect1 = new Rectangle(0,0,0,0);
		Rectangle	resize_rect2 = new Rectangle(0,0,0,0);
		Color		save_color = null;

		if (this.selected)
		{
			save_color = g.getColor();

			g.setPaintMode();
			g.setColor(Color.black);
			g.drawLine(this.start_pt.x, this.start_pt.y, this.end_pt.x, this.end_pt.y);	// make a black version

			//***** Draw the object
			g.setXORMode(Color.white);
			g.drawLine(this.start_pt.x, this.start_pt.y, this.end_pt.x, this.end_pt.y);
			g.setPaintMode();

			//***** Draw the first resize rect
			resize_rect1.x = this.start_pt.x - 2;
			resize_rect1.y = this.start_pt.y - 2;
			resize_rect1.width = RESIZE_RECT_WIDTH;
			resize_rect1.height = RESIZE_RECT_HEIGHT;

			g.setColor(Color.yellow);
			g.fillRect(resize_rect1.x, resize_rect1.y, resize_rect1.width,
					   resize_rect1.height);

			//****** Draw the second resize rect
			resize_rect2.x = this.end_pt.x - 2;
			resize_rect2.y = this.end_pt.y - 2;
			resize_rect2.width = RESIZE_RECT_WIDTH;
			resize_rect2.height = RESIZE_RECT_HEIGHT;
			g.fillRect(resize_rect2.x, resize_rect2.y, resize_rect2.width,
					   resize_rect2.height);

			g.setColor(save_color);

			//***** Adjust the object rect to accomodate the resize rects
			this.object_rect.x -= 2;
			this.object_rect.y -= 2;
			this.object_rect.width += 4;
			this.object_rect.height += 4;

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

		Rectangle			resize_rect1 = new Rectangle(0,0,0,0);
		Rectangle			resize_rect2 = new Rectangle(0,0,0,0);
		Color				save_color = null;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			g.setPaintMode();
			if (on)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);

			g.drawLine(this.start_pt.x, this.start_pt.y, this.end_pt.x, this.end_pt.y);

			//***** Draw the first resize rect
			resize_rect1.x = this.start_pt.x - 2;
			resize_rect1.y = this.start_pt.y - 2;
			resize_rect1.width = RESIZE_RECT_WIDTH;
			resize_rect1.height = RESIZE_RECT_HEIGHT;

			g.setColor(Color.yellow);
			g.fillRect(resize_rect1.x, resize_rect1.y, resize_rect1.width,
					   resize_rect1.height);

			//****** Draw the second resize rect
			resize_rect2.x = this.end_pt.x - 2;
			resize_rect2.y = this.end_pt.y - 2;
			resize_rect2.width = RESIZE_RECT_WIDTH;
			resize_rect2.height = RESIZE_RECT_HEIGHT;
			g.fillRect(resize_rect2.x, resize_rect2.y, resize_rect2.width,
					   resize_rect2.height);

			g.setColor(save_color);
		}// if we're selecting

		return;

	}// end of flashObject()

	//********************************************************
	//*		G E T  L I N E  R E C T
	//*		Determines the rectangle which contains the line's
	//*		end points.
	//*
	//*		The 4's and 2's are to include the resize rects
	//********************************************************
	public void getLineRect()
	{
		int			width = 0, height = 0;

		//***** Get width
		width = this.end_pt.x - this.start_pt.x;
		if (width >= 0)
			this.object_rect.width = width;
		else
		{
			width = this.start_pt.x - this.end_pt.x;
			if (width >= 0)
				this.object_rect.width = width;
			else
				width = 2; // minimum width of 2
		}

		//***** Get height
		height = this.end_pt.y - this.start_pt.y;
		if (height >= 0)
			this.object_rect.height = height;
		else
		{
			height = this.start_pt.y - this.end_pt.y;
			if (height >=0)
				this.object_rect.height = height;
			else
				height = 2;// minimum height of 2
		}

		//***** Set starting point's x coordinate
		if (this.start_pt.x <= this.end_pt.x)
			this.object_rect.x = this.start_pt.x -2;
		else
			this.object_rect.x = this.end_pt.x -2;

		//***** Set the starting point's y coordinate
		if (this.start_pt.y <= this.end_pt.y)
			this.object_rect.y = this.start_pt.y - 2;
		else
			this.object_rect.y = this.end_pt.y - 2;

		return;

	}// end of getLineRect()


	//************************************************
	//*   I S  I D E N T I C A L  O B J E C T
	//************************************************
	public boolean	isIdenticalObject(AnnotationObject test_obj)
	{
		boolean			is_identical = false;
		LineObject		line_obj = null;

		is_identical = super.isIdenticalObject(test_obj);

		if (!is_identical)
			return(false);

		if (!(test_obj instanceof LineObject))
			return(false);

		line_obj = (LineObject)line_obj;

		if (start_pt.x != line_obj.start_pt.x ||
			start_pt.y != line_obj.start_pt.y ||
	 		end_pt.x != line_obj.end_pt.x ||
			end_pt.y != line_obj.end_pt.y)
			return(false);

		return(true);

	}// end of isIdenticalObject()


	//*********************************************************************
	//*		G E T  P R O X Y
	//*********************************************************************
	public AnnotationObjectProxy	getProxy()
	{
		return(new LineObjectProxy(this));
	}// end of getProxy()


	//*********************************************************************
	//*		G E T  O B J E C T  O P T I O N S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
  /*
	public void getObjectOptions(Frame parent) throws Exception
	{
		LineOptDlog		lod = null;

		//***** Get the pen options
 		lod = new LineOptDlog(Settings.editing_window, this, true);
 		if (lod != null)
 			lod.setVisible(true);

 		return;

	}// end of getObjectOptions()
  */


}// end of class LineObject

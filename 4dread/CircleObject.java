import java.awt.*;
import java.lang.*;
import java.io.*;
import java.util.*;

//**************************************************************************
//*		C I R C L E  O B J E C T
//**************************************************************************
public class CircleObject extends AnnotationObject
{


	protected	int			pen_width = 0;
	protected	boolean		filled = false;

	//***** Constructor method
	public CircleObject()
	{
		super();
		this.object_type = CIRCLE_OBJECT;
		this.pen_width = 0;
		this.object_color = EditingSettings.circle_color;
		this.filled = EditingSettings.circles_filled;

	}// end of CircleObject contstructor


	public CircleObject(int x, int y)
	{
		this();
		object_rect.x = x;
		object_rect.y = y;

		return;
	}

	public CircleObject(int x, int y, int width, int height)
	{
		this(x, y);
		object_rect.width = width;
		object_rect.height = height;

		return;
	}


	//************************************************
	//*		D R A W  O B J E C T
	//************************************************
	public void drawObject(Graphics g)
	{

		if (g == null)
			return;

		if (this.finished)
		{
			g.setPaintMode();
			g.setColor(this.object_color);
		}
		else if (!this.finished)
		{
			g.setXORMode(Color.white);
		}

		if (!this.filled || !this.finished)
		{
			g.drawOval(this.object_rect.x,
					   this.object_rect.y,
					   this.object_rect.width,
					   this.object_rect.height);
		}
		else
		{
			g.fillOval(this.object_rect.x,
					   this.object_rect.y,
					   this.object_rect.width,
					   this.object_rect.height);
		}

		g.setPaintMode();

		this.dirty = false;

		return;

	}// end of draw_object()


	//*******************************************************************
	//*		E D I T  O B J E C T
	//*******************************************************************
	public void editObject(Graphics g, int x, int y)
	{
		Point			start_pt = new Point(0,0);
		Point			mouse_pt = null;
		int				width = 0, height = 0;

		if (g == null)
			return;

		mouse_pt = new Point(x, y);

		drawObject(g);// get rid of the old one

		start_pt.x = object_rect.x;
		start_pt.y = object_rect.y;
		width = mouse_pt.x - start_pt.x;
		if (width >= 0)
			object_rect.width = width;
		height = mouse_pt.y - start_pt.y;
		if (height >= 0)
			object_rect.height = height;

		drawObject(g);

		return;

	}// end of editObject



	//*******************************************************************
	//*		F I N I S H O B J E C T
	//*******************************************************************
	public void finishObject(Graphics g, int x, int y)
	{
		Point				start_pt = new Point(0,0);
		Point				mouse_pt = new Point(x,y);
		int					width = 0, height = 0;

		drawObject(g);// get rid of the old one

		start_pt.x = object_rect.x;
		start_pt.y = object_rect.y;
		width = mouse_pt.x - start_pt.x;
		if (width >= 0)
			object_rect.width = width;
		height = mouse_pt.y - start_pt.y;
		if (height >= 0)
			object_rect.height = height;

		finished = true;

		drawObject(g); // draw the new one

		return;

	}// end of finishCircleObject()


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


	//****************************************************************
	//*		C L I C K  I N  R E S I Z E  R E C T
	//*		Returns true if the click was in the object's resize rect
	//*		and the object was selected
	//****************************************************************
	public boolean clickInResizeRect(int x, int y)
	{
		Rectangle	resize_rect = new Rectangle(0,0,0,0);


		resize_rect.x = this.object_rect.x + this.object_rect.width - RESIZE_RECT_WIDTH;
		resize_rect.y = this.object_rect.y + this.object_rect.height - RESIZE_RECT_HEIGHT;
		resize_rect.width = RESIZE_RECT_WIDTH;
		resize_rect.height = RESIZE_RECT_HEIGHT;

		if (resize_rect.contains(x, y) && this.selected)
			return(true);
		else
			return(false);

	}// end of clickInResizeRect()


	//************************************************
	//*		D R A G  O B J E C T
	//************************************************
  /*
	public void dragObject(EditingCanvas ec, int x, int y)
	{
		int					x_offset = 0;
		int					y_offset = 0;
		Graphics			g = ec.getGraphics();

		ec.clearObject(this.object_rect); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		this.object_rect.x += x_offset;
		this.object_rect.y += y_offset;

		this.drawObject(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		g.dispose();// get rid of the graphics context

		return;

	}// end of dragObject()
  */


	//************************************************
	//*		R E S I Z E  O B J E C T
	//************************************************
  /*
	public void resizeObject(EditingCanvas ec, int x, int y)
	{
		int					width = 0, height = 0;
		Graphics			g = ec.getGraphics();

		if (this.selected || this.finished)
			ec.clearObject(this.object_rect); // get rid of the old one
		else
			this.drawObject(g);// get rid of the old one

		width = x - this.object_rect.x;
		if (width > (0 + RESIZE_RECT_WIDTH))
			this.object_rect.width = width;

		height = y - this.object_rect.y;
		if (height > (0 + RESIZE_RECT_HEIGHT))
			this.object_rect.height = height;

		this.drawObject(g);// draw the new one

		g.dispose();// get rid of graphics context

		return;

	}// end of resizeObject()
  */


	//************************************************
	//*		H A N D L E  C L I C K  O N  O B J E C T
	//********************************************
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
	//************************************************************
  /*
	public boolean checkSelect(EditingCanvas ec, int x, int y)
	{

		if (x == first_click.x && y == first_click.y)
		{
			Graphics		g = ec.getGraphics();

			this.selected = !this.selected;
			if (!this.selected)
			{
				ec.clearObject(this.object_rect);
				this.drawObject(g); // draw new one
			}// if we deselected

			g.dispose();

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

		Rectangle			resize_rect = new Rectangle(0,0,0,0);
		Color				save_color = null;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			g.setPaintMode();
			g.setColor(Color.black);
			g.drawOval(this.object_rect.x, this.object_rect.y,
					   this.object_rect.width, this.object_rect.height);// make a black version

			g.setXORMode(Color.white);
			g.drawOval(this.object_rect.x, this.object_rect.y,
					   this.object_rect.width, this.object_rect.height);
			g.setPaintMode();

			resize_rect.x = this.object_rect.x + this.object_rect.width - 4;
			resize_rect.y = this.object_rect.y + this.object_rect.height - 4;
			resize_rect.width = 4;
			resize_rect.height = 4;

			g.setColor(Color.yellow);
			g.fillRect(resize_rect.x, resize_rect.y, resize_rect.width, resize_rect.height);

			g.setColor(save_color);
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

		Rectangle			resize_rect = new Rectangle(0,0,0,0);
		Color				save_color = null;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			g.setPaintMode();
			if (on)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);
			g.drawOval(this.object_rect.x, this.object_rect.y,
					   this.object_rect.width, this.object_rect.height);// make a background version

			resize_rect.x = this.object_rect.x + this.object_rect.width - 4;
			resize_rect.y = this.object_rect.y + this.object_rect.height - 4;
			resize_rect.width = 4;
			resize_rect.height = 4;

			g.setColor(Color.yellow);
			g.fillRect(resize_rect.x, resize_rect.y, resize_rect.width, resize_rect.height);

			g.setColor(save_color);
		}// if we're selecting

		return;

	}// end of flashObject()


	//*******************************************************************
	//*		I S  V A L I D  O B J E C T
	//*		Retuns false if object fails tests for validity
	//*******************************************************************
	public boolean  isValidObject()
	{
		if (object_rect.width <= 0 || object_rect.height <= 0)
			return(false);

		return(true);

	}// end of isValidObject()


	//************************************************
	//*   I S  I D E N T I C A L  O B J E C T
	//************************************************
	public boolean	isIdenticalObject(AnnotationObject test_obj)
	{
		boolean is_identical = false;

		is_identical = super.isIdenticalObject(test_obj);

		if (!is_identical)
			return(false);

		if (!(test_obj instanceof CircleObject))
			return(false);

		return(true);

	}// end of isIdenticalObject()


	//*********************************************************************
	//*		G E T  P R O X Y
	//*********************************************************************
	public AnnotationObjectProxy	getProxy()
	{
		return(new CircleObjectProxy(this));
	}// end of getProxy()


	//*********************************************************************
	//*		G E T  O B J E C T  O P T I O N S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
  /*
	public void getObjectOptions(Frame parent) throws Exception
	{
		CircleOptDlog		cod = null;

 		//***** Get the pen options
 		cod = new CircleOptDlog(Settings.editing_window, this, true);
 		if (cod != null)
 			cod.setVisible(true);

 		return;

	}// end of getObjectOptions()

  */

}// end of class CircleObject

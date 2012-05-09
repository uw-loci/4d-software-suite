import java.awt.*;
import java.lang.*;
import java.io.*;
import java.util.*;

//**************************************************************************
//*		A R R O W  O B J E C T
//**************************************************************************
public class ArrowObject extends AnnotationObject
{
	protected	Point				start_pt = null; // the tip of the arrow
	protected	Point				end_pt = null; // the end of the arrow
	protected	boolean			filled = false; // is it an outline, or is it filled
	protected	boolean			resized = false; // should we determine new length after resize?

	//***** Creator method
	public ArrowObject()
	{
		super();

		this.setObjectType(ARROW_OBJECT);

		this.start_pt = new Point(0,0);
		this.end_pt = new Point(0,0);
		this.object_color = EditingSettings.arrow_color;
		this.filled = EditingSettings.arrows_filled;
		this.resized = false;

	}// end of ArrowObject constructor


	public ArrowObject(int x, int y)
	{
		this();
		this.start_pt.x = x;
		this.start_pt.y = y;
		this.end_pt.x = x;
		this.end_pt.y = y;
		this.last_click.x = x;
		this.last_click.y = y;

		return;
	}

	//************************************************
	//*		D R A W  O B J E C T
	//************************************************
	public void drawObject(Graphics g)
	{
		if (g == null)
			return;

		if (this.getFinished())
		{
			g.setPaintMode();
			g.setColor(this.object_color);
		}
		else if (!this.finished)
		{
			g.setXORMode(Color.white);
		}

		//***** Draw the arrow
		drawAnArrow(g);

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

		drawObject(g);

		return;

	}// end of editObject


	//*******************************************************************
	//*		F I N I S H  O B J E C T
	//*******************************************************************
	public void finishObject(Graphics g, int x, int y)
	{
		drawObject(g); // get rid of the old one

		end_pt.x = x;
		end_pt.y = y;
		finished = true;
		drawObject(g); // draw the new one

		return;

	}// end of finishArrowObject()


	//****************************************************************
	//*		D R A W  A N  A R R O W
	//****************************************************************
	public void drawAnArrow (Graphics g)
	{

		Polygon 		arrow_poly = new Polygon();
		Polygon 		shaft_poly = new Polygon();
		double			arrow_orienation = 0.0;
		double			arrow_length = 0.0;
		Point			draw_pt1 = null, draw_pt2 = null;
		Point			draw_pt3 = null, draw_pt4 = null;
		Point			draw_pt5 = null, draw_pt6 = null;
		Rectangle		poly_rect = null;


		//***** Get the length and orientation (direction it's pointing) of the arrow
		arrow_length = getLength();
		arrow_orienation = getOrientation();

		arrow_poly.addPoint(start_pt.x, start_pt.y);

		//***** Draw the line from the point to one side of the arrowhead
		//draw_pt1 = getPoint(arrow_length * 0.30, (arrow_orienation + 30) % 360, this.start_pt);
		draw_pt1 = getPoint(arrow_length, (arrow_orienation + 12) % 360, this.start_pt);
		g.drawLine(this.start_pt.x, this.start_pt.y, draw_pt1.x, draw_pt1.y);
		arrow_poly.addPoint(draw_pt1.x, draw_pt1.y);

		//***** Draw the line from the point to the other side of the arrowhead
		//draw_pt2 = getPoint(arrow_length * 0.30, (arrow_orienation + 330) % 360, this.start_pt);
		draw_pt2 = getPoint(arrow_length, (arrow_orienation + 348) % 360, this.start_pt);
		g.drawLine(this.start_pt.x, this.start_pt.y, draw_pt2.x, draw_pt2.y);
		arrow_poly.addPoint(draw_pt2.x, draw_pt2.y);

		g.drawLine(draw_pt1.x, draw_pt1.y, draw_pt2.x, draw_pt2.y);
		arrow_poly.addPoint(draw_pt2.x, draw_pt2.y);

		//***** Connect the two non-tip points of the arrowhead and fill
		if (this.filled)
		{
			g.drawLine(draw_pt1.x, draw_pt1.y,draw_pt2.x, draw_pt2.y);
			g.fillPolygon(arrow_poly);
		}

		//***** Get the object's bounding rectangle
		if (this.resized || !this.finished)
		{
			poly_rect = arrow_poly.getBounds();
			this.object_rect.x = poly_rect.x - 1;
			this.object_rect.y = poly_rect.y - 1;
			this.object_rect.width = poly_rect.width + 2;
			this.object_rect.height = poly_rect.height + 2;
			this.resized = false;
		}// if we should determine the bounding rect

		return;

	}// end of drawAnArrow()




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
	public void dragObject(EditingCanvas ec, int x, int y)
	{
		int				x_offset = 0;
		int				y_offset = 0;
		Graphics		g = ec.getGraphics();

		if (g == null)
			return;

		ec.clearObject(this.object_rect); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		//***** Offset the start point
		this.start_pt.x += x_offset;
		this.start_pt.y += y_offset;
		this.end_pt.x += x_offset;
		this.end_pt.y += y_offset;

		//***** Offset the bounding rectangle
		this.object_rect.x += x_offset;
		this.object_rect.y += y_offset;

		this.drawObject(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		g.dispose();

		return;

	}// end of dragObject()


	//************************************************
	//*		R E S I Z E  O B J E C T
	//************************************************
	public void resizeObject(EditingCanvas ec, int x, int y)
	{
		/*
		int			width = 0, height = 0;
		Graphics	g = ec.getGraphics();

		ec.clearObject(this.object_rect); // get rid of the old  object

		width = x - this.object_rect.x;
		if (width > (0 + RESIZE_RECT_WIDTH))
			this.object_rect.width = width;

		height = y - this.object_rect.y;
		if (height > (0 + RESIZE_RECT_HEIGHT))
			this.object_rect.height = height;

		//***** Reset the start point to be half of the box's width or height
		if (arrow_orienation == UP || arrow_orienation == DOWN)
			this.start_pt.x = this.object_rect.x + (this.object_rect.width/2);
		else
			this.start_pt.y = this.object_rect.y + (this.object_rect.height/2);

		this.selectObject(g);// draw the new box
		this.resized = true; // tell draw routine to calculate new box

		g.dispose();
		*/
		return;

	}// end of resizeObject()


	//******************************************************
	//*		H A N D L E  C L I C K  O N  O B J E C T
	//*		Handles mouse down events in completed objects
	//******************************************************
	public void handleClickOnObject(int x, int y)
	{
		this.last_click.x = x;
		this.last_click.y = y;

		first_click.x = x;
		first_click.y = y;

		return;

	}// end of handleClickOnObject()


	//************************************************************
	//*		C H E C K  S E L E C T
	//* 	Checks to see if select status needs to be changed
	//*		Returns TRUE if the status was changed
	//************************************************************
	public boolean checkSelect(EditingCanvas ec, int x, int y)
	{
		boolean 	changed = false;

		if (x == first_click.x && y == first_click.y)
		{
			Graphics		g = ec.getGraphics();

			this.selected = !this.selected;
			if (!this.selected)
			{
				ec.clearObject(this.object_rect);
				this.drawObject(g); // draw new one
			}
			g.dispose();

			return(true);
		}
		else
			return(false);

	}// end of checkSelect()


	//************************************************
	//*		S E L E C T  O B J E C T
	//********************************************
	public void selectObject(Graphics g)
	{

		Rectangle		resize_rect = new Rectangle(0,0,0,0);
		Color			save_color = null;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			//***** Draw the outline
			g.setXORMode(Color.white);
			g.drawRect(this.object_rect.x, this.object_rect.y,
					   this.object_rect.width, this.object_rect.height);
			g.setPaintMode();
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

		Rectangle		resize_rect = new Rectangle(0,0,0,0);
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
			g.setPaintMode();
		}// if we're selecting

		return;

	}// end of flashObject()


	//*******************************************************************
	//*		I S  V A L I D  O B J E C T
	//*		Retuns false if object fails tests for validity
	//*******************************************************************
	public boolean  isValidObject()
	{
		if (getLength() <= 1.0)
			return(false);

		return(true);

	}// end of isValidObject()


	//************************************************
	//*   I S  I D E N T I C A L  O B J E C T
	//************************************************
	public boolean	isIdenticalObject(AnnotationObject test_obj)
	{
		boolean			is_identical = false;
		ArrowObject		arrow_obj = null;

		is_identical = super.isIdenticalObject(test_obj);

		if (!is_identical)
			return(false);

		if (!(test_obj instanceof ArrowObject))
			return(false);

		arrow_obj = (ArrowObject)test_obj;

		if (start_pt.x != arrow_obj.start_pt.x ||
			start_pt.y != arrow_obj.start_pt.y ||
			end_pt.x != arrow_obj.end_pt.x ||
			end_pt.y != arrow_obj.end_pt.y ||
			filled != arrow_obj.filled)
			return(false);

		return(true);

	}// end of isIdenticalObject()


	//*********************************************************************
	//*		G E T  P R O X Y
	//*********************************************************************
	public AnnotationObjectProxy	getProxy()
	{
		return(new ArrowObjectProxy(this));
	}// end of getProxy()


	//*********************************************************************
	//*		G E T  P O I N T
	//*********************************************************************
	public Point	getPoint(double length, double vector, Point origin)
	{
		Point		final_point = new Point(0,0);
		double		angle = 0;
		double		radians = 0;
		int			i = 0;

		angle = -vector;// + 90;
		if (angle < 0)
			angle += 360;

		radians = angle/(360/(2 * Math.PI)); // convert angle degrees to radians

		final_point.x = (int)(origin.x + (length * Math.sin(radians))); // was cos
		final_point.y = (int)(origin.y + (length * Math.cos(radians))); // was sin

		return(final_point);

	}// end of getPoint()



	//*********************************************************************
	//*		G E T  O R I E N T A T I O N
	//*		Orientation in this case refers to the direction the arrow will point
	//*		given that the arrowhead is always at the start_pt.
	//*
	//*		e.g. if the start_pt is at 10,20 and the end_pt is at 10,10, the line is being drawn
	//*		from south to north, but the arrowhead is pointing south, so the orientation is "180"
	//*********************************************************************
	public double  getOrientation()
	{
		double		t_angle = 0.0;
		int			delta_x = 0, delta_y = 0;
		double		length = 0.0;
		double		angle_in_radians = 0.0, y_over_length = 0.0;
		double		arrow_orienation = 0.0;

		delta_x = end_pt.x - start_pt.x;
		delta_y = end_pt.y - start_pt.y;
		length = getLength();

		if (length > 0.0)
		{
			y_over_length = (double)delta_y/(double)length;
			angle_in_radians = Math.asin(y_over_length);
			t_angle = angle_in_radians * (180/Math.PI);

			if (delta_x < 0)
				t_angle = 180.0 - t_angle;
			else if (delta_y < 0)
				t_angle += 360.0;
			arrow_orienation = t_angle - 90.0;

		}// if length is greater than 0
		else
			arrow_orienation = 0.0;

		return(arrow_orienation);

	}// end of getOrientation()


	//*********************************************************************
	//*		G E T  L E N G T H
	//*********************************************************************
	public double  getLength()
	{
		double	length = 0.0;
		int		delta_x = 0, delta_y = 0;

		delta_x = end_pt.x - start_pt.x;
		delta_y = end_pt.y - start_pt.y;
		length = Math.sqrt(Math.pow(delta_x, 2)  + Math.pow(delta_y, 2));

		return(length);

	}// end of getLength()


	//*********************************************************************
	//*		G E T  O B J E C T  O P T I O N S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public void getObjectOptions(Frame parent) throws Exception
	{
		ArrowOptDlog	aod = null;

		aod = new ArrowOptDlog(Settings.editing_window, this, true);
 		if (aod != null)
 			aod.setVisible(true);

 		return;

	}// end of getObjectOptions()


}// end of class ArrowObject

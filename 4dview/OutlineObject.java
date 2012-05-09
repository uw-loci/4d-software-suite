import java.awt.*;
import java.lang.*;
import java.io.*;
import java.util.*;

//**************************************************************************
//*		O U T L I N E  O B J E C T
//**************************************************************************
public class OutlineObject extends AnnotationObject
{

	//***** Class variables
	protected	int			pen_width = 0;
	protected	int			num_points = 0;
	protected	Vector		point_array = null;


	//***** Creator method
	public OutlineObject()
	{
		super();
		this.object_type = OUTLINE_OBJECT;
		this.pen_width = 0;
	 	this.num_points = 0;
	 	this.point_array = new Vector(10,1);
		this.object_color = EditingSettings.outline_color;

	}// end of OutlineObject constructor

	public OutlineObject(int x, int y)
	{
		this();
		this.point_array.addElement(new Point(x, y));
		this.num_points = 1;
		return;
	}

	public void addPoint(Point p)
	{
		point_array.addElement(p);
		num_points += 1;
	}

	//************************************************
	//*		D R A W  O B J E C T
	//************************************************
	public void drawObject(Graphics g)
	{
		Point			first_pt = null;
		Point			next_pt = null;
		Point			cur_pt = new Point(0,0);
		Point			end_pt = new Point(0,0);
		int				i = 0;

		if (g == null)
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

		first_pt = (Point)this.point_array.elementAt(0);
		cur_pt.x = first_pt.x;
		cur_pt.y = first_pt.y;

		for (i = 0; i < this.num_points; i++)
		{
			next_pt = (Point)this.point_array.elementAt(i);
			end_pt.x = next_pt.x;
			end_pt.y = next_pt.y;

			g.drawLine(cur_pt.x,
				       cur_pt.y,
				       end_pt.x,
				       end_pt.y);
			cur_pt.x = end_pt.x;
			cur_pt.y = end_pt.y;
		}

		g.setPaintMode(); // return paint mode to normal

		this.dirty = false;

		return;

	}// end of draw_object()


	//*******************************************************************
	//*		E D I T  O B J E C T
	//*******************************************************************
	public void editObject(Graphics g, int x, int y)
	{
		if (g == null)
			return;

		point_array.addElement(new Point(x, y));
		num_points += 1;

		getOutlineRect();
		drawObject(g);

		return;

	}// end of editObject()


	//*******************************************************************
	//*		F I N I S H  O B J E C T
	//*******************************************************************
	public void finishObject(Graphics g, int x, int y)
	{

		if (g != null)
		{
			drawObject(g);// get rid of the old one

			point_array.addElement(new Point(x,y));
			num_points += 1;
			getOutlineRect();
			finished = true;

			drawObject(g); // draw the new one
		}

		return;

	}// end of finishOutlineObject()




	//*******************************************************************
	//*		I S  V A L I D  O B J E C T
	//*		Retuns false if object fails tests for validity
	//*******************************************************************
	public boolean  isValidObject()
	{
		if (num_points <= 1)
			return(false);

		return(true);

	}// end of isValidObject()


	//**************************************************************
	//*		C L I C K  I N  O B J E C T
	//*		Returns true if the click was in the object's boundary
	//**************************************************************
	public boolean clickInObject(int x, int y)
	{
		int		i = 0;
		Point	new_pt = null;

		for (i = 0; i < this.num_points; i++)
		{

			new_pt = (Point)this.point_array.elementAt(i);

			//***** Find out if the ecdow click was within three pixels of the outline
			//***** If so, say that we clicked on the outline.
			if ((x >= new_pt.x - 3) && (x <= new_pt.x + 3) &&
				(y >= new_pt.y - 3) && (y <= new_pt.y + 3))
			{
				return(true);
			}

		}// end of for

		return (false);

	}// end of clickInObject()


	//************************************************
	//*		D R A G  O B J E C T
	//************************************************
	public void dragObject(EditingCanvas ec, int x, int y)
	{
		int				x_offset = 0;
		int				y_offset = 0;
		int				i = 0;
		Point			cur_pt = null;
		Graphics		g = ec.getGraphics();

		if (g == null)
			return;

		ec.clearObject(this.object_rect); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		//***** Change the offset for all points
		for (i = 0; i < num_points; i++)
		{
			cur_pt = (Point)this.point_array.elementAt(i);
			cur_pt.x += x_offset;
			cur_pt.y += y_offset;
		}// for each point

		this.getOutlineRect();// find the new bounding rectangle
		this.drawObject(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		g.dispose();// get rid of graphics context

		return;

	}// end of dragObject()


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
	//*		Returns TRUE if we changed status
	//************************************************************
	public boolean checkSelect(EditingCanvas ec, int x, int y)
	{
		if (x == first_click.x && y == first_click.y)
		{
			Graphics g = ec.getGraphics();

			this.selected = !this.selected;
			if (!this.selected && g != null)
			{
				ec.clearObject(this.object_rect);
				this.drawObject(g); // draw new one
				g.dispose();// get rid of graphics context
			}

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
		Point		cur_pt = new Point(0,0);
		Point		end_pt = new Point(0,0);
		Point		first_pt = null, next_pt = null;
		Color		save_color = null;
		int			i = 0;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			g.setPaintMode();
			g.setColor(Color.black);

			//***** Draw a black version
			first_pt = (Point)this.point_array.elementAt(0);
			cur_pt.x = first_pt.x;
			cur_pt.y = first_pt.y;

			for (i = 0; i < this.num_points; i++)
			{
				next_pt = (Point)this.point_array.elementAt(i);
				end_pt.x = next_pt.x;
				end_pt.y = next_pt.y;

				g.drawLine(cur_pt.x, cur_pt.y, end_pt.x, end_pt.y);
				cur_pt.x = end_pt.x;
				cur_pt.y = end_pt.y;
			}

			g.setXORMode(Color.white); // change pen to XOr mode

			//***** Draw the selected version
			first_pt = (Point)this.point_array.elementAt(0);
			cur_pt.x = first_pt.x;
			cur_pt.y = first_pt.y;

			for (i = 0; i < this.num_points; i++)
			{
				next_pt = (Point)this.point_array.elementAt(i);
				end_pt.x = next_pt.x;
				end_pt.y = next_pt.y;

				g.drawLine(cur_pt.x, cur_pt.y, end_pt.x, end_pt.y);
				cur_pt.x = end_pt.x;
				cur_pt.y = end_pt.y;
			}

			g.setPaintMode();// reset the pen

			this.selected = true;

			g.setColor(save_color);

		}// if we're selecting

		return;

	}// end of select_object()



	//************************************************
	//*		F L A S H  O B J E C T
	//*		If on, the object flashes white
	//*		if !on, the object flashes black
	//********************************************
	public void flashObject(Graphics g, boolean on)
	{

		Point		cur_pt = new Point(0,0);
		Point		end_pt = new Point(0,0);
		Point		first_pt = null, next_pt = null;
		Color		save_color = null;
		int			i = 0;

		if (this.selected && g != null)
		{
			save_color = g.getColor();

			g.setPaintMode();
			if (on)
				g.setColor(Color.black);
			else
				g.setColor(Color.white);

			first_pt = (Point)this.point_array.elementAt(0);
			cur_pt.x = first_pt.x;
			cur_pt.y = first_pt.y;

			for (i = 0; i < this.num_points; i++)
			{
				next_pt = (Point)this.point_array.elementAt(i);
				end_pt.x = next_pt.x;
				end_pt.y = next_pt.y;

				g.drawLine(cur_pt.x, cur_pt.y, end_pt.x, end_pt.y);
				cur_pt.x = end_pt.x;
				cur_pt.y = end_pt.y;
			}

			g.setColor(save_color);

		}// if we're selecting

		return;

	}// end of flashObject()


	//**********************************************************
	//*		G E T  O U T L I N E  R E C T
	//**********************************************************
	public void getOutlineRect()
	{
		int			sm_x = 32000, sm_y = 32000;
		int			bg_x = -1, bg_y = -1;
		int			i = 0;
		Point		cur_pt = null;

		for (i = 0; i < this.num_points; i++)
		{
			cur_pt = (Point)this.point_array.elementAt(i);

			//***** Find the smallest values of x and y
			if (cur_pt.x < sm_x)
				sm_x = cur_pt.x;
			if (cur_pt.y < sm_y)
				sm_y = cur_pt.y;

			//***** Find the biggest values of x and y
			if (cur_pt.x > bg_x)
				bg_x = cur_pt.x;
			if (cur_pt.y > bg_y)
				bg_y = cur_pt.y;

		}

		this.object_rect.x = sm_x - 1;
		this.object_rect.y = sm_y -1;
		this.object_rect.width = (bg_x - sm_x) +1;
		this.object_rect.height = (bg_y - sm_y) +1;

		return;

	}// end of getOutlineRect()


	//************************************************
	//*   I S  I D E N T I C A L  O B J E C T
	//************************************************
	public boolean	isIdenticalObject(AnnotationObject test_obj)
	{
		boolean			is_identical = false;
		OutlineObject	outline_obj = null;
		int				i = 0;
		Point			this_point = null, test_point = null;

		is_identical = super.isIdenticalObject(test_obj);

		if (!is_identical)
			return(false);

		if (!(test_obj instanceof OutlineObject))
			return(false);

		outline_obj = (OutlineObject)test_obj;

		if (num_points != outline_obj.num_points)
			return(false);

		try
		{
			for (i = 0; i < point_array.size(); i++)
			{
				this_point = (Point)point_array.elementAt(i);
				test_point = (Point)outline_obj.point_array.elementAt(i);
				if (this_point.x != test_point.x ||
					this_point.y != test_point.y)
						return(false);
			}
		}
		catch (Exception e)
		{
			return(false);
		}
		return(true);

	}// end of isIdenticalObject()


	//*********************************************************************
	//*		G E T  P R O X Y
	//*********************************************************************
	public AnnotationObjectProxy	getProxy()
	{
		return(new OutlineObjectProxy(this));
	}// end of getProxy()


	//*********************************************************************
	//*		G E T  O B J E C T  O P T I O N S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public void getObjectOptions(Frame parent) throws Exception
	{
		OutlineOptDlog		ood = null;

 		//***** Get the pen options
 		ood = new OutlineOptDlog(Settings.editing_window, this, true);
 		if (ood != null)
 			ood.setVisible(true);

 		return;

	}// end of getObjectOptions()


}// end of class OutlineObject

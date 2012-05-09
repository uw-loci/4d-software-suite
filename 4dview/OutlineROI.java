import java.awt.*;
import java.util.*;

class OutlineROI extends LineROI
{

	protected	Vector		point_array = null;

	public OutlineROI(EditingCanvas ec)
	{
		super(ec);
		this.setROIType(this.OUTLINE_ROI);
	 	this.point_array = new Vector(10,1);

	}// init


	//************************************************
	//*		A D D  P O I N T
	//************************************************
	public void addPoint(Point p)
	{
		point_array.addElement(p);
	}

	//************************************************
	//*		D R A W  R O I
	//*		Modified 9/6/02 for v1.62
	//************************************************
	public void drawROI(Graphics g) throws Exception
	{

		Point			first_pt = null;
		Point			next_pt = null;
		Point			cur_pt = new Point(0,0);
		Point			end_pt = new Point(0,0);
		int			i = 0;

		if (g == null)
			throw new Exception("null Graphics object in drawROI()");

		g.setXORMode(Color.white);

		first_pt = (Point)this.point_array.elementAt(0);
		cur_pt.x = first_pt.x;
		cur_pt.y = first_pt.y;

		for (i = 0; i < point_array.size(); i++)
		{
			next_pt = (Point)this.point_array.elementAt(i);
			end_pt.x = next_pt.x;
			end_pt.y = next_pt.y;

			g.drawLine(cur_pt.x,  cur_pt.y,  end_pt.x,  end_pt.y);
			cur_pt.x = end_pt.x;
			cur_pt.y = end_pt.y;
		}

		g.setPaintMode();

		return;

	}// end of drawROI()


	//*******************************************************************
	//*		E D I T  R O I
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void editROI(Graphics g, int x, int y) throws Exception
	{
		if (g == null)
			throw new Exception("null Graphics object in editROI()");

		point_array.addElement(new Point(x, y));

		getBoundingRect(); // determine the object's bounding rectangle

		drawROI(g);

		return;

	}// end of editROI


	//*******************************************************************
	//*		F I N I S H  R O I
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void finishROI(int x, int y) throws Exception
	{

		point_array.addElement(new Point(x,y));
		getBoundingRect();
		measureROI();

		return;

	}// end of finishROI()


	//************************************************
	//*		D R A G  R O I
	//*		Modified 9/5/02 for v1.62
	//************************************************
	public void dragROI(Graphics g, int x, int y) throws Exception
	{
		int				x_offset = 0;
		int				y_offset = 0;
		int				i = 0;
		Point				cur_pt = null;

		if (g == null)
			throw new Exception("null Graphics object in dragROI()");

		ec.clearObject(this.bounding_rect); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		//***** Change the offset for all points
		for (i = 0; i < point_array.size(); i++)
		{
			cur_pt = (Point)point_array.elementAt(i);
			cur_pt.x += x_offset;
			cur_pt.y += y_offset;
		}// for each point

		this.getBoundingRect();// find the new bounding rectangle
		this.drawROI(g);// draw the new one
		this.last_click.x = x;
		this.last_click.y = y;

		return;

	}// end of dragROI()


	//**********************************************************************
	//*		G E T  B O U N D I N G  R E C T
	//*		Returns the the object's bounding rect -
	//*		a rect that is one pixel bigger than the object's rect
	//*		Modified 9/5/02 for v1.62
	//**********************************************************************
	public Rectangle getBoundingRect() throws Exception
	{
		int			sm_x = 32000, sm_y = 32000;
		int			bg_x = -1, bg_y = -1;
		int			i = 0;
		Point		cur_pt = null;

		for (i = 0; i < point_array.size(); i++)
		{
			cur_pt = (Point)point_array.elementAt(i);

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

		bounding_rect.x = sm_x - 1;
		bounding_rect.y = sm_y -1;
		bounding_rect.width = (bg_x - sm_x) +2;
		bounding_rect.height = (bg_y - sm_y) +2;

		return(new Rectangle(bounding_rect));

	}// end of getBoundingRect();


	//********************************************************************************
	//*		M E A S U R E  R O I
	//*		Modified 9/5/02 for v1.62
	//********************************************************************************
	public void	measureROI()
	{
		String	num_string = null;
		double	length = 0.0;
		Double	d = null;


		try
		{
			length = getOutlineLength();
			d = new Double(length);
		}
		catch (Exception e)
		{
			ec.displayMessage("Unable to get ROI length.");
		}

		//***** Output the length
		ec.displayMessage("Length: " + d.toString() + " " + EditingSettings.units_string);

		return;

	}// end of measureROI()


	//********************************************************************
	//*		G E T  O U T L I N E  L E N G T H
	//*		Returns the length of an outline
	//********************************************************************
	 double	getOutlineLength() throws Exception
	{

		Point			pt0 = null, pt1 = null;
		double 		result = 0.0;
		int			i = 0;

		if (point_array == null)
			throw new Exception("Point vector is invalid.");


		for (i = 0; i < point_array.size() - 1; i++)
		{
			pt0 = (Point)point_array.elementAt(i);
			pt1 = (Point)point_array.elementAt(i + 1);
			result += getLineLength(pt0, pt1);
		}// for each pair of points

		return(result);

	}// end of getOutineLength

}// end of class OutlineROI

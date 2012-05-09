import java.awt.*;

class LineROI extends ROIObject
{
	protected	Point 		start_pt = null, end_pt = null; // for line ROI's

	public LineROI(EditingCanvas ec)
	{
		super(ec);
		this.start_pt = new Point(0,0);
		this.end_pt = new Point(0,0);
		this.setROIType(this.LINE_ROI);

		return;
	}// init


	public LineROI(EditingCanvas ec, Point start_pt, Point end_pt)
	{
		this(ec);
		this.start_pt = new Point(start_pt);
		this.end_pt = new Point(end_pt);

		return;
	}



	//************************************************
	//*		D R A W  R O I
	//*		Modified 9/6/02 for v1.62
	//************************************************
	public void drawROI(Graphics g) throws Exception
	{
		if (g == null)
			throw new Exception("null Graphics object in drawROI()");

		g.setXORMode(Color.white);

		g.drawLine(this.start_pt.x,
				   this.start_pt.y,
				   this.end_pt.x,
				   this.end_pt.y);

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

		drawROI(g); // get rid of the old one

		end_pt.x = x;
		end_pt.y = y;
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

		end_pt.x = x;
		end_pt.y = y;
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

		if (g == null)
			return;

		ec.clearObject(getROIRect()); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		bounding_rect.x += x_offset;
		bounding_rect.y += y_offset;

		this.start_pt.x += x_offset;
		this.end_pt.x += x_offset;
		this.start_pt.y += y_offset;
		this.end_pt.y += y_offset;

		drawROI(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		return;

	}// end of dragROI()


	//**********************************************************************
	//*		G E T  R O I  R E C T
	//*		Returns the the object's rect, differs from bounding rect below
	//**********************************************************************
	public Rectangle getROIRect()
	{
		int			width = 0, height = 0;
		Rectangle		rect = new Rectangle(0,0,0,0);

		//***** Get width
		width = this.end_pt.x - this.start_pt.x;
		if (width >= 0)
			rect.width = width;
		else
		{
			width = this.start_pt.x - this.end_pt.x;
			if (width >= 0)
				rect.width = width;
		}

		//***** Get height
		height = this.end_pt.y - this.start_pt.y;
		if (height >= 0)
			rect.height = height;
		else
		{
			height = this.start_pt.y - this.end_pt.y;
			if (height >= 0)
				rect.height = height;
		}


		//***** Set  x coordinate
		if (this.start_pt.x <= this.end_pt.x)
			rect.x = this.start_pt.x;
		else
			rect.x = this.end_pt.x;

		//***** Set the y coordinate
		if (this.start_pt.y <= this.end_pt.y)
			rect.y = this.start_pt.y;
		else
			rect.y = this.end_pt.y;


		return(rect);

	}// end of getROIRect()


	//**********************************************************************
	//*		G E T  B O U N D I N G  R E C T
	//*		Returns the the object's bounding rect -
	//*		a rect that is one pixel bigger than the object's rect
	//*
	//*		Modified 9/5/02 for v1.62
	//**********************************************************************
	public Rectangle getBoundingRect() throws Exception
	{
		int			width = 0, height = 0;

		//***** Get width
		width = this.end_pt.x - this.start_pt.x;
		if (width >= 0)
			this.bounding_rect.width = width;
		else
		{
			width = this.start_pt.x - this.end_pt.x;
			if (width >= 0)
				this.bounding_rect.width = width;
		}

		//***** Get height
		height = this.end_pt.y - this.start_pt.y;
		if (height >= 0)
			this.bounding_rect.height = height;
		else
		{
			height = this.start_pt.y - this.end_pt.y;
			if (height >= 0)
				this.bounding_rect.height = height;
		}


		//***** Set  x coordinate
		if (this.start_pt.x <= this.end_pt.x)
			this.bounding_rect.x = this.start_pt.x;
		else
			this.bounding_rect.x = this.end_pt.x;

		//***** Set the y coordinate
		if (this.start_pt.y <= this.end_pt.y)
			this.bounding_rect.y = this.start_pt.y;
		else
			this.bounding_rect.y = this.end_pt.y;

		return(new Rectangle(bounding_rect));

	}// end of getBoundingRect();


	//********************************************************************************
	//*		M E A S U R E  R O I
	//*		Modified 9/5/02 for v1.62
	//********************************************************************************
	public void	measureROI()
	{
		String	num_string = null;

		try
		{
			num_string = getLineLengthString(start_pt, end_pt, EditingSettings.multiplier);

			//***** Output the length
			ec.displayMessage("Length: " + num_string + " " + EditingSettings.units_string);
		}
		catch (Exception e)
		{
			ec.displayMessage("Unable to get ROI information");
		}

		return;

	}// end of measureROI()


	//********************************************************************
	//*		G E T  L I N E  L E N G T H
	//*		Returns the distance between two coordinate points
	//********************************************************************
	double	getLineLength(Point pt_zero, Point pt_one) throws Exception
	{

		double 	result = 0.0;
		int		delta_x = 0, delta_y = 0;


		delta_x = pt_zero.x - pt_one.x;
		delta_y = pt_zero.y - pt_one.y;
		result = Math.sqrt(Math.pow(delta_x, 2)  + Math.pow(delta_y, 2));

		return(result);

	}// end of getLineLength


	//******************************************************************
	//*		G E T  L I N E  L E N G T H  S T R I N G
	//*		Modified 9/5/02 for v1.62
	//******************************************************************
	String	getLineLengthString(Point pt0, Point pt1, double multiplier) throws Exception
	{
		String		num_string = null;
		double		line_length = 0.0;
		int			dot_index = NOT_FOUND;
		Double		num_obj = null;

		//***** Find out how long the line was in pixels
		line_length = getLineLength(pt0, pt1);

		//***** Convert to appropriate units
		line_length *= multiplier;

		//***** Make a string
		num_obj = new Double(line_length);
		num_string = num_obj.toString();
		dot_index = num_string.indexOf('.');

		//***** Trim off excess numbers, leave two decimal places
		if (dot_index != NOT_FOUND)
		{
			dot_index += 3;
			if (dot_index > num_string.length())
				dot_index = num_string.length();
			num_string = num_string.substring(0, dot_index);
		}

		return(num_string);

	}// end of getLineLengthString()

}// end of LineROI

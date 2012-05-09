import java.awt.*;
import java.awt.image.*;

class RectROI extends ROIObject
{

	protected	Rectangle		object_rect = null;// for rect ROI's

	//************************************************
	//*		I N I T
	//************************************************
	public RectROI(EditingCanvas ec)
	{
		super(ec);

		object_rect = new Rectangle(0,0,0,0);
		roi_type = RECT_ROI;

		return;
	}// init


	//************************************************
	//*		I N I T
	//************************************************
	public RectROI(EditingCanvas ec, Rectangle r)
	{
		this(ec);

		try
		{
			this.getObjectRect(r);
		}
		catch (Exception e){}

		return;

	}// init


	//************************************************
	//*		D R A W  R O I
	//*		Modified 9/6/02 for v1.62
	//************************************************
	public void drawROI(Graphics g) throws Exception
	{
		if (g == null)
			throw new Exception("null graphics object in dragROI()");

		g.setXORMode(Color.white);

		g.drawRect(this.object_rect.x,
				   this.object_rect.y,
				   this.object_rect.width,
				   this.object_rect.height);
		g.setPaintMode();

		return;

	}// end of drawROI()


	//*******************************************************************
	//*		E D I T  R O I
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void editROI(Graphics g, int x, int y) throws Exception
	{
		Rectangle			local_rect = null;
		int				width = 0, height = 0;


		if (g == null)
			throw new Exception("null graphics object in editROI()");

		drawROI(g); // get rid of the old one

		width = x - object_rect.x;
		if (width < 0)
			width = 0;
		height = y - object_rect.y;
		if (height < 0)
			height = 0;

		object_rect.width = width;
		object_rect.height = height;

		drawROI(g);

		return;

	}// end of editROI


	//*******************************************************************
	//*		F I N I S H  R O I
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void finishROI(int x, int y) throws Exception
	{

		object_rect.width = x - object_rect.x;
		object_rect.height = y - object_rect.y;
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
			throw new Exception("null graphics object in dragROI()");

		ec.clearObject(getROIRect()); // get rid of the old one

		x_offset = x - last_click.x;
		y_offset = y - last_click.y;

		object_rect.x += x_offset;
		object_rect.y += y_offset;
		bounding_rect.x += x_offset;
		bounding_rect.y += y_offset;

		drawROI(g);// draw the new one

		this.last_click.x = x;
		this.last_click.y = y;

		return;

	}// end of dragROI()


	//*******************************************************************
	//*		S E T  O B J E C T  R E C T
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void getObjectRect(Rectangle r) throws Exception
	{
		if (r == null)
			return;

		this.object_rect.x = r.x;
		this.object_rect.y = r.y;
		this.object_rect.width = r.width;
		this.object_rect.height = r.height;

		getBoundingRect();

		return;

	}// end of getObjectRect()

	//**********************************************************************
	//*		G E T  R O I  R E C T
	//*		Returns a copy of the object rect
	//*		Modified 9/5/02 for v1.62
	//**********************************************************************
	public Rectangle getROIRect() throws Exception
	{
		return( new Rectangle(this.object_rect));
	}

	//**********************************************************************
	//*		G E T  B O U N D I N G  R E C T
	//*		Defines, then returns the the object's bounding rect -
	//*		a rect that is one pixel bigger than the object's rect
	//*		Modified 9/5/02 for v1.62
	//**********************************************************************
	public Rectangle getBoundingRect() throws Exception
	{
		bounding_rect = new Rectangle(this.object_rect);

		//***** Expand by 1 pixel in each direction
		bounding_rect.x -= 1;
		bounding_rect.y -= 1;
		bounding_rect.width += 2;
		bounding_rect.height += 2;

		return(new Rectangle(bounding_rect));

	}// end of getBoundingRect()


	//********************************************************************************
	//*		M E A S U R E  R O I
	//********************************************************************************
	public void	measureROI()
	{
		String			area_num_string = null;
		String			mean_num_string = null;
		PixelGrabber		pixel_grabber = null;
		Rectangle			roi_rect = null;
		boolean			ok = false;

		try
		{

			if (ec.cur_image == null)
				return;

			roi_rect = getROIRect();

			//***** Make the pixel grabber
			pixel_grabber = new PixelGrabber(ec.cur_image, roi_rect.x, roi_rect.y,
											 roi_rect.width, roi_rect.height, null, 0, 1);
			try
			{
				do
				{
					ok = pixel_grabber.grabPixels();// grab the pixels into the object's pixel array
				}
				while (!ok);

				pixel_array = (int[])pixel_grabber.getPixels();
				num_pixels = roi_rect.width * roi_rect.height;
			}
			catch (InterruptedException ie)
			{
				ec.displayMessage("InterruptedException thrown while getting ROI pixels!");
			}

			area_num_string = getRectAreaString();
			mean_num_string = getMeanString();

			//***** Output the area
			ec.displayMessage("Area: " + area_num_string + " square " + EditingSettings.units_string + "   " +
								  	   "Mean: " + mean_num_string);

		}
		catch (Exception e)
		{
			ec.displayMessage("Unable to get ROI information");
		}

		return;

	}// end of measureROI()


	//********************************************************************
	//*		G E T  R E C T A N G L E  P E R I M E T E R
	//*		Returns the distance around a rectangle's perimeter
	//*		Modified 9/5/02 for v1.62
	//********************************************************************
	double	getRectanglePerimeter(Rectangle rect) throws Exception
	{

		double 	result = 0.0;

		// x = 2w + 2h
		result += (2 * rect.width);
		result += (2 * rect.height);

		return(result);

	}// end of getRectanglePerimeter


	//********************************************************************
	//*		G E T  R E C T A N G L E  A R E A
	//*		Modified 9/5/02 for v1.62
	//********************************************************************
	double	getRectangleArea(Rectangle rect) throws Exception
	{

		double 	result = 0.0;

		// x = w * h
		result = rect.width * rect.height;

		return(result);

	}// end of getRectanglePerimeter


	//******************************************************************
	//*		G E T  R E C T  A R E A  S T R I N G
	//*		Modified 9/5/02 for v1.62
	//******************************************************************
	String	getRectAreaString() throws Exception
	{
		String		area_string = "Bleah";
		double		area = 0;
		Double		num_obj = null;
		int			dot_index = 0;

		area = getRectangleArea(object_rect);

		//***** Convert to appropriate units by multiplying by the square of
		//***** the multiplier
		area *= (Math.pow(EditingSettings.multiplier, 2));

		//***** Make a string
		num_obj = new Double(area);
		area_string = num_obj.toString();
		dot_index = area_string.indexOf('.');

		//***** Trim off excess numbers, leave two decimal places
		if (dot_index != NOT_FOUND)
		{
			dot_index += 3;
			if (dot_index > area_string.length())
				dot_index = area_string.length();
			area_string = area_string.substring(0, dot_index);
		}

		return(area_string);
	}





}// end of RectROI

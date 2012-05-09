import java.awt.*;
import java.lang.*;

//********************************************************************
//*		C L A S S  R O I  O B J E C T
//*		Holds the pixels and the boundary of a Region Of Interest
//********************************************************************

class ROIObject
{
	final		static	int			RECT_ROI = 1;
	final		static	int			OVAL_ROI = 2;
	final		static	int			LINE_ROI = 3;
	final		static	int			OUTLINE_ROI = 4;

	final		static	int			NOT_FOUND = -1;

	protected 	EditingCanvas	ec = null;
	protected	EditingSettings es = null;
	protected	int				roi_type = 0;
	protected	Point			last_click = null; // for dragging operations
	protected	Rectangle		bounding_rect = null; // usually one pixel larger
	protected	int				pixel_array[] = null;// holds the pixels in the ROI
	protected	int				num_pixels = 0;

	public ROIObject(EditingCanvas edit_canvas)
	{
		this.ec = edit_canvas;
		this.last_click = new Point(0,0);
		this.bounding_rect = new Rectangle(0,0,0,0);
		this.pixel_array = null;
		this.num_pixels = 0;

		return;

	}// init


	//**************************************************************
	//*		C L I C K  I N  R O I
	//*		Returns true if the click was in the ROI's boundary
	//*		Modified 9/5/02 for v1.62
	//**************************************************************
	public boolean clickInROI(int x, int y) throws Exception
	{
		if (getBoundingRect().contains(x, y))
			return(true);
		else
			return(false);

	}// end of click_in_ROI()


	//************************************************
	//*		D R A W  R O I
	//*		Modified 9/5/02 for v1.62
	//************************************************
	public void drawROI(Graphics g) throws Exception {}


	//************************************************
	//*		D R A G  R O I
	//*		Modified 9/5/02 for v1.62
	//************************************************
	public void dragROI(Graphics g, int x, int y) throws Exception {}


	//************************************************
	//*		E D I T  R O I
	//*		Modified 9/5/02 for v1.62
	//************************************************
	public void editROI(Graphics g, int x, int y) throws Exception {}


	//*******************************************************************
	//*		F I N I S H  R O I
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void finishROI(int x, int y) throws Exception {}


	//************************************************
	//*		M E A S U R E  R O I
	//*		Modified 9/5/02 for v1.62
	//************************************************
	public void measureROI() throws Exception {}


	//************************************************
	//*		H A N D L E  C L I C K  O N  ROI
	//*		Modified 9/5/02 for v1.62
	//********************************************
	public void handleClickOnROI(int x, int y) throws Exception
	{
		last_click.x = x;
		last_click.y = y;

		return;

	}// end of handle_mouse_pressed()


	//**********************************************************************
	//*		S E T  P I X E L  A R R A Y
	//*		Modified 9/5/02 for v1.62
	//**********************************************************************
	public void setPixelArray(int pixel_array[]) throws Exception
	{
		int i = 1, pixel = 0;

		if (pixel_array == null)
			throw new Exception("pixel_array was null!");

		this.pixel_array = pixel_array;

		//Count the pixels
		try
		{
			for (i = 0; true; i++)
				pixel = pixel_array[i];
		}
		catch (ArrayIndexOutOfBoundsException e){}

		this.num_pixels = i;

		return;
	}// end of setPixelArray


	//**********************************************************************
	//*		G E T  P I X E L  A R R A Y
	//*		Modified 9/5/02 for v1.62
	//**********************************************************************
	public int[]  getPixelArray() throws Exception
	{
		return(this.pixel_array);

	}// end of getPixelArray()


	//**********************************************************************
	//*		S E T  R O I  T Y P E
	//**********************************************************************
	public void setROIType(int roi_type)
	{
		if (roi_type >= 0 && roi_type < 6)
			this.roi_type = roi_type;

		return;

	}// end of setROIType

	//**********************************************************************
	//*		G E T  R O I  T Y P E
	//**********************************************************************
	public int getROIType()
	{
		return(this.roi_type);

	}// end of getROIType()


	//**********************************************************************
	//*		G E T  B O U N D I N G  R E C T
	//*		Returns the the ROI's bounding rect -
	//*		a rect that is one pixel bigger than the ROI's rect
	//**********************************************************************
	public Rectangle getBoundingRect() throws Exception
	{
		return(null);
	}// end of getBoundingRect()


	//********************************************************************
	//*		G E T  M E A N  I N T E N S I T Y
	//*		Gets mean intensity of an array of RGB pixels
	//********************************************************************
	double	getMeanIntensity(int[] int_array, int num_pixels) throws Exception
	{
	    int 		cur_value = 0, i = 0;
        double 		total_value = 0, cur_mean_value = 0;
        double		mean = 0;
		int			r = 0, g = 0, b = 0;

        for (i = 0; i < num_pixels; i++)
        {
        	cur_value = int_array[i];
        	r = (cur_value >> 16) & 0xFF;
        	g = (cur_value >> 8) & 0xFF;
        	b = cur_value & 0xFF;

        	cur_mean_value = (double)((r + g + b)/3);
        	total_value += cur_mean_value;
        }

   		mean = (double)(total_value / num_pixels);

   		return(mean);

	}// end of getMeanIntensity


	//******************************************************************
	//*		G E T  M E A N  S T R I N G
	//******************************************************************
	String	getMeanString() throws Exception
	{
		String		mean_string = "Very Mean!";
		double		mean = 0;
		Double		num_obj = null;
		int			dot_index = 0;


		mean = getMeanIntensity(pixel_array, num_pixels);

		//***** Make a string
		num_obj = new Double(mean);
		mean_string = num_obj.toString();
		dot_index = mean_string.indexOf('.');

		//***** Trim off excess numbers, leave two decimal places
		if (dot_index != NOT_FOUND)
		{
			dot_index += 3;
			if (dot_index > mean_string.length())
				dot_index = mean_string.length();
			mean_string = mean_string.substring(0, dot_index);
		}

		return(mean_string);

	}// enf of getMeanString();


}// end of class ROIObject

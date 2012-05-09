import java.awt.*;
import java.awt.image.*;

class OvalROI extends RectROI
{


	//************************************************
	//*		I N I T
	//************************************************
	public OvalROI(EditingCanvas ec)
	{
		super(ec);
		roi_type = OVAL_ROI;

		return;
	}// init


	//************************************************
	//*		I N I T
	//************************************************
	public OvalROI(EditingCanvas ec, Rectangle r)
	{
		super(ec, r);
		roi_type = OVAL_ROI;

		return;

	}// init


	//************************************************
	//*		D R A W  R O I
	//*		Modified 9/6/02 for v1.62
	//************************************************
	public void drawROI(Graphics g) throws Exception
	{

		if (g == null)
			throw new Exception("null graphics object in drawObject()");

		g.setXORMode(Color.white);

		g.drawOval(this.object_rect.x,
				   this.object_rect.y,
				   this.object_rect.width,
				   this.object_rect.height);
		g.setPaintMode();

		return;

	}// end of drawROI()


	//********************************************************************************
	//*		M E A S U R E  R O I
	//*		Modified 9/5/02 for v1.62
	//********************************************************************************
	public void	measureROI()
	{
		return;

	}// end of measureROI()


	//********************************************************************
	//*		G E T  O V A L  P E R I M E T E R
	//*		Returns the APPROXIMATE distance around an oval's perimenter
	//*		Modified 9/5/02 for v1.62
	//********************************************************************
	static 	double	getOvalPerimeter(Rectangle rect) throws Exception
	{

		double 	result = 0.0;

		result = 2 * Math.PI * Math.sqrt(((rect.width/2) * 2 + (rect.height/2) * 2)/2);

		return(result);

	}// end of getOvalPerimeter


	//********************************************************************
	//*		G E T  O V A L  A R E A
	//*		Returns the area of an oval
	//*		Modified 9/5/02 for v1.62
	//********************************************************************
	double	getOvalArea(Rectangle rect) throws Exception
	{

		double 	result = 0.0;

		//  ?WH
		result = Math.PI * (rect.width * rect.height);

		return(result);

	}// end of getOvalArea


	//******************************************************************
	//*		G E T  R E C T  A R E A  S T R I N G
	//*		Modified 9/5/02 for v1.62
	//******************************************************************
	String	getOvalAreaString() throws Exception
	{
		String		area_string = "Bleah";
		double		area = 0;
		Double		num_obj = null;
		int			dot_index = 0;

		area = getOvalArea(object_rect);

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

	}// end of getOvalAreaString()


}// end of OvalROI

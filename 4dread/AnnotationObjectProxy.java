import java.io.*;
import java.awt.*;

//************************************************************
//*		C L A S S  A N N O T A T I O N  O B J E C T  P R O X Y
//*
//*		AnnotationObjectProxy objects "stand-in" for AnnotationObjects when overlays
//*		are being read to or read from the disk
//************************************************************

class AnnotationObjectProxy implements Serializable
{
	protected	String			structure_descriptor = null;
	protected	String			object_comments = null;
	protected	int				object_rect_x = 0, object_rect_y = 0;
	protected	int				object_rect_width = 0, object_rect_height = 0;
	protected	int				object_type = 0;
	protected	int				object_color = 0;

	public AnnotationObjectProxy(AnnotationObject ao)
	{
		this.structure_descriptor = new String(ao.structure_descriptor);
		this.object_comments = new String(ao.object_comments);
		this.object_rect_x = ao.object_rect.x;
		this.object_rect_y = ao.object_rect.y;
		this.object_rect_width = ao.object_rect.width;
		this.object_rect_height = ao.object_rect.height;
		this.object_type = ao.object_type;
		getColorCode(ao.object_color);

		return;

	}// init


	//*********************************************************************
	//*		G E T  A N N O T A T I O N  O B J E C T
	//*********************************************************************
	public AnnotationObject	getAnnotationObject()
	{
		return(null);
	}

	//*********************************************************************
	//*		W R I T E  O B J E C T  T O  S T R E A M
	//*********************************************************************
	public void writeObjectToStream(ObjectOutputStream  stream) throws Exception
	{
		stream.writeObject(this);
		return;

	}// end of writeObjectToBuffer()

	//*********************************************************************
	//* 		G E T  C O L O R  C O D E
	//*********************************************************************
	private void getColorCode(Color c)
	{
		//***** Select the current pen color
		if (c == Color.black)
		{
			this.object_color = AnnotationObject.BLACK;
		}
		if (c == Color.blue)
		{
			this.object_color = AnnotationObject.BLUE;
		}
		if (c == Color.cyan)
		{
			this.object_color = AnnotationObject.CYAN;
		}
		if (c == Color.darkGray)
		{
			this.object_color = AnnotationObject.DARK_GRAY;
		}
		if (c == Color.gray)
		{
			this.object_color = AnnotationObject.GRAY;
		}
		if (c == Color.green)
		{
			this.object_color = AnnotationObject.GREEN;
		}
		if (c == Color.lightGray)
		{
			this.object_color = AnnotationObject.LIGHT_GRAY;
		}
		if (c == Color.magenta)
		{
			this.object_color = AnnotationObject.MAGENTA;
		}
		if (c == Color.orange)
		{
			this.object_color = AnnotationObject.ORANGE;
		}
		if (c == Color.pink)
		{
			this.object_color = AnnotationObject.PINK;
		}
		if (c == Color.red)
		{
			this.object_color = AnnotationObject.RED;
		}
		if (c == Color.white)
		{
			this.object_color = AnnotationObject.WHITE;
		}
		if (c == Color.yellow)
		{
			this.object_color = AnnotationObject.YELLOW;
		}
	}// end of getColorCode()


	//*********************************************************************
	//* 		G E T  C O L O R
	//*********************************************************************
	protected Color getColor(int color_code)
	{

		switch (color_code)
		{
			case AnnotationObject.BLACK:
				return(Color.black);
			case AnnotationObject.BLUE:
				return(Color.blue);
			case AnnotationObject.CYAN:
				return(Color.cyan);
			case AnnotationObject.DARK_GRAY:
				return(Color.darkGray);
			case AnnotationObject.GRAY:
				return(Color.gray);
			case AnnotationObject.GREEN:
				return(Color.green);
			case AnnotationObject.LIGHT_GRAY:
				return(Color.lightGray);
			case AnnotationObject.MAGENTA:
				return(Color.magenta);
			case AnnotationObject.ORANGE:
				return(Color.orange);
			case AnnotationObject.PINK:
				return(Color.pink);
			case AnnotationObject.RED:
				return(Color.red);
			case AnnotationObject.WHITE:
				return(Color.white);
			case AnnotationObject.YELLOW:
				return(Color.yellow);
			default:
				return(Color.black);
		}// end of switch

	}// end of getColor()

}// end of AnnotationObjectProxy

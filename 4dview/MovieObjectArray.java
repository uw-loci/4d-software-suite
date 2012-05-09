import java.util.*;
import java.awt.*;

	//********************************************************
	//********************************************************
	//*
	//*			C L A S S  M O V I E  O B J E C T  A R R A Y
	//*
	//*			Holds and manipulates all annotation objects in a movie canvas
	//*
	//********************************************************
	//********************************************************

class MovieObjectArray extends ObjectArray
{
	final  static  int			NOT_FOUND = -1;

	MovieCanvas	mc = null;

	public MovieObjectArray(MovieCanvas movie_canvas)
	{
		super();
		this.mc = movie_canvas;

		return;

	}// init


	//********************************************************************************
	//*		R E S T O R E  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//********************************************************************************
	public void restoreObjects() throws Exception
	{
		Graphics			g = null;

		if (getNumObjects() > 0)
		{
			g = mc.getGraphics();
			if (g == null)
				return;

			restoreObjects(g);

			g.dispose();

		}// if we have objects

		return;

	}// end of restoreObjects()



	//**************************************************************
	//*		C L E A R  O B J E C T
	//**************************************************************
	public void clearObject(Rectangle erase_rect) throws Exception
	{}// end of clearObject()

	//****************************************************************
	//*		D R A W  D I R T Y  O B J E C T S
	//*****************************************************************
	public void drawDirtyObjects () throws Exception
	{
		Graphics			g = null;

		if (getNumObjects() > 0)
		{
			g = mc.getGraphics();
			if (g == null)
				return;

			drawDirtyObjects(g);

			//***** Get rid of the graphics context
			g.dispose();

		}// if we have objects

		return;

	}// end of drawDirtyObjects()


	//***********************************************
	//*		D O  O B J E C T  C O M M E N T S
	//*		Display the object's comment strings and
	//*		offer the option to edit them
	//************************************************
	public void doObjectComments(AnnotationObject ao)
	{
		super.doObjectComments(Settings.main_window, ao);

		return;

	}// end of doObjectComments()


	//***********************************************
	//*		G E T  G R A P H I C S
	//***********************************************
	public Graphics getGraphics()
	{
		return(mc.getGraphics());
	}// end of getGraphics()

	//********************************************************************
	//*		G E T  A R R A Y  O B J E C T S
	//********************************************************************
	public MovieObjectArray getArrayObjects()
	{
		MovieObjectArray  array_objects = null;

		return(array_objects);
	}


}// end of class MovieObjectArray

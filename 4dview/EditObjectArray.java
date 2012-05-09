import java.util.*;
import java.awt.*;

	//********************************************************
	//********************************************************
	//*
	//*			C L A S S  E D I T  O B J E C T  A R R A Y
	//*
	//*			Holds and manipulates all annotation objects in an editing canvas
	//*
	//********************************************************
	//********************************************************

class EditObjectArray extends ObjectArray
{
	final  static  int			NOT_FOUND = -1;

	EditingCanvas	ec = null;

	public EditObjectArray(EditingCanvas edit_canvas)
	{
		super();
		this.ec = edit_canvas;

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
			g = ec.getGraphics();
			if (g == null)
				return;

			restoreObjects(g);

			g.dispose();

		}// if we have objects

		return;

	}// end of restoreObjects()



	//**************************************************************
	//*		C L E A R  O B J E C T
	//* 	Calls clearRect() in the editing canvas to erase the object
	//*		Modified 11/6/02 for v1.68
	//**************************************************************
	public void clearObject(Rectangle erase_rect) throws Exception
	{
		ec.clearRect(erase_rect);
		return;

	}// end of clearObject()

	//****************************************************************
	//*		D R A W  D I R T Y  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//*****************************************************************
	public void drawDirtyObjects () throws Exception
	{
		Graphics			g = null;

		if (getNumObjects() > 0)
		{
			g = ec.getGraphics();
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
		super.doObjectComments(Settings.editing_window, ao);

		return;

	}// end of doObjectComments()


	//***********************************************
	//*		G E T  G R A P H I C S
	//***********************************************
	public Graphics getGraphics()
	{
		return(ec.getGraphics());
	}// end of getGraphics()

	//********************************************************************
	//*		G E T  A R R A Y  O B J E C T S
	//********************************************************************
	public EditObjectArray getArrayObjects()
	{
		EditObjectArray  array_objects = null;

		return(array_objects);

	}// end of getArrayObjects()


}// end of class EditObjectArray

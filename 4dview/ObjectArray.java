import java.util.*;
import java.awt.*;
import java.io.*;

	//********************************************************
	//********************************************************
	//*
	//*			O B J E C T  A R R A Y
	//*
	//*			Holds and manipulates annotation objects
	//*
	//********************************************************
	//********************************************************

class ObjectArray implements Serializable
{
	final  static  int			NOT_FOUND = -1;

	Vector		object_array;
	boolean		is_new_object_array; // is this an array in the editing window used to hold "new" objects
								 // e.g., those objects not yet saved to the disk

	public ObjectArray()
	{
		this.object_array = new Vector(10,1);
		this.is_new_object_array = false;

		return;

	}// init


	//****************************************************************
	//*		C H E C K  F O R  O B J E C T  I N T E R S E C T I O N S
	//*		Checks each object in the array to see if it's
	//*		bounding rectangle overlaps with check_rect
	//*		If so, flags that object for redrawing
	//*		Modified 11/6/02 for v1.68
	//*****************************************************************
	public void checkForObjectIntersections (Rectangle check_rect) throws Exception
	{
		int						i = 0, num_objects = 0;
		AnnotationObject				cur_obj = null;
		Rectangle					local_check_rect = new Rectangle(check_rect);


		num_objects = getNumObjects();

		//***** Check a rect that's one pixels bigger in each direction
		local_check_rect.x -= 1;
		local_check_rect.y -= 1;
		local_check_rect.width += 2;
		local_check_rect.height += 2;

		//***** Check the object array for intersections
		for (i = 0; i < num_objects; i++)
		{
			cur_obj = getObject(i);
			if (!(cur_obj.getObjectRect().equals(check_rect)))
			{
				if ((cur_obj.getObjectRect()).intersects(local_check_rect))
					cur_obj.setDirty(true);
			}// if it's not the object being checked against

		}// for each object

		return;

	}// end of checkForObjectIntersections()


	//*******************************************************************
	//*		A D D  N E W  O B J E C T
	//*		Modified 8/22/02 for v1.61
	//*******************************************************************
	public void addObject(AnnotationObject ao)
	{
		if (ao != null)
			object_array.addElement(ao);

		return;

	}//end of addObject()


	//*******************************************************************
	//*		A D D  N E W  O B J E C T
	//*		Modified 8/22/02 for v1.61
	//*******************************************************************
	public void addObject(AnnotationObject ao, int index)
	{
		if (ao != null)
		{
			if (object_array.size() < index + 1)
				object_array.setSize(index + 1);

			object_array.setElementAt(ao, index);
		}
		return;

	}//end of addObject()


	//********************************************************************************
	//*		R E S T O R E  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//********************************************************************************
	public void restoreObjects() throws Exception{}


	//********************************************************************************
	//*		R E S T O R E  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//********************************************************************************
	public void restoreObjects(Graphics g) throws Exception
	{
		AnnotationObject		ao = null;
		int 					i = 0, num_objects = 0;

		if (g == null)
			return;

		if (getNumObjects() > 0)
		{

			//***** Draw each object in the object array
			for (i = 0; i < getNumObjects(); i++)
			{
				ao = getObject(i);
				ao.drawObject(g);
			}// for each object

			//***** If this is a "new object array" (i.e. an array in the EditingWindow containing new objects
			//***** that the user is adding to the overlay), and it contains objects, then
			//***** the overlay is dirty... so tell the user
			if (is_new_object_array && Settings.editing_window != null)
				Settings.editing_window.setOverlayStatus(EditingSettings.OVERLAY_IS_DIRTY);
		}// if we have objects
		else
		{
			//***** If this is a "new object array" (i.e. an array in the EditingWindow containing new objects
			//***** that the user is adding to the overlay) and it contains objects then
			//***** the overlay is dirty... so tell the user
			if (is_new_object_array && Settings.editing_window != null)
				Settings.editing_window.setOverlayStatus(EditingSettings.OVERLAY_SAVED);
		}// if we have no overlay objects to restore


		return;

	}// end of restoreObjects()


	//********************************************************************************
	//*		C H E C K  F O R  O B J E C T  V A L I D I T Y
	//*		Gets rid of invalid objects
	//*		Modified 11/6/02 for v1.68
	//********************************************************************************
	public void checkForObjectValidity() throws Exception
	{
		int 					i = 0;
		Vector					delete_array = new Vector(10,1);
		AnnotationObject		ao = null;

		for (i = 0; i < getNumObjects(); i++)
		{
			ao = getObject(i);
			if (!ao.isValidObject())
				delete_array.addElement(ao);
		}

		for (i = 0; i < delete_array.size(); i++)
		{
			deleteObject((AnnotationObject)delete_array.elementAt(i));
		}

		return;

	}// end of checkForObjectValidity()


	//*****************************************************************
	//*		R E M O V E  D U P L I C A T E  O B J E C T S
	//*		Searches the object_array one object at a time and removes
	//*		objects that are identical to the object being searched on
	//*		Modified 11/6/02 for v1.68
	//******************************************************************
	public void removeDuplicateObjects() throws Exception
	{
		int					i = 0, j = 0;
		AnnotationObject			test_obj = null, current_obj = null;
		Vector				delete_array = new Vector(10,1);

		for (i = 0; i < getNumObjects(); i++)
		{
			test_obj = getObject(i);

			for (j = 0; j < getNumObjects(); j++)
			{
				if (j != i)// don't test an object against itself
				{
					current_obj = getObject(j);

					if (test_obj.isIdenticalObject(current_obj))
					{
						//***** If it's a duplicate, load it into the array to be deleted
						delete_array.addElement(test_obj);
						break;
					}// if it's equal
				}// if it's not testing itself
			}// for j
		}// for i

		//***** Delete all the duplicates
		for (i = 0; i < delete_array.size(); i++)
		{
			test_obj = (AnnotationObject)delete_array.elementAt(i);
			deleteObject(test_obj);
		}// for each delete object

		return;

	}// end of removeDuplicateObjects()


	//**************************************************************
	//*		C L E A R  O B J E C T
	//*		Modified 11/6/02 for v1.68
	//**************************************************************
	public void clearObject(Rectangle erase_rect) throws Exception {}


	//***********************************************
	//*		G E T  A R R A Y
	//***********************************************
	public Vector getArray()
	{
		return(object_array);
	}

	//***********************************************
	//*		S E T  A R R A Y
	//***********************************************
	public void setArray(Vector array)
	{
		if (array != null)
			this.object_array = array;

		return;
	}


	//************************************************
	//*		R E S E T  A R R A Y
	//********************************************
	public void resetArray()
	{
		object_array.removeAllElements();

		return;
	}// end of resetArray()


	//************************************************
	//*		D E L E T E  O B J E C T
	//*		Modified 11/6/02 for v1.68
	//********************************************
	public void deleteObject(AnnotationObject ao) throws Exception
	{
		int				index = NOT_FOUND, i = 0;
		int				num_objects = 0;

		num_objects = getNumObjects();

		//****** Look for the object's index number in the object array
		for (i = 0; i < num_objects; i++)
		{
			if (getObject(i) == ao)
				index = i;
		}

		//***** If we didn't find it, return
		if (index == NOT_FOUND)
			return;

		//***** Erase the object
		clearObject(ao.object_rect);

		//***** Remove the object from the appropriate object array
		object_array.removeElementAt(index);

		return;

	}// end of deleteObject()


	//****************************************************
	//*		D E L E T E  S E L E C T E D  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//****************************************************
	public void deleteSelectedObjects() throws Exception
	{
		Vector			delete_array = null;
		int 				i = 0, num_delete_objects = 0;
		int				num_objects = 0;

		num_objects = getNumObjects();

		//***** Create an array to hold objects that will be deleted
		delete_array = new Vector(10,1);

		//***** Check each object_array object for selection
		for (i = 0; i < num_objects; i++)
		{
			//***** If it's selected, add it to the delete array
			if ((getObject(i)).selected)
			{
				delete_array.addElement(getObject(i));
				num_delete_objects += 1;
			}
		}// for each object

		//***** Actually delete the objects that need to be deleted
		for (i = 0; i < num_delete_objects; i++)
		{
			deleteObject((AnnotationObject)delete_array.elementAt(i));
		}

		restoreObjects(getGraphics());

		return;

	}// end of deleteSelectedObjects()


	//****************************************************
	//*		D E L E T E  O L D  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//****************************************************
	public void deleteOldObjects() throws Exception
	{
		Vector				delete_array = null;
		int 					i = 0, num_delete_objects = 0;
		AnnotationObject			test_obj = null;
		int					num_objects = 0;


		num_objects = getNumObjects();

		//***** Create an array to hold objects that will be deleted
		delete_array = new Vector(10,1);

		//***** Check each object_array object for selection
		for (i = 0; i < num_objects; i++)
		{
			test_obj = getObject(i);

			//***** If it's selected, add it to the delete array
			if (test_obj.old_object)
			{
				delete_array.addElement(test_obj);
				num_delete_objects += 1;
			}
		}// for each object

		//***** Actually delete the objects that need to be deleted
		for (i = 0; i < num_delete_objects; i++)
		{
			deleteObject((AnnotationObject)delete_array.elementAt(i));
		}

		restoreObjects(getGraphics());

		return;

	}// end of deleteOldObjects()


	//****************************************************
	//*		D E S E L E C T  A L L  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//****************************************************
	public void deselectAllObjects() throws Exception
	{
		int 					i = 0, num_objects = 0;
		AnnotationObject		cur_obj;


		num_objects = getNumObjects();

		//***** Check each object_array object for selection
		for (i = 0; i < num_objects; i++)
		{
			cur_obj = getObject(i);
			//***** If it's selected, deselect it
			if (cur_obj.selected)
			{
				clearObject(cur_obj.object_rect); // erase old one
				cur_obj.selected = false;
			}
		}// for each object

		restoreObjects(getGraphics());

		return;

	}// end of deselectAllObjects()


	//****************************************************************
	//*		C L I C K  I N  W H I C H  O B J E C T
	//*		Returns -1 if the click was not inside an existing object
	//*		Otherwise, returns the object's array index number
	//*		Modified 11/6/02 for v1.68
	//*****************************************************************
	public int	clickInWhichObject(int x, int y) throws Exception
	{
		AnnotationObject	ao = null;
		boolean				found = false;
		int					i = 0;

		if (getNumObjects() > 0)
		{
			//***** Check the object_array
			for (i = 0; i < getNumObjects(); i++)
			{
				ao = getObject(i);
				found = ao.clickInObject(x, y);
				if (found)
					return(i);
			}// for each object
		}//if there are objects

		return(NOT_FOUND);

	}//end of clickInWhichObject()


	//****************************************************************
	//*		D R A W  D I R T Y  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//*****************************************************************
	public void drawDirtyObjects () throws Exception
	{}


	//****************************************************************
	//*		D R A W  D I R T Y  O B J E C T S
	//*****************************************************************
	public void drawDirtyObjects (Graphics g) throws Exception
	{
		AnnotationObject		ao = null;
		int				i = 0;

		if (g == null)
			return;

		if (getNumObjects() > 0)
		{
			//***** Draw dirty objects from the object array
			for (i = 0; i < getNumObjects(); i++)
			{
				ao = getObject(i);
				if (ao.dirty)
					ao.drawObject(g);
			}// for each object
		}// if we have objects

		return;

	}// end of drawDirtyObjects()


	//***********************************************
	//*		D O  O B J E C T  C O M M E N T S
	//*		Display the object's comment strings and
	//*		offer the option to edit them
	//*		Modified 10/15/02 for v1.66
	//*		Modified 1/23/03 for v1.70
	//*		Modified 6/30/03 for 1.74
	//************************************************
	public void doObjectComments(Frame parent, AnnotationObject ao)
	{
		StringWrapper		obj_desc_str = null, obj_comments_str = null;
		//BooleanWrapper		was_cancelled = new BooleanWrapper(false);

		try
		{
			if (ao == null)
				throw new Exception("Object was null in doObjectComments()!");

			obj_desc_str = new StringWrapper(ao.getStructureDescriptor());
			obj_comments_str = new StringWrapper(ao.getObjectComments());

			if (parent == Settings.editing_window)
			{
				EdCommentDlog	ecd = null;

				ecd = new EdCommentDlog(parent, true, obj_desc_str, obj_comments_str);
				if (ecd != null)
					ecd.setVisible(true);
			}// if we're in the editing window
			else
			{
				ObjCommentDlog	ocd = null;

				ocd = new ObjCommentDlog(parent, true, obj_desc_str, obj_comments_str);
				if (ocd != null)
					ocd.setVisible(true);
			}// if we're in the movie window

			if (parent == Settings.editing_window)
			{
				ao.setStructureDescriptor(obj_desc_str.getString());
				ao.setObjectComments(obj_comments_str.getString());
				Settings.editing_window.setOverlayStatus(EditingSettings.OVERLAY_IS_DIRTY);
			}

		}// try
		catch (CancelledException ce)
		{
			Settings.main_window.displayMessage("Cancelled object comments.");
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown obtaining object comments!");
		}

		return;

	}// end of doObjectComments()


	//**************************************************************
	//*		D O  O B J E C T  O P T I O N S
	//*		Created 8/7/02 for v1.60
	//*		Modified 8/8/02 for v1.60
	//**************************************************************
	public void doObjectOptions(Frame parent, AnnotationObject ao) throws Exception
	{
		ao.getObjectOptions(parent);

		//***** Flag the overlay as needing to be saved
		if (Settings.editing_window != null)
			Settings.editing_window.setOverlayStatus(EditingSettings.OVERLAY_IS_DIRTY);

		return;

	}// end of doObjectOptions()


	//***********************************************
	//*		G E T  O B J E C T
	//*		Modified 11/6/02 for v1.68
	//***********************************************
	public AnnotationObject  getObject(int index) throws Exception
	{
		AnnotationObject		ao = null;

		ao = (AnnotationObject)object_array.elementAt(index);

		return(ao);

	}// end of getObject()


	//***********************************************
	//*		G E T  N U M  O B J E C T S
	//***********************************************
	public int	getNumObjects()
	{
		return(object_array.size());
	}// end of getNumObjects()


	//***********************************************
	//*		G E T  G R A P H I C S
	//***********************************************
	public Graphics	getGraphics()
	{
		return(null);
	}

	//*************************************************************************
	//*		G E T  O V E R L A Y  S I Z E
	//*************************************************************************
	int		getOverlaySize() throws Exception
	{
		int					overlay_size = 0;
		ByteArrayOutputStream	os = null;

		os = new ByteArrayOutputStream();
		overlay_size = writeOverlayToStream(os);
		os.close();

		return(overlay_size);

	}// end of getOverlaySize()


	//*************************************************************************
	//*		  W R I T E  O V E R L A Y  T O  S T R E A M
	//*		  Returns the size of the overlay
	//*************************************************************************
	int  writeOverlayToStream(ByteArrayOutputStream os) throws Exception
	{
		int				i = 0, overlay_size = 0;
		Integer			num_obj = null;
		ObjectOutputStream	stream = null;

		try
		{
			stream = new ObjectOutputStream(os);

			//****** Write the number of objects
			num_obj = new Integer(getNumObjects());
			stream.writeObject(num_obj);

			//***** Write each object to stream
			for (i = 0; i <  getNumObjects(); i++)
			{
				getObject(i).writeObjectToStream(stream);
			}// for each object

			stream.flush();
			overlay_size = os.size();
		}
		catch (Exception e)
		{
			throw(e);
		}

		return(overlay_size);

	}// end of writeOverlayToStream()



	//*************************************************************************
	//*		R E A D  O V E R L A Y  F R OM  S T R E A M
	//*		Modified 8/22/02 for v1.61
	//*************************************************************************
	void readOverlayFromStream(ByteArrayInputStream is) throws Exception
	{
		int					i = 0, overlay_size = 0;
		Integer				num_obj = null;
		ObjectInputStream		stream = null;
		AnnotationObject			ao = null;
		AnnotationObjectProxy	aop = null;

		try
		{
			stream = new ObjectInputStream(is);

			//****** Read the number of objects
			num_obj = (Integer)stream.readObject();

			if (num_obj.intValue() > 0)
				resetArray();

			//***** Write each object to stream
			for (i = 0; i <  num_obj.intValue(); i++)
			{
				aop = (AnnotationObjectProxy)stream.readObject();
				ao = aop.getAnnotationObject();
				if (ao != null)
					addObject(ao);
			}// for each object
		}
		catch (Exception e)
		{
			throw(e);
		}

		return;

	}// end of writeOverlayToStream()


	//**********************************************************************************
	//*		S E L E C T  A L L
	//*		Added 6/30/03 for v1.74
	//**********************************************************************************
	void selectAll() throws Exception
	{
		int 				i = 0;
		AnnotationObject	ao = null;

		if (getNumObjects() > 0)
		{
			//***** Select all objects in the object array
			for (i = 0; i < getNumObjects(); i++)
			{
				ao = getObject(i);
				ao.setSelected(true);
			}// for each object
		}// if we have objects

		return;

	}// end of selectAll()


}// end of class ObjectArray

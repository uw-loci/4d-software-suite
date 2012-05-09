import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EditingCanvas extends Canvas
{

	final  static  int			NOT_FOUND = -1;

	//***** Modifier Key Constants
	final  static  int			NO_MODIFIER = 0;
	final  static  int			SHIFT_CLICK = 1;
	final  static  int			ALT_OR_OPTION_CLICK = 2;
	final  static  int			CMD_CLICK = 3;
	final  static  int			CTRL_CLICK = 4;


	//***** Begin Class Variables
	Image					cur_image = null;
	int						image_width = -1;
	int						image_height = -1;
	Rectangle				image_rect = null;

	ObjectArray				disk_object_array = null; // holds overlay objects that have been read from the disk
	ObjectArray				new_object_array = null; // holds overlay objects that the user is adding
	ObjectArray				carried_object_array = null; // holds objects to be carried over between frames

	AnnotationObject		drag_object = null; // which object is being dragged
	AnnotationObject		resize_object = null; // which object is being resized
	AnnotationObject		new_object = null; // object in the process of being created

	ROIObject				roi = null;
	boolean					dragging_roi = false;// true if the roi is being draged
	boolean					unfinished_text_object = false; // true if there's a text object we're working on

	Thread					select_thread = null, insert_thread = null;


	//*****************************************************************
	//*		I N I T
	//*		Modified 7/23/02 for v1.59
	//*****************************************************************
	public EditingCanvas()
	{

		setSize(400,300);

		//***** Initialize Class Variables
    	this.cur_image = null;
		this.image_width = 0;
		this.image_height = 0;
		this.image_rect = null;

		this.disk_object_array = new EditObjectArray(this);
		this.new_object_array = new EditObjectArray(this);
		this.new_object_array.is_new_object_array = true;
		this.carried_object_array = new EditObjectArray(this);

		this.drag_object = null;
		this.resize_object = null;

		this.roi = null;
		this.dragging_roi = false;
		this.unfinished_text_object = false;

		this.select_thread = null;
		this.insert_thread = null;

		//***** Add listeners
		MyMouseAdapter mouse_adapter = new MyMouseAdapter();
		this.addMouseListener(mouse_adapter);

		MyMouseMotionAdapter mouse_motion_adapter = new MyMouseMotionAdapter();
		this.addMouseMotionListener(mouse_motion_adapter);

		return;

	}// end of EditingCanvas()


	//********************************************************************************
	//*		U P D A T E
	//********************************************************************************
	public void update(Graphics g)
	{

		super.update(g);

		try
		{
			paint(g);
		}//try
		catch (Exception e)
		{
			displayMessage("Exception thrown while updating EditingCanvas!");
		}
		catch (OutOfMemoryError e)
		{
			displayMessage("Ran out of memory while updating EditingCanvas!");
			displayMessage("Try quitting and restarting after allocating");
			displayMessage("more memory to the application.");
		}

		return;

	}// end of update()


	//********************************************************************************
	//*		P A I N T
	//********************************************************************************
	public void paint(Graphics g)
	{

		super.paint(g);

		try
		{
			if (this.cur_image != null)
			{
				//***** Restore the image
				g.drawImage(this.cur_image, 0, 0, this);
			}// if we have an image

			g.setColor(Color.black);
			g.drawRect(1, 1, this.getBounds().width -2, this.getBounds().height-2);

			//***** Draw any overlay objects
			disk_object_array.restoreObjects();
			new_object_array.restoreObjects();

		}//try
		catch (Exception e)
		{
			displayMessage("Exception thrown during EditingCanvas.paint()!");
		}
		catch (OutOfMemoryError e)
		{
			displayMessage("Ran out of memory while painting EditingCanvas!");
			displayMessage("Try quitting and restarting after allocating");
			displayMessage("more memory to the application.");
		}

		return;

	}// end of paint()


	//*******************************************************************
	//*		M Y  M O U S E  A D A P T E R
	//*		MyMouseAdapter is a mouse listener which parses mouse events
	//*		to the appropriate methods
	//*******************************************************************
	class MyMouseAdapter extends MouseAdapter
	{

		public void mousePressed(MouseEvent event)
		{
			try
			{
				doMousePressed(event);
			}
			catch (Exception e)
			{
				displayMessage("Exception thrown in EditingCanvas.mousePressed()!");
			}
		}

		public void mouseReleased(MouseEvent event)
		{
			try
			{
				doMouseReleased(event);
			}
			catch (Exception e)
			{
				displayMessage("Exception thrown in EditingCanvas.mouseReleased()!");
			}
		}

	}// end of class MyMouseAdapter



	//*******************************************************************
	//*		M Y  M O U S E  M O T I O N  A D A P T E R
	//*		MyMouseMotionAdapter is a mouse listener which shuttles
	//*		mouse moved events to the appropriate methods
	//*******************************************************************
	class MyMouseMotionAdapter extends MouseMotionAdapter
	{

		public void mouseMoved(MouseEvent event)
		{
			doMouseMoved(event);
		}

		public void mouseDragged(MouseEvent event)
		{
			try
			{
				doMouseDragged(event);
			}
			catch (Exception e)
			{
				displayMessage("Exception thrown in EditingCanvas.mouseDragged()!");
			}
		}

	}// end of class MyMouseMotionAdapter


	//*****************************************************************
	//*		D O  M O U S E  P R E S S E D
	//*		Modified 3/12/03 for v1.72
	//*****************************************************************
	public void doMousePressed(MouseEvent e) throws Exception
	{
		int					x = 0, y = 0;
		int					modifier_key = NO_MODIFIER;


		//***** See if any modifier keys were down
		if ((e.getModifiers() & e.ALT_MASK) != 0) // if alt key is pressed
		{
			modifier_key =  ALT_OR_OPTION_CLICK;
			Settings.main_window.displayMessage("Alt or Option click.");
		}
		else if ((e.getModifiers() & e.CTRL_MASK) != 0) // if control key is pressed
		{
			modifier_key =  CTRL_CLICK;
			Settings.main_window.displayMessage("Control click.");
		}
		else if ((e.getModifiers() & e.SHIFT_MASK) != 0) // if shift key is pressed
		{
			modifier_key =  SHIFT_CLICK;
			Settings.main_window.displayMessage("Shift click.");
		}
		else if ((e.getModifiers() & e.META_MASK) != 0) // if command key is pressed
		{
			modifier_key =  CMD_CLICK;
			Settings.main_window.displayMessage("Command click.");
		}

		//***** Get the click position
		x = e.getX();
		y = e.getY();

		//***** Handle old ROI, if there is one
		if (this.roi != null && !this.roi.clickInROI(x, y))
		{
			clearOldROI();
		}// if we have an old ROI

		//***** Handle Select Tool clicks
		if (EditingSettings.tool_selected ==  EditingPalette.SELECT_TOOL)
		{
			doSelectToolMousePressed(x, y, modifier_key);
		}// if the select tool is active

		//***** If we're not using the select tool, proceed with making a new object
		else if (EditingSettings.tool_selected != EditingPalette.SELECT_TOOL)
		{
			switch (EditingSettings.tool_selected)
			{
				case EditingPalette.SQUARE_TOOL:
					new_object = new SquareObject(x, y);
					 break;
				case EditingPalette.PENCIL_TOOL:
					new_object = new OutlineObject(x, y);
					 break;
				case EditingPalette.CIRCLE_TOOL:
					new_object = new CircleObject(x, y);
					 break;
				case EditingPalette.LINE_TOOL:
					new_object = new LineObject(x, y);
					 break;
				case EditingPalette.TEXT_TOOL:
					 if (!this.unfinished_text_object)
					 {
						new_object = new TextObject(x, y);
						unfinished_text_object = true;
						createInsertThread();
					 }
					 else
					 	//***** If we've got an unfinished text object, finish it
						finishObject(x, y);
					 break;
				case EditingPalette.ARROW_TOOL:
					 new_object = new ArrowObject(x, y);
					 break;
				case EditingPalette.MEASURE_TOOL:
					 if (modifier_key == ALT_OR_OPTION_CLICK)
					 {
						this.roi = new RectROI(this, new Rectangle(x, y, 0, 0));
					 }
					 else
						this.roi = new LineROI(this, new Point(x, y),new Point(x, y));
					 break;
			}// end of switch

		}// if the select tool is not active

		return;

	}// end of doMousePressed()


	//*******************************************************************
	//*		M O U S E  M O V E D
	//*******************************************************************
	public void doMouseMoved(MouseEvent e)
	{
		int 	x = 0, y = 0;

		x = e.getX();
		y = e.getY();

		changeCursor(x,y);

		return;

	}// end of doMouseMoved()


	//*******************************************************************
	//*		D O  M O U S E  D R A G G E D
	//*		Modified 9/5/02 for v1.62
	//*******************************************************************
	public void doMouseDragged(MouseEvent e) throws Exception
	{
		Rectangle			canvas_rect = null;
		int					x = 0, y = 0;

		canvas_rect = new Rectangle(0,0, this.getSize().width, this.getSize().height);
		x = e.getX();
		y = e.getY();

		//***** Don't process the request if it's not in the image window
		if (!canvas_rect.contains(x,y))
			return;

		//****** If the select tool is being used
		if (EditingSettings.tool_selected ==  EditingPalette.SELECT_TOOL)
		{
			doSelectToolMouseDragged(x, y);
		}// select tool
		else if (EditingSettings.tool_selected == EditingPalette.MEASURE_TOOL)
		{
			roi.editROI(this.getGraphics(), x, y);

		}// measure tool
		else
		{
			//***** If we're editing a new object
			if (new_object == null)
				Settings.main_window.displayMessage("new_object was null!");
			else
			{
				new_object.editObject(this.getGraphics(), x, y);
			}
		}// editing new object

		return;

	}// end of doMouseDragged()


	//*******************************************************************
	//*		M O U S E  R E L E A S E D
	//*		Modified 1/23/03 for v1.70
	//*		Modified 3/12/03 for v1.72
	//*******************************************************************
	public void doMouseReleased(MouseEvent e) throws Exception
	{
		int					x = 0, y = 0;
		Point				new_point = null;
		BooleanWrapper		done = new BooleanWrapper(false);

		new_point = getValidPoint(e.getX(), e.getY());
		x = new_point.x;
		y = new_point.y;

		if (this.drag_object != null || this.resize_object != null)
		{
			this.drag_object = null;
			this.resize_object = null;

			disk_object_array.restoreObjects();
			new_object_array.restoreObjects();
		}// if we're dragging

		//***** Finish dragging the roi, if we were, and measure
		if (this.dragging_roi)
		{
			this.dragging_roi = false;
			this.roi.measureROI();
		}// if we were dragging the roi

		//***** Select the object if appropriate
		if (EditingSettings.tool_selected ==  EditingPalette.SELECT_TOOL)
		{
			doSelectToolMouseReleased(x, y);
		}
		else if (EditingSettings.tool_selected == EditingPalette.MEASURE_TOOL)
		{
			this.roi.finishROI(x, y);
		}
		else if (EditingSettings.tool_selected == EditingPalette.TEXT_TOOL)
		{
			// don't do anything, TEXT_TOOL stuff is handled in doMousePressed()
		}
		else
		{
			finishObject(x, y);
		}// if we're finishing a new object

		return;

	}// end of doMouseReleased()


	//************************************************************************
	//*		D O  S E L E C T  T O O L  M O U S E  P R E S S E D
	//************************************************************************
	private void doSelectToolMousePressed(int x, int y, int modifier_key)
	{
		ObjectArray				obj_array = null;
		AnnotationObject		ao = null;
		int						which_object = NOT_FOUND;


		try
		{
			//***** See if the click was in an existing object
			which_object = new_object_array.clickInWhichObject(x, y);

			//***** See if the object was in the new_object_array
			if (which_object != NOT_FOUND)
			{
				obj_array = (ObjectArray)new_object_array;
			}// checking new_object_array
			else
			{
				//***** See if the object was in the disk_object_array
				which_object = disk_object_array.clickInWhichObject(x, y);
				if (which_object != NOT_FOUND)
					obj_array = (ObjectArray)disk_object_array;
			}// checking disk_object_array

			//***** If it was in one of the arrays, handle it
			if (obj_array != null)
			{
				ao = (AnnotationObject)obj_array.getObject(which_object);
				if (modifier_key == ALT_OR_OPTION_CLICK)
				{
					obj_array.doObjectComments(Settings.editing_window, ao);
				}// alt click
				else if (modifier_key == CMD_CLICK)
				{
					obj_array.doObjectOptions(Settings.editing_window, ao);
				}// control click
				else if (modifier_key == NO_MODIFIER)
				{
					ao.handleClickOnObject(x, y);
					if (ao.clickInResizeRect(x, y))
						this.resize_object = ao;
					else
						this.drag_object= ao;
				}// normal click

				obj_array.restoreObjects();

			}// if the click was in an existing object


			if (this.roi != null && this.roi.clickInROI(x, y))
			{
				this.dragging_roi = true;
				this.roi.handleClickOnROI(x, y);
			}// if the click was in the roi

		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown in EditingCanvas.doSelectToolMousePressed()!");
		}

		return;

	}// end of doSelectToolMousePressed()


	//**************************************************************************
	//*		D O  S E L E C T  T O O L  M O U S E  D R A G G E D
	//*		Modified 9/5/02 for v1.62
	//**************************************************************************
	private  void  doSelectToolMouseDragged(int x, int y) throws Exception
	{
		Rectangle		check_rect = null;

		//***** If there's a new object to be dragged
		if (this.drag_object != null)
		{
			this.drag_object.dragObject(this, x, y);
			check_rect = this.drag_object.object_rect;
			setOverlayStatus(EditingSettings.OVERLAY_IS_DIRTY);
		}// if we're dragging an overlay object

		//***** If we're resizing
		else if (this.resize_object != null)
		{
			this.resize_object.resizeObject(this, x, y);
			check_rect = this.resize_object.object_rect;
			setOverlayStatus(EditingSettings.OVERLAY_IS_DIRTY);
		}// if we're resizing an object

		else if (dragging_roi)
		{
			this.roi.dragROI(this.getGraphics(), x, y);
			check_rect = this.roi.getBoundingRect();
		}// if we're dragging the roi

		if (check_rect != null)
		{
			disk_object_array.checkForObjectIntersections(check_rect);
			disk_object_array.drawDirtyObjects();
			new_object_array.checkForObjectIntersections(check_rect);
			new_object_array.drawDirtyObjects();
		}

		return;

	}// end of doSelectToolMouseDragged


	//**************************************************************************
	//*		D O  S E L E C T  T O O L  M O U S E  R E L E A S E D
	//**************************************************************************
	private  void  doSelectToolMouseReleased(int x, int y)
	{
		AnnotationObject		ao = null;
		int						 which_object = NOT_FOUND;
		boolean					changed = false;

		try
		{
			which_object = new_object_array.clickInWhichObject(x, y);

			//***** If it's in the new_object_array, select it
			if (which_object != NOT_FOUND)
			{
				ao = new_object_array.getObject(which_object);
				changed = ao.checkSelect(this, x, y);
				new_object_array.restoreObjects();
			}// if the click was in an existing new_object_array object

			which_object = disk_object_array.clickInWhichObject(x, y);

			//***** If it's in the new_object_array, select it
			if (which_object != NOT_FOUND)
			{
				ao = disk_object_array.getObject(which_object);
				changed = ao.checkSelect(this, x, y);
				disk_object_array.restoreObjects();
			}// if the click was in an existing new_object_array object

			//***** If something was selected, create a thread to flash those objects
			if (changed && select_thread == null)
				createSelectThread();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown in EditingCanvas.doSelectToolMouseReleased()!");
		}

		return;

	}// end of doSelectToolMouseReleased()


	//*******************************************************************
	//*		K E Y  P R E S S E D
	//*
	//*		WARNING:  Key events are being shuttled in from the
	//*		Editing Window's doKeyPressed() method.  For some reason
	//*		the key adapter in this object isn't getting events.
	//*
	//*		Modified 7/29/02 for v1.59
	//*		Modified 6/25/03 for v1.74
	//*		Modified 6/30/03 for v1.74
	//*******************************************************************
	public void doKeyPressed (KeyEvent e) throws Exception
	{
		TextObject				text_obj = null;
		int						key = 0;
		char					key_ch = 0;

		key_ch = e.getKeyChar();
		key = e.getKeyCode();

		if ((e.getModifiers() & e.CTRL_MASK) != 0) // if control key is pressed
		{
		}// if the control key was down
		else if ((e.getModifiers() & e.ALT_MASK) != 0) // if alt key is pressed
		{
		}// if the alt key was down
		else if ((e.getModifiers() & e.META_MASK) != 0)
		{
			switch (key_ch)
			{
				case 'f':// cmd-f was pressed
					 //tsd = new TextSearchDialog(win, true);
					 //if (tsd != null)
						// tsd.setVisible(true);
					 break;
				case 'a':// cmd-a was pressed
					  selectAll();
					  break;
			}// switch

		}// if the cmd key was down
		else
		{
			if (EditingSettings.tool_selected ==  EditingPalette.TEXT_TOOL && this.unfinished_text_object && new_object != null)
			{
				text_obj = (TextObject)new_object;
				text_obj.addTextCharacter(this, key_ch);
			}// if we're making a text object
			else
			{
				switch (key_ch)
				{
					case KeyEvent.VK_DELETE:
					case TextObject.DELETE_CHAR:
						 disk_object_array.deleteSelectedObjects();
						 new_object_array.deleteSelectedObjects();
						 if (new_object_array.getNumObjects() <= 0)
						 	setOverlayStatus(EditingSettings.OVERLAY_SAVED);
						 break;
				}// switch
			}// if we're not making a text object

		}// if it was a normal key press

		return;

	}// end of doKeyPressed()


	//****************************************************************
	//*		C H A N G E  C U R S O R
	//*****************************************************************
	public void changeCursor (int x, int y)
	{
		Rectangle		boundary_rect = new Rectangle();

		boundary_rect.x = 0;
		boundary_rect.y = 0;
		boundary_rect.width = this.getBounds().width;
		boundary_rect.height = this.getBounds().height;

		if (boundary_rect.contains(x, y))
		{
			switch (EditingSettings.tool_selected)
			{
				case EditingPalette.SELECT_TOOL:
				case EditingPalette.PENCIL_TOOL:
				case EditingPalette.ARROW_TOOL:
					 Settings.editing_window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					 break;
				case EditingPalette.CIRCLE_TOOL:
				case EditingPalette.SQUARE_TOOL:
				case EditingPalette.LINE_TOOL:
				case EditingPalette.MEASURE_TOOL:
					 Settings.editing_window.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					 break;
				case EditingPalette.TEXT_TOOL:
					 Settings.editing_window.setCursor(new Cursor(Cursor.TEXT_CURSOR));
					 break;
				default:
					 Settings.editing_window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}// end of switch
		}// if the change is happening within the canvas
		else
			Settings.editing_window.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

		return;

	}// end of changeCursor()


	//*****************************************************************
	//*		S E T  I M A G E
	//*****************************************************************
	  public void setImage(Image img, int w, int h)
 	 {
  		if (img != null && w > 0 && h > 0)
  		{
		 		this.cur_image = img;

		 		if (w != this.image_width || h != this.image_height)
		 		{
					this.image_width = w;
					this.image_height = h;

					//***** Resize the canvas to match the image
					this.setSize(image_width, image_height);
				}// if the size of the new image is different than the old one

				repaint();
	 	 }

		return;

 	 }// end of setImage


	//*****************************************************************
	//*		G E T  I M A G E
	//*****************************************************************
	  public Image getImage()
  	{
  		Image		img = null;

  		img = this.cur_image;

  		return(img);

  	}// end of getImage


	//*****************************************************************
	//*		G E T  I M A G E  R E C T
	//*****************************************************************
	public Rectangle getImageRect()
	{
		Rectangle image_rect = null;

		return(image_rect);

	}// end of getImageRect()


	//***********************************************************************
	//*		D I S P L A Y  M E S S A G E
	//***********************************************************************
	void displayMessage(String msg)
	{
		Settings.editing_window.displayMessage(msg);
		return;
	}// end of displayMessage()


	//**************************************************************
	//*		C L E A R  O B J E C T
	//*		Modified 11/6/02 for v1.68
	//**************************************************************
	public void clearObject(Rectangle erase_rect)
	{
		try
		{
			new_object_array.clearObject(erase_rect);
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown in EditingCanvas.clearObject()!");
		}

		return;
	}// end of clearObject()


	//**************************************************************
	//*		C L E A R  R E C T
	//* 	Creates a new graphics environment which is clipped to
	//*		the erase_rect, then redraws the background into
	//*		that rectangle
	//**************************************************************
	public void clearRect(Rectangle erase_rect)
	{
		Graphics		g = null;
		Rectangle		local_erase_rect = new Rectangle(erase_rect);

		if (cur_image == null)
			return;

		try
		{
			g = getGraphics();
			if (g == null)
				return;

			//***** Erase a rect that's bigger by 2 pixels in each direction
			local_erase_rect.x -= 2;
			local_erase_rect.y -= 2;
			local_erase_rect.width += 4;
			local_erase_rect.height += 4;

			//***** Clip the region to the erase rect
			g.setClip(local_erase_rect.x, local_erase_rect.y, local_erase_rect.width,
				      local_erase_rect.height);

			//***** Redraw the image into the object region
			g.drawImage(cur_image, 0, 0, this);

			g.dispose();// get rid of the graphics context

		}
		catch (Exception e)
		{
			displayMessage("Exception thrown in EditingCanvas.clearRect()!");
		}

		return;

	}// end of clearRect()


	//********************************************************************************
	//*		C L E A R  C A N V A S
	//********************************************************************************
	public void clearCanvas() throws Exception
	{
		Rectangle	r = this.getBounds();
		Graphics	g = this.getGraphics();

		if (g != null && r != null)
		{
			g.setColor(Color.lightGray);
			if (r != null && g != null)
				g.fillRect(r.x, r.y, r.width, r.height);
		}

		return;

	}// end of clearCanvas()


	//********************************************************************************
	//*		G E T  V A L I D  P O I N T
	//*		Returns a point that is in the image area, even if the incoming point
	//*		is not.
	//********************************************************************************
	public Point getValidPoint(int incoming_x, int incoming_y)
	{
		Point			new_point = null;
		int				x = incoming_x, y = incoming_y;
		Rectangle		imaging_area = null;

		//***** Define the rectangle of the currently showing image
		imaging_area = new Rectangle (0,0, this.getSize().width, this.getSize().height);

		//***** Process the request if it's not in the image window
		if (!imaging_area.contains(x,y))
		{
			if (x < imaging_area.x)
				x = imaging_area.x;
			if (y < imaging_area.y)
				y = imaging_area.y;
			if (x > imaging_area.x + imaging_area.width)
				x = imaging_area.x + imaging_area.width;
			if (y > imaging_area.y + imaging_area.height)
				y = imaging_area.y + imaging_area.height;
		}// make sure it happened in the window

		new_point = new Point(x, y);

		return(new_point);

	}// end of getValidPoint()


	//**********************************************************************************
	//*		S A V E  C A R R I E D  O B J E C T S
	//*		This method makes sure that, depending on the user preferences,
	//*		the correct overlay objects are brought along when the user moves
	//*		to a new frame.
	//*
	//*		Modified 9/11/02 for v1.61
	//**********************************************************************************
	public void  saveCarriedObjects() throws Exception
	{
		switch (Settings.overlay_forward_pref)
		{
			case Settings.BRING_ALL_OBJECTS: // both old and new are brought forward
				 copyDiskObjectsToCarriedObjectArray();
				 break;
			case Settings.BRING_NEW_OBJECTS: // old objects are discarded
				 break;
			case Settings.BRING_NO_OBJECTS:  // old and new objects are both discarded
				 new_object_array.resetArray();
				 break;
		}//switch

		return;

	}// end of saveCarriedObjects()


	//**********************************************************************************
	//*		C O P Y  D I S K  O B J E C T S  T O  C A R R I E D  O B J E C T  A R R A Y
	//*		Created 9/11/02 for v1.63
	//**********************************************************************************
	public void  copyDiskObjectsToCarriedObjectArray() throws Exception
	{
		carried_object_array.resetArray();

		for (int i = 0; i < disk_object_array.getNumObjects(); i++)
			carried_object_array.addObject(disk_object_array.getObject(i));

		return;

	}// end of copyDiskObjectsToCarriedObjectArray()


	//**********************************************************************************
	//*		R E S T O R E  C A R R I E D  O B J E C T S
	//*		Created 9/11/02 for v1.63
	//**********************************************************************************
	public void  restoreCarriedObjects() throws Exception
	{
		for (int i = 0; i < carried_object_array.getNumObjects(); i++)
			new_object_array.addObject(carried_object_array.getObject(i));

		new_object_array.removeDuplicateObjects();
		carried_object_array.resetArray();

		return;

	}// end of restoreCarriedObjects()


	//*****************************************************
	//*		D E L E T E  D U P L I C A T E  A R R A Y  O B J E C T S
	//*		Searches the disk_object_array and the new_object_array
	//*		If it finds objects which appear in both arrays, it gests rid
	//*		of the copy in the new_object_array
	//*		Modified 11/6/02 for v1.68
	//*****************************************************
	public void deleteDuplicateArrayObjects() throws Exception
	{
		int					i = 0, j = 0;
		AnnotationObject	test_obj = null, current_obj = null;
		Vector				delete_array = new Vector(10,1);

		for (i = 0; i < disk_object_array.getNumObjects(); i++)
		{
			test_obj = disk_object_array.getObject(i);

			for (j = 0; j < new_object_array.getNumObjects(); j++)
			{
				current_obj = new_object_array.getObject(j);

				if (test_obj.isIdenticalObject(current_obj))
				{
					//***** If it's a duplicate, load it into the array to be deleted
					delete_array.addElement(current_obj);
					break;
				}// if it's equal
			}// for j
		}// for i

		//***** Delete all the duplicates
		for (i = 0; i < delete_array.size(); i++)
		{
			test_obj = (AnnotationObject)delete_array.elementAt(i);
			new_object_array.deleteObject(test_obj);
		}// for each delete object

		return;

	}// end of deleteDuplicateArrayObjects()



	//***********************************************************************
	//*		F I N I S H  O B J E C T
	//*		Added 3/12/03 for v1.72
	//***********************************************************************
	private  void  finishObject(int x, int y)
	{
		try
		{
			if (new_object != null)
			{
				new_object.finishObject(this.getGraphics(), x, y);
				new_object_array.doObjectComments(Settings.editing_window, new_object);
				new_object_array.addObject(new_object);
				new_object_array.checkForObjectValidity();
				new_object_array.restoreObjects();
				new_object = null;
			}
			if (this.unfinished_text_object)
				this.unfinished_text_object = false;
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while finishing new object!");
		}
		return;

	}// end of finishObject()


	//***********************************************************************
	//*		C L E A R  O L D  R O I
	//*		Modified 9/5/02 for v1.62
	//***********************************************************************
	private	void 	clearOldROI()
	{
		try
		{
			// get rid of the old one
			Rectangle erase_rect = roi.getBoundingRect();
			new_object_array.clearObject(erase_rect);
			this.roi = null;
		}
		catch (Exception e)
		{
		}

		return;

	}// end of clearOldROI()


	//***********************************************************************
	//*		C R E A T E  S E L E C T  T H R E A D
	//*		Creates a low-priority thread which will run in the background
	//*		and flash objects which are selected.
	//*		Modified 6/30/03 for v1.74
	//***********************************************************************
	public void	createSelectThread()
	{
		if (this.select_thread != null)
			return;

		//***** Create a runnable object to do the movie checking
		Runnable do_it = new Runnable()
		{
			public void run()
			{
				handleSelectedObjects();
			}
		};

		//***** Create a thread for the runnable object to run within
		this.select_thread = new Thread(do_it);

		//***** Set its priority low so that the window can update
		this.select_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the conversion thread running
		this.select_thread.start();

	}// end of createSelectThread()


	//*****************************************************************
	//*		H A N D L E  S E L E C T E D  O B J E C T S
	//*		A method to run in the canvas' select_thread that
	//*		will flash any selected objects
	//*		Modified 11/6/02 for v1.68
	//*****************************************************************
	public	void	handleSelectedObjects()
	{
		AnnotationObject	ao = null;
		boolean				keep_going = true, on = false;
		int 				i = 0;
		Graphics			g = null;

		try
		{
			while (keep_going)
			{
				if (g == null)
					g = getGraphics();

				if (select_thread == null)
				{
					keep_going = false;
					return;
				}

				if (disk_object_array == null || new_object_array == null)
					return;

				if (disk_object_array.getNumObjects() > 0 || new_object_array.getNumObjects() > 0)
				{
					keep_going = false; // won't be set to true unless there's an object to flash

					for (i = 0; i < disk_object_array.getNumObjects(); i++)
					{
						ao = disk_object_array.getObject(i);
						if (ao != null && ao.selected)
						{
							keep_going = true;
							if (g != null)
								ao.flashObject(g, on);// if "on" flash will be light
						}
					}// for each object

					for (i = 0; i < new_object_array.getNumObjects(); i++)
					{
						ao = new_object_array.getObject(i);
						if (ao != null && ao.selected)
						{
							keep_going = true;
							if (g != null)
								ao.flashObject(g, on);// if "on" flash will be light
						}
					}// for each object


				}// if we have objects

				Thread.sleep(250);// reset for 1/2 second
				Thread.yield();

				on = !on;// next time the flash will be dark

			}// while keep_going

			if (g != null)
				g.dispose();

			select_thread = null;

		}//try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception in handleSelectedObjects().");
		}

		return;

	}// end of handleSelectedObjects()


	//***********************************************************************
	//*		C R E A T E  I N S E R T  T H R E A D
	//*		Creates a low-priority thread which will run in the background
	//*		and flash the insert object during text object creation.
	//***********************************************************************
	private void	createInsertThread()
	{

		//***** Create a runnable object to do the movie checking
		Runnable do_it = new Runnable()
		{
			public void run()
			{
				handleInsertPointMarker();
			}
		};

		//***** Create a thread for the runnable object to run within
		this.insert_thread = new Thread(do_it);

		//***** Set its priority low so that the window can update
		this.insert_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the thread running
		this.insert_thread.start();

	}// end of createInsertThread()


	//*****************************************************************
	//*		H A N D L E  I N S E R T  P O I N T  M A R K E R
	//*		A method to run in the canvas' insert_thread that
	//*		will flash the insert object during text object creation
	//*		Last modifed 11/6/02 for v1.68
	//*****************************************************************
	private	void	handleInsertPointMarker()
	{
		TextObject	text_obj = null;
		Graphics	g = this.getGraphics();
		boolean		flash_on = true, drew = false;
		Point		old_start_pt = new Point(-1,-1);
		Point		old_end_pt = new Point(-1,-1);
		Point		start_pt = null;
		Point		end_pt = null;
		int			text_height = 0;


		if (!unfinished_text_object || g == null)
			return;

		try
		{
			flash_on = false;

			//***** Get the text object
			if (new_object != null)
				text_obj = (TextObject)new_object;
			else
				text_obj = (TextObject)new_object_array.getObject(new_object_array.getNumObjects() -1);

			if (text_obj == null)
				return;

			g.setXORMode(Color.white);

			do
			{
				start_pt = text_obj.getInsertPoint();
				end_pt = new Point(start_pt.x, start_pt.y + text_obj.getCharHeight());

				if (start_pt != null && end_pt != null)
				{

					//***** Erase old line if we're moving to a new location and it is drawn
					if ((start_pt.x != old_start_pt.x || start_pt.y != old_start_pt.y) && flash_on && drew)
					{
						//****** Erase the old insert point
						g.drawLine(old_start_pt.x,
							   	   old_start_pt.y,
							   	   old_end_pt.x,
							       old_end_pt.y);

						flash_on = false;
					}

					//****** Draw the insert point
					g.drawLine(start_pt.x,
						   	   start_pt.y,
						   	   end_pt.x,
						       end_pt.y);


					old_start_pt = start_pt;
					old_end_pt = end_pt;
					drew = true;

					//**** Flip the flash flag
					flash_on = !flash_on;

				}// if we have valid points

				try
				{
					insert_thread.sleep(350);
				}// try
				catch (InterruptedException ue){}


			}// do
			while (unfinished_text_object || flash_on);

			g.dispose();

		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while flashing text cursor!");
		}

		return;

	}// end of handleInsertPointMarker()


	//**********************************************************************************
	//*		S E T  O V E R L A Y  S T A T U S
	//*		This method simply calls EditingWindow.setOverlayStatus()
	//*		Modified 7/29/02 for v1.59
	//**********************************************************************************
	void setOverlayStatus(int status)
	{
		Settings.editing_window.setOverlayStatus(status);

		return;

	}//end of setOverlayStatus()


	//**********************************************************************************
	//*		S E L E C T  A L L
	//*		Added 6/30/03 for v1.74
	//**********************************************************************************
	void selectAll()
	{
		try
		{
			Settings.editing_canvas.disk_object_array.selectAll();
			Settings.editing_canvas.new_object_array.selectAll();
			Settings.editing_canvas.createSelectThread();

		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown in EditingWindow.selectAll()!");
		}

		return;

	}// end of selectAll()


}//End of EditingCanvas




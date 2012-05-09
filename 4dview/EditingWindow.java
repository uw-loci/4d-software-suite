import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class EditingWindow extends Frame
{

	//***** PALETTE CONSTANTS
	Vector				bookmark_array = null; // holds the bookmark entry objects
	EditObjectArray		combined_object_array = null;// stores objects when saving overlay

	EditingPanel 		edit_panel = null;

	GridBagLayout		gbl = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;


	//*********************************************************
	//*		I N I T
	//*		Modified 8/26/02 for v1.61
	//*		Modified 4/9/03 for v1.72
	//*********************************************************
	public EditingWindow()
	{
		Dimension	size = null;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(gbl);
		insets = new Insets(0,0,0,0);

 		this.bookmark_array = new Vector(10,1);

		if (EditingSettings.header_array == null)
			EditingSettings.header_array = new HeaderArray();

		setVisible(false);
		setSize(430,270);
		setResizable(false);
		setFont(new Font("Dialog", Font.PLAIN, 12));


		edit_panel = new EditingPanel();
		edit_panel.setSize(this.getSize().width, this.getSize().height);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;
		add(edit_panel);
		gbl.setConstraints(edit_panel, constraints);

		setTitle("Editing Window");

		//***** Tell it what events to receive
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		MyWindowAdapter window_adapter = new MyWindowAdapter();
		this.addWindowListener(window_adapter);

		MyMouseMotionAdapter mouse_motion_adapter = new MyMouseMotionAdapter();
		this.addMouseMotionListener(mouse_motion_adapter);

		MyKeyAdapter key_adapter = new MyKeyAdapter();
		this.addKeyListener(key_adapter);

		pack();

		setVisible(true);

		requestFocus();

		return;

	}// init()


	//*****************************************************************
	//*		S E T  V I S I B L E
	//*****************************************************************
    public void setVisible(boolean show)
	{
		super.setVisible(show);

		return;

	}// end of setVisible()


	//*****************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 8/28/02 v 1.61
	//*****************************************************************
	public void addNotify()
	{
	    // Record the size of the window prior to calling parents addNotify.
	    //Dimension d = getSize();

		super.addNotify();

		//if (fComponentsAdjusted)
		//	return;

		// Adjust components according to the insets
		//setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
		//Component components[] = getComponents();
		//for (int i = 0; i < components.length; i++)
		//{
		//	Point p = components[i].getLocation();
		//	p.translate(getInsets().left, getInsets().top);
		//	components[i].setLocation(p);
		//}
		//fComponentsAdjusted = true;

		return;

	}// end of addNotify()


    // Used for addNotify check.
	boolean fComponentsAdjusted = false;



	public void update(Graphics g)
	{
		super.update(g);
	}

	//**********************************************************************************
	//*		M Y  W I N D O W  A D A P T E R
	//**********************************************************************************
	class MyWindowAdapter extends WindowAdapter
	{
		public void windowDeiconified(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == EditingWindow.this)
				EditingWindow_WindowDeiconified(event);
		}

		public void windowOpened(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == EditingWindow.this)
				EditingWindow_WindowOpened(event);
		}

		public void windowActivated(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == EditingWindow.this)
				EditingWindow_WindowActivated(event);
		}

		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == EditingWindow.this)
				EditingWindow_WindowClosing(event);
		}
	}// end of MyWindowAdapter

	void EditingWindow_WindowDeiconified(java.awt.event.WindowEvent event){}
	void EditingWindow_WindowOpened(java.awt.event.WindowEvent event){}


	//**********************************************************************************
	//*		W I N D O W  A C T I V A T E D
	//*		Modified 8/29/02 for v1.61
	//**********************************************************************************
	void EditingWindow_WindowActivated(java.awt.event.WindowEvent event)
	{
		Settings.main_window.copyImageToEditingWindow();
		requestFocus();
		return;

	}// end of EditingWindow_WindowActivated()


	//**********************************************************************************
	//*		W I N D O W  C L O S I N G
	//**********************************************************************************
	void EditingWindow_WindowClosing(java.awt.event.WindowEvent event)
	{
		displayMessage("Closing Editing Window.");

		setVisible(false);		 // hide the Frame
		Settings.editing_window = null;
		Settings.editing_panel = null;
		Settings.editing_canvas = null;
		dispose();

		return;

	}// end of EditingWindow_WindowClosing()


	//**********************************************************************************
	//*		M Y  M O U S E  M O T I O N  A D A P T E R
	//**********************************************************************************
	class MyMouseMotionAdapter extends java.awt.event.MouseMotionAdapter
	{

		public void mouseMoved(java.awt.event.MouseEvent event)
		{
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

	}// end of class MyMouseMotionAdapter


	//**********************************************************************************
	//*		M Y  K E Y  A D A P T E R
	//**********************************************************************************
	class MyKeyAdapter extends java.awt.event.KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
			doKeyPressed(e);
		}
		public void keyTyped(KeyEvent e){}
		public void keyReleased(KeyEvent e){}

	}// end of class myKeyAdapter


	//**********************************************************************************
	//*		D O  K E Y  P R E S S E D
	//*		Modified 9/10/02 for v1.63
	//**********************************************************************************
	void doKeyPressed(java.awt.event.KeyEvent event)
	{
		int					key = 0;
		char				key_ch = 0;

		try
		{
			key_ch = event.getKeyChar();
			key = event.getKeyCode();

			if ((event.getModifiers() & event.CTRL_MASK) != 0) // if control key is pressed
			{
				switch (key_ch)
				{

				}

			}// if the control key was down
			else if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
			{

			}// if the alt key was down
			else if ((event.getModifiers() & event.META_MASK) != 0)
			{
				switch (key_ch)
				{
					case 's':// cmd-s was pressed
						 saveOverlay();
					 	 break;
					case 'e': // cmd-e was pressed
					  	 eraseOverlay();
					  	 break;
				}// switch

			}// if the cmd key was down
			else
			{
				switch (key)
				{
					case KeyEvent.VK_RIGHT:
						 moveOneFrame(true);
						 break;
					case KeyEvent.VK_LEFT:
						 moveOneFrame(false);
						 break;
					case KeyEvent.VK_UP:
						 moveOnePlane(true);
						 break;
					case KeyEvent.VK_DOWN:
						 moveOnePlane(false);
						 break;
				}// switch
			}// if it was a normal key press

			//***** Pass the key press along to the editing canvas
			edit_panel.editing_canvas.doKeyPressed(event);

		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown in EditingWindow.keyPressed()!");
		}
		return;

	}// end of keyPressed()


	//**********************************************************************************
	//*		D I S P L A Y  I M A G E
	//*		Modified 7/31/02 for v1.59
	//*		Modified 7/18/03 for v1.75
	//**********************************************************************************
	public	void displayImage(Image the_image)
	{
		int		image_width = 0, image_height = 0;


		if (the_image == null)
			return;

		try
		{
			//***** Get the size of the image
			do
			{
				image_width = the_image.getWidth(null);
				image_height = the_image.getHeight(null);

				try
				{
					Thread.sleep(100);
				}
				catch (InterruptedException ignore){}
			}
			while (image_height == -1 || image_width == -1);

			//**** Resize edit window
			Insets insets = getInsets();

			Settings.editing_canvas.setImage(the_image, image_width, image_height);

			//***** Set the size of the window and the editing panel
			this.setSize(ToolPalette.PALETTE_WIDTH + insets.left + (insets.right + image_width),
				  	  	  (insets.top + insets.bottom + image_height));
			edit_panel.setSize(this.getSize().width, this.getSize().height);
		}
		catch(Exception e)
		{
			displayMessage("Exception thrown in EditingWindow.displayMovie()!");
		}

		return;

	}// end of displayMovie()


	//**********************************************************************************
	//*		M O V E  O N E  F R A M E
	//*		Modified 9/16/02 for v1.64
	//*		Modified 2/19/03 for v1.71
	//**********************************************************************************
	public  void  moveOneFrame(boolean forward) throws Exception
	{
		try
		{
			Settings.editing_canvas.saveCarriedObjects();
			Settings.data_display_panel.moveOneFrame(forward);
 			Settings.editing_canvas.restoreCarriedObjects();
			Settings.editing_canvas.deleteDuplicateArrayObjects();
			edit_panel.displayDataInfo();
			setOverlayStatus();
		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown while changing frames!");
		}

		return;

	}// end of moveOneFrame()


	//**********************************************************************************
	//*		M O V E  O N E  P L A N E
	//*		Modified 9/16/02 for v1.64
	//*		Modified 2/19/03 for v1.71
	//**********************************************************************************
	public  void  moveOnePlane(boolean up)
	{
		try
		{
			Settings.editing_canvas.saveCarriedObjects();
			Settings.data_display_panel.moveOnePlane(up);
 			Settings.editing_canvas.restoreCarriedObjects();
			Settings.editing_canvas.deleteDuplicateArrayObjects();
			edit_panel.displayDataInfo();
			setOverlayStatus();
		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown while changing planes!");
		}

		return;

	}// end of moveOnePlane()



	//**********************************************************************************
	//*		S A V E  O V E R L A Y
	//*		Modified 8/22/02 for v1.61
	//*		Modified 2/19/03 for v1.71
	//*		Modified 3/12/03 for v1.72
	//**********************************************************************************
	void saveOverlay()
	{
		int					i = 0;

		try
		{
			displayMessage("Saving overlay to disk...");

			//***** Make the array, if necessary
			if (combined_object_array == null)
				combined_object_array = new EditObjectArray(Settings.editing_canvas);

			//***** Make sure we've gotten rid of any duplicates
			Settings.editing_canvas.deleteDuplicateArrayObjects();

			//***** Add disk objects to the combined array
			for (i = 0; i < Settings.editing_canvas.disk_object_array.getNumObjects(); i++)
				combined_object_array.addObject(Settings.editing_canvas.disk_object_array.getObject(i));

			//***** Add new objects to the combined array
			for (i = 0; i < Settings.editing_canvas.new_object_array.getNumObjects(); i++)
				combined_object_array.addObject(Settings.editing_canvas.new_object_array.getObject(i));

			OverlayOutput.saveOverlayToDisk(Settings.output_directory, combined_object_array, EditingSettings.header_array, DataInfo.cur_focal_plane, DataInfo.cur_frame);

			//***** Reset the array to be ready for next overlay saving
			combined_object_array.resetArray();

			if (Settings.advance_to_next_frame)
				moveOneFrame(true);

			setOverlayStatus(EditingSettings.OVERLAY_SAVED);

			displayMessage("Overlay saved!");

		}// try
		catch (ArrayIndexOutOfBoundsException aioobe)
		{
			displayMessage("ArrayIndexOutOfBoundsException thrown while saving overlay.");
			displayMessage("Overlay may not have been saved properly.");

		}// catch ArrayIndexOutOfBoundsException
		catch (Exception e)
		{
			displayMessage("Exception thrown while saving overlay.");
			displayMessage("Overlay may not have been saved properly.");

		}// catch Exception

		return;

	}// end of saveOverlay()


	//**********************************************************************************
	//*		E R A S E  O V E R L A Y
	//*		Modified 2/19/03 for v1.71
	//*		Modified 4/4/03 for v1.71
	//**********************************************************************************
	void eraseOverlay()
	{
		EraseOverlayDialog	eod = null;
		BooleanWrapper		confirm = new BooleanWrapper(false);
		try
		{
			eod = new EraseOverlayDialog(confirm, this, true);
			eod.setVisible(true);

			if (!confirm.getValue())
				return;

			//***** Erase the overlay
			displayMessage("Erasing overlay " + DataInfo.cur_frame + " from disk...");
			OverlayOutput.eraseOverlayFromDisk(Settings.output_directory, EditingSettings.header_array, DataInfo.cur_focal_plane, DataInfo.cur_frame);
			displayMessage("Done.");

			//***** Get rid of erased objects and restore any new objects
			resetAfterOverlayErase();

		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown while erasing overlay.");
			displayMessage("Overlay may not have been erased properly.");
		}
		return;

	}// end of eraseOverlay()


	//********************************************************************
	//*		C L O S E  D A T A  S E T
	//********************************************************************
	public void closeDataSet()
	{
		try
		{
			Settings.editing_canvas.clearCanvas();

			bookmark_array.removeAllElements();
			EditingSettings.header_array.resetArray();
			DataInfo.num_focal_planes = 0;
			DataInfo.num_frames = 0;
			DataInfo.cur_focal_plane = 0;
			DataInfo.cur_frame = 0;
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while closing data set. " + e.getMessage());
		}

		return;

	}// end of closeDataSet()


	//**********************************************************************************
	//*		S E T  O V E R L A Y  S T A T U S
	//*		Modified 8/22/02 for v1.61
	//**********************************************************************************
	void setOverlayStatus()
	{
		if (Settings.editing_canvas.new_object_array.getNumObjects() > 0)
			setOverlayStatus(EditingSettings.OVERLAY_IS_DIRTY);
		else
			setOverlayStatus(EditingSettings.OVERLAY_SAVED);

		return;

	}//end of setOverlayStatus()


	//**********************************************************************************
	//*		S E T  O V E R L A Y  S T A T U S
	//*		This method will set EditingSettings.overlay_status
	//*		and also update the overlay status panel to reflect this new status
	//*		If "dirty" is true the overlay has been changed but not saved.
	//*		Modified 7/29/02 for v1.59
	//**********************************************************************************
	void setOverlayStatus(int status)
	{
		try
		{
			EditingSettings.overlay_status = status;
			edit_panel.overlay_status_panel.repaint();

		}
		catch (Exception e)
		{
			displayMessage("Overlay status was not set properly.");
		}

		return;

	}//end of setOverlayStatus()


	//**********************************************************************************
	//*		R E S E T  A F T E R  O V E R L A Y  E R A S E
	//*		Added 4/2/03 for v1.72
	//**********************************************************************************
	void resetAfterOverlayErase() throws Exception
	{
		Settings.editing_canvas.disk_object_array.resetArray();

		//***** Draw any overlay objects
		Settings.editing_canvas.new_object_array.restoreObjects();

		return;

	}// end of resetAfterOverlayErase()


	//**********************************************************************************
	//*		D I S P L A Y  M E S S A G E
	//**********************************************************************************
	void displayMessage(String msg)
	{
		Settings.data_display_panel.displayMessage(msg);
		return;

	}// end of displayMessage()


}// end of EditingWindow class


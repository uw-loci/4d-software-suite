import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;



public class TiffPanel extends DataDisplayPanel
{

	final	static	 String		SAMPLE_DATA_SET_URL = "http://www.loci.wisc.edu/4d/java/ViewerApplet/SampleDataSet/";


	//***** SLIDER CONSTANTS
	final	static		int			SLIDER_HEIGHT = 15;

	TiffCanvas 			tiff_canvas = null;
	DataSlider			data_slider = null;

	TiffReader			cur_reader = null;
	Image				cur_image = null;

	//GridBagLayout		gbl = null;
	//GridBagConstraints	constraints = null;
	//Insets				insets = null;

	Thread				play_thread = null;

	boolean				keep_checking = false;

	//******************************************************
	//*		I N I T
	//*		Modified 10/18/02 for v1.67
	//*		Modified 6/5/03 for v1.72
	//*		Modified 7/24/03 for v1.75
	//******************************************************
	public TiffPanel()
	{

		//gbl = new GridBagLayout();
		//constraints = new GridBagConstraints();
		//setLayout(gbl);
		//insets = new Insets(0,0,0,0);

		setVisible(true);
		setSize(400,315);

		initPanelVariables();

		tiff_canvas = new TiffCanvas();
		tiff_canvas.setSize(400,300);
		tiff_canvas.setLocation(0,0);
		//constraints.gridx = 0;
		//constraints.gridy = 0;
		//constraints.gridwidth = 1;
		//constraints.gridheight = 1;
		//constraints.anchor = GridBagConstraints.NORTHWEST;
		//constraints.fill = GridBagConstraints.NONE;
		//constraints.weightx = 0;
		//constraints.weighty = 0;
		//insets.top = 0;
		//insets.bottom = 0;
		//insets.left = 0;
		//insets.right = 0;
		//constraints.insets = insets;
		add(tiff_canvas);
		//gbl.setConstraints(tiff_canvas, constraints);
		Settings.movie_canvas = null;
		Settings.tiff_canvas = tiff_canvas;

		data_slider = new DataSlider(this);
		data_slider.setSize(getSize().width, SLIDER_HEIGHT);
		data_slider.setLocation(0, tiff_canvas.canvas_height);
		//constraints.gridx = 0;
		//constraints.gridy = 1;
		//constraints.gridwidth = 1;
		//constraints.gridheight = 1;
		//constraints.anchor = GridBagConstraints.SOUTHWEST;
		//constraints.fill = GridBagConstraints.HORIZONTAL;
		//constraints.weightx = 0;
		//constraints.weighty = 0;
		//insets.top = 0;
		//insets.bottom = 0;
		//insets.left = 0;
		//insets.right = 0;
		//constraints.insets = insets;
		add(data_slider);
		//gbl.setConstraints(data_slider, constraints);
		data_slider.setVisible(true);

		MyComponentListener component_listener = new MyComponentListener();
		this.addComponentListener(component_listener);

		return;

	}// init()


	//*************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 10/15/02 for v1.66
	//*************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify();

	//********************************************************************************
	//*		U P D A T E
	//********************************************************************************
	public void update(Graphics g)
	{

		super.update(g);

		return;

	}// end of update()


	//*************************************************************
	//*		P A I N T
	//*		Modified 10/22/02 for v1.68
	//*************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);


		try
		{
			tiff_canvas.restoreObjects();
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while restoring objects!");
		}

		displayDataSetInfo();


		Rectangle b = this.getBounds();
		g.setColor(Color.black);
		g.drawRect(1, 1, this.size().width - 2, this.size().height - 2);

		return;

	}// end of paint()


	//*************************************************************
	//*		S E T  S I Z E
	//*		Added 10/22/02 for v1.68
	//*************************************************************
	public void setSize(Dimension d)
	{
		setSize(d.width, d.height);

		return;

	}// end of setSize()


	//*************************************************************
	//*		S E T  S I Z E
	//*		Modified 11/11/02 for v1.69
	//*************************************************************
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		if (width <= 1 || height <= 1)
			Settings.main_window.displayMessage("Tiff Panel is illegal size!");

		panel_width = width;
		panel_height = height;

		return;

	}// end of setSize()


	//*************************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 10/17/02 for v1.66
	//*		Modified 10/22/02 for v1.68
	//*************************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(panel_width, panel_height));

	}// end of getPreferredSize()


	//**********************************************************************************
	//*		M Y  C O M P O N E N T  L I S T E N E R
	//*		Added 10/18/02 for v1.67
	//**********************************************************************************
	class MyComponentListener extends ComponentAdapter
	{
		public void componentMoved(ComponentEvent event)
		{
			handleLayout();
			return;

		}// end of componentMoved()

		public void componentResized(ComponentEvent event)
		{
			handleLayout();
			return;

		}// end of componentResized()

	}// end of class MyComponentListener


	//**********************************************************************************
	//*		H A N D L E  L A Y O U T
	//*		Added 10/18/02 for v1.67
	//*		Modified 10/22/02 for v1.68
	//*		Modified 5/12/03 for v1.72
	//*		Modified 7/25/03 for v1.75
	//**********************************************************************************
	public void handleLayout()
	{

		try
		{
			//gbl.layoutContainer(this);

			this.setSize(tiff_canvas.canvas_width, tiff_canvas.canvas_height + DataSlider.SLIDER_HEIGHT);
			tiff_canvas.setLocation(0,0);
			data_slider.setSize(tiff_canvas.canvas_width, DataSlider.SLIDER_HEIGHT);
			data_slider.setLocation(0, tiff_canvas.canvas_height);
			//Settings.main_window.displayMessage("Laying out TiffPanel.");

		}
		catch (Exception e)
		{
			displayMessage("Exception in MoviePanel.handleLayout().");
		}

		return;

	}// end of handleLayout()


	//*************************************************************
	//*		L O A D  D A T A  S E T
	//*		Modified 10/22/02 for v1.68
	//*		Modified 5/6/03 for v1.72
	//*************************************************************
	public void	loadDataSet()
	{
		TiffReader		reader = null;
		String			filename = null;
		int				file_num = 0;


		try
		{
			displayMessage("Opening data set.  Please wait...");

			tiff_canvas.clearCanvas();

			for (int i = 0; i < DataInfo.num_focal_planes; i++)
			{
				file_num = i + 1;
				filename = NameUtils.createTiffName(DataInfo.base_file_name, file_num);
				displayMessage("Opening file " + file_num + " of " + DataInfo.num_focal_planes + ".  Please wait...");

				reader = new TiffReader(filename);

				if (reader != null)
				{
					data_array.addElement(reader);
					if (i == 0)
					{
						cur_reader = reader;
						cur_image = cur_reader.getSlice(1);
						displayImage(cur_image, DataInfo.data_width, DataInfo.data_height);
						DataInfo.cur_focal_plane = 1;
						DataInfo.cur_frame = 1;

					}
				}// if we have a movie
			}// for each focal plane

			if (Settings.display_overlays)
			{
				readOverlayHeaders();
				readOverlay();
			}// if we're displaying overlays

			handleLayout();
			setupDataSlider();
			displayDataInfo();
			tiff_canvas.restoreObjects();

		}// try
		catch (CancelledException ce)
		{
			displayMessage("User cancelled while loading data set.");
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while loading data set.");
		}// catch

		return;

	}// end of loadDataSet()


/*
	//*************************************************************
	//*		L O A D  S A M P L E  D A T A  S E T
	//*		Modified 10/17/02 for v1.66
	//*************************************************************
	public void	loadSampleDataSet()
	{
		String			filename = null;
		URL				url = null;
		int				movie_num = 0;

		try
		{

			url = new URL(SAMPLE_DATA_SET_URL + "4D%20Format%20File");

			//***** Open the 4D Format File and get info
			Input.read4DFormatFile(url);

			displayMessage("Opening data set.  Please wait...");

			for (int i = 0; i < DataInfo.num_focal_planes; i++)
			{
				movie_num = i + 1;
				filename = Input.makeMovieFilename(DataInfo.base_file_name, movie_num);
				displayMessage("Opening movie " + movie_num + " of " + DataInfo.num_focal_planes + ".  Please wait...");
				url = new URL(SAMPLE_DATA_SET_URL + filename + ".mov");
				cur_reader = Input.loadMovie(url);

				if (cur_reader != null)
				{
					if (i == 0)
					{
						getMovieDimensions(cur_reader);
						displayMovie(cur_reader, DataInfo.data_width, DataInfo.data_height);
						DataInfo.cur_focal_plane = 1;
					}
					tiff_array.addElement(cur_reader);
				}// if we have a movie
				else
					throw (new Exception("Null movie was returned by loadMovie()"));
			}// for each focal plane

			if (Settings.display_overlays)
			{
				readOverlayHeaders();
				readOverlay();
			}

			Settings.main_window.gbl.layoutContainer(Settings.main_window);

		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown during sample data set loading! ");
		}

		return;

	}// end of loadSampleDataSet()
*/

	//**********************************************************************
	//*		D I S P L A Y  I M A G E
	//*		Modified 5/1/03 for v1.72
	//*		Modified 7/18/03 for v1.75
	//**********************************************************************
	public void displayImage(Image img) throws Exception
	{

		if (img == null)
			return;

        do
        {
        	DataInfo.data_width = img.getWidth(null);
        	DataInfo.data_height = img.getHeight(null);

        	try
        	{
        		Thread.sleep(100);
        	}
        	catch (InterruptedException ignore){}
        }
        while (DataInfo.data_width == -1 || DataInfo.data_height == -1);

  		displayImage(img, DataInfo.data_width, DataInfo.data_height);

		return;

	}// end of displayImage()


	//**********************************************************************
	//*		D I S P L A Y  I M A G E
	//*		Modified 5/6/03 for v1.72
	//**********************************************************************
	public void displayImage(Image img, int data_width, int data_height) throws Exception
	{

		if (img == null)
			return;

		if (data_width <= 0 || data_height <= 0)
			throw new Exception("Illegal image size in displayImage()!");

		//***** Set the canvas size
		if (data_width != tiff_canvas.getWidth() || data_height != tiff_canvas.getHeight())
		{
			handleNewDataSize(data_width, data_height);
		}// if we're displaying an image that's a different size than the canvas

  		tiff_canvas.displayImage(img, data_width, data_height);

		//***** Configure the slider
		data_slider.setValue((int)DataInfo.cur_frame);

		//**** Make the check thread
		if (this.check_thread == null)
			createCheckThread();

		return;

	}// end of displayImage()


	//**********************************************************************
	//*		H A N D L E  N E W  D A T A  S I Z E
	//*		Modified 5/12/03 for v1.72
	//**********************************************************************
	protected void handleNewDataSize(int width, int height)
	{
		//***** Make the movie canvas the right size
		tiff_canvas.setSize(width, height);

		//***** Make this panel the right size
		setSize(width, height + SLIDER_HEIGHT);

		Settings.main_window.displayMessage("New data size, w: " + width + " h: " + height);

		handleLayout();
		Settings.main_window.handleLayout();
		Settings.main_window.pack();

		return;

	}// end of handleNewDataSize()


	//**********************************************************************
	//*		S E T U P  D A T A  S L I D E R
	//*		Created 8/29/02 for v1.62
	//*		Modified 10/16/02 for v1.66
	//*		Modified 7/9/03 for v1.74
	//**********************************************************************
	private void setupDataSlider() throws Exception
	{
		if (this.data_slider == null)
		{
			this.data_slider = new DataSlider(this);
			this.data_slider.setMinimum(1);
			this.data_slider.setMaximum((int)DataInfo.num_frames);
			this.data_slider.setValue((int)DataInfo.cur_frame);
			this.data_slider.setPageIncrement((int)DataInfo.num_frames/10);
		}
		else
		{
			this.data_slider.setVisible(true);
			this.data_slider.setMinimum(1);
			this.data_slider.setMaximum((int)DataInfo.num_frames);
			this.data_slider.setValue((int)DataInfo.cur_frame);
			this.data_slider.setPageIncrement((int)DataInfo.num_frames/10);
			this.data_slider.drawTheSlider(null);
		}

		return;

	}// end of setupDataSlider()



	//*************************************************************************
	//*		G O  T O
	//*		Modified 9/16/02 for v1.64
	//*		Modified 5/6/03 for v1.72
	//*************************************************************************
	public void  goTo(int  plane, long frame)
	{
		boolean		new_plane = false;

		try
		{
			if (this.data_array.size() <= 0)
			{
				displayMessage("No data set available!");
				return;
			}

			if (DataInfo.data_playing)
				stop();

			//***** Manage input info
			if (plane == DataInfo.cur_focal_plane && frame == DataInfo.cur_frame)
				return;
			if (plane < 1)
				plane = 1;
			if (plane > DataInfo.num_focal_planes)
				plane = DataInfo.num_focal_planes;
			if (frame < 1)
				frame = 1;
			if (frame > DataInfo.num_frames)
				frame = DataInfo.num_frames;

			if (plane != DataInfo.cur_focal_plane)
				new_plane = true;

			//****** Get the proper tiff image
			cur_reader = (TiffReader)data_array.elementAt(plane - 1);

			//****** Display the image
			cur_image = cur_reader.getSlice(frame);
			displayImage(cur_image, DataInfo.data_width, DataInfo.data_height);

			//***** Show where we are
			DataInfo.cur_frame = frame;
			DataInfo.cur_focal_plane = plane;
			displayDataInfo();

			if (new_plane)
				readOverlayHeaders();

			if (Settings.display_overlays)
			{
				if (Settings.editing_window != null)
					Settings.editing_canvas.saveCarriedObjects();

				readOverlay();
			}

			//***** Update the slider
			this.data_slider.setValue((int)DataInfo.cur_frame);

			//***** Draw any overlay objects, or left-over objects
			tiff_canvas.restoreObjects();

			if (Settings.editing_window != null)
			{
				copyCurrentFrameToEditingWindow();
				copyObjectsToEditingWindow();
 				Settings.editing_canvas.restoreCarriedObjects();
				Settings.editing_canvas.deleteDuplicateArrayObjects();
				Settings.editing_window.edit_panel.displayDataInfo();
			}

		}// try
		catch (Exception e)
		{
			displayMessage("Unable to move to requested location!");
		}
		return;

	}// end of goTo()


	//*************************************************************************
	//*		M O V E  O N E  F R A M E
	//*************************************************************************
	public  void moveOneFrame(boolean forward)
	{
		moveOneFrame(cur_reader, forward);
		return;

	}// end of moveOneFrame()


	//*************************************************************************
	//*		M O V E  O N E  F R A M E
	//*		Modified 9/16/02 for v1.64
	//*		Modified 5/12/03 for v1.72
	//*************************************************************************
	public  void  moveOneFrame(TiffReader the_reader, boolean forward)
	{
		boolean		overlay_found = false;
		Image		img = null;

		if (the_reader == null)
			return;

		try
		{
			//if (DataInfo.data_playing)
			//	stop();


			if (forward)
			{
				if (DataInfo.cur_frame + 1 > DataInfo.num_frames)
					return;
				img = the_reader.getSlice(++DataInfo.cur_frame);
			}
			else
			{
				if (DataInfo.cur_frame - 1 <= 0)
					return;
				img = the_reader.getSlice(--DataInfo.cur_frame);
			}

			//***** Display the image
			if (img != null)
				displayImage(img, DataInfo.data_width, DataInfo.data_height);
			else
				throw new Exception("Unable to extract new frame!");

			//if (forward)
			//	DataInfo.cur_frame += 1;
			//else
			//	DataInfo.cur_frame -= 1;


			if (Settings.display_overlays)
				readOverlay();

			//***** Draw any overlay objects
			tiff_canvas.restoreObjects();

			//***** Show where we are
			displayDataInfo();

			//***** Update the slider
			data_slider.setValue((int)DataInfo.cur_frame);

			if (Settings.editing_window != null)
			{
				copyCurrentFrameToEditingWindow();
				copyObjectsToEditingWindow();
			}

		}// try
		catch (Exception e)
		{
			displayMessage("Error moving movie frame.");
		}

		return;

	}// end of moveOneFrame()


	//*************************************************************************
	//*		M O V E  O N E  P L A N E
	//*		Modified 10/22/02 for v1.68
	//*		Modified 5/5/03 for v1.72
	//*************************************************************************
	public  void  moveOnePlane(boolean move_up)
	{
		boolean		was_playing = false;
		boolean		overlay_found = false;

		try
		{
			if (DataInfo.data_playing)
			{
				stop();
				was_playing = true;
			}

			//***** Get the new TiffReader
			if (move_up)
			{
				if (DataInfo.cur_focal_plane + 1 > DataInfo.num_focal_planes)
					return;
				cur_reader = (TiffReader)data_array.elementAt(++DataInfo.cur_focal_plane - 1);
			}// if we're increasing the focal plane
			else
			{
				if (DataInfo.cur_focal_plane - 1 <= 0)
					return;
				cur_reader = (TiffReader)data_array.elementAt(--DataInfo.cur_focal_plane - 1);
			}// if we're decreasing the focal plane

			//***** Get the new image and display it
			cur_image = cur_reader.getSlice(DataInfo.cur_frame);
			displayImage(cur_image, DataInfo.data_width, DataInfo.data_height);

			//if (move_up)
			//	DataInfo.cur_frame +=1;
			//else
			//	DataInfo.cur_frame -=1;

			if (Settings.display_overlays)
			{
				readOverlayHeaders();
				readOverlay();
			}

			//***** Draw any overlay objects, or left-over objects
			tiff_canvas.restoreObjects();

			//***** Show where we are
			displayDataInfo();

			if (was_playing)
				play(DataInfo.data_playing_forward);


			if (Settings.editing_window != null)
			{
				copyCurrentFrameToEditingWindow();
				copyObjectsToEditingWindow();
			}

		}// try
		catch (Exception e)
		{
			displayMessage("Exception thrown in MoviePanel.moveOnePlane().");
		}

		return;

	}// end of moveOnePlane()


	//********************************************************************
	//*		G E T  C U R R E N T  F R A M E
	//********************************************************************
	public long	getCurrentFrame()
	{
		return(DataInfo.cur_frame);
	}// end of getCurrentFrame()


	//********************************************************************
	//*		G E T  C U R R E N T  P L A N E
	//********************************************************************
	public int	getCurrentPlane()
	{
		return(DataInfo.cur_focal_plane);
	}// end of getCurrentPlane


	//********************************************************************
	//*		D I S P L A Y  M O V I E  I N F O
	//*		Writes the current frame and plane onto the position panel
	//********************************************************************
	public void  displayDataInfo()
	{

		if (Settings.movie_position_panel != null)
			Settings.movie_position_panel.setPosition(DataInfo.cur_frame, DataInfo.cur_focal_plane);

		else
			displayMessage("Unable to display movie position info!");

		return;

	}// end of displayDataInfo();


	//********************************************************************
	//*		P L A Y
	//********************************************************************
	public void  play(boolean forward) throws Exception
	{
		final	boolean		direction_is_forward = forward;

		Runnable do_it = new Runnable()
		{
			public void run()
			{
				DataInfo.keep_playing = true;
				playData(direction_is_forward);
			}
		};

		//***** Create a thread for the runnable object to run within
		play_thread = new Thread(do_it);

		//***** Set its priority
		play_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the conversion thread running
		play_thread.start();
	}// end of play()


	//********************************************************************
	//*		P L A Y  D A T A
	//*		Added 5/9/03 for v1.72
	//*		Modified 5/15/03 for v1.72
	//********************************************************************
	private	void	playData(boolean forward)
	{
		try
		{
			DataInfo.data_playing = true;

			while (DataInfo.keep_playing)
			{
				moveOneFrame(forward);
				Thread.yield();
			}// while

			DataInfo.data_playing = false;
			play_thread = null;

		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while playing data.");
			DataInfo.data_playing = false;
		}


		return;

	}// end of playData()


	//**************************************************
	//*		S T O P
	//*		Modified 5/9/03 for v1.72
	//*		Modified 9/4/03 for v1.76
	//**************************************************
	public void	stop() throws Exception
	{

		DataInfo.keep_playing = false;
		DataInfo.data_playing = false;
		Settings.movie_palette.resetPlayButtons();

		DataInfo.cur_frame = getCurrentFrame();
		displayDataSetInfo();

		return;

	}// end of stop()


	//*******************************************************
	//*		S A V E  C U R R E N T  F R A M E  A S  T I F F
	//*******************************************************
	public	void	saveCurrentFrameAsTiff()
	{
		Image				tiff_image = null;
		int					width = -1, height = -1;

		try
		{
			if (cur_reader == null)
				throw new Exception();

		}//try
		catch	(Exception e)
		{
			displayMessage("Unable to current frame as TIFF. " + e.getMessage());
		}

		return;

	}// end of saveMovieImageAsTiff();


	//**********************************************************
	//*		C O P Y  I M A G E  T O  E D I T I N G  W I N D O W
	//*		Modified 8/22/02 for v1.61
	//*		Modified 5/9/03 for v1.72
	//*********************************************************
	public	void	copyCurrentFrameToEditingWindow()
	{
		Image		tiff_image = null;


		try
		{
			if (cur_reader == null)
				return;

			//****** Get the image
			tiff_image = cur_reader.getSlice(DataInfo.cur_frame);

			//***** Make the editing window, if it's not there
			if (Settings.editing_window == null)
			{
				Settings.editing_window = new EditingWindow();
				Settings.editing_window.setVisible(true);
			}

			//***** Display the image in the editing window
			Settings.editing_window.displayImage(tiff_image);

		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while copying image to editing window. " + e.getMessage());
		}

		return;

	}// end of copyImageToEditingWindow()



	//***********************************************************************
	//*		C O P Y  O B J E C T S  T O  E D I T I N G  W I N D O W
	//*		Modified 8/22/02 for v1.61
	//***********************************************************************
	public void copyObjectsToEditingWindow() throws Exception
	{

		if (Settings.editing_window == null || tiff_canvas == null)
			return;

		if (Settings.editing_canvas != null)
		{
			Settings.editing_canvas.disk_object_array.resetArray();

			for (int i = 0; i < tiff_canvas.object_array.getNumObjects(); i++)
				Settings.editing_canvas.disk_object_array.addObject(tiff_canvas.object_array.getObject(i));
		}// if we have an editing canvas

		return;

	}// end of copyObjectsToEditingWindow()


	//***********************************************************************
	//*		C R E A T E  C H E C K  T H R E A D
	//*		Creates a low-priority thread which will run in the background
	//*		and check the movie for certain things such as hitting
	//*		the beginning or end so that looping can be done, etc.
	//***********************************************************************
	public void	createCheckThread()
	{
		//***** Create a runnable object to do the movie checking
		Runnable do_it = new Runnable()
		{
			public void run()
			{
				checkData();
			}
		};

		//***** Create a thread for the runnable object to run within
		check_thread = new Thread(do_it);

		//***** Set its priority
		check_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the conversion thread running
		check_thread.start();

	}// end of createCheckThread()



	//***********************************************************************
	//*		C H E C K  D A T A
	//*		Modified 4/25/03 for v1.72
	//*		Modified 7/8/03 for v1.74
	//***********************************************************************
	public void checkData()
	{

		try
		{

			keep_checking = true;

			while (keep_checking)
			{
				if (cur_reader == null)
				{
					keep_checking = false;
					break;
				}

				if (DataInfo.data_playing)
				{
					//***** Look for the end of the data set being reached
					if (DataInfo.cur_frame == DataInfo.num_frames)
					{
						if (DataInfo.data_display_mode == DataInfo.DEFAULT_MODE)
						{
							//***** Stop the data
							stop();
						}// if we're in the default mode
						else if (DataInfo.data_display_mode == DataInfo.LOOP_MODE)
						{
							//***** Reset the position
							if (!DataInfo.data_playing_forward)
							{
								DataInfo.cur_frame = 1;
							}// reset to end of data set
							else
							{
								DataInfo.cur_frame = DataInfo.num_frames;
							}// reset to beginning of data set
						}
						else if (DataInfo.data_display_mode == DataInfo.BOUNCE_MODE)
						{
							play(!DataInfo.data_playing_forward);

						}// if we're bouncing

					}// if the data set is at the end

					else
					{
						//***** Show the data info
						displayDataInfo();

						//***** Update the slider
						data_slider.setValue((int)DataInfo.cur_frame);

					}// if the data set's not to the end yet

					Thread.yield();//sleep(200);

				}// if the data's playing
				else
				{
					Thread.yield();//sleep(500);

				}// if the data's not playing

			}// while keep_checking
		}
		catch (Exception e)
		{
			displayMessage("Exception during checkMovie(): ");
			displayMessage(e.getMessage());
		}

		return;

	}// end of checkMovie()


	//********************************************************************
	//*		C L O S E  D A T A  S E T
	//*		Modified 12/10/02 for v1.70
	//*		Modifed 5/12/03 for v1.72
	//*		Modified 7/9/03 for v1.74
	//********************************************************************
	public void closeDataSet() throws Exception
	{

		cur_reader = null;
		EditingSettings.header_array.resetArray();
		data_array.removeAllElements();
		bookmark_array.resetArray();

		clearPanel();

		DataInfo.data_width = -1;
		DataInfo.data_height = -1;
		DataInfo.num_focal_planes = 0;
		DataInfo.num_frames = 0;
		DataInfo.frame_duration = 0;
		DataInfo.cur_focal_plane = 0;
		DataInfo.cur_frame = 0;
		DataInfo.base_file_name = new String("");

		Settings.movie_position_panel.setPosition(0,0);

		keep_checking = false;
		check_thread = null;
		play_thread = null;

		tiff_canvas.closeDataSet();
		//data_slider.setSize(tiff_canvas.getSize().width, SLIDER_HEIGHT);
		handleLayout();
		Settings.main_window.handleLayout();

		return;

	}// end of closeDataSet()


	//********************************************************************************
	//*		C L E A R  P A N E L
	//*		Added 12/10/02 for v1.70
	//********************************************************************************
	public void clearPanel()
	{
		Rectangle	r = this.getBounds();
		Graphics	g = this.getGraphics();

		if (g != null && r != null)
		{
			g.setColor(Color.white);
			g.fillRect(r.x, r.y, r.width, r.height);

			g.dispose();
		}

		return;

	}// end of clearPanel()


	//********************************************************************
	//*		I N I T  P A N E L  V A R I A B L E S
	//********************************************************************
	protected void initPanelVariables()
	{

		super.initPanelVariables();

		this.tiff_canvas = null;
		this.data_slider = null;
		this.cur_reader = null;
		this.cur_image = null;
       	this.data_array = new Vector(10,1);
       	this.play_thread = null;

		return;

	}// end of initPanelVariables()



	//********************************************************************
	//*		R E A D  O V E R L A Y
	//*		Modified 8/22/02 for v1.61
	//********************************************************************
	protected void readOverlay()
	{
		try
		{
			OverlayInput.readOverlayFromDisk(Input.getInputDirectory(),
											 tiff_canvas.object_array,
											 EditingSettings.header_array,
											 DataInfo.cur_focal_plane,
											 DataInfo.cur_frame);
		}// try
		catch (Exception e)
		{
		}

		return;
	}// end of readOverlay()


	//********************************************************************
	//*		R E A D  O V E R L A Y  H E A D E R S
	//*		Modified 8/22/02 for v1.61
	//********************************************************************
	protected void readOverlayHeaders()
	{
		try
		{
			OverlayInput.readOverlayHeaders(Input.getInputDirectory(),
										    EditingSettings.header_array,
										    DataInfo.cur_focal_plane);
		}
		catch (Exception e)
		{
		}
		return;

	}// end of readOverlayHeaders()


	//********************************************************************
	//*		D I S P L A Y  M E S S A G E
	//********************************************************************
	protected void displayMessage(String msg)
	{
		if (Settings.main_window != null)
			Settings.main_window.displayMessage(msg);

		return;

	}// end of displayMessage()


}// end of TiffPanel class


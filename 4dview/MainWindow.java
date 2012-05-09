/*
		4 D  V I E W E R / J A V A

		     v 1.79
		    10/17/03

		written by Charles Thomas
		Laboratory for Optical and Computational Instrumentation (LOCI)
		University of Wisconsin - Madison
		1675 Observatory Dr.
		Madison, WI  53706

		cthomas@facstaff.wisc.edu
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import quicktime.*;
import quicktime.io.*;
import quicktime.std.movies.*;
import quicktime.std.movies.media.*;

public class MainWindow extends Frame implements ItemListener
{

	final	static String		version = "1.79";

	//***** Used for addNotify check.
	boolean components_adjusted = false;

	//***** Other class variables
	MoviePalette		movie_palette = null;
	PositionPanel		position_panel = null;
	DataDisplayPanel	data_display_panel = null;
	TitleCanvas			title_canvas = null;
	MessagesPanel		messages_panel = null;

	//***** Menu stuff
	MenuBar mainMenuBar = null;

	//***** File
	Menu file_menu = null;
	MenuItem new_menu_item = null;
	MenuItem open_menu_item = null;
	MenuItem open_sample_item = null;
	MenuItem close_menu_item = null;
	MenuItem save_menu_item = null;
	MenuItem saveas_menu_item = null;
	MenuItem page_setup_menu_item = null;
	MenuItem print_menu_item = null;
	MenuItem save_settings_menu_item = null;
	MenuItem quit_menu_item = null;

	//***** Edit
	Menu edit_menu = null;
	MenuItem undo_menu_item = null;
	MenuItem cut_menu_item = null;
	MenuItem copy_menu_item = null;
	MenuItem paste_menu_item = null;
	MenuItem clear_menu_item = null;
	MenuItem selectall_menu_item = null;

	//***** Movie
	Menu movie_menu = null;
	Menu play_mode_menu = null;
		CheckboxMenuItem default_mode_menu_item = null;
		CheckboxMenuItem loop_mode_menu_item = null;
		CheckboxMenuItem bounce_mode_menu_item = null;
	MenuItem clear_messages_menu_item = null;
	MenuItem speed_menu_item = null;
	MenuItem goto_menu_item = null;
	MenuItem save_snapshot_menu_item = null;
	MenuItem export_images_as_movie_item = null;


	//***** Annotate
	Menu annotate_menu = null;
	CheckboxMenuItem display_overlays_menu_item = null;
	MenuItem set_scale_menu_item = null;
	MenuItem ann_prefs_menu_item = null;
	MenuItem find_text_menu_item = null;
	MenuItem save_overlay_menu_item = null;
	MenuItem erase_overlay_menu_item = null;

	//***** Help
	Menu help_menu = null;
	MenuItem about_menu_item = null;


	GridBagLayout		gbl = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;

	//*******************************************************************
	//*		I N I T
	//*		Modified 11/11/02 for v1.69
	//*		Modified 7/16/03 for v1.75
	//*		Modified 9/4/03 for v1.76
	//*******************************************************************
	public MainWindow()
	{

		//***** Set global variables
		Settings.main_window = this;
		Settings.editing_window = null;
		Settings.bookmarks_window = null;

		Settings.running_as_applet = ViewerApplet.getInstance() != null;

		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		gbl = new GridBagLayout();
		setLayout(gbl);

		setVisible(false);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(Color.lightGray);
		setTitle("4D Viewer/Java v" + version);
		setSize(400,530);
		setResizable(false);

		data_display_panel = new MoviePanel();
		Settings.data_display_panel = data_display_panel;

		movie_palette = new MoviePalette();
		Settings.movie_palette = movie_palette;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;
		add(movie_palette);
		gbl.setConstraints(movie_palette, constraints);


		//data_display_panel = new DataDisplayPanel();  // DataDisplayPanel was created above
		try
		{
			setupDataDisplayPanel();
		}
		catch (Exception e)
		{
			displayMessage("Unable to set up data display!");
		}

		position_panel = new PositionPanel();
		Settings.movie_position_panel = position_panel;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;
		add(position_panel);
		gbl.setConstraints(position_panel, constraints);

		messages_panel = new MessagesPanel();
		messages_panel.setSize(300, 139);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;
		add(messages_panel);
		gbl.setConstraints(messages_panel, constraints);

		title_canvas = new TitleCanvas();
		title_canvas.setSize(TitleCanvas.TITLE_IMAGE_WIDTH, TitleCanvas.TITLE_IMAGE_HEIGHT);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;
		add(title_canvas);
		gbl.setConstraints(title_canvas, constraints);

		setupMenus();
		setupEventListeners();

		pack();

		return;

	}// init


	//********************************************************************************************
	//*   	S E T  V I S I B L E
	//********************************************************************************************
	public void setVisible(boolean show)
	{
		if(show)
		{
			setLocation(20, 20);
		}

		super.setVisible(show);

		return;

	}// end of setVisible();



	//**********************************************************************************
	//*		M A I N
	//*		Modified 10/1/02 for v1.65
	//**********************************************************************************
	static public void main(String args[])
	{
		MainWindow		mw = null;

		//***** Initialize Quicktime & open control window
		try
		{
			QTSession.open();
			mw = new MainWindow();
			mw.setVisible(true);
			mw.displayMessage("Welcome to 4D Viewer/Java v" + version);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(0);
		}

	}// end of main()



	//**********************************************************************************
	//*		M Y  C O M P O N E N T  L I S T E N E R
	//*		Added 11/11/02 for v1.69
	//**********************************************************************************
	class MyComponentListener extends ComponentAdapter
	{
		public void componentMoved(ComponentEvent event)
		{
			//handleComponentEvent(event);
			return;

		}// end of componentMoved()

		public void componentResized(ComponentEvent event)
		{
			handleComponentEvent(event);
			return;

		}// end of componentResized()

	}// end of class MyComponentListener


	//**********************************************************************************
	//*		H A N D L E  C O M P O N E N T  E V E N T
	//*		Added 11/11/02 for v1.69
	//**********************************************************************************
	public void handleComponentEvent(ComponentEvent event)
	{
		//***** Layout the components
		handleLayout();
		pack();

		return;

	}// end of handleComponentEvent()


	//**********************************************************************************
	//*		H A N D L E  L A Y O U T
	//*		Added 12/10/02 for v1.70
	//**********************************************************************************
	public void handleLayout()
	{

		try
		{
			gbl.layoutContainer(this);
			pack();
		}
		catch (Exception e)
		{
			displayMessage("Exception in ImageWindow.handleLayout().");
		}

		return;

	}// end of handleLayout()


	//**********************************************************************************
	//*		P R O C E S S   W I N D O W  E V E N T
	//**********************************************************************************
	protected void processWindowEvent(WindowEvent event)
	{
		switch (event.getID())
		{
			case WindowEvent.WINDOW_ACTIVATED:
				 windowActivated(event);
				 break;
			case WindowEvent.WINDOW_CLOSING:
				 windowClosing(event);
				 break;
		}//switch
	}// end of processWindowEvent()


	//**********************************************************************************
	//*		W I N D O W  A C T I V A T E D
	//*		Modified 11/11/02 for v1.69
	//**********************************************************************************
	void windowActivated(WindowEvent event)
	{
		requestFocus();

		if (data_display_panel != null)
			data_display_panel.repaint();

		return;

	}// end of windowActivated()


	//**********************************************************************************
	//*		W I N D O W  C L O S I N G
	//**********************************************************************************
	void windowClosing(WindowEvent event)
	{
		try
		{
			exitTheProgram();
		}
		catch (Exception e){}

		return;

	}// end of windowClosing()


	//**********************************************************************************
	//*		A C T I O N  L I S T E N E R
	//*		Modified 12/9/02 for v1.70
	//**********************************************************************************
	class MyActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object object = null;

			object = event.getSource();

			//***** File Menu
			if (object == new_menu_item)
				newMenuItemSelected();
			else if (object == open_menu_item)
				openMenuItemSelected();
			else if (object == open_sample_item)
				openSampleMenuItemSelected();
			else if (object == close_menu_item)
				closeMenuItemSelected();
			else if (object == save_settings_menu_item)
				saveSettingsMenuItemSelected();
			else if (object == quit_menu_item)
				quitMenuItemSelected();

			//***** Movie Menu
			else if (object == default_mode_menu_item)
			{
				DataInfo.data_display_mode = DataInfo.DEFAULT_MODE;
				default_mode_menu_item.setState(true);
				loop_mode_menu_item.setState(false);
				bounce_mode_menu_item.setState(false);
			}
			else if (object == loop_mode_menu_item)
			{
				DataInfo.data_display_mode = DataInfo.LOOP_MODE;
				default_mode_menu_item.setState(false);
				loop_mode_menu_item.setState(true);
				bounce_mode_menu_item.setState(false);
			}
			else if (object == bounce_mode_menu_item)
			{
				DataInfo.data_display_mode = DataInfo.BOUNCE_MODE;
				default_mode_menu_item.setState(false);
				loop_mode_menu_item.setState(false);
				bounce_mode_menu_item.setState(true);
			}

			else if (object == clear_messages_menu_item)
			{
				try
				{
				if (messages_panel != null)
					messages_panel.clearMessages();
				}catch (Exception e){}
			}
			else if (object == speed_menu_item)
				;
			else if (object == goto_menu_item)
				;
			else if (object == save_snapshot_menu_item)
				saveSnapshot();
			else if (object == export_images_as_movie_item)
				exportImagesAsMovieMenuItemSelected();

			//***** Annotate Menu
			else if (object == set_scale_menu_item)
				setScaleMenuItemSelected();
			else if (object == ann_prefs_menu_item)
				annPrefsMenuItemSelected();
			else if (object == find_text_menu_item)
				findTextMenuItemSelected();

			//***** Help Menu
			else if (object == about_menu_item)
				aboutMenuItemSelected();

			return;

		}// end of actionPerformed()

	}// end of class MyActionListener


	//***********************************************************************
	//*		N E W  M E N U  I T E M  S E L E C T E D
	//***********************************************************************
	void newMenuItemSelected()
	{
	}// end of newMenuItemSelected()


	//***********************************************************************
	//*		O P E N  M E N U  I T E M  S E L E C T E D
	//*		Modified 4/29/03 for v1.72
	//***********************************************************************
	void openMenuItemSelected()
	{
		Thread		open_thread = null;
		Runnable 	do_it = null;

		//***** Create a runnable object to do the image saving
		do_it = new Runnable()
		{
			public void run()
			{
				try
				{
					openDataSet();
				}
				catch (Exception e)
				{
					displayMessage("Unable to open data set!");
					return;
				}
			}
		};

		//***** Create a thread for the runnable object to run within
		open_thread = new Thread(do_it);

		//***** Set its priority
		open_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the thread running
		open_thread.start();

		return;

	}// end of openMenuItemSelected()



	//***********************************************************************
	//*		O P E N  S A M P L E  M E N U  I T E M  S E L E C T E D
	//***********************************************************************
	void openSampleMenuItemSelected()
	{

		Thread		open_thread = null;
		Runnable 	do_it = null;

		//***** Create a runnable object to do the image saving
		do_it = new Runnable()
		{
			public void run()
			{
				Settings.data_display_panel.loadSampleDataSet();
			}
		};

		//***** Create a thread for the runnable object to run within
		open_thread = new Thread(do_it);

		//***** Set its priority
		open_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the thread running
		open_thread.start();

		return;

	}// end of openSampleMenuItemSelected()


	//***********************************************************************
	//*		C L O S E  M E N U  I T E M  S E L E C T E D
	//***********************************************************************
	void closeMenuItemSelected()
	{
		try
		{
			closeDataSet();
		}
		catch (Exception e){}

		return;

	}// end of closeMenuItemSelected()


	//***********************************************************************
	//*		S A V E  S E T T I N G S  M E N U  I T E M  S E L E C T E D
	//***********************************************************************
	void saveSettingsMenuItemSelected()
	{
		Settings.saveSettings();
		return;

	}// end of saveSettingsMenuItemSelected()


	//*************************************************************
	//*		Q U I T  M E N U  I T E M  S E L E C T E D
	//*		Modified 10/14/02 for v1.66
	//*************************************************************
	void quitMenuItemSelected()
	{
		BooleanWrapper		confirm = new BooleanWrapper(false);
		try
		{

			QuitDialog qd = new QuitDialog(confirm, this, true);
			qd.setVisible(true);

			if (confirm.getValue())
			{
				exitTheProgram();
			}
		}
		catch (Exception e)
		{

		}

		return;

	}// end of quitMenuItemSelected()


	//*************************************************************
	//*		A B O U T  M E N U  I T E M  S E L E C T E D
	//*		Modified 10/14/02 for v1.66
	//*************************************************************
	void aboutMenuItemSelected()
	{
		AboutDialog ad = null;

		ad = new AboutDialog(this, true);
		ad.setVisible(true);

		return;

	}// end of aboutMenuItemSelected()


	//**********************************************************************************
	//*		E X P O R T  I M A G E S  A S  M O V I E  M E N U  I T E M  S E L E C T E D
	//*		Added 8/22/03 for v1.76
	//**********************************************************************************
	void exportImagesAsMovieMenuItemSelected()
	{

		Thread		open_thread = null;
		Runnable 	do_it = null;

		//***** Create a runnable object to do the image saving
		do_it = new Runnable()
		{
			public void run()
			{
				exportImagesAsMovie();
			}
		};

		//***** Create a thread for the runnable object to run within
		open_thread = new Thread(do_it);

		//***** Set its priority
		open_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the thread running
		open_thread.start();

		return;

	}// end of exportImagesAsMovieMenuItemSelected()


	//*************************************************************
	//*		O P E N  D A T A  S E T
	//*		Added 4/29/03 for v1.72
	//*		Modified 01/29/04 for v1.79
	//*************************************************************
	private void openDataSet() throws Exception
	{
		ByteArrayInputStream		buffer = null;
		boolean						successful = true;

		buffer = Input.open4DFormatFile();

		//***** Open the 4D Format File
		try
		{
			Input.readNew4DFormatFile(buffer);
		}
		catch (CancelledException ce)
		{
			displayMessage("User cancelled data set opening.");
			return;
		}
		catch (Exception e)
		{
			displayMessage("This file doesn't seem to be a properly ");
			displayMessage("formatted 4D Format file.");
			displayMessage("Checking to see if file might be from ");
			displayMessage("an older version of 4D Grabber...");
			successful = false;
		}// catch

		if (!successful)
		{
			//***** If that didn't work, try the old format
			try
			{
				Input.readOld4DFormatFile(buffer);// url object is null
			}
			catch (CancelledException ce)
			{
				displayMessage("User cancelled data set opening.");
				return;
			}
			catch (Exception e)
			{
				displayMessage("This file doesn't seem to be a properly formatted 4D Format file.");
				return;
			}
		}// if we were not able to open it using the new method

		//***** Set up data display panel
		setupDataDisplayPanel();

		//***** Load the data set
		Settings.data_display_panel.loadDataSet();

		return;

	}// end of openDataSet()


	//********************************************************************
	//*		S E T U P  D A T A  D I S P L A Y  P A N E L
	//*		Make the appropriate panel for the type of data we're viewing
	//*		Added 5/1/03 for v1.72
	//********************************************************************
	private void setupDataDisplayPanel() throws Exception
	{

		if (DataInfo.data_set_type == DataInfo.MOVIE_DATA_SET)
		{
			if (data_display_panel != null)
			{
				if (!(data_display_panel instanceof MoviePanel))
				{
					data_display_panel.setVisible(false);
					remove(data_display_panel);
					data_display_panel = null;
					data_display_panel = new MoviePanel();
					Settings.data_display_panel = data_display_panel;
				}
			}// if we have a panel already

		}// movie data set
		else if (DataInfo.data_set_type == DataInfo.TIFF_DATA_SET)
		{
			if (data_display_panel != null)
			{
				if (!(data_display_panel instanceof TiffPanel))
				{
					data_display_panel.setVisible(false);
					remove(data_display_panel);
					data_display_panel = null;
					data_display_panel = new TiffPanel();
					Settings.data_display_panel = data_display_panel;
				}
			}// if we have a panel already

		}// tiff data set

		//***** Set parameters for Grid Bag Layout
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;

		//***** Add the new panel to the window
		add(data_display_panel);
		data_display_panel.setVisible(true);
		gbl.setConstraints(data_display_panel, constraints);

		handleLayout();

		return;

	}// end of setupDataDisplayPanel()


	//*************************************************************
	//*		S A V E  S N A P S H O T
	//*		Saves the movie panel's current image to the disk
	//*		as a TIFF file
	//*************************************************************
	void saveSnapshot()
	{
		Thread		save_thread = null;
		Runnable 	do_it = null;


		//***** See if there's a data set open
		if (!Settings.data_set_open)
		{
			displayMessage("No open data set available!");
			return;
		}

		if (Settings.data_display_panel != null)
		{

			//***** Create a runnable object to do the image saving
			do_it = new Runnable()
			{
				public void run()
				{
					Settings.data_display_panel.saveCurrentFrameAsTiff();
				}
			};

			//***** Create a thread for the runnable object to run within
			save_thread = new Thread(do_it);

			//***** Set its priority low so that the window can update
			save_thread.setPriority(Thread.MIN_PRIORITY);

			//***** Start the thread running
			save_thread.start();
		}// if data_display_panel is valid

		return;

	}// saveSnapshot()


	//***************************************************************
	//*		S E T  S C A L E  M E N U  I T E M  S E L E C T E D
	//***************************************************************
	void setScaleMenuItemSelected()
	{

		SetScaleDlog ssd = null;

		ssd = new SetScaleDlog(this, true);
		ssd.setVisible(true);

		return;

	}// end of setScaleMenuItemSelected()


	//***************************************************************
	//*		A N N  P R E F S  M E N U  I T E M  S E L E C T E D
	//***************************************************************
	void annPrefsMenuItemSelected()
	{

		AnnotationPrefsDlog apd = null;

		apd = new AnnotationPrefsDlog(this, true);
		apd.setVisible(true);

		return;

	}// end of annPrefsMenuItemSelected()


	//***********************************************************************
	//*		F I N D  T E X T  M E N U  I T E M  S E L E C T E D
	//*		Added 10/16/02 for v1.66
	//***********************************************************************
	void findTextMenuItemSelected()
	{
		findTextInOverlays();

		return;

	}// end of findTextMenuItemSelected()



	//***********************************************************************
	//*		E X P O R T  I M A G E S  A S  M O V I E
	//*		Added 8/22/03 for v1.76
	//*		Modified 9/11/03 for v1.77
	//*		Modified 9/24/03 for v1.78
	//*		Modified 01/29/04 for v1.80
	//***********************************************************************
	void exportImagesAsMovie()
	{
		GetSubMovieParamsDlog	gsmpd = null;
		IntWrapper				plane = new IntWrapper(DataInfo.cur_focal_plane);
		LongWrapper				start_frame = new LongWrapper(DataInfo.cur_frame);
		LongWrapper				end_frame = new LongWrapper(DataInfo.num_frames);
		BooleanWrapper			cancelled = new BooleanWrapper(false);

		Movie					export_movie = null;
		QTFile					qt_file = null;
		Track					movie_track = null;
		VideoMedia				movie_media = null;
		CompressionSettings		cs = new CompressionSettings();
		Image					cur_image = null;
		String					filename = null;
		int						movie_width = -1, movie_height = -1;
		long					i = 0;

		try
		{
			//***** See if there's a data set open
			if (!Settings.data_set_open)
			{
				displayMessage("No open data set available!");
				return;
			}

			gsmpd = new GetSubMovieParamsDlog(this, true, plane, start_frame, end_frame, cancelled);
			gsmpd.setVisible(true);

			if (!cancelled.getValue())
			{
				Settings.main_window.displayMessage("Creating sub-movie...");

				//***** Make the export movie
				movie_width = Settings.movie_canvas.canvas_width;
				movie_height = Settings.movie_canvas.canvas_height;
				filename = new String("Export Movie_" + start_frame.getValue() + "_to_" + end_frame.getValue() + ".MOV");
				qt_file = MovieUtils.createQTFile(filename);
				export_movie = MovieUtils.createMovie(qt_file);
				movie_track = MovieUtils.createVideoTrack(export_movie, movie_width, movie_height);
				movie_media = MovieUtils.createVideoMedia(movie_track);

				//***** Add each image to the movie
				for (i = start_frame.getValue(); i <= end_frame.getValue(); i++)
				{
					//***** Move to the first frame to be exported
					Settings.data_display_panel.goTo(plane.getValue(), i);

					//***** Give it time to get there
					Thread.yield();

					//***** Get the image from that frame
					cur_image = Settings.data_display_panel.getCurrentFrameAsImage();

					if (cur_image == null)
						throw new Exception("Image was not valid!");

					//***** Initialize if it's the first image
					if (i == start_frame.getValue())
					{
						cs.getCodecSettings(null);
						MovieUtils.addVideoSample_init(cs, cur_image, movie_media, movie_track, movie_width, movie_height);
					}// if it's the first image

					//***** Add the image to the movie
					MovieUtils.addVideoSampleToMedia(cs, cur_image, movie_media);

					//Thread.yield();

				}// for each image

				//***** Insert the media into the track
				movie_track.insertMedia(0,//TrackStartTime
								  		0,//MediaStartTime
								  		movie_media.getDuration(),// Media Duration
							  	  		1);//MediaRate, 1 means "playback at normal media rate"

				MovieUtils.saveMovie(export_movie, Settings.output_directory, qt_file);

				//***** Clean up
				MovieUtils.addVideoSample_dispose(movie_media);

				Settings.main_window.displayMessage("Sub-movie saved to disk successfully!");


			}// if they didn't cancel
			else
			{
				throw new CancelledException();
			}// if they cancelled

		}// try
		catch (CancelledException ce)
		{
			displayMessage("User cancelled image export.");

		}
		catch (Exception e)
		{
			displayMessage("Exception caught in exportImagesAsMovie()!");
			displayMessage("Export failed!");
		}
		catch (OutOfMemoryError oome)
		{
			displayMessage("Out of memory!");
		}

		return;

	}// end of exportImagesAsMovie()


	//***************************************************************
	//*		I T E M  S T A T E  C H A N G E D
	//***************************************************************
	public void itemStateChanged(ItemEvent event)
	{
		Object object = null;

		object = event.getSource();

		if (object == display_overlays_menu_item)
		{
			switch (event.getStateChange())
			{
				case ItemEvent.DESELECTED:
				case ItemEvent.SELECTED:
					 Settings.display_overlays = !Settings.display_overlays;
					 display_overlays_menu_item.setState(Settings.display_overlays);
					 break;
			}
		}// if it was the display_overlays_menu_item

		return;

	}// end of itemStateChanged()


	//***************************************************************
	//*		P R O C E S S  K E Y  E V E N T
	//***************************************************************
	protected void processKeyEvent(KeyEvent e)
	{
		Object obj = null;

		obj = e.getSource();

		switch (e.getID())
		{
			case KeyEvent.KEY_PRESSED:
				 doKeyPressed(e);
				 break;
			case KeyEvent.KEY_TYPED:
				 break;
			case KeyEvent.KEY_RELEASED:
				 break;
		}// end of switch

	}// end of processKeyEvent()



	//***************************************************************
	//*		D O  K E Y  P R E S S E D
	//*		Modified 10/16/02 for v1.66
	//***************************************************************
	void doKeyPressed(KeyEvent event)
	{
		int				key = 0;
		char			key_ch = 0;
		QuitDialog 		qd = null;


		key_ch = event.getKeyChar();
		key = event.getKeyCode();

		if ((event.getModifiers() & KeyEvent.CTRL_MASK) != 0) // if control key is pressed
		{

		}// if the control key was down
		else if ((event.getModifiers() & KeyEvent.ALT_MASK) != 0) // if alt key is pressed
		{

		}// if the alt key was down
		else if ((event.getModifiers() & KeyEvent.META_MASK) != 0)
		{
			switch (key_ch)
			{
				case 'q':
					 try
					 {
					 	BooleanWrapper confirm = new BooleanWrapper(false);
						qd = new QuitDialog(confirm, this, true);
						qd.setVisible(true);

						if (confirm.getValue())
						{
							exitTheProgram();
						}
					 }
					 catch (Exception e){}
					 break;
				case 'f':
					 findTextInOverlays();
					 break;
			}// switch

		}// if the cmd key was down
		else
		{
			switch (key)
			{
				case KeyEvent.VK_RIGHT:
					 Settings.data_display_panel.moveOneFrame(true);
					 break;
				case KeyEvent.VK_LEFT:
					 Settings.data_display_panel.moveOneFrame(false);
					 break;
				case KeyEvent.VK_UP:
					 Settings.data_display_panel.moveOnePlane(true);
					 break;
				case KeyEvent.VK_DOWN:
					 Settings.data_display_panel.moveOnePlane(false);
					 break;
			}// switch
		}// if it was a normal key press

		return;

	}// end of doKeyPressed()



	//***************************************************************
	//*		S E T U P  M E N U S
	//*		Modified 10/16/02 for v1.66
	//*		Modified 8/22/03 for v1.76
	//***************************************************************
	void setupMenus()
	{
		mainMenuBar = new MenuBar();

		//***** File Menu
		file_menu = new Menu("File");
		new_menu_item = new MenuItem("New");
		new_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_N,false));
		new_menu_item.setEnabled(false);
		file_menu.add(new_menu_item);

		open_menu_item = new MenuItem("Open...");
		open_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_O,false));

		if (!Settings.running_as_applet)
		{
			file_menu.add(open_menu_item);
		}

		open_sample_item = new MenuItem("Open Sample Data Set...");

		if (Settings.running_as_applet)
		{
			file_menu.add(open_sample_item);
		}// if we're running as an applet

		file_menu.addSeparator();//=========================

		close_menu_item = new MenuItem("Close");
		file_menu.add(close_menu_item);

		save_menu_item = new MenuItem("Save");
		save_menu_item.setEnabled(false);
		save_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_S,false));
		file_menu.add(save_menu_item);

		saveas_menu_item = new MenuItem("Save As...");
		saveas_menu_item.setEnabled(false);
		saveas_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_S,true));
		file_menu.add(saveas_menu_item);

		file_menu.addSeparator();//=========================

		page_setup_menu_item = new MenuItem("Page Setup...");
		page_setup_menu_item.setEnabled(false);
		file_menu.add(page_setup_menu_item);

		print_menu_item = new MenuItem("Print...");
		print_menu_item.setEnabled(false);
		print_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_P,false));
		file_menu.add(print_menu_item);

		file_menu.addSeparator();//=========================

		save_settings_menu_item= new MenuItem("Save Settings");
		file_menu.add(save_settings_menu_item);

		file_menu.addSeparator();//=========================


		quit_menu_item = new MenuItem("Quit");
		quit_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_Q,false));
		file_menu.add(quit_menu_item);
		mainMenuBar.add(file_menu);

		//***** Edit Menu
		edit_menu = new Menu("Edit");
		undo_menu_item = new MenuItem("Undo");
		undo_menu_item.setEnabled(false);
		undo_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_Z,false));
		edit_menu.add(undo_menu_item);
		edit_menu.addSeparator();//=========================
		cut_menu_item = new MenuItem("Cut");
		cut_menu_item.setEnabled(false);
		cut_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_X,false));
		edit_menu.add(cut_menu_item);
		copy_menu_item = new MenuItem("Copy");
		copy_menu_item.setEnabled(false);
		copy_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_C,false));
		edit_menu.add(copy_menu_item);
		paste_menu_item = new MenuItem("Paste");
		paste_menu_item.setEnabled(false);
		paste_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_V,false));
		edit_menu.add(paste_menu_item);
		clear_menu_item = new MenuItem("Clear");
		clear_menu_item.setEnabled(false);
		edit_menu.add(clear_menu_item);
		selectall_menu_item = new MenuItem("Select All");
		selectall_menu_item.setEnabled(false);
		selectall_menu_item.setShortcut(new MenuShortcut(KeyEvent.VK_A,false));
		edit_menu.add(selectall_menu_item);
		edit_menu.addSeparator();
		mainMenuBar.add(edit_menu);

		//***** Movie Menu
		movie_menu = new Menu("Movie");
		play_mode_menu = new Menu("Movie Play Mode");
			default_mode_menu_item = new CheckboxMenuItem("Default");
			default_mode_menu_item.setState(true);
			play_mode_menu.add(default_mode_menu_item);
			loop_mode_menu_item = new CheckboxMenuItem("Loop");
			loop_mode_menu_item.setState(false);
			play_mode_menu.add(loop_mode_menu_item);
			bounce_mode_menu_item = new CheckboxMenuItem("Bounce");
			bounce_mode_menu_item.setState(false);
			play_mode_menu.add(bounce_mode_menu_item);
		movie_menu.add(play_mode_menu);
		movie_menu.addSeparator();//=========================
		clear_messages_menu_item = new MenuItem("Clear Messages");
		movie_menu.add(clear_messages_menu_item);
		movie_menu.addSeparator();//=========================
		speed_menu_item = new MenuItem("Movie Speed...");
		speed_menu_item.setEnabled(false);
		movie_menu.add(speed_menu_item);
		movie_menu.addSeparator();//=========================
		goto_menu_item = new MenuItem("Go To...");
		goto_menu_item.setEnabled(false);
		movie_menu.add(goto_menu_item);
		movie_menu.addSeparator();//=========================
		save_snapshot_menu_item = new MenuItem("Save Snapshot To Disk...");
		movie_menu.add(save_snapshot_menu_item);
		export_images_as_movie_item = new MenuItem("Export Images As Movie");
		movie_menu.add(export_images_as_movie_item);
		mainMenuBar.add(movie_menu);

		//***** Annotate Menu
		annotate_menu = new Menu("Annotate");
		display_overlays_menu_item = new CheckboxMenuItem("Display Overlays", Settings.display_overlays);
		display_overlays_menu_item.addItemListener(this);
		annotate_menu.add(display_overlays_menu_item);
		set_scale_menu_item = new MenuItem("Set Scale...");
		annotate_menu.add(set_scale_menu_item);
		annotate_menu.addSeparator();//=========================
		ann_prefs_menu_item = new MenuItem("Annotation Prefs...");
		ann_prefs_menu_item.setEnabled(true);
		annotate_menu.add(ann_prefs_menu_item);
		annotate_menu.addSeparator();//=========================
		find_text_menu_item = new MenuItem("Find Text In Overlays...");
		annotate_menu.add(find_text_menu_item);
		find_text_menu_item.setEnabled(true);
		annotate_menu.addSeparator();//=========================
		save_overlay_menu_item = new MenuItem("Save Overlay To Disk");
		save_overlay_menu_item.setEnabled(false);
		annotate_menu.add(save_overlay_menu_item);
		erase_overlay_menu_item = new MenuItem("Erase Overlay From Disk");
		erase_overlay_menu_item.setEnabled(false);
		annotate_menu.add(erase_overlay_menu_item);
		mainMenuBar.add(annotate_menu);


		//***** Help Menu
		help_menu = new Menu("Help");
		mainMenuBar.setHelpMenu(help_menu);
		about_menu_item = new MenuItem("About..");
		help_menu.add(about_menu_item);
		mainMenuBar.add(help_menu);
		setMenuBar(mainMenuBar);

		return;

	}// end of setupMenus()


	//***************************************************************
	//*		S E T U P  E V E N T  L I S T E N E R S
	//*		Modified 11/11/02 for v1.69
	//*		Modified 8/22/03 for v1.76
	//***************************************************************
	void setupEventListeners()
	{
		//***** Tell it what events to receive
		enableEvents(AWTEvent.FOCUS_EVENT_MASK);
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
		enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);

		MyActionListener action_listener = new MyActionListener();
		new_menu_item.addActionListener(action_listener);
		open_menu_item.addActionListener(action_listener);
		open_sample_item.addActionListener(action_listener);
		close_menu_item.addActionListener(action_listener);
		saveas_menu_item.addActionListener(action_listener);
		save_settings_menu_item.addActionListener(action_listener);
		quit_menu_item.addActionListener(action_listener);

		default_mode_menu_item.addActionListener(action_listener);
		loop_mode_menu_item.addActionListener(action_listener);
		bounce_mode_menu_item.addActionListener(action_listener);
		clear_messages_menu_item.addActionListener(action_listener);
		speed_menu_item.addActionListener(action_listener);
		goto_menu_item.addActionListener(action_listener);
		save_snapshot_menu_item.addActionListener(action_listener);
		export_images_as_movie_item.addActionListener(action_listener);

		set_scale_menu_item.addActionListener(action_listener);
		ann_prefs_menu_item.addActionListener(action_listener);
		find_text_menu_item.addActionListener(action_listener);
		save_overlay_menu_item.addActionListener(action_listener);
		erase_overlay_menu_item.addActionListener(action_listener);

		about_menu_item.addActionListener(action_listener);


		MyComponentListener component_listener = new MyComponentListener();
		this.addComponentListener(component_listener);
		data_display_panel.addComponentListener(component_listener);

		return;

	}// end of setupEventListeners()


	//***************************************************************
	//*		C L O S E  D A T A  S E T
	//***************************************************************
	void closeDataSet() throws Exception
	{

		if (Settings.bookmarks_window != null)
		{
			Settings.bookmarks_window.setVisible(false);
			Settings.bookmarks_window.dispose();
		}

		if (Settings.editing_window != null)
		{
			Settings.editing_window.closeDataSet();
			Settings.editing_window.setVisible(false);
			Settings.editing_window.dispose();
		}

		Settings.data_display_panel.closeDataSet();

		Settings.data_set_open = false;

		return;

	}// end of closeDataSet()


	//***************************************************************
	//*		C O P Y  I M A G E  T O  E D I T I N G  W I N D O W
	//***************************************************************
	void copyImageToEditingWindow()
	{
		Settings.data_display_panel.copyCurrentFrameToEditingWindow();
		return;

	}// end of copyImageToEditingWindow()


	//***************************************************************
	//*		F I N D  T E X T  I N  O V E R L A Y S
	//*		Added 10/16/02 for v1.66
	//***************************************************************
	void findTextInOverlays()
	{
		FindTextDlog		ftd = null;

		try
		{
			ftd = new FindTextDlog(this, false);
			ftd.setVisible(true);

		}
		catch (Exception e)
		{
			displayMessage("Exception thrown in findTextInOverlays().");
		}

		return;

	}// end of findTextInOverlays()


	//***************************************************************
	//*		D I S P L A Y  M E S S A G E
	//***************************************************************
	void displayMessage(String msg)
	{
		try
		{
			messages_panel.displayMessage(msg);
		}
		catch (Exception e)
		{

		}

		return;

	}// end of displayMessage()


	//***************************************************************
	//*		E X I T  T H E  P R O G R A M
	//***************************************************************
	void exitTheProgram () throws Exception
	{
		closeDataSet();


		if (Settings.editing_window != null)
		{
			Settings.editing_window.setVisible(false);
			Settings.editing_window.dispose();
		}

		Settings.data_display_panel = null;
		Settings.movie_canvas = null;

		//****** Get rid of the control window
		this.setVisible(false);	// hide the Frame
		dispose();			// free the system resources

		//***** Exit the application
		System.exit(0);		// close the application

	}// end of exitTheProgram()


}// end of MainWindow class



import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;


import quicktime.*;
import quicktime.qd.*;
import quicktime.std.*;
import quicktime.std.movies.*;
import quicktime.app.players.*;

public class MoviePanel extends DataDisplayPanel implements StdQTConstants
{

	final	static	 String		SAMPLE_DATA_SET_URL = "http://www.loci.wisc.edu/4d/java/ViewerApplet/SampleDataSet/";


	//***** SLIDER CONSTANTS
	final	static		int			SLIDER_HEIGHT = 15;

	MovieCanvas 		movie_canvas = null;
	DataSlider			data_slider = null;

	MoviePlayer			movie_player = null;
	Movie				cur_movie = null;

	Thread				play_thread = null;

	//GridBagLayout			gbl = null;
	//GridBagConstraints	constraints = null;
	//Insets				insets = null;

	boolean				keep_checking = false;

	//******************************************************
	//*		I N I T
	//*		Modified 10/18/02 for v1.67
	//*		Modified 6/5/03 for v1.72
	//*		Modified 7/9/03 for v1.74
	//******************************************************
	public MoviePanel()
	{

		super();

		//gbl = new GridBagLayout();
		//constraints = new GridBagConstraints();
		//setLayout(gbl);
		//insets = new Insets(0,0,0,0);

		setVisible(true);
		setSize(MovieCanvas.SPLASH_WIDTH,MovieCanvas.SPLASH_HEIGHT + DataSlider.SLIDER_HEIGHT);

		initPanelVariables();

		movie_canvas = new MovieCanvas();
		movie_canvas.setSize(MovieCanvas.SPLASH_WIDTH, MovieCanvas.SPLASH_HEIGHT);
		movie_canvas.setLocation(0,0);
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
		add(movie_canvas);
		//gbl.setConstraints(movie_canvas, constraints);
		Settings.tiff_canvas = null;
		Settings.movie_canvas = movie_canvas;

		data_slider = new DataSlider(this);
		data_slider.setSize(getSize().width, SLIDER_HEIGHT);
		data_slider.setLocation(0, movie_canvas.canvas_height);
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

		return;

	}// init()


	//********************************************************************************
	//*		U P D A T E
	//********************************************************************************
	public void update(Graphics g)
	{

		super.update(g);

		try
		{
			if (cur_movie != null)
				cur_movie.task(100);
		}
		catch (Exception e){}

		return;

	}// end of update()


	//*************************************************************
	//*		P A I N T
	//*		Modified 10/22/02 for v1.68
	//*		Modified 7/3/03 for v1.74
	//*************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		updateDataSet();

		try
		{
			movie_canvas.restoreObjects();
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while restoring objects!");
		}

		displayDataSetInfo();

		g.setColor(Color.black);
		g.drawRect(1, 1, getSize().width-2, getSize().height-2);

		return;

	}// end of paint()


	//**********************************************************************************
	//*		H A N D L E  L A Y O U T
	//*		Added 10/18/02 for v1.67
	//*		Modified 10/22/02 for v1.68
	//*		Modified 7/9/03 for v1.74
	//**********************************************************************************
	public void handleLayout()
	{

		try
		{
			//gbl.layoutContainer(this);

			this.setSize(movie_canvas.canvas_width, movie_canvas.canvas_height + DataSlider.SLIDER_HEIGHT);
			movie_canvas.setLocation(0,0);
			data_slider.setSize(movie_canvas.canvas_width, DataSlider.SLIDER_HEIGHT);
			data_slider.setLocation(0, movie_canvas.canvas_height);

			updateDataSet();

			//Settings.main_window.displayMessage("Laying out MoviePanel.");
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
	//*		Modified 4/29/03 for v1.72
	//*		Modified 9/22/03 for v1.77
	//*		Modified 10/17/03 for v1.79
	//*************************************************************
	public void	loadDataSet()
	{
		Movie			the_movie = null;
		String			filename = null;
		URL				url = null;
		int				movie_num = 0;

		try
		{
			displayMessage("Opening data set.  Please wait...");

			movie_canvas.clearCanvas();

			for (int i = 0; i < DataInfo.num_focal_planes; i++)
			{
				movie_num = i + 1;
				filename = NameUtils.createMovieName(DataInfo.base_file_name, movie_num);
				displayMessage("Opening movie " + movie_num + " of " + DataInfo.num_focal_planes + ".  Please wait...");

				the_movie = Input.loadMovie(filename);

				if (the_movie != null)
				{
					data_array.addElement(the_movie);
					if (i == 0)
					{
						cur_movie = the_movie;
						getMovieDimensions(the_movie);
						displayMovie(the_movie, DataInfo.data_width, DataInfo.data_height);
						DataInfo.cur_focal_plane = 1;

					}
				}// if we have a movie
			}// for each focal plane

			if (Settings.display_overlays)
			{
				readOverlayHeaders();
				readOverlay();
			}// if we're displaying overlays

			handleLayout();

			displayDataSetInfo();
			movie_canvas.restoreObjects();

			Settings.data_set_open = true;

		}// try
		catch (FileNotFoundException fnfe)
		{
			displayMessage("Unable to locate file " + filename + "!");
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while loading data set.");
		}// catch

		return;

	}// end of loadDataSet()



	//*************************************************************
	//*		L O A D  S A M P L E  D A T A  S E T
	//*		Modified 10/17/02 for v1.66
	//*		Modified 10/17/03 for v1.79
	//*************************************************************
	public void	loadSampleDataSet()
	{
		String					filename = null;
		URL						url = null;
		int						movie_num = 0;
		ByteArrayInputStream	buffer = null;

		try
		{

			url = new URL(SAMPLE_DATA_SET_URL + "4D%20Format%20File");
			buffer = Input.open4DFormatFile(url);

			//***** Open the 4D Format File and get info
			try
			{
				Input.readNew4DFormatFile(buffer);
			}
			catch (Exception e)
			{

			}

			try
			{
				Input.readOld4DFormatFile(buffer);
			}
			catch (Exception e)
			{
				throw new Exception("Unable to open 4D Format File!");
			}

			displayMessage("Opening data set.  Please wait...");

			for (int i = 0; i < DataInfo.num_focal_planes; i++)
			{
				movie_num = i + 1;
				filename = NameUtils.createMovieName(DataInfo.base_file_name, movie_num);
				displayMessage("Opening movie " + movie_num + " of " + DataInfo.num_focal_planes + ".  Please wait...");
				url = new URL(SAMPLE_DATA_SET_URL + filename + ".mov");
				cur_movie = Input.loadMovie(url);

				if (cur_movie != null)
				{
					if (i == 0)
					{
						getMovieDimensions(cur_movie);
						displayMovie(cur_movie, DataInfo.data_width, DataInfo.data_height);
						DataInfo.cur_focal_plane = 1;
					}
					data_array.addElement(cur_movie);
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


	//**********************************************************************
	//*		G E T  D A T A  D I M E N S I O N S
	//*		Added 10/22/02 for v1.68
	//**********************************************************************
	public void getMovieDimensions(Movie the_movie) throws Exception
	{
		int		width = -1, height = -1;
		int		iterations = 0;

		do
		{
			width = the_movie.getBounds().getWidth();
			height = the_movie.getBounds().getHeight();
			iterations += 1;

			if (iterations > 20)
				throw new Exception("Unable to get movie dimensions!");
		}
		while (width <= 1 || height <= 1);

		DataInfo.data_width = width;
		DataInfo.data_height = height;

		displayMessage("Movie width = " + DataInfo.data_width);
		displayMessage("Movie height = " + DataInfo.data_height);

		return;

	}// end of getMovieDimensions()


	//**********************************************************************
	//*		D I S P L A Y  M O V I E
	//*		Modified 10/16/02 for v1.66
	//**********************************************************************
	public void displayMovie(Movie the_movie) throws Exception
	{
		QDRect	movie_bounds = null;

		if (the_movie == null)
			return;

		movie_bounds = the_movie.getBounds();
		DataInfo.data_width = movie_bounds.getWidth();
		DataInfo.data_height = movie_bounds.getHeight();

		displayMovie(the_movie, DataInfo.data_width, DataInfo.data_height);

		return;

	}// end of displayMovie()


	//**********************************************************************
	//*		D I S P L A Y  M O V I E
	//*		Modified 10/22/02 for v1.68
	//**********************************************************************
	public void displayMovie(Movie the_movie, int data_width, int data_height) throws Exception
	{
		QDRect		player_rect = null;

		if (the_movie == null)
			return;

		DataInfo.num_frames = countMovieFrames(the_movie);

		player_rect = new QDRect(0, 0, DataInfo.data_width, DataInfo.data_height);

		//***** Make the new movie player
		this.movie_player = new MoviePlayer(the_movie);

		//***** Set the player's boundaries to the size of the movie
		this.movie_player.setDisplayBounds(player_rect);

		//***** Set the movie canvas size
		if (data_width != movie_canvas.getWidth() || data_height != movie_canvas.getHeight())
		{
			handleNewDataSize(data_width, data_height);
		}// if we're displaying a movie that's a different size than the canvas

		//***** Add the movie player to the canvas
		this.movie_canvas.setClient(movie_player, false); // false means not to resize the canvas

		this.cur_movie = the_movie;
		DataInfo.cur_frame = getCurrentFrame();

		//***** Configure the slider
		setupSlider();

		//**** Make the check thread
		if (this.check_thread == null)
			createCheckThread();

		return;

	}// end of displayMovie()


	//**********************************************************************
	//*		H A N D L E  N E W  D A T A  S I Z E
	//*		Modified 10/22/02 for v1.68
	//*		Modified 4/22/03 for v1.72
	//**********************************************************************
	protected void handleNewDataSize(int data_width, int data_height)
	{
		//***** Make the movie canvas the right size
		movie_canvas.setSize(data_width, data_height);

		//***** Make this panel the right size
		setSize(data_width, data_height + SLIDER_HEIGHT);

		return;

	}// end of handleNewDataSize()


	//**********************************************************************
	//*		S E T U P  S L I D E R
	//*		Created 8/29/02 for v1.62
	//*		Modified 10/16/02 for v1.66
	//*		Modified 7/9/03 for v1.74
	//**********************************************************************
	protected void setupSlider() throws Exception
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

	}// end of setupSlider()


	//**********************************************************************
	//*		C O U N T  M O V I E  F R A M E S
	//**********************************************************************
	public  long  countMovieFrames(Movie the_movie)
	{
		TimeInfo		info = null;
		int[]			media_types = new int[1];
		int			flags = nextTimeMediaSample + nextTimeEdgeOK;
		long			num_frames = 0;
		int			search_start_time = 0;
		boolean		first = true;

		if (the_movie == null)
			return(0);

		media_types[0] = videoMediaType;
		try
		{
			while (search_start_time >= 0)
			{
				num_frames++;
			 	info = the_movie.getNextInterestingTime(flags, media_types, search_start_time, 1);

			 	if (first)
			 	{
			 		DataInfo.frame_duration = info.duration;
			 		first = false;
			 	}

				flags = nextTimeMediaSample;
				search_start_time = info.time;
			}
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown in MoviePanel.countMovieFrames()!");
			return(-1);
		}

		return(num_frames);


	}// end of countMovieFrames()


	//*************************************************************************
	//*		G O  T O
	//*		Modified 9/16/02 for v1.64
	//*************************************************************************
	public void  goTo(int  plane, long frame)
	{
		int				goto_time = 0;
		Movie			new_movie = null;
		boolean			new_plane = false;

		try
		{
			if (this.data_array.size() <= 0)
			{
				displayMessage("No data set available!");
				return;
			}

			if (DataInfo.data_playing)
				stop();

			if (DataInfo.frame_duration > 0)
			{
				goto_time = (int)((frame-1) * DataInfo.frame_duration);

				if (plane != DataInfo.cur_focal_plane)
				{
					new_plane = true;
					new_movie = (Movie)data_array.elementAt(plane - 1);
					if (new_movie == null)
						return;
					new_movie.setTimeValue(goto_time);
					displayMovie(new_movie, DataInfo.data_width, DataInfo.data_height);
					DataInfo.cur_focal_plane = plane;

				}
				else
				{
					cur_movie.setTimeValue(goto_time);
				}

				DataInfo.cur_frame = frame;

			}// if we know the frame duration
			else
			{
				return;
			}// if we don't know the frame duration

			//***** Show where we are
			displayDataSetInfo();

			if (new_plane)
				readOverlayHeaders();

			if (Settings.display_overlays)
			{
				if (Settings.editing_window != null)
					Settings.editing_canvas.saveCarriedObjects();

				readOverlay();
			}
			//***** Draw any overlay objects, or left-over objects
			movie_canvas.restoreObjects();


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
			displayMessage("Exception thrown in MoviePanel.goTo()");
			displayMessage("Unable to move to requested location!");
		}
		return;

	}// end of goTo()


	//*************************************************************************
	//*		M O V E  O N E  F R A M E
	//*************************************************************************
	public  void  moveOneFrame(boolean forward)
	{
		moveOneFrame(cur_movie, forward);
		return;
	}// end of moveOneFrame()


	//*************************************************************************
	//*		M O V E  O N E  F R A M E
	//*		Modified 9/16/02 for v1.64
	//*		Modified 8/3/03 for 1.76
	//*************************************************************************
	public  void  moveOneFrame(Movie the_movie, boolean forward)
	{
		TimeInfo	info = null;
		int[]		media_types = new int[1];
		int			flags = nextTimeMediaSample;
		int			search_direction = 0;
		int			start_time = 0, end_time = 0;
		boolean		overlay_found = false;

		if (the_movie == null)
			return;

		//***** Set Search Direction
		if (forward)
			search_direction = 1;
		else
			search_direction = -1;

		try
		{
			//if (DataInfo.data_playing)
			//	stop();

			//***** Set search start time
			start_time = the_movie.getTime();

			if (DataInfo.frame_duration > 0)
			{
				if (forward)
					end_time = start_time + DataInfo.frame_duration;
				else
					end_time = start_time - DataInfo.frame_duration;
				the_movie.setTimeValue(end_time);
				the_movie.task(0);
			}// if we know the frame duration
			else
			{
				media_types[0] = videoMediaType;
				info = the_movie.getNextInterestingTime(flags, media_types, start_time,
														search_direction);
				if (info.time > 0)
				{
					the_movie.setTimeValue(info.time);
					the_movie.task(0);
				}
			}// if we don't know the frame duration

			DataInfo.cur_frame = getCurrentFrame();

			if (Settings.display_overlays)
				readOverlay();

			//***** Draw any overlay objects
			movie_canvas.restoreObjects();

			//***** Show where we are
			displayDataSetInfo();

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
			displayMessage("Exception thrown in MoviePanel.moveOneFrame()");
		}

		return;

	}// end of moveOneFrame()


	//*************************************************************************
	//*		M O V E  O N E  P L A N E
	//*		Modified 10/22/02 for v1.68
	//*************************************************************************
	public  void  moveOnePlane(boolean move_up)
	{
		int			movie_time = 0;
		Movie		new_movie = null;
		boolean		was_playing = false;
		boolean		overlay_found = false;

		try
		{
			if (cur_movie == null)
				return;

			if (DataInfo.data_playing)
			{
				stop();
				was_playing = true;
			}

			movie_time = cur_movie.getTime();

			if (move_up)
			{
				if (DataInfo.cur_focal_plane < DataInfo.num_focal_planes)
				{
					DataInfo.cur_focal_plane += 1;
					new_movie = (Movie)data_array.elementAt(DataInfo.cur_focal_plane - 1);
					new_movie.setTimeValue(movie_time);
					displayMovie(new_movie, DataInfo.data_width, DataInfo.data_height);
				}
			}// if we're increasing the focal plane
			else
			{
				if (DataInfo.cur_focal_plane > 1)
				{
					DataInfo.cur_focal_plane -= 1;
					new_movie = (Movie)data_array.elementAt(DataInfo.cur_focal_plane - 1);
					new_movie.setTimeValue(movie_time);
					displayMovie(new_movie, DataInfo.data_width, DataInfo.data_height);
				}
			}// if we're decreasing the focal plane

			DataInfo.cur_frame = getCurrentFrame();

			if (Settings.display_overlays)
			{
				readOverlayHeaders();
				readOverlay();
			}

			//***** Draw any overlay objects, or left-over objects
			movie_canvas.restoreObjects();

			//***** Show where we are
			displayDataSetInfo();

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
	public long	getCurrentFrame() throws Exception
	{
		long		frame_num = -1;
		int			movie_time = 0;


		try
		{
			if (cur_movie == null)
				throw new Exception("No current movie!");

			if (DataInfo.frame_duration <= 0 || DataInfo.num_frames <= 0)
				DataInfo.num_frames = countMovieFrames(cur_movie);

			movie_time = cur_movie.getTime();

			if (movie_time == 0)
				frame_num = 1;

			frame_num = ((long)movie_time/(long)DataInfo.frame_duration) + 1;
		}
		catch (Exception e)
		{
			return(0);
		}

		DataInfo.cur_frame = frame_num;
		return(frame_num);

	}// end of getCurrentFrame()


	//********************************************************************
	//*		G E T  C U R R E N T  P L A N E
	//********************************************************************
	public int	getCurrentPlane() throws Exception
	{
		return(DataInfo.cur_focal_plane);
	}// end of getCurrentPlane


	//********************************************************************
	//*		U P D A T E  D A T A  S E T
	//*		Added 10/22/02 for v1.68
	//********************************************************************
	public void  updateDataSet()
	{
		try
		{
			if (cur_movie != null)
				cur_movie.task(100);
		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while updating movie!");
		}

		return;

	}// end of udpateDataSet();


	//********************************************************************
	//*		D I S P L A Y  D A T A  S E T  I N F O
	//*		Writes the current frame and plane onto the position panel
	//********************************************************************
	public void  displayDataSetInfo()
	{

		try
		{
			DataInfo.cur_frame = getCurrentFrame();

			if (Settings.movie_position_panel != null)
				Settings.movie_position_panel.setPosition(DataInfo.cur_frame, DataInfo.cur_focal_plane);

			else
				throw new Exception("No movie position panel found!");

		}//try
		catch (Exception e)
		{
			displayMessage("Unable to display movie position info!");
		}

		return;

	}// end of displayDataSetInfo();


	//********************************************************************
	//*		P L A Y
	//*		Modified 4/24/03 for v1.72
	//*		Modified 8/3/03 for 1.76
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

		return;

	}// end of play()


	//********************************************************************
	//*		P L A Y  D A T A
	//*		Added 8/3/03 for v1.76
	//*		Modified 8/4/03 for v1.76
	//********************************************************************
	private	void	playData(boolean forward)
	{
		try
		{
			DataInfo.data_playing = true;
			DataInfo.keep_playing = true;
			DataInfo.data_playing_forward = forward;

			try
			{
				Settings.movie_palette.resetPlayButtons();
			}
			catch (Exception e)
			{
				Settings.main_window.displayMessage("Exception thrown while setting palette buttons.");
			}

			while (DataInfo.keep_playing)
			{
				moveOneFrame(forward);
				Thread.yield();
			}// while

			DataInfo.data_playing = false;
			play_thread = null;

		}
		catch (Exception e2)
		{
			Settings.main_window.displayMessage("Exception thrown while playing data.");
			DataInfo.data_playing = false;
		}


		return;

	}// end of playData()


	//**************************************************
	//*		S T O P
	//*		Modified 8/3/03 for v1.76
	//**************************************************
	public void	stop() throws Exception
	{
		if (cur_movie == null)
			throw new Exception("No movie to stop!");

		cur_movie.stop();

		DataInfo.keep_playing = false;
		DataInfo.data_playing = false;

		Settings.movie_palette.resetPlayButtons();

		DataInfo.cur_frame = getCurrentFrame();
		displayDataSetInfo();

		return;

	}// end of stop()


	//**************************************************************************
	//*		G E T  C U R R E N T  F R A M E  A S  I M A G E
	//*		Added 9/10/03 for v1.77
	//**************************************************************************
	public	Image	getCurrentFrameAsImage() throws Exception
	{

		Image				movie_image = null;
		Pict				movie_pict = null;
		Graphics			g = null;
		Image				mutable_image = null;
		MovieObjectArray	moa = null;

		//****** Get the Pict from the movie
		movie_pict = cur_movie.getPict(cur_movie.getTime());

		//***** Convert the Pict to a java.awt.Image
		movie_image = Utils.pictToImage(movie_pict);

		//***** Get the size of the image
		Dimension d = Utils.getImageDimensions(this, movie_image);

		//***** Draw the annotation objects into the graphics context
		if (Settings.display_overlays)
			readOverlay();

		//***** Get the movie object array from the movie canvas
		moa = movie_canvas.object_array;

		//***** Draw the movie information and the overlays into a new image
		if (moa != null)
		{
			//***** Create a mutable image, one that can be drawn into
			mutable_image = Settings.main_window.createImage(d.width, d.height);
			if (mutable_image != null)
			{
				g = mutable_image.getGraphics();
				if (g != null)
				{
					//***** Draw the old image into the mutable image
					g.drawImage(movie_image, 0, 0, null);

					//***** Draw the objects into the mutable image
					moa.restoreObjects(g);

					g.dispose();
				}// if g != null
			}// if mutable_image != null
			moa = null;
		}// if moa != null

		if (mutable_image != null)
			return(mutable_image);
		else
			return(movie_image);

	}// end of getCurrentFrameAsImage()


	//**************************************************
	//*		S A V E  C U R R E N T  F R A M E  A S  T I F F
	//*		Saves as RGB Image
	//*		Modified 7/18/03 for v1.75
	//**************************************************
	public	void	saveCurrentFrameAsTiff()
	{
		String				filename = null;
		TiffWriter			tiff_writer = null;
		Image				cur_image = null;
		Dimension			d = null;


		try
		{
			if (cur_movie == null)
				throw new Exception();

			cur_image = getCurrentFrameAsImage();

			//***** Get the size of the image
			d = Utils.getImageDimensions(this, cur_image);

			//***** Save the image to the disk
			filename = new String("Plane" + DataInfo.cur_focal_plane + "_Frame" + DataInfo.cur_frame + ".tif");
			Settings.output_file_type = ImageWriter.RGB_IMAGE;
			tiff_writer = new TiffWriter(filename, d.width, d.height);
			if (cur_image != null)
				tiff_writer.saveImage(cur_image);

		}//try
		catch	(Exception e)
		{
			displayMessage("Unable to save frame as TIFF. " + e.getMessage());
		}

		return;

	}// end of saveCurrentFrameAsTiff();


	//**********************************************************
	//*		C O P Y  C U R R E N T  F R A M E  T O  E D I T I N G  W I N D O W
	//*		Modified 8/22/02 for v1.61
	//*********************************************************
	public	void	copyCurrentFrameToEditingWindow()
	{
		Pict		movie_pict = null;
		Image		movie_image = null;


		try
		{
			if (cur_movie == null)
				return;

			//****** Get the Pict from the movie
			movie_pict = cur_movie.getPict(cur_movie.getTime());

			//***** Convert the Pict to a java.awt.Image
			movie_image = Utils.pictToImage(movie_pict);

			//***** Make the editing window, if it's not there
			if (Settings.editing_window == null)
			{
				Settings.editing_window = new EditingWindow();
				Settings.editing_window.setVisible(true);
			}

			//***** Display the image in the editing window
			Settings.editing_window.displayImage(movie_image);

		}
		catch (Exception e)
		{
			displayMessage("Exception thrown while copying image to editing window. " + e.getMessage());
		}

		return;

	}// end of copyCurrentFrameToEditingWindow()



	//***********************************************************************
	//*		C O P Y  O B J E C T S  T O  E D I T I N G  W I N D O W
	//*		Modified 8/22/02 for v1.61
	//***********************************************************************
	public void copyObjectsToEditingWindow() throws Exception
	{

		if (Settings.editing_window == null || movie_canvas == null)
			return;

		if (Settings.editing_canvas != null)
		{
			Settings.editing_canvas.disk_object_array.resetArray();

			for (int i = 0; i < movie_canvas.object_array.getNumObjects(); i++)
				Settings.editing_canvas.disk_object_array.addObject(movie_canvas.object_array.getObject(i));
		}// if we have an editing canvas

		return;

	}// end of copyObjectsToEditingWindow()


	//***********************************************************************
	//*		C H E C K  D A T A  S E T
	//*		Modified 7/8/03 for v1.74
	//***********************************************************************
	public void checkDataSet()
	{

		try
		{
			keep_checking = true;

			while (keep_checking)
			{
				if (cur_movie == null)
				{
					keep_checking = false;
					break;
				}

				if (DataInfo.data_playing)
				{
					//***** Look for the end of the movie being reached
					if (cur_movie.isDone())
					{
						if (DataInfo.data_display_mode == DataInfo.DEFAULT_MODE)
						{
							//***** Stop the movie
							stop();
						}// if we're in the default mode
						else if (DataInfo.data_display_mode == DataInfo.LOOP_MODE)
						{
							//***** Reset the position
							if (!DataInfo.data_playing_forward)
								cur_movie.goToEnd(); // reset to end of movie
							else
								cur_movie.goToBeginning();// reset to beginning of movie
						}
						else if (DataInfo.data_display_mode == DataInfo.BOUNCE_MODE)
						{
							play(!DataInfo.data_playing_forward);

						}// if we're bouncing

					}// if the movie is at the end

					else
					{
						//***** Show the movie info
						displayDataSetInfo();

						//***** Update the slider
						data_slider.setValue((int)DataInfo.cur_frame);

					}// if the movie's not to the end yet

					Thread.yield();//sleep(200);

				}// if the movie's playing
				else
				{
					Thread.yield();//sleep(500);

				}// if the movie's not playing

			}// while keep_checking
		}
		catch (Exception e)
		{
			displayMessage("Exception during checkMovie(): ");
			displayMessage(e.getMessage());
		}

		return;

	}// end of checkDataSet()


	//********************************************************************
	//*		C L O S E  D A T A  S E T
	//*		Modified 12/10/02 for v1.70
	//*		Modified 7/9/03 for v1.74
	//********************************************************************
	public void closeDataSet() throws Exception
	{

		cur_movie = null;
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

		movie_canvas.closeDataSet();
		movie_canvas.removeClient();
		movie_player = null;
		handleLayout();
		Settings.main_window.handleLayout();

		return;

	}// end of closeDataSet()



	//********************************************************************
	//*		I N I T  P A N E L  V A R I A B L E S
	//*		Modified 4/24/03 for v1.72
	//********************************************************************
	protected void initPanelVariables()
	{

		super.initPanelVariables();

		this.movie_canvas = null;
		this.data_slider = null;
		this.movie_player = null;
		this.cur_movie = null;
       	this.data_array = new Vector(10,1);

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
											 movie_canvas.object_array,
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

}// end of MoviePanel class


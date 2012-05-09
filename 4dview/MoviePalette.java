import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class MoviePalette extends ToolPalette
{

	final	static	int		PREFERRED_WIDTH = 2 * BUTTON_WIDTH;
	final	static	int		PREFERRED_HEIGHT = 5 * BUTTON_HEIGHT;

	ImageButton 			bookmarks_button = null;
	ImageButton 			edit_image_button = null;
	ImageButton 			down_arrow_button = null;
	ImageButton 			up_arrow_button = null;
	ImageButton 			frame_forward_button = null;
	ImageButton 			frame_backward_button = null;
	ImageButton 			play_backward_button = null;
	ImageButton 			play_forward_button = null;
	ImageButton				snapshot_button = null;

	Image					frame_forward_img = null, frame_backward_img = null;
	Image					play_forward_img = null, play_backward_img = null, stop_img = null;
	Image					up_arrow_img = null, down_arrow_img = null;
	Image					bookmark_img = null, edit_img = null;
	Image					snapshot_img = null;

	//**********************************************************************
	//*		I N I T
	//*		Last modifed 10/22/02 for v 1.68
	//**********************************************************************
	public MoviePalette()
	{
		super();

		setVisible(true);
		setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);

		//***** Load the icon images
		loadButtonImages();

		//**** Add buttons
		play_backward_button = new ImageButton();
		play_backward_button.setImage(play_backward_img);
		play_backward_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(play_backward_button);
		setGBLConstraints(play_backward_button, 0, 0);

		play_forward_button = new ImageButton();
		play_forward_button.setImage(play_forward_img);
		play_forward_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(play_forward_button);
		setGBLConstraints(play_forward_button, 1, 0);

		frame_backward_button = new ImageButton();
		frame_backward_button.setImage(frame_backward_img);
		frame_backward_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(frame_backward_button);
		setGBLConstraints(frame_backward_button, 0, 1);

		frame_forward_button = new ImageButton();
		frame_forward_button.setImage(frame_forward_img);
		frame_forward_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(frame_forward_button);
		setGBLConstraints(frame_forward_button, 1, 1);

		up_arrow_button = new ImageButton();
		up_arrow_button.setImage(up_arrow_img);
		up_arrow_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(up_arrow_button);
		setGBLConstraints(up_arrow_button, 0, 2);

		down_arrow_button = new ImageButton();
		down_arrow_button.setImage(down_arrow_img);
		down_arrow_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(down_arrow_button);
		setGBLConstraints(down_arrow_button, 1, 2);

		edit_image_button = new ImageButton();
		edit_image_button.setImage(edit_img);
		edit_image_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(edit_image_button);
		setGBLConstraints(edit_image_button, 0, 3);

		bookmarks_button = new ImageButton();
		bookmarks_button.setImage(bookmark_img);
		bookmarks_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(bookmarks_button);
		setGBLConstraints(bookmarks_button, 1, 3);

		snapshot_button = new ImageButton();
		snapshot_button.setImage(snapshot_img);
		snapshot_button.setSize(BUTTON_WIDTH,BUTTON_HEIGHT);
		add(snapshot_button);
		setGBLConstraints(snapshot_button, 0, 4);

		//****** Add mouse listeners
		if (mouse_adapter != null)
		{
			play_forward_button.addMouseListener(mouse_adapter);
			play_backward_button.addMouseListener(mouse_adapter);
			frame_forward_button.addMouseListener(mouse_adapter);
			frame_backward_button.addMouseListener(mouse_adapter);
			up_arrow_button.addMouseListener(mouse_adapter);
			down_arrow_button.addMouseListener(mouse_adapter);
			edit_image_button.addMouseListener(mouse_adapter);
			bookmarks_button.addMouseListener(mouse_adapter);
			snapshot_button.addMouseListener(mouse_adapter);
		}

		return;

	}// init


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 10/15/02 for v1.66
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

	}// end of getPreferredSize()


	//*********************************************************************
	//*		L O A D  B U T T O N  I M A G E S
	//*********************************************************************
	public void loadButtonImages()
	{
		InputStream		is = null;
		byte[]			bytes = null;
		URL				url = null;

		try
		{
			url = getClass().getResource("Images/Bookmarks.jpeg");
			if (url != null)
			{
    			bookmark_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (bookmark_img == null)
			{
				is = getClass().getResourceAsStream("Images/Bookmarks.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			bookmark_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(bookmark_img);

			url = getClass().getResource("Images/Edit_button.jpeg");
			if (url != null)
			{
    			edit_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (edit_img == null)
			{
				is = getClass().getResourceAsStream("Images/Edit_button.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			edit_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream


			confirmImageLoading(edit_img);

			url = getClass().getResource("Images/Down_arrow.jpeg");
			if (url != null)
			{
    			down_arrow_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (down_arrow_img == null)
			{
				is = getClass().getResourceAsStream("Images/Down_arrow.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			down_arrow_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(down_arrow_img);

			url = getClass().getResource("Images/Up_arrow.jpeg");
			if (url != null)
			{
    			up_arrow_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (up_arrow_img == null)
			{
				is = getClass().getResourceAsStream("Images/Up_arrow.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			up_arrow_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(up_arrow_img);

			url = getClass().getResource("Images/Frame_forward.jpeg");
			if (url != null)
			{
    			frame_forward_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (frame_forward_img == null)
			{
				is = getClass().getResourceAsStream("Images/Frame_forward.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			frame_forward_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(frame_forward_img);

			url = getClass().getResource("Images/Frame_backward.jpeg");
			if (url != null)
			{
    			frame_backward_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (frame_backward_img == null)
			{
				is = getClass().getResourceAsStream("Images/Frame_backward.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			frame_backward_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(frame_backward_img);

			url = getClass().getResource("Images/Play_backward.jpeg");
			if (url != null)
			{
    			play_backward_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (play_backward_img == null)
			{
				is = getClass().getResourceAsStream("Images/Play_backward.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			play_backward_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(play_backward_img);

			url = getClass().getResource("Images/Play_forward.jpeg");
			if (url != null)
			{
    			play_forward_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (play_forward_img == null)
			{
				is = getClass().getResourceAsStream("Images/Play_forward.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			play_forward_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(play_forward_img);

			url = getClass().getResource("Images/Snapshot.jpeg");
			if (url != null)
			{
    			snapshot_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (snapshot_img == null)
			{
				is = getClass().getResourceAsStream("Images/Snapshot.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			snapshot_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(snapshot_img);

			url = getClass().getResource("Images/Stop.jpeg");
			if (url != null)
			{
    			stop_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (stop_img == null)
			{
				is = getClass().getResourceAsStream("Images/Stop.jpeg");
        		bytes = new byte[is.available()];
        		is.read(bytes);
    			stop_img = Toolkit.getDefaultToolkit().createImage(bytes);
    		}// try with getResourceAsStream

			confirmImageLoading(stop_img);

		}
		catch (Exception e)
		{
			Settings.data_display_panel.displayMessage("Exception thrown in MoviePalette.loadButtonImages()!");
		}

		return;

	}// end of loadButtonImages()


	//*********************************************************************
	//*		R E S E T  B U T T O N  I M A G E S
	//*		Mostly for when running as an applet
	//*		Applets seem to purge the button images, so this refreshes them
	//*		Modified 9/4/03 for v1.76
	//*********************************************************************
	public void resetButtonImages() throws Exception
	{

		//loadButtonImages();

		if (DataInfo.data_playing)
		{
			if (DataInfo.data_playing_forward)
			{
				play_backward_button.setImage(play_backward_img);
				play_forward_button.setImage(stop_img);
			}
			else
			{
				play_backward_button.setImage(stop_img);
				play_forward_button.setImage(play_forward_img);
			}
		}
		else
		{
				play_backward_button.setImage(play_backward_img);
				play_forward_button.setImage(play_forward_img);
		}

		frame_backward_button.setImage(frame_backward_img);
		frame_forward_button.setImage(frame_forward_img);
		up_arrow_button.setImage(up_arrow_img);
		down_arrow_button.setImage(down_arrow_img);
		edit_image_button.setImage(edit_img);
		bookmarks_button.setImage(bookmark_img);
		snapshot_button.setImage(snapshot_img);

		return;

	}// end of resetButtonImages()


	//*********************************************************************
	//*		R E S E T  P L A Y  B U T T O N S
	//*		Modified 9/4/03 for v1.76
	//*********************************************************************
	protected void resetPlayButtons() throws Exception
	{
		if (!DataInfo.data_playing)
		{
			play_forward_button.setImage(play_forward_img);
			play_backward_button.setImage(play_backward_img);
		}// if the movie's not playing
		else
		{
			//***** Reset the window buttons
			if (DataInfo.data_playing_forward)
			{
				play_forward_button.setImage(stop_img);
				play_backward_button.setImage(play_backward_img);
			}
			else
			{
				play_forward_button.setImage(play_forward_img);
				play_backward_button.setImage(stop_img);
			}
		}// if the movie's playing

		return;
	}// end of resetPlayButtons()


	protected void doMouseClicked(MouseEvent e)
	{

		Object 		object = e.getSource();

		if (object == play_forward_button)
			play_forward_button_mouseClicked(e);
		else if (object == play_backward_button)
			play_backward_button_mouseClicked(e);
		else if (object == frame_forward_button)
			frame_forward_button_mouseClicked(e);
		else if (object == frame_backward_button)
			frame_backward_button_mouseClicked(e);
		else if (object == up_arrow_button)
			upArrowButton_mouseClicked(e);
		else if (object == down_arrow_button)
			downArrowButton_mouseClicked(e);
		else if (object == edit_image_button)
			editImageButton_mouseClicked(e);
		else if (object == bookmarks_button)
			bookmarksButton_mouseClicked(e);
		else if (object == snapshot_button)
			snapshotButton_mouseClicked(e);

		return;

	}// end of mouseClicked();



	//***************************************************************************
	//*		P L A Y  F O R W A R D  B U T T O N
	//*		Modified 5/12/03 for v1.72
	//*		Modified 9/4/03 for v1.76
	//***************************************************************************
	void play_forward_button_mouseClicked(java.awt.event.MouseEvent event)
	{

		try
		{
			if (!DataInfo.data_playing)
			{
				Settings.data_display_panel.play(true);
			}// if the movie's stopped
			else if (DataInfo.data_playing_forward)
			{
				Settings.data_display_panel.stop();
			}// if the movie's playing forward
			else if (!DataInfo.data_playing_forward)
			{
				Settings.data_display_panel.play(true);
			}// if the movie's playing backward

			resetPlayButtons();
		}// try
		catch(Exception e)
		{
			Settings.main_window.displayMessage("Exception caught in MoviePalette.playForwardButtonMouseClicked()");
		}

		if (Settings.running_as_applet)
		{
			try
			{
				resetButtonImages();
			}
			catch (Exception e){}
		}

	}// end of play_forward_button()


	//***************************************************************************
	//*		P L A Y  B A C K W A R D  B U T T O N
	//*		Modified 5/12/03 for v1.72
	//***************************************************************************
	void play_backward_button_mouseClicked(java.awt.event.MouseEvent event)
	{

		try
		{
			if (!DataInfo.data_playing)
			{
				Settings.data_display_panel.play(false);
			}// if the movie's stopped
			else if (DataInfo.data_playing_forward)
			{
				Settings.data_display_panel.play(false);
			}// if the movie's playing forward
			else if (!DataInfo.data_playing_forward)
			{
				Settings.data_display_panel.stop();
			}// if the movie's playing backward

			resetPlayButtons();

		}// try
		catch(Exception sqe){}

	}// end of play_backward_button_mouseClicked()


	void frame_forward_button_mouseClicked(java.awt.event.MouseEvent event)
	{
		frame_forward_button.clickButton();
		Settings.data_display_panel.moveOneFrame(true);
	}

	void frame_backward_button_mouseClicked(java.awt.event.MouseEvent event)
	{
		frame_backward_button.clickButton();
		Settings.data_display_panel.moveOneFrame(false);
	}


	void upArrowButton_mouseClicked(java.awt.event.MouseEvent event)
	{
		up_arrow_button.clickButton();
		Settings.data_display_panel.moveOnePlane(true);
	}

	void downArrowButton_mouseClicked(java.awt.event.MouseEvent event)
	{
		down_arrow_button.clickButton();
		Settings.data_display_panel.moveOnePlane(false);
	}

	//********************************************************************************
	//*		E D I T  I M A G E  B U T T O N  M O U S E  C L I C K E D
	//*		Modified 8/22/02 for v1.61
	//********************************************************************************
	void editImageButton_mouseClicked(java.awt.event.MouseEvent event)
	{
		try
		{
			edit_image_button.clickButton();

			if (Settings.data_display_panel.data_array.size() <= 0)
			{
				Settings.data_display_panel.displayMessage("No data set available for editing!");
				return;
			}

			Settings.data_display_panel.displayMessage("Opening Editing Window.");

			Settings.data_display_panel.copyCurrentFrameToEditingWindow();
			Settings.data_display_panel.copyObjectsToEditingWindow();

		}//try
		catch(Exception e)
		{
			Settings.data_display_panel.displayMessage("Unable to open Editing Window!");
		}
	}// end of editImageButton_mouseClicked()


	void bookmarksButton_mouseClicked(java.awt.event.MouseEvent event)
	{
		BookmarksWindow		bw = null;

		bookmarks_button.clickButton();

		Settings.data_display_panel.displayMessage("Opening Bookmarks Window.");

		bw = new BookmarksWindow();
		bw.setVisible(true);
		bw.toFront();
	}

	void snapshotButton_mouseClicked(java.awt.event.MouseEvent event)
	{
		Thread		save_thread = null;
		Runnable 	do_it = null;

		snapshot_button.clickButton();

		Settings.data_display_panel.displayMessage("Saving snapshot to disk.");

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

		return;

	}

	public void dispose()
	{
		bookmarks_button = null;
		edit_image_button = null;
		down_arrow_button = null;
		up_arrow_button = null;
		frame_forward_button = null;
		frame_backward_button = null;
		play_backward_button = null;
		play_forward_button = null;

		frame_forward_img = null;
		frame_backward_img = null;
		play_forward_img = null;
		play_backward_img = null;
		stop_img = null;
		up_arrow_img = null;
		down_arrow_img = null;
		bookmark_img = null;
		edit_img = null;

	}// end of dispose()


}// end of class MoviePalette

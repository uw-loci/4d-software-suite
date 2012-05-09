import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import quicktime.*;
import quicktime.io.*;
import quicktime.qd.*;
import quicktime.std.*;
import quicktime.std.image.*;
import quicktime.std.movies.*;
import quicktime.std.movies.media.*;
import quicktime.util.*;
import quicktime.app.display.*;
import quicktime.app.image.*;

class MovieUtils implements StdQTConstants
{

	private	static	QTCanvas			offscreen_canvas = null;
	private	static	QDGraphics			offscreen_graf_port = null;
	private	static	ImagePainter		img_painter = null;
	private	static	QDRect				bounds_rect = null;
	private	static	QTHandle			compressed_data = null;
	private	static	RawEncodedImage		compressed_data_ptr = null;
	private	static	ImageDescription	image_desc = null;
	private	static	QTImageDrawer		qt_img_drawer = null;


	//*******************************************************************
	//*		C R E A T E  Q T  F I L E
	//*		Added 9/15/03 for v1.77
	//*******************************************************************
	static QTFile createQTFile(String filename) throws Exception
	{
		FileDialog		fd = null;
		QTFile			qt_file= null;

		//****** Create the QTFile
		try
		{
			fd = new FileDialog (Settings.main_window, "Save Movie As...", FileDialog.SAVE);
			fd.setFile(filename);
			fd.show();
			if(fd.getFile() != null)
			{
				Settings.output_directory = fd.getDirectory();
				qt_file = createQTFile(Settings.output_directory, filename);
			}// if we didn't cancel
			else
				throw new CancelledException();
		}
		catch (Exception e)
		{
			throw new Exception("Exception thrown trying to create QTFile object.");
		}

		return(qt_file);

	}// end of createQTFile


	//*******************************************************************
	//*		C R E A T E  Q T  F I L E
	//*		Added 9/15/03 for v1.77
	//*******************************************************************
	static QTFile createQTFile(String directory, String filename) throws Exception
	{
		QTFile			qt_file= null;

		//****** Create the QTFile
		try
		{
			qt_file = new QTFile(directory + filename);
		}
		catch (Exception e)
		{
			throw new Exception("Exception thrown trying to create QTFile object.");
		}

		return(qt_file);

	}// end of createQTFile


	//*******************************************************************
	//*		C R E A T E  M O V I E
	//*		Added 9/10/03 for v1.77
	//*******************************************************************
	static Movie createMovie(String filename) throws CancelledException, Exception
	{
		Movie			the_movie = null;
		FileDialog		fd = null;

		//****** Create the movie, media, and track
		try
		{

			fd = new FileDialog (Settings.main_window, "Save Movie As...", FileDialog.SAVE);
			fd.setFile(filename);
			fd.show();
			if(fd.getFile() != null)
			{
				Settings.output_directory = fd.getDirectory();
				the_movie = createMovie(Settings.output_directory, filename);
			}// if we didn't cancel
			else
				throw new CancelledException();
		}// try
		catch (Exception e)
		{
			throw new Exception("Unable to create movie.");
		}

		return(the_movie);

	}// end of createMovie


	//*******************************************************************
	//*		C R E A T E  M O V I E
	//*		Added 9/10/03 for v1.77
	//*******************************************************************
	static Movie createMovie(String directory, String filename) throws Exception
	{
		Movie			the_movie = null;
		QTFile			qt_file = null;

		//****** Create the movie
		try
		{
			qt_file = new QTFile(directory + filename);
			the_movie = createMovie(qt_file);
		}
		catch (Exception e)
		{
			throw new Exception("Cannot create movie file.");
		}

		return(the_movie);

	}// end of createMovie


	//*******************************************************************
	//*		C R E A T E  M O V I E
	//*		Added 9/10/03 for v1.77
	//*******************************************************************
	static Movie createMovie(QTFile qt_file) throws Exception
	{
		Movie			the_movie = null;

		//****** Create the movie
		try
		{
			the_movie = Movie.createMovieFile (qt_file,
											   kMoviePlayer,
											   createMovieFileDeleteCurFile | createMovieFileDontCreateResFile);
		}
		catch (Exception e)
		{
			throw new Exception("Cannot create movie file.");
		}

		return(the_movie);

	}// end of createMovie


	//*******************************************************************
	//*		C R E A T E  V I D E O  T R A C K
	//*		Added 9/10/03 for v1.77
	//*******************************************************************
	static	Track createVideoTrack(Movie the_movie, int width, int height) throws Exception
	{
		Track	the_track = null;

		if (the_movie == null)
			return(null);

		try
		{
			the_track = the_movie.addTrack(width,
										   height,
										   0); // track volume
		}
		catch (Exception e)
		{
			throw(e);
		}
		return(the_track);

	}// end of createVideoTrack()


	//*******************************************************************
	//*		C R E A T E  V I D E O  M E D I A
	//*		Added 9/10/03 for v1.77
	//*******************************************************************
	static VideoMedia createVideoMedia(Track the_track) throws Exception
	{
		VideoMedia		the_media = null;
		int				kVidTimeScale = 600; // 600 is the standard video rate

		try
		{
			if (the_track != null)
				the_media = new VideoMedia(the_track, kVidTimeScale);
		}
		catch (Exception e)
		{
			throw(e);
		}

		return(the_media);

	}// end of createVideoMedia


	//********************************************************************************
	//*		A D D  V I D E O  S A M P L E  I N I T
	//*
	//*		This MUST be called before calling addVideoSampleToMedia()
	//*
	//*		Added 9/10/03 for v1.77
	//********************************************************************************
	public static void addVideoSample_init(CompressionSettings cs, Image img, VideoMedia the_media, Track the_track,
										   int width, int height) throws Exception
	{
		int    compressed_size = 0;


      	//***** Check parameters
      	if (cs == null || img == null || the_media == null || the_track == null || width <=0 || height <= 0)
      		throw new Exception("Invalid paramater to addVideoSample_init()");

		//***** Make QDRect the size of the image
		bounds_rect = new QDRect(0, 0, width, height);

		//***** Make offscreen canvas and add it to the image window,
		//***** this is required by awt, not QTJava
		offscreen_canvas = new QTCanvas();
		offscreen_canvas.setVisible(false);
		Settings.main_window.add(offscreen_canvas);
		Settings.main_window.addNotify();

		//***** Make offscreen graphics world
		offscreen_graf_port = new QDGraphics(bounds_rect);

		//***** Get the size of the final, compressed image
		compressed_size = QTImage.getMaxCompressionSize(offscreen_graf_port,
                                                        bounds_rect, //QDRect
                                                        cs.color_depth, //int
                                                        cs.spatial_quality, // int
                                                        cs.comp_type, // int
                                                        cs.comp); //CodecComponent

        //***** Make an empty QTHandle to hold the compressed data
        compressed_data = new QTHandle(compressed_size, false);

        //***** Lock it in memory so it doesn't go anywhere while you're using it
        compressed_data.lock();

        //****** Get a data pointer to the empty QTHandle
        compressed_data_ptr = RawEncodedImage.fromQTHandle (compressed_data);

        //***** Make the ImagePainter, the "Paintable" object
        img_painter = new ImagePainter(img, width, height);
        img_painter.setCurrentImage(img);
        img_painter.setWidth(width);
        img_painter.setHeight(height);


         //***** Make the QTImageDrawer which takes the paintable object
         //***** and facilitates the
         //***** conversion of java.awt drawing into quicktime space
         qt_img_drawer = new QTImageDrawer(img_painter, new Dimension(width, height), Redrawable.kMultiFrame);
         qt_img_drawer.setRedrawing(true);
         offscreen_canvas.setClient(qt_img_drawer, true);  // if true java.awt will re-layout the canvas

         // tell the QTImageDrawer to use the offscreen GrafPort
         qt_img_drawer.setGWorld(offscreen_graf_port);

         // set the boundries for drawing
         qt_img_drawer.setDisplayBounds(bounds_rect);

         // get the media ready to go
         the_media.beginEdits();

         return;

   }// end of addVideoSampleToMedia_init()


	//********************************************************************************
	//*		A D D  V I D E O  S A M P L E  T O  M E D I A
	//*		Added 9/10/03 for v1.77
	//*		Modified 9/15/03 for v1.77
	//*		Modified 9/25/03 for v1.78
	//********************************************************************************
	static void addVideoSampleToMedia(CompressionSettings cs, Image img, VideoMedia the_media) throws Exception
	{
		int		data_offset = 0, num_samples = 1, flags = 0;
		int		frame_duration = 0;

		//***** Add the sample to the media
		//the_media.beginEdits();

		img_painter.setCurrentImage(img);
		qt_img_drawer.redraw(null);  // null here implies that the entire thing should be redrawn

		//***** Compress the offscreen graphics port's data into the
		//***** compressed data pointer
		image_desc = QTImage.compress(offscreen_graf_port,
										   bounds_rect,
										   cs.spatial_quality,
										   cs.comp_type,
										   compressed_data_ptr);

		//***** Determine the frame duration
		frame_duration = (int)(600/cs.frames_per_second);  // 600 is the standard video sample rate
		if (frame_duration <= 0)
			frame_duration = 1;

		//***** Add the compressed data into the video media
		the_media.addSample(compressed_data, 			// media data handle
							data_offset, 				// offset into sample data
							image_desc.getDataSize(), 	// media sample's data size
							frame_duration, 			// media sample's duration * was cs.frame_duration
							image_desc, 				// sample description
							num_samples, 				// number of samples
							flags); 					// flags for samples

		//the_media.endEdits();

		return;

	}// end of addVideoSampleToMedia()


	//********************************************************************************
	//*		A D D  V I D E O  S A M P L E  D I S P O S E
	//*
	//*		This MUST be called after the last call to addVideoSampleToMedia()
	//*		Added 9/10/03 for v1.77
	//*		Modified 9/15/03 for v1.77
	//********************************************************************************
	public static void addVideoSample_dispose(VideoMedia the_media) throws Exception
	{

		the_media.endEdits(); // close the media to editing
		offscreen_canvas.removeClient(); // remove the offscreen canvas from the window

      	//***** Clean up after ourselves
		offscreen_canvas = null;
		offscreen_graf_port = null;
		img_painter = null;
		bounds_rect = null;
		compressed_data = null;
		compressed_data_ptr = null;
		image_desc = null;
		qt_img_drawer = null;

		QTUtils.reclaimMemory();

		return;

   }// end of addVideoSampleToMedia_dispose()



	//*****************************************************************
	//*		S A V E  M O V I E
	//*		Added 9/10/03 for v1.77
	//*		Modified 9/15/03 for v1.77
	//*****************************************************************
	static void saveMovie(Movie the_movie, QTFile the_file) throws CancelledException, Exception
	{
		FileDialog				fd = null;
		String					output_filename = null;
		OpenMovieFile			os = null;


		//***** Ask where to save the file
		fd = new FileDialog(Settings.main_window, "Save Movie File...", FileDialog.SAVE);
		fd.setFile(the_file.getName());
		fd.setVisible(true);

		if (fd.getFile() == null)
			throw new CancelledException();

		Settings.output_directory = fd.getDirectory();
		fd.dispose();
		if (output_filename == null)
			return;

		try
		{
			saveMovie(the_movie, Settings.output_directory, the_file);
		}
		catch (Exception e)
		{
			throw(e);//new Exception ("Unable to save movie file!");
		}

	}// end of saveMovie()


	//*****************************************************************
	//*		S A V E  M O V I E
	//*		Added 9/10/03 for v1.77
	//*		Modified 9/15/03 for v1.77
	//*****************************************************************
	static void saveMovie(Movie the_movie, String directory, QTFile the_file) throws Exception
	{
		OpenMovieFile			os = null;

		try
		{
			os = OpenMovieFile.asWrite(the_file);
			if (os == null)
				throw (new Exception("Unable to save movie file!"));

			the_movie.addResource(os, movieInDataForkResID, the_file.getName());
			os.close();
		}
		catch (Exception e)
		{
			throw(e);// new Exception ("Unable to save movie file!");
		}

	}// end of saveMovie()

}// end of class MovieUtils

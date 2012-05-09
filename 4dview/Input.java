import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.net.URL;


import quicktime.*;
import quicktime.io.*;
import quicktime.qd.*;
import quicktime.std.*;
import quicktime.std.movies.*;
import quicktime.std.movies.media.*;
import quicktime.util.*;
import quicktime.app.players.*;
import quicktime.app.display.*;
import quicktime.app.time.*;

class Input
{

	final		static	int			NOT_FOUND = -1;

	//*************************************************************************
	//*		O P E N  4 D  F O R M A T  F I L E
	//*		Modified 4/29/03 for v1.72
	//*************************************************************************
	static ByteArrayInputStream open4DFormatFile () throws CancelledException, Exception
	{
		FileDialog 					fd = null;
		String						input_filename = null;
		BufferedInputStream			is = null;
		ByteArrayInputStream		buffer = null;
		byte[]						input_array = null;
		int							err = 0;


		fd = new FileDialog(Settings.main_window, "Open 4D Format File...", FileDialog.LOAD);
		fd.setVisible(true);
		input_filename = fd.getFile();
		Settings.input_directory = fd.getDirectory();
		Settings.output_directory = fd.getDirectory();
		fd.dispose();

		if (input_filename == null)
			throw new CancelledException();

		buffer = open4DFormatFile(Settings.input_directory, input_filename);

		return(buffer);

	}// end of open4DFormatFile()


	//*************************************************************************
	//*		O P E N  4 D  F O R M A T  F I L E
	//*		Added 10/17/03 for v1.79
	//*************************************************************************
	static ByteArrayInputStream open4DFormatFile (String input_directory, String input_filename) throws CancelledException, Exception
	{
		BufferedInputStream			is = null;
		ByteArrayInputStream		buffer = null;
		byte[]						input_array = null;
		int							err = 0;



		try
		{
			is = new BufferedInputStream(new FileInputStream(input_directory + input_filename));
		}//try
		catch (Exception e)
		{
			throw (new Exception("Error opening format file!"));
		}//catch

		if (is == null)
			return(null);

		input_array = new byte[is.available()];
		if (input_array != null)
		{
			err = is.read(input_array, 0, is.available());
			if (err == -1)
			{
				throw (new Exception("Unexpected end of file reached."));
			}
		}// if the input array is valid

		is.close();

		//***** Create ByteArrayInputStream
		buffer = new ByteArrayInputStream(input_array);

		return(buffer);

	}// end of open4DFormatFile()


	//*************************************************************************
	//*		O P E N  4 D  F O R M A T  F I L E
	//*		Returns a ByteArrayInputStream from a URL object
	//*		Modified 4/29/03 for v1.72
	//*************************************************************************
	static ByteArrayInputStream open4DFormatFile (URL  url) throws CancelledException, Exception
	{
		BufferedInputStream			is = null;
		ByteArrayInputStream		buffer = null;
		byte[]						input_array = null;
		int							err = 0;


		if (url == null)
			throw new Exception("No URL specified for 4D Format File!");

		try
		{
			is = new BufferedInputStream(url.openStream());
		}//try
		catch (Exception e)
		{
			throw (new Exception("Error opening format file! " + e.getMessage()));
		}//catch

		if (is == null)
			throw (new Exception("Error opening format file!"));

		input_array = new byte[is.available()];
		if (input_array != null)
		{
			err = is.read(input_array, 0, is.available());
			if (err == -1)
			{
				throw (new Exception("Unexpected end of file reached."));
			}
		}// if the input array is valid

		is.close();

		//***** Create ByteArrayInputStream
		buffer = new ByteArrayInputStream(input_array);

		return(buffer);

	}// end of open4DFormatFile()




	//*************************************************************************
	//*		R E A D  N E W  4 D  F O R M A T  F I L E
	//*		The URL object will be null unless the application is running
	//*		as an applet in a browser window
	//*		Modified 4/30/03 for v1.72
	//*		Modified 10/17/03 for v1.79
	//*************************************************************************
	static void	readNew4DFormatFile(ByteArrayInputStream  buffer) throws CancelledException, Exception
	{
		long						num_focal_planes = 0;
		long						num_frames = 0;
		String						base_filename_string = new String("");
		String						num_string = new String("");
		byte[]						ch = new byte[1];
		int							err = 0;
		Integer						int_obj = null;

		try
		{
			ch[0] = (byte)' ';

			if (buffer == null)
				throw new Exception("Null buffer passed to readNew4DFormatFile()!");

			//***** Make sure we're reading from the beginning
			buffer.reset();

			//***** Read the owner string "4DVJ"
			while (ch[0] != KeyEvent.VK_ENTER && ch[0] != TextObject.RETURN_CHAR &&
			   	ch[0] != TextObject.LINE_FEED_CHAR && err != -1)
			{
				err = buffer.read(ch, 0, 1);
				if (ch[0] != '4')
					throw (new Exception("Improper file format."));
				err = buffer.read(ch, 0, 1);
				if (ch[0] != 'D')
					throw (new Exception("Improper file format."));
				err = buffer.read(ch, 0, 1);
				if (ch[0] != 'V')
					throw (new Exception("Improper file format."));
				err = buffer.read(ch, 0, 1);
				if (ch[0] != 'J')
					throw (new Exception("Improper file format."));

				err = buffer.read(ch, 0, 1);// either [return] if movie or [tab] if 4DTiff

				//***** Look for "4DTIFF"
				if (ch[0] == KeyEvent.VK_TAB)
				{
					err = buffer.read(ch, 0, 1);
					if (ch[0] != '4')
						throw (new Exception("Improper file format."));
					err = buffer.read(ch, 0, 1);
					if (ch[0] != 'D')
						throw (new Exception("Improper file format."));
					err = buffer.read(ch, 0, 1);
					if (ch[0] != 'T')
						throw (new Exception("Improper file format."));
					err = buffer.read(ch, 0, 1);
					if (ch[0] != 'I')
						throw (new Exception("Improper file format."));
					err = buffer.read(ch, 0, 1);
					if (ch[0] != 'F')
						throw (new Exception("Improper file format."));
					err = buffer.read(ch, 0, 1);
					if (ch[0] != 'F')
						throw (new Exception("Improper file format."));
					err = buffer.read(ch, 0, 1);// return character

					DataInfo.data_set_type = DataInfo.TIFF_DATA_SET;
				}
				else
					DataInfo.data_set_type = DataInfo.MOVIE_DATA_SET;
			}

			//***** Read the root filename
			while (ch[0] != KeyEvent.VK_TAB && err != -1)
			{
				err = buffer.read(ch, 0, 1);// read one character
				if (err == -1)
					throw (new Exception("Unexpected end of file reached."));

				if (ch[0] >= (char)32 && ch[0] <= (char)126) // put characters into the array
					base_filename_string += (char)ch[0];
			}// while we haven't read the first tab

			ch[0] = (byte)' ';

			//***** Read the number of focal planes
			while (ch[0] != KeyEvent.VK_ENTER && ch[0] != TextObject.RETURN_CHAR &&
			   	ch[0] != TextObject.LINE_FEED_CHAR && err != -1)
			{
				err = buffer.read(ch, 0, 1);// read one character
				if (err == -1)
					break;
				if (ch[0] >= '0' && ch[0] <= '9') // put numbers into the array
					num_string += (char)ch[0];
			}

			int_obj = new Integer(num_string);
			DataInfo.num_focal_planes = int_obj.intValue();
			DataInfo.base_file_name = new String(base_filename_string);

			Settings.old_style_data_set = false;

			//***** Close the input stream
			if (buffer != null)
				buffer.close();

		}// try
		catch (Exception e)
		{
			if (buffer != null)
				buffer.close();
			throw(new Exception("Unable to open 4D Format File " + e.getMessage()));
		}

		return;

	}// end of readNew4DFormatFile()



	//*************************************************************************
	//*		R E A D  O L D  4 D  F O R M A T  F I L E
	//*		The URL object will be null unless the application is running
	//*		as an applet in a browser window
	//*		Added 10/17/03 for v1.79
	//*************************************************************************
	static void	readOld4DFormatFile(ByteArrayInputStream  buffer) throws CancelledException, Exception
	{
		long						num_focal_planes = 0;
		long						num_frames = 0;
		String						base_filename_string = new String("");
		String						num_string = new String("");
		byte[]						ch = new byte[1];
		int							err = 0;
		Integer						int_obj = null;

		try
		{
			ch[0] = (byte)' ';

			if (buffer == null)
				return;

			//***** Make sure we're reading from the beginning
			buffer.reset();

			//***** Read the root filename
			while (ch[0] != KeyEvent.VK_TAB && err != -1)
			{
				err = buffer.read(ch, 0, 1);// read one character
				if (err == -1)
					throw (new Exception("Unexpected end of file reached."));

				if (ch[0] >= (char)32 && ch[0] <= (char)126) // put characters into the array
					base_filename_string += (char)ch[0];
			}// while we haven't read the first tab

			ch[0] = (byte)' ';

			//***** Read the number of focal planes
			while (ch[0] != KeyEvent.VK_ENTER && ch[0] != TextObject.RETURN_CHAR &&
			   	ch[0] != TextObject.LINE_FEED_CHAR && err != -1)
			{
				err = buffer.read(ch, 0, 1);// read one character
				if (err == -1)
					break;
				if (ch[0] >= '0' && ch[0] <= '9') // put numbers into the array
					num_string += (char)ch[0];
			}

			int_obj = new Integer(num_string);
			DataInfo.num_focal_planes = int_obj.intValue();
			DataInfo.base_file_name = new String(base_filename_string);

			Settings.old_style_data_set = true;

			//***** Close the input stream
			if (buffer != null)
				buffer.close();

		}// try
		catch (Exception e)
		{
			if (buffer != null)
				buffer.close();

			throw(new Exception("Unable to open 4D Format File " + e.getMessage()));
		}

		return;

	}// end of readOld4DFormatFile()



	//*******************************************************************
	//*		L O A D  M O V I E
	//*******************************************************************
	static	Movie	loadMovie() throws Exception
	{
		Movie				cur_movie = null;
		QTFile				qtf = null;
		OpenMovieFile		movie_file = null;

		qtf = QTFile.standardGetFilePreview(QTFile.kStandardQTFileTypes);
		movie_file = OpenMovieFile.asRead(qtf);
		cur_movie = Movie.fromFile(movie_file);

		return(cur_movie);
	}


	//*******************************************************************
	//*		L O A D  M O V I E
	//*******************************************************************
	static	Movie	loadMovie(String filename) throws Exception
	{
		Movie				cur_movie = null;
		QTFile				qtf = null;
		OpenMovieFile		movie_file = null;

		if (filename == null)
			return(null);

		if (Settings.input_directory == null)
		{
			cur_movie = loadMovie();
			return(cur_movie);
		}
		qtf = new QTFile(Settings.input_directory + filename);
		movie_file = OpenMovieFile.asRead(qtf);
		cur_movie = Movie.fromFile(movie_file);

		return(cur_movie);
	}


	//*******************************************************************
	//*		L O A D  M O V I E
	//*******************************************************************
	synchronized static	Movie	loadMovie(URL url) throws Exception
	{
		Movie				cur_movie = null;
		DataRef				data_ref = null;
		String				url_string = null;


		if (url == null)
			throw (new Exception ("URL invalid!"));

		url_string = url.toString();
		data_ref = new DataRef(url_string);
		cur_movie = Movie.fromDataRef(data_ref, StdQTConstants.newMovieActive);

		return(cur_movie);

	}// end of loadMovie()


	//*******************************************************************
	//*		G E T  I N P U T  D I R E C T O R Y
	//*******************************************************************
	static String getInputDirectory()
	{
		return(Settings.input_directory);
	}// end of getInputDirectory();

}// end of class Input

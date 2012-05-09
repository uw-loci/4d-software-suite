import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

class OverlayInput
{

	static final int 						NOT_FOUND = -1;

	//*************************************************************************
	//*		R E A D  O V E R L A Y  F R O M  D I S K
	//*		Puts up a dialog to ask where the overlay is
	//*************************************************************************
	static void readOverlayFromDisk(ObjectArray object_array, HeaderArray header_array, int focal_plane, long frame) throws Exception
	{
		FileInputStream			is = null;
		String				filename = null;

		if (!Settings.display_overlays)
			return;

		filename = OverlayInput.makeOverlayFilename(focal_plane);
		if (filename == null)
			return;

		is = getFileInputStream((Frame)Settings.main_window, filename);

		readOverlayFromDisk(Settings.input_directory, object_array, header_array, focal_plane, frame);

		return;

	}// end of readOverlayFromDisk()


	//*************************************************************************
	//*		R E A D  O V E R L A Y  F R O M  D I S K
	//*************************************************************************
	static void readOverlayFromDisk(String directory, ObjectArray object_array, HeaderArray header_array,
									int cur_focal_plane, long cur_frame) throws Exception
	{

		String					filename = null;
		ByteArrayInputStream	buffer = null;
		int						overlay_index = NOT_FOUND;
		long					offset = 0;
		boolean					ok = false;


		if (!Settings.display_overlays)
			return;

		object_array.resetArray();// get rid of objects

		Settings.input_directory = directory;

		//***** Search the headers for the current overlay
		overlay_index = header_array.getOverlayIndex(cur_focal_plane, cur_frame);

		if (overlay_index != NOT_FOUND)
		{
			//***** Get the file's byte array input stream
			filename = makeOverlayFilename(cur_focal_plane);

			try
			{
				buffer = getByteArrayInputStream(Settings.input_directory, filename);
			}
			catch (Exception e)
			{
				return;
			}

			//***** Read the overlay from the disk
			offset = header_array.getEntry(overlay_index).offset_to_overlay;

			readOneOverlay(buffer, object_array, offset);

			buffer.close();

		}// if this frame has an overlay

		return;

	}// end of readOverlayFromDisk()


	//*************************************************************************
	//*		R E A D  O N E  O V E R L A Y
	//*************************************************************************
	static void	readOneOverlay(ByteArrayInputStream is, ObjectArray object_array, long offset_to_overlay) throws Exception
	{
		if (is == null)
			return;

		is.skip(offset_to_overlay);

		object_array.readOverlayFromStream(is);

		return;

	}// end of readOneOverlay()


	//**************************************************************************
	//*		R E A D  O V E R L A Y  H E A D E R S
	//**************************************************************************
	static	void  readOverlayHeaders(String directory, HeaderArray header_array, int cur_focal_plane) throws Exception
	{
		String					filename = null;
		FileInputStream			os = null;
		ByteArrayInputStream	buffer = null;
		URL						url = null;


		try
		{
			Settings.input_directory = directory;

			//***** Get the file's byte array input stream
			filename = makeOverlayFilename(cur_focal_plane);

			if (Settings.input_directory == null)
				os = getFileInputStream((Frame)Settings.main_window, filename);

			buffer = getByteArrayInputStream(Settings.input_directory, filename);

			if (buffer == null)
				throw (new FileNotFoundException());

			header_array.readHeadersFromStream(buffer);

			buffer.close();
		}
		catch (FileNotFoundException fnfe)
		{
			header_array.resetArray();
		}

		return;

	}// end of readOverlayHeaders()


	//*****************************************************************
	//*		G E T  D I S K  D A T A
	//*		Gets data from a disk file from the byte position 'beginning'
	//*		to the byte position 'end' in a buffer
	//*****************************************************************
	static ByteArrayInputStream getDiskData(String input_filename, long beginning, long end) throws Exception
	{
		BufferedInputStream		is = null;
		ByteArrayInputStream	buffer = null;
		int						err = 0;
		byte[]					input_array = null;
		long					bytes_read = 0, file_size = 0;


		try
		{
			is = new BufferedInputStream(new FileInputStream(Settings.input_directory + input_filename));
			if (is == null)
				return(null);

			file_size = is.available();
			if (end > is.available())
				return(null);

			input_array = new byte[(int)end - (int)beginning];
			if (input_array != null)
			{
				bytes_read = is.skip((int)beginning);
				err = is.read(input_array, 0, (int)end - (int)beginning);
				if (err == -1)
				{
					throw new Exception("Unexpected end of file reached.");
				}
			}// if the input array is valid
			is.close();

			//***** Create ByteArrayInputStream
			buffer = new ByteArrayInputStream(input_array);
			if (buffer == null)
				throw new Exception();
		}//try
		catch (Exception e)
		{
			 throw(e);
		}//catch

		return(buffer);

	}// end of getDiskData()


	//*****************************************************************
	//*		G E T  D I S K  D A T A
	//*		Gets data from a disk file and returns it as a ByteArrayInputStream
	//*****************************************************************
	static ByteArrayInputStream getDiskData(ByteArrayInputStream is, long beginning, long end) throws Exception
	{
		ByteArrayInputStream	buffer = null;
		int					err = 0;
		byte[]				input_array = null;
		long					bytes_skipped = 0;


		if (is == null)
			throw (new Exception("Invalid input stream."));

		bytes_skipped = is.skip((int)beginning);
		input_array = new byte[(int)end - (int)beginning];

		err = is.read(input_array, 0, (int)end - (int)beginning);
		if (err == -1)
			throw new Exception("Unexpected end of file reached.");

		is.close();

		//***** Create ByteArrayInputStream
		buffer = new ByteArrayInputStream(input_array);
		if (buffer == null)
			throw new Exception("Unable to create buffer.");

		return(buffer);

	}// end of getDiskData()


	//*******************************************************************
	//*		M A K E  O V E R L A Y  F I L E N A M E
	//*******************************************************************
	static String makeOverlayFilename(int cur_focal_plane)
	{
		String			filename = null;
		String			fp_string = null;
		Integer			int_obj = null;


		int_obj = new Integer(cur_focal_plane);
		fp_string = new String(int_obj.toString());

		filename = new String("Overlays-Plane" + fp_string);

		return(filename);

	}//end of makeOverlayFilename


	//*************************************************************************
	//*		G E T  F I L E  I N P U T  S T R E A M
	//*************************************************************************
	static FileInputStream getFileInputStream (Frame parent, String filename) throws Exception
	{
		FileDialog 					fd = null;
		String					input_filename = null;
		FileInputStream				os = null;


		fd = new FileDialog(parent, "Open Overlay...", FileDialog.SAVE);
		fd.setFile(filename);
		fd.setVisible(true);
		input_filename = fd.getFile();
		Settings.input_directory = fd.getDirectory();
		fd.dispose();
		if (input_filename == null)
			return(null);

		try
		{
			os = getFileInputStream(Settings.input_directory, input_filename);
		}//try
		catch(IOException e)
		{
			throw(new Exception("Error getting the input stream."));
		}

		return(os);

	}// end of getFileInputStream()


	//*************************************************************************
	//*		G E T  F I L E  I N P U T  S T R E A M
	//*************************************************************************
	static FileInputStream getFileInputStream (String directory, String filename) throws Exception
	{
		FileInputStream	os = null;

		try
		{
			os = new FileInputStream(directory + filename);
		}//try
		catch(IOException e)
		{
			throw(new Exception("Error getting the input stream."));
		}

		return(os);

	}// end of getFileInputStream()


	//*************************************************************************
	//*		G E T  B Y T E  A R R A Y  I N P U T  S T R E A M
	//*************************************************************************
	static ByteArrayInputStream getByteArrayInputStream(String directory, String filename) throws IOException
	{
		BufferedInputStream			is = null;
		ByteArrayInputStream		buffer = null;
		byte[]					input_array = null;
		int						err = 0;


		is = new BufferedInputStream(new FileInputStream(directory + filename));
		if (is == null)
			return(null);

		input_array = new byte[is.available()];
		if (input_array != null)
		{
			err = is.read(input_array, 0, is.available());
			if (err == -1)
			{
				throw (new IOException("Unexpected end of file reached."));
			}
		}// if the input array is valid

		is.close();

		//***** Create ByteArrayInputStream
		buffer = new ByteArrayInputStream(input_array);

		return(buffer);

	}// end of getByteArrayInputStream()


	//*************************************************************************
	//*		G E T  B Y T E  A R R A Y  I N P U T  S T R E A M
	//*************************************************************************
	static ByteArrayInputStream getByteArrayInputStream(URL url) throws FileNotFoundException, Exception
	{
		BufferedInputStream			is = null;
		ByteArrayInputStream		buffer = null;
		int						err = 0;
		byte[]					input_array = null;

		try
		{
				is = new BufferedInputStream(url.openStream());
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
		}//try
		catch (FileNotFoundException fnfe)
		{
			throw(fnfe);
		}
		catch (Exception e)
		{
			throw (new Exception("Unable to open overlay file."));
		}//catch

		return(buffer);

	}// end of getByteArrayInputStream()


}// end of class OverlayInput

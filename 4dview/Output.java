import java.io.*;
import java.awt.*;

class Output
{

	//*************************************************************************
	//*		G E T  F I L E  O U T P U T  S T R E A M
	//*************************************************************************
	static FileOutputStream getFileOutputStream (Frame parent, String filename) throws Exception
	{
		FileDialog 						fd = null;
		String						output_filename = null;
		FileOutputStream				os = null;


		fd = new FileDialog(parent, "Save Overlay...", FileDialog.SAVE);
		fd.setFile(filename);
		fd.setVisible(true);
		output_filename = fd.getFile();
		Settings.output_directory = fd.getDirectory();
		fd.dispose();
		if (output_filename == null)
			return(null);

		try
		{
			os = getFileOutputStream(Settings.output_directory, output_filename);
		}//try
		catch(IOException e)
		{
			throw(new Exception("Error getting the output stream. " + e.getMessage()));
		}

		return(os);

	}// end of getFileOutputStream()


	//*************************************************************************
	//*		G E T  F I L E  O U T P U T  S T R E A M
	//*************************************************************************
	static FileOutputStream getFileOutputStream (String directory, String filename) throws Exception
	{
		FileOutputStream	os = null;

		try
		{
			os = new FileOutputStream(directory + filename);
		}//try
		catch(IOException e)
		{
			throw(new Exception("Error getting the output stream. " + e.getMessage()));
		}

		return(os);

	}// end of getFileOutputStream()


	//*************************************************************************
	//*		G E T  F I L E  I N P U T  S T R E A M
	//*************************************************************************
	static FileInputStream getFileInputStream (String directory, String filename) throws Exception
	{
		FileInputStream	is = null;

		try
		{
			is = new FileInputStream(directory + filename);
		}//try
		catch(IOException e)
		{
			throw(new Exception("Error getting the output stream. " + e.getMessage()));
		}

		return(is);

	}// end of getFileInputStream()


	//*************************************************************************
	//*		G E T  O B J E C T  O U T P U T  S T R E A M
	//*************************************************************************
	static ObjectOutputStream getObjectOutputStream (FileOutputStream os) throws Exception
	{
		ObjectOutputStream  oos = new ObjectOutputStream(os);

		return(oos);

	}// end of getObjectOutputStream()


	//*************************************************************************
	//*		G E T  O B J E C T  O U T P U T  S T R E A M
	//*************************************************************************
	static ObjectOutputStream getObjectOutputStream (String directory, String filename) throws Exception
	{
		FileOutputStream	os = null;

		os = getFileOutputStream(directory, filename);

		ObjectOutputStream  oos = new ObjectOutputStream(os);

		return(oos);

	}// end of getObjectOutputStream()



	//*****************************************************************
	//*		W R I T E  B U F F E R  T O  O U T P U T  S T R E A M
	//*		Writes the contents of a ByteArrayOutputStream to the disk
	//*		Returns how many bytes were written
	//*****************************************************************
	static	long	writeBufferToOutputStream(OutputStream os, ByteArrayOutputStream buffer) throws IOException
	{
		int						cur_offset = 0;
		int						bytes_written = 0;
		int						size = 0;
		int						count = 0;
		byte[]					output_array = null;


		output_array = buffer.toByteArray(); // convert contents to byte array
		size = buffer.size();  //size of file to write;
		count = 8192;

		while (bytes_written < size)
		{
			if ((bytes_written + count) > size)
				count = size - bytes_written;
			os.write(output_array, bytes_written, count);
			bytes_written += count;
		}// while we've not yet written it all

		return(bytes_written);

	}// end of writeBufferToOutputStream()


	//*****************************************************************
	//*		W R I T E  B U F F E R  T O  O U T P U T  S T R E A M
	//*		Writes the contents of a ByteArrayInputStream to the disk
	//*		Returns the bytes_written
	//*****************************************************************
	static	long	writeBufferToOutputStream(OutputStream os, ByteArrayInputStream buffer) throws IOException
	{
		int			bytes_written = 0;
		int			cur_offset = 0;
		int			size = 0;
		int			count = 0;
		byte[]		output_array;


		size = buffer.available();
		output_array = new byte[size];
		buffer.read(output_array, 0, size); // convert contents to byte array
		count = 8192;

		while (bytes_written < size)
		{
			if ((bytes_written + count) > size)
				count = size - bytes_written;
			os.write(output_array, bytes_written, count);
			bytes_written += count;
		}// while we've not yet written it all

		return(bytes_written);

	}// end of writeBufferToOutputStream()


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
				throw (new IOException("Unexpected end of file reached. "));
			}
		}// if the input array is valid

		is.close();

		//***** Create ByteArrayInputStream
		buffer = new ByteArrayInputStream(input_array);

		return(buffer);

	}// end of getByteArrayInputStream()


	//*************************************************************************
	//*		G E T  O U T P U T  D I R E C T O R Y
	//*************************************************************************
	static String getOutputDirectory()
	{
		return(Settings.output_directory);
	}// end of getOutputDirectory()

}// end of class Output

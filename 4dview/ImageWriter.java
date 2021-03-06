//Some code modified from Wayne Rasband's freeware application ImageJ

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

class ImageWriter
{

	//***** FILE TYPE CONSTANTS
	static final	int			GRAY_8_BIT_IMAGE = 0;
	static final	int			GRAY_16_BIT_IMAGE = 1;
	static final	int			GRAY_32_BIT_IMAGE = 2;
	static final	int			COLOR_8_BIT_IMAGE = 3;
	static final	int			RGB_IMAGE = 4;


					DataOutputStream	os = null;
					String				filename = null;
					int					output_file_type = Settings.output_file_type;
					int					image_width = -1;
					int					image_height = -1;
					int					num_slices = 0;
					int					slices_written = 0;
					boolean				ok_to_write = false;



	//******************************************************************************
	//*		I N I T
	//******************************************************************************
	public ImageWriter(String file_name, int width, int height) throws Exception
	{

		if (width < 1 || height < 1)
			throw (new Exception("Illegal width or height argument."));

		os = null;
		this.filename = file_name;
		output_file_type = Settings.output_file_type;
		num_slices = 0;
		slices_written = 0;
		ok_to_write = false;

		image_width = width;
		image_height = height;

		if (Settings.output_directory == null)
			os = getImageOutputStream(file_name);
		else
			os = getImageOutputStream(Settings.output_directory, filename);

		ok_to_write = true;

		return;

	}// end of init()



	//*****************************************************************
	//*		G E T  I M A G E  O U T P U T  S T R E A M
	//*****************************************************************
	DataOutputStream getImageOutputStream(String file_name) throws Exception
	{
		DataOutputStream		out_stream = null;
		FileDialog				fd = null;

		try
		{
			//***** Get an output stream
			fd = new FileDialog(Settings.main_window, "Save image...", FileDialog.SAVE);
			fd.setFile(file_name);
			fd.setVisible(true);
			this.filename = fd.getFile();
			Settings.output_directory = fd.getDirectory();

			if (filename == null || Settings.output_directory == null)
				throw (new IOException());

			out_stream = getImageOutputStream(Settings.output_directory, filename);
		}
		catch (Exception e)
		{
			throw (new Exception("Unable to create DataOutputStream.  " + e.getMessage()));
		}

		return(out_stream);

	}// end of getImageOutputStream()



	//*****************************************************************
	//*		G E T  I M A G E  O U T P U T  S T R E A M
	//*****************************************************************
	DataOutputStream		getImageOutputStream(String output_directory, String file_name) throws Exception
	{

		DataOutputStream	out_stream = null;

		Settings.output_directory = output_directory;
		this.filename = file_name;
		out_stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Settings.output_directory + filename)));

		return(out_stream);

	}// end of getImageOutputStream()


	//*****************************************************************
	//*		G E T  F I L E N A M E
	//*****************************************************************
	public	String	getFilename()
	{
		return(new String(this.filename));
	}


	//*****************************************************************
	//*		G E T  N U M  S L I C E S
	//*****************************************************************
	public	int	getNumSlices()
	{
		return(this.slices_written);
	}


	//******************************************************************************
	//*		S A V E  I M A G E
	//******************************************************************************
	void saveImage (Image img) throws Exception{}


	//******************************************************************************
	//*		S A V E  S T A C K
	//*		image_array contains the individual java.awt.image objects
	//******************************************************************************
	void saveStack (Vector image_array) throws Exception{}


	//******************************************************************************
	//*		C R E A T E  S T A C K
	//******************************************************************************
	void createStack (int num_slices) throws Exception
	{
		System.out.println("Whatever");
	}


	//******************************************************************************
	//*		W R I T E  I M A G E  S L I C E
	//*		Writes an image as a slice of a tiff stack
	//*		Modified 7/18/03 for v1.75
	//******************************************************************************
	void writeImageSlice(Image img) throws Exception
	{
			if (image_width == -1 || image_height == -1)
			{
				//***** Get the size of the image
				do
				{
					image_width = img.getWidth(null);
					image_height = img.getHeight(null);

					try
					{
						Thread.sleep(100);
					}
					catch (InterruptedException ignore){}
				}
				while (image_height == -1 || image_width == -1);
			}// get image width and height

			switch (output_file_type)
			{
				case GRAY_8_BIT_IMAGE:
					 write8BitImage(img);
					 break;
				case GRAY_16_BIT_IMAGE:
					 write16BitImage(img, true);
					 break;
				case RGB_IMAGE:
					 writeRGBImage(img);
					 break;
				 default:
				 	 throw (new Exception("Can't save TIFF file in this format!"));
			}

			slices_written += 1;
			Settings.main_window.displayMessage("Writing slice " + slices_written + " to file " + filename);

			return;

	}// end of writeImageSlice()


	//*****************************************************************
	//*		W R I T E  I M A G E  S L I C E S
	//*****************************************************************
	protected void writeImageSlices(Vector image_array) throws Exception
	{
		int		i = 0;
		Image	cur_image = null;

		//***** Write all the image data to the file
		for (i = 0; i < image_array.size(); i++)
		{
			int slice = i + 1;
			Settings.main_window.displayMessage("Writing TIFF slice " + slice);
			cur_image = (Image)image_array.elementAt(i);
			writeImageSlice(cur_image);
		}// for each image

		return;

	}// end of writeImageArray()


	//*****************************************************************
	//*		W R I T E  8  B I T  I M A G E
	//*****************************************************************
	void write8BitImage(Image img) throws Exception
	{
		int 				bytes_written = 0;
		int 				count = 0;
		int[] 				pixels = null;
		int					i = 0, image_size_in_bytes = 0;
		byte[]				buffer = null;

		try
		{
			if (!ok_to_write)
				throw (new Exception("Output Stream is not open."));

			count = 8192;
			image_size_in_bytes = image_width *image_height;
			buffer = new byte[image_size_in_bytes];

			//***** Get the image's pixels
			pixels = getImagePixels(img, new Rectangle(0, 0, image_width, image_height), 1);

			//***** Read the pixels into byte array
			for (i = 0; i < image_size_in_bytes; i++)
			{
				buffer[i] = (byte)pixels[i];
			}

			//Settings.main_window.displayMessage("Saving TIFF slice to disk.");

			//***** Write to the output stream
			while (bytes_written < image_size_in_bytes)
			{
				if ((bytes_written + count)  > image_size_in_bytes)
					count = image_size_in_bytes - bytes_written;
				os.write(buffer, bytes_written, count);
				bytes_written += count;
			}//while there are still bytes to write

		}// end of try
		catch (Exception e)
		{
			throw (e);
		}

		return;

	}


	//*****************************************************************
	//*		W R I T E  1 6  B I T  I M A G E
	//*****************************************************************
	void write16BitImage(Image img, boolean unsigned) throws Exception
	{
		int 				bytes_written = 0;
		int 				count = 0, value = 0;
		int					i = 0, j = 0, image_size_in_bytes = 0;
		byte[]				buffer = null;
		int[] 				pixels = null;

		try
		{
			if (!ok_to_write)
				throw (new Exception("Output Stream is not open."));

			count = image_width * 16;
			image_size_in_bytes = getImageSizeInBytes(image_width, image_height);
			buffer = new byte[count];

			//***** Get the image's pixels
			pixels = getImagePixels(img, new Rectangle(0, 0, image_width, image_height), 2);

			//***** Write to the output stream
			while (bytes_written < image_size_in_bytes)
			{
				if ((bytes_written + count)  > image_size_in_bytes)
					count = image_size_in_bytes - bytes_written;

				j = bytes_written/2;
				for (i = 0; i < count; i += 2)
				{
					value = pixels[j];
					if (unsigned)
						value += 32768;
					buffer[i] = (byte)(value >>>8);
					buffer[i+1] = (byte)value;
					j++;
				}
				os.write(buffer, 0, count);
				bytes_written += count;
			}//while there are still bytes to write

		}// end of try
		catch (Exception e)
		{
			throw (e);
		}

		return;

	}// end of write16BitImage()



	//*****************************************************************
	//*		W R I T E  R G B  I M A G E
	//*****************************************************************
	void writeRGBImage(Image img) throws Exception
	{
		int 				bytes_written = 0;
		int 				count = 0, image_size_in_bytes = 0;
		int					i = 0, j = 0;
		byte[]				buffer = null;
		int[] 				pixels = null;

		try
		{
			if (!ok_to_write)
				throw (new Exception("Output Stream is not open."));

			count = image_width * 24;
			image_size_in_bytes = image_width * image_height * 3;
			buffer = new byte[count];

			//***** Get the image's pixels
			pixels = getImagePixels(img, new Rectangle(0, 0, image_width, image_height), 3);

			//***** Write to the output stream
			while (bytes_written < image_size_in_bytes)
			{
				if ((bytes_written + count)  > image_size_in_bytes)
					count = image_size_in_bytes - bytes_written;

				j = bytes_written/3;
				for (i = 0; i < count; i += 3)
				{
					buffer[i]   = (byte)((pixels[j]>>16)  &0xff);	//red
					buffer[i+1] = (byte)((pixels[j]>>8) & 0xff);	//green
					buffer[i+2] = (byte)(pixels[j] & 0xff);		    //blue
					j++;
				}
				os.write(buffer, 0, count);
				bytes_written += count;
			}//while there are still bytes to write

		}// end of try
		catch (Exception e)
		{
			throw (e);
		}

		return;

	}// end of writeRGBImage()


	//******************************************************************************
	//*		C L O S E  S T A C K
	//******************************************************************************
	public void closeStack() throws Exception{}


	//*****************************************************************
	//*		G E T  I M A G E  P I X E L S
	//*****************************************************************
	int[]	getImagePixels (Image img, Rectangle grab_rect, int bytes_per_pixel) throws Exception
	{
 		PixelGrabber	pg = null;
		int[]			pixels = null;


		pixels = new int[(grab_rect.width * grab_rect.height) * bytes_per_pixel];

		//***** Grab the pixels
		pg = new PixelGrabber(img, grab_rect.x, grab_rect.y, grab_rect.width, grab_rect.height, pixels, 0, grab_rect.width);
		pg.grabPixels();

		return(pixels);

	}// end of getImagePixels()



	//*****************************************************************
	//*		G E T  I M A G E  S I Z E  I N  B Y T E S
	//*****************************************************************
	int	getImageSizeInBytes(int width, int height) throws Exception
	{
		int			image_size = 0;

		switch (output_file_type)
		{
			case GRAY_8_BIT_IMAGE:
				 image_size = image_width * image_height;
				 break;
			case GRAY_16_BIT_IMAGE:
				 image_size = image_width * image_height * 2;
			 	break;
			case RGB_IMAGE:
				 image_size = image_width * image_height * 3;
			 	break;
			 default:
			 	 throw (new Exception("Unknown output file type!"));
		}// end of switch

		return(image_size);

	}// end of getImageSize()



} // end of TiffWriter



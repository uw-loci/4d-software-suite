//******************************************************
//*		I M A G E  I O  H A N D L E R
//******************************************************

//Some code modified from Wayne Rasband's freeware application "Image/J"

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

class ImageReader
{

	//***** FILENAME SYNTAX CONSTANTS
	final   static   int			STANDARD_NON_PADDED = 0;
	final   static   int			STANDARD_THREE_PADDED = 1;
	final   static   int			STANDARD_FOUR_PADDED = 2;
	final   static   int			BIORAD_600 = 3;
	final   static   int			BIORAD_1024 = 4;
	final	static	 int			TIFF = 5;

	//***** FILE TYPE CONSTANTS
	final   static   int			GRAY_8_BIT_IMAGE = 0;
	final   static   int			GRAY_16_BIT_IMAGE = 1;
	final   static   int			GRAY_32_BIT_IMAGE = 2;
	final   static   int			COLOR_8_BIT_IMAGE = 3;
	final   static   int			RGB_IMAGE_CHUNKY = 4;
	final   static   int			RGB_IMAGE_PLANAR = 5;

	//***** FILE FORMAT CONSTANTS
	final   static   int			UNKNOWN_FILE = -1;
	final   static   int			TIFF_FILE = 0;
	final   static   int			JPEG_FILE = 1;
	final   static   int			BIORAD_600_FILE = 2;
	final   static   int			BIORAD_1024_FILE = 3;
	final   static   int			PICT_FILE = 4;
	final   static   int			PICS_FILE = 5;
	final   static   int			BMP_FILE = 6;
	final   static   int			GIF_FILE = 7;
	final   static   int			MACPAINT_FILE = 8;
	final   static   int			PHOTOSHOP_FILE = 9;
	final   static   int			QUICKDRAW_GX_PICTURE_FILE = 10;
	final   static   int			QUICKTIME_IMAGE_FILE = 11;
	final   static   int			SGI_IMAGE_FILE = 12;
	final   static   int			TARGA_IMAGE_FILE = 13;

	final   static   int			kQTFileTypeTiff = 1414088262;

	final   static   int			SHORT_FIELD = 3;
	final   static   int			LONG_FIELD = 4;

	final  static  int				NOT_FOUND = -1;


	int						bytes_per_pixel = 0;
	int						image_format = UNKNOWN_FILE;
	int						image_type = NOT_FOUND;
	int						filename_syntax = TIFF;
	int						image_width = -1, image_height = -1;
	boolean					swap_bytes = false;
	byte[]					color_table = null;
	boolean					zero_is_black = true;
	boolean					image_opened = false;
	int						num_pixels = 0;
	int						offset_to_image_data = 0;
	int						image_size_in_bytes = 0;
	int						buffer_size = 0;
	int						file_size = 0;
	String					filename = null;
	String					file_extension = null;
	DataInputStream			is = null;
	RandomAccessFile		raf = null;
	File					f = null;
	boolean					got_file_info = false;


	//******************************************************************
	//*		I N I T
	//******************************************************************
	public ImageReader()
	{
		bytes_per_pixel = 0;
		image_type = GRAY_8_BIT_IMAGE;
		swap_bytes = false;
		zero_is_black = false;
		image_opened = false;
		num_pixels = 0;
		offset_to_image_data = 0;
		image_size_in_bytes = 0;
		buffer_size = 0;
		file_size = 0;
		filename = null;
		file_extension = null;
		is = null;
		raf = null;
		f = null;
		got_file_info = false;

		return;

	}// init


	//******************************************************************
	//*		I N I T
	//******************************************************************
	public ImageReader(String filename)
	{
		super();

		this.filename = filename;

		return;

	}// init

	//******************************************************************
	//*		G E T  I M A G E
	//*		Prompts user for directory and file with standard File Dialog
	//*		if they haven't been defined already.
	//*		Returns a single slice of a stack as a java.awt.Image
	//*		Modified 5/2/03 for v1.72
	//******************************************************************
	Image	getImage(long slice) throws CancelledException, Exception
	{
		FileDialog			fd = null;
		Image				img = null;

		if (Settings.input_directory == null || filename == null)
		{
			fd = new FileDialog(Settings.main_window, "Open...");
			fd.setVisible(true);

			if (fd.getFile() != null)
			{
				Settings.input_directory = fd.getDirectory();
				filename = fd.getFile();
			}
			else throw new CancelledException();
		}

		img = getImage(Settings.input_directory, filename, slice);


		return(img);

	}// end of getImage()


	//******************************************************************
	//*			G E T  I M A G E
	//******************************************************************
	Image	getImage(String input_directory, String filename, long slice) throws Exception
	{
		return(null);
	}// end of getImage()


	//*****************************************************************
	//*		G E T  I M A G E  I N P U T  S T R E A M
	//*		Prompts user with a standard File Dialog
	//*		Modified 11/25/02 v2.13
	//*****************************************************************
	DataInputStream  getImageInputStream() throws FileNotFoundException, Exception
	{
		DataInputStream		is = null;
		FileDialog			fd = null;

		//***** Get an input stream

		try
		{
			fd = new FileDialog(Settings.main_window, "Open...");
			fd.setVisible(true);

			if (fd.getFile() != null)
			{
				Settings.input_directory = fd.getDirectory();
				filename = fd.getFile();
			}

			is = new DataInputStream(new BufferedInputStream(new FileInputStream(Settings.input_directory + filename)));
		}// try
		catch (FileNotFoundException fnfe)
		{
			throw new FileNotFoundException("Unable to find file " + filename);
		}
		catch (Exception e)
		{
			throw new Exception("Unable to open file " + filename);
		}

		return(is);

	}// end of getImageInputStream()


	//*****************************************************************
	//*		G E T  I M A G E  I N P U T  S T R E A M
	//*		Uses file and directory passed into method by user
	//*		Modified 11/25/02 for v2.13
	//*****************************************************************
	DataInputStream  getImageInputStream (String input_directory, String filename) throws FileNotFoundException, Exception
	{
		FileInputStream		fis = null;
		BufferedInputStream	bis = null;
		DataInputStream		dis = null;

		//***** Get an input stream
		try
		{
			fis = new FileInputStream(input_directory + filename);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
		}
		catch (FileNotFoundException fnfe)
		{
			throw new FileNotFoundException("Unable to find file " + filename);
		}
		catch (Exception e)
		{
			throw new Exception("Unable to open file " + filename);
		}

		return(dis);

	}// end of getImageInputStream()


	//******************************************************************************
	//*		R E A D  8  B I T  I M A G E
	//*		Assumes read pointer is positioned at the start of 8 bit image data and
	//*		that image_width, image_height, and num_pixels has been specified
	//*		Modified 5/14/03 for v1.72
	//******************************************************************************
	Image	read8BitImage(DataInputStream is) throws Exception
	{
		byte[] 			pixels = null;
		Image			img = null;
		int 			bytes_read = 0;
		ColorModel 		cm = null;
		int				pixels_to_read = 17384;//8192;
		boolean			invert = false;

		try
		{
			pixels = new byte[num_pixels];

			while (bytes_read < num_pixels)
			{
				if ((num_pixels - bytes_read) < pixels_to_read)
					pixels_to_read = (num_pixels - bytes_read);

				bytes_read += is.read(pixels, bytes_read, pixels_to_read);
			}

			//***** Make the LUT
			if (this.image_type == GRAY_8_BIT_IMAGE)
				cm = makeGrayscaleColorModel(this.swap_bytes);//!this.swap_bytes);
	 		else
	  			cm = makeColorModel();

	 	 	//***** Make the image
	  		img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(DataInfo.data_width,
	  																			DataInfo.data_height,
	  																			cm,
	  																			pixels,
	  																			0,
	  																			DataInfo.data_width));
	  	}
	  	catch (Exception e)
	  	{
	  		throw(new Exception("Unable to read 8-bit image data."));
	  	}

	 	 return(img);

	}// end of read8BitImage()


	//******************************************************************************
	//*		R E A D  1 6  B I T  I M A G E
	//*		16 bit images are scaled to 8 bit for display
	//*		Assumes read pointer is positioned at the start of 16 bit image data and
	//*		that image_width, image_height, and num_pixels has been specified
	//******************************************************************************
	Image read16BitImage(DataInputStream is) throws Exception
	{
		int 			pixels_read = 0;
		Image			img = null;
		byte[] 			buffer = new byte[buffer_size];
		byte[] 			pixels8 = new byte[num_pixels];
		short[] 		pixels = new short[num_pixels];
		int 			bytes_read = 0;
		int 			total_read = 0;
		int 			base = 0, value = 0;
		int				i = 0, j = 0;
		int				min = 32767, max = -32768;
		double 			scale = 0;
 		ColorModel 		cm = null;


		try
		{
			while (total_read < file_size)
			{
				bytes_read = is.read(buffer, 0, buffer_size);
				total_read += bytes_read;
				pixels_read = bytes_read/this.bytes_per_pixel;
				if ((base + pixels_read) > num_pixels)
					pixels_read = num_pixels - base;

				if (this.swap_bytes)
				{
					for (i = base; i < (base + pixels_read); i++)
					{
						pixels[i] = (short)(((buffer[j+1] & 0xff) << 8) | (buffer[j] & 0xff));
						j += 2;
					}
				}
				else
				{
					for (i = base; i < (base + pixels_read); i++)
					{
						pixels[i] = (short)(((buffer[j] & 0xff) <<8 ) | (buffer[j+1 ]& 0xff));
						j += 2;
					}
				}// if not swap_bytes

				base += pixels_read;
			} // while

			cm = makeGrayscaleColorModel(this.swap_bytes);

		    //***** Find the minimum and maximum values in the image
			for (i = 0; i < DataInfo.data_width * DataInfo.data_height; i++)
			{
				value = pixels[i];
				if (value < min)
					min = value;
				if (value > max)
					max = value;
			}

			scale = 255.0/(max-min);// find scaling factor

			//***** Scale each 16 bit value to 8 bits for display
			for (i = 0; i < num_pixels; i++)
				pixels8[i] = (byte)((int)(scale * (pixels[i] - min)) & 0xff);

			//***** Finally, make the image with the 8 bit values and the color table
			img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(DataInfo.data_width,
																				DataInfo.data_height,
																				cm, pixels8,
																				0,
																				DataInfo.data_width));
		}
		catch (Exception e)
		{
			throw (e);
		}

		return(img);

	}// end of read16BitImage();


	//******************************************************************************
	//*		R E A D  3 2  B I T  I M A G E
	//*		32-bit float images are converted to 8-bit for display
	//******************************************************************************
	Image read32BitImage(DataInputStream is) throws Exception
	{
		int 			pixels_read = 0;
		byte[]			buffer = new byte[buffer_size];
		byte[]			pixels8 = new byte[num_pixels];
		float[]			pixels = new float[num_pixels];
		int 			tmp = 0;
		int				bytes_read = 0;
		int 			total_read = 0;
		int 			base = 0, i = 0, j = 0;
		Image			img = null;
		float			min = Float.MAX_VALUE, max = -Float.MAX_VALUE;
		float 			scale = 0, value = 0;
 		ColorModel 		cm = null;

		try
		{
			while (total_read < file_size)
			{
				bytes_read = is.read(buffer, 0, buffer_size);
				total_read += bytes_read;
				pixels_read = bytes_read/this.bytes_per_pixel;
				if ((base + pixels_read)  > num_pixels)
					pixels_read = num_pixels - base;

				if (this.swap_bytes)
					for (i = base; i < (base + pixels_read); i++)
					{
						tmp = (int)(((buffer[j+3] & 0xff) << 24) | ((buffer[j+2] & 0xff) << 16) |
									((buffer[j+1] & 0xff) << 8) | (buffer[j] & 0xff));
						if (this.image_type == GRAY_32_BIT_IMAGE)
						{
							pixels[i] = Float.intBitsToFloat(tmp);
						}
						else
							pixels[i] = tmp;
						j += 4;
					}
				else
					for (i = base; i < (base +  pixels_read); i++)
					{
						tmp = (int)(((buffer[j] & 0xff) << 24) | ((buffer[j+1] & 0xff)<<16) |
								 	((buffer[j+2] & 0xff) << 8) | (buffer[j+3] & 0xff));
						if (this.image_type == GRAY_32_BIT_IMAGE)
							pixels[i] = Float.intBitsToFloat(tmp);
						else
							pixels[i] = tmp;
						j += 4;
					}
				base += pixels_read;
			}// while total_read < file_size

			cm = makeGrayscaleColorModel(true);

			//***** Get the minimum and maximum values in the image
			for (i = 0; i < num_pixels; i++)
			{
				value = pixels[i];
				if (value < min)
					min = value;
				if (value > max)
					max = value;
			}

			scale = 255f/(max-min);

			//***** Scale the float values
			for (i = 0; i < num_pixels; i++)
				pixels8[i] = (byte)((int)(scale*(pixels[i] - min)) & 0xff);

			//***** Create the image
			img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(image_width, image_height, cm, pixels8, 0, image_width));
		}
		catch (Exception e)
		{
			throw(e);
		}

		return(img);

	}// end of read32BitImage()


	//******************************************************************************
	//*		R E A D  C H U N K Y  R G B  I M A G E
	//******************************************************************************
	Image	readChunkyRGBImage(DataInputStream is) throws Exception
	{
		int				pixels_read = 0;
		Image			img = null;
		byte[] 			buffer = null;
		int[]			pixels = null;
		int 			r = 0, g = 0, b = 0;
		int 			bytes_read = 0;
		int 			total_read = 0;
		int 			base = 0, i = 0, j = 0;
		boolean 		first = true;

		try
		{
			buffer_size = 24 * image_width;
			buffer = new byte[buffer_size];
			pixels = new int[num_pixels];

			while (total_read < file_size)
			{
				bytes_read = is.read(buffer, 0, buffer_size);
				total_read += bytes_read;
				pixels_read = bytes_read/this.bytes_per_pixel;
				if ((base + pixels_read) > num_pixels)
					pixels_read = num_pixels - base;

				j = 0;
				for (i = base; i < (base + pixels_read); i++)
				{
					r = buffer[j++] & 0xff;
					g = buffer[j++] & 0xff;
					b = buffer[j++] & 0xff;
					pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
				}
				base += pixels_read;
			}

	   		img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(image_width, image_height, pixels, 0, image_width));
		}
        		catch (Exception e)
        		{
        			throw(e);
        		}

		return(img);

	}// end of readChunkyRGBImage()


	//******************************************************************************
	//*		R E A D  P L A N A R  R G B  I M A G E
	//******************************************************************************
	Image	readPlanarRGBImage(DataInputStream is) throws Exception
	{
		int 			plane_size = num_pixels; // 1/3 image size
		byte[] 			buffer = new byte[plane_size];
		int[] 			pixels = new int[num_pixels];
		int 			r = 0, g = 0, b = 0;
		int 			bytes_read = 0, i = 0;
		Image			img = null;


		try
		{
			bytes_read = is.read(buffer, 0, plane_size);
			for (i = 0; i < plane_size; i++)
			{
				r = buffer[i] & 0xff;
				pixels[i] = 0xff000000 | (r << 16);
			}

			bytes_read = is.read(buffer, 0, plane_size);

			for (i = 0; i < plane_size; i++)
			{
				g = buffer[i] & 0xff;
				pixels[i] |= g << 8;
			}

			bytes_read = is.read(buffer, 0, plane_size);

			for (i = 0; i < plane_size; i++)
			{
				b = buffer[i] & 0xff;
				pixels[i] |= b;
			}

		    img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(image_width, image_height, pixels, 0, image_width));
	    }
	    catch (Exception e)
	    {
	    	throw (e);
	    }

	    return(img);

	}// end of readPlanarRGBImage()


	//******************************************************************************
	//*		M A K E  G R A Y S C A L E  C O L O R  M O D E L
	//******************************************************************************
	ColorModel makeGrayscaleColorModel(boolean invert)
	 {
		byte[] 	red_LUT = null, green_LUT = null, blue_LUT = null;
		int 	i = 0;

		red_LUT = new byte[256];
		green_LUT = new byte[256];
		blue_LUT = new byte[256];

		if (invert)
			for (i = 0; i < 256; i++)
			{
				red_LUT[255-i]=(byte)(i & 0xff);
				green_LUT[255-i]=(byte)(i & 0xff);
				blue_LUT[255-i]=(byte)(i & 0xff);
			}
		else
		{
			for (i = 0; i < 256; i++)
			{
				red_LUT[i]=(byte)(i & 0xff);
				green_LUT[i]=(byte)(i & 0xff);
				blue_LUT[i]=(byte)(i & 0xff);
			}
		}

		return (new IndexColorModel(8, 256, red_LUT, green_LUT, blue_LUT));

	}// end of makeGrayscaleColorModel()


	//******************************************************************************
	//*		M A K E  C O L O R  M O D E L
	//******************************************************************************
	ColorModel makeColorModel()
	{
		byte[] 		red_LUT = null, green_LUT, blue_LUT = null;
		int 		i = 0, k = 0;

		red_LUT = new byte[256];
		green_LUT = new byte[256];
		blue_LUT = new byte[256];

		for(i = 0; i < 256; i++)
		{
			red_LUT[i] = this.color_table[i];
			green_LUT[i] = this.color_table[256+i];
			blue_LUT[i] = this.color_table[512+i];
		}
		return(new IndexColorModel(8, 256, red_LUT, green_LUT, blue_LUT));

	}// end of makeColorModel()


	//******************************************************************
	//*			G E T  F I L E  I N F O
	//******************************************************************
	void	 getFileInfo() throws Exception
	{
		return;
	}


	//******************************************************************************
	//*		S E T  F I L E  I O  I N F O
	//*		Sets the following parameters:
	//*		image_size_in_bytes, num_pixels, buffer_size
	//******************************************************************************
	void setFileIOInfo()
	{
		this.image_size_in_bytes = image_width * image_height * this.bytes_per_pixel;
		this.num_pixels = image_width * image_height;

		if (this.file_size > image_size_in_bytes)
			this.file_size = this.image_size_in_bytes;
		this.buffer_size = this.file_size/25;

		if (this.buffer_size < 8192)
			this.buffer_size = 8192;
		else
			this.buffer_size = (buffer_size/8192)*8192;

		return;

	}// end of setFileIOInfo()


	//******************************************************************
	//*			G E T  V A L U E
	//******************************************************************
	int getValue(RandomAccessFile raf, int field_type, int count) throws Exception
	{
		int value = 0;
		int unused = 0;

		if (field_type == SHORT_FIELD && count == 1)
		{
			value = getShort(raf);
			unused = getShort(raf);
		}
		else
			value = getInt(raf);

		return(value);

	}// end of getValue()


	//******************************************************************
	//*			G E T  I N T
	//******************************************************************
	final int getInt(RandomAccessFile  raf) throws Exception
	{
		int b1 = raf.read();
		int b2 = raf.read();
		int b3 = raf.read();
		int b4 = raf.read();
		if (this.swap_bytes)
			return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1 << 0));
		else
			return ((b1 << 24) + (b2 << 16) + (b3 << 8) + b4);

	}// end of getInt()


	//*************************************************************************
	//*		G E T  I N T
	//*************************************************************************
	static  int  getInt(String num_string)
	{
		Double		d = null;
		try
		{
			d = new Double(num_string);
		}
		catch (NumberFormatException e)
		{
			return(NOT_FOUND);
		}// if an exception was generated

		return (d.intValue());

	}


	//******************************************************************
	//*			G E T  S H O R T
	//******************************************************************
	int getShort(RandomAccessFile raf) throws Exception
	{
		int b1 = raf.read();
		int b2 = raf.read();
		if (this.swap_bytes)
			return ((b2 << 8) + b1);
		else
			return ((b1 << 8) + b2);

	}// end of getShort()


	//******************************************************************
	//*			C O U N T  F I L E S  I N  D I R E C T O R Y
	//******************************************************************
	int		countFilesInDirectory(String directory)
	{
		File			first_file = null;
		String[]		file_list = null;
		String			cur_filename = null;
		int				num_files = 0, i = 0;

		first_file = new File (directory);
		if (first_file != null && first_file.exists())
		{
			file_list = first_file.list();

			if (file_list != null)
			{
				//***** Count the files
				try
				{
					do
					{
						cur_filename = new String(file_list[num_files]);
						num_files += 1;
					}// do
					while(true);
				}// try
				catch (ArrayIndexOutOfBoundsException e)
				{
					return(num_files);
				}
			}// if we have files
		}// if the file exists

		return(num_files);

	}// end of countFilesInDirectory()


	//******************************************************************
	//*			C O U N T  V A L I D  F I L E S  I N  D I R E C T O R Y
	//*			Returns the number of files in the directory that are
	//*			of type filename_syntax
	//******************************************************************
	int		countValidFilesInDirectory(String directory, int filename_syntax)
	{
		File			first_file = null;
		String[]		file_list = null;
		String			cur_filename = null;
		int				file_counter = 0, valid_files = 0;

		first_file = new File (directory);
		if (first_file != null && first_file.exists())
		{
			file_list = first_file.list();

			if (file_list != null)
			{
				//***** Count the files
				try
				{
					do
					{
						cur_filename = new String(file_list[file_counter]);
						if (NameUtils.isFilenameFormat(cur_filename, filename_syntax))
							valid_files += 1;
						file_counter += 1;
					}// do
					while(true);
				}// try
				catch (ArrayIndexOutOfBoundsException e)
				{
					return(valid_files);
				}
			}// if we have files
		}// if the file exists

		return(valid_files);

	}// end of countValidFilesInDirectory()


	//*************************************************************************
	//*		C R E A T E  F I L E N A M E
	//*		Modified 11/21/02 for v2.13
	//*************************************************************************
	String  createFilename(String base_name, int timepoint) throws Exception
	{
		String		filename = null;

		switch (this.filename_syntax)
		{
			case STANDARD_NON_PADDED:
				  filename = createNonPaddedName(base_name, timepoint);
				  break;
			case STANDARD_THREE_PADDED:
				  filename = createThreePaddedName(base_name, timepoint);
				  break;
			case STANDARD_FOUR_PADDED:
				  filename = createFourPaddedName(base_name, timepoint);
				  break;
		}// switch

		if (filename == null)
			throw new Exception("Unable to create filename!");

		return(filename);

	}// end of createFilename()


	//*************************************************************************
	//*		C R E A T E  N O N  P A D D E D  N A M E
	//*		Returns a name in the format "Filename.1", "Filename.2", ...
	//*************************************************************************
	static String createNonPaddedName(String base_name, int timepoint)
	{
		String		final_name = new String(base_name);

		final_name += '.';
		final_name += timepoint;


		return(final_name);
	}// end of createNonPaddedName()


	//*************************************************************************
	//*		C R E A T E  T H R E E  P A D D E D  N A M E
	//*		Returns a name in the format "Filename.001", "Filename.002", ...
	//*************************************************************************
	static String createThreePaddedName(String base_name, int timepoint)
	{
		String		final_name = new String(base_name);

		final_name += '.';

		if (timepoint < 10)
			final_name += "00";
		else if (timepoint < 100)
			final_name += "0";

		final_name += timepoint;

		return(final_name);

	}// end of createThreePaddedName()


	//*************************************************************************
	//*		C R E A T E  F O U R  P A D D E D  N A M E
	//*		Returns a name in the format "Filename.0001", "Filename.0002", ...
	//*************************************************************************
	static String createFourPaddedName(String base_name, int timepoint)
	{
		String		final_name = new String(base_name);

		final_name += '.';

		if (timepoint < 10)
			final_name += "000";
		else if (timepoint < 100)
			final_name += "00";
		else if (timepoint < 1000)
			final_name += "0";

		final_name += timepoint;


		return(final_name);

	}// end of createFourPaddedName()

}// end of class ImageReader

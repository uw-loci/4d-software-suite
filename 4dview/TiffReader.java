//******************************************************
//*		T I F F  P R O C E S S O R
//******************************************************

//Some code modified from Wayne Rasband's freeware application "NIH-Image/J"

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

class TiffReader extends ImageReader
{
	//***** TIFF HEADER CONSTANTS
	final		static	int			NEW_SUBFILE_TYPE = 254;
	final		static	int			IMAGE_WIDTH = 256;
	final		static	int			IMAGE_LENGTH = 257;
	final		static	int			BITS_PER_SAMPLE = 258;
	final		static	int			COMPRESSION = 259;
	final		static	int			PHOTO_INTERP = 262;
	final		static	int			STRIP_OFFSETS = 273;
	final		static	int			SAMPLES_PER_PIXEL = 277;
	final		static	int			ROWS_PER_STRIP = 278;
	final		static	int			STRIP_BYTE_COUNT = 279;
	final		static	int			X_RESOLUTION = 282;
	final		static	int			Y_RESOLUTION = 283;
	final		static	int			PLANAR_CONFIGURATION = 284;
	final		static	int			RESOLUTION_UNIT = 296;
	final		static	int			COLOR_MAP = 320;
	final		static	int			IMAGE_HDR = -22222; //43314


	//******************************************************************
	//*			I N I T
	//******************************************************************
	public TiffReader()
	{
		super();

		return;

	}// init


	//******************************************************************
	//*		I N I T
	//******************************************************************
	public TiffReader(String filename)
	{
		super();

		this.filename = filename;

		return;

	}// init


	//******************************************************************
	//*			G E T  F I L E  I N F O
	//******************************************************************
	void	 getFileInfo() throws Exception
	{
		int 		ifd_offset_to_image_data = 0;



		if (filename == null)
			throw new Exception("No filename specified!");

		f = new File(Settings.input_directory + filename);
		raf = new RandomAccessFile(f, "r");

		//***** Get offset to image data
		ifd_offset_to_image_data = getOffsetToImageData(raf);
		if (ifd_offset_to_image_data < 0)
		{
			raf.close();
			throw (new Exception("Offset to image data < 0"));
		}

		//***** Get the image file directory info
		raf.seek(ifd_offset_to_image_data);
		openIFD(raf);
		raf.close();

		//**** Get the number of slices
		DataInfo.num_frames = getNumSlices();

		//***** Set the IO information
		setFileIOInfo();

		got_file_info = true;

		return;

	}// end of getFileInfo()


	//******************************************************************
	//*			G E T  I M A G E
	//*		Modified 5/2/03 for v1.72
	//******************************************************************
	Image	getImage(String input_directory, String filename, long slice) throws FileNotFoundException, Exception
	{
		Image				img = null;

		try
		{
			//***** Try opening as a tiff
			is = getImageInputStream(input_directory, filename); //open the file

			if (is != null)
			{
				//***** Try opening as a tiff
				if (!got_file_info)
					getFileInfo(); // get the file info

				img = getSlice(is, slice); // read in the tiff file

				is.close();

				if (img != null)
					image_format = ImageReader.TIFF_FILE;
			}// if the info stream is available
			else
				throw new Exception("Unable to open input stream!");

		}// try
		catch (FileNotFoundException fnfe)
		{
			if (is != null)
				is.close();
			throw(fnfe);
		}
		catch (Exception e)
		{
			if (is != null)
				is.close();
			throw(e);
		}

		return(img);

	}// end of getImage()


	//******************************************************************************
	//*		G E T  S L I C E
	//*		Skips to the requested image and reads it
	//*		Added 5/9/03 for v1.72
	//******************************************************************************
	public Image  getSlice(long slice) throws Exception
	{
		Image		img = null;

		//***** Try opening as a tiff
		is = getImageInputStream(Settings.input_directory, filename); //open the file

		if (is == null)
			throw new Exception("Unable to open file!");

		//***** Get file info
		if (!got_file_info)
			getFileInfo(); // get the file info

		img = getSlice(is, slice);

		return(img);

	}// end of getSlice()


	//******************************************************************************
	//*		G E T  S L I C E
	//*		Skips to the requested image and reads it
	//*		Modified 5/16/03 for v1.72
	//******************************************************************************
	private Image  getSlice(DataInputStream is, long slice) throws Exception
	{
		long		skipped_bytes = 0;
		Image		img = null;
		int			offset_to_slice = 0;// bytes to skip to get to the slice's data

		try
		{
			//***** Get file info
			if (!got_file_info)
				getFileInfo(); // get the file info

			if (slice <= 0)
				slice = 1;

			file_size = is.available();


			//***** Open the image
			if (offset_to_image_data != 0)
			{
				offset_to_slice = (int)(offset_to_image_data + ((slice - 1) * image_size_in_bytes));

				if (file_size < (offset_to_image_data + image_size_in_bytes))
					throw (new Exception("Slice not available in image file."));

				//***** Skip to the start of the image data
				skipped_bytes = is.skip((long)offset_to_slice);
				if (skipped_bytes != offset_to_slice)
					throw (new Exception("Cannot skip to image data."));

				file_size = (int)(file_size - skipped_bytes);
			}// if there's an offset_to_image_data

			switch (this.image_type)
			{
				case GRAY_8_BIT_IMAGE:
				case COLOR_8_BIT_IMAGE:
					img = read8BitImage(is);
					break;
				case GRAY_16_BIT_IMAGE:
					img = read16BitImage(is);
					break;
				case GRAY_32_BIT_IMAGE:
					img = read32BitImage(is);
					break;
				case RGB_IMAGE_CHUNKY:
					img = readChunkyRGBImage(is);
					break;
				case RGB_IMAGE_PLANAR:
					img = readPlanarRGBImage(is);
					break;
			}// switch


			if (img != null)
				image_format = TIFF_FILE;

		}// try

		catch (Exception e)
		{
			throw (e); // rethrow the exception
		}// catch

		return(img);

	}// end of getSlice()


	//******************************************************************************
	//*		G E T  N E X T  S L I C E
	//******************************************************************************
	private Image  getNextSlice(DataInputStream is) throws Exception
	{
		Image		img = null;

		try
		{
			file_size = is.available();

			if (file_size < image_size_in_bytes)
					throw (new Exception("Slice not available in image file."));

			switch (this.image_type)
			{
				case GRAY_8_BIT_IMAGE:
				case COLOR_8_BIT_IMAGE:
					img = read8BitImage(is);
					break;
				case GRAY_16_BIT_IMAGE:
					img = read16BitImage(is);
					break;
				case GRAY_32_BIT_IMAGE:
					img = read32BitImage(is);
					break;
				case RGB_IMAGE_CHUNKY:
					img = readChunkyRGBImage(is);
					break;
				case RGB_IMAGE_PLANAR:
					img = readPlanarRGBImage(is);
					break;
			}// switch

			if (img != null)
				image_format = TIFF_FILE;

		}// try

		catch (Exception e)
		{
			throw (e); // rethrow the exception
		}// catch

		return(img);

	}// end of getNextSlice()

	//**************************************************************************
	//*		G E T  O F F S E T  T O  I M A G E  D A T A
	//**************************************************************************
	private int getOffsetToImageData(RandomAccessFile raf) throws Exception
	{
		// Open 8-byte file header at start of file.
		// Returns the offset_to_image_data in bytes to the first IFD

		int 	byte_order = 0, magic_number = 0;
		int		offset_to_image_data = 0;

		byte_order = raf.readShort();

		if (byte_order == 0x4949) // "II"
			this.swap_bytes = true;
		else if (byte_order == 0x4d4d) // "MM"
			this.swap_bytes = false;
		else
		{
			raf.close();
			throw (new Exception("Not a valid TIFF file"));
		}

		magic_number = getShort(raf); // 42

		if (magic_number != 42)
			throw (new Exception("Not a valid TIFF file"));

		offset_to_image_data = getInt(raf); // get the offset_to_image_data

		return (offset_to_image_data);

	}// end of getOffsetToImageData()


	//******************************************************************
	//*			O P E N  I F D
	//******************************************************************
	private void	openIFD (RandomAccessFile raf) throws Exception
	{
		// Get Image File Directory data

		int 		tag = 0, field_type = 0;
		int			count = 0, value = 0;
		int 		num_entries = 0, i = 0;
		long		save_loc = 0;

		//***** Get the number of entries
		num_entries = getShort(raf);

		if (num_entries < 1)
			throw new Exception("Invalid TIFF header.");

		//**** Get each entry
		for (i = 0; i < num_entries; i++)
		{
			tag = getShort(raf);
			field_type = getShort(raf);
			count = getInt(raf);
			value = getValue(raf, field_type, count);

			if (tag == 0)
				throw new Exception("Invalid TIFF header.");

			switch (tag)
			{
				case IMAGE_WIDTH:
					image_width = value;
					DataInfo.data_width = value;
					break;
				case IMAGE_LENGTH:
					image_height = value;
					DataInfo.data_height = value;
					break;
				case STRIP_OFFSETS:
					if (count == 1)
						offset_to_image_data = value;
					else
					{
						save_loc = raf.getFilePointer();
						raf.seek(value);
						offset_to_image_data = getInt(raf); // Assumes contiguous strips
						raf.seek(save_loc); // return to the saved location
					}
					break;
				case PHOTO_INTERP:
					this.zero_is_black = value == 1;
					break;
				case BITS_PER_SAMPLE:
						if (count == 1)
						{
							if (value == 8)
							{
								this.image_type = GRAY_8_BIT_IMAGE;
								this.bytes_per_pixel = 1;
							}
							else if (value == 16)
							{
								this.image_type = GRAY_16_BIT_IMAGE;
								this.bytes_per_pixel = 2;
							}
							else if (value == 32)
							{
								this.image_type = GRAY_32_BIT_IMAGE;
								this.bytes_per_pixel = 4;
							}
							else
								throw new Exception("Unsupported TIFF BitsPerSample: " + value);
						}
						break;
				case SAMPLES_PER_PIXEL:
					if (value == 3)
					{
						this.image_type = RGB_IMAGE_CHUNKY;
						this.bytes_per_pixel = 3;
					}
					else if (value != 1)
						throw new Exception("Unsupported TIFF SamplesPerPixel: " + value);
					break;
				case PLANAR_CONFIGURATION:
					if (value == 2 && this.image_type == RGB_IMAGE_CHUNKY)
					{
						this.image_type = RGB_IMAGE_PLANAR;
						this.bytes_per_pixel = 3;
					}
					break;
				case COMPRESSION:
					if (value !=1 )
						throw new Exception("TIFF compression is not supported");
				case COLOR_MAP:
					if (count == 768)
						getColorMap(raf, value);
						this.image_type = COLOR_8_BIT_IMAGE;
						this.bytes_per_pixel = 1;
					break;
				default:
			}// switch
		}// for each entry

		return;

	}// end of openIFD


	//******************************************************************************
	//*		G E T  N U M  S L I C E S
	//*		Determines the number of slices available by doing math on the file size
	//*		Modified 45/2/03 for v1.72
	//******************************************************************************
	private int	getNumSlices() throws Exception
	{
		int							is_size = 0;
		int							n_slices = 0;
		int							img_size = 0;
		DataInputStream 			is = null;

		img_size = image_width * image_height * this.bytes_per_pixel;
		if (img_size <= 0)
			return(0);

		is = new DataInputStream(new BufferedInputStream(new FileInputStream(Settings.input_directory + this.filename)));
		is_size = is.available();// get the size of the file in bytes
		is_size -= offset_to_image_data;
		n_slices = is_size/img_size;// determine num slices
		is.close();

		return(n_slices);

	}// end of getNumSlices()


	//******************************************************************
	//*			G E T  C O L O R  M A P
	//******************************************************************
	void getColorMap(RandomAccessFile raf, int offset_to_image_data) throws Exception
	{
		byte[] 		l_color_table;
		long 		save_loc = 0;
		int			bytes_read = 0;
		int			j = 0, i = 0;

		l_color_table = new byte[768*2];
		save_loc = raf.getFilePointer();

		raf.seek(offset_to_image_data);
		bytes_read = raf.read(l_color_table);
		raf.seek(save_loc);
		if (bytes_read != 768*2)
			return;

		this.color_table = new byte[768];

		j = 0;
		if (this.swap_bytes)
			j++;
		for (i = 0; i < 768; i++)
		{
			this.color_table[i] = l_color_table[j];
			j += 2;
		}

		return;

	}// end of getColorMap


	//*************************************************************************
	//*		C R E A T E  F I L E N A M E
	//*		Modified 11/21/02 for v2.13
	//*************************************************************************
	String  createFilename(String base_name, int timepoint) throws Exception
	{
		String		filename = null;

		if (base_name == null)
			throw new Exception("Unable to create filename!");


		filename = new String(base_name + timepoint + ".");

		if (this.file_extension != null)
			filename += this.file_extension;
		else
			filename += "TIF";

		if (filename == null)
			throw new Exception("Unable to create filename!");

		return(filename);

	}// end of createFilename()


}// end of class TiffReader

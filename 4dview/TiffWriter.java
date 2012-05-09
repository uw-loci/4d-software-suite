//Some code modified from Wayne Rasband's freeware application ImageJ

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

class TiffWriter extends ImageWriter
{

	//***** TIFF HEADER CONSTANTS
	static final	int			BPS_DATA_SIZE = 6;
	static final	int			HEADER_SIZE = 8;
	static final	int			NUM_ENTRIES = 9;
	static final	int			IFD_SIZE = 2 + (NUM_ENTRIES * 12) + 4; // num_entries + ImageFileDirectory entries + next_IFD
	static final	int			OFFSET_TO_IMAGE_DATA = 768;

	static final	int			NEW_SUBFILE_TYPE = 254;
	static final	int			IMAGE_WIDTH = 256;
	static final	int			IMAGE_LENGTH = 257;
	static final	int			BITS_PER_SAMPLE = 258;
	static final	int			COMPRESSION = 259;
	static final	int			PHOTO_INTERP = 262;
	static final	int			STRIP_OFFSETS = 273;
	static final	int			SAMPLES_PER_PIXEL = 277;
	static final	int			ROWS_PER_STRIP = 278;
	static final	int			STRIP_BYTE_COUNT = 279;
	static final	int			X_RESOLUTION = 282;
	static final	int			Y_RESOLUTION = 283;
	static final	int			PLANAR_CONFIGURATION = 284;
	static final	int			RESOLUTION_UNIT = 296;
	static final	int			COLOR_MAP = 320;
	static final	int			IMAGE_HDR = -22222; //43314


					boolean		invert;



	//******************************************************************************
	//*		I N I T
	//******************************************************************************
	public TiffWriter(String file_name, int width, int height) throws Exception
	{

		super(file_name, width, height);

		invert = false;

		return;

	}// end of init()


	//******************************************************************************
	//*		C R E A T E  S T A C K
	//******************************************************************************
	void createStack (int n_slices) throws Exception
	{
		int						image_data_size = 0, image_size_in_bytes = 0;
		int						next_IFD = 0;


		if (n_slices < 1)
			throw (new Exception("Need at least one slice in a stack."));

		this.num_slices = n_slices;

		image_size_in_bytes = getImageSizeInBytes(image_width, image_height);
		image_data_size = image_size_in_bytes * num_slices;

		if (num_slices > 1)
			next_IFD = OFFSET_TO_IMAGE_DATA + image_data_size;
		else
			next_IFD = 0;
		writeTiffHeader(next_IFD);

		return;

	}// end of createStack


	//******************************************************************************
	//*		S A V E  I M A G E
	//******************************************************************************
	void saveImage (Image img) throws Exception
	{
		int						bps_size = 0;
		byte[]					filler = null;
		int						next_IFD = 0;

		try
		{
			if (!ok_to_write)
				throw (new Exception("Output Stream is not open."));
			if (img == null)
				return;

			writeTiffHeader(next_IFD);
			writeImageSlice(img);
			os.close();
			ok_to_write = false;

		}
		catch (Exception e)
		{
			os.close();
			throw(e);
		}

		return;

	}// end of saveImage()


	//******************************************************************************
	//*		S A V E  S T A C K
	//*		image_array contains the individual java.awt.image objects
	//******************************************************************************
	void saveStack (Vector image_array) throws Exception
	{
		int						i = 0;
		int						image_data_size = 0, image_size_in_bytes = 0;
		int						next_IFD = 0;

		try
		{

			if (!ok_to_write)
				throw (new Exception("Output Stream is not open."));

			if (image_array == null)
				return;

			if (image_array.size() <= 1)
				throw (new Exception("File is not a TIFF stack."));

			//***** Get variable info
			num_slices = image_array.size();
			image_size_in_bytes = getImageSizeInBytes(image_width, image_height);
			image_data_size = image_size_in_bytes * num_slices;
			next_IFD = OFFSET_TO_IMAGE_DATA + image_data_size;

			writeTiffHeader(next_IFD);
			writeImageSlices(image_array);
			writeStackFooter();
			os.close();
			ok_to_write = false;

		}
		catch (Exception e)
		{
			os.close();
			throw(e);
		}

		return;

	}// end of saveStack()


	//*****************************************************************
	//*		W R I T E  T I F F  H E A D E R
	//*****************************************************************
	private void writeTiffHeader(int next_IFD) throws Exception
	{
		int				bps_size = 0;
		byte[]			filler = null;

		if (!ok_to_write)
			throw (new Exception("Output Stream is not open."));

		writeTiffIdentifier();
		writeTiffImageFileDirectory(OFFSET_TO_IMAGE_DATA, next_IFD);
		if (output_file_type == RGB_IMAGE)
		{
			writeBitsPerPixel();
			bps_size = BPS_DATA_SIZE;
		}
		filler = new byte[OFFSET_TO_IMAGE_DATA - (HEADER_SIZE + IFD_SIZE + bps_size)]; // create an empty buffer to pad out the header to 768 bytes
		os.write(filler);

		return;

	}// end of writeTiffHeader()


	//******************************************************************************
	//*		W R I T E  I M A G E  S L I C E
	//*		Writes an image as a slice of a tiff stack
	//******************************************************************************
	void writeImageSlice(Image img) throws Exception
	{
		super.writeImageSlice(img);

	}// end of writeImageSlice()


	//******************************************************************************
	//*		C L O S E  S T A C K
	//******************************************************************************
	public void closeStack() throws Exception
	{
		writeStackFooter();

		os.close();
		ok_to_write = false;

	}// end of closeStack()


	//*****************************************************************
	//*		W R I T E  S T A C K  F O O T E R
	//*****************************************************************
	private void writeStackFooter() throws Exception
	{
		int		i = 0, image_size = 0, image_data_size = 0;
		int		image_offset = OFFSET_TO_IMAGE_DATA;
		int		next_IFD = 0;


		if (!ok_to_write)
			throw (new Exception("Output Stream is not open."));

		image_size = getImageSizeInBytes(image_width, image_height);
		image_data_size = image_size * num_slices;
		next_IFD = OFFSET_TO_IMAGE_DATA + image_data_size;

		//***** Write the footer info to the file, basically the IFDs for slices 2 through n
		for (i = 2; i <= num_slices; i++)
		{
			if (i == num_slices)
				next_IFD = 0;
			else
				next_IFD += IFD_SIZE;

			image_offset += image_size;
			writeTiffImageFileDirectory(image_offset, next_IFD);

		}// for each image after the first


		return;
	}// end of writeStackFooter()


	//******************************************************************************
	//*		W R I T E  T I F F  I D E N T I F I E R
	//******************************************************************************
	private void writeTiffIdentifier() throws Exception
	{
		try
		{
			if (!ok_to_write)
				throw (new Exception("Output Stream is not open."));

			byte[] hdr = new byte[8];
			hdr[0] = 77; // "MM" (Motorola byte order)
			hdr[1] = 77;
			hdr[2] = 0;  // 42 (magic number)
			hdr[3] = 42;
			hdr[4] = 0;  // 8 (offset to first ImageFileDirectory)
			hdr[5] = 0;
			hdr[6] = 0;
			hdr[7] = 8;
			os.write(hdr);
		}
		catch (Exception e)
		{
			throw(e);
		}

		return;

	}// end of writeTiffIdentifier()


	//******************************************************************************
	//*		W R I T E  T I F F  E N T R Y
	//******************************************************************************
	private void writeTiffEntry(int tag, int field_type, int count, int value) throws Exception
	{
		if (!ok_to_write)
			throw (new Exception("Output Stream is not open."));

		os.writeShort(tag);
		os.writeShort(field_type);
		os.writeInt(count);
		if (count== 1 && field_type == 3)
			value <<= 16;
		os.writeInt(value);

		return;

	}// end of writeTiffEntry()


	//***********************************************************************************
	//*		W R I T E  T I F F  I M A G E  F I L E  D I R E C T O R Y
	//*
	//*		An IFD provides information about a specific image such as height, width
	//*		bit depth, etc.  image_offset is the offset in bytes from the start of the
	//*		file to the beginning of the image for which the IFD is supplying information
	//*		next_IFD is the offset in bytes from the end of the current IFD to the
	//*		beginning of the next IFD
	//************************************************************************************
	private void writeTiffImageFileDirectory(int image_offset, int next_IFD) throws Exception
	{
		int 		bits_per_sample = 8, photo_interp = 0;
		int			bytes_per_pixel = 1, samples_per_pixel = 1;
		int			image_size_in_bytes = 0;
		int			tag_data_offset = 0;


		if (!ok_to_write)
			throw (new Exception("Output Stream is not open."));

		switch (Settings.output_file_type)
		{
			case GRAY_8_BIT_IMAGE:
				 bits_per_sample = 8;
				 photo_interp = invert?0:1;
				 break;
			case GRAY_16_BIT_IMAGE:
				 bits_per_sample = 16;
				 photo_interp = invert?0:1;
				 bytes_per_pixel = 2;
				 break;
			case RGB_IMAGE:
				 photo_interp = 2;
				 bytes_per_pixel = 3;
				 samples_per_pixel = 3;
				 break;
			default:
				 throw (new Exception("Can't save this format as TIFF file!"));
		}// end of switch

		image_size_in_bytes = getImageSizeInBytes(image_width, image_height);
		tag_data_offset = HEADER_SIZE + IFD_SIZE;

		try
		{
			os.writeShort(NUM_ENTRIES);

			writeTiffEntry(NEW_SUBFILE_TYPE, 4, 1, 0);
			writeTiffEntry(IMAGE_WIDTH,      3, 1, image_width);
			writeTiffEntry(IMAGE_LENGTH,     3, 1, image_height);
			if (Settings.output_file_type == RGB_IMAGE)
			{
				writeTiffEntry(BITS_PER_SAMPLE,  3, 3, tag_data_offset);
				tag_data_offset += BPS_DATA_SIZE;
			}
			else
				writeTiffEntry(BITS_PER_SAMPLE,  3, 1, bits_per_sample);
			writeTiffEntry(PHOTO_INTERP,     3, 1, photo_interp);
			writeTiffEntry(STRIP_OFFSETS,    4, 1, image_offset);
			writeTiffEntry(SAMPLES_PER_PIXEL,3, 1, bytes_per_pixel);
			writeTiffEntry(ROWS_PER_STRIP,   3, 1, image_height);
			writeTiffEntry(STRIP_BYTE_COUNT, 4, 1, image_size_in_bytes);

			//***** Write the offset to the next IFD, this will be 0 for the last IFD
			os.writeInt(next_IFD);


		}
		catch (Exception e)
		{
			throw (e);
		}

		return;

	}// end of writeTiffImageFileDirectory


	//*****************************************************************
	//*		W R I T E  B I T S  P E R  P I X E L
	//*****************************************************************
	void writeBitsPerPixel() throws Exception
	{
		if (!ok_to_write)
			throw (new Exception("Output Stream is not open."));

		os.writeShort(8);
		os.writeShort(8);
		os.writeShort(8);

		return;

	}// end of writeBitsPerPixel()


} // end of TiffWriter



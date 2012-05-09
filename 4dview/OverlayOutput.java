import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Vector;

class OverlayOutput
{

	static final int 						NOT_FOUND = -1;
	static ByteArrayInputStream				before_buffer = null, after_buffer = null;

	//*************************************************************************
	//*		S A V E  O V E R L A Y  T O  D I S K
	//*************************************************************************
	static	void  saveOverlayToDisk(ObjectArray object_array, HeaderArray header_array,  int focal_plane, int frame) throws Exception
	{
		FileOutputStream		os = null;
		String				filename = null;

		filename = OverlayInput.makeOverlayFilename(focal_plane);
		if (filename == null)
			return;

		os = Output.getFileOutputStream((Frame)Settings.main_window, filename);

		Settings.output_directory = Output.getOutputDirectory();

		saveOverlayToDisk(Settings.output_directory, object_array, header_array, focal_plane, frame);

		return;
	}

	//*************************************************************************
	//*		S A V E  O V E R L A Y  T O  D I S K
	//*		Modified 2/19/03 for v1.71
	//*************************************************************************
	static  void  saveOverlayToDisk(String directory, ObjectArray object_array, HeaderArray header_array,  int focal_plane, long frame) throws Exception
	{
		File					old_file = null, new_file = null;
		FileOutputStream		os = null;
		ByteArrayInputStream	header_in_buffer = null;
		ByteArrayOutputStream	header_out_buffer = new ByteArrayOutputStream();
		ByteArrayOutputStream	overlay_buffer = new ByteArrayOutputStream();
		HeaderEntry				he = new HeaderEntry(focal_plane, frame);
		String					filename = null;
		long					file_size = 0, overlay_size = 0;
		long					new_header_array_size = 0;//old_header_array_size = 0,
		long					bytes_written = 0;
		int						i = 0;
		int						overlay_index = NOT_FOUND;
		BooleanWrapper			replacing = new BooleanWrapper(false);
		boolean					ok = false;


		Settings.output_directory = directory;

		if (object_array == null || header_array == null)
			return;

		filename = OverlayInput.makeOverlayFilename(focal_plane);
		if (filename == null)
			return;

		file_size = getFileSize(Settings.output_directory, filename);

		//***** Write overlay to stream
		overlay_size = object_array.writeOverlayToStream(overlay_buffer);
		overlay_buffer.close();

		//***** Read headers from file, if it exists
		if (file_size > 0)
		{
			header_in_buffer = Output.getByteArrayInputStream(Settings.output_directory, filename);
			header_array.readHeadersFromStream(header_in_buffer);
			header_in_buffer.close();
		}

		//***** Get the new index position for the entry we'll create
		overlay_index = header_array.getNewOverlayIndex(frame, replacing);

		if (overlay_index == NOT_FOUND)
			throw new Exception("Illegal overlay index returned by getNewOverlayIndex()!");

		//***** Get before and after data
		getBeforeAndAfterData(filename, header_array, overlay_index,  file_size, replacing.getValue());

		//***** Create a new header entry in the header array
		overlay_index = header_array.addNewHeaderArrayEntry(replacing, overlay_size, he);

		//***** Find out the size of the array
		new_header_array_size = header_array.getArraySizeInBytes();

		//***** Update header offsets and overlay_size fields to reflect the addition of the new entry and overlay
		updateHeaderFields(header_array, overlay_index, new_header_array_size, overlay_size);

		//***** Write the updated headers to the stream
		new_header_array_size = header_array.writeHeadersToStream(header_out_buffer);
		header_out_buffer.close();

		//***** Make the output stream
		os = Output.getFileOutputStream(Settings.output_directory, "temp");

		//***** Write the buffers to the disk
		bytes_written = Output.writeBufferToOutputStream(os, header_out_buffer);

		if (before_buffer != null)
		{
			bytes_written = Output.writeBufferToOutputStream(os, before_buffer);
		}
		bytes_written = Output.writeBufferToOutputStream(os, overlay_buffer);
		if (after_buffer != null)
		{
			bytes_written = Output.writeBufferToOutputStream(os, after_buffer);
		}

		os.flush();
		os.close();

		old_file = new File (Settings.output_directory, filename);
		ok = old_file.delete();

		old_file = new File(Settings.output_directory, filename);
		new_file = new File (Settings.output_directory, "temp");
		ok = new_file.renameTo(old_file);

		return;

	}//end of saveOverlayToDisk()


	//****************************************************************************
	//*		 G E T  F I L E  S I Z E
	//****************************************************************************
	static long getFileSize(String directory, String filename) throws Exception
	{
		FileInputStream		is = null;
		long 				file_size = 0;

		//***** Get the size of the file
		try
		{
			is = Output.getFileInputStream(directory, filename);
		}
		catch (Exception e)
		{
			is = null;
			return(file_size);

		}
		if (is != null)
		{
			file_size = is.available();
			is.close();
		}

		return(file_size);

	}// end of getFileSize()


	//****************************************************************************
	//*		 U P D A T E  H E A D E R  F I E L D S
	//*		 This method builds the header info from scratch
	//****************************************************************************
	static void updateHeaderFields (HeaderArray header_array, int index, long size_of_headers, long overlay_size) throws Exception
	{
		HeaderEntry					he = null, next_entry = null;
		int							i = 0;
		long						overlay_offset = 0;

		for (i = 0; i < header_array.getNumEntries(); i++)
		{
			he = header_array.getEntry(i);

			//***** If we're adding or replacing an overlay, we need to note its size
			if (i == index)
				he.size_of_overlay = overlay_size;

			he.offset_to_overlay = size_of_headers + overlay_offset;

			//***** Increment overlay_offset by the size of the overlay
			overlay_offset += he.size_of_overlay;
		}// for each header

		return;

	}// end of updateHeaderFields()


	//*************************************************************************
	//*		G E T  B E F O R E  A N D  A F T E R  D A T A
	//*		Modified 3/28/03 for v1.72
	//*************************************************************************
	static public void getBeforeAndAfterData(String filename, HeaderArray header_array, int overlay_index,
											 long file_size, boolean replacing_or_erasing) throws Exception
	{
		long			start_position = 0, end_position = 0;
		HeaderEntry		he = null;

		if (header_array.getNumEntries() <= 0)
			return;

		//***** Get the BEFORE BUFFER, the data that comes before our new overlay
		if (overlay_index == 0)
		{
			start_position = 0;
			end_position = 0;
		}// if we're putting the new overlay in the first position
		else
		{
			//***** Start position is the beginning of the overlay data
			start_position = header_array.getEntry(0).offset_to_overlay;

			//***** End position is the end of the last overlay prior to the one we're erasing, inserting, or replacing
			end_position = header_array.getEntry(overlay_index -1).offset_to_overlay +
						   header_array.getEntry(overlay_index -1).size_of_overlay;
		}// if we're not putting the new overlay into the first position

		//***** If there's data to get, stick it in the before_buffer
		if (end_position > start_position)
		{
			before_buffer = OverlayInput.getDiskData(filename, start_position, end_position);
			if (before_buffer == null)
				throw (new Exception("Before buffer was null."));
		}// if there's beginning data to get

		//***** Get the data that comes after the place our new overlay will go
		if (replacing_or_erasing)
		{
			//***** If we're replacing or erasing an overlay, the end data starts after the overlay we're replacing
			start_position = header_array.getEntry(overlay_index).offset_to_overlay +
							 header_array.getEntry(overlay_index).size_of_overlay;

			//**** And ends with the end of the file
			end_position = file_size;
		}// if we're replacing or erasing
		else
		{
			//***** If we're inserting an overlay, the end data starts at the end of the overlay
			//***** just before the one we're inserting
			start_position = header_array.getEntry(overlay_index-1).offset_to_overlay +
							 header_array.getEntry(overlay_index-1).size_of_overlay;

			//***** And ends at the end of the file
			end_position = file_size;
		}// if we're not replacing

		if (end_position > start_position)
		{
			after_buffer = OverlayInput.getDiskData(filename, start_position, end_position);
			if (after_buffer == null)
				throw(new Exception ("After buffer was null."));
		}// if there's end data to get

		return;

	}// end of getBeforeAndAfterData()


	//*************************************************************************
	//*		E R A S E  O V E R L A Y  F R O M  D I S K
	//*		Modified 2/19/03 for v1.71
	//*		Modified 3/28/03 for v1.72
	//*************************************************************************
	static	void  eraseOverlayFromDisk(String directory, HeaderArray header_array, int focal_plane, long frame) throws Exception
	{
		File					old_file = null, new_file = null;
		FileOutputStream		os = null;
		ByteArrayInputStream	header_in_buffer = null;
		ByteArrayOutputStream	header_out_buffer = new ByteArrayOutputStream();
		HeaderEntry				he = new HeaderEntry(focal_plane, frame);
		String					filename = null;
		long					file_size = 0;
		long					old_header_array_size = 0, new_header_array_size = 0;
		long					erased_overlay_size = 0;
		long					bytes_written = 0;
		int						i = 0;
		int						overlay_index = NOT_FOUND;
		BooleanWrapper			replacing = new BooleanWrapper(false);
		boolean					ok = false;


		Settings.output_directory = directory;

		if (header_array == null)
			throw new Exception("Header array was null.  Overlay not erased!");

		filename = OverlayInput.makeOverlayFilename(focal_plane);
		if (filename == null)
			throw new Exception("Unable to create filename.  Overlay not erased!");

		file_size = getFileSize(Settings.output_directory, filename);

		//***** Read headers from file, if it exists
		if (file_size > 0)
		{
			header_in_buffer = Output.getByteArrayInputStream(Settings.output_directory, filename);
			header_array.readHeadersFromStream(header_in_buffer);
			header_in_buffer.close();
		}
		else
		{
			Settings.main_window.displayMessage("Unable to locate an overlay file for this focal plane!");
			return;
		}

		//***** Find out the size of the array
		old_header_array_size = header_array.getArraySizeInBytes();

		//***** Get the index position for the overlay we're erasing
		overlay_index = header_array.getNewOverlayIndex(frame, replacing);
		if (overlay_index == NOT_FOUND || !replacing.getValue())
		{
			Settings.main_window.displayMessage("Unable to locate an overlay for this position!");
			return;
		}

		//***** Get before and after data
		getBeforeAndAfterData(filename, header_array, overlay_index,  file_size, true);

		//***** Get the size of the overlay we're deleting
		erased_overlay_size = header_array.getEntry(overlay_index).size_of_overlay;

		//**** Delete the header entry from the array
		header_array.removeEntry(overlay_index);

		//***** Find out the size of the array
		new_header_array_size = header_array.getArraySizeInBytes();

		//***** Update header offsets and overlay_size fields to reflect the deltion of the entry and overlay
		updateHeaderFields(header_array,
								   -1, // -1 here will be interpreted as "don't need to deal with size of the overlay"
								   new_header_array_size,
								   0);// size of the overlay is zero 'cause we're not adding one

		//***** Write the new header array to the header_out_buffer
		new_header_array_size = header_array.writeHeadersToStream(header_out_buffer);
		header_out_buffer.close();

		//***** Make the output stream
		os = Output.getFileOutputStream(Settings.output_directory, "temp");

		//***** Write the header_out_buffer to the disk as a file called "temp"
		bytes_written = Output.writeBufferToOutputStream(os, header_out_buffer);

		//***** Write the before_buffer to the disk
		if (before_buffer != null)
		{
			bytes_written = Output.writeBufferToOutputStream(os, before_buffer);
		}

		//***** Write the after_buffer to the disk
		if (after_buffer != null)
		{
			bytes_written = Output.writeBufferToOutputStream(os, after_buffer);
		}

		//***** Close the output streams
		os.flush();
		os.close();

		//**** Delete the old file since the "temp" file has been successfully created
		old_file = new File (Settings.output_directory, filename);
		ok = old_file.delete();

		//***** Rename the "temp" file as the current overlay file
		old_file = new File(Settings.output_directory, filename);
		new_file = new File (Settings.output_directory, "temp");
		ok = new_file.renameTo(old_file);


		return;

	}//end of eraseOverlayFromDisk()

}// end of class OverlayOutput

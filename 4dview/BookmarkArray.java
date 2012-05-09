import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class BookmarkArray
{
	static final String OWNER_STRING = "4DVJ";

	private		Vector		entry_array = null;
	private		String		output_filename = null;
	private		String		input_filename = null;

	//*************************************************************************
	//*		I N I T
	//*************************************************************************
	public BookmarkArray()
	{
		entry_array = new Vector(10,1);
		output_filename = null;
		input_filename = null;

		return;

	}// init


	//*************************************************************************
	//*		R E A D  B O O K M A R K S  F R O M  D I S K
	//*************************************************************************
	void readBookmarksFromDisk() throws Exception
	{

		String						owner_string = null;
		char[]						char_array = null;
		ByteArrayInputStream		buffer = null;
		ObjectInputStream			stream = null;
		int							i = 0;
		int							bookmarks_to_read = 0;


		buffer = getByteArrayInputStream(Settings.main_window);
		stream = new ObjectInputStream(buffer);

		//***** Check the owner code
		owner_string = (String)stream.readObject();
		if (!owner_string.equals("4DVJ"))
		{
			throw (new Exception("There seems to be a problem with the format of this bookmark file!"));
		}

		//***** Read the number of bookmarks
		bookmarks_to_read = ((Integer)stream.readObject()).intValue();

		//***** Read in each bookmark
		for (i = 0; i < bookmarks_to_read; i++)
		{
			readOneBookmark(stream);
		}// for each bookmark

		return;

	}// end of readRookmarksFromDisk()


	//*************************************************************************
	//*		R E A D  O N E  B O O K M A R K
	//*************************************************************************
	void  readOneBookmark(ObjectInputStream  stream) throws Exception
	{
		BookmarkEntry		be = null;

		be = (BookmarkEntry)stream.readObject();

		if (be != null)
			addEntry(be);

		return;

	}// end of readOneBookmark()


	//*************************************************************************
	//*		S A V E  B O O K M A R K S  T O  D I S K
	//*************************************************************************
	void  saveBookmarksToDisk() throws Exception
	{
		FileDialog 						fd = null;
		FileOutputStream				os = null;
		String							output_filename = null, owner_string = OWNER_STRING;
		ByteArrayOutputStream			buffer = null;
		ObjectOutputStream				stream = null;
		BookmarkEntry					be = null;
		int								i = 0;
		long							bytes_written = 0;


		//***** Ask them which bookmark file to open
		fd = new FileDialog(Settings.main_window, "Save Bookmark File...", FileDialog.SAVE);
		fd.setVisible(true);
		output_filename = fd.getFile();
		Settings.output_directory = fd.getDirectory();
		fd.dispose();
		if (output_filename == null)
			throw (new IOException("User cancelled."));

		buffer = new ByteArrayOutputStream();
		stream = new ObjectOutputStream(buffer);

		//***** Write the owner string to the stream
		stream.writeObject(owner_string);

		//***** Write the number of bookmark entries to the stream
		stream.writeObject(new Integer(numBookmarks()));

		//***** Write the entries to the stream
		for (i = 0; i < numBookmarks(); i++)
			stream.writeObject(getEntry(i));

		os = new FileOutputStream(Settings.output_directory + output_filename);

		bytes_written = Output.writeBufferToOutputStream(os, buffer);

		os.flush();
		os.close();

		return;

	}//end of saveBookmarksToDisk()


	//*************************************************************************
	//*		G E T  B Y T E  A R R A Y  I N P U T  S T R E A M
	//*************************************************************************
	ByteArrayInputStream getByteArrayInputStream(Frame parent) throws IOException
	{
		FileDialog 					fd = null;
		ByteArrayInputStream		buffer = null;

		//***** Ask them which bookmark file to open
		fd = new FileDialog(parent, "Open Bookmark File...", FileDialog.LOAD);
		fd.setVisible(true);
		input_filename = fd.getFile();
		Settings.input_directory = fd.getDirectory();
		Settings.output_directory = fd.getDirectory();
		fd.dispose();
		if (input_filename == null)
			throw (new IOException("User cancelled."));

		buffer = getByteArrayInputStream(Settings.input_directory, input_filename);

		return(buffer);

	}// end of getByteArrayInputStream()


	//*************************************************************************
	//*		G E T  B Y T E  A R R A Y  I N P U T  S T R E A M
	//*************************************************************************
	ByteArrayInputStream getByteArrayInputStream(String directory, String filename) throws IOException
	{
		BufferedInputStream			is = null;
		ByteArrayInputStream		buffer = null;
		byte[]						input_array = null;
		int							err = 0;


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
	//*		S E T  B O O K M A R K
	//*************************************************************************
	void setBookmark (Frame parent, int focal_plane, long frame) throws Exception
	{
		int						i = 0, num_bookmarks = 0;
		BookmarkEntry			cur_bookmark = null;
		String					bookmark_name = null;


		if (entry_array == null)
			return;

		num_bookmarks = entry_array.size();

		//***** Find out if a bookmark already exists here.  If so, return
		for (i = 0; i < num_bookmarks; i++)
		{
			cur_bookmark = getEntry(i);
			if (frame == cur_bookmark.frame && focal_plane == cur_bookmark.focal_plane)
				return;
		}

		bookmark_name = getBookmarkName(parent, focal_plane, frame);
		if (bookmark_name == null)
			return;

		cur_bookmark = new BookmarkEntry(bookmark_name, focal_plane, frame);

		//***** Add the bookmark to the window's bookmark array
		if (cur_bookmark != null)
		{
			addEntry(cur_bookmark);
		}

		return;

	}// end of setBookmark()


	//*************************************************************************
	//*		C L E A R  B O O K M A R K
	//*************************************************************************
	void clearBookmark (int cur_focal_plane, long cur_frame) throws Exception
	{
		int							i = 0, num_bookmarks = 0;
		BookmarkEntry				cur_bookmark = null;
		String						bookmark_name = null;

		if (entry_array == null)
			return;

		num_bookmarks = entry_array.size();

		//***** Find out if the requested bookmark exists.  If so, clear it
		for (i = 0; i < num_bookmarks; i++)
		{
			cur_bookmark = getEntry(i);
			if (cur_frame == cur_bookmark.frame && cur_focal_plane == cur_bookmark.focal_plane)
			{
				deleteEntry(i);
			}
		}

		return;

	}// end of clearBookmark()


	//*************************************************************************
	//*		C L E A R  B O O K M A R K
	//*************************************************************************
	void clearBookmark (int index) throws Exception
	{
		int					i = 0, num_bookmarks = 0;


		if (entry_array == null)
			return;

		num_bookmarks = entry_array.size();
		if (index < 0 || index > num_bookmarks)
				return;

		deleteEntry(index);

 		return;

	}// end of clearBookmark()


	//*************************************************************************
	//*		G E T  B O O K M A R K  N A M E
	//*************************************************************************
	String	getBookmarkName(Frame parent, int focal_plane, long frame) throws Exception
	{
		String						name_string = null;
		String						default_string = null;
		String						prompt = new String("Bookmark Name?");
		StringWrapper				return_string = new StringWrapper();
		GStrDlog					gsd = null;

		//***** Build the default name string
		default_string = new String("Plane: ");
		default_string += Utils.numToString(focal_plane, 0);
		default_string += new String(" Frame: ");
		default_string += Utils.numToString(frame, 0);

		//***** Ask them for a bookmark name
		gsd = new GStrDlog(parent, true, prompt, default_string, return_string);
		gsd.setVisible(true);
		name_string = return_string.getString();


		return(name_string);

	}// end of getBookmarkName()


	//*************************************************************************
	//*		A D D  E N T R Y
	//*************************************************************************
	void	addEntry(BookmarkEntry be) throws Exception
	{

		if (be != null)
			this.entry_array.addElement(be);
		return;

	}// end of addBookmarkEntry()


	//*************************************************************************
	//*		G E T  E N T R Y
	//*************************************************************************
	BookmarkEntry	getEntry(int index) throws Exception
	{
		BookmarkEntry	be = null;

		if (entry_array == null || entry_array.size() < index)
			return(null);

		be = (BookmarkEntry)entry_array.elementAt(index);

		return(be);

	}// end of addBookmarkEntry()


	//*************************************************************************
	//*		D E L E T E  E N T R Y
	//*************************************************************************
	void	deleteEntry(int index) throws Exception
	{

		if (index < 0 || index > entry_array.size())
			return;

		entry_array.removeElementAt(index);

		return;

	}// end of deleteBookmarkEntry()

	//*************************************************************************
	//*		N U M  B O O K M A R K S
	//*************************************************************************
	int		numBookmarks() throws Exception
	{
		return(entry_array.size());
	}


	//*************************************************************************
	//*		R E S E T  A R R A Y
	//*************************************************************************
	public void  resetArray() throws Exception
	{
		entry_array.removeAllElements();
		return;
	}// end of resetArray()

}// end of class Bookmarks

import java.util.*;
import java.io.*;
import java.awt.event.*;

class HeaderArray implements Serializable
{
	final static int		NOT_FOUND = -1;


	Vector 	header_array = null;

	public HeaderArray()
	{
		header_array = new Vector(10,1);

	}// init


	//*******************************************************************
	//*		A D D  E N T R Y
	//*******************************************************************
	public void addEntry(HeaderEntry he)
	{
		if (he != null)
			header_array.addElement(he);

		return;

	}//end of addEntry()


	//***********************************************
	//*		G E T  A R R A Y
	//***********************************************
	public Vector getArray()
	{
		return(header_array);
	}


	//************************************************
	//*		R E S E T   A R R A Y
	//********************************************
	public void resetArray()
	{
		header_array.removeAllElements();
		return;
	}// end of resetArray()



	//***********************************************
	//*		G E T  E N T R Y
	//***********************************************
	public HeaderEntry  getEntry(int index) throws Exception
	{
		return((HeaderEntry)header_array.elementAt(index));

	}// end of getEntry()


	//***********************************************
	//*		R E M O V E  E N T R Y
	//***********************************************
	public void removeEntry(int index) throws Exception
	{

		header_array.removeElementAt(index);

		return;

	}// end of removeEntry()


	//***********************************************
	//*		G E T  N U M  E N T R I E S
	//***********************************************
	public int	getNumEntries() throws Exception
	{
		return(header_array.size());
	}// end of getNumEntries()


	//**************************************************************************************
	//*			G E T  O V E R L A Y  I N D E X
	//**************************************************************************************
	int		getOverlayIndex(int cur_focal_plane, long cur_frame) throws Exception
	{
		int				i = 0;
		HeaderEntry		he;


		for (i = 0; i < getNumEntries(); i++)
		{
			he = getEntry(i);
			if (cur_frame == he.frame && cur_focal_plane == he.focal_plane)
				return(i);
		}// for

		return(NOT_FOUND);

	}// end of getOverlayIndex()


	//**************************************************************************************
	//*			G E T  N E W  O V E R L A Y  I N D E X
	//*			Determine the index position for an overlay with the frame number given.
	//*			Replacing will be set to "true" if there's already an overlay at that frame.
	//*			Modified 2/19/03 for v1.71
	//*			Modified 4/1/03 for v1.72
	//**************************************************************************************
	int		getNewOverlayIndex(long frame, BooleanWrapper replacing) throws Exception
	{
		int						i = 0, num_entries = 0;
		int						index = NOT_FOUND;
		HeaderEntry				cur_entry = null;


		replacing.setValue(false);

		num_entries = getNumEntries();

		//***** If there are no headers, it's the first one
		if (num_entries == 0)
			return(0);

		//****** Scan through and find where the new one belongs
		for (i = 0; i < num_entries; i++)
		{
			cur_entry = getEntry(i);

			if (frame == cur_entry.frame)
			{
				index = i;
				replacing.setValue(true);
				break;
			}
			else if (cur_entry.frame > frame)
			{
				index = i;
				replacing.setValue(false);
				break;
			}
		}// for each header in the array

		//***** If it's not before any of the existing entries, it'll be last
		if (index == NOT_FOUND)
		{
			index = num_entries;
			return(index);
		}
		else
			return(index);


	}// end of getNewOverlayIndex()



	//*************************************************************************
	//*		W R I T E  N U M  H E A D E R S   T O  S T R E A M
	//*************************************************************************
	void	writeNumHeadersToStream(ObjectOutputStream stream) throws Exception
	{
		Integer		num_obj = new Integer(getNumEntries());

		stream.writeObject(num_obj);

		return;

	}// end of writeNumHeadersToStream()



	//********************************************************************************
	//*		W R I T E  H E A D E R S  T O  S T R E A M
	//*		Returns the total size of the written objects in bytes
	//********************************************************************************
	int	writeHeadersToStream(ByteArrayOutputStream os) throws Exception
	{
		int				i = 0, buffer_size = 0;
		HeaderEntry		he = null;
		ObjectOutputStream 	stream = null;

		stream = new ObjectOutputStream(os);

		writeOwnerStringToStream(stream);
		writeVersionStringToStream(stream);
		writeNumHeadersToStream(stream);

		for (i = 0; i < getNumEntries(); i++)
		{
			he = getEntry(i);
			stream.writeObject(he);
		}

		stream.flush();

		buffer_size = os.size();

		return(buffer_size);

	}// end of writeHeadersToStream()


	//*************************************************************************
	//*		W R I T E  O W N E R  S T R I N G  T O  S T R E A M
	//*************************************************************************
	void writeOwnerStringToStream(ObjectOutputStream stream) throws Exception
	{
		String		owner_string = "4DVJ";

		stream.writeObject(owner_string);

		return;

	}// end of writeOwnerStringToStream()


	//*************************************************************************
	//*		W R I T E  V E R S I O N  T O  S T R E A M
	//*************************************************************************
	void writeVersionStringToStream(ObjectOutputStream stream) throws Exception
	{
		String		version_string = new String(Settings.main_window.version);

		stream.writeObject(version_string);

		return;

	}// end of writeVersionStringToStream()


	//********************************************************************************
	//*		R E A D  H E A D E R S  F R O M  S T R E A M
	//********************************************************************************
	void	readHeadersFromStream(ByteArrayInputStream is) throws Exception
	{
		Integer			num_obj = null;
		int				i = 0, headers_to_read = 0;
		HeaderEntry		he = null;
		String			owner_string = null, version = null;
		ObjectInputStream	stream = null;

		stream = new ObjectInputStream(is);

		resetArray();
		owner_string = (String)stream.readObject();
		version = (String)stream.readObject();
		num_obj = (Integer)stream.readObject();
		headers_to_read = num_obj.intValue();

		for (i = 0; i < headers_to_read; i++)
		{
			he = (HeaderEntry)stream.readObject();
			addEntry(he);
		}

		return;

	}// end of readHeadersFromStream()


	//*************************************************************************
	//*		G E T  E N T R Y   S I Z E
	//*************************************************************************
	int		getEntrySize(HeaderEntry he) throws Exception
	{
		int					entry_size = 0;
		ByteArrayOutputStream	os = null;
		ObjectOutputStream		stream = null;

		os = new ByteArrayOutputStream();
		stream = new ObjectOutputStream(os);
		stream.writeObject(he);
		stream.flush();
		entry_size = os.size();
		os.close();

		return(entry_size);

	}// end of getEntrySize()


	//*************************************************************************
	//*		G E T  A R R A Y  S I Z E  I N  B Y T E S
	//*************************************************************************
	long		getArraySizeInBytes() throws Exception
	{
		long					array_size = 0;
		ByteArrayOutputStream	os = null;

		os = new ByteArrayOutputStream();
		array_size = writeHeadersToStream(os);
		os.close();

		return(array_size);

	}// end of getArraySizeInBytes()


	//*********************************************************************
	//*		A D D  N E W  H E A D E R  A R R A Y  E N T R Y
	//*		Finds where the entry should go in the header array
	//*		then puts it there and returns its index
	//*********************************************************************
	int	addNewHeaderArrayEntry(BooleanWrapper replacing, long overlay_size, HeaderEntry he) throws Exception
	{
		int						i = 0;
		int						index = NOT_FOUND;
		HeaderEntry				cur_entry = null;


		replacing.setValue(false);

		//***** If there are no headers, make a new one and add it
		if (getNumEntries() == 0)
		{
			index = 0;
			header_array.addElement(he);
			return(index);
		}

		//****** Scan through and find where the new one belongs
		for (i = 0; i < getNumEntries(); i++)
		{
			cur_entry = getEntry(i);
			if (he.frame <= cur_entry.frame)
			{
				index = i;
				break;
			}
		}// for each header in the array

		//***** If it's not to be inserted, add a new element at the end of the array
		if (index == NOT_FOUND)
		{
			index = getNumEntries();
			he.size_of_overlay = overlay_size;
			header_array.addElement(he);
			return(index);
		}// if the index was not found

		//***** If we're replacing an existing one, replace it, set the flag and return
		else if (he.frame == (getEntry(index)).frame)
		{
			replacing.setValue(true);
			return(index);
		}// if we're replacing one

		//***** If we're inserting a new entry, make it so
		else
		{
			he.size_of_overlay = overlay_size;
			header_array.insertElementAt(he, index);
			return(index);

		}// if we're inserting into the middle of the existing ones

	}// end of addNewHeaderArrayEntry()

}// end of class HeaderArray

import java.awt.*;
import java.io.*;


class NameUtils
{

	//***** FILENAME SYNTAX CONSTANTS
	final   static   int			STANDARD_NON_PADDED = 0;
	final   static   int			STANDARD_THREE_PADDED = 1;
	final   static   int			STANDARD_FOUR_PADDED = 2;
	final	static	 int			TIFF = 3;
	final   static   int			BIORAD_600 = 4;
	final   static   int			BIORAD_1024 = 5;

	final   static   int			NOT_FOUND = -1;


	//*************************************************************************
	//*		C R E A T E  M O V I E  N A M E
	//*		Modified 10/17/03 for v1.79
	//*************************************************************************
	static String createMovieName(String base_name, int focal_plane) throws Exception
	{
		String		movie_name = null;

		if (Settings.old_style_data_set)
			movie_name = createOldMovieName(base_name, focal_plane);
		else
			movie_name = createNewMovieName(base_name, focal_plane);

		return(movie_name);

	}// end of createMovieName()


	//*************************************************************************
	//*		C R E A T E  N E W  M O V I E  N A M E
	//*		"New" movies are named "Moviename1.mov", "Moviename2.mov", etc.
	//*		Added 10/17/03 for v1.79
	//*************************************************************************
	static String createNewMovieName(String base_name, int focal_plane) throws Exception
	{
		String			filename = null;
		String			fp_string = null;
		Integer			int_obj = null;


		int_obj = new Integer(focal_plane);
		fp_string = new String(int_obj.toString());

		filename = new String(base_name + fp_string + ".mov");

		return(filename);

	}// end of createNewMovieName()


	//*************************************************************************
	//*		C R E A T E  O L D  M O V I E  N A M E
	//*		"Old" movies were named "Moviename01", "Moviename02", etc.
	//*		Added 10/17/03 for v1.79
	//*************************************************************************
	static String createOldMovieName(String base_name, int focal_plane) throws Exception
	{
		String  	movie_name = null;
		String		fp_string = null;
		Integer		int_obj = null;


		if (focal_plane > 99)
			throw new Exception("Old format files only support < 100 planes!");

		int_obj = new Integer(focal_plane);

		if (focal_plane < 10)
		{
			fp_string = new String("0");
			fp_string += int_obj.toString();
		}
		else
		{
			fp_string = int_obj.toString();
		}

		movie_name = new String(base_name + fp_string);

		return(movie_name);

	}// end of createOldMovieName()



	//*************************************************************************
	//*		C R E A T E  T I F F  N A M E
	//*		Returns a name in the format "base_name1.TIF", "base_name2.TIF", ...
	//*		Added 4/10/03 for v2.14
	//*		Modified 10/17/03 for v1.79
	//*************************************************************************
	static String createTiffName(String base_name, int focal_plane)
	{
		String			filename = null;
		String			fp_string = null;
		Integer			int_obj = null;


		int_obj = new Integer(focal_plane);
		fp_string = new String(int_obj.toString());

		filename = new String(base_name + fp_string + ".TIF");

		return(filename);

	}// end of createTiffName()


	//*******************************************************************************
	//*		F I N D  B A S E  T I M E P O I N T  N A M E
	//*		Returns the base name from a given timepoint's filename
	//*		E.G:
	//*		STANDARD_NON_PADDED: "File.1" returns "File"
	//*		STANDARD_THREE_PADDED: "File.001" returns "File"
	//*		STANDARD_FOUR_PADDED: "File.0001" returns "File"
	//*		TIFF: "File1.TIF" returns "File"
	//*		BIORAD_600: "File1.PIC" returns "File"
	//*		BIORAD_1024: "Fil00101.PIC" returns "Fil"
	//*
	//*		Modified 11/22/02 for v2.13
	//*******************************************************************************
	static String findBaseTimepointName(int filename_syntax, String fn)
	{
		String  	base_filename = new String();
		int			i = 0;


		switch (filename_syntax)
		{
			case STANDARD_NON_PADDED:
				  for (i = 0; fn.charAt(i) != '.'; i++)
				  		base_filename += fn.charAt(i);
				  break;
			case STANDARD_THREE_PADDED:
				  for (i = 0; fn.charAt(i) != '.'; i++)
				  		base_filename += fn.charAt(i);
				  break;
			case STANDARD_FOUR_PADDED:
				  for (i = 0; fn.charAt(i) != '.'; i++)
				  		base_filename += fn.charAt(i);
				  break;
			case TIFF:
			case BIORAD_600:
				  for (i = 0; i < 8 && fn.charAt(i) != '.'; i++)
				  {
				  	if (fn.charAt(i) < '0' || fn.charAt(i) > '9')
				  		base_filename += fn.charAt(i);
				  }// for
				  break;
			case BIORAD_1024:
				 base_filename = get1024BaseName(fn);
				 break;
		}// switch

		return(base_filename);

	}// end of findBaseTimepointName()


	//*************************************************************************
	//*		G E T  1 0 2 4  B A S E  N A M E
	//*		Returns the base filename given a 1024 file
	//*************************************************************************
	static String  get1024BaseName(String fn)
	{
		int			num_chars = 0, i = 0;
		int			dot_position = NOT_FOUND;
		char			ch = 0;
		String 		base_name = new String();


		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the dot
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If there are less than six characters that proceed the dot
		//		it's really not a valid filename
		if (dot_position - 5 <= 0)
			return (null);

		//***** Get the characters between the beginning and the five sequential
		//		and channel numbers
		for (i = 0; i < dot_position-5; i++)
			base_name += fn.charAt(i);

		return(base_name);

	}// end of getBaseName()


	//*******************************************************************************
	//*		F I N D  E X T E N S I O N
	//*		Returns the extension of a given filename, if it can find one.
	//*		If not, returns null.
	//*
	//*		E.G:
	//*		STANDARD_NON_PADDED: "File.1" returns "1"
	//*		STANDARD_THREE_PADDED: "File.001" returns "001"
	//*		STANDARD_FOUR_PADDED: "File.0001" returns "0001"
	//*		BIORAD_600: "File1.PIC" returns "PIC" (case sensitive)
	//*		BIORAD_1024: "Fil00101.pic" returns "pic" (case sensitive)
	//*		TIFF: "File1.Tif" returns "Tif" (case sensitive)
	//*
	//*		Adde 11/22/02 for v2.13
	//*******************************************************************************
	static String findExtension(String fn)
	{
		String  	extension = null;
		int			i = 0, dot_position = NOT_FOUND;
		int			num_chars = 0;


		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the dot
		for (i = 0; i < num_chars; i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}// for each character that's not the dot

		if (dot_position == NOT_FOUND)
			return(null);
		else
		{
			for (i = dot_position + 1; i < num_chars; i++)
			{
				if (extension == null)
					extension = new String("");
				extension += fn.charAt(i);
			}

		}// if we have a "." in the filename

		return(extension);

	}// end of findExtension()


	//*******************************************************************************
	//*		F I N D  S E Q U E N T I A L  N U M B E R
	//*		Returns the sequential number from a given timepoint's filename
	//*		E.G:
	//*		STANDARD_NON_PADDED: "File.1" returns 1
	//*		STANDARD_THREE_PADDED: "File.001" returns 1
	//*		STANDARD_FOUR_PADDED: "File.0001" returns 1
	//*		TIFF: "File1.TIF" returns 1
	//*		BIORAD_600: "File1.PIC" returns 1
	//*		BIORAD_1024: "Fil00101.PIC" returns 1
	//*
	//*		Modified 11/22/02 for v2.13
	//*******************************************************************************
	static int findSequentialNumber(int filename_syntax, String fn)
	{
		String  	num_string = new String();
		int			i = 0, dot_position = 0, start_position = 0;
		int			num = NOT_FOUND;

		switch (filename_syntax)
		{
			case STANDARD_NON_PADDED:
			case STANDARD_THREE_PADDED:
			case STANDARD_FOUR_PADDED:
				  //***** Find the dot
				  for (i = 0; i < fn.length(); i++)
				  {
				  	if (fn.charAt(i) == '.')
				  	{
				  		dot_position = i;
				  		break;
				  	}
				  }// for

				  //***** Build the number string
			  	  for (i = dot_position + 1; i < fn.length(); i++)
				  {
				  	if (fn.charAt(i) >= '0' && fn.charAt(i) <= '9')
				  		num_string += fn.charAt(i);
				  }// for each character after the dot
				  num = intFromString(num_string);
				  break;
			case BIORAD_600:
			case TIFF:
				  //***** Find the dot
				  for (i = 0; i < fn.length(); i++)
				  {
				  	if (fn.charAt(i) == '.')
				  	{
				  		dot_position = i;
				  		break;
				  	}
				  }// for

				  //***** Find the first number
				  for (i = 0; i < fn.length(); i++)
				  {
				  	if (fn.charAt(i) >= '0' && fn.charAt(i) <= '9')
				  	{
				  		start_position = i;
				  		break;
				  	}
				  }// for

				  //***** Build the number string
				  for (i = start_position; i < fn.length() && i < dot_position; i++)
				  {
				  	if (fn.charAt(i) >= '0' && fn.charAt(i) <= '9')
				  		num_string += fn.charAt(i);
				  }
				  num = intFromString(num_string);
				  break;
			case BIORAD_1024:
				  //***** Find the first number
				  for (i = 0; i < fn.length(); i++)
				  {
				  	// Start 5 chars before the dot
				  	if (fn.charAt(i) == '.')
				  	{
				  		start_position = i-5;
				  		break;
				  	}
				  }// for

				  if (start_position < 0)
				  	return(-1);

				  //***** Build the number string
				  for (i = start_position; i < start_position + 3; i++)
				  {
				  	if (fn.charAt(i) >= '0' && fn.charAt(i) <= '9')
				  		num_string += fn.charAt(i);
				  }
				  num = intFromString(num_string);
				  break;
		}// switch

		return(num);

	}// end of findSequentialNumber()



	//*************************************************************************
	//*		I N T  F R O M  S T R I N G
	//*************************************************************************
	static  int  intFromString(String num_string)
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

	}// end of intFromString()


	//*************************************************************************
	//*			F I N D  F I L E N A M E  S Y N T A X
	//*			Tries to determine the filename syntax by stepping through
	//*			methods that test the filename.  Starts with the easiest to
	//*			differentiate, then progresses to harder tests.
	//*			Modified 11/22/02 for v2.13
	//*************************************************************************
	static  int  findFilenameSyntax(String fn)
	{
		int		syntax = NOT_FOUND;

		if (isBiorad1024Name(fn))
		{
			syntax = BIORAD_1024;
		}
		else if (isBiorad600Name(fn))
		{
			syntax = BIORAD_600;
		}
		else if (isFourPaddedName(fn))
		{
			syntax = STANDARD_FOUR_PADDED;
		}
		else if (isThreePaddedName(fn))
		{
			syntax = STANDARD_THREE_PADDED;
		}
		else if (isNonPaddedName(fn))
		{
			syntax = STANDARD_NON_PADDED;
		}
		else if (isTiffName(fn))
		{
			syntax = TIFF;
		}
		return(syntax);

	}// end of findFilenameSyntax()



	//*************************************************************************
	//*		I S  F I L E N A M E  F O R M A T
	//*		Returns TRUE if fn matches the syntax of filename type 'filetype'
	//*************************************************************************
	static	boolean  isFilenameFormat(String fn, int filetype)
	{
		switch (filetype)
		{
			case STANDARD_NON_PADDED:
				 return(isNonPaddedName(fn));
			case STANDARD_THREE_PADDED:
				 return(isThreePaddedName(fn));
			case STANDARD_FOUR_PADDED:
				 return(isFourPaddedName(fn));
			case TIFF:
				 return(isTiffName(fn));
			case BIORAD_600:
				 return(isBiorad600Name(fn));
			case BIORAD_1024:
				 return(isBiorad1024Name(fn));
		}//end of switch

		return(false);

	}// end of isFilenameFormat()


	//*************************************************************************
	//*		I S  T I F F  N A M E
	//*		Returns TRUE if fn is likely to be a Biorad MRC-600 filename
	//*		(e.g. "File1.tif" or "File1.TIF")
	//*		This test is not completely conclusive, it can really only exclude
	//*		Added 10/11/02 for v2.12
	//*************************************************************************
	static boolean  isTiffName(String fn)
	{
		int		num_chars = 0, i = 0;
		int		dot_position = NOT_FOUND;

		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the "."
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If we didn't find a '.' in the filename, it's not it
		if (dot_position == NOT_FOUND)
			return(false);

		//***** If we don't have three characters following the dot, it's not it
		if (num_chars-1 != dot_position + 3)
			return(false);

		//***** If the next three characters aren't "pic" or "PIC", it's not it
		if ((fn.charAt(dot_position + 1) != 't' && fn.charAt(dot_position + 2) != 'T' &&
			fn.charAt(dot_position + 3) != 'i') && (fn.charAt(dot_position + 1) != 'I' &&
			fn.charAt(dot_position + 2) != 'f' && fn.charAt(dot_position + 3) != 'F'))
			return(false);

		//***** Otherwise, it could be it
		return(true);

	}// end of isTiffName()


	//*************************************************************************
	//*		I S  B I O R A D  6 0 0  N A M E
	//*		Returns TRUE if fn is likely to be a Biorad MRC-600 filename
	//*		(e.g. "File1.PIC")
	//*		This test is not completely conclusive, it can really only exclude
	//*		Also, it doesn't mean much unless the isBiorad1024Name test failed
	//*		Modified 11/21/02 for v2.13
	//*************************************************************************
	static boolean  isBiorad600Name(String fn)
	{
		int		num_chars = 0, i = 0;
		int		dot_position = NOT_FOUND;

		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the "."
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If we didn't find a '.' in the filename, it's not it
		if (dot_position == NOT_FOUND)
			return(false);

		//***** If we don't have three characters following the dot, it's not it
		if (num_chars-1 != dot_position + 3)
			return(false);

		//***** If the next three characters aren't "pic" or "PIC", it's not
		if ((fn.charAt(dot_position + 1) != 'p' && fn.charAt(dot_position + 1) != 'P') ||
			(fn.charAt(dot_position + 2) != 'i' && fn.charAt(dot_position + 2) != 'I') ||
			(fn.charAt(dot_position + 3) != 'c' && fn.charAt(dot_position + 3) != 'C'))
			return(false);

		//***** Otherwise, it could be
		else
			return(true);

	}// end of isBiorad600Name()


	//*************************************************************************
	//*		I S  B I O R A D  1 0 2 4  N A M E
	//*		Returns TRUE if fn is likely to be a Biorad MRC-1024 filename
	//*		(e.g. "Fil00101.PIC")
	//*		This test is not completely conclusive, it can really only exclude
	//*************************************************************************
	static boolean  isBiorad1024Name(String fn)
	{
		int		num_chars = 0, i = 0;
		int		dot_position = NOT_FOUND;
		char	ch = 0;

		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the "."
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If we didn't find a '.' in the filename, it's not it
		if (dot_position == NOT_FOUND)
			return(false);

		//***** If we don't have three characters following the dot, it's not it
		if (num_chars-1 != dot_position + 3)
			return(false);

		//***** If the next four characters aren't "pic" or "PIC", it's not it
		if ((fn.charAt(dot_position + 1) != 'p' && fn.charAt(dot_position + 2) != 'i' &&
			fn.charAt(dot_position + 3) != 'c') && (fn.charAt(dot_position + 1) != 'P' &&
			fn.charAt(dot_position + 2) != 'I' && fn.charAt(dot_position + 3) != 'C'))
			return(false);

		//***** If the five characters preceding the '.' aren't numbers, it's not it
		for (i = 1; i <= 5 && dot_position - i >= 0; i++)
		{
			ch = fn.charAt(dot_position - i);
			if (ch < '0' || ch > '9')
				return(false);
		}

		//***** Otherwise, it could be it
		return(true);

	}// end of isBiorad1024Name()


	//*************************************************************************
	//*		I S  F O U R  P A D D E D  N A M E
	//*		Returns TRUE if fn is likely to be a four-padded filename
	//*		(e.g. "File.0001")
	//*		This test is not completely conclusive, it can really only exclude
	//*************************************************************************
	static boolean  isFourPaddedName(String fn)
	{
		int		num_chars = 0, i = 0;
		int		dot_position = NOT_FOUND;

		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the "."
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If we didn't find a '.' in the filename, it's not it
		if (dot_position == NOT_FOUND)
			return(false);

		//***** If we don't have four numbers following the dot, it's not it
		if (num_chars-1 != dot_position + 4)
			return(false);

		//***** If the next four characters aren't numbers, it's not it
		if (fn.charAt(dot_position + 1) < '0' || fn.charAt(dot_position + 1) > '9')
			return(false);
		if (fn.charAt(dot_position + 2) < '0' || fn.charAt(dot_position + 2) > '9')
			return(false);
		if (fn.charAt(dot_position + 3) < '0' || fn.charAt(dot_position + 3) > '9')
			return(false);
		if (fn.charAt(dot_position + 4) < '0' || fn.charAt(dot_position + 4) > '9')
			return(false);

		//***** Otherwise, it could be it
		return(true);

	}// end of isFourPaddedName()


	//*************************************************************************
	//*		I S  T H R E E  P A D D E D  N A M E
	//*		Returns TRUE if fn is likely to be a three-padded filename
	//*		(e.g. "File.001")
	//*		This test is not completely conclusive, it can really only exclude
	//*************************************************************************
	static boolean  isThreePaddedName(String fn)
	{
		int		num_chars = 0, i = 0;
		int		dot_position = NOT_FOUND;

		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the "."
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If we didn't find a '.' in the filename, it's not it
		if (dot_position == NOT_FOUND)
			return(false);

		//***** If we don't have three numbers following the dot, it's not it
		if (num_chars-1 != dot_position + 3)
			return(false);

		//***** If the next three characters aren't numbers, it's not it
		if (fn.charAt(dot_position + 1) < '0' || fn.charAt(dot_position + 1) > '9')
			return(false);
		if (fn.charAt(dot_position + 2) < '0' || fn.charAt(dot_position + 2) > '9')
			return(false);
		if (fn.charAt(dot_position + 3) < '0' || fn.charAt(dot_position + 3) > '9')
			return(false);

		//***** Otherwise, it could be it
		return(true);

	}// end of isThreePaddedName()


	//*************************************************************************
	//*		I S  N O N  P A D D E D  N A M E
	//*		Returns TRUE if fn is likely a non-padded filename (e.g. "File.1")
	//*		This test is not completely conclusive, it can really only exclude
	//*************************************************************************
	static boolean  isNonPaddedName(String fn)
	{
		int		num_chars = 0, i = 0;
		int		dot_position = NOT_FOUND;

		//**** Find number of characters
		num_chars = fn.length();

		//***** Find the "."
		for (i = 0; i < fn.length(); i++)
		{
			if (fn.charAt(i) == '.')
			{
				dot_position = i;
				break;
			}
		}

		//***** If we didn't find a '.' in the filename, it's not it
		if (dot_position == NOT_FOUND)
			return(false);

		//***** If we have a number following the dot that's zero it's not it
		if (num_chars-1 > dot_position && fn.charAt(dot_position + 1) == '0')
			return(false);

		//**** If it's a number and not a zero, it could be it
		if (num_chars-1 > dot_position && (fn.charAt(dot_position + 1) >= '1') &&
		    (fn.charAt(dot_position + 1) <= '9'))
			return(true);

		else
			return(false);
	}// end of isNonPaddedName()




}// end of class NameUtils

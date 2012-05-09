import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class IOUtils 
{
		

	//*************************************************************************
	//*		G E T  O U T P U T  S T R E A M
	//*************************************************************************
	static BufferedOutputStream getOutputStream (String output_directory, String output_filename) throws Exception
	{
		BufferedOutputStream	os = null;		
				
		try
		{
			os = new BufferedOutputStream(new FileOutputStream(output_directory + output_filename));
		}//try
		catch(IOException e)
		{
			throw(new Exception("Error getting the output stream."));
		}
		
		return(os);

	}// end of getOutputStream()


	//*************************************************************************
	//*		P U T  M E S S A G E
	//*************************************************************************
	static void putMessage(Frame fr, String message)
	{
		PutMsgDlog		md;
		
		md = new PutMsgDlog(fr, true, message);
		md.setVisible(true);
		return;
		
	}// end of putMessage



	//*********************************************************************
	//*		G E T  D I R E C T O R Y  F O L D E R S
	//*********************************************************************
	static Vector getDirectoryFolders(String cur_directory) 
	{
		File		cur_file = null;
		Vector		directories = new Vector(10,1);
		String[]	file_list = null;
		String		filename = null;
		String		path = null;
		int			i = 0;
		boolean		is_file = false, is_dir = false, exists = false;
		
		try 
		{
			cur_file = new File(cur_directory);
			if (cur_file.isDirectory())
			{
				directories.addElement(cur_file.getAbsolutePath());
				file_list = cur_file.list();
				
				if (file_list == null)
					return(null);
				
				try
				{	
					//***** Add only files that are directories
					do
					{
						if (cur_directory.endsWith(File.separator))
							cur_file = new File(cur_directory + file_list[i]);
						else
							cur_file = new File(cur_directory + File.separator + file_list[i]);

						if (cur_file != null)
						{
							exists = cur_file.exists();
							is_dir = cur_file.isDirectory();
							is_file = cur_file.isFile();
							
							filename = file_list[i];
							path = cur_file.getAbsolutePath();
							if (cur_file.isDirectory())
								directories.addElement(filename);
						}// if the file is valid
						i += 1;
					}while (true);
				}
				catch (ArrayIndexOutOfBoundsException ex){}
				
			}// if we have a valid directory as a parameter
			
			return(directories);
		}
		catch (Exception e) 
		{
			return(null);
		}// if an exception was generated
		
	}// end of getDirectoryFolders()
	
	
	//*********************************************************************
	//*		G E T  D I R E C T O R Y  P A T H S
	//*		Returns a list of folders (directories) in the current folder
	//*		with full path names.  Strings containing directory pathnames 
	//*		will be truncated to max_path_length characters.
	//*********************************************************************
	static Vector getDirectoryPaths(String cur_directory) 
	{
		File		cur_file = null;
		Vector		directories = new Vector(10,1);
		String[]	file_list = null;
		String		filename = null;
		String		path = null;
		int			i = 0;
		boolean		is_file = false, is_dir = false, exists = false;
		
		try 
		{
			if (cur_directory == null)
				return(null);
				
			cur_file = new File(cur_directory);
			if (cur_file.isDirectory())
			{
				directories.addElement(cur_file.getAbsolutePath());
				file_list = cur_file.list();
				
				if (file_list == null)
					return(null);
				
				try
				{	
					//***** Add only files that are directories
					do
					{
						if (cur_directory.endsWith(File.separator))
							cur_file = new File(cur_directory + file_list[i]);
						else
							cur_file = new File(cur_directory + File.separator + file_list[i]);

						if (cur_file != null)
						{
							exists = cur_file.exists();
							is_dir = cur_file.isDirectory();
							is_file = cur_file.isFile();
							
							filename = file_list[i];
							path = cur_file.getAbsolutePath();
							if (cur_file.isDirectory())
								directories.addElement(path);
						}// if the file is valid
						i += 1;
					}while (true);
				}
				catch (ArrayIndexOutOfBoundsException ex){}
				
			}// if we have a valid directory as a parameter
			
			return(directories);
		}
		catch (Exception e) 
		{
			return(null);
		}// if an exception was generated
		
	}// end of getDirectoryPaths()	


	//*****************************************************************
	//*		G E T  O U T P U T  D I R E C T O R Y
	//*		Last modified 5/9/02
	//*****************************************************************
	static void getOutputDirectory() throws Exception
	{
		FileDialog				fd = null;
		String					filename = null, directory = null;

		try
		{	
			//***** Get an output stream
			fd = new FileDialog(Settings.image_window, "Select a file in the desired output directory.", FileDialog.SAVE);
			fd.setVisible(true);
			filename = fd.getFile();
			directory = fd.getDirectory();	
								
			if (directory == null)
				throw (new IOException("Output directory specified was invalid."));	
	
			Settings.output_directory = directory;
				
		}
		catch (Exception e)
		{
			throw (new Exception("Invalid output directory selected.  " + e.getMessage()));
		}
		
		return;
	
	}// end of getOutputDirectory()
	
}// end of class IOUtils
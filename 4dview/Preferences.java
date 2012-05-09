import java.awt.*;
import java.io.*;
import java.util.*;

class Preferences
{
	final  static String		FILENAME = "4D Viewer Prefs";

	String				input_directory;
	String				output_directory;
	EditingSettings			es;


	public Preferences()
	{
		input_directory = null;
		output_directory = null;
		es = null;

		return;

	}// init


	public Preferences(EditingSettings edit_set)
	{
		this();
		this.es = edit_set;

		return;

	}// init


	public Preferences(String input_directory, String output_directory, EditingSettings edit_set)
	{
		this(edit_set);
		this.input_directory = input_directory;
		this.output_directory = output_directory;

		return;

	}// init


	//***********************************************************************
	//*		S A V E  P R E F E R E N C E S
	//***********************************************************************
	void	savePreferences()
	{
		Properties			props = null;
		FileOutputStream	os = null;
		FileDialog			fd = null;
		String			output_filename = FILENAME;
		String			property = null;

		try
		{
			//***** Build the properties file
			props = new Properties();
			saveToProperties(props);

			//***** Save the properties file to disk
			if (output_directory == null)
			{
				//***** Ask where to save the file
				fd = new FileDialog(Settings.control_window, "Save 4D Viewer Prefs...", FileDialog.SAVE);
				fd.setFile(FILENAME);
				fd.setVisible(true);
				output_filename = fd.getFile();
				output_directory = fd.getDirectory();
				fd.dispose();
			}

			props.put("4dvPref.output_directory", output_directory);
			os = new FileOutputStream(output_directory + output_filename);
			props.save(os, FILENAME);


		}// try
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return;

	}// end of savePreferences


	//***********************************************************************
	//*		L O A D  P R E F E R E N C E S
	//*		if auto_load is true, the method will look in the directory that
	//*		is in the input_directory string and load a file called
	//*		"4D ViewerPrefs".  If it's false, it will first try that method, then
	//*		if it fails, it will ask the user which prefs file to load.
	//***********************************************************************
	void	loadPreferences(boolean auto_load) throws Exception
	{
		Properties			props = null;
		FileInputStream		is = null;
		FileDialog			fd = null;
		String			input_filename = FILENAME;
		String			property = null, key = null;
		boolean			loaded = false;

		props = new Properties();

		//***** Try the default directory
		try
		{
			if (input_directory == null)
			{
				props = System.getProperties();
				key = "user.dir";
				input_directory = props.getProperty(key);
			}
			is = new FileInputStream(input_directory + input_filename);
			props.load(is);
			loaded = true;
		}// try
		catch (Exception e)
		{
		}// catch

		//***** Ask the user if it can't find the default file
		if (!loaded && !auto_load)
		{
			fd = new FileDialog(Settings.control_window, "Open 4D Viewer Prefs...", FileDialog.LOAD);
			fd.setVisible(true);
			input_filename = fd.getFile();
			input_directory = fd.getDirectory();
			fd.dispose();

			if (input_filename != null)
			{
				try
				{
					is = new FileInputStream(input_directory + input_filename);
					props.load(is);
					loaded = true;
				}// try
				catch (Exception e)
				{
				}
			}// if we have a filename
			else
				return;
		}// if we didn't load yet

		if (loaded)
			//***** Restore settings from the properties object
			loadFromProperties(props);

		return;

	}// end of loadPreferences


   public  void saveToProperties(Properties p)
   {
	p.put("4dvPref.output_directory", output_directory);
	p.put("4dvPref.input_directory", input_directory);

	p.put("4dvPref.tool_selected",  es.tool_selected + "");
	p.put("4dvPref.pen_size", es.pen_size + "");
	p.put("4dvPref.font_name", es.font_name + "");
	p.put("4dvPref.font_style",  es.font_style + "");
	p.put("4dvPref.font_size",   es.font_size + "");
	p.put("4dvPref.circle_color",   es.circle_color.toString());
	p.put("4dvPref.square_color",   es.square_color.toString());
	p.put("4dvPref.line_color",   es.line_color.toString());
	p.put("4dvPref.outline_color",   es.outline_color.toString());
	p.put("4dvPref.text_color",   es.text_color.toString());
	p.put("4dvPref.arrow_color",   es.arrow_color.toString());
 	p.put("4dvPref.arrow_orientation",   es.arrow_orientation + "");
	p.put("4dvPref.arrow_length",   es.arrow_length + "");
  	p.put("4dvPref.arrows_filled",   (new Boolean (es.arrows_filled)).toString());
  	p.put("4dvPref.circles_filled",   (new Boolean (es.circles_filled)).toString());
  	p.put("4dvPref.squares_filled",   (new Boolean (es.squares_filled)).toString());
	p.put("4dvPref.units_string",   es.units_string + "");
	p.put("4dvPref.multiplier",   (new Double(es.multiplier)).toString());

	return;

   }// end of saveToProperties()


   public  void loadFromProperties(Properties p) throws Exception
   {
   	input_directory = p.getProperty("4dvPref.input_directory");
    	output_directory = p.getProperty("4dvPref.output_directory");

	es.tool_selected =  Integer.parseInt(p.getProperty("4dvPref.tool_selected"));
	es.pen_size =  Integer.parseInt(p.getProperty("4dvPref.pen_size"));
	es.font_name =  p.getProperty("4dvPref.font_name");
	es.font_style =  Integer.parseInt(p.getProperty("4dvPref.font_style"));
	es.font_size =  Integer.parseInt(p.getProperty("4dvPref.font_size"));
	es.circle_color =  Color.getColor(p.getProperty("4dvPref.circle_color"));
	es.square_color =  Color.getColor(p.getProperty("4dvPref.square_color"));
	es.line_color =  Color.getColor(p.getProperty("4dvPref.line_color"));
	es.outline_color =  Color.getColor(p.getProperty("4dvPref.outline_color"));
	es.text_color = Color.getColor(p.getProperty("4dvPref.text_color"));
	es.arrow_color =  Color.getColor(p.getProperty("4dvPref.arrow_color"));
	es.arrow_orientation =  Integer.parseInt(p.getProperty("4dvPref.arrow_orientation"));
	es.arrow_length =  Integer.parseInt(p.getProperty("4dvPref.arrow_length"));
	es.arrows_filled =  Boolean.getBoolean(p.getProperty("4dvPref.arrows_filled"));
	es.circles_filled =  Boolean.getBoolean(p.getProperty("4dvPref.circles_filled"));
	es.squares_filled =  Boolean.getBoolean(p.getProperty("4dvPref.squares_filled"));
	es.units_string = p.getProperty("4dvPref.units_string");
	es.multiplier =  Double.valueOf(p.getProperty("4dvPref.multiplier")).doubleValue();

      	return;

   }// end of loadFromProperties()


}// end of class Preferences

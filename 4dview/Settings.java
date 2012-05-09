import java.awt.*;
import java.io.*;
import java.util.*;

class Settings
{
	final  static 	String		FILENAME = "4D Viewer Prefs";

	final	static		int			BRING_ALL_OBJECTS = 0;
	final	static		int			BRING_NEW_OBJECTS = 1;
	final	static		int			BRING_NO_OBJECTS = 2;


	//***** Global application variables
	static 	MainWindow				main_window = null;
	static		DataDisplayPanel		data_display_panel = null;
	static		MovieCanvas				movie_canvas = null;
	static		TiffCanvas				tiff_canvas = null;
	static		PositionPanel			movie_position_panel = null;
	static		MoviePalette			movie_palette = null;

	static 	EditingWindow			editing_window = null;
	static		EditingPanel			editing_panel = null;
	static		EditingCanvas			editing_canvas = null;

	static 	BookmarksWindow 		bookmarks_window = null;

	static		String				input_directory = null;
	static		String				output_directory = null;
	static		String				settings_directory = null;

	static		boolean				display_overlays = true;
	static		boolean				running_as_applet = false;
	static		boolean				data_set_open = false;
	static		boolean				old_style_data_set = false;

	static 		int					overlay_forward_pref = BRING_NEW_OBJECTS;
	static		boolean				prompt_for_saves = false;
	static		boolean				advance_to_next_frame = true;
	static		boolean				auto_object_comments = true;

	static		int					output_file_type = ImageWriter.GRAY_8_BIT_IMAGE;

	//***********************************************************************
	//*		S A V E  S E T T I N G S
	//***********************************************************************
	static void	saveSettings( )
	{
		Properties			props = null;
		FileOutputStream	os = null;
		FileDialog			fd = null;
		String				property = null, key = null;
		String				path_separator = null;

		try
		{
			props = System.getProperties();
			key = "path.separator";
			path_separator = props.getProperty(key);
			key = "user.home";
			settings_directory = props.getProperty(key);

			if (settings_directory == null)
				return;

			//***** Build the properties file
			props = new Properties();
			saveToProperties(props);

			os = new FileOutputStream(settings_directory + FILENAME);
			props.save(os, FILENAME);


		}// try
		catch (Exception e)
		{
			main_window.displayMessage("Unable to save settings.");
		}

		return;

	}// end of saveSettings


	//***********************************************************************
	//*		L O A D  S E T T I N G S
	//*		if auto_load is true, the method will look in the directory that
	//*		is in the input_directory string and load a file called
	//*		"4D ViewerPrefs".  If it's false, it will first try that method, then
	//*		if it fails, it will ask the user which prefs file to load.
	//***********************************************************************
	static EditingSettings	loadSettings(boolean auto_load)
	{
		Properties			props = null;
		File				f = null;
		FileInputStream		is = null;
		FileDialog			fd = null;
		String			property = null, key = null;
		String			input_filename = FILENAME;
		boolean			loaded = false;
		EditingSettings		es = new EditingSettings();
		String			test_dir = null;
		String			path_separator = null;

		props = new Properties();

		//***** Try the default directory
		try
		{
			if (settings_directory == null)
			{
				props = System.getProperties();
				key = "path.separator";
				path_separator = props.getProperty(key);
				key = "user.home";
				settings_directory = props.getProperty(key);
				test_dir = settings_directory;
			}

			f = new File(settings_directory + FILENAME);
			if (f.exists())
			{
				is = new FileInputStream(settings_directory + FILENAME);
				props.load(is);
				loaded = true;
			}
		}// try
		catch (Exception e)
		{
		}// catch

		//***** Ask the user if it can't find the default file
		if (!loaded && !auto_load)
		{
			fd = new FileDialog(Settings.main_window, "Open 4D Viewer Prefs...", FileDialog.LOAD);
			fd.setVisible(true);
			input_filename = fd.getFile();
			settings_directory = fd.getDirectory();
			fd.dispose();

			if (input_filename != null)
			{
				try
				{
					is = new FileInputStream(settings_directory + input_filename);
					props.load(is);
					loaded = true;
				}// try
				catch (Exception e)
				{
				}
			}// if we have a filename
			else
				return(es);
		}// if we didn't load yet

		try
		{
			if (loaded)
				//***** Restore settings from the properties object
				loadFromProperties(props);
		}
		catch (Exception e){}

		return(es);

	}// end of loadSettings


   //*************************************************************************
   //*		S A V E  T O  P R O P E R T I E S
   //*		Modified 1/22/03 for v1.70
   //*************************************************************************
   private  static void saveToProperties(Properties p) throws Exception
   {

 	  	//***** General Settings
		p.put("4dvPref.output_directory", output_directory);
		p.put("4dvPref.input_directory", input_directory);
		p.put("4dvPref.display_overlays", new Boolean(display_overlays).toString());
		p.put("4dvPref.overlay_forward_pref", overlay_forward_pref + "");
		p.put("4dvPref.prompt_for_saves", new Boolean(prompt_for_saves).toString());
		p.put("4dvPref.advance_to_next_frame", new Boolean(advance_to_next_frame).toString());
		p.put("4dvPref.auto_object_comments", new Boolean(auto_object_comments).toString());

		//***** Editing Settings
		p.put("4dvPref.tool_selected",  EditingSettings.tool_selected + "");
		p.put("4dvPref.pen_size", EditingSettings.pen_size + "");
		p.put("4dvPref.font_name", EditingSettings.font_name + "");
		p.put("4dvPref.font_style",  EditingSettings.font_style + "");
		p.put("4dvPref.font_size",   EditingSettings.font_size + "");
		p.put("4dvPref.circle_color",   EditingSettings.circle_color.toString());
		p.put("4dvPref.square_color",   EditingSettings.square_color.toString());
		p.put("4dvPref.line_color",   EditingSettings.line_color.toString());
		p.put("4dvPref.outline_color",   EditingSettings.outline_color.toString());
		p.put("4dvPref.text_color",   EditingSettings.text_color.toString());
		p.put("4dvPref.arrow_color",   EditingSettings.arrow_color.toString());
  		p.put("4dvPref.arrows_filled",   (new Boolean (EditingSettings.arrows_filled)).toString());
  		p.put("4dvPref.circles_filled",   (new Boolean (EditingSettings.circles_filled)).toString());
  		p.put("4dvPref.squares_filled",   (new Boolean (EditingSettings.squares_filled)).toString());
		p.put("4dvPref.units_string",   EditingSettings.units_string + "");
		p.put("4dvPref.multiplier",   (new Double(EditingSettings.multiplier)).toString());


		return;

   }// end of saveToProperties()


   //*************************************************************************
   //*		L O A D  F R O M  P R O P E R T I E S
   //*		Modified 1/22/03 for v1.70
   //*************************************************************************
   private  static void loadFromProperties(Properties p) throws Exception
   {

   		//***** General Settings
   		Settings.input_directory = p.getProperty("4dvPref.input_directory");
		Settings.output_directory = p.getProperty("4dvPref.output_directory");
		Settings.display_overlays = Boolean.getBoolean(p.getProperty("4dvPref.display_overlays"));
		Settings.overlay_forward_pref = Integer.parseInt(p.getProperty("4dvPref.overlay_forward_pref"));
		Settings.prompt_for_saves = Boolean.getBoolean(p.getProperty("4dvPref.prompt_for_saves"));
		Settings.advance_to_next_frame = Boolean.getBoolean(p.getProperty("4dvPref.advance_to_next_frame"));
		Settings.auto_object_comments = Boolean.getBoolean(p.getProperty("4dvPref.auto_object_comments"));


		//***** Editing Settings
		EditingSettings.tool_selected =  Integer.parseInt(p.getProperty("4dvPref.tool_selected"));
		EditingSettings.pen_size =  Integer.parseInt(p.getProperty("4dvPref.pen_size"));
		EditingSettings.font_name =  p.getProperty("4dvPref.font_name");
		EditingSettings.font_style =  Integer.parseInt(p.getProperty("4dvPref.font_style"));
		EditingSettings.font_size =  Integer.parseInt(p.getProperty("4dvPref.font_size"));
		EditingSettings.circle_color =  Color.getColor(p.getProperty("4dvPref.circle_color"));
		EditingSettings.square_color =  Color.getColor(p.getProperty("4dvPref.square_color"));
		EditingSettings.line_color =  Color.getColor(p.getProperty("4dvPref.line_color"));
		EditingSettings.outline_color =  Color.getColor(p.getProperty("4dvPref.outline_color"));
		EditingSettings.text_color = Color.getColor(p.getProperty("4dvPref.text_color"));
		EditingSettings.arrow_color =  Color.getColor(p.getProperty("4dvPref.arrow_color"));
		EditingSettings.arrows_filled =  Boolean.getBoolean(p.getProperty("4dvPref.arrows_filled"));
		EditingSettings.circles_filled =  Boolean.getBoolean(p.getProperty("4dvPref.circles_filled"));
		EditingSettings.squares_filled =  Boolean.getBoolean(p.getProperty("4dvPref.squares_filled"));
		EditingSettings.units_string = p.getProperty("4dvPref.units_string");
		EditingSettings.multiplier =  Double.valueOf(p.getProperty("4dvPref.multiplier")).doubleValue();

      	return;

   }// end of loadFromProperties()


}// end of class Settings

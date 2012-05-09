import java.net.*;


//************************************************************************
//*		C L A S S  S E T T I N G S
//*		Last modified 11/13/02 for v1.36
//************************************************************************
class Settings
{
	//****** Constants for application activity
	static	final	int				INACTIVE = 0;
	static	final	int				COMPRESSING = 1;
	static	final	int				DECOMPRESSING = 2;
	static	final	int				ESTIMATING_THRESHOLD = 3;
	
	//***** Window variables
	static	ImageWindow				image_window = null;
	static	ImageCanvas				image_canvas = null;
	static	StatusPanel				status_panel = null;
	
	
	//***** Conversion variables
	static	String					input_directory = null;
	static	String					output_directory = null;
	
	static	String					input_filename = null;
	static	String					output_filename = null;
	
	static	String					base_movie_name = null;
	static	String					base_timepoint_name = null;
	static	String					base_bit_filename = null;
	static	String					file_extension = null;
	static	String					input_directory_url_string = null;
	static	URL						input_url = null;
	
	static	String					prefs_directory = null;
	static	String					prefs_filename = null;
	
	static	int						num_movies = 0;
		
	//***** Stack variables
	static	int						first_stack_image = 1;// used in stack-to-movie
	static	int						last_stack_image = 1; // used in stack-to-movie

	static	double					desired_compression_ratio = 100;// used in estimating threshold
		
	//***** Processing variables
	static	int						first_timepoint = 1;
	static	int						last_timepoint = 1;
	static	int						current_timepoint = 1;
	static	int						total_timepoints = 1;

	static	int						first_plane = 1;// first plane to be processed
	static	int						last_plane = 1;// last plane to be processed
	static	int						current_plane = 1;
	static	int						total_planes = 1;// planes to be processed

	static	int						first_bitfile = 1;// first bitfile to be processed
	static	int						last_bitfile = 1;// last bitfile to be processed
	static	int						current_bitfile = 1; // bitfile currently being processed
	static	int						total_bitfiles = 1;// bitfiles to process
	
	static	OriginalCompSettings	ocs = null;
		
	static	int 					timepoints_in_each_block = 0;
	static	int						planes_in_each_stack = 0;
	static	int						image_width = -1;
	static	int						image_height = -1;
	static	int						input_file_type = ImageReader.GRAY_8_BIT_IMAGE;// 8-bit gray, RGB, etc.
	static	int						output_file_type = ImageReader.GRAY_8_BIT_IMAGE;
	static	int						input_file_format = ImageReader.TIFF_FILE;// TIFF, PICT, JPEG, PIC, etc
	static	int						output_file_format = ImageReader.TIFF_FILE;
	static	int						filename_syntax = NameUtils.UNKNOWN_FILE;
	static	long					start_time = 0;	
	static	boolean					abort = false;// flag to stop processing
	static	int						processing_action = INACTIVE;
	static	BioradRGBSettings		rgb_settings = null;
	static	boolean					test_mode = false;
	static	boolean					running_as_applet = false;
	static	boolean					use_URL; // working from the web?
	
	
	//**************************************************************************
	//*		R E S E T  S E T T I N G S
	//*		Changes those settings that need to be reset between runs
	//*		Last modified 11/13/02 for v1.36
	//**************************************************************************
	static void resetSettings()
	{	
	
		//input_directory = null;
		//output_directory = null;
		
		output_filename = null;
		input_filename = null; 
	
		//base_movie_name = new String("FocalPlane");
		//base_timepoint_name = new String("Timepoint");
		//base_bit_filename = new String("Bitfile");
		file_extension = null;
		input_url = null;

		//prefs_directory = null;
		//prefs_filename = null;
		num_movies = 0;
					
		first_timepoint = 1;
		last_timepoint = 10;
		current_timepoint = 1;
		total_timepoints = 1;
		
		first_plane = 1;
		last_plane = 1;
		current_plane = 1;
		total_planes = 1;
		
		first_bitfile = 1;
		last_bitfile = 1;
		current_bitfile = 1;
		total_bitfiles = 1;

		//desired_compression_ratio = 100;
		
		first_stack_image = 1;
		last_stack_image = 1;
		timepoints_in_each_block = 0;
		planes_in_each_stack = 0;
		image_width = -1;
		image_height = -1; 
		input_file_type = ImageReader.GRAY_8_BIT_IMAGE;
		output_file_type = ImageReader.GRAY_8_BIT_IMAGE;
		input_file_format = ImageReader.TIFF_FILE;
		output_file_format = ImageReader.TIFF_FILE;
		filename_syntax = NameUtils.UNKNOWN_FILE;
		start_time = 0;
		abort = false;
		processing_action = INACTIVE;
		rgb_settings = null;	
		//test_mode = false;
		//Settings.running_as_applet = ProcessorApplet.getInstance() != null;
		use_URL = false;

				
	}// end of resetSettings
	
}// end of class Settings

import java.awt.*;

class DataInfo
{
	//***** MOVIE PLAY MODE CONSTANTS
	final	static		int			DEFAULT_MODE = 0;
	final	static		int			LOOP_MODE = 1;
	final	static		int			BOUNCE_MODE = 2;

	//***** DATA SET TYPE CONSTANTS
	final	static		int			MOVIE_DATA_SET = 0;
	final	static		int			TIFF_DATA_SET = 1;


	static		int		data_width = -1;
	static		int		data_height = -1;
	static		int		num_focal_planes = 0;
	static		long	num_frames = 0;
	static		int		frame_duration = 0;
	static		int		cur_focal_plane = 0;
	static		long	cur_frame = 0;
	static		String	base_file_name = null;
	static		int		data_set_type = MOVIE_DATA_SET;
	static		int		data_display_mode = DEFAULT_MODE; // loop, bounce, etc.
	static		boolean	keep_playing = false;
	static		boolean	data_playing = false;
	static		boolean	data_playing_forward = false;

}// end of class DataInfo

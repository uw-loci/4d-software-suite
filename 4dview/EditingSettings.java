import java.awt.*;


//*********************************************************
//*		E D I T I N G  S E T T I N G S
//*		Keeps track of preferences and variables related to object editing
//*		Modified 7/25/02 for v1.59
//*********************************************************
class EditingSettings
{

	//***** Overlay status constants
	final	static		int			OVERLAY_IS_DIRTY = 0;
	final	static		int			OVERLAY_SAVED = 1;

	static		int				tool_selected = EditingPalette.SELECT_TOOL;
	static		int				pen_size = 2;
	static		String			font_name = new String("Geneva");
	static		int				font_style = Font.PLAIN;
	static		int				font_size = 20;
	static		Color				circle_color = Color.black;
	static		Color				square_color = Color.black;
	static		Color				line_color = Color.black;
	static		Color				outline_color = Color.black;
	static		Color				text_color =Color.black;
	static		Color				arrow_color = Color.black;
	static		boolean			arrows_filled = true;
	static		boolean			circles_filled = false;
	static		boolean			squares_filled = false;

	static		String			units_string = new String("pixels");
	static		double			multiplier = 1.0;
	static		int				overlay_status = OVERLAY_SAVED;

	static		HeaderArray		header_array = null; // stores info about what frames/planes have overlays

}// end of EditingSettings

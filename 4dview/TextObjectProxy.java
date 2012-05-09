
//************************************************************
//*		C L A S S  T E X T  O B J E C T  P R O X Y
//************************************************************

class TextObjectProxy extends AnnotationObjectProxy
{
	protected	String				text_string = null;// the text itself
	protected	int					text_counter = 0; // how many characters?
	protected	int					start_pt_x = 0, start_pt_y = 0; // where did the mouse click occur?
	protected	int					pen_pt_x = 0, pen_pt_y = 0; // where will the actual drawing begin?

	protected	String				font_name = null;
	protected	int					font_style = 0;
	protected	int					font_size = 0;
	protected	int					bottom_margin = 0; // how far to drop the bottom margin
										        // to include "g" and other descending chars

	public TextObjectProxy(TextObject obj)
	{
		super(obj);

		this.text_string = obj.text_string;
		this.text_counter = obj.text_counter;
		this.start_pt_x = obj.start_pt.x;
		this.start_pt_y = obj.start_pt.y;
		this.pen_pt_x = obj.pen_pt.x;
		this.pen_pt_y = obj.pen_pt.y;
		this.font_name = obj.font_name;
		this.font_style = obj.font_style;
		this.font_size = obj.font_size;
		this.bottom_margin = obj.bottom_margin;

		return;

	}// init


	//*********************************************************************
	//*		G E T  A N N O T A T I O N  O B J E C T
	//*********************************************************************
	public AnnotationObject	getAnnotationObject()
	{
		TextObject		obj = new TextObject();

		obj.finished = true;

		obj.structure_descriptor = this.structure_descriptor;
		obj.object_comments = this.object_comments;
		obj.object_rect.x = this.object_rect_x;
		obj.object_rect.y = this.object_rect_y;
		obj.object_rect.width = this.object_rect_width;
		obj.object_rect.height = this.object_rect_height;
		obj.object_type = this.object_type;
		obj.object_color = getColor(this.object_color);

		obj.text_string = this.text_string;
		obj.text_counter = this.text_counter;
		obj.start_pt.x = this.start_pt_x;
		obj.start_pt.y = this.start_pt_y;
		obj.pen_pt.x = this.pen_pt_x;
		obj.pen_pt.y = this.pen_pt_y;
		obj.font_name = this.font_name;
		obj.font_style = this.font_style;
		obj.font_size = this.font_size;
		obj.bottom_margin = this.bottom_margin;

		return(obj);

	}// end of getAnnotationObject()

}// end of TextObjectProxy

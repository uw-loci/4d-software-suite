//************************************************************
//*		C L A S S  L I N E  O B J E C T  P R O X Y
//************************************************************

class LineObjectProxy extends AnnotationObjectProxy
{
	protected	int			pen_width = 0;
	protected	int			start_pt_x =0, start_pt_y =0;
	protected	int			end_pt_x =0, end_pt_y =0;

	public LineObjectProxy(LineObject obj)
	{
		super(obj);

		this.pen_width = obj.pen_width;
		this.start_pt_x = obj.start_pt.x;
		this.start_pt_y = obj.start_pt.y;
		this.end_pt_x = obj.end_pt.x;
		this.end_pt_y = obj.end_pt.y;

		return;

	}// init


	//*********************************************************************
	//*		G E T  A N N O T A T I O N  O B J E C T
	//*********************************************************************
	public AnnotationObject	getAnnotationObject()
	{
		LineObject		obj = new LineObject();

		obj.finished = true;

		obj.structure_descriptor = this.structure_descriptor;
		obj.object_comments = this.object_comments;
		obj.object_rect.x = this.object_rect_x;
		obj.object_rect.y = this.object_rect_y;
		obj.object_rect.width = this.object_rect_width;
		obj.object_rect.height = this.object_rect_height;
		obj.object_type = this.object_type;
		obj.object_color = getColor(this.object_color);

		obj.pen_width = this.pen_width;
		obj.start_pt.x = this.start_pt_x;
		obj.start_pt.y = this.start_pt_y;
		obj.end_pt.x = this.end_pt_x;
		obj.end_pt.y = this.end_pt_y;

		return(obj);

	}// end of getAnnotationObject()

}// end of LineObjectProxy

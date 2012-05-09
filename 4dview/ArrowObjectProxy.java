//************************************************************
//*		C L A S S  A R R O W  O B J E C T  P R O X Y
//************************************************************

class ArrowObjectProxy extends AnnotationObjectProxy
{
	protected	int					start_pt_x = 0, start_pt_y = 0; // the tip of the arrow
	protected	int					end_pt_x = 0, end_pt_y = 0; // the end of the shaft of the arrow
	protected	boolean				filled = false; // is it an outline, or is it filled

	public ArrowObjectProxy(ArrowObject obj)
	{
		super(obj);

		this.start_pt_x = obj.start_pt.x;
		this.start_pt_y = obj.start_pt.y;
		this.end_pt_x = obj.end_pt.x;
		this.end_pt_y = obj.end_pt.y;
		this.filled = obj.filled;

		return;

	}// init


	//*********************************************************************
	//*		G E T  A N N O T A T I O N  O B J E C T
	//*********************************************************************
	public AnnotationObject	getAnnotationObject()
	{
		ArrowObject		obj = new ArrowObject();

		obj.finished = true;

		obj.structure_descriptor = this.structure_descriptor;
		obj.object_comments = this.object_comments;
		obj.object_rect.x = this.object_rect_x;
		obj.object_rect.y = this.object_rect_y;
		obj.object_rect.width = this.object_rect_width;
		obj.object_rect.height = this.object_rect_height;
		obj.object_type = this.object_type;
		obj.object_color = getColor(this.object_color);

		obj.start_pt.x = this.start_pt_x;
		obj.start_pt.y = this.start_pt_y;
		obj.end_pt.x = this.end_pt_x;
		obj.end_pt.y = this.end_pt_y;
		obj.filled = this.filled;

		return(obj);

	}// end of getAnnotationObject()

}// end of ArrowObjectProxy

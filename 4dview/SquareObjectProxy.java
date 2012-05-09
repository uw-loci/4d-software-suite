//************************************************************
//*		C L A S S  S Q U A R E  O B J E C T  P R O X Y
//************************************************************

class SquareObjectProxy extends AnnotationObjectProxy
{
	protected	int			pen_width = 0;
	protected	boolean		filled = false;

	public SquareObjectProxy(SquareObject obj)
	{
		super(obj);

		this.pen_width = obj.pen_width;
		this.filled = obj.filled;

		return;

	}// init

	//*********************************************************************
	//*		G E T  A N N O T A T I O N  O B J E C T
	//*********************************************************************
	public AnnotationObject	getAnnotationObject()
	{
		SquareObject		obj = new SquareObject();

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
		obj.filled = this.filled;

		return(obj);

	}// end of getAnnotationObject()

}// end of SquareObjectProxy

import java.util.*;
import java.awt.*;

//************************************************************
//*		C L A S S  O U T L I N E  O B J E C T  P R O X Y
//************************************************************

class OutlineObjectProxy extends AnnotationObjectProxy
{
	protected	int			pen_width = 0;
	protected	int			num_points = 0;
	protected	Vector		point_array = null;
			int			i = 0;

	public OutlineObjectProxy(OutlineObject obj)
	{
		super(obj);

		this.pen_width = obj.pen_width;
		this.num_points = obj.num_points;
		this.point_array = obj.point_array;

		/*
		this.point_array = new Vector(obj.point_array.size(), 1);

		for (i = 0; i < obj.point_array.size(); i++)
			this.point_array.addElement(new Point((Point)obj.point_array.elementAt(i)));
		*/

		return;

	}// init


	//*********************************************************************
	//*		G E T  A N N O T A T I O N  O B J E C T
	//*********************************************************************
	public AnnotationObject	getAnnotationObject()
	{
		OutlineObject		obj = new OutlineObject();

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
		obj.num_points = this.num_points;
		obj.point_array = this.point_array;

		return(obj);

	}// end of getAnnotationObject()

}// end of OutlineObjectProxy

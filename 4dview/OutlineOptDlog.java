import java.awt.*;

public class OutlineOptDlog extends ObjectOptDlog
{

	OutlineObject		ann_obj = null;

	//*********************************************************************
	//*		I N I T
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public OutlineOptDlog(Frame parent, OutlineObject oo, boolean modal)
	{
		super(parent, oo, modal);

		this.ann_obj = oo;

		setTitle("Outline Options");

		setupDlog();

		return;

	}// init


	//*********************************************************************
	//*		S E T U P  D L O G
	//*********************************************************************
	public void setupDlog()
	{
		super.setupDlog();
		return;

	}// end of setupDlog()


	//*********************************************************************
	//*		G E T  R E S U L T S
	//*********************************************************************
	public void getResults()
	{
		if (ann_obj == null)
		{
			//***** Get the pen size
			EditingSettings.pen_size = getInt(pen_size_field, EditingSettings.pen_size);
			EditingSettings.outline_color = getColor(colors_list.getSelectedIndex());
		}// if we're getting global settings
		else
		{
			ann_obj.pen_width = getInt(pen_size_field, EditingSettings.pen_size);
			ann_obj.object_color = getColor(colors_list.getSelectedIndex());
		}// if we're getting settings for a specific object

		return;

	}// end of getResults()


}// end of OutlineOptDlog class


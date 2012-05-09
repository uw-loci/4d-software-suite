import java.awt.*;

public class LineOptDlog extends ObjectOptDlog
{

	LineObject		ann_obj = null;

	//*********************************************************************
	//*		I N I T
	//*		Modified 10/23/02 for v1.68
	//*********************************************************************
	public LineOptDlog(Frame parent, LineObject lo, boolean modal)
	{
		super(parent, lo, modal);

		this.ann_obj = lo;

		setTitle("Line Options");

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
	//*		Modified 10/23/02 for v1.68
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


}// end of LineOptDlog class


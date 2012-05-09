import java.awt.*;

public class SquareOptDlog extends ObjectOptDlog
{

	CheckboxGroup	Group1 = null;
	Checkbox		filled_radio_button = null;
	Checkbox		outline_radio_button = null;

	SquareObject	ann_obj = null;

	//*********************************************************************
	//*		I N I T
	//*		Modified 10/22/02 for v1.68
	//*********************************************************************
	public SquareOptDlog(Frame parent, SquareObject so, boolean modal)
	{
		super(parent, so, modal);

		this.ann_obj = so;

		Group1 = new CheckboxGroup();

		filled_radio_button = new java.awt.Checkbox("Filled Squares", Group1,true);
		filled_radio_button.setSize(117,22);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(filled_radio_button);
		gbl.setConstraints(filled_radio_button, constraints);

		outline_radio_button = new java.awt.Checkbox("Outlined Squares", Group1, false);
		outline_radio_button.setSize(117,22);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(outline_radio_button);
		gbl.setConstraints(outline_radio_button, constraints);

		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		gbl.setConstraints(cancel_button, constraints);

		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		gbl.setConstraints(ok_button, constraints);

		setupSquareDlog();

		setTitle("Square Object Options");

		pack();

		return;

	}// init


	//*********************************************************************
	//*		S E T U P  S Q U A R E  D L O G
	//*		Modified 10/22/02 for v1.68
	//*********************************************************************
	public void setupSquareDlog()
	{
		if (ann_obj == null)
		{
			filled_radio_button.setState(EditingSettings.circles_filled);
			outline_radio_button.setState(!EditingSettings.circles_filled);
		}// if we're getting global settings
		else
		{
			filled_radio_button.setState(ann_obj.filled);
			outline_radio_button.setState(!ann_obj.filled);
		}// if we're getting settings for an individual object

		return;

	}// end of setupSquareDlog()


	//*********************************************************************
	//*		G E T  R E S U L T S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public void getResults()
	{
		if (this.ann_obj == null)
		{
			//***** Get the pen size
			EditingSettings.pen_size = getInt(pen_size_field, EditingSettings.pen_size);
			EditingSettings.square_color = getColor(colors_list.getSelectedIndex());
			EditingSettings.squares_filled = filled_radio_button.getState();
		}// if we're getting global options
		else
		{
			ann_obj.pen_width = getInt(pen_size_field, EditingSettings.pen_size);
			ann_obj.object_color = getColor(colors_list.getSelectedIndex());
			ann_obj.filled = filled_radio_button.getState();
		}// if we're getting options for a specific object
		return;

	}// end of getResults()


}// end of SquareOptDlog class


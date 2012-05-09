import java.awt.*;

public class CircleOptDlog extends ObjectOptDlog
{
	CheckboxGroup	Group1 = null;
	Checkbox		filled_radio_button = null;
	Checkbox		outline_radio_button = null;

	CircleObject	ann_obj = null;

	//*********************************************************************
	//*		I N I T
	//*		Modified 10/22/02 for v1.68
	//*********************************************************************
	public CircleOptDlog(Frame parent, CircleObject co, boolean modal)
	{
		super(parent, co, modal);

		this.ann_obj = co;

		Group1 = new CheckboxGroup();

		filled_radio_button = new java.awt.Checkbox("Filled Circles", Group1, true);
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

		outline_radio_button = new java.awt.Checkbox("Outlined Circles", Group1, false);
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

		setTitle("Circle Options");

		setupCircleDlog();

		pack();

		return;

	}// init


	//*********************************************************************
	//*		S E T U P  C I R C L E  D L O G
	//*		Modified 10/22/02 for v1.68
	//*********************************************************************
	public void setupCircleDlog()
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

	}// end of setupCircleDlog()


	//*********************************************************************
	//*		G E T  R E S U L T S
	//*		Modified 8/7/02
	//*********************************************************************
	public void getResults()
	{
		if (ann_obj == null)
		{
			EditingSettings.pen_size = getInt(pen_size_field, EditingSettings.pen_size);
			EditingSettings.circle_color = getColor(colors_list.getSelectedIndex());
			EditingSettings.circles_filled = filled_radio_button.getState();
		}// if we're getting global settings
		else
		{
			ann_obj.pen_width = getInt(pen_size_field, EditingSettings.pen_size);
			ann_obj.object_color = getColor(colors_list.getSelectedIndex());
			ann_obj.filled = filled_radio_button.getState();
		}// if we're getting settings for a specific object

		return;

	}// end of getResults()

}// end of CircleOptDlog class


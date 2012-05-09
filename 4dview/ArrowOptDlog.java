import java.awt.*;
import java.awt.event.*;

public class ArrowOptDlog extends Dialog
{
	Choice 					color_choice = null;
	Label 					arrow_color_label = null;
	Checkbox 				filled_radio_button = null;
	CheckboxGroup 			Group1 = null;
	Checkbox 				outline_radio_button = null;
	Button 					ok_button = null;
	Button 					cancel_button = null;
	Integer					num_obj = null;

	ArrowObject				ann_obj = null;

	GridBagLayout			gbl = null;
	GridBagConstraints		constraints = null;
	Insets					insets = null;


	//************************************************************************
	//*		I N I T
	//*		Modified 10/24/02 for v1.68
	//*		Modified 01/29/03 for v1.71
	//************************************************************************
	public ArrowOptDlog(Frame parent, ArrowObject ao, boolean modal)
	{
		super(parent, modal);

		this.ann_obj = ao;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setVisible(false);
		setSize(298,282);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(Color.black);
		setBackground(Color.white);
		setResizable(false);

		arrow_color_label = new java.awt.Label("Arrow Color:");
		arrow_color_label.setSize(101,22);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(arrow_color_label);
		gbl.setConstraints(arrow_color_label, constraints);

		color_choice = new java.awt.Choice();
		color_choice.setSize(122,21);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(color_choice);
		gbl.setConstraints(color_choice, constraints);

		Group1 = new CheckboxGroup();

		outline_radio_button = new java.awt.Checkbox("Outlined Arrow", Group1, !EditingSettings.arrows_filled);
		outline_radio_button.setSize(117,22);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(outline_radio_button);
		gbl.setConstraints(outline_radio_button, constraints);

		filled_radio_button = new java.awt.Checkbox("Filled Arrow", Group1, EditingSettings.arrows_filled);
		filled_radio_button.setSize(117,22);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(filled_radio_button);
		gbl.setConstraints(filled_radio_button, constraints);

		cancel_button = new java.awt.Button();
		cancel_button.setLabel("Cancel");
		cancel_button.setSize(75,26);
		constraints.gridx = 0;
		constraints.gridy = 4;
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
		add(cancel_button);
		gbl.setConstraints(cancel_button, constraints);

		ok_button = new java.awt.Button();
		ok_button.setLabel("   OK   ");
		ok_button.setSize(75,26);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 0;
		insets.right = 10;
		constraints.insets = insets;
		add(ok_button);
		gbl.setConstraints(ok_button, constraints);

		setTitle("Arrow Options");

		setupDlog();

		MyActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);
		cancel_button.addActionListener(action_listener);

		pack();

		return;

	}// end of init()


	//************************************************************************
	//*		S E T U P  D L O G
	//*		Modified 8/8/02 for v1.60
	//************************************************************************
	public void setupDlog()
	{
		color_choice.addItem("Black");
		color_choice.addItem("Blue");
		color_choice.addItem("Cyan");
		color_choice.addItem("Dark Gray");
		color_choice.addItem("Gray");
		color_choice.addItem("Green");
		color_choice.addItem("Light Gray");
		color_choice.addItem("Magenta");
		color_choice.addItem("Orange");
		color_choice.addItem("Pink");
		color_choice.addItem("Red");
		color_choice.addItem("White");
		color_choice.addItem("Yellow");
		selectColor(EditingSettings.arrow_color, color_choice);

		if (ann_obj == null)
		{
			filled_radio_button.setState(EditingSettings.arrows_filled);
			outline_radio_button.setState(!EditingSettings.arrows_filled);
		}// if we're getting global settings
		else
		{
			filled_radio_button.setState(ann_obj.filled);
			outline_radio_button.setState(!ann_obj.filled);
		}// if we're getting settings for a specific object

		return;

	}// end of setupDlog()


	//***************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 10/24/02 for v1.68
	//***************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//***************************************************************
	//*		S E T  V I S I B L E
	//***************************************************************
    public void setVisible(boolean b)
	{
		if(b)
		{
			Rectangle bounds = getParent().getBounds();
			Rectangle abounds = getBounds();

			setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
				 bounds.y + (bounds.height - abounds.height)/2);
		}

		super.setVisible(b);

		return;

	}// end of setVisible()


	//***************************************************************
	//*		M Y  A C T I O N  L I S T E N E R
	//***************************************************************
	class MyActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ok_button)
				okButton_ActionPerformed(event);
			else if (object == cancel_button)
				cancelButton_ActionPerformed(event);

			return;

		}// end of actionPerformed()

	}// end of class MyActionListener

	void okButton_ActionPerformed(ActionEvent event)
	{
		getResults();
		setVisible(false);
		dispose();
	}

	void cancelButton_ActionPerformed(ActionEvent event)
	{
		setVisible(false);
		dispose();
	}


	//*********************************************************************
	//*		S E L E C T  C O L O R
	//*********************************************************************
	public void	selectColor(Color cur_color, Choice color_choice)
	{
		//***** Select the current text color
		if (cur_color == Color.black)
		{
			color_choice.select(AnnotationObject.BLACK);
		}
		if (cur_color == Color.blue)
		{
			color_choice.select(AnnotationObject.BLUE);
		}
		if (cur_color == Color.cyan)
		{
			color_choice.select(AnnotationObject.CYAN);
		}
		if (cur_color == Color.darkGray)
		{
			color_choice.select(AnnotationObject.DARK_GRAY);
		}
		if (cur_color == Color.gray)
		{
			color_choice.select(AnnotationObject.GRAY);
		}
		if (cur_color == Color.green)
		{
			color_choice.select(AnnotationObject.GREEN);
		}
		if (cur_color == Color.lightGray)
		{
			color_choice.select(AnnotationObject.LIGHT_GRAY);
		}
		if (cur_color == Color.magenta)
		{
			color_choice.select(AnnotationObject.MAGENTA);
		}
		if (cur_color == Color.orange)
		{
			color_choice.select(AnnotationObject.ORANGE);
		}
		if (cur_color == Color.pink)
		{
			color_choice.select(AnnotationObject.PINK);
		}
		if (cur_color == Color.red)
		{
			color_choice.select(AnnotationObject.RED);
		}
		if (cur_color == Color.white)
		{
			color_choice.select(AnnotationObject.WHITE);
		}
		if (cur_color == Color.yellow)
		{
			color_choice.select(AnnotationObject.YELLOW);
		}

	}//end of selectColor()


	//*********************************************************************
	//*		G E T  C O L O R
	//*********************************************************************
	protected Color getColor(int color_code)
	{
		Color		return_color = null;

		switch (color_code)
		{
			case AnnotationObject.BLACK:
				 return_color = Color.black;
				 break;
			case AnnotationObject.BLUE:
				 return_color = Color.blue;
				 break;
			case AnnotationObject.CYAN:
				 return_color = Color.cyan;
				 break;
			case AnnotationObject.DARK_GRAY:
				 return_color = Color.darkGray;
				 break;
			case AnnotationObject.GRAY:
				 return_color = Color.gray;
				 break;
			case AnnotationObject.GREEN:
				 return_color = Color.green;
				 break;
			case AnnotationObject.LIGHT_GRAY:
				 return_color = Color.lightGray;
				 break;
			case AnnotationObject.MAGENTA:
				 return_color = Color.magenta;
				 break;
			case AnnotationObject.ORANGE:
				 return_color = Color.orange;
				 break;
			case AnnotationObject.PINK:
				 return_color = Color.pink;
				 break;
			case AnnotationObject.RED:
				 return_color = Color.red;
				 break;
			case AnnotationObject.WHITE:
				 return_color = Color.white;
				 break;
			case AnnotationObject.YELLOW:
				 return_color = Color.yellow;
				 break;
		}// end of switch

		return(return_color);

	}// end of getColor()


	//*********************************************************************
	//*		G E T  R E S U L T S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public void getResults()
	{
		if (ann_obj == null)
		{
			//***** Get the filled checkbox value
			EditingSettings.arrows_filled = filled_radio_button.getState();

			//***** Get the arrow_color
			EditingSettings.arrow_color = getColor(color_choice.getSelectedIndex());
		}// if we're getting preferences for the entire program
		else
		{
			ann_obj.filled = filled_radio_button.getState();
			ann_obj.object_color = getColor(color_choice.getSelectedIndex());
		}// if we're getting preferences for a specific object

		return;

	}// end of getResults()


	//*********************************************************************
	//*		G E T  I N T
	//*********************************************************************
	int getInt(TextField field, int default_value)
	{
		Double d;

		try
		{
			d = new Double(field.getText());
		}
		catch (NumberFormatException e)
		{
			field.setText(""+default_value);// if the number's invalid, use default
			d = null;
		}// if an exception was generated

		if (d != null)
			return((int)d.doubleValue());
		else
			return(default_value);

	}// end of getInt()


}// end of class ArrowOptDlog


import java.awt.*;

public class SetScaleDlog extends Dialog
{

	Button 			cancel_button = null;
	Button 			ok_button = null;
	TextField 		other_text_field = null;
	Choice	 		units_choice = null;
	TextField 		num_text_field = null;
	Label 			one_pixel_label = null;
	Label			num_label = null;


	//********************************************************
	//*		I N I T
	//*		Modified 10/15/02 for v1.66
	//*		Modified 01/29/03 for v1.71
	//********************************************************
	public SetScaleDlog(Frame parent, boolean modal)
	{

		super(parent, modal);

		GridBagLayout		gbl = null;
		GridBagConstraints	constraints = null;
		Insets				insets = null;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(gbl);
		insets = new Insets(0,0,0,0);

		setVisible(false);
		setSize(414,281);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setResizable(false);

		one_pixel_label = new java.awt.Label("One pixel equals:");
		one_pixel_label.setSize(116,23);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
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
		add(one_pixel_label);
		gbl.setConstraints(one_pixel_label, constraints);

		num_text_field = new java.awt.TextField();
		num_text_field.setSize(60,30);
		num_text_field.setColumns(8);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 5;
		insets.right = 0;
		constraints.insets = insets;
		add(num_text_field);
		gbl.setConstraints(num_text_field, constraints);

		num_label = new java.awt.Label("(number)");
		num_label.setSize(63,25);
		constraints.gridx = 2;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 5;
		insets.right = 10;
		constraints.insets = insets;
		add(num_label);
		gbl.setConstraints(num_label, constraints);

		units_choice = new Choice();
		units_choice.setSize(85,25);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
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
		add(units_choice);
		gbl.setConstraints(units_choice, constraints);

		other_text_field = new java.awt.TextField();
		other_text_field.setSize(151,31);
		other_text_field.setColumns(12);
		other_text_field.setEnabled(false);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 3;
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
		add(other_text_field);
		gbl.setConstraints(other_text_field, constraints);

		cancel_button = new java.awt.Button();
		cancel_button.setLabel("Cancel");
		cancel_button.setSize(79,27);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(cancel_button);
		gbl.setConstraints(cancel_button, constraints);

		ok_button = new java.awt.Button();
		ok_button.setLabel("   OK   ");
		ok_button.setSize(79,27);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(ok_button);
		gbl.setConstraints(ok_button, constraints);


		setTitle("Set Scale");

		setUpDlog();

		MyActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);
		cancel_button.addActionListener(action_listener);

		MyItemListener item_listener = new MyItemListener();
		units_choice.addItemListener(item_listener);

		pack();

		return;

	}// end of init()


	//*************************************************************
	//*		S E T U P  D L O G
	//*		Modified 10/15/02 for v1.66
	//*************************************************************
	public void setUpDlog()
	{
		//***** Set up the choice
		units_choice.addItem("Nanometers");
		units_choice.addItem("Microns");
		units_choice.addItem("Millimeters");
		units_choice.addItem("Centimeters");
		units_choice.addItem("Meters");
		units_choice.addItem("Kilometers");
		units_choice.addItem("Other...");


		//***** Set the radio buttons
		if (EditingSettings.units_string.equals("nm"))
			units_choice.select("Nanometers");
		else if (EditingSettings.units_string.equals("?m"))
			units_choice.select("Microns");
		else if (EditingSettings.units_string.equals("mm"))
			units_choice.select("Millimeters");
		else if (EditingSettings.units_string.equals("cm"))
			units_choice.select("Centimeters");
		else if (EditingSettings.units_string.equals("m"))
			units_choice.select("Meters");
		else if (EditingSettings.units_string.equals("km"))
			units_choice.select("Kilometers");
		else
		{
			units_choice.select("Other...");
			other_text_field.setText(EditingSettings.units_string);
			other_text_field.setEnabled(true);
		}

		//***** Set the num text field
		num_text_field.setText(" " + EditingSettings.multiplier);

		return;

	}// end of setupDlog()


	//*************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 10/15/02 for v1.66
	//*************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//*************************************************************
	//*		S E T  V I S I B L E
	//*************************************************************
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


	class MyActionListener implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ok_button)
				okButton_ActionPerformed(event);
			else if (object == cancel_button)
				cancelButton_ActionPerformed(event);

			return;

		}// end of actionPerformed()

	}// end of class MyActionListener


	void okButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
			getResults();
			setVisible(false);
	}

	void cancelButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		setVisible(false);
	}


	class MyItemListener implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if (object == units_choice)
				unitsChoice_ItemEvent(event);

			return;

		}// end of itemStateChanged()

	}// end of class MyItemListener


	//********************************************************************
	//*		U N I T S  C H O I C E  I T E M  E V E N T
	//*		Added 10/15/02 for v1.66
	//********************************************************************
	void unitsChoice_ItemEvent(java.awt.event.ItemEvent event)
	{

		//***** Enable the other_text_field if the selected item of the choice is "Other..."
		other_text_field.setEnabled(units_choice.getSelectedItem() == "Other...");

		return;

	}// end of unitsChoice_ItemEvent()


	//********************************************************************
	//*		G E T  R E S U L T S
	//*		Modified 10/15/02 for v1.66
	//********************************************************************
	public void getResults()
	{
		Double		mult = null;

		try
		{
			//***** Get the number to be used as a multiplier
			mult = getDouble(num_text_field, EditingSettings.multiplier);
			EditingSettings.multiplier = mult.doubleValue();


			//***** Get the units suffix
			if (units_choice.getSelectedItem() == "Kilometers")
				EditingSettings.units_string = new String("km");
			else if (units_choice.getSelectedItem() == "Meters")
				EditingSettings.units_string = new String("m");
			else if (units_choice.getSelectedItem() == "Centimeters")
				EditingSettings.units_string = new String("cm");
			else if (units_choice.getSelectedItem() == "Millimeters")
				EditingSettings.units_string = new String("mm");
			else if (units_choice.getSelectedItem() == "Nanometers")
				EditingSettings.units_string = new String("nm");
			else if (units_choice.getSelectedItem() == "Microns")
				EditingSettings.units_string = new String("?m");
			else if (units_choice.getSelectedItem() == "Other...")
			{
				EditingSettings.units_string = other_text_field.getText();
			}
		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception in SetScaleDlog.getResults()!");
		}

		return;

	}// end of getResults()


	//*********************************************************************
	//*		G E T  D O U B L E
	//*********************************************************************
	Double getDouble(TextField field, double default_value)
	{
		Double d = null;

		try
		{
			d = new Double(field.getText());
		}
		catch (NumberFormatException e)
		{
			d = new Double(default_value);
		}// if an exception was generated

		return(d);

	}// end of getDouble()

}


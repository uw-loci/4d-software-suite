import java.awt.*;
import java.awt.event.*;


public class ObjectOptDlog extends Dialog
{
	TextField 					pen_size_field = null;
	Label 						pen_size_label = null;
	Label 						color_label = null;
	Choice 						colors_list = null;

	Button 						ok_button = null;
	Button 						cancel_button = null;

	AnnotationObject			ann_obj = null; // used when setting preferences for a specific object

	GridBagLayout				gbl = null;
	GridBagConstraints			constraints = null;
	Insets						insets = null;


	//***************************************************************************
	//*		I N I T
	//*		The parameter "ao" will be null when setting general preferences.
	//*		Modified 10/22/02 for v1.68
	//*		Modified 01/29/03 for v1.71
	//***************************************************************************
	public ObjectOptDlog(Frame parent, AnnotationObject ao, boolean modal)
	{
		super(parent, modal);

		this.ann_obj = ao;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setSize(193,153);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setResizable(false);

		color_label = new java.awt.Label("Object Color:");
		color_label.setSize(77,22);
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
		add(color_label);
		gbl.setConstraints(color_label, constraints);

		colors_list = new java.awt.Choice();
		colors_list.setSize(120,22);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(colors_list);
		gbl.setConstraints(colors_list, constraints);

		pen_size_label = new java.awt.Label("Pen Size:");
		pen_size_label.setSize(71,22);
		constraints.gridx = 0;
		constraints.gridy = 2;
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
		add(pen_size_label);
		gbl.setConstraints(pen_size_label, constraints);

		pen_size_field = new java.awt.TextField();
		pen_size_field.setSize(32,22);
		pen_size_field.setColumns(3);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 5;
		insets.right = 10;
		constraints.insets = insets;
		add(pen_size_field);
		gbl.setConstraints(pen_size_field, constraints);

		cancel_button = new java.awt.Button();
		cancel_button.setLabel("Cancel");
		cancel_button.setSize(60,23);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.EAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(cancel_button);
		gbl.setConstraints(cancel_button, constraints);

		ok_button = new java.awt.Button();
		ok_button.setLabel("   OK   ");
		ok_button.setSize(60,23);
		constraints.gridx = 1;
		constraints.gridy = 3;
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
		add(ok_button);
		gbl.setConstraints(ok_button, constraints);

		setTitle("Object Options");

		MyActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);
		cancel_button.addActionListener(action_listener);

		MyKeyAdapter key_adapter = new MyKeyAdapter();
		this.addKeyListener(key_adapter);

		setupDlog();

		return;

	}// end of init()


	//**********************************************************
	//*		A D D  N O T I F Y
	//**********************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//**********************************************************
	//*		S E T  V I S I B L E
	//**********************************************************
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


	//**********************************************************
	//*		M Y  A C T I O N  L I S T E N E R
	//**********************************************************
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


	//**********************************************************
	//*		M Y  K E Y  A D A P T E R
	//**********************************************************
	class MyKeyAdapter extends java.awt.event.KeyAdapter
	{
		public void keyPressed(java.awt.event.KeyEvent event)
		{
			Object object = event.getSource();
			if (object == ObjectOptDlog.this)
				doKeyPressed(event);

			return;

		}// end of keyPressed()

	}// end of class MyKeyAdapter


	//*********************************************************************
	//*		S E T U P  D L O G
	//*		Modified 8/8/02 for v1.60
	//*********************************************************************
	public void setupDlog()
	{

		//***** Setup the colors list
		colors_list.addItem("Black");
		colors_list.addItem("Blue");
		colors_list.addItem("Cyan");
		colors_list.addItem("Dark Gray");
		colors_list.addItem("Gray");
		colors_list.addItem("Green");
		colors_list.addItem("Light Gray");
		colors_list.addItem("Magenta");
		colors_list.addItem("Orange");
		colors_list.addItem("Pink");
		colors_list.addItem("Red");
		colors_list.addItem("White");
		colors_list.addItem("Yellow");

		if (ann_obj == null)
		{
			pen_size_field.setText("" + EditingSettings.pen_size);
			selectColor(Color.black, colors_list);
		}// if we're getting global settings
		else
		{
			pen_size_field.setText("" + EditingSettings.pen_size);
			selectColor(ann_obj.object_color, colors_list);
		}// if we're gettings settings for an individual object

		return;
	}// end of setupDlog()



	//*********************************************************************
	//*		G E T  R E S U L T S
	//*********************************************************************
	public void getResults()
	{
		return;

	}// end of getResults()


	//*********************************************************************
	//*		G E T  I N T
	//*********************************************************************
	int getInt(TextField field, int default_value)
	{
		Double d = null;

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
	//*		D O  K E Y  P R E S S E D
	//*********************************************************************
	void doKeyPressed(java.awt.event.KeyEvent event)
	{
		char	key_ch;

		key_ch = event.getKeyChar();

		if (key_ch == event.VK_ENTER || key_ch == TextObject.RETURN_CHAR ||
			key_ch == TextObject.LINE_FEED_CHAR)
		{
			getResults();
			setVisible(false);
			dispose();

		}// if it's the CR or LF key

		return;

	}// end of doKeyPressed()


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

}// end of ObjectOptDialog class


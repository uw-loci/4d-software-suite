
import java.awt.*;
import java.awt.event.*;

public class TextOptDlog extends Dialog
{
	Choice 					colors_list = null;
	Choice 					font_list = null;
	TextField 				font_size_field = null;
	Label 					text_color_label = null;
	Label 					text_font_label = null;
	Label 					font_size_label = null;
	Button 					ok_button = null;
	Button 					cancel_button = null;

	TextObject				ann_obj = null;

	GridBagLayout			gbl = null;
	GridBagConstraints		constraints = null;
	Insets					insets = null;

	//***************************************************************************
	//*		I N I T
	//*		The parameter "to" will be null when setting general preferences.
	//*		Modified 10/23/02 for v1.68
	//*		Modified 01/29/03 for v1.71
	//***************************************************************************
	public TextOptDlog(Frame parent, TextObject to, boolean modal)
	{
		super(parent, modal);

		this.ann_obj = to;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setSize(234,192);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(Color.black);
		setBackground(Color.white);
		setResizable(false);

		font_size_label = new Label("Font Size:");
		font_size_label.setSize(69,22);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
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
		add(font_size_label);
		gbl.setConstraints(font_size_label, constraints);

		font_size_field = new TextField();
		font_size_field.setSize(33,22);
		font_size_field.setColumns(4);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
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
		add(font_size_field);
		gbl.setConstraints(font_size_field, constraints);

		text_font_label = new Label("Text Font:");
		text_font_label.setSize(69,22);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(text_font_label);
		gbl.setConstraints(text_font_label, constraints);

		font_list = this.makeFontList();
		font_list.setSize(117,25);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
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
		add(font_list);
		gbl.setConstraints(font_list, constraints);

		text_color_label = new Label("Text Color:");
		text_color_label.setSize(72,22);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(text_color_label);
		gbl.setConstraints(text_color_label, constraints);

		colors_list = new Choice();
		colors_list.setSize(117,25);
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
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(colors_list);
		gbl.setConstraints(colors_list, constraints);

		cancel_button = new Button();
		cancel_button.setLabel("Cancel");
		cancel_button.setSize(62,25);
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
		insets.right = 0;
		constraints.insets = insets;
		add(cancel_button);
		gbl.setConstraints(cancel_button, constraints);

		ok_button = new Button();
		ok_button.setLabel("   OK   ");
		ok_button.setSize(62,25);
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

		setTitle("Text Options");

		MyActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);
		cancel_button.addActionListener(action_listener);

		setupDlog();

		pack();

		return;

	}// end of init()


	//*********************************************************************
	//*		S E T U P  D L O G
	//*		Modified 8/8/02 for v1.60
	//*********************************************************************
	public void setupDlog()
	{
		Integer				num_obj = null;

		//***** Add the colors to the colors liset
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
			selectColor(EditingSettings.text_color, colors_list);
			num_obj = new Integer(EditingSettings.font_size);
			font_size_field.setText(num_obj.toString());
		}// if we're getting global settings
		else
		{
			selectColor(ann_obj.object_color, colors_list);
			num_obj = new Integer(ann_obj.font_size);
			font_size_field.setText(num_obj.toString());
		}// if we're getting settings for an individual object

		return;

	}// end of setupDlog()


	//*********************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 8/8/02 for v1.60
	//*********************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


 	//*********************************************************************
	//*		S E T  V I S I B L E
	//*********************************************************************
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


 	//*********************************************************************
	//*		M Y  W I N D O W  A D A P T E R
	//*********************************************************************
	class MyWindowAdapter extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			Object object = event.getSource();
			if (object == TextOptDlog.this)
				doWindowClosing(event);

			return;

		}// end of windowClosing()

	}// end of class MyWindowAdapter


 	//*********************************************************************
	//*		D O  W I N D O W  C L O S I N G
	//*********************************************************************
	void doWindowClosing(WindowEvent event)
	{
		setVisible(false);

		return;

	}// end of doWindowClosing()


 	//*********************************************************************
	//*		M A K E  F O N T  L I S T
	//*		Modified 10/23/02 for v1.68
	//*********************************************************************
	Choice	makeFontList()
	{
		Choice			font_list = null;
     	String[]		font_items = null;
		int				i = 0;

		font_list = new Choice();
		if (font_list != null)
		{

			font_items = Toolkit.getDefaultToolkit().getFontList();

			for (i = 0; i < font_items.length; i++)
			{
				font_list.addItem(font_items[i]);

				//***** If the item is the current font, select it
				if (EditingSettings.font_name.equals(font_list.getItem(i)))
				{
					font_list.select(i); // select it
				}// if the item is the current font

			}// for each font item

		}// if we have a valid list

		return(font_list);

	}// end of makeFontList()


 	//*********************************************************************
	//*		M Y  A C T I O N  L I S T E N E R
	//*********************************************************************
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

	}// end of class MyActionListener()


	//*********************************************************************
	//*		G E T  R E S U L T S
	//*		Modified 8/8/02 for v1.60
	//*********************************************************************
	public void getResults()
	{
		if (ann_obj ==  null)
		{
			//***** Get the font size
			EditingSettings.font_size = getInt(font_size_field, EditingSettings.font_size);

			//***** Get the font name
			if (font_list.getSelectedItem() != null)
			{
				EditingSettings.font_name = new String(font_list.getSelectedItem());
			}

			//***** Get the text color
			EditingSettings.text_color = getColor(colors_list.getSelectedIndex());
		}// if we're getting global settings
		else
		{
			//***** Get the font size
			ann_obj.font_size = getInt(font_size_field, ann_obj.font_size);

			//***** Get the font name
			if (font_list.getSelectedItem() != null)
			{
				ann_obj.font_name = new String(font_list.getSelectedItem());
			}

			//***** Get the text color
			ann_obj.object_color = getColor(colors_list.getSelectedIndex());
		}// if we're getting settings for an individual object

		return;

	}// end of getResults()


	//*********************************************************************
	//*		O K  B U T T O N
	//*********************************************************************
	void okButton_ActionPerformed(ActionEvent event)
	{
			getResults();
			setVisible(false);
			dispose();

			return;

	}// end of okButton_ActionPerformed()


	//*********************************************************************
	//*		C A N C E L  B U T T O N
	//*********************************************************************
	void cancelButton_ActionPerformed(ActionEvent event)
	{
		setVisible(false);
		dispose();

		return;

	}// end of cancelButton_ActionPerformed()


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

		return;

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


}// end of class TextOpDlog


import java.awt.*;
import java.awt.event.*;

public class GStrDlog extends Dialog
{

	Label 				prompt_label = null;
	TextField 			return_string_field = null;
	Button 				ok_button = null;
	Button 				cancel_button = null;

	StringWrapper		return_string = null;
	String				default_string = null, prompt_string = null;

	GridBagLayout		gb_layout = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;


	//******************************************************************
	//*		I N I T
	//*		Added 10/14/02 for v1.66
	//*		Modified 01/29/03 for v1.71
	//******************************************************************
	public GStrDlog(Frame parent, boolean modal, String prompt,
					 String default_str, StringWrapper return_str)
	{
		super(parent, modal);


		this.prompt_string = prompt;
		this.return_string = return_str;
		this.default_string = default_str;

		setSize(391,148);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setTitle("Get String");
		setResizable(false);

		gb_layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gb_layout);

		prompt_label = new Label();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(prompt_label);
		gb_layout.setConstraints(prompt_label, constraints);

		return_string_field = new TextField();
		return_string_field.setColumns(55);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(return_string_field);
		gb_layout.setConstraints(return_string_field, constraints);
		return_string_field.selectAll();

		cancel_button = new java.awt.Button();
		cancel_button.setLabel("Cancel");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 20;
		insets.left = 40;
		insets.right = 20;
		constraints.insets = insets;
		add(cancel_button);
		gb_layout.setConstraints(cancel_button, constraints);

		ok_button = new java.awt.Button();
		ok_button.setLabel("OK");
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 20;
		insets.left = 20;
		insets.right = 40;
		constraints.insets = insets;
		add(ok_button);
		gb_layout.setConstraints(ok_button, constraints);

		setupDlog();

		ActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);
		cancel_button.addActionListener(action_listener);

		pack();

		return;

	}// end of init()


	//***************************************************************
	//*		A D D  N O T I F Y
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


	class MyActionListener implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ok_button)
				okButton_MousePressed(event);
			else if (object == cancel_button)
				cancelButton_MousePressed(event);
		}
	}

	void okButton_MousePressed(ActionEvent event)
	{
			getResults();
			setVisible(false);
	}

	void cancelButton_MousePressed(ActionEvent event)
	{
		setVisible(false);
	}

	protected void setupDlog()
	{
		if (prompt_string != null)
			prompt_label.setText(new String(prompt_string));

		if (default_string != null)
			return_string_field.setText(new String(default_string));

		return;

	}// end of setupDlog()


	protected void getResults()
	{
		//***** Get the font name
		if (return_string_field.getText() != null)
		{
			return_string.setString(return_string_field.getText());
		}

	}// end of getResults()

}


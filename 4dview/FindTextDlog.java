import java.awt.*;
import java.awt.event.*;


public class FindTextDlog extends Dialog
{
	TextField 	find_text_field = null;
	Label 		find_text_label = null;
	Checkbox 	ignore_case_checkbox = null;
	Button 		done_button = null;
	Button 		find_next_button = null;
	Button 		find_button = null;


    //***********************************************************************
    //*		I N I T
    //*		Modified 10/16/02 for v1.66
    //*		Modified 1/29/03 for v1.71
    //***********************************************************************
	public FindTextDlog(Frame parent, boolean modal)
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
		setSize(286,146);
		setFont(new Font("Dialog", Font.PLAIN, 11));
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setResizable(false);

		find_text_label = new Label("Find:");
		find_text_label.setSize(39,20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(find_text_label);
		gbl.setConstraints(find_text_label, constraints);

		find_text_field = new TextField();
		find_text_field.setSize(242,20);
		find_text_field.setColumns(30);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 5;
		insets.right = 10;
		constraints.insets = insets;
		add(find_text_field);
		gbl.setConstraints(find_text_field, constraints);

		ignore_case_checkbox = new Checkbox("Ignore Case");
		ignore_case_checkbox.setSize(106,22);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(ignore_case_checkbox);
		gbl.setConstraints(ignore_case_checkbox, constraints);

		find_button = new Button();
		find_button.setLabel("Find");
		find_button.setSize(70,22);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(find_button);
		gbl.setConstraints(find_button, constraints);

		find_next_button = new Button();
		find_next_button.setLabel("Find Next");
		find_next_button.setSize(70,22);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(find_next_button);
		gbl.setConstraints(find_next_button, constraints);

		done_button = new Button();
		done_button.setLabel("Done");
		done_button.setSize(70,22);
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(done_button);
		gbl.setConstraints(done_button, constraints);

		setTitle("Text Search");

		MyActionListener action_listener = new MyActionListener();
		done_button.addActionListener(action_listener);
		find_button.addActionListener(action_listener);
		find_next_button.addActionListener(action_listener);

		pack();

		return;


	}// end of init


    //***********************************************************************
    //*		A D D  N O T I F Y
    //*		Modified 10/14/02 for v1.66
    //***********************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}/// end of addNotify()


    //***********************************************************************
    //*		S E T  V I S I B L E
    //***********************************************************************
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


	//*****************************************************************
	//*		M Y   A C T I O N  L I S T E N E R
	//*		Added 10/16/02 for v1.66
	//*****************************************************************
	class MyActionListener implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();

			if (object == find_button)
				findButtonClicked(event);
			else if (object == find_next_button)
				findNextButtonClicked(event);
			else if (object == done_button)
				doneButtonClicked(event);

			return;

		}// end of actionPerformed();

	}// end of MyActionListener


	//*****************************************************************
	//*		F I N D  B U T T O N  C L I C K E D
	//*		Added 10/16/02 for v1.66
	//*****************************************************************
	private void findButtonClicked(ActionEvent event)
	{
		try
		{

		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown during text search.");
		}//catch

		return;

	}// end of findButtonClicked()


	//*****************************************************************
	//*		F I N D  N E X T  B U T T O N  C L I C K E D
	//*		Added 10/16/02 for v1.66
	//*****************************************************************
	private void findNextButtonClicked(ActionEvent event)
	{
		try
		{

		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown during text search.");
		}//catch

		return;

	}// end of findNextButtonClicked()


	//*****************************************************************
	//*		D O N E  B U T T O N  C L I C K E D
	//*		Added 10/16/02 for v1.66
	//*****************************************************************
	private void doneButtonClicked(ActionEvent event)
	{
		try
		{
			dispose();

		}// try
		catch (Exception e)
		{
		}//catch

		return;

	}// end of doneButtonClicked()

}


import java.awt.*;
import java.awt.event.*;

public class QuitDialog extends Dialog
{
	Button 				yes_button = null;
	Button 				no_button = null;
	Label 				label_one = null;
	BooleanWrapper		confirmed = null;

	GridBagLayout		gbl = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;


	//***************************************************************************
	//*		I N I T
	//*		Added 10/14/02 for v1.66
	//*		Modified 01/29/03 for v1.71
	//***************************************************************************
	public QuitDialog(BooleanWrapper confirm, Frame parent, boolean modal)
	{
		super(parent, modal);

		confirmed = confirm;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setVisible(false);
		setSize(237,114);
		setResizable(false);
		setFont(new Font("Dialog", Font.BOLD, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));

		label_one = new java.awt.Label("Confirm quit?",Label.CENTER);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(label_one);
		setTitle("QuitDialog");
		gbl.setConstraints(label_one, constraints);

		yes_button = new java.awt.Button();
		yes_button.setLabel(" Yes ");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 20;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(yes_button);
		gbl.setConstraints(yes_button, constraints);

		no_button = new java.awt.Button();
		no_button.setLabel("  No  ");
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 20;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(no_button);
		gbl.setConstraints(no_button, constraints);

		MyActionListener action_listener = new MyActionListener();
		no_button.addActionListener(action_listener);
		yes_button.addActionListener(action_listener);

		pack();

		return;

	}// end of init()


	//***************************************************************
	//*		A D D  N O T I F Y
	//*		Added 10/14/02 for v1.66
	//***************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//***************************************************************
	//*		S E T  V I S I B L E
	//*		Added 10/14/02 for v1.66
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
	}// end of setVisible


	class MyActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object object = event.getSource();
			if (object == no_button)
				noButtonClicked(event);
			else if (object == yes_button)
				yesButtonClicked(event);
		}
	}

	void yesButtonClicked(ActionEvent event)
	{
		confirmed.setValue(true);
		dispose();
	}

	void noButtonClicked(ActionEvent event)
	{
		confirmed.setValue(false);
	    dispose();
	}

}// end of class QuitDialog


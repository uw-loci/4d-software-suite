import java.awt.*;

public class AboutDialog extends Dialog
{
	Label				label_one = null, label_two = null;
	Label				label_three = null, label_four = null, label_five = null;
	Button				ok_button = null;


	//*******************************************************
	//*		I N I T
	//*		Added 10/14/02 for v1.66
	//*		Modified 1/29/03 for v1.71
	//*******************************************************
	public AboutDialog(Frame parent, boolean modal)
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
		setSize(280,150);
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setFont(new Font("Geneva", Font.PLAIN, 9));
		setResizable(false);

		label_one = new java.awt.Label(" 4D Viewer/Java ",Label.CENTER);
		label_one.setFont(new Font("Geneva", Font.BOLD, 11));
		label_one.setSize(250,20);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 3;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 3;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(label_one);
		gbl.setConstraints(label_one, constraints);

		label_two = new Label("A Java application for the navigation", Label.CENTER);
		label_two.setSize(250,15);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(label_two);
		gbl.setConstraints(label_two, constraints);

		label_three = new Label("and analysis of 4D data sets.", Label.CENTER);
		label_three.setSize(250,15);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(label_three);
		gbl.setConstraints(label_three, constraints);

		label_four = new Label("Written by Charles Thomas", Label.CENTER);
		label_four.setSize(250,15);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(label_four);
		gbl.setConstraints(label_four, constraints);

		label_five = new Label("for Univ. of Wisconsin - Madison ?2002", Label.CENTER);
		label_five.setSize(250,15);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		insets.top = 0;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(label_five);
		gbl.setConstraints(label_five, constraints);

		ok_button = new java.awt.Button();
		ok_button.setLabel("OK");
		ok_button.setSize(64,27);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.NONE;
		insets.top = 20;
		insets.bottom = 20;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(ok_button);
		gbl.setConstraints(ok_button, constraints);

		setTitle("About 4D Turnaround");

		MyActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);

		pack();

	}// end of init()


	//******************************************************************
	//*		I N I T
	//*		Added 10/14/02 for v1.66
	//******************************************************************
	public AboutDialog(Frame parent, String title, boolean modal)
	{
		this(parent, modal);
		setTitle(title);

	}// end of init()


	//******************************************************************
	//*		A D D  N O T I F Y
	//*		Added 10/14/02 for v1.66
	//******************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify();

	//******************************************************************
	//*		S E T  V I S I B L E
	//*		Added 10/14/02 for v1.66
	//******************************************************************
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
	}

	class MyActionListener implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ok_button)
				ok_button_Clicked(event);
		}
	}

	void ok_button_Clicked(java.awt.event.ActionEvent event)
	{
    	dispose();
	}

}// end of class AboutDialog


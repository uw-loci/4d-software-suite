import java.awt.*;

public class PutMsgDlog extends Dialog
{
	Button 		ok_button = null;
	Label 		msg_label = null;

	GridBagLayout		gb_layout = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;

	//**********************************************************************
	//*		I N I T
	//*		Added 10/14/02 for v1.66
	//*		Modified 1/29/03 for v1.71
	//**********************************************************************
	public PutMsgDlog(Frame parent, boolean modal, String msg)
	{
		super(parent, modal);

		setVisible(false);
		setSize(319,156);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setTitle("Message");
		setResizable(false);

		gb_layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gb_layout);

		msg_label = new Label(msg, Label.CENTER);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(msg_label);
		gb_layout.setConstraints(msg_label, constraints);

		ok_button = new java.awt.Button();
		ok_button.setLabel("OK");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 20;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(ok_button);
		gb_layout.setConstraints(ok_button, constraints);

		MyMouseAdapter mouse_adapter = new MyMouseAdapter();
		ok_button.addMouseListener(mouse_adapter);

		setupDlog();

		pack();

		return;

	}// end of init


	//***********************************************************************
	//*		A D D  N O T I F Y
	//*		Added 10/14/02 for v1.66
	//***********************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//***********************************************************************
	//*		S E T  V I S I B L E
	//*		Added 10/14/02 for v1.66
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


	class MyMouseAdapter extends java.awt.event.MouseAdapter
	{
		public void mousePressed(java.awt.event.MouseEvent event)
		{
			Object object = event.getSource();
			if (object == ok_button)
				okButton_MousePressed(event);
		}
	}

	void okButton_MousePressed(java.awt.event.MouseEvent event)
	{
		this.setVisible(false);
	}


	private void setupDlog()
	{
		return;

	}// end of setupDlog()

}// end of class PutMsgDlog


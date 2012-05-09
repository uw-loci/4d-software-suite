import java.awt.*;public class PutMsgDlog extends Dialog{	Button 		ok_button = null;	Label 		msg_label = null;	GridBagLayout		gb_layout = null;	GridBagConstraints	constraints = null;	Insets				insets = null;	//**********************************************************************	//*			I N I T	//*			Last modified 12/30/02 for v1.37	//**********************************************************************	public PutMsgDlog(Frame parent, boolean modal, String msg)	{		super(parent, modal);				setLayout(null);		setVisible(false);		setSize(319,156);		setFont(new Font("Dialog", Font.PLAIN, 12));		setForeground(new Color(0));		setBackground(new Color(16777215));		setTitle("Message");		setResizable(false);		gb_layout = new GridBagLayout();		constraints = new GridBagConstraints();		insets = new Insets(0,0,0,0);		setLayout(gb_layout);		msg_label = new Label(msg, Label.CENTER);		//msg_label.setLocation(15,11);		//msg_label.setSize(292,93);		constraints.gridx = 0;		constraints.gridy = 0;		constraints.gridwidth = 1;		constraints.anchor = GridBagConstraints.CENTER;		constraints.fill = GridBagConstraints.HORIZONTAL;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 20;		insets.bottom = 0;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		add(msg_label);		gb_layout.setConstraints(msg_label, constraints);		ok_button = new java.awt.Button();		ok_button.setLabel("OK");		//ok_button.setLocation(109,122);		//ok_button.setSize(91,26);		constraints.gridx = 0;		constraints.gridy = 1;		constraints.gridwidth = 1;		constraints.anchor = GridBagConstraints.CENTER;		constraints.fill = GridBagConstraints.NONE;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 20;		insets.bottom = 20;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		add(ok_button);		gb_layout.setConstraints(ok_button, constraints);		MyMouseAdapter mouse_adapter = new MyMouseAdapter();		ok_button.addMouseListener(mouse_adapter);				setupDlog();				pack();	}		public void addNotify()	{  	    // Record the size of the window prior to calling parents addNotify.	    Dimension d = getSize();		super.addNotify();		if (fComponentsAdjusted)			return;		// Adjust components according to the getInsets		setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);		Component components[] = getComponents();		for (int i = 0; i < components.length; i++)		{			Point p = components[i].getLocation();			p.translate(getInsets().left, getInsets().top);			components[i].setLocation(p);		}		fComponentsAdjusted = true;	}    // Used for addNotify check.	boolean fComponentsAdjusted = false;	public PutMsgDlog(Frame parent, String title, boolean modal, String msg)	{		this(parent, modal, msg);		setTitle(title);	}    /**     * Shows or hides the component depending on the boolean flag b.     * @param b  if true, show the component; otherwise, hide the component.     * @see java.awt.Component#isVisible     */    public void setVisible(boolean b)	{		if(b)		{			Rectangle bounds = getParent().getBounds();			Rectangle abounds = getBounds();				setLocation(bounds.x + (bounds.width - abounds.width)/ 2,				 bounds.y + (bounds.height - abounds.height)/2);		}		super.setVisible(b);	}	class MyMouseAdapter extends java.awt.event.MouseAdapter	{		public void mousePressed(java.awt.event.MouseEvent event)		{			Object object = event.getSource();			if (object == ok_button)				okButton_MousePressed(event);		}	}	void okButton_MousePressed(java.awt.event.MouseEvent event)	{		this.setVisible(false);	}			private void setupDlog()	{		return;			}// end of setupDlog()}
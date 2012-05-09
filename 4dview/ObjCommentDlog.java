import java.awt.*;
import java.awt.event.*;

public class ObjCommentDlog extends Dialog
{
	final	static	int		NUM_ROWS = 10;
	final	static	int		NUM_COLUMNS = 35;


	StringWrapper	ods = null;
	StringWrapper	ocs = null;
	//BooleanWrapper 	was_cancelled = null;

	Label			object_descriptor_label = null;
	TextField		object_descriptor_field = null;
	Label 			object_comments_label = null;
	TextArea 		object_comments_area = null;
	//Button			cancel_button = null;
	Button			ok_button = null;


	//*****************************************************************************
	//*		I N I T
	//*		Modified 10/15/02 for v1.66
	//*		Modified 1/23/03 for v1.70
	//*		Modified 7/7/03 for 1.74
	//*****************************************************************************
	public ObjCommentDlog(Frame parent, boolean modal, StringWrapper obj_desc_str,
						  StringWrapper obj_comments_str)
	{
		super(parent, modal);


		GridBagLayout		gbl = null;
		GridBagConstraints	constraints = null;
		Insets				insets = null;

		ods = obj_desc_str;
		ocs = obj_comments_str;
		//was_cancelled = cancelled;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(gbl);
		insets = new Insets(0,0,0,0);

		setVisible(false);
		setSize(293,319);
		setFont(new Font("Dialog", Font.PLAIN, 11));
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setResizable(false);

		object_descriptor_label = new java.awt.Label("Object Descriptor:");
		object_descriptor_label.setSize(119,22);
		constraints.gridx = 0;
		constraints.gridy = 0;
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
		add(object_descriptor_label);
		gbl.setConstraints(object_descriptor_label, constraints);

		object_descriptor_field = new java.awt.TextField();
		object_descriptor_field.setSize(273,28);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(object_descriptor_field);
		gbl.setConstraints(object_descriptor_field, constraints);

		object_comments_label = new java.awt.Label("Object Comments:");
		object_comments_label.setSize(134,22);
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
		insets.right = 10;
		constraints.insets = insets;
		add(object_comments_label);
		gbl.setConstraints(object_comments_label, constraints);

		object_comments_area = new TextArea(null, NUM_ROWS, NUM_COLUMNS,TextArea.SCROLLBARS_VERTICAL_ONLY);
		object_comments_area.setSize(274,144);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(object_comments_area);
		gbl.setConstraints(object_comments_area, constraints);

/*		cancel_button = new java.awt.Button();
		cancel_button.setLabel("Cancel");
		cancel_button.setSize(49,25);
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
		insets.left = 0;
		insets.right = 0;
		constraints.insets = insets;
		add(cancel_button);
		gbl.setConstraints(cancel_button, constraints);
*/
		ok_button = new java.awt.Button();
		ok_button.setLabel("   OK   ");
		ok_button.setSize(49,25);
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
		insets.left = 10;
		insets.right = 10;
		constraints.insets = insets;
		add(ok_button);
		gbl.setConstraints(ok_button, constraints);

		setTitle("Object Comments");

		MyWindowAdapter window_adapter = new MyWindowAdapter();
		this.addWindowListener(window_adapter);

		ActionListener action_listener = new MyActionListener();
		ok_button.addActionListener(action_listener);
		//cancel_button.addActionListener(action_listener);

		setupDlog();

		pack();

		return;

	}// end of init()


	//*****************************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 10/15/02 for v1.66
	//*****************************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//*****************************************************************************
	//*		S E T  V I S I B L E
	//*		Modified 10/15/02 for v1.66
	//*****************************************************************************
    public void setVisible(boolean b)
	{
		if(b)
		{
			setLocation(50,50);
		}
		super.setVisible(b);

		return;

	}// end of setVisible()


	//**************************************************************
	//*		M Y  W I N D O W  A D A P T E R
	//*		Added 7/7/03 for v1.74
	//**************************************************************
	class MyWindowAdapter extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			setVisible(false);
			dispose();

			return;

		}// end of windowClosing()

	}// end of class MyWindowAdapter


	//**********************************************************************
	//*		C L A S S  M Y  A C T I O N  L I S T E N E R
	//*		Added 1/23/03 for v1.70
	//*		Modified 6/30/03 for 1.74
	//**********************************************************************
	class MyActionListener implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == ok_button)
				okButton_ActionPerformed(event);
			//else if (object == cancel_button)
			//	cancelButton_ActionPerformed(event);

			return;

		}// end of actionPerformed()

	}// end of class MyActionListener


	//**********************************************************************
	//*		S E T U P  D L O G
	//*		Modified 10/15/02 for v1.66
	//**********************************************************************
	protected void setupDlog()
	{

		object_descriptor_field.setEditable(false);
		object_comments_area.setEditable(false);

		object_descriptor_field.setText(ods.getString());
		object_comments_area.setText(ocs.getString());

		return;

	}// end of setupDlog()


	//**********************************************************************
	//*		O K  B U T T O N
	//*		Modified 10/15/02 for v1.66
	//*		Modified 1/23/03 for v1.70
	//*		Modified 6/30/03 for 1.74
	//**********************************************************************
	void okButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		//was_cancelled.setValue(false);
		setVisible(false);
		dispose();

	}// end of ok_button()

/*
	//**********************************************************************
	//*		C A N C E L  B U T T O N
	//*		Modified 1/23/03 for v1.70
	//**********************************************************************
	void cancelButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		was_cancelled.setValue(true);
		setVisible(false);
		dispose();
	}
*/
}// end of class ObjCommentDlog


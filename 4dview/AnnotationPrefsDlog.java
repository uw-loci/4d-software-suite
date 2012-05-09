import java.awt.*;

public class AnnotationPrefsDlog extends Dialog
{
	Label 			annotation_prefs_label = null;

	CheckboxGroup 	Group1 = null;
	Checkbox 		bring_new_radio_btn = null;
	Checkbox 		bring_all_radio_btn = null;
	Checkbox 		bring_no_radio_btn = null;

	Checkbox 		advance_checkbox = null;
	Checkbox 		prompt_checkbox = null;
	Checkbox		auto_object_comments_checkbox = null;

	Button 			ok_btn = null;
	Button 			cancel_btn = null;


	//********************************************************************
	//*		I N I T
	//*		Modified 1/22/03 for v1.70
	//********************************************************************
	public AnnotationPrefsDlog(Frame parent, boolean modal)
	{
		super(parent, modal);

		GridBagLayout		gbl = null;
		GridBagConstraints	constraints = null;
		Insets				insets = null;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setSize(371,300);
		setResizable(false);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));

		annotation_prefs_label = new java.awt.Label("Annotation Preferences:");
		annotation_prefs_label.setSize(223,24);
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
		add(annotation_prefs_label);
		gbl.setConstraints(annotation_prefs_label, constraints);

		Group1 = new CheckboxGroup();

		bring_all_radio_btn = new java.awt.Checkbox("Bring all overlay objects forward", Group1, false);
		bring_all_radio_btn.setSize(300,20);
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
		insets.right = 0;
		constraints.insets = insets;
		add(bring_all_radio_btn);
		gbl.setConstraints(bring_all_radio_btn, constraints);

		bring_new_radio_btn = new java.awt.Checkbox("Bring only new objects forward", Group1, false);
		bring_new_radio_btn.setSize(300,20);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
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
		add(bring_new_radio_btn);
		gbl.setConstraints(bring_new_radio_btn, constraints);

		bring_no_radio_btn = new java.awt.Checkbox("Bring no objects forward", Group1, false);
		bring_no_radio_btn.setSize(300,20);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
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
		add(bring_no_radio_btn);
		gbl.setConstraints(bring_no_radio_btn, constraints);

		prompt_checkbox = new java.awt.Checkbox("Prompt user to save changes to overlays");
		prompt_checkbox.setSize(281,20);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(prompt_checkbox);
		gbl.setConstraints(prompt_checkbox, constraints);

		advance_checkbox = new java.awt.Checkbox("Advance to next frame after saving overlay to disk");
		advance_checkbox.setSize(349,20);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 2;
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
		add(advance_checkbox);
		gbl.setConstraints(advance_checkbox, constraints);

		auto_object_comments_checkbox = new java.awt.Checkbox("Ask for comments after creating new object");
		auto_object_comments_checkbox.setSize(349,20);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 2;
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
		add(auto_object_comments_checkbox);
		gbl.setConstraints(auto_object_comments_checkbox, constraints);

		cancel_btn = new Button();
		cancel_btn.setLabel("Cancel");
		cancel_btn.setSize(60,23);
		constraints.gridx = 0;
		constraints.gridy = 7;
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
		add(cancel_btn);
		gbl.setConstraints(cancel_btn, constraints);

		ok_btn = new Button();
		ok_btn.setLabel("   OK   ");
		ok_btn.setBounds(125,199,60,23);
		constraints.gridx = 1;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 0;
		insets.right = 10;
		constraints.insets = insets;
		add(ok_btn);
		gbl.setConstraints(ok_btn, constraints);

		setTitle("Annotation Preferences");

		MyActionListener action_listener = new MyActionListener();
		ok_btn.addActionListener(action_listener);
		cancel_btn.addActionListener(action_listener);

		setupDlog();

	}// init


	//**********************************************************
	//*		A D D  N O T I F Y
	//*		Modified 10/16/02 for v1.66
	//**********************************************************
	public void addNotify()
	{
		super.addNotify();

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
			if (object == ok_btn)
				ok_btn_ActionPerformed(event);
			else if (object == cancel_btn)
				cancel_btn_ActionPerformed(event);

			return;

		}// end of actionPerformed()

	}// end of class MyActionListener


	//**********************************************************
	//*		O K  B U T T O N
	//**********************************************************
	void ok_btn_ActionPerformed(java.awt.event.ActionEvent event)
	{
			getResults();
			setVisible(false);
			dispose();

			return;
	}// end of ok_button


	//**********************************************************
	//*		C A N C E L  B U T T O N
	//**********************************************************
	void cancel_btn_ActionPerformed(java.awt.event.ActionEvent event)
	{
		setVisible(false);
		dispose();

		return;
	}


	//**********************************************************
	//*		S E T U P  D L O G
	//*		Modified 1/22/03 for v1.70
	//**********************************************************
 	public void setupDlog()
	{
		bring_new_radio_btn.setState(Settings.overlay_forward_pref == Settings.BRING_NEW_OBJECTS);
		bring_all_radio_btn.setState(Settings.overlay_forward_pref == Settings.BRING_ALL_OBJECTS);
		bring_no_radio_btn.setState(Settings.overlay_forward_pref == Settings.BRING_NO_OBJECTS);

		prompt_checkbox.setState(Settings.prompt_for_saves);
		advance_checkbox.setState(Settings.advance_to_next_frame);
		auto_object_comments_checkbox.setState(Settings.auto_object_comments);

		return;

	}// end of setupDlog()


	//**********************************************************
	//*		G E T  R E S U L T S
	//*		Modified 1/22/03 for v1.70
	//**********************************************************
	public void getResults()
	{
		if (bring_new_radio_btn.getState())
			Settings.overlay_forward_pref = Settings.BRING_NEW_OBJECTS;
		if (bring_all_radio_btn.getState())
			Settings.overlay_forward_pref = Settings.BRING_ALL_OBJECTS;
		if (bring_no_radio_btn.getState())
			Settings.overlay_forward_pref = Settings.BRING_NO_OBJECTS;

		Settings.prompt_for_saves = prompt_checkbox.getState();
		Settings.advance_to_next_frame = advance_checkbox.getState();
		Settings.auto_object_comments = auto_object_comments_checkbox.getState();

		return;

	}// end of getResults()

}// end of class AnnotationPrefsDlog


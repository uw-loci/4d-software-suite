import java.awt.*;
import java.awt.event.*;

public class GetSubMovieParamsDlog extends Dialog
{

	Label 				prompt_label = null;
	Label				fp_label = null;
	Label				start_frame_label = null;
	Label				end_frame_label = null;
	TextField 			focal_plane_field = null;
	TextField			start_frame_field = null;
	TextField			end_frame_field = null;
	Button 				ok_button = null;
	Button 				cancel_button = null;

	IntWrapper			plane = null;
	LongWrapper			start_frame = null;
	LongWrapper			end_frame = null;
	BooleanWrapper		cancelled = null;

	GridBagLayout		gb_layout = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;


	//******************************************************************
	//*		I N I T
	//*		Added 8/25/03 for v1.76
	//******************************************************************
	public GetSubMovieParamsDlog(Frame parent,
								 boolean modal,
								 IntWrapper pl,
								 LongWrapper sf,
								 LongWrapper ef,
								 BooleanWrapper cbw)
	{
		super(parent, modal);


		setSize(200,400);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));
		setTitle("Get Sub-Movie Parameters");
		setResizable(false);

		this.plane = pl;
		this.start_frame = sf;
		this.end_frame = ef;
		this.cancelled = cbw;

		gb_layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gb_layout);

		prompt_label = new Label("Enter sub-movie parameters:");
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

		fp_label = new Label("Focal Plane:");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(fp_label);
		gb_layout.setConstraints(fp_label, constraints);

		focal_plane_field = new TextField();
		focal_plane_field.setColumns(5);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(focal_plane_field);
		gb_layout.setConstraints(focal_plane_field, constraints);
		focal_plane_field.selectAll();

		start_frame_label = new Label("Start Frame:");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(start_frame_label);
		gb_layout.setConstraints(start_frame_label, constraints);

		start_frame_field = new TextField();
		start_frame_field.setColumns(5);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(start_frame_field);
		gb_layout.setConstraints(start_frame_field, constraints);

		end_frame_label = new Label("End Frame:");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(end_frame_label);
		gb_layout.setConstraints(end_frame_label, constraints);

		end_frame_field = new TextField();
		end_frame_field.setColumns(5);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 20;
		insets.right = 20;
		constraints.insets = insets;
		add(end_frame_field);
		gb_layout.setConstraints(end_frame_field, constraints);

		cancel_button = new java.awt.Button();
		cancel_button.setLabel("Cancel");
		constraints.gridx = 0;
		constraints.gridy = 4;
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
		constraints.gridy = 4;
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


	//****************************************************************************
	//*		O K  B U T T O N
	//****************************************************************************
	void okButton_MousePressed(ActionEvent event)
	{
		cancelled.setValue(false);
		getResults();
		setVisible(false);

		return;
	}

	//****************************************************************************
	//*		C A N C E L  B U T T O N
	//*		Modified 9/22/03 for v1.77
	//****************************************************************************
	void cancelButton_MousePressed(ActionEvent event)
	{
		cancelled.setValue(true);
		setVisible(false);

		return;
	}


	//****************************************************************************
	//*		S E T U P  D L O G
	//*		Added 8/25/03 for v1.76
	//****************************************************************************
	protected void setupDlog()
	{
		focal_plane_field.setText("" + DataInfo.cur_focal_plane);
		start_frame_field.setText("" + DataInfo.cur_frame);
		end_frame_field.setText("" + DataInfo.num_frames);

		return;

	}// end of setupDlog()


	//****************************************************************************
	//*		G E T  R E S U L T S
	//*		Added 9/11/03 for v1.77
	//****************************************************************************
	protected void getResults()
	{
		plane.setValue(getInt(focal_plane_field, DataInfo.cur_focal_plane));
		start_frame.setValue(getLong(start_frame_field, DataInfo.cur_frame));
		end_frame.setValue(getLong(end_frame_field, DataInfo.num_frames));

		return;

	}// end of getResults()


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


	//*********************************************************************
	//*		G E T  L O N G
	//*********************************************************************
	long getLong(TextField field, long default_value)
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
			return((long)d.doubleValue());
		else
			return(default_value);

	}// end of getLong()

}// end of class GetSubMovieParamsDlog


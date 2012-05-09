import java.awt.*;

class PositionPanel extends Panel
{
	final	static	int		PREFERRED_WIDTH = 64;
	final	static	int		PREFERRED_HEIGHT = 64;

	private	int				plane = 0;
	private	long			frame = 0;

	private	Label			frame_label = null;
	private	Label			plane_label = null;
	private	TextField		frame_text_field = null;
	private	TextField		plane_text_field = null;

	private	Button			go_to_button = null;


	//**********************************************************
	//*		I N I T
	//*		Modified 10/14/02 for v1.66
	//**********************************************************
	public PositionPanel()
	{

		super();

		GridBagLayout		gbl = null;
		GridBagConstraints	constraints = null;
		Insets				insets = null;

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(gbl);
		insets = new Insets(0,0,0,0);

		setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
		setFont(new Font("Geneva", Font.PLAIN, 9));
		setForeground(new Color(0));
		setBackground(Color.lightGray);

		frame_label = new java.awt.Label("Frame:");
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 3;
		insets.bottom = 0;
		insets.left = 3;
		insets.right = 0;
		constraints.insets = insets;
		add(frame_label);
		gbl.setConstraints(frame_label, constraints);

		frame_text_field = new java.awt.TextField();
		frame_text_field.setColumns(3);
		frame_text_field.setBackground(Color.white);
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 3;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 3;
		constraints.insets = insets;
		add(frame_text_field);
		gbl.setConstraints(frame_text_field, constraints);

		plane_label = new java.awt.Label("Plane:");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 3;
		insets.bottom = 0;
		insets.left = 3;
		insets.right = 0;
		constraints.insets = insets;
		add(plane_label);
		gbl.setConstraints(plane_label, constraints);

		plane_text_field = new java.awt.TextField();
		plane_text_field.setColumns(3);
		plane_text_field.setBackground(Color.white);
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 3;
		insets.bottom = 0;
		insets.left = 0;
		insets.right = 3;
		constraints.insets = insets;
		add(plane_text_field);
		gbl.setConstraints(plane_text_field, constraints);

		go_to_button = new Button("Go To");
		go_to_button.setFont(new Font("Geneva", Font.PLAIN, 8));
		//go_to_button.setSize(54, 10);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 0;
		constraints.weighty = 0;
		insets.top = 3;
		insets.bottom = 3;
		insets.left = 3;
		insets.right = 3;
		constraints.insets = insets;
		add(go_to_button);
		gbl.setConstraints(go_to_button, constraints);

		MyActionListener action_listener = new MyActionListener();
		go_to_button.addActionListener(action_listener);

		setFrame(0);
		setPlane(0);

		return;

	}// init


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 10/15/02 for v1.66
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

	}// end of getPreferredSize()


	//*******************************************************
	//*		P A I N T
	//*		Modified 7/25/02 for v1.59
	//*******************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		try
		{
			outlinePanel(g);
		}
		catch (Exception e)
		{
		}

		return;

	}// end of paint


	//*******************************************************
	//*		O U T L I N E  P A N E L
	//*		Modified 8/23/02 for v1.61
	//*******************************************************
	private void outlinePanel(Graphics incoming_graphics) throws Exception
	{
		Graphics		g = null;

		if (incoming_graphics == null)
			g = this.getGraphics();
		else
			g = incoming_graphics;

		if (g != null)
		{
			g.setColor(Color.black);
			g.drawRect(1, 1, this.getSize().width - 2, this.getSize().height -2);
		}

		if (incoming_graphics == null && g != null)
			g.dispose();

		return;

	}// end of outlinePanel()


	//*******************************************************
	//*		S E T  F R A M E
	//*******************************************************
	void setFrame(long frame)
	{
		this.frame = frame;
		frame_text_field.setText(" " + frame);
		return;
	}// end of setFrame()

	//*******************************************************
	//*		S E T  P L A N E
	//*******************************************************
	void setPlane(int plane)
	{
		this.plane = plane;
		plane_text_field.setText(" " + plane);
		return;
	}// end of setPlane()

	//*******************************************************
	//*		S E T  P O S I T I O N
	//*******************************************************
	void setPosition(long frame, int plane)
	{
		setFrame(frame);
		setPlane(plane);
		return;
	}// end of setPosition()


	//*******************************************************
	//*		G E T  F R A M E
	//*******************************************************
	long getFrame()
	{
		return(getLong(frame_text_field, this.frame));
	}// end of setFrame()

	//*******************************************************
	//*		G E T  P L A N E
	//*******************************************************
	int getPlane()
	{
		return(getInt(plane_text_field, this.plane));
	}// end of setPlane()


	//*******************************************************
	//*		M Y  A C T I O N  L I S T E N E R
	//*		Modified 12/18/02 for v1.70
	//*******************************************************
	class MyActionListener implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == go_to_button)
			{
				if (Settings.data_display_panel != null)
				{
					Settings.data_display_panel.goTo(getPlane(), getFrame());

					if (Settings.main_window != null)
						Settings.main_window.requestFocus();
				}
			}// if it was the "go to" button

			return;

		}// end of actionPerformed()

	}// end of class MyActionListener


	//*******************************************************
	//*		G E T  I N T
	//*******************************************************
	int getInt(TextField field, int default_value)
	{
		Double d = null;

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


	//*******************************************************
	//*		G E T  L O N G
	//*******************************************************
	long getLong(TextField field, long default_value)
	{
		Double d = null;

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


}// end of class PositionPanel

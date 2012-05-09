import java.awt.*;import java.awt.event.*;import java.io.*;import java.util.*;public class DecompInfoDlog extends Dialog implements ItemListener{	Label 				num_bit_files_label = null;	TextField 			num_bit_files_text_field = null;	TextField 			num_fp_text_field = null;	TextField 			num_tp_text_field = null;	Label 				output_format_label = null;	Choice 				output_format_choice = null;	Label 				num_fp_label = null;	Label 				num_tp_label = null;	Label 				base_fn_label = null;	TextField 			base_fn_text_field = null;		Label				output_directory_label = null;	Choice				output_directory_choice = null;		Button 				ok_button = null;	Button 				cancel_button = null;	BooleanWrapper		cancelled = null;		String				cur_directory = null;	Vector				directories = null;	boolean				first_update = true;	//***** Layout crap	GridBagLayout		gb_layout = null;	GridBagConstraints	constraints = null;	Insets				insets = null;	int					text_field_columns = 5;	//********************************************************************************	//*		I N I T	//*		Last modified 12/30/02 for v1.37	//********************************************************************************	public DecompInfoDlog(Frame parent, boolean modal, BooleanWrapper cancel_obj)	{		super(parent, modal);		setTitle("Decompression Info");		this.cancelled = cancel_obj;		this.cur_directory = Settings.output_directory;		setVisible(false);		setSize(361,340);		setResizable(false);		setForeground(new Color(0));		setBackground(new Color(16777215));		setResizable(false);		setFont(new Font("Dialog", Font.PLAIN, 12));		gb_layout = new GridBagLayout();		constraints = new GridBagConstraints();		insets = new Insets(0,0,0,0);		setLayout(gb_layout);				num_tp_label = new Label("Num Time Points:");		//num_tp_label.setLocation(19,20);		//num_tp_label.setSize(127,20);		constraints.gridx = 0;		constraints.gridy = 0;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 0;		insets.top = 20;		insets.bottom = 0;		constraints.insets = insets;		add(num_tp_label);		gb_layout.setConstraints(num_tp_label, constraints);		num_tp_text_field = new TextField();		num_tp_text_field.setColumns(text_field_columns);				//num_tp_text_field.setLocation(169,28);		//num_tp_text_field.setSize(61,20);		constraints.gridx = 1;		constraints.gridy = 0;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 20;		insets.right = 20;		insets.top = 20;		insets.bottom = 0;		constraints.insets = insets;		add(num_tp_text_field);		gb_layout.setConstraints(num_tp_text_field, constraints);				num_fp_label = new Label("Num Focal Planes:");		//num_fp_label.setLocation(19,61);		//num_fp_label.setSize(127,20);		constraints.gridx = 0;		constraints.gridy = 1;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 0;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(num_fp_label);		gb_layout.setConstraints(num_fp_label, constraints);		num_fp_text_field = new TextField();		num_fp_text_field.setColumns(text_field_columns);				//num_fp_text_field.setLocation(169,58);		//num_fp_text_field.setSize(61,20);		constraints.gridx = 1;		constraints.gridy = 1;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 20;		insets.right = 20;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(num_fp_text_field);		gb_layout.setConstraints(num_fp_text_field, constraints);			num_bit_files_label = new Label("Num Bit Files:");		//num_bit_files_label.setLocation(19,93);		//num_bit_files_label.setSize(127,20);		constraints.gridx = 0;		constraints.gridy = 2;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 0;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(num_bit_files_label);		gb_layout.setConstraints(num_bit_files_label, constraints);				num_bit_files_text_field = new TextField();		num_bit_files_text_field.setColumns(text_field_columns);				//num_bit_files_text_field.setLocation(169,90);		//num_bit_files_text_field.setSize(61,20);		constraints.gridx = 1;		constraints.gridy = 2;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 20;		insets.right = 20;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(num_bit_files_text_field);		gb_layout.setConstraints(num_bit_files_text_field, constraints);				base_fn_label = new Label("Base Filename:");		//base_fn_label.setLocation(19,123);		//base_fn_label.setSize(117,20);		constraints.gridx = 0;		constraints.gridy = 3;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 0;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(base_fn_label);		gb_layout.setConstraints(base_fn_label, constraints);		base_fn_text_field = new TextField();		base_fn_text_field.setColumns(20);				//base_fn_text_field.setLocation(135,125);		//base_fn_text_field.setSize(121,20);		constraints.gridx = 1;		constraints.gridy = 3;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 20;		insets.right = 20;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(base_fn_text_field);		gb_layout.setConstraints(base_fn_text_field, constraints);						output_format_label = new Label("Decompress As:");		//output_format_label.setLocation(18,153);		//output_format_label.setSize(127,20);		constraints.gridx = 0;		constraints.gridy = 4;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 0;		insets.top = 15;		insets.bottom = 0;		constraints.insets = insets;		add(output_format_label);		gb_layout.setConstraints(output_format_label, constraints);				output_format_choice = new Choice();		//output_format_choice.setLocation(18,174);		//output_format_choice.setSize(182,20);		constraints.gridx = 0;		constraints.gridy = 5;		constraints.gridwidth = 2;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 20;		insets.right = 20;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(output_format_choice);		gb_layout.setConstraints(output_format_choice, constraints);				output_directory_label = new Label("Folder to save files:");		//output_directory_label.setLocation(18, 210);		//output_directory_label.setSize(185, 20);		constraints.gridx = 0;		constraints.gridy = 6;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 0;		insets.top = 15;		insets.bottom = 0;		constraints.insets = insets;		add(output_directory_label);		gb_layout.setConstraints(output_directory_label, constraints);				output_directory_choice = new Choice();		//output_directory_choice.setLocation(18, 234);		//output_directory_choice.setSize(285, 30);		output_directory_choice.setFont(new Font("Geneva", Font.PLAIN, 10));		constraints.gridx = 0;		constraints.gridy = 7;		constraints.gridwidth = 2;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.HORIZONTAL;		insets.left = 20;		insets.right = 20;		insets.top = 0;		insets.bottom = 0;		constraints.insets = insets;		add(output_directory_choice);		gb_layout.setConstraints(output_directory_choice, constraints);						cancel_button = new Button();		cancel_button.setLabel("Cancel");		//cancel_button.setLocation(89,294);		//cancel_button.setSize(60,23);		constraints.gridx = 0;		constraints.gridy = 8;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.EAST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 20;		insets.right = 20;		insets.top =20;		insets.bottom = 20;		constraints.insets = insets;		add(cancel_button);		gb_layout.setConstraints(cancel_button, constraints);		ok_button = new Button();		ok_button.setLabel("OK");		//ok_button.setLocation(189,294);		//ok_button.setSize(60,23);		constraints.gridx = 1;		constraints.gridy = 8;		constraints.gridwidth = 1;		constraints.gridheight = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.weightx = 1;		constraints.weighty = 1;		constraints.fill = GridBagConstraints.NONE;		insets.left = 40;		insets.right = 20;		insets.top = 20;		insets.bottom = 20;		constraints.insets = insets;		add(ok_button);		gb_layout.setConstraints(ok_button, constraints);						setupDlog();		MyMouseAdapter mouse_adapter = new MyMouseAdapter();		cancel_button.addMouseListener(mouse_adapter);		ok_button.addMouseListener(mouse_adapter);				output_directory_choice.addItemListener(this);		pack();	}		public void addNotify()	{  	    // Record the size of the window prior to calling parents addNotify.	    Dimension d = getSize();		super.addNotify();		if (fComponentsAdjusted)			return;		// Adjust components according to the getInsets		setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);		Component components[] = getComponents();		for (int i = 0; i < components.length; i++)		{			Point p = components[i].getLocation();			p.translate(getInsets().left, getInsets().top);			components[i].setLocation(p);		}		fComponentsAdjusted = true;	}    // Used for addNotify check.	boolean fComponentsAdjusted = false;    /**     * Shows or hides the component depending on the boolean flag b.     * @param b  if true, show the component; otherwise, hide the component.     * @see Component#isVisible     */    public void setVisible(boolean b)	{		if(b)		{			Rectangle bounds = getParent().getBounds();			Rectangle abounds = getBounds();				setLocation(bounds.x + (bounds.width - abounds.width)/ 2,				 bounds.y + (bounds.height - abounds.height)/2);		}		super.setVisible(b);	}	class MyMouseAdapter extends MouseAdapter	{		public void mousePressed(MouseEvent event)		{			Object object = event.getSource();			if (object == cancel_button)				cancelButton_MousePressed(event);			else if (object == ok_button)				okButton_MousePressed(event);		}	}	//*********************************************************************	//*		C A N C E L  B U T T O N	//*		Last modified 12/30/02 for v1.37	//*********************************************************************	void cancelButton_MousePressed(MouseEvent event)	{		cancelled.setValue(true);// flag for aborted run		setVisible(false);		dispose();	}	//*********************************************************************	//*		O K  B U T T O N	//*		Last modified 12/30/02 for v1.37	//*********************************************************************	void okButton_MousePressed(MouseEvent event)	{		cancelled.setValue(false);// flag for aborted run		getResults();		setVisible(false);		dispose();	}	public void itemStateChanged(ItemEvent event)	{		String		path_string = null;		File		cur_file = null;		Object 		object = null;				object = event.getSource();				if (object == output_directory_choice)		{			if (output_directory_choice.getSelectedIndex() == 0)			{				cur_file = new File(this.cur_directory);				if (cur_file != null && cur_file.exists())				{						cur_file = new File(cur_file.getParent());										if (cur_file != null && cur_file.exists())					{						path_string = cur_file.getAbsolutePath();												//***** Make it the cur_directory if we're not at the root						if (!path_string.equals(File.separator) && !path_string.equals(File.pathSeparator))							this.cur_directory = path_string;											updateChoice();					}					}// if we have a valid directory				else					Settings.image_window.displayMessage("Invalid directory selected.");								}// if they chose the ".." item			else if (output_directory_choice.getSelectedIndex() == 1)			{				// do nothing if they choose the current directory			}			else if (output_directory_choice.getSelectedIndex() > 1)			{				path_string = (String)this.directories.elementAt(output_directory_choice.getSelectedIndex() - 1);				cur_file = new File(path_string);						if (cur_file != null && cur_file.exists())				{											//***** Make it the cur_directory if we're not at the root					if (!path_string.equals(File.separator) && !path_string.equals(File.pathSeparator))						this.cur_directory = path_string;								updateChoice();				}// if the directory exists				else					Settings.image_window.displayMessage("Invalid directory selected.");			}// if they're choosing a sub-directory of the current directory		}// if the event was in the choice	}// end of itemStateChanged()				protected void setupDlog()	{				base_fn_text_field.setText(Settings.base_bit_filename);		//Settings.ocs.num_first_timepoint = 0;		num_tp_text_field.setText(Integer.toString(Settings.ocs.num_timepoints_compressed));		num_fp_text_field.setText(Integer.toString(Settings.ocs.num_planes_in_stack));		num_bit_files_text_field.setText(Integer.toString(Settings.ocs.num_bitfiles_created));		output_format_choice.addItem("8 Bit Grayscale - TIFF");		output_format_choice.addItem("24 Bit RGB - TIFF");				updateChoice();						try 		{			if (Settings.output_file_type == ImageReader.GRAY_8_BIT_IMAGE)				output_format_choice.select(0);			else if (Settings.output_file_type == ImageReader.RGB_IMAGE)				output_format_choice.select(1);		}		catch (IllegalArgumentException e) { }		return;			}// end of setupDlog()			void getResults()	{		int out_format = 0;								out_format = output_format_choice.getSelectedIndex();// debugging				switch (out_format)		{			case 0:				 Settings.output_file_type = ImageReader.GRAY_8_BIT_IMAGE;				 break;			case 1:				 Settings.output_file_type = ImageReader.RGB_IMAGE;				 break;		}// switch		Settings.total_timepoints = getInt(num_tp_text_field, Settings.ocs.num_timepoints_compressed);		Settings.total_planes = getInt(num_fp_text_field, Settings.ocs.num_planes_in_stack);		Settings.total_bitfiles = getInt(num_bit_files_text_field, Settings.ocs.num_bitfiles_created);		Settings.base_timepoint_name = base_fn_text_field.getText();				//***** Set the output directory		if (output_directory_choice.getSelectedIndex() == 1)		{			Settings.output_directory = this.cur_directory;		}		else if (output_directory_choice.getSelectedIndex() > 1)		{			if (this.cur_directory.endsWith(File.separator))				Settings.output_directory = this.cur_directory + output_directory_choice.getSelectedItem();			else				Settings.output_directory = this.cur_directory + File.separator + output_directory_choice.getSelectedItem();		}				//***** Make sure the directory ends with a file separator		if (!Settings.output_directory.endsWith(File.separator))			Settings.output_directory += File.separator;				//***** Test		Settings.image_window.displayMessage("Output Directory selected:");		Settings.image_window.displayMessage(Settings.output_directory);				return;				}// end of getResults()		//*********************************************************************	//*		G E T  I N T	//*********************************************************************	int getInt(TextField field, int default_value) 	{		Double d = null;				try 		{			d = new Double(field.getText());		}		catch (NumberFormatException e) 		{			field.setText(""+default_value);// if the number's invalid, use default			d = null;		}// if an exception was generated				if (d != null)			return((int)d.doubleValue());		else			return(default_value);			}// end of getInt()			//*********************************************************************	//*		U P D A T E  C H O I C E	//*		Last updated 6/27/02	//*********************************************************************	void updateChoice() 	{		Properties	sys_props = null;		String		key = null;		FontMetrics	fm = null;		String		truncated_string = null, test_string = "A sample string of a reasonable length.";		int			i = 0, default_width = 0;		boolean		at_root = false;						if (cur_directory == null)		{			//***** If the cur_directory is null, make it the same directory as the application.			sys_props = System.getProperties();			key = "user.dir"; 			cur_directory = sys_props.getProperty(key);			Settings.output_directory = cur_directory;						if (cur_directory == null)				return;		}				//***** Find our default width		fm = Toolkit.getDefaultToolkit().getFontMetrics(output_directory_choice.getFont());		default_width = fm.stringWidth(test_string);				//***** Set the choice to a longer string to prevent layout error during pack()		if (first_update)		{			output_directory_choice.add(test_string);			this.pack();			this.setSize(getPreferredSize());			first_update = false;		}				output_directory_choice.removeAll();			//***** Add the ".." choice if we're not at the root			if (cur_directory.equals(File.separator) || cur_directory.equals(File.pathSeparator))			at_root = true;				if (!at_root)			output_directory_choice.add("..");				directories = IOUtils.getDirectoryPaths(cur_directory);				if (directories == null)			return;				for (i = 0; i < directories.size(); i++)		{			try			{				truncated_string = StringUtils.fitStringToWidth((String)directories.elementAt(i), 															    output_directory_choice.getFont(), 															    default_width);			}			catch (Exception e){}			output_directory_choice.add(truncated_string);		}				if (!at_root && output_directory_choice.getItemCount() > 1)			output_directory_choice.select(1);		else if (output_directory_choice.getItemCount() > 0)			output_directory_choice.select(0);					return;			}// end of updateChoice()		}// end of DecompInfoDlog class
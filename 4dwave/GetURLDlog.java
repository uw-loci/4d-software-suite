import java.awt.*;public class GetURLDlog	extends GStrDlog{	Choice		url_choice	 = null;			//******************************************************************	//*		I N I T	//*		Last modified 6/27/02	//******************************************************************	public GetURLDlog(Frame parent, boolean modal, String prompt, 					  String default_str, StringWrapper return_str,					  String[] url_array)	{			super(parent, modal, prompt, default_str, return_str);			setTitle("Get Input URL");			url_choice = new Choice();				//***** Setup the url_choice		if (url_array != null)		{			try			{				//***** Add the URLs to the choice				for (int i = 0; url_array[i] != null; i++)					url_choice.add(url_array[i]);				}			catch (ArrayIndexOutOfBoundsException aioobe)			{								}		}// if we have a url_choice		if (url_choice != null && url_choice.getItemCount() > 0)		{			url_choice.select(0);			return_string_field.setText(url_choice.getSelectedItem());		}		constraints.gridx = 0;		constraints.gridy = 0;		constraints.gridwidth = 2;		constraints.anchor = GridBagConstraints.WEST;		constraints.fill = GridBagConstraints.NONE;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 20;		insets.bottom = 0;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		gb_layout.setConstraints(prompt_label, constraints);		constraints.gridx = 0;		constraints.gridy = 1;		constraints.gridwidth = 2;		constraints.anchor = GridBagConstraints.WEST;		constraints.fill = GridBagConstraints.HORIZONTAL;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 0;		insets.bottom = 0;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		add(url_choice);		gb_layout.setConstraints(url_choice, constraints);		constraints.gridx = 0;		constraints.gridy = 2;		constraints.gridwidth = 2;		constraints.anchor = GridBagConstraints.WEST;		constraints.fill = GridBagConstraints.HORIZONTAL;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 20;		insets.bottom = 0;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		gb_layout.setConstraints(return_string_field, constraints);		constraints.gridx = 0;		constraints.gridy = 3;		constraints.gridwidth = 1;		constraints.anchor = GridBagConstraints.EAST;		constraints.fill = GridBagConstraints.NONE;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 20;		insets.bottom = 20;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		gb_layout.setConstraints(cancel_button, constraints);		constraints.gridx = 1;		constraints.gridy = 3;		constraints.gridwidth = 1;		constraints.anchor = GridBagConstraints.WEST;		constraints.fill = GridBagConstraints.NONE;		constraints.weightx = 0;		constraints.weighty = 1;		insets.top = 20;		insets.bottom = 20;		insets.left = 20;		insets.right = 20;		constraints.insets = insets;		gb_layout.setConstraints(ok_button, constraints);				MyItemListener item_listener = new MyItemListener();		url_choice.addItemListener(item_listener);							pack();				return;			}// init	//***************************************************************	//*		S E T U P  D L O G	//*		Last modified 6/27/02	//***************************************************************	protected void setupDlog()	{		super.setupDlog();						return;			}// end of setupDlog()	class MyItemListener implements java.awt.event.ItemListener	{		public void itemStateChanged(java.awt.event.ItemEvent event)		{			Object object = event.getSource();					if (object == url_choice && url_choice.getItemCount() > 0)			{				return_string_field.setText(url_choice.getSelectedItem());			}						return;				}// end of itemStateChanged();			}// class MyItemListener	//***************************************************************	//*		G E T  R E S U L T S	//*		Last modified 6/27/02	//***************************************************************	protected void getResults()	{					super.getResults();				return;					}// end of getResults()}// end of class GetURLDlog
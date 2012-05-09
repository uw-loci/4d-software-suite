
import java.awt.*;
import java.awt.event.*;

public class BookmarksWindow extends Frame
{
	Choice						bookmarks_choice = null;
	Label						list_actions_label = null;
	Label						file_label = null;
	Label						bookmarks_label = null;
	Button						goto_button = null;
	Button						load_bookmarks_button = null;
	Button						save_bookmarks_button = null;
	Button						done_button = null;
	Button						clear_bookmark_button = null;
	Button						set_bookmark_button = null;

	BookmarkArray				bookmark_array = null;

	GridBagLayout				gbl = null;
	GridBagConstraints			constraints = null;
	Insets						insets = null;


	//*****************************************************************
	//*		I N I T
	//*		Modified 10/25/02 for v1.68
	//*****************************************************************

	public BookmarksWindow()
	{
		this.bookmark_array = new BookmarkArray();

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setVisible(false);
		setSize(332,301);
		setResizable(false);
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setForeground(new Color(0));
		setBackground(new Color(16777215));

		bookmarks_label = new Label("Current Bookmarks:");
		bookmarks_label.setSize(140,21);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(bookmarks_label);
		gbl.setConstraints(bookmarks_label, constraints);

		bookmarks_choice = new Choice();
		bookmarks_choice.setSize(291,26);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 3;
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
		add(bookmarks_choice);
		gbl.setConstraints(bookmarks_choice, constraints);

		list_actions_label = new Label("Bookmark Actions:");
		list_actions_label.setSize(128,23);
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(list_actions_label);
		gbl.setConstraints(list_actions_label, constraints);

		set_bookmark_button = new Button();
		set_bookmark_button.setLabel("Set New");
		set_bookmark_button.setSize(62,33);
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(set_bookmark_button);
		gbl.setConstraints(set_bookmark_button, constraints);

		clear_bookmark_button = new Button();
		clear_bookmark_button.setLabel("Clear");
		clear_bookmark_button.setSize(62,31);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(clear_bookmark_button);
		gbl.setConstraints(clear_bookmark_button, constraints);

		goto_button = new Button();
		goto_button.setLabel("Go To");
		goto_button.setSize(62,32);
		constraints.gridx = 2;
		constraints.gridy = 3;
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
		add(goto_button);
		gbl.setConstraints(goto_button, constraints);

		file_label = new Label("File Actions:");
		file_label.setSize(86,22);
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(file_label);
		gbl.setConstraints(file_label, constraints);

		save_bookmarks_button = new Button();
		save_bookmarks_button.setLabel("Save");
		save_bookmarks_button.setSize(61,31);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(save_bookmarks_button);
		gbl.setConstraints(save_bookmarks_button, constraints);

		load_bookmarks_button = new Button();
		load_bookmarks_button.setLabel("Load");
		load_bookmarks_button.setSize(62,31);
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 10;
		insets.bottom = 0;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(load_bookmarks_button);
		gbl.setConstraints(load_bookmarks_button, constraints);

		done_button = new Button();
		done_button.setLabel("     Done     ");
		done_button.setSize(113,30);
		done_button.setForeground(Color.red);
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.weightx = 1;
		constraints.weighty = 1;
		insets.top = 20;
		insets.bottom = 10;
		insets.left = 10;
		insets.right = 0;
		constraints.insets = insets;
		add(done_button);
		gbl.setConstraints(done_button, constraints);

		setTitle("Bookmarks");

		MyWindowAdapter window_adapter = new MyWindowAdapter();
		this.addWindowListener(window_adapter);

		MyActionListener action_listener = new MyActionListener();
		done_button.addActionListener(action_listener);
		clear_bookmark_button.addActionListener(action_listener);
		set_bookmark_button.addActionListener(action_listener);
		load_bookmarks_button.addActionListener(action_listener);
		save_bookmarks_button.addActionListener(action_listener);
		goto_button.addActionListener(action_listener);

		//***** Add the names of the bookmarks
		updateBookmarksChoice();
		Settings.bookmarks_window = this;

		pack();

		return;

	}// end of init()


	//**************************************************************
	//*		S E T  V I S I B L E
	//**************************************************************
    public void setVisible(boolean show)
	{
		if(show)
		{
			setLocation(50, 50);
		}

		super.setVisible(show);

		return;

	}// end of setVisible()


	//**************************************************************
	//*		A D D  N O T I F Y
	//*		Modified 10/25/02 for v1.68
	//**************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify()


	//**************************************************************
	//*		M Y  W I N D O W  A D A P T E R
	//**************************************************************
	class MyWindowAdapter extends WindowAdapter
	{
		public void windowClosing(WindowEvent event)
		{
			Object object = event.getSource();
			if (object == BookmarksWindow.this)
				BookmarksWindow_WindowClosing(event);

			return;

		}// end of windowClosing()

	}// end of class MyWindowAdapter


	void BookmarksWindow_WindowClosing(WindowEvent event)
	{
		Settings.main_window.displayMessage("Closing bookmarks window.");
		setVisible(false);		 // hide the Frame
	}


	//**************************************************************
	//*		M Y  A C T I O N  L I S T E N E R
	//**************************************************************
	class MyActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object object = event.getSource();
			if (object == done_button)
				doneButton_ActionPerformed(event);
			if (object == clear_bookmark_button)
				clearBookmarkButton_ActionPerformed(event);
			else if (object == set_bookmark_button)
				setBookmarkButton_ActionPerformed(event);
			else if (object == load_bookmarks_button)
				loadBookmarksButton_ActionPerformed(event);
			else if (object == save_bookmarks_button)
				saveBookmarksButton_ActionPerformed(event);
			else if (object == goto_button)
				gotoButton_ActionPerformed(event);

			return;

		}// end of actionPerformed()


	}// end of class MyActionListener


	void gotoButton_ActionPerformed(ActionEvent event)
	{
		BookmarkEntry		be = null;
		long				frame = 0;
		int					plane = 0, num_bookmarks = 0, index = -1;

		try
		{
			index = bookmarks_choice.getSelectedIndex();
			num_bookmarks = bookmark_array.numBookmarks();

			if (index >= 0 && index < num_bookmarks)
			{
				be = bookmark_array.getEntry(index);
				frame = be.frame;
				plane = be.focal_plane;
				Settings.data_display_panel.goTo(plane, frame);
			}
		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while accessing new location!");
		}
	}

	void clearBookmarkButton_ActionPerformed(ActionEvent event)
	{
		int		index = -1;

		try
		{
			index = bookmarks_choice.getSelectedIndex();
			bookmark_array.clearBookmark(index);
			updateBookmarksChoice();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown clearing bookmarks!");
		}
	}

	void setBookmarkButton_ActionPerformed(ActionEvent event)
	{
		int		num_bookmarks = 0;

		try
		{
			num_bookmarks = bookmark_array.numBookmarks();
			bookmark_array.setBookmark(Settings.main_window, DataInfo.cur_focal_plane, DataInfo.cur_frame);
			updateBookmarksChoice();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while setting bookmark!");
		}

	}

	void loadBookmarksButton_ActionPerformed(ActionEvent event)
	{
		try
		{
			bookmark_array.readBookmarksFromDisk();
		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown loading bookmarks from disk!");
		}
		this.updateBookmarksChoice();
	}

	void saveBookmarksButton_ActionPerformed(ActionEvent event)
	{
		try
		{
			bookmark_array.saveBookmarksToDisk();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown saving bookmarks to disk!");
		}
	}

	void doneButton_ActionPerformed(ActionEvent event)
	{
		setVisible(false);
		Settings.bookmarks_window = null;
		dispose();
	}


	//**************************************************************
	//*		U P D A T E  B O O K S M A R K S  C H O I C E
	//*		Modified 10/25/02 for v1.68
	//**************************************************************
	public void updateBookmarksChoice()
	{
		BookmarkEntry		be = null;
		int					num_bookmarks = 0, index = -1;
		try
		{
			bookmarks_choice.removeAll();
			num_bookmarks = bookmark_array.numBookmarks();

			for (int i = 0; i < num_bookmarks; i++)
			{
				index = i;

				be = bookmark_array.getEntry(i);
				bookmarks_choice.addItem(new String(be.name));
			}// for each bookmark entry

			if (index != -1)
				bookmarks_choice.select(index);
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception in BookmarksWindow.updateBookmarksChoice().");
		}
	}// end of updateBookmarksChoice()

}// class BookmarksWindow


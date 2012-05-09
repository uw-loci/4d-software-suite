import java.awt.*;
import java.util.*;
import java.net.*;
import java.awt.event.*;


public class EditingPanel extends Panel
{

	EditingCanvas 		editing_canvas = null;
	EditingPalette		editing_palette = null;
	PositionPanel		position_panel = null;
	OverlayStatusPanel	overlay_status_panel = null;

	GridBagLayout		gbl = null;
	GridBagConstraints	constraints = null;
	Insets				insets = null;

	//******************************************************
	//*		I N I T
	//*		Modified 8/26/02 for v1.61
	//******************************************************
	public EditingPanel()
	{
		int			num_components = 0;

		setVisible(true);

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		setLayout(gbl);
		insets = new Insets(0,0,0,0);

		editing_palette = new EditingPalette();
		if (editing_palette != null)
		{
			editing_palette.setSize(2 * EditingPalette.BUTTON_WIDTH, 5 * EditingPalette.BUTTON_HEIGHT);
			constraints.gridy = num_components;
			constraints.gridx = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 0;
			constraints.weighty = 0;
			insets.top = 0;
			insets.bottom = 0;
			insets.left = 0;
			insets.right = 0;
			constraints.insets = insets;
			gbl.setConstraints(editing_palette, constraints);
			add(editing_palette);
			num_components += 1;
		}// if we have an editing palette


		position_panel = new PositionPanel();
		if (position_panel != null)
		{
			position_panel.setSize(ToolPalette.PALETTE_WIDTH,
								   ToolPalette.PALETTE_WIDTH);
			constraints.gridy = num_components;
			constraints.gridx = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 0;
			constraints.weighty = 0;
			insets.top = 0;
			insets.bottom = 0;
			insets.left = 0;
			insets.right = 0;
			constraints.insets = insets;
			gbl.setConstraints(position_panel, constraints);
			add(position_panel);
			num_components += 1;
		}// if we have a position panel


		overlay_status_panel = new OverlayStatusPanel();
		if (overlay_status_panel != null)
		{
			overlay_status_panel.setSize(ToolPalette.PALETTE_WIDTH,
										 ToolPalette.BUTTON_HEIGHT/2);
			constraints.gridy = num_components;
			constraints.gridx = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 0;
			constraints.weighty = 0;
			//constraints.ipadx = 54;
			//constraints.ipady = 6;
			insets.top = 0;
			insets.bottom = 0;
			insets.left = 0;
			insets.right = 0;
			constraints.insets = insets;
			gbl.setConstraints(overlay_status_panel, constraints);
			add(overlay_status_panel);
			num_components += 1;

		}// if we have an overlay_status_panel


		editing_canvas = new EditingCanvas();
		if (editing_canvas != null)
		{
			editing_canvas.setSize(DataInfo.data_width, DataInfo.data_height);
			constraints.gridy = 0;
			constraints.gridx = 1;
			constraints.gridwidth = 1;
			constraints.gridheight = num_components;
			constraints.anchor = GridBagConstraints.CENTER;
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 0;
			constraints.weighty = 0;
			insets.top = 0;
			insets.bottom = 0;
			insets.left = 0;
			insets.right = 0;
			constraints.insets = insets;
			gbl.setConstraints(editing_canvas, constraints);
			add(editing_canvas);
			Settings.editing_canvas = editing_canvas;
			num_components += 1;
		}// if we have an editing canvas

		MyKeyAdapter key_adapter = new MyKeyAdapter();
		this.addKeyListener(key_adapter);

		return;

	}// end of init


	//**********************************************************************************
	//*		M Y  K E Y  A D A P T E R
	//**********************************************************************************
	class MyKeyAdapter extends java.awt.event.KeyAdapter
	{
		public void keyPressed(KeyEvent e)
		{
		}
		public void keyTyped(KeyEvent e){}
		public void keyReleased(KeyEvent e){}

	}// end of class myKeyAdapter


	//******************************************************
	//*		P A I N T
	//*		Modified 7/23/02 for v1.59
	//******************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		displayDataInfo();

		g.setColor(Color.green);
		g.drawRect(1, 1, this.getBounds().width -2, this.getBounds().height-2);

		return;

	}// end of paint




	//********************************************************************
	//*		D I S P L A Y  M O V I E  I N F O
	//*		Writes the current frame and plane onto the position panel
	//********************************************************************
	public void  displayDataInfo()
	{
		if (position_panel != null)
			position_panel.setPosition(DataInfo.cur_frame, DataInfo.cur_focal_plane);

		return;

	}// end of displayDataInfo();


}// end of class EditingPanel

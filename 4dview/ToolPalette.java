import java.awt.*;
import java.awt.event.*;

class ToolPalette extends Panel
{

	//***** PALETTE CONSTANTS
	final	static		int			BUTTON_HEIGHT = 32;
	final	static		int			BUTTON_WIDTH = 32;
	final	static		int			PALETTE_WIDTH = 64;
	final	static		int			PALETTE_HEIGHT = 5 * BUTTON_HEIGHT;

	GridBagLayout			gbl = null;
	GridBagConstraints		constraints = null;
	Insets					insets = null;

	MyMouseAdapter			mouse_adapter = null;


	//********************************************************
	//*		I N I T
	//*		Modified 10/22/02 for v1.68
	//********************************************************
	public ToolPalette()
	{
		super();

		gbl = new GridBagLayout();
		constraints = new GridBagConstraints();
		insets = new Insets(0,0,0,0);
		setLayout(gbl);

		setVisible(true);

		//***** Add mouse listeners
		mouse_adapter = new MyMouseAdapter();
		this.addMouseListener(mouse_adapter);

	}// init

	class MyMouseAdapter extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e)
		{
			doMouseClicked(e);
		}// end of mouseClicked()

	}// end of MyMouseAdapter()

	protected void doMouseClicked(MouseEvent e)
	{
		return;
	}

	//**************************************************************
	//*		C O N F I R M  I M A G E  L O A D I N G
	//*		Modified 7/19/02 for v1.59
	//**************************************************************
	protected void confirmImageLoading(Image img)
	{
		int				image_width = -1, image_height = -1;

		if (img == null)
			return;

		do
		{
			image_width = img.getWidth(null);
			image_height = img.getHeight(null);

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ignore){}
		}
		while (image_width == -1 || image_height == -1);

	}// end of confirmImageLoading()


	//**************************************************************
	//*		S E T  G B L  C O N S T R A I N T S
	//*		Modified 7/19/02 for v1.59
	//**************************************************************
	protected void setGBLConstraints(ImageButton btn, int x_pos, int y_pos)
	{
		if (btn == null)
			return;
		try
		{
			constraints.gridx = x_pos;
			constraints.gridy = y_pos;
			constraints.gridwidth = 1;
			constraints.anchor = GridBagConstraints.WEST;
			constraints.fill = GridBagConstraints.NONE;
			constraints.weightx = 1;
			constraints.weighty = 1;
			insets.top = 0;
			insets.bottom = 0;
			insets.left = 0;
			insets.right = 0;
			constraints.insets = insets;
			gbl.setConstraints(btn, constraints);

		}
		catch (Exception e)
		{

		}

		return;
	}// end of setGBLConstraints()


	//**************************************************************
	//*		P A I N T
	//*		Added 7/3/03 for v1.74
	//**************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		g.setColor(Color.lightGray);
		g.drawRect(1, 1, this.getBounds().width -2, this.getBounds().height-2);

	}// end of paint()

}// end of class ToolPalette

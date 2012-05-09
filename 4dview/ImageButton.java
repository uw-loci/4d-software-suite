import java.awt.*;
import java.awt.event.*;


public class ImageButton extends Canvas
{

	Image		button_image = null;


	//*************************************************************
	//*		S E T  I M A G E
	//*************************************************************
	void	setImage(Image img)
	{

		if (img != null)
		{
			this.button_image = img;
		}

		repaint();

		return;

	}// end of setImage


	//*************************************************************
	//*		P A I N T
	//*************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);
		updateImage(g);

		return;

	}// end of paint()


	//*************************************************************
	//*		U P D A T E  I M A G E
	//*************************************************************
	public void updateImage(Graphics g)
	{

		if (button_image != null)
		{
			g.drawImage(button_image, 0, 0, this.getSize().width, this.getSize().height, this);
		}

		return;

	}// end of updateImage


	//*************************************************************
	//*		C L I C K  B U T T O N
	//*************************************************************
	public void clickButton()
	{
		Graphics	g = this.getGraphics();

		if (button_image != null && g != null)
		{
			//***** Draw the rect in black
			g.fillRect(0,0,this.getSize().width, this.getSize().height);

			//***** Wait around briefly
			try{Thread.sleep(20);}catch (Exception e){}

			//***** Restore the image
			updateImage(g);

			//***** Get rid of the graphics context
			g.dispose();
		}

		return;

	}// end of clickButton


}// end of class ImageButton

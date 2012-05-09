import java.awt.*;
import quicktime.app.image.*;


//*****************************************************************************************
//*****************************************************************************************
//*		C L A S S  I M A G E  P A I N T E R
//*		Paints a java.awt.Image into a QTImageDrawer
//*****************************************************************************************
//*****************************************************************************************

public class ImagePainter implements Paintable
{
	private int 					width = -1, height = -1;
	private java.awt.Image			cur_image = null;
	private Rectangle[] 			return_rect = null;


	//**************************************************
	//*		I N I T
	//**************************************************
	public ImagePainter (Image img, int width, int height)
	{
		setCurrentImage(img);
		this.width = width;
		this.height = height;
		return_rect = new Rectangle[1];

		return;

	}// end of init


	public void setCurrentImage (Image img)
	{
		if (img != null)
		{
			this.cur_image = null;
			System.gc();// run the garbage collector
			this.cur_image = img;
		}
		return;
	}

	public void setWidth(int w)
	{
		if (w > 0)
		{
			this.width = w;
		}
		return;
	}

	public void setHeight(int h)
	{
		if (h > 0)
		{
			this.height = h;
		}
		return;
	}


	/**
	 * The Parent object of the Paintable tell the paintable object the size of its available
	 * drawing surface. Any drawing done outside of these bounds (originating at 0,0) will
	 * be clipped.
	 */
	public void newSizeNotified (QTImageDrawer drawer, Dimension d)
	{
		width = d.width;
		height = d.height;
		return_rect[0] = new Rectangle (width, height);
	}

	/**
	 * Paint on the graphics. The supplied component is the component from which
	 * the graphics object was derived or related to and is also the component
	 * that is the object that paint was called upon that has called this method.
	 * The Graphics object is what you should paint on.
	 * This maybe an on or off screen graphics.
	 * You should not cache this graphics object as it can be different
	 * between different calls.
	 * @param comp the component from which the Graphics object was derived or
	 * related too.
	 * @param g the graphics to paint on.
	 */
	public Rectangle[] paint (Graphics g)
	{
		g.drawImage(cur_image, 0, 0, null);

		return_rect[0] = new Rectangle(width, height);

		return(return_rect);
	}

}// end of class ImagePainter

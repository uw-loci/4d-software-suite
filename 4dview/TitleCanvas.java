import java.awt.*;
import java.awt.image.*;
import java.net.*;
import java.io.*;


class TitleCanvas extends Canvas implements ImageObserver
{
	final static	int		TITLE_IMAGE_WIDTH = 300;
	final static	int		TITLE_IMAGE_HEIGHT = 139;

	Image			title_image = null;


	//*******************************************************
	//*		I N I T
	//*		Modified 10/5/02 for v1.66
	//*		Modified 7/22/03 for v1.75
	//*******************************************************
	public TitleCanvas()
	{
		super();

		setSize(TITLE_IMAGE_WIDTH, TITLE_IMAGE_HEIGHT);
		setBackground(Color.white);
		setVisible(true);

		try
		{
			loadTitleImage();
		}
		catch (Exception e){}

		repaint();

		return;

	}//init


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 10/15/02 for v1.66
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(TITLE_IMAGE_WIDTH, TITLE_IMAGE_HEIGHT));

	}// end of getPreferredSize()


	//*******************************************************
	//*		S E T  I M A G E
	//*		Modified 7/18/03 for v1.75
	//*******************************************************
	public void setImage(Image img)
	{
		Dimension d = null;

		if (img != null)
		{
			d = Utils.getImageDimensions(this, img);

        	title_image = img;
        }


		return;

	}// end of setImage()


	//*******************************************************
	//*		P A I N T
	//*******************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		if (title_image != null)
			g.drawImage(title_image, 0,0, null);

		g.setColor(Color.black);
		g.drawRect(1, 1, this.getBounds().width -2, this.getBounds().height-2);

		return;

	}// end of paint()


	//*******************************************************
	//*		L O A D  T I T L E  I M A G E
	//*		Modified 7/22/03 for v1.75
	//*		Modified 9/24/03 for v1.78
	//*******************************************************
	public void loadTitleImage()
	{
		MediaTracker	mt = null;
		Image			img = null;
		InputStream		is = null;
		byte[]			bytes = null;
		URL				url = null;

		try
		{

			url = this.getClass().getResource("Images/Title_image.jpeg");
			if (url != null)
			{
 				//Settings.main_window.displayMessage("" + url);
    			img = Toolkit.getDefaultToolkit().getImage(url);
			}
 		}// try
 		catch (Exception e1)
 		{
			Settings.main_window.displayMessage("Exception thrown during method #1 in loadTitleImage().");
 		}

 		try
 		{
 			if (img == null)
 			{
				url = this.getClass().getResource("Title_image.jpeg");
				if (url != null)
				{
    				img = Toolkit.getDefaultToolkit().getImage(url);
				}

 			}// if we have no image yet
 		}// try
 		catch (Exception e2)
 		{
			Settings.main_window.displayMessage("Exception thrown during method #2 in loadTitleImage().");
 		}

 		try
 		{
        	if (img == null)
        	{
        		is = this.getClass().getResourceAsStream("Title_image.jpeg");
        		if (is != null)
        		{
        			bytes = new byte[is.available()];
        			is.read(bytes);
        			img = Toolkit.getDefaultToolkit().createImage(bytes);
        		}

       		}// if the title image wasn't loaded
 		}// try
 		catch (Exception e3)
 		{
			Settings.main_window.displayMessage("Exception thrown during method #3 in loadTitleImage().");
 		}

 		try
 		{
        	if (img == null)
        	{
        		is = this.getClass().getResourceAsStream("Images/Title_image.jpeg");
        		if (is != null)
        		{
        			bytes = new byte[is.available()];
        			is.read(bytes);
        			img = Toolkit.getDefaultToolkit().createImage(bytes);
        		}

        	}// if the title image wasn't loaded
  		}// try
 		catch (Exception e4)
 		{
			Settings.main_window.displayMessage("Exception thrown during method #1 in loadTitleImage().");
 		}

        if (img != null)
        {
    		mt = new MediaTracker(this);
    		mt.addImage(img, 0);
    		try
    		{
  				mt.waitForAll();
  			}
  			catch (InterruptedException ie)
  			{
  				Thread.currentThread().interrupt();
  			}

    		Object[] errors = mt.getErrorsAny();
    		if (errors != null)
    		{
    			Settings.main_window.displayMessage("Errors were generated during title image loading!");
    		}

        	setImage(img);
        }
        else
        	Settings.main_window.displayMessage("Unable to load title image.");

		return;

	}// end of loadTitleImage()


	//********************************************************************************
	//*		I M A G E  U P D A T E
	//*		Added 7/22/03 for v1.75
	//********************************************************************************
	public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height)
	{

		if ((flags & ABORT) != 0)
			Settings.main_window.displayMessage("ABORT");
		if ((flags & ALLBITS) != 0)
			Settings.main_window.displayMessage("ALLBITS");
		if ((flags & ERROR) != 0)
			Settings.main_window.displayMessage("ERROR");
		if ((flags & FRAMEBITS) != 0)
			Settings.main_window.displayMessage("FRAMEBITS");
		if ((flags & HEIGHT) != 0)
			Settings.main_window.displayMessage("HEIGHT");
		if ((flags & PROPERTIES) != 0)
			Settings.main_window.displayMessage("PROPERTIES");
		if ((flags & SOMEBITS) != 0)
			Settings.main_window.displayMessage("SOMEBITS");
		if ((flags & WIDTH) != 0)
			Settings.main_window.displayMessage("WIDTH");

		else

			Settings.main_window.displayMessage("Width: " + width + ", Height: " + height);

		return(true);

	}// end of imageUpdate()

}// end of class TitleCanvas

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class TiffCanvas extends Canvas
{
	final static int		SPLASH_WIDTH = 400;
	final static int		SPLASH_HEIGHT = 300;
	final static int		NOT_FOUND = -1;

	//***** Begin Class Variables
	public		TiffObjectArray		object_array = null; // holds the overlay objects
	public		int					canvas_width = -1, canvas_height = -1;
	private		Image				splash_image = null;
	private		Image				cur_image = null;

	//*****************************************************************
	//*		I N I T
	//*		Modified 10/21/02 for v1.67
	//*****************************************************************
	public TiffCanvas()
	{
		//***** Initialize Class Variables
		this.object_array = new TiffObjectArray(this);

		this.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
		this.setVisible(true);

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);

		try
		{
			displaySplashImage();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Cannot load splash image!");
		}

		setVisible(true);

		return;

	}// end of init()


	//*******************************************************
	//*		G E T  W I D T H
	//*		Added 10/11/02 for v1.68
	//*******************************************************
	public int getWidth()
	{
		return(canvas_width);

	}// end of getWidth()


	//*******************************************************
	//*		G E T  H E I G H T
	//*		Added 10/11/02 for v1.68
	//*******************************************************
	public int getHeight()
	{
		return(canvas_height);

	}// end of getHeight()


	//*******************************************************
	//*		S E T  S I Z E
	//*		Added 10/17/02 for v1.66
	//*******************************************************
	public void setSize(Dimension d)
	{
		setSize(d.width, d.height);
		return;

	}// end of setSize()


	//*******************************************************
	//*		S E T  S I Z E
	//*		Added 10/22/02 for v1.68
	//*		Modified 11/11/02 for v1.69
	//*******************************************************
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		canvas_width = width;
		canvas_height = height;

		return;

	}// end of setSize()


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 10/15/02 for v1.66
	//*		Modified 10/17/02 for v1.66
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(canvas_width, canvas_height));

	}// end of getPreferredSize()


	//*****************************************************************
	//*		P R O C E S S  M O U S E  E V E N T
	//*****************************************************************
	protected void processMouseEvent(MouseEvent e)
	{
		Object object = e.getSource();

		switch (e.getID())
		{
			case MouseEvent.MOUSE_CLICKED:
				 MovieCanvas_MousePressed(e);
				 break;
			case MouseEvent.MOUSE_ENTERED:
				 break;
			case MouseEvent.MOUSE_EXITED:
				 break;
			case MouseEvent.MOUSE_PRESSED:
				 break;
			case MouseEvent.MOUSE_RELEASED:
				 break;
		}// end of switch

	}// end of processMouseEvent()


	//*****************************************************************
	//*		M O U S E  P R E S S E D
	//*		Modified 10/17/02 for v1.66
	//*****************************************************************
	public void MovieCanvas_MousePressed(MouseEvent event)
	{

		boolean				alt_click = false;
		int					which_object = NOT_FOUND;
		int					x = 0, y = 0;
		AnnotationObject	ao = null;

		try
		{
			if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
				alt_click = true;

			x = event.getX();
			y = event.getY();

			//***** See if we clicked in an existing object
			which_object = object_array.clickInWhichObject(x, y);
			if (which_object != NOT_FOUND)
			{
				ao = object_array.getObject(which_object);
				if (alt_click)
					object_array.doObjectComments(ao);
			}// if the click was in an existing object
		}// try
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown in MovieCanvas.mousePressed()!");
			if (e.getMessage() != null)
				Settings.main_window.displayMessage(e.getMessage());

		}

		return;

	}// end of mousePressed()


	//**********************************************************************
	//*		D I S P L A Y  I M A G E
	//*		Modified 5/1/03 for v1.72
	//*		Modified 7/18/03 for 1.75
	//**********************************************************************
	public void displayImage(Image img) throws Exception
	{
		if (img == null)
			return;

		//***** Get the size of the image
        do
        {
        	DataInfo.data_width = img.getWidth(null);
        	DataInfo.data_height = img.getHeight(null);
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ignore){}
        }
        while (DataInfo.data_width == -1 || DataInfo.data_height == -1);

  		displayImage(img, DataInfo.data_width, DataInfo.data_height);

		return;

	}// end of displayImage()


	//**********************************************************************
	//*		D I S P L A Y  I M A G E
	//*		Modified 5/22/03 for v1.72
	//**********************************************************************
	public void displayImage(Image img, int image_width, int image_height) throws Exception
	{
		Graphics g = this.getGraphics();

		if (img == null || g == null)
			return;

		this.cur_image = img;

		//***** Set the canvas size
		if (image_width != canvas_width || image_height != canvas_height)
		{
			setSize(image_width, image_height);
			Settings.main_window.displayMessage("TiffCanvas resized, w: " + image_width + " h: " + image_height);
		}// if we're displaying an image that's a different size than the canvas

		DataInfo.data_width = image_width;
		DataInfo.data_height = image_height;

        //repaint();

		//****** Draw the image into the canvas
		g.drawImage(cur_image, 0, 0, image_width, image_height, this);
		g.dispose();

		return;

	}// end of displayImage()


	//********************************************************************************
	//*		P A I N T
	//*		Modified 10/21/02 for v1.67
	//*		Modified 5/6/03 for v1.72
	//********************************************************************************
	public void paint(Graphics g)
	{

		super.paint(g);

		try
		{

			//***** Draw the splash image, if necessary
			if (Settings.data_display_panel.data_array.size() <= 0 && splash_image != null)
			{
				drawSplashImage(g);
			}

			//***** Draw an outline around the canvas
			g.setColor(Color.black);
			g.drawRect(1, 1, this.getBounds().width -2, this.getBounds().height-2);

			//****** Draw the current image, if any
			if (cur_image != null)
				g.drawImage(cur_image, 0, 0, canvas_width, canvas_height, null);

			//***** Draw any overlay objects
			this.restoreObjects();

		}//try
		catch (Exception ex)
		{
			Settings.main_window.displayMessage("Exception thrown while drawing TiffCanvas!");
		}
		catch (OutOfMemoryError er)
		{
			Settings.main_window.displayMessage("Out Of Memory Error thrown!");
			Settings.main_window.displayMessage("Quit application, allocate more memory, and try again.");
		}

		return;

	}// end of paint()


	//********************************************************************************
	//*		R E S T O R E  O B J E C T S
	//*		Modified 11/6/02 for v1.68
	//********************************************************************************
	public void restoreObjects() throws Exception
	{
		object_array.restoreObjects();

		return;

	}// end of restoreObjects()


	//********************************************************************
	//*		C L O S E  D A T A  S E T
	//*		Added 12/10/02 for v1.70
	//*		Modified 5/8/03 for v1.72
	//********************************************************************
	public void closeDataSet() throws Exception
	{
			cur_image = null;
			clearCanvas();
			object_array.resetArray();
			displaySplashImage();

			return;

	}// end of closeDataSet()


	//********************************************************************************
	//*		C L E A R  C A N V A S
	//*		Modified 10/17/02 for v1.66
	//********************************************************************************
	public void clearCanvas()
	{
		Rectangle	r = this.getBounds();
		Graphics	g = this.getGraphics();

		if (g != null && r != null)
		{
			g.setColor(Color.lightGray);
			g.fillRect(r.x, r.y, r.width, r.height);

			g.dispose();
		}

		return;

	}// end of clearCanvas()


	//********************************************************************************
	//*		D I S P L A Y  S P L A S H  I M A G E
	//*		Modified 12/10/02 for v1.70
	//*		Modified 7/9/03 for v1.74
	//*		Modified 7/18/03 for v1.75
	//********************************************************************************
	public void displaySplashImage() throws Exception
	{
		InputStream		is = null;
		byte[]			bytes = null;
		URL				url = null;
		int				image_width = -1, image_height = -1;

		if (splash_image == null)
		{
			try
			{

				url = getClass().getResource("Images/4DSplash.jpeg");
				if (url != null)
				{
    				splash_image = Toolkit.getDefaultToolkit().createImage((ImageProducer)url.getContent());
				}

        		if (splash_image == null)
        		{
        			is = getClass().getResourceAsStream("Images/4DSplash.jpeg");
        			bytes = new byte[is.available()];
        			is.read(bytes);
        			splash_image = Toolkit.getDefaultToolkit().createImage(bytes);
        		}// if the title image wasn't loaded

        		if (splash_image != null)
        		{
        			do
        			{
        				image_width = splash_image.getWidth(null);
        				image_height = splash_image.getHeight(null);

        				try
        				{
        					Thread.sleep(100);
        				}
        				catch (InterruptedException ignore){}
        			}
        			while (image_width == -1 || image_height == -1);

            		setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
            		Settings.data_display_panel.setSize(SPLASH_WIDTH, SPLASH_HEIGHT + MoviePanel.SLIDER_HEIGHT);
   				}// if we have an image

			}// try
			catch (Exception e)
			{
				Settings.main_window.displayMessage("Exception during splash image loading!");
			}

		}// if it's not loaded, load the image

		drawSplashImage(null);

		return;

	}// end of displaySplashImage()


	//********************************************************************************
	//*		D R A W  S P L A S H  I M A G E
	//*		Added 7/9/03
	//********************************************************************************
	public void drawSplashImage(Graphics graph) throws Exception
	{

		Graphics g = null;

		if (graph == null)
			g = this.getGraphics();
		else
			g = graph;

		setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
		g.drawImage(splash_image, 0,0, SPLASH_WIDTH, SPLASH_HEIGHT, null);

		if (graph == null && g != null)
			g.dispose();

		return;

	}// end of drawSplashImage()

}//End of TiffCanvas




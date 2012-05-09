import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import quicktime.app.display.*;

public class MovieCanvas extends QTCanvas implements ImageObserver
{
	final static int		SPLASH_WIDTH = 400;
	final static int		SPLASH_HEIGHT = 300;
	final static int		NOT_FOUND = -1;

	//***** Begin Class Variables
	public		MovieObjectArray	object_array = null; // holds the overlay objects
	public		int					canvas_width = -1, canvas_height = -1;
	private		Image				splash_image = null;
	private		Thread				load_thread = null;


	//*****************************************************************
	//*		I N I T
	//*		Modified 10/21/02 for v1.67
	//*****************************************************************
	public MovieCanvas()
	{
		//***** Initialize Class Variables
		this.object_array = new MovieObjectArray(this);

		this.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
		this.setVisible(true);

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);

		doSplashImageLoading();

		setVisible(true);

		return;

	}// end of MovieCanvas()


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


	//********************************************************************************
	//*		P A I N T
	//*		Modified 10/21/02 for v1.67
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

			//***** Draw any overlay objects
			this.restoreObjects();

		}//try
		catch (Exception ex)
		{
			Settings.main_window.displayMessage("Exception thrown while drawing MovieCanvas!");
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
	//*		Modified 7/9/03 for v1.74
	//********************************************************************
	public void closeDataSet() throws Exception
	{
			object_array.resetArray();
			loadSplashImage();
			drawSplashImage(null);

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


	//***********************************************************************
	//*		D O  S P L A S H  I M A G E  L O A D I N G
	//*		Creates a thread which will run and load the title image
	//*		Added 4/22/03 for v1.72
	//***********************************************************************
	public void	doSplashImageLoading()
	{
		//***** Create a runnable object to do the movie checking
		Runnable do_it = new Runnable()
		{
			public void run()
			{
				try
				{
					loadSplashImage();
				}
				catch (Exception e)
				{
					Settings.main_window.displayMessage("Exception while loading splash image!");
					Settings.main_window.displayMessage(e.getMessage());
				}
			}
		};

		//***** Create a thread for the runnable object to run within
		load_thread = new Thread(do_it);

		//***** Set its priority
		load_thread.setPriority(Thread.MIN_PRIORITY);

		//***** Start the conversion thread running
		load_thread.start();

	}// end of doSplashImageLoading()


	//********************************************************************************
	//*		L O A D  S P L A S H  I M A G E
	//*		Modified 12/10/02 for v1.70
	//*		Modified 7/9/03 for 1.74
	//*		Modified 7/18/03 for 1.75
	//*		Modified 9/22/03 for v1.77
	//********************************************************************************
	public void loadSplashImage() throws Exception
	{
		MediaTracker	mt = null;
		InputStream		is = null;
		byte[]			bytes = null;
		URL				url = null;
		boolean			done = false;

		try
		{

			url = this.getClass().getResource("Images/4DSplash.jpeg");
			if (url != null)
			{
    			//Settings.main_window.displayMessage("" + url);
    			splash_image = Toolkit.getDefaultToolkit().getImage(url);
			}
		}
		catch (Exception e1)
		{
			Settings.main_window.displayMessage("Unable to load splash image!");
			Settings.main_window.displayMessage(e1.getMessage());
		}

		try
		{
			if (splash_image == null)
			{
				url = this.getClass().getResource("4DSplash.jpeg");
				if (url != null)
				{
    				splash_image = Toolkit.getDefaultToolkit().getImage(url);
				}// if we have a URL
			}// if we don't have an image
		}// try
		catch (Exception e2)
		{
			Settings.main_window.displayMessage("Unable to load splash image!");
			Settings.main_window.displayMessage(e2.getMessage());
		}// catch

		try
		{
        	if (splash_image == null)
        	{
      			is = this.getClass().getResourceAsStream("Images/4DSplash.jpeg");
        		if (is != null)
        		{
        			bytes = new byte[is.available()];
        			is.read(bytes);
        			splash_image = Toolkit.getDefaultToolkit().createImage(bytes);

    			}// if we have an input stream
        	}// if the title image wasn't loaded
		}
		catch (Exception e3)
		{
			Settings.main_window.displayMessage("Unable to load splash image!");
			Settings.main_window.displayMessage(e3.getMessage());
		}

		try
		{
       		if (splash_image == null)
        	{
      			is = this.getClass().getResourceAsStream("Images/4DSplash.jpeg");

        		if (is != null)
        		{
        			bytes = new byte[is.available()];
        			is.read(bytes);
        			splash_image = Toolkit.getDefaultToolkit().createImage(bytes);

    			}// if we have an input stream
        	}// if the title image wasn't loaded
		}
		catch (Exception e4)
		{
			Settings.main_window.displayMessage("Unable to load splash image!");
			Settings.main_window.displayMessage(e4.getMessage());
		}

		try
		{
       		if (splash_image != null)
        	{
    			mt = new MediaTracker(this);
    			mt.addImage(splash_image, 0);

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
    				Settings.main_window.displayMessage("Errors were generated during splash image loading!");
    			}

           		this.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
           		Settings.data_display_panel.setSize(SPLASH_WIDTH, SPLASH_HEIGHT + MoviePanel.SLIDER_HEIGHT);

           		drawSplashImage(null);
    		}
    		else
    			throw new Exception("Splash image was not found!");

		}
		catch (Exception e5)
		{
			Settings.main_window.displayMessage("MediaTracker could not load splash image!");
			Settings.main_window.displayMessage(e5.getMessage());
		}

		return;

	}// end of loadSplashImage()


	//********************************************************************************
	//*		D R A W  S P L A S H  I M A G E
	//*		Added 7/9/03 for v1.74
	//*		Modified 7/16/03 for v1.75
	//********************************************************************************
	public void drawSplashImage(Graphics graph) throws Exception
	{

		Graphics g = null;

		if (graph == null)
			g = this.getGraphics();
		else
			g = graph;

		if (g == null || splash_image == null)
			throw new Exception("Unable to draw splash image!");

		this.setSize(SPLASH_WIDTH, SPLASH_HEIGHT);
		g.drawImage(splash_image, 0,0, SPLASH_WIDTH, SPLASH_HEIGHT, this);

		if (graph == null && g != null)
			g.dispose();

		return;

	}// end of drawSplashImage()


	//********************************************************************************
	//*		I M A G E  U P D A T E
	//*		Added 7/17/03 for v1.75
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



}//End of MovieCanvas




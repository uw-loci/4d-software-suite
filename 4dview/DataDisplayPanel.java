import java.awt.*;
import java.awt.event.*;
import java.util.*;
//import java.net.*;



public class DataDisplayPanel extends Panel
{

	final	static	 String		SAMPLE_DATA_SET_URL = "http://www.loci.wisc.edu/4d/java/ViewerApplet/SampleDataSet/";

	Vector				data_array = null;
	ObjectArray			object_array = null;

	BookmarkArray		bookmark_array = null; // holds the bookmark entry objects
	Thread				check_thread = null;// checks the movie for specific occurances

	int					panel_width = -1;
	int					panel_height = -1;


	//******************************************************
	//*		I N I T
	//*		Added 4/22/03 for v1.72
	//*		Modified 7/9/03 for v1.74
	//******************************************************
	public DataDisplayPanel()
	{

		setVisible(true);
		setSize(400,315);
		setLayout(null);

		MyComponentListener component_listener = new MyComponentListener();
		this.addComponentListener(component_listener);

		data_array = new Vector(10,1);

		return;

	}// init()


	//*************************************************************
	//*		A D D  N O T I F Y
	//*		Added 4/22/03 for v1.72
	//*************************************************************
	public void addNotify()
	{
		super.addNotify();

		return;

	}// end of addNotify();

	//********************************************************************************
	//*		U P D A T E
	//*		Added 4/22/03 for v1.72
	//********************************************************************************
	public void update(Graphics g)
	{

		super.update(g);

		return;

	}// end of update()


	//*************************************************************
	//*		P A I N T
	//*		Added 4/22/03 for v1.72
	//*************************************************************
	public void paint(Graphics g)
	{
		super.paint(g);

		g.setColor(Color.black);
		g.drawRect(1, 1, this.getBounds().width -2, this.getBounds().height-2);

		return;

	}// end of paint()


	//*************************************************************
	//*		S E T  S I Z E
	//*		Added 4/22/03 for v1.72
	//*************************************************************
	public void setSize(Dimension d)
	{
		setSize(d.width, d.height);

		return;

	}// end of setSize()


	//*************************************************************
	//*		S E T  S I Z E
	//*		Added 4/22/03 for v1.72
	//*		Modified 5/5/03 for v1.72
	//*************************************************************
	public void setSize(int width, int height)
	{
		super.setSize(width, height);

		if (width <= 1 || height <= 1)
		{
			Settings.main_window.displayMessage("Data Display Panel is illegal size!");
			return;
		}

		panel_width = width;
		panel_height = height;

		return;

	}// end of setSize()


	//*************************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 4/22/03 for v1.72
	//*************************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(panel_width, panel_height));

	}// end of getPreferredSize()


	//**********************************************************************************
	//*		M Y  C O M P O N E N T  L I S T E N E R
	//*		Added 4/22/03 for v1.72
	//**********************************************************************************
	class MyComponentListener extends ComponentAdapter
	{
		public void componentMoved(ComponentEvent event)
		{
			handleLayout();
			return;

		}// end of componentMoved()

		public void componentResized(ComponentEvent event)
		{
			handleLayout();
			return;

		}// end of componentResized()

	}// end of class MyComponentListener


	//**********************************************************************************
	//*		H A N D L E  L A Y O U T
	//*		Added 4/22/03 for v1.72
	//**********************************************************************************
	public void handleLayout()
	{
		return;

	}// end of handleLayout()


	//*************************************************************
	//*		L O A D  D A T A  S E T
	//*		Added 4/22/03 for v1.72
	//*************************************************************
	public void	loadDataSet()
	{
		return;

	}// end of loadDataSet()



	//*************************************************************
	//*		L O A D  S A M P L E  D A T A  S E T
	//*		Added 4/22/03 for v1.72
	//*************************************************************
	public void	loadSampleDataSet()
	{
		return;

	}// end of loadSampleDataSet()


	//**********************************************************************
	//*		G E T  D A T A  D I M E N S I O N S
	//*		Added 4/22/03 for v1.72
	//**********************************************************************
	public void getDataDimensions() throws Exception
	{

		return;

	}// end of getDataDimensions()


	//**********************************************************************
	//*		H A N D L E  N E W  D A T A  S I Z E
	//*		Added 4/22/03 for v1.72
	//**********************************************************************
	protected void handleNewDataSize(int data_width, int data_height)
	{
		return;

	}// end of handleNewDataSize()


	//**********************************************************************
	//*		S E T U P  S L I D E R
	//*		Added 4/22/03 for v1.72
	//**********************************************************************
	protected void setupSlider() throws Exception
	{
		return;

	}// end of setupSlider()

	//*************************************************************************
	//*		G O  T O
	//*		Added 4/22/03 for v1.72
	//*************************************************************************
	public void  goTo(int  plane, long frame)
	{
		return;

	}// end of goTo()

	//*************************************************************************
	//*		M O V E  O N E  F R A M E
	//*		Added 4/22/03 for v1.72
	//*************************************************************************
	public  void  moveOneFrame(boolean forward)
	{
		return;
	}// end of moveOneFrame()



	//*************************************************************************
	//*		M O V E  O N E  P L A N E
	//*		Added 4/22/03 for v1.72
	//*************************************************************************
	public  void  moveOnePlane(boolean move_up)
	{
		return;

	}// end of moveOnePlane()


	//********************************************************************
	//*		G E T  C U R R E N T  F R A M E
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	public long	getCurrentFrame() throws Exception
	{
		return(-1);
	}// end of getCurrentFrame()


	//********************************************************************
	//*		G E T  C U R R E N T  P L A N E
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	public int	getCurrentPlane() throws Exception
	{
		return(-1);
	}// end of getCurrentPlane


	//********************************************************************
	//*		U P D A T E  D A T A  S E T
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	public void  updateDataSet()
	{
		return;

	}// end of udpateDataSet();


	//********************************************************************
	//*		D I S P L A Y  D A T A  S E T  I N F O
	//*		Writes the current frame and plane onto the position panel
	//********************************************************************
	public void  displayDataSetInfo()
	{
		return;

	}// end of displayDataSetInfo();


	//********************************************************************
	//*		P L A Y
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	public void  play(boolean forward) throws Exception
	{
		return;

	}// end of play()


	//**************************************************
	//*		S T O P
	//*		Added 4/22/03 for v1.72
	//**************************************************
	public void	stop() throws Exception
	{
		return;
	}// end of stop()


	//**************************************************
	//*		S A V E  C U R R E N T  F R A M E  A S  T I F F
	//*		Saves as RGB Image
	//*		Added 4/22/03 for v1.72
	//**************************************************
	public	void	saveCurrentFrameAsTiff()
	{
		return;

	}// end of saveCurrentFrameAsTiff();


	//**************************************************************************
	//*		C O P Y  C U R R E N T  F R A M E  T O  E D I T I N G  W I N D O W
	//*		Added 4/22/03 for v1.72
	//**************************************************************************
	public	void	copyCurrentFrameToEditingWindow()
	{

		return;

	}// end of copyCurrentFrameToEditingWindow()


	//**************************************************************************
	//*		G E T  C U R R E N T  F R A M E  A S  I M A G E
	//*		Added 9/10/03 for v1.77
	//**************************************************************************
	public	Image	getCurrentFrameAsImage() throws Exception
	{
		return(null);

	}// end of getCurrentFrameAsImage()


	//***********************************************************************
	//*		C O P Y  O B J E C T S  T O  E D I T I N G  W I N D O W
	//*		Added 4/22/03 for v1.72
	//***********************************************************************
	public void copyObjectsToEditingWindow() throws Exception
	{

		return;

	}// end of copyObjectsToEditingWindow()


	//***********************************************************************
	//*		C R E A T E  C H E C K  T H R E A D
	//*		Creates a low-priority thread which will run in the background
	//*		and check the data set for certain things such as hitting
	//*		the beginning or end so that looping can be done, etc.
	//*		Added 4/22/03 for v1.72
	//***********************************************************************
	public void	createCheckThread()
	{
		//***** Create a runnable object to do the movie checking
		Runnable do_it = new Runnable()
		{
			public void run()
			{
				checkDataSet();
			}
		};

		//***** Create a thread for the runnable object to run within
		check_thread = new Thread(do_it);

		//***** Set its priority
		check_thread.setPriority(Thread.NORM_PRIORITY);

		//***** Start the conversion thread running
		check_thread.start();

	}// end of createCheckThread()



	//***********************************************************************
	//*		C H E C K  D A T A  S E T
	//*		Check the data set for certain things such as hitting
	//*		the beginning or end so that looping can be done, etc.
	//*		Added 4/22/03 for v1.72
	//***********************************************************************
	public void checkDataSet()
	{
		return;

	}// end of checkDataSet()


	//********************************************************************
	//*		C L O S E  D A T A  S E T
	//*		Modified 12/10/02 for v1.70
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	public void closeDataSet() throws Exception
	{

		return;

	}// end of closeDataSet()


	//********************************************************************************
	//*		C L E A R  P A N E L
	//*		Added 4/22/03 for v1.72
	//********************************************************************************
	public void clearPanel()
	{
		Rectangle	r = this.getBounds();
		Graphics	g = this.getGraphics();

		if (g != null && r != null)
		{
			g.setColor(Color.white);
			g.fillRect(r.x, r.y, r.width, r.height);

			g.dispose();
		}

		return;

	}// end of clearPanel()


	//********************************************************************
	//*		I N I T  P A N E L  V A R I A B L E S
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	protected void initPanelVariables()
	{

	    Settings.loadSettings(true);

		if (EditingSettings.header_array == null)
			EditingSettings.header_array = new HeaderArray();

		this.bookmark_array = new BookmarkArray();
		this.check_thread = null;

		return;

	}// end of initPanelVariables()



	//********************************************************************
	//*		R E A D  O V E R L A Y
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	protected void readOverlay()
	{
		return;
	}// end of readOverlay()


	//********************************************************************
	//*		R E A D  O V E R L A Y  H E A D E R S
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	protected void readOverlayHeaders()
	{
		return;

	}// end of readOverlayHeaders()


	//********************************************************************
	//*		D I S P L A Y  M E S S A G E
	//*		Added 4/22/03 for v1.72
	//********************************************************************
	protected void displayMessage(String msg)
	{
		if (Settings.main_window != null)
			Settings.main_window.displayMessage(msg);

		return;

	}// end of displayMessage()


}// end of DataDisplayPanel class


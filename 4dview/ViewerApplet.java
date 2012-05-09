import java.awt.*;
import java.applet.*;

import quicktime.*;


/** This is an applet shell that runs 4D Viewer/Java. */

public class ViewerApplet extends java.applet.Applet
{

	private		static  ViewerApplet va = null;


    public void init()
    {
		va = this;

		try
		{
			QTSession.open();
			MainWindow mw = new MainWindow();
			mw.setVisible(true);// show the window
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }

	public static ViewerApplet getInstance()
	{
		return(va);

	}// end of getInstance();


	public void dispose()
	{
		QTSession.close();
	}

}// end of class ViewerApplet

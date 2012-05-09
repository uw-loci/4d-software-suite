import java.awt.*;
import java.lang.*;
import java.io.*;

// AnnotationObject will be extended to all other types of overlay objects
// Contains those elements that all objects have in common

//**************************************************************************
//*		A N N O T A T I O N  O B J E C T
//**************************************************************************
public class AnnotationObject
{

	//***** OBJECT CONSTANTS
	final		static	int			NO_OBJECT = 0;
	final		static	int			SQUARE_OBJECT = 1;
	final		static	int			OUTLINE_OBJECT = 2;
	final		static	int			LINE_OBJECT = 3;
	final		static	int			CIRCLE_OBJECT = 4;
	final		static	int			TEXT_OBJECT = 5;
	final		static	int			ARROW_OBJECT = 6;
	final		static	int			MEASURING_LINE_OBJECT = 7;

	//***** COLOR CONSTANTS
	final		static	int			BLACK = 0;
	final		static	int			BLUE = 1;
	final		static	int			CYAN = 2;
	final		static	int			DARK_GRAY = 3;
	final		static	int			GRAY = 4;
	final		static	int			GREEN = 5;
	final		static	int			LIGHT_GRAY = 6;
	final		static	int			MAGENTA = 7;
	final		static	int			ORANGE = 8;
	final		static	int			PINK = 9;
	final		static	int			RED = 10;
	final		static	int			WHITE = 11;
	final		static	int			YELLOW = 12;

	//***** RESIZE RECT CONSTANTS
	final		static	int			RESIZE_RECT_WIDTH = 4;
	final		static	int			RESIZE_RECT_HEIGHT = 4;

	protected	String			structure_descriptor = null;
	protected	String			object_comments = null;
	protected	Rectangle		object_rect = null;
	protected	int				object_type = 0;
	protected	Color			object_color = null;
	protected	Point			last_click = null; // starting point for dragging operations
	protected	Point			first_click = null; // starting point to see if we're changing select mode
	protected	boolean			selected = false; // is the object selected?
	protected	boolean			dirty = false;// does the object need to be redrawn?
	protected	boolean			finished = false; // is the object in the process of being drawn or edited?
	protected	boolean			old_object = false; // true if the object was read from the disk

	//***** Constructor method
	public AnnotationObject()
	{
		this.structure_descriptor = new String("" + System.currentTimeMillis());// default is just the time in milliseconds 'cause it's always unique
		this.object_comments = new String();
		this.object_rect = new Rectangle(0,0,0,0);
		this.object_type = 0;
		this.object_color = Color.blue;
		this.last_click = new Point(0,0);// this is used for dragging
		this.first_click = new Point(0,0); // where the intial click to create the object occurred
	 	this.selected = false;// selected objects flash, and in some cases, have resize rects
	 	this.dirty = false; // dirty objects need to be redrawn
	 	this.finished = false;// when the object is done being created, it's finished
	 	this.old_object = false; // old objects are ones read from an overlay file

	}// end of init()


	//************************************************
	//*		D R A W  O B J E C T
	//************************************************
	public void drawObject(Graphics g){}


	//************************************************
	//*		D R A G  O B J E C T
	//************************************************
	public void dragObject(EditingCanvas ec, int x, int y){}


	//**************************************************************
	//*		C L I C K  I N  O B J E C T
	//*		Returns true if the click was in the object's boundary
	//**************************************************************
	public boolean clickInObject(int x, int y)
	{
		return(false);
	}


	//************************************************
	//*		R E S I Z E  O B J E C T
	//************************************************
	public void resizeObject(EditingCanvas ec, int x, int y){}


	//******************************************************
	//*		H A N D L E  C L I C K  O N  O B J E C T
	//*		Handles mouse down events in completed objects
	//******************************************************
	public void handleClickOnObject(int x, int y){}


	//************************************************
	//*		C L I C K  I N  R E S I Z E  R E C T
	//************************************************
	public boolean clickInResizeRect(int x, int y)
	{
		return(false);
	}

	//************************************************
	//*		E D I T  O B J E C T
	//************************************************
	public void editObject(Graphics g, int x, int y){}


	//************************************************
	//*		F I N I S H  O B J E C T
	//************************************************
	public void finishObject(Graphics g, int x, int y){}


	//*******************************************************************
	//*		I S  V A L I D  O B J E C T
	//*		Retuns false if object fails tests for validity
	//*******************************************************************
	public boolean  isValidObject()
	{
		return(true);

	}// end of isValidObject()


	//************************************************************
	//*		C H E C K  S E L E C T
	//* 	Checks to see if select status needs to be changed
	//*		Returns TRUE if the status was changed
	//************************************************************
	public boolean checkSelect(EditingCanvas ec, int x, int y)
	{
		return(false);
	}

	//************************************************
	//*		F L A S H  O B J E C T
	//************************************************
	public void flashObject(Graphics g, boolean on){}




	//*********************************************************
	//*		G E T  S T R U C T U R E  D E S C R I P T O R
	//*********************************************************
	public String	 getStructureDescriptor()
	{
			return(new String(this.structure_descriptor));
	}// end of getStructureDescriptor()


	//*********************************************************
	//*		S E T  S T R U C T U R E  D E S C R I P T O R
	//*********************************************************
	public void  setStructureDescriptor(String  sd)
	{
			this.structure_descriptor = new String(sd);
	}// end of setStructureDescriptor


	//*********************************************************
	//*		 G E T  O B J E C T  C O M M E N T S
	//*********************************************************
	public String	 getObjectComments()
	{
			return(new String(this.object_comments));
	}// end of getObjectComments()


	//*********************************************************
	//*		 S E T  O B J E C T  C O M M E N T S
	//*********************************************************
	public void  setObjectComments(String  oc)
	{
			this.object_comments = new String(oc);
	}// end of setObjectComments


	//*********************************************************
	//*		G E T  O B J E C T  R E C T
	//*********************************************************
	public Rectangle  getObjectRect()
	{
			return(new Rectangle(this.object_rect));
	}// end of getObjectRect()


	//*********************************************************
	//*		S E T  O B J E C T  R E C T
	//*********************************************************
	 public void	  setObjectRect(Rectangle obj_rect)
	 {
	 	this.object_rect.x = obj_rect.x;
	 	this.object_rect.y = obj_rect.y;
	 	this.object_rect.width = obj_rect.width;
	 	this.object_rect.height = obj_rect.height;
	 }// end of setObjectRect()


	//*********************************************************
	//*		S E T  O B J E C T  R E C T
	//*********************************************************
	 public void	  setObjectRect(int x, int y, int width, int height)
	 {
	 	this.object_rect.x = x;
	 	this.object_rect.y = y;
	 	this.object_rect.width = width;
	 	this.object_rect.height = height;
	 }// end of setObjectRect()


	//*********************************************************
	//*		 G E T  O B J E C T  T Y P E
	//*********************************************************
	public int	 getObjectType()
	{
		return(this.object_type);
	}// end of getObjectType()


	//*********************************************************
	//*		 S E T  O B J E C T  T Y P E
	//*********************************************************
	public void  setObjectType(int type)
	{
		this.object_type = type;
	}// end of setObjectType


	//*********************************************************
	//*		 G E T  O B J E C T  C O L O R
	//*********************************************************
	public Color	 getObjectColor()
	{
		return(this.object_color);
	}// end of getObjectType()


	//*********************************************************
	//*		 S E T  O B J E C T  C O L O R
	//*********************************************************
	public void  setObjectColor(Color clr)
	{
		this.object_color = clr;
	}// end of setObjectColor


	//*********************************************************
	//*		 G E T  L A S T  C L I C K
	//*********************************************************
	public Point	 getLastClick()
	{
		return(new Point(this.last_click));
	}// end of getLastClick()


	//*********************************************************
	//*		 S E T  L A S T  C L I C K
	//*********************************************************
	public void	 setLastClick(Point pt)
	{
		this.last_click.x = pt.x;
		this.last_click.y = pt.y;
	}// end of getLastClick()


	//*********************************************************
	//*		 G E T  F I R S T  C L I C K
	//*********************************************************
	public Point	 getFirstClick()
	{
		return(new Point(this.first_click));
	}// end of getLastClick()


	//*********************************************************
	//*		 S E T   F I R S T  C L I C K
	//*********************************************************
	public void	 setFirstClick(Point pt)
	{
		this.first_click.x = pt.x;
		this.first_click.y = pt.y;
	}// end of setFirstClick()


	//*********************************************************
	//*		 G E T  S E L E C T E D
	//*********************************************************
	public boolean	 getSelected()
	{
		return(this.selected);
	}// end of getSelected()


	//*********************************************************
	//*		 S E T   S E L E C T E D
	//*********************************************************
	public void	 setSelected(boolean selected)
	{
		this.selected = selected;
	}// end of setSelected()


	//*********************************************************
	//*		 G E T  D I R T Y
	//*********************************************************
	public boolean	 getDirty()
	{
		return(this.dirty);
	}// end of getDirty()


	//*********************************************************
	//*		 S E T   D I R T Y
	//*********************************************************
	public void	 setDirty(boolean dirty)
	{
		this.dirty = dirty;
	}// end of setDirty()


	//*********************************************************
	//*		 G E T  F I N I S H E D
	//*********************************************************
	public boolean	 getFinished()
	{
		return(this.finished);
	}// end of getFinished()


	//*********************************************************
	//*		 S E T   F I N I S H E D
	//*********************************************************
	public void	 setFinished(boolean finished)
	{
		this.finished = finished;
	}// end of setFinished()


	//*********************************************************
	//*		 G E T  O L D  O B J E C T
	//*********************************************************
	public boolean	 getOldObject()
	{
		return(this.old_object);
	}// end of getOldObject()


	//*********************************************************
	//*		 S E T   O L D  O B J E C T
	//*********************************************************
	public void	 setOldObject(boolean old)
	{
		this.old_object = old;
	}// end of setOldObject()


	//*********************************************************************
	//*		G E T  O B J E C T  O P T I O N S
	//*		Modified 8/7/02 for v1.60
	//*********************************************************************
	public void getObjectOptions(Frame parent) throws Exception
	{
		return;

	}// end of getObjectOptions()


	//*********************************************************************
	//*		W R I T E  O B J E C T  T O  S T R E A M
	//*********************************************************************
	public void writeObjectToStream(ObjectOutputStream  stream) throws Exception
	{
		AnnotationObjectProxy	aop = null;

		aop = getProxy();

		if (aop != null)
			aop.writeObjectToStream(stream);

		return;

	}// end of writeObjectToBuffer()


	//*********************************************************************
	//*		G E T  P R O X Y
	//*********************************************************************
	public AnnotationObjectProxy	getProxy()
	{
		return(null);
	}// end of getProxy()


	//************************************************
	//*   I S  I D E N T I C A L  O B J E C T
	//************************************************
	public boolean	isIdenticalObject(AnnotationObject test_obj)
	{

		if (!(object_rect.equals(test_obj.object_rect)) ||
			  object_type != test_obj.object_type ||
			  object_color != test_obj.object_color)
			return(false);

		return(true);

	}// end of isIdenticalObject()


}// end of class AnnotationObject

import java.awt.*;
import java.lang.*;
import java.util.*;
import java.awt.event.*;


public class DataSlider extends java.awt.Canvas
{


	//***** SLIDER CONSTANTS
	final		static	int			SLIDER_HEIGHT = 15;
	final		static	int			THUMB_WIDTH = 10;
	final		static	int			THUMB = 0;
	final		static	int			PAGE_UP = 1;
	final		static	int			PAGE_DOWN = 2;
	final		static	int			NOT_FOUND = -1;

	DataDisplayPanel	data_panel = null;
	int					block_increment = 0, page_increment = 0,unit_increment = 0;
	int					thumb_width = 0;
	int					maximum = 0, minimum = 0, cur_value = 0;
	int					orientation = 0;
	Rectangle			thumb_rect = null;
	Rectangle			divider_rect = null;
	Rectangle			page_up_rect = null;
	Rectangle			page_down_rect = null;
	Point				last_click = null;// used for dragging events
	boolean				dragging = false;

	//*************************************************
	//*		I N I T
	//*		Modified 10/16/02 for v1.66
	//*************************************************
	public DataSlider(DataDisplayPanel data_panel)
	{
		setBackground(new Color(16777215));

		this.data_panel = data_panel;
		this.block_increment = 10;
		this.page_increment = 10;
		this.unit_increment = 1;
		this.thumb_width = THUMB_WIDTH;
		this.minimum = 0;
		this.maximum = 100;
		this.cur_value = 0;
		this.orientation = Scrollbar.HORIZONTAL;
		this.last_click = new Point(0, 0);
		this.dragging = false;

		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

		setVisible(true);

		return;

	}// end of init()


	//*************************************************
	//*		I N I T
	//*		Modified 10/16/02 for v1.66
	//*************************************************
	public DataSlider(MoviePanel movie_panel, int min, int max, int cur_value)
	{

		this(movie_panel);

		this.minimum = min;
		this.maximum = max;
		this.cur_value = cur_value;

		return;

	}// end of init()


	public int getBlockIncrement()
	{
		return(this.block_increment);

	}// end of getBlockIncrement()


	public void setBlockIncrement(int value)
	{
		if (value >= 0)
		{
			this.block_increment = value;
			this.page_increment = value;
		}

		return;

	}// end of setBlockIncrement()

	public int getMaximum()
	{
		return(this.maximum);

	}// end of getMaximum()

	public void setMaximum(int value)
	{
		this.maximum = value;

		return;

	}// end of setMaximum()

	public int getMinimum()
	{
		return(this.minimum);

	}// end of getMinimum()

	public void setMinimum(int value)
	{
		this.minimum = value;

		return;

	}// end of setMinimum()

	public int getOrientation()
	{
		return(this.orientation);

	}// end of getOrientation()

	public void setOrientation(int value)
	{
		if (value == Scrollbar.HORIZONTAL)
			this.orientation = value;

	}// end of setOrientation()

	public int getPageIncrement()
	{
		return(this.page_increment);

	}// end of getPageIncrement()

	public void setPageIncrement(int value)
	{
		if (value >= 0)
		{
			this.block_increment = value;
			this.page_increment = value;
		}

		return;

	}// end of setPageIncrement()


	public int getUnitIncrement()
	{
		return(this.unit_increment);

	}// end of getUnitIncrement()

	public void setUnitIncrement(int value)
	{
		if (value >= 0)
			this.unit_increment = value;

		return;

	}// end of setUnitIncrement()


	public int getValue()
	{
		return(this.cur_value);

	}// end of getValue()


	public void setValue(int value)
	{
		if (value >= 0)
			this.cur_value = value;

		repaint();

		return;

	}// end of setValue()


	public void update(Graphics g)
	{
		super.update(g);
		drawTheSlider(g);

		return;
	}// end of update


	public void paint(Graphics g)
	{
		super.paint(g);
		drawTheSlider(g);

		return;

	}// end of paint()


	//*****************************************************************
	//*		D R A W  T H E  S L I D E R
	//*		Modified 10/16/02 for v1.66
	//*****************************************************************
	public void drawTheSlider(Graphics graph)
	{
		Graphics 	g = null;
		Point		start_pt = new Point(0,0);
		Point		end_pt = new Point (0,0);

		try
		{
			//***** Get a graphics context
			if (graph != null)
				g = graph;
			else
				g = this.getGraphics();

			if (g == null)
				return;

			//***** Define all the drawing rects
			setUpRects();

			//***** Erase old drawing
			g.setColor(Color.black);
			g.clearRect(0,0, this.getSize().width, this.getSize().height);

			//***** Draw a line down the middle
			g.setColor(Color.black);
			g.fillRect(divider_rect.x, divider_rect.y, divider_rect.width, divider_rect.height);

			//***** Draw the thumb
			g.clearRect(thumb_rect.x, thumb_rect.y, thumb_rect.width, thumb_rect.height);
			g.drawRect(thumb_rect.x, thumb_rect.y, thumb_rect.width, thumb_rect.height);
			g.setColor(Color.lightGray);
			g.fillRect(thumb_rect.x + 1, thumb_rect.y, thumb_rect.width - 1, thumb_rect.height);
			g.setColor(Color.black);

			//***** Outline the slider
			g.drawRect(1, 1, this.getSize().width - 2, this.getSize().height - 2);

			//***** Get rid of the graphics context if we made it internally
			if (graph == null)
				g.dispose();

		}
		catch (Exception e) {}

		return;

	}// end of drawTheSlider


	//*****************************************************************
	//*		S E T  U P  R E C T S
	//*		Modified 10/16/02 for v1.66
	//*****************************************************************
	public void setUpRects()
	{
		int			x = 0, pixels_of_width = 0, units_in_scale = 0;
		double		pixels_per_unit = 0.0;

		pixels_of_width = this.getSize().width - THUMB_WIDTH;
		units_in_scale = (this.maximum - this.minimum) + 1;
		pixels_per_unit = (double)((double)pixels_of_width/(double)units_in_scale);

		//***** Define the thumb_rect
		x = (int)(this.cur_value * pixels_per_unit);

		thumb_rect = new Rectangle(x, 1, this.thumb_width, this.getSize().height - 2);

		//**** Define the divider rect
		divider_rect = new Rectangle(0, this.getSize().height/2, this.getSize().width, 2);


		//***** Define the page_up_rect
		page_up_rect = new Rectangle(thumb_rect.x + thumb_rect.width + 1,
									 0,
									 this.getSize().width - (thumb_rect.x + thumb_rect.width + 1),
									 this.getSize().height);

		//***** Define the page_down_rect
		page_down_rect = new Rectangle(0,
									 0,
									 (thumb_rect.x - 1),
									 this.getSize().height);

		return;

	}// end of setUpRects()


	//*****************************************************************
	//*		P R O C E S S  M O U S E  E V E N T
	//*****************************************************************
	protected void processMouseEvent(MouseEvent e)
	{
		Object object = e.getSource();

		switch (e.getID())
		{
			case MouseEvent.MOUSE_CLICKED:
				 break;
			case MouseEvent.MOUSE_ENTERED:
				 break;
			case MouseEvent.MOUSE_EXITED:
				 break;
			case MouseEvent.MOUSE_PRESSED:
				 DataSlider_MousePressed(e);
				 break;
			case MouseEvent.MOUSE_RELEASED:
				 DataSlider_MouseReleased(e);
				 break;
		}// end of switch

	}// end of processMouseEvent()



	//*****************************************************************
	//*		P R O C E S S  M O U S E  M O T I O N  E V E N T
	//*****************************************************************
	protected void processMouseMotionEvent(MouseEvent e)
	{
		Object object = e.getSource();

		switch (e.getID())
		{
			case MouseEvent.MOUSE_MOVED:
				 break;
			case MouseEvent.MOUSE_DRAGGED:
				 DataSlider_MouseDragged(e);
				 break;
		}// end of switch

	}// end of processMouseEvent()


	//*****************************************************************
	//*		M O U S E  P R E S S E D
	//*****************************************************************
	public void DataSlider_MousePressed(MouseEvent e)
	{
		int		where = NOT_FOUND;

		where = findClickArea(e.getX(), e.getY());

		switch (where)
		{
			case THUMB:
				 this.last_click.x = e.getX();
				 this.last_click.y = e.getY();
				 this.dragging = true;
				 break;
			case PAGE_UP:
				 this.cur_value += this.page_increment;
				 if (cur_value > this.maximum)
				 	this.cur_value = maximum;
				 repaint();

				 //***** Move the movie
				 data_panel.goTo(DataInfo.cur_focal_plane, this.cur_value);
				 break;
			case PAGE_DOWN:
				 this.cur_value -= this.page_increment;
				 if (cur_value < this.minimum)
				 	this.cur_value = minimum;
				 repaint();

				 //***** Move the movie
				 data_panel.goTo(DataInfo.cur_focal_plane, this.cur_value);
				 break;
		}// switch

		return;

	}// end of mousePressed()


	//*****************************************************************
	//*		M O U S E  D R A G G E D
	//*****************************************************************
	public void DataSlider_MouseDragged(MouseEvent e)
	{
		int			x = 0;
		int			pixels_of_width = 0, units_in_scale = 0;
		double		pixels_per_unit = 0.0;


		if (this.dragging)
		{
			x = e.getX();
			if (x < 0)
					x = 0;
			if (x > this.getSize().width - THUMB_WIDTH)
				 x = this.getSize().width - THUMB_WIDTH;

			pixels_of_width = this.getSize().width - THUMB_WIDTH;
			units_in_scale = (this.maximum - this.minimum) + 1;
			pixels_per_unit = (double)((double)pixels_of_width/(double)units_in_scale);
			this.cur_value = (int)(x/pixels_per_unit);

			if (cur_value < this.minimum)
					this.cur_value = minimum;
			if (cur_value > this.maximum)
				 this.cur_value = maximum;
			this.last_click.x = x;

			repaint();
		}// if we're dragging

		repaint();

		return;

	}// end of mouseDragged()


	//*****************************************************************
	//*		M O U S E  R E L E A S E D
	//*****************************************************************
	public void DataSlider_MouseReleased(MouseEvent e)
	{
		int			where = NOT_FOUND;
		int			x = 0;
		int			pixels_of_width = 0, units_in_scale = 0;
		double		pixels_per_unit = 0.0;


		if (this.dragging)
		{
			x = e.getX();
			if (x < 0)
				x = 0;
			if (x > this.getSize().width - THUMB_WIDTH)
				x = this.getSize().width - THUMB_WIDTH;

			pixels_of_width = this.getSize().width - THUMB_WIDTH;
			units_in_scale = (this.maximum - this.minimum) + 1;
			pixels_per_unit = (double)((double)pixels_of_width/(double)units_in_scale);
			this.cur_value = (int)(x/pixels_per_unit);

			if (this.cur_value < this.minimum)
				this.cur_value = this.minimum;
			if (this.cur_value > this.maximum)
				this.cur_value = this.maximum;

			repaint();

			//***** Move the movie
			data_panel.goTo(DataInfo.cur_focal_plane, this.cur_value);

			this.dragging = false;

		}// if we're dragging


		return;

	}// end of mouseReleased()


	//*****************************************************************
	//*		F I N D  C L I C K  A R E A
	//*****************************************************************
	public int findClickArea(int x, int y)
	{
		if (this.page_up_rect.contains(x, y))
			return(PAGE_UP);
		else if (this.page_down_rect.contains(x, y))
			return(PAGE_DOWN);
		else
			return(THUMB);

	}// end of findClickArea()


}// end of class DataSlider


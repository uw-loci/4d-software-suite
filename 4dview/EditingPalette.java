import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class EditingPalette extends ToolPalette
{
	final	static	int		PREFERRED_WIDTH = 2 * BUTTON_WIDTH;
	final	static	int		PREFERRED_HEIGHT = 5 * BUTTON_HEIGHT;


	//***** EDITING TOOL CONSTANTS
	final		static 	int	SELECT_TOOL = 0;
	final		static 	int	SQUARE_TOOL = 1;
	final 		static 	int	PENCIL_TOOL = 2;
	final		static 	int	LINE_TOOL = 3;
	final		static 	int	CIRCLE_TOOL = 4;
	final		static 	int	TEXT_TOOL = 5;
	final		static 	int	ARROW_TOOL = 6;
	final		static 	int	MEASURE_TOOL = 7;
	final		static 	int	HAND_TOOL = 8;
	final		static 	int	ROI_TOOL = 9;
	final		static 	int	NOT_USED_TOOL = 10;

	ImageButton 		select_tool_button = null;
	ImageButton 		square_tool_button = null;
	ImageButton 		pencil_tool_button = null;
	ImageButton 		circle_tool_button = null;
	ImageButton 		text_tool_button = null;
	ImageButton 		line_tool_button = null;
	ImageButton 		arrow_tool_button = null;
	ImageButton			measure_tool_button = null;
	ImageButton 		save_overlay_button = null;
	ImageButton 		erase_overlay_button = null;

	Image				select_img = null, select_img_invert = null;
	Image				square_img = null, square_img_invert = null;
	Image				pencil_img = null, pencil_img_invert = null;
	Image				circle_img = null, circle_img_invert = null;
	Image				text_img = null, text_img_invert = null;
	Image				line_img = null, line_img_invert = null;
	Image				arrow_img = null, arrow_img_invert = null;
	Image				measure_img = null, measure_img_invert = null;
	Image				save_img = null, erase_img = null;

	//*****************************************************
	//*		I N I T
	//*		Modified 10/22/02 for v1.68
	//*****************************************************
	public EditingPalette()
	{
		super();

		setVisible(true);
		setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);

		//***** Load the button images
		loadButtonImages();

		//*** Add tool buttons to window
		select_tool_button = new ImageButton();
		select_tool_button.setImage(select_img);
		select_tool_button.setSize(BUTTON_WIDTH, BUTTON_WIDTH);
		add(select_tool_button);
		setGBLConstraints(select_tool_button, 0, 0);

		pencil_tool_button = new ImageButton();
		pencil_tool_button.setImage(pencil_img);
		pencil_tool_button.setSize(BUTTON_WIDTH,BUTTON_WIDTH);
		add(pencil_tool_button);
		setGBLConstraints(pencil_tool_button, 1, 0);

		square_tool_button = new ImageButton();
		square_tool_button.setImage(square_img);
		square_tool_button.setSize(BUTTON_WIDTH,BUTTON_WIDTH);
		add(square_tool_button);
		setGBLConstraints(square_tool_button, 0, 1);

		circle_tool_button = new ImageButton();
		circle_tool_button.setImage(circle_img);
		circle_tool_button.setSize(BUTTON_WIDTH,BUTTON_WIDTH);
		add(circle_tool_button);
		setGBLConstraints(circle_tool_button, 1, 1);

		text_tool_button = new ImageButton();
		text_tool_button.setImage(text_img);
		text_tool_button.setSize(BUTTON_WIDTH,BUTTON_WIDTH);
		add(text_tool_button);
		setGBLConstraints(text_tool_button, 0, 2);

		line_tool_button = new ImageButton();
		line_tool_button.setImage(line_img);
		line_tool_button.setSize(BUTTON_WIDTH,BUTTON_WIDTH);
		add(line_tool_button);
		setGBLConstraints(line_tool_button, 1, 2);

		arrow_tool_button = new ImageButton();
		arrow_tool_button.setImage(arrow_img);
		arrow_tool_button.setSize(BUTTON_WIDTH,BUTTON_WIDTH);
		add(arrow_tool_button);
		setGBLConstraints(arrow_tool_button, 0, 3);

		measure_tool_button = new ImageButton();
		measure_tool_button.setImage(save_img);
		measure_tool_button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		add(measure_tool_button);
		setGBLConstraints(measure_tool_button, 1, 3);

		erase_overlay_button = new ImageButton();
		erase_overlay_button.setImage(erase_img);
		erase_overlay_button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		add(erase_overlay_button);
		setGBLConstraints(erase_overlay_button, 0, 4);

		save_overlay_button = new ImageButton();
		save_overlay_button.setImage(save_img);
		save_overlay_button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
		add(save_overlay_button);
		setGBLConstraints(save_overlay_button, 1, 4);

		resetPaletteButtons();

		//****** Add mouse listeners
		if (mouse_adapter != null)
		{
			select_tool_button.addMouseListener(mouse_adapter);
			square_tool_button.addMouseListener(mouse_adapter);
			circle_tool_button.addMouseListener(mouse_adapter);
			pencil_tool_button.addMouseListener(mouse_adapter);
			text_tool_button.addMouseListener(mouse_adapter);
			line_tool_button.addMouseListener(mouse_adapter);
			arrow_tool_button.addMouseListener(mouse_adapter);
			measure_tool_button.addMouseListener(mouse_adapter);
			save_overlay_button.addMouseListener(mouse_adapter);
			erase_overlay_button.addMouseListener(mouse_adapter);
		}

		return;

	}// init


	//*******************************************************
	//*		G E T  P R E F E R R E D  S I Z E
	//*		Added 10/15/02 for v1.66
	//*******************************************************
	public Dimension getPreferredSize()
	{
		super.getPreferredSize();

		return(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

	}// end of getPreferredSize()


	//**************************************************************************
	//*		U P D A T E
	//*		Modified 7/23/02 for v1.59
	//**************************************************************************
	public void update(Graphics g)
	{
		super.update(g);

		resetPaletteButtons();

		return;
	}

	//**************************************************************************
	//*		L O A D  B U T T O N  I M A G E S
	//*		Tries loading as url first, if that doesn't work
	//*		tries loading as resource stream
	//**************************************************************************
	public void loadButtonImages()
	{
		InputStream		is = null;
		byte[]			bytes = null;
		URL				url = null;

		try
		{
			//***** select_img
			url = getClass().getResource("Images/Select_tool.jpeg");
			if (url != null)
			{
    				select_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (select_img == null)
			{
				is = getClass().getResourceAsStream("Images/Select_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
   		 		select_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(select_img);

			//***** select_img_invert
			url = getClass().getResource("Images/Select_tool_invert.jpeg");
			if (url != null)
			{
    				select_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (select_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Select_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				select_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(select_img_invert);

			//***** square_img
			url = getClass().getResource("Images/Square_tool.jpeg");
			if (url != null)
			{
    				square_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (square_img == null)
			{
				is = getClass().getResourceAsStream("Images/Square_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				square_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(square_img);

			//***** square_img_invert
			url = getClass().getResource("Images/Square_tool_invert.jpeg");
			if (url != null)
			{
    				square_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (square_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Square_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				square_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(square_img_invert);

			//***** pencil_img
			url = getClass().getResource("Images/Pencil_tool.jpeg");
			if (url != null)
			{
    				pencil_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (pencil_img == null)
			{
				is = getClass().getResourceAsStream("Images/Pencil_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				pencil_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(pencil_img);

			//***** pencil_img_invert
			url = getClass().getResource("Images/Pencil_tool_invert.jpeg");
			if (url != null)
			{
    				pencil_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (pencil_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Pencil_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				pencil_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(pencil_img_invert);

			//***** circle_img
			url = getClass().getResource("Images/Circle_tool.jpeg");
			if (url != null)
			{
    				circle_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (circle_img == null)
			{
				is = getClass().getResourceAsStream("Images/Circle_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				circle_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(circle_img);

			//***** circle_img_invert
			url = getClass().getResource("Images/Circle_tool_invert.jpeg");
			if (url != null)
			{
    				circle_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (circle_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Circle_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				circle_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(circle_img_invert);

			//***** text_img
			url = getClass().getResource("Images/Text_tool.jpeg");
			if (url != null)
			{
    				text_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (text_img == null)
			{
				is = getClass().getResourceAsStream("Images/Text_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				text_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(text_img);

			//***** text_img_invert
			url = getClass().getResource("Images/Text_tool_invert.jpeg");
			if (url != null)
			{
    				text_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (text_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Text_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				text_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(text_img_invert);

			//***** line_img
			url = getClass().getResource("Images/Line_tool.jpeg");
			if (url != null)
			{
    				line_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (line_img == null)
			{
				is = getClass().getResourceAsStream("Images/Line_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				line_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(line_img);

			//***** line_img_invert
			url = getClass().getResource("Images/Line_tool_invert.jpeg");
			if (url != null)
			{
    				line_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (line_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Line_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				line_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(line_img_invert);

			//***** arrow_img
			url = getClass().getResource("Images/Arrow_tool.jpeg");
			if (url != null)
			{
    				arrow_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (arrow_img == null)
			{
				is = getClass().getResourceAsStream("Images/Arrow_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				arrow_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(arrow_img);

			//***** arrow_img_invert
			url = getClass().getResource("Images/Arrow_tool_invert.jpeg");
			if (url != null)
			{
    				arrow_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (arrow_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Arrow_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				arrow_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(arrow_img_invert);

			//***** measure_img
			url = getClass().getResource("Images/Measure_tool.jpeg");
			if (url != null)
			{
    				measure_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (measure_img == null)
			{
				is = getClass().getResourceAsStream("Images/Measure_tool.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				measure_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(measure_img);

			//***** measure_img_invert
			url = getClass().getResource("Images/Measure_tool_invert.jpeg");
			if (url != null)
			{
    				measure_img_invert = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (measure_img_invert == null)
			{
				is = getClass().getResourceAsStream("Images/Measure_tool_invert.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				measure_img_invert = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(measure_img_invert);

			//***** save_img
			url = getClass().getResource("Images/Save_overlay.jpeg");
			if (url != null)
			{
    				save_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (save_img == null)
			{
				is = getClass().getResourceAsStream("Images/Save_overlay.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				save_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(save_img);

			//***** erase_img
			url = getClass().getResource("Images/Erase_overlay.jpeg");
			if (url != null)
			{
    				erase_img = Toolkit.getDefaultToolkit().createImage((ImageProducer) url.getContent());
			}// try with url
			if (erase_img == null)
			{
				is = getClass().getResourceAsStream("Images/Erase_overlay.jpeg");
        				bytes = new byte[is.available()];
        				is.read(bytes);
				erase_img = Toolkit.getDefaultToolkit().createImage(bytes);
			}// try with getResourceAsStream

			confirmImageLoading(erase_img);

		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown in EditingCanvas.loadButtonImages()!");
		}

		return;

	}// loadButtonImages()


	//*****************************************************
	//*		R E S E T  P A L E T T E  B U T T O N S
	//*		Modified 7/19/02 for v1.59
	//*****************************************************
	public void	resetPaletteButtons()
	{
		try
		{
			select_tool_button.setImage(select_img);
			circle_tool_button.setImage(circle_img);
			square_tool_button.setImage(square_img);
			pencil_tool_button.setImage(pencil_img);
			text_tool_button.setImage(text_img);
			line_tool_button.setImage(line_img);
			arrow_tool_button.setImage(arrow_img);
			measure_tool_button.setImage(measure_img);
			save_overlay_button.setImage(save_img);
			erase_overlay_button.setImage(erase_img);

			switch (EditingSettings.tool_selected)
			{
				case SELECT_TOOL:
					  select_tool_button.setImage(select_img_invert);
					  break;
				case CIRCLE_TOOL:
					  circle_tool_button.setImage(circle_img_invert);
					  break;
				case SQUARE_TOOL:
					  square_tool_button.setImage(square_img_invert);
					  break;
				case PENCIL_TOOL:
					  pencil_tool_button.setImage(pencil_img_invert);
					  break;
				case TEXT_TOOL:
					  text_tool_button.setImage(text_img_invert);
					  break;
				case LINE_TOOL:
					  line_tool_button.setImage(line_img_invert);
					  break;
				case ARROW_TOOL:
					  arrow_tool_button.setImage(arrow_img_invert);
					  break;
				case MEASURE_TOOL:
					  measure_tool_button.setImage(measure_img_invert);
					  break;
			}// end of switch
		}// try
		catch(Exception e) {}

	}// end of resetPaletteButtons()


	protected void doMouseClicked(MouseEvent event)
	{
		Object object = event.getSource();
		if (object == select_tool_button)
			selectToolbutton_mouseClicked(event);
		else if (object == pencil_tool_button)
			pencilToolbutton_mouseClicked(event);
		else if (object == circle_tool_button)
			circleToolbutton_mouseClicked(event);
		else if (object == square_tool_button)
			squareToolbutton_mouseClicked(event);
		else if (object == text_tool_button)
			textToolbutton_mouseClicked(event);
		else if (object == line_tool_button)
			lineToolbutton_mouseClicked(event);
		else if (object == arrow_tool_button)
			arrowToolbutton_mouseClicked(event);
		else if (object == measure_tool_button)
			measureToolbutton_mouseClicked(event);
		else if (object == save_overlay_button)
			saveOverlayButton_mouseClicked(event);
		else if (object == erase_overlay_button)
			eraseOverlayButton_mouseClicked(event);

	}// end of doMouseClicked()


	void selectToolbutton_mouseClicked(MouseEvent event)
	{
		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			//no action for alt/select tool
 		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = SELECT_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed

	}//select_tool_mouseClicked

	void squareToolbutton_mouseClicked(MouseEvent event)
	{
		SquareOptDlog		sod = null;

		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			//***** Get the pen options
 			sod = new SquareOptDlog(Settings.editing_window, null, true);
 			if (sod != null)
 				sod.setVisible(true);

 		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = SQUARE_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}

	void pencilToolbutton_mouseClicked(MouseEvent event)
	{
		OutlineOptDlog		ood = null;

		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			//***** Get the pen options
 			ood = new OutlineOptDlog(Settings.editing_window, null, true);
 			if (ood != null)
 				ood.setVisible(true);

  		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = PENCIL_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}

	void circleToolbutton_mouseClicked(MouseEvent event)
	{
		CircleOptDlog		cod = null;

		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			//***** Get the pen options
 			cod = new CircleOptDlog(Settings.editing_window, null, true);
 			if (cod != null)
 				cod.setVisible(true);
  		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = CIRCLE_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}

	void textToolbutton_mouseClicked(MouseEvent event)
	{
		TextOptDlog		tod = null;

		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			tod = new TextOptDlog(Settings.editing_window, null, true);
 			if (tod != null)
 				tod.setVisible(true);
 		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = TEXT_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}

	void lineToolbutton_mouseClicked(MouseEvent event)
	{
		LineOptDlog		lod = null;

		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			//***** Get the pen options
 			lod = new LineOptDlog(Settings.editing_window, null, true);
 			if (lod != null)
 				lod.setVisible(true);
  		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = LINE_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}

	void arrowToolbutton_mouseClicked(MouseEvent event)
	{
		ArrowOptDlog	aod = null;

		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
 			aod = new ArrowOptDlog(Settings.editing_window, null, true);
 			if (aod != null)
 				aod.setVisible(true);
 		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = ARROW_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}

	void measureToolbutton_mouseClicked(MouseEvent event)
	{
		if ((event.getModifiers() & event.ALT_MASK) != 0) // if alt key is pressed
 		{
  		}// if alt key is pressed
 		else
 		{
			EditingSettings.tool_selected = MEASURE_TOOL;
			resetPaletteButtons();
		}// if the alt key wasn't pressed
	}


	void saveOverlayButton_mouseClicked(MouseEvent event)
	{

		try
		{
			Settings.editing_window.saveOverlay();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while saving overlay!");
			Settings.main_window.displayMessage("Overlay was not saved.");
		}
	}

	void eraseOverlayButton_mouseClicked(MouseEvent event)
	{

		try
		{
			Settings.editing_window.eraseOverlay();
		}
		catch (Exception e)
		{
			Settings.main_window.displayMessage("Exception thrown while erasing overlay!");
			Settings.main_window.displayMessage("Overlay was not erased.");
		}
	}

	void dispose()
	{
		select_img = null;
		select_img_invert = null;
		square_img = null;
		square_img_invert = null;
		pencil_img = null;
		pencil_img_invert = null;
		circle_img = null;
		circle_img_invert = null;
		text_img = null;
		text_img_invert = null;
		line_img = null;
		line_img_invert = null;
		arrow_img = null;
		arrow_img_invert = null;
		measure_img = null;
		measure_img_invert = null;
		save_img = null;
		erase_img = null;

		return;
	}// end of dispose()

}//end of class EditingPalette

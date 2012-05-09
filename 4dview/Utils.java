import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.Properties;

import quicktime.qd.*;
import quicktime.app.image.*;

class Utils {

	//********************************************************************
	//*		N U M  T O  S T R I N G
	//********************************************************************
	static String	numToString (long	number, int  places)
	{
		int			count = 0, number_chars = 1;
		long		temp_number = 0;
		boolean		negative_number = false;
		int			i = 0, j = 0;
		int			zeros_to_pad = 0;
		String		raw_num_string;
		String		num_string = new String("");
		char[]		char_array;

		//***** Convert number to positive, if necesssary
		if (number < 0)
		{
			negative_number = true;
			number = -number;
		}

		temp_number = number;

		//***** find out number of characters in the number
		for (count = 0; temp_number >= 10; count++)
		{
			temp_number = temp_number/10;
			number_chars += 1;
		}

		//***** Find out how many zeros we're going to pad
		if (places <= 1)
			zeros_to_pad = 0;
		else
			zeros_to_pad = places - number_chars;

		temp_number = number;

		char_array = new char[number_chars];

		// ***** convert number to a byte_array
		for (count = (number_chars-1); count >= 0; count--)
		{
			char_array[count] = (char)((temp_number % 10) + 48);
			temp_number = temp_number / 10;
		}

		//***** Convert char array to string
		raw_num_string = new String(char_array);

		//***** Add the - if necessary
		if (negative_number)
			num_string += '-';

		//***** Finally, pad out the zeros, if necessary
		for (i = 0; i < zeros_to_pad; i++)
		{
			num_string += '0';
		}// for each zero to pad out

		num_string += raw_num_string;

		return(num_string);

	} // end of num_to_string()


	//********************************************************************
	//*		P I C T  T O  I M A G E
	//********************************************************************
	static	Image pictToImage(Pict cur_pict) throws Exception
	{
		Image						cur_image = null;
		ImagePresenter 				data = null;
		QDRect						image_rect = null;
		QTImageProducer				qt_image_producer = null;
		Dimension						d = null;

		data = ImagePresenter.fromPict(cur_pict);
		image_rect = data.getDisplayBounds();
		d = new Dimension(image_rect.getWidth(), image_rect.getHeight());
		qt_image_producer = new QTImageProducer(data, d);
		cur_image = Toolkit.getDefaultToolkit().createImage(qt_image_producer);

		return(cur_image);

	}// end of pictToImage()


	//********************************************************************
	//*		G E T  I M A G E  D I M E N S I O N S
	//*		Created 7/18/03 for v1.75
	//********************************************************************
	static Dimension getImageDimensions(Component comp, Image img)
	{
		MediaTracker	mt = null;
		Dimension 		d = new Dimension(-1,-1);
		int				image_width = -1, image_height = -1;
		boolean			done = false;

		try
		{
			if (img == null || comp == null)
				return(d);

			mt = new MediaTracker(comp);
			mt.addImage(img, 0);

			do
			{
				done = mt.checkAll(true);

				try{ Thread.sleep(100);} catch (Exception ignore){}
			}
			while(!done);

			image_width = img.getWidth(null);
			image_height = img.getHeight(null);

			d = new Dimension(image_width, image_height);

		}// try
		catch (Exception e)
		{

		}

		return(d);

	}// end of getImageDimensions();


}// end of class Utils

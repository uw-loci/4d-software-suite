//******************************************************//*		I M A G E  R E A D E R//******************************************************//Some code modified from Wayne Rasband's freeware application "Image/J"import java.awt.*;import java.awt.image.*;import java.io.*;import java.net.*;class ImageReader{	//***** FILENAME SYNTAX CONSTANTS	final   static   int			STANDARD_NON_PADDED = 0;	final   static   int			STANDARD_THREE_PADDED = 1;	final   static   int			STANDARD_FOUR_PADDED = 2;	final   static   int			BIORAD_600 = 3;	final   static   int			BIORAD_1024 = 4;		//***** FILE TYPE CONSTANTS	final   static   int			GRAY_8_BIT_IMAGE = 0;	final   static   int			GRAY_16_BIT_IMAGE = 1;	final   static   int			GRAY_32_BIT_IMAGE = 2;	final   static   int			COLOR_8_BIT_IMAGE = 3;	final   static   int			RGB_IMAGE = 4;		//***** FILE FORMAT CONSTANTS	final   static   int			UNKNOWN_FILE = -1;	final   static   int			TIFF_FILE = 0;	final   static   int			BIORAD_600_FILE = 1;	final   static   int			BIORAD_1024_FILE = 2;	final   static   int			JPEG_FILE = 3;/*	final   static   int			PICT_FILE = 4;	final   static   int			PICS_FILE = 5;	final   static   int			BMP_FILE = 6;	final   static   int			GIF_FILE = 7;	final   static   int			MACPAINT_FILE = 8; 	final   static   int			PHOTOSHOP_FILE = 9; 	final   static   int			QUICKDRAW_GX_PICTURE_FILE = 10;	final   static   int			QUICKTIME_IMAGE_FILE = 11; 	final   static   int			SGI_IMAGE_FILE = 12; 	final   static   int			TARGA_IMAGE_FILE = 13;*/		final   static   int			kQTFileTypeTiff = 1414088262;		final   static   int			SHORT_FIELD = 3;	final   static   int			LONG_FIELD = 4;		final  static  int				NOT_FOUND = -1;	String			filename = null;	URL				url = null;	//int				image_width;	//int				image_height;	int				bytes_per_pixel = 0;	boolean			swap_bytes = false;	byte[]			color_table = null;	boolean			zero_is_black = true; 	boolean			image_opened = false;	int				num_pixels = 0;	int				offset_to_image_data = 0;	int				image_size_in_bytes = 0;	int				buffer_size;	int				file_size;				//***********************************************************	//*		I N I T	//*		Last modifed 5/30/02	//***********************************************************	public ImageReader()	{    	filename = new String("");    	url = null;		//image_width = 0;		//image_height = 0;		bytes_per_pixel = 0;		swap_bytes = false;		color_table = null;		zero_is_black = false;		image_opened = false;			num_pixels = 0;		offset_to_image_data = 0;		image_size_in_bytes = 0;		buffer_size = 0;		file_size = 0;						return;			}// init			//******************************************************************	//*			G E T  I M A G E	//*			Prompts user for directory and file with standard File Dialog	//*			Returns a single slice of a stack as a java.awt.Image	//******************************************************************	Image	getImage(String filename) throws Exception	{		FileDialog			fd = null;		Image				img = null;			fd = new FileDialog(Settings.image_window, "Open...");		fd.setVisible(true);					if (fd.getFile() != null)		{			Settings.input_directory = fd.getDirectory();			this.filename = filename;			img = getImage(Settings.input_directory, filename, 1);		}				return(img);			}// end of getImage()			//******************************************************************	//*			G E T  I M A G E	//*			Prompts user for directory and file with standard File Dialog	//*			Returns a single slice of a stack as a java.awt.Image	//******************************************************************	Image	getImage(int slice) throws Exception	{		FileDialog			fd = null;		Image				img = null;			fd = new FileDialog(Settings.image_window, "Open...");		fd.setVisible(true);					if (fd.getFile() != null)		{			Settings.input_directory = fd.getDirectory();			this.filename = fd.getFile();			img = getImage(Settings.input_directory, filename, slice);		}				return(img);			}	//******************************************************************	//*			G E T  I M A G E	//******************************************************************	Image	getImage(String input_directory, String filename) throws Exception	{		Image		img = null;				img = getImage(input_directory, filename, 1);								return(img);									}// end of getImage()	//******************************************************************	//*			G E T  I M A G E	//******************************************************************	Image	getImage(String input_directory, String filename, int slice) throws Exception	{		return(null);	}	//******************************************************************	//*			G E T  I M A G E	//*			Gets the first slice from the specificed URL	//*			Last modified 5/30/02	//******************************************************************	Image	getImage(URL  url) throws Exception	{		getImage(url, 1);		return(null);	}	//******************************************************************	//*			G E T  I M A G E	//*			Gets the specified slice from the image URL	//*			Last modified 5/30/02	//******************************************************************	Image	getImage(URL  url, int slice) throws Exception	{		return(null);	}		//*****************************************************************	//*		G E T  I M A G E  I N P U T  S T R E A M	//*		Prompts user with a standard File Dialog	//*		Last modified 6/6/02	//*****************************************************************	DataInputStream  getImageInputStream () throws Exception, CancelledException	{		DataInputStream		is = null;		FileDialog			fd = null;		String				file_name = null;		//***** Get an input stream		fd = new FileDialog(Settings.image_window, "Open...");		fd.setVisible(true);					if (fd.getFile() != null)		{			Settings.input_directory = fd.getDirectory();			file_name = fd.getFile();		}		else			throw new CancelledException();					if (file_name != null)			is = getImageInputStream(Settings.input_directory, file_name);				return(is);		}// end of getImageInputStream()	//*****************************************************************	//*		G E T  I M A G E  I N P U T  S T R E A M	//*		Uses file and directory passed into method by user	//*		Last modified 7/17/02 for v1.33	//*****************************************************************	DataInputStream  getImageInputStream (String input_directory, String file_name) throws Exception	{		FileInputStream		fis = null;		BufferedInputStream	bis = null;		DataInputStream		dis = null;		//***** Get an input stream		fis = new FileInputStream(input_directory + file_name);				if (fis == null)			throw new Exception("Unable to obtain FileInputStream from file.");					bis = new BufferedInputStream(fis);		if (bis == null)			throw new Exception("Unable to obtain BufferedInputStream from file.");							dis = new DataInputStream(bis);		if (dis == null)			throw new Exception("Unable to obtain DataInputStream from file.");		this.filename = file_name;		return(dis);		}// end of getImageInputStream()	//*****************************************************************	//*		G E T  I M A G E  I N P U T  S T R E A M	//*		Uses URL passed into method by user	//*		Last modified 7/17/02 for v1.33	//*****************************************************************	DataInputStream  getImageInputStream (URL url) throws Exception	{		InputStream				is = null;		DataInputStream			dis = null;		//***** Get an input stream		is = url.openStream();				if (is != null)		{			dis = new DataInputStream(is);						if (dis != null)				this.url = url;			else				throw new Exception("Unable to obtain DataInputStream from URL.");					}		else			throw new Exception("Unable to obtain InputStream from URL.");						return(dis);		}// end of getImageInputStream()	//*****************************************************************	//*		G E T  F I L E N A M E	//*****************************************************************	public	String	getFilename()	{		return(new String(this.filename));	}	//******************************************************************************	//*		R E A D  8  B I T  I M A G E	//*		Assumes read pointer is positioned at the start of 8 bit image data and	//*		that image_width, image_height, and num_pixels has been specified	//******************************************************************************	Image	read8BitImage(DataInputStream is) throws Exception 	{		byte[] 			pixels = null;		Image			img = null;		int 			bytes_read = 0;		ColorModel 		cm = null;		int				pixels_to_read = 17384;//8192;				try		{			pixels = new byte[num_pixels];			while (bytes_read < num_pixels) 			{				if ((num_pixels - bytes_read) < pixels_to_read)					pixels_to_read = (num_pixels - bytes_read);									bytes_read += is.read(pixels, bytes_read, pixels_to_read);			}					//***** Make the LUT			if (Settings.input_file_type == GRAY_8_BIT_IMAGE)				cm = makeGrayscaleColorModel(!this.swap_bytes);	 		else 	  			cm = makeColorModel();	    	 	 	//***** Make the image	  		img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(Settings.image_width, //width	  																			Settings.image_height, //height	  																			cm, // color model	  																			pixels, // pixel data	  																			0, // offset	  																			Settings.image_width));// scan width	  	}	  	catch (Exception e)	  	{	  		throw(new Exception("Unable to read 8-bit image data."));	  	} 	  		 	 return(img);	    	}// end of read8BitImage()			//******************************************************************************	//*		R E A D  1 6  B I T  I M A G E	//*		16 bit images are scaled to 8 bit for display	//*		Assumes read pointer is positioned at the start of 16 bit image data and	//*		that image_width, image_height, and num_pixels has been specified	//******************************************************************************	Image read16BitImage(DataInputStream is) throws Exception 	{		int 			pixels_read = 0;		Image			img = null;		byte[] 			buffer = new byte[buffer_size];		byte[] 			pixels8 = new byte[num_pixels];		short[] 		pixels = new short[num_pixels];		int 			bytes_read = 0;		int 			total_read = 0;		int 			base = 0, value = 0;		int				i = 0, j = 0;		int				min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;		double 			scale = 0; 		ColorModel 		cm = null;	 							try		{			while (total_read < file_size) 			{				bytes_read = is.read(buffer, 0, buffer_size);				total_read += bytes_read;				pixels_read = bytes_read/this.bytes_per_pixel;				if ((base + pixels_read) > num_pixels)					pixels_read = num_pixels - base;								if (this.swap_bytes)				{					for (i = base; i < (base + pixels_read); i++) 					{						pixels[i] = (short)(((buffer[j+1] & 0xff) << 8) | (buffer[j] & 0xff));						j += 2;					}				}				else				{					for (i = base; i < (base + pixels_read); i++) 					{						pixels[i] = (short)(((buffer[j] & 0xff) <<8 ) | (buffer[j+1 ]& 0xff));						j += 2;					}				}// if not swap_bytes							base += pixels_read;			} // while			cm = makeGrayscaleColorModel(this.swap_bytes);	    		    //***** Find the minimum and maximum values in the image			for (i = 0; i < Settings.image_width * Settings.image_height; i++) 			{				value = pixels[i];				if (value < min)					min = value;				if (value > max)					max = value;			}			scale = 255.0/(max-min);// find scaling factor					//***** Scale each 16 bit value to 8 bits for display			for (i = 0; i < num_pixels; i++)				pixels8[i] = (byte)((int)(scale * (pixels[i] - min)) & 0xff);						//***** Finally, make the image with the 8 bit values and the color table				img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(Settings.image_width, 																			    Settings.image_height, 																			    cm, 																			    pixels8, 																			    0, 																			    Settings.image_width));		}		catch (Exception e)		{			throw (e);		}    	    						return(img);			}// end of read16BitImage();			//******************************************************************************	//*		R E A D  3 2  B I T  I M A G E	//*		32-bit float images are converted to 8-bit for display	//******************************************************************************	Image read32BitImage(DataInputStream is) throws Exception 	{		int 			pixels_read = 0;		byte[]			buffer = new byte[buffer_size];		byte[]			pixels8 = new byte[num_pixels];		float[]			pixels = new float[num_pixels];		int 			tmp = 0;		int				bytes_read = 0;		int 			total_read = 0;		int 			base = 0, i = 0, j = 0;		Image			img = null;		float			min = Float.MAX_VALUE, max = -Float.MAX_VALUE;		float 			scale = 0, value = 0; 		ColorModel 		cm = null;					try		{			while (total_read < file_size) 			{				bytes_read = is.read(buffer, 0, buffer_size);				total_read += bytes_read;				pixels_read = bytes_read/this.bytes_per_pixel;				if ((base + pixels_read)  > num_pixels)					pixels_read = num_pixels - base;								if (this.swap_bytes)					for (i = base; i < (base + pixels_read); i++) 					{						tmp = (int)(((buffer[j+3] & 0xff) << 24) | ((buffer[j+2] & 0xff) << 16) | 									((buffer[j+1] & 0xff) << 8) | (buffer[j] & 0xff));						if (Settings.input_file_type == GRAY_32_BIT_IMAGE) 						{							pixels[i] = Float.intBitsToFloat(tmp);						}						else							pixels[i] = tmp;						j += 4;					}				else					for (i = base; i < (base +  pixels_read); i++) 					{						tmp = (int)(((buffer[j] & 0xff) << 24) | ((buffer[j+1] & 0xff)<<16) |								 	((buffer[j+2] & 0xff) << 8) | (buffer[j+3] & 0xff));						if (Settings.input_file_type == GRAY_32_BIT_IMAGE)							pixels[i] = Float.intBitsToFloat(tmp);						else							pixels[i] = tmp;						j += 4;					}				base += pixels_read;			}// while total_read < file_size				cm = makeGrayscaleColorModel(true);				//***** Get the minimum and maximum values in the image			for (i = 0; i < num_pixels; i++) 			{				value = pixels[i];				if (value < min)					min = value;				if (value > max)					max = value;			}							scale = 255/(max-min);// find scaling factor					//***** Scale the float values			for (i = 0; i < num_pixels; i++)				pixels8[i] = (byte)((int)(scale*(pixels[i] - min)) & 0xff);					//***** Create the image				img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(Settings.image_width, Settings.image_height, cm, pixels8, 0, Settings.image_width));		}		catch (Exception e)		{			throw(e);		}			    		return(img);	}// end of read32BitImage()		//******************************************************************************	//*		R E A D  R G B  I M A G E	//******************************************************************************	Image	readRGBImage(DataInputStream is) throws Exception 	{		int				pixels_read = 0;		Image			img = null;		byte[] 			buffer = null;		int[]			pixels = null;		int 			r = 0, g = 0, b = 0;		int 			bytes_read = 0;		int 			total_read = 0;		int 			base = 0, i = 0, j = 0;		boolean 		first = true;		try		{			buffer_size = 24 * Settings.image_width;			buffer = new byte[buffer_size];			pixels = new int[num_pixels];					while (total_read < file_size) 			{				bytes_read = is.read(buffer, 0, buffer_size);				total_read += bytes_read;				pixels_read = bytes_read/this.bytes_per_pixel;				if ((base + pixels_read) > num_pixels)					pixels_read = num_pixels - base;					j = 0;				for (i = base; i < (base + pixels_read); i++) 				{					r = buffer[j++] & 0xff;					g = buffer[j++] & 0xff;					b = buffer[j++] & 0xff;					pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;				}				base += pixels_read;			}	   	   		img = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(Settings.image_width, Settings.image_height, pixels, 0, Settings.image_width));		}        		catch (Exception e)        		{        			throw(e);        		}        		return(img);        	}// end of readRGBImage()	//******************************************************************************	//*		M A K E  G R A Y S C A L E  C O L O R  M O D E L	//*		Last modified 7/9/02	//******************************************************************************	ColorModel makeGrayscaleColorModel(boolean invert) throws Exception	 {	 	ColorModel 	cm = null;		byte[] 		red_LUT = null, green_LUT = null, blue_LUT = null;		int 		i = 0;				red_LUT = new byte[256];		green_LUT = new byte[256];		blue_LUT = new byte[256];				if (invert)			for (i = 0; i < 256; i++) 			{				red_LUT[255-i]=(byte)(i & 0xff);				green_LUT[255-i]=(byte)(i & 0xff);				blue_LUT[255-i]=(byte)(i & 0xff);			}		else		{			for (i = 0; i < 256; i++) 			{				red_LUT[i]=(byte)(i & 0xff);				green_LUT[i]=(byte)(i & 0xff);				blue_LUT[i]=(byte)(i & 0xff);			}		}				cm = new IndexColorModel(8, 256, red_LUT, green_LUT, blue_LUT);				if (cm == null)			throw new Exception("Unable to obtain grayscale color model.");		else				return(cm);			}// end of makeGrayscaleColorModel()	//******************************************************************************	//*		M A K E  C O L O R  M O D E L	//*		Last modified 7/9/02	//******************************************************************************	ColorModel makeColorModel() throws Exception	{	 	ColorModel 	cm = null;		byte[] 		red_LUT = null, green_LUT, blue_LUT = null;		int 		i = 0, k = 0;				red_LUT = new byte[256];		green_LUT = new byte[256];		blue_LUT = new byte[256];				for(i = 0; i < 256; i++) 		{			red_LUT[i] = this.color_table[i];			green_LUT[i] = this.color_table[256+i];			blue_LUT[i] = this.color_table[512+i];		}				cm = new IndexColorModel(8, 256, red_LUT, green_LUT, blue_LUT);				if (cm == null)			throw new Exception("Unable to obtain color model.");		else				return(cm);			}// end of makeColorModel()		//******************************************************************	//*			G E T  F I L E  I N F O	//******************************************************************	void	 getFileInfo(String directory, String filename) throws Exception 	{		return;	}	//******************************************************************************	//*		S E T  F I L E  I O  I N F O	//*		Sets the following parameters:	//*		image_size_in_bytes, num_pixels, buffer_size	//******************************************************************************	void setFileIOInfo() 	{		this.image_size_in_bytes = Settings.image_width * Settings.image_height * this.bytes_per_pixel;		this.num_pixels = Settings.image_width * Settings.image_height;				if (this.file_size > image_size_in_bytes)			this.file_size = this.image_size_in_bytes;		this.buffer_size = this.file_size/25;				if (this.buffer_size < 8192)			this.buffer_size = 8192;		else			this.buffer_size = (buffer_size/8192)*8192;					return;			}// end of setFileIOInfo()					//******************************************************************	//*			G E T  V A L U E	//******************************************************************	int getValue(RandomAccessFile raf, int field_type, int count) throws Exception 	{		int value = 0;		int unused = 0;				if (field_type == SHORT_FIELD && count == 1) 		{			value = getShort(raf);			unused = getShort(raf);		}		else			value = getInt(raf);					return(value);			}// end of getValue()		//******************************************************************	//*			G E T  V A L U E	//******************************************************************	int getValue(DataInputStream is, int field_type, int count) throws Exception 	{		int value = 0;		int unused = 0;				if (field_type == SHORT_FIELD && count == 1) 		{			value = getShort(is);			unused = getShort(is);		}		else			value = getInt(is);					return(value);			}// end of getValue()		//******************************************************************	//*			G E T  I N T	//******************************************************************	int getInt(RandomAccessFile  raf) throws Exception 	{		int b1 = raf.read();		int b2 = raf.read();		int b3 = raf.read();		int b4 = raf.read();		if (this.swap_bytes)			return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1 << 0));		else			return ((b1 << 24) + (b2 << 16) + (b3 << 8) + b4);				}// end of getInt()	//******************************************************************	//*			G E T  I N T	//*			Last modified 6/12/02	//******************************************************************	int getInt(DataInputStream is) throws Exception 	{		int b1 = is.read();		int b2 = is.read();		int b3 = is.read();		int b4 = is.read();		if (this.swap_bytes)			return ((b4 << 24) + (b3 << 16) + (b2 << 8) + (b1 << 0));		else			return ((b1 << 24) + (b2 << 16) + (b3 << 8) + b4);				}// end of getInt()	//******************************************************************	//*			G E T  S H O R T	//******************************************************************	int getShort(RandomAccessFile raf) throws Exception 	{		int b1 = raf.read();		int b2 = raf.read();		if (this.swap_bytes)			return ((b2 << 8) + b1);		else			return ((b1 << 8) + b2);				}// end of getShort()		//******************************************************************	//*			G E T  S H O R T	//*			Last modified 6/12/02	//******************************************************************	int getShort(DataInputStream is) throws Exception 	{		int b1 = is.read();		int b2 = is.read();		if (this.swap_bytes)			return ((b2 << 8) + b1);		else			return ((b1 << 8) + b2);				}// end of getShort()		}// end of class ImageReader
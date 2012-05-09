import java.awt.*;import java.lang.*;import java.io.*;import java.util.*;import java.net.*;import wvlib.*;class DataCompressor{	Thread					proc_thread = null;	Thread					update_thread = null;	ImageReader				image_reader = null;	boolean					keep_updating = false;	wv4DBWCompressor		compressor = null;	wvFakeCompressor		fake_compressor = null;	DataOutputStream		bit_out = null;	int						num_timepoint_blocks = 0, num_focalplane_blocks = 0;	int						timepoints_remaining = 0, focalplanes_remaining = 0;	int						images_processed = 0;	int						bytes_per_pixel = 1;	long					bytes_written_to_disk = 0;	int						image_width = -1, image_height = -1;	boolean					data_processed = false;	//********************************************************************************	//*		I N I T	//********************************************************************************	public DataCompressor(ImageReader iih)	{		this.image_reader = iih;						return;			}// init	//********************************************************************************	//*		D O  P R O C E S S I N G	//********************************************************************************	public void processData()	{				//***** Create a runnable object to do the conversion		Runnable do_it = new Runnable()		{			public void run()			{				doProcessing();				//try{testTiffOutput();}catch(Exception e){e.printStackTrace();}			}			};				//***** Create a thread for the runnable object to run within		proc_thread = new Thread(do_it);				//***** Set its priority low so that the window can update		proc_thread.setPriority(Thread.NORM_PRIORITY);				//***** Start the conversion thread running		proc_thread.start();	}// end of processData()	//********************************************************************************	//*		D O  P R O C E S S I N G	//*		Last modified 1/15/03 for v1.38	//********************************************************************************	protected void	 doProcessing()	{		Image			cur_image = null;						try		{			//***** Open the first timepoint			cur_image = getImageSlice(Settings.input_filename, 1);						//***** Get processing info			getUserProcessingInfo();			//***** Get compression parameters			CompressionSettings.getCompressionParameters();				//***** Display the image in the window			displayImage(cur_image);						//***** Loop through and process each timepoint			Settings.image_window.displayMessage("Beginning compression...");			processTimepoints();						if (data_processed)				Settings.image_window.displayMessage("Your data set was compressed successfully!");			else				Settings.image_window.displayMessage("No data was processed.");									resetClassVariables();		}		catch (OutOfMemoryError ome)		{			handleOutOfMemory();			return;		}		catch (CancelledException ce)		{			handleCancelled();			return;		}		catch (AbortedException ae)		{			handleAborted();			return;		}		catch (FileNotFoundException fnfe)		{			handleFileNotFound();			return;				}		catch (Exception e)		{			handleException(e);			return;		}				return;			}// end of doProcessing()		//****************************************************************************************************************	//* 	P R O C E S S  T I M E P O I N T S	//* 	//* 	We want to compress from {Settings.first_timepoint, ..., Settings.last_timepoint}	//* 	and from {Settings.first_plane, ..., Settings.last_plane}, so these must be set prior to calling this method.	//*		//* 	The data will be grouped into smaller blocks.	//* 	The number of timepoints will be sub-grouped into chunks of CompressionSettings.timepoints_per_block images. 	//* 	The total focal planes will be sub-grouped into chunks of CompressionSettings.focalplanes_per_block images.	//*	//*		Last modified 1/7/03 for v1.37	//****************************************************************************************************************	protected void processTimepoints() throws Exception	{		String				movie_filename = null, timepoint_filename = null;		String				bit_name = null;		Image				cur_image = null;		boolean 			block_size_set = false;		int					plane = 0, tp = 0;		int					time = 0, space = 0;		int					cur_timepoint_block = 0, cur_focalplane_block = 0;				//***** Get info and output settings		image_width = -1;		image_height = -1;					Settings.abort = false;		Settings.start_time = -1;			getNumTimepointBlocks();		getNumFocalplaneBlocks();		displaySettings(num_timepoint_blocks, num_focalplane_blocks);		createUpdateThread();		checkForMissingFiles();			 		for (cur_timepoint_block = 0; cur_timepoint_block < num_timepoint_blocks; cur_timepoint_block++) 		{			bit_name = NameUtils.createBitfileName(Settings.base_timepoint_name, cur_timepoint_block+1);			bit_out = new DataOutputStream(new FileOutputStream(Settings.output_directory + bit_name));			timepoints_remaining = getTimepointsRemaining(cur_timepoint_block);			getCompressor();							  				    							  				  			for (cur_focalplane_block = 0; cur_focalplane_block < num_focalplane_blocks; cur_focalplane_block++)			{				focalplanes_remaining = getFocalplanesRemaining(cur_focalplane_block);				block_size_set = false;								for (tp = Settings.first_timepoint + (cur_timepoint_block * CompressionSettings.timepoints_per_block), time = 0; 					 time < timepoints_remaining; tp++, time++)				{					timepoint_filename = NameUtils.createInputFilename(Settings.base_timepoint_name, tp, Settings.filename_syntax, 0);									//**** Update settings					Settings.input_filename = new String(timepoint_filename);					//***** Process each plane					for (plane = Settings.first_plane + (cur_focalplane_block * CompressionSettings.focalplanes_per_block), space = 0; 						 space < focalplanes_remaining; plane++, space++)					{												cur_image = getImageSlice(timepoint_filename, plane);							      				displayImage(cur_image);							      										if (!Settings.test_mode)						{							if (!block_size_set)								compressor.setBlockSize(image_width, image_height, focalplanes_remaining);							compressor.setImage(cur_image, space, time);						}						else						{							if (!block_size_set)								fake_compressor.setBlockSize(image_width, image_height, focalplanes_remaining);							fake_compressor.setImage(cur_image, space, time);						}																			cur_image = null;						images_processed += 1;	      				block_size_set = true;												updateInfoPanel(tp, plane);						checkForAbort();																	}// for each focal plane in a space block														}// for each timepoint in a time block	  	  			//***** Process this focalplane block				doWaveletTransform();				writeEncodedFile();				displayCompressionRatio();			  								  				  						} // for each cur_focalplane_block						bytes_written_to_disk += bit_out.size();			bit_out.close();								} // for each cur_timepoint_block						return;	}// end of processTimepoints()	//********************************************************************************	//*		C R E A T E  U P D A T E  T H R E A D	//********************************************************************************	protected void createUpdateThread()	{				Settings.start_time = System.currentTimeMillis();		//***** Create a runnable object to do the conversion		Runnable do_it = new Runnable()		{			public void run()			{				doUpdating();			}			};				//***** Create a thread for the runnable object to run within		update_thread = new Thread(do_it);				//***** Set its priority		update_thread.setPriority(Thread.NORM_PRIORITY);				//***** Start the conversion thread running		update_thread.start();	}// end of createUpdateThread()			//********************************************************************************	//*		D O  U P D A T I N G	//********************************************************************************	protected void doUpdating()	{		String	time_string = null;				keep_updating = true;			try		{					while (keep_updating)			{				Settings.status_panel.handleElapsedTime();				time_string = getTimeRemaining();				Settings.status_panel.handleTimeRemaining(time_string);				try				{					update_thread.sleep(499);				}				catch (InterruptedException ie)				{				}							}// while keep_updating					}		catch (Exception e) 		{			Settings.image_window.displayMessage("Exception in doUpdating()");		}				return;			}// end of doUpdating	//********************************************************************************	//*		C H E C K  F O R  M I S S I N G  F I L E S	//*		Added 11/29/02 for v1.36	//********************************************************************************	protected void checkForMissingFiles() throws Exception	{		if (!Settings.use_URL)		{			NameUtils.checkForMissingFiles(Settings.input_directory, Settings.base_timepoint_name, 										   Settings.filename_syntax, Settings.first_timepoint, Settings.total_timepoints);		}// if we're not compressing over the web				return;			}// end of checkForMissingFiles()	//********************************************************************************	//*		G E T  U S E R  P R O C E S S I N G  I N F O	//*		Last modified 11/21/02 for v1.36	//********************************************************************************	private void getUserProcessingInfo() throws Exception, CancelledException	{		BooleanWrapper		dlog_cancelled = new BooleanWrapper(true);		CompInfoDlog		cid = null;		boolean				user_info_valid = false;						//***** Set last timepoint default to be the number of files in the directory		if (!Settings.use_URL)			Settings.last_timepoint = Settings.first_timepoint + NameUtils.countValidFilesInDirectory(Settings.input_directory, Settings.base_timepoint_name, Settings.filename_syntax) -1;		Settings.base_timepoint_name = NameUtils.findBaseFileName(Settings.input_filename);		//***** Get the compression processing information		cid = new CompInfoDlog(Settings.image_window, true, dlog_cancelled);		cid.setVisible(true);				//***** See if the user cancelled		if (dlog_cancelled.getValue())			throw new CancelledException();				//***** Check for input validity.  If not valid, try again		if (!userInputValid())		{			IOUtils.putMessage(Settings.image_window, "Illegal value entered!");			getUserProcessingInfo();		}				 		return;			}// end of getUserProcessingInfo()		//********************************************************************************	//*		U S E R  I N P U T  V A L I D	//*		Runs a series of tests on user input to see if it's invalid	//*		passing this test does NOT insure user input is free of errors.	//*		Added 11/21/02 for v1.36	//********************************************************************************	private boolean userInputValid()	{				if (Settings.first_timepoint <= 0)			return(false);		if (Settings.last_timepoint < Settings.first_timepoint)			return(false);		if (Settings.total_timepoints <= 0)			return(false);		if (Settings.first_plane <= 0)			return(false);		if (Settings.last_plane < Settings.first_plane)			return(false);		if (Settings.total_planes <= 0)			return(false);		if (Settings.base_timepoint_name == null)			return(false);		if (Settings.output_directory == null)			return(false);				return(true);			}// end of userInputValid()			//********************************************************************************	//*		G E T  C O M P R E S S O R	//********************************************************************************	private void getCompressor() throws Exception	{		//***** Get our compressor		if (!Settings.test_mode)		{			compressor = new wv4DBWCompressor((float)(CompressionSettings.p),                 // p											  (float)(CompressionSettings.threshold),         // threshold							  				  bit_out,                                        // DataOutputStream											  Settings.first_timepoint,                       // number of first timepoint				   							  Settings.base_timepoint_name,					  // base timepoint name		   									  Settings.total_timepoints,					  // total timepoints compressed		   									  num_timepoint_blocks,							  // num bitfiles created											  Settings.last_plane - Settings.first_plane + 1, // num focal planes in stack							  				  num_focalplane_blocks,                          // number of blocks							  				  timepoints_remaining,                           // time points in each block							  				  wvGraylevels.GRAY8BIT);                         // gray level		}// if not in test mode		else		{  			fake_compressor = new wvFakeCompressor((float)(CompressionSettings.p),            // p											  (float)(CompressionSettings.threshold),         // threshold							  				  bit_out,                                        // DataOutputStream											  Settings.first_timepoint,                       // number of first timepoint				   							  Settings.base_timepoint_name,					  // base timepoint name			   								  Settings.total_timepoints,					  // total timepoints compressed			   								  num_timepoint_blocks,							  // num bitfiles created											  Settings.last_plane - Settings.first_plane + 1, // num focal planes in stack							  				  num_focalplane_blocks,                          // number of blocks							  				  timepoints_remaining,                           // time points in each block							  				  wvGraylevels.GRAY8BIT);                         // gray level		}// if in test mode				return;			}// end of getCompressor()	//*********************************************************************************	//*		G E T  N U M  T I M E P O I N T  B L O C K S	//*********************************************************************************	protected void getNumTimepointBlocks() throws Exception	{		if (CompressionSettings.timepoints_per_block > 0)			this.num_timepoint_blocks = (int)(Math.ceil(Settings.total_timepoints/(double)(CompressionSettings.timepoints_per_block)));		else			throw new Exception("Illegal parameter in getNumTimepointBlocks()");			return;			}// end of getNumTimepointBlocks()			//********************************************************************************	//*		G E T  N U M  F O C A L P L A N E  B L O C K S	//********************************************************************************	protected void getNumFocalplaneBlocks() throws Exception	{		if (CompressionSettings.focalplanes_per_block > 0)			this.num_focalplane_blocks = (int)(Math.ceil(Settings.total_planes/(double)(CompressionSettings.focalplanes_per_block)));		else			throw new Exception("Illegal parameter in getNumFocalplaneBlocks()");		return;				}// end of getNumFocalplaneBlocks()			//********************************************************************************	//*		G E T  T I M E P O I N T S  R E M A I N I N G	//********************************************************************************	protected int getTimepointsRemaining(int cur_timepoint_block)	{		return(Math.min(CompressionSettings.timepoints_per_block, 						Settings.last_timepoint - (Settings.first_timepoint + 						(cur_timepoint_block * CompressionSettings.timepoints_per_block)) + 1 ));	}// end of getTimepointsRemaining	//********************************************************************************	//*		G E T  F O C A L P L A N E S  R E M A I N I N G	//********************************************************************************	protected int getFocalplanesRemaining(int cur_focalplane_block)	{		return(Math.min(CompressionSettings.focalplanes_per_block, 						Settings.last_plane - (Settings.first_plane + 						(cur_focalplane_block * CompressionSettings.focalplanes_per_block)) + 1));	}// end of getFocalplanesRemaining()		//********************************************************************************	//*		D O  W A V E L E T  T R A N S F O R M	//*		Last modified 1/15/03 for v1.38	//********************************************************************************	protected void doWaveletTransform() throws Exception	{		//Settings.image_window.displayMessage("Performing wavelet transform... please wait.");	  	try		{			if (!Settings.test_mode)				compressor.doWaveletTransform();		 	else		  		fake_compressor.doWaveletTransform();		  				  	data_processed = true;	  	}		catch (OutOfMemoryError ome)	  	{	  		Settings.image_window.displayMessage("OUT OF MEMORY!! Unable to do wavelet tranform.");	  	}	  			  		return;	  			}// end of doWaveletTransform()			//********************************************************************************	//*		W R I T E  E N C O D E D  F I L E	//* 	Last modified 1/15/03 for v1.38	//********************************************************************************	protected void writeEncodedFile() throws Exception	{		//Settings.image_window.displayMessage("Writing encoded file... please wait.");	  	if (!Settings.test_mode)	  		compressor.writeEncoded();	  	else	  		fake_compressor.writeEncoded();	  			  	return;	}// end of writeEncodedFile()			//********************************************************************************	//*		C H E C K  F O R  A B O R T	//*		Last modified 6/6/02	//********************************************************************************	protected void checkForAbort() throws AbortedException	{		if (Settings.abort)		{			Settings.abort = false;			keep_updating = false;			throw new AbortedException();		}	}// end of checkForAbort()			//********************************************************************************	//*		G E T  S T A R T  T I M E	//********************************************************************************	protected void getStartTime()	{		if (Settings.start_time == -1)			Settings.start_time = System.currentTimeMillis();				return;			}// end of getStartTime()		//********************************************************************************	//*		H A N D L E  E X C E P T I O N	//*		Last updated 5/30/02	//********************************************************************************	protected void handleException(Exception e)	{		Settings.image_window.displayMessage("An exception was caught.");		Settings.image_window.displayMessage("Data compression failed.");		keep_updating = false;		Settings.resetSettings();		e.printStackTrace();				return;			}// end of handleException		//********************************************************************************	//*		H A N D L E  C A N C E L L E D	//*		Last updated 5/30/02	//********************************************************************************	protected void handleCancelled()	{		Settings.resetSettings();		resetClassVariables();		Settings.image_window.displayMessage("Compression was cancelled.");				return;			}// end of handleCancelled()	//********************************************************************************	//*		H A N D L E  A B O R T E D	//*		Last updated 6/6/02	//********************************************************************************	protected void handleAborted()	{		Settings.resetSettings();		resetClassVariables();		Settings.image_window.displayMessage("Compression was aborted.");				return;			}// end of handleAborted()	//********************************************************************************	//*		H A N D L E  F I L E  N O T  F O U N D	//*		Last updated 6/28/02	//********************************************************************************	protected void handleFileNotFound()	{		Settings.image_window.displayMessage("The specified file: ");		Settings.image_window.displayMessage(Settings.input_filename);				if (Settings.use_URL)			Settings.image_window.displayMessage("At URL: " + Settings.input_directory_url_string);		else			Settings.image_window.displayMessage("In directory: " + Settings.input_directory);		Settings.image_window.displayMessage("could not be found!");		Settings.resetSettings();		resetClassVariables();								return;			}// end of handleFileNotFound()		//********************************************************************************	//*		H A N D L E  O U T  O F  M E M O R Y	//*		Last updated 6/6/02	//********************************************************************************	protected void handleOutOfMemory()	{		Settings.image_window.displayMessage("Out of memory error generated.");		Settings.image_window.displayMessage("Data compression failed.");		keep_updating = false;		Settings.resetSettings();				return;			}// end of handleOutOfMemory()			//********************************************************************************	//*		D I S P L A Y  S E T T I N G S	//********************************************************************************	protected void displaySettings(int num_tp_blocks, int num_fp_blocks)	{		Settings.image_window.displayMessage("Timepoints per block = " + num_tp_blocks);		Settings.image_window.displayMessage("Focal planes per block = " + num_fp_blocks);		Settings.image_window.displayMessage("First timepoint = " + Settings.first_timepoint);		Settings.image_window.displayMessage("Last timepoint = " + Settings.last_timepoint);		Settings.image_window.displayMessage("First plane = " + Settings.first_plane);		Settings.image_window.displayMessage("Last plane = " + Settings.last_plane);		Settings.image_window.displayMessage("Time size = " + CompressionSettings.timepoints_per_block);		Settings.image_window.displayMessage("Space size = " + CompressionSettings.focalplanes_per_block);				return;	}// end of displaySettings()	//********************************************************************************	//*		D I S P L A Y  S T A T U S	//*		Last modified 9/25/02 for v1.35	//********************************************************************************	protected void displayStatus(String bit_name, int cur_timepoint_block, int cur_focalplane_block, int tp, int plane)	{		Settings.image_window.displayMessage("Current filename = " + bit_name);		//Settings.image_window.displayMessage("Current timepoint block = " + cur_timepoint_block);		//Settings.image_window.displayMessage("Current focalplane block = " + cur_focalplane_block);		Settings.image_window.displayMessage("Current tp = " + tp);		Settings.image_window.displayMessage("Current plane = " + plane);						return;			}// end of displayStatus()	//********************************************************************************	//*		D I S P L A Y  C O M P R E S S I O N  R A T I O	//********************************************************************************	protected void displayCompressionRatio()	{		long		bytes_per_image = 0;		long		uncompressed_data_size = 0, compressed_data_size = 0;		double		uncompressed_mb = 0.0, compressed_mb = 0.0;		double		compression_ratio = 0.0;				try		{			bytes_per_image = image_width * image_height * bytes_per_pixel;			uncompressed_data_size = bytes_per_image * images_processed;			uncompressed_mb = uncompressed_data_size/1000000.0;			compressed_data_size = bytes_written_to_disk + bit_out.size();			compressed_mb = compressed_data_size/1000000.0;			compression_ratio = uncompressed_data_size/compressed_data_size;					Settings.image_window.displayMessage("Compression ratio = " + 												 Double.toString(compression_ratio) + ":1");												 					}		catch (Exception e)		{			Settings.image_window.displayMessage("Exception thrown while calculating compression ratio.");		}				return;		}// end of displayCompressionRatio()			//********************************************************************************	//*		R E S E T  C L A S S  V A R I A B L E S	//********************************************************************************	protected void resetClassVariables()	{		proc_thread = null;		compressor = null;		fake_compressor = null;		bit_out = null;		num_timepoint_blocks = 0;		num_focalplane_blocks = 0;		timepoints_remaining = 0;		focalplanes_remaining = 0;		images_processed = 0;		image_width = -1;		image_height = -1;		keep_updating = false;		data_processed = false;				return;			}// end of resetClassVariables()/*	//********************************************************************************	//*		T E S T  T I F F  O U T P U T	//********************************************************************************	public void testTiffOutput() throws Exception	{		TiffWriter			tiff_writer = null;		Vector				image_array = new Vector(10,1);		Image				cur_image = null;		int					plane = 0;		String				filename = null;		int					width = -1, height = -1;						//***** Open the first timepoint			cur_image = image_reader.getImage(Settings.input_directory, Settings.input_filename, 1);		//***** Get processing info		getUserProcessingInfo();		//***** Open the stack		for (plane = 1; plane <= Settings.last_plane; plane++)		{			Settings.image_window.displayMessage("Opening TIFF slice " + plane);			cur_image = image_reader.getImage(Settings.input_directory, Settings.input_filename, plane);			image_array.addElement(cur_image);						//***** Display the image in the window			Settings.image_canvas.setImage(cur_image);			proc_thread.yield();// wait for update		}			do		{			width = cur_image.getWidth(null);			height = cur_image.getHeight(null);		} while (height == -1 || width == -1);				//***** Save the stack as a tiff file		tiff_writer = new TiffWriter("TestStack.tif", width, height);		//tiff_writer.saveStack(image_array);				tiff_writer.createStack(image_array.size());				for (plane = 0; plane < image_array.size(); plane++)		{			cur_image = (Image)image_array.elementAt(plane);			tiff_writer.writeImageSlice(cur_image);		}				tiff_writer.closeStack();				tiff_writer = null;						Settings.image_window.displayMessage("Tiff stack 'TestStack.tif'" + "saved to disk.");			return;			}//end of testTiffOutput()*/	//********************************************************************************	//*		G E T  T I M E  R E M A I N I N G	//********************************************************************************		public String getTimeRemaining()	{		long			delta_ms = 0, total_secs = 0;		long			total_images = 0, remaining_planes = 0;		long			ms_per_slice = 0, remaining_ms = 0;		int				hours = 0, mins = 0, secs = 0;		int				total_mins = 0;		String			time_string = "??:??:??";				try		{			//***** Determine time remaining			total_images = Settings.total_timepoints * Settings.total_planes;			remaining_planes = total_images - this.images_processed;			delta_ms = System.currentTimeMillis() - Settings.start_time;			ms_per_slice = delta_ms/this.images_processed;			remaining_ms = ms_per_slice * remaining_planes;			total_secs = remaining_ms/1000;			total_mins = (int)(total_secs/60);			secs = (int)(total_secs % 60);			hours = (int)(total_mins/60);			mins = (int)(total_mins % 60);					//***** Build the info string			time_string = StringUtils.getTimeString(hours, mins, secs);		}		catch (Exception e)		{			return(time_string);		}				return(time_string);			}// end of handleTimeRemaining()	//*****************************************************************************************	//*		G E T  I M A G E  S L I C E	//*		Last modified 6/17/02	//*****************************************************************************************	protected Image getImageSlice(String timepoint_filename, int plane) throws Exception	{		Image		cur_image = null;				//***** Get the individual slice		if (Settings.use_URL)			cur_image = image_reader.getImage(Settings.input_url, plane);		else			cur_image = image_reader.getImage(Settings.input_directory, timepoint_filename, plane);					return(cur_image);			}// end of getImageSlice()			//*****************************************************************************************	//*		D I S P L A Y  I M A G E	//*		Last modified 1/7/03 for v1.37	//*****************************************************************************************	protected void displayImage(Image img)	{		try		{			//***** Display the image in the window			if (image_width == -1 || image_height == -1)				getImageSize(img);			Settings.image_canvas.displayImage(img, image_width, image_height);								//try			//{				//this.proc_thread.sleep(50);// wait for update				//this.update_thread.sleep(50);// wait for update			//}			//catch (InterruptedException ue){}								}		catch (Exception e)		{			Settings.image_window.displayMessage("Unable to display decompressed image.");		}				return;			}// end of displayImage()	//********************************************************************************	//*		U P D A T E  I N F O  P A N E L	//********************************************************************************		private void updateInfoPanel(int cur_timepoint, int cur_focalplane)	{		String		time_string = null;				try		{			Settings.status_panel.handleCurImage(this.images_processed);			Settings.status_panel.handleTotalImages(Settings.total_timepoints * Settings.total_planes);			Settings.status_panel.handleCurTimepoint(cur_timepoint);			Settings.status_panel.handleCurFocalplane(cur_focalplane);			Settings.status_panel.handleElapsedTime();			time_string = getTimeRemaining();			Settings.status_panel.handleTimeRemaining(time_string);			Settings.status_panel.handleFilename(Settings.input_filename);			}		catch (Exception e){}				return;			}// end of updateInfoPanel()	//*****************************************************************************************	//*		G E T  I M A G E  S I Z E	//*****************************************************************************************	protected void getImageSize(Image img)	{		do		{			image_width = img.getWidth(null);			image_height = img.getHeight(null);		}		while (image_width == -1 || image_height == -1);				return;			}// end of getImageSize()	}// end of class DataCompressor
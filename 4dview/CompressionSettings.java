import java.awt.*;

import quicktime.qd.*;
import quicktime.std.*;
import quicktime.std.image.*;
import quicktime.std.qtcomponents.*;

class CompressionSettings implements StdQTConstants
{
	//***** Compressor settings
	int				color_depth = 0;
	int				quality = codecNormalQuality;
	int				comp_type = kJPEGCodecType;
	CodecComponent	comp = CodecComponent.anyCodec;
	int				spatial_quality = codecNormalQuality;
	int				temporal_quality = codecNormalQuality;
	float			frames_per_second = 8;
	int				key_frame_rate = 10;
	int				data_rate = 600; // was 1000
	int				frame_duration = 60; // was 8
	int				min_spatial_quality = codecMinQuality;
	int				min_temporal_quality = codecMinQuality;
	boolean			settings_obtained = false;  // flag set when user has chosen the settings
								        		//  "false" indicates defaults are being used

	//*******************************************************************
	//*		I N I T
	//*******************************************************************
	public  CompressionSettings()
	{
		color_depth = 0; // let ICM choose depth
		quality = codecNormalQuality;// normal quality
		comp_type = kJPEGCodecType;// JPEG compressor
		comp = CodecComponent.anyCodec;
		spatial_quality = codecNormalQuality;
		temporal_quality = codecNormalQuality;
		frames_per_second = 8;
		key_frame_rate = 10;
	  	data_rate = 600; // was 1000
	  	frame_duration = 60; // was 8
		min_spatial_quality = codecMinQuality;
		min_temporal_quality = codecMinQuality;
		settings_obtained = false;

		return;

	}// init


	//*******************************************************************
	//*		G E T  C O D E C  S E T T I N G S
	//*		Last modified 10/10/02 for v2.11
	//*******************************************************************
	void	getCodecSettings(PixMap test_pix_map) throws CancelledException, Exception
	{
		ImageCompressionDialog	comp_dlog = null;
		SpatialSettings			spatial_settings = null;
		TemporalSettings		temporal_settings = null;
		DataRateSettings		data_rate_settings = null;
		QDRect					test_rect = null;
		int						test_flags = 0;
		boolean					cancelled = false;
		int						flags = 0;

		try
		{
			//***** Get the standard QT compression dialog
			comp_dlog = new ImageCompressionDialog();

			//***** Set the default image
			comp_dlog.setTestImagePixMap(test_pix_map,
   	                        		     test_rect,
   	                        		     test_flags);

			//***** Set up the default settings
			spatial_settings = new SpatialSettings(this.comp_type,
       	  									   this.comp,
       	 									   this.color_depth,
       										   this.spatial_quality);
			comp_dlog.setInfoSpatialSettings(spatial_settings);

			temporal_settings = new TemporalSettings(this.temporal_quality,
       		                   						 this.frames_per_second,
       		                   						 this.key_frame_rate);
			comp_dlog.setInfoTemporalSettings(temporal_settings);

			data_rate_settings = new DataRateSettings(this.data_rate,
   		                       						  this.frame_duration,
   	   	                    						  this.min_spatial_quality,
       		                   						  this.min_temporal_quality);
			comp_dlog.setInfoDataRateSettings(data_rate_settings);

			//***** Set the default such that BEST DEPTH is not an option
			flags &= ~scShowBestDepth;
			comp_dlog.setInfoPreferences(flags);

			//***** Display the dialog for the user
			try
			{
				comp_dlog.requestSequenceSettings();
			}
			catch (Error er)
			{
				cancelled = true;
				throw new CancelledException();
			}

			if (!cancelled)
			{
				//***** Get the user settings
				spatial_settings = comp_dlog.getInfoSpatialSettings();
					this.comp_type = spatial_settings.getCodecType();
					this.comp = spatial_settings.getCodec();
					this.color_depth = spatial_settings.getDepth();
					this.spatial_quality = spatial_settings.getSpatialQuality();
				temporal_settings = comp_dlog.getInfoTemporalSettings();
					this.temporal_quality = temporal_settings.getTemporalQuality();
					this.frames_per_second = temporal_settings.getFrameRate();
					this.key_frame_rate = temporal_settings.getKeyFrameRate();
				data_rate_settings = comp_dlog.getInfoDataRateSettings();
					this.data_rate = data_rate_settings.getDataRate();
					this.frame_duration = data_rate_settings.getFrameDuration();
					this.min_spatial_quality = data_rate_settings.getMinSpatialQuality();
					this.min_temporal_quality = data_rate_settings.getMinTemporalQuality();
			}//if we didn't cancel

		}// try
		catch (StdQTException sqe)
		{
			if (sqe.errorCode() == -128) // userCancelledErr
				throw new CancelledException();
			else
				throw sqe;
		}
		catch (Exception e)
		{
			throw e;
		}
		return;

	}// end of getCodecSettings()

}//end of class CompressionSettings

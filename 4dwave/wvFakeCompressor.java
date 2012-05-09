// This class emulates the class wv4DBWCompressor, except it does
// not do any actual wavelet compression. It simply ignores the input images. 
// That makes the class useful for testing a user interface without having
// access to the actual wavelet library.
import java.awt.Image;
import java.io.DataOutputStream;
import java.awt.image.PixelGrabber;

import wvlib.*;

public class wvFakeCompressor
{
  public wvFakeCompressor( float p, float threshold,
			   java.io.DataOutputStream outStream,
			   int actualNumFirstTimepoint,
			   String baseFileName,
			   int numTimepointsCompressed,
			   int numBitfilesCreated,
			   int numFocalplanesInStack,
			   int numBlocks, int numTimePointsInEachBlock,
			   short grayLevel) throws java.io.IOException
    {
      // Exception handling.
      if (threshold <= 0)
	throw new java.lang.IllegalArgumentException("Threshold must be > 0.");
      if (numBlocks <= 0)
	throw new java.lang.IllegalArgumentException("Number of blocks must be > 0.");
      if (outStream == null)
	throw new java.lang.IllegalArgumentException("DataOutputStream must be non-null");
      if (numTimePointsInEachBlock <= 0)
	throw new java.lang.IllegalArgumentException("number of time points a block must be > 0.");
      m_outStream = outStream;
      m_numTimePoints = numTimePointsInEachBlock;
      m_outStream.writeInt(m_version);
      m_outStream.writeInt(actualNumFirstTimepoint);

      m_outStream.writeUTF(baseFileName);
      m_outStream.writeInt(numTimepointsCompressed);
      m_outStream.writeInt(numBitfilesCreated);

      m_outStream.writeInt(numFocalplanesInStack);
      m_outStream.writeInt(numBlocks);
      switch(grayLevel)
      {
      case wvGraylevels.GRAY8BIT : 
	break;
      case wvGraylevels.GRAY16BIT   : 
	break;
      case wvGraylevels.GRAY24BIT   : 
	break;
      default:
	throw new java.lang.IllegalArgumentException("Gray level must be set to one of 8bit, 16bit or 24bit.");
      }
      m_outStream.writeShort(grayLevel);
    }

  public void setBlockSize(int width, int height,
			   int numFocalPlanesInaBlock)
    {
      if (width <= 0)
	throw new java.lang.IllegalArgumentException("width must be > 0.");
      if (height <= 0)
	throw new java.lang.IllegalArgumentException("height must be > 0.");
      if (numFocalPlanesInaBlock <= 0)
	throw new java.lang.IllegalArgumentException("number of focal planes in a block must be > 0.");

      if (m_width != width || m_height != height
	  || m_numFocalPlanes != numFocalPlanesInaBlock)
      {
	m_numFocalPlanes = numFocalPlanesInaBlock;
	m_width = width;
	m_height = height;
      }
    }
  public void setImage(Image img, int spaceIndex, int timeIndex)
    {
      if (spaceIndex < 0 || spaceIndex > m_numFocalPlanes)
      {
	String s = "focal plane index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numFocalPlanes - 1)
	  + " but it was " + String.valueOf(spaceIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      if (timeIndex < 0 || timeIndex > m_numTimePoints)
      {
	String s = "time point index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numTimePoints - 1)
	  + " but it was " + String.valueOf(timeIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      /////////
      // Finished checking input arguments.
      //
    }
  public void setImage(int[][] val, int spaceIndex, int timeIndex)
    {
      if (spaceIndex < 0 || spaceIndex > m_numFocalPlanes)
      {
	String s = "focal plane index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numFocalPlanes - 1)
	  + " but it was " + String.valueOf(spaceIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      if (timeIndex < 0 || timeIndex > m_numTimePoints)
      {
	String s = "time point index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numTimePoints - 1)
	  + " but it was " + String.valueOf(timeIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      /////////
      // Finished checking input arguments.
      //
    }
  public void doWaveletTransform()
    {
    }
  public void writeEncoded() throws java.io.IOException
    {
      //////
      // We only write the dimensions of the input images to the 
      // output stream.
      // This will allow the decoder to read these same dimensions
      // from file and then make up some images of that same size.
      m_outStream.writeInt(m_numFocalPlanes);
      m_outStream.writeInt(m_numTimePoints);
      m_outStream.writeInt(m_width);
      m_outStream.writeInt(m_height);
    }
  private int m_numFocalPlanes = 0;
  private int m_numTimePoints = 0;
  private int m_width = 0;
  private int m_height = 0;
  private java.io.DataOutputStream m_outStream;
  final private int m_version = 1;
}

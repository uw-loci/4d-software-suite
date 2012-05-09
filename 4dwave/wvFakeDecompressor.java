// This class emulates the class wv4DBWDecompressor, except it does
// not do any actual wavelet decompression. It simply generates images.
// That makes the class useful for testing a user interface without having
// access to the actual wavelet library.
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import wvlib.*;


public class wvFakeDecompressor
{
  ///////////////////
  // A colormap is a function that assigns a (R, G, B) triple
  // to each pixel value. R, G and B are all between 0 and 255.
  // We assume that the number of significant bits in the
  // pixel values are 'numBits' and that they represent gray scale
  // values. 
     private ColorModel makeGrayscaleColorModel(int numBits,
                                                boolean invert)
     {
       int numGraylevels = (1 << numBits);
       byte[] red_LUT = new byte[numGraylevels];
       byte[] green_LUT = new byte[numGraylevels];
       byte[] blue_LUT = new byte[numGraylevels];
       // The R, G and B values will be between 0 and
       // standardGraylevels - 1.
       int standardGraylevels = 256; 
       for (int i = 0; i < numGraylevels; i++)
       {
         byte value = (byte)((int)(Math.floor(i*(double)(standardGraylevels)
                                        /(double)(numGraylevels))) & 0xff);
         if (invert)
         {
           red_LUT[numGraylevels - 1 - i] = value;
           green_LUT[numGraylevels - 1 - i] = value;
           blue_LUT[numGraylevels - 1 - i] = value;
         }
         else
         {
           red_LUT[i] = value;
           green_LUT[i] = value;
           blue_LUT[i] = value;
         }
       }
       return (new IndexColorModel(numBits, numGraylevels,
                                   red_LUT, green_LUT, blue_LUT));
/*

		byte[] 	red_LUT = null, green_LUT = null, blue_LUT = null;
		int 	i = 0;
		
		red_LUT = new byte[256];
		green_LUT = new byte[256];
		blue_LUT = new byte[256];
		
		if (invert)
			for (i = 0; i < 256; i++) 
			{
				red_LUT[255-i]=(byte)(i & 0xff);
				green_LUT[255-i]=(byte)(i & 0xff);
				blue_LUT[255-i]=(byte)(i & 0xff);
			}
		else
		{
			for (i = 0; i < 256; i++) 
			{
				red_LUT[i]=(byte)(i & 0xff);
				green_LUT[i]=(byte)(i & 0xff);
				blue_LUT[i]=(byte)(i & 0xff);
			}
		}
		
		return (new IndexColorModel(8, 256, red_LUT, green_LUT, blue_LUT));
  */                   
     }// end of makeGrayscaleColorModel()


  public wvFakeDecompressor(java.io.DataInputStream inStream) throws java.io.IOException,  Exception
    {
      if (inStream == null)
	throw new java.lang.IllegalArgumentException("DataInputStream must be non-null");
      /////////
      // Finished checking input arguments.
      //
      m_inStream = inStream;
      int version = m_inStream.readInt();
      if (version != m_version)
	throw new Exception("Incorrect version of encoded stream.");
      m_actualNumFirstTimepoint = m_inStream.readInt();

      m_basefilename = m_inStream.readUTF();
      m_numTimepointsCompressed = m_inStream.readInt();
      m_numBitfilesCreated = m_inStream.readInt();
      
      m_numFocalplanesInStack = m_inStream.readInt();
      m_numBlocks = m_inStream.readInt();
      m_graylevel = m_inStream.readShort();

      //System.out.println("Number of focalplanes in stack = " + getNumberOfPlanesInStack());
      //System.out.println("Number of blocks (in this file) = " + getNumberOfBlocks());
      switch(m_graylevel)
      {
      case wvGraylevels.GRAY8BIT : m_maxVal = (1L<< 8)  - 1L;
	break;
      case wvGraylevels.GRAY16BIT   : m_maxVal = (1L<< 16)  - 1L;
	break;
      case wvGraylevels.GRAY24BIT   : m_maxVal = (1L<< 24)  - 1L;
	break;
      default:
	throw new java.lang.Exception("Read illegal gray levels from file.\n"
				      + "Gray level must be set to one of 8bit, 16bit or 24bit");
      }
    }
  public void readEncoded() throws java.io.IOException
    {
      if (m_numBlocksRead >= m_numBlocks)
	throw new java.io.IOException("Reading past end of file.");
      m_focalplanesRead += m_numFocalPlanes;

      m_numFocalPlanes = m_inStream.readInt();
      m_numTimepoints = m_inStream.readInt();      
      m_width = m_inStream.readInt();
      m_height = m_inStream.readInt();
      //System.out.println("Number of focal planes (in this block) = " + m_numFocalPlanes);
      //System.out.println("Number of time points = " + m_numTimepoints);
      m_numBlocksRead++;

    }
  public void doInverseWaveletTransform()
    {
    }
  public Image getImage(int spaceIndex, int timeIndex) throws Exception
    {
      //System.out.println("Fetching focal plane " + spaceIndex + " time point " + timeIndex);
      if (spaceIndex < 0 || spaceIndex >= m_numFocalPlanes)
      {
	String s = "focal plane index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numFocalPlanes - 1)
	  + " but it was " + String.valueOf(spaceIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      if (timeIndex < 0 || timeIndex >= m_numTimepoints)
      {
	String s = "time point index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numTimepoints - 1)
	  + " but it was " + String.valueOf(timeIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      /////////
      // Finished checking input arguments.
      //
      int[] val = new int[m_width*m_height];
      int index = 0;
      this.getImage(val, spaceIndex, timeIndex);
      java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
      short numberOfBits = wvGraylevels.numberOfBits(m_graylevel);
      ColorModel cm = makeGrayscaleColorModel(numberOfBits, false);
      Image ret= tk.createImage(new MemoryImageSource(m_width, m_height,
                                                      cm, val, 0, m_width));
      return ret;
//       int[] val = new int[m_width*m_height];
//       this.getImage(val, spaceIndex, timeIndex);
//       java.awt.Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
//       return tk.createImage(new MemoryImageSource(m_width, m_height, val, 0, m_width));
    }
  public void getImage(int[] val, int spaceIndex, int timeIndex)
    {    
      if (val == null)
	throw new java.lang.IllegalArgumentException("Image vector must be non-null");
      if (spaceIndex < 0 || spaceIndex >= m_numFocalPlanes)
      {
	String s = "focal plane index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numFocalPlanes - 1)
	  + " but it was " + String.valueOf(spaceIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      if (timeIndex < 0 || timeIndex >= m_numTimepoints)
      {
	String s = "time point index must be no smaller than 0 and "
	  + "no greater than " + String.valueOf(m_numTimepoints - 1)
	  + " but it was " + String.valueOf(timeIndex);
	throw new java.lang.IllegalArgumentException(s);
      }
      /////////
      // Finished checking input arguments.
      //
      this.fillImage(val, spaceIndex, timeIndex);
    }
  public void getImage(int[][] val, int spaceIndex, int timeIndex)
    {
      if (val == null)
	throw new java.lang.IllegalArgumentException("Image vector must be non-null");
      if (val.length != this.getHeight())
	throw new java.lang.IllegalArgumentException("image array is of illegal size.");
      for (int row = 0; row < this.getHeight(); row++)
	if (val[row].length !=  this.getWidth())
	  throw new java.lang.IllegalArgumentException("image array is of illegal size.");

      if (spaceIndex < 0 || spaceIndex >= this.getNumberOfPlanesInCurrentBlock())
	throw new java.lang.IllegalArgumentException(
	  "Focal plane index must be no smaller than 0 and no greater than " 
	  + String.valueOf(this.getNumberOfPlanesInCurrentBlock() - 1)
	  + " but was " + spaceIndex);
      if (timeIndex < 0 || timeIndex >= this.getNumberOfTimepointsInEachBlock())
	throw new java.lang.IllegalArgumentException(
	  "Time point index must be no smaller than 0 and no greater than " 
	  + String.valueOf(this.getNumberOfTimepointsInEachBlock() - 1)
	  + " but was " + timeIndex);
      /////////
      // Finished checking input arguments.
      //
      int[] vec = new int[m_width*m_height];
      int count = 0;
      this.fillImage(vec, spaceIndex, timeIndex);
      for (int row = 0; row < m_height; row++)
	for (int col = 0; col < m_width; col++)
	  val[row][col] = vec[count++];
	
    }
  public int getNumberOfBlocks()
    {
      return m_numBlocks;
    }
  public int getWidth()
    {
      return m_width;
    }
  public int getHeight()
    {
      return m_height;
    }
  public int getNumberOfPlanesInCurrentBlock()
    {
      return m_numFocalPlanes;
    }
  public int getNumberOfTimepointsInEachBlock()
    {
      return m_numTimepoints;
    }
  public int getNumberOfPlanesInStack()
    {
      return  m_numFocalplanesInStack;
    }
  public int getActualNumberOfFirstTimepoint()
    {
      return m_actualNumFirstTimepoint;
    }
  public int getNumberOfTimepointsCompressed()
    {
      return m_numTimepointsCompressed;
    }
  public int getNumberOfBitfilesCreated()
    {
      return m_numBitfilesCreated;
    }
  public String getBasefilename()
    {
      return m_basefilename;
    }
  public short getGrayLevel()
    {
      return m_graylevel;
    }
  private void fillImage(int[] val, int spaceIndex, int timeIndex)
    {
      for (int i = 0; i < val.length; i++)
	val[i] = 0;

      int spacelineWidth = Math.min(5, m_width/(2*spaceIndex + 1)); 
      int timelineWidth = Math.min(5, m_width/(2*timeIndex + 1)); 
      int spaceSpacing = Math.min(20, (m_width - (spaceIndex + 1)*spacelineWidth)/(spaceIndex + 1));
      int timeSpacing = Math.min(20, (m_width - (timeIndex + 1)*timelineWidth)/(timeIndex + 1));
      int lineHeight = Math.min(100, m_height/3);
      for (int i = 0; i < timeIndex + 1; i++)      
	this.putLine(val, 0, (i+1)*timeSpacing, lineHeight);

      for (int i = 0; i < m_focalplanesRead + spaceIndex + 1; i++)
	this.putLine(val, m_height/2, (i+1)*spaceSpacing, lineHeight);

    }
  private void putLine(int[] val, int startRow, int col, int numRows)
    {
      for (int c = col; c < col+5; c++)
	for (int row = startRow; row < startRow + numRows; row++)
	  val[c + row*m_width] = (int)(m_maxVal-1);
    }
  private java.io.DataInputStream m_inStream = null;
  private int m_width = 0;
  private int m_height = 0;
  private int m_actualNumFirstTimepoint = 0;
  private int m_numTimepointsCompressed = 0;
  private int m_numBitfilesCreated = 0;
  private String m_basefilename;
  private int m_numFocalplanesInStack = 0;
  private int m_numTimepoints = 0;
  private int m_numFocalPlanes = 0;
  private short m_graylevel = 0;
  private int m_numBlocks = 0;
  private int m_numBlocksRead = 0;
  private int m_focalplanesRead = 0;
  private int m_timepointsRead = 0;
  final private int m_version = 1;
  private long m_maxVal = 0;  
}

import java.io.*;

public class HeaderEntry implements Serializable
{

	int			focal_plane = 0; 		// focal plane that this header entry refers to
	long		frame = 0;				// frame that this header entry refers to
	long		offset_to_overlay = 0;	// offset in bytes from the start of the file to the start of the overlay data
	long		size_of_overlay = 0;	// size in bytes of the overlay data itself

	public HeaderEntry()
	{
		this.focal_plane = 0;
		this.frame = 0;
		this.offset_to_overlay = 0;
		this.size_of_overlay = 0;

		return;

	}// init

	public HeaderEntry(int plane, long frame)
	{
		this();
		this.focal_plane = plane;
		this.frame = frame;

		return;

	}// init

	public HeaderEntry(int plane, long frame, long offset, long size)
	{
		this(plane, frame);
		this.offset_to_overlay = offset;
		this.size_of_overlay = size;

		return;

	}// init


}// end of class HeaderEntry

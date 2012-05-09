import java.io.*;

public class BookmarkEntry implements Serializable
{

	String			name = null;
	int				focal_plane = 0;
	long			frame = 0;

	public BookmarkEntry()
	{
		this.name = new String("");
		this.focal_plane = 0;
		this.frame = 0;
	}// end of constructor method


	public BookmarkEntry(String name, int focal_plane, long frame)
	{
		this.name = new String(name);
		this.focal_plane = focal_plane;
		this.frame = frame;
	}// end of constructor method

}// end of class BookmarkEntry

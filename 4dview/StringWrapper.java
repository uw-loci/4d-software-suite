import java.awt.*;

public class StringWrapper
{
	String  text;

	public StringWrapper()
	{
		this.text = null;
	}// init()

	public StringWrapper(String str)
	{
		this.text = new String(str);
	}

	public void setString(String str)
	{
		this.text = new String(str);
	}

	public String getString()
	{
		if (this.text == null)
			return (null);
		else
			return (new String(this.text));
	}

} // StringWrapper

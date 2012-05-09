import java.awt.*;

public class LongWrapper
{
	long		value;

	public LongWrapper()
	{
		this.value = 0;
		return;

	}// init()

	public LongWrapper(long i)
	{
		this.value = i;
		return;
	}

	public void setValue(long i)
	{
		this.value = i;
		return;
	}

	public long getValue()
	{
		return(this.value);
	}

} // LongWrapper

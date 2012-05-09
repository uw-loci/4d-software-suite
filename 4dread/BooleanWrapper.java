import java.lang.*;

//***** A wrapper class for boolean values
class BooleanWrapper
{
	boolean		b;

	public BooleanWrapper()
	{
		b = false;

	}// init

	public BooleanWrapper(boolean value)
	{
		this();

		this.b = value;

		return;
	}// init

	public boolean  getValue()
	{
		return(this.b);

	}// end of getValue

	public void  setValue(boolean value)
	{
		this.b = value;

		return;
	}

}// end of class BooleanWrapper

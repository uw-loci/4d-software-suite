import java.awt.*;
import java.awt.event.*;

public class EdCommentDlog extends ObjCommentDlog
{

	//**********************************************************************
	//*		I N I T
	//*		Modified 10/15/02 for v1.66
	//*		Modified 6/30/03 for 1.74
	//**********************************************************************
	public EdCommentDlog(Frame parent, boolean modal, StringWrapper obj_desc_str,
						  StringWrapper obj_comments_str)
	{
		super((Frame)parent, modal, obj_desc_str, obj_comments_str);

		setTitle("Edit Object Comments");

		setupDlog();

		pack();

		return;

	}// end of init();


	//**********************************************************************
	//*		S E T U P  D L O G
	//*		Added 10/15/02 for v1.66
	//*		Modified 1/22/03 for v1.70
	//**********************************************************************
	protected void setupDlog()
	{

		super.setupDlog();

		object_descriptor_field.setEditable(true);
		object_comments_area.setEditable(true);

		return;

	}// end of setupDlog()


	//**********************************************************************
	//*		O K  B U T T O N
	//*		Modified 10/15/02 for v1.66
	//*		Modified 1/23/03 for v1.70
	//*		Modified 6/30/03 for 1.74
	//**********************************************************************
	void okButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		//was_cancelled.setValue(false);

		ods.setString(object_descriptor_field.getText());
		ocs.setString(object_comments_area.getText());

		setVisible(false);
		dispose();
	}

/*
	//**********************************************************************
	//*		C A N C E L  B U T T O N
	//*		Modified 1/23/03 for v1.70
	//**********************************************************************
	void cancelButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		was_cancelled.setValue(true);
		setVisible(false);
		dispose();
	}
*/
}// end of class EdCommenDlog


/*******************************************************************************
 * The contents of this file are subject to the Common Public Attribution License 
 * Version 1.0 (the "License"); you may not use this file except in compliance with 
 * the License. You may obtain a copy of the License at 
 * http://www.projectlibre.com/license . The License is based on the Mozilla Public 
 * License Version 1.1 but Sections 14 and 15 have been added to cover use of 
 * software over a computer network and provide for limited attribution for the 
 * Original Developer. In addition, Exhibit A has been modified to be consistent 
 * with Exhibit B. 
 *
 * Software distributed under the License is distributed on an "AS IS" basis, 
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the 
 * specific language governing rights and limitations under the License. The 
 * Original Code is ProjectLibre. The Original Developer is the Initial Developer 
 * and is ProjectLibre Inc. All portions of the code written by ProjectLibre are 
 * Copyright (c) 2012-2019. All Rights Reserved. All portions of the code written by 
 * ProjectLibre are Copyright (c) 2012-2019. All Rights Reserved. Contributor 
 * ProjectLibre, Inc.
 *
 * Alternatively, the contents of this file may be used under the terms of the 
 * ProjectLibre End-User License Agreement (the ProjectLibre License) in which case 
 * the provisions of the ProjectLibre License are applicable instead of those above. 
 * If you wish to allow use of your version of this file only under the terms of the 
 * ProjectLibre License and not to allow others to use your version of this file 
 * under the CPAL, indicate your decision by deleting the provisions above and 
 * replace them with the notice and other provisions required by the ProjectLibre 
 * License. If you do not delete the provisions above, a recipient may use your 
 * version of this file under either the CPAL or the ProjectLibre Licenses. 
 *
 *
 * [NOTE: The text of this Exhibit A may differ slightly from the text of the notices 
 * in the Source Code files of the Original Code. You should use the text of this 
 * Exhibit A rather than the text found in the Original Code Source Code for Your 
 * Modifications.] 
 *
 * EXHIBIT B. Attribution Information for ProjectLibre required
 *
 * Attribution Copyright Notice: Copyright (c) 2012-2019, ProjectLibre, Inc.
 * Attribution Phrase (not exceeding 10 words): 
 * ProjectLibre, open source project management software.
 * Attribution URL: http://www.projectlibre.com
 * Graphic Image as provided in the Covered Code as file: projectlibre-logo.png with 
 * alternatives listed on http://www.projectlibre.com/logo 
 *
 * Display of Attribution Information is required in Larger Works which are defined 
 * in the CPAL as a work which combines Covered Code or portions thereof with code 
 * not governed by the terms of the CPAL. However, in addition to the other notice 
 * obligations, all copies of the Covered Code in Executable and Source Code form 
 * distributed must, as a form of attribution of the original author, include on 
 * each user interface screen the "ProjectLibre" logo visible to all users. 
 * The ProjectLibre logo should be located horizontally aligned with the menu bar 
 * and left justified on the top left of the screen adjacent to the File menu. The 
 * logo must be at least 144 x 31 pixels. When users click on the "ProjectLibre" 
 * logo it must direct them back to http://www.projectlibre.com. 
 *******************************************************************************/
package com.projectlibre1.dialog.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.undo.UndoableEditSupport;

import com.projectlibre1.dialog.FieldDialog;
import com.projectlibre1.field.Field;
import com.projectlibre1.field.FieldContext;
import com.projectlibre1.field.FieldParseException;
import com.projectlibre1.field.ObjectRef;
import com.projectlibre1.field.Select;
import com.projectlibre1.options.EditOption;
import com.projectlibre1.undo.FieldEdit;
import com.projectlibre1.util.Alert;
import com.projectlibre1.util.DateTime;

/**
 *
 */
public class FieldVerifier extends InputVerifier {
	protected FieldContext context = null;
	protected Field field;
	protected ObjectRef objectRef = null;
	protected Object source;
	protected Object value;
	protected Exception exception = null;
	protected boolean updating = false;
	boolean testing = false;
//	private UndoableEditSupport undoableEditSupport;
	/**
	 * @param value TODO
	 * 
	 */
	public FieldVerifier(Field field, ObjectRef objectRef, Object value/*,UndoableEditSupport undoableEditSupport*/) {
		super();
		this.field = field;
		this.objectRef = objectRef;
		setValue(value);
		this.source = this;
//		this.undoableEditSupport=undoableEditSupport;
	}
	
	/**
	 * Get the top level component.  For dates and spinners, the verification is triggered on a grandchild.  We need the grandparent
	 * @param component
	 * @return
	 */
	static JComponent valueHoldingComponent(JComponent component) {
		Object p = component.getParent();
		// for spinners and dates, need to go up to grandparent to get the control which holds the value
		if (p != null && p instanceof LookupField)
			p = ((LookupField)p).getDisplay();
		else if (p != null && p instanceof Component)
			p = ((Component)p).getParent();
		if (p instanceof JSpinner || p instanceof ExtDateField)
			component = (JComponent) p;
		return component;
	}


	
	/* (non-Javadoc)
	 * @see javax.swing.InputVerifier#verify(javax.swing.JComponent)
	 */
	public boolean verify(JComponent component) {
		if (updating) {
			return true;
		}

		FieldDialog parentFieldDialog = ComponentFactory.getParentFieldDialog(component);
		if (parentFieldDialog != null)
			parentFieldDialog.setDirtyComponent(null);
		
		
		JComponent c = component;
		component = valueHoldingComponent(component);
		
		component.setForeground(Color.BLACK);
		c.setForeground(Color.BLACK);
		
		Object newValue = ComponentFactory.getValueFromComponent(component, field);
//System.out.println("new value " + newValue + " " + (newValue != null ?newValue.getClass():""));
		
		// avoid validating unchanged controls
		if (newValue == value || (newValue != null && newValue.equals(value))) { //unchanged
			if (component instanceof JSpinner || component instanceof ExtDateField) { // if a spinner, check for modified text
				String text = ((JTextField)c).getText();
				try {
					if (!(component instanceof ExtDateField) || text.trim().length() > 0)
						newValue = field.getFormat().parseObject(text);
					else {
						((JTextField)c).setText(""); // put in empty text
						newValue = null;
					}
				} catch (ParseException e1) {
					exception = new FieldParseException(field.syntaxErrorForField());
					component.setForeground(Color.RED);
					c.setForeground(Color.RED);
					if (parentFieldDialog != null)
						parentFieldDialog.setDirtyComponent(c);

					return false;
				}
			} else {
				return true;
			}
		}
		if (newValue != null && value != null && newValue.toString().equals(value.toString()))
			return true;
		
		exception = null;
		try {
			if (field.hasOptions())  {
				if (newValue == null)
					newValue = Select.EMPTY;
				
				field.setText(objectRef,newValue.toString(),context);
			} else {
				if (field.isDate()) {
					if (newValue != null && newValue instanceof String) {
						try {
							newValue = EditOption.getInstance().getDateFormat().parseObject((String) newValue);
						} catch (ParseException e) {
						}
					}
					if (newValue == null || newValue.toString().trim().equals("")) // empty text on date is a null date
						newValue = DateTime.getZeroDate();
				}
				if (newValue != value){
					Object oldValue=field.getValue(objectRef, context);
					if (field.isMoney())
						field.setText(objectRef,""+newValue,context);
					else		
						field.setValue(objectRef,source,newValue,context);
					UndoableEditSupport undoableEditSupport=objectRef.getDataFactory().getUndoController().getEditSupport();
					if (undoableEditSupport!=null){
						undoableEditSupport.postEdit(new FieldEdit(field,objectRef,value,oldValue,this,context));
					}
				}
				
			}
		} catch (FieldParseException e) {
			exception = e;
			component.setForeground(Color.RED);
			c.setForeground(Color.RED);
			if (parentFieldDialog != null)
				parentFieldDialog.setDirtyComponent(c);
			return false;
		}
		setValue(newValue); // set to new value for next time
		return true;
	}

	
	/* (non-Javadoc)
	 * @see javax.swing.InputVerifier#shouldYieldFocus(javax.swing.JComponent)
	 */
	public boolean shouldYieldFocus(JComponent arg0) {
		if (testing) // sempaphore to protect infinite focus loop when popping up error dialog.  Does not need to be synchronized since the verifier is not shared
			return true;
		testing = true;
		boolean result = super.shouldYieldFocus(arg0);
		if (result == false)
			Alert.error(exception.getMessage(),arg0);
		testing = false;
		return result;
	}
	/**
	 * @param value The value to set.
	 */
	void setValue(Object value) {
		this.value = value;
	}

	/** A generic listener class that will validate on an event */
	public static class VerifierListener implements ActionListener {
	    public void actionPerformed(ActionEvent e){
	    	JComponent c = (JComponent)e.getSource();
	    	InputVerifier v = c.getInputVerifier();
	    	if (v != null) // on init, it is null
	    		v.verify(c);
	    }
	}
	public final void setUpdating(boolean doNotVerify) {
		this.updating = doNotVerify;
	}
	final Object getValue() {
		return value;
	}

	/**
	 * @return
	 */
	final boolean isUpdating() {
		return updating;
	}
	public final Exception getException() {
		return exception;
	}
}

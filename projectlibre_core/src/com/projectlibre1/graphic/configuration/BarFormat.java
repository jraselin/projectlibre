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
package com.projectlibre1.graphic.configuration;

import java.awt.Color;
import java.io.Serializable;

import org.apache.commons.digester.Digester;

import com.projectlibre1.configuration.Configuration;
import com.projectlibre1.configuration.FieldDictionary;
import com.projectlibre1.configuration.NamedItem;
import com.projectlibre1.field.Field;
import com.projectlibre1.functor.ScheduleIntervalGenerator;
import com.projectlibre1.strings.Messages;

/**
 *
 */
public class BarFormat implements NamedItem, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5622075758083562963L;
	public static final String category="BarFormatCategory";

	public BarFormat() {}
	public String getCategory() {
		return category;
	}
	
	/**
	 * @return Returns the end.
	 */
	public TexturedShape getEnd() {
		return end;
	}
	/**
	 * @param end The end to set.
	 */
	public void setEnd(TexturedShape end) {
		end.build();
		this.end = end;
	}
	/**
	 * @return Returns the from.
	 */
	public String getFrom() {
		return from;
	}
	/**
	 * @param from The from to set.
	 */
	public void setFrom(String from) {
		this.from = from;
		fromField = Configuration.getFieldFromId(from);
	}
	
	/**
	 * @return Returns the to.
	 */
	public String getTo() {
		return to;
	}
	/**
	 * @param to The to to set.
	 */
	public void setTo(String to) {
		this.to = to; 
		toField = Configuration.getFieldFromId(to);
	}
	/**
	 * @return Returns the line.
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param line The line to set.
	 */
	public void setRow(int line) {
		this.row = line;
	}
	/**
	 * @return Returns the middle.
	 */
	public TexturedShape getMiddle() {
		return middle;
	}
	/**
	 * @param middle The middle to set.
	 */
	public void setMiddle(TexturedShape middle) {
		middle.build();
		this.middle = middle;
	}
	
	public void setMiddle(TexturedShape middle, Color color) {
		middle.build(color);
		this.middle = middle;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
		setName(Messages.getString(id));
	}
	/**
	 * @return Returns the start.
	 */
	public TexturedShape getStart() {
		return start;
	}
	/**
	 * @param start The start to set.
	 */
	public void setStart(TexturedShape start) {
		start.build();
		this.start = start;
	}


	
	
	
	
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public String getIntervalGenerator() {
		return intervalGenerator;
	}
	public void setIntervalGenerator(String intervalGenerator) {
		this.intervalGenerator = intervalGenerator;
	}
	
	public ScheduleIntervalGenerator getScheduleIntervalGenerator() {
		if (intervalGenerator!=null&&scheduleIntervalGenerator==null){
			try {
				scheduleIntervalGenerator=(ScheduleIntervalGenerator)Class.forName(intervalGenerator).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return scheduleIntervalGenerator;
	}
	public boolean isMain(){ //compatibily
		return main||intervalGenerator!=null;
	}
	public void setMain(boolean main) {
		this.main = main;
	}
	
	public FormFormat getForm() {
		return form;
	}
	public void setForm(FormFormat form) {
		this.form = form;
	}
/**
 * Add digester events for the bar as well as the three sections.
 * The XML root is * /bar/shape
 * 	
 */
	public static void addDigesterEvents(Digester digester){
		// main properties of bar
		digester.addObjectCreate("*/bar/format", "com.projectlibre1.graphic.configuration.BarFormat");
	    digester.addSetProperties("*/bar/format");
		digester.addSetNext("*/bar/format", "add", "com.projectlibre1.configuration.NamedItem"); // add to dictionary
		
		// start section
		digester.addObjectCreate("*/bar/format/start", "com.projectlibre1.graphic.configuration.TexturedShape");
	    digester.addSetProperties("*/bar/format/start");
	    digester.addSetNext("*/bar/format/start", "setStart", "com.projectlibre1.graphic.configuration.TexturedShape");
	    
	    //middle section
		digester.addObjectCreate("*/bar/format/middle", "com.projectlibre1.graphic.configuration.TexturedShape");
	    digester.addSetProperties("*/bar/format/middle");
	    digester.addSetNext("*/bar/format/middle", "setMiddle", "com.projectlibre1.graphic.configuration.TexturedShape");
	    
	    //end section
		digester.addObjectCreate("*/bar/format/end", "com.projectlibre1.graphic.configuration.TexturedShape");
	    digester.addSetProperties("*/bar/format/end");
	    digester.addSetNext("*/bar/format/end", "setEnd", "com.projectlibre1.graphic.configuration.TexturedShape");
	    
	    //end section
		digester.addObjectCreate("*/bar/format/form", "com.projectlibre1.graphic.configuration.FormFormat");
	    digester.addSetProperties("*/bar/format/form");
	    digester.addSetNext("*/bar/format/form", "setForm", "com.projectlibre1.graphic.configuration.FormFormat");
		
	    FormFormat.addDigesterEvents(digester);
	    
	}	
	
	String name = null;
	String id = null;
	int row = 0;
	String intervalGenerator=null;
	ScheduleIntervalGenerator scheduleIntervalGenerator=null;
	String from;
	String to;
	Field fromField = null;
	Field toField = null;
	TexturedShape start = null;
	TexturedShape middle = null;
	TexturedShape end = null;
	FormFormat form=null;
	boolean main=false;
	public static final int MIN_FOREGROUND_LAYER=1;
	public static final int MAX_FOREGROUND_LAYER=499;
	public static final int MIN_LINK_LAYER=500;
	public static final int MAX_LINK_LAYER=999;
	public static final int MIN_BACKGROUND_LAYER=1000;
	public static final int MAX_BACKGROUND_LAYER=1499;
	int layer=MIN_BACKGROUND_LAYER;
	String fieldId=null;
	Field field=null; //for annotations
	
	/**
	 * @return Returns the category.
	 */
	/**
	 * @return Returns the fromField.
	 */
	public Field getFromField() {
		return fromField;
	}
	/**
	 * @return Returns the toField.
	 */
	public Field getToField() {
		return toField;
	}
	
	public int getNumberOfSections() {
		int count = 0;
		if (start != null)
			count++;
		if (middle != null)
			count++;
		if (end != null)
			count++;
		return count;
	}
	
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
		getField();
	}
	public Field getField(){
		if (field==null||field.getId()!=fieldId){
			if (fieldId==null) field=null;
			field=FieldDictionary.getInstance().getFieldFromId(fieldId);
		}
		return field;
	}

}

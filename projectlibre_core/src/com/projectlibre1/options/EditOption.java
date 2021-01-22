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
package com.projectlibre1.options;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import com.projectlibre1.util.DateTime;

/**
 *
 */
public class EditOption {
	private static EditOption instance = null;
	public static EditOption getInstance() {
		if (instance == null)
			instance = new EditOption();
		return instance;
	}
	private SimpleDateFormat dateFormat = null;
	private DateFormat shortDateFormat = null;
	
	private boolean isShort;
	private String customDateFormatPattern;
	

	/**
	 * 
	 */
	public EditOption() {
		super();
		//TODO integrate correct locale stuff here for web and client
	//	dateFormat = DateTime.dateFormatInstance("MM/dd/yy");
		dateFormat = DateTime.utcDateFormatInstance();
		shortDateFormat = DateTime.utcShortDateFormatInstance();
	}
	
	private boolean addSpaceBeforeLabel = true;
	
	/**
	 * @return Returns the addSpaceBeforeLabel.
	 */
	public boolean isAddSpaceBeforeLabel() {
		return addSpaceBeforeLabel;
	}
	/**
	 * @param addSpaceBeforeLabel The addSpaceBeforeLabel to set.
	 */
	public void setAddSpaceBeforeLabel(boolean addSpaceBeforeLabel) {
		this.addSpaceBeforeLabel = addSpaceBeforeLabel;
	}
	
	private int[] viewAs = new int[]{2,2,2,2,2,2,2}; // index into resource string of types array.  See DurationFormat
	public int getViewAs(int index) {
		if (index < 0)
			return 2; // hack for material resources
		return viewAs[index % (viewAs.length)];
	}
	public void setViewAs(int index, int value) {
		viewAs[index % (viewAs.length)] = value;
	}
	
	
	/**
	 * @return Returns the dateFormat.
	 */
	public final DateFormat getDateFormat() {
		return dateFormat;
	}
	/**
	 * @param dateFormat The dateFormat to set.
	 */
	public final void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	public DateFormat getShortDateFormat() {
		return shortDateFormat;
	}
	public void setShortDateFormat(DateFormat shortDateFormat) {
		this.shortDateFormat = shortDateFormat;
	}
	public boolean isShort() {
		return isShort;
	}
	public void setShort(boolean isShort) {
		this.isShort = isShort;
	}
	public String getCustomDateFormatPattern() {
		return customDateFormatPattern;
	}
	public  void setCustomDateFormatPattern(String customDateFormatPattern) {
		this.customDateFormatPattern = customDateFormatPattern;
		dateFormat = new SimpleDateFormat(customDateFormatPattern);
		
	}
}

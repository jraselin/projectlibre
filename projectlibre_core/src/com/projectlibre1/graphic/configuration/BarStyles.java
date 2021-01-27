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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.collections.Closure;
import org.apache.commons.digester.Digester;

import com.projectlibre1.configuration.NamedItem;
import com.projectlibre1.pm.task.Task;
import com.projectlibre1.strings.Messages;

/**
 * Styles of bars on the gantt chart.  Holds a collection of bar formats.
 */
public class BarStyles implements NamedItem {
//	static Log log = LogFactory.getLog(BarStyles.class);
	public static final String category="BarStylesCategory";

	public String getCategory() {
		return category;
	}

	String name = null;
	String id = null;
	ArrayList rows = new ArrayList();

	public BarStyles() {}
	/**
	 * Applies a closure to all bars which should be displayed.  The renderer is called back
	 * with the BarFormat to apply for bars which meet their display conditions.
	 * @param ganttable - A task, resource, assignment... whatever can be displayed in gantt
	 * @param action - Callback - The callback parametes are BarFormats
	 */
	public void apply(Object ganttable, Closure action) {
		apply(ganttable,action,false,false,false, false);
	}
	public void apply(Object ganttable, Closure action,boolean link,boolean annotation,boolean calendar, boolean horizontalGrid) {
		Iterator i = rows.iterator();
		BarStyle row;
		Task task = null;
		if(ganttable != null 
				&& ganttable instanceof Task) {
			task = (Task)ganttable;
		}
		boolean isTaskCustomBar = task != null && task.getBarFormat() != null && !task.isCritical() && !annotation;
		while (i.hasNext()) {
			row = (BarStyle)i.next();
			if (row.isLink()==link && row.isHorizontalGrid() == horizontalGrid &&row.isAnnotation()==annotation&&row.isCalendar()==calendar
					&& row.evaluate(ganttable)  
					) { // see if meets filter
				if(isTaskCustomBar 
						&& "Bar.task".equalsIgnoreCase(row.getFormatId())
						) {
					action.execute(((Task)(ganttable)).getBarFormat());
				} else {
					action.execute(row.getBarFormat());
				}
			}
		}
	}


	public void addStyle(BarStyle style) {
		style.setBelongsTo(this);
		style.build(); // set references
		rows.add(style);
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

	public void setId(String id) {
		this.id = id;
		setName(Messages.getString(id));
	}
	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return Returns the rows.
	 */
	public ArrayList getRows() {
		return rows;
	}


	String zoomX = null;
	public String getZoomX() {
		return zoomX;
	}
	public void setZoomX(String zoomX) {
		this.zoomX = zoomX;
	}
	String zoomY = null;
	public String getZoomY() {
		return zoomY;
	}
	public void setZoomY(String zoomY) {
		this.zoomY = zoomY;
	}

	double[] zoomRatioX=null;
	int defaultZoomIndexX;
	public double getRatioX(int zoom,boolean in){
		initZoomX();
		if (zoomX==null) return 1.0;
		int index=defaultZoomIndexX+zoom-((in)?0:1);
		if (index<0||index>=zoomRatioX.length) return 1.0;
		return (in)?zoomRatioX[index]:1.0/zoomRatioX[index];
	}
	protected void initZoomX(){
		if (zoomRatioX==null){
			if (zoomX==null) return;
			StringTokenizer st=new StringTokenizer(zoomX,",;:|");
			zoomRatioX=new double[st.countTokens()];
			int index=0;
			while (st.hasMoreTokens()){
				String s=st.nextToken();
				if ("*".equals(s)) defaultZoomIndexX=index;
				else zoomRatioX[index++]=Double.parseDouble(s);
			}
		}
	}
	double[] zoomRatioY=null;
	int defaultZoomIndexY;
	public double getRatioY(int zoom,boolean in){
		initZoomY();
		if (zoomY==null) return 1.0;
		int index=defaultZoomIndexY+zoom-((in)?0:1);
		if (index<0||index>=zoomRatioY.length) return 1.0;
		return (in)?zoomRatioY[index]:1.0/zoomRatioY[index];
	}
	protected void initZoomY(){
		if (zoomRatioY==null){
			if (zoomY==null) return;
			StringTokenizer st=new StringTokenizer(zoomY,",;:|");
			zoomRatioY=new double[st.countTokens()];
			int index=0;
			while (st.hasMoreTokens()){
				String s=st.nextToken();
				if ("*".equals(s)) defaultZoomIndexY=index;
				else zoomRatioY[index++]=Double.parseDouble(s);
			}
		}
	}

	public int getMinZoom(){
		initZoomX();
		initZoomY();
		if (zoomRatioX==null||zoomRatioY==null) return 0;
		return Math.min(-defaultZoomIndexX,-defaultZoomIndexY);
	}
	public int getMaxZoom(){
		initZoomX();
		initZoomY();
		if (zoomRatioX==null||zoomRatioY==null) return 0;
		return Math.min(zoomRatioX.length-defaultZoomIndexX-1,zoomRatioY.length-defaultZoomIndexY-1);
	}


	public static void addDigesterEvents(Digester digester){
		// main properties of bar
		digester.addFactoryCreate("*/bar/styles", "com.projectlibre1.graphic.configuration.BarStylesFactory");
	    digester.addSetProperties("*/bar/styles");
		digester.addSetNext("*/bar/styles", "add", "com.projectlibre1.configuration.NamedItem");

		// start section
		digester.addObjectCreate("*/bar/styles/style", "com.projectlibre1.graphic.configuration.BarStyle");
	    digester.addSetProperties("*/bar/styles/style");
	    digester.addSetNext("*/bar/styles/style", "addStyle", "com.projectlibre1.graphic.configuration.BarStyle");

	}
}

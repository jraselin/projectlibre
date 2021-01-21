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
package com.projectlibre1.pm.graphic.timescale;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.projectlibre1.pm.graphic.frames.DocumentFrame;
import com.projectlibre1.pm.graphic.spreadsheet.common.GradientCorner;
import com.projectlibre1.timescale.TimeScaleEvent;
import com.projectlibre1.timescale.TimeScaleListener;
import com.projectlibre1.workspace.SavableToWorkspace;
import com.projectlibre1.workspace.WorkspaceSetting;

/**
 *
 */
public class ScaledScrollPane extends JScrollPane implements TimeScaleListener, SavableToWorkspace {
	private static final long serialVersionUID = -6608484720122760191L;
	protected TimeScaleComponent timeScaleComponent;
	protected CoordinatesConverter coord;
	protected ScaledComponent main;
	protected DocumentFrame documentFrame;

	
	
	/**
	 * @param documentFrame
	 * 
	 */
	public ScaledScrollPane(ScaledComponent main,CoordinatesConverter coord, DocumentFrame documentFrame,int verticalIncrement) {
		super((JComponent)main,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.main=main;
		this.coord=coord;
		this.documentFrame=documentFrame;
		main.setCoord(coord);
		createLayout();
		coord.addTimeScaleListener(this);
		this.getVerticalScrollBar().setUnitIncrement(verticalIncrement);
		this.getHorizontalScrollBar().setUnitIncrement(coord.getTimescaleManager().getMinWidth());
		
	}

	public void createLayout(){
		setPreferredSize(new Dimension(300, 250));
		
	    //JViewport mainVP=new JViewport();
		//mainVP.setView((JComponent)main);
		
		timeScaleComponent=new TimeScaleComponent(coord);
		
		
		//JViewport tsVP=new JViewport();
		//tsVP.setView(timeScaleComponent);
		setColumnHeaderView(/*tsVP*/timeScaleComponent);
		
		
		getViewport().addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
			    updateTimeScaleComponentSize();
			}
		});
		
//		These buttons don't size correctly with substancde layout. They aren't needed anyway
//		Box zoom=new Box(BoxLayout.Y_AXIS);
//		JButton zoomIn=new JButton(IconManager.getIcon("timescale.zoomIn.icon"));
//		zoomIn.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				coord.zoomIn();
//			}
//		});
//		JButton zoomOut=new JButton(IconManager.getIcon("timescale.zoomOut.icon"));
//		zoomOut.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				coord.zoomOut();
//			}
//		});
//		zoom.add(zoomIn);
//		zoom.add(zoomOut);
		setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER,new GradientCorner());
	}
	
	public void timeScaleChanged(TimeScaleEvent e) {
		timeScaleComponent.repaint();
		this.getHorizontalScrollBar().setUnitIncrement(coord.getTimescaleManager().getMinWidth());
	}
	
	private Dimension olddmain=null;
	
	public void updateTimeScaleComponentSize(){
		Dimension dmain=getViewport().getViewSize();
		if (dmain.equals(olddmain)) return;
		olddmain=dmain;
		timeScaleComponent.setPreferredSize(new Dimension(dmain.width,timeScaleComponent.getPreferredSize().height));
		getColumnHeader().setViewSize(new Dimension(dmain.width,getColumnHeader().getViewSize().height));
	}
	
	public Component getTimeScaleComponent() {
		return timeScaleComponent;
	}
	
	protected JComponent emptyRowHeader=null;
	//protected JComponent emptyCorner=null;
	
	public void activateEmptyRowHeader(boolean activate){
		if (activate) activateEmptyRowHeader();
		else deactivateEmptyRowHeader();
	}
	
	public void activateEmptyRowHeader(/*int width*/){
		if (emptyRowHeader==null){
			int width=40;
			emptyRowHeader=new JPanel();
			emptyRowHeader.setBackground(getViewport().getView().getBackground());
			emptyRowHeader.setPreferredSize(new Dimension(width,(int)getViewport().getViewSize().getHeight()));
			JViewport header=new JViewport();
			header.setView(emptyRowHeader);
			header.setPreferredSize(emptyRowHeader.getPreferredSize());
			setRowHeader(header);
			
			/*emptyCorner=new JPanel();
			emptyRowHeader.setBackground(getColumnHeader().getView().getBackground());
			emptyCorner.setPreferredSize(new Dimension(width,(int)getColumnHeader().getViewSize().getHeight()));
			//JViewport corner=new JViewport();
			//corner.setView(emptyCorner);
			//corner.setPreferredSize(emptyCorner.getPreferredSize());
			setCorner(JScrollPane.UPPER_LEFT_CORNER,emptyCorner);*/
		}
	}
	public void deactivateEmptyRowHeader(){
		if (emptyRowHeader!=null){
	        getRowHeader().remove(emptyRowHeader);
	        remove(getRowHeader());
	        emptyRowHeader=null;
	        
	       // getCorner(JScrollPane.UPPER_LEFT_CORNER).remove(emptyCorner);
	        /*remove(emptyCorner);
	        emptyCorner=null;*/

		}

	}
	
	public void restoreWorkspace(WorkspaceSetting w, int context) {
		Workspace ws = (Workspace) w;
     	if (ws.viewPosition != null) {
     		getViewport().setViewPosition(ws.viewPosition);
     	}
	}
	public WorkspaceSetting createWorkspace(int context) {
		Workspace ws = new Workspace();
   		ws.viewPosition = getViewport().getViewPosition();
		return ws;
	}

	public static class Workspace implements WorkspaceSetting { 
		private static final long serialVersionUID = 8372367946057729222L;
		Point viewPosition = null;

		public Point getViewPosition() {
			return viewPosition;
		}

		public void setViewPosition(Point viewPosition) {
			this.viewPosition = viewPosition;
		}

	}
}

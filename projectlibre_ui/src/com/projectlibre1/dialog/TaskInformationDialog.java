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
package com.projectlibre1.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.SerializationUtils;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.projectlibre1.dialog.util.FieldComponentMap;
import com.projectlibre1.help.HelpUtil;
import com.projectlibre1.menu.MenuActionConstants;
import com.projectlibre1.pm.graphic.frames.DocumentFrame;
import com.projectlibre1.pm.graphic.frames.DocumentSelectedEvent;
import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.pm.graphic.gantt.Gantt;
import com.projectlibre1.pm.graphic.model.cache.NodeModelCache;
import com.projectlibre1.pm.graphic.spreadsheet.SpreadSheet;
import com.projectlibre1.pm.graphic.spreadsheet.SpreadSheetModel;
import com.projectlibre1.pm.graphic.spreadsheet.SpreadSheetUtils;
import com.projectlibre1.pm.graphic.views.GanttView;
import com.projectlibre1.pm.graphic.views.UsageDetailView;
import com.projectlibre1.association.AssociationList;
import com.projectlibre1.configuration.Configuration;
import com.projectlibre1.field.Field;
import com.projectlibre1.graphic.configuration.BarFormat;
import com.projectlibre1.graphic.configuration.BarStyle;
import com.projectlibre1.graphic.configuration.SpreadSheetCategories;
import com.projectlibre1.graphic.configuration.TexturedShape;
import com.projectlibre1.graphic.configuration.shape.Colors;
import com.projectlibre1.graphic.configuration.shape.PredefinedPaint;
import com.projectlibre1.graphic.configuration.shape.PredefinedShape;
import com.projectlibre1.pm.assignment.Assignment;
import com.projectlibre1.pm.assignment.AssignmentEntry;
import com.projectlibre1.pm.dependency.Dependency;
import com.projectlibre1.pm.dependency.DependencyNodeModelDataFactory;
import com.projectlibre1.pm.key.HasId;
import com.projectlibre1.pm.task.NormalTask;
import com.projectlibre1.pm.task.Task;
import com.projectlibre1.strings.Messages;
/**
 *
 */
public class TaskInformationDialog extends InformationDialog {
	private static final long serialVersionUID = 1L;

	public static TaskInformationDialog getInstance(Frame owner, Task task, boolean notes) {
		return new TaskInformationDialog(owner, task, notes);
	}

	private TaskInformationDialog(Frame owner, Task task, boolean notes) {
		super(owner, Messages.getString("TaskInformationDialog.TaskInformation")); //$NON-NLS-1$
		setObjectClass(Task.class);
		setObject(task);
		addDocHelp("Task_Information_Dialog");
		}

	private JTabbedPane taskTabbedPane;
	private int notesTabIndex;
	private int resourcesTabIndex;
	
	public void setObject(Object object) {
		super.setObject(object);
		String title = Messages.getString("TaskInformationDialog.TaskInformation");
		if (object != null)
			title += " - " + ((HasId)object).getId();
		this.setTitle(title);
		
		
		// JRA
		Task task = (Task)getObject();
		// WBS
		if(buttonChooseColor !=null && task != null) {
			buttonChooseColor.setEnabled(!task.isSummary());
		}
		
	}
	public JComponent createContentPanel() {	
	    	
		FormLayout layout = new FormLayout("350dlu:grow","fill:250dlu:grow"); //$NON-NLS-1$ //$NON-NLS-2$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		taskTabbedPane= new JTabbedPane();
		taskTabbedPane.addTab(Messages.getString("TaskInformationDialog.General"),createGeneralPanel()); //$NON-NLS-1$
		taskTabbedPane.addTab(Messages.getString("TaskInformationDialog.Predecessors"),createPredecessorsPanel()); //$NON-NLS-1$
		taskTabbedPane.addTab(Messages.getString("TaskInformationDialog.Successors"),createSuccessorsPanel()); //$NON-NLS-1$
		String resources = Messages.getString("TaskInformationDialog.Resources"); //$NON-NLS-1$
		taskTabbedPane.addTab(resources,createResourcesPanel());
		resourcesTabIndex = taskTabbedPane.indexOfTab(resources);

		taskTabbedPane.addTab(Messages.getString("TaskInformationDialog.Advanced"),createAdvancedPanel()); //$NON-NLS-1$
		
		String notes = Messages.getString("TaskInformationDialog.Notes"); //$NON-NLS-1$
		taskTabbedPane.addTab(notes,createNotesPanel());
		notesTabIndex = taskTabbedPane.indexOfTab(notes);
		builder.add(taskTabbedPane);
		mainComponent = taskTabbedPane;

		return builder.getPanel();
	}

	public void showNotes() {
		taskTabbedPane.setSelectedIndex(notesTabIndex);
	}
	public void showResources() {
		taskTabbedPane.setSelectedIndex(resourcesTabIndex);
	}

	protected JComponent createHeaderFieldsPanel(FieldComponentMap map) {
		// Repeat of fields from general tab 
		FormLayout layout = new FormLayout(
		        "p,3dlu,300dlu" //$NON-NLS-1$
				,"p,3dlu"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		map.append(builder,"Field.name"); //$NON-NLS-1$
		builder.nextLine(); // border at bottom
		return builder.getPanel();
	}
	
	
	private JComponent createGeneralPanel(){
		FieldComponentMap map = createMap();
		FormLayout layout = new FormLayout(
		        "max(50dlu;pref), 3dlu, 90dlu 10dlu, p, 3dlu,90dlu,60dlu", // extra padding on right is for estimated field //$NON-NLS-1$
				"p, 3dlu,p, 3dlu,p, 3dlu, p, 3dlu, p, 3dlu, p,3dlu, p, 3dlu,p, 3dlu, fill:50dlu:grow"); //$NON-NLS-1$

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		CellConstraints cc = new CellConstraints();
		builder.setDefaultDialogBorder();
		builder.add(createHeaderFieldsPanel(map),cc.xyw(builder.getColumn(), builder
				.getRow(), 8));
		
		
		builder.nextLine(2);
		map.appendSometimesReadOnly(builder,"Field.duration"); //$NON-NLS-1$
		map.append(builder,"Field.estimated"); //$NON-NLS-1$
		builder.nextLine(2);
		map.appendSometimesReadOnly(builder,"Field.percentComplete"); //$NON-NLS-1$
		map.append(builder,"Field.priority"); //$NON-NLS-1$
		
		builder.nextLine(2);
		map.append(builder,"Field.cost"); //$NON-NLS-1$
		map.append(builder,"Field.work"); //$NON-NLS-1$
		builder.nextLine(4);
		builder.addSeparator(Messages.getString("TaskInformationDialog.Dates")); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.start"); //$NON-NLS-1$
		map.append(builder,"Field.finish"); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.baselineStart"); //$NON-NLS-1$
		map.append(builder,"Field.baselineFinish"); //$NON-NLS-1$
		// JRA
		builder.nextLine(2);
		builder.add(createSelectColor());
		builder.nextLine(2);
		return builder.getPanel();
	}
	
	JButton buttonChooseColor;
	
	protected JComponent createSelectColor() {
		// Repeat of fields from general tab 
		FormLayout layout = new FormLayout(
		        "p,3dlu,50dlu" //$NON-NLS-1$
				,"p,3dlu"); //$NON-NLS-1$
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		
		 buttonChooseColor = new JButton("Choose color");
		buttonChooseColor.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent arg0) {
		    	
		    	Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.WHITE);
		    	if(newColor != null) {
			    	Gantt gantt = GanttView.gantt;
			    	Task task = (Task)getObject();
			    	BarFormat barFormat = task.getBarFormat();
			    	if(barFormat == null) {
			    		
				    	if(gantt != null) {
				    		BarStyle row;
				    		// barstyles global
				    		for (Object object : gantt.getBarStyles().getRows()) {
								row = (BarStyle) object;
								if("Bar.task".equalsIgnoreCase(row.getBarFormat().getId())){
									barFormat = new BarFormat();
									barFormat.setRow(row.getBarFormat().getRow());
									barFormat.setMain(row.getBarFormat().isMain());
									TexturedShape shape = new TexturedShape();
									TexturedShape middle = row.getBarFormat().getMiddle();
									shape.setPaintName(middle.getPaintName());
									shape.setShapeName(middle.getShapeName());
									shape.setStrokeName(middle.getStrokeName());
									barFormat.setMiddle(shape, newColor);
									barFormat.setFrom(row.getBarFormat().getFrom());
									barFormat.setTo(row.getBarFormat().getTo());
									barFormat.setFieldId("Field.name");
									task.setBarFormat(barFormat);
								}
							}
				    	}
			    	
		    	}
		    	barFormat.getMiddle().setColor(newColor);
		    	barFormat.getMiddle().setPaint(new PredefinedPaint(PredefinedPaint.find("SOLID"), newColor, Colors.findColor("WHITE")));
		    	
		    	
		    	gantt.updateUI();
		    	}
		    	
		    }
		});
		buttonChooseColor.setBounds(10, 11, 10, 23);
		
		builder.append("Couleur :",buttonChooseColor);
		builder.nextLine(); // border at bottom
		return builder.getPanel();
	}
	
	
	private JComponent createAdvancedPanel(){
		FieldComponentMap map = createMap();
		FormLayout layout = new FormLayout(
		        "max(50dlu;pref), 3dlu, 90dlu, 10dlu, p, 3dlu,90dlu,30dlu", // extra padding on right is for estimated field //$NON-NLS-1$
	    		  "p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu,p,3dlu, fill:50dlu:grow"); //$NON-NLS-1$

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.add(createHeaderFieldsPanel(map),cc.xyw(builder.getColumn(), builder
				.getRow(), 8));
		builder.nextLine(2);
		map.append(builder,"Field.wbs"); //$NON-NLS-1$
		map.append(builder,"Field.markTaskAsMilestone",3); //$NON-NLS-1$
		builder.nextLine(2);
		builder.addSeparator(Messages.getString("TaskInformationDialog.ConstrainTask")); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.constraintType"); //$NON-NLS-1$
		map.appendSometimesReadOnly(builder,"Field.constraintDate"); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.deadline"); //$NON-NLS-1$
		builder.nextLine(4);
		builder.addSeparator("	"); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.taskType"); //$NON-NLS-1$
		map.append(builder,"Field.effortDriven",3); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.taskCalendar"); //$NON-NLS-1$
		map.append(builder,"Field.ignoreResourceCalendar",3); //$NON-NLS-1$
		builder.nextLine(2);
		map.append(builder,"Field.earnedValueMethod"); //$NON-NLS-1$

		return builder.getPanel();
	}	
	
	public JComponent createPredecessorsPanel() {
		FieldComponentMap map = createMap();		
		FormLayout layout = new FormLayout("p:grow","p,3dlu,p,3dlu,fill:150dlu:grow"); //$NON-NLS-1$ //$NON-NLS-2$

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(createHeaderFieldsPanel(map),cc.xyw(builder.getColumn(), builder
				.getRow(), 1));
		builder.nextLine(2);
		builder.append(Messages.getString("Spreadsheet.Dependency.predecessors")+":"); //$NON-NLS-1$ //$NON-NLS-2$
		builder.nextLine(2);
		builder.add(createPredecessorsSpreadsheet());
		JComponent pred = builder.getPanel();
		HelpUtil.addDocHelp(pred,"Linking");
		return pred;	
	}
	
	private class DependencySpreadSheet extends SpreadSheet {
    	InformationDialog dlg;
		Field clickField;
		boolean predecessor;
    	DependencySpreadSheet(InformationDialog dlg, boolean predecessor) {
    		this.dlg = dlg;
    		this.clickField = Configuration.getFieldFromId(predecessor ? "Field.predecessorName" : "Field.successorName");
    		this.predecessor = predecessor;
    	}
    	public void doDoubleClick(int row, int col) {}
    	public void doClick(int row, int col) {
    		Object obj = getCurrentRowImpl();
    		if (obj!= null) {
				Field field = ((SpreadSheetModel)getModel()).getFieldInColumn(col+1);
				if (field == clickField) {
        			NormalTask pred = (NormalTask) (predecessor ? ((Dependency)obj).getLeft() : ((Dependency)obj).getRight());
        			dlg.setObject(pred);
        			dlg.updateAll();
        			pred.getDocument().getObjectSelectionEventManager().fire(this,pred);
				}
    		}
    	}
    	
		public Component prepareRenderer(TableCellRenderer renderer, int row,
				int column) {
			Component component =  super.prepareRenderer(renderer, row, column);
			Field field = ((SpreadSheetModel)getModel()).getFieldInColumn(column+1);
			if (field == clickField) {
				JLabel l = (JLabel)component;
				l.setText("<html><a href=\"\">" + l.getText() + "</a></html>");
			}
			return component;
		}
		
	}
	
	protected SpreadSheet predecessorsSpreadSheet;
 	public static final String DEPENDENCY_SPREADSHEET=SpreadSheetCategories.dependencySpreadsheetCategory;
    protected JScrollPane createPredecessorsSpreadsheet() {
    	final TaskInformationDialog self = this;
        predecessorsSpreadSheet = new DependencySpreadSheet(this,true);
		predecessorsSpreadSheet.setSpreadSheetCategory(DEPENDENCY_SPREADSHEET);
    	predecessorsSpreadSheet.setCanModifyColumns(false);
    	predecessorsSpreadSheet.setCanSelectFieldArray(false);
    	predecessorsSpreadSheet.setActions(new String[]{MenuActionConstants.ACTION_DELETE});
    	SpreadSheetUtils.createCollectionSpreadSheet(predecessorsSpreadSheet
				,(object==null)?new AssociationList():((Task)object).getPredecessorList()
				//,(object==null)?null:((NormalTask)object).getDocument()
				,"View.TaskInformation.Predecessors" //$NON-NLS-1$
				,DEPENDENCY_SPREADSHEET
				,"Spreadsheet.Dependency.predecessors" //$NON-NLS-1$
				,true
				,new DependencyNodeModelDataFactory()
				, 0
//				,false
//				,true
				);
	    return SpreadSheetUtils.makeSpreadsheetScrollPane(predecessorsSpreadSheet);

    }
    //cache reconstructed because the main cache holding edges isn't ordered
    protected void updatePredecessorsSpreadsheet() {
    	SpreadSheetUtils.updateCollectionSpreadSheet(predecessorsSpreadSheet
    					,(object==null)?new AssociationList():((Task)object).getPredecessorList()
						,new DependencyNodeModelDataFactory()
						, 0);
    }

	public JComponent createSuccessorsPanel() {
		FieldComponentMap map = createMap();		
		FormLayout layout = new FormLayout("p:grow","p,3dlu,p,3dlu,fill:150dlu:grow"); //$NON-NLS-1$ //$NON-NLS-2$

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(createHeaderFieldsPanel(map),cc.xyw(builder.getColumn(), builder
				.getRow(), 1));
		builder.nextLine(2);
		builder.append(Messages.getString("Spreadsheet.Dependency.successors")+":"); //$NON-NLS-1$ //$NON-NLS-2$
		builder.nextLine(2);
		builder.add(createSuccessorsSpreadsheet());
		JComponent succ = builder.getPanel();
		HelpUtil.addDocHelp(succ,"Linking");
		return succ;	
	}
	
	protected SpreadSheet successorsSpreadSheet;
    protected JScrollPane createSuccessorsSpreadsheet() {
        successorsSpreadSheet = new DependencySpreadSheet(this,false);
		successorsSpreadSheet.setSpreadSheetCategory(DEPENDENCY_SPREADSHEET);
    	successorsSpreadSheet.setCanModifyColumns(false);
    	successorsSpreadSheet.setCanSelectFieldArray(false);
    	successorsSpreadSheet.setActions(new String[]{MenuActionConstants.ACTION_DELETE});
    	
    	SpreadSheetUtils.createCollectionSpreadSheet(successorsSpreadSheet
				,(object==null)?new AssociationList():((Task)object).getSuccessorList()
				//,(object==null)?null:((NormalTask)object).getDocument()
				,"View.TaskInformation.Successors" //$NON-NLS-1$
				,DEPENDENCY_SPREADSHEET
				,"Spreadsheet.Dependency.successors" //$NON-NLS-1$
				,false
				,new DependencyNodeModelDataFactory()
				, 0
//				,false
//				,true
				);

	    return SpreadSheetUtils.makeSpreadsheetScrollPane(successorsSpreadSheet);

    }
    //cache reconstructed because the main cache holding edges isn't ordered
    protected void updateSuccessorsSpreadsheet() {
    	SpreadSheetUtils.updateCollectionSpreadSheet(successorsSpreadSheet
				,(object==null)?new AssociationList():((Task)object).getSuccessorList()
				,new DependencyNodeModelDataFactory()
				, 0);
    }

	public JComponent createResourcesPanel() {
		FieldComponentMap map = createMap();
		
		FormLayout layout = new FormLayout("p:grow,0dlu,right:p","p,3dlu,p,3dlu,fill:150dlu:grow"); //$NON-NLS-1$ //$NON-NLS-2$

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		builder.add(createHeaderFieldsPanel(map),cc.xyw(builder.getColumn(), builder
				.getRow(), 3));
		builder.nextLine(2);
		builder.append(Messages.getString("TaskInformationDialog.Resources") + ":",getAssignResourceButton()); //$NON-NLS-1$
		builder.nextLine(2);
		builder.add(createAssignmentSpreadsheet(),cc.xyw(builder.getColumn(), builder
				.getRow(), 3));
		JComponent panel = builder.getPanel();
		HelpUtil.addDocHelp(panel,"Assign_Resources");
		return panel;	
	}

	protected SpreadSheet assignmentSpreadSheet;
    protected JScrollPane createAssignmentSpreadsheet() {
        assignmentSpreadSheet = SpreadSheetUtils.createFilteredSpreadsheet(GraphicManager.getInstance(this).getCurrentFrame()
        							,false
									,"View.TaskInformation.Assignments" //$NON-NLS-1$
									,UsageDetailView.resourceAssignmentSpreadsheetCategory
									,"Spreadsheet.Assignment.resourceUsage" //$NON-NLS-1$
									,true
									//, 0
									,new String[]{MenuActionConstants.ACTION_DELETE}
									/*, new int[] {SpreadSheet.DELETE}*/);
        assignmentSpreadSheet.setActions(new String[]{MenuActionConstants.ACTION_DELETE});

		updateAssignmentSpreadsheet();
	    return SpreadSheetUtils.makeSpreadsheetScrollPane(assignmentSpreadSheet);

    }
    protected void updateAssignmentSpreadsheet() {
    	SpreadSheetUtils.updateFilteredSpreadsheet(assignmentSpreadSheet,(object==null)?new AssociationList():((NormalTask)object).getAssignments());
    	((SpreadSheetModel)assignmentSpreadSheet.getModel()).fireUpdateAll();
    }
    
	public void updateAll() {
		activateListeners();
		super.updateAll();
		updatePredecessorsSpreadsheet();
		updateSuccessorsSpreadsheet();
		updateAssignmentSpreadsheet();
	}
	
	public void documentSelected(DocumentSelectedEvent evt) {
		if (assignmentSpreadSheet==null) return;
        DocumentFrame df=evt.getCurrent();
        if (df!=null){
//        	List impls=df.getSelectedImpls();
//        	if (impls!=null&&impls.size()>0) setObject(impls.get(0));
        	NodeModelCache cache = df.createCache(false,Messages.getString("View.TaskInformation.Assignments")); //$NON-NLS-1$
			assignmentSpreadSheet.setCache(cache);
        }
	}
	
	
	protected void activateListeners() {
		super.activateListeners();
		predecessorsSpreadSheet.getCache().setReceiveEvents(true);
		successorsSpreadSheet.getCache().setReceiveEvents(true);
		//assignmentSpreadSheet.getCache().setReceiveEvents(true);
	}

	protected void desactivateListeners() {
		super.desactivateListeners();
		predecessorsSpreadSheet.getCache().setReceiveEvents(false);
		successorsSpreadSheet.getCache().setReceiveEvents(false);
		//assignmentSpreadSheet.getCache().setReceiveEvents(false); 
		//causes an update problem of the filtered cache
	}


	protected boolean hasHelpButton() {
		return true;
	}

	
}

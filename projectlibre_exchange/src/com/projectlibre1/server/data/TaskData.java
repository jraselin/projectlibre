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
 * Copyright (c) 2012. All Rights Reserved. All portions of the code written by 
 * ProjectLibre are Copyright (c) 2012. All Rights Reserved. Contributor 
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
 * Attribution Copyright Notice: Copyright (c) 2012, ProjectLibre, Inc.
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
package com.projectlibre1.server.data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */
public class TaskData extends SerializedDataObject {
	static final long serialVersionUID = 37382828283746L;
	public static final int CREATION_STATUS_NORMAL = 0;
	public static final int CREATION_STATUS_TIMESHEET = 1;

	protected CalendarData calendar;
    protected Collection assignments;
    protected Collection predecessors;
    protected TaskData parentTask;
    protected long childPosition;
    protected long parentTaskId=-1L;
    protected long calendarId=-1;
    protected boolean external = false;
    protected long projectId = 0L;
    protected long subprojectId = 0L; // for subproject tasks, what project they represent
    protected Map subprojectFieldValues = null;
    protected boolean timesheetCreated = false;
    protected String notes;
    protected transient Map attributes;
    // JRA
	private String paintName;
	private String shapeName;
	private String strokeName;
	private int row;
	private boolean main;
	private String from;
	private String to;
	private Color color;
	private String fieldId;
	
	public String getPaintName() {
		return paintName;
	}

	public void setPaintName(String paintName) {
		this.paintName = paintName;
	}

	public String getShapeName() {
		return shapeName;
	}

	public void setShapeName(String shapeName) {
		this.shapeName = shapeName;
	}

	public String getStrokeName() {
		return strokeName;
	}

	public void setStrokeName(String strokeName) {
		this.strokeName = strokeName;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean isMain() {
		return main;
	}

	public void setMain(boolean main) {
		this.main = main;
	}


// this code is to set fields which are exposed in database
//    protected long start;
//	  protected long finish;
//    protected long baselineStart;
//    protected long baselineFinish;
//    protected long completedThrough;
//    protected double percentComplete;



    public static final SerializedDataObjectFactory FACTORY=new SerializedDataObjectFactory(){
        public SerializedDataObject createSerializedDataObject(){
            return new TaskData();
        }
    };

    public Collection getAssignments() {
        return assignments;
    }
    public void setAssignments(Collection assignments) {
        this.assignments = assignments;
    }
    public CalendarData getCalendar() {
        return calendar;
    }
    public void setCalendar(CalendarData calendar) {
        this.calendar = calendar;
        setCalendarId((calendar==null)?-1L:calendar.getUniqueId());
    }

    /*public Collection getPredecessors() {
        return predecessors;
    }
    public void setPredecessors(Collection predecessors) {
        this.predecessors = predecessors;
    }*/
    public Collection getPredecessors() {
        return predecessors;
    }
    public void setPredecessors(Collection predecessors) {
        this.predecessors = predecessors;
    }
	/**
	 * @return Returns the childPosition.
	 */
	public long getChildPosition() {
		return childPosition;
	}
	/**
	 * @param childPosition The childPosition to set.
	 */
	public void setChildPosition(long childPosition) {
		this.childPosition = childPosition;
	}
	/**
	 * @return Returns the parentTask.
	 */
	public TaskData getParentTask() {
		return parentTask;
	}
	/**
	 * @param parentTask The parentTask to set.
	 */
	public void setParentTask(TaskData parentTask) {
		this.parentTask = parentTask;
        setParentTaskId((parentTask==null)?-1L:parentTask.getUniqueId());
	}


    public long getParentTaskId() {
		return parentTaskId;
	}
	public void setParentTaskId(long parentTaskId) {
		this.parentTaskId = parentTaskId;
	}
	public int getType(){
        return DataObjectConstants.TASK_TYPE;
    }
    public long getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(long calendarId) {
		this.calendarId = calendarId;
	}

	public void emtpy(){
    	super.emtpy();
    	calendar=null;
    	assignments=null;
    	predecessors=null;
    	parentTask=null;
    	external = false;
    }

	//syncronizer
	public void addAssignment(AssignmentData assignmentData){
		if (assignments==null) assignments=new ArrayList();
		assignmentData.setTask(this);
		assignments.add(assignmentData);
	}
	public void addPredecessor(LinkData linkData){
		if (predecessors==null) predecessors=new ArrayList();
		linkData.setSuccessor(this);
		predecessors.add(linkData);
	}
	public final boolean isExternal() {
		return external;
	}
	public final void setExternal(boolean external) {
		this.external = external;
	}
	public final long getProjectId() {
		return projectId;
	}
	public final void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public final boolean isSubproject() {
		return subprojectId != 0;
	}
	public final void setSubprojectId(long subprojectId) {
		this.subprojectId = subprojectId;
	}
	public final long getSubprojectId() {
		return subprojectId;
	}
	public final Map getSubprojectFieldValues() {
		return subprojectFieldValues;
	}
	public final void setSubprojectFieldValues(Map subprojectFieldValues) {
		this.subprojectFieldValues = subprojectFieldValues;
	}
	public boolean isTimesheetCreated() {
		return timesheetCreated;
	}
	public void setTimesheetCreated(boolean timesheetCreated) {
		this.timesheetCreated = timesheetCreated;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Map getAttributes() {
		return attributes;
	}
	public void setAttributes(Map attributes) {
		this.attributes = attributes;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getFieldId() {
		return fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

// this code is to set fields which are exposed in database
//    public long getStart() {
//		return start;
//	}
//	public void setStart(long start) {
//		this.start = start;
//	}
//	public long getFinish() {
//		return finish;
//	}
//	public void setFinish(long finish) {
//		this.finish = finish;
//	}
//	public long getBaselineStart() {
//		return baselineStart;
//	}
//	public void setBaselineStart(long baselineStart) {
//		this.baselineStart = baselineStart;
//	}
//	public long getBaselineFinish() {
//		return baselineFinish;
//	}
//	public void setBaselineFinish(long baselineFinish) {
//		this.baselineFinish = baselineFinish;
//	}
//	public long getCompletedThrough() {
//		return completedThrough;
//	}
//	public void setCompletedThrough(long completedThrough) {
//		this.completedThrough = completedThrough;
//	}
//	public double getPercentComplete() {
//		return percentComplete;
//	}
//	public void setPercentComplete(double percentComplete) {
//		this.percentComplete = percentComplete;
//	}

}

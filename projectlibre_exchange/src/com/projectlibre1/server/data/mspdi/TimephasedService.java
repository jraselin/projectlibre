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
package com.projectlibre1.server.data.mspdi;

import java.util.Calendar;

import com.projectlibre1.algorithm.Query;
import com.projectlibre1.algorithm.RangeIntervalGenerator;
import com.projectlibre1.algorithm.SelectFrom;
import com.projectlibre1.pm.assignment.Assignment;
import com.projectlibre1.pm.assignment.HasTimeDistributedData;
import com.projectlibre1.pm.assignment.TimeDistributedHelper;
import com.projectlibre1.pm.assignment.functor.AssignmentFieldFunctor;
import com.projectlibre1.pm.scheduling.Schedule;
import com.projectlibre1.util.DateTime;

import net.sf.mpxj.mspdi.schema.ObjectFactory;
import net.sf.mpxj.mspdi.schema.TimephasedDataType;

/**
 *
 */
public class TimephasedService {
	protected static TimephasedService instance=null;
	protected TimephasedService() {
	}

	public static TimephasedService getInstance(){
		if (instance==null) instance=new TimephasedService();
		return instance;
	}

	private void doQuery(Assignment assignment, ObjectFactory factory, TimephasedConsumer consumer,Object fieldType, int type, long id) {
		SelectFrom clause = SelectFrom.getInstance();
		AssignmentFieldFunctor dataFunctor = assignment.getDataSelect(fieldType,clause,false);
		TimephasedGetter getter = TimephasedGetter.getInstance(factory,consumer,dataFunctor,type,id);
		long end = assignment.getEnd();
		long start = assignment.getStart();

		if (fieldType == HasTimeDistributedData.ACTUAL_WORK)
			end = assignment.getStop();
		else if (fieldType == HasTimeDistributedData.REMAINING_WORK)
			start = assignment.getStop();
		RangeIntervalGenerator dailyInRange = RangeIntervalGenerator.getInstance(start, end, Calendar.DATE);

		Query.getInstance().selectFrom(clause)
		.groupBy(dailyInRange)
		.action(getter)
		.execute();
	}
	public void consumeTimephased(Schedule schedule,TimephasedConsumer consumer,Object factory){ //claur removed exception
		ObjectFactory mspdiTimephasedFactory=(ObjectFactory)factory;

		if (!(schedule instanceof Assignment))
			return; // only do assignments
		Assignment assignment = (Assignment)schedule;

		long id = 0;

		if ( assignment.getPercentComplete() > 0) {
			doQuery(assignment,mspdiTimephasedFactory, consumer,HasTimeDistributedData.ACTUAL_WORK, TimeDistributedTypeMapper.ASSIGNMENT_ACTUAL_WORK, id++);
		}
		doQuery(assignment,mspdiTimephasedFactory, consumer,HasTimeDistributedData.REMAINING_WORK, TimeDistributedTypeMapper.ASSIGNMENT_REMAINING_WORK, id++);


		Object fields[] = HasTimeDistributedData.baselineWorkTypes;
		Assignment baselineAssignment;
		for (int i = 0; i < fields.length; i++) {
			baselineAssignment = assignment.getBaselineAssignment(new Integer(i), false);
			if (baselineAssignment == null)
				continue;
			int mapType = TimeDistributedTypeMapper.getTimeDistributedType(i,false,baselineAssignment);
			doQuery(baselineAssignment,mspdiTimephasedFactory, consumer,HasTimeDistributedData.WORK, mapType, id++);
		}
	}

	public void readTimephased (Assignment assignment,TimephasedDataType t) {
		// if reading current info, do not bother unless the contour is nonstandard
//		if (TimeDistributedTypeMapper.isCurrent(t.getType().intValue()))// && assignment.getWorkContourType() != ContourTypes.CONTOURED)
//			return;

		if (!TimeDistributedHelper.isWork(t.getType())) //TODO do not treat costs for now
			return;

		Object type = TimeDistributedTypeMapper.getOPPrField(t.getType());

		// do not treat current values for non contoured assignments
//		if (TimeDistributedTypeMapper.isCurrent(t.getType().intValue()) && assignment.getWorkContourType() != ContourTypes.CONTOURED)
//			return;
		long duration = XsdDuration.millis(t.getValue());
		assignment.setInterval(type,DateTime.gmt(t.getStart().getTime()),DateTime.gmt(t.getFinish().getTime()), duration);
	}
}

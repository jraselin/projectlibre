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
package com.projectlibre1.algorithm;

import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An interval generator which is itself contains a collection of one or more other generators
 */
public class IntervalGeneratorSet implements IntervalGenerator {
	private IntervalGenerator currentIntervalGenerator = null;
	private LinkedList generators = null;
	private boolean sameEarliestEnding = true; // flag if more than one of the earliest generators has the same start time
	public Collection getGenerators() {
		return generators;
	}
	public static IntervalGenerator extractUnshared(Collection generatorList) {
		Iterator i = generatorList.iterator();
		IntervalGenerator current;
		while (i.hasNext()) {
			current = (IntervalGenerator) i.next();
			if (!current.canBeShared()) {
				i.remove();
				if (generatorList.isEmpty()) // list can't be empty. The fields themselves wouldn't be evalulated
					generatorList.add(RangeIntervalGenerator.continuous());
				return current;
			}
		}
		return null;
	}

	public static IntervalGeneratorSet getInstance(LinkedList intervalGenerators) {
		return new IntervalGeneratorSet(intervalGenerators);
	}

	public static IntervalGeneratorSet getInstance(IntervalGenerator intervalGenerator) {
		return new IntervalGeneratorSet(intervalGenerator);
	}

	public static IntervalGeneratorSet getInstance(IntervalGenerator intervalGenerator1, IntervalGenerator intervalGenerator2) {
		return new IntervalGeneratorSet(intervalGenerator1, intervalGenerator2);
	}

	/**
	 * The earliest ending interval is returned. In case of a tie, priority goes to the groupBy generator, and then
	 * the order of the from list
	 * @return
	 */
	protected IntervalGenerator earliestEndingGenerator() {
		long minEnd = Long.MAX_VALUE;
		IntervalGenerator result = null;
		if (generators != null) {
			long generatorEnd;
			Iterator i = generators.iterator();
			IntervalGenerator current;
			while (i.hasNext()) {
				current = (IntervalGenerator) i.next();
				generatorEnd = current.currentEnd();
				if (generatorEnd == minEnd)
					sameEarliestEnding = true;
				if (generatorEnd < minEnd) {
					minEnd = generatorEnd;
					result = current;
				}
			}
		}
	
		return result;
	}
	
//	private long earliestStart() {
//		long minStart = Long.MAX_VALUE;
//		if (generators != null) {
//			long generatorStart;
//			Iterator i = generators.iterator();
//			IntervalGenerator current;
//			while (i.hasNext()) {
//				current = (IntervalGenerator) i.next();
//				generatorStart = current.currentStart();
//				sameStart = generatorStart == minStart; // keep track if there is more than one with same start
//				if (generatorStart < minStart) {
//					minStart = generatorStart;
//				}
//			}
//		}
//		return minStart;
//	}		
	
	/**
	 * 
	 */
	private IntervalGeneratorSet(LinkedList intervalGenerators) {
		super();
		generators = intervalGenerators;
		initialize();
	}

	private IntervalGeneratorSet(IntervalGenerator intervalGenerator) {
		if (generators == null)
			generators = new LinkedList();
		generators.add(intervalGenerator);
		initialize();
	}

	private IntervalGeneratorSet(IntervalGenerator intervalGenerator1, IntervalGenerator intervalGenerator2) {
		if (generators == null)
			generators = new LinkedList();
		generators.add(intervalGenerator1);
		generators.add(intervalGenerator2);		
		initialize();		
	}
	
	public void remove(IntervalGenerator removeMe) {
		generators.remove(removeMe);
	}

	public boolean isEmpty() {
		return generators.isEmpty();
	}
	
	private void initialize() {
		currentIntervalGenerator = earliestEndingGenerator();
		if (currentIntervalGenerator == null)
			System.out.println("0 intialize" + currentIntervalGenerator);
	}
	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.IntervalGenerator#current()
	 */
	public Object current() {
		return currentIntervalGenerator.current();
	}

	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.IntervalGenerator#currentEnd()
	 */
	public long currentEnd() {
		return currentIntervalGenerator.currentEnd();		
	}

	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.IntervalGenerator#currentStart()
	 */
	public long currentStart() {
		return currentIntervalGenerator.currentStart();
	}

	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.IntervalGenerator#isCurrentActive()
	 */
	public boolean isCurrentActive() {
		return currentIntervalGenerator.isCurrentActive();
	}

	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.IntervalGenerator#hasNext()
	 */
	public boolean hasNext() {
		return currentIntervalGenerator != null;
	}

	/* (non-Javadoc)
	 * @see com.projectlibre1.algorithm.IntervalGenerator#canBeShared()
	 */
	public boolean canBeShared() {
		return false;
	}

	public boolean evaluate(Object arg0) {
		boolean result = true;
		currentIntervalGenerator = earliestEndingGenerator();
		if (currentIntervalGenerator == null)
			return false;
		
		// it is fairly common that two or more generators share the same endpoint.  If so, they all must be evaluated
		if (sameEarliestEnding) {
			long earliestEnd = currentIntervalGenerator.currentEnd();
			Iterator i = generators.iterator();
			IntervalGenerator current;
			while (i.hasNext()) {
				current = (IntervalGenerator) i.next();
				if (current.currentEnd() == earliestEnd) { // see if this generator is at the earliest end point too
					if (!current.evaluate(arg0))
						result = false;
				}
			}
		} else { // only one generator
			result = currentIntervalGenerator.evaluate(arg0);	
		}
		return result;
	}
}
		
		


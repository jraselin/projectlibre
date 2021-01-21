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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;

import javax.swing.JPanel;
import javax.swing.UIManager;

import com.projectlibre1.pm.graphic.frames.GraphicManager;
import com.projectlibre1.pm.graphic.gantt.GanttParams;
import com.projectlibre1.graphic.configuration.GraphicConfiguration;
import com.projectlibre1.timescale.TimeInterval;
import com.projectlibre1.timescale.TimeIterator;
import com.projectlibre1.util.Environment;


/**
 *
 */
public class TimeScaleComponent extends JPanel {
	protected CoordinatesConverter coord;
	protected static Color textColor,lineColor;
	/**
	 *
	 */
	public TimeScaleComponent(CoordinatesConverter coord) {
		super();
		this.coord=coord;


		int h=GraphicConfiguration.getInstance().getColumnHeaderHeight();


		if (textColor==null) textColor=Color.BLACK;
		if (lineColor==null) lineColor=Environment.isMac()?Color.LIGHT_GRAY:Color.BLACK;

//		setBackground(UIManager.getColor("TableHeader.cellColor"));
//		setBackground(UIManager.getColor("TableHeader.cellBackground"));

		//setBackground(LafUtils.getUnselectedBackgroundColor());

		//setMinimumSize(new Dimension(0,h));
		//setMaximumSize(new Dimension(Integer.MAX_VALUE,h));


		setPreferredSize(new Dimension(0,h));


		if (Environment.isMac()){
			setBackground(GraphicManager.getInstance().getLafManager().getUnselectedBackgroundColor()); //Using ColorUIResource directly doesn't work
		}
		else setBorder(UIManager.getBorder ("TableHeader.cellBorder"));
	}


	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
        paintTimeScale(g2,coord,getFont(),new Dimension(0,getHeight()),true);
	}

	public static void paintTimeScale(Graphics2D g2,GanttParams params,Font font) {
        paintTimeScale(g2,params.getCoord(),font,new Dimension((int)params.getGanttBounds().getWidth(),params.getConfiguration().getColumnHeaderHeight()),false);
	}

	public static void paintTimeScale(Graphics2D g2,CoordinatesConverter coord,Font font,Dimension d,boolean clipping){
		Rectangle clipBounds = g2.getClipBounds();
		double h=d.getHeight();
		double x0,w;
		if (clipping){
			x0=clipBounds.getX();
			w=clipBounds.getWidth();//getWidth();
			GraphicManager.getInstance().getLafManager().paintTimeScale(g2, clipBounds.x, 0,clipBounds.width,d.height, new Shape[]{
					new Line2D.Double(x0,0,x0+w,0),
					new Line2D.Double(x0,h-1,x0+w,h-1),
			});
		}else{
			x0=0;
			w=d.getWidth();
		}

		g2.setColor(lineColor);
		if (Environment.isMac()){
			g2.draw(new Line2D.Double(x0,h-1,x0+w,h-1));
		}
		g2.draw(new Line2D.Double(x0,h/2,x0+w,h/2));


		TimeIterator i=coord.getTimeIterator(x0,x0+w);
		//g2.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setFont(new Font("Courrier", Font.PLAIN, 12));
		g2.setFont(/*UIManager.getFont("TableHeader.cellFont")*/font);
		//Font[]  fonts=GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		//for (int k=0;k<fonts.length;k++)
		FontRenderContext context = g2.getFontRenderContext();

		long start=-1;
		long end=-1;

		while(i.hasNext()){
			TimeInterval interval=i.next();
			if (start==-1) start=interval.getStart1();
			end=interval.getEnd1();

			double x1=coord.toX(interval.getStart1());
			double x2=coord.toX(interval.getEnd1());
			g2.setColor(lineColor);
			if (clipping) g2.draw(new Line2D.Double(x1,h/2,x1,h)); //when scrolling pixel by pixel both lines are needed
			g2.draw(new Line2D.Double(x2,h/2,x2,h));

			String text=interval.getText1();
			LineMetrics metrics=font.getLineMetrics(text,context);
			g2.setColor(textColor);
			g2.drawString(text,(int)x1+2,((int)h)-metrics.getDescent()-metrics.getLeading());

			if (interval.getText2()!=null){
				double X1=/*Math.round(*/coord.toX(interval.getStart2())/*)*/; //round for TimeSpreadSheet
				double X2=coord.toX(interval.getEnd2());

				g2.setColor(lineColor);
				if (clipping) g2.draw(new Line2D.Double(X1,0,X1,h/2));//when scrolling pixel by pixel both lines are needed
				g2.draw(new Line2D.Double(X2,0,X2,h/2));
				text=interval.getText2();
				metrics=font.getLineMetrics(text,context);
				if (clipping||((int)X1+2>=x0)){
					g2.setColor(textColor);
					g2.drawString(text,(int)X1+2,((int)h)/2-metrics.getDescent()-metrics.getLeading());
				}
				//avoids svg clipping. Very slow with firefox or opera
			}


		}

	}
}

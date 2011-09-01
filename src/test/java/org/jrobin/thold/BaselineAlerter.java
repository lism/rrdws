package org.jrobin.thold;

import java.io.IOException;

import org.jrobin.core.ConsolFuns;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDef;
import org.jrobin.core.RrdException;
import org.jrobin.core.Sample;

import cc.co.llabor.threshold.rrd.Threshold;

/** 
 * <b>Description:TODO</b>
 * @author      vipup<br>
 * <br>
 * <b>Copyright:</b>     Copyright (c) 2006-2008 Monster AG <br>
 * <b>Company:</b>       Monster AG  <br>
 * 
 * Creation:  31.08.2011::22:15:56<br> 
 */
public class BaselineAlerter extends RddUpdateAlerter implements Threshold {

	private double gap;

	public double getGap() { 
			return gap;
	}
 
	public BaselineAlerter(String rrdName, double baseLine, double gap,  long activationTimeoutInSeconds) {
		super(rrdName, baseLine,activationTimeoutInSeconds );
		this.gap = gap; 
	}	


	@Override
	public void checkIncident(double val, long timestamp) {
//	} //BaselineAlerter
	//  !----- -10--- -9-----...------0-----1----2 F(t) ------3------5-----11 ..---!
	//                (Baseline)-------->!<
	//                (gap)-------->!<==>!<==>! 
	/// >>------ alert ------------>!         !<--------------- alert ------------<<
	
	//else if (toCheck instanceof BaselineAlerter){
//		BaselineAlerter baselineAlerter = (BaselineAlerter)toCheck;
//		if (
//			val >  baselineAlerter.getBaseLine() +baselineAlerter.getDelta()
//			||
//			val <  baselineAlerter.getBaseLine() -baselineAlerter.getDelta()
//		){
//			toCheck.incident(charlieTmp.timestamp);
//		}else{
//			toCheck.clear();
//		}

		if (
			val >  this.getBaseLine() +this.getGap()
			||
			val <  this.getBaseLine() -this.getGap()
		){
				this.incident(timestamp);
			}else{
				this.clear();
		}
	}


	
}


 
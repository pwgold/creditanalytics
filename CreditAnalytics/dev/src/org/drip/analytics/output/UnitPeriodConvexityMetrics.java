
package org.drip.analytics.output;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for fixed income analysts and developers -
 * 		http://www.credit-trader.org/Begin.html
 * 
 *  DRIP is a free, full featured, fixed income rates, credit, and FX analytics library with a focus towards
 *  	pricing/valuation, risk, and market making.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * UnitPeriodMetrics holds the results of a unit composable period convexity metrics estimate output.
 *
 * @author Lakshmi Krishnamurthy
 */

public class UnitPeriodConvexityMetrics {
	private double _dblEndDate = java.lang.Double.NaN;
	private double _dblStartDate = java.lang.Double.NaN;
	private org.drip.analytics.output.ConvexityAdjustment _convAdj = null;

	/**
	 * UnitPeriodConvexityMetrics constructor
	 * 
	 * @param dblStartDate Metric Period Start Date
	 * @param dblEndDate Metric Period End Date
	 * @param convAdj Coupon Period Convexity Adjustment
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public UnitPeriodConvexityMetrics (
		final double dblStartDate,
		final double dblEndDate,
		final org.drip.analytics.output.ConvexityAdjustment convAdj)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblStartDate = dblStartDate) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblEndDate = dblEndDate) || _dblEndDate <
				_dblStartDate || null == (_convAdj = convAdj))
			throw new java.lang.Exception ("UnitPeriodConvexityMetrics ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Start Date
	 * 
	 * @return The Start Date
	 */

	public double startDate()
	{
		return _dblStartDate;
	}

	/**
	 * Retrieve the End Date
	 * 
	 * @return The End Date
	 */

	public double endDate()
	{
		return _dblEndDate;
	}

	/**
	 * Retrieve the Convexity Adjustment
	 * 
	 * @return The Convexity Adjustment
	 */

	public org.drip.analytics.output.ConvexityAdjustment convAdj()
	{
		return _convAdj;
	}
}


package org.drip.analytics.template;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
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
 * ZeroVolExecutionOutput holds the Templated Output of the Execution Run of a Component Instance.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ZeroVolExecutionOutput {
	private org.drip.analytics.cashflow.CompositePeriod _cp = null;
	private org.drip.analytics.support.CaseInsensitiveHashMap<java.lang.Double> _mapMeasure = null;

	/**
	 * ZeroVolExecutionOutput Constructor
	 * 
	 * @param mapMeasure The Map of the Product Measures
	 * @param cp The Product Coupon Periods
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public ZeroVolExecutionOutput (
		final org.drip.analytics.support.CaseInsensitiveHashMap<java.lang.Double> mapMeasure,
		final org.drip.analytics.cashflow.CompositePeriod cp)
		throws java.lang.Exception
	{
		if (null == (_mapMeasure = mapMeasure) || 0 == _mapMeasure.size())
			throw new java.lang.Exception ("ZeroVolExecutionOutput Constructor => Invalid Inputs");

		_cp = cp;
	}

	/**
	 * Retrieve the Computed Product Measure Map
	 * 
	 * @return The Computed Product Measure Map
	 */

	public org.drip.analytics.support.CaseInsensitiveHashMap<java.lang.Double> measure()
	{
		return _mapMeasure;
	}

	/**
	 * Retrieve the Product Coupon Periods
	 * 
	 * @return The Product Coupon Periods
	 */

	public org.drip.analytics.cashflow.CompositePeriod couponPeriods()
	{
		return _cp;
	}
}


package org.drip.math.spline;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.credit-trader.org
 * 
 * CreditAnalytics is a free, full featured, fixed income credit analytics library, developed with a special
 * 		focus towards the needs of the bonds and credit products community.
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
 * ExponentialTensionBasisSetParams implements per-segment parameters for the exponential tension basis set -
 *  currently it only contains the tension parameter.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ExponentialTensionBasisSetParams implements org.drip.math.spline.BasisSetParams {
	private double _dblTension = java.lang.Double.NaN;

	/**
	 * ExponentialTensionBasisSetParams constructor
	 * 
	 * @param dblTension Segment Tension
	 * 
	 * @throws java.lang.Exception
	 */

	public ExponentialTensionBasisSetParams (
		final double dblTension)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (_dblTension = dblTension))
			throw new java.lang.Exception ("ExponentialTensionBasisSetParams ctr: Invalid Inputs");
	}

	/**
	 * Get the Segment Tension
	 * 
	 * @return The Segment Tension
	 */

	public double getTension()
	{
		return _dblTension;
	}
}

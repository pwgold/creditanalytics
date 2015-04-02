
package org.drip.kernel.spaces;

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
 * HilbertSpace implements the normed, bounded/unbounded Continuous Multi-dimensional Real-valued R^2 Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class HilbertSpace extends org.drip.kernel.spaces.BanachSpace {

	/**
	 * Construct the Standard R^2 HilbertSpace Instance
	 * 
	 * @param iDimension The Space Dimension
	 * 
	 * @return The Standard R^2 HilbertSpace Instance
	 */

	public static final HilbertSpace StandardHilbert (
		final int iDimension)
	{
		try {
			return 0 >= iDimension ? null : new HilbertSpace (new
				org.drip.kernel.spaces.UnidimensionalRealValuedSpace[iDimension]);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * HilbertSpace Constructor
	 * 
	 * @param aURVS Array of the Real Valued Spaces
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public HilbertSpace (
		final org.drip.kernel.spaces.UnidimensionalRealValuedSpace[] aURVS)
		throws java.lang.Exception
	{
		super (aURVS, 2);
	}

	@Override public double norm (
		final double[] adblX)
		throws java.lang.Exception
	{
		if (!validate (adblX))
			throw new java.lang.Exception ("HilbertSpace::norm => Cannot Validate Inputs");

		double dblNorm = 0.;
		int iDimension = adblX.length;

		for (int i = 0; i < iDimension; ++i) {
			double dblAbsoluteX = java.lang.Math.abs (adblX[i]);

			dblNorm += dblAbsoluteX * dblAbsoluteX;
		}

		return dblNorm;
	}
}
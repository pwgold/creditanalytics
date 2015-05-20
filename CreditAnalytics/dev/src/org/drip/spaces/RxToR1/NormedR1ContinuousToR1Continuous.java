
package org.drip.spaces.RxToR1;

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
 * NormedR1ContinuousToR1Continuous implements the f : Validated Normed R^1 Continuous -> Validated Normed
 * 	R^1 Continuous Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedR1ContinuousToR1Continuous extends org.drip.spaces.RxToR1.NormedR1ToNormedR1 {

	/**
	 * NormedR1ContinuousToR1Continuous Function Space Constructor
	 * 
	 * @param funcR1ToR1 The R^1 -> R^1 Function
	 * @param cruInput The R^1 Input Metric Vector Space
	 * @param cruOutput The R^1 Output Metric Vector Space
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public NormedR1ContinuousToR1Continuous (
		final org.drip.function.deterministic.R1ToR1 funcR1ToR1,
		final org.drip.spaces.metric.ContinuousRealUnidimensional cruInput,
		final org.drip.spaces.metric.ContinuousRealUnidimensional cruOutput)
		throws java.lang.Exception
	{
		super (cruInput, cruOutput, funcR1ToR1);
	}

	@Override public double populationMetricNorm()
		throws java.lang.Exception
	{
		org.drip.spaces.tensor.GeneralizedUnidimensionalVectorSpace guvsInput = input();

		org.drip.spaces.metric.ContinuousRealUnidimensional cru =
			(org.drip.spaces.metric.ContinuousRealUnidimensional) guvsInput;

		final org.drip.function.deterministic.R1ToR1 funcR1ToR1 = function();

		final org.drip.measure.continuous.UnivariateDistribution uniDist = cru.borelSigmaMeasure();

		if (null == uniDist || null == funcR1ToR1)
			throw new java.lang.Exception
				("NormedR1ContinuousToR1Continuous::populationMetricNorm => Invalid Inputs");

		final int iPNorm = output().pNorm();

		org.drip.function.deterministic.R1ToR1 am = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblX)
				throws java.lang.Exception
			{
				return java.lang.Math.pow (java.lang.Math.abs (funcR1ToR1.evaluate (dblX)), iPNorm) *
					uniDist.density (dblX);
			}
		};

		return java.lang.Math.pow (am.integrate (cru.leftEdge(), cru.rightEdge()), 1. / iPNorm);
	}
}
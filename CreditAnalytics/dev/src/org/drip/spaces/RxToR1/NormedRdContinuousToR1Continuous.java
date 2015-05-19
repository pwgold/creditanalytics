
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
 * NormedRdContinuousToR1Continuous implements the f : Validated Normed R^d Continuous -> Validated Normed
 *  R^1 Continuous Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedRdContinuousToR1Continuous extends org.drip.spaces.RxToR1.NormedRdToNormedR1 {

	/**
	 * NormedRdContinuousToR1Continuous Function Space Constructor
	 * 
	 * @param funcRdToR1 The R^d -> R^1 Function
	 * @param crmbInput The Continuous R^d Input Metric Vector Space
	 * @param cruOutput The Continuous R^1 Output Metric Vector Space
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public NormedRdContinuousToR1Continuous (
		final org.drip.function.deterministic.RdToR1 funcRdToR1,
		final org.drip.spaces.metric.ContinuousRealMultidimensionalBanach crmbInput,
		final org.drip.spaces.metric.ContinuousRealUnidimensional cruOutput)
		throws java.lang.Exception
	{
		super (crmbInput, cruOutput, funcRdToR1);
	}

	@Override public double populationMetricNorm()
		throws java.lang.Exception
	{
		org.drip.spaces.metric.ContinuousRealMultidimensionalBanach crmb =
			(org.drip.spaces.metric.ContinuousRealMultidimensionalBanach) input();

		final org.drip.measure.continuous.MultivariateDistribution multiDist = crmb.borelSigmaMeasure();

		final org.drip.function.deterministic.RdToR1 funcRdToR1 = function();

		if (null == multiDist || null == funcRdToR1)
			throw new java.lang.Exception
				("NormedRdContinuousToR1Continuous::populationMetricNorm => Measure/Function not specified");

		final int iPNorm = output().pNorm();

		org.drip.function.deterministic.RdToR1 am = new
			org.drip.function.deterministic.RdToR1 (null) {
			@Override public double evaluate (
				final double[] adblX)
				throws java.lang.Exception
			{
				return java.lang.Math.pow (java.lang.Math.abs (funcRdToR1.evaluate (adblX)), iPNorm) *
					multiDist.density (adblX);
			}
		};

		return java.lang.Math.pow (am.integrate (crmb.leftDimensionEdge(), crmb.rightDimensionEdge()), 1. /
			iPNorm);
	}
}


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
 * NormedRdCombinatorialToR1Continuous implements the f : Validated Normed R^d Combinatorial -> Validated
 * 	Normed R^1 Continuous Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedRdCombinatorialToR1Continuous extends org.drip.spaces.RxToR1.NormedRdToNormedR1 {

	/**
	 * NormedRdCombinatorialToR1Continuous Function Space Constructor
	 * 
	 * @param funcRdToR1 The R^d -> R^1 Function
	 * @param crmbInput The Combinatorial R^d Input Metric Vector Space
	 * @param cruOutput The Continuous R^1 Output Metric Vector Space
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public NormedRdCombinatorialToR1Continuous (
		final org.drip.function.definition.RdToR1 funcRdToR1,
		final org.drip.spaces.metric.CombinatorialRealMultidimensionalBanach crmbInput,
		final org.drip.spaces.metric.ContinuousRealUnidimensional cruOutput)
		throws java.lang.Exception
	{
		super (crmbInput, cruOutput, funcRdToR1);
	}

	@Override public double populationMetricNorm()
		throws java.lang.Exception
	{
		org.drip.spaces.metric.CombinatorialRealMultidimensionalBanach crmb =
			(org.drip.spaces.metric.CombinatorialRealMultidimensionalBanach) input();

		org.drip.measure.continuous.Rd multiDist = crmb.borelSigmaMeasure();

		org.drip.function.definition.RdToR1 funcRdToR1 = function();

		if (null == multiDist || null == funcRdToR1)
			throw new java.lang.Exception
				("NormedRdCombinatorialToR1Continuous::populationMetricNorm => No Multivariate Distribution/Function");

		org.drip.spaces.tensor.CombinatorialRealMultidimensionalIterator crmi = crmb.iterator();

		double[] adblVariate = crmi.cursorVariates();

		double dblPopulationMetricNorm  = 0.;
		double dblNormalizer = 0.;

		int iPNorm = output().pNorm();

		while (null != adblVariate) {
			double dblProbabilityDensity = multiDist.density (adblVariate);

			dblNormalizer += dblProbabilityDensity;

			dblPopulationMetricNorm += dblProbabilityDensity * java.lang.Math.pow (java.lang.Math.abs
				(funcRdToR1.evaluate (adblVariate)), iPNorm);

			adblVariate = crmi.nextVariates();
		}

		return java.lang.Math.pow (dblPopulationMetricNorm / dblNormalizer, 1. / iPNorm);
	}
}


package org.drip.spaces.RxToRd;

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
 * NormedRdCombinatorialToRdContinuous implements the f : Validated Normed R^d Combinatorial -> Normed
 *  Validated R^d Continuous Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedR1CombinatorialToRdContinuous extends org.drip.spaces.RxToRd.NormedR1ToNormedRd {

	/**
	 * NormedR1CombinatorialToRdContinuous Function Space Constructor
	 * 
	 * @param funcR1ToRd The R1ToRd Function
	 * @param cruInput The Combinatorial R^1 Input Metric Vector Space
	 * @param crmbOutput The Continuous R^d Output Metric Vector Space
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public NormedR1CombinatorialToRdContinuous (
		final org.drip.function.deterministic.R1ToRd funcR1ToRd,
		final org.drip.spaces.metric.CombinatorialRealUnidimensional cruInput,
		final org.drip.spaces.metric.ContinuousRealMultidimensionalBanach crmbOutput)
		throws java.lang.Exception
	{
		super (cruInput, crmbOutput, funcR1ToRd);
	}

	@Override public double[] populationMetricNorm()
	{
		org.drip.spaces.metric.CombinatorialRealUnidimensional cru =
			(org.drip.spaces.metric.CombinatorialRealUnidimensional) input();

		org.drip.measure.continuous.UnivariateDistribution uniDist = cru.borelSigmaMeasure();

		org.drip.function.deterministic.R1ToRd funcR1ToRd = function();

		if (null == uniDist || null == funcR1ToRd) return null;

		java.util.List<java.lang.Double> lsElem = cru.elementSpace();

		double dblProbabilityDensity = java.lang.Double.NaN;
		double[] adblPopulationMetricNorm = null;
		int iOutputDimension = -1;
		double dblNormalizer = 0.;

		int iPNorm = output().pNorm();

		for (double dblElement : lsElem) {
			try {
				dblProbabilityDensity = uniDist.density (dblElement);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			double[] adblValue = funcR1ToRd.evaluate (dblElement);

			if (null == adblValue || 0 == (iOutputDimension = adblValue.length)) return null;

			dblNormalizer += dblProbabilityDensity;

			if (null == adblPopulationMetricNorm) {
				adblPopulationMetricNorm = new double[iOutputDimension];

				for (int i = 0; i < iOutputDimension; ++i)
					adblPopulationMetricNorm[i] = 0;
			}

			for (int i = 0; i < iOutputDimension; ++i)
				adblPopulationMetricNorm[i] += dblProbabilityDensity * java.lang.Math.pow (java.lang.Math.abs
					(adblValue[i]), iPNorm);
		}

		for (int i = 0; i < iOutputDimension; ++i)
			adblPopulationMetricNorm[i] += java.lang.Math.pow (adblPopulationMetricNorm[i] / dblNormalizer,
				1. / iPNorm);

		return adblPopulationMetricNorm;
	}
}

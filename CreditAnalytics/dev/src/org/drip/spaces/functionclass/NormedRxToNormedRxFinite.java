
package org.drip.spaces.functionclass;

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
 * NormedRxToNormedRxFinite exposes the Space of Functions that are a Transform from the Normed R^x -> Normed
 *  R^x Spaces.
 *  
 *  The References are:
 *  
 *  1) Carl, B. (1985): Inequalities of the Bernstein-Jackson type and the Degree of Compactness of Operators
 *  	in Banach Spaces, Annals of the Fourier Institute 35 (3) 79-118.
 *  
 *  2) Carl, B., and I. Stephani (1990): Entropy, Compactness, and the Approximation of Operators, Cambridge
 *  	University Press, Cambridge UK. 
 *  
 *  3) Williamson, R. C., A. J. Smola, and B. Scholkopf (2000): Entropy Numbers of Linear Function Classes,
 *  	in: Proceedings of the 13th Annual Conference on Computational Learning Theory, ACM New York.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedRxToNormedRxFinite {

	/**
	 * Compute the Dyadic Entropy Number from the nth Entropy Number
	 * 
	 * @param dblLogNEntropyNumber Log of the nth Entropy Number
	 * 
	 * @return The Dyadic Entropy Number
	 * 
	 * @throws java.lang.Exception Thrown if the Dyadic Entropy Number cannot be calculated
	 */

	public static final double DyadicEntropyNumber (
		final double dblLogNEntropyNumber)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblLogNEntropyNumber))
			throw new java.lang.Exception
				("NormedRxToNormedRxFinite::DyadicEntropyNumber => Invalid Inputs");

		return 1. + (dblLogNEntropyNumber / java.lang.Math.log (2.));
	}

	/**
	 * Retrieve the Scale-Sensitive Covering Number Upper/Lower Bounds given the Specified Sample for the
	 *  Function Class
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param funcR1ToR1FatShatter The Cover Fat Shattering Coefficient R^1 -> R^1
	 * 
	 * @return The Scale-Sensitive Covering Number Upper/Lower Bounds given the Specified Sample for the
	 *  Function Class
	 */

	public org.drip.spaces.cover.FunctionClassCoveringBounds scaleSensitiveCoveringBounds (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final org.drip.function.definition.R1ToR1 funcR1ToR1FatShatter)
	{
		if (null == gvvi || null == funcR1ToR1FatShatter) return null;

		int iSampleSize = -1;

		if (gvvi instanceof org.drip.spaces.instance.ValidatedR1) {
			double[] adblInstance = ((org.drip.spaces.instance.ValidatedR1) gvvi).instance();

			if (null == adblInstance) return null;

			iSampleSize = adblInstance.length;
		} else if (gvvi instanceof org.drip.spaces.instance.ValidatedRd) {
			double[][] aadblInstance = ((org.drip.spaces.instance.ValidatedRd) gvvi).instance();

			if (null == aadblInstance) return null;

			iSampleSize = aadblInstance.length;
		}

		try {
			return new org.drip.spaces.cover.ScaleSensitiveCoveringBounds (funcR1ToR1FatShatter,
				iSampleSize);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieve the Agnostic Covering Number Upper/Lower Bounds for the Function Class
	 * 
	 * @return The Agnostic Covering Number Upper/Lower Bounds for the Function Class
	 */

	public abstract org.drip.spaces.cover.FunctionClassCoveringBounds agnosticCoveringNumberBounds();

	/**
	 * Retrieve the Input Vector Space
	 * 
	 * @return The Input Vector Space
	 */

	public abstract org.drip.spaces.metric.GeneralizedMetricVectorSpace inputMetricVectorSpace();

	/**
	 * Retrieve the Output Vector Space
	 * 
	 * @return The Output Vector Space
	 */

	public abstract org.drip.spaces.metric.GeneralizedMetricVectorSpace outputMetricVectorSpace();
}

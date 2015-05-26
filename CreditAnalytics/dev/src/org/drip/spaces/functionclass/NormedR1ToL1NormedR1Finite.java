
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
 * NormedR1ToL1NormedR1Finite implements the Class f E F : Normed R^1 -> Normed L1 R^1 Spaces of Finite
 * 	Functions.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedR1ToL1NormedR1Finite extends org.drip.spaces.functionclass.NormedR1ToNormedR1Finite {

	/**
	 * Create Bounded R^1 -> Bounded L1 R^1 Function Class for the specified Bounded Class of Finite
	 *  Functions
	 * 
	 * @param aR1ToR1 The Bounded R^1 -> Bounded R^1 Function Set
	 * @param dblPredictorSupport The Set Predictor Support
	 * @param dblResponseBound The Set Response Bound
	 * 
	 * @return The Bounded R^1 -> Bounded R^1 Function Class for the specified Function Set
	 */

	public static final NormedR1ToL1NormedR1Finite BoundedPredictorBoundedResponse (
		final org.drip.function.deterministic.R1ToR1[] aR1ToR1,
		final double dblPredictorSupport,
		final double dblResponseBound)
	{
		if (null == aR1ToR1) return null;

		int iNumFunction = aR1ToR1.length;
		org.drip.spaces.RxToR1.NormedR1ToNormedR1[] aR1ToR1FunctionSpace = new
			org.drip.spaces.RxToR1.NormedR1ToNormedR1[iNumFunction];

		if (0 == iNumFunction) return null;

		try {
			org.drip.spaces.metric.ContinuousRealUnidimensional cruInput = new
				org.drip.spaces.metric.ContinuousRealUnidimensional (-0.5 * dblPredictorSupport, 0.5 *
					dblPredictorSupport, null, 1);

			org.drip.spaces.metric.ContinuousRealUnidimensional cruOutput = new
				org.drip.spaces.metric.ContinuousRealUnidimensional (-0.5 * dblResponseBound, 0.5 *
					dblResponseBound, null, 1);

			for (int i = 0; i < iNumFunction; ++i)
				aR1ToR1FunctionSpace[i] = new org.drip.spaces.RxToR1.NormedR1ContinuousToR1Continuous
					(aR1ToR1[i], cruInput, cruOutput);

			return new NormedR1ToL1NormedR1Finite (aR1ToR1FunctionSpace);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected NormedR1ToL1NormedR1Finite (
		final org.drip.spaces.RxToR1.NormedR1ToNormedR1[] aR1ToR1FunctionSpace)
		throws java.lang.Exception
	{
		super (aR1ToR1FunctionSpace);
	}

	@Override public org.drip.spaces.cover.FunctionClassCoveringBounds agnosticCoveringNumberBounds()
	{
		org.drip.spaces.RxToR1.NormedR1ToNormedR1[] aNormedR1ToNormedR1 =
			(org.drip.spaces.RxToR1.NormedR1ToNormedR1[]) functionSpaces();

		int iNumFunction = aNormedR1ToNormedR1.length;
		double dblResponseLowerBound = java.lang.Double.NaN;
		double dblResponseUpperBound = java.lang.Double.NaN;
		double dblPredictorLowerBound = java.lang.Double.NaN;
		double dblPredictorUpperBound = java.lang.Double.NaN;

		for (int i = 0; i < iNumFunction; ++i) {
			org.drip.spaces.RxToR1.NormedR1ToNormedR1 r1Tor1 = aNormedR1ToNormedR1[i];

			org.drip.spaces.metric.RealUnidimensionalNormedSpace runsInput = r1Tor1.input();

			org.drip.spaces.metric.RealUnidimensionalNormedSpace runsOutput = r1Tor1.output();

			if (!runsInput.isPredictorBounded() || !runsOutput.isPredictorBounded()) return null;

			double dblResponseLeftBound = runsOutput.leftEdge();

			double dblPredictorLeftBound = runsInput.leftEdge();

			double dblResponseRightBound = runsOutput.rightEdge();

			double dblPredictorRightBound = runsInput.rightEdge();

			if (!org.drip.quant.common.NumberUtil.IsValid (dblPredictorLowerBound))
				dblPredictorLowerBound = dblPredictorLeftBound;
			else {
				if (dblPredictorLowerBound > dblPredictorLeftBound)
					dblPredictorLowerBound = dblPredictorLeftBound;
			}

			if (!org.drip.quant.common.NumberUtil.IsValid (dblPredictorUpperBound))
				dblPredictorUpperBound = dblPredictorRightBound;
			else {
				if (dblPredictorUpperBound < dblPredictorRightBound)
					dblPredictorUpperBound = dblPredictorRightBound;
			}

			if (!org.drip.quant.common.NumberUtil.IsValid (dblResponseLowerBound))
				dblResponseLowerBound = dblResponseLeftBound;
			else {
				if (dblResponseLowerBound > dblResponseLeftBound)
					dblResponseLowerBound = dblResponseLeftBound;
			}

			if (!org.drip.quant.common.NumberUtil.IsValid (dblResponseUpperBound))
				dblResponseUpperBound = dblResponseRightBound;
			else {
				if (dblResponseUpperBound < dblResponseRightBound)
					dblResponseUpperBound = dblResponseRightBound;
			}
		}

		double dblVariation = dblResponseUpperBound - dblResponseLowerBound;

		try {
			return new org.drip.spaces.cover.R1L1CoveringBounds (dblPredictorUpperBound -
				dblPredictorLowerBound, dblVariation, dblVariation);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

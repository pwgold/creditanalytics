
package org.drip.spaces.cover;

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
 * R1ToL1R1Class implements the Class F of f : R^1 -> L1 R^1 Normed Function Spaces of all Variants.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class R1ToL1R1Class extends org.drip.spaces.cover.GeneralizedNormedFunctionClass {

	/**
	 * Create Bounded R^1 -> Bounded L1 R^1 Function Class for the specified Bounded Function Class
	 * 
	 * @param aR1ToR1 The Bounded R^1 -> Bounded R^1 Function Set
	 * @param dblPredictorSupport The Set Predictor Support
	 * @param dblResponseBound The Set Response Bound
	 * 
	 * @return The Bounded R^1 -> Bounded R^1 Function Class for the specified Function Set
	 */

	public static final R1ToL1R1Class BoundedPredictorBoundedResponse (
		final org.drip.function.deterministic.R1ToR1[] aR1ToR1,
		final double dblPredictorSupport,
		final double dblResponseBound)
	{
		if (null == aR1ToR1) return null;

		int iNumFunction = aR1ToR1.length;
		org.drip.spaces.function.NormedR1ToR1[] aR1ToR1FunctionSpace = new
			org.drip.spaces.function.NormedR1ToR1[iNumFunction];

		if (0 == iNumFunction) return null;

		try {
			org.drip.spaces.metric.ContinuousRealUnidimensional cruInput = new
				org.drip.spaces.metric.ContinuousRealUnidimensional (-0.5 * dblPredictorSupport, 0.5 *
					dblPredictorSupport, null, 1);

			org.drip.spaces.metric.ContinuousRealUnidimensional cruOutput = new
				org.drip.spaces.metric.ContinuousRealUnidimensional (-0.5 * dblResponseBound, 0.5 *
					dblResponseBound, null, 1);

			for (int i = 0; i < iNumFunction; ++i)
				aR1ToR1FunctionSpace[i] = new org.drip.spaces.function.NormedR1ContinuousToR1Continuous
					(aR1ToR1[i], cruInput, cruOutput);

			return new R1ToL1R1Class (aR1ToR1FunctionSpace);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * R1ToL1R1Class Function Class Constructor
	 * 
	 * @param aR1ToR1FunctionSpace Array of the Function Spaces
	 * 
	 * @throws java.lang.Exception Thrown if R1ToL1R1Class Instance cannot be created
	 */

	public R1ToL1R1Class (
		final org.drip.spaces.function.NormedR1ToR1[] aR1ToR1FunctionSpace)
		throws java.lang.Exception
	{
		super (aR1ToR1FunctionSpace);

		for (int i = 0; i < aR1ToR1FunctionSpace.length; ++i) {
			if (null == aR1ToR1FunctionSpace[i] || 1 != aR1ToR1FunctionSpace[i].output().pNorm())
				throw new java.lang.Exception ("R1ToL1R1Class ctr: Invalid Input Function");
		}
	}

	@Override public org.drip.spaces.cover.CoveringNumberEstimate agnosticCoveringNumber()
	{
		org.drip.spaces.function.GeneralizedNormedFunctionSpace[] aGNFS = functionSpaces();

		int iNumFunction = aGNFS.length;
		double dblResponseLowerBound = java.lang.Double.NaN;
		double dblResponseUpperBound = java.lang.Double.NaN;
		double dblPredictorLowerBound = java.lang.Double.NaN;
		double dblPredictorUpperBound = java.lang.Double.NaN;

		for (int i = 0; i < iNumFunction; ++i) {
			org.drip.spaces.function.NormedR1ToR1 r1Tor1 = (org.drip.spaces.function.NormedR1ToR1) aGNFS[i];

			org.drip.spaces.tensor.GeneralizedUnidimensionalVectorSpace guvsOutput =
				(org.drip.spaces.tensor.GeneralizedUnidimensionalVectorSpace) r1Tor1.output();

			org.drip.spaces.tensor.GeneralizedUnidimensionalVectorSpace guvsInput = r1Tor1.input();

			if (!guvsInput.isPredictorBounded() || !guvsOutput.isPredictorBounded()) return null;

			double dblResponseLeftBound = guvsOutput.leftEdge();

			double dblResponseRightBound = guvsOutput.rightEdge();

			double dblPredictorLeftBound = guvsInput.leftEdge();

			double dblPredictorRightBound = guvsInput.rightEdge();

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
			return new org.drip.spaces.cover.BoundedFunctionCoveringNumber (dblPredictorUpperBound -
				dblPredictorLowerBound, dblVariation, dblVariation);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.spaces.cover.CoveringNumberEstimate scaleSensitiveCoveringNumber (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final org.drip.function.deterministic.R1ToR1 r1r1FatShatter)
	{
		if (null == gvvi || !(gvvi instanceof org.drip.spaces.instance.ValidatedRealUnidimensional) || null
			== r1r1FatShatter)
			return null;

		org.drip.spaces.instance.ValidatedRealUnidimensional vru =
			(org.drip.spaces.instance.ValidatedRealUnidimensional) gvvi;

		double[] adblInstance = vru.instance();

		try {
			return null == adblInstance ? null : new org.drip.spaces.cover.ScaleSensitiveCoveringNumber
				(r1r1FatShatter, adblInstance.length);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public double coveringNumber (
		final double dblCover)
		throws java.lang.Exception
	{
		throw new java.lang.Exception ("R1ToR1Class::coveringNumber => Cannot estimate");
	}

	@Override public double uniformCoveringNumber (
		final org.drip.spaces.instance.ValidatedRealUnidimensional vru,
		final double dblCover)
		throws java.lang.Exception
	{
		throw new java.lang.Exception ("R1ToR1Class::uniformCoveringNumber => Cannot estimate");
	}

	@Override public double uniformCoveringNumber (
		final org.drip.spaces.instance.ValidatedRealMultidimensional vrm,
		final double dblCover)
		throws java.lang.Exception
	{
		throw new java.lang.Exception ("R1ToR1Class::uniformCoveringNumber => Cannot estimate");
	}
}

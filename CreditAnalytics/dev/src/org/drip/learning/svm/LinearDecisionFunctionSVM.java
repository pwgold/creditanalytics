
package org.drip.learning.svm;

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
 * LinearDecisionFunctionSVM implements the Linear Decision Function-Based SVM Functionality for
 * 	Classification and Regression.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class LinearDecisionFunctionSVM extends org.drip.function.deterministic.RdToR1 implements
	org.drip.learning.svm.SupportVectorMachine {
	private double _dblB = java.lang.Double.NaN;
	private double[] _adblInverseMarginWeight = null;
	private org.drip.spaces.metric.RealMultidimensionalNormedSpace _rmnsInverseMargin = null;
	private org.drip.spaces.tensor.GeneralizedMultidimensionalVectorSpace _gmvsPredictor = null;

	/**
	 * LinearDecisionFunctionSVM Constructor
	 * 
	 * @param gmvsPredictor The R^d Metric Input Predictor Space
	 * @param rmnsInverseMargin The Inverse Margin Weights R^d L2 Space
	 * @param adblInverseMarginWeight Array of Inverse Margin Weights
	 * @param dblB The Offset
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public LinearDecisionFunctionSVM (
		final org.drip.spaces.tensor.GeneralizedMultidimensionalVectorSpace gmvsPredictor,
		final org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsInverseMargin,
		final double[] adblInverseMarginWeight,
		final double dblB)
		throws java.lang.Exception
	{
		super (null);

		if (null == (_adblInverseMarginWeight = adblInverseMarginWeight) || null == (_gmvsPredictor =
			gmvsPredictor) || _gmvsPredictor.dimension() != _adblInverseMarginWeight.length ||
				!org.drip.quant.common.NumberUtil.IsValid (_dblB = dblB) || null == (_rmnsInverseMargin =
					rmnsInverseMargin) || 2 != _rmnsInverseMargin.pNorm())
			throw new java.lang.Exception ("LinearDecisionFunctionSVM ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Inverse Margin Weights Array
	 * 
	 * @return The Inverse Margin Weights Array
	 */

	public double[] inverseMarginWeights()
	{
		return _adblInverseMarginWeight;
	}

	/**
	 * Retrieve the Inverse Margin Weight Metric Vector Space
	 * 
	 * @return The Inverse Margin Weight Metric Vector Space
	 */

	public org.drip.spaces.metric.RealMultidimensionalNormedSpace inverseMarginSpace()
	{
		return _rmnsInverseMargin;
	}

	/**
	 * Retrieve the Offset
	 * 
	 * @return The Offset
	 */

	public double offset()
	{
		return _dblB;
	}

	@Override public double evaluate (
		final double[] adblX)
		throws java.lang.Exception
	{
		if (!_gmvsPredictor.validateInstance (adblX))
			throw new java.lang.Exception ("LinearDecisionFunctionSVM::evaluate => Invalid Inputs");

		double dblDotProduct = 0.;
		int iDimension = adblX.length;

		for (int i = 0; i < iDimension; ++i)
			dblDotProduct += _adblInverseMarginWeight[i] * adblX[i];

		return dblDotProduct + _dblB;
	}

	@Override public org.drip.spaces.tensor.GeneralizedMultidimensionalVectorSpace predictorSpace()
	{
		return _gmvsPredictor;
	}

	@Override public double regress (
		final double[] adblX)
		throws java.lang.Exception
	{
		return evaluate (adblX);
	}

	@Override public short classify (
		final double[] adblX)
		throws java.lang.Exception
	{
		return evaluate (adblX) > 0. ? org.drip.spaces.tensor.BinaryBooleanVector.BBV_UP :
			org.drip.spaces.tensor.BinaryBooleanVector.BBV_DOWN;
	}

	/**
	 * Optimize the Hyper-plane for the Purposes of Regression
	 * 
	 * @param adblEmpirical The Empirical Observation Array
	 * @param dblMargin The Optimization Margin
	 * 
	 * @return TRUE => The Hyper-plane has been successfully Optimized for Regression
	 */

	public abstract boolean optimizeRegressionHyperplane (
		final double[] adblEmpirical,
		final double dblMargin
	);

	/**
	 * Optimize the Hyper-plane for the Purposes of Classification
	 * 
	 * @param asEmpirical The Empirical Observation Array
	 * @param dblMargin The Optimization Margin
	 * 
	 * @return TRUE => The Hyper-plane has been successfully Optimized for Classification
	 */

	public abstract boolean optimizeClassificationHyperplane (
		final short[] asEmpirical,
		final double dblMargin
	);
}

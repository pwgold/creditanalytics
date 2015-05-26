
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
 * KernelDecisionFunctionSVM implements the Kernel-based Decision Function-Based SVM Functionality for
 * 	Classification and Regression.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class KernelDecisionFunctionSVM extends org.drip.function.deterministic.RdToR1 implements
	org.drip.learning.svm.SupportVectorMachine {
	private double[] _adblKernelWeight = null;
	private double _dblB = java.lang.Double.NaN;
	private double[][] _aadblKernelPredictorPivot = null;
	private org.drip.learning.kernel.SymmetricRxToNormedR1Kernel _kernel = null;
	private org.drip.spaces.tensor.GeneralizedMultidimensionalVectorSpace _gmvsPredictor = null;

	/**
	 * KernelDecisionFunctionSVM Constructor
	 * 
	 * @param gmvsPredictor The R^d Metric Input Predictor Space
	 * @param kernel The Kernel
	 * @param adblKernelWeight Array of the Kernel Weights
	 * @param aadblKernelPredictorPivot Array of the Kernel R^d Predictor Pivot Nodes
	 * @param dblB The Kernel Offset
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public KernelDecisionFunctionSVM (
		final org.drip.spaces.tensor.GeneralizedMultidimensionalVectorSpace gmvsPredictor,
		final org.drip.learning.kernel.SymmetricRxToNormedR1Kernel kernel,
		final double[] adblKernelWeight,
		final double[][] aadblKernelPredictorPivot,
		final double dblB)
		throws java.lang.Exception
	{
		super (null);

		if (null == (_gmvsPredictor = gmvsPredictor) || null == (_kernel = kernel) || null ==
			(_adblKernelWeight = adblKernelWeight) || null == (_aadblKernelPredictorPivot =
				aadblKernelPredictorPivot) || !org.drip.quant.common.NumberUtil.IsValid (_dblB = dblB))
			throw new java.lang.Exception ("KernelDecisionFunctionSVM ctr: Invalid Inputs");

		int iKernelInputDimension = _kernel.input().dimension();

		int iNumPredictorPivot = _adblKernelWeight.length;

		if (0 == iNumPredictorPivot || iNumPredictorPivot != _aadblKernelPredictorPivot.length ||
			_gmvsPredictor.dimension() != iKernelInputDimension)
			throw new java.lang.Exception ("KernelDecisionFunctionSVM ctr: Invalid Inputs");

		for (int i = 0; i < iNumPredictorPivot; ++i) {
			if (null == _aadblKernelPredictorPivot[i] || _aadblKernelPredictorPivot[i].length !=
				iKernelInputDimension)
				throw new java.lang.Exception ("KernelDecisionFunctionSVM ctr: Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Decision Kernel
	 * 
	 * @return The Decision Kernel
	 */

	public org.drip.learning.kernel.SymmetricRxToNormedR1Kernel kernel()
	{
		return _kernel;
	}

	/**
	 * Retrieve the Decision Kernel Weights
	 * 
	 * @return The Decision Kernel Weights
	 */

	public double[] kernelWeights()
	{
		return _adblKernelWeight;
	}

	/**
	 * Retrieve the Decision Kernel Predictor Pivot Nodes
	 * 
	 * @return The Decision Kernel Predictor Pivot Nodes
	 */

	public double[][] kernelPredictorPivot()
	{
		return _aadblKernelPredictorPivot;
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
		if (null == adblX || adblX.length != _gmvsPredictor.dimension())
			throw new java.lang.Exception ("KernelDecisionFunctionSVM::evaluate => Invalid Inputs");

		double dblDotProduct = 0.;
		int iNumPredictorPivot = _adblKernelWeight.length;

		for (int i = 0; i < iNumPredictorPivot; ++i)
			dblDotProduct += _adblKernelWeight[i] * _kernel.evaluate (_aadblKernelPredictorPivot[i], adblX);

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

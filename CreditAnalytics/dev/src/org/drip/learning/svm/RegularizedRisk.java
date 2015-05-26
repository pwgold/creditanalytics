
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
 * RegularizedRisk computes the Full SVM Empirical Risk - the Empirical and the Structural Losses and Risks.
 * 
 * The References are:
 * 
 * 	1) Smola, A. J., B. Scholkopf, and K. R. Muller (1998): The Connection between Regularization Operators
 * 		and Support Vector Kernels, Neural Networks, 11 637-649.
 * 
 * @author Lakshmi Krishnamurthy
 */

public abstract class RegularizedRisk {
	private double _dblRegularizationConstant = java.lang.Double.NaN;
	private org.drip.learning.svm.KernelDecisionFunctionSVM _svmKernel = null;

	/**
	 * RegularizedRisk Constructor
	 * 
	 * @param svmKernel The Kernel Decision Function SVM Instance
	 * @param dblRegularizationConstant The Regularization Constant
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public RegularizedRisk (
		final org.drip.learning.svm.KernelDecisionFunctionSVM svmKernel,
		final double dblRegularizationConstant)
		throws java.lang.Exception
	{
		if (null == (_svmKernel = svmKernel) || !org.drip.quant.common.NumberUtil.IsValid
			(_dblRegularizationConstant = dblRegularizationConstant))
			throw new java.lang.Exception ("RegularizedRisk ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Regularization Kernel
	 * 
	 * @return The Regularization Kernel
	 */

	public org.drip.learning.svm.KernelDecisionFunctionSVM svmKernel()
	{
		return _svmKernel;
	}

	/**
	 * Retrieve the Regularization Constant
	 * 
	 * @return The Regularization Constant
	 */

	public double regularizationConstant()
	{
		return _dblRegularizationConstant;
	}

	/**
	 * Compute the Structural Error
	 * 
	 * @param adblPredictor The Input R^d Predictor
	 * @param dblResponseOutcome The Outcome Response Value
	 * 
	 * @return The Structural Error
	 */

	public abstract org.drip.spaces.metric.GeneralizedMetricVectorSpace structuralError (
		final double[] adblPredictor,
		final double dblResponseOutcome);

	/**
	 * Retrieve the Regularized Error
	 * 
	 * @param adblPredictor The Input R^d Predictor
	 * @param dblResponseOutcome The Outcome Response Value
	 * 
	 * @return The Regularized Error
	 * 
	 * @throws java.lang.Exception Thrown if the Regularized Error cannot be computed
	 */

	public double regularizedError (
		final double[] adblPredictor,
		final double dblResponseOutcome)
		throws java.lang.Exception
	{
		org.drip.spaces.metric.GeneralizedMetricVectorSpace gmvsError = structuralError (adblPredictor,
			dblResponseOutcome);

		if (null == gmvsError)
			throw new java.lang.Exception ("RegularizedRisk::regularizedError => Invalid Inputs");

		return gmvsError.populationMetricNorm() + 0.5 * _dblRegularizationConstant * java.lang.Math.abs
			(_svmKernel.regress (adblPredictor) - dblResponseOutcome);
	}
}

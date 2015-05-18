
package org.drip.learning.kernel;

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
 * L2KernelIntegralOperator implements the R^x L2 -> R^x L2 Kernel Integral Operator defined by:
 * 
 * 		T_k [f(.)] := Integral Over Input Space {k (., y) * f(y) * d[Prob(y)]}
 *  
 *  The References are:
 *  
 *  1) Ash, R. (1965): Information Theory, Inter-science New York.
 *  
 *  2) Konig, H. (1986): Eigenvalue Distribution of Compact Operators, Birkhauser, Basel, Switzerland. 
 *  
 *  3) Smola, A. J., A. Elisseff, B. Scholkopf, and R. C. Williamson (2000): Entropy Numbers for Convex
 *  	Combinations and mlps, in: Advances in Large Margin Classifiers, A. Smola, P. Bartlett, B. Scholkopf,
 *  	and D. Schuurmans - editors, MIT Press, Cambridge, MA.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class L2KernelIntegralOperator {
	private org.drip.function.deterministic.RdToR1 _funcRdToR1 = null;
	private org.drip.learning.kernel.SymmetricNormedRxToNormedR1 _kernel = null;
	private org.drip.measure.continuous.MultivariateDistribution _mdInputBorelSigmaMeasure = null;

	/**
	 * L2KernelIntegralOperator Constructor
	 * 
	 * @param kernel The L2 Symmetric Mercer Kernel
	 * @param funcRdToR1 The R^d -> R^1 Operator Function
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public L2KernelIntegralOperator (
		final org.drip.learning.kernel.SymmetricNormedRxToNormedR1 kernel,
		final org.drip.function.deterministic.RdToR1 funcRdToR1)
		throws java.lang.Exception
	{
		if (null == (_kernel = kernel) || null == (_funcRdToR1 = funcRdToR1))
			throw new java.lang.Exception ("L2KernelIntegralOperator ctr: Invalid Inputs");

		org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsKernelInput = _kernel.input();

		if (2 != rmnsKernelInput.pNorm() || null == (_mdInputBorelSigmaMeasure =
			rmnsKernelInput.borelSigmaMeasure()))
			throw new java.lang.Exception ("L2KernelIntegralOperator ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the L2 Symmetric Mercer Kernel
	 * 
	 * @return The L2 Symmetric Mercer Kernel
	 */

	public org.drip.learning.kernel.SymmetricNormedRxToNormedR1 l2SymmetricMercerKernel()
	{
		return _kernel;
	}

	/**
	 * Retrieve the R^d -> R^1 Kernel Operator Function
	 * 
	 * @return The R^d -> R^1 Kernel Operator Function
	 */

	public org.drip.function.deterministic.RdToR1 kernelOperatorFunction()
	{
		return _funcRdToR1;
	}

	/**
	 * Retrieve the Input Space Borel Sigma Measure
	 * 
	 * @return The Input Space Borel Sigma Measure
	 */

	public org.drip.measure.continuous.MultivariateDistribution inputSpaceBorelMeasure()
	{
		return _mdInputBorelSigmaMeasure;
	}

	/**
	 * Compute the Operator's Kernel Integral across the specified X Variate Instance
	 * 
	 * @param gvviX Validated Vector Instance X
	 * 
	 * @return The Operator's Kernel Integral across the specified X Variate Instance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public double computeOperatorIntegral (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviX)
		throws java.lang.Exception
	{
		org.drip.function.deterministic.RdToR1 funcRdToR1 = new org.drip.function.deterministic.RdToR1 (null)
		{
			@Override public double evaluate (
				final double[] adblY)
				throws java.lang.Exception
			{
				return _kernel.evaluate (gvviX, new org.drip.spaces.instance.ValidatedRealMultidimensional
					(_kernel.input(), new double[][] {adblY})) * _funcRdToR1.evaluate (adblY);
			}
		};

		return _kernel.input().borelMeasureSpaceExpectation (funcRdToR1);
	}

	/**
	 * Indicate the Kernel Operator Integral's Positivity across the specified X Variate Instance
	 * 
	 * @param gvviX Validated Vector Instance X
	 * 
	 * @return TRUE => The Kernel Operator Integral is Positive across the specified X Variate Instance
	 */

	public boolean positivity (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviX)
		throws java.lang.Exception
	{
		try {
			return 0 < computeOperatorIntegral (gvviX);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Eigenize the Kernel Integral Operator into its Components
	 * 
	 * @return The Array of Eigen-Components of the Kernel Integral Operator
	 */

	public abstract OperatorEigenComponent[] eigenize();
}


package org.drip.learning.regularization;

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
 * RegularizerR1CombinatorialToR1Continuous computes the Structural Loss and Risk for the specified Normed
 *  R^1 Combinatorial -> Normed R^1 Continuous Learning Function.
 *  
 * The References are:
 *  
 *  1) Alon, N., S. Ben-David, N. Cesa Bianchi, and D. Haussler (1997): Scale-sensitive Dimensions, Uniform
 *  	Convergence, and Learnability, Journal of Association of Computational Machinery, 44 (4) 615-631.
 * 
 *  2) Anthony, M., and P. L. Bartlett (1999): Artificial Neural Network Learning - Theoretical Foundations,
 *  	Cambridge University Press, Cambridge, UK.
 *  
 *  3) Kearns, M. J., R. E. Schapire, and L. M. Sellie (1994): Towards Efficient Agnostic Learning, Machine
 *  	Learning, 17 (2) 115-141.
 *  
 *  4) Lee, W. S., P. L. Bartlett, and R. C. Williamson (1998): The Importance of Convexity in Learning with
 *  	Squared Loss, IEEE Transactions on Information Theory, 44 1974-1980.
 * 
 *  5) Vapnik, V. N. (1998): Statistical Learning Theory, Wiley, New York.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RegularizerR1CombinatorialToR1Continuous extends
	org.drip.spaces.RxToR1.NormedR1CombinatorialToR1Continuous implements
		org.drip.learning.regularization.RegularizerR1ToR1 {
	private double _dblLambda = java.lang.Double.NaN;

	/**
	 * RegularizerR1CombinatorialToR1Continuous Function Space Constructor
	 * 
	 * @param funcRegularizerR1ToR1 The R^1 -> R^1 Regularizer Function
	 * @param cruInput The Combinatorial R^1 Input Metric Vector Space
	 * @param cruOutput The Continuous R^1 Output Metric Vector Space
	 * @param dblLambda The Regularization Lambda
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public RegularizerR1CombinatorialToR1Continuous (
		final org.drip.function.deterministic.R1ToR1 funcRegularizerR1ToR1,
		final org.drip.spaces.metric.CombinatorialRealUnidimensional cruInput,
		final org.drip.spaces.metric.ContinuousRealUnidimensional cruOutput,
		final double dblLambda)
		throws java.lang.Exception
	{
		super (funcRegularizerR1ToR1, cruInput, cruOutput);

		if (!org.drip.quant.common.NumberUtil.IsValid (_dblLambda = dblLambda) || 0 > _dblLambda)
			throw new java.lang.Exception
				("RegularizerR1CombinatorialToR1Continuous Constructor => Invalid Inputs");
	}

	@Override public double lambda()
	{
		return _dblLambda;
	}

	@Override public double structuralLoss (
		final org.drip.function.deterministic.R1ToR1 funcR1ToR1,
		final double[] adblInstance)
		throws java.lang.Exception
	{
		if (null == funcR1ToR1 || null == adblInstance)
			throw new java.lang.Exception
				("RegularizerR1CombinatorialToR1Continuous::structuralLoss => Invalid Inputs");

		double dblLoss = 0.;
		int iNumSample = adblInstance.length;

		if (0 == iNumSample)
			throw new java.lang.Exception
				("RegularizerR1CombinatorialToR1Continuous::structuralLoss => Invalid Inputs");

		int iPNorm = output().pNorm();

		org.drip.function.deterministic.R1ToR1 funcRegularizerR1ToR1 = function();

		for (int i = 0; i < iNumSample; ++i)
			dblLoss += java.lang.Math.pow (java.lang.Math.abs (funcRegularizerR1ToR1.evaluate
				(adblInstance[i])), iPNorm);

		return dblLoss / iPNorm;
	}
}

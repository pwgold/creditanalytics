
package org.drip.classifier.functionclass;

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
 * CoveringProbabilisticLossAsymptote provides the Probabilistic Asymptotic Sample Behavior of Empirical Loss
 *  of the given Classifier Class using the Covering Number Generalization Bounds. This is expressed as
 *  
 *  						C1 (n) * N (epsilon, n) * exp (-n.epsilon^b/C2),
 *  
 *  where:
 *  	- n is the Size of the Sample
 *  	- 'epsilon' is the cover
 *  	- C1 (n) is the sample coefficient function
 *  	- C2 is an exponent scaling constant
 *  	- 'b' an exponent ((i.e., the Cover Exponent) that depends on the setting (i.e.,
 *  		agnostic/classification/regression/convex etc)
 *  
 *  The References are:
 *  
 *  1) Alon, N., S. Ben-David, N. Cesa Bianchi, and D. Haussler (1997): Scale-sensitive Dimensions, Uniform
 *  	Convergence, and Learnability, Journal of Association of COmputational Machinery, 44 (4) 615-631.
 * 
 *  2) Anthony, M., and P. L. Bartlett (1999): Artificial Neural Network Learning - Theoretical Foundations,
 *  	Cambridge University Press, Cambridge, UK.
 * 
 *  3) Vapnik, V. N. (1998): Statistical learning Theory, Wiley, New York.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CoveringProbabilisticLossAsymptote {
	private double _dblCoverExponent = java.lang.Double.NaN;
	private double _dblExponentScaler = java.lang.Double.NaN;
	private org.drip.function.deterministic.R1ToR1 _funcSampleCoefficient = null;

	/**
	 * CoveringProbabilisticLossAsymptote Constructor
	 * 
	 * @param funcSampleCoefficient The Sample Coefficient Function
	 * @param dblCoverExponent The Cover Exponent
	 * @param dblExponentScaler The Exponent Scaler
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public CoveringProbabilisticLossAsymptote (
		final org.drip.function.deterministic.R1ToR1 funcSampleCoefficient,
		final double dblCoverExponent,
		final double dblExponentScaler)
		throws java.lang.Exception
	{
		if (null == (_funcSampleCoefficient = funcSampleCoefficient) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblCoverExponent = dblCoverExponent) ||
				!org.drip.quant.common.NumberUtil.IsValid (_dblExponentScaler = dblExponentScaler))
			throw new java.lang.Exception ("CoveringProbabilisticLossAsymptote ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Sample Coefficient Function
	 * 
	 * @return The Sample Coefficient Function
	 */

	public org.drip.function.deterministic.R1ToR1 sampleCoefficient()
	{
		return _funcSampleCoefficient;
	}

	/**
	 * Retrieve the Exponential Cover Exponent
	 * 
	 * @return The Exponential Cover Exponent
	 */

	public double coverExponent()
	{
		return _dblCoverExponent;
	}

	/**
	 * Retrieve the Exponent Scaler
	 * 
	 * @return The Exponent Scaler
	 */

	public double exponentScaler()
	{
		return _dblExponentScaler;
	}

	/**
	 * Compute the Sample Uniform Convergence Bound
	 * 
	 * @param iSampleSize The Sample Size
	 * @param dblCover The Sample Cover
	 * 
	 * @return The Sample Uniform Convergence Bound
	 * 
	 * @throws java.lang.Exception Thrown if the Sample Uniform Convergence Bound cannot be computed
	 */

	public double sampleUniformConvergenceBound (
		final int iSampleSize,
		final double dblCover)
		throws java.lang.Exception
	{
		if (0 >= iSampleSize || !org.drip.quant.common.NumberUtil.IsValid (dblCover))
			throw new java.lang.Exception
				("CoveringProbabilisticLossAsymptote::sampleUniformConvergenceBound => Invalid Inputs");

		return _funcSampleCoefficient.evaluate (iSampleSize) * java.lang.Math.exp (-1. * iSampleSize *
			java.lang.Math.pow (dblCover, _dblCoverExponent) / _dblExponentScaler);
	}
}

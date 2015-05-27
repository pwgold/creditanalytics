
package org.drip.learning.RxToR1;

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
 * EmpiricalSupremumLossMetrics implements Efron-Stein Metrics for the Empirical Loss Supremum R^x -> R^1
 *  Functions.
 *
 * @author Lakshmi Krishnamurthy
 */

public class EmpiricalSupremumLossMetrics extends org.drip.sequence.functional.EfronSteinMetrics {
	private org.drip.learning.RxToR1.EmpiricalLossSupremum _els = null;

	/**
	 * EmpiricalSupremumLossMetrics Constructor
	 * 
	 * @param els R^x -> R^1 Empirical Loss Supremum Function
	 * @param aSSAM Array of the Individual Single Sequence Metrics
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public EmpiricalSupremumLossMetrics (
		final org.drip.learning.RxToR1.EmpiricalLossSupremum els,
		final org.drip.sequence.metrics.SingleSequenceAgnosticMetrics[] aSSAM)
		throws java.lang.Exception
	{
		super (els, aSSAM);

		if (null == (_els = els))
			throw new java.lang.Exception ("EmpiricalSupremumLossMetrics ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Empirical Loss Supremum Function
	 * 
	 * @return The Empirical Loss Supremum Function
	 */

	public org.drip.learning.RxToR1.EmpiricalLossSupremum empiricalLossSupremum()
	{
		return _els;
	}

	/**
	 * Retrieve the Univariate Sequence Dependent Variance Bound
	 * 
	 * @param adblVariate The univariate Sequence
	 * 
	 * @return The Univariate Sequence Dependent Variance Bound
	 * 
	 * @throws java.lang.Exception Thrown if the Date Dependent Variance Bound cannot be Computed
	 */

	public double dataDependentVarianceBound (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		return _els.evaluate (adblVariate) / adblVariate.length;
	}

	/**
	 * Retrieve the Multivariate Sequence Dependent Variance Bound
	 * 
	 * @param aadblVariate The Multivariate Sequence
	 * 
	 * @return The Multivariate Sequence Dependent Variance Bound
	 * 
	 * @throws java.lang.Exception Thrown if the Date Dependent Variance Bound cannot be Computed
	 */

	public double dataDependentVarianceBound (
		final double[][] aadblVariate)
		throws java.lang.Exception
	{
		return _els.evaluate (aadblVariate) / aadblVariate.length;
	}

	/**
	 * Compute the Lugosi Data-Dependent Variance Bound from the Sample and the Classifier Class Asymptotic
	 * 	Behavior: Source =>
	 * 
	 * 		G. Lugosi (2002): Pattern Classification and Learning Theory, in: L.Gyorfi, editor, Principles of
	 * 			Non-parametric Learning, 5-62, Springer, Wien.
	 * 
	 * @param adblVariate The Sample Univariate Array
	 * 
	 * @return The Lugosi Data-Dependent Variance Bound
	 * 
	 * @throws java.lang.Exception Thrown if the Lugosi Data-Dependent Variance Bound cannot be computed
	 */

	public double lugosiVarianceBound (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		org.drip.function.deterministic.R1ToR1 supClassifier = _els.supremumR1ToR1 (adblVariate);

		if (null == supClassifier)
			throw new java.lang.Exception
				("EmpiricalSupremumLossMetrics::lugosiVarianceBound => Cannot Find Supremum Classifier");

		org.drip.learning.bound.MeasureConcentrationExpectationBound casb =
			_els.learnerClass().concentrationLossBoundEvaluator();

		if (null == casb)
			throw new java.lang.Exception
				("EmpiricalSupremumLossMetrics::lugosiVarianceBound => Cannot Find Class Asymptote");

		return dataDependentVarianceBound (adblVariate) + casb.constant() + java.lang.Math.pow
			(adblVariate.length, casb.exponent());
	}

	/**
	 * Compute the Lugosi Data-Dependent Variance Bound from the Sample and the Classifier Class Asymptotic
	 * 	Behavior: Source =>
	 * 
	 * 		G. Lugosi (2002): Pattern Classification and Learning Theory, in: L.Gyorfi, editor, Principles of
	 * 			Non-parametric Learning, 5-62, Springer, Wien.
	 * 
	 * @param aadblVariate The Sample Multivariate Array
	 * 
	 * @return The Lugosi Data-Dependent Variance Bound
	 * 
	 * @throws java.lang.Exception Thrown if the Lugosi Data-Dependent Variance Bound cannot be computed
	 */

	public double lugosiVarianceBound (
		final double[][] aadblVariate)
		throws java.lang.Exception
	{
		org.drip.function.deterministic.RdToR1 supClassifier = _els.supremumRdToR1 (aadblVariate);

		if (null == supClassifier)
			throw new java.lang.Exception
				("EmpiricalSupremumLossMetrics::lugosiVarianceBound => Cannot Find Supremum Classifier");

		org.drip.learning.bound.MeasureConcentrationExpectationBound casb =
			_els.learnerClass().concentrationLossBoundEvaluator();

		if (null == casb)
			throw new java.lang.Exception
				("EmpiricalSupremumLossMetrics::lugosiVarianceBound => Cannot Find Class Asymptote");

		return dataDependentVarianceBound (aadblVariate) + casb.constant() + java.lang.Math.pow
			(aadblVariate.length, casb.exponent());
	}
}

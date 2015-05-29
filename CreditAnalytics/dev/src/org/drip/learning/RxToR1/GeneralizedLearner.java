
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
 * GeneralizedLearner implements the Learner Class that holds the Space of Normed R^x -> Normed R^1 Learning
 * 	Functions along with their Custom Empirical Loss. Class-Specific Asymptotic Sample, Covering Number based
 *  Upper Probability Bounds and other Parameters are also maintained.
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

public abstract class GeneralizedLearner implements org.drip.learning.RxToR1.EmpiricalLearningMetricEstimator
{
	private org.drip.learning.bound.CoveringNumberLossBound _funcClassCNLB = null;
	private org.drip.spaces.functionclass.NormedRxToNormedR1Finite _funcClassRxToR1 = null;

	/**
	 * GeneralizedLearner Constructor
	 * 
	 * @param funcClassRxToR1 R^x -> R^1 Function Class
	 * @param funcClassCNLB The Function Class Covering Number based Deviation Upper Probability Bound
	 * 	Generator
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public GeneralizedLearner (
		final org.drip.spaces.functionclass.NormedRxToNormedR1Finite funcClassRxToR1,
		final org.drip.learning.bound.CoveringNumberLossBound funcClassCNLB)
		throws java.lang.Exception
	{
		if (null == (_funcClassRxToR1 = funcClassRxToR1) || null == (_funcClassCNLB = funcClassCNLB))
			throw new java.lang.Exception ("GeneralizedLearner ctr: Invalid Inputs");
	}

	@Override public org.drip.spaces.functionclass.NormedRxToNormedR1Finite functionClass()
	{
		return _funcClassRxToR1;
	}

	/**
	 * Retrieve the Covering Number based Deviation Upper Probability Bound Generator
	 * 
	 * @return The Covering Number based Deviation Upper Probability Bound Generator
	 */

	public org.drip.learning.bound.CoveringNumberLossBound coveringLossBoundEvaluator()
	{
		return _funcClassCNLB;
	}

	/**
	 * Compute the Upper Bound of the Probability of the Absolute Deviation of the Empirical Mean from the
	 * 	Population Mean using the Function Class Supremum Covering Number for General-Purpose Learning
	 * 
	 * @param iSampleSize The Sample Size
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Upper Bound of the Probability of the Absolute Deviation of the Empirical Mean from the
	 * 	Population Mean using the Function Class Supremum Covering Number for General-Purpose Learning
	 * 
	 * @throws java.lang.Exception Thrown if the Upper Probability Bound cannot be computed
	 */

	public double genericCoveringProbabilityBound (
		final int iSampleSize,
		final double dblEpsilon,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		return _funcClassCNLB.deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * (bSupremum ?
			_funcClassRxToR1.populationSupremumCoveringNumber (dblEpsilon) :
				_funcClassRxToR1.populationCoveringNumber (dblEpsilon));
	}

	/**
	 * Compute the Minimum Possible Sample Size needed to generate the required Upper Probability Bound for
	 *  the Specified Empirical Deviation using the Covering Number Convergence Bounds.
	 *  
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param dblDeviationUpperProbabilityBound The Upper Bound of the Probability for the given Deviation
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Minimum Possible Sample Size
	 * 
	 * @throws java.lang.Exception Thrown if the Minimum Sample Size cannot be computed
	 */

	public double genericCoveringSampleSize (
		final double dblEpsilon,
		final double dblDeviationUpperProbabilityBound,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEpsilon) ||
			!org.drip.quant.common.NumberUtil.IsValid (dblDeviationUpperProbabilityBound))
			throw new java.lang.Exception
				("GeneralizedLearner::genericCoveringSampleSize => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcDeviationUpperProbabilityBound = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return genericCoveringProbabilityBound ((int) dblSampleSize, dblEpsilon, bSupremum);
			}
		};

		org.drip.function.solver1D.FixedPointFinderOutput fpfo = new
			org.drip.function.solver1D.FixedPointFinderZheng (dblDeviationUpperProbabilityBound,
				funcDeviationUpperProbabilityBound, false).findRoot();

		if (null == fpfo || !fpfo.containsRoot())
			throw new java.lang.Exception
				("GeneralizedLearner::genericCoveringSampleSize => Cannot Estimate Minimal Sample Size");

		return fpfo.getRoot();
	}

	/**
	 * Compute the Sample/Data Dependent Upper Bound of the Probability of the Absolute Deviation between
	 *  the Empirical and the Population Means using the Function Class Supremum Covering Number for
	 *  General-Purpose Learning
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param iSampleSize The Sample Size
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Sample/Data Dependent Upper Bound of the Probability of the Absolute Deviation between
	 *  the Empirical and the Population Means using the Function Class Supremum Covering Number for
	 *  General-Purpose Learning
	 * 
	 * @throws java.lang.Exception Thrown if the Upper Probability Bound cannot be computed
	 */

	public double genericCoveringProbabilityBound (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final int iSampleSize,
		final double dblEpsilon,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		return _funcClassCNLB.deviationProbabilityUpperBound (iSampleSize, dblEpsilon) *
			lossSampleCoveringNumber (gvvi, dblEpsilon, bSupremum);
	}

	/**
	 * Compute the Minimum Possible Sample Size needed to generate the required Upper Probability Bound for
	 *  the Specified Empirical Deviation using the Covering Number Convergence Bounds.
	 *  
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param dblDeviationUpperProbabilityBound The Upper Bound of the Probability for the given Deviation
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Minimum Possible Sample Size
	 * 
	 * @throws java.lang.Exception Thrown if the Minimum Sample Size cannot be computed
	 */

	public double genericCoveringSampleSize (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final double dblEpsilon,
		final double dblDeviationUpperProbabilityBound,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		if (null == gvvi || !org.drip.quant.common.NumberUtil.IsValid (dblEpsilon) ||
			!org.drip.quant.common.NumberUtil.IsValid (dblDeviationUpperProbabilityBound))
			throw new java.lang.Exception
				("GeneralizedLearner::genericCoveringSampleSize => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcDeviationUpperProbabilityBound = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return genericCoveringProbabilityBound (gvvi, (int) dblSampleSize, dblEpsilon, bSupremum);
			}
		};

		org.drip.function.solver1D.FixedPointFinderOutput fpfo = new
			org.drip.function.solver1D.FixedPointFinderZheng (dblDeviationUpperProbabilityBound,
				funcDeviationUpperProbabilityBound, false).findRoot();

		if (null == fpfo || !fpfo.containsRoot())
			throw new java.lang.Exception
				("GeneralizedLearner::genericCoveringSampleSize => Cannot Estimate Minimal Sample Size");

		return fpfo.getRoot();
	}

	/**
	 * Compute the Upper Bound of the Probability of the Absolute Deviation between the Empirical and the
	 * 	Population Means using the Function Class Supremum Covering Number for Regression Learning
	 * 
	 * @param iSampleSize The Sample Size
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Upper Bound of the Probability of the Absolute Deviation between the Empirical and the
	 * 	Population Means using the Function Class Supremum Covering Number for Regression Learning
	 * 
	 * @throws java.lang.Exception Thrown if the Upper Probability Bound cannot be computed
	 */

	public double regressorCoveringProbabilityBound (
		final int iSampleSize,
		final double dblEpsilon,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEpsilon) || 0. >= dblEpsilon || iSampleSize < (2. /
			(dblEpsilon * dblEpsilon)))
			throw new java.lang.Exception
				("GeneralizedLearner::regressorCoveringProbabilityBound => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcSampleCoefficient = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return 12. * dblSampleSize;
			}
		};

		return (new org.drip.learning.bound.CoveringNumberLossBound (funcSampleCoefficient, 2.,
			36.)).deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * (bSupremum ?
				_funcClassRxToR1.populationSupremumCoveringNumber (dblEpsilon / 6.) :
					_funcClassRxToR1.populationCoveringNumber (dblEpsilon / 6.));
	}

	/**
	 * Compute the Minimum Possible Sample Size needed to generate the required Upper Probability Bound for
	 *  the Specified Empirical Deviation using the Covering Number Convergence Bounds for Regression
	 *  Learning.
	 *  
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param dblDeviationUpperProbabilityBound The Upper Bound of the Probability for the given Deviation
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Minimum Possible Sample Size
	 * 
	 * @throws java.lang.Exception Thrown if the Minimum Sample Size cannot be computed
	 */

	public double regressorCoveringSampleSize (
		final double dblEpsilon,
		final double dblDeviationUpperProbabilityBound,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEpsilon) ||
			!org.drip.quant.common.NumberUtil.IsValid (dblDeviationUpperProbabilityBound))
			throw new java.lang.Exception
				("GeneralizedLearner::regressorCoveringSampleSize => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcDeviationUpperProbabilityBound = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return regressorCoveringProbabilityBound ((int) dblSampleSize, dblEpsilon, bSupremum);
			}
		};

		org.drip.function.solver1D.FixedPointFinderOutput fpfo = new
			org.drip.function.solver1D.FixedPointFinderZheng (dblDeviationUpperProbabilityBound,
				funcDeviationUpperProbabilityBound, false).findRoot();

		if (null == fpfo || !fpfo.containsRoot())
			throw new java.lang.Exception
				("GeneralizedLearner::regressorCoveringSampleSize => Cannot Estimate Minimal Sample Size");

		return fpfo.getRoot();
	}

	/**
	 * Compute the Sample/Data Dependent Upper Bound of the Probability of the Absolute Deviation between
	 *  the Empirical and the Population Means using the Function Class Supremum Covering Number for
	 *  Regression Learning
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param iSampleSize The Sample Size
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Sample/Data Dependent Upper Bound of the Probability of the Absolute Deviation between
	 *  the Empirical and the Population Means using the Function Class Supremum Covering Number for
	 *  Regression Learning
	 * 
	 * @throws java.lang.Exception Thrown if the Upper Probability Bound cannot be computed
	 */

	public double regressorCoveringProbabilityBound (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final int iSampleSize,
		final double dblEpsilon,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEpsilon) || 0. >= dblEpsilon || iSampleSize < (2. /
			(dblEpsilon * dblEpsilon)))
			throw new java.lang.Exception
				("GeneralizedLearner::regressorCoveringProbabilityBound => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcSampleCoefficient = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return 12. * dblSampleSize;
			}
		};

		return (new org.drip.learning.bound.CoveringNumberLossBound (funcSampleCoefficient, 2.,
			36.)).deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * lossSampleCoveringNumber (gvvi,
				dblEpsilon / 6., bSupremum);
	}

	/**
	 * Compute the Minimum Possible Sample Size needed to generate the required Upper Probability Bound for
	 *  the Specified Empirical Deviation using the Covering Number Convergence Bounds for Regression
	 *  Learning.
	 *  
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param dblEpsilon The Deviation of the Empirical Mean from the Population Mean
	 * @param dblDeviationUpperProbabilityBound The Upper Bound of the Probability for the given Deviation
	 * @param bSupremum TRUE => Use the Supremum Metric in place of the Built-in Metric
	 * 
	 * @return The Minimum Possible Sample Size
	 * 
	 * @throws java.lang.Exception Thrown if the Minimum Sample Size cannot be computed
	 */

	public double regressorCoveringSampleSize (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final double dblEpsilon,
		final double dblDeviationUpperProbabilityBound,
		final boolean bSupremum)
		throws java.lang.Exception
	{
		if (null == gvvi || !org.drip.quant.common.NumberUtil.IsValid (dblEpsilon) ||
			!org.drip.quant.common.NumberUtil.IsValid (dblDeviationUpperProbabilityBound))
			throw new java.lang.Exception
				("GeneralizedLearner::regressorCoveringSampleSize => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcDeviationUpperProbabilityBound = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return regressorCoveringProbabilityBound (gvvi, (int) dblSampleSize, dblEpsilon, bSupremum);
			}
		};

		org.drip.function.solver1D.FixedPointFinderOutput fpfo = new
			org.drip.function.solver1D.FixedPointFinderZheng (dblDeviationUpperProbabilityBound,
				funcDeviationUpperProbabilityBound, false).findRoot();

		if (null == fpfo || !fpfo.containsRoot())
			throw new java.lang.Exception
				("GeneralizedLearner::regressorCoveringSampleSize => Cannot Estimate Minimal Sample Size");

		return fpfo.getRoot();
	}
}

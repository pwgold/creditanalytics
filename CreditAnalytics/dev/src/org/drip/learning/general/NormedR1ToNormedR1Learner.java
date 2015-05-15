
package org.drip.learning.general;

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
 * NormedR1ToNormedR1Learner implements the Class that holds the Space of Normed R^1 -> Normed R^1 Learning
 * 	Functions. Class-Specific Asymptotic Sample, Covering-Number based Upper Probability Bounds and other
 * 	Parameters are also maintained.
 *  
 * The Reference are:
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
 *  5) Vapnik, V. N. (1998): Statistical learning Theory, Wiley, New York.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedR1ToNormedR1Learner extends org.drip.spaces.functionclass.NormedR1ToNormedR1Class
{
	private org.drip.learning.general.CoveringDeviationProbabilityBound _cdpb = null;
	private org.drip.learning.general.ConcentrationLossExpectationBound _cleb = null;

	/**
	 * NormedR1ToNormedR1Learner Constructor
	 * 
	 * @param aR1ToR1Learner Array of Candidate Learning Functions belonging to the Function Class
	 * @param cdpb The Covering Number based Deviation Upper Probability Bound Generator
	 * @param cleb The Concentration of Measure based Loss Expectation Upper Bound Evaluator
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public NormedR1ToNormedR1Learner (
		final org.drip.spaces.RxToR1.NormedR1ToNormedR1[] aR1ToR1Learner,
		final org.drip.learning.general.CoveringDeviationProbabilityBound cdpb,
		final org.drip.learning.general.ConcentrationLossExpectationBound cleb)
		throws java.lang.Exception
	{
		super (aR1ToR1Learner);

		_cdpb = cdpb;
		_cleb = cleb;
	}

	/**
	 * Retrieve the Array of Learning Classifier Functions
	 * 
	 * @return The Array of Learning Classifier Functions
	 */

	public org.drip.learning.classifier.AbstractBinaryClassifier[] classifiers()
	{
		org.drip.function.deterministic.R1ToR1[] aR1ToR1 = functionR1ToR1Set();

		int iNumFunction = aR1ToR1.length;
		org.drip.learning.classifier.AbstractBinaryClassifier[] aABE = new
			org.drip.learning.classifier.AbstractBinaryClassifier[iNumFunction];

		for (int i = 0; i < iNumFunction; ++i) {
			if (!(aR1ToR1[i] instanceof org.drip.learning.classifier.AbstractBinaryClassifier))
				return null;

			aABE[i] = (org.drip.learning.classifier.AbstractBinaryClassifier) aR1ToR1[i];
		}

		return aABE;
	}

	/**
	 * Retrieve the Array of Learning Regression Functions
	 * 
	 * @return The Array of Learning Regression Functions
	 */

	public org.drip.function.deterministic.R1ToR1[] regressors()
	{
		return functionR1ToR1Set();
	}

	/**
	 * Retrieve the Concentration of Measure based Loss Expectation Upper Bound Evaluator Instance
	 * 
	 * @return The Concentration of Measure based Loss Expectation Upper Bound Evaluator Instance
	 */

	public org.drip.learning.general.ConcentrationLossExpectationBound concentrationLossBoundEvaluator()
	{
		return _cleb;
	}

	/**
	 * Retrieve the Covering Number based Deviation Upper Probability Bound Generator
	 * 
	 * @return The Covering Number based Deviation Upper Probability Bound Generator
	 */

	public org.drip.learning.general.CoveringDeviationProbabilityBound coveringLossBoundEvaluator()
	{
		return _cdpb;
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
		return _cdpb.deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * (bSupremum ?
			populationSupremumCoveringNumber (dblEpsilon) : populationCoveringNumber (dblEpsilon));
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
				("NormedR1ToNormedR1Learner::genericCoveringSampleSize => Invalid Inputs");

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

		if (null == fpfo || null == fpfo)
			throw new java.lang.Exception
				("NormedR1ToNormedR1Learner::genericCoveringSampleSize => Cannot Estimate Minimal Sample Size");

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
		return _cdpb.deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * (bSupremum ?
			sampleSupremumCoveringNumber (gvvi, dblEpsilon) : sampleCoveringNumber (gvvi, dblEpsilon));
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
				("NormedR1ToNormedR1Learner::genericCoveringSampleSize => Invalid Inputs");

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

		if (null == fpfo || null == fpfo)
			throw new java.lang.Exception
				("NormedR1ToNormedR1Learner::genericCoveringSampleSize => Cannot Estimate Minimal Sample Size");

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
				("NormedR1ToNormedR1Learner::regressorCoveringProbabilityBound => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcSampleCoefficient = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return 12. * dblSampleSize;
			}
		};

		return (new org.drip.learning.general.CoveringDeviationProbabilityBound (funcSampleCoefficient, 2.,
			36.)).deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * (bSupremum ?
				populationSupremumCoveringNumber (dblEpsilon / 6.) : populationCoveringNumber (dblEpsilon /
					6.));
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
				("NormedR1ToNormedR1Learner::regressorCoveringSampleSize => Invalid Inputs");

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

		if (null == fpfo || null == fpfo)
			throw new java.lang.Exception
				("NormedR1ToNormedR1Learner::regressorCoveringSampleSize => Cannot Estimate Minimal Sample Size");

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
				("NormedR1ToNormedR1Learner::regressorCoveringProbabilityBound => Invalid Inputs");

		org.drip.function.deterministic.R1ToR1 funcSampleCoefficient = new
			org.drip.function.deterministic.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblSampleSize)
				throws java.lang.Exception
			{
				return 12. * dblSampleSize;
			}
		};

		return (new org.drip.learning.general.CoveringDeviationProbabilityBound (funcSampleCoefficient, 2.,
			36.)).deviationProbabilityUpperBound (iSampleSize, dblEpsilon) * (bSupremum ?
				sampleSupremumCoveringNumber (gvvi, dblEpsilon / 6.) : sampleCoveringNumber (gvvi, dblEpsilon
					/ 6.));
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
				("NormedR1ToNormedR1Learner::regressorCoveringSampleSize => Invalid Inputs");

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

		if (null == fpfo || null == fpfo)
			throw new java.lang.Exception
				("NormedR1ToNormedR1Learner::regressorCoveringSampleSize => Cannot Estimate Minimal Sample Size");

		return fpfo.getRoot();
	}
}


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
 * EmpiricalLossSupremum contains the Implementation of the Bounded Empirical Classifier Loss dependent on
 *  Multivariate Random Variables where the Multivariate Function is a Linear Combination of Bounded
 * 	Univariate Functions acting on each Random Variate.
 *
 * @author Lakshmi Krishnamurthy
 */

public class EmpiricalLossSupremum extends org.drip.sequence.functional.BoundedMultivariateRandom {

	class Supremum {
		int _iIndex = -1;
		double _dblValue = java.lang.Double.NaN;

		Supremum (
			int iIndex,
			double dblValue)
		{
			_iIndex = iIndex;
			_dblValue = dblValue;
		}
	}

	private org.drip.spaces.RxToR1.NormedR1ToNormedR1[] _aR1ToR1 = null;
	private org.drip.spaces.RxToR1.NormedRdToNormedR1[] _aRdToR1 = null;
	private org.drip.learning.RxToR1.EmpiricalLearningMetricEstimator _elme = null;
	private org.drip.spaces.instance.GeneralizedValidatedVectorInstance _gvviY = null;

	private Supremum supremumR1 (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviX)
	{
		if (null == _aR1ToR1) return null;

		int iSupremumIndex  = 0;
		int iNumR1ToR1 = _aR1ToR1.length;
		double dblEmpiricalLossSupremum = 0.;

		for (int i = 0 ; i < iNumR1ToR1; ++i) {
			org.drip.function.deterministic.R1ToR1 funcR1ToR1 = _aR1ToR1[i].function();

			if (null == funcR1ToR1) return null;

			double dblEmpiricalLoss = 0.;

			try {
				dblEmpiricalLoss += _elme.empiricalLoss (funcR1ToR1, gvviX, _gvviY);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			if (dblEmpiricalLoss > dblEmpiricalLossSupremum) {
				iSupremumIndex = i;
				dblEmpiricalLossSupremum = dblEmpiricalLoss;
			}
		}

		return new Supremum (iSupremumIndex, dblEmpiricalLossSupremum / gvviX.sampleSize());
	}

	private Supremum supremumRd (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviX)
	{
		if (null == _aRdToR1) return null;

		int iSupremumIndex  = 0;
		int iNumRdToR1 = _aRdToR1.length;
		double dblEmpiricalLossSupremum = 0.;

		for (int i = 0 ; i < iNumRdToR1; ++i) {
			org.drip.function.deterministic.RdToR1 funcRdToR1 = _aRdToR1[i].function();

			if (null == funcRdToR1) return null;

			double dblEmpiricalLoss = 0.;

			try {
				dblEmpiricalLoss += _elme.empiricalLoss (funcRdToR1, gvviX, _gvviY);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			if (dblEmpiricalLoss > dblEmpiricalLossSupremum) {
				iSupremumIndex = i;
				dblEmpiricalLossSupremum = dblEmpiricalLoss;
			}
		}

		return new Supremum (iSupremumIndex, dblEmpiricalLossSupremum / gvviX.sampleSize());
	}

	/**
	 * EmpiricalLossSupremum Constructor
	 * 
	 * @param elme The Empirical Learning Metric Estimator Instance
	 * @param gvviY The Validated Outcome Instance
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public EmpiricalLossSupremum (
		final org.drip.learning.RxToR1.EmpiricalLearningMetricEstimator elme,
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviY)
		throws java.lang.Exception
	{
		if (null == (_elme = elme) || null == (_gvviY = gvviY))
			throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");

		org.drip.spaces.RxToR1.NormedRxToNormedR1[] aRxToR1 = _elme.functionClass().functionSpaces();

		if (null == aRxToR1) throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");

		if (aRxToR1 instanceof org.drip.spaces.RxToR1.NormedR1ToNormedR1[])
			_aR1ToR1 = (org.drip.spaces.RxToR1.NormedR1ToNormedR1[]) aRxToR1;
		else
			_aRdToR1 = (org.drip.spaces.RxToR1.NormedRdToNormedR1[]) aRxToR1;

		int iNumRxToR1 = aRxToR1.length;

		for (int i = 0; i < iNumRxToR1; ++i) {
			if (null == aRxToR1[i])
				throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Empirical Learning Metric Estimator Instance
	 * 
	 * @return The Empirical Learning Metric Estimator Instance
	 */

	public org.drip.learning.RxToR1.EmpiricalLearningMetricEstimator elme()
	{
		return _elme;
	}

	/**
	 * Retrieve the Validated Outcome Instance
	 * 
	 * @return The Validated Outcome Instance
	 */

	public org.drip.spaces.instance.GeneralizedValidatedVectorInstance empiricalOutcomes()
	{
		return _gvviY;
	}

	/**
	 * Retrieve the Supremum R^1 -> R^1 Function Instance for the specified Variate Sequence
	 * 
	 * @param adblX The Predictor Instance
	 * 
	 * @return The Supremum R^1 -> R^1 Function Instance
	 */

	public org.drip.function.deterministic.R1ToR1 supremumR1ToR1 (
		final double[] adblX)
	{
		Supremum sup = null;

		try {
			sup = supremumR1 (new org.drip.spaces.instance.ValidatedRealUnidimensional
				(org.drip.spaces.tensor.ContinuousRealUnidimensionalVector.Standard(), adblX));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return _aR1ToR1[sup._iIndex].function();
	}

	/**
	 * Retrieve the Supremum R^d -> R^1 Function Instance for the specified Variate Sequence
	 * 
	 * @param aadblX The Predictor Instance
	 * 
	 * @return The Supremum R^d -> R^1 Function Instance
	 */

	public org.drip.function.deterministic.RdToR1 supremumRdToR1 (
		final double[][] aadblX)
	{
		Supremum sup = null;

		try {
			sup = supremumRd (new org.drip.spaces.instance.ValidatedRealMultidimensional
				(org.drip.spaces.tensor.ContinuousRealMultidimensionalVector.Standard (aadblX.length),
					aadblX));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return _aRdToR1[sup._iIndex].function();
	}

	@Override public double evaluate (
		final double[] adblX)
		throws java.lang.Exception
	{
		Supremum sup = supremumR1 (new org.drip.spaces.instance.ValidatedRealUnidimensional
			(org.drip.spaces.tensor.ContinuousRealUnidimensionalVector.Standard(), adblX));

		if (null == sup) throw new java.lang.Exception ("EmpiricalLossSupremum::evaluate => Invalid Inputs");

		return sup._dblValue;
	}

	/**
	 * Retrieve the Worst-case Loss over the Multivariate Sequence
	 * 
	 * @param aadblX The Multivariate Array
	 * 
	 * @return The Worst-case Loss over the Multivariate Sequence
	 * 
	 * @throws java.lang.Exception Thrown if the Worst-Case Loss cannot be computed
	 */

	public double evaluate (
		final double[][] aadblX)
		throws java.lang.Exception
	{
		if (null == aadblX)
			throw new java.lang.Exception ("EmpiricalLossSupremum::evaluate => Invalid Inputs");

		Supremum sup = supremumRd (new org.drip.spaces.instance.ValidatedRealMultidimensional
			(org.drip.spaces.tensor.ContinuousRealMultidimensionalVector.Standard (aadblX.length), aadblX));

		if (null == sup) throw new java.lang.Exception ("EmpiricalLossSupremum::evaluate => Invalid Inputs");

		return sup._dblValue;
	}

	@Override public double targetVariateVarianceBound (
		final int iTargetVariateIndex)
		throws java.lang.Exception
	{
		return 1. / (_gvviY.sampleSize() * _gvviY.sampleSize());
	}
}

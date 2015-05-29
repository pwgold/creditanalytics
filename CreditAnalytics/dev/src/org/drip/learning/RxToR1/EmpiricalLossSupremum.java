
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

	private double[] _adblEmpiricalOutcome = null;
	private org.drip.spaces.RxToR1.NormedR1ToNormedR1[] _aR1ToR1 = null;
	private org.drip.spaces.RxToR1.NormedRdToNormedR1[] _aRdToR1 = null;
	private org.drip.learning.RxToR1.EmpiricalLearningMetricEstimator _elme = null;

	private Supremum supremum (
		final double[] adblVariate)
	{
		if (null == _aR1ToR1) return null;

		int iSupremumIndex  = 0;
		int iNumR1ToR1 = _aR1ToR1.length;
		double dblEmpiricalLossSupremum = 0.;
		int iNumEmpiricalOutcome = _adblEmpiricalOutcome.length;

		if (null == adblVariate || adblVariate.length != iNumEmpiricalOutcome) return null;

		for (int i = 0 ; i < iNumR1ToR1; ++i) {
			org.drip.function.deterministic.R1ToR1 funcR1ToR1 = _aR1ToR1[i].function();

			if (null == funcR1ToR1) return null;

			double dblEmpiricalLoss = 0.;

			for (int j = 0; j < iNumEmpiricalOutcome; ++j) {
				try {
					dblEmpiricalLoss += _elme.empiricalLoss (funcR1ToR1, adblVariate[j],
						_adblEmpiricalOutcome[j]);
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return null;
				}
			}

			if (dblEmpiricalLoss > dblEmpiricalLossSupremum) {
				iSupremumIndex = i;
				dblEmpiricalLossSupremum = dblEmpiricalLoss;
			}
		}

		return new Supremum (iSupremumIndex, dblEmpiricalLossSupremum / iNumEmpiricalOutcome);
	}

	private Supremum supremum (
		final double[][] aadblVariate)
	{
		if (null == _aRdToR1) return null;

		int iSupremumIndex  = 0;
		int iNumR1ToR1 = _aRdToR1.length;
		double dblEmpiricalLossSupremum = 0.;
		int iNumEmpiricalOutcome = _adblEmpiricalOutcome.length;

		if (null == aadblVariate || aadblVariate.length != iNumEmpiricalOutcome) return null;

		for (int i = 0 ; i < iNumR1ToR1; ++i) {
			org.drip.function.deterministic.RdToR1 funcRdToR1 = _aRdToR1[i].function();

			if (null == funcRdToR1) return null;

			double dblEmpiricalLoss = 0.;

			for (int j = 0; j < iNumEmpiricalOutcome; ++j) {
				try {
					dblEmpiricalLoss += _elme.empiricalLoss (funcRdToR1, aadblVariate[j],
						_adblEmpiricalOutcome[j]);
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return null;
				}
			}

			if (dblEmpiricalLoss > dblEmpiricalLossSupremum) {
				iSupremumIndex = i;
				dblEmpiricalLossSupremum = dblEmpiricalLoss;
			}
		}

		return new Supremum (iSupremumIndex, dblEmpiricalLossSupremum / iNumEmpiricalOutcome);
	}

	/**
	 * EmpiricalLossSupremum Constructor
	 * 
	 * @param elme The Empirical Learning Metric Estimator Instance
	 * @param adblEmpiricalOutcome Array of the Empirical Outcomes
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public EmpiricalLossSupremum (
		final org.drip.learning.RxToR1.EmpiricalLearningMetricEstimator elme,
		final double[] adblEmpiricalOutcome)
		throws java.lang.Exception
	{
		if (null == (_elme = elme) || null == (_adblEmpiricalOutcome = adblEmpiricalOutcome))
			throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");

		org.drip.spaces.RxToR1.NormedRxToNormedR1[] aRxToR1 = _elme.functionClass().functionSpaces();

		if (null == aRxToR1) throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");

		if (aRxToR1 instanceof org.drip.spaces.RxToR1.NormedR1ToNormedR1[])
			_aR1ToR1 = (org.drip.spaces.RxToR1.NormedR1ToNormedR1[]) aRxToR1;
		else
			_aRdToR1 = (org.drip.spaces.RxToR1.NormedRdToNormedR1[]) aRxToR1;

		int iNumRxToR1 = aRxToR1.length;
		int iNumEmpiricalOutcome = _adblEmpiricalOutcome.length;

		if (0 == iNumEmpiricalOutcome)
			throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");

		for (int i = 0; i < iNumRxToR1; ++i) {
			if (null == aRxToR1[i])
				throw new java.lang.Exception ("EmpiricalLossSupremum ctr: Invalid Inputs");
		}

		for (int i = 0; i < iNumEmpiricalOutcome; ++i) {
			if (0 > _adblEmpiricalOutcome[i])
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
	 * Retrieve the Array of Empirical Outcomes
	 * 
	 * @return The Array of Empirical Outcomes
	 */

	public double[] empiricalOutcomes()
	{
		return _adblEmpiricalOutcome;
	}

	/**
	 * Retrieve the Supremum R^1 -> R^1 Function Instance for the specified Variate Sequence
	 * 
	 * @param adblVariate The Univariate Sequence
	 * 
	 * @return The Supremum R^1 -> R^1 Function Instance
	 */

	public org.drip.function.deterministic.R1ToR1 supremumR1ToR1 (
		final double[] adblVariate)
	{
		Supremum sup = supremum (adblVariate);

		if (null == sup) return null;

		return _aR1ToR1[sup._iIndex].function();
	}

	/**
	 * Retrieve the Supremum R^d -> R^1 Function Instance for the specified Variate Sequence
	 * 
	 * @param aadblVariate The Multivariate Sequence
	 * 
	 * @return The Supremum R^d -> R^1 Function Instance
	 */

	public org.drip.function.deterministic.RdToR1 supremumRdToR1 (
		final double[][] aadblVariate)
	{
		Supremum sup = supremum (aadblVariate);

		if (null == sup) return null;

		return _aRdToR1[sup._iIndex].function();
	}

	@Override public double evaluate (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		Supremum sup = supremum (adblVariate);

		if (null == sup) throw new java.lang.Exception ("EmpiricalLossSupremum::evaluate => Invalid Inputs");

		return sup._dblValue;
	}

	/**
	 * Retrieve the Worst-case Loss over the Multivariate Sequence
	 * 
	 * @param aadblVariate The Multivariate Array
	 * 
	 * @return The Worst-case Loss over the Multivariate Sequence
	 * 
	 * @throws java.lang.Exception Thrown if the Worst-Case Loss cannot be computed
	 */

	public double evaluate (
		final double[][] aadblVariate)
		throws java.lang.Exception
	{
		Supremum sup = supremum (aadblVariate);

		if (null == sup) throw new java.lang.Exception ("EmpiricalLossSupremum::evaluate => Invalid Inputs");

		return sup._dblValue;
	}

	@Override public double targetVariateVarianceBound (
		final int iTargetVariateIndex)
		throws java.lang.Exception
	{
		return 1. / (_adblEmpiricalOutcome.length * _adblEmpiricalOutcome.length);
	}
}

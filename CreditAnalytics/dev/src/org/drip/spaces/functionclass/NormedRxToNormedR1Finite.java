
package org.drip.spaces.functionclass;

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
 * NormedRxToNormedR1Finite implements the Class F with f E f : Normed R^x -> Normed R^1 Space of Finite 
 * 	Functions.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public class NormedRxToNormedR1Finite {
	private org.drip.spaces.RxToR1.NormedRxToNormedR1[] _aNormedRxToNormedR1 = null;

	/**
	 * Compute the Dyadic Entropy Number from the nth Entropy Number
	 * 
	 * @param dblLogNEntropyNumber Log of the nth Entropy Number
	 * 
	 * @return The Dyadic Entropy Number
	 * 
	 * @throws java.lang.Exception Thrown if the Dyadic Entropy Number cannot be calculated
	 */

	public static final double DyadicEntropyNumber (
		final double dblLogNEntropyNumber)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblLogNEntropyNumber))
			throw new java.lang.Exception ("NormedRxToNormedR1Finite::DyadicEntropyNumber => Invalid Inputs");

		return 1. + (dblLogNEntropyNumber / java.lang.Math.log (2.));
	}

	protected NormedRxToNormedR1Finite (
		final org.drip.spaces.RxToR1.NormedRxToNormedR1[] aNormedRxToNormedR1)
		throws java.lang.Exception
	{
		int iClassSize = null == (_aNormedRxToNormedR1 = aNormedRxToNormedR1) ? 0 :
			_aNormedRxToNormedR1.length;

		if (null != _aNormedRxToNormedR1 && 0 == iClassSize)
			throw new java.lang.Exception ("NormedRxToNormedR1Finite ctr: Invalid Inputs");

		for (int i = 0; i < iClassSize; ++i) {
			if (null == _aNormedRxToNormedR1[i])
				throw new java.lang.Exception ("NormedRxToNormedR1Finite ctr: Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Agnostic Covering Number Upper/Lower Bounds for the Function Class
	 * 
	 * @return The Agnostic Covering Number Upper/Lower Bounds for the Function Class
	 */

	public org.drip.spaces.cover.FunctionClassCoveringBounds agnosticCoveringNumberBounds()
	{
		return null;
	}

	/**
	 * Retrieve the Scale-Sensitive Covering Number Upper/Lower Bounds given the Specified Sample for the
	 *  Function Class
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param funcR1ToR1FatShatter The Cover Fat Shattering Coefficient R^1 -> R^1
	 * 
	 * @return The Scale-Sensitive Covering Number Upper/Lower Bounds given the Specified Sample for the
	 *  Function Class
	 */

	public org.drip.spaces.cover.FunctionClassCoveringBounds scaleSensitiveCoveringBounds (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final org.drip.function.definition.R1ToR1 funcR1ToR1FatShatter)
	{
		if (null == gvvi || null == funcR1ToR1FatShatter) return null;

		int iSampleSize = -1;

		if (gvvi instanceof org.drip.spaces.instance.ValidatedR1) {
			double[] adblInstance = ((org.drip.spaces.instance.ValidatedR1) gvvi).instance();

			if (null == adblInstance) return null;

			iSampleSize = adblInstance.length;
		} else if (gvvi instanceof org.drip.spaces.instance.ValidatedRd) {
			double[][] aadblInstance = ((org.drip.spaces.instance.ValidatedRd) gvvi).instance();

			if (null == aadblInstance) return null;

			iSampleSize = aadblInstance.length;
		}

		try {
			return new org.drip.spaces.cover.ScaleSensitiveCoveringBounds (funcR1ToR1FatShatter,
				iSampleSize);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Estimate for the Function Class Population Covering Number
	 * 
	 * @param dblCover The Size of the Cover
	 * 
	 * @return Function Class Population Covering Number Estimate
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double populationCoveringNumber (
		final double dblCover)
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::populationCoveringNumber => Finite Function Set Unspecified");

		int iFunctionSpaceSize = _aNormedRxToNormedR1.length;

		double dblPopulationCoveringNumber = _aNormedRxToNormedR1[0].populationCoveringNumber (dblCover);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblPopulationCoveringNumber))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::populationCoveringNumber => Cannot Compute Population Covering Number");

		for (int i = 1; i < iFunctionSpaceSize; ++i) {
			double dblFunctionPopulationCoveringNumber = _aNormedRxToNormedR1[i].populationCoveringNumber
				(dblCover);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblFunctionPopulationCoveringNumber))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::populationCoveringNumber => Cannot Compute Population Covering Number");

			if (dblPopulationCoveringNumber < dblFunctionPopulationCoveringNumber)
				dblPopulationCoveringNumber = dblFunctionPopulationCoveringNumber;
		}

		return dblPopulationCoveringNumber;
	}

	/**
	 * Estimate for the Function Class Population Supremum Covering Number
	 * 
	 * @param dblCover The Size of the Cover
	 * 
	 * @return Function Class Population Supremum Covering Number Estimate
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double populationSupremumCoveringNumber (
		final double dblCover)
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::populationSupremumCoveringNumber => Finite Function Set Unspecified");

		int iFunctionSpaceSize = _aNormedRxToNormedR1.length;

		double dblPopulationSupremumCoveringNumber = _aNormedRxToNormedR1[0].populationCoveringNumber
			(dblCover);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblPopulationSupremumCoveringNumber))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::populationSupremumCoveringNumber => Cannot Compute Population Supremum Covering Number");

		for (int i = 1; i < iFunctionSpaceSize; ++i) {
			double dblFunctionPopulationSupremumCoveringNumber =
				_aNormedRxToNormedR1[i].populationSupremumCoveringNumber (dblCover);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblFunctionPopulationSupremumCoveringNumber))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::populationSupremumCoveringNumber => Cannot Compute Population Supremum Covering Number");

			if (dblPopulationSupremumCoveringNumber < dblFunctionPopulationSupremumCoveringNumber)
				dblPopulationSupremumCoveringNumber = dblFunctionPopulationSupremumCoveringNumber;
		}

		return dblPopulationSupremumCoveringNumber;
	}

	/**
	 * Estimate for the Scale-Sensitive Sample Covering Number for the specified Cover Size
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param dblCover The Size of the Cover
	 * 
	 * @return The Scale-Sensitive Sample Covering Number for the specified Cover Size
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double sampleCoveringNumber (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final double dblCover)
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::sampleCoveringNumber => Finite Function Set Unspecified");

		int iFunctionSpaceSize = _aNormedRxToNormedR1.length;

		double dblSampleCoveringNumber = _aNormedRxToNormedR1[0].sampleCoveringNumber (gvvi, dblCover);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblSampleCoveringNumber))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::sampleCoveringNumber => Cannot Compute Sample Covering Number");

		for (int i = 1; i < iFunctionSpaceSize; ++i) {
			double dblFunctionSampleCoveringNumber = _aNormedRxToNormedR1[i].sampleCoveringNumber (gvvi,
				dblCover);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblFunctionSampleCoveringNumber))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::sampleCoveringNumber => Cannot Compute Sample Covering Number");

			if (dblSampleCoveringNumber < dblFunctionSampleCoveringNumber)
				dblSampleCoveringNumber = dblFunctionSampleCoveringNumber;
		}

		return dblSampleCoveringNumber;
	}

	/**
	 * Estimate for the Scale-Sensitive Sample Supremum Covering Number for the specified Cover Size
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param dblCover The Size of the Cover
	 * 
	 * @return The Scale-Sensitive Sample Supremum Covering Number for the specified Cover Size
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double sampleSupremumCoveringNumber (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final double dblCover)
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::sampleSupremumCoveringNumber => Finite Function Set Unspecified");

		int iFunctionSpaceSize = _aNormedRxToNormedR1.length;

		double dblSampleSupremumCoveringNumber = _aNormedRxToNormedR1[0].sampleSupremumCoveringNumber (gvvi,
			dblCover);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblSampleSupremumCoveringNumber))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::sampleSupremumCoveringNumber => Cannot Compute Sample Supremum Covering Number");

		for (int i = 1; i < iFunctionSpaceSize; ++i) {
			double dblFunctionSampleSupremumCoveringNumber =
				_aNormedRxToNormedR1[i].sampleSupremumCoveringNumber (gvvi, dblCover);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblFunctionSampleSupremumCoveringNumber))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::sampleSupremumCoveringNumber => Cannot Compute Sample Supremum Covering Number");

			if (dblSampleSupremumCoveringNumber < dblFunctionSampleSupremumCoveringNumber)
				dblSampleSupremumCoveringNumber = dblFunctionSampleSupremumCoveringNumber;
		}

		return dblSampleSupremumCoveringNumber;
	}

	/**
	 * Retrieve the Array of Function Spaces in the Class
	 * 
	 * @return The Array of Function Spaces in the Class
	 */

	public org.drip.spaces.RxToR1.NormedRxToNormedR1[] functionSpaces()
	{
		return _aNormedRxToNormedR1;
	}

	/**
	 * Retrieve the Input Vector Space
	 * 
	 * @return The Input Vector Space
	 */

	public org.drip.spaces.metric.GeneralizedMetricVectorSpace inputMetricVectorSpace()
	{
		return null == _aNormedRxToNormedR1 ? null : _aNormedRxToNormedR1[0].inputMetricVectorSpace();
	}

	/**
	 * Retrieve the Output Vector Space
	 * 
	 * @return The Output Vector Space
	 */

	public org.drip.spaces.metric.R1Normed outputMetricVectorSpace()
	{
		return null == _aNormedRxToNormedR1 ? null : _aNormedRxToNormedR1[0].outputMetricVectorSpace();
	}

	/**
	 * Compute the Operator Population Metric Norm
	 * 
	 * @return The Operator Population Metric Norm
	 * 
	 * @throws java.lang.Exception Thrown if the Operator Population Metric Norm cannot be computed
	 */

	public double operatorPopulationMetricNorm()
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorPopulationMetricNorm => Finite Function Set Unspecified");

		int iNumFunction = _aNormedRxToNormedR1.length;

		double dblOperatorPopulationMetricNorm = _aNormedRxToNormedR1[0].populationMetricNorm();

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorPopulationMetricNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorPopulationMetricNorm => Cannot compute Population Metric Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblPopulationMetricNorm = _aNormedRxToNormedR1[i].populationMetricNorm();

			if (!org.drip.quant.common.NumberUtil.IsValid (dblPopulationMetricNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::operatorPopulationMetricNorm => Cannot compute Population Metric Norm for Function #"
						+ i);

			if (dblOperatorPopulationMetricNorm > dblPopulationMetricNorm)
				dblOperatorPopulationMetricNorm = dblPopulationMetricNorm;
		}

		return dblOperatorPopulationMetricNorm;
	}

	/**
	 * Compute the Operator Population Supremum Norm
	 * 
	 * @return The Operator Population Supremum Norm
	 * 
	 * @throws java.lang.Exception Thrown if the Operator Population Supremum Norm cannot be computed
	 */

	public double operatorPopulationSupremumNorm()
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorPopulationSupremumNorm => Finite Function Set Unspecified");

		int iNumFunction = _aNormedRxToNormedR1.length;

		double dblOperatorPopulationSupremumNorm = _aNormedRxToNormedR1[0].populationESS();

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorPopulationSupremumNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorPopulationSupremumNorm => Cannot compute Population Supremum Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblPopulationSupremumNorm = _aNormedRxToNormedR1[i].populationESS();

			if (!org.drip.quant.common.NumberUtil.IsValid (dblPopulationSupremumNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::operatorPopulationSupremumNorm => Cannot compute Population Supremum Norm for Function #"
						+ i);

			if (dblOperatorPopulationSupremumNorm > dblPopulationSupremumNorm)
				dblOperatorPopulationSupremumNorm = dblPopulationSupremumNorm;
		}

		return dblOperatorPopulationSupremumNorm;
	}

	/**
	 * Compute the Operator Sample Metric Norm
	 * 
	 * @param gvvi The Validated Vector Space Instance
	 * 
	 * @return The Operator Sample Metric Norm
	 * 
	 * @throws java.lang.Exception Thrown if the Operator Sample Metric Norm cannot be computed
	 */

	public double operatorSampleMetricNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorSampleMetricNorm => Finite Function Set Unspecified");

		int iNumFunction = _aNormedRxToNormedR1.length;

		double dblOperatorSampleMetricNorm = _aNormedRxToNormedR1[0].sampleMetricNorm (gvvi);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorSampleMetricNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorSampleMetricNorm => Cannot compute Sample Metric Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblSampleMetricNorm = _aNormedRxToNormedR1[i].sampleMetricNorm (gvvi);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblSampleMetricNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::operatorSampleMetricNorm => Cannot compute Sample Metric Norm for Function #"
						+ i);

			if (dblOperatorSampleMetricNorm > dblSampleMetricNorm)
				dblOperatorSampleMetricNorm = dblSampleMetricNorm;
		}

		return dblOperatorSampleMetricNorm;
	}

	/**
	 * Compute the Operator Sample Supremum Norm
	 * 
	 * @param gvvi The Validated Vector Space Instance
	 * 
	 * @return The Operator Sample Supremum Norm
	 * 
	 * @throws java.lang.Exception Thrown if the Operator Sample Supremum Norm cannot be computed
	 */

	public double operatorSampleSupremumNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
		throws java.lang.Exception
	{
		if (null == _aNormedRxToNormedR1)
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorSampleSupremumNorm => Finite Function Set Unspecified");

		int iNumFunction = _aNormedRxToNormedR1.length;

		double dblOperatorSampleSupremumNorm = _aNormedRxToNormedR1[0].sampleSupremumNorm (gvvi);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorSampleSupremumNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Finite::operatorSampleSupremumNorm => Cannot compute Sample Supremum Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblSampleSupremumNorm = _aNormedRxToNormedR1[i].sampleSupremumNorm (gvvi);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblSampleSupremumNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Finite::operatorSampleSupremumNorm => Cannot compute Sample Supremum Norm for Function #"
						+ i);

			if (dblOperatorSampleSupremumNorm > dblSampleSupremumNorm)
				dblOperatorSampleSupremumNorm = dblSampleSupremumNorm;
		}

		return dblOperatorSampleSupremumNorm;
	}
}

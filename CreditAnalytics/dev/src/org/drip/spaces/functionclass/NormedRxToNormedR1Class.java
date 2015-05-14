
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
 * NormedRxToNormedR1Class implements the Class F with f E f : Normed R^x -> Normed R^x Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedRxToNormedR1Class {
	private org.drip.spaces.RxToR1.NormedRxToNormedR1[] _aNormedRxToNormedR1Space = null;

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
			throw new java.lang.Exception ("NormedRxToNormedR1Class::DyadicEntropyNumber => Invalid Inputs");

		return 1. + (dblLogNEntropyNumber / java.lang.Math.log (2.));
	}

	protected NormedRxToNormedR1Class (
		final org.drip.spaces.RxToR1.NormedRxToNormedR1[] aNormedRxToNormedR1Space)
		throws java.lang.Exception
	{
		if (null == (_aNormedRxToNormedR1Space = aNormedRxToNormedR1Space))
			throw new java.lang.Exception ("NormedRxToNormedR1Class ctr: Invalid Inputs");

		int iClassSize = _aNormedRxToNormedR1Space.length;

		if (0 == iClassSize) throw new java.lang.Exception ("NormedRxToNormedR1Class ctr: Invalid Inputs");

		for (int i = 0; i < iClassSize; ++i) {
			if (null == _aNormedRxToNormedR1Space[i])
				throw new java.lang.Exception ("NormedRxToNormedR1Class ctr: Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Agnostic Covering Number Upper/Lower Bounds for the Function Class
	 * 
	 * @return The Agnostic Covering Number Upper/Lower Bounds for the Function Class
	 */

	public abstract org.drip.spaces.cover.CoveringNumberBounds agnosticCoveringNumberBounds();

	/**
	 * Retrieve the Scale-Sensitive Covering Number Upper/Lower Bounds given the Specified Sample for the
	 *  Function Class
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param r1r1FatShatter The Cover Fat Shattering Coefficient R^1 -> R^1
	 * 
	 * @return The Scale-Sensitive Covering Number Upper/Lower Bounds given the Specified Sample for the
	 *  Function Class
	 */

	public abstract org.drip.spaces.cover.CoveringNumberBounds scaleSensitiveCoveringBounds (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final org.drip.function.deterministic.R1ToR1 r1r1FatShatter);

	/**
	 * Estimate for the Function Class Covering Number
	 * 
	 * @param dblCover The Size of the Cover
	 * 
	 * @return Function Class Covering Number Estimate
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double coveringNumber (
		final double dblCover)
		throws java.lang.Exception
	{
		int iFunctionSpaceSize = _aNormedRxToNormedR1Space.length;

		double dblCoveringNumber = _aNormedRxToNormedR1Space[0].populationCoveringNumber (dblCover);

		for (int i = 1; i < iFunctionSpaceSize; ++i) {
			double dblFunctionCoveringNumber = _aNormedRxToNormedR1Space[i].populationCoveringNumber (dblCover);

			if (dblCoveringNumber < dblFunctionCoveringNumber) dblCoveringNumber = dblFunctionCoveringNumber;
		}

		return dblCoveringNumber;
	}

	/**
	 * Estimate for the Scale-Sensitive Covering Number for the specified Cover Size
	 * 
	 * @param gvvi The Validated Instance Vector Sequence
	 * @param dblCover The Size of the Cover
	 * 
	 * @return The Scale-Sensitive Covering Number for the specified Cover Size
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double uniformCoveringNumber (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi,
		final double dblCover)
		throws java.lang.Exception
	{
		int iFunctionSpaceSize = _aNormedRxToNormedR1Space.length;

		double dblCoveringNumber = _aNormedRxToNormedR1Space[0].sampleCoveringNumber (gvvi, dblCover);

		for (int i = 1; i < iFunctionSpaceSize; ++i) {
			double dblFunctionCoveringNumber = _aNormedRxToNormedR1Space[i].sampleCoveringNumber (gvvi, dblCover);

			if (dblCoveringNumber < dblFunctionCoveringNumber) dblCoveringNumber = dblFunctionCoveringNumber;
		}

		return dblCoveringNumber;
	}

	/**
	 * Retrieve the Array of Function Spaces in the Class
	 * 
	 * @return The Array of Function Spaces in the Class
	 */

	public org.drip.spaces.RxToR1.NormedRxToNormedR1[] functionSpaces()
	{
		return _aNormedRxToNormedR1Space;
	}

	/**
	 * Retrieve the Input Vector Space
	 * 
	 * @return The Input Vector Space
	 */

	public org.drip.spaces.metric.GeneralizedMetricVectorSpace input()
	{
		return _aNormedRxToNormedR1Space[0].input();
	}

	/**
	 * Retrieve the Output Vector Space
	 * 
	 * @return The Output Vector Space
	 */

	public org.drip.spaces.metric.RealUnidimensionalNormedSpace output()
	{
		return _aNormedRxToNormedR1Space[0].output();
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
		int iNumFunction = _aNormedRxToNormedR1Space.length;

		double dblOperatorPopulationMetricNorm = _aNormedRxToNormedR1Space[0].populationMetricNorm();

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorPopulationMetricNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Class::operatorPopulationMetricNorm => Cannot compute Population Metric Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblPopulationMetricNorm = _aNormedRxToNormedR1Space[i].populationMetricNorm();

			if (!org.drip.quant.common.NumberUtil.IsValid (dblPopulationMetricNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Class::operatorPopulationMetricNorm => Cannot compute Population Metric Norm for Function #"
						+ i);

			if (dblOperatorPopulationMetricNorm > dblPopulationMetricNorm)
				dblOperatorPopulationMetricNorm = dblPopulationMetricNorm;
		}

		return dblOperatorPopulationMetricNorm;
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
		int iNumFunction = _aNormedRxToNormedR1Space.length;

		double dblOperatorSampleMetricNorm = _aNormedRxToNormedR1Space[0].sampleMetricNorm (gvvi);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorSampleMetricNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Class::operatorSampleMetricNorm => Cannot compute Sample Metric Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblSampleMetricNorm = _aNormedRxToNormedR1Space[i].sampleMetricNorm (gvvi);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblSampleMetricNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Class::operatorSampleMetricNorm => Cannot compute Sample Metric Norm for Function #"
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

	public double operatorSupremumNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
		throws java.lang.Exception
	{
		int iNumFunction = _aNormedRxToNormedR1Space.length;

		double dblOperatorSampleSupremumNorm = _aNormedRxToNormedR1Space[0].sampleSupremumNorm (gvvi);

		if (!org.drip.quant.common.NumberUtil.IsValid (dblOperatorSampleSupremumNorm))
			throw new java.lang.Exception
				("NormedRxToNormedR1Class::operatorSampleMetricNorm => Cannot compute Sample Supremum Norm for Function #"
					+ 0);

		for (int i = 1; i < iNumFunction; ++i) {
			double dblSampleSupremumNorm = _aNormedRxToNormedR1Space[i].sampleSupremumNorm (gvvi);

			if (!org.drip.quant.common.NumberUtil.IsValid (dblSampleSupremumNorm))
				throw new java.lang.Exception
					("NormedRxToNormedR1Class::operatorSampleMetricNorm => Cannot compute Sample Supremum Norm for Function #"
						+ i);

			if (dblOperatorSampleSupremumNorm > dblSampleSupremumNorm)
				dblOperatorSampleSupremumNorm = dblSampleSupremumNorm;
		}

		return dblOperatorSampleSupremumNorm;
	}
}

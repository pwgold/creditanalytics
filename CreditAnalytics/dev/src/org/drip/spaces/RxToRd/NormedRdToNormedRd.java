
package org.drip.spaces.RxToRd;

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
 * NormedRdToNormedRd is the abstract class underlying the f : Normed, Validated R^d -> Normed, Validated R^d
 *  Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedRdToNormedRd extends org.drip.spaces.RxToRd.NormedRxToNormedRd {
	private org.drip.function.definition.RdToRd _funcRdToRd = null;
	private org.drip.spaces.metric.RealMultidimensionalNormedSpace _rmnsInput = null;
	private org.drip.spaces.metric.RealMultidimensionalNormedSpace _rmnsOutput = null;

	protected NormedRdToNormedRd (
		final org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsInput,
		final org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsOutput,
		final org.drip.function.definition.RdToRd funcRdToRd)
		throws java.lang.Exception
	{
		if (null == (_rmnsInput = rmnsInput) || null == (_rmnsOutput = rmnsOutput))
			throw new java.lang.Exception ("NormedRdToNormedRd ctr: Invalid Inputs");

		_funcRdToRd = funcRdToRd;
	}

	/**
	 * Retrieve the Underlying RdToRd Function
	 * 
	 * @return The Underlying RdToRd Function
	 */

	public org.drip.function.definition.RdToRd function()
	{
		return _funcRdToRd;
	}

	/**
	 * Retrieve the Population R^d ESS (Essential Spectrum) Array
	 * 
	 * @return The Population R^d ESS (Essential Spectrum) Array
	 */

	public double[] populationRdESS()
	{
		return _funcRdToRd.evaluate (_rmnsInput.populationMode());
	}

	@Override public org.drip.spaces.metric.RealMultidimensionalNormedSpace input()
	{
		return _rmnsInput;
	}

	@Override public org.drip.spaces.metric.RealMultidimensionalNormedSpace output()
	{
		return _rmnsOutput;
	}

	@Override public double[] sampleSupremumNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
	{
		if (null == _funcRdToRd || null == gvvi || !gvvi.tensorSpaceType().match (_rmnsInput) || ! (gvvi
			instanceof org.drip.spaces.instance.ValidatedRealMultidimensional))
			return null;

		org.drip.spaces.instance.ValidatedRealMultidimensional vrmInstance =
			(org.drip.spaces.instance.ValidatedRealMultidimensional) gvvi;

		double[][] aadblInstance = vrmInstance.instance();

		int iNumSample = aadblInstance.length;

		int iOutputDimension = _rmnsOutput.dimension();

		double[] adblSupremumNorm = _funcRdToRd.evaluate (aadblInstance[0]);

		if (null == adblSupremumNorm || iOutputDimension != adblSupremumNorm.length ||
			!org.drip.quant.common.NumberUtil.IsValid (adblSupremumNorm))
			return null;

		for (int i = 0; i < iOutputDimension; ++i)
			adblSupremumNorm[i] = java.lang.Math.abs (adblSupremumNorm[i]);

		for (int i = 1; i < iNumSample; ++i) {
			double[] adblSampleNorm = _funcRdToRd.evaluate (aadblInstance[i]);

			if (null == adblSampleNorm || iOutputDimension != adblSampleNorm.length) return null;

			for (int j = 0; j < iOutputDimension; ++j) {
				if (!org.drip.quant.common.NumberUtil.IsValid (adblSampleNorm[j])) return null;

				if (adblSampleNorm[j] > adblSupremumNorm[j]) adblSupremumNorm[j] = adblSampleNorm[j];
			}
		}

		return adblSupremumNorm;
	}

	@Override public double[] sampleMetricNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
	{
		if (null == _funcRdToRd || null == gvvi || !gvvi.tensorSpaceType().match (_rmnsInput) || ! (gvvi
			instanceof org.drip.spaces.instance.ValidatedRealMultidimensional))
			return null;

		org.drip.spaces.instance.ValidatedRealMultidimensional vrmInstance =
			(org.drip.spaces.instance.ValidatedRealMultidimensional) gvvi;

		double[][] aadblInstance = vrmInstance.instance();

		int iOutputDimension = _rmnsOutput.dimension();

		double[] adblMetricNorm = new double[iOutputDimension];
		int iNumSample = aadblInstance.length;

		int iPNorm = output().pNorm();

		for (int i = 0; i < iNumSample; ++i)
			adblMetricNorm[i] = 0.;

		for (int i = 0; i < iNumSample; ++i) {
			double[] adblPointValue = _funcRdToRd.evaluate (aadblInstance[i]);

			if (null == adblPointValue || iOutputDimension != adblPointValue.length) return null;

			for (int j = 0; j < iOutputDimension; ++j) {
				if (!org.drip.quant.common.NumberUtil.IsValid (adblPointValue[j])) return null;

				adblMetricNorm[j] += java.lang.Math.pow (java.lang.Math.abs (adblPointValue[j]), iPNorm);
			}
		}

		for (int i = 0; i < iNumSample; ++i)
			adblMetricNorm[i] = java.lang.Math.pow (adblMetricNorm[i], 1. / iPNorm);

		return adblMetricNorm;
	}

	@Override public double[] populationESS()
	{
		return _funcRdToRd.evaluate (_rmnsInput.populationMode());
	}
}

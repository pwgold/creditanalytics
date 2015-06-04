
package org.drip.spaces.RxToR1;

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
 * NormedRdToNormedR1 is the abstract class underlying the f : Validated Normed R^d -> Validated Normed R^1
 *  Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedRdToNormedR1 extends org.drip.spaces.RxToR1.NormedRxToNormedR1 {
	private org.drip.function.definition.RdToR1 _funcRdToR1 = null;
	private org.drip.spaces.metric.RealUnidimensionalNormedSpace _runsOutput = null;
	private org.drip.spaces.metric.RealMultidimensionalNormedSpace _rmnsInput = null;

	protected NormedRdToNormedR1 (
		final org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsInput,
		final org.drip.spaces.metric.RealUnidimensionalNormedSpace runsOutput,
		final org.drip.function.definition.RdToR1 funcRdToR1)
		throws java.lang.Exception
	{
		if (null == (_rmnsInput = rmnsInput) || null == (_runsOutput = runsOutput))
			throw new java.lang.Exception ("NormedRdToNormedR1 ctr: Invalid Inputs");

		_funcRdToR1 = funcRdToR1;
	}

	/**
	 * Retrieve the Underlying RdToR1 Function
	 * 
	 * @return The Underlying RdToR1 Function
	 */

	public org.drip.function.definition.RdToR1 function()
	{
		return _funcRdToR1;
	}

	@Override public double sampleSupremumNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
		throws java.lang.Exception
	{
		if (null == _funcRdToR1 || null == gvvi || !gvvi.tensorSpaceType().match (_rmnsInput))
			throw new java.lang.Exception ("NormedRdToNormedR1::sampleSupremumNorm => Invalid Input");

		double[][] aadblInstance = ((org.drip.spaces.instance.ValidatedRealMultidimensional)
			gvvi).instance();

		int iNumSample = aadblInstance.length;

		double dblSupremumNorm = java.lang.Math.abs (_funcRdToR1.evaluate (aadblInstance[0]));

		for (int i = 1; i < iNumSample; ++i) {
			double dblResponse = java.lang.Math.abs (_funcRdToR1.evaluate (aadblInstance[i]));

			if (dblResponse > dblSupremumNorm) dblSupremumNorm = dblResponse;
		}

		return dblSupremumNorm;
	}

	@Override public double sampleMetricNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvvi)
		throws java.lang.Exception
	{
		if (null == _funcRdToR1 || null == gvvi || !gvvi.tensorSpaceType().match (_rmnsInput))
			throw new java.lang.Exception ("NormedRdToNormedR1::sampleMetricNorm => Invalid Input");

		double[][] aadblInstance = ((org.drip.spaces.instance.ValidatedRealMultidimensional)
			gvvi).instance();

		int iNumSample = aadblInstance.length;
		double dblNorm = 0.;

		int iPNorm = _runsOutput.pNorm();

		for (int i = 0; i < iNumSample; ++i)
			dblNorm += java.lang.Math.pow (java.lang.Math.abs (_funcRdToR1.evaluate (aadblInstance[i])),
				iPNorm);

		return java.lang.Math.pow (dblNorm, 1. / iPNorm);
	}

	@Override public double populationESS()
		throws java.lang.Exception
	{
		if (null == _funcRdToR1)
			throw new java.lang.Exception ("NormedRdToNormedR1::populationESS => Invalid Input");

		return _funcRdToR1.evaluate (_rmnsInput.populationMode());
	}

	@Override public org.drip.spaces.metric.RealUnidimensionalNormedSpace output()
	{
		return _runsOutput;
	}

	@Override public org.drip.spaces.metric.RealMultidimensionalNormedSpace input()
	{
		return _rmnsInput;
	}
}

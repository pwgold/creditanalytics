
package org.drip.learning.kernel;

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
 * MercerKernel exposes the Functionality behind the Mercer Kernel, i.e., the Kernel that is Normed R^x X
 *  Normed R^x -> Supremum R^1, that is, a Kernel that symmetric in the Input Metric Space in terms of both
 *  the Metric and the Dimensionality.
 *  
 *  The References are:
 *  
 *  1) Ash, R. (1965): Information Theory, Inter-science New York.
 *  
 *  2) Konig, H. (1986): Eigenvalue Distribution of Compact Operators, Birkhauser, Basel, Switzerland. 
 *  
 *  3) Smola, A. J., A. Elisseff, B. Scholkopf, and R. C. Williamson (2000): Entropy Numbers for Convex
 *  	Combinations and mlps, in: Advances in Large Margin Classifiers, A. Smola, P. Bartlett, B. Scholkopf,
 *  	and D. Schuurmans - editors, MIT Press, Cambridge, MA.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class MercerKernel {
	private org.drip.spaces.metric.RealUnidimensionalNormedSpace _runsOutput = null;
	private org.drip.spaces.metric.RealMultidimensionalNormedSpace _rmnsInput = null;

	/**
	 * MercerKernel Constructor
	 * 
	 * @param rmnsInput The Symmetric Input R^x Space
	 * @param runsOutput The Output R^1 Metric Space
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public MercerKernel (
		final org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsInput,
		final org.drip.spaces.metric.RealUnidimensionalNormedSpace runsOutput)
		throws java.lang.Exception
	{
		if (null == (_rmnsInput = rmnsInput) || 2 != _rmnsInput.pNorm() || null == (_runsOutput = runsOutput)
			|| 0 != _runsOutput.pNorm())
			throw new java.lang.Exception ("MercerKernel ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Symmetric Input Metric R^x Space
	 * 
	 * @return The Symmetric Input Metric R^x Space
	 */

	public org.drip.spaces.metric.RealMultidimensionalNormedSpace input()
	{
		return _rmnsInput;
	}

	/**
	 * Retrieve the Output R^1 Metric Space
	 * 
	 * @return The Output R^1 Metric Space
	 */

	public org.drip.spaces.metric.RealUnidimensionalNormedSpace output()
	{
		return _runsOutput;
	}

	/**
	 * Compute the Kernel's R^x X R^x -> R^1 Value
	 * 
	 * @param gvviX Validated Vector Instance X
	 * @param gvviY Validated Vector Instance Y
	 * 
	 * @return The Kernel's R^x X R^x -> R^1 Value
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public abstract double evaluate (
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviX,
		final org.drip.spaces.instance.GeneralizedValidatedVectorInstance gvviY)
		throws java.lang.Exception;
}

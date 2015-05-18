
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
 * OperatorEigenComponent holds the Eigen-vector Functions/Spaces and the Eigenvalue Functions/Spaces of the
 *  R^x L2 -> R^x L2 Kernel Integral Operator defined by:
 * 
 * 		T_k [f(.)] := Integral Over Input Space {k (., y) * f(y) * d[Prob(y)]}
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

public class OperatorEigenComponent {
	private org.drip.spaces.RxToR1.NormedRdToNormedR1 _eigenValueFunctionSpace = null;
	private org.drip.spaces.RxToR1.NormedRdToNormedR1 _eigenVectorFunctionSpace = null;

	/**
	 * OperatorEigenComponent Constructor
	 * 
	 * @param eigenVectorFunctionSpace Normed R^d -> Normed L2 R^1 Eigen-vector Function Space
	 * @param eigenValueFunctionSpace Normed R^d -> Normed L1 R^1 Eigenvalue Function Space
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public OperatorEigenComponent (
		final org.drip.spaces.RxToR1.NormedRdToNormedR1 eigenVectorFunctionSpace,
		final org.drip.spaces.RxToR1.NormedRdToNormedR1 eigenValueFunctionSpace)
		throws java.lang.Exception
	{
		if (null == (_eigenVectorFunctionSpace = eigenVectorFunctionSpace) || 2 !=
			_eigenVectorFunctionSpace.output().pNorm() || null == (_eigenValueFunctionSpace =
				eigenValueFunctionSpace) || 1 != _eigenValueFunctionSpace.output().pNorm())
				throw new java.lang.Exception ("OperatorEigenComponent ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Eigen-Vector Function Space
	 * 
	 * @return The Eigen-Vector Function Space
	 */

	public org.drip.spaces.RxToR1.NormedRdToNormedR1 eigenVectorFunctionSpace()
	{
		return _eigenVectorFunctionSpace;
	}

	/**
	 * Retrieve the Eigenvalue Function Space
	 * 
	 * @return The Eigenvalue Function Space
	 */

	public org.drip.spaces.RxToR1.NormedRdToNormedR1 eigenValueFunctionSpace()
	{
		return _eigenValueFunctionSpace;
	}
}

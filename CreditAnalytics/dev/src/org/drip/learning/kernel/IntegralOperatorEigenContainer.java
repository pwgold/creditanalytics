
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
 * IntegralOperatorEigenContainer holds the Group of Eigen-Components that result from the Eigenization of
 *  the R^x L2 -> R^x L2 Kernel Linear Integral Operator defined by:
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

public class IntegralOperatorEigenContainer {
	private org.drip.learning.kernel.IntegralOperatorEigenComponent[] _aIOEC = null;

	/**
	 * IntegralOperatorEigenContainer Constructor
	 * 
	 * @param aIOEC Array of the Integral Operator Eigen-Components
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public IntegralOperatorEigenContainer (
		final org.drip.learning.kernel.IntegralOperatorEigenComponent[] aIOEC)
		throws java.lang.Exception
	{
		if (null == (_aIOEC = aIOEC) || 0 == _aIOEC.length)
			throw new java.lang.Exception ("IntegralOperatorEigenContainer ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Array of the Integral Operator Eigen-Components
	 * 
	 * @return The Array of the Integral Operator Eigen-Components
	 */

	public org.drip.learning.kernel.IntegralOperatorEigenComponent[] eigenComponents()
	{
		return _aIOEC;
	}

	/**
	 * Retrieve the Eigen Input Space
	 * 
	 * @return The Eigen Input Space
	 */

	public org.drip.spaces.metric.RealMultidimensionalNormedSpace input()
	{
		return _aIOEC[0].eigenFunction().input();
	}

	/**
	 * Retrieve the Eigen Output Space
	 * 
	 * @return The Eigen Output Space
	 */

	public org.drip.spaces.metric.RealUnidimensionalNormedSpace output()
	{
		return _aIOEC[0].eigenFunction().output();
	}

	/**
	 * Compute the Array of RKHS Feature Space Bounds on Application of the Diagonal Scaling Operator
	 * 
	 * @param adblDiagonalScalingOperator The Diagonal Scaling Operator Array
	 * 
	 * @return Array of RKHS Feature Space Bounds on Application of the Diagonal Scaling Operator
	 */

	public double[] diagonalScalingBounds (
		final double[] adblDiagonalScalingOperator)
	{
		if (null == adblDiagonalScalingOperator) return null;

		int iDimension = adblDiagonalScalingOperator.length;
		double[] adblDiagonalScalingBound = new double[iDimension];

		if (iDimension != _aIOEC.length) return null;

		for (int i = 0;i < iDimension; ++i) {
			if (!org.drip.quant.common.NumberUtil.IsValid (adblDiagonalScalingOperator[i])) return null;

			adblDiagonalScalingBound[i] = 0.5 * adblDiagonalScalingOperator[i] *
				_aIOEC[i].rkhsFeatureParallelepipedLength();
		}

		return adblDiagonalScalingBound;
	}
}

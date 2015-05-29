
package org.drip.learning.regularization;

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
 * RegularizerBuilder constructs Custom Regularizers for the different Normed Learner Function Types.
 *  
 * The References are:
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
 *  5) Vapnik, V. N. (1998): Statistical Learning Theory, Wiley, New York.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RegularizerBuilder {

	/**
	 * Construct an Instance of R^1 Combinatorial -> R^1 Continuous Regularizer
	 * 
	 * @param funcRegularizerR1ToR1 The R^1 -> R^1 Regularizer Function
	 * @param funcSpaceR1ToR1 The R^1 Combinatorial -> R^1 Continuous Learner Function Space
	 * @param dblLambda The Regularization Lambda
	 * 
	 * @return The R^1 Combinatorial -> R^1 Continuous Regularizer Instance
	 */

	public static final org.drip.learning.regularization.RegularizerR1CombinatorialToR1Continuous
		R1CombinatorialToR1Continuous (
			final org.drip.function.deterministic.R1ToR1 funcRegularizerR1ToR1,
			final org.drip.spaces.RxToR1.NormedR1CombinatorialToR1Continuous funcSpaceR1ToR1,
			final double dblLambda)
	{
		try {
			return null == funcSpaceR1ToR1 ? null : new
				org.drip.learning.regularization.RegularizerR1CombinatorialToR1Continuous
					(funcRegularizerR1ToR1, (org.drip.spaces.metric.CombinatorialRealUnidimensional)
						funcSpaceR1ToR1.input(), (org.drip.spaces.metric.ContinuousRealUnidimensional)
							funcSpaceR1ToR1.output(), dblLambda);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct an Instance of R^1 Continuous -> R^1 Continuous Regularizer
	 * 
	 * @param funcRegularizerR1ToR1 The R^1 -> R^1 Regularizer Function
	 * @param funcSpaceR1ToR1 The R^1 Continuous -> R^1 Continuous Learner Function Space
	 * @param dblLambda The Regularization Lambda
	 * 
	 * @return The R^1 Continuous -> R^1 Continuous Regularizer Instance
	 */

	public static final org.drip.learning.regularization.RegularizerR1ContinuousToR1Continuous
		R1ContinuousToR1Continuous (
			final org.drip.function.deterministic.R1ToR1 funcRegularizerR1ToR1,
			final org.drip.spaces.RxToR1.NormedR1ContinuousToR1Continuous funcSpaceR1ToR1,
			final double dblLambda)
	{
		try {
			return null == funcSpaceR1ToR1 ? null : new
				org.drip.learning.regularization.RegularizerR1ContinuousToR1Continuous
					(funcRegularizerR1ToR1, (org.drip.spaces.metric.ContinuousRealUnidimensional)
						funcSpaceR1ToR1.input(), (org.drip.spaces.metric.ContinuousRealUnidimensional)
							funcSpaceR1ToR1.output(), dblLambda);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct an Instance of R^d Combinatorial -> R^1 Continuous Regularizer
	 * 
	 * @param funcRegularizerRdToR1 The R^d -> R^1 Regularizer Function
	 * @param funcSpaceRdToR1 The R^d Combinatorial -> R^1 Continuous Learner Function Space
	 * @param dblLambda The Regularization Lambda
	 * 
	 * @return The R^d Combinatorial -> R^1 Continuous Regularizer Instance
	 */

	public static final org.drip.learning.regularization.RegularizerRdCombinatorialToR1Continuous
		RdCombinatorialToR1Continuous (
			final org.drip.function.deterministic.RdToR1 funcRegularizerRdToR1,
			final org.drip.spaces.RxToR1.NormedRdCombinatorialToR1Continuous funcSpaceRdToR1,
			final double dblLambda)
	{
		try {
			return null == funcSpaceRdToR1 ? null : new
				org.drip.learning.regularization.RegularizerRdCombinatorialToR1Continuous
					(funcRegularizerRdToR1, (org.drip.spaces.metric.CombinatorialRealMultidimensionalBanach)
						funcSpaceRdToR1.input(), (org.drip.spaces.metric.ContinuousRealUnidimensional)
							funcSpaceRdToR1.output(), dblLambda);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct an Instance of R^d Continuous -> R^1 Continuous Regularizer
	 * 
	 * @param funcRegularizerRdToR1 The R^d -> R^1 Regularizer Function
	 * @param funcSpaceRdToR1 The R^d Continuous -> R^1 Continuous Learner Function Space
	 * @param dblLambda The Regularization Lambda
	 * 
	 * @return The R^d Continuous -> R^1 Continuous Regularizer Instance
	 */

	public static final org.drip.learning.regularization.RegularizerRdContinuousToR1Continuous
		RdContinuousToR1Continuous (
			final org.drip.function.deterministic.RdToR1 funcRegularizerRdToR1,
			final org.drip.spaces.RxToR1.NormedRdContinuousToR1Continuous funcSpaceRdToR1,
			final double dblLambda)
	{
		try {
			return null == funcSpaceRdToR1 ? null : new
				org.drip.learning.regularization.RegularizerRdContinuousToR1Continuous
					(funcRegularizerRdToR1, (org.drip.spaces.metric.ContinuousRealMultidimensionalBanach)
						funcSpaceRdToR1.input(), (org.drip.spaces.metric.ContinuousRealUnidimensional)
							funcSpaceRdToR1.output(), dblLambda);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}

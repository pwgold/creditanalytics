
package org.drip.learning.svm;

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
 * SupportMachine exposes the Basic SVM Functionality for Classification and Regression.
 * 
 * The References are:
 * 
 * 	1) Vapnik, V., and A. Chervonenkis (1974): Theory of Pattern Recognition (in Russian), Nauka, Moscow
 * 		USSR.
 * 
 * 	2) Vapnik, V. (1995): The Nature of Statistical Learning, Springer-Verlag, New York.
 * 
 * 	3) Shawe-Taylor, J., P. L. Bartlett, R. C. Williamson, and M. Anthony (1996): A Framework for Structural
 * 		Risk Minimization, in: Proceedings of the 9th Annual Conference on Computational Learning Theory, ACM
 * 		New York 68-76.
 *
 * @author Lakshmi Krishnamurthy
 */

public interface SupportVectorMachine {

	/**
	 * Retrieve the Input Predictor Metric Vector Space
	 * 
	 * @return The Input Predictor Metric Vector Space
	 */

	public abstract org.drip.spaces.tensor.GeneralizedMultidimensionalVectorSpace predictorSpace();

	/**
	 * Classify the Specified Multi-dimensional Point
	 * 
	 * @param adblX The Multi-dimensional Input Point
	 * 
	 * @return +1/-1 Boolean Space Output Equivalents
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public abstract short classify (
		final double[] adblX)
		throws java.lang.Exception;

	/**
	 * Regress on the Specified Multi-dimensional Point
	 * 
	 * @param adblX The Multi-dimensional Input Point
	 * 
	 * @return The Regression Output
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public abstract double regress (
		final double[] adblX)
		throws java.lang.Exception;
}


package org.drip.math.function;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.credit-trader.org
 * 
 * CreditAnalytics is a free, full featured, fixed income credit analytics library, developed with a special
 * 		focus towards the needs of the bonds and credit products community.
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
 * BernsteinPolynomial provides the evaluation of the BernsteinPolynomial and its derivatives for a specified
 * 	variate.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BernsteinPolynomial extends org.drip.math.function.UnivariateConvolution {

	/**
	 * Construct a BernsteinPolynomial instance
	 * 
	 * @param iBaseExponent Base Exponent
	 * @param iComplementExponent Complement Exponent
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public BernsteinPolynomial (
		final int iBaseExponent,
		final int iComplementExponent)
		throws java.lang.Exception
	{
		super (new org.drip.math.function.NaturalLogSeriesElement (iBaseExponent), new
			org.drip.math.function.UnivariateReflection (new org.drip.math.function.NaturalLogSeriesElement
				(iComplementExponent)));
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		BernsteinPolynomial bp = new BernsteinPolynomial (3, 3);

		System.out.println ("BPDeriv[0.25] = " + bp.calcDerivative (0.25, 1));
	}
}

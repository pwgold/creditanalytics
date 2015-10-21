
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
 * UnivariateReflection provides the evaluation f(1-x) instead of f(x) for a given f.
 *
 * @author Lakshmi Krishnamurthy
 */

public class UnivariateReflection extends org.drip.math.function.AbstractUnivariate {
	private org.drip.math.function.AbstractUnivariate _au = null;

	/**
	 * UnivariateReflection constructor
	 * 
	 * @param au Univariate Function
	 * 
	 * @throws java.lang.Exception Thrown if the input is invalid
	 */

	public UnivariateReflection (
		final org.drip.math.function.AbstractUnivariate au)
		throws java.lang.Exception
	{
		super (null);

		if (null == (_au = au)) throw new java.lang.Exception ("UnivariateReflection ctr: Invalid Inputs");
	}

	@Override public double evaluate (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("UnivariateReflection::evaluate => Invalid Inputs");

		return _au.evaluate (1. - dblVariate);
	}

	@Override public double calcDerivative (
		final double dblVariate,
		final int iOrder)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblVariate) || 0 >= iOrder)
			throw new java.lang.Exception ("UnivariateReflection::calcDerivative => Invalid Inputs");

		return java.lang.Math.pow (-1., iOrder) * _au.calcDerivative (1. - dblVariate, iOrder);
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		UnivariateReflection ur = new UnivariateReflection (new Polynomial (4));

		System.out.println ("UnivariateReflection[0.0] = " + ur.evaluate (0.0));

		System.out.println ("UnivariateReflection[0.5] = " + ur.evaluate (0.5));

		System.out.println ("UnivariateReflection[1.0] = " + ur.evaluate (1.0));

		System.out.println ("UnivariateReflectionDeriv[0.0] = " + ur.calcDerivative (0.0, 3));

		System.out.println ("UnivariateReflectionDeriv[0.5] = " + ur.calcDerivative (0.5, 3));

		System.out.println ("UnivariateReflectionDeriv[1.0] = " + ur.calcDerivative (1.0, 3));
	}
}

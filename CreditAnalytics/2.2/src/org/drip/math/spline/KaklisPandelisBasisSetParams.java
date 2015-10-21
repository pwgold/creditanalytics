
package org.drip.math.spline;

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
 * KaklisPandelisBasisSetParams implements per-segment parameters for the Kaklis Pandelis basis set -
 *  currently it only holds the polynomial tension degree.
 *
 * @author Lakshmi Krishnamurthy
 */

public class KaklisPandelisBasisSetParams implements org.drip.math.spline.BasisSetParams {
	private int _iPolynomialTensionDegree = -1;

	/**
	 * KaklisPantelisBasisSetParams constructor
	 * 
	 * @param iPolynomialTensionDegree Segment Polynomial Tension Degree
	 * 
	 * @throws java.lang.Exception
	 */

	public KaklisPandelisBasisSetParams (
		final int iPolynomialTensionDegree)
		throws java.lang.Exception
	{
		if (0 >= (_iPolynomialTensionDegree = iPolynomialTensionDegree))
			throw new java.lang.Exception ("KaklisPantelisBasisSetParams ctr: Invalid Inputs");
	}

	/**
	 * Get the Segment Polynomial Tension Degree
	 * 
	 * @return The Segment Polynomial Tension Degree
	 */

	public int getPolynomialTensionDegree()
	{
		return _iPolynomialTensionDegree;
	}
}

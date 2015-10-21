
package org.drip.math.linearalgebra;

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
 * This class holds the results of Matrix transforms on the source and the complement, e.g., during a Matrix
 *  Inversion Operation.
 *
 * @author Lakshmi Krishnamurthy
 */

public class MatrixComplementTransform {
	private double[][] _aadblSource = null;
	private double[][] _aadblComplement = null;

	/**
	 * MatrixComplementTransform constructor
	 * 
	 * @param aadblSource Transformed Source Matrix
	 * @param aadblComplement Transformed Complement Matrix
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public MatrixComplementTransform (
		final double[][] aadblSource,
		final double[][] aadblComplement)
		throws java.lang.Exception
	{
		if (null == (_aadblSource = aadblSource) || null == (_aadblComplement = aadblComplement))
			throw new java.lang.Exception ("MatrixComplementTransform ctr: Invalid Inputs");

		int iSize = _aadblSource.length;

		if (0 == iSize || iSize != _aadblSource.length || iSize != _aadblSource[0].length || iSize !=
			_aadblComplement.length || iSize != _aadblComplement[0].length)
			throw new java.lang.Exception ("MatrixComplementTransform ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Transformed Source
	 * 
	 * @return The Transformed Source
	 */

	public double[][] getSource()
	{
		return _aadblSource;
	}

	/**
	 * Retrieve the Transformed Complement
	 * 
	 * @return The Transformed Complement
	 */

	public double[][] getComplement()
	{
		return _aadblComplement;
	}

	/**
	 * Retrieve the Dimension Length
	 * 
	 * @return The Dimension Length
	 */

	public int size()
	{
		return _aadblComplement.length;
	}
}

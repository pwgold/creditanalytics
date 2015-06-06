
package org.drip.spaces.tensor;

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
 * CombinatorialIteratorRd contains the Functionality to iterate through an R^d Combinatorial Space.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CombinatorialIteratorRd extends org.drip.analytics.support.MultidimensionalInterator {
	private org.drip.spaces.tensor.CombinatorialVectorR1[] _aCVR1 = null;

	/**
	 * Retrieve the CombinatorialIteratorRd Instance associated with the Underlying Vector Space
	 * 
	 * @param aCVR1 Array of the Combinatorial R^1 Vectors
	 * 
	 * @return The CombinatorialIteratorRd Instance associated with the Underlying Vector Space
	 */

	public static final CombinatorialIteratorRd Standard (
		final org.drip.spaces.tensor.CombinatorialVectorR1[] aCVR1)
	{
		if (null == aCVR1) return null;

		int iDimension = aCVR1.length;
		int[] aiMax = new int[iDimension];

		if (0 == iDimension) return null;

		for (int i = 0; i < iDimension; ++i)
			aiMax[i] = (int) aCVR1[i].cardinality().number();

		try {
			return new CombinatorialIteratorRd (aCVR1, aiMax);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * CombinatorialIteratorRd Constructor
	 * 
	 * @param aCVR1 Array of the Combinatorial R^1 Vectors
	 * @param aiMax The Array of Dimension Maximum
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public CombinatorialIteratorRd (
		final org.drip.spaces.tensor.CombinatorialVectorR1[] aCVR1,
		final int[] aiMax)
		throws java.lang.Exception
	{
		super (aiMax, false);

		if (null == (_aCVR1 = aCVR1) || _aCVR1.length != aiMax.length)
			throw new java.lang.Exception ("CombinatorialIteratorRd ctr: Invalid Inputs");
	}

	/**
	 * Retrieve the Array of the Combinatorial R^1 Vectors
	 * 
	 * @return The Array of the Combinatorial R^1 Vectors
	 */

	public org.drip.spaces.tensor.CombinatorialVectorR1[] cvR1()
	{
		return _aCVR1;
	}

	/**
	 * Convert the Vector Space Index Array to the Variate Array
	 * 
	 * @param aiIndex Vector Space Index Array
	 * 
	 * @return Variate Array
	 */

	public double[] vectorSpaceIndexToVariate (
		final int[] aiIndex)
	{
		if (null == aiIndex) return null;

		org.drip.spaces.tensor.CombinatorialVectorR1[] _aCVR1 = cvR1();

		int iDimension = _aCVR1.length;
		double[] adblVariate = new double[iDimension];

		if (iDimension != aiIndex.length) return null;

		for (int i = 0; i < iDimension; ++i)
			adblVariate[i] = _aCVR1[i].elementSpace().get (aiIndex[i]);

		return adblVariate;
	}

	/**
	 * Retrieve the Cursor Variate Array
	 * 
	 * @return The Cursor Variate Array
	 */

	public double[] cursorVariates()
	{
		return vectorSpaceIndexToVariate (cursor());
	}

	/**
	 * Retrieve the Subsequent Variate Array
	 * 
	 * @return The Subsequent Variate Array
	 */

	public double[] nextVariates()
	{
		return vectorSpaceIndexToVariate (next());
	}
}

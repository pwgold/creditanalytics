
package org.drip.spaces.functionclass;

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
 * NormedR1ToNormedR1Class implements the Class F of f : Normed R^1 -> Normed R^1 Function Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedR1ToNormedR1Class extends org.drip.spaces.functionclass.NormedRxToNormedR1Class {

	/**
	 * R1ToR1Class Function Class Constructor
	 * 
	 * @param aR1ToR1FunctionSpace Array of the Function Spaces
	 * 
	 * @throws java.lang.Exception Thrown if R1ToR1Class Instance cannot be created
	 */

	public NormedR1ToNormedR1Class (
		final org.drip.spaces.RxToR1.NormedR1ToNormedR1[] aR1ToR1FunctionSpace)
		throws java.lang.Exception
	{
		super (aR1ToR1FunctionSpace);

		for (int i = 0; i < aR1ToR1FunctionSpace.length; ++i) {
			if (null == aR1ToR1FunctionSpace[i])
				throw new java.lang.Exception ("NormedR1ToNormedR1Class ctr: Invalid Input Function");
		}
	}

	/**
	 * Retrieve the Class of R^1 -> R^1 Functions
	 * 
	 * @return The Class of R^1 -> R^1 Functions
	 */

	public org.drip.function.deterministic.R1ToR1[] functionR1ToR1Set()
	{
		org.drip.spaces.RxToR1.NormedR1ToNormedR1[] aNormedR1ToNormedR1 =
			(org.drip.spaces.RxToR1.NormedR1ToNormedR1[]) functionSpaces();

		if (null == aNormedR1ToNormedR1) return null;

		int iNumFunction = aNormedR1ToNormedR1.length;
		org.drip.function.deterministic.R1ToR1[] aR1ToR1 = new
			org.drip.function.deterministic.R1ToR1[iNumFunction];

		for (int i = 0; i < iNumFunction; ++i)
			aR1ToR1[i] = aNormedR1ToNormedR1[i].function();

		return aR1ToR1;
	}
}

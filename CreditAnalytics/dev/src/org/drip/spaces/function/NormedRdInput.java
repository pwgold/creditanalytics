
package org.drip.spaces.function;

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
 * NormedRdInput is the abstract class underlying the Normed Function Space f that takes as Input Validated
 *  R^d Spaces.
 * 
 * The Reference we've used is:
 * 
 * 	- Carl, B., and I. Stephani (1990): Entropy, Compactness, and Approximation of Operators, Cambridge
 * 		University Press, Cambridge UK.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedRdInput implements org.drip.spaces.function.GeneralizedNormedFunctionSpace {
	private org.drip.spaces.metric.RealMultidimensionalNormedSpace _rmnsInput = null;

	protected NormedRdInput (
		final org.drip.spaces.metric.RealMultidimensionalNormedSpace rmnsInput)
		throws java.lang.Exception
	{
		if (null == (_rmnsInput = rmnsInput))
			throw new java.lang.Exception ("NormedRdInput ctr: Invalid Inputs");
	}

	@Override public org.drip.spaces.metric.RealMultidimensionalNormedSpace input()
	{
		return _rmnsInput;
	}
}

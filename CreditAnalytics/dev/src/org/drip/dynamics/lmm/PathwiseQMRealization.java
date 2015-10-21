
package org.drip.dynamics.lmm;

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
 * PathwiseQMRealization contains the Sequence of the Simulated Target Point State QM Realizations and their
 *  corresponding Date Nodes. The formulations for the case of the Forward Rates are in:
 * 
 *  1) Goldys, B., M. Musiela, and D. Sondermann (1994): Log-normality of Rates and Term Structure Models,
 *  	The University of New South Wales.
 * 
 *  2) Musiela, M. (1994): Nominal Annual Rates and Log-normal Volatility Structure, The University of New
 *   	South Wales.
 * 
 * 	3) Brace, A., D. Gatarek, and M. Musiela (1997): The Market Model of Interest Rate Dynamics, Mathematical
 * 		Finance 7 (2), 127-155.
 *
 * @author Lakshmi Krishnamurthy
 */

public class PathwiseQMRealization {
	private double[] _adblTargetDateNode = null;
	private double[] _adblPointStateQMRealization = null;

	/**
	 * PathwiseQMRealization Constructor
	 * 
	 * @param adblTargetDateNode Array of Target Date Nodes
	 * @param adblPointStateQMRealization Array of the Realized QM
	 * 
	 * @throws java.lang.Exception Thrown if Inputs are Invalid
	 */

	public PathwiseQMRealization (
		final double[] adblTargetDateNode,
		final double[] adblPointStateQMRealization)
		throws java.lang.Exception
	{
		_adblTargetDateNode = adblTargetDateNode;
		_adblPointStateQMRealization = adblPointStateQMRealization;
	}

	/**
	 * Retrieve the Array of the Target Date Nodes
	 * 
	 * @return Array of the Target Date Nodes
	 */

	public double[] targetDate()
	{
		return _adblTargetDateNode;
	}

	/**
	 * Retrieve the Array of the Realized QM
	 * 
	 * @return Array of the Realized QM
	 */

	public double[] realizedQM()
	{
		return _adblPointStateQMRealization;
	}
}

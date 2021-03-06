
package org.drip.product.creator;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
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
 * BondBasketBuilder contains the suite of helper functions for creating the bond Basket Product from different
 *  kinds of inputs and byte streams.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondBasketBuilder {

	/**
	 * BondBasket constructor
	 * 
	 * @param strName BondBasket Name
	 * @param aBond Component bonds
	 * @param adblWeights Component Bond weights
	 * @param dtEffective Effective date
	 * @param dblNotional Basket Notional
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static final org.drip.product.definition.BasketProduct CreateBondBasket (
		final java.lang.String strName,
		final org.drip.product.definition.Bond[] aBond,
		final double[] adblWeights,
		final org.drip.analytics.date.JulianDate dtEffective,
		final double dblNotional)
	{
		try {
			return new org.drip.product.credit.BondBasket (strName, aBond, adblWeights, dtEffective,
				dblNotional);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create a BondBasket Instance from the byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @return BondBasket Instance
	 */

	public static final org.drip.product.definition.BasketProduct FromByteArray (
		final byte[] ab)
	{
		if (null == ab || 0 == ab.length) return null;

		try {
			return new org.drip.product.credit.BondBasket (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}


package org.drip.sample.misc;

import org.drip.service.api.CreditAnalytics;
import org.drip.service.env.CacheManager;

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
 * CacheManagerAPI demonstrates Cache Manager API Functionality.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CacheManagerAPI {

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		CreditAnalytics.Init ("");

		String strObj = "TESTOBJECT";
		long lCacheEntryTimeout = 10L; // Cache Entry Time-out in Seconds

		CacheManager.Put (
			strObj,
			strObj,
			lCacheEntryTimeout
		);

		while (true) {
			System.out.println (
				"\t[" + new java.util.Date() + "] " + strObj + " exists: " +
				CacheManager.Contains (strObj)
			);

			java.lang.Thread.sleep (1000);
		}
	}
}

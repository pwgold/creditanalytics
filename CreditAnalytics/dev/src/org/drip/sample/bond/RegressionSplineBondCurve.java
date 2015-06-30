
package org.drip.sample.bond;

import java.util.*;

import org.drip.analytics.cashflow.CompositePeriod;
import org.drip.analytics.date.*;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.support.AnalyticsHelper;
import org.drip.product.creator.BondBuilder;
import org.drip.product.definition.Bond;
import org.drip.service.api.CreditAnalytics;

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
 * RegressionSplineBondCurve demonstrates the Functionality behind the Regression Spline based OLS best-fit
 *  Construction of a Bond Discount Curve Based on Input Price/Yield.
 *
 * @author Lakshmi Krishnamurthy
 */

public class RegressionSplineBondCurve {

	private static final Bond FixedCouponBond (
		final String strName,
		final JulianDate dtEffective,
		final JulianDate dtMaturity,
		final double dblCoupon,
		final String strCurrency,
		final String strDayCount,
		final int iFreq)
		throws Exception
	{
		return BondBuilder.CreateSimpleFixed (
			strName,
			strCurrency,
			"",
			dblCoupon,
			iFreq,
			strDayCount,
			dtEffective,
			dtMaturity,
			null,
			null
		);
	}

	private static final Map<JulianDate, Double> BondYieldFlows (
		final Bond bond,
		final double dblYield,
		final double dblValueDate)
		throws Exception
	{
		Map<JulianDate, Double> mapDateYield = new TreeMap<JulianDate, Double>();

		for (CompositePeriod cp : bond.couponPeriods()) {
			if (cp.payDate() <= dblValueDate) continue;

			mapDateYield.put (
				new JulianDate (cp.payDate()),
				AnalyticsHelper.Yield2DF (
					bond.freq(),
					dblYield,
					Convention.YearFraction (
						dblValueDate,
						cp.payDate(),
						bond.couponDC(),
						false,
						null,
						bond.currency()
					)
				)
			);
		}

		return mapDateYield;
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		CreditAnalytics.Init ("");

		String strCurrency = "USD";
		String strDayCount = "30/360";

		JulianDate dtSpot = DateUtil.CreateFromYMD (
			2015,
			DateUtil.JUNE,
			30
		);

		Bond bond = FixedCouponBond (
			"BOND1",
			DateUtil.CreateFromYMD (
				2010,
				DateUtil.JANUARY,
				1
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.JANUARY,
				1
			),
			0.05,
			strCurrency,
			strDayCount,
			2
		);

		Map<JulianDate, Double> mapDateDF = BondYieldFlows (
			bond,
			0.06,
			dtSpot.julian()
		);

		for (Map.Entry<JulianDate, Double> me : mapDateDF.entrySet())
			System.out.println ("\t " + me.getKey() + " => " + me.getValue());
	}
}

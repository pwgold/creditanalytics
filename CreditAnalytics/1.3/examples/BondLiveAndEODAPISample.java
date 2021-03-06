
package org.drip.service.sample;

import java.util.*;

import org.drip.service.api.FI;
import org.drip.analytics.curve.*;
import org.drip.param.valuation.*;
import org.drip.util.common.FIGen;
import org.drip.analytics.period.*;
import org.drip.product.credit.Bond;
import org.drip.util.date.JulianDate;
import org.drip.param.pricer.PricerParams;
import org.drip.analytics.daycount.DayCountBasis;
import org.drip.param.market.ComponentMarketParams;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.CreditAnalytics.org
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
 * Comprehensive sample class demo'ing the usage of the EOD and Live Curve FI API functions
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondLiveAndEODAPISample {
	private static final String FIELD_SEPARATOR = "    ";

	/**
	 * Sample demonstrating the calculation of the bond's EOD yield measures from price
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BondEODMeasuresAPISample() throws Exception {
		String strISIN = "008686AA5"; // Amortizer

		/* 
		 * Sample for EOD measure in, measure out, as in <<MeasureOut>>From<<MeasureIn>>, where MeasureIn
		 * 		is one of
		 * 			- Price
		 * 			- TSY Spread
		 * 			- Yield
		 * 		and measure in is one of
		 * 			- Convexity
		 * 			- Credit Basis
		 * 			- Duration
		 * 			- G Spread
		 * 			- I Spread
		 * 			- OAS
		 * 			- Par Asset Swap Spread
		 * 			- Price
		 * 			- TSY Spread
		 * 			- Yield
		 * 			- ZSpread
		 * 
		 * Please see the FI javadoc for the API details.
		 */

		System.out.println ("EOD Yield From Price: " + FIGen.FormatPrice
			(FI.BondEODYieldFromPrice (strISIN, JulianDate.CreateFromYMD (2011, 12, 16), 1.)));
	}

	/**
	 * Sample demonstrating the calculation of the bond's full EOD measures from price, TSY spread, or yield
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BondEODSample() {
		JulianDate dtEOD = JulianDate.CreateFromYMD (2012, 1, 13);

		String strISIN = "US78490FUS63"; // Short dated floater 9/15/2012
		// String strISIN = "US78442GGV23"; // Long dated floater
		// String strISIN = "US44180Y2046"; // Plain old fixed coupon

		Map<String, Double> mapPriceMeasures = FI.BondEODMeasuresFromPrice (strISIN, dtEOD, 1.);

		System.out.println ("\n--------------\nPrice Measures\n--------------");

		for (Map.Entry<String, Double> me : mapPriceMeasures.entrySet())
			System.out.println (me.getKey() + "=" + me.getValue());

		Map<String, Double> mapTSYSpreadMeasures = FI.BondEODMeasuresFromTSYSpread (strISIN, dtEOD, 0.0486);

		System.out.println ("\n---------------\nSpread Measures\n---------------");

		for (Map.Entry<String, Double> me : mapTSYSpreadMeasures.entrySet())
			System.out.println (me.getKey() + "=" + me.getValue());

		Map<String, Double> mapYieldMeasures = FI.BondEODMeasuresFromYield (strISIN, dtEOD, 0.0749);

		System.out.println ("\n--------------\nYield Measures\n--------------");

		for (Map.Entry<String, Double> me : mapYieldMeasures.entrySet())
			System.out.println (me.getKey() + "=" + me.getValue());
	}

	/**
	 * Sample demonstrating the calculation of analytics for the set of bonds associated with the ticker
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BondTickerAPISample() throws Exception {
		int iNumBonds = 0;
		String strTicker = "SLMA";

		JulianDate dtToday = JulianDate.Today();

		DiscountCurve dc = DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.05);

		DiscountCurve dcTSY = DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.04);

		CreditCurve cc = CreditCurve.FromFlatHazard (dtToday.getJulian(), "CC", 0.02, 0.4);

		ComponentMarketParams cmp = ComponentMarketParams.MakeCreditCMP (dc, cc);

		List<String> lsstrISIN = FI.GetISINsForTicker (strTicker);

		System.out.println ("Dumping: ISIN, FLOAT/FIXED, Bond, Yield, Z Spread, Opt Adj Spread, TSY Spread, Credit Basis, Credit Price");

		System.out.println ("---------------------------------------------------------------------------------------------------------");

		for (String strISIN : lsstrISIN) {
			Bond bond = FI.GetBond (strISIN);

			if (null != bond && !bond.hasVariableCoupon() && !bond.hasBeenExercised() && !bond.hasDefaulted()
				&& bond.getMaturityDate().getJulian() > dtToday.getJulian()) {
				double dblZSpreadFromPrice = Double.NaN;
				double dblOASpreadFromPrice = Double.NaN;

				double dblYieldFromPrice = FI.BondYieldFromPrice (strISIN, dtToday, dc, 1.);

				if (!FI.IsBondFloater (strISIN)) {
					dblZSpreadFromPrice = FI.BondZSpreadFromPrice (strISIN, dtToday, dc, 1.);

					dblOASpreadFromPrice = FI.BondOASFromPrice (strISIN, dtToday, dc, 1.);
				}

				double dblTSYSpreadFromPrice = FI.BondTSYSpreadFromPrice (strISIN, dtToday, dc, dcTSY, 1.);

				double dblCreditBasisFromPrice = FI.BondCreditBasisFromPrice (strISIN, dtToday, dc, cc, 1.);

				double dblBondCreditPrice = FI.BondCreditPrice (strISIN, dtToday, dc, cc);

				++iNumBonds;

				System.out.println (strISIN + FIELD_SEPARATOR +
					(bond.isFloater() ? "FLOAT   " : "FIXED   ") + bond.getTicker() + FIELD_SEPARATOR +
					FIGen.FormatPrice (bond.getCoupon (dtToday.getJulian(), cmp, null)) + FIELD_SEPARATOR +
					bond.getMaturityDate() + FIELD_SEPARATOR +
					FIGen.FormatPrice (dblYieldFromPrice) + FIELD_SEPARATOR +
					FIGen.FormatSpread (dblZSpreadFromPrice) + FIELD_SEPARATOR +
					FIGen.FormatSpread (dblOASpreadFromPrice) + FIELD_SEPARATOR +
					FIGen.FormatSpread (dblTSYSpreadFromPrice) + FIELD_SEPARATOR +
					FIGen.FormatSpread (dblCreditBasisFromPrice) + FIELD_SEPARATOR +
					FIGen.FormatPrice (dblBondCreditPrice)
				);
			}
		}

		System.out.println ("Processed " + iNumBonds + " " + strTicker + " bonds!");

		for (String strISIN : lsstrISIN) {
			Bond bond = FI.GetBond (strISIN);

			System.out.println (
				strISIN + FIELD_SEPARATOR + bond.getTicker() + FIELD_SEPARATOR +
				FIGen.FormatPrice (bond.getCoupon (JulianDate.Today().getJulian(), null, null)) + FIELD_SEPARATOR +
				bond.getMaturityDate() + FIELD_SEPARATOR +
				FIGen.FormatPrice (FI.GetBondDoubleField (strISIN, "OutstandingAmount"), 10, 0, 1.)
			);
		}

		/*
		 * Calculate the bucketed outstanding notional for all the bonds of this ticker
		 */

		JulianDate[] adtAscending = new JulianDate[5];

		adtAscending[0] = dtToday.addYears (3);

		adtAscending[1] = dtToday.addYears (5);

		adtAscending[2] = dtToday.addYears (10);

		adtAscending[3] = dtToday.addYears (30);

		adtAscending[4] = dtToday.addYears (60);

		Map<JulianDate, Double> mapOutstandingNotional = FI.GetIssuerAggregateOutstandingNotional (dtToday, strTicker, adtAscending);

		for (Map.Entry<JulianDate, Double> me : mapOutstandingNotional.entrySet())
			System.out.println ("[" + JulianDate.Today() + "=>" + me.getKey() + "] = " + me.getValue());
	}

	/**
	 * Sample demonstrating the usage of the (full set of) bond analytics API. Also shows the usage of the
	 * 		bond loss and coupon flow functionality
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	public static final void BondAPISample() throws Exception {
		Set<String> setstrTickers = FI.GetAvailableTickers();

		for (String strTicker : setstrTickers)
			System.out.println (strTicker);

		List<String> lsstrISIN = FI.GetISINsForTicker ("DB");

		for (String strBondISIN : lsstrISIN)
			System.out.println (strBondISIN);

		String strISIN = "US78490FUS63"; // Short-dated floater maturing at 9/15/2012
		// String strISIN = "US760677FD19"; // Amortizer
		double dblZTMFromPrice = Double.NaN;
		double dblOASTMFromPrice = Double.NaN;
		double dblZSpreadFromPrice = Double.NaN;
		double dblOASpreadFromPrice = Double.NaN;
		double dblZSpreadFromTSYSpread = Double.NaN;
		double dblOASpreadFromTSYSpread = Double.NaN;

		QuotingParams quotingParams = new QuotingParams ("30/360", 2, true, null, "USD", false);

		Bond bond = FI.GetBond (strISIN);

		JulianDate dtToday = JulianDate.Today();

		DiscountCurve dc = DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.03);

		DiscountCurve dcTSY = DiscountCurve.CreateFromFlatRate (dtToday, "USD", 0.04);

		CreditCurve cc = CreditCurve.FromFlatHazard (dtToday.getJulian(), "CC", 0.02, 0.);

		ValuationParams valParams = ValuationParams.CreateValParams (dtToday, 0, "", DayCountBasis.DR_ACTUAL);

		PricerParams pricerParams = PricerParams.MakeStdPricerParams();

		ComponentMarketParams cmp = ComponentMarketParams.MakeCreditCMP (dc, cc);

		WorkoutInfo wi = FI.BondWorkoutInfoFromPrice (strISIN, dtToday, dc, 1.);

		double dblYieldFromPrice = FI.BondYieldFromPrice (strISIN, dtToday, dc, 1.);

		double dblYTMFromPrice = FI.BondYTMFromPrice (strISIN, valParams, dc, 1., quotingParams);

		if (!FI.IsBondFloater (strISIN)) {
			dblZSpreadFromPrice = FI.BondZSpreadFromPrice (strISIN, dtToday, dc, 1.);

			dblZTMFromPrice = FI.BondZTMFromPrice (strISIN, valParams, dc, 1., quotingParams);

			dblOASpreadFromPrice = FI.BondOASFromPrice (strISIN, dtToday, dc, 1.);

			dblOASTMFromPrice = FI.BondZTMFromPrice (strISIN, valParams, dc, 1., quotingParams);
		}

		double dblISpreadFromPrice = FI.BondISpreadFromPrice (strISIN, dtToday, dc, 1.);

		double dblITMFromPrice = FI.BondITMFromPrice (strISIN, valParams, dc, 1., quotingParams);

		double dblDiscountMarginFromPrice = FI.BondDiscountMarginFromPrice (strISIN, dtToday, dc, 1.);

		double dblDiscountMarginTMFromPrice = FI.BondDiscountMarginTMFromPrice (strISIN, valParams, dc, 1., quotingParams);

		double dblTSYSpreadFromPrice = FI.BondTSYSpreadFromPrice (strISIN, dtToday, dc, dcTSY, 1.);

		double dblTSYTMFromPrice = FI.BondTSYTMFromPrice (strISIN, valParams, dc, dcTSY, 1., quotingParams);

		double dblGSpreadFromPrice = FI.BondGSpreadFromPrice (strISIN, dtToday, dc, dcTSY, 1.);

		double dblGTMFromPrice = FI.BondGTMFromPrice (strISIN, valParams, dc, dcTSY, 1., quotingParams);

		double dblParASWFromPrice = FI.BondParASWFromPrice (strISIN, dtToday, dc, 1.);

		double dblParASWTMFromPrice = FI.BondParASWTMFromPrice (strISIN, valParams, dc, 1., null);

		double dblCreditBasisFromPrice = FI.BondCreditBasisFromPrice (strISIN, dtToday, dc, cc, 1.);

		double dblCreditBasisTMFromPrice = FI.BondCreditBasisTMFromPrice (strISIN, valParams, dc, cc, 1., quotingParams);

		double dblPriceFromTSYSpread = FI.BondPriceFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);

		double dblYieldFromTSYSpread = FI.BondYieldFromTSYSpread (strISIN, dtToday, dcTSY, 0.0271);

		if (!FI.IsBondFloater (strISIN)) {
			dblZSpreadFromTSYSpread = FI.BondZSpreadFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);

			dblOASpreadFromTSYSpread = FI.BondOASFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);
		}

		double dblISpreadFromTSYSpread = FI.BondISpreadFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);

		double dblDiscountMarginFromTSYSpread = FI.BondDiscountMarginFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);

		double dblGSpreadFromTSYSpread = FI.BondGSpreadFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);

		double dblParASWFromTSYSpread = FI.BondParASWFromTSYSpread (strISIN, dtToday, dc, dcTSY, 0.0271);

		double dblCreditBasisFromTSYSpread = FI.BondCreditBasisFromTSYSpread (strISIN, dtToday, dc, dcTSY, cc, 0.0271);

		double dblBondCreditPrice = FI.BondCreditPrice (strISIN, valParams, dc, cc, quotingParams);

		JulianDate dtPreviousCoupon = FI.PreviousCouponDate (strISIN, dtToday);

		JulianDate dtCurrentCoupon = FI.NextCouponDate (strISIN, dtToday);

		JulianDate dtNextCoupon = FI.NextCouponDate (strISIN, dtToday);

		JulianDate dtEffective = FI.EffectiveDate (strISIN);

		JulianDate dtMaturity = FI.MaturityDate (strISIN);

		boolean bInFirstPeriod = FI.InFirstPeriod (strISIN, dtToday.getJulian());

		boolean bInLastPeriod = FI.InLastPeriod (strISIN, dtToday.getJulian());

		NextExerciseInfo nei = FI.NextExerciseInfo (strISIN, dtToday);

		System.out.println (strISIN + "    " + bond.getTicker() + " " + FIGen.FormatPrice (bond.getCoupon
			(valParams._dblValue, cmp, null)) + " " + bond.getMaturityDate());

		System.out.println ("Work-out date From Price: " + new JulianDate (wi._dblDate));

		System.out.println ("Work-out factor From Price: " + wi._dblExerciseFactor);

		System.out.println ("Work-out Yield From Price: " + FIGen.FormatPrice (wi._dblYield));

		System.out.println ("Work-out Type for Price: " + org.drip.util.internal.FIUtil.WorkoutTypeToString (wi._iWOType));

		System.out.println ("Yield From Price: " + FIGen.FormatPrice (dblYieldFromPrice) + " / " + FIGen.FormatPrice (dblYTMFromPrice));

		System.out.println ("Z Spread From Price: " + (int) (10000. * dblZSpreadFromPrice) + " / " + (int) (10000. * dblZTMFromPrice));

		System.out.println ("Option Adj Spread From Price: " + (int) (10000. * dblOASpreadFromPrice) + " / " + (int) (10000. * dblOASTMFromPrice));

		System.out.println ("I Spread From Price: " + (int) (10000. * dblISpreadFromPrice) + " / " + (int) (10000. * dblITMFromPrice));

		System.out.println ("Discount Margin From Price: " + (int) (10000. * dblDiscountMarginFromPrice) + " / " + (int) (10000. * dblDiscountMarginTMFromPrice));

		System.out.println ("TSY Spread From Price: " + (int) (10000. * dblTSYSpreadFromPrice) + " / " + (int) (10000. * dblTSYTMFromPrice));

		System.out.println ("G Spread From Price: " + (int) (10000. * dblGSpreadFromPrice) + " / " + (int) (10000. * dblGTMFromPrice));

		System.out.println ("Par ASW From Price: " + (int) dblParASWFromPrice + " / " + (int) dblParASWTMFromPrice);

		System.out.println ("Credit Basis From Price: " + (int) (10000. * dblCreditBasisFromPrice) + " / " + (int) (10000. * dblCreditBasisTMFromPrice));

		System.out.println ("Price From TSY Spread: " + FIGen.FormatPrice (dblPriceFromTSYSpread));

		System.out.println ("Yield From TSY Spread: " + FIGen.FormatPrice (dblYieldFromTSYSpread));

		System.out.println ("Z Spread From TSY Spread: " + (int) (10000. * dblZSpreadFromTSYSpread));

		System.out.println ("OAS From TSY Spread: " + (int) (10000. * dblOASpreadFromTSYSpread));

		System.out.println ("I Spread From TSY Spread: " + (int) (10000. * dblISpreadFromTSYSpread));

		System.out.println ("Discount Margin From TSY Spread: " + (int) (10000. * dblDiscountMarginFromTSYSpread));

		System.out.println ("G Spread From TSY Spread: " + (int) (10000. * dblGSpreadFromTSYSpread));

		System.out.println ("Par ASW From TSY Spread: " + (int) dblParASWFromTSYSpread);

		System.out.println ("Credit Basis From TSY Spread: " + (int) (10000. * dblCreditBasisFromTSYSpread));

		System.out.println ("Credit Risky Price: " + FIGen.FormatPrice (dblBondCreditPrice));

		System.out.println ("Valuation Date: " + JulianDate.Today());

		System.out.println ("Effective Date: " + dtEffective);

		System.out.println ("Maturity Date: " + dtMaturity);

		System.out.println ("Is Val Date in the first period? " + bInFirstPeriod);

		System.out.println ("Is Val Date in the last period? " + bInLastPeriod);

		System.out.println ("Previous Coupon Date: " + dtPreviousCoupon);

		System.out.println ("Current Coupon Date: " + dtCurrentCoupon);

		System.out.println ("Next Coupon Date: " + dtNextCoupon);

		System.out.println ("Next Exercise Date: " + new JulianDate (nei._dblDate));

		System.out.println ("Next Exercise Factor: " + nei._dblExerciseFactor);

		System.out.println ("Next Exercise Type: " + org.drip.util.internal.FIUtil.WorkoutTypeToString (nei._iWOType));

		if (bond.isFloater()) {
			System.out.println ("Acc Start       Acc End     Pay Date    Index   Spread   Cpn DCF    Pay01    Surv01");

			System.out.println ("---------      ---------    ---------   ------  ------   -------- --------- --------");

			for (ProductCouponPeriodCurveMeasures p : bond.getCouponFlow (valParams, pricerParams, cmp))
				System.out.println (
					JulianDate.fromJulian (p.getAccrualStartDate()) + FIELD_SEPARATOR +
					JulianDate.fromJulian (p.getAccrualEndDate()) + FIELD_SEPARATOR +
					JulianDate.fromJulian (p.getPayDate()) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (p.getIndexRate(), 1, 4, 1.) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (p.getSpread(), 1, 4, 1.) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (p.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (dc.getDF (p.getPayDate()), 1, 4, 1.) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (cc.getSurvival (p.getPayDate()), 1, 4, 1.)
				);
		} else {
			System.out.println ("Acc Start       Acc End     Pay Date   Cpn DCF    Pay01    Surv01");

			System.out.println ("---------      ---------    ---------  -------- --------- --------");

			for (ProductCouponPeriodCurveMeasures p : bond.getCouponFlow (valParams, pricerParams, cmp))
				System.out.println (
					JulianDate.fromJulian (p.getAccrualStartDate()) + FIELD_SEPARATOR +
					JulianDate.fromJulian (p.getAccrualEndDate()) + FIELD_SEPARATOR +
					JulianDate.fromJulian (p.getPayDate()) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (p.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (dc.getDF (p.getPayDate()), 1, 4, 1.) + FIELD_SEPARATOR +
					FIGen.FormatSpreadSimple (cc.getSurvival (p.getPayDate()), 1, 4, 1.)
				);
		}

		System.out.println ("Loss Start     Loss End      Pay Date      Cpn    Notl     Rec    EffDF    StartSurv  EndSurv");

		System.out.println ("----------     --------      --------      ---    ----     ---    -----    ---------  -------");

		for (ProductLossPeriodCurveMeasures dp : bond.getLossFlow (valParams, pricerParams, cmp))
			System.out.println (
				JulianDate.fromJulian (dp.getStartDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (dp.getEndDate()) + FIELD_SEPARATOR +
				JulianDate.fromJulian (dp.getPayDate()) + FIELD_SEPARATOR +
				FIGen.FormatSpreadSimple (dp.getCouponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
				FIGen.FormatSpreadSimple (dp.getEffectiveNotional(), 1, 0, 1.) + FIELD_SEPARATOR +
				FIGen.FormatSpreadSimple (dp.getEffectiveRecovery(), 1, 2, 1.) + FIELD_SEPARATOR +
				FIGen.FormatSpreadSimple (dp.getEffectiveDF(), 1, 4, 1.)  + FIELD_SEPARATOR +
				FIGen.FormatSpreadSimple (dp.getStartSurvival(), 1, 4, 1.) + FIELD_SEPARATOR +
				FIGen.FormatSpreadSimple (dp.getEndSurvival(), 1, 4, 1.)
			);
	}

	public static final void main (final String astrArgs[]) throws Exception {
		// String strConfig = "c:\\Lakshmi\\java\\BondAnal\\Config.xml";

		String strConfig = "";

		if (!FI.Init (strConfig)) {
			System.out.println ("Cannot fully init FI!");

			System.exit (303);
		}

		BondEODMeasuresAPISample();

		BondEODSample();

		BondAPISample();
	}
}

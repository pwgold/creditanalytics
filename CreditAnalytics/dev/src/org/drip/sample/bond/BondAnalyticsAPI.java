
package org.drip.sample.bond;

/*
 * Credit Product imports
 */

import org.drip.analytics.cashflow.*;
import org.drip.analytics.date.*;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.definition.*;
import org.drip.analytics.rates.DiscountCurve;
import org.drip.analytics.support.*;
import org.drip.market.otc.*;
import org.drip.param.definition.*;
import org.drip.param.market.CurveSurfaceQuoteSet;
import org.drip.param.pricer.CreditPricerParams;
import org.drip.param.valuation.*;
import org.drip.product.params.*;
import org.drip.product.rates.*;
import org.drip.product.definition.*;
import org.drip.param.creator.*;
import org.drip.product.creator.*;
import org.drip.quant.common.*;
import org.drip.service.api.CreditAnalytics;
import org.drip.state.creator.*;
import org.drip.state.identifier.ForwardLabel;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
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
 * BondAnalyticsAPI contains a demo of the bond analytics API Sample. It generates the value and the RV
 * 	measures for essentially the same bond (with identical cash flows) constructed in 3 different ways:
 * 	- As a fixed rate bond.
 * 	- As a floater.
 * 	- As a bond constructed from a set of custom coupon and principal flows.
 * 
 * It shows these measures reconcile where they should.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BondAnalyticsAPI {
	private static final String FIELD_SEPARATOR = "    ";

	private static final FixFloatComponent OTCIRS (
		final JulianDate dtSpot,
		final String strCurrency,
		final String strMaturityTenor,
		final double dblCoupon)
	{
		FixedFloatSwapConvention ffConv = IBORFixedFloatContainer.ConventionFromJurisdiction (
			strCurrency,
			"ALL",
			strMaturityTenor,
			"MAIN"
		);

		return ffConv.createFixFloatComponent (
			dtSpot,
			strMaturityTenor,
			dblCoupon,
			0.,
			1.
		);
	}

	/*
	 * Sample demonstrating building of rates curve from cash/future/swaps
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static DiscountCurve BuildRatesCurveFromInstruments (
		final JulianDate dtStart,
		final String[] astrCashTenor,
		final double[] adblCashRate,
		final String[] astrIRSTenor,
		final double[] adblIRSRate,
		final double dblBump,
		final String strCurrency)
		throws Exception
	{
		int iNumDCInstruments = astrCashTenor.length + adblIRSRate.length;
		double adblDate[] = new double[iNumDCInstruments];
		double adblRate[] = new double[iNumDCInstruments];
		String astrCalibMeasure[] = new String[iNumDCInstruments];
		double adblCompCalibValue[] = new double[iNumDCInstruments];
		CalibratableFixedIncomeComponent aCompCalib[] = new CalibratableFixedIncomeComponent[iNumDCInstruments];

		// Cash Calibration

		JulianDate dtCashEffective = dtStart.addBusDays (1, strCurrency);

		for (int i = 0; i < astrCashTenor.length; ++i) {
			astrCalibMeasure[i] = "Rate";
			adblRate[i] = java.lang.Double.NaN;
			adblCompCalibValue[i] = adblCashRate[i] + dblBump;

			aCompCalib[i] = SingleStreamComponentBuilder.Deposit (
				dtCashEffective,
				new JulianDate (adblDate[i] = dtCashEffective.addTenor (astrCashTenor[i]).julian()),
				ForwardLabel.Create (
					strCurrency,
					astrCashTenor[i]
				)
			);
		}

		// IRS Calibration

		JulianDate dtIRSEffective = dtStart.addBusDays (
			2,
			strCurrency
		);

		for (int i = 0; i < astrIRSTenor.length; ++i) {
			astrCalibMeasure[i + astrCashTenor.length] = "Rate";
			adblRate[i + astrCashTenor.length] = java.lang.Double.NaN;
			adblCompCalibValue[i + astrCashTenor.length] = adblIRSRate[i] + dblBump;

			adblDate[i + astrCashTenor.length] = dtIRSEffective.addTenor (astrIRSTenor[i]).julian();

			aCompCalib[i + astrCashTenor.length] = OTCIRS (
				dtIRSEffective,
				strCurrency,
				astrIRSTenor[i],
				0.
			);
		}

		/*
		 * Build the IR curve from the components, their calibration measures, and their calibration quotes.
		 */

		return ScenarioDiscountCurveBuilder.NonlinearBuild (
			dtStart,
			strCurrency,
			DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD,
			aCompCalib,
			adblCompCalibValue,
			astrCalibMeasure,
			null
		);
	}

	/*
	 * Sample demonstrating creation of discount curve
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final DiscountCurve MakeDiscountCurve (
		final JulianDate dtCurve)
		throws Exception
	{
		String[] astrCashTenor = new String[] {};
		double[] adblCashRate = new double[] {};
		String[] astrIRSTenor = new String[] {   "1Y",    "2Y",    "3Y",    "4Y",    "5Y",    "6Y",    "7Y",
			   "8Y",    "9Y",   "10Y",   "11Y",   "12Y",   "15Y",   "20Y",   "25Y",   "30Y",   "40Y",   "50Y"};
		double[] adblIRSRate = new double[]  {0.00367, 0.00533, 0.00843, 0.01238, 0.01609, 0.01926, 0.02191,
			0.02406, 0.02588, 0.02741, 0.02870, 0.02982, 0.03208, 0.03372, 0.03445, 0.03484, 0.03501, 0.03484};

		return BuildRatesCurveFromInstruments (
			dtCurve,
			astrCashTenor,
			adblCashRate,
			astrIRSTenor,
			adblIRSRate,
			0.,
			"USD"
		);
	}

	/*
	 * Sample demonstrating creation of the principal factor schedule from date and factor array
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final Array2D MakeFSPrincipal()
		throws Exception
	{
		double[] adblDate = new double[5];
		double[] adblFactor = new double[] {1., 1.0, 1.0, 1.0, 1.0};
		// double[] adblFactor = new double[] {1., 0.9, 0.8, 0.7, 0.6};

		JulianDate dtEOSStart = DateUtil.Today().addDays (2);

		for (int i = 0; i < 5; ++i)
			adblDate[i] = dtEOSStart.addYears (i + 2).julian();

		return Array2D.FromArray (
			adblDate,
			adblFactor
		);
	}

	/*
	 * Sample demonstrating creation of the coupon factor schedule from date and factor array
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final Array2D MakeFSCoupon()
		throws Exception
	{
		double[] adblDate = new double[5];
		double[] adblFactor = new double[] {1., 1.0, 1.0, 1.0, 1.0};
		// double[] adblFactor = new double[] {1., 0.9, 0.8, 0.7, 0.6};

		JulianDate dtEOSStart = DateUtil.Today().addDays (2);

		for (int i = 0; i < 5; ++i)
			adblDate[i] = dtEOSStart.addYears (i + 2).julian();

		return Array2D.FromArray (
			adblDate,
			adblFactor
		);
	}

	/*
	 * Sample creates a custom named bond from the bond type and parameters
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final Bond CreateCustomBond (
		final String strName,
		final String strCreditCurve,
		final int iBondType)
		throws Exception
	{
		BondProduct bond = null;
		boolean bEOSOn = false;
		boolean bEOSAmerican = false;

		if (BondBuilder.BOND_TYPE_SIMPLE_FLOATER == iBondType)
			bond = BondBuilder.CreateSimpleFloater ( // Simple Floating Rate Bond
				strName,		// Name
				"USD",			// Currency
				"USD-6M", // Rate Index
				strCreditCurve, // Credit Curve
				0.01,			// Floating Spread
				2,				// Coupon Frequency
				"30/360",		// Day Count
				DateUtil.CreateFromYMD (
					2008,
					9,
					21
				), // Effective
				DateUtil.CreateFromYMD (
					2023,
					9,
					20
				),	// Maturity
				MakeFSPrincipal(),		// Principal Schedule
				MakeFSCoupon()		// Coupon Schedule
			);
		else if (BondBuilder.BOND_TYPE_SIMPLE_FIXED == iBondType)
			bond = BondBuilder.CreateSimpleFixed (	// Simple Fixed Rate Bond
				strName,		// Name
				"USD",			// Currency
				strCreditCurve, // Credit Curve
				0.05,			// Bond Coupon
				2,				// Coupon Frequency
				"30/360",		// Day Count
				DateUtil.CreateFromYMD (
					2008,
					9,
					21
				), // Effective
				DateUtil.CreateFromYMD (
					2023,
					9,
					20
				),	// Maturity
				MakeFSPrincipal(),		// Principal Schedule
				MakeFSCoupon()		// Coupon Schedule
			);
		else if (BondBuilder.BOND_TYPE_SIMPLE_FROM_CF == iBondType) {	// Bond from custom coupon and principal flows
			final int NUM_CF_ENTRIES = 30;
			double[] adblCouponAmount = new double[NUM_CF_ENTRIES];
			double[] adblPrincipalAmount = new double[NUM_CF_ENTRIES];
			JulianDate[] adt = new JulianDate[NUM_CF_ENTRIES];

			JulianDate dtEffective = DateUtil.CreateFromYMD (
				2008,
				9,
				20
			);

			for (int i = 0; i < NUM_CF_ENTRIES; ++i) {
				adt[i] = dtEffective.addMonths (6 * (i + 1));

				adblCouponAmount[i] = 0.05;
				adblPrincipalAmount[i] = 1.0;
			}

			bond = BondBuilder.CreateBondFromCF (
				strName,				// Name
				dtEffective,			// Effective
				"USD",					// Currency
				strCreditCurve, 		// Credit Curve
				"30/360",				// Day Count
				1.,						// Initial Notional
				0.05,					// Coupon Rate
				2,						// Frequency
				adt,					// Array of dates
				adblCouponAmount,		// Array of coupon amount
				adblPrincipalAmount,	// Array of principal amount
				false					// Principal is an outstanding notional
			);
		}

		/*
		 * Bonds with options embedded
		 */

		if (bEOSOn) {
			double[] adblDate = new double[5];
			double[] adblPutFactor = new double[5];
			double[] adblCallFactor = new double[5];
			EmbeddedOptionSchedule eosPut = null;
			EmbeddedOptionSchedule eosCall = null;

			JulianDate dtEOSStart = DateUtil.Today().addDays (2);

			for (int i = 0; i < 5; ++i) {
				adblPutFactor[i] = 0.9;
				adblCallFactor[i] = 1.0;

				adblDate[i] = dtEOSStart.addYears (i + 2).julian();
			}

			if (bEOSAmerican) {		// Creation of the American call and put schedule
				eosCall = EmbeddedOptionSchedule.FromAmerican (
					DateUtil.Today().julian() + 1,
					adblDate,
					adblCallFactor,
					false,
					30,
					false,
					Double.NaN,
					"",
					Double.NaN
				);

				eosPut = EmbeddedOptionSchedule.FromAmerican (
					DateUtil.Today().julian(),
					adblDate,
					adblPutFactor,
					true,
					30,
					false,
					Double.NaN,
					"",
					Double.NaN
				);
			} else {		// Creation of the European call and put schedule
				eosCall = new EmbeddedOptionSchedule (
					adblDate,
					adblCallFactor,
					false,
					30,
					false,
					Double.NaN,
					"",
					Double.NaN
				);

				eosPut = new EmbeddedOptionSchedule (
					adblDate,
					adblPutFactor,
					true,
					30,
					false,
					Double.NaN,
					"",
					Double.NaN
				);
			}

			bond.setEmbeddedCallSchedule (eosCall);

			bond.setEmbeddedPutSchedule (eosPut);
		}

		return (Bond) bond;
	}

	/*
	 * Sample demonstrating the creation/usage of the custom bond API
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final void CustomBondAPISample()
		throws Exception
	{
		Bond[] aBond = new Bond[3];
		String strCreditCurve = "CC";

		/*
		 * Creates a simple fixed coupon bond and adds it to the FI cache as a named object
		 */

		if (null == (aBond[0] = CreditAnalytics.GetBond ("CustomFixed")))
			CreditAnalytics.PutBond ("CustomFixed", aBond[0] = CreateCustomBond (
				"CustomFixed",
				strCreditCurve,
				BondBuilder.BOND_TYPE_SIMPLE_FIXED)
			);

		/*
		 * Creates a simple floater and adds it to the FI cache as a named object
		 */

		if (null == (aBond[1] = CreditAnalytics.GetBond ("CustomFRN")))
			CreditAnalytics.PutBond ("CustomFRN", aBond[1] = CreateCustomBond (
				"CustomFRN",
				strCreditCurve,
				BondBuilder.BOND_TYPE_SIMPLE_FLOATER)
			);

		/*
		 * Creates a custom bond from arbitrary cash flows and adds it to the FI cache as a named object
		 */

		if (null == (aBond[2] = CreditAnalytics.GetBond ("CustomBondFromCF")))
			CreditAnalytics.PutBond ("CustomBondFromCF", aBond[2] = CreateCustomBond (
				"CustomBondFromCF",
				strCreditCurve,
				BondBuilder.BOND_TYPE_SIMPLE_FROM_CF)
			);

		/*
		 * Base Discount Curve
		 */

		DiscountCurve dc = MakeDiscountCurve (DateUtil.Today());

		/*
		 * Treasury Discount Curve
		 */

		DiscountCurve dcTSY = DiscountCurveBuilder.CreateFromFlatRate (
			DateUtil.Today(),
			"USD",
			null,
			0.03
		);

		/*
		 * Credit Curve
		 */

		CreditCurve cc = CreditCurveBuilder.FromFlatHazard (
			DateUtil.Today().julian(),
			strCreditCurve,
			"USD",
			0.01,
			0.4
		);

		for (int i = 0; i < aBond.length; ++i) {
			System.out.println ("\nBOND #" + i + "; " + aBond[i].name());

			System.out.println ("--------------------------");

			System.out.println ("--------------------------");

			System.out.println ("\n\tAcc Start     Acc End     Pay Date      Cpn DCF       Pay01       Surv01");

			System.out.println ("\t---------    ---------    ---------    ---------    ---------    --------");

			/*
			 * Generates and displays the coupon period details for the bonds
			 */

			for (CompositePeriod p : aBond[i].couponPeriods())
				System.out.println (
					DateUtil.FromJulian (p.startDate()) + FIELD_SEPARATOR +
					DateUtil.FromJulian (p.endDate()) + FIELD_SEPARATOR +
					DateUtil.FromJulian (p.payDate()) + FIELD_SEPARATOR +
					FormatUtil.FormatDouble (p.couponDCF(), 1, 4, 1.) + FIELD_SEPARATOR +
					FormatUtil.FormatDouble (dc.df (p.payDate()), 1, 4, 1.) + FIELD_SEPARATOR +
					FormatUtil.FormatDouble (cc.survival (p.payDate()), 1, 4, 1.)
				);

			/*
			 * Create the bond's component market parameters from the market inputs
			 */

			CurveSurfaceQuoteSet mktParams = MarketParamsBuilder.Create (
				dc,		// Discount curve
				dcTSY,	// TSY Discount Curve (Includes Optional EDSF if available, or BILLS etc)
				cc,		// Credit Curve
				null,	// TSY quotes
				null,	// BOND ID
				null,	// Bond market quote
				AnalyticsHelper.CreateFixingsObject (
					aBond[i],
					DateUtil.Today(),
					0.04	// Fixings
				)
			);

			/*
			 * Construct Valuation Parameters
			 */

			ValuationParams valParams = ValuationParams.Spot (
				DateUtil.Today(),
				0,
				"",
				Convention.DATE_ROLL_ACTUAL
			);

			ProductQuote cquote = QuoteBuilder.CreateProductQuote();

			Quote q = QuoteBuilder.CreateQuote (
				"mid",
				0.05,
				Double.NaN
			);

			cquote.addQuote (
				"Yield",
				q,
				true
			);

			mktParams.setProductQuote (
				aBond[i].name(),
				cquote
			);

			System.out.println ("\n\tPrice From Yield: " +
				FormatUtil.FormatDouble (
					aBond[i].priceFromYield (
						valParams,
						mktParams,
						null,
						0.03
					), 1, 3, 100.));

			double dblPrice = aBond[i].priceFromYield (
				valParams,
				mktParams,
				null,
				0.03
			);

			WorkoutInfo wi = aBond[i].exerciseYieldFromPrice (
				valParams,
				mktParams,
				null,
				dblPrice
			);

			System.out.println ("\tWorkout Date: " + DateUtil.FromJulian (wi.date()));

			System.out.println ("\tWorkout Factor: " + wi.factor());

			System.out.println ("\tWorkout Yield: " + FormatUtil.FormatDouble (wi.yield(), 1, 2, 100.));

			System.out.println (
				"\tWorkout Yield From Price: " +
				FormatUtil.FormatDouble (
					aBond[i].yieldFromPrice (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						1.
					), 1, 2, 100.));

			if (!aBond[i].isFloater()) {
				System.out.println (
					"\tZ Spread From Price: " +
					FormatUtil.FormatDouble (
						aBond[i].zspreadFromPrice (
							valParams,
							mktParams,
							null,
							wi.date(),
							wi.factor(),
							1.
						), 1, 0, 10000.));

				System.out.println (
					"\tOAS From Price: " +
						FormatUtil.FormatDouble (
						aBond[i].oasFromPrice (
							valParams,
							mktParams,
							null,
							wi.date(),
							wi.factor(),
							1.
						), 1, 0, 10000.));
			} else;
				System.out.println (
					"\tDiscount Margin From Price: " +
						FormatUtil.FormatDouble (
						aBond[i].discountMarginFromPrice (
							valParams,
							mktParams,
							null,
							wi.date(),
							wi.factor(),
							1.
						), 1, 0, 10000.));

			System.out.println (
				"\tI Spread From Price: " +
				FormatUtil.FormatDouble (
					aBond[i].iSpreadFromPrice (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						1.
					), 1, 0, 10000.));

			double dblTreasurySpread = Double.NaN;

			System.out.println (
				"\tTSY Spread From Price: " +
				FormatUtil.FormatDouble (
					dblTreasurySpread = aBond[i].tsySpreadFromPrice (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						1.
					), 1, 0, 10000.));

			System.out.println (
				"\tASW From Price: " +
				FormatUtil.FormatDouble (
					aBond[i].aswFromPrice (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						1.
					), 1, 0, 10000.));

			System.out.println (
				"\tCredit Basis From Price: " +
				FormatUtil.FormatDouble (
					aBond[i].creditBasisFromPrice (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						1.
					), 1, 0, 10000.));

			System.out.println (
				"\tPrice From TSY Spread: " +
				FormatUtil.FormatDouble (
					aBond[i].priceFromTSYSpread (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						dblTreasurySpread
					), 1, 0, 100.));

			System.out.println (
				"\tYield From TSY Spread: " +
				FormatUtil.FormatDouble (
					aBond[i].yieldFromTSYSpread (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						dblTreasurySpread
					), 1, 0, 100.));

			System.out.println (
				"\tASW From TSY Spread: " +
				FormatUtil.FormatDouble (
					aBond[i].aswFromTSYSpread (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						dblTreasurySpread
					), 1, 0, 10000.));

			System.out.println (
				"\tCredit Basis From TSY Spread: " +
				FormatUtil.FormatDouble (
					aBond[i].creditBasisFromTSYSpread (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						dblTreasurySpread
					), 1, 0, 10000.));

			/* System.out.println ("\tPECS From TSY Spread: " + FormatUtil.FormatDouble
				(aBond[i].pecsFromTSYSpread (valParams, mktParams, null, 0.0188), 1, 0, 10000.)); */

			System.out.println (
				"\tTheoretical Price: " +
				FormatUtil.FormatDouble (
					aBond[i].priceFromCreditBasis (
						valParams,
						mktParams,
						null,
						wi.date(),
						wi.factor(),
						0.
					), 1, 2, 100.));
		}
	}

	/*
	 * API demonstrating how to calibrate a CDS curve from CDS and bond quotes
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static void BondCDSCurveCalibration()
		throws Exception
	{
		/*
		 * Bond calibration instrument
		 */

		Bond bond = BondBuilder.CreateSimpleFixed (
			"CCCalibBond",
			"DKK",
			"CC",
			0.05,
			2,
			"30/360",
			DateUtil.CreateFromYMD (
				2008,
				9,
				21
			),
			DateUtil.CreateFromYMD (
				2023,
				9,
				20
			),
			null,
			null
		);

		/*
		 * Discount Curve
		 */

		DiscountCurve dc = DiscountCurveBuilder.CreateFromFlatRate (
			DateUtil.Today(),
			"DKK",
			null,
			0.04
		);

		/*
		 * Credit Curve
		 */

		CreditCurve cc = CreditCurveBuilder.FromFlatHazard (
			DateUtil.Today().julian(),
			"CC",
			"USD",
			0.01,
			0.4
		);

		/*
		 * Component Market Parameters Container
		 */

		CurveSurfaceQuoteSet mktParams =  MarketParamsBuilder.Create (
			dc,
			null,
			null,
			cc,
			null,
			null,
			null,
			null
		);

		/*
		 * Valuation Parameters
		 */

		ValuationParams valParams = ValuationParams.Spot (
			DateUtil.Today(),
			0,
			"USD",
			Convention.DATE_ROLL_ACTUAL
		);

		/*
		 * Theoretical Price
		 */

		double dblTheoreticalPrice = bond.priceFromCreditBasis (
			valParams,
			mktParams,
			null,
			bond.maturityDate().julian(),
			1.,
			0.01
		);


		System.out.println ("Credit Price From DC and CC: " + dblTheoreticalPrice);

		/*
		 * CDS calibration instrument
		 */

		CreditDefaultSwap cds = CDSBuilder.CreateCDS (
			DateUtil.Today(),
			DateUtil.Today().addTenor ("5Y"),
			0.1,
			"DKK",
			0.40,
			"CC",
			"DKK",
			true
		);

		/*
		 * Set up the calibration instruments
		 */

		CalibratableFixedIncomeComponent[] aCalibInst = new CalibratableFixedIncomeComponent[] {
			cds,
			bond
		};

		/*
		 * Set up the calibration measures
		 */

		String[] astrCalibMeasure = new String[] {
			"FairPremium",
			"FairPrice"
		};

		/*
		 * Set up the calibration quotes
		 */

		double[] adblQuotes = new double[] {
			100.,
			dblTheoreticalPrice
		};

		/*
		 * Setup the curve scenario calibrator/generator and build the credit curve
		 */

		CreditCurve ccCalib = CreditScenarioCurveBuilder.CreateCreditCurve (
			"CC", 					// Name
			DateUtil.Today(), 	// Date
			aCalibInst,				// Calibration instruments
			dc,						// Discount Curve
			adblQuotes,				// Component Quotes
			astrCalibMeasure,		// Calibration Measures
			0.40,					// Recovery
			false					// Calibration is not flat
		);

		/*
		 * Calculate the survival probability, and recover the input quotes
		 */

		System.out.println (
			"Surv (2021, 1, 14): " +
			ccCalib.survival (
				DateUtil.CreateFromYMD (
					2021,
					1,
					14
				)
			)
		);

		/*
		 * Calibrated Component Market Parameters Container
		 */

		CurveSurfaceQuoteSet mktParamsCalib = MarketParamsBuilder.Create (
			dc,
			null,
			null,
			ccCalib,
			null,
			null,
			null,
			null
		);

		/*
		 * Verify the CDS fair premium using the calibrated credit curve
		 */

		System.out.println (
			cds.primaryCode() + " => " + cds.measureValue (
				valParams,
				CreditPricerParams.Standard(),
				mktParamsCalib,
				null,
				"FairPremium"
			)
		);

		/*
		 * Verify the Bond fair price using the calibrated credit curve
		 */

		System.out.println (
			bond.primaryCode() + " => " + bond.priceFromCreditBasis (
				valParams,
				mktParamsCalib,
				null,
				bond.maturityDate().julian(),
				1.,
				0.
			)
		);
	}

	public static final void main (
		final String astrArgs[])
		throws Exception
	{
		// String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		String strConfig = "";

		CreditAnalytics.Init (strConfig);

		CustomBondAPISample();

		BondCDSCurveCalibration();
	}
}

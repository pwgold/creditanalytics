
package org.drip.tester.functional;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
 * Copyright (C) 2011 Lakshmi Krishnamurthy
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
 * CreditAnalyticsTestSuite tests more-or-less the full suite of functionality exposed in CreditAnalytics API
 * 	across all products, curves, quotes, outputs, and parameters, and their variants.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CreditAnalyticsTestSuite {
	private static final boolean s_bSupressErrMsg = false;

	/*
	 * Holiday Calendar Demo OP
	 */

	private static final boolean s_bPrintHolLoc = false;
	private static final boolean s_bPrintHolsInYear = false;
	private static final boolean s_bPrintWeekDayHolsInYear = false;
	private static final boolean s_bPrintWeekendHolsInYear = false;
	private static final boolean s_bPrintWeekendDays = false;
	private static final boolean s_bPrintHolidaySet = false;

	/*
	 * DayCount Demo OP
	 */

	private static final boolean s_bPrintDayCountList = false;
	private static final boolean s_bPrintDayCountTest = false;

	/*
	 * Discount Curve Demo OP
	 */

	private static final boolean s_bPrintEODIRCurveNames = false;
	private static final boolean s_bPrintEODIRFullCurve = false;
	private static final boolean s_bPrintEODIRFullCurves = false;
	private static final boolean s_bPrintEODIRCashCurve = false;
	private static final boolean s_bPrintEODIRCashCurves = false;
	private static final boolean s_bPrintEODIREDFCurve = false;
	private static final boolean s_bPrintEODIREDFCurves = false;
	private static final boolean s_bPrintEODIRSwapCurve = false;
	private static final boolean s_bPrintEODIRSwapCurves = false;
	private static final boolean s_bDCFromDF = false;
	private static final boolean s_bDCFromRate = false;
	private static final boolean s_bDCFromFlatRate = false;
	private static final boolean s_bPrintEODTSYCurveNames = false;
	private static final boolean s_bPrintEODTSYCurve = false;
	private static final boolean s_bPrintEODTSYCurves = false;

	/*
	 * Credit Curve Demo OP
	 */

	private static final boolean s_bCCFromFlatHazard = false;
	private static final boolean s_bCCFromSurvival = false;
	private static final boolean s_bPrintEODCDSCurveNames = false;
	private static final boolean s_bPrintEODCDSCurve = false;
	private static final boolean s_bPrintEODCDSQuotes = false;
	private static final boolean s_bPrintEODCDSCurves = false;

	/*
	 * CDS Demo OP
	 */

	// private static final boolean s_bCDSCouponCFDisplay = false;
	private static final boolean s_bCDSLossCFDisplay = false;

	/*
	 * Bond Demo OP
	 */

	private static final boolean s_bAvailableTickers = false;
	private static final boolean s_bISINForTicker = false;
	// private static final boolean s_bBondCouponCFDisplay = false;
	private static final boolean s_bBondLossCFDisplay = false;
	private static final boolean s_bBondAnalDisplay = false;

	/*
	 * Custom Bond Tests
	 */

	private static final boolean s_bCustomBondAnalDisplay = false;
	private static final boolean s_bCustomBondCouponCFDisplay = false;

	/*
	 * Bond Ticker Demo OP
	 */

	private static final boolean s_bTickerAnalDisplay = false;
	private static final boolean s_bTickerNotionalDisplay = false;
	private static final boolean s_bCumulativeTickerNotionalDisplay = false;

	/*
	 * Bond EOD Measures
	 */

	private static final boolean s_bBondEODMeasuresFromPrice = false;
	private static final boolean s_bBondEODMeasuresFromTSYSpread = false;
	private static final boolean s_bBondEODMeasuresFromYield = false;

	/*
	 * CDS EOD Measures
	 */

	private static final boolean s_bEODCDSMeasures = false;

	/*
	 * Bond Static Demo OP
	 */

	private static final boolean s_bStaticDisplay = false;

	/*
	 * Credit Curve calibration from Bond and CDS quotes Demo OP
	 */

	private static final boolean s_bCDSBondCreditCurve = false;

	/*
	 * FX Forward OP
	 */

	private static final boolean s_bFXFwd = false;

	/*
	 * Bond Basket OP
	 */

	private static final boolean s_bBasketBond = false;

	/*
	 * CDS Basket/CDX/iTRAXX OP
	 */

	private static final boolean s_bBasketCDS = false;
	private static final boolean s_bNamedCDXMap = false;
	private static final boolean s_bStandardCDXNames = false;
	private static final boolean s_bOnTheRun = false;
	private static final boolean s_bCDXSeries = false;

	private static final org.drip.quant.common.Array2D MakeFSPrincipal()
	{
		int NUM_SCHEDULE_ENTRIES = 5;
		double[] adblDate = new double[NUM_SCHEDULE_ENTRIES];
		double[] adblFactor = new double[NUM_SCHEDULE_ENTRIES];

		org.drip.analytics.date.JulianDate dtEOSStart = org.drip.analytics.date.DateUtil.Today().addDays
			(2);

		for (int i = 0; i < NUM_SCHEDULE_ENTRIES; ++i) {
			adblFactor[i] = 1.0 - 0. * i;

			adblDate[i] = dtEOSStart.addYears (i + 2).julian();
		}

		try {
			return org.drip.quant.common.Array2D.FromArray (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static final org.drip.quant.common.Array2D MakeFSCoupon()
	{
		int NUM_SCHEDULE_ENTRIES = 5;
		double[] adblDate = new double[NUM_SCHEDULE_ENTRIES];
		double[] adblFactor = new double[NUM_SCHEDULE_ENTRIES];

		org.drip.analytics.date.JulianDate dtEOSStart = org.drip.analytics.date.DateUtil.Today().addDays
			(2);

		for (int i = 0; i < NUM_SCHEDULE_ENTRIES; ++i) {
			adblFactor[i] = 1.0 - 0. * i;

			adblDate[i] = dtEOSStart.addYears (i + 2).julian();
		}

		try {
			return org.drip.quant.common.Array2D.FromArray (adblDate, adblFactor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates a custom named bond from the bond type and parameters
	 * 
	 * @param strName String representing the bond name
	 * @param iBondType Integer representing the bond type (fixed/floating/custom)
	 * 
	 * @return The created Bond
	 */

	public static final org.drip.product.definition.Bond CreateCustomBond (
		final java.lang.String strName,
		final int iBondType)
	{
		boolean bEOSOn = false;
		boolean bEOSAmerican = false;
		org.drip.product.credit.BondComponent bond = null;
		org.drip.product.params.EmbeddedOptionSchedule eosPut = null;
		org.drip.product.params.EmbeddedOptionSchedule eosCall = null;

		if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FLOATER == iBondType)
			bond = org.drip.product.creator.BondBuilder.CreateSimpleFloater (strName, "USD", "DRIPRI", "",
				0.01, 2, "30/360", org.drip.analytics.date.DateUtil.CreateFromYMD (2012, 3, 21),
					org.drip.analytics.date.DateUtil.CreateFromYMD (2023, 9, 20), MakeFSPrincipal(),
						MakeFSCoupon());
		else if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FIXED == iBondType)
			bond = org.drip.product.creator.BondBuilder.CreateSimpleFixed (strName, "USD", "", 0.05, 2,
				"30/360", org.drip.analytics.date.DateUtil.CreateFromYMD (2012, 3, 21),
					org.drip.analytics.date.DateUtil.CreateFromYMD (2023, 9, 20), MakeFSPrincipal(),
						MakeFSCoupon());
		else if (org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FROM_CF == iBondType) {
			final int NUM_CF_ENTRIES = 30;
			double[] adblCouponAmount = new double[NUM_CF_ENTRIES];
			double[] adblPrincipalAmount = new double[NUM_CF_ENTRIES];
			org.drip.analytics.date.JulianDate[] adt = new
				org.drip.analytics.date.JulianDate[NUM_CF_ENTRIES];

			org.drip.analytics.date.JulianDate dtEffective = org.drip.analytics.date.DateUtil.CreateFromYMD
				(2008, 9, 20);

			for (int i = 0; i < NUM_CF_ENTRIES; ++i) {
				adt[i] = dtEffective.addMonths (6 * (i + 1));

				adblCouponAmount[i] = 0.025;
				adblPrincipalAmount[i] = 1.0;
			}

			bond = org.drip.product.creator.BondBuilder.CreateBondFromCF (strName, dtEffective, "USD", "",
				"30/360", 1., 0.05, 2, adt, adblCouponAmount, adblPrincipalAmount, false);
		}

		if (bEOSOn) {
			double[] adblDate = new double[5];
			double[] adblPutFactor = new double[5];
			double[] adblCallFactor = new double[5];

			org.drip.analytics.date.JulianDate dtEOSStart =
				org.drip.analytics.date.DateUtil.Today().addDays (2);

			for (int i = 0; i < 5; ++i) {
				adblPutFactor[i] = 0.9;
				adblCallFactor[i] = 1.0;

				adblDate[i] = dtEOSStart.addYears (i + 2).julian();
			}

			if (bEOSAmerican) {
				eosCall = org.drip.product.params.EmbeddedOptionSchedule.FromAmerican
					(org.drip.analytics.date.DateUtil.Today().julian() + 1, adblDate, adblCallFactor,
						false, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);

				eosPut = org.drip.product.params.EmbeddedOptionSchedule.FromAmerican
					(org.drip.analytics.date.DateUtil.Today().julian(), adblDate, adblPutFactor, true,
						30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);
			} else {
				try {
					eosCall = new org.drip.product.params.EmbeddedOptionSchedule (adblDate, adblCallFactor,
						false, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);

					eosPut = new org.drip.product.params.EmbeddedOptionSchedule (adblDate, adblPutFactor,
						true, 30, false, java.lang.Double.NaN, "", java.lang.Double.NaN);
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CreateCustomBond failed.");

						return null;
					}

					e.printStackTrace();
				}
			}

			bond.setEmbeddedCallSchedule (eosCall);

			bond.setEmbeddedPutSchedule (eosPut);
		}

		return bond;
	}

	/**
	 * Sample demonstrating the calendar API
	 */

	public static final void CalenderAPISample()
	{
		java.util.Set<java.lang.String> setLoc = org.drip.service.api.CreditAnalytics.GetHolLocations();

		org.drip.analytics.date.JulianDate[] adtHols = org.drip.service.api.CreditAnalytics.GetHolsInYear
			("USD,GBP", 2011);

		org.drip.analytics.date.JulianDate[] adtWeekDayHols =
			org.drip.service.api.CreditAnalytics.GetWeekDayHolsInYear ("USD,GBP", 2011);

		org.drip.analytics.date.JulianDate[] adtWeekendHols =
			org.drip.service.api.CreditAnalytics.GetWeekendHolsInYear ("USD,GBP", 2011);

		int[] aiWkendDays = org.drip.service.api.CreditAnalytics.GetWeekendDays ("USD,GBP");

		boolean bIsHoliday = false;

		try {
			bIsHoliday = org.drip.service.api.CreditAnalytics.IsHoliday ("USD,GBP",
				org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 12, 28));
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("CalendarAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		java.util.List<java.lang.Double> lsHols = org.drip.analytics.daycount.Convention.HolidaySet
			(org.drip.analytics.date.DateUtil.Today().julian(),
				org.drip.analytics.date.DateUtil.Today().addYears (1).julian(), "USD,GBP");

		if (s_bPrintHolLoc) {
			System.out.println ("Num Hol Locations: " + setLoc.size());

			for (java.lang.String strLoc : setLoc)
				System.out.println (strLoc);
		}

		if (s_bPrintHolsInYear) {
			System.out.println (org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 12, 28) +
				" is a USD,GBP holiday? " + bIsHoliday);

			System.out.println ("USD,GBP has " + adtHols.length + " hols");

			for (int i = 0; i < adtHols.length; ++i)
				System.out.println (adtHols[i]);
		}

		if (s_bPrintWeekDayHolsInYear) {
			System.out.println ("USD,GBP has " + adtWeekDayHols.length + " week day hols");

			for (int i = 0; i < adtWeekDayHols.length; ++i)
				System.out.println (adtWeekDayHols[i]);
		}

		if (s_bPrintWeekendHolsInYear) {
			System.out.println ("USD,GBP has " + adtWeekendHols.length + " weekend hols");

			for (int i = 0; i < adtWeekendHols.length; ++i)
				System.out.println (adtWeekendHols[i]);
		}

		if (s_bPrintWeekendDays) {
			System.out.println ("USD,GBP has " + aiWkendDays.length + " weekend days");

			for (int i = 0; i < aiWkendDays.length; ++i) {
				try {
					System.out.println (org.drip.analytics.date.DateUtil.DayChars (aiWkendDays[i]));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CalendarAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintHolidaySet) {
			for (double dblDate : lsHols) {
				try {
					System.out.println (new org.drip.analytics.date.JulianDate (dblDate).toOracleDate());
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CalendarAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample API demonstrating the day count functionality
	 */

	public static final void DayCountAPISample()
	{
		java.lang.String strDCList = org.drip.service.api.CreditAnalytics.GetAvailableDC();

		double dblYF = java.lang.Double.NaN;

		try {
			dblYF = org.drip.service.api.CreditAnalytics.YearFraction
				(org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 1, 14),
					org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 2, 14), "Act/360", false, "USD");
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("DayCountAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.analytics.date.JulianDate dtAdjusted = org.drip.service.api.CreditAnalytics.Adjust
			(org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 1, 16), "USD", 0);

		org.drip.analytics.date.JulianDate dtRoll = org.drip.service.api.CreditAnalytics.RollDate
			(org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 1, 16), "USD",
				org.drip.analytics.daycount.Convention.DATE_ROLL_PREVIOUS);

		if (s_bPrintDayCountList)
			System.out.println (strDCList);

		if (s_bPrintDayCountTest) {
			System.out.println ("YearFract: " + dblYF);

			System.out.println ("Adjusted: " + dtAdjusted);

			System.out.println ("Rolled: " + dtRoll);
		}
	}

	/**
	 * Sample API demonstrating the creation/usage of discount curve
	 */

	public static final void DiscountCurveAPISample()
	{
		org.drip.analytics.date.JulianDate dt1 = org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 12,
			16);

		org.drip.analytics.date.JulianDate dt2 = org.drip.analytics.date.DateUtil.CreateFromYMD (2012, 1,
			17);

		java.util.Set<java.lang.String> setstrIRCurves =
			org.drip.service.api.CreditAnalytics.GetEODIRCurveNames (dt1);

		org.drip.analytics.rates.DiscountCurve dc =
			org.drip.service.api.CreditAnalytics.LoadEODFullIRCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.rates.DiscountCurve> mapDC
			= org.drip.service.api.CreditAnalytics.LoadEODFullIRCurves ("EUR", dt1, dt2);

		org.drip.analytics.rates.DiscountCurve dcCash =
			org.drip.service.api.CreditAnalytics.LoadEODIRCashCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.rates.DiscountCurve>
			mapCashDC = org.drip.service.api.CreditAnalytics.LoadEODIRCashCurves ("EUR", dt1, dt2);

		org.drip.analytics.rates.DiscountCurve dcEDF =
			org.drip.service.api.CreditAnalytics.LoadEODEDSFCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.rates.DiscountCurve>
			mapEDSFDC = org.drip.service.api.CreditAnalytics.LoadEODEDSFCurves ("EUR", dt1, dt2);

		org.drip.analytics.rates.DiscountCurve dcIRS =
			org.drip.service.api.CreditAnalytics.LoadEODIRSwapCurve ("EUR", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.rates.DiscountCurve>
			mapIRSDC = org.drip.service.api.CreditAnalytics.LoadEODIRSwapCurves ("EUR", dt1, dt2);

		double[] adblDF = new double[5];
		double[] adblDate = new double[5];
		double[] adblRate = new double[5];

		org.drip.analytics.date.JulianDate dtStart = org.drip.analytics.date.DateUtil.Today();

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).julian();

			adblDF[i] = 1. - 2 * (i + 1) * (adblRate[i] = 0.05);
		}

		org.drip.analytics.rates.DiscountCurve dcFromDF =
			org.drip.state.creator.DiscountCurveBuilder.BuildFromDF (dtStart, "EUR", null, adblDate, adblDF,
				org.drip.state.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.rates.DiscountCurve dcFromRate =
			org.drip.state.creator.DiscountCurveBuilder.CreateDC (dtStart, "EUR", null, adblDate, adblRate,
				org.drip.state.creator.DiscountCurveBuilder.BOOTSTRAP_MODE_CONSTANT_FORWARD);

		org.drip.analytics.rates.DiscountCurve dcFromFlatRate =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate (dtStart, "DKK", null, 0.04);

		java.util.Set<java.lang.String> setstrTSYCurves =
			org.drip.service.api.CreditAnalytics.GetEODTSYCurveNames (dt1);

		org.drip.analytics.rates.DiscountCurve dcTSY =
			org.drip.service.api.CreditAnalytics.LoadEODTSYCurve ("USD", dt1);

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.rates.DiscountCurve>
			mapTSYDC = org.drip.service.api.CreditAnalytics.LoadEODTSYCurves ("USD", dt1, dt2);

		if (s_bPrintEODIRCurveNames) {
			try {
				System.out.println ("2011.1.14 has " + setstrIRCurves.size() + " IR Curves. They are:");

				for (java.lang.String strIRCurveName : setstrIRCurves)
					System.out.println (strIRCurveName);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODTSYCurveNames) {
			try {
				System.out.println ("2011.1.14 has " + setstrTSYCurves.size() + " IR Curves. They are:");

				for (java.lang.String strTSYCurveName : setstrTSYCurves)
					System.out.println (strTSYCurveName);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODIRFullCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dc.df
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			org.drip.product.definition.CalibratableFixedIncomeComponent[] aCC = dc.calibComp();

			for (int i = 0; i < aCC.length; ++i) {
				try {
					System.out.println (aCC[i].primaryCode() + " => " + dc.manifestMeasure
						(aCC[i].primaryCode()));
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}

		if (s_bPrintEODIRFullCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.rates.DiscountCurve> meDC : mapDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meDC.getKey();

				org.drip.analytics.rates.DiscountCurve dcEOD = meDC.getValue();

				try {
					System.out.println (dt + "[IRS.3Y] => " + dcEOD.manifestMeasure ("IRS.3Y"));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintEODIRCashCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcCash.df
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			org.drip.product.definition.CalibratableFixedIncomeComponent[] aCCCash = dcCash.calibComp();

			for (int i = 0; i < aCCCash.length; ++i) {
				try {
					System.out.println (aCCCash[i].primaryCode() + " => " + (int) (10000. *
						dcCash.manifestMeasure (aCCCash[i].primaryCode()).get ("Rate")));
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}

		if (s_bPrintEODIRCashCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.rates.DiscountCurve> meCashDC : mapCashDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meCashDC.getKey();

				org.drip.analytics.rates.DiscountCurve dcEOD = meCashDC.getValue();

				try {
					System.out.println (dt + "[3M] => " + (int) (10000. * dcEOD.manifestMeasure ("3M").get
						("Rate")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintEODIREDFCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcEDF.df
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			org.drip.product.definition.CalibratableFixedIncomeComponent[] aCCEDF = dcEDF.calibComp();

			for (int i = 0; i < aCCEDF.length; ++i) {
				try {
					System.out.println (aCCEDF[i].primaryCode() + " => " + (int) (10000. *
						dcEDF.manifestMeasure (aCCEDF[i].primaryCode()).get ("Rate")));
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}

		if (s_bPrintEODIREDFCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.rates.DiscountCurve> meEDFDC : mapEDSFDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meEDFDC.getKey();

				org.drip.analytics.rates.DiscountCurve dcEOD = meEDFDC.getValue();

				try {
					System.out.println (dt + "[EDZ3] => " + (int) (10000. * dcEOD.manifestMeasure
						("EDZ3").get ("Rate")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bPrintEODIRSwapCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcIRS.df
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			org.drip.product.definition.CalibratableFixedIncomeComponent[] aCCIRS = dcIRS.calibComp();

			for (int i = 0; i < aCCIRS.length; ++i) {
				try {
					System.out.println (aCCIRS[i].primaryCode() + " => " + (int) (10000. *
						dcIRS.manifestMeasure (aCCIRS[i].primaryCode()).get ("Rate")));
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}

		if (s_bPrintEODIRSwapCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.rates.DiscountCurve> meIRSDC : mapIRSDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meIRSDC.getKey();

				org.drip.analytics.rates.DiscountCurve dcEOD = meIRSDC.getValue();

				try {
					System.out.println (dt + "[IRS.40Y bp] => " + (int) (dcEOD.manifestMeasure
						("IRS.40Y").get ("Rate") * 10000.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		if (s_bDCFromDF) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.DateUtil.Today().addYears (10);

			try {
				System.out.println ("DCFromDF[" + dt.toString() + "]; DF=" + dcFromDF.df (dt) + "; Rate="
					+ dcFromDF.zero ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bDCFromRate) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.DateUtil.Today().addYears (10);

			try {
				System.out.println ("DCFromRate[" + dt.toString() + "]; DF=" + dcFromRate.df (dt) +
					"; Rate=" + dcFromRate.zero ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bDCFromFlatRate) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.DateUtil.Today().addYears (10);

			try {
				System.out.println ("DCFromFlatRate[" + dt.toString() + "]; DF=" + dcFromFlatRate.df (dt)
					+ "; Rate=" + dcFromFlatRate.zero ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODTSYCurve) {
			try {
				System.out.println ("DF (2021, 1, 14): " + dcTSY.df
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("DiscountCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			org.drip.product.definition.CalibratableFixedIncomeComponent[] aCompTSY = dcTSY.calibComp();

			for (int i = 0; i < aCompTSY.length; ++i) {
				try {
					System.out.println (aCompTSY[i].primaryCode() + " => " + (int) (10000. *
						dcTSY.manifestMeasure (aCompTSY[i].primaryCode()).get ("Rate")));
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}

		if (s_bPrintEODTSYCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.rates.DiscountCurve> meTSYDC : mapTSYDC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meTSYDC.getKey();

				org.drip.analytics.rates.DiscountCurve dcTSYEOD = meTSYDC.getValue();

				try {
					System.out.println (dt + "[5Y] => " + (int) (10000. * dcTSYEOD.manifestMeasure ("5Y").get
						("Rate")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("DiscountCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample API demonstrating the creation/usage of the credit curve API
	 */

	public static final void CreditCurveAPISample()
	{
		org.drip.analytics.definition.CreditCurve ccFlatHazard =
			org.drip.state.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.DateUtil.Today().julian(), "CC", "USD", 0.02, 0.4);

		double[] adblDate = new double[5];
		double[] adblSurvival = new double[5];

		org.drip.analytics.date.JulianDate dtStart = org.drip.analytics.date.DateUtil.Today();

		for (int i = 0; i < 5; ++i) {
			adblDate[i] = dtStart.addYears (2 * i + 2).julian();

			adblSurvival[i] = 1. - 0.1 * (i + 1);
		}

		org.drip.analytics.definition.CreditCurve ccFromSurvival =
			org.drip.state.creator.CreditCurveBuilder.FromSurvival (dtStart.julian(), "CC", "USD",
				adblDate, adblSurvival, 0.4);

		java.util.Set<java.lang.String> setstrCDSCurves =
			org.drip.service.api.CreditAnalytics.GetEODCDSCurveNames
				(org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 7, 21));

		org.drip.analytics.definition.CreditCurve ccEOD =
			org.drip.service.api.CreditAnalytics.LoadEODCDSCreditCurve ("813796", "USD",
				org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 7, 21));

		java.util.Map <org.drip.analytics.date.JulianDate, org.drip.analytics.definition.CreditCurve> mapCC =
			org.drip.service.api.CreditAnalytics.LoadEODCDSCreditCurves ("813796", "USD",
				org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 7, 14),
					org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 7, 21));

		if (s_bPrintEODCDSCurveNames) {
			try {
				System.out.println ("2011.1.14 has " + setstrCDSCurves.size() + " CDS Curves. They are:");

				for (java.lang.String strCDSCurveName : setstrCDSCurves)
					System.out.println (strCDSCurveName);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bCCFromFlatHazard) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.DateUtil.Today().addYears (10);

			try {
				System.out.println ("CCFromFlatHazard[" + dt.toString() + "]; Survival=" +
					ccFlatHazard.survival ("10Y") + "; Hazard=" + ccFlatHazard.hazard ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bCCFromSurvival) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.DateUtil.Today().addYears (10);

			try {
				System.out.println ("CCFromSurvival[" + dt.toString() + "]; Survival=" +
					ccFromSurvival.survival ("10Y") + "; Hazard=" + ccFromSurvival.hazard ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODCDSCurve) {
			org.drip.analytics.date.JulianDate dt = org.drip.analytics.date.DateUtil.Today().addYears (10);

			try {
				System.out.println ("CCFromEOD[" + dt.toString() + "]; Survival=" + ccEOD.survival ("10Y") +
					"; Hazard=" + ccEOD.hazard ("10Y"));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		if (s_bPrintEODCDSQuotes) {
			try {
				System.out.println ("Surv (2021, 1, 14): " + ccEOD.survival
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("CreditCurveAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			org.drip.product.definition.CalibratableFixedIncomeComponent[] aCompCDS = ccEOD.calibComp();

			for (int i = 0; i < aCompCDS.length; ++i) {
				try {
					System.out.println (aCompCDS[i].primaryCode() + " => " + (ccEOD.manifestMeasure
						(aCompCDS[i].primaryCode()).get ("Rate")));
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return;
				}
			}
		}

		if (s_bPrintEODCDSCurves) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate,
				org.drip.analytics.definition.CreditCurve> meCC : mapCC.entrySet()) {
				org.drip.analytics.date.JulianDate dt = meCC.getKey();

				org.drip.analytics.definition.CreditCurve ccCOB = meCC.getValue();

				try {
					System.out.println (dt + "[CDS.5Y] => " + (ccCOB.manifestMeasure ("CDS.5Y").get
						("Rate")));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CreditCurveAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the CDS API
	 */

	public static final void CDSAPISample()
	{
		org.drip.analytics.rates.DiscountCurve dc =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "USD", null, 0.05);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.state.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.DateUtil.Today().julian(), "CC", "USD", 0.02, 0.4);

		org.drip.product.definition.CreditDefaultSwap cds = org.drip.product.creator.CDSBuilder.CreateSNAC
			(org.drip.analytics.date.DateUtil.Today(), "5Y", 0.1, "CC");

		if (s_bCDSLossCFDisplay) {
			System.out.println
				("Loss Start     Loss End      Pay Date      Cpn    Notl     Rec    EffDF    StartSurv  EndSurv");

			System.out.println
				("----------     --------      --------      ---    ----     ---    -----    ---------  -------");

			for (org.drip.analytics.cashflow.LossQuadratureMetrics dp : cds.lossFlow
				(org.drip.param.valuation.ValuationParams.Spot (org.drip.analytics.date.DateUtil.Today(), 0,
					"USD", org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL),
						org.drip.param.pricer.CreditPricerParams.Standard(),
							org.drip.param.creator.MarketParamsBuilder.Credit (dc, cc)))
				System.out.println (org.drip.analytics.date.DateUtil.FromJulian (dp.start()) + "    " +
					org.drip.analytics.date.DateUtil.FromJulian (dp.end()) + "    " +
						org.drip.quant.common.FormatUtil.FormatDouble (dp.effectiveNotional(), 1, 0, 1.) +
							"    " + org.drip.quant.common.FormatUtil.FormatDouble (dp.effectiveRecovery(),
								1, 2, 1.) + "    " + org.drip.quant.common.FormatUtil.FormatDouble
									(dp.effectiveDF(), 1, 4, 1.)  + "    " +
										org.drip.quant.common.FormatUtil.FormatDouble (dp.startSurvival(), 1,
											4, 1.) + "    " + org.drip.quant.common.FormatUtil.FormatDouble
												(dp.endSurvival(), 1, 4, 1.));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the bond API
	 */

	public static final void BondAPISample()
	{
		java.util.Set<java.lang.String> setstrTickers =
			org.drip.service.api.CreditAnalytics.GetAvailableTickers();

		java.util.List<java.lang.String> lsstrISIN = org.drip.service.api.CreditAnalytics.GetISINsForTicker
			("DB");

		// java.lang.String strISIN = "XS0145044193";
		java.lang.String strISIN = "US78490FPP89"; // EOS
		// java.lang.String strISIN = "US760677FD19"; // Amortizer
		org.drip.param.valuation.ValuationCustomizationParams quotingParams = null;
		boolean bInFirstPeriod = true;
		boolean bInLastPeriod = true;
		double dblGTMFromPrice = java.lang.Double.NaN;
		double dblITMFromPrice = java.lang.Double.NaN;
		double dblYTMFromPrice = java.lang.Double.NaN;
		double dblZTMFromPrice = java.lang.Double.NaN;
		double dblPECSFromPrice = java.lang.Double.NaN;
		double dblTSYTMFromPrice = java.lang.Double.NaN;
		double dblYieldFromPrice = java.lang.Double.NaN;
		double dblBondCreditPrice = java.lang.Double.NaN;
		double dblParASWFromPrice = java.lang.Double.NaN;
		double dblPECSTMFromPrice = java.lang.Double.NaN;
		double dblGSpreadFromPrice = java.lang.Double.NaN;
		double dblISpreadFromPrice = java.lang.Double.NaN;
		double dblZSpreadFromPrice = java.lang.Double.NaN;
		double dblParASWTMFromPrice = java.lang.Double.NaN;
		double dblPriceFromTSYSpread = java.lang.Double.NaN;
		double dblTSYSpreadFromPrice = java.lang.Double.NaN;
		double dblYieldFromTSYSpread = java.lang.Double.NaN;
		double dblParASWFromTSYSpread = java.lang.Double.NaN;
		double dblCreditBasisFromPrice = java.lang.Double.NaN;
		double dblGSpreadFromTSYSpread = java.lang.Double.NaN;
		double dblISpreadFromTSYSpread = java.lang.Double.NaN;
		double dblZSpreadFromTSYSpread = java.lang.Double.NaN;
		double dblCreditBasisTMFromPrice = java.lang.Double.NaN;
		double dblDiscountMarginFromPrice = java.lang.Double.NaN;
		double dblCreditBasisFromTSYSpread = java.lang.Double.NaN;
		double dblDiscountMarginTMFromPrice = java.lang.Double.NaN;
		double dblDiscountMarginFromTSYSpread = java.lang.Double.NaN;

		try {
			quotingParams = new org.drip.param.valuation.ValuationCustomizationParams ("30/360", 2, true,
				null, "USD", false, null, null);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.product.definition.Bond bond = org.drip.service.api.CreditAnalytics.GetBond (strISIN);

		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.DateUtil.Today();

		org.drip.param.valuation.ValuationParams valParams = org.drip.param.valuation.ValuationParams.Spot
			(dtToday, 0, "", org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL);

		org.drip.analytics.rates.DiscountCurve dc =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "USD", null, 0.03);

		org.drip.analytics.rates.DiscountCurve dcTSY =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", null, 0.04);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.state.creator.CreditCurveBuilder.FromFlatHazard (dtToday.julian(), "CC", "USD", 0.02,
				0.);

		org.drip.param.valuation.WorkoutInfo wi =
			org.drip.service.api.CreditAnalytics.BondWorkoutInfoFromPrice (strISIN, dtToday, dc, 1.);

		try {
			dblYieldFromPrice = org.drip.service.api.CreditAnalytics.BondYieldFromPrice (strISIN, dtToday,
				dc, 1.);

			dblYTMFromPrice = org.drip.service.api.CreditAnalytics.BondYTMFromPrice (strISIN, valParams, dc,
				1., quotingParams);

			dblZSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondZSpreadFromPrice (strISIN,
				dtToday, dc, 1.);

			dblZTMFromPrice = org.drip.service.api.CreditAnalytics.BondZTMFromPrice (strISIN, valParams, dc,
				1., quotingParams);

			dblISpreadFromPrice = org.drip.service.api.CreditAnalytics.BondISpreadFromPrice (strISIN,
				dtToday, dc, 1.);

			dblITMFromPrice = org.drip.service.api.CreditAnalytics.BondITMFromPrice (strISIN, valParams, dc,
				1., quotingParams);

			dblDiscountMarginFromPrice = org.drip.service.api.CreditAnalytics.BondDiscountMarginFromPrice
				(strISIN, dtToday, dc, 1.);

			dblDiscountMarginTMFromPrice = org.drip.service.api.CreditAnalytics.BondDiscountMarginTMFromPrice
				(strISIN, valParams, dc, 1., quotingParams);

			dblTSYSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondTSYSpreadFromPrice (strISIN,
				dtToday, dc, dcTSY, 1.);

			dblTSYTMFromPrice = org.drip.service.api.CreditAnalytics.BondTSYTMFromPrice (strISIN, valParams,
				dc, dcTSY, 1., quotingParams);

			dblGSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondGSpreadFromPrice (strISIN,
				dtToday, dc, dcTSY, 1.);

			dblGTMFromPrice = org.drip.service.api.CreditAnalytics.BondGTMFromPrice (strISIN, valParams, dc,
				dcTSY, 1., quotingParams);

			dblCreditBasisFromPrice = org.drip.service.api.CreditAnalytics.BondCreditBasisFromPrice (strISIN,
				dtToday, dc, cc, 1.);

			dblCreditBasisTMFromPrice = org.drip.service.api.CreditAnalytics.BondCreditBasisTMFromPrice
				(strISIN, valParams, dc, cc, 1., quotingParams);

			dblPECSFromPrice = org.drip.service.api.CreditAnalytics.BondPECSFromPrice (strISIN, dtToday, dc,
				cc, 1.);

			dblPECSTMFromPrice = org.drip.service.api.CreditAnalytics.BondCreditBasisTMFromPrice (strISIN,
				valParams, dc, cc, 1., quotingParams);

			dblPriceFromTSYSpread = org.drip.service.api.CreditAnalytics.BondPriceFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblYieldFromTSYSpread = org.drip.service.api.CreditAnalytics.BondYieldFromTSYSpread (strISIN,
				dtToday, dcTSY, 0.0271);

			dblZSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondZSpreadFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblISpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondISpreadFromTSYSpread (strISIN,
				dtToday, dc, dcTSY, 0.0271);

			dblDiscountMarginFromTSYSpread = org.drip.service.api.CreditAnalytics.BondISpreadFromTSYSpread
				(strISIN, dtToday, dc, dcTSY, 0.0271);

			dblGSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondGSpreadFromTSYSpread (strISIN,
				dtToday, dc,  dcTSY, 0.0271);

			dblCreditBasisFromTSYSpread = org.drip.service.api.CreditAnalytics.BondCreditBasisFromTSYSpread
				(strISIN, dtToday, dc, dcTSY, cc, 0.0271);

			dblBondCreditPrice = org.drip.service.api.CreditAnalytics.BondCreditPrice (strISIN, valParams,
				dc, cc, quotingParams);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.analytics.date.JulianDate dtPreviousCoupon =
			org.drip.service.api.CreditAnalytics.PreviousCouponDate (strISIN, dtToday);

		org.drip.analytics.date.JulianDate dtCurrentCoupon =
			org.drip.service.api.CreditAnalytics.NextCouponDate (strISIN, dtToday);

		org.drip.analytics.date.JulianDate dtNextCoupon =
			org.drip.service.api.CreditAnalytics.NextCouponDate (strISIN, dtToday);

		org.drip.analytics.date.JulianDate dtEffective = org.drip.service.api.CreditAnalytics.EffectiveDate
			(strISIN);

		org.drip.analytics.date.JulianDate dtMaturity = org.drip.service.api.CreditAnalytics.MaturityDate
			(strISIN);

		try {
			bInFirstPeriod = org.drip.service.api.CreditAnalytics.InFirstPeriod (strISIN,
				valParams.valueDate());

			bInLastPeriod = org.drip.service.api.CreditAnalytics.InLastPeriod (strISIN,
				valParams.valueDate());
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) return;

			e.printStackTrace();
		}

		org.drip.analytics.output.ExerciseInfo nei = org.drip.service.api.CreditAnalytics.NextExerciseInfo
			(strISIN, dtToday);

		if (s_bAvailableTickers) {
			for (java.lang.String strTicker : setstrTickers)
				System.out.println (strTicker);
		}

		if (s_bISINForTicker) {
			for (java.lang.String strBondISIN : lsstrISIN)
				System.out.println (strBondISIN);
		}

		if (s_bBondLossCFDisplay) {
			System.out.println
				("Loss Start     Loss End      Pay Date      Cpn    Notl     Rec    EffDF    StartSurv  EndSurv");

			System.out.println
				("----------     --------      --------      ---    ----     ---    -----    ---------  -------");
	
			for (org.drip.analytics.cashflow.LossQuadratureMetrics dp : bond.lossFlow (valParams,
				org.drip.param.pricer.CreditPricerParams.Standard(),
					org.drip.param.creator.MarketParamsBuilder.Credit (dc, cc)))
				System.out.println (org.drip.analytics.date.DateUtil.FromJulian (dp.start()) + "    " +
					org.drip.analytics.date.DateUtil.FromJulian (dp.end()) + "    " +
						org.drip.quant.common.FormatUtil.FormatDouble (dp.effectiveNotional(), 1, 0, 1.) +
							"    " + org.drip.quant.common.FormatUtil.FormatDouble (dp.effectiveRecovery(),
								1, 2, 1.) + "    " + org.drip.quant.common.FormatUtil.FormatDouble
									(dp.effectiveDF(), 1, 4, 1.)  + "    " +
										org.drip.quant.common.FormatUtil.FormatDouble (dp.startSurvival(), 1,
											4, 1.) + "    " + org.drip.quant.common.FormatUtil.FormatDouble
												(dp.endSurvival(), 1, 4, 1.));
		}

		if (s_bBondAnalDisplay) {
			try {
				System.out.println (strISIN + "    " + bond.ticker() + " " +
					org.drip.quant.common.FormatUtil.FormatDouble (bond.couponMetrics
						(org.drip.analytics.date.DateUtil.Today().julian(), valParams,
							org.drip.param.creator.MarketParamsBuilder.Credit (dc, cc)).rate(), 2, 3, 100.) +
								" " + bond.maturityDate());

				System.out.println ("Work-out date From Price: " + new org.drip.analytics.date.JulianDate
					(wi.date()));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			System.out.println ("Work-out factor From Price: " + wi.factor());

			System.out.println ("Work-out Yield From Price: " +
				org.drip.quant.common.FormatUtil.FormatDouble (wi.yield(), 2, 3, 100.));

			System.out.println ("Work-out Type for Price: " +
				org.drip.analytics.support.AnalyticsHelper.WorkoutTypeToString (wi.type()));

			System.out.println ("Yield From Price: " + org.drip.quant.common.FormatUtil.FormatDouble
				(dblYieldFromPrice, 2, 3, 100.) + " / " + org.drip.quant.common.FormatUtil.FormatDouble
					(dblYTMFromPrice, 2, 3, 100.));

			System.out.println ("Z Spread From Price: " + (int) (10000. * dblZSpreadFromPrice) + " / " +
				(int) (10000. * dblZTMFromPrice));

			System.out.println ("I Spread From Price: " + (int) (10000. * dblISpreadFromPrice) + " / " +
				(int) (10000. * dblITMFromPrice));

			System.out.println ("Discount Margin From Price: " + (int) (10000. * dblDiscountMarginFromPrice)
				+ " / " + (int) (10000. * dblDiscountMarginTMFromPrice));

			System.out.println ("TSY Spread From Price: " + (int) (10000. * dblTSYSpreadFromPrice) + " / " +
				(int) (10000. * dblTSYTMFromPrice));

			System.out.println ("G Spread From Price: " + (int) (10000. * dblGSpreadFromPrice) + " / " +
				(int) (10000. * dblGTMFromPrice));

			System.out.println ("Par ASW From Price: " + (int) dblParASWFromPrice + " / " + (int)
				dblParASWTMFromPrice);

			System.out.println ("Credit Basis From Price: " + (int) (10000. * dblCreditBasisFromPrice) +
				" / " + (int) (10000. * dblCreditBasisTMFromPrice));

			System.out.println ("PECS From Price: " + (int) (10000. * dblPECSFromPrice) + " / " + (int)
				(10000. * dblPECSTMFromPrice));

			System.out.println ("Price From TSY Spread: " +
				org.drip.quant.common.FormatUtil.FormatDouble (dblPriceFromTSYSpread, 2, 3, 100.));

			System.out.println ("Yield From TSY Spread: " +
				org.drip.quant.common.FormatUtil.FormatDouble (dblYieldFromTSYSpread, 2, 3, 100.));

			System.out.println ("Z Spread From TSY Spread: " + (int) (10000. * dblZSpreadFromTSYSpread));

			System.out.println ("I Spread From TSY Spread: " + (int) (10000. * dblISpreadFromTSYSpread));

			System.out.println ("Discount Margin From TSY Spread: " + (int) (10000. *
				dblDiscountMarginFromTSYSpread));

			System.out.println ("G Spread From TSY Spread: " + (int) (10000. * dblGSpreadFromTSYSpread));

			System.out.println ("Par ASW From TSY Spread: " + (int) dblParASWFromTSYSpread);

			System.out.println ("Credit Basis From TSY Spread: " + (int) (10000. *
				dblCreditBasisFromTSYSpread));

			System.out.println ("Credit Risky Price: " + org.drip.quant.common.FormatUtil.FormatDouble
				(dblBondCreditPrice, 2, 3, 100.));

			System.out.println ("Valuation Date: " + org.drip.analytics.date.DateUtil.Today());

			System.out.println ("Effective Date: " + dtEffective);

			System.out.println ("Maturity Date: " + dtMaturity);

			System.out.println ("Is Val Date in the first period? " + bInFirstPeriod);

			System.out.println ("Is Val Date in the last period? " + bInLastPeriod);

			System.out.println ("Previous Coupon Date: " + dtPreviousCoupon);

			System.out.println ("Current Coupon Date: " + dtCurrentCoupon);

			System.out.println ("Next Coupon Date: " + dtNextCoupon);

			try {
				System.out.println ("Next Exercise Date: " + new org.drip.analytics.date.JulianDate
					(nei.date()));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			System.out.println ("Next Exercise Factor: " + nei.factor());

			System.out.println ("Next Exercise Type: " +
				org.drip.analytics.support.AnalyticsHelper.WorkoutTypeToString (nei.workoutType()));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the custom bond API
	 */

	public static final void CustomBondAPISample()
	{
		org.drip.product.definition.Bond[] aBond = new org.drip.product.definition.Bond[3];

		if (null == (aBond[0] = org.drip.service.api.CreditAnalytics.GetBond ("CustomFixed")))
			org.drip.service.api.CreditAnalytics.PutBond ("CustomFixed", aBond[0] = CreateCustomBond
				("CustomFixed", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FIXED));

		if (null == (aBond[1] = org.drip.service.api.CreditAnalytics.GetBond ("CustomFRN")))
			org.drip.service.api.CreditAnalytics.PutBond ("CustomFRN", aBond[1] = CreateCustomBond
				("CustomFRN", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FLOATER));

		if (null == (aBond[2] = org.drip.service.api.CreditAnalytics.GetBond ("CustomBondFromCF")))
			org.drip.service.api.CreditAnalytics.PutBond ("CustomBondFromCF", aBond[2] = CreateCustomBond
				("CustomBondFromCF", org.drip.product.creator.BondBuilder.BOND_TYPE_SIMPLE_FROM_CF));

		org.drip.analytics.rates.DiscountCurve dc =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "USD", null, 0.04);

		org.drip.analytics.rates.DiscountCurve dcTSY =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "USD", null, 0.03);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.state.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.DateUtil.Today().julian(), "CC", "USD", 0.01, 0.4);

		for (int i = 0; i < aBond.length; ++i) {
			if (s_bCustomBondCouponCFDisplay) {
				System.out.println
					("\nAcc Start     Acc End     Pay Date      Cpn DCF       Pay01       Surv01");

				System.out.println
					("---------    ---------    ---------    ---------    ---------    --------");

				for (org.drip.analytics.cashflow.CompositePeriod p : aBond[i].couponPeriods()) {
					try {
						System.out.println (org.drip.analytics.date.DateUtil.FromJulian (p.startDate()) +
							"    " + org.drip.analytics.date.DateUtil.FromJulian (p.endDate()) + "    " +
								org.drip.analytics.date.DateUtil.FromJulian (p.payDate()) + "    " +
									org.drip.quant.common.FormatUtil.FormatDouble (p.couponDCF(), 1, 4, 1.) +
										"    " + org.drip.quant.common.FormatUtil.FormatDouble (dc.df
											(p.payDate()), 1, 4, 1.) + "    " +
												org.drip.quant.common.FormatUtil.FormatDouble (cc.survival
													(p.payDate()), 1, 4, 1.));
					} catch (java.lang.Exception e) {
						if (s_bSupressErrMsg) {
							System.out.println ("CustomAPISample failed.");

							return;
						}

						e.printStackTrace();
					}
				}
			}

			org.drip.param.market.CurveSurfaceQuoteSet mktParams =
				org.drip.param.creator.MarketParamsBuilder.Create (dc, dcTSY, cc, null, null, null,
					org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (aBond[i],
						org.drip.analytics.date.DateUtil.Today(), 0.04));

			if (s_bCustomBondAnalDisplay) {
				try {
					System.out.println ("\nPrice From Yield: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].priceFromYield
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, 0.), 2, 3, 100.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CustomAPISample failed.");

						return;
					}

					e.printStackTrace();
				}

				org.drip.param.valuation.WorkoutInfo wi = aBond[i].exerciseYieldFromPrice
					(org.drip.param.valuation.ValuationParams.Spot (org.drip.analytics.date.DateUtil.Today(),
						0, "", org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams, null,
							1.);

				System.out.println ("Workout Date: " + org.drip.analytics.date.DateUtil.FromJulian
					(wi.date()));

				System.out.println ("Workout Factor: " + wi.factor());

				System.out.println ("Workout Yield: " + org.drip.quant.common.FormatUtil.FormatDouble
					(wi.yield(), 2, 3, 100.));

				try {
					System.out.println ("Workout Yield From Price: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].yieldFromPrice
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, wi.date(), wi.factor(), 1.), 2, 3, 100.));

					try {
						System.out.println ("Z Spread From Price: " +
							org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].zspreadFromPrice
								(org.drip.param.valuation.ValuationParams.Spot
									(org.drip.analytics.date.DateUtil.Today(), 0, "",
										org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
											null, wi.date(), wi.factor(), 1.), 1, 3, 100.));
					} catch (java.lang.Exception e) {
						e.printStackTrace();
					}

					System.out.println ("TSY Spread From Price: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].tsySpreadFromPrice
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams, null,
										wi.date(), wi.factor(), 1.), 1, 3, 100.));

					System.out.println ("Credit Basis From Price: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].creditBasisFromPrice
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, wi.date(), wi.factor(), 1.), 1, 3, 100.));

					System.out.println ("PECS From Price: " +
						org.drip.quant.common.FormatUtil.FormatDouble
							(aBond[i].pecsFromPrice (org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, wi.date(), wi.factor(), 1.), 1, 3, 100.));

					System.out.println ("Price From TSY Spread: " +
						org.drip.quant.common.FormatUtil.FormatDouble
							(aBond[i].priceFromTSYSpreadToOptimalExercise
								(org.drip.param.valuation.ValuationParams.Spot
									(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
										org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
											null, 0.0188), 2, 3, 100.));

					System.out.println ("Yield From TSY Spread: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].yieldFromTSYSpread
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, 0.0188), 2, 3, 100.));

					System.out.println ("Credit Basis From TSY Spread: " +
						org.drip.quant.common.FormatUtil.FormatDouble
							(aBond[i].creditBasisFromTSYSpread (org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, 0.0188), 1, 3, 100.));

					System.out.println ("PECS From TSY Spread: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].pecsFromTSYSpread
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, 0.0188), 1, 3, 100.));

					System.out.println ("Theoretical Price: " +
						org.drip.quant.common.FormatUtil.FormatDouble (aBond[i].priceFromCreditBasis
							(org.drip.param.valuation.ValuationParams.Spot
								(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
									org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), mktParams,
										null, wi.date(), wi.factor(), 0.), 2, 3, 100.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("CustomAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Sample demonstrating the calculation of analytics for the set of bonds associated with the ticker
	 */

	public static final void BondTickerAPISample()
	{
		int iNumBonds = 0;
		java.lang.String strTicker = "GE";

		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.DateUtil.Today();

		org.drip.analytics.rates.DiscountCurve dc =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", null, 0.05);

		org.drip.analytics.rates.DiscountCurve dcTSY =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD", null, 0.04);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.state.creator.CreditCurveBuilder.FromFlatHazard (dtToday.julian(), "CC", "USD", 0.02,
				0.4);

		java.util.List<java.lang.String> lsstrISIN = org.drip.service.api.CreditAnalytics.GetISINsForTicker
			(strTicker);

		if (s_bTickerAnalDisplay) {
			System.out.println
				("Dumping: ISIN, Bond, FIX/FLT, Yield, Z Spread, Disc Margin, TSY Spread, Credit Basis, PECS, Credit Price");

			System.out.println
				("--------------------------------------------------------------------------------------------------------");
		}

		for (java.lang.String strISIN : lsstrISIN) {
			org.drip.product.definition.Bond bond = org.drip.service.api.CreditAnalytics.GetBond (strISIN);

			if (null != bond && !bond.variableCoupon() && !bond.exercised() && !bond.defaulted() &&
				bond.maturityDate().julian() > dtToday.julian()) {
				try {
					++iNumBonds;
					double dblPECSFromPrice = java.lang.Double.NaN;
					double dblZSpreadFromPrice = java.lang.Double.NaN;
					double dblCreditBasisFromPrice = java.lang.Double.NaN;

					double dblYieldFromPrice = org.drip.service.api.CreditAnalytics.BondYieldFromPrice
						(strISIN, dtToday, dc, 1.);

					if (!org.drip.service.api.CreditAnalytics.IsBondFloater (strISIN))
						dblZSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondZSpreadFromPrice
							(strISIN, dtToday, dc, 1.);

					double dblDiscountMarginFromPrice =
						org.drip.service.api.CreditAnalytics.BondDiscountMarginFromPrice (strISIN, dtToday,
							dc, 1.);

					double dblTSYSpreadFromPrice =
						org.drip.service.api.CreditAnalytics.BondTSYSpreadFromPrice (strISIN, dtToday, dc,
							dcTSY, 1.);

					try {
						dblCreditBasisFromPrice =
							org.drip.service.api.CreditAnalytics.BondCreditBasisFromPrice (strISIN, dtToday,
								dc, cc, 1.);
					} catch (java.lang.Exception e) {
					}

					try {
						dblPECSFromPrice = org.drip.service.api.CreditAnalytics.BondPECSFromPrice (strISIN,
							dtToday, dc, cc, 1.);
					} catch (java.lang.Exception e) {
					}

					double dblBondCreditPrice = org.drip.service.api.CreditAnalytics.BondCreditPrice
						(strISIN, dtToday, dc, cc);

					if (s_bTickerAnalDisplay)
						System.out.println (strISIN + "    " + bond.ticker() + " " +
							org.drip.quant.common.FormatUtil.FormatDouble (bond.couponMetrics
								(org.drip.analytics.date.DateUtil.Today().julian(), null,
									org.drip.param.creator.MarketParamsBuilder.Credit (dc, cc)).rate(), 2, 3,
										100.) + " " + bond.maturityDate() + "    " + (bond.isFloater() ?
											"FLOAT" : "FIXED") + "     " +
												org.drip.quant.common.FormatUtil.FormatDouble
													(dblYieldFromPrice, 2, 3, 100.) + "    " +
														org.drip.quant.common.FormatUtil.FormatDouble
															(dblZSpreadFromPrice, 1, 3, 100.) + "    " +
																org.drip.quant.common.FormatUtil.FormatDouble
							(dblDiscountMarginFromPrice, 1, 3, 100.) + "    " +
								org.drip.quant.common.FormatUtil.FormatDouble (dblTSYSpreadFromPrice, 1, 3,
									100.) + "    " + org.drip.quant.common.FormatUtil.FormatDouble
										(dblCreditBasisFromPrice, 1, 3, 100.) + "    " + (dblPECSFromPrice) +
											"    " + org.drip.quant.common.FormatUtil.FormatDouble
												(dblBondCreditPrice, 2, 3, 100.));
				} catch (java.lang.Exception e) {
					if (s_bSupressErrMsg) {
						System.out.println ("BondTickerAPISample failed.");

						return;
					}

					e.printStackTrace();
				}
			}
		}

		System.out.println ("Processed " + iNumBonds + " " + strTicker + " bonds!");

		for (java.lang.String strISIN : lsstrISIN) {
			org.drip.product.definition.Bond bond = org.drip.service.api.CreditAnalytics.GetBond (strISIN);

			try {
				double dblOutstandingAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField
					(strISIN, "OutstandingAmount");

				if (s_bTickerNotionalDisplay)
					System.out.println (strISIN + "    " + bond.ticker() + " " +
						org.drip.quant.common.FormatUtil.FormatDouble (bond.couponMetrics
							(org.drip.analytics.date.DateUtil.Today().julian(), null,
								org.drip.param.creator.MarketParamsBuilder.Credit (dc, cc)).rate(), 2, 3,
									100.) + " " + bond.maturityDate() + "    " +
										org.drip.quant.common.FormatUtil.FormatDouble (dblOutstandingAmount,
											10, 0, 1.));
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondTickerAPISample failed.");

					return;
				}

				e.printStackTrace();
			}
		}

		org.drip.analytics.date.JulianDate[] adtAscending = new org.drip.analytics.date.JulianDate[5];

		adtAscending[0] = org.drip.analytics.date.DateUtil.Today().addYears (3);

		adtAscending[1] = org.drip.analytics.date.DateUtil.Today().addYears (5);

		adtAscending[2] = org.drip.analytics.date.DateUtil.Today().addYears (10);

		adtAscending[3] = org.drip.analytics.date.DateUtil.Today().addYears (30);

		adtAscending[4] = org.drip.analytics.date.DateUtil.Today().addYears (60);

		java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> mapOutstandingNotional =
			org.drip.service.api.CreditAnalytics.GetIssuerAggregateOutstandingNotional
				(org.drip.analytics.date.DateUtil.Today(), strTicker, adtAscending);

		if (s_bCumulativeTickerNotionalDisplay) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate, java.lang.Double> me :
				mapOutstandingNotional.entrySet())
				System.out.println ("[" + org.drip.analytics.date.DateUtil.Today() + "=>" + me.getKey() +
					"] = " + me.getValue());
		}
	}

	/**
	 * Sample demonstrating the calculation of the bond's EOD measures from price
	 */

	public static final void BondEODMeasuresAPISample()
	{
		java.lang.String strISIN = "008686AA5"; // Amortizer
		double dblEODPrice = 1.0;
		double dblEODConvexityFromPrice = java.lang.Double.NaN;
		double dblEODCreditBasisFromPrice = java.lang.Double.NaN;
		double dblEODPECSFromPrice = java.lang.Double.NaN;
		double dblEODDiscountMarginFromPrice = java.lang.Double.NaN;
		double dblEODDurationFromPrice = java.lang.Double.NaN;
		double dblEODGSpreadFromPrice = java.lang.Double.NaN;
		double dblEODISpreadFromPrice = java.lang.Double.NaN;
		double dblEODOASFromPrice = java.lang.Double.NaN;
		double dblEODParASWFromPrice = java.lang.Double.NaN;
		double dblEODTSYSpreadFromPrice = java.lang.Double.NaN;
		double dblEODYieldFromPrice = java.lang.Double.NaN;
		double dblEODZSpreadFromPrice = java.lang.Double.NaN;

		org.drip.analytics.date.JulianDate dtEOD = org.drip.analytics.date.DateUtil.CreateFromYMD (2011,
			12, 16);

		System.out.println ("Price measures for " + org.drip.service.api.CreditAnalytics.GetBondStringField
			(strISIN, "Description"));

		try {
			dblEODConvexityFromPrice = org.drip.service.api.CreditAnalytics.BondEODConvexityFromPrice
				(strISIN, dtEOD, dblEODPrice);

			try {
				dblEODCreditBasisFromPrice = org.drip.service.api.CreditAnalytics.BondEODCreditBasisFromPrice
					(strISIN, dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			try {
				dblEODPECSFromPrice = org.drip.service.api.CreditAnalytics.BondEODPECSFromPrice (strISIN,
					dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblEODDiscountMarginFromPrice =
				org.drip.service.api.CreditAnalytics.BondEODDiscountMarginFromPrice (strISIN, dtEOD,
					dblEODPrice);

			dblEODDurationFromPrice = org.drip.service.api.CreditAnalytics.BondEODDurationFromPrice (strISIN,
				dtEOD, dblEODPrice);

			try {
				dblEODGSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODGSpreadFromPrice
					(strISIN, dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblEODISpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODISpreadFromPrice (strISIN,
				dtEOD, dblEODPrice);

			dblEODOASFromPrice = org.drip.service.api.CreditAnalytics.BondEODOASFromPrice (strISIN, dtEOD,
				dblEODPrice);

			try {
				dblEODTSYSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODTSYSpreadFromPrice
					(strISIN, dtEOD, dblEODPrice);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			dblEODYieldFromPrice = org.drip.service.api.CreditAnalytics.BondEODYieldFromPrice (strISIN,
				org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 12, 16), dblEODPrice);

			dblEODZSpreadFromPrice = org.drip.service.api.CreditAnalytics.BondEODZSpreadFromPrice (strISIN,
				dtEOD, dblEODPrice);

			if (s_bBondEODMeasuresFromPrice) {
				System.out.println ("EOD Convexity From Price: " + dblEODConvexityFromPrice);

				System.out.println ("EOD Credit Basis From Price: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODCreditBasisFromPrice, 1, 3, 100.));

				System.out.println ("EOD PECS From Price: " + org.drip.quant.common.FormatUtil.FormatDouble
					(dblEODPECSFromPrice, 1, 3, 100.));

				System.out.println ("EOD Discount Margin From Price: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODDiscountMarginFromPrice, 1, 3,
						100.));

				System.out.println ("EOD Duration From Price: " + dblEODDurationFromPrice);

				System.out.println ("EOD G Spread From Price: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODGSpreadFromPrice, 1, 3, 100.));

				System.out.println ("EOD I Spread From Price: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODISpreadFromPrice, 1, 3, 100.));

				System.out.println ("EOD OAS From Price: " + org.drip.quant.common.FormatUtil.FormatDouble
					(dblEODOASFromPrice, 1, 3, 100.));

				System.out.println ("EOD Par ASW From Price: " + (int) dblEODParASWFromPrice);

				System.out.println ("EOD TSY Spread From Price: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODTSYSpreadFromPrice, 1, 3, 100.));

				System.out.println ("EOD Yield From Price: " + org.drip.quant.common.FormatUtil.FormatDouble
					(dblEODYieldFromPrice, 1, 3, 100.));

				System.out.println ("EOD Z Spread From Price: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODZSpreadFromPrice, 1, 3, 100.));
			}
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondEODMeasuresAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		double dblEODYield = 0.0782;
		double dblEODConvexityFromYield = java.lang.Double.NaN;
		double dblEODCreditBasisFromYield = java.lang.Double.NaN;
		double dblEODPECSFromYield = java.lang.Double.NaN;
		double dblEODDiscountMarginFromYield = java.lang.Double.NaN;
		double dblEODDurationFromYield = java.lang.Double.NaN;
		double dblEODGSpreadFromYield = java.lang.Double.NaN;
		double dblEODISpreadFromYield = java.lang.Double.NaN;
		double dblEODOASFromYield = java.lang.Double.NaN;
		double dblEODParASWFromYield = java.lang.Double.NaN;
		double dblEODPriceFromYield = java.lang.Double.NaN;
		double dblEODTSYSpreadFromYield = java.lang.Double.NaN;
		double dblEODZSpreadFromYield = java.lang.Double.NaN;

		System.out.println ("\nYield measures for " + org.drip.service.api.CreditAnalytics.GetBondStringField
			(strISIN, "Description"));

		try {
			dblEODConvexityFromYield = org.drip.service.api.CreditAnalytics.BondEODConvexityFromYield
				(strISIN, dtEOD, dblEODYield);

			try {
				dblEODCreditBasisFromYield = org.drip.service.api.CreditAnalytics.BondEODCreditBasisFromYield
					(strISIN, dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			try {
				dblEODPECSFromYield = org.drip.service.api.CreditAnalytics.BondEODPECSFromYield (strISIN,
					dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODDiscountMarginFromYield =
				org.drip.service.api.CreditAnalytics.BondEODDiscountMarginFromYield (strISIN, dtEOD,
					dblEODYield);

			dblEODDurationFromYield = org.drip.service.api.CreditAnalytics.BondEODDurationFromYield (strISIN,
				dtEOD, dblEODYield);

			try {
				dblEODGSpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODGSpreadFromYield
					(strISIN, dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODISpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODISpreadFromYield (strISIN,
				dtEOD, dblEODYield);

			dblEODOASFromYield = org.drip.service.api.CreditAnalytics.BondEODOASFromYield (strISIN, dtEOD,
				dblEODYield);

			dblEODPriceFromYield = org.drip.service.api.CreditAnalytics.BondEODPriceFromYield (strISIN,
				dtEOD, dblEODYield);

			try {
				dblEODTSYSpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODTSYSpreadFromYield
					(strISIN, dtEOD, dblEODYield);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODZSpreadFromYield = org.drip.service.api.CreditAnalytics.BondEODZSpreadFromYield (strISIN,
				dtEOD, dblEODYield);

			if (s_bBondEODMeasuresFromYield) {
				System.out.println ("EOD Convexity From Yield: " + dblEODConvexityFromYield);

				System.out.println ("EOD Credit Basis From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODCreditBasisFromYield, 1, 3, 100.));

				System.out.println ("EOD PECS From Yield: " + org.drip.quant.common.FormatUtil.FormatDouble
					(dblEODPECSFromYield, 1, 3, 100.));

				System.out.println ("EOD Discount Margin From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODDiscountMarginFromYield, 1, 3,
						100.));

				System.out.println ("EOD Duration From Yield: " + dblEODDurationFromYield);

				System.out.println ("EOD G Spread From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODGSpreadFromYield, 1, 3, 100.));

				System.out.println ("EOD I Spread From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODISpreadFromYield, 1, 3, 100.));

				System.out.println ("EOD OAS From Yield: " + org.drip.quant.common.FormatUtil.FormatDouble
					(dblEODOASFromYield, 1, 3, 100.));

				System.out.println ("EOD Par ASW From Yield: " + (int) dblEODParASWFromYield);

				System.out.println ("EOD Price From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODPriceFromYield, 2, 3, 100.));

				System.out.println ("EOD TSY Spread From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODTSYSpreadFromYield, 1, 3, 100.));

				System.out.println ("EOD Z Spread From Yield: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODZSpreadFromYield, 1, 3, 100.));
			}
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondEODMeasuresAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		double dblEODTSYSpread = 0.0618;
		double dblEODConvexityFromTSYSpread = java.lang.Double.NaN;
		double dblEODCreditBasisFromTSYSpread = java.lang.Double.NaN;
		double dblEODPECSFromTSYSpread = java.lang.Double.NaN;
		double dblEODDiscountMarginFromTSYSpread = java.lang.Double.NaN;
		double dblEODDurationFromTSYSpread = java.lang.Double.NaN;
		double dblEODGSpreadFromTSYSpread = java.lang.Double.NaN;
		double dblEODISpreadFromTSYSpread = java.lang.Double.NaN;
		double dblEODOASFromTSYSpread = java.lang.Double.NaN;
		double dblEODParASWFromTSYSpread = java.lang.Double.NaN;
		double dblEODPriceFromTSYSpread = java.lang.Double.NaN;
		double dblEODYieldFromTSYSpread = java.lang.Double.NaN;
		double dblEODZSpreadFromTSYSpread = java.lang.Double.NaN;

		System.out.println ("\nTSY Spread measures for " +
			org.drip.service.api.CreditAnalytics.GetBondStringField (strISIN, "Description"));

		try {
			dblEODConvexityFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODConvexityFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			try {
				dblEODCreditBasisFromTSYSpread =
					org.drip.service.api.CreditAnalytics.BondEODCreditBasisFromTSYSpread (strISIN, dtEOD,
						dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			try {
				dblEODPECSFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODPECSFromTSYSpread
					(strISIN, dtEOD, dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODDiscountMarginFromTSYSpread =
				org.drip.service.api.CreditAnalytics.BondEODDiscountMarginFromTSYSpread (strISIN, dtEOD,
					dblEODTSYSpread);

			dblEODDurationFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODDurationFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			try {
				dblEODGSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODGSpreadFromTSYSpread
					(strISIN, dtEOD, dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODISpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODISpreadFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			dblEODOASFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODOASFromTSYSpread (strISIN,
				dtEOD, dblEODTSYSpread);

			dblEODPriceFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODPriceFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			try {
				dblEODYieldFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODYieldFromTSYSpread
					(strISIN, dtEOD, dblEODTSYSpread);
			} catch (java.lang.Exception e) {
				if (s_bSupressErrMsg) {
					System.out.println ("BondEODMeasuresAPISample failed.");

					return;
				}

				e.printStackTrace();
			}

			dblEODZSpreadFromTSYSpread = org.drip.service.api.CreditAnalytics.BondEODZSpreadFromTSYSpread
				(strISIN, dtEOD, dblEODTSYSpread);

			if (s_bBondEODMeasuresFromTSYSpread) {
				System.out.println ("EOD Convexity From TSY Spread: " + dblEODConvexityFromTSYSpread);

				System.out.println ("EOD Credit Basis From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODCreditBasisFromTSYSpread, 1, 3,
						100.));

				System.out.println ("EOD PECS From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODPECSFromTSYSpread, 1, 3, 100.));

				System.out.println ("EOD Discount Margin From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODDiscountMarginFromTSYSpread, 1, 3,
						100.));

				System.out.println ("EOD Duration From TSY Spread: " + dblEODDurationFromTSYSpread);

				System.out.println ("EOD G Spread From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODGSpreadFromTSYSpread, 1, 3, 100.));

				System.out.println ("EOD I Spread From TSYSpread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODISpreadFromTSYSpread, 1, 3, 100.));

				System.out.println ("EOD OAS From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODOASFromTSYSpread, 1, 3, 100.));

				System.out.println ("EOD Par ASW From TSY Spread: " + (int) dblEODParASWFromTSYSpread);

				System.out.println ("EOD Price From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODPriceFromTSYSpread, 1, 3, 100.));

				System.out.println ("EOD Yield From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODYieldFromTSYSpread, 1, 3, 100.));

				System.out.println ("EOD Z Spread From TSY Spread: " +
					org.drip.quant.common.FormatUtil.FormatDouble (dblEODZSpreadFromTSYSpread, 1, 3, 100.));
			}
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondEODMeasuresAPISample failed.");

				return;
			}

			e.printStackTrace();
		}
	}

	/**
	 * Sample demonstrating the calculation of the CDS EOD measures from price
	 */

	public static final void CDSEODMeasuresAPISample()
	{
		org.drip.analytics.date.JulianDate dtEOD = org.drip.analytics.date.DateUtil.CreateFromYMD (2011, 7,
			21);

		org.drip.product.definition.CreditDefaultSwap cds = org.drip.product.creator.CDSBuilder.CreateSNAC
			(org.drip.analytics.date.DateUtil.Today(), "5Y", 0.1, "813796");

		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapEODCDSMeasures =
			org.drip.service.api.CreditAnalytics.GetEODCDSMeasures (cds, dtEOD);

		if (s_bEODCDSMeasures) {
			for (java.util.Map.Entry<java.lang.String, java.lang.Double> me : mapEODCDSMeasures.entrySet())
				System.out.println (me.getKey() + " => " + me.getValue());
		}
	}

	/**
	 * Sample demonstrating the retrieval of the bond's static fields
	 */

	public static final void BondStaticAPISample()
	{
		java.lang.String strBondISIN = "US001383CA43";
		boolean bIsBearer = true;
		boolean bIsCallable = true;
		boolean bDefaulted = true;
		boolean bExercised = true;
		boolean bIsFloater = true;
		boolean bIsPerpetual = true;
		boolean bIsPrivatePlacement = false;
		boolean bIsPutable = true;
		boolean bIsRegistered = false;
		boolean bIsReverseConvertible = true;
		boolean bIsSinkable = true;
		boolean bIsStructuredNote = true;
		boolean bIsTradeStatus = false;
		boolean bIsUnitTraded = false;
		double dblCoupon = java.lang.Double.NaN;
		double dblCurrentCoupon = java.lang.Double.NaN;
		double dblFloatSpread = java.lang.Double.NaN;
		double dblIssueAmount = java.lang.Double.NaN;
		double dblIssuePrice = java.lang.Double.NaN;
		double dblMinimumIncrement = java.lang.Double.NaN;
		double dblMinimumPiece = java.lang.Double.NaN;
		double dblOutstandingAmount = java.lang.Double.NaN;
		double dblParAmount = java.lang.Double.NaN;
		double dblRedemptionValue = java.lang.Double.NaN;
		int iCouponFrequency = 1;

		java.lang.String strAccrualDC = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"AccrualDC");

		java.lang.String strBBG_ID = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"BBG_ID");

		java.lang.String strBBGParent = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"BBGParent");

		java.lang.String strBBGUniqueID = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "BBGUniqueID");

		java.lang.String strCalculationType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CalculationType");

		java.lang.String strCDRCountryCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CDRCountryCode");

		java.lang.String strCDRSettleCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CDRSettleCode");

		java.lang.String strCollateralType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CollateralType");

		java.lang.String strCountryOfDomicile = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CountryOfDomicile");

		java.lang.String strCountryOfGuarantor = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CountryOfGuarantor");

		java.lang.String strCountryOfIncorporation = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CountryOfIncorporation");

		java.lang.String strCouponCurrency = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CouponCurrency");

		java.lang.String strCouponDC = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"CouponDC");

		java.lang.String strCouponType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CouponType");

		java.lang.String strCreditCurve = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "CreditCurve");

		java.lang.String strCUSIP = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"CUSIP");

		java.lang.String strDescription = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "Description");

		java.lang.String strExchangeCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "ExchangeCode");

		java.lang.String strFitch = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Fitch");

		java.lang.String strFloatCouponConvention = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "FloatCouponConvention");

		java.lang.String strIndustryGroup = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IndustryGroup");

		java.lang.String strIndustrySector = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IndustrySector");

		java.lang.String strIndustrySubgroup = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IndustrySubgroup");

		java.lang.String strIRCurve = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"IRCurve");

		java.lang.String strISIN = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"ISIN");

		java.lang.String strIssuer = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Issuer");

		java.lang.String strIssuerCategory = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerCategory");

		java.lang.String strIssuerCountry = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerCountry");

		java.lang.String strIssuerCountryCode = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerCountryCode");

		java.lang.String strIssuerIndustry = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "IssuerIndustry");

		java.lang.String strLeadManager = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "LeadManager");

		java.lang.String strLongCompanyName = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "LongCompanyName");

		java.lang.String strMarketIssueType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "MarketIssueType");

		java.lang.String strMaturityType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "MaturityType");

		java.lang.String strMoody = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Moody");

		java.lang.String strName = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Name");

		java.lang.String strRateIndex = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"RateIndex");

		java.lang.String strRedemptionCurrency = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "RedemptionCurrency");

		java.lang.String strSecurityType = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "SecurityType");

		java.lang.String strSeniorSub = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"SeniorSub");

		java.lang.String strSeries = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Series");

		java.lang.String strSnP = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"SnP");

		java.lang.String strShortName = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"ShortName");

		java.lang.String strTicker = org.drip.service.api.CreditAnalytics.GetBondStringField (strBondISIN,
			"Ticker");

		java.lang.String strTradeCurrency = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "TradeCurrency");

		java.lang.String strTreasuryCurve = org.drip.service.api.CreditAnalytics.GetBondStringField
			(strBondISIN, "TreasuryCurve");

		try {
			bIsBearer = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Bearer");

			bIsCallable = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Callable");

			bDefaulted = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Defaulted");

			bExercised = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Exercised");

			bIsFloater = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Floater");

			bIsPerpetual = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"Perpetual");

			bIsPrivatePlacement = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"PrivatePlacement");

			bIsPutable = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Putable");

			bIsRegistered = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"Registered");

			bIsReverseConvertible = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"ReverseConvertible");

			bIsSinkable = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN, "Sinkable");

			bIsStructuredNote = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"StructuredNote");

			bIsTradeStatus = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"TradeStatus");

			bIsUnitTraded = org.drip.service.api.CreditAnalytics.GetBondBooleanField (strBondISIN,
				"UnitTraded");

			dblCoupon = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN, "Coupon");

			dblCurrentCoupon = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"CurrentCoupon");

			dblFloatSpread = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"FloatSpread");

			dblIssueAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"IssueAmount");

			dblIssuePrice = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"IssuePrice");

			dblMinimumIncrement = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"MinimumIncrement");

			dblMinimumPiece = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"MinimumPiece");

			dblOutstandingAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"OutstandingAmount");

			dblParAmount = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"ParAmount");

			dblRedemptionValue = org.drip.service.api.CreditAnalytics.GetBondDoubleField (strBondISIN,
				"RedemptionValue");

			iCouponFrequency = org.drip.service.api.CreditAnalytics.GetBondIntegerField (strBondISIN,
				"Frequency");
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondStaticAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.analytics.date.JulianDate dtAccrualStart =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "AccrualStartDate");

		org.drip.analytics.date.JulianDate dtAnnounce = org.drip.service.api.CreditAnalytics.GetBondDateField
			(strBondISIN, "AnnounceDate");

		org.drip.analytics.date.JulianDate dtFirstCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "FirstCouponDate");

		org.drip.analytics.date.JulianDate dtFirstSettle =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "FirstSettleDate");

		org.drip.analytics.date.JulianDate dtFinalMaturity =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "FinalMaturity");

		org.drip.analytics.date.JulianDate dtIssue = org.drip.service.api.CreditAnalytics.GetBondDateField
			(strBondISIN, "IssueDate");

		org.drip.analytics.date.JulianDate dtMaturity = org.drip.service.api.CreditAnalytics.GetBondDateField
			(strBondISIN, "Maturity");

		org.drip.analytics.date.JulianDate dtNextCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "NextCouponDate");

		org.drip.analytics.date.JulianDate dtPenultimateCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "PenultimateCouponDate");

		org.drip.analytics.date.JulianDate dtPreviousCoupon =
			org.drip.service.api.CreditAnalytics.GetBondDateField (strBondISIN, "PreviousCouponDate");

		if (s_bStaticDisplay) {
			System.out.println ("AccrualDC: " + strAccrualDC);

			System.out.println ("BBG_ID: " + strBBG_ID);

			System.out.println ("BBGParent: " + strBBGParent);

			System.out.println ("BBGUniqueID: " + strBBGUniqueID);

			System.out.println ("CalculationType: " + strCalculationType);

			System.out.println ("CDRCountryCode: " + strCDRCountryCode);

			System.out.println ("CDRSettleCode: " + strCDRSettleCode);

			System.out.println ("CollateralType: " + strCollateralType);

			System.out.println ("CountryOfDomicile: " + strCountryOfDomicile);

			System.out.println ("CountryOfGuarantor: " + strCountryOfGuarantor);

			System.out.println ("CountryOfIncorporation: " + strCountryOfIncorporation);

			System.out.println ("CouponCurrency: " + strCouponCurrency);

			System.out.println ("CouponDC: " + strCouponDC);

			System.out.println ("CouponType: " + strCouponType);

			System.out.println ("CreditCurve: " + strCreditCurve);

			System.out.println ("CUSIP: " + strCUSIP);

			System.out.println ("Description: " + strDescription);

			System.out.println ("ExchangeCode: " + strExchangeCode);

			System.out.println ("Fitch: " + strFitch);

			System.out.println ("FloatCouponConvention: " + strFloatCouponConvention);

			System.out.println ("IndustryGroup: " + strIndustryGroup);

			System.out.println ("IndustrySector: " + strIndustrySector);

			System.out.println ("IndustrySubgroup: " + strIndustrySubgroup);

			System.out.println ("IRCurve: " + strIRCurve);

			System.out.println ("ISIN: " + strISIN);

			System.out.println ("Issuer: " + strIssuer);

			System.out.println ("IssuerCategory: " + strIssuerCategory);

			System.out.println ("IssuerCountry: " + strIssuerCountry);

			System.out.println ("IssuerCountryCode: " + strIssuerCountryCode);

			System.out.println ("IssuerIndustry: " + strIssuerIndustry);

			System.out.println ("LeadManager: " + strLeadManager);

			System.out.println ("LongCompanyName: " + strLongCompanyName);

			System.out.println ("MarketIssueType: " + strMarketIssueType);

			System.out.println ("MaturityType: " + strMaturityType);

			System.out.println ("Moody: " + strMoody);

			System.out.println ("Name: " + strName);

			System.out.println ("RateIndex: " + strRateIndex);

			System.out.println ("RedemptionCurrency: " + strRedemptionCurrency);

			System.out.println ("SecurityType: " + strSecurityType);

			System.out.println ("Series: " + strSeries);

			System.out.println ("SeniorSub: " + strSeniorSub);

			System.out.println ("ShortName: " + strShortName);

			System.out.println ("SnP: " + strSnP);

			System.out.println ("Ticker: " + strTicker);

			System.out.println ("TradeCurrency: " + strTradeCurrency);

			System.out.println ("TreasuryCurve: " + strTreasuryCurve);

			System.out.println ("IsBearer: " + bIsBearer);

			System.out.println ("IsCallable: " + bIsCallable);

			System.out.println ("IsDefaulted: " + bDefaulted);

			System.out.println ("IsExercised: " + bExercised);

			System.out.println ("IsFloater: " + bIsFloater);

			System.out.println ("IsPrivatePlacement: " + bIsPrivatePlacement);

			System.out.println ("IsPerpetual: " + bIsPerpetual);

			System.out.println ("IsPutable: " + bIsPutable);

			System.out.println ("IsRegistered: " + bIsRegistered);

			System.out.println ("IsReverseConvertible: " + bIsReverseConvertible);

			System.out.println ("IsSinkable: " + bIsSinkable);

			System.out.println ("IsStructuredNote: " + bIsStructuredNote);

			System.out.println ("IsTradeStatus: " + bIsTradeStatus);

			System.out.println ("IsUnitTraded: " + bIsUnitTraded);

			System.out.println ("Coupon: " + org.drip.quant.common.FormatUtil.FormatDouble (dblCoupon, 2, 3,
				100.));

			System.out.println ("CurrentCoupon: " + org.drip.quant.common.FormatUtil.FormatDouble
				(dblCurrentCoupon, 2, 3, 100.));

			System.out.println ("FloatSpread: " + org.drip.quant.common.FormatUtil.FormatDouble
				(dblFloatSpread, 1, 3, 100.));

			System.out.println ("IssueAmount: " + dblIssueAmount);

			System.out.println ("IssuePrice: " + dblIssuePrice);

			System.out.println ("MinimumIncrement: " + dblMinimumIncrement);

			System.out.println ("MinimumPiece: " + dblMinimumPiece);

			System.out.println ("OutstandingAmount: " + dblOutstandingAmount);

			System.out.println ("ParAmount: " + dblParAmount);

			System.out.println ("RedemptionValue: " + dblRedemptionValue);

			System.out.println ("Coupon Freq: " + iCouponFrequency);

			System.out.println ("AccrualStart: " + dtAccrualStart);

			System.out.println ("Announce: " + dtAnnounce);

			System.out.println ("FinalMaturity: " + dtFinalMaturity);

			System.out.println ("FirstCoupon: " + dtFirstCoupon);

			System.out.println ("FirstSettle: " + dtFirstSettle);

			System.out.println ("Issue: " + dtIssue);

			System.out.println ("Maturity: " + dtMaturity);

			System.out.println ("NextCoupon: " + dtNextCoupon);

			System.out.println ("PenultimateCoupon: " + dtPenultimateCoupon);

			System.out.println ("PrevCoupon: " + dtPreviousCoupon);
		}
	}

	/**
	 * API demonstrating how to calibrate a CDS curve from CDS and bond quotes
	 */

	public static void BondCDSCurveCalibration()
	{
		double dblCreditPrice = java.lang.Double.NaN;

		org.drip.analytics.rates.DiscountCurve dc =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "DKK", null, 0.04);

		org.drip.analytics.definition.CreditCurve cc =
			org.drip.state.creator.CreditCurveBuilder.FromFlatHazard
				(org.drip.analytics.date.DateUtil.Today().julian(), "CC", "USD", 0.01, 0.4);

		org.drip.product.credit.BondComponent bond = org.drip.product.creator.BondBuilder.CreateSimpleFixed
			("CCCalibBond", "DKK", "CC", 0.05, 2, "30/360", org.drip.analytics.date.DateUtil.CreateFromYMD
				(2008, 9, 21), org.drip.analytics.date.DateUtil.CreateFromYMD (2023, 9, 20), null, null);

		org.drip.param.market.CurveSurfaceQuoteSet mktParams =
			org.drip.param.creator.MarketParamsBuilder.Create (dc, null, null, cc, null, null, null,
				org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (bond,
					org.drip.analytics.date.DateUtil.Today(), 0.04));

		org.drip.param.valuation.ValuationParams valParams = org.drip.param.valuation.ValuationParams.Spot
			(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
				org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL);

		try {
			dblCreditPrice = bond.priceFromCreditBasis (valParams, mktParams, null,
				bond.maturityDate().julian(), 1., 0.);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondCDSCurveCalibration failed.");

				return;
			}

			e.printStackTrace();
		}

		org.drip.product.definition.CreditDefaultSwap cds = org.drip.product.creator.CDSBuilder.CreateCDS
			(org.drip.analytics.date.DateUtil.Today(), org.drip.analytics.date.DateUtil.Today().addTenor
				("5Y"), 0.1, "DKK", 0.40, "CC", "DKK", true);

		if (s_bCDSBondCreditCurve) System.out.println ("Credit Price: " + dblCreditPrice);

		org.drip.product.definition.CalibratableFixedIncomeComponent[] aCalibInst = new
			org.drip.product.definition.CalibratableFixedIncomeComponent[2];
		java.lang.String[] astrCalibMeasure = new java.lang.String[2];
		double[] adblQuotes = new double[2];
		aCalibInst[0] = cds;
		aCalibInst[1] = bond;
		astrCalibMeasure[0] = "FairPremium";
		astrCalibMeasure[1] = "FairPrice";
		adblQuotes[0] = 100.;
		adblQuotes[1] = dblCreditPrice;
		org.drip.analytics.definition.CreditCurve ccCalib = null;

		try {
			org.drip.state.estimator.CreditCurveScenarioGenerator ccsg = new
				org.drip.state.estimator.CreditCurveScenarioGenerator (aCalibInst);

			ccCalib = ccsg.createCC ("CC", valParams, dc, null, adblQuotes, 0.40, astrCalibMeasure, null, new
				org.drip.param.valuation.ValuationCustomizationParams ("30/360", 2, true, null, "USD", false,
					null, null), false);

			if (s_bCDSBondCreditCurve)
				System.out.println ("Surv (2021, 1, 14): " + ccCalib.survival
					(org.drip.analytics.date.DateUtil.CreateFromYMD (2021, 1, 14)));
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BondCDSCurveCalibration failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bCDSBondCreditCurve) {
			try {
				System.out.println (cds.primaryCode() + " => " + cds.measureValue (valParams,
					org.drip.param.pricer.CreditPricerParams.Standard(),
						org.drip.param.creator.MarketParamsBuilder.Create (dc, null, null, ccCalib,
							null, null, null, org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject
								(bond, org.drip.analytics.date.DateUtil.Today(), 0.04)), null,
									"FairPremium"));

				System.out.println (bond.primaryCode() + " => " + bond.priceFromCreditBasis (valParams,
					org.drip.param.creator.MarketParamsBuilder.Create (dc, null, null, ccCalib, null, null,
						null, org.drip.analytics.support.AnalyticsHelper.CreateFixingsObject (bond,
							org.drip.analytics.date.DateUtil.Today(), 0.04)), null,
								bond.maturityDate().julian(), 1., 0.));
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the bond basket API
	 */

	public static final void BasketBondAPISample()
	{
		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.DateUtil.Today();

		org.drip.product.definition.BasketProduct bb = org.drip.service.api.CreditAnalytics.MakeBondBasket
			("SLMA_ETF", new java.lang.String[] {"US78490FVJ55", "US78490FWD76", "US78490FVL02",
				"US78442FAZ18", "US78490FTL30"}, new double[] {1., 2., 3., 4., 5.}, dtToday);

		org.drip.param.market.CurveSurfaceQuoteSet csqs = new org.drip.param.market.CurveSurfaceQuoteSet();

		csqs.setFundingCurve (org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate (dtToday, "USD",
			null, 0.04));

		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapResult = null;

		try {
			mapResult = bb.value (org.drip.param.valuation.ValuationParams.Spot (dtToday, 0, "USD",
				org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL), new
					org.drip.param.pricer.CreditPricerParams (7, null, false,
						org.drip.param.pricer.CreditPricerParams.PERIOD_DISCRETIZATION_FULL_COUPON), csqs,
							null);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("BasketBondAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bBasketBond) {
			System.out.println ("Fair Clean Price: " + org.drip.quant.common.FormatUtil.FormatDouble
				(mapResult.get ("FairCleanPV"), 2, 3, 100.));

			System.out.println ("Fair Yield: " + org.drip.quant.common.FormatUtil.FormatDouble
				(mapResult.get ("Yield"), 2, 3, 100.));

			System.out.println ("Fair GSpread: " + org.drip.quant.common.FormatUtil.FormatDouble
				(mapResult.get ("FairGSpread"), 1, 3, 100.));

			System.out.println ("Fair ZSpread: " + org.drip.quant.common.FormatUtil.FormatDouble
				(mapResult.get ("FairZSpread"), 1, 3, 100.));

			System.out.println ("Fair ISpread: " + org.drip.quant.common.FormatUtil.FormatDouble
				(mapResult.get ("FairISpread"), 1, 3, 100.));

			System.out.println ("Fair DV01: " + mapResult.get ("FairDV01"));

			System.out.println ("Accrued: " + mapResult.get ("Accrued"));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the FX API
	 */

	public static final void FXAPISample()
	{
		org.drip.product.params.CurrencyPair cp = null;

		try {
			cp = new org.drip.product.params.CurrencyPair ("EUR", "USD", "USD", 10000.);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		java.util.Random rand = new java.util.Random();

		org.drip.analytics.rates.DiscountCurve dcUSD =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "USD", null, 0.05);

		org.drip.analytics.rates.DiscountCurve dcEUR =
			org.drip.state.creator.DiscountCurveBuilder.CreateFromFlatRate
				(org.drip.analytics.date.DateUtil.Today(), "EUR", null, 0.04);

		double dblFXSpot = 1.40;
		double dblFXFwdMarket = 1.40;
		double[] adblNodes = new double[5];
		double[] adblFXFwd = new double[5];
		boolean[] abIsPIP = new boolean[5];
		double dblFXFwd = java.lang.Double.NaN;
		double dblFXFwdPIP = java.lang.Double.NaN;
		double dblDCEURBasis = java.lang.Double.NaN;
		double dblDCUSDBasis = java.lang.Double.NaN;
		org.drip.state.curve.DerivedFXForward fxCurve = null;

		for (int i = 0; i < 5; ++i) {
			abIsPIP[i] = false;
			adblFXFwd[i] = dblFXSpot - (i + 1) * 0.01 * rand.nextDouble();

			adblNodes[i] = org.drip.analytics.date.DateUtil.Today().addYears (i + 1).julian();

			if (s_bFXFwd)
				System.out.println ("Input " + cp.code() + "[" + (i + 1) + "] = " +
					org.drip.quant.common.FormatUtil.FormatDouble (adblFXFwd[i], 1, 3, 100.));
		}

		org.drip.param.valuation.ValuationParams valParams = org.drip.param.valuation.ValuationParams.Spot
			(org.drip.analytics.date.DateUtil.Today(), 0, "USD",
				org.drip.analytics.daycount.Convention.DATE_ROLL_ACTUAL);

		org.drip.product.definition.FXForward fxfwd =
			org.drip.product.creator.FXForwardBuilder.CreateFXForward (cp,
				org.drip.analytics.date.DateUtil.Today(), "1Y");

		try {
			dblFXFwd = fxfwd.imply (valParams, dcEUR, dcUSD, 1.4, false);

			dblFXFwdPIP = fxfwd.imply (valParams, dcEUR, dcUSD, 1.4, true);

			dblDCEURBasis = fxfwd.discountCurveBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket,
				false);

			dblDCUSDBasis = fxfwd.discountCurveBasis (valParams, dcEUR, dcUSD, dblFXSpot, dblFXFwdMarket,
				true);

			fxCurve = new org.drip.state.curve.DerivedFXForward (cp,
				org.drip.analytics.date.DateUtil.Today(), dblFXSpot, adblNodes, adblFXFwd, abIsPIP);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		double[] adblFullUSDBasis = fxCurve.zeroBasis (valParams, dcEUR, dcUSD, true);

		double[] adblFullEURBasis = fxCurve.zeroBasis (valParams, dcEUR, dcUSD, false);

		double[] adblBootstrappedUSDBasis = fxCurve.bootstrapBasis (valParams, dcEUR, dcUSD, true);

		double[] adblBootstrappedEURBasis = fxCurve.bootstrapBasis (valParams, dcEUR, dcUSD, false);

		double[] adblFXFwdFromUSDBasis = null;
		double[] adblFXFwdFromEURBasis = null;
		org.drip.analytics.definition.FXBasisCurve fxEURBasisCurve = null;
		org.drip.analytics.definition.FXBasisCurve fxUSDBasisCurve = null;

		try {
			fxUSDBasisCurve = org.drip.state.creator.FXBasisCurveBuilder.CreateFXBasisCurve (cp,
				org.drip.analytics.date.DateUtil.Today(), dblFXSpot, adblNodes, adblFullUSDBasis, false);

			adblFXFwdFromUSDBasis = fxUSDBasisCurve.fxForward (valParams, dcEUR, dcUSD, true, false);

			fxEURBasisCurve = org.drip.state.creator.FXBasisCurveBuilder.CreateFXBasisCurve (cp,
				org.drip.analytics.date.DateUtil.Today(), dblFXSpot, adblNodes, adblFullEURBasis, false);

			adblFXFwdFromEURBasis = fxEURBasisCurve.fxForward (valParams, dcEUR, dcUSD, false, false);
		} catch (java.lang.Exception e) {
			if (s_bSupressErrMsg) {
				System.out.println ("FXAPISample failed.");

				return;
			}

			e.printStackTrace();
		}

		if (s_bFXFwd) {
			System.out.println (cp.code() + "[1Y]= " + dblFXFwd);

			System.out.println (cp.code() + "[1Y](pip)= " + org.drip.quant.common.FormatUtil.FormatDouble
				(dblFXFwdPIP, 1, 3, 100.));

			System.out.println ("EUR Basis bp for " + cp.code() + "[1Y] = " + dblFXFwdMarket + ": " +
				org.drip.quant.common.FormatUtil.FormatDouble (dblDCEURBasis, 1, 3, 100.));

			System.out.println ("USD Basis bp for " + cp.code() + "[1Y] = " + dblFXFwdMarket + ": " +
				org.drip.quant.common.FormatUtil.FormatDouble (dblDCUSDBasis, 1, 3, 100.));

			for (int i = 0; i < adblFullUSDBasis.length; ++i) {
				System.out.println ("FullUSDBasis[" + (i + 1) + "Y]=" +
						org.drip.quant.common.FormatUtil.FormatDouble (adblFullUSDBasis[i], 1, 3, 100.));

				System.out.println ("FullEURBasis[" + (i + 1) + "Y]=" +
						org.drip.quant.common.FormatUtil.FormatDouble (adblFullEURBasis[i], 1, 3, 100.));
			}

			for (int i = 0; i < adblBootstrappedUSDBasis.length; ++i) {
				System.out.println ("Bootstrapped USDBasis from FX fwd for " + cp.code() + "[" + (i + 1) +
					"Y]=" + org.drip.quant.common.FormatUtil.FormatDouble (adblBootstrappedUSDBasis[i], 1, 3,
						100.));

				System.out.println ("Bootstrapped EURBasis from FX fwd for " + cp.code() + "[" + (i + 1) +
					"Y]=" + org.drip.quant.common.FormatUtil.FormatDouble (adblBootstrappedEURBasis[i], 1, 3,
						100.));
			}

			for (int i = 0; i < adblFXFwdFromUSDBasis.length; ++i)
				System.out.println ("FX Fwd from Bootstrapped USD Basis: " + cp.code() + "[" + (i + 1) +
					"Y]=" + org.drip.quant.common.FormatUtil.FormatDouble (adblFXFwdFromUSDBasis[i], 1, 3,
						100.));

			for (int i = 0; i < adblFXFwdFromEURBasis.length; ++i)
				System.out.println ("FX Fwd from Bootstrapped EUR Basis: " + cp.code() + "[" + (i + 1) +
					"Y]=" + org.drip.quant.common.FormatUtil.FormatDouble (adblFXFwdFromEURBasis[i], 1, 3,
						100.));
		}
	}

	/**
	 * Sample demonstrating the creation/usage of the CDX API
	 */

	public static final void BasketCDSAPISample()
	{
		org.drip.analytics.date.JulianDate dtToday = org.drip.analytics.date.DateUtil.Today();

		org.drip.product.definition.BasketProduct bpCDX = org.drip.service.api.CreditAnalytics.MakeCDX
			("CDX.NA.IG", 17, "5Y");

		org.drip.product.definition.BasketProduct bpCDXOTR = org.drip.service.api.CreditAnalytics.MakeCDX
			("CDX.NA.IG", dtToday, "5Y");

		java.util.Set<java.lang.String> setstrCDXNames =
			org.drip.service.env.StandardCDXManager.GetCDXNames();

		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.String> mapCDXDescr =
			org.drip.service.env.StandardCDXManager.GetCDXDescriptions();

		org.drip.product.definition.BasketProduct bpPresetOTR =
			org.drip.service.env.StandardCDXManager.GetOnTheRun ("CDX.EM", dtToday.subtractTenor ("1Y"),
				"5Y");

		org.drip.product.definition.BasketProduct bpPreLoadedOTR =
			org.drip.service.env.StandardCDXManager.GetOnTheRun ("ITRAXX.ENERGY", dtToday.subtractTenor
				("7Y"), "5Y");

		java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Integer> mapCDXSeries =
			org.drip.service.env.StandardCDXManager.GetCDXSeriesMap ("ITRAXX.ENERGY");

		if (s_bBasketCDS) {
			System.out.println (bpCDX.name() + ": " + bpCDX.effective() + "=>" + bpCDX.maturity());

			System.out.println (bpCDXOTR.name() + ": " + bpCDXOTR.effective() + "=>" +
				bpCDXOTR.maturity());
		}

		if (s_bStandardCDXNames) {
			int i = 0;

			for (java.lang.String strCDX : setstrCDXNames)
				System.out.println ("CDX[" + i++ + "]: " + strCDX);
		}

		if (s_bNamedCDXMap) {
			for (java.util.Map.Entry<java.lang.String, java.lang.String> meCDXDescr : mapCDXDescr.entrySet())
				System.out.println ("[" + meCDXDescr.getKey() + "]: " + meCDXDescr.getValue());
		}

		if (s_bOnTheRun) {
			System.out.println (bpPresetOTR.name() + ": " + bpPresetOTR.effective() + "=>" +
				bpPresetOTR.maturity());

			System.out.println (bpPreLoadedOTR.name() + ": " + bpPreLoadedOTR.effective() + "=>" +
				bpPreLoadedOTR.maturity());
		}

		if (s_bCDXSeries) {
			for (java.util.Map.Entry<org.drip.analytics.date.JulianDate, java.lang.Integer> me :
				mapCDXSeries.entrySet())
				System.out.println ("ITRAXX.ENERGY[" + me.getValue() + "]: " + me.getKey());
		}
	}

	public static final void main (
		final java.lang.String astrArgs[])
	{
		java.lang.String strConfig = "c:\\Lakshmi\\BondAnal\\Config.xml";

		// java.lang.String strConfig = "";

		if (null != astrArgs) {
			if (1 == astrArgs.length)
				strConfig = astrArgs[0];
			else {
				if (2 <= astrArgs.length) {
					if (astrArgs[0].equalsIgnoreCase ("-config") || astrArgs[0].equalsIgnoreCase ("-cfg") ||
						astrArgs[0].equalsIgnoreCase ("-conf"))
						strConfig = astrArgs[1];
				}
			}
		}

		boolean bFIInit = org.drip.service.api.CreditAnalytics.Init (strConfig);

		if (!bFIInit) System.out.println ("Cannot fully init FI!");

		CalenderAPISample();

		DayCountAPISample();

		if (bFIInit) {
			DiscountCurveAPISample();

			CreditCurveAPISample();
		}

		CDSAPISample();

		if (bFIInit) BondAPISample();

		CustomBondAPISample();

		if (bFIInit) {
			BondTickerAPISample();

			BondEODMeasuresAPISample();

			CDSEODMeasuresAPISample();

			BondStaticAPISample();
		}

		BondCDSCurveCalibration();

		FXAPISample();

		if (bFIInit) BasketBondAPISample();

		BasketCDSAPISample();
	}
}

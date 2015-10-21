
package org.drip.sample.bond;

import org.drip.analytics.cashflow.CompositePeriod;
import org.drip.analytics.date.*;
import org.drip.param.market.CurveSurfaceQuoteSet;
import org.drip.param.valuation.ValuationParams;
import org.drip.product.creator.FixedMortgageBondBuilder;
import org.drip.product.definition.Bond;
import org.drip.quant.common.FormatUtil;
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
 * FixedPaymentMortgageBond demonstrates the Construction and Valuation of a Custom Fixed Cash-flow Mortgage
 *  Bond.
 *
 * @author Lakshmi Krishnamurthy
 */

public class FixedPaymentMortgageBond {

	private static final void BondMetrics (
		final Bond bond,
		final double dblInitialNotional,
		final JulianDate dtSettle,
		final CurveSurfaceQuoteSet mktParams,
		final double dblCleanPrice)
		throws Exception
	{
		double dblAccrued = bond.accrued (
			dtSettle.julian(),
			null
		);

		double dblYield = bond.yieldFromPrice (
			new ValuationParams (
				dtSettle,
				dtSettle,
				bond.currency()
			),
			mktParams,
			null,
			dblCleanPrice
		);

		double dblModifiedDuration = bond.modifiedDurationFromPrice (
			new ValuationParams (
				dtSettle,
				dtSettle,
				bond.currency()
			),
			mktParams,
			null,
			dblCleanPrice
		);

		double dblRisk = bond.yield01FromPrice (
			new ValuationParams (
				dtSettle,
				dtSettle,
				bond.currency()
			),
			mktParams,
			null,
			dblCleanPrice
		);

		double dblConvexity = bond.convexityFromPrice (
			new ValuationParams (
				dtSettle,
				dtSettle,
				bond.currency()
			),
			mktParams,
			null,
			dblCleanPrice
		);

		JulianDate dtPreviousCouponDate = bond.previousCouponDate (dtSettle);

		double dblCurrentPrincipal = bond.notional (dtPreviousCouponDate.julian()) * dblInitialNotional;

		double dblAccruedAmount = dblAccrued * dblInitialNotional;

		System.out.println ("\t-------------------------------------");

		System.out.println ("\tAnalytics Metrics for " + bond.name());

		System.out.println ("\t-------------------------------------");

		System.out.println ("\tPrice             : " + FormatUtil.FormatDouble (dblCleanPrice, 1, 4, 100.));

		System.out.println ("\tYield             : " + FormatUtil.FormatDouble (dblYield, 1, 2, 100.) + "%");

		System.out.println ("\tSettle            :  " + dtSettle);

		System.out.println();

		System.out.println ("\tModified Duration : " + FormatUtil.FormatDouble (dblModifiedDuration, 1, 4, 10000.));

		System.out.println ("\tRisk              : " + FormatUtil.FormatDouble (dblRisk, 1, 4, 10000.));

		System.out.println ("\tConvexity         : " + FormatUtil.FormatDouble (dblConvexity * dblInitialNotional, 1, 4, 1.));

		System.out.println ("\tDV01              : " + FormatUtil.FormatDouble (dblRisk * dblInitialNotional, 1, 2, 1.));

		System.out.println();

		System.out.println ("\tPrevious Coupon Date : " + dtPreviousCouponDate);

		System.out.println ("\tFace                 : " + FormatUtil.FormatDouble (dblInitialNotional, 1, 2, 1.));

		System.out.println ("\tCurrent Principal    : " + FormatUtil.FormatDouble (dblCurrentPrincipal, 1, 2, 1.));

		System.out.println ("\tAccrued              : " + FormatUtil.FormatDouble (dblAccruedAmount, 1, 6, 1.));

		System.out.println ("\tTotal                : " + FormatUtil.FormatDouble (dblCleanPrice * dblCurrentPrincipal + dblAccruedAmount, 1, 2, 1.));

		System.out.println ("\tAccrual Days         : " + FormatUtil.FormatDouble (dtSettle.julian() - dtPreviousCouponDate.julian(), 1, 0, 1.));
	}

	public static void main (
		final String[] astrArgs)
		throws Exception
	{
		CreditAnalytics.Init ("");

		double dblBeginPrincipalFactor = 1.;
		double dblCouponRate = 0.0924;
		double dblServiceFeeRate = 0.00;
		double dblBondNotional = 10131.81;
		String strDayCount = "Act/365";
		String strCurrency = "USD";
		int iNumPayment = 36;
		int iPayFrequency = 12;
		double dblFixedMonthlyAmount = 638.23;

		double dblFixedPaymentAmount = FixedMortgageBondBuilder.ConstantUniformPaymentAmount (
			dblBondNotional,
			dblCouponRate,
			iNumPayment / iPayFrequency
		);

		JulianDate dtEffective = DateUtil.CreateFromYMD (
			2015,
			DateUtil.MARCH,
			23
		);

		Bond bond = FixedMortgageBondBuilder.Create (
			"FPMA 12.99 2016",
			dtEffective,
			strCurrency,
			iNumPayment,
			strDayCount,
			iPayFrequency,
			dblCouponRate,
			0.,
			dblFixedMonthlyAmount,
			dblBondNotional
		);

		System.out.println ("\n\n\t|------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|                                         FIXED CASH-FLOW MORTGAGE BOND ANALYTICS                                                    ||");

		System.out.println ("\t|                                         ----- --------- -------- ---- ---------                                                    ||");

		System.out.println ("\t|    L -> R:                                                                                                                         ||");

		System.out.println ("\t|            - Start Date                                                                                                            ||");

		System.out.println ("\t|            - End Date                                                                                                              ||");

		System.out.println ("\t|            - Pay Date                                                                                                              ||");

		System.out.println ("\t|            - Principal Factor                                                                                                      ||");

		System.out.println ("\t|            - Accrual Days                                                                                                          ||");

		System.out.println ("\t|            - Accrual Fraction                                                                                                      ||");

		System.out.println ("\t|            - Coupon Rate (%)                                                                                                       ||");

		System.out.println ("\t|            - Coupon Amount                                                                                                         ||");

		System.out.println ("\t|            - Fee Rate (%)                                                                                                          ||");

		System.out.println ("\t|            - Fee Amount                                                                                                            ||");

		System.out.println ("\t|            - Principal Amount                                                                                                      ||");

		System.out.println ("\t|            - Total Amount                                                                                                          ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------------||");

		for (CompositePeriod p : bond.couponPeriods()) {
			double dblPeriodCouponRate = p.couponMetrics (
				dtEffective.julian(),
				null
			).rate();

			double dblCouponDCF = p.couponDCF();

			double dblEndPrincipalFactor = bond.notional (p.endDate());

			double dblPrincipalAmount = (dblBeginPrincipalFactor - dblEndPrincipalFactor) * dblBondNotional;

			double dblCouponAmount = dblBeginPrincipalFactor * dblPeriodCouponRate * dblCouponDCF * dblBondNotional;

			System.out.println ("\t| [" +
				DateUtil.FromJulian (p.startDate()) + " -> " +
				DateUtil.FromJulian (p.endDate()) + "] => " +
				DateUtil.FromJulian (p.payDate()) + " | " +
				FormatUtil.FormatDouble (dblBeginPrincipalFactor, 1, 4, 1.) + " | " +
				FormatUtil.FormatDouble (dblCouponDCF * 365, 1, 0, 1.) + " | " +
				FormatUtil.FormatDouble (dblCouponDCF, 1, 10, 1.) + " | " +
				FormatUtil.FormatDouble (dblPeriodCouponRate, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (dblCouponAmount, 2, 2, 1.) + " | " +
				FormatUtil.FormatDouble (dblServiceFeeRate, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (dblCouponAmount * dblServiceFeeRate / dblPeriodCouponRate, 2, 2, 1.) + " | " +
				FormatUtil.FormatDouble (dblPrincipalAmount, 2, 2, 1.) + " | " +
				FormatUtil.FormatDouble (dblPrincipalAmount + dblCouponAmount, 2, 2, 1.) + " ||"
			);

			dblBeginPrincipalFactor = dblEndPrincipalFactor;
		}

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------------||\n\n");

		JulianDate dtSettle = DateUtil.CreateFromYMD (
			2015,
			DateUtil.MARCH,
			31
		);

		double dblCleanPrice = 1.00; // PAR

		CurveSurfaceQuoteSet mktParams = new CurveSurfaceQuoteSet();

		BondMetrics (
			bond,
			dblBondNotional,
			dtSettle,
			mktParams,
			dblCleanPrice
		);

		Bond bondFeeAdjusted = FixedMortgageBondBuilder.Create (
			"FPMA 12.99 2016",
			dtEffective,
			strCurrency,
			iNumPayment,
			strDayCount,
			iPayFrequency,
			dblCouponRate,
			dblServiceFeeRate,
			dblFixedMonthlyAmount,
			dblBondNotional
		);

		double dblYieldFeeAdjusted = bondFeeAdjusted.yieldFromPrice (
			new ValuationParams (
				dtSettle,
				dtSettle,
				bond.currency()
			),
			mktParams,
			null,
			dblCleanPrice
		);

		System.out.println ("\tFee Adjusted Yield   : " + FormatUtil.FormatDouble (dblYieldFeeAdjusted, 1, 2, 100.) + "%");

		System.out.println ("\n\tUniform Constant Mortgage Amount => " + FormatUtil.FormatDouble (dblFixedPaymentAmount, 1, 2, 1.));
	}
}

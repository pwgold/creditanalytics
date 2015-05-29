
package org.drip.sample.api;

import org.drip.analytics.date.JulianDate;
import org.drip.market.otc.*;
import org.drip.product.creator.SingleStreamComponentBuilder;
import org.drip.product.definition.CalibratableFixedIncomeComponent;
import org.drip.product.rates.*;
import org.drip.state.identifier.ForwardLabel;

public class OIS {
	public static final FixFloatComponent OTCSwap (
		final JulianDate dtValue,
		final String strCurrency,
		final String strMaturityTenor,
		final double dblCoupon,
		final double dblNotional)
	{
		return OvernightFixedFloatContainer.ConventionFromJurisdiction (strCurrency).createFixFloatComponent (
			dtValue,
			strMaturityTenor,
			dblCoupon,
			0.,
			dblNotional
		);
	}

	public static final SingleStreamComponent Deposit (
		final JulianDate dtEffective,
		final String strCurrency,
		final String strFloaterTenor,
		final String strMaturityTenor)
		throws Exception
	{
		return Deposit (
			dtEffective, // MESSED UP
			strCurrency, 
			dtEffective.addTenorAndAdjust (
				strMaturityTenor, 
				strCurrency
			)
		);
	}

	public static final SingleStreamComponent Deposit (
		final JulianDate dtEffective,
		final String strCurrency,
		final int iDay)
		throws Exception
	{
		return Deposit (
			dtEffective, 
			strCurrency, 
			dtEffective.addBusDays (
				iDay,
				strCurrency
			)
		);
	}

	public static final SingleStreamComponent Deposit (
		final JulianDate dtEffective,
		final String strCurrency,
		final JulianDate dtMaturity)
	{
		return SingleStreamComponentBuilder.Deposit (
			dtEffective,
			dtMaturity,
			ForwardLabel.Create (
				strCurrency,
				"ON"
			)
		);
	}

	public static final CalibratableFixedIncomeComponent[] Deposits (
		final JulianDate dtValue,
		final String strCurrency,
		final int[] iDepositDays,
		final double[] adblDepositQuote)
		throws Exception
	{
		CalibratableFixedIncomeComponent[] aDepositComp = new CalibratableFixedIncomeComponent[iDepositDays.length];

		for (int i = 0; i < iDepositDays.length; ++i)
			aDepositComp[i] = Deposit (
				dtValue,
				strCurrency, 
				iDepositDays[i]
			);

		return aDepositComp;
	}

	public static final CalibratableFixedIncomeComponent[] OTCSwaps (
		final JulianDate dtValue,
		final String strCurrency,
		final String[] adblLongEndOISTenor,
		final double[] adblLongEndOISQuote)
		throws Exception
	{
		CalibratableFixedIncomeComponent[] aLongEndOISComp = new CalibratableFixedIncomeComponent[adblLongEndOISTenor.length];

		for (int i = 0; i < adblLongEndOISTenor.length; ++i)
			aLongEndOISComp[i] = OTCSwap (
				dtValue, 
				strCurrency, 
				adblLongEndOISTenor[i],
				adblLongEndOISQuote[i],
				1.
			);

		return aLongEndOISComp;
	}
}

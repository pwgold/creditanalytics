
package org.drip.sample.api;

import java.util.List;

import org.drip.analytics.date.JulianDate;
import org.drip.analytics.daycount.*;
import org.drip.analytics.support.*;
import org.drip.market.otc.*;
import org.drip.param.period.*;
import org.drip.product.definition.CalibratableFixedIncomeComponent;
import org.drip.product.rates.*;
import org.drip.state.identifier.ForwardLabel;

public class IRS {
	public static final FixFloatComponent OTCSwap (
		final JulianDate dtValue,
		final String strCurrency,
		final String strMaturityTenor,
		final double dblCoupon,
		final double dblNotional)
		throws Exception
	{
		return IBORFixedFloatContainer.ConventionFromJurisdiction (strCurrency).createFixFloatComponent (
			dtValue,
			strMaturityTenor,
			dblCoupon,
			0.,
			dblNotional
		);
	}		

	public static final FixFloatComponent Swap (
		final JulianDate dtEffective,
		final String strCurrency,
		final JulianDate dtMaturity,
		final String strFixedDayCount,
		final double dblFixedCoupon,
		final String strFixedTenor,
		final String strFloaterComposableTenor,
		final String strFloaterCompositeTenor,
		final double dblNotional)
		throws Exception
	{
		List<Double> lsFixedStreamEdgeDate = CompositePeriodBuilder.BackwardEdgeDates (
			dtEffective,
			dtMaturity,
			strFixedTenor,
			new DateAdjustParams (
				Convention.DATE_ROLL_FOLLOWING,
				0,
				strCurrency
			),
			CompositePeriodBuilder.SHORT_STUB
		);

		List<Double> lsFloatingStreamEdgeDate = CompositePeriodBuilder.BackwardEdgeDates (
			dtEffective,
			dtMaturity,
			strFloaterCompositeTenor,
			new DateAdjustParams (
				Convention.DATE_ROLL_FOLLOWING,
				0,
				strCurrency
			),
			CompositePeriodBuilder.SHORT_STUB
		);

		return Swap (
			dtEffective, 
			strCurrency, 
			lsFixedStreamEdgeDate,
			lsFloatingStreamEdgeDate,
			strFixedDayCount,
			dblFixedCoupon,
			strFixedTenor,
			strFloaterComposableTenor,
			strFloaterCompositeTenor,
			dblNotional
		);		
	}

	public static final FixFloatComponent Swap (
		final JulianDate dtEffective,
		final String strCurrency,
		final String strMaturityTenor,
		final String strFixedDayCount,
		final double dblFixedCoupon,
		final String strFixedTenor,
		final String strFloaterComposableTenor,
		final String strFloaterCompositeTenor,
		final double dblNotional)
		throws Exception
	{
		List<Double> lsFixedStreamEdgeDate = CompositePeriodBuilder.RegularEdgeDates (
			dtEffective,
			strFixedTenor,
			strMaturityTenor,
			null
		);

		List<Double> lsFloatingStreamEdgeDate = CompositePeriodBuilder.RegularEdgeDates (
			dtEffective,
			strFloaterComposableTenor,
			strMaturityTenor,
			null
		);

		return Swap (
			dtEffective, 
			strCurrency, 
			lsFixedStreamEdgeDate,
			lsFloatingStreamEdgeDate,
			strFixedDayCount,
			dblFixedCoupon,
			strFixedTenor,
			strFloaterComposableTenor,
			strFloaterCompositeTenor,
			dblNotional
		);		
	}

	public static final FixFloatComponent Swap (
		final JulianDate dtEffective,
		final String strCurrency,
		List<Double> lsFixedStreamEdgeDate,
		List<Double> lsFloatingStreamEdgeDate,
		final String strFixedDayCount,
		final double dblFixedCoupon,
		final String strFixedTenor,
		final String strFloaterComposableTenor,
		final String strFloaterCompositeTenor,
		final double dblNotional)
		throws Exception
	{
		int iFixedFreq = AnalyticsHelper.TenorToFreq (strFixedTenor);

		UnitCouponAccrualSetting ucasFixed = new UnitCouponAccrualSetting (
			iFixedFreq,
			strFixedDayCount,
			false,
			strFixedDayCount,
			false,
			strCurrency,
			false,
			CompositePeriodBuilder.ACCRUAL_COMPOUNDING_RULE_GEOMETRIC
		);

		ComposableFloatingUnitSetting cfusFloating = new ComposableFloatingUnitSetting (
			strFloaterComposableTenor,
			CompositePeriodBuilder.EDGE_DATE_SEQUENCE_REGULAR,
			new DateAdjustParams (
				Convention.DATE_ROLL_FOLLOWING,
				0,
				strCurrency
			),
			ForwardLabel.Create (
				strCurrency,
				strFloaterComposableTenor
			),
			CompositePeriodBuilder.REFERENCE_PERIOD_IN_ADVANCE,
			0.
		);

		ComposableFixedUnitSetting cfusFixed = new ComposableFixedUnitSetting (
			strFixedTenor,
			CompositePeriodBuilder.EDGE_DATE_SEQUENCE_REGULAR,
			new DateAdjustParams (
				Convention.DATE_ROLL_FOLLOWING,
				0,
				strCurrency
			),
			dblFixedCoupon,
			0.,
			strCurrency
		);

		int iFloaterFreq = AnalyticsHelper.TenorToFreq (strFloaterCompositeTenor);

		CompositePeriodSetting cpsFloating = new CompositePeriodSetting (
			iFloaterFreq,
			strFloaterCompositeTenor,
			strCurrency,
			null,
			-1. * dblNotional,
			null,
			null,
			null,
			null
		);

		CompositePeriodSetting cpsFixed = new CompositePeriodSetting (
			iFixedFreq,
			strFixedTenor,
			strCurrency,
			null,
			1. * dblNotional,
			null,
			null,
			null,
			null
		);

		Stream floatingStream = new Stream (
			CompositePeriodBuilder.FloatingCompositeUnit (
				lsFloatingStreamEdgeDate,
				cpsFloating,
				cfusFloating
			)
		);

		Stream fixedStream = new Stream (
			CompositePeriodBuilder.FixedCompositeUnit (
				lsFixedStreamEdgeDate,
				cpsFixed,
				ucasFixed,
				cfusFixed
			)
		);

		return new FixFloatComponent (
			fixedStream,
			floatingStream,
			null
		);
	}

	public static final SingleStreamComponent Deposit (
		final JulianDate dtEffective,
		final String strCurrency,
		final String strMaturityTenor)
		throws Exception
	{
		return Deposit (
			dtEffective, 
			strCurrency, 
			IBORFixedFloatContainer.ConventionFromJurisdiction (strCurrency).floatStreamConvention().floaterIndex().tenor(),
			dtEffective.addTenorAndAdjust (
				strMaturityTenor, 
				strCurrency
			)
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
			dtEffective, 
			strCurrency, 
			strFloaterTenor, 
			dtEffective.addTenorAndAdjust (
				strMaturityTenor, 
				strCurrency
			)
		);
	}

	public static final SingleStreamComponent Deposit (
		final JulianDate dtEffective,
		final String strCurrency,
		final String strFloaterTenor,
		final int iDay)
		throws Exception
	{
		return Deposit (
			dtEffective, 
			strCurrency, 
			strFloaterTenor, 
			dtEffective.addBusDays (
				iDay,
				strCurrency
			)
		);
	}

	public static final SingleStreamComponent Deposit (
		final JulianDate dtEffective,
		final String strCurrency,
		final String strFloaterTenor,
		final JulianDate dtMaturity)
		throws Exception
	{
		ComposableFloatingUnitSetting cfus = new ComposableFloatingUnitSetting (
			strFloaterTenor,
			CompositePeriodBuilder.EDGE_DATE_SEQUENCE_SINGLE, 
			null,
			ForwardLabel.Create (
				strCurrency,
				strFloaterTenor
			),
			CompositePeriodBuilder.REFERENCE_PERIOD_IN_ADVANCE,
			0.
		);

		CompositePeriodSetting cps = new CompositePeriodSetting (
			AnalyticsHelper.TenorToFreq (strFloaterTenor),
			strFloaterTenor,
			strCurrency,
			null,
			1.,
			null,
			null,
			null,
			null
		);

		List<Double> lsFixedStreamEdgeDate = CompositePeriodBuilder.EdgePair (
			dtEffective,
			dtMaturity
		);

		return new SingleStreamComponent (
			"DEPOSIT",
			new Stream (
				CompositePeriodBuilder.FloatingCompositeUnit (
					lsFixedStreamEdgeDate,
					cps,
					cfus
				)
			),
			null
		);
	}

	public static final CalibratableFixedIncomeComponent[] Deposits (
		final JulianDate dtValue,
		final String strCurrency,
		final String[] astrMaturityTenor,
		final double[] adblDepositQuote)
		throws Exception
	{
		CalibratableFixedIncomeComponent[] aDepositComp = new CalibratableFixedIncomeComponent[astrMaturityTenor.length];

		for (int i = 0; i < astrMaturityTenor.length; ++i)
			aDepositComp[i] = Deposit (
				dtValue,
				strCurrency, 
				astrMaturityTenor[i]
			);

		return aDepositComp;
	}
	
	public static final CalibratableFixedIncomeComponent[] Deposits (
		final JulianDate dtValue,
		final String strCurrency,
		final String strFloaterTenor,
		final String[] astrMaturityTenor,
		final double[] adblDepositQuote)
		throws Exception
	{
		CalibratableFixedIncomeComponent[] aDepositComp = new CalibratableFixedIncomeComponent[astrMaturityTenor.length];

		for (int i = 0; i < astrMaturityTenor.length; ++i)
			aDepositComp[i] = Deposit (
				dtValue,
				strCurrency, 
				strFloaterTenor,
				astrMaturityTenor[i]
			);

		return aDepositComp;
	}
	
	public static final CalibratableFixedIncomeComponent[] OTCSwaps (
		final JulianDate dtValue,
		final String strCurrency,
		final String[] astrMaturityTenor,
		final double[] adblCoupon)
		throws Exception
	{
		CalibratableFixedIncomeComponent[] aLongEndLiborComp = new CalibratableFixedIncomeComponent[astrMaturityTenor.length];

		for (int i = 0; i < astrMaturityTenor.length; ++i)
			aLongEndLiborComp[i] = OTCSwap (
				dtValue,
				strCurrency, 
				astrMaturityTenor[i],
				adblCoupon[i],
				1.
			);

		return aLongEndLiborComp;
	}
}

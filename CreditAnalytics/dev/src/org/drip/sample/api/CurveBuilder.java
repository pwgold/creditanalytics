
package org.drip.sample.api;

import java.util.Map;

import org.drip.analytics.date.*;
import org.drip.analytics.rates.*;
import org.drip.function.deterministic1D.QuadraticRationalShapeControl;
import org.drip.market.otc.*;
import org.drip.param.creator.*;
import org.drip.param.market.CurveSurfaceQuoteSet;
import org.drip.param.valuation.*;
import org.drip.product.definition.CalibratableFixedIncomeComponent;
import org.drip.product.rates.FixFloatComponent;
import org.drip.quant.common.FormatUtil;
import org.drip.service.api.CreditAnalytics;
import org.drip.spline.basis.*;
import org.drip.spline.params.*;
import org.drip.spline.stretch.*;
import org.drip.state.estimator.LatentStateStretchBuilder;
import org.drip.state.identifier.ForwardLabel;
import org.drip.state.inference.*;

public class CurveBuilder {
	public static final LinearLatentStateCalibrator getLinearCurveCalibrator (
		String interpolation)
		throws Exception
	{
		LinearLatentStateCalibrator lcc;

		if (interpolation.toUpperCase().equals("LINEAR"))
			lcc = new LinearLatentStateCalibrator (
				new SegmentCustomBuilderControl (
					MultiSegmentSequenceBuilder.BASIS_SPLINE_POLYNOMIAL,
					new PolynomialFunctionSetParams (2), // 4
					SegmentInelasticDesignControl.Create (0, 2), //2 2 
					new ResponseScalingShapeControl (true, new QuadraticRationalShapeControl (0.)),
					null),
				BoundarySettings.NaturalStandard(),
				MultiSegmentSequence.CALIBRATE,
				null,
				null
			);
		else if(interpolation.toUpperCase().equals("QUADRATIC")){
			lcc = new LinearLatentStateCalibrator (
					new SegmentCustomBuilderControl (
						MultiSegmentSequenceBuilder.BASIS_SPLINE_POLYNOMIAL,
						new PolynomialFunctionSetParams (3), // 4
						SegmentInelasticDesignControl.Create (1, 2), //2 2 
						new ResponseScalingShapeControl (true, new QuadraticRationalShapeControl (0.)),
						null),
					BoundarySettings.NaturalStandard(),
					MultiSegmentSequence.CALIBRATE,
					null,
					null);
		}else if(interpolation.toUpperCase().equals("CUBIC POLYNOMIAL")){
				lcc = new LinearLatentStateCalibrator (
						new SegmentCustomBuilderControl (
							MultiSegmentSequenceBuilder.BASIS_SPLINE_POLYNOMIAL,
							new PolynomialFunctionSetParams (4), // 4
							SegmentInelasticDesignControl.Create (2, 2), //2 2 
							new ResponseScalingShapeControl (true, new QuadraticRationalShapeControl (0.)),
							null),
						BoundarySettings.NaturalStandard(),
						MultiSegmentSequence.CALIBRATE,
						null,
						null);
		}else if(interpolation.toUpperCase().equals("KLK HYPERBOLIC")){
			lcc = new LinearLatentStateCalibrator (
					 new SegmentCustomBuilderControl (
					 MultiSegmentSequenceBuilder.BASIS_SPLINE_KLK_HYPERBOLIC_TENSION,
					 new ExponentialTensionSetParams (1.), // NB: This is a ratio/weight of linear/cubic. 
					 SegmentInelasticDesignControl.Create (2, 2), // NB: Can reduce CK to 0 < ck < 2
					 new ResponseScalingShapeControl (true, new QuadraticRationalShapeControl (0.)),
					 null),
					 BoundarySettings.NaturalStandard(),
					 MultiSegmentSequence.CALIBRATE,
					 null,
				null);
		}else{
			throw new Exception("interpolatolation ("+interpolation+") needs to be LINEAR, CUBIC POLYNOMIAL, or KLK HYPERBOLIC)");
		}
		return lcc;	
	}

	public static final DiscountCurve BuildDiscountCurve (
		String strCurrency,
		JulianDate dtValue,
		LatentStateStretchSpec[] aStretchSpec,
		String interpolation)
		throws Exception
	{
		return ScenarioDiscountCurveBuilder.ShapePreservingDFBuild (
			getLinearCurveCalibrator (interpolation),
			aStretchSpec,
			new ValuationParams (
				dtValue,
				dtValue,
				strCurrency
			),
			null,
			null,
			null,
			1.
		);
	}

	public static final ForwardCurve MakeForwardCurve (
		String strCurrency,
		JulianDate dtValue,
		LatentStateStretchSpec[] aStretchSpec,
		String interpolation,
		DiscountCurve dcOvernight)
		throws Exception
	{
		return MakeForwardCurve (
			strCurrency,
			dtValue,
			aStretchSpec,
			interpolation,
			IBORFixedFloatContainer.ConventionFromJurisdiction (strCurrency).floatStreamConvention().floaterIndex().tenor(),
			dcOvernight
		);
	}

	public static final ForwardCurve MakeForwardCurve (
		String strCurrency,
		JulianDate dtValue,
		LatentStateStretchSpec[] aStretchSpec,
		String interpolation,
		String strForwardTenor,
		DiscountCurve dcOvernight)
		throws Exception
	{
		CurveSurfaceQuoteSet mktParams = MarketParamsBuilder.Create (
			dcOvernight,
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);

		return ScenarioForwardCurveBuilder.ShapePreservingForwardCurve (
			getLinearCurveCalibrator (interpolation),
			aStretchSpec,
			ForwardLabel.Create (
				strCurrency,
				strForwardTenor
			),
			new ValuationParams (
				dtValue,
				dtValue,
				strCurrency
			),
			null,
			mktParams,
			null,
			0.0004
		);
	}

	public static final void main (
		final String[] args)
		throws Exception
	{
		CreditAnalytics.Init ("");

		final JulianDate dtValue = DateUtil.CreateFromYMD(2015, 05, 1);
		final String strCurrency = "EUR";
		final String interpolation = "KLK HYPERBOLIC";

		int[] iOISDepositDays = new int[] {
			1, 2, 3
		};
		
		double[] adblOISDepositQuote = new double[] {
			0.0004, 0.0004, 0.0004		 
		};

		String[] astrlLongEndOISTenor = new java.lang.String[] {
			"15M", "18M", "21M", "2Y", "3Y", "4Y", "5Y", "6Y", "7Y", "8Y", "9Y", "10Y", "11Y", "12Y", "15Y", "20Y", "25Y", "30Y"
		};

		double[] adblLongEndOISQuote = new double[] {
			0.00002,    //  15M
			0.00008,    //  18M
			0.00021,    //  21M
			0.00036,    //   2Y
			0.00127,    //   3Y
			0.00274,    //   4Y
			0.00456,    //   5Y
			0.00647,    //   6Y
			0.00827,    //   7Y
			0.00996,    //   8Y
			0.01147,    //   9Y
			0.01280,    //  10Y
			0.01404,    //  11Y
			0.01516,    //  12Y
			0.01764,    //  15Y
			0.01939,    //  20Y
			0.02003,    //  25Y
			0.02038     //  30Y
		};
		
		/*
		 * Construct the Array of Deposit Instruments and their Quotes from the given set of parameters
		 */

		double[] adblLiborDepositQuote = new double[] {
			0.003565,	// 1D
			0.003858,	// 1W
			0.003840,	// 2W
			0.003922,	// 3W
			0.003869,	// 1M
			0.003698,	// 2M
		};

		String[] astrLiborDepositTenor = new String[] {
			"1D",
			"1W",
			"2W",
			"3W",
			"1M",
			"2M",
		};

		/*
		 * Construct the Array of Fix-Float Component and their Quotes from the given set of parameters
		 */

		double[] adblLongEndLiborQuote = new double[] {
			0.004240,	//  3Y
			0.005760,	//  4Y			
			0.007620,	//  5Y
			0.009540,	//  6Y
			0.011350,	//  7Y
			0.013030,	//  8Y
			0.014520,	//  9Y
			0.015840,	// 10Y
			0.018090,	// 12Y
			0.020370,	// 15Y
			0.021870,	// 20Y
			0.022340,	// 25Y
			0.022560,	// 30Y
			0.022950,	// 35Y
			0.023480,	// 40Y
			0.024210,	// 50Y
			0.024630	// 60Y
		};

		String[] astrLongEndLiborTenor = new String[] {
			 "3Y",
			 "4Y",
			 "5Y",
			 "6Y",
			 "7Y",
			 "8Y",
			 "9Y",
			"10Y",
			"12Y",
			"15Y",
			"20Y",
			"25Y",
			"30Y",
			"35Y",
			"40Y",
			"50Y",
			"60Y"
		};
		
		System.out.println ("\n\t----------------------------------------------------------------");
		System.out.println ("\t----------------------------------------------------------------");

		CalibratableFixedIncomeComponent[] aOISDepositComp = OIS.Deposits (
			dtValue,
			strCurrency,
			iOISDepositDays,
			adblOISDepositQuote
		);

		CalibratableFixedIncomeComponent[] aLongEndOISComp = OIS.OTCSwaps (
			dtValue,
			strCurrency,
			astrlLongEndOISTenor,
			adblLongEndOISQuote
		);

		LatentStateStretchSpec depositOISStretch = LatentStateStretchBuilder.ForwardFundingStretchSpec (
			"DEPOSIT",
			aOISDepositComp,
			"ForwardRate",
			adblOISDepositQuote
		);

		LatentStateStretchSpec oisLongEndOISStretch = LatentStateStretchBuilder.ForwardFundingStretchSpec (
			"OIS_LONG_END",
			aLongEndOISComp,
			"SwapRate",       // what should this be for single stretch?
			adblLongEndOISQuote
		);

		LatentStateStretchSpec[] aOISStretchSpec = new LatentStateStretchSpec[] {
			depositOISStretch,
			oisLongEndOISStretch
		};
		
		DiscountCurve discountCurve = BuildDiscountCurve (
			strCurrency,
			dtValue,
			aOISStretchSpec,
			interpolation
		);

		CalibratableFixedIncomeComponent[] aLiborDepositComp = IRS.Deposits (
			dtValue,
			strCurrency,
			astrLiborDepositTenor,
			adblLiborDepositQuote
		);

		CalibratableFixedIncomeComponent[] aLongEndLiborComp = IRS.OTCSwaps (
			dtValue,
			strCurrency,
			astrLongEndLiborTenor,
			adblLongEndLiborQuote
		);

		LatentStateStretchSpec depositLiborStretch = LatentStateStretchBuilder.ForwardStretchSpec (
			"DEPOSIT",
			aLiborDepositComp,
			"ForwardRate",
			adblLiborDepositQuote 
		);

		LatentStateStretchSpec oisLongEndLiborStretch = LatentStateStretchBuilder.ForwardStretchSpec (
			"LIBOR_LONG_END",
			aLongEndLiborComp,
			"SwapRate",      
			adblLongEndOISQuote
		);
		
		LatentStateStretchSpec[] aStretchLiborSpec = new LatentStateStretchSpec[] {
			depositLiborStretch,
			oisLongEndLiborStretch
		};

		ForwardCurve forwardCurve = MakeForwardCurve (
			strCurrency,
			dtValue,
			aStretchLiborSpec,
			interpolation,
			discountCurve
		);

		String strMaturity = "5Y"; // For simplicity, price a 5Y swap here though this could have an arbitrary maturity date.
		double dblCoupon = 0.000;
		double notional = 1.; // 50e6;
		
		FixFloatComponent ffcSwap = IRS.OTCSwap (
			dtValue,
			strCurrency,
			strMaturity,
			dblCoupon,
			notional
		);

		CurveSurfaceQuoteSet mktParams = MarketParamsBuilder.Create (
			discountCurve,
			forwardCurve,
			null,
			null,
			null,
			null,
			null,
			null
		);

		System.out.println (ffcSwap.maturityDate() + " | " + ffcSwap.freq());

		Map<String, Double> mapSwap = ffcSwap.value (
			new ValuationParams (
				dtValue,
				dtValue,
				strCurrency
			),	
			null,
			mktParams,
			null
		);

		/* double dblBasePV = mapSwap.get ("PV");
		double dblBaseFixedDV01 = mapSwap.get ("FixedDV01");
		double dblBaseFloatDV01 = mapSwap.get ("FloatDV01");
		System.out.println ("\n---- Swap Output Measures ----\n");

		System.out.println ("Mkt Val      : " + FormatUtil.FormatDouble (dblBasePV, 0, 0, 1)); */
		System.out.println ("Par Cpn      : " + FormatUtil.FormatDouble (mapSwap.get ("SwapRate"), 1, 5, 100.));
		/* System.out.println ("Calib Cpn      : " + FormatUtil.FormatDouble (mapSwap.get ("CalibSwapRate"), 1, 5, 100.));
		System.out.println ("Fixed DV01   : " + FormatUtil.FormatDouble (dblBaseFixedDV01, 0, 0, notional));
		System.out.println ("Float DV01   : " + FormatUtil.FormatDouble (dblBaseFloatDV01, 0, 0, notional));
		System.out.println ("Total DV01   : " + FormatUtil.FormatDouble (dblBaseFloatDV01+dblBaseFixedDV01, 0, 0, notional)); */
	}
}


package org.drip.product.fra;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
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
 * FRAStandardCapFloor implements the Caps and Floors on the Standard FRA.
 *
 * @author Lakshmi Krishnamurthy
 */

/**
 * @author Spooky
 *
 */
public class FRAStandardCapFloor extends org.drip.product.definition.FixedIncomeComponent {
	private boolean _bIsCap = false;
	private java.lang.String _strName = "";
	private double _dblStrike = java.lang.Double.NaN;
	private org.drip.product.rates.Stream _stream = null;
	private org.drip.param.valuation.CashSettleParams _csp = null;

	private java.util.List<org.drip.product.fra.FRAStandardCapFloorlet> _lsFRACapFloorlet = new
		java.util.ArrayList<org.drip.product.fra.FRAStandardCapFloorlet>();

	/**
	 * FRAStandardCapFloor constructor
	 * 
	 * @param strName Name of the Cap/Floor Instance
	 * @param stream The Underlying Stream
	 * @param strManifestMeasure Measure of the Underlying Component
	 * @param bIsCap Is the FRA Option a Cap? TRUE => YES
	 * @param dblStrike Strike of the Underlying Component's Measure
	 * @param ltds Last Trading Date Setting
	 * @param csp Cash Settle Parameters
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public FRAStandardCapFloor (
		final java.lang.String strName,
		final org.drip.product.rates.Stream stream,
		final java.lang.String strManifestMeasure,
		final boolean bIsCap,
		final double dblStrike,
		final org.drip.product.params.LastTradingDateSetting ltds,
		final org.drip.param.valuation.CashSettleParams csp)
		throws java.lang.Exception
	{
		if (null == (_strName = strName) || _strName.isEmpty() || null == (_stream = stream) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblStrike = dblStrike))
			throw new java.lang.Exception ("FRAStandardCapFloor Constructor => Invalid Inputs");

		_csp = csp;
		_bIsCap = bIsCap;

		org.drip.state.identifier.ForwardLabel fri = _stream.forwardLabel();

		if (null == fri)
			throw new java.lang.Exception ("FRAStandardCapFloor Constructor => Invalid Floater Index");

		java.lang.String strCalendar = _stream.calendar();

		java.lang.String strDayCount = _stream.couponDC();

		for (org.drip.analytics.cashflow.CompositePeriod period : _stream.periods()) {
			org.drip.product.fra.FRAStandardComponent fra =
				org.drip.product.creator.SingleStreamComponentBuilder.FRAStandard (new
					org.drip.analytics.date.JulianDate (period.startDate()), fri, _dblStrike);

			_lsFRACapFloorlet.add (new org.drip.product.fra.FRAStandardCapFloorlet (fra, strManifestMeasure,
				_bIsCap, _dblStrike, _stream.notional (period.startDate()), ltds, strDayCount, strCalendar));
		}
	}

	@Override public java.lang.String name()
	{
		return _strName;
	}

	@Override public org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.String> couponCurrency()
	{
		return _lsFRACapFloorlet.get (0).couponCurrency();
	}

	@Override public java.lang.String payCurrency()
	{
		return _stream.payCurrency();
	}

	@Override public java.lang.String principalCurrency()
	{
		return _stream.payCurrency();
	}

	@Override public org.drip.state.identifier.CreditLabel creditLabel()
	{
		return _stream.creditLabel();
	}

	@Override public
		org.drip.analytics.support.CaseInsensitiveTreeMap<org.drip.state.identifier.ForwardLabel>
			forwardLabel()
	{
		return _lsFRACapFloorlet.get (0).forwardLabel();
	}

	@Override public org.drip.state.identifier.FundingLabel fundingLabel()
	{
		return _stream.fundingLabel();
	}

	@Override public
		org.drip.analytics.support.CaseInsensitiveTreeMap<org.drip.state.identifier.FXLabel> fxLabel()
	{
		return _lsFRACapFloorlet.get (0).fxLabel();
	}

	@Override public double initialNotional()
		throws java.lang.Exception
	{
		return _stream.initialNotional();
	}

	@Override public double notional (
		final double dblDate)
		throws java.lang.Exception
	{
		return _stream.notional (dblDate);
	}

	@Override public double notional (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		return _stream.notional (dblDate1, dblDate2);
	}

	@Override public org.drip.analytics.output.CompositePeriodCouponMetrics couponMetrics (
		final double dblAccrualEndDate,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs)
	{
		return null;
	}

	@Override public int freq()
	{
		return _stream.freq();
	}

	@Override public org.drip.analytics.date.JulianDate effectiveDate()
	{
		return _stream.effective();
	}

	@Override public org.drip.analytics.date.JulianDate maturityDate()
	{
		return _stream.maturity();
	}

	@Override public org.drip.analytics.date.JulianDate firstCouponDate()
	{
		return _stream.firstCouponDate();
	}

	@Override public java.util.List<org.drip.analytics.cashflow.CompositePeriod> couponPeriods()
	{
		return _stream.cashFlowPeriod();
	}

	@Override public org.drip.param.valuation.CashSettleParams cashSettleParams()
	{
		return _csp;
	}

	@Override public org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs,
		final org.drip.param.valuation.ValuationCustomizationParams quotingParams)
	{
		double dblPV = 0.;
		double dblPrice = 0.;
		double dblUpfront = 0.;
		double dblATMPrice = 0.;
		org.drip.function.solverR1ToR1.FixedPointFinderOutput fpfo = null;
		org.drip.function.solverR1ToR1.FixedPointFinderOutput fpfoATM = null;

		long lStart = System.nanoTime();

		final double dblValueDate = valParams.valueDate();

		for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapFRAResult = fracfl.value
				(valParams, pricerParams, csqs, quotingParams);

			if (null == mapFRAResult) continue;

			if (mapFRAResult.containsKey ("ATMPrice")) dblATMPrice += mapFRAResult.get ("ATMPrice");

			if (mapFRAResult.containsKey ("Price")) dblPrice += mapFRAResult.get ("Price");

			if (mapFRAResult.containsKey ("PV")) dblPV += mapFRAResult.get ("PV");

			if (mapFRAResult.containsKey ("Upfront")) dblUpfront += mapFRAResult.get ("Upfront");
		}

		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapResult = new
			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double>();

		mapResult.put ("ATMPrice", dblATMPrice);

		mapResult.put ("Price", dblPrice);

		mapResult.put ("PV", dblPV);

		mapResult.put ("Upfront", dblUpfront);

		org.drip.function.definition.R1ToR1 funcVolPricer = new org.drip.function.definition.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblVolatility)
				throws java.lang.Exception
			{
				double dblCapFloorletPrice = 0.;

				for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
					double dblExerciseDate = fracfl.exerciseDate().julian();

					if (dblExerciseDate <= dblValueDate) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapOutput =
						fracfl.valueFromSurfaceVariance (valParams, pricerParams, csqs, quotingParams,
							dblVolatility * dblVolatility * (dblExerciseDate - dblValueDate) / 365.25);

					if (null == mapOutput || !mapOutput.containsKey ("Price"))
						throw new java.lang.Exception
							("FRAStandardCapFloor::value => Cannot generate Calibration Measure");

					dblCapFloorletPrice += mapOutput.get ("Price");
				}

				return dblCapFloorletPrice;
			}
		};

		org.drip.function.definition.R1ToR1 funcATMVolPricer = new org.drip.function.definition.R1ToR1 (null)
		{
			@Override public double evaluate (
				final double dblVolatility)
				throws java.lang.Exception
			{
				double dblCapFloorletATMPrice = 0.;

				for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
					double dblExerciseDate = fracfl.exerciseDate().julian();

					if (dblExerciseDate <= dblValueDate) continue;

					java.util.Map<java.lang.String, java.lang.Double> mapOutput =
						fracfl.valueFromSurfaceVariance (valParams, pricerParams, csqs, quotingParams,
							dblVolatility * dblVolatility * (dblExerciseDate - dblValueDate) / 365.25);

					if (null == mapOutput || !mapOutput.containsKey ("ATMPrice"))
						throw new java.lang.Exception
							("FRAStandardCapFloor::value => Cannot generate Calibration Measure");

					dblCapFloorletATMPrice += mapOutput.get ("ATMPrice");
				}

				return dblCapFloorletATMPrice;
			}
		};

		try {
			fpfo = (new org.drip.function.solverR1ToR1.FixedPointFinderBracketing (dblPrice, funcVolPricer,
				null, org.drip.function.solverR1ToR1.VariateIteratorPrimitive.BISECTION, false)).findRoot
					(org.drip.function.solverR1ToR1.InitializationHeuristics.FromHardSearchEdges (0.0001,
						5.));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return mapResult;
		}

		try {
			fpfoATM = (new org.drip.function.solverR1ToR1.FixedPointFinderBracketing (dblATMPrice,
				funcATMVolPricer, null, org.drip.function.solverR1ToR1.VariateIteratorPrimitive.BISECTION,
					false)).findRoot
						(org.drip.function.solverR1ToR1.InitializationHeuristics.FromHardSearchEdges (0.0001,
							5.));
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return mapResult;
		}

		mapResult.put ("CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		if (null != fpfo && fpfo.containsRoot())
			mapResult.put ("FlatVolatility", fpfo.getRoot());
		else
			mapResult.put ("FlatVolatility", java.lang.Double.NaN);

		if (null != fpfoATM && fpfoATM.containsRoot())
			mapResult.put ("FlatATMVolatility", fpfoATM.getRoot());
		else
			mapResult.put ("FlatATMVolatility", java.lang.Double.NaN);

		return mapResult;
	}

	@Override public java.util.Set<java.lang.String> measureNames()
	{
		java.util.Set<java.lang.String> setstrMeasureNames = new java.util.TreeSet<java.lang.String>();

		setstrMeasureNames.add ("CalcTime");

		setstrMeasureNames.add ("FlatATMVolatility");

		setstrMeasureNames.add ("Price");

		setstrMeasureNames.add ("PV");

		setstrMeasureNames.add ("Upfront");

		return setstrMeasureNames;
	}

	/**
	 * Retrieve the Stream Instance Underlying the Cap
	 * 
	 * @return The Stream Instance Underlying the Cap
	 */

	public org.drip.product.rates.Stream stream()
	{
		return _stream;
	}

	/**
	 * Retrieve the Strike
	 * 
	 * @return The Strike
	 */

	public double strike()
	{
		return _dblStrike;
	}

	/**
	 * Indicate if this is a Cap or Floor
	 * 
	 * @return TRUE => The Product is a Cap
	 */

	public boolean isCap()
	{
		return _bIsCap;
	}

	/**
	 * Compute the Cap/Floor Price from the Flat Volatility
	 * 
	 * @param valParams The Valuation Parameters
	 * @param pricerParams The Pricer Parameters
	 * @param csqs The Market Parameters
	 * @param quotingParams The Quoting Parameters
	 * @param dblFlatVolatility The Flat Volatility
	 * 
	 * @return The Cap/Floor Price
	 * 
	 * @throws java.lang.Exception Thrown if the Price cannot be calculated
	 */

	public double priceFromFlatVolatility (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs,
		final org.drip.param.valuation.ValuationCustomizationParams quotingParams,
		final double dblFlatVolatility)
		throws java.lang.Exception
	{
		if (null == valParams || !org.drip.quant.common.NumberUtil.IsValid (dblFlatVolatility))
			throw new java.lang.Exception ("FRAStandardCapFloor::priceFromFlatVolatility => Invalid Inputs");

		double dblValueDate = valParams.valueDate();

		double dblPrice = 0.;

		for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
			org.drip.analytics.date.JulianDate dtExercise = fracfl.exerciseDate();

			double dblExerciseDate = dtExercise.julian();

			if (dblExerciseDate <= dblValueDate) continue;

			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapCapFloorlet =
				fracfl.valueFromSurfaceVariance (valParams, pricerParams, csqs, quotingParams,
					dblFlatVolatility * dblFlatVolatility * (dblExerciseDate - dblValueDate) / 365.25);

			if (null == mapCapFloorlet || !mapCapFloorlet.containsKey ("Price"))
				throw new java.lang.Exception
					("FRAStandardCapFloor::priceFromFlatVolatility => Cannot value capFloorlet");

			dblPrice += mapCapFloorlet.get ("Price");
		}

		return dblPrice;
	}

	/**
	 * Imply the Forward Rate Volatility of the Unmarked Segment of the Volatility Term Structure
	 * 
	 * @param valParams The Valuation Parameters
	 * @param pricerParams The pricer Parameters
	 * @param csqs The Market Parameters
	 * @param quotingParams The Quoting Parameters
	 * @param strCalibMeasure The Calibration Measure
	 * @param dblCalibValue The Calibration Value
	 * @param mapDateVol The Date/Volatility Map
	 * 
	 * @return TRUE => The Forward Rate Volatility of the Unmarked Segment of the Volatility Term Structure
	 * 	successfully implied
	 */

	public boolean implyVolatility (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs,
		final org.drip.param.valuation.ValuationCustomizationParams quotingParams,
		final java.lang.String strCalibMeasure,
		final double dblCalibValue,
		final java.util.Map<org.drip.analytics.date.JulianDate, java.lang.Double> mapDateVol)
	{
		if (null == valParams || null == strCalibMeasure || strCalibMeasure.isEmpty() ||
			!org.drip.quant.common.NumberUtil.IsValid (dblCalibValue) || null == mapDateVol)
			return false;

		int iIndex = 0;
		double dblPreceedingCapFloorletPV = 0.;
		org.drip.function.solverR1ToR1.FixedPointFinderOutput fpfo = null;

		final double dblValueDate = valParams.valueDate();

		final java.util.List<java.lang.Integer> lsCalibCapFloorletIndex = new
			java.util.ArrayList<java.lang.Integer>();

		for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
			org.drip.analytics.date.JulianDate dtExercise = fracfl.exerciseDate();

			double dblExerciseDate = dtExercise.julian();

			if (dblExerciseDate <= dblValueDate) continue;

			if (mapDateVol.containsKey (dtExercise)) {
				double dblExerciseVolatility = mapDateVol.get (dtExercise);

				org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapCapFloorlet =
					fracfl.valueFromSurfaceVariance (valParams, pricerParams, csqs, quotingParams,
						dblExerciseVolatility * dblExerciseVolatility * (dblExerciseDate - dblValueDate) /
							365.25);

				if (null == mapCapFloorlet || !mapCapFloorlet.containsKey (strCalibMeasure)) return false;

				dblPreceedingCapFloorletPV += mapCapFloorlet.get (strCalibMeasure);
			} else
				lsCalibCapFloorletIndex.add (iIndex);

			++iIndex;
		}

		org.drip.function.definition.R1ToR1 funcVolPricer = new org.drip.function.definition.R1ToR1 (null) {
			@Override public double evaluate (
				final double dblVolatility)
				throws java.lang.Exception
			{
				int iIndex = 0;
				double dblSucceedingCapFloorletPV = 0.;

				for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
					double dblExerciseDate = fracfl.exerciseDate().julian();

					if (dblExerciseDate <= dblValueDate) continue;

					if (lsCalibCapFloorletIndex.contains (iIndex)) {
						java.util.Map<java.lang.String, java.lang.Double> mapOutput =
							fracfl.valueFromSurfaceVariance (valParams, pricerParams, csqs, quotingParams,
								dblVolatility * dblVolatility * (dblExerciseDate - dblValueDate) / 365.25);
	
						if (null == mapOutput || !mapOutput.containsKey (strCalibMeasure))
							throw new java.lang.Exception
								("FRAStandardCapFloor::implyVolatility => Cannot generate Calibration Measure");
	
						dblSucceedingCapFloorletPV += mapOutput.get (strCalibMeasure);
					}
				}

				return dblSucceedingCapFloorletPV;
			}
		};

		try {
			fpfo = (new org.drip.function.solverR1ToR1.FixedPointFinderBracketing (dblCalibValue -
				dblPreceedingCapFloorletPV, funcVolPricer, null,
					org.drip.function.solverR1ToR1.VariateIteratorPrimitive.BISECTION, false)).findRoot
						(org.drip.function.solverR1ToR1.InitializationHeuristics.FromHardSearchEdges (0.0001,
							5.));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		if (null == fpfo || !fpfo.containsRoot()) return false;

		double dblVolatility = fpfo.getRoot();

		for (org.drip.product.fra.FRAStandardCapFloorlet fracfl : _lsFRACapFloorlet) {
			if (lsCalibCapFloorletIndex.contains (iIndex))
				mapDateVol.put (fracfl.exerciseDate(), dblVolatility);
		}

		return true;
	}
}

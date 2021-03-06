
package org.drip.analytics.definition;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * CreditCurve is the stub for the survival curve functionality. It extends the Curve object by exposing the
 * 	following functions:
 * 	- Set of curve and market identifiers
 * 	- Recovery to a specific date/tenor, and effective recovery between a date interval
 * 	- Hazard Rate to a specific date/tenor, and effective hazard rate between a date interval
 * 	- Survival to a specific date/tenor, and effective survival between a date interval
 *  - Set/unset date of specific default
 *  - Generate scenario curves from the base credit curve (flat/parallel/custom)
 *  - Set/unset the Curve Construction Inputs, Latent State, and the Manifest Metrics
 *  - Serialization/De-serialization to and from Byte Arrays
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class CreditCurve extends org.drip.service.stream.Serializer implements
	org.drip.analytics.definition.Curve {
	private static final int NUM_DF_QUADRATURES = 5;

	protected java.lang.String _strName = "";
	protected java.lang.String _strCurrency = "";
	protected double _dblEpochDate = java.lang.Double.NaN;
	protected double _dblSpecificDefaultDate = java.lang.Double.NaN;

	/*
	 * Manifest Measure Inputs that go into building the Curve Span
	 */

	protected boolean _bFlat = false;
	protected double[] _adblCalibQuote = null;
	protected java.lang.String[] _astrCalibMeasure = null;
	protected org.drip.param.pricer.PricerParams _pricerParam = null;
	protected org.drip.analytics.rates.DiscountCurve _dc = null;
	protected org.drip.analytics.rates.DiscountCurve _dcTSY = null;
	protected org.drip.analytics.rates.DiscountCurve _dcEDSF = null;
	protected org.drip.param.valuation.ValuationParams _valParam = null;
	protected org.drip.param.valuation.QuotingParams _quotingParams = null;
	protected org.drip.product.definition.CalibratableComponent[] _aCalibInst = null;
	protected org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> _mapQuote = null;
	protected org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.String> _mapMeasure = null;
	protected java.util.Map<org.drip.analytics.date.JulianDate,
		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double>> _mmFixing = null;

	protected CreditCurve (
		final double dblEpochDate,
		final java.lang.String strName,
		final java.lang.String strCurrency)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblEpochDate = dblEpochDate) || null == (_strName =
			strName) || _strName.isEmpty() || null == (_strCurrency = strCurrency) || _strCurrency.isEmpty())
			throw new java.lang.Exception ("CreditCurve ctr: Invalid Inputs");
	}

	@Override public java.lang.String name()
	{
		return _strName;
	}

	@Override public java.lang.String currency()
	{
		return _strCurrency;
	}

	@Override public org.drip.analytics.date.JulianDate epoch()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblEpochDate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Set the Specific Default Date
	 * 
	 * @param dblSpecificDefaultDate Date of Specific Default
	 * 
	 * @return TRUE if successful
	 */

	public boolean setSpecificDefault (
		final double dblSpecificDefaultDate)
	{
		_dblSpecificDefaultDate = dblSpecificDefaultDate;
		return true;
	}

	/**
	 * Remove the Specific Default Date
	 * 
	 * @return TRUE if successful
	 */

	public boolean unsetSpecificDefault()
	{
		_dblSpecificDefaultDate = java.lang.Double.NaN;
		return true;
	}

	/**
	 * Calculate the survival to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public abstract double getSurvival (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Calculate the survival to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public double getSurvival (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("CreditCurve::getSurvival => Invalid Date");

		return getSurvival (dt.getJulian());
	}

	/**
	 * Calculate the survival to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public double getSurvival (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("CreditCurve::getSurvival => Bad tenor");

		return getSurvival (new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor (strTenor));
	}

	/**
	 * Calculate the time-weighted survival between a pair of 2 dates
	 * 
	 * @param dblDate1 First Date
	 * @param dblDate2 Second Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public double getEffectiveSurvival (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (dblDate1 == dblDate2) return getSurvival (dblDate1);

		int iNumQuadratures = 0;
		double dblEffectiveSurvival = 0.;
		double dblQuadratureWidth = (dblDate2 - dblDate1) / NUM_DF_QUADRATURES;

		for (double dblDate = dblDate1; dblDate <= dblDate2; dblDate += dblQuadratureWidth) {
			++iNumQuadratures;

			dblEffectiveSurvival += (getSurvival (dblDate) + getSurvival (dblDate + dblQuadratureWidth));
		}

		return dblEffectiveSurvival / (2. * iNumQuadratures);
	}

	/**
	 * Calculate the time-weighted survival between a pair of 2 dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public double getEffectiveSurvival (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2)
			throw new java.lang.Exception ("CreditCurve::getEffectiveSurvival => Invalid date");

		return getEffectiveSurvival (dt1.getJulian(), dt2.getJulian());
	}

	/**
	 * Calculate the time-weighted survival between a pair of 2 tenors
	 * 
	 * @param strTenor1 First tenor
	 * @param strTenor2 Second tenor
	 * 
	 * @return Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the survival probability cannot be calculated
	 */

	public double getEffectiveSurvival (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("CreditCurve::getEffectiveSurvival => bad tenor");

		return getEffectiveSurvival (new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor
			(strTenor1), new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor (strTenor2));
	}

	/**
	 * Calculate the recovery rate to the given date
	 * 
	 * @param dblDate Date
	 * 
	 * @return Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery rate cannot be calculated
	 */

	public abstract double getRecovery (
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Calculate the recovery rate to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery rate cannot be calculated
	 */

	public double getRecovery (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("CreditCurve::getRecovery => Invalid Date");

		return getRecovery (dt.getJulian());
	}

	/**
	 * Calculate the recovery rate to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery rate cannot be calculated
	 */

	public double getRecovery (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("CreditCurve::getRecovery => Invalid Tenor");

		return getRecovery (new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor (strTenor));
	}

	/**
	 * Calculate the time-weighted recovery between a pair of dates
	 * 
	 * @param dblDate1 First Date
	 * @param dblDate2 Second Date
	 * 
	 * @return Time-weighted recovery
	 * 
	 * @throws java.lang.Exception Thrown if the recovery cannot be calculated
	 */

	public double getEffectiveRecovery (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (dblDate1 == dblDate2) return getRecovery (dblDate1);

		int iNumQuadratures = 0;
		double dblEffectiveRecovery = 0.;
		double dblQuadratureWidth = (dblDate2 - dblDate1) / NUM_DF_QUADRATURES;

		for (double dblDate = dblDate1; dblDate <= dblDate2; dblDate += dblQuadratureWidth) {
			++iNumQuadratures;

			dblEffectiveRecovery += (getRecovery (dblDate) + getRecovery (dblDate + dblQuadratureWidth));
		}

		return dblEffectiveRecovery / (2. * iNumQuadratures);
	}

	/**
	 * Calculate the time-weighted recovery between a pair of dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Time-weighted recovery
	 * 
	 * @throws java.lang.Exception Thrown if the recovery cannot be calculated
	 */

	public double getEffectiveRecovery (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2)
			throw new java.lang.Exception ("CreditCurve::getEffectiveRecovery => Invalid date");

		return getEffectiveRecovery (dt1.getJulian(), dt2.getJulian());
	}

	/**
	 * Calculate the time-weighted recovery between a pair of tenors
	 * 
	 * @param strTenor1 First Tenor
	 * @param strTenor2 Second Tenor
	 * 
	 * @return Time-weighted recovery
	 * 
	 * @throws java.lang.Exception Thrown if the recovery cannot be calculated
	 */

	public double getEffectiveRecovery (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception
	{
		if (null == strTenor1 || strTenor1.isEmpty() || null == strTenor2 || strTenor2.isEmpty())
			throw new java.lang.Exception ("CreditCurve::getEffectiveRecovery => Invalid tenor");

		return getEffectiveRecovery (new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor
			(strTenor1), new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor (strTenor2));
	}

	/**
	 * Calculate the hazard rate between a pair of forward dates
	 * 
	 * @param dt1 First Date
	 * @param dt2 Second Date
	 * 
	 * @return Hazard Rate
	 * 
	 * @throws java.lang.Exception Thrown if the hazard rate cannot be calculated
	 */

	public double calcHazard (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception
	{
		if (null == dt1 || null == dt2)
			throw new java.lang.Exception ("CreditCurve::calcHazard => Invalid dates");

		if (dt1.getJulian() < _dblEpochDate || dt2.getJulian() < _dblEpochDate) return 0.;

		return 365.25 / (dt2.getJulian() - dt1.getJulian()) * java.lang.Math.log (getSurvival (dt1) /
			getSurvival (dt2));
	}

	/**
	 * Calculate the hazard rate to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return Hazard Rate
	 * 
	 * @throws java.lang.Exception Thrown if the hazard rate cannot be calculated
	 */

	public double calcHazard (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		return calcHazard (dt, new org.drip.analytics.date.JulianDate (_dblEpochDate));
	}

	/**
	 * Calculate the hazard rate to the given tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return Hazard Rate
	 * 
	 * @throws java.lang.Exception Thrown if the hazard rate cannot be calculated
	 */

	public double calcHazard (
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("CreditCurve::calcHazard => Bad Tenor");

		return calcHazard (new org.drip.analytics.date.JulianDate (_dblEpochDate).addTenor (strTenor));
	}

	/**
	 * Create a flat hazard curve from the inputs
	 * 
	 * @param dblFlatNodeValue Flat hazard node value
	 * @param bSingleNode Uses a single node for Calibration (True)
	 * @param dblRecovery (Optional) Recovery to be used in creation of the flat curve
	 * 
	 * @return New CreditCurve instance
	 */

	public abstract CreditCurve createFlatCurve (
		final double dblFlatNodeValue,
		final boolean bSingleNode,
		final double dblRecovery);

	/**
	 * Set the calibration inputs for the CreditCurve
	 * 
	 * @param valParam ValuationParams
	 * @param bFlat Flat calibration desired (True)
	 * @param dc Base Discount Curve
	 * @param dcTSY Treasury Discount Curve
	 * @param dcEDSF EDSF Discount Curve
	 * @param pricerParam PricerParams
	 * @param aCalibInst Array of calibration instruments
	 * @param adblCalibQuote Array of calibration quotes
	 * @param astrCalibMeasure Array of calibration measures
	 * @param mmFixing Fixings object
	 * @param quotingParams Quoting Parameters
	 */

	public void setInstrCalibInputs (
		final org.drip.param.valuation.ValuationParams valParam,
		final boolean bFlat,
		final org.drip.analytics.rates.DiscountCurve dc,
		final org.drip.analytics.rates.DiscountCurve dcTSY,
		final org.drip.analytics.rates.DiscountCurve dcEDSF,
		final org.drip.param.pricer.PricerParams pricerParam,
		final org.drip.product.definition.CalibratableComponent[] aCalibInst,
		final double[] adblCalibQuote,
		final java.lang.String[] astrCalibMeasure,
		final java.util.Map<org.drip.analytics.date.JulianDate,
			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double>> mmFixing,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		_dc = dc;
		_bFlat = bFlat;
		_dcTSY = dcTSY;
		_dcEDSF = dcEDSF;
		_valParam = valParam;
		_mmFixing = mmFixing;
		_aCalibInst = aCalibInst;
		_pricerParam = pricerParam;
		_quotingParams = quotingParams;
		_adblCalibQuote = adblCalibQuote;
		_astrCalibMeasure = astrCalibMeasure;

		_mapQuote = new org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double>();

		_mapMeasure = new org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.String>();

		for (int i = 0; i < aCalibInst.length; ++i) {
			_mapMeasure.put (_aCalibInst[i].getPrimaryCode(), astrCalibMeasure[i]);

			_mapQuote.put (_aCalibInst[i].getPrimaryCode(), adblCalibQuote[i]);

			java.lang.String[] astrSecCode = _aCalibInst[i].getSecondaryCode();

			if (null != astrSecCode) {
				for (int j = 0; j < astrSecCode.length; ++j)
					_mapQuote.put (astrSecCode[j], adblCalibQuote[i]);
			}
		}
	}

	@Override public boolean setCCIS (
		final org.drip.analytics.definition.CurveConstructionInputSet ccis)
	{
		return false;
	}

	@Override public org.drip.product.definition.CalibratableComponent[] calibComp()
	{
		return _aCalibInst;
	}

	@Override public org.drip.state.representation.LatentStateMetricMeasure[] lsmm()
	{
		if (null == _adblCalibQuote) return null;

		int iNumLSMM = _adblCalibQuote.length;
		org.drip.state.representation.LatentStateMetricMeasure[] aLSMM = new
			org.drip.state.representation.LatentStateMetricMeasure[iNumLSMM];

		if (0 == iNumLSMM) return null;

		for (int i = 0; i < iNumLSMM; ++i) {
			try {
				aLSMM[i] = new org.drip.state.representation.LatentStateMetricMeasure
					(org.drip.state.representation.LatentStateMetricMeasure.LATENT_STATE_SURVIVAL,
						org.drip.state.representation.LatentStateMetricMeasure.QUANTIFICATION_METRIC_FORWARD_HAZARD_RATE,
							_astrCalibMeasure[i], _adblCalibQuote[i]);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		return aLSMM;
	}

	@Override public double manifestMeasure (
		final java.lang.String strInstr)
		throws java.lang.Exception
	{
		if (null == _mapQuote || 0 == _mapQuote.size() || null == strInstr || strInstr.isEmpty() ||
			!_mapQuote.containsKey (strInstr))
			throw new java.lang.Exception ("CreditCurve::getManifestMeasure => Cannot get " + strInstr);

		return _mapQuote.get (strInstr);
	}
}

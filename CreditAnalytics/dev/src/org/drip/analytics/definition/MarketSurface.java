
package org.drip.analytics.definition;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * MarketSurface exposes the stub that implements the market surface that holds the latent state's
 * 	Evolution parameters.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class MarketSurface extends org.drip.service.stream.Serializer implements
	org.drip.analytics.definition.Curve {
	protected java.lang.String _strName = "";
	protected java.lang.String _strCurrency = "";
	protected double _dblEpochDate = java.lang.Double.NaN;

	protected MarketSurface (
		final double dblEpochDate,
		final java.lang.String strName,
		final java.lang.String strCurrency)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblEpochDate = dblEpochDate) || null == (_strName =
			strName) || _strName.isEmpty() || null == (_strCurrency = strCurrency) || _strCurrency.isEmpty())
			throw new java.lang.Exception ("MarketSurface ctr: Invalid Inputs");
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

	@Override public boolean setCCIS (
		final org.drip.analytics.definition.CurveConstructionInputSet ccis)
	{
		return false;
	}

	@Override public org.drip.product.definition.CalibratableFixedIncomeComponent[] calibComp()
	{
		return null;
	}

	@Override public org.drip.state.representation.LatentStateMetricMeasure[] lsmm()
	{
		return null;
	}

	@Override public double manifestMeasure (
		final java.lang.String strInstr)
		throws java.lang.Exception
	{
		throw new java.lang.Exception ("MarketSurface::manifestMeasure => Cannot get " + strInstr);
	}

	@Override public org.drip.state.representation.LatentState parallelShiftManifestMeasure (
		final double dblShift)
	{
		return null;
	}

	@Override public org.drip.state.representation.LatentState shiftManifestMeasure (
		final int iSpanIndex,
		final double dblShift)
	{
		return null;
	}

	@Override public org.drip.state.representation.LatentState customTweakManifestMeasure (
		final org.drip.param.definition.ResponseValueTweakParams rvtp)
	{
		return null;
	}

	@Override public org.drip.state.representation.LatentState parallelShiftQuantificationMetric (
		final double dblShift)
	{
		return null;
	}

	@Override public org.drip.state.representation.LatentState customTweakQuantificationMetric (
		final org.drip.param.definition.ResponseValueTweakParams rvtp)
	{
		return null;
	}

	/**
	 * Get the Market Node given the Strike and the Maturity
	 * 
	 * @param dblStrike The Strike
	 * @param dblDate The Julian Maturity Date
	 * 
	 * @return The Volatility evaluated from the Volatility Surface
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public abstract double node (
		final double dblStrike,
		final double dblDate)
		throws java.lang.Exception;

	/**
	 * Get the Market Node given the Strike and the Maturity
	 * 
	 * @param dblStrike The Strike
	 * @param dt The Julian Maturity Date
	 * 
	 * @return The Volatility evaluated from the Volatility Surface
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double node (
		final double dblStrike,
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt) throw new java.lang.Exception ("MarketSurface::node => Invalid Inputs");

		return node (dblStrike, dt.getJulian());
	}

	/**
	 * Get the Market Node given the Strike and the Tenor
	 * 
	 * @param dblStrike The Strike
	 * @param strTenor The Maturity Tenor
	 * 
	 * @return The Volatility evaluated from the Volatility Surface
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public double node (
		final double dblStrike,
		final java.lang.String strTenor)
		throws java.lang.Exception
	{
		if (null == strTenor || strTenor.isEmpty())
			throw new java.lang.Exception ("MarketSurface::node => Invalid Inputs");

		return node (dblStrike, epoch().addTenor (strTenor).getJulian());
	}

	/**
	 * Extract the Term Structure Constructed at the Strike Anchor Node
	 * 
	 * @param dblStrike The Strike Anchor Node
	 * 
	 * @return The Term Structure Constructed at the Strike Anchor Node
	 */

	public abstract org.drip.analytics.definition.TermStructure strikeAnchorTermStructure (
		final double dblStrikeAnchor);

	/**
	 * Extract the Term Structure Constructed at the Maturity Anchor Node
	 * 
	 * @param dblMaturityDateAnchor The Maturity Date Anchor
	 * 
	 * @return The Term Structure Constructed at the Maturity Anchor Node
	 */

	public abstract org.drip.analytics.definition.TermStructure maturityAnchorTermStructure (
		final double dblMaturityDateAnchor);

	/**
	 * Extract the Term Structure Constructed at the Maturity Anchor Node
	 * 
	 * @param dtMaturityAnchor The Maturity Date Anchor
	 * 
	 * @return The Term Structure Constructed at the Maturity Anchor Node
	 */

	public org.drip.analytics.definition.TermStructure maturityAnchorTermStructure (
		final org.drip.analytics.date.JulianDate dtMaturityAnchor)
	{
		return null == dtMaturityAnchor ? null : maturityAnchorTermStructure (dtMaturityAnchor.getJulian());
	}

	/**
	 * Extract the Term Structure Constructed at the Maturity Anchor Tenor
	 * 
	 * @param strTenorAnchor The Anchor Tenor
	 * 
	 * @return The Term Structure Constructed at the Maturity Anchor Tenor
	 */

	public org.drip.analytics.definition.TermStructure maturityAnchorTermStructure (
		final java.lang.String strTenorAnchor)
	{
		return null == strTenorAnchor || strTenorAnchor.isEmpty() ? null : maturityAnchorTermStructure
			(epoch().addTenor (strTenorAnchor).getJulian());
	}
}

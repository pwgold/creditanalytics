
package org.drip.product.rates;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * 
 * This file is part of CreditAnalytics, a free-software/open-source library for fixed income analysts and
 * 		developers - http://www.credit-trader.org
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
 * Implements the InterestRateSwap product contract/valuation details.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FloatingStream extends org.drip.product.definition.RatesComponent {
	private static final boolean s_bBlog = false;

	private double _dblNotional = 100.;
	private double _dblSpread = 0.0001;
	private java.lang.String _strIR = "";
	private java.lang.String _strCode = "";
	private boolean _bApplyAccEOMAdj = false;
	private boolean _bApplyCpnEOMAdj = false;
	private double _dblMaturity = java.lang.Double.NaN;
	private double _dblEffective = java.lang.Double.NaN;
	private java.lang.String _strFloatingRateIndex = "USD-LIBOR-3M";
	private org.drip.product.params.FactorSchedule _notlSchedule = null;
	private org.drip.param.valuation.CashSettleParams _settleParams = null;
	private java.util.List<org.drip.analytics.period.CouponPeriod> _lsCouponPeriod = null;

	@Override protected org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> calibMeasures (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		return null;
	}

	/**
	 * FloatingStream constructor
	 * 
	 * @param dblEffective Effective Date
	 * @param dblMaturity Maturity Date
	 * @param dblCoupon Coupon
	 * @param iFreq Frequency
	 * @param strCouponDC Coupon Day Count
	 * @param strAccrualDC Accrual Day Count
	 * @param strFloatingRateIndex Floating Rate Index
	 * @param bFullStub TRUE => Generate full first-stub
	 * @param dapEffective Effective DAP
	 * @param dapMaturity Maturity DAP
	 * @param dapPeriodStart Period Start DAP
	 * @param dapPeriodEnd Period End DAP
	 * @param dapAccrualStart Accrual Start DAP
	 * @param dapAccrualEnd Accrual End DAP
	 * @param dapPay Pay DAP
	 * @param dapReset Reset DAP
	 * @param notlSchedule Notional Schedule
	 * @param dblNotional Initial Notional Amount
	 * @param strIR IR Curve
	 * @param strCalendar Calendar
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public FloatingStream (
		final double dblEffective,
		final double dblMaturity,
		final double dblSpread,
		final int iFreq,
		final java.lang.String strCouponDC,
		final java.lang.String strAccrualDC,
		final java.lang.String strFloatingRateIndex,
		final boolean bFullStub,
		final org.drip.analytics.daycount.DateAdjustParams dapEffective,
		final org.drip.analytics.daycount.DateAdjustParams dapMaturity,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodStart,
		final org.drip.analytics.daycount.DateAdjustParams dapPeriodEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualStart,
		final org.drip.analytics.daycount.DateAdjustParams dapAccrualEnd,
		final org.drip.analytics.daycount.DateAdjustParams dapPay,
		final org.drip.analytics.daycount.DateAdjustParams dapReset,
		final org.drip.product.params.FactorSchedule notlSchedule,
		final double dblNotional,
		final java.lang.String strIR,
		final java.lang.String strCalendar)
		throws java.lang.Exception
	{
		if (null == (_strIR = strIR) || _strIR.isEmpty() || !org.drip.math.common.NumberUtil.IsValid
			(_dblEffective = dblEffective) || !org.drip.math.common.NumberUtil.IsValid (_dblMaturity =
				dblMaturity) || !org.drip.math.common.NumberUtil.IsValid (_dblSpread = dblSpread) ||
					!org.drip.math.common.NumberUtil.IsValid (_dblNotional = dblNotional))
			throw new java.lang.Exception ("FloatingStream ctr => Invalid Input params!");

		if (null == (_notlSchedule = notlSchedule))
			_notlSchedule = org.drip.product.params.FactorSchedule.CreateBulletSchedule();

		_strFloatingRateIndex = strIR + "-LIBOR-3M";

		if (null != strFloatingRateIndex && strFloatingRateIndex.isEmpty())
			_strFloatingRateIndex = strFloatingRateIndex;

		if (null == (_lsCouponPeriod = org.drip.analytics.period.CouponPeriod.GeneratePeriodsBackward (
			dblEffective, // Effective
			dblMaturity, // Maturity
			dapEffective, // Effective DAP
			dapMaturity, // Maturity DAP
			dapPeriodStart, // Period Start DAP
			dapPeriodEnd, // Period End DAP
			dapAccrualStart, // Accrual Start DAP
			dapAccrualEnd, // Accrual End DAP
			dapPay, // Pay DAP
			dapReset, // Reset DAP
			iFreq, // Coupon Freq
			strCouponDC, // Coupon Day Count
			_bApplyCpnEOMAdj,
			strAccrualDC, // Accrual Day Count
			_bApplyAccEOMAdj,
			bFullStub, // Full First Coupon Period?
			false, // Merge the first 2 Periods - create a long stub?
			false,
			strCalendar)) || 0 == _lsCouponPeriod.size())
			throw new java.lang.Exception ("FloatingStream ctr: Cannot generate Period Schedule");
	}

	/**
	 * FloatingStream de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FloatingStream cannot be properly de-serialized
	 */

	public FloatingStream (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FloatingStream de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Empty state");

		java.lang.String strSerializedFloatingStream = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedFloatingStream || strSerializedFloatingStream.isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.analytics.support.GenericUtil.Split
			(strSerializedFloatingStream, getFieldDelimiter());

		if (null == astrField || 13 > astrField.length)
			throw new java.lang.Exception ("FloatingStream de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]).doubleValue();

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate notional");

		_dblNotional = new java.lang.Double (astrField[1]).doubleValue();

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[2]))
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate coupon");

		_dblSpread = new java.lang.Double (astrField[2]).doubleValue();

		if (null == astrField[3] || astrField[3].isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate IR curve name");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[3]))
			_strIR = astrField[3];
		else
			_strIR = "";

		if (null == astrField[4] || astrField[4].isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate code");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[4]))
			_strCode = astrField[4];
		else
			_strCode = "";

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[5]))
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate Apply Acc EOM Adj");

		_bApplyAccEOMAdj = new java.lang.Boolean (astrField[5]).booleanValue();

		if (null == astrField[6] || astrField[6].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[6]))
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate Apply Cpn EOM Adj");

		_bApplyCpnEOMAdj = new java.lang.Boolean (astrField[6]).booleanValue();

		if (null == astrField[7] || astrField[7].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[7]))
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate maturity date");

		_dblMaturity = new java.lang.Double (astrField[7]).doubleValue();

		if (null == astrField[8] || astrField[8].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[8]))
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate effective date");

		_dblEffective = new java.lang.Double (astrField[8]).doubleValue();

		if (null == astrField[9] || astrField[9].isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate rate index");

		if (!org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[9]))
			_strFloatingRateIndex = astrField[9];
		else
			_strFloatingRateIndex = "";

		if (null == astrField[10] || astrField[10].isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate notional schedule");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[10]))
			_notlSchedule = null;
		else
			_notlSchedule = new org.drip.product.params.FactorSchedule (astrField[10].getBytes());

		if (null == astrField[11] || astrField[11].isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate cash settle params");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[11]))
			_settleParams = null;
		else
			_settleParams = new org.drip.param.valuation.CashSettleParams (astrField[11].getBytes());

		if (null == astrField[12] || astrField[12].isEmpty())
			throw new java.lang.Exception ("FloatingStream de-serializer: Cannot locate the periods");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[12]))
			_lsCouponPeriod = null;
		else {
			java.lang.String[] astrRecord = org.drip.analytics.support.GenericUtil.Split (astrField[12],
				getCollectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty() ||
						org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrRecord[i]))
						continue;

					if (null == _lsCouponPeriod)
						_lsCouponPeriod = new java.util.ArrayList<org.drip.analytics.period.CouponPeriod>();

					_lsCouponPeriod.add (new org.drip.analytics.period.CouponPeriod
						(astrRecord[i].getBytes()));
				}
			}
		}
	}

	@Override public java.lang.String getPrimaryCode()
	{
		return _strCode;
	}

	@Override public void setPrimaryCode (
		final java.lang.String strCode)
	{
		_strCode = strCode;
	}

	@Override public java.lang.String getComponentName()
	{
		return "FloatingStream=" + org.drip.analytics.date.JulianDate.fromJulian (_dblMaturity);
	}

	@Override public java.lang.String getTreasuryCurveName()
	{
		return "";
	}

	@Override public java.lang.String getEDSFCurveName()
	{
		return "";
	}

	@Override public double getInitialNotional()
	{
		return _dblNotional;
	}

	@Override public double getNotional (
		final double dblDate)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("FloatingStream::getNotional => Bad date into getNotional");

		return _notlSchedule.getFactor (dblDate);
	}

	@Override public double getNotional (
		final double dblDate1,
		final double dblDate2)
		throws java.lang.Exception
	{
		if (null == _notlSchedule || !org.drip.math.common.NumberUtil.IsValid (dblDate1) ||
			!org.drip.math.common.NumberUtil.IsValid (dblDate2))
			throw new java.lang.Exception ("FloatingStream::getNotional => Bad date into getNotional");

		return _notlSchedule.getFactor (dblDate1, dblDate2);
	}

	@Override public boolean setCurves (
		final java.lang.String strIR,
		final java.lang.String strIRTSY, final
		java.lang.String strCC)
	{
		if (null == strIR || strIR.isEmpty()) return false;

		_strIR = strIR;
		return true;
	}

	@Override public double getCoupon (
		final double dblValueDate,
		final org.drip.param.definition.ComponentMarketParams mktParams)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblValueDate) || null == mktParams)
			throw new java.lang.Exception ("FloatingStream::getCoupon => Invalid Inputs");

		org.drip.analytics.period.Period currentPeriod = null;

		for (org.drip.analytics.period.CouponPeriod period : _lsCouponPeriod) {
			if (null == period) continue;

			if (dblValueDate >= period.getStartDate() && dblValueDate < period.getEndDate()) {
				currentPeriod = period;
				break;
			}
		}

		if (null == currentPeriod)
			throw new java.lang.Exception ("FloatingStream::getCoupon => Invalid Inputs");

		java.util.Map<org.drip.analytics.date.JulianDate,
			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double>> mapFixings =
				mktParams.getFixings();

		if (null != mapFixings) {
			double dblCurrentResetDate = currentPeriod.getResetDate();

			if (org.drip.math.common.NumberUtil.IsValid (dblCurrentResetDate)) {
				org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapIndexFixing = mapFixings.get (new
					org.drip.analytics.date.JulianDate (dblCurrentResetDate));

				if (null != mapIndexFixing) {
					java.lang.Double dblFixing = mapIndexFixing.get (_strFloatingRateIndex);

					if (null != dblFixing) return dblFixing + _dblSpread;
				}
			}
		}

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc) throw new java.lang.Exception ("FloatingStream::getCoupon => cant determine index");

		return dc.calcLIBOR (currentPeriod.getStartDate(), currentPeriod.getEndDate()) + _dblSpread;
	}

	@Override public java.lang.String getIRCurveName()
	{
		return _strIR;
	}

	@Override public java.lang.String getRatesForwardCurveName()
	{
		return _strFloatingRateIndex;
	}

	@Override public java.lang.String getCreditCurveName()
	{
		return "";
	}

	@Override public org.drip.analytics.date.JulianDate getEffectiveDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblEffective);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getMaturityDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_dblMaturity);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.analytics.date.JulianDate getFirstCouponDate()
	{
		try {
			return new org.drip.analytics.date.JulianDate (_lsCouponPeriod.get (0).getEndDate());
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public java.util.List<org.drip.analytics.period.CouponPeriod> getCouponPeriod()
	{
		return _lsCouponPeriod;
	}

	@Override public org.drip.param.valuation.CashSettleParams getCashSettleParams()
	{
		return _settleParams;
	}

	@Override public org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> value (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || null == mktParams) return null;

		org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

		if (null == dc) return null;

		long lStart = System.nanoTime();

		double dblFixing01 = 0.;
		double dblAccrued01 = 0.;
		double dblDirtyDV01 = 0.;
		boolean bFirstPeriod = true;
		double dblDirtyFloatingPV = 0.;
		double dblCashPayDF = java.lang.Double.NaN;
		double dblResetDate = java.lang.Double.NaN;
		double dblResetRate = java.lang.Double.NaN;

		org.drip.analytics.definition.DiscountCurve dcForward = null == mktParams.getForwardDiscountCurve() ?
			dc : mktParams.getForwardDiscountCurve();

		for (org.drip.analytics.period.CouponPeriod period : _lsCouponPeriod) {
			double dblFloatingRate = 0.;
			double dblDirtyPeriodDV01 = java.lang.Double.NaN;

			double dblPeriodPayDate = period.getPayDate();

			if (dblPeriodPayDate < valParams._dblValue) continue;

			try {
				if (bFirstPeriod) {
					bFirstPeriod = false;

					if (null == mktParams.getFixings() || null == mktParams.getFixings().get (new
						org.drip.analytics.date.JulianDate (period.getResetDate())) || null ==
							mktParams.getFixings().get (new org.drip.analytics.date.JulianDate
								(period.getResetDate())).get (_strFloatingRateIndex))
						dblResetRate = dblFloatingRate = dcForward.calcLIBOR (period.getStartDate(),
							period.getEndDate());
					else
						dblResetRate = dblFloatingRate = mktParams.getFixings().get (new
							org.drip.analytics.date.JulianDate (period.getResetDate())).get
								(_strFloatingRateIndex);

					dblFixing01 = period.getAccrualDCF (valParams._dblValue) * 0.01 * getNotional
						(period.getAccrualStartDate(), valParams._dblValue);

					if (period.getStartDate() < valParams._dblValue) dblAccrued01 = dblFixing01;

					dblResetDate = period.getResetDate();
				} else
					dblFloatingRate = dcForward.calcLIBOR (period.getStartDate(), period.getEndDate());

				dblDirtyPeriodDV01 = 0.01 * period.getCouponDCF() * mktParams.getDiscountCurve().getDF
					(dblPeriodPayDate) * getNotional (period.getAccrualStartDate(), period.getEndDate());
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			if (s_bBlog) {
				try {
					System.out.println (new org.drip.analytics.date.JulianDate (period.getResetDate()) + " ["
						+ new org.drip.analytics.date.JulianDate (period.getStartDate()) + "->" + new
							org.drip.analytics.date.JulianDate (period.getEndDate()) + "] => " +
								org.drip.math.common.FormatUtil.FormatDouble (dblFloatingRate, 1, 4, 100.));
				} catch (java.lang.Exception e) {
					e.printStackTrace();
				}
			}

			dblDirtyDV01 += dblDirtyPeriodDV01;
			dblDirtyFloatingPV += dblDirtyPeriodDV01 * dblFloatingRate;
		}

		try {
			double dblCashSettle = valParams._dblCashPay;

			if (null != _settleParams) dblCashSettle = _settleParams.cashSettleDate (valParams._dblValue);

			dblCashPayDF = mktParams.getDiscountCurve().getDF (dblCashSettle);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		dblDirtyDV01 /= dblCashPayDF;
		dblDirtyFloatingPV /= dblCashPayDF;
		double dblNotlFactor = _dblNotional * 0.01;
		double dblCleanDV01 = dblDirtyDV01 - dblAccrued01;
		double dblCleanFloatingPV = dblDirtyFloatingPV - dblAccrued01 * (dblResetRate + _dblSpread);

		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapResult = new
			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double>();

		mapResult.put ("ResetDate", dblResetDate);

		mapResult.put ("ResetRate", dblResetRate);

		mapResult.put ("Accrued01", dblAccrued01 * dblNotlFactor);

		mapResult.put ("Fixing01", dblFixing01 * dblNotlFactor);

		mapResult.put ("FloatAccrued", dblAccrued01 * (dblResetRate + _dblSpread) * dblNotlFactor);

		mapResult.put ("DV01", dblCleanDV01 * dblNotlFactor);

		mapResult.put ("CleanDV01", dblCleanDV01 * dblNotlFactor);

		mapResult.put ("DirtyDV01", dblDirtyDV01 * dblNotlFactor);

		mapResult.put ("CleanFloatingPV", dblCleanFloatingPV * dblNotlFactor);

		mapResult.put ("DirtyFloatingPV", dblDirtyFloatingPV * dblNotlFactor);

		mapResult.put ("PV", dblCleanFloatingPV * dblNotlFactor);

		mapResult.put ("CleanPV", dblCleanFloatingPV * dblNotlFactor);

		mapResult.put ("DirtyPV", dblDirtyFloatingPV * dblNotlFactor);

		mapResult.put ("Upfront", dblCleanFloatingPV * dblNotlFactor);

		mapResult.put ("FairPremium", dblCleanFloatingPV / dblCleanDV01);

		mapResult.put ("Rate", dblCleanFloatingPV / dblCleanDV01);

		mapResult.put ("ParRate", dblCleanFloatingPV / dblCleanDV01);

		double dblValueNotional = java.lang.Double.NaN;

		try {
			dblValueNotional = getNotional (valParams._dblValue);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		if (org.drip.math.common.NumberUtil.IsValid (dblValueNotional)) {
			double dblPrice = 100. * (1. + (dblCleanFloatingPV / _dblNotional / dblValueNotional));

			mapResult.put ("Price", dblPrice);

			mapResult.put ("CleanPrice", dblPrice);
		}

		mapResult.put ("CalcTime", (System.nanoTime() - lStart) * 1.e-09);

		return mapResult;
	}

	@Override public java.util.Set<java.lang.String> getMeasureNames()
	{
		java.util.Set<java.lang.String> setstrMeasureNames = new java.util.TreeSet<java.lang.String>();

		setstrMeasureNames.add ("Accrued01");

		setstrMeasureNames.add ("CalcTime");

		setstrMeasureNames.add ("CleanFloatingPV");

		setstrMeasureNames.add ("CleanDV01");

		setstrMeasureNames.add ("CleanPrice");

		setstrMeasureNames.add ("CleanPV");

		setstrMeasureNames.add ("DirtyFloatingPV");

		setstrMeasureNames.add ("DirtyDV01");

		setstrMeasureNames.add ("DirtyPrice");

		setstrMeasureNames.add ("DirtyPV");

		setstrMeasureNames.add ("DV01");

		setstrMeasureNames.add ("FairPremium");

		setstrMeasureNames.add ("Fixing01");

		setstrMeasureNames.add ("FloatAccrued");

		setstrMeasureNames.add ("ParRate");

		setstrMeasureNames.add ("Price");

		setstrMeasureNames.add ("PV");

		setstrMeasureNames.add ("Rate");

		setstrMeasureNames.add ("ResetDate");

		setstrMeasureNames.add ("ResetRate");

		setstrMeasureNames.add ("Upfront");

		return setstrMeasureNames;
	}

	@Override public org.drip.math.calculus.WengertJacobian calcPVDFMicroJack (
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == mktParams || null ==
			mktParams.getDiscountCurve())
			return null;

		org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapMeasures = value (valParams,
			pricerParams, mktParams, quotingParams);

		if (null == mapMeasures) return null;

		double dblPV = mapMeasures.get ("PV");

		double dblParSwapRate = mapMeasures.get ("SwapRate");

		try {
			org.drip.math.calculus.WengertJacobian wjPVDFMicroJack = null;

			org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

			for (org.drip.analytics.period.CouponPeriod p : _lsCouponPeriod) {
				double dblPeriodPayDate = p.getPayDate();

				if (dblPeriodPayDate < valParams._dblValue) continue;

				org.drip.math.calculus.WengertJacobian wjPeriodFwdRateDF = dc.getForwardRateJacobian
					(p.getStartDate(), p.getEndDate());

				org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF = dc.getDFJacobian (dblPeriodPayDate);

				if (null == wjPeriodFwdRateDF || null == wjPeriodPayDFDF) continue;

				double dblForwardRate = dc.calcLIBOR (p.getStartDate(), p.getEndDate());

				double dblPeriodPayDF = dc.getDF (dblPeriodPayDate);

				if (null == wjPVDFMicroJack)
					wjPVDFMicroJack = new org.drip.math.calculus.WengertJacobian (1,
						wjPeriodFwdRateDF.numParameters());

				double dblPeriodNotional = getNotional (p.getStartDate(), p.getEndDate());

				double dblPeriodDCF = p.getCouponDCF();

				for (int k = 0; k < wjPeriodFwdRateDF.numParameters(); ++k) {
					double dblPeriodPVDFMicroJack = dblPeriodDCF * ((dblParSwapRate - dblForwardRate) *
						wjPeriodPayDFDF.getFirstDerivative (0, k) - dblPeriodPayDF *
							wjPeriodFwdRateDF.getFirstDerivative (0, k));

					if (!wjPVDFMicroJack.accumulatePartialFirstDerivative (0, k, dblPeriodNotional *
						dblPeriodDCF * dblPeriodPVDFMicroJack))
						return null;
				}
			}

			return adjustPVDFMicroJackForCashSettle (valParams._dblCashPay, dblPV, dc, wjPVDFMicroJack) ?
				wjPVDFMicroJack : null;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public org.drip.math.calculus.WengertJacobian calcQuoteDFMicroJack (
		final java.lang.String strQuote,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final org.drip.param.definition.ComponentMarketParams mktParams,
		final org.drip.param.valuation.QuotingParams quotingParams)
	{
		if (null == valParams || valParams._dblValue >= _dblMaturity || null == strQuote || null == mktParams
			|| null == mktParams.getDiscountCurve())
			return null;

		if ("Rate".equalsIgnoreCase (strQuote) || "SwapRate".equalsIgnoreCase (strQuote)) {
			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapMeasures = value (valParams,
				pricerParams, mktParams, quotingParams);

			if (null == mapMeasures) return null;

			double dblDirtyDV01 = mapMeasures.get ("DirtyDV01");

			double dblParSwapRate = mapMeasures.get ("SwapRate");

			try {
				org.drip.math.calculus.WengertJacobian wjSwapRateDFMicroJack = null;

				org.drip.analytics.definition.DiscountCurve dc = mktParams.getDiscountCurve();

				for (org.drip.analytics.period.CouponPeriod p : _lsCouponPeriod) {
					double dblPeriodPayDate = p.getPayDate();

					if (dblPeriodPayDate < valParams._dblValue) continue;

					org.drip.math.calculus.WengertJacobian wjPeriodFwdRateDF = dc.getForwardRateJacobian
						(p.getStartDate(), p.getEndDate());

					org.drip.math.calculus.WengertJacobian wjPeriodPayDFDF = dc.getDFJacobian
						(dblPeriodPayDate);

					if (null == wjPeriodFwdRateDF || null == wjPeriodPayDFDF) continue;

					double dblForwardRate = dc.calcLIBOR (p.getStartDate(), p.getEndDate());

					double dblPeriodPayDF = dc.getDF (dblPeriodPayDate);

					if (null == wjSwapRateDFMicroJack)
						wjSwapRateDFMicroJack = new org.drip.math.calculus.WengertJacobian (1,
							wjPeriodFwdRateDF.numParameters());

					double dblPeriodNotional = getNotional (p.getStartDate(), p.getEndDate());

					double dblPeriodDCF = p.getCouponDCF();

					for (int k = 0; k < wjPeriodFwdRateDF.numParameters(); ++k) {
						double dblPeriodMicroJack = (dblForwardRate - dblParSwapRate) *
							wjPeriodPayDFDF.getFirstDerivative (0, k) + dblPeriodPayDF *
								wjPeriodFwdRateDF.getFirstDerivative (0, k);

						if (!wjSwapRateDFMicroJack.accumulatePartialFirstDerivative (0, k, dblPeriodNotional
							* dblPeriodDCF * dblPeriodMicroJack / dblDirtyDV01))
							return null;
					}
				}

				return wjSwapRateDFMicroJack;
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "!";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "&";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		sb.append (_dblNotional + getFieldDelimiter());

		sb.append (_dblSpread + getFieldDelimiter());

		if (null == _strIR || _strIR.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strIR + getFieldDelimiter());

		if (null == _strCode || _strCode.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strCode + getFieldDelimiter());

		sb.append (_bApplyAccEOMAdj + getFieldDelimiter());

		sb.append (_bApplyCpnEOMAdj + getFieldDelimiter());

		sb.append (_dblMaturity + getFieldDelimiter());

		sb.append (_dblEffective + getFieldDelimiter());

		if (null == _strFloatingRateIndex || _strFloatingRateIndex.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strFloatingRateIndex + getFieldDelimiter());

		if (null == _notlSchedule)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_notlSchedule.serialize()) + getFieldDelimiter());

		if (null == _settleParams)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (new java.lang.String (_settleParams.serialize()) + getFieldDelimiter());

		if (null == _lsCouponPeriod)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbPeriods = new java.lang.StringBuffer();

			for (org.drip.analytics.period.CouponPeriod p : _lsCouponPeriod) {
				if (null == p) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbPeriods.append (getCollectionRecordDelimiter());

				sbPeriods.append (new java.lang.String (p.serialize()));
			}

			if (sbPeriods.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbPeriods.toString());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab) {
		try {
			return new FloatingStream (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		FloatingStream fs = new org.drip.product.rates.FloatingStream
			(org.drip.analytics.date.JulianDate.Today().getJulian(),
				org.drip.analytics.date.JulianDate.Today().addTenor ("4Y").getJulian(), 0.03, 2, "30/360",
					"30/360", "JPY-LIBOR", false, null, null, null, null, null, null, null, null, null, 100.,
						"JPY", "JPY");

		byte[] abFS = fs.serialize();

		System.out.println (new java.lang.String (abFS));

		FloatingStream fsDeser = new FloatingStream (abFS);

		System.out.println (new java.lang.String (fsDeser.serialize()));
	}
}

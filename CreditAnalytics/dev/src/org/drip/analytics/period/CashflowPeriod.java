
package org.drip.analytics.period;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * CashflowPeriod extends the period class with the cash-flow specific fields. It exposes the following
 * 	functionality:
 * 
 * 	- Frequency, reset date, and accrual day-count convention
 * 	- Static methods to construct cash-flow period sets starting backwards/forwards, generate single period
 * 	 sets, as well as merge cash-flow periods.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CashflowPeriod extends org.drip.service.stream.Serializer implements
	java.lang.Comparable<CashflowPeriod> {

	/*
	 * Period Date Fields
	 */

	private double _dblEndDate = java.lang.Double.NaN;
	private double _dblPayDate = java.lang.Double.NaN;
	private double _dblStartDate = java.lang.Double.NaN;
	private double _dblFXFixingDate = java.lang.Double.NaN;
	private double _dblTerminalDate = java.lang.Double.NaN;
	private double _dblAccrualEndDate = java.lang.Double.NaN;
	private double _dblAccrualStartDate = java.lang.Double.NaN;
	private java.util.List<org.drip.product.params.ResetPeriod> _lsResetPeriod = null;

	/*
	 * Period Date Generation Fields
	 */

	private int _iFreq = 2;
	private boolean _bApplyAccEOMAdj = false;
	private boolean _bApplyCpnEOMAdj = false;
	private java.lang.String _strCalendar = "";
	private double _dblDCF = java.lang.Double.NaN;
	private java.lang.String _strCouponDC = "30/360";
	private java.lang.String _strAccrualDC = "30/360";

	/*
	 * Period Latent State Identification Support Fields
	 */

	private java.lang.String _strPayCurrency = "";
	private org.drip.state.identifier.CreditLabel _creditLabel = null;
	private org.drip.state.identifier.ForwardLabel _forwardLabel = null;

	/*
	 * Period Cash Extensive Fields
	 */

	private double _dblSpread = java.lang.Double.NaN;
	private double _dblFixedCoupon = java.lang.Double.NaN;
	private double _dblEndNotional = java.lang.Double.NaN;
	private double _dblStartNotional = java.lang.Double.NaN;

	/*
	 * Period Computed Metrics
	 */

	private double _dblEndDF = java.lang.Double.NaN;
	private double _dblForwardRate = java.lang.Double.NaN;
	private double _dblEndSurvival = java.lang.Double.NaN;
	private java.util.List<org.drip.analytics.period.LossQuadratureMetrics> _lsLQM = null;

	private double resetPeriodRate (
		final double dblAccrualEndDate,
		final org.drip.product.params.ResetPeriod rp,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs)
		throws java.lang.Exception
	{
		double dblFixingDate = rp.fixing();

		if (csqs.available (dblFixingDate, _forwardLabel))
			return csqs.getFixing (dblFixingDate, _forwardLabel);

		double dblResetEndDate = rp.end();

		org.drip.analytics.rates.ForwardRateEstimator fc = csqs.forwardCurve (_forwardLabel);

		if (null != fc) return fc.forward (dblResetEndDate);

		org.drip.analytics.rates.DiscountCurve dcFunding = csqs.fundingCurve (fundingLabel());

		if (null == dcFunding)
			throw new java.lang.Exception
				("CashflowPeriod::resetPeriodRate => Cannot locate Discount Curve");

		double dblResetStartDate = rp.start();

		double dblEpochDate = dcFunding.epoch().julian();

		if (dblEpochDate > dblResetStartDate)
			dblResetEndDate = new org.drip.analytics.date.JulianDate (dblResetStartDate =
				dblEpochDate).addTenor (_forwardLabel.tenor()).julian();

		return dcFunding.libor (dblResetStartDate, dblResetEndDate, _dblDCF);
	}

	private double resetPeriodDCF (
		final double dblAccrualEndDate,
		final org.drip.product.params.ResetPeriod rp)
		throws java.lang.Exception
	{
		double dblResetStartDate = rp.start();

		if (dblAccrualEndDate < dblResetStartDate) return 0.;

		double dblResetEndDate = rp.end();

		return org.drip.analytics.daycount.Convention.YearFraction (dblResetStartDate, dblAccrualEndDate >=
			dblResetEndDate ? dblResetEndDate : dblAccrualEndDate, _strAccrualDC, _bApplyAccEOMAdj,
				_dblTerminalDate, null, _strCalendar);
	}

	private org.drip.analytics.output.PeriodCouponMeasures resetRate (
		final int iResetPeriodIndex,
		final double dblAccrualEndDate,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs)
	{
		org.drip.product.params.ResetPeriod rp = _lsResetPeriod.get (iResetPeriodIndex);

		try {
			return org.drip.analytics.output.PeriodCouponMeasures.Nominal (resetPeriodRate
				(dblAccrualEndDate, rp, csqs), resetPeriodDCF (dblAccrualEndDate, rp));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct a CashflowPeriod instance from the specified dates
	 * 
	 * @param dblStartDate Period Start Date
	 * @param dblEndDate Period End Date
	 * @param dblAccrualStart Period Accrual Start Date
	 * @param dblAccrualEndDate Period Accrual End Date
	 * @param dblPayDate Period Pay Date
	 * @param lsResetPeriod List of Reset Periods
	 * @param dblFXFixingDate The FX Fixing Date for non-MTM'ed Cash-flow
	 * @param dblTerminalDate Cash flow Terminal Date
	 * @param iFreq Frequency
	 * @param dblDCF Full Period Day Count Fraction
	 * @param strCouponDC Coupon day count
	 * @param strAccrualDC Accrual Day count
	 * @param bApplyCpnEOMAdj Apply end-of-month adjustment to the coupon periods
	 * @param bApplyAccEOMAdj Apply end-of-month adjustment to the accrual periods
	 * @param strCalendar Holiday Calendar
	 * @param strPayCurrency Pay Currency
	 * @param forwardLabel The Forward Label
	 * @param creditLabel The Credit Label
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public CashflowPeriod (
		final double dblStartDate,
		final double dblEndDate,
		final double dblAccrualStartDate,
		final double dblAccrualEndDate,
		final double dblPayDate,
		final java.util.List<org.drip.product.params.ResetPeriod> lsResetPeriod,
		final double dblFXFixingDate,
		final double dblTerminalDate,
		final int iFreq,
		final double dblDCF,
		final java.lang.String strCouponDC,
		final java.lang.String strAccrualDC,
		final boolean bApplyCpnEOMAdj,
		final boolean bApplyAccEOMAdj,
		final java.lang.String strCalendar,
		final java.lang.String strPayCurrency,
		final org.drip.state.identifier.ForwardLabel forwardLabel,
		final org.drip.state.identifier.CreditLabel creditLabel)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblStartDate = dblStartDate) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblEndDate = dblEndDate) ||
				!org.drip.quant.common.NumberUtil.IsValid (_dblAccrualStartDate = dblAccrualStartDate) ||
					!org.drip.quant.common.NumberUtil.IsValid (_dblAccrualEndDate = dblAccrualEndDate) ||
						!org.drip.quant.common.NumberUtil.IsValid (_dblPayDate = dblPayDate) ||
							!org.drip.quant.common.NumberUtil.IsValid (_dblDCF = dblDCF) || _dblStartDate >=
								_dblEndDate || _dblAccrualStartDate >= _dblAccrualEndDate || null ==
									(_strPayCurrency = strPayCurrency) || _strPayCurrency.isEmpty())
			throw new java.lang.Exception ("CashflowPeriod ctr: Invalid inputs");

		_iFreq = iFreq;
		_creditLabel = creditLabel;
		_strCalendar = strCalendar;
		_strCouponDC = strCouponDC;
		_strAccrualDC = strAccrualDC;
		_bApplyAccEOMAdj = bApplyAccEOMAdj;
		_bApplyCpnEOMAdj = bApplyCpnEOMAdj;
		_dblFXFixingDate = dblFXFixingDate;
		_dblTerminalDate = dblTerminalDate;

		if (null != (_forwardLabel = forwardLabel) && (null == (_lsResetPeriod = lsResetPeriod) || 0 ==
			_lsResetPeriod.size()))
			throw new java.lang.Exception ("CashflowPeriod ctr: Invalid Forward/Reset Combination");
	}

	/**
	 * De-serialization of CashflowPeriod from byte stream
	 * 
	 * @param ab Byte stream
	 * 
	 * @throws java.lang.Exception Thrown if cannot properly de-serialize
	 */

	public CashflowPeriod (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("CashflowPeriod de-serialize: Invalid byte stream input");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Empty state");

		java.lang.String strPeriod = strRawString.substring (0, strRawString.indexOf
			(super.objectTrailer()));

		if (null == strPeriod || strPeriod.isEmpty())
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.quant.common.StringUtil.Split (strPeriod,
			super.fieldDelimiter());

		if (null == astrField || 23 > astrField.length)
			throw new java.lang.Exception ("CashflowPeriod de-serialize: Invalid number of fields");

		// double dblVersion = new java.lang.Double (astrField[0]);

		/*
		 * Period Dates
		 */

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate start date");

		_dblStartDate = new java.lang.Double (astrField[1]);

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[2]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate end date");

		_dblEndDate = new java.lang.Double (astrField[2]);

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[3]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate accrual start date");

		_dblAccrualStartDate = new java.lang.Double (astrField[3]);

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[4]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate accrual end date");

		_dblAccrualEndDate = new java.lang.Double (astrField[4]);

		if (null == astrField[5] || astrField[5].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[5]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate pay date");

		_dblPayDate = new java.lang.Double (astrField[5]);

		if (null == astrField[6] || astrField[6].isEmpty())
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Reset Period");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[6]))
			_lsResetPeriod = new java.util.ArrayList<org.drip.product.params.ResetPeriod>();
		else {
			java.lang.String[] astrRecord = org.drip.quant.common.StringUtil.Split (astrField[6],
				collectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty() ||
						org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrRecord[i]))
						continue;

					if (null == _lsResetPeriod)
						_lsResetPeriod = new java.util.ArrayList<org.drip.product.params.ResetPeriod>();

					_lsResetPeriod.add (new org.drip.product.params.ResetPeriod (astrRecord[i].getBytes()));
				}
			}
		}

		if (null == astrField[7] || astrField[7].isEmpty())
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate FX Fixing Date");

		_dblFXFixingDate = org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[7])
			? java.lang.Double.NaN : new java.lang.Double (astrField[7]);

		if (null == astrField[8] || astrField[8].isEmpty())
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Terminal Date");

		_dblTerminalDate = org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[8])
			? java.lang.Double.NaN : new java.lang.Double (astrField[8]);

		/*
		 * Period Date Parameters - End
		 */

		/*
		 * Period Accrual/Coupon Fraction Generation Parameters
		 */

		if (null == astrField[9] || astrField[9].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[9]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Frequency");

		_iFreq = new java.lang.Integer (astrField[9]);

		if (null == astrField[10] || astrField[10].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[10]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Full Period DCF");

		_dblDCF = new java.lang.Double (astrField[10]);

		if (null == astrField[11] || astrField[11].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[11]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Coupon Day Count");

		_strCouponDC = new java.lang.String (astrField[11]);

		if (null == astrField[12] || astrField[12].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[12]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Accrual Day Count");

		_strAccrualDC = new java.lang.String (astrField[12]);

		if (null == astrField[13] || astrField[13].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[13]))
			throw new java.lang.Exception
				("CashflowPeriod de-serializer: Cannot locate Coupon EOM Adjustment");

		_bApplyCpnEOMAdj = new java.lang.Boolean (astrField[13]);

		if (null == astrField[14] || astrField[14].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[14]))
			throw new java.lang.Exception
				("CashflowPeriod de-serializer: Cannot locate Accrual EOM Adjustment");

		_bApplyAccEOMAdj = new java.lang.Boolean (astrField[14]);

		if (null == astrField[15] || astrField[15].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[15]))
			_strCalendar = "";
		else
			_strCalendar = new java.lang.String (astrField[15]);

		/*
		 * Period Accrual/Coupon Fraction Generation Parameters - End
		 */

		/*
		 * Period Latent State Identification Settings
		 */

		if (null == astrField[16] || astrField[16].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[16]))
			throw new java.lang.Exception ("CashflowPeriod de-serializer: Cannot locate Pay Currency");

		_strPayCurrency = new java.lang.String (astrField[16]);

		if (null == astrField[17] || astrField[17].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[17]))
			_forwardLabel = null;
		else
			_forwardLabel = org.drip.state.identifier.ForwardLabel.Standard (astrField[17]);

		if (null != astrField[18] && !astrField[18].isEmpty() &&
			!org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[18]))
			_creditLabel = org.drip.state.identifier.CreditLabel.Standard (astrField[18]);

		/*
		 * Period Latent State Identification Settings - End
		 */

		/*
		 * Period "Extensive Cash" Parameter Settings
		 */

		if (null == astrField[19] || astrField[19].isEmpty())
			throw new java.lang.Exception
				("CashflowPeriod de-serializer: Cannot locate Period Start Notional");

		_dblStartNotional = org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase
			(astrField[19]) ? java.lang.Double.NaN : new java.lang.Double (astrField[19]);

		if (null == astrField[20] || astrField[20].isEmpty())
			throw new java.lang.Exception
				("CashflowPeriod de-serializer: Cannot locate Period End Notional");

		_dblEndNotional = org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[20])
			? java.lang.Double.NaN : new java.lang.Double (astrField[20]);

		if (null == astrField[21] || astrField[21].isEmpty())
			throw new java.lang.Exception
				("CashflowPeriod de-serializer: Cannot locate Period Fixed Coupon");

		_dblFixedCoupon = org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[21])
			? java.lang.Double.NaN : new java.lang.Double (astrField[21]);

		if (null == astrField[22] || astrField[22].isEmpty())
			throw new java.lang.Exception
				("CashflowPeriod de-serializer: Cannot locate Period Coupon Spread");

		_dblSpread = org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[22]) ?
			java.lang.Double.NaN : new java.lang.Double (astrField[22]);

		/*
		 * Period "Extensive Cash" Parameter Settings - End
		 */

		/*
		 * Period Computed Metrics
		 */

		/*
		 * Period Computed Metrics - End
		 */

		/* if (null == astrField[24] || astrField[24].isEmpty())
			throw new java.lang.Exception ("CDSComponent de-serializer: Cannot locate the Loss Metrics");

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[24]))
			_lsLQM = null;
		else {
			java.lang.String[] astrRecord = org.drip.quant.common.StringUtil.Split (astrField[24],
				collectionRecordDelimiter());

			if (null != astrRecord && 0 != astrRecord.length) {
				for (int i = 0; i < astrRecord.length; ++i) {
					if (null == astrRecord[i] || astrRecord[i].isEmpty() ||
						org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrRecord[i]))
						continue;

					if (null == _lsLQM)
						_lsLQM = new java.util.ArrayList<org.drip.analytics.period.LossQuadratureMetrics>();

					_lsLQM.add (new org.drip.analytics.period.LossQuadratureMetrics
						(astrRecord[i].getBytes()));
				}
			}
		} */
	}

	/**
	 * Return the period Start Date
	 * 
	 * @return Period Start Date
	 */

	public double startDate()
	{
		return _dblStartDate;
	}

	/**
	 * Return the period End Date
	 * 
	 * @return Period End Date
	 */

	public double endDate()
	{
		return _dblEndDate;
	}

	/**
	 * Return the period Accrual Start Date
	 * 
	 * @return Period Accrual Start Date
	 */

	public double accrualStartDate()
	{
		return _dblAccrualStartDate;
	}

	/**
	 * Check whether the supplied date is inside the period specified
	 * 
	 * @param dblDate Date input
	 * 
	 * @return True indicates the specified date is inside the period
	 * 
	 * @throws java.lang.Exception Thrown if input is invalid
	 */

	public boolean contains (
		final double dblDate)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblDate))
			throw new java.lang.Exception ("CashflowPeriod::contains => Invalid Inputs");

		if (_dblStartDate > dblDate || dblDate > _dblEndDate) return false;

		return true;
	}

	/**
	 * Set the period Accrual Start Date
	 * 
	 * @param dblAccrualStartDate Period Accrual Start Date
	 * 
	 * @return TRUE => Accrual Start Date Successfully Set
	 */

	public boolean setAccrualStartDate (
		final double dblAccrualStartDate)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblAccrualStartDate)) return false;

		_dblAccrualStartDate = dblAccrualStartDate;
		return true;
	}

	/**
	 * Return the period Accrual End Date
	 * 
	 * @return Period Accrual End Date
	 */

	public double accrualEndDate()
	{
		return _dblAccrualEndDate;
	}

	/**
	 * Return the period Pay Date
	 * 
	 * @return Period Pay Date
	 */

	public double payDate()
	{
		return _dblPayDate;
	}

	/**
	 * Set the period Pay Date
	 * 
	 * @param dblPayDate Period Pay Date
	 * 
	 * @return TRUE => Period Pay Date Successfully set
	 */

	public boolean setPayDate (
		final double dblPayDate)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblPayDate)) return false;

		_dblPayDate = dblPayDate;
		return true;
	}

	/**
	 * Return the Reset Periods
	 * 
	 * @return The RTeset Periods
	 */

	public java.util.List<org.drip.product.params.ResetPeriod> resetPeriods()
	{
		return _lsResetPeriod;
	}

	/**
	 * Return the period FX Fixing Date
	 * 
	 * @return Period FX Fixing Date
	 */

	public double fxFixingDate()
	{
		return _dblFXFixingDate;
	}

	/**
	 * Is this Cash Flow FX MTM'ed?
	 * 
	 * @return TRUE => FX MTM is on (i.e., FX is not driven by fixing)
	 */

	public boolean isFXMTM()
	{
		return !org.drip.quant.common.NumberUtil.IsValid (_dblFXFixingDate);
	}

	/**
	 * Return the Terminal Date
	 * 
	 * @return The Terminal Date
	 */

	public double terminalDate()
	{
		return _dblTerminalDate;
	}

	/**
	 * Retrieve the Coupon Day Count
	 * 
	 * @return The Coupon Day Count
	 */

	public java.lang.String couponDC()
	{
		return _strCouponDC;
	}

	/**
	 * Retrieve the Coupon EOM Adjustment Flag
	 * 
	 * @return The Coupon EOM Adjustment Flag
	 */

	public boolean couponEODAdjustment()
	{
		return _bApplyCpnEOMAdj;
	}

	/**
	 * Retrieve the Accrual Day Count
	 * 
	 * @return The Accrual Day Count
	 */

	public java.lang.String accrualDC()
	{
		return _strAccrualDC;
	}

	/**
	 * Retrieve the Accrual EOM Adjustment Flag
	 * 
	 * @return The Accrual EOM Adjustment Flag
	 */

	public boolean accrualEODAdjustment()
	{
		return _bApplyAccEOMAdj;
	}

	/**
	 * Get the coupon DCF
	 * 
	 * @return The coupon DCF
	 */

	public double couponDCF()
	{
		return _dblDCF;
	}

	/**
	 * Get the period Accrual Day Count Fraction to an accrual end date
	 * 
	 * @param dblAccrualEnd Accrual End Date
	 * 
	 * @exception Throws if inputs are invalid, or if the date does not lie within the period
	 */

	public double accrualDCF (
		final double dblAccrualEnd)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblAccrualEnd))
			throw new java.lang.Exception ("CashflowPeriod::accrualDCF => Accrual end is NaN!");

		if (_dblAccrualStartDate > dblAccrualEnd && dblAccrualEnd > _dblAccrualEndDate)
			throw new java.lang.Exception ("CashflowPeriod::accrualDCF => Invalid in-period accrual date!");

		org.drip.analytics.daycount.ActActDCParams actactDCParams = new
			org.drip.analytics.daycount.ActActDCParams (_iFreq, _dblAccrualStartDate, _dblAccrualEndDate);

		return org.drip.analytics.daycount.Convention.YearFraction (_dblAccrualStartDate, dblAccrualEnd,
			_strAccrualDC, _bApplyAccEOMAdj, _dblTerminalDate, actactDCParams, _strCalendar) /
				org.drip.analytics.daycount.Convention.YearFraction (_dblAccrualStartDate,
					_dblAccrualEndDate, _strAccrualDC, _bApplyAccEOMAdj, _dblTerminalDate, actactDCParams,
						_strCalendar) * _dblDCF;
	}

	/**
	 * Retrieve the Coupon Frequency
	 * 
	 * @return The Coupon Frequency
	 */

	public int freq()
	{
		return _iFreq;
	}

	/**
	 * Convert the Coupon Frequency into a Tenor
	 * 
	 * @return The Coupon Frequency converted into a Tenor
	 */

	public java.lang.String tenor()
	{
		int iTenorInMonths = 12 / _iFreq ;

		return 1 == iTenorInMonths || 2 == iTenorInMonths || 3 == iTenorInMonths || 6 == iTenorInMonths || 12
			== iTenorInMonths ? iTenorInMonths + "M" : "ON";
	}

	/**
	 * Retrieve the Coupon Currency
	 * 
	 * @return The Coupon Currency
	 */

	public java.lang.String couponCurrency()
	{
		return null == _forwardLabel ? _strPayCurrency : _forwardLabel.currency();
	}

	/**
	 * Retrieve the Pay Currency
	 * 
	 * @return The Pay Currency
	 */

	public java.lang.String payCurrency()
	{
		return _strPayCurrency;
	}

	/**
	 * Retrieve the Calendar
	 * 
	 * @return The Calendar
	 */

	public java.lang.String calendar()
	{
		return _strCalendar;
	}

	/**
	 * Get the Period Fixed Coupon Rate
	 * 
	 * @return Period Fixed Coupon Rate
	 */

	public double fixedCoupon()
	{
		return _dblFixedCoupon;
	}

	/**
	 * Set the Fixed Coupon Rate
	 * 
	 * @param dblFixedCoupon The Fixed Coupon Rate
	 * 
	 * @return TRUE => The Fixed Coupon Rate Set
	 */

	public boolean setFixedCoupon (
		final double dblFixedCoupon)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblFixedCoupon)) return false;

		_dblFixedCoupon = dblFixedCoupon;
		return true;
	}

	/**
	 * Get the period spread over the floating index
	 * 
	 * @return Period Spread
	 */

	public double spread()
	{
		return _dblSpread;
	}

	/**
	 * Set the Coupon Spread
	 * 
	 * @param dblSpread The Coupon Spread
	 * 
	 * @return The Full Coupon Spread
	 */

	public boolean setSpread (
		final double dblSpread)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblSpread)) return false;

		_dblSpread = dblSpread;
		return true;
	}

	/**
	 * Get the period start Notional
	 * 
	 * @return Period Start Notional
	 */

	public double startNotional()
	{
		return _dblStartNotional;
	}

	/**
	 * Set the Starting Notional
	 * 
	 * @param dblStartNotional The Starting Notional
	 * 
	 * @return The Starting Notional
	 */

	public boolean setStartNotional (
		final double dblStartNotional)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblStartNotional)) return false;

		_dblStartNotional = dblStartNotional;
		return true;
	}

	/**
	 * Get the period end Notional
	 * 
	 * @return Period end Notional
	 */

	public double endNotional()
	{
		return _dblEndNotional;
	}

	/**
	 * Set the End Notional
	 * 
	 * @param dblEndNotional The End Notional
	 * 
	 * @return The End Notional
	 */

	public boolean setEndNotional (
		final double dblEndNotional)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEndNotional)) return false;

		_dblEndNotional = dblEndNotional;
		return true;
	}

	/**
	 * Get the Period Reference Forward rate
	 * 
	 * @return Period Reference Forward Rate
	 */

	public double forwardRate()
	{
		return _dblForwardRate;
	}

	/**
	 * Set the Period Reference Forward Rate
	 * 
	 * @param dblForwardRate The Period Reference Forward Rate
	 * 
	 * @return The Period Reference Forward Rate
	 */

	public boolean setForwardRate (
		final double dblForwardRate)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblForwardRate)) return false;

		_dblForwardRate = dblForwardRate;
		return true;
	}

	/**
	 * Get the period end discount factor
	 * 
	 * @return Period end discount factor
	 */

	public double endDF()
	{
		return _dblEndDF;
	}

	/**
	 * Set the End Discount Factor
	 * 
	 * @param dblEndDF The End Discount Factor
	 * 
	 * @return The End Discount Factor
	 */

	public boolean setEndDF (
		final double dblEndDF)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEndDF)) return false;

		_dblEndDF = dblEndDF;
		return true;
	}

	/**
	 * Get the period end survival probability
	 * 
	 * @return Period end survival probability
	 */

	public double endSurvival()
	{
		return _dblEndSurvival;
	}

	/**
	 * Set the End Survival Probability
	 * 
	 * @param dblEndSurvival The End Survival Probability
	 * 
	 * @return The End Survival Probability
	 */

	public boolean setEndSurvival (
		final double dblEndSurvival)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblEndSurvival)) return false;

		_dblEndSurvival = dblEndSurvival;
		return true;
	}

	/**
	 * Create a set of loss period measures
	 * 
	 * @param comp Component for which the measures are to be generated
	 * @param valParams ValuationParams from which the periods are generated
	 * @param pricerParams PricerParams that control the generation characteristics
	 * @param dblWorkoutDate Double JulianDate representing the absolute end of all the generated periods
	 * @param csqs Market Parameters
	 *  
	 * @return TRUE => Loss Quadrature Steps successfully generated
	 */

	public boolean generateLossMetrics (
		final org.drip.product.definition.CreditComponent comp,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.pricer.PricerParams pricerParams,
		final double dblWorkoutDate,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs)
	{
		if (null == comp || null == valParams || null == pricerParams || null == csqs || null ==
			csqs.creditCurve (comp.creditLabel()[0]) || !org.drip.quant.common.NumberUtil.IsValid
				(dblWorkoutDate) || _dblStartDate > dblWorkoutDate)
			return false;

		org.drip.analytics.rates.DiscountCurve dc = csqs.fundingCurve
			(org.drip.state.identifier.FundingLabel.Standard (_strPayCurrency));

		if (null == dc) return false;

		int iDiscretizationScheme = pricerParams.discretizationScheme();

		double dblPeriodEndDate = _dblEndDate < dblWorkoutDate ? _dblEndDate : dblWorkoutDate;

		if (org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_DAY_STEP == iDiscretizationScheme &&
			(null == (_lsLQM = org.drip.analytics.support.LossQuadratureGenerator.GenerateDayStepLossPeriods
				(comp, valParams, this, dblPeriodEndDate, pricerParams.unitSize(), csqs)) || 0 ==
					_lsLQM.size()))
				return false;

		if (org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_PERIOD_STEP == iDiscretizationScheme &&
			(null == (_lsLQM =
				org.drip.analytics.support.LossQuadratureGenerator.GeneratePeriodUnitLossPeriods (comp,
					valParams, this, dblPeriodEndDate, pricerParams.unitSize(), csqs)) || 0 ==
						_lsLQM.size()))
			return false;

		if (org.drip.param.pricer.PricerParams.PERIOD_DISCRETIZATION_FULL_COUPON == iDiscretizationScheme &&
			(null == (_lsLQM = org.drip.analytics.support.LossQuadratureGenerator.GenerateWholeLossPeriods
				(comp, valParams, this, dblPeriodEndDate, csqs)) || 0 == _lsLQM.size()))
			return false;

		return true;
	}

	/**
	 * Retrieve the List of Loss Quadrature Metrics
	 * 
	 * @return The List of Loss Quadrature Metrics
	 */

	public java.util.List<org.drip.analytics.period.LossQuadratureMetrics> lossMetrics()
	{
		return _lsLQM;
	}

	/**
	 * Return the Collateral Label
	 * 
	 * @return The Collateral Label
	 */

	public org.drip.state.identifier.CollateralLabel collateralLabel()
	{
		return org.drip.state.identifier.CollateralLabel.Standard (_strPayCurrency);
	}

	/**
	 * Return the Funding Label
	 * 
	 * @return The Funding Label
	 */

	public org.drip.state.identifier.FundingLabel fundingLabel()
	{
		return org.drip.state.identifier.FundingLabel.Standard (_strPayCurrency);
	}

	/**
	 * Return the Forward Label
	 * 
	 * @return The Forward Label
	 */

	public org.drip.state.identifier.ForwardLabel forwardLabel()
	{
		return _forwardLabel;
	}

	/**
	 * Return the Credit Label
	 * 
	 * @return The Credit Label
	 */

	public org.drip.state.identifier.CreditLabel creditLabel()
	{
		return _creditLabel;
	}

	/**
	 * Return the FX Label
	 * 
	 * @return The FX Label
	 */

	public org.drip.state.identifier.FXLabel fxLabel()
	{
		java.lang.String strCouponCurrency = couponCurrency();

		return _strPayCurrency.equalsIgnoreCase (strCouponCurrency) ? null :
			org.drip.state.identifier.FXLabel.Standard (_strPayCurrency + "/" + strCouponCurrency);
	}

	/**
	 * Compute the Coupon Measures at the specified Accrual End Date
	 * 
	 * @param dblAccrualEndDate The Accrual End Date
	 * @param valParams The Valuation Parameters
	 * @param csqs The Market Curve Surface/Quote Set
	 * 
	 * @return The Coupon Measures at the specified Accrual End Date
	 */

	public org.drip.analytics.output.PeriodCouponMeasures baseRate (
		final double dblAccrualEndDate,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.market.CurveSurfaceQuoteSet csqs)
	{
		double dblValueDate = valParams.valueDate();

		if (null == _forwardLabel) {
			try {
				return new org.drip.analytics.output.PeriodCouponMeasures (_dblFixedCoupon, _dblFixedCoupon,
					accrualDCF (dblValueDate));
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		org.drip.analytics.output.PeriodCouponMeasures pcmPeriod = resetRate (0, dblValueDate, csqs);

		if (null == pcmPeriod) return null;

		for (int iResetIndex = 1; iResetIndex < _lsResetPeriod.size(); ++iResetIndex) {
			org.drip.analytics.output.PeriodCouponMeasures pcm = resetRate (iResetIndex, dblValueDate, csqs);

			if (null != pcm&& !pcmPeriod.absorb (pcm)) return null;
		}

		return pcmPeriod;
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + fieldDelimiter());

		sb.append (_dblStartDate + fieldDelimiter());

		sb.append (_dblEndDate + fieldDelimiter());

		sb.append (_dblAccrualStartDate + fieldDelimiter());

		sb.append (_dblAccrualEndDate + fieldDelimiter());

		sb.append (_dblPayDate + fieldDelimiter());

		if (null == _lsResetPeriod || 0 == _lsResetPeriod.size())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbPeriods = new java.lang.StringBuffer();

			for (org.drip.product.params.ResetPeriod rp : _lsResetPeriod) {
				if (null == rp) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbPeriods.append (collectionRecordDelimiter());

				sbPeriods.append (new java.lang.String (rp.serialize()));
			}

			if (sbPeriods.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbPeriods.toString());
		}

		sb.append (fieldDelimiter());

		sb.append (_dblFXFixingDate + fieldDelimiter());

		sb.append (_dblTerminalDate + fieldDelimiter());

		sb.append (_iFreq + fieldDelimiter());

		sb.append (_dblDCF + fieldDelimiter());

		sb.append (_strCouponDC + fieldDelimiter());

		sb.append (_strAccrualDC + fieldDelimiter());

		sb.append (_bApplyCpnEOMAdj + fieldDelimiter());

		sb.append (_bApplyAccEOMAdj + fieldDelimiter());

		if (null == _strCalendar || _strCalendar.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + fieldDelimiter());
		else
			sb.append (_strCalendar + fieldDelimiter());

		sb.append (_strPayCurrency + fieldDelimiter());

		if (null == _forwardLabel)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + fieldDelimiter());
		else
			sb.append (_forwardLabel.fullyQualifiedName() + fieldDelimiter());

		if (null == _creditLabel)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + fieldDelimiter());
		else
			sb.append (_creditLabel.fullyQualifiedName() + fieldDelimiter());

		sb.append (_dblStartNotional + fieldDelimiter());

		sb.append (_dblEndNotional + fieldDelimiter());

		sb.append (_dblFixedCoupon + fieldDelimiter());

		sb.append (_dblSpread);

		/* if (null == _lsLQM)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbPeriods = new java.lang.StringBuffer();

			for (org.drip.analytics.period.LossQuadratureMetrics lqm : _lsLQM) {
				if (null == lqm) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbPeriods.append (collectionRecordDelimiter());

				sbPeriods.append (new java.lang.String (lqm.serialize()));
			}

			if (sbPeriods.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING);
			else
				sb.append (sbPeriods.toString());
		} */

		return sb.append (objectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab)
	{
		try {
			return new CashflowPeriod (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override public int hashCode()
	{
		long lBits = java.lang.Double.doubleToLongBits ((int) _dblPayDate);

		return (int) (lBits ^ (lBits >>> 32));
	}

	@Override public int compareTo (
		final CashflowPeriod periodOther)
	{
		if ((int) _dblPayDate > (int) (periodOther._dblPayDate)) return 1;

		if ((int) _dblPayDate < (int) (periodOther._dblPayDate)) return -1;

		return 0;
	}
}

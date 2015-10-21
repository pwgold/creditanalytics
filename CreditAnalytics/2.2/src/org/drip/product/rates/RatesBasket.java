
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
 * RatesBasket contains the implementation of the Basket of Rates Component legs. RatesBasket is made from
 * 	zero/more fixed and floating streams.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class RatesBasket extends org.drip.product.definition.BasketProduct {
	private java.lang.String _strName = "";
	private org.drip.product.rates.FixedStream[] _aCompFixedStream = null;
	private org.drip.product.rates.FloatingStream[] _aCompFloatStream = null;

	@Override protected int measureAggregationType (
		final java.lang.String strMeasureName)
	{
		if ("Accrued01".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("CleanDV01".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("CleanFixedPV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("CleanFloatingPV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("CleanPrice".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_WEIGHTED_CUMULATIVE;

		if ("CleanPV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("DirtyDV01".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("DirtyFixedPV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("DirtyFloatingPV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("DirtyPV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("DV01".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("FairPremium".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_WEIGHTED_CUMULATIVE;

		if ("FixAccrued".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("FloatAccrued".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("ParRate".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_WEIGHTED_CUMULATIVE;

		if ("Price".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_WEIGHTED_CUMULATIVE;

		if ("PV".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_CUMULATIVE;

		if ("Rate".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_WEIGHTED_CUMULATIVE;

		if ("ResetDate".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_UNIT_ACCUMULATE;

		if ("ResetRate".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_UNIT_ACCUMULATE;

		if ("Upfront".equalsIgnoreCase (strMeasureName))
			return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_WEIGHTED_CUMULATIVE;

		return org.drip.product.definition.BasketProduct.MEASURE_AGGREGATION_TYPE_IGNORE;
	}

	/**
	 * RatesBasket constructor
	 * 
	 * @param strName Basket Name
	 * @param aCompFixedStream Array of Fixed Stream Components
	 * @param aCompFloatStream Array of Float Stream Components
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public RatesBasket (
		final java.lang.String strName,
		final org.drip.product.rates.FixedStream[] aCompFixedStream,
		final org.drip.product.rates.FloatingStream[] aCompFloatStream)
		throws java.lang.Exception
	{
		if (null == (_strName = strName) || _strName.isEmpty() || null == (_aCompFixedStream =
			aCompFixedStream) || 0 == _aCompFixedStream.length || null == (_aCompFloatStream =
				aCompFloatStream) || 0 == _aCompFloatStream.length)
			throw new java.lang.Exception ("RatesBasket ctr => Invalid Inputs");
	}

	/**
	 * RatesBasket de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if RatesBasket cannot be properly de-serialized
	 */

	public RatesBasket (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("RatesBasket de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("RatesBasket de-serializer: Empty state");

		java.lang.String strSerializedRatesBasket = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedRatesBasket || strSerializedRatesBasket.isEmpty())
			throw new java.lang.Exception ("RatesBasket de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.math.common.StringUtil.Split (strSerializedRatesBasket,
			getFieldDelimiter());

		if (null == astrField || 4 > astrField.length)
			throw new java.lang.Exception ("RatesBasket de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
			_strName = "";
		else
			_strName = astrField[1];

		java.lang.String[] astrCompFixedStream = org.drip.math.common.StringUtil.Split (astrField[2],
			getCollectionRecordDelimiter());

		if (null == astrCompFixedStream || 0 == astrCompFixedStream.length)
			throw new java.lang.Exception
				("RatesBasket de-serializer: Cannot locate fixed stream component array");

		_aCompFixedStream = new org.drip.product.rates.FixedStream[astrCompFixedStream.length];

		for (int i = 0; i < astrCompFixedStream.length; ++i) {
			if (null == astrCompFixedStream[i] || astrCompFixedStream[i].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrCompFixedStream[i]))
				throw new java.lang.Exception
					("RatesBasket de-serializer: Cannot locate fixed stream component #" + i);

			_aCompFixedStream[i] = new org.drip.product.rates.FixedStream
				(astrCompFixedStream[i].getBytes());
		}

		java.lang.String[] astrCompFloatStream = org.drip.math.common.StringUtil.Split (astrField[3],
			getCollectionRecordDelimiter());

		if (null == astrCompFloatStream || 0 == astrCompFloatStream.length)
			throw new java.lang.Exception
				("RatesBasket de-serializer: Cannot locate float stream component array");

		_aCompFloatStream = new org.drip.product.rates.FloatingStream[astrCompFloatStream.length];

		for (int i = 0; i < astrCompFloatStream.length; ++i) {
			if (null == astrCompFloatStream[i] || astrCompFloatStream[i].isEmpty() ||
				org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrCompFloatStream[i]))
				throw new java.lang.Exception
					("RatesBasket de-serializer: Cannot locate floating stream component #" + i);

			_aCompFloatStream[i] = new org.drip.product.rates.FloatingStream
				(astrCompFloatStream[i].getBytes());
		}
	}

	@Override public java.lang.String getName()
	{
		return _strName;
	}

	@Override public org.drip.product.definition.Component[] getComponents()
	{
		int iNumFixedComp = (null == _aCompFixedStream ? 0 : _aCompFixedStream.length);
		int iNumFloatComp = (null == _aCompFloatStream ? 0 : _aCompFloatStream.length);

		org.drip.product.definition.Component[] aComp = new
			org.drip.product.definition.Component[iNumFixedComp + iNumFloatComp];

		for (int i = 0; i < iNumFixedComp; ++i)
			aComp[i] = _aCompFixedStream[i];

		for (int i = 0; i < iNumFloatComp; ++i)
			aComp[iNumFixedComp + i] = _aCompFloatStream[i];

		return aComp;
	}

	/**
	 * Retrieve the array of the fixed stream components
	 * 
	 * @return The array of the fixed stream components
	 */

	public org.drip.product.rates.FixedStream[] getFixedStreamComponents()
	{
		return _aCompFixedStream;
	}

	/**
	 * Retrieve the array of the float stream components
	 * 
	 * @return The array of the float stream components
	 */

	public org.drip.product.rates.FloatingStream[] getFloatStreamComponents()
	{
		return _aCompFloatStream;
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return "#";
	}

	@Override public java.lang.String getCollectionRecordDelimiter()
	{
		return "@";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return ":";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter());

		if (null == _strName || _strName.isEmpty())
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else
			sb.append (_strName + getFieldDelimiter());

		if (null == _aCompFixedStream || 0 == _aCompFixedStream.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbFixStream = new java.lang.StringBuffer();

			for (org.drip.product.rates.FixedStream fixStream : _aCompFixedStream) {
				if (null == fixStream) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbFixStream.append (getCollectionRecordDelimiter());

				sbFixStream.append (new java.lang.String (fixStream.serialize()));
			}

			if (sbFixStream.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbFixStream.toString() + getFieldDelimiter());
		}

		if (null == _aCompFloatStream || 0 == _aCompFloatStream.length)
			sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
		else {
			boolean bFirstEntry = true;

			java.lang.StringBuffer sbFloatStream = new java.lang.StringBuffer();

			for (org.drip.product.rates.FloatingStream floatStream : _aCompFloatStream) {
				if (null == floatStream) continue;

				if (bFirstEntry)
					bFirstEntry = false;
				else
					sbFloatStream.append (getCollectionRecordDelimiter());

				sbFloatStream.append (new java.lang.String (floatStream.serialize()));
			}

			if (sbFloatStream.toString().isEmpty())
				sb.append (org.drip.service.stream.Serializer.NULL_SER_STRING + getFieldDelimiter());
			else
				sb.append (sbFloatStream.toString() + getFieldDelimiter());
		}

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab)
	{
		try {
			return new RatesBasket (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		org.drip.analytics.daycount.Convention.Init ("c:\\Lakshmi\\BondAnal\\Config.xml");

		org.drip.analytics.date.JulianDate dtEffective = org.drip.analytics.date.JulianDate.Today();

		org.drip.product.rates.FixedStream[] aFixedStream = new org.drip.product.rates.FixedStream[3];
		org.drip.product.rates.FloatingStream[] aFloatStream = new org.drip.product.rates.FloatingStream[3];

		org.drip.analytics.daycount.DateAdjustParams dap = new org.drip.analytics.daycount.DateAdjustParams
			(org.drip.analytics.daycount.Convention.DR_FOLL, "XYZ");

		aFixedStream[0] = new org.drip.product.rates.FixedStream (dtEffective.getJulian(),
			dtEffective.addTenor ("3Y").getJulian(), 0.03, 2, "Act/360", "30/360", false, null, null, dap,
				dap, dap, dap, null, null, 100., "ABC", "DEF");

		aFixedStream[1] = new org.drip.product.rates.FixedStream (dtEffective.getJulian(),
			dtEffective.addTenor ("5Y").getJulian(), 0.05, 2, "30/360", "30/360", false, null, null, dap,
				dap, dap, dap, null, null, 100., "GHI", "JKL");

		aFixedStream[2] = new org.drip.product.rates.FixedStream (dtEffective.getJulian(),
			dtEffective.addTenor ("7Y").getJulian(), 0.07, 2, "30/360", "30/360", false, null, null, dap,
				dap, dap, dap, null, null, 100., "MNO", "PQR");

		aFloatStream[0] = new org.drip.product.rates.FloatingStream (dtEffective.getJulian(),
			dtEffective.addTenor ("3Y").getJulian(), 0.03, 4, "Act/360", "Act/360", "RI", false, null, null,
				dap, dap, dap, dap, null, null, null, -100., "ABC", "DEF");

		aFloatStream[1] = new org.drip.product.rates.FloatingStream (dtEffective.getJulian(),
			dtEffective.addTenor ("5Y").getJulian(), 0.05, 4, "Act/360", "Act/360", "RI", false, null, null,
				dap, dap, dap, dap, null, null, null, -100., "ABC", "DEF");

		aFloatStream[2] = new org.drip.product.rates.FloatingStream (dtEffective.getJulian(),
			dtEffective.addTenor ("7Y").getJulian(), 0.07, 1, "Act/360", "Act/360", "RI", false, null, null,
				dap, dap, dap, dap, null, null, null, -100., "ABC", "DEF");

		RatesBasket rb = new RatesBasket ("SAMRB", aFixedStream, aFloatStream);

		byte[] abRB = rb.serialize();

		System.out.println (new java.lang.String (abRB));

		RatesBasket rbDeser = (RatesBasket) rb.deserialize (abRB);

		System.out.println (new java.lang.String (rbDeser.serialize()));
	}
}


package org.drip.product.params;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2014 Lakshmi Krishnamurthy
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
 * FloatingRateIndex contains the Index Parameters corresponding to a Floating Stream. It provides the
 *  following functionality:
 *  - Retrieve Index, Tenor, Currency, and Fully Qualified Name.
 *  - Serialization into and de-serialization out of byte arrays.
 *  
 * @author Lakshmi Krishnamurthy
 */

public class FloatingRateIndex extends org.drip.service.stream.Serializer implements
	org.drip.product.params.Validatable, org.drip.state.representation.LatentStateLabel {
	private java.lang.String _strIndex = "";
	private java.lang.String _strTenor = "";
	private java.lang.String _strCurrency = "";
	private java.lang.String _strFullyQualifiedName = "";

	/**
	 * Create from the Currency, the Index, and the Tenor
	 * 
	 * @param strCurrency Currency
	 * @param strIndex Index
	 * @param strTenor Tenor
	 * 
	 * @return FloatingRateIndex Instance
	 */

	public static final FloatingRateIndex Create (
		final java.lang.String strCurrency,
		final java.lang.String strIndex,
		final java.lang.String strTenor)
	{
		try {
			return new FloatingRateIndex (strCurrency, strIndex, strTenor, strCurrency + "-" + strIndex + "-"
				+ strTenor);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct a FloatingRateIndex from the corresponding Fully Qualified Name
	 * 
	 * @param strFullyQualifiedName The Fully Qualified Name
	 * 
	 * @return FloatingRateIndex Instance
	 */

	public static final FloatingRateIndex Create (
		final java.lang.String strFullyQualifiedName)
	{
		if (null == strFullyQualifiedName|| strFullyQualifiedName.isEmpty()) return null;

		java.lang.String[] astr = strFullyQualifiedName.split ("-");

		if (null == astr || 3 != astr.length) return null;

		try {
			return new FloatingRateIndex (astr[0], astr[1], astr[2], strFullyQualifiedName);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * FloatingRateIndex constructor
	 * 
	 * @param strCurrency Currency
	 * @param strIndex Index
	 * @param strTenor Tenor
	 * @param strFullyQualifiedName The Fully Qualified Name
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are invalid
	 */

	public FloatingRateIndex (
		final java.lang.String strCurrency,
		final java.lang.String strIndex,
		final java.lang.String strTenor,
		final java.lang.String strFullyQualifiedName)
		throws java.lang.Exception
	{
		if (null == (_strCurrency = strCurrency) || _strCurrency.isEmpty() || null == (_strIndex = strIndex)
			|| _strIndex.isEmpty() || null == (_strTenor = strTenor) || _strTenor.isEmpty() || null ==
				(_strFullyQualifiedName = strFullyQualifiedName) || _strFullyQualifiedName.isEmpty())
			throw new java.lang.Exception ("FloatingRateIndex ctr: Invalid Inputs");
	}

	/**
	 * FloatingRateIndex de-serialization from input byte array
	 * 
	 * @param ab Byte Array
	 * 
	 * @throws java.lang.Exception Thrown if FloatingRateIndex cannot be properly de-serialized
	 */

	public FloatingRateIndex (
		final byte[] ab)
		throws java.lang.Exception
	{
		if (null == ab || 0 == ab.length)
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Invalid input Byte array");

		java.lang.String strRawString = new java.lang.String (ab);

		if (null == strRawString || strRawString.isEmpty())
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Empty state");

		java.lang.String strSerializedFloatingRateIndex = strRawString.substring (0, strRawString.indexOf
			(getObjectTrailer()));

		if (null == strSerializedFloatingRateIndex || strSerializedFloatingRateIndex.isEmpty())
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Cannot locate state");

		java.lang.String[] astrField = org.drip.quant.common.StringUtil.Split (strSerializedFloatingRateIndex,
			getFieldDelimiter());

		if (null == astrField || 5 > astrField.length)
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Invalid reqd field set");

		// double dblVersion = new java.lang.Double (astrField[0]);

		if (null == astrField[1] || astrField[1].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[1]))
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Cannot locate Currency");

		_strCurrency = astrField[1];

		if (null == astrField[2] || astrField[2].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[2]))
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Cannot locate Index");

		_strIndex = astrField[2];

		if (null == astrField[3] || astrField[3].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[3]))
			throw new java.lang.Exception ("FloatingRateIndex de-serializer: Cannot locate Tenor");

		_strTenor = astrField[3];

		if (null == astrField[4] || astrField[4].isEmpty() ||
			org.drip.service.stream.Serializer.NULL_SER_STRING.equalsIgnoreCase (astrField[4]))
			throw new java.lang.Exception
				("FloatingRateIndex de-serializer: Cannot locate Fully Qualified Name");

		_strFullyQualifiedName = astrField[4];

		if (!validate()) throw new java.lang.Exception ("FloatingRateIndex de-serializer: Cannot validate!");
	}

	/**
	 * Retrieve the Currency
	 * 
	 * @return The Currency
	 */

	public java.lang.String currency()
	{
		return _strCurrency;
	}

	/**
	 * Retrieve the Index
	 * 
	 * @return The Index
	 */

	public java.lang.String index()
	{
		return _strIndex;
	}

	/**
	 * Retrieve the Tenor
	 * 
	 * @return The Tenor
	 */

	public java.lang.String tenor()
	{
		return _strTenor;
	}

	@Override public java.lang.String fullyQualifiedName()
	{
		return _strFullyQualifiedName;
	}

	@Override public boolean match (
		final org.drip.state.representation.LatentStateLabel lslOther)
	{
		return null == lslOther || !(lslOther instanceof org.drip.product.params.FloatingRateIndex) ? false :
			_strFullyQualifiedName.equalsIgnoreCase (lslOther.fullyQualifiedName());
	}

	@Override public boolean validate()
	{
		return null != _strCurrency && !_strCurrency.isEmpty() && null != _strIndex && !_strIndex.isEmpty()
			&& null != _strTenor && !_strTenor.isEmpty() && null != _strFullyQualifiedName &&
				!_strFullyQualifiedName.isEmpty();
	}

	@Override public java.lang.String getFieldDelimiter()
	{
		return ",";
	}

	@Override public java.lang.String getObjectTrailer()
	{
		return "'";
	}

	@Override public byte[] serialize()
	{
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		sb.append (org.drip.service.stream.Serializer.VERSION + getFieldDelimiter() + _strCurrency +
			getFieldDelimiter() + _strIndex + getFieldDelimiter() + _strTenor + getFieldDelimiter() +
				_strFullyQualifiedName);

		return sb.append (getObjectTrailer()).toString().getBytes();
	}

	@Override public org.drip.service.stream.Serializer deserialize (
		final byte[] ab)
	{
		try {
			return new FloatingRateIndex (ab);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static final void main (
		final java.lang.String[] astrArgs)
		throws java.lang.Exception
	{
		FloatingRateIndex fri = Create ("USD-LIBOR-6M");

		byte[] abFRI = fri.serialize();

		System.out.println (new java.lang.String (abFRI));

		FloatingRateIndex friDeser = new FloatingRateIndex (abFRI);

		System.out.println (new java.lang.String (friDeser.serialize()));
	}
}

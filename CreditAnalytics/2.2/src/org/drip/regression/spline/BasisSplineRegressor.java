
package org.drip.regression.spline;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2013 Lakshmi Krishnamurthy
 * Copyright (C) 2012 Lakshmi Krishnamurthy
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
 * BasisSplineRegressor implements the custom basis spline regressor for the given basis spline. As part of
 *  the regression run, it executes the following:
 *  - Calibrate and compute the left and he right Jacobian.
 *  - Reset right node and re-run calibration.
 *  - Compute an intermediate value Jacobian.
 *
 * @author Lakshmi Krishnamurthy
 */

public class BasisSplineRegressor extends org.drip.regression.core.UnitRegressionExecutor {
	private java.lang.String _strName = "";
	private org.drip.math.grid.Segment _seg1 = null;
	private org.drip.math.grid.Segment _seg2 = null;
	private org.drip.math.calculus.WengertJacobian _wjLeft = null;
	private org.drip.math.calculus.WengertJacobian _wjRight = null;
	private org.drip.math.calculus.WengertJacobian _wjValue = null;

	/**
	 * Creates an instance of Polynomial BasisSplineRegressor
	 * 
	 * @param strName Regressor Name
	 * @param strScenarioName Regressor Scenario Name
	 * @param iNumBasis Number of Basis Functions
	 * @param iCk Ck
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static final BasisSplineRegressor CreatePolynomialSplineRegressor (
		final java.lang.String strName,
		final java.lang.String strScenarioName,
		final int iNumBasis,
		final int iCk)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU =
				org.drip.math.spline.SegmentBasisSetBuilder.PolynomialBasisSet (new
					org.drip.math.spline.PolynomialBasisSetParams (iNumBasis));

			return null == aAU ? null : new BasisSplineRegressor (strName, strScenarioName, aAU, iCk);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an instance of Bernstein Polynomial BasisSplineRegressor
	 * 
	 * @param strName Regressor Name
	 * @param strScenarioName Regressor Scenario Name
	 * @param iNumBasis Number of Basis Functions
	 * @param iCk Ck
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static final BasisSplineRegressor CreateBernsteinPolynomialSplineRegressor (
		final java.lang.String strName,
		final java.lang.String strScenarioName,
		final int iNumBasis,
		final int iCk)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU =
				org.drip.math.spline.SegmentBasisSetBuilder.BernsteinPolynomialBasisSet (new
					org.drip.math.spline.PolynomialBasisSetParams (iNumBasis));

			return null == aAU ? null : new BasisSplineRegressor (strName, strScenarioName, aAU, iCk);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an instance of Exponential BasisSplineRegressor
	 * 
	 * @param strName Regressor Name
	 * @param strScenarioName Regressor Scenario Name
	 * @param dblTension Tension Parameter
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static final BasisSplineRegressor CreateExponentialTensionSplineRegressor (
		final java.lang.String strName,
		final java.lang.String strScenarioName,
		final double dblTension)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU =
				org.drip.math.spline.SegmentBasisSetBuilder.ExponentialTensionBasisSet (new
					org.drip.math.spline.ExponentialTensionBasisSetParams (dblTension));

			return null == aAU ? null : new BasisSplineRegressor (strName, strScenarioName, aAU, 2);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an instance of Hyperbolic BasisSplineRegressor
	 * 
	 * @param strName Regressor Name
	 * @param strScenarioName Regressor Scenario Name
	 * @param dblTension Tension Parameter
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static final BasisSplineRegressor CreateHyperbolicTensionSplineRegressor (
		final java.lang.String strName,
		final java.lang.String strScenarioName,
		final double dblTension)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU =
				org.drip.math.spline.SegmentBasisSetBuilder.HyperbolicTensionBasisSet (new
					org.drip.math.spline.ExponentialTensionBasisSetParams (dblTension));

			return null == aAU ? null : new BasisSplineRegressor (strName, strScenarioName, aAU, 2);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Creates an instance of the Kaklis-Pandelis BasisSplineRegressor
	 * 
	 * @param strName Regressor Name
	 * @param strScenarioName Regressor Scenario Name
	 * @param iKPPolynomialTension KP Polynomial Tension Parameter
	 * 
	 * @throws java.lang.Exception Thrown if inputs are invalid
	 */

	public static final BasisSplineRegressor CreateKaklisPandelisSplineRegressor (
		final java.lang.String strName,
		final java.lang.String strScenarioName,
		final int iKPPolynomialTension)
	{
		try {
			org.drip.math.function.AbstractUnivariate[] aAU =
				org.drip.math.spline.SegmentBasisSetBuilder.KaklisPandelisBasisSet (new
					org.drip.math.spline.KaklisPandelisBasisSetParams (iKPPolynomialTension));

			return null == aAU ? null : new BasisSplineRegressor (strName, strScenarioName, aAU, 2);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private BasisSplineRegressor (
		final java.lang.String strName,
		final java.lang.String strScenarioName,
		final org.drip.math.function.AbstractUnivariate[] aAU,
		final int iCk)
		throws java.lang.Exception
	{
		super (strName, strScenarioName);

		if (null == (_strName = strName) || _strName.isEmpty())
			throw new java.lang.Exception ("BasisSplineRegressor ctr: Invalid Input");

		org.drip.math.spline.SegmentInelasticParams segParams = new
			org.drip.math.spline.SegmentInelasticParams (iCk, null);

		org.drip.math.function.AbstractUnivariate rsc = new org.drip.math.function.RationalShapeControl (1.);

		if (null == (_seg1 = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (1.0, 3.0, aAU, rsc,
			segParams)) || null == (_seg2 = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (3.0, 6.0,
				aAU, rsc, segParams)))
			throw new java.lang.Exception ("BasisSplineRegressor ctr: Cant create the segments");
	}

	@Override public boolean preRegression()
	{
		return true;
	}

	@Override public boolean execRegression()
	{
		try {
			return null != (_wjLeft = _seg1.calibrateJacobian (25., 0., 20.25)) && null !=
				(_wjRight = _seg2.calibrateJacobian (_seg1, 16.)) && _seg2.calibrate (_seg1,
					14.) && null != (_wjValue = _seg2.calcValueJacobian (5.));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override public boolean postRegression (
		final org.drip.regression.core.RegressionRunDetail rnvd)
	{
		try {
			if (!rnvd.set (_strName + "_Seg1_1_0", "" + _seg1.calcValue (1.))) return false;

			if (!rnvd.set (_strName + "_Seg1_3_0", "" + _seg1.calcValue (3.))) return false;

			if (!rnvd.set (_strName + "_Seg1_Jack", _wjLeft.displayString()));

			if (!rnvd.set (_strName + "_Seg1_Head_Jack", _seg1.calcJacobian().displayString()));

			if (!rnvd.set (_strName + "_Seg1_Monotone", _seg1.monotoneType().toString()));

			if (!rnvd.set (_strName + "_Seg2_3_0", "" + _seg2.calcValue (3.))) return false;

			if (!rnvd.set (_strName + "_Seg2_6_0", "" + _seg2.calcValue (6.))) return false;

			if (!rnvd.set (_strName + "_Seg2_Jack", _wjRight.displayString()));

			if (!rnvd.set (_strName + "_Seg2_Head_Jack", _seg2.calcJacobian().displayString()));

			if (!rnvd.set (_strName + "_Seg2_Monotone", _seg2.monotoneType().toString()));

			return rnvd.set (_strName + "_Seg2_Value_Jack", _wjValue.displayString());
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}

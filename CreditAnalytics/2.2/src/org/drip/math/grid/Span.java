	
package org.drip.math.grid;

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
 * This class implements the span that spans multiple segments. It holds the ordered segment sequence, the
 * 	segment control parameters, and, if available, the spanning Jacobian. It exports the following group of
 * 	functionality:
 * 	- Construct adjoining segment sequences in accordance with the segment control parameters
 * 	- Calibrate according to a varied set of (i.e., NATURAL/FINANCIAL) boundary conditions
 * 	- Interpolate both the value, the ordered derivatives, and the Jacobian at the given ordinate
 * 	- Compute the monotonicity details - segment/span level monotonicity, co-monotonicity, local
 * 		monotonicity.
 * 	- Insert knots
 * 
 * It also exports several static Span creation/calibration methods to generate customized basis splines,
 * 	with customized segment behavior using the segment control.
 *
 * @author Lakshmi Krishnamurthy
 */

public class Span extends org.drip.math.function.AbstractUnivariate {

	/**
	 * Polynomial Spline
	 */

	public static final java.lang.String BASIS_SPLINE_POLYNOMIAL = "Polynomial";

	/**
	 * Bernstein Polynomial Spline
	 */

	public static final java.lang.String BASIS_SPLINE_BERNSTEIN_POLYNOMIAL = "BernsteinPolynomial";

	/**
	 * Hyperbolic Tension Spline
	 */

	public static final java.lang.String BASIS_SPLINE_HYPERBOLIC_TENSION = "HyperbolicTension";

	/**
	 * Exponential Tension Spline
	 */

	public static final java.lang.String BASIS_SPLINE_EXPONENTIAL_TENSION = "ExponentialTension";

	/**
	 * Kaklis Pandelis Spline
	 */

	public static final java.lang.String BASIS_SPLINE_KAKLIS_PANDELIS = "KaklisPandelis";

	/**
	 * Calibration Mode: Natural Boundary Condition
	 */

	public static final java.lang.String SPLINE_BOUNDARY_MODE_NATURAL = "Natural";

	/**
	 * Calibration Mode: Financial Boundary Condition
	 */

	public static final java.lang.String SPLINE_BOUNDARY_MODE_FINANCIAL = "Financial";

	/**
	 * Span Set Up Mode: Set Up ITEP
	 */

	public static final int SET_ITEP = 1;

	/**
	 * Span Set Up Mode: Calibrate SPAN
	 */

	public static final int CALIBRATE_SPAN = 2;

	/**
	 * Span Set Up Mode: Calibrate Jacobian
	 */

	public static final int CALIBRATE_JACOBIAN = 4;

	private org.drip.math.grid.Segment[] _aCSS = null;
	private InterpolatorTargetEvalParams _itep = null;
	private org.drip.math.calculus.WengertJacobian _wjSpan = null;
	private org.drip.math.grid.SegmentControlParams _segControlParams = null;

	/**
	 * Creates a calibrated Span instance over the specified X and Y input array points, using the specified
	 * 	basis splines.
	 * 
	 * @param adblX Input X array points
	 * @param adblY Input Y array points
	 * @param strCalibrationMode Calibration Mode
	 * @param segControlParams Segment Control Parameters
	 * @param iSetupMode Setup Mode
	 * 
	 * @return Span instance
	 */

	public static final Span CreateCalibratedSpanInterpolator (
		final double[] adblX,
		final double[] adblY,
		final java.lang.String strCalibrationMode,
		final org.drip.math.grid.SegmentControlParams segControlParams,
		final int iSetupMode)
	{
		if (null == adblX || null == adblY || 0 == adblX.length || adblX.length != adblY.length || null ==
			strCalibrationMode)
			return null;

		org.drip.math.grid.Span csi = null;

		try {
			csi = new Span (adblX, segControlParams);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		return csi.setup (adblY, strCalibrationMode, iSetupMode) ? csi : null;
	}

	/**
	 * Creates a Calibrated Span instance from an array of X points and a flat Y point
	 * 
	 * @param adblX X Array
	 * @param dblY Flat Y Input
	 * @param strCalibrationMode Calibration Mode
	 * @param segControlParams Segment Control Parameters
	 * @param iSetupMode Setup Mode
	 * 
	 * @return Span Instance
	 */

	public static final Span CreateCalibratedSpanInterpolator (
		final double[] adblX,
		final double dblY,
		final java.lang.String strCalibrationMode,
		final org.drip.math.grid.SegmentControlParams segControlParams,
		final int iSetupMode)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblY)) return null;

		int iNumNode = adblX.length;

		if (0 == iNumNode) return null;

		double[] adblY = new double[iNumNode];

		for (int i = 0; i < iNumNode; ++i)
			adblY[i] = dblY;

		return org.drip.math.grid.Span.CreateCalibratedSpanInterpolator (adblX, adblY, strCalibrationMode,
			segControlParams, iSetupMode);
	}

	private boolean initStartingSegment (
		final double dblLeftSlope)
	{
		return _aCSS[0].calibrate (_itep._adblNodeValue[0], dblLeftSlope, _itep._adblNodeValue[1]);
	}

	private boolean calibSegmentFromRightNodeValue (
		final int iSegment,
		final double[] adblCalibValue)
	{
		if (0 == iSegment) return _aCSS[0].calibrate (null, adblCalibValue[1]);

		return _aCSS[iSegment].calibrate (_aCSS[iSegment - 1], adblCalibValue[iSegment + 1]);
	}

	private boolean calibSegmentElastics (
		final int iStartingSegment,
		final double[] adblCalibValue)
	{
		for (int iSegment = iStartingSegment; iSegment < _aCSS.length; ++iSegment) {
			if (!calibSegmentFromRightNodeValue (iSegment, adblCalibValue)) return false;
		}

		return true;
	}

	private boolean setSpanJacobian (
		final int iNodeIndex,
		final org.drip.math.calculus.WengertJacobian wjSegment)
	{
		if (null == wjSegment) return false;

		int iParameterIndex = 0 == iNodeIndex ? 0 : 2;

		if (!_wjSpan.accumulatePartialFirstDerivative (0, iNodeIndex, wjSegment.getFirstDerivative (0,
			iParameterIndex)))
			return false;

		if (!_wjSpan.accumulatePartialFirstDerivative (1, iNodeIndex, wjSegment.getFirstDerivative (1,
			iParameterIndex)))
			return false;

		if (!_wjSpan.accumulatePartialFirstDerivative (2, iNodeIndex, wjSegment.getFirstDerivative (2,
			iParameterIndex)))
			return false;

		return _wjSpan.accumulatePartialFirstDerivative (3, iNodeIndex, wjSegment.getFirstDerivative (3,
			iParameterIndex));
	}

	private final org.drip.math.calculus.WengertJacobian setJacobian (
		final int iNodeIndex,
		final org.drip.math.calculus.WengertJacobian wjSegment)
	{
		if (null == wjSegment) return null;

		org.drip.math.calculus.WengertJacobian wjSpan = null;

		try {
			wjSpan = new org.drip.math.calculus.WengertJacobian (1, _aCSS.length + 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		for (int i = 0; i <= _aCSS.length; ++i) {
			if (i == iNodeIndex) {
				if (!wjSpan.accumulatePartialFirstDerivative (0, i, wjSegment.getFirstDerivative (0,
					org.drip.math.grid.Segment.LEFT_NODE_VALUE_PARAMETER_INDEX)) ||
						!wjSpan.accumulatePartialFirstDerivative (0, i + 1, wjSegment.getFirstDerivative (0,
							org.drip.math.grid.Segment.RIGHT_NODE_VALUE_PARAMETER_INDEX)))
					return null;
			}
		}

		return wjSpan;
	}

	private boolean setup (
		final double[] adblY,
		final java.lang.String strCalibrationMode,
		final int iSetupMode)
	{
		if (0 != (SET_ITEP & iSetupMode)) {
			try {
				_itep = new InterpolatorTargetEvalParams (adblY, strCalibrationMode);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}
		}

		if (0 != (CALIBRATE_SPAN & iSetupMode)) {
			org.drip.math.solver1D.FixedPointFinderOutput rfopCalib = null;

			/* try {
				rfopCalib = new org.drip.math.solver1D.FixedPointFinderNewton (0., this).findRoot();
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			} */

			if (null == rfopCalib || !rfopCalib.containsRoot()) {
				try {
					rfopCalib = new org.drip.math.solver1D.FixedPointFinderBrent (0., this).findRoot();
				} catch (java.lang.Exception e) {
					e.printStackTrace();

					return false;
				}
			}

			if (null == rfopCalib || !org.drip.math.common.NumberUtil.IsValid (rfopCalib.getRoot()))
				return false;
		}

		if (0 != (CALIBRATE_JACOBIAN & iSetupMode)) {
			try {
				if (null == (_wjSpan = new org.drip.math.calculus.WengertJacobian (_aCSS[0].numBasis(),
					_aCSS.length + 1)))
					return false;
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}

			org.drip.math.calculus.WengertJacobian wjHead = _aCSS[0].calcJacobian();

			if (!setSpanJacobian (0, wjHead) || !setSpanJacobian (1, wjHead)) return false;

			for (int i = 1; i < _aCSS.length; ++i) {
				if (!setSpanJacobian (i + 1, _aCSS[i].calcJacobian())) return false;
			}
		}

		return true;
	}

	/**
	 * Span constructor - Constructs a sequence of basis spline segments
	 * 
	 * @param adblX Array of segment end points
	 * @param segControlParams Segment Control Parameters
	 * 
	 * @throws java.lang.Exception Thrown if the inputs are invalid
	 */

	public Span (
		final double[] adblX,
		final org.drip.math.grid.SegmentControlParams segControlParams)
		throws java.lang.Exception
	{
		super (null);

		if (null == adblX || null == (_segControlParams = segControlParams))
			throw new java.lang.Exception ("Span ctr => Invalid inputs!");

		int iNumSegment = adblX.length - 1;
		_aCSS = new org.drip.math.grid.Segment[iNumSegment];

		java.lang.String strBasisSpline = _segControlParams.getBasisSpline();

		if (1 >= iNumSegment || null == strBasisSpline || (!BASIS_SPLINE_POLYNOMIAL.equalsIgnoreCase
			(strBasisSpline) && !BASIS_SPLINE_BERNSTEIN_POLYNOMIAL.equalsIgnoreCase (strBasisSpline) &&
				!BASIS_SPLINE_HYPERBOLIC_TENSION.equalsIgnoreCase (strBasisSpline) &&
					!BASIS_SPLINE_EXPONENTIAL_TENSION.equalsIgnoreCase (strBasisSpline) &&
						!BASIS_SPLINE_KAKLIS_PANDELIS.equalsIgnoreCase (strBasisSpline)))
			throw new java.lang.Exception ("Span ctr => Invalid inputs!");

		for (int i = 0; i < iNumSegment; ++i) {
			if (BASIS_SPLINE_POLYNOMIAL.equalsIgnoreCase (strBasisSpline)) {
				if (null == (_aCSS[i] = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (adblX[i],
					adblX[i + 1], org.drip.math.spline.SegmentBasisSetBuilder.PolynomialBasisSet
						((org.drip.math.spline.PolynomialBasisSetParams)
							_segControlParams.getBasisSetParams()), _segControlParams.getShapeController(),
								_segControlParams.getSegmentInelasticParams())))
					throw new java.lang.Exception ("Span ctr => Cannot construct " + BASIS_SPLINE_POLYNOMIAL
						+ " Spline!");
			} else if (BASIS_SPLINE_BERNSTEIN_POLYNOMIAL.equalsIgnoreCase (strBasisSpline)) {
				if (null == (_aCSS[i] = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (adblX[i],
					adblX[i + 1], org.drip.math.spline.SegmentBasisSetBuilder.BernsteinPolynomialBasisSet
						((org.drip.math.spline.PolynomialBasisSetParams)
							_segControlParams.getBasisSetParams()), _segControlParams.getShapeController(),
								_segControlParams.getSegmentInelasticParams())))
					throw new java.lang.Exception ("Span ctr => Cannot construct " +
						BASIS_SPLINE_BERNSTEIN_POLYNOMIAL + " Spline!");
			} else if (BASIS_SPLINE_HYPERBOLIC_TENSION.equalsIgnoreCase (strBasisSpline)) {
				if (null == (_aCSS[i] = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (adblX[i],
					adblX[i + 1], org.drip.math.spline.SegmentBasisSetBuilder.HyperbolicTensionBasisSet
						((org.drip.math.spline.ExponentialTensionBasisSetParams)
							_segControlParams.getBasisSetParams()), _segControlParams.getShapeController(),
								_segControlParams.getSegmentInelasticParams())))
					throw new java.lang.Exception ("Span ctr => Cannot construct " +
						BASIS_SPLINE_HYPERBOLIC_TENSION + " Spline!");
			} else if (BASIS_SPLINE_EXPONENTIAL_TENSION.equalsIgnoreCase (strBasisSpline)) {
				if (null == (_aCSS[i] = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (adblX[i],
					adblX[i + 1], org.drip.math.spline.SegmentBasisSetBuilder.ExponentialTensionBasisSet
						((org.drip.math.spline.ExponentialTensionBasisSetParams)
							_segControlParams.getBasisSetParams()), _segControlParams.getShapeController(),
								_segControlParams.getSegmentInelasticParams())))
					throw new java.lang.Exception ("Span ctr => Cannot construct " +
						BASIS_SPLINE_EXPONENTIAL_TENSION + " Spline!");
			} else if (BASIS_SPLINE_KAKLIS_PANDELIS.equalsIgnoreCase (strBasisSpline)) {
				if (null == (_aCSS[i] = org.drip.math.spline.SegmentBasisSetBuilder.CreateCk (adblX[i],
					adblX[i + 1], org.drip.math.spline.SegmentBasisSetBuilder.KaklisPandelisBasisSet
						((org.drip.math.spline.KaklisPandelisBasisSetParams)
							_segControlParams.getBasisSetParams()), _segControlParams.getShapeController(),
								_segControlParams.getSegmentInelasticParams())))
					throw new java.lang.Exception ("Span ctr => Cannot construct " +
						BASIS_SPLINE_KAKLIS_PANDELIS + " Spline!");
			}
		}
	}

	@Override public double evaluate (
		final double dblLeftSlope)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftSlope))
			throw new java.lang.Exception ("Span::evalTarget => Invalid inputs!");

		if (SPLINE_BOUNDARY_MODE_NATURAL.equalsIgnoreCase (_itep._strSolverMode)) {
			if (!initStartingSegment (dblLeftSlope) || !calibSegmentElastics (1, _itep._adblNodeValue))
				throw new java.lang.Exception ("Span::evalTarget => cannot set segment elastics!");

			return calcTailDerivative (2);
		}

		throw new java.lang.Exception ("Span::evalTarget => Unknown Solver Mode " + _itep._strSolverMode);
	}

	/**
	 * Sets the left slope
	 * 
	 * @param dblLeftValue Left most node value
	 * @param dblLeftSlope Left most node slope
	 * @param dblRightValue Left most node value
	 * 
	 * @return TRUE => Left slope successfully set
	 */

	public boolean setLeftNode (
		final double dblLeftValue,
		final double dblLeftSlope,
		final double dblRightValue)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblLeftValue) ||
			!org.drip.math.common.NumberUtil.IsValid (dblLeftSlope) ||
				!org.drip.math.common.NumberUtil.IsValid (dblRightValue))
			return false;

		return _aCSS[0].calibrate (dblLeftValue, dblLeftSlope, dblRightValue);
	}

	/**
	 * Calculates the interpolated value at the given input point
	 * 
	 * @param dblX Input point
	 * 
	 * @return Interpolated output
	 * 
	 * @throws java.lang.Exception Thrown if the interpolation did not succeed
	 */

	public double calcValue (
		final double dblX)
		throws java.lang.Exception
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblX))
			throw new java.lang.Exception ("Span::calcValue => Invalid inputs!");

		if (_aCSS[0].getLeft() > dblX || _aCSS[_aCSS.length - 1].getRight() < dblX)
			throw new java.lang.Exception ("Span::calcValue => Input out of range!");

		int iIndex = 0;

		for (int i = 0; i < _aCSS.length; ++i) {
			if (_aCSS[i].getLeft() <= dblX && _aCSS[i].getRight() >= dblX) {
				iIndex = i;
				break;
			}
		}

		return _aCSS[iIndex].calcValue (dblX);
	}

	/**
	 * Calculates the Jacobian to the inputs at the given input point
	 * 
	 * @param dblX Input point
	 * 
	 * @return Jacobian to the inputs
	 */

	public org.drip.math.calculus.WengertJacobian calcValueJacobian (
		final double dblX)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblX)) return null;

		if (_aCSS[0].getLeft() > dblX || _aCSS[_aCSS.length - 1].getRight() < dblX) return null;

		int iIndex = 0;

		for (int i = 0 ; i < _aCSS.length; ++i) {
			if (_aCSS[i].getLeft() <= dblX && _aCSS[i].getRight() >= dblX) {
				iIndex = i;
				break;
			}
		}

		return setJacobian (iIndex, _aCSS[iIndex].calcValueJacobian (dblX));
	}

	/**
	 * Identifies the monotone type for the segment underlying the given input point
	 * 
	 * @param dblX Input point
	 * 
	 * @return Segment monotone Type
	 */

	public org.drip.math.grid.SegmentMonotonocity monotoneType (
		final double dblX)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblX)) return null;

		if (_aCSS[0].getLeft() > dblX || _aCSS[_aCSS.length - 1].getRight() < dblX) return null;

		for (int i = 0; i < _aCSS.length; ++i) {
			if (_aCSS[i].getLeft() <= dblX && _aCSS[i].getRight() >= dblX)
				return _aCSS[i].monotoneType();
		}

		return null;
	}

	/**
	 * Indicates if all the comprising segments are monotone
	 * 
	 * @return TRUE => Fully locally monotonic
	 * 
	 * @throws java.lang.Exception Thrown if the Segment monotone Type could not be estimated
	 */

	public boolean isLocallyMonotone()
		throws java.lang.Exception
	{
		for (int i = 0; i < _aCSS.length; ++i) {
			org.drip.math.grid.SegmentMonotonocity sm = null;

			try {
				sm = _aCSS[i].monotoneType();
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			if (null == sm || org.drip.math.grid.SegmentMonotonocity.MONOTONIC != sm.type()) return false;
		}

		return true;
	}

	/**
	 * Verify whether the segment and spline mini-max behavior matches
	 * 
	 * @param adblY Input Y array points
	 * 
	 * @return TRUE => Span is co-monotonic with the input points
	 * 
	 * @throws java.lang.Exception Thrown if the Segment monotone Type could not be estimated
	 */

	public boolean isCoMonotone (
		final double[] adblY)
		throws java.lang.Exception
	{
		if (null == adblY || adblY.length != _aCSS.length + 1)
			throw new java.lang.Exception ("Span.isCoMonotone => Data input inconsistent with the segment");

		int iMaximaNode = 1;
		int iMinimaNode = 2;
		int[] aiNodeMiniMax = new int[adblY.length];
		int[] aiMonotoneType = new int[_aCSS.length];

		for (int i = 0; i < adblY.length; ++i) {
			if (0 == i || adblY.length - 1 == i)
				aiNodeMiniMax[i] = 0;
			else {
				if (adblY[i - 1] < adblY[i] && adblY[i + 1] < adblY[i])
					aiNodeMiniMax[i] = iMaximaNode;
				else if (adblY[i - 1] > adblY[i] && adblY[i + 1] > adblY[i])
					aiNodeMiniMax[i] = iMinimaNode;
				else
					aiNodeMiniMax[i] = 0;
			}

			if (i < adblY.length - 1) {
				org.drip.math.grid.SegmentMonotonocity sm = _aCSS[i].monotoneType();

				if (null != sm) aiMonotoneType[i] = sm.type();
			}
		}

		for (int i = 1; i < adblY.length - 1; ++i) {
			if (iMaximaNode == aiNodeMiniMax[i]) {
				if (org.drip.math.grid.SegmentMonotonocity.MAXIMA != aiMonotoneType[i] &&
					org.drip.math.grid.SegmentMonotonocity.MAXIMA != aiMonotoneType[i - 1])
					return false;
			} else if (iMinimaNode == aiNodeMiniMax[i]) {
				if (org.drip.math.grid.SegmentMonotonocity.MINIMA != aiMonotoneType[i] &&
					org.drip.math.grid.SegmentMonotonocity.MINIMA != aiMonotoneType[i - 1])
					return false;
			}
		}

		return true;
	}

	/**
	 * Is the given X a knot location
	 * 
	 * @param dblX Knot X
	 * 
	 * @return TRUE => Given Location corresponds to a Knot
	 */

	public boolean isKnot (
		final double dblX)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblX)) return false;

		for (int i = 0; i < _aCSS.length; ++i) {
			if (dblX == _aCSS[i].getLeft()) return false;
		}

		return dblX == _aCSS[_aCSS.length - 1].getLeft();
	}

	/**
	 * Insert a Knot
	 * 
	 * @param dblX Knot X
	 * @param dblY Knot Y
	 * 
	 * @return The Span with the Knot inserted
	 */

	public Span insertKnot (
		final double dblX,
		final double dblY)
	{
		if (!org.drip.math.common.NumberUtil.IsValid (dblX) || !org.drip.math.common.NumberUtil.IsValid
			(dblY) || isKnot (dblX))
			return null;

		int iNewIndex = 0;
		int iNumCurrentSegment = _aCSS.length;
		double[] adblX = new double[iNumCurrentSegment + 2];
		double[] adblY = new double[iNumCurrentSegment + 2];

		if (dblX < _aCSS[0].getLeft()) {
			adblX[iNewIndex] = dblX;
			adblY[iNewIndex++] = dblY;
		}

		for (int i = 0; i < iNumCurrentSegment; ++i) {
			adblX[iNewIndex] = _aCSS[i].getLeft();

			try {
				adblY[iNewIndex++] = calcValue (_aCSS[i].getLeft());
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			if (dblX > _aCSS[i].getLeft() && dblX < _aCSS[i].getRight()) {
				adblX[iNewIndex] = dblX;
				adblY[iNewIndex++] = dblY;
			}
		}

		adblX[iNewIndex] = _aCSS[iNumCurrentSegment - 1].getRight();

		try {
			adblY[iNewIndex++] = calcValue (_aCSS[iNumCurrentSegment - 1].getRight());
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (dblX > _aCSS[iNumCurrentSegment - 1].getRight()) {
			adblX[iNewIndex] = dblX;
			adblY[iNewIndex++] = dblY;
		}

		return org.drip.math.grid.Span.CreateCalibratedSpanInterpolator (adblX, adblY,
			SPLINE_BOUNDARY_MODE_NATURAL, _segControlParams, SET_ITEP | CALIBRATE_SPAN);
	}

	/**
	 * Calculate the tail derivative of the requested order for the given node
	 * 
	 * @param iOrder Order of the derivative
	 * 
	 * @return The Tail Derivative
	 * 
	 * @throws java.lang.Exception Thrown if the derivative cannot be calculated
	 */

	public double calcTailDerivative (
		final int iOrder)
		throws java.lang.Exception
	{
		if (iOrder >= 3) throw new java.lang.Exception ("Span::calcTailDerivative => Invalid inputs!");

		org.drip.math.grid.Segment css = _aCSS[_aCSS.length - 1];

		return css.calcOrderedDerivative (css.getRight(), iOrder, false);
	}

	/**
	 * Reset the given node with the given value
	 * 
	 * @param iNodeIndex Node whose value is set
	 * @param dblNodeValue Node Value
	 * 
	 * @return TRUE => If the calibration succeeds
	 */

	public boolean resetNode (
		final int iNodeIndex,
		final double dblNodeValue)
	{
		if (0 == iNodeIndex || 1 == iNodeIndex || _aCSS.length < iNodeIndex ||
			!org.drip.math.common.NumberUtil.IsValid (dblNodeValue))
			return false;

		return _aCSS[iNodeIndex - 1].calibrate (_aCSS[iNodeIndex - 2], dblNodeValue);
	}

	/**
	 * Displays the full span segment begin and end derivatives
	 * 
	 * @return Derivatives returned as a string
	 */

	public java.lang.String displayDerivatives() {
		java.lang.StringBuffer sb = new java.lang.StringBuffer();

		try {
			for (int i = 0; i < _aCSS.length; ++i) {
				org.drip.math.grid.Segment ec = _aCSS[i];

				sb.append ("\n\t\t[Derivative at " + new org.drip.analytics.date.JulianDate (ec.getLeft()) +
					"]; 0th=" + org.drip.math.common.FormatUtil.FormatDouble (ec.calcOrderedDerivative
						(ec.getLeft(), 0, true), 4, 4, 1.) + "; 1st=" +
							org.drip.math.common.FormatUtil.FormatDouble (ec.calcOrderedDerivative
								(ec.getLeft(), 1, true), 4, 4, 1.) + "; 2nd=" +
									org.drip.math.common.FormatUtil.FormatDouble (ec.calcOrderedDerivative
										(ec.getLeft(), 2, true), 4, 4, 1.) + "; 3rd=" +
											org.drip.math.common.FormatUtil.FormatDouble
												(ec.calcOrderedDerivative (ec.getLeft(), 3, true), 4, 4,
													1.));

				sb.append ("\n\t\t\t" + _aCSS[i].displayString());
			}

			return sb.toString();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public class InterpolatorTargetEvalParams {
		private double[] _adblNodeValue = null;
		private java.lang.String _strSolverMode = "";

		/**
		 * InterpolatorTargetEvalParams constructor
		 * 
		 * @param adblNodeValue node truth values
		 * @param strSolverMode Solver Mode - NATURAL | FINANCIAL
		 * 
		 * @throws java.lang.Exception Thrown if the inputs are invalid
		 */

		public InterpolatorTargetEvalParams (
			final double[] adblNodeValue,
			final java.lang.String strSolverMode)
			throws java.lang.Exception
		{
			if (null == (_adblNodeValue = adblNodeValue) || 0 == _adblNodeValue.length || null ==
				(_strSolverMode = strSolverMode) || _strSolverMode.isEmpty())
				throw new java.lang.Exception ("InterpolatorEvalParams ctr: Invalid inputs!");

			if (!SPLINE_BOUNDARY_MODE_NATURAL.equalsIgnoreCase (_strSolverMode) &&
				!SPLINE_BOUNDARY_MODE_FINANCIAL.equalsIgnoreCase (_strSolverMode))
				throw new java.lang.Exception ("InterpolatorEvalParams ctr: Unknown Solver Mode!");
		}
	}
}


package org.drip.quant.function1D;

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
 * AbstractUnivariate provides the evaluation of the objective function and its derivatives for a specified
 * 	variate. Default implementation of the derivatives are for non-analytical black box objective functions.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class AbstractUnivariate {
	protected org.drip.quant.calculus.DerivativeControl _dc = null;

	/**
	 * Objective Function constructor
	 * 
	 * @param dc Derivative Control
	 */

	public AbstractUnivariate (
		final org.drip.quant.calculus.DerivativeControl dc)
	{
		if (null == (_dc = dc)) _dc = new org.drip.quant.calculus.DerivativeControl();
	}

	/**
	 * Evaluate for the given variate
	 * 
	 * @param dblVariate Variate
	 *  
	 * @return Returns the calculated value
	 * 
	 * @throws java.lang.Exception Thrown if evaluation cannot be done
	 */

	public abstract double evaluate (
		final double dblVariate)
		throws java.lang.Exception;

	/**
	 * Calculate the derivative
	 * 
	 * @param dblVariate Variate at which the derivative is to be calculated
	 * @param dblOFBase Base Value for the Objective Function
	 * @param iOrder Order of the derivative to be computed
	 * 
	 * @return The Derivative
	 */

	public org.drip.quant.calculus.Differential calcDifferential (
		final double dblVariate,
		final double dblOFBase,
		final int iOrder)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (dblVariate) || 0 >= iOrder) return null;

		double dblVariateInfinitesimal = java.lang.Double.NaN;

		try {
			dblVariateInfinitesimal = _dc.getVariateInfinitesimal (dblVariate);
		} catch (java.lang.Exception e) {
			e.printStackTrace();

			return null;
		}

		if (1 != iOrder) {
			try {
				org.drip.quant.calculus.Differential diffLeft = calcDifferential (dblVariate - 0.5 *
					dblVariateInfinitesimal, iOrder - 1);

				if (null == diffLeft) return null;

				org.drip.quant.calculus.Differential diffRight = calcDifferential (dblVariate + 0.5 *
					dblVariateInfinitesimal, iOrder - 1);

				if (null == diffRight) return null;

				return new org.drip.quant.calculus.Differential ((diffLeft.getDeltaOF() -
					diffRight.getDeltaOF()) / dblVariateInfinitesimal, dblVariateInfinitesimal);
			} catch (java.lang.Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		try {
			return new org.drip.quant.calculus.Differential (dblVariateInfinitesimal, evaluate (dblVariate +
				dblVariateInfinitesimal) - dblOFBase);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Calculate the derivative
	 * 
	 * @param dblVariate Variate at which the derivative is to be calculated
	 * @param iOrder Order of the derivative to be computed
	 * 
	 * @return The Derivative
	 */

	public org.drip.quant.calculus.Differential calcDifferential (
		final double dblVariate,
		final int iOrder)
	{
		try {
			return calcDifferential (dblVariate, evaluate (dblVariate), iOrder);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Calculate the derivative as a double
	 * 
	 * @param dblVariate Variate at which the derivative is to be calculated
	 * @param iOrder Order of the derivative to be computed
	 * 
	 * @return The Derivative
	 */

	public double calcDerivative (
		final double dblVariate,
		final int iOrder)
		throws java.lang.Exception
	{
		return calcDifferential (dblVariate, evaluate (dblVariate), iOrder).calcSlope (false);
	}
}
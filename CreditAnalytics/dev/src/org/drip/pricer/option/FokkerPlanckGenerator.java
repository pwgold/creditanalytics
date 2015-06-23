
package org.drip.pricer.option;

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
 * FokkerPlanckGenerator holds the base functionality that the performs the PDF evolution oriented Option
 *  Pricing.
 * 
 * @author Lakshmi Krishnamurthy
 */

public abstract class FokkerPlanckGenerator {

	/**
	 * Carry out a Pricing Run and generate the Pricing related measure set
	 * 
	 * @param dblStrike Option Strike
	 * @param dblTimeToExpiry Option Time To Expiry
	 * @param dblRiskFreeRate Option Risk Free Rate
	 * @param dblUnderlier Option Underlier Value
	 * @param bIsForward TRUE => The Underlier represents the Forward, FALSE => it represents Spot
	 * @param dblInitialVolatility Option Initial Volatility Value
	 * @param bCalibMode TRUE => Run on Calibration Mode
	 * 
	 * @return TRUE => Computation Successful
	 */

	public abstract boolean compute (
		final double dblStrike,
		final double dblTimeToExpiry,
		final double dblRiskFreeRate,
		final double dblUnderlier,
		final boolean bIsForward,
		final double dblInitialVolatility,
		final boolean bCalibMode);

	/**
	 * The Option Terminal Discount Factor
	 * 
	 * @return The Option Terminal Discount Factor
	 */

	public abstract double df();

	/**
	 * The "Effective" Volatility
	 * 
	 * @return The "Effective" Volatility
	 */

	public abstract double effectiveVolatility();

	/**
	 * The Call Option Charm
	 * 
	 * @return The Call Option Charm
	 */

	public abstract double callCharm();

	/**
	 * The Call Option Color
	 * 
	 * @return The Call Option Color
	 */

	public abstract double callColor();

	/**
	 * The Call Option Delta
	 * 
	 * @return The Call Option Delta
	 */

	public abstract double callDelta();

	/**
	 * The Call Option Gamma
	 * 
	 * @return The Call Option Gamma
	 */

	public abstract double callGamma();

	/**
	 * The Call Option Price
	 * 
	 * @return The Call Option Price
	 */

	public abstract double callPrice();

	/**
	 * The Call Prob 1 Term
	 * 
	 * @return The Call Prob 1 Term
	 */

	public abstract double callProb1();

	/**
	 * The Call Prob 2 Term
	 * 
	 * @return The Call Prob 2 Term
	 */

	public abstract double callProb2();

	/**
	 * The Call Option Rho
	 * 
	 * @return The Call Option Rho
	 */

	public abstract double callRho();

	/**
	 * The Call Option Speed
	 * 
	 * @return The Call Option Speed
	 */

	public abstract double callSpeed();

	/**
	 * The Call Option Theta
	 * 
	 * @return The Call Option Theta
	 */

	public abstract double callTheta();

	/**
	 * The Call Option Ultima
	 * 
	 * @return The Call Option Ultima
	 */

	public abstract double callUltima();

	/**
	 * The Call Option Vanna
	 * 
	 * @return The Call Option Vanna
	 */

	public abstract double callVanna();

	/**
	 * The Call Option Vega
	 * 
	 * @return The Call Option Vega
	 */

	public abstract double callVega();

	/**
	 * The Call Option Veta
	 * 
	 * @return The Call Option Veta
	 */

	public abstract double callVeta();

	/**
	 * The Call Option Vomma
	 * 
	 * @return The Call Option Vomma
	 */

	public abstract double callVomma();

	/**
	 * The Put Option Charm
	 * 
	 * @return The Put Option Charm
	 */

	public abstract double putCharm();

	/**
	 * The Put Option Color
	 * 
	 * @return The Put Option Color
	 */

	public abstract double putColor();

	/**
	 * The Put Option Delta
	 * 
	 * @return The Put Option Delta
	 */

	public abstract double putDelta();

	/**
	 * The Put Option Gamma
	 * 
	 * @return The Put Option Gamma
	 */

	public abstract double putGamma();

	/**
	 * The Put Option Price
	 * 
	 * @return The Put Option Price
	 */

	public abstract double putPrice();

	/**
	 * The Put Option Price Computed from the Put-Call Parity Relation
	 * 
	 * @return The Put Option Price Computed from the Put-Call Parity Relation
	 */

	public abstract double putPriceFromParity();

	/**
	 * The Put Prob 1 Term
	 * 
	 * @return The Put Prob 1 Term
	 */

	public abstract double putProb1();

	/**
	 * The Put Prob 2 Term
	 * 
	 * @return The Put Prob 2 Term
	 */

	public abstract double putProb2();

	/**
	 * The Put Option Rho
	 * 
	 * @return The Put Option Rho
	 */

	public abstract double putRho();

	/**
	 * The Put Option Speed
	 * 
	 * @return The Put Option Speed
	 */

	public abstract double putSpeed();

	/**
	 * The Put Option Theta
	 * 
	 * @return The Put Option Theta
	 */

	public abstract double putTheta();

	/**
	 * The Put Option Ultima
	 * 
	 * @return The Put Option Ultima
	 */

	public abstract double putUltima();

	/**
	 * The Put Option Vanna
	 * 
	 * @return The Put Option Vanna
	 */

	public abstract double putVanna();

	/**
	 * The Put Option Vega
	 * 
	 * @return The Put Option Vega
	 */

	public abstract double putVega();

	/**
	 * The Put Option Veta
	 * 
	 * @return The Put Option Veta
	 */

	public abstract double putVeta();

	/**
	 * The Put Option Vomma
	 * 
	 * @return The Put Option Vomma
	 */

	public abstract double putVomma();

	/**
	 * Carry out a Pricing Run and generate the Pricing related measure set
	 * 
	 * @param dblSpotDate Spot Date
	 * @param dblExpiryDate Expiry Date
	 * @param dblStrike Option Strike
	 * @param dcFunding The Funding Curve
	 * @param dblUnderlier Option Underlier Value
	 * @param bIsForward TRUE => The Underlier represents the Forward, FALSE => it represents Spot
	 * @param funcVolatilityR1ToR1 The R^1 -> R^1 Volatility Term Structure
	 * @param bCalibMode TRUE => Run on Calibration Mode
	 * 
	 * @return TRUE => Computation Successful
	 */

	public boolean compute (
		final double dblSpotDate,
		final double dblExpiryDate,
		final double dblStrike,
		final org.drip.analytics.rates.DiscountCurve dcFunding,
		final double dblUnderlier,
		final boolean bIsForward,
		final org.drip.function.definition.R1ToR1 funcVolatilityR1ToR1,
		final boolean bCalibMode)
	{
		{
			if (!org.drip.quant.common.NumberUtil.IsValid (dblSpotDate) ||
				!org.drip.quant.common.NumberUtil.IsValid (dblExpiryDate) || dblExpiryDate <= dblSpotDate ||
					!org.drip.quant.common.NumberUtil.IsValid (dblStrike) || null == dcFunding || null ==
						funcVolatilityR1ToR1)
				return false;

			double dblRiskFreeRate = java.lang.Double.NaN;
			double dblEffectiveVolatility = java.lang.Double.NaN;
			double dblTimeToExpiry = (dblExpiryDate - dblSpotDate) / 365.25;

			org.drip.function.definition.R1ToR1 funcVarianceR1ToR1 = new org.drip.function.definition.R1ToR1
				(null) {
				@Override public double evaluate (
					final double dblX)
					throws java.lang.Exception
				{
					return funcVolatilityR1ToR1.evaluate (dblX) * funcVolatilityR1ToR1.evaluate (dblX);
				}
			};

			try {
				dblRiskFreeRate = dcFunding.libor (dblSpotDate, dblExpiryDate);

				dblEffectiveVolatility = java.lang.Math.sqrt (funcVarianceR1ToR1.integrate (dblSpotDate,
					dblExpiryDate) / 365.25) / dblTimeToExpiry;
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return false;
			}

			return compute (dblStrike, dblTimeToExpiry, dblRiskFreeRate, dblUnderlier, bIsForward,
				dblEffectiveVolatility, bCalibMode);
		}
	}
}

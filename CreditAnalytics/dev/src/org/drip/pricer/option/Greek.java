
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
 * Greek contains the Sensitivities generated during the Option Pricing Run (i.e., the Greeks).
 * 
 * @author Lakshmi Krishnamurthy
 */

public class Greek {
	private double _dblDF = java.lang.Double.NaN;
	private double _dblPutRho = java.lang.Double.NaN;
	private double _dblCallRho = java.lang.Double.NaN;
	private double _dblPutVega = java.lang.Double.NaN;
	private double _dblPutVeta = java.lang.Double.NaN;
	private double _dblPutCharm = java.lang.Double.NaN;
	private double _dblPutDelta = java.lang.Double.NaN;
	private double _dblPutColor = java.lang.Double.NaN;
	private double _dblPutGamma = java.lang.Double.NaN;
	private double _dblPutPrice = java.lang.Double.NaN;
	private double _dblPutProb1 = java.lang.Double.NaN;
	private double _dblPutProb2 = java.lang.Double.NaN;
	private double _dblPutSpeed = java.lang.Double.NaN;
	private double _dblPutTheta = java.lang.Double.NaN;
	private double _dblPutVanna = java.lang.Double.NaN;
	private double _dblPutVomma = java.lang.Double.NaN;
	private double _dblCallVega = java.lang.Double.NaN;
	private double _dblCallVeta = java.lang.Double.NaN;
	private double _dblCallCharm = java.lang.Double.NaN;
	private double _dblCallColor = java.lang.Double.NaN;
	private double _dblCallDelta = java.lang.Double.NaN;
	private double _dblCallGamma = java.lang.Double.NaN;
	private double _dblCallPrice = java.lang.Double.NaN;
	private double _dblCallProb1 = java.lang.Double.NaN;
	private double _dblCallProb2 = java.lang.Double.NaN;
	private double _dblCallSpeed = java.lang.Double.NaN;
	private double _dblCallTheta = java.lang.Double.NaN;
	private double _dblCallVanna = java.lang.Double.NaN;
	private double _dblCallVomma = java.lang.Double.NaN;
	private double _dblPutUltima = java.lang.Double.NaN;
	private double _dblCallUltima = java.lang.Double.NaN;
	private double _dblPutPriceFromParity = java.lang.Double.NaN;
	private double _dblEffectiveVolatility = java.lang.Double.NaN;

	/**
	 * The Greek Constructor
	 * 
	 * @param dblDF The Payoff Discount Factor
	 * @param dblEffectiveVolatility Effective Volatility
	 * @param dblCallPrice Call Price
	 * @param dblCallProb1 Call Probability Term #1
	 * @param dblCallProb2 Call Probability Term #2
	 * @param dblCallDelta Call Delta
	 * @param dblCallVega Call Vega
	 * @param dblCallTheta Call Theta
	 * @param dblCallRho Call Rho
	 * @param dblCallGamma Call Gamma
	 * @param dblCallVanna Call Vanna
	 * @param dblCallVomma Call Vomma
	 * @param dblCallCharm Call Charm
	 * @param dblCallVeta Call Veta
	 * @param dblCallColor Call Color
	 * @param dblCallSpeed Call Speed
	 * @param dblCallUltima Call Ultima
	 * @param dblPutPrice Put Price
	 * @param dblPutPriceFromParity Put Price Computed from Put-Call Parity
	 * @param dblPutProb1 Put Probability Term #1
	 * @param dblPutProb2 Put Probability Term #2
	 * @param dblPutDelta Put Delta
	 * @param dblPutVega Put Vega
	 * @param dblPutTheta Put Theta
	 * @param dblPutRho Put Rho
	 * @param dblPutGamma Put Gamma
	 * @param dblPutVanna Put Vanna
	 * @param dblPutVomma Put Vomma
	 * @param dblPutCharm Put Charm
	 * @param dblPutVeta Put Veta
	 * @param dblPutColor Put Color
	 * @param dblPutSpeed Put Speed
	 * @param dblPutUltima Put Ultima
	 * 
	 * @throws java.lang.Exception Thrown if Essential Inputs are Invalid
	 */

	public Greek (
		final double dblDF,
		final double dblEffectiveVolatility,
		final double dblCallPrice,
		final double dblCallProb1,
		final double dblCallProb2,
		final double dblCallDelta,
		final double dblCallVega,
		final double dblCallTheta,
		final double dblCallRho,
		final double dblCallGamma,
		final double dblCallVanna,
		final double dblCallVomma,
		final double dblCallCharm,
		final double dblCallVeta,
		final double dblCallColor,
		final double dblCallSpeed,
		final double dblCallUltima,
		final double dblPutPrice,
		final double dblPutPriceFromParity,
		final double dblPutProb1,
		final double dblPutProb2,
		final double dblPutDelta,
		final double dblPutVega,
		final double dblPutTheta,
		final double dblPutRho,
		final double dblPutGamma,
		final double dblPutVanna,
		final double dblPutVomma,
		final double dblPutCharm,
		final double dblPutVeta,
		final double dblPutColor,
		final double dblPutSpeed,
		final double dblPutUltima)
		throws java.lang.Exception
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (_dblDF = dblDF) ||
			!org.drip.quant.common.NumberUtil.IsValid (_dblEffectiveVolatility = dblEffectiveVolatility) ||
				!org.drip.quant.common.NumberUtil.IsValid (_dblCallPrice = dblCallPrice))
			throw new java.lang.Exception ("Greek ctr: Invalid Inputs");

		_dblCallProb1 = dblCallProb1;
		_dblCallProb2 = dblCallProb2;
		_dblCallDelta = dblCallDelta;
		_dblCallVega = dblCallVega;
		_dblCallTheta = dblCallTheta;
		_dblCallRho = dblCallRho;
		_dblCallGamma = dblCallGamma;
		_dblCallVanna = dblCallVanna;
		_dblCallVomma = dblCallVomma;
		_dblCallCharm = dblCallCharm;
		_dblCallVeta = dblCallVeta;
		_dblCallColor = dblCallColor;
		_dblCallSpeed = dblCallSpeed;
		_dblCallUltima = dblCallUltima;
		_dblPutPrice = dblPutPrice;
		_dblPutPriceFromParity = dblPutPriceFromParity;
		_dblPutProb1 = dblPutProb1;
		_dblPutProb2 = dblPutProb2;
		_dblPutDelta = dblPutDelta;
		_dblPutVega = dblPutVega;
		_dblPutTheta = dblPutTheta;
		_dblPutRho = dblPutRho;
		_dblPutGamma = dblPutGamma;
		_dblPutVanna = dblPutVanna;
		_dblPutVomma = dblPutVomma;
		_dblPutCharm = dblPutGamma;
		_dblPutVeta = dblPutVeta;
		_dblPutColor = dblPutColor;
		_dblPutSpeed = dblPutSpeed;
		_dblPutUltima = dblPutUltima;
	}

	/**
	 * The Option Terminal Discount Factor
	 * 
	 * @return The Option Terminal Discount Factor
	 */

	public double df()
	{
		return _dblDF;
	}

	/**
	 * The "Effective" Volatility
	 * 
	 * @return The "Effective" Volatility
	 */

	public double effectiveVolatility()
	{
		return _dblEffectiveVolatility;
	}

	/**
	 * The Call Option Price
	 * 
	 * @return The Call Option Price
	 */

	public double callPrice()
	{
		return _dblCallPrice;
	}

	/**
	 * The Call Prob 1 Term
	 * 
	 * @return The Call Prob 1 Term
	 */

	public double callProb1()
	{
		return _dblCallProb1;
	}

	/**
	 * The Call Prob 2 Term
	 * 
	 * @return The Call Prob 2 Term
	 */

	public double callProb2()
	{
		return _dblCallProb2;
	}

	/**
	 * The Call Option Delta
	 * 
	 * @return The Call Option Delta
	 */

	public double callDelta()
	{
		return _dblCallDelta;
	}

	/**
	 * The Call Option Vega
	 * 
	 * @return The Call Option Vega
	 */

	public double callVega()
	{
		return _dblCallVega;
	}

	/**
	 * The Call Option Theta
	 * 
	 * @return The Call Option Theta
	 */

	public double callTheta()
	{
		return _dblCallTheta;
	}

	/**
	 * The Call Option Rho
	 * 
	 * @return The Call Option Rho
	 */

	public double callRho()
	{
		return _dblCallRho;
	}

	/**
	 * The Call Option Gamma
	 * 
	 * @return The Call Option Gamma
	 */

	public double callGamma()
	{
		return _dblCallGamma;
	}

	/**
	 * The Call Option Vanna
	 * 
	 * @return The Call Option Vanna
	 */

	public double callVanna()
	{
		return _dblCallVanna;
	}

	/**
	 * The Call Option Vomma
	 * 
	 * @return The Call Option Vomma
	 */

	public double callVomma()
	{
		return _dblCallVomma;
	}

	/**
	 * The Call Option Charm
	 * 
	 * @return The Call Option Charm
	 */

	public double callCharm()
	{
		return _dblCallCharm;
	}

	/**
	 * The Call Option Veta
	 * 
	 * @return The Call Option Veta
	 */

	public double callVeta()
	{
		return _dblCallVeta;
	}

	/**
	 * The Call Option Color
	 * 
	 * @return The Call Option Color
	 */

	public double callColor()
	{
		return _dblCallColor;
	}

	/**
	 * The Call Option Speed
	 * 
	 * @return The Call Option Speed
	 */

	public double callSpeed()
	{
		return _dblCallSpeed;
	}

	/**
	 * The Call Option Ultima
	 * 
	 * @return The Call Option Ultima
	 */

	public double callUltima()
	{
		return _dblCallUltima;
	}

	/**
	 * The Put Option Price
	 * 
	 * @return The Put Option Price
	 */

	public double putPrice()
	{
		return _dblPutPrice;
	}

	/**
	 * The Put Option Price Computed from the Put-Call Parity Relation
	 * 
	 * @return The Put Option Price Computed from the Put-Call Parity Relation
	 */

	public double putPriceFromParity()
	{
		return _dblPutPriceFromParity;
	}

	/**
	 * The Put Prob 1 Term
	 * 
	 * @return The Put Prob 1 Term
	 */

	public double putProb1()
	{
		return _dblPutProb1;
	}

	/**
	 * The Put Prob 2 Term
	 * 
	 * @return The Put Prob 2 Term
	 */

	public double putProb2()
	{
		return _dblPutProb2;
	}

	/**
	 * The Put Delta
	 * 
	 * @return The Put Delta
	 */

	public double putDelta()
	{
		return _dblPutDelta;
	}

	/**
	 * The Put Option Vega
	 * 
	 * @return The Put Option Vega
	 */

	public double putVega()
	{
		return _dblPutVega;
	}

	/**
	 * The Put Option Theta
	 * 
	 * @return The Put Option Theta
	 */

	public double putTheta()
	{
		return _dblPutTheta;
	}

	/**
	 * The Put Option Rho
	 * 
	 * @return The Put Option Rho
	 */

	public double putRho()
	{
		return _dblPutRho;
	}

	/**
	 * The Put Option Gamma
	 * 
	 * @return The Put Option Gamma
	 */

	public double putGamma()
	{
		return _dblPutGamma;
	}

	/**
	 * The Put Option Vanna
	 * 
	 * @return The Put Option Vanna
	 */

	public double putVanna()
	{
		return _dblPutVanna;
	}

	/**
	 * The Put Option Vomma
	 * 
	 * @return The Put Option Vomma
	 */

	public double putVomma()
	{
		return _dblPutVomma;
	}

	/**
	 * The Put Option Charm
	 * 
	 * @return The Put Option Charm
	 */

	public double putCharm()
	{
		return _dblPutCharm;
	}

	/**
	 * The Put Option Veta
	 * 
	 * @return The Put Option Veta
	 */

	public double putVeta()
	{
		return _dblPutVeta;
	}

	/**
	 * The Put Option Color
	 * 
	 * @return The Put Option Color
	 */

	public double putColor()
	{
		return _dblPutColor;
	}

	/**
	 * The Put Option Speed
	 * 
	 * @return The Put Option Speed
	 */

	public double putSpeed()
	{
		return _dblPutSpeed;
	}

	/**
	 * The Put Option Ultima
	 * 
	 * @return The Put Option Ultima
	 */

	public double putUltima()
	{
		return _dblPutUltima;
	}
}

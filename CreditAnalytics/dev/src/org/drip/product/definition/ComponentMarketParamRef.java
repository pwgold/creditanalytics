
package org.drip.product.definition;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
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
 * ComponentMarketParamRef interface provides stubs for name, IR curve, forward curve, credit curve, TSY
 * 	curve, and needed to value the component.
 *
 * @author Lakshmi Krishnamurthy
 */

public interface ComponentMarketParamRef {

	/**
	 * Get the component name
	 * 
	 * @return The component name
	 */

	public abstract java.lang.String name();

	/**
	 * Get the Map of Coupon Currencies
	 * 
	 * @return The Map of Coupon Currencies
	 */

	public abstract org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.String> couponCurrency();

	/**
	 * Get the Pay Currency
	 * 
	 * @return The Pay Currency
	 */

	public abstract java.lang.String payCurrency();

	/**
	 * Get the Principal Currency
	 * 
	 * @return The Principal Currency
	 */

	public abstract java.lang.String principalCurrency();

	/**
	 * Get the Credit Curve Latent State Identifier Label
	 * 
	 * @return The Credit Curve Latent State Identifier Label
	 */

	public abstract org.drip.state.identifier.CreditLabel creditLabel();

	/**
	 * Get the Map of Forward Curve Latent State Labels
	 * 
	 * @return The Map of the Forward Curve Latent State Labels
	 */

	public abstract org.drip.analytics.support.CaseInsensitiveTreeMap<org.drip.state.identifier.ForwardLabel>
		forwardLabel();

	/**
	 * Get the Funding Curve Latent State Label
	 * 
	 * @return Funding Curve Latent State Label
	 */

	public abstract org.drip.state.identifier.FundingLabel fundingLabel();

	/**
	 * Get the Map of FX Latent State Identifier Labels
	 * 
	 * @return The Map of FX Latent State Identifier Labels
	 */

	public abstract org.drip.analytics.support.CaseInsensitiveTreeMap<org.drip.state.identifier.FXLabel>
		fxLabel();
}

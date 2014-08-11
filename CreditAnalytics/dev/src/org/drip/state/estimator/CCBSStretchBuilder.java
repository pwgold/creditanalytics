
package org.drip.state.estimator;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
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
 * CCBSStretchRepresentationBuilder contains the Representation Spec Creation Routines for Forward Curve
 * 	Stretches to be built out of CCBS Quotes.
 *
 * @author Lakshmi Krishnamurthy
 */

public class CCBSStretchBuilder {

	/**
	 * Construct an instance of LatentStateStretchSpec for the Construction of the Forward Curve from the
	 * 	specified Inputs
	 * 
	 * @param strName Stretch Name
	 * @param aCCSP Array of Calibration Cross Currency Swap Pair Instances
	 * @param valParams The Valuation Parameters
	 * @param mktParams The Basket Market Parameters to imply the Market Quote Measure
	 * @param adblReferenceComponentBasis Array of the Reference Component Reference Leg Basis Spread
	 * @param bBasisOnDerivedLeg TRUE => Apply the Basis on the Derived Leg (FALSE => Reference Leg)
	 * 
	 * return Instance of LatentStateStretchSpec
	 */

	public static final org.drip.state.inference.LatentStateStretchSpec ForwardStretch (
		final java.lang.String strName,
		final org.drip.product.fx.ComponentPair[] aCCSP,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.market.CurveSurfaceQuoteSet mktParams,
		final double[] adblReferenceComponentBasis,
		final boolean bBasisOnDerivedLeg)
	{
		if (null == aCCSP || null == mktParams || null == adblReferenceComponentBasis) return null;

		int iNumCCSP = aCCSP.length;

		if (0 == iNumCCSP || adblReferenceComponentBasis.length != iNumCCSP) return null;

		org.drip.state.inference.LatentStateSegmentSpec[] aSegmentSpec = new
			org.drip.state.inference.LatentStateSegmentSpec[iNumCCSP];

		for (int i = 0; i < iNumCCSP; ++i) {
			if (null == aCCSP[i]) return null;

			org.drip.product.calib.ProductQuoteSet pqs = null;

			org.drip.product.definition.CalibratableFixedIncomeComponent comp = aCCSP[i].derivedComponent();

			org.drip.state.identifier.ForwardLabel[] aForwardLabel = comp instanceof
				org.drip.product.cashflow.DualStreamComponent ?
					((org.drip.product.cashflow.DualStreamComponent) comp).derivedStream().forwardLabel() :
						comp.forwardLabel();

			if (null == aForwardLabel || 0 == aForwardLabel.length) return null;

			try { 
				pqs = comp.calibQuoteSet (new org.drip.state.representation.LatentStateSpecification[] {new
					org.drip.state.representation.LatentStateSpecification
						(org.drip.analytics.rates.ForwardCurve.LATENT_STATE_FORWARD,
							org.drip.analytics.rates.ForwardCurve.QUANTIFICATION_METRIC_FORWARD_RATE,
								aForwardLabel[0])});
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapOP = aCCSP[i].value
				(valParams, null, mktParams, null);

			org.drip.product.definition.CalibratableFixedIncomeComponent rcReference =
				aCCSP[i].referenceComponent();

			java.lang.String strReferenceComponentName = rcReference.name();

			java.lang.String strReferenceComponentPV = strReferenceComponentName + "[PV]";
			java.lang.String strReferenceComponentReferenceLegCleanDV01 = strReferenceComponentName +
				"[ReferenceCleanDV01]";
			java.lang.String strReferenceComponentDerivedLegCleanDV01 = strReferenceComponentName +
				"[DerivedCleanDV01]";

			if (null == mapOP || !mapOP.containsKey (strReferenceComponentPV) || !mapOP.containsKey
				(strReferenceComponentReferenceLegCleanDV01) || !mapOP.containsKey
					(strReferenceComponentDerivedLegCleanDV01))
				return null;

			if (!pqs.set ("PV", -1. * (mapOP.get (strReferenceComponentPV) + 10000. * (bBasisOnDerivedLeg ?
				mapOP.get (strReferenceComponentDerivedLegCleanDV01) : mapOP.get
					(strReferenceComponentReferenceLegCleanDV01)) * adblReferenceComponentBasis[i])))
				return null;

			try {
				aSegmentSpec[i] = new org.drip.state.inference.LatentStateSegmentSpec (comp, pqs);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		try {
			return new org.drip.state.inference.LatentStateStretchSpec (strName, aSegmentSpec);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct an instance of LatentStateStretchSpec for the Construction of the Discount Curve from the
	 * 	specified Inputs
	 * 
	 * @param strName Stretch Name
	 * @param aCCSP Array of Calibration Cross Currency Swap Pair Instances
	 * @param valParams The Valuation Parameters
	 * @param mktParams The Basket Market Parameters to imply the Market Quote Measure
	 * @param adblReferenceComponentBasis Array of the Reference Component Reference Leg Basis Spread
	 * @param adblSwapRate Array of the IRS Calibration Swap Rates
	 * @param bBasisOnDerivedLeg TRUE => Apply the Basis on the Derived Leg (FALSE => Reference Leg)
	 * 
	 * return Instance of LatentStateStretchSpec
	 */

	public static final org.drip.state.inference.LatentStateStretchSpec DiscountStretch (
		final java.lang.String strName,
		final org.drip.product.fx.ComponentPair[] aCCSP,
		final org.drip.param.valuation.ValuationParams valParams,
		final org.drip.param.market.CurveSurfaceQuoteSet mktParams,
		final double[] adblReferenceComponentBasis,
		final double[] adblSwapRate,
		final boolean bBasisOnDerivedLeg)
	{
		if (null == aCCSP || null == mktParams || null == adblReferenceComponentBasis || null == adblSwapRate)
			return null;

		int iNumCCSP = aCCSP.length;

		if (0 == iNumCCSP || adblReferenceComponentBasis.length != iNumCCSP || adblSwapRate.length !=
			iNumCCSP)
			return null;

		org.drip.state.inference.LatentStateSegmentSpec[] aSegmentSpec = new
			org.drip.state.inference.LatentStateSegmentSpec[iNumCCSP];

		for (int i = 0; i < iNumCCSP; ++i) {
			if (null == aCCSP[i]) return null;

			org.drip.product.definition.CalibratableFixedIncomeComponent comp = aCCSP[i].derivedComponent();

			org.drip.product.calib.ProductQuoteSet pqs = null;

			org.drip.state.identifier.ForwardLabel[] aForwardLabel = comp instanceof
				org.drip.product.cashflow.DualStreamComponent ?
					((org.drip.product.cashflow.DualStreamComponent) comp).derivedStream().forwardLabel() :
						comp.forwardLabel();

			if (null == aForwardLabel || 0 == aForwardLabel.length) return null;

			try { 
				pqs = comp.calibQuoteSet (new org.drip.state.representation.LatentStateSpecification[] {new
					org.drip.state.representation.LatentStateSpecification
						(org.drip.analytics.rates.DiscountCurve.LATENT_STATE_DISCOUNT,
							org.drip.analytics.rates.DiscountCurve.QUANTIFICATION_METRIC_DISCOUNT_FACTOR,
								org.drip.state.identifier.FundingLabel.Standard
									(comp.couponCurrency()[0])), new
										org.drip.state.representation.LatentStateSpecification
											(org.drip.analytics.rates.ForwardCurve.LATENT_STATE_FORWARD,
												org.drip.analytics.rates.ForwardCurve.QUANTIFICATION_METRIC_FORWARD_RATE,
													aForwardLabel[0])});
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}

			org.drip.analytics.support.CaseInsensitiveTreeMap<java.lang.Double> mapOP = aCCSP[i].value
				(valParams, null, mktParams, null);

			org.drip.product.definition.CalibratableFixedIncomeComponent rcReference =
				aCCSP[i].referenceComponent();

			java.lang.String strReferenceComponentName = rcReference.name();

			java.lang.String strReferenceComponentPV = strReferenceComponentName + "[PV]";
			java.lang.String strReferenceComponentReferenceLegCleanDV01 = strReferenceComponentName +
				"[ReferenceCleanDV01]";
			java.lang.String strReferenceComponentDerivedLegCleanDV01 = strReferenceComponentName +
				"[DerivedCleanDV01]";

			if (null == mapOP || !mapOP.containsKey (strReferenceComponentPV) || !mapOP.containsKey
				(strReferenceComponentReferenceLegCleanDV01) || !mapOP.containsKey
					(strReferenceComponentDerivedLegCleanDV01))
				return null;

			if (!pqs.set ("SwapRate", adblSwapRate[i]) || !pqs.set ("PV", -1. * (mapOP.get
				(strReferenceComponentPV) + 10000. * (bBasisOnDerivedLeg ? mapOP.get
					(strReferenceComponentDerivedLegCleanDV01) : mapOP.get
						(strReferenceComponentReferenceLegCleanDV01)) * adblReferenceComponentBasis[i])))
				return null;

			try {
				aSegmentSpec[i] = new org.drip.state.inference.LatentStateSegmentSpec (comp, pqs);
			} catch (java.lang.Exception e) {
				e.printStackTrace();

				return null;
			}
		}

		try {
			return new org.drip.state.inference.LatentStateStretchSpec (strName, aSegmentSpec);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
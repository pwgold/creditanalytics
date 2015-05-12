
package org.drip.classifier.functionclass;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2015 Lakshmi Krishnamurthy
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
 * GeneralizedClassifierFunctionClass implements the Class that holds the Space of Classifier Functions.
 *  Class-Specific Asymptotic Sample, Covering Bounds and other Parameters are also maintained.
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class GeneralizedClassifierFunctionClass extends org.drip.spaces.functionclass.R1ToR1Class {
	private org.drip.classifier.functionclass.ConcentrationExpectedLossAsymptote _asymptote = null;

	/**
	 * GeneralizedClassifierFunctionClass Constructor
	 * 
	 * @param aClassifier Array of Classifiers belonging to the Function Class
	 * @param asymptote Asymptotic Bounds Behavior of the Function Class
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public GeneralizedClassifierFunctionClass (
		final org.drip.spaces.function.NormedR1ToR1[] aR1ToR1FunctionSpace,
		final org.drip.classifier.functionclass.ConcentrationExpectedLossAsymptote asymptote)
		throws java.lang.Exception
	{
		super (aR1ToR1FunctionSpace);

		_asymptote = asymptote;
	}

	/**
	 * Retrieve the Array of Classifiers
	 * 
	 * @return The Array of Classifiers
	 */

	public org.drip.classifier.functionclass.AbstractBinaryClassifier[] classifiers()
	{
		org.drip.function.deterministic.R1ToR1[] aR1ToR1 = functionR1ToR1Set();

		int iNumFunction = aR1ToR1.length;
		org.drip.classifier.functionclass.AbstractBinaryClassifier[] aABE = new
			org.drip.classifier.functionclass.AbstractBinaryClassifier[iNumFunction];

		for (int i = 0; i < iNumFunction; ++i) {
			if (!(aR1ToR1[i] instanceof org.drip.classifier.functionclass.AbstractBinaryClassifier))
				return null;

			aABE[i] = (org.drip.classifier.functionclass.AbstractBinaryClassifier) aR1ToR1[i];
		}

		return aABE;
	}

	/**
	 * Retrieve the Class Asymptotic Bounds Behavior
	 * 
	 * @return The Class Asymptotic Bounds Behavior
	 */

	public org.drip.classifier.functionclass.ConcentrationExpectedLossAsymptote asymptote()
	{
		return _asymptote;
	}
}

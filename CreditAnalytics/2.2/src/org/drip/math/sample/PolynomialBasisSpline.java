
package org.drip.math.sample;

import org.drip.math.calculus.WengertJacobian;
import org.drip.math.function.*;
import org.drip.math.grid.Segment;
import org.drip.math.spline.*;

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
 * PolynomialBasisSpline implements Samples for the Construction and the usage of polynomial basis spline
 * 	functions. It demonstrates the following:
 * 	- Control the polynomial segment using the rational shape controller, the appropriate Ck, and the basis
 * 		function.
 * 	- Demonstrate the variational shape optimization behavior.
 * 	- Interpolate the node value and the node value Jacobian with the segment, as well as at the boundaries.
 * 	- Calculate the segment monotonicity.
 *
 * @author Lakshmi Krishnamurthy
 */

public class PolynomialBasisSpline {

	/*
	 * This sample demonstrates the following:
	 * 
	 * 	- Construction of two segments, 1 and 2.
	 *  - Calibration of the segments to the left and the right node values
	 *  - Extraction of the segment Jacobians and segment monotonicity
	 *  - Interpolate point value and the Jacobian
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final void TestPolynomialSpline (
		final int iNumBasis,
		final int iCk,
		final AbstractUnivariate rsc)
		throws Exception
	{
		System.out.println (" ------------------------------ \n     POLYNOMIAL n = " + iNumBasis +
			"; Ck = " + iCk + "\n ------------------------------ \n");

		/*
		 * Construct the segment inelastic parameter that is C2 (iCk = 2 sets it to C2), without constraint
		 */

		SegmentInelasticParams segParams = new SegmentInelasticParams (iCk, null);

		/*
		 * Create the basis parameter set from the number of basis functions, and construct the basis
		 */

		PolynomialBasisSetParams polybsbp = new PolynomialBasisSetParams (iNumBasis);

		AbstractUnivariate[] aAU = SegmentBasisSetBuilder.PolynomialBasisSet (polybsbp);

		/*
		 * Construct the left and the right segments
		 */

		Segment seg1 = SegmentBasisSetBuilder.CreateCk (1.0, 1.5, aAU, rsc, segParams);

		Segment seg2 = SegmentBasisSetBuilder.CreateCk (1.5, 2.0, aAU, rsc, segParams);

		/*
		 * Calibrate the left segment using the node values, and compute the segment Jacobian
		 */

		WengertJacobian wj1 = seg1.calibrateJacobian (25., 0., 20.25);

		System.out.println ("\tY[" + 1.0 + "]: " + seg1.calcValue (1.));

		System.out.println ("\tY[" + 1.5 + "]: " + seg1.calcValue (1.5));

		System.out.println ("Segment 1 Jacobian: " + wj1.displayString());

		System.out.println ("Segment 1 Head: " + seg1.calcJacobian().displayString());

		System.out.println ("Segment 1 Monotone Type: " + seg1.monotoneType());

		/*
		 * Calibrate the right segment using the node values, and compute the segment Jacobian
		 */

		WengertJacobian wj2 = seg2.calibrateJacobian (seg1, 16.);

		System.out.println ("\tY[" + 1.5 + "]: " + seg2.calcValue (1.5));

		System.out.println ("\tY[" + 2. + "]: " + seg2.calcValue (2.));

		System.out.println ("Segment 2 Jacobian: " + wj2.displayString());

		System.out.println ("Segment 2 Regular Jacobian: " + seg2.calcJacobian().displayString());

		System.out.println ("Segment 2 Monotone Type: " + seg2.monotoneType());

		seg2.calibrate (seg1, 14.);

		/*
		 * Interpolate the segment value at the given variate, and compute the corresponding Jacobian
		 */

		double dblX = 2.0;

		System.out.println ("\t\tValue[" + dblX + "]: " + seg2.calcValue (dblX));

		System.out.println ("\t\tValue Jacobian[" + dblX + "]: " + seg2.calcValueJacobian (dblX).displayString());
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		/*
		 * Construct a rational shape controller with the shape controller tension of 1.
		 */

		double dblShapeControllerTension = 1.;

		AbstractUnivariate rsc = new RationalShapeControl (dblShapeControllerTension);

		/*
		 * Test the polynomial spline across different polynomial degrees and Ck's
		 */

		TestPolynomialSpline (2, 0, rsc);

		TestPolynomialSpline (3, 1, rsc);

		TestPolynomialSpline (4, 1, rsc);

		TestPolynomialSpline (4, 2, rsc);

		TestPolynomialSpline (5, 1, rsc);

		TestPolynomialSpline (5, 2, rsc);

		TestPolynomialSpline (5, 3, rsc);

		TestPolynomialSpline (6, 1, rsc);

		TestPolynomialSpline (6, 2, rsc);

		TestPolynomialSpline (6, 3, rsc);

		TestPolynomialSpline (6, 4, rsc);

		TestPolynomialSpline (7, 1, rsc);

		TestPolynomialSpline (7, 2, rsc);

		TestPolynomialSpline (7, 3, rsc);

		TestPolynomialSpline (7, 4, rsc);

		TestPolynomialSpline (7, 5, rsc);
	}
}

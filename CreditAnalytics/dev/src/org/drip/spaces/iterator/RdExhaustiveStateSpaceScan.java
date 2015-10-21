
package org.drip.spaces.iterator;

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
 * RdExhaustiveStateSpaceScan contains the Functionality to iterate exhaustively through the R^d Space.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class RdExhaustiveStateSpaceScan extends org.drip.spaces.iterator.RdSpanningStateSpaceScan {

	/**
	 * RdExhaustiveStateSpaceScan Constructor
	 * 
	 * @param aiTerminalStateIndex Upper Array Bounds for each Dimension
	 * @param bCyclicalScan TRUE => Cycle Post a Full Scan
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public RdExhaustiveStateSpaceScan (
		final int[] aiTerminalStateIndex,
		final boolean bCyclicalScan)
		throws java.lang.Exception
	{
		super (aiTerminalStateIndex, bCyclicalScan);
	}

	@Override public int[] resetStateIndexCursor()
	{
		int iDimension = dimension();

		int[] aiStateIndexCursor = stateIndexCursor();

		for (int i = 0; i < iDimension; ++i)
			aiStateIndexCursor[i] = 0;

		return setStateIndexCursor (aiStateIndexCursor) ? aiStateIndexCursor : null;
	}

	@Override public int[] nextStateIndexCursor()
	{
		int iDimension = dimension();

		int iStateIndexToUpdate = -1;

		int[] aiStateIndexCursor = stateIndexCursor();

		int[] aiTerminalStateIndex = terminalStateIndex();

		for (int i = iDimension - 1; i >= 0; --i) {
			if (aiStateIndexCursor[i] != aiTerminalStateIndex[i] - 1) {
				iStateIndexToUpdate = i;
				break;
			}
		}

		if (-1 == iStateIndexToUpdate) return cyclicalScan() ? resetStateIndexCursor() : null;

		aiStateIndexCursor[iStateIndexToUpdate] = aiStateIndexCursor[iStateIndexToUpdate] + 1;

		for (int i = iStateIndexToUpdate + 1; i < iDimension; ++i)
			aiStateIndexCursor[i] = 0;

		return setStateIndexCursor (aiStateIndexCursor) ? aiStateIndexCursor : null;
	}
}

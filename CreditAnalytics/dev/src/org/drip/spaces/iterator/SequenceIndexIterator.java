
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
 * SequenceIndexIterator contains the Functionality to iterate through a List of Sequence Indexes.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class SequenceIndexIterator {
	private int[] _aiMax = null;
	private int _iIndexCursor = -1;
	private boolean _bCycle = false;
	private int _iSequenceCursor = -1;

	/**
	 * Create a Standard Sequence/Index Iterator
	 * 
	 * @param iNumSequence Number Variable Sequences
	 * @param iNumIndex Number of Indexes per Variable Sequence
	 * 
	 * @return The Sequence/Index Iterator Instance
	 */

	public static final SequenceIndexIterator Standard (
		final int iNumSequence,
		final int iNumIndex)
	{
		if (0 >= iNumSequence || 0 >= iNumIndex) return null;

		int[] aiMax = new int[iNumSequence];

		for (int i = 0; i < iNumSequence; ++i)
			aiMax[i] = iNumIndex - 1;

		try {
			return new SequenceIndexIterator (aiMax, false);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private int[] setFromCursor()
	{
		int iNumSequence = _aiMax.length;
		int[] aiCurrent = new int[iNumSequence];

		for (int i = 0; i < iNumSequence; ++i) {
			if (i < _iSequenceCursor)
				aiCurrent[i] = _aiMax[i];
			else if (i > _iSequenceCursor)
				aiCurrent[i] = 0;
			else
				aiCurrent[i] = _iIndexCursor;
		}

		return aiCurrent;
	}

	/**
	 * IndexIterator Constructor
	 * 
	 * @param aiMax Maximum Entries per Index
	 * @param bCycle TRUE => Cycle around the Index Entries
	 * 
	 * @throws java.lang.Exception Thrown if Inputs are incalid
	 */

	public SequenceIndexIterator (
		final int[] aiMax,
		final boolean bCycle)
		throws java.lang.Exception
	{
		if (null == (_aiMax = aiMax))
			throw new java.lang.Exception ("SequenceIndexIterator ctr => Invalid Inputs");

		_bCycle = bCycle;
		_iIndexCursor = 0;
		_iSequenceCursor = 0;
		int iNumSequence = _aiMax.length;

		if (0 == iNumSequence) throw new java.lang.Exception ("SequenceIndexIterator ctr => Invalid Inputs");

		for (int i = 0; i < iNumSequence; ++i) {
			if (0 > _aiMax[i]) throw new java.lang.Exception ("SequenceIndexIterator ctr => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the First Cursor
	 * 
	 * @return The First Cursor
	 */

	public int[] first()
	{
		_iIndexCursor = 0;
		_iSequenceCursor = 0;

		return setFromCursor();
	}

	/**
	 * Retrieve the Next Cursor
	 * 
	 * @return The Next Cursor
	 */

	public int[] next()
	{
		if (++_iIndexCursor <= _aiMax[_iSequenceCursor]) return setFromCursor();

		_iIndexCursor = 0;

		if (++_iSequenceCursor < _aiMax.length) return setFromCursor();

		return _bCycle ? first() : null;
	}

	/**
	 * Retrieve the Size of the Iterator
	 * 
	 * @return Size of the Iterator
	 */

	public int size()
	{
		int iSize = 0;
		int iNumSequence = _aiMax.length;

		for (int i = 0; i < iNumSequence; ++i)
			iSize += _aiMax[i] + 1;

		return iSize;
	}
}

/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite CSharp Client
 * Copyright (C) 2012, 2013 Zimbra Software, LLC.
 * 
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.4 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
// Copyright (c) 2005-2011 Quadralay Corporation.  All rights reserved.
//

function Pairs_Object(ParamArray)
{
  // Store Original Array
  //
  this.mOriginalArray = ParamArray;

  // Hash of word pairs to test, keys are prefixed by '~'
  //
  this.mPairsHash = new Object();

  // Define Functions for this object
  //
  this.fGetPairs     = Pairs_GetPairs;
  this.fResetMatches = Pairs_ResetMatches;
  this.fTestPair     = Pairs_TestPair;
  this.fIsMatch      = Pairs_IsMatch;
  this.fCreateHash   = Pairs_CreateHash;
}

// Create the Hash of Word Pairs
//
function Pairs_CreateHash()
{
  var index = 0;
  var innerHash;
  var lastWord;
  var currentWord;
  var currentInnerHash;
  var innerHashValue

  for(index = 0; index < this.mOriginalArray.length; index++)
  {
    innerHash = new Object();
    if (index == 0)
    {
      lastWord = "~" + this.mOriginalArray[index];
    }
    else
    {
      currentWord = "~" + this.mOriginalArray[index];
      currentInnerHash = this.mPairsHash[lastWord];
      if(currentInnerHash != null)
      {
        innerHashValue = currentInnerHash[currentWord]
        if(innerHashValue == null)
        {
          currentInnerHash[currentWord] = 0;
        }
      }
      else
      {
        innerHash[currentWord] = 0;
        this.mPairsHash[lastWord] = innerHash;
      }

      lastWord = currentWord;
    }
  }
}

// Accessor function to return the hash
//
function Pairs_GetPairs()
{
  return this.mPairsHash;
}

// After each doc is tested for the occurrence
// of the pairs in the search phrase, the recorded
// matches in the hash can be reset for the next
// doc to test
//
function Pairs_ResetMatches()
{
  var outerKey;
  var innerKey;
  var innerHash;

  for(outerKey in this.mPairsHash)
  {
    innerHash = this.mPairsHash[outerKey];
    for(innerKey in innerHash)
    {
      innerHash[innerKey] = 0;
    }
  }
}

// The list of word pairs generated during output
// calls this function with each word pair in the doc
// if the word pair is present in the hash created
// from the search phrase, then that match is recorded.
//
function Pairs_TestPair(ParamFirst, ParamSecond)
{
  var innerHash;

  innerHash = this.mPairsHash["~" + ParamFirst];
  if(this.mPairsHash["~" + ParamFirst] != null &&
      this.mPairsHash["~" + ParamFirst]["~" + ParamSecond] != null)
  {
    this.mPairsHash["~" + ParamFirst]["~" + ParamSecond]++;
  }
}

// IsMatch iterates all keys of mPairsHash testing
// whether the inner hashes have values greater than 0
// If all inner hash values are greater than 0, then
// There is a match on the phrase in the doc.
//
function Pairs_IsMatch()
{
  var result = true;

  var outerKey;
  var innerKey;
  var matchValue;

  for(outerKey in this.mPairsHash)
  {
    for(innerKey in this.mPairsHash[outerKey])
    {
      matchValue = this.mPairsHash[outerKey][innerKey];

      if(matchValue <= 0)
      {
        result = false;
        break;
      }
    }

    if(!result)
    {
      break;
    }
  }

  return result;
}
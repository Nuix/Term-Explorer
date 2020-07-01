package com.nuix.termexplorer;

import com.nuix.superutilities.misc.ExpandedTermInfo;

public class ExpandedTermAndSeachInfo {
	private ExpandedTermInfo termInfo = null;
	private long scopeItemCount = 0; 
	
	public ExpandedTermAndSeachInfo(ExpandedTermInfo termInfo) {
		this.termInfo = termInfo;
	}

	public long getScopeItemCount() {
		return scopeItemCount;
	}

	public void setScopeItemCount(long scopeItemCount) {
		this.scopeItemCount = scopeItemCount;
	}

	public String getMatchedTerm() {
		return termInfo.getMatchedTerm();
	}

	public long getOcurrences() {
		return termInfo.getOcurrences();
	}

	public String getOriginalTerm() {
		return termInfo.getOriginalTerm();
	}

	public float getSimilarity() {
		return termInfo.getSimilarity();
	}
}

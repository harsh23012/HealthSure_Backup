package com.infinite.jsf.provider.dao;

import java.util.List;

import com.infinite.jsf.provider.model.ClaimHistory;

public interface ClaimHistorySearchDao {
	
//	Search claims history (only for this Provider, based on date range, status, or HID or Claim ID)
//	List<ClaimHistory> searchClaimByClaimId(String claimId)	;
//	
//	List<ClaimHistory>findAllClaims();
	
	public List<ClaimHistory> findAllClaimsHistory() ;
}

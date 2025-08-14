package com.infinite.jsf.test;

import java.util.List;

import com.infinite.jsf.provider.controller.ClaimHistorySearchController;
import com.infinite.jsf.provider.dao.ClaimHistorySearchDao;
import com.infinite.jsf.provider.daoImpl.ClaimHistorySearchDaoImpl;
import com.infinite.jsf.provider.model.ClaimHistory;

public class ClaimHistorySearchTest {
	public static void main(String[] args) {
		
		ClaimHistorySearchDao dao=new ClaimHistorySearchDaoImpl();
		ClaimHistorySearchController controller=new ClaimHistorySearchController();
		
//		System.out.println(dao.searchClaimByClaimId(""));
		
//		System.out.println("=============searchby id is called===================   ");
//		System.out.println("=============searchby id is called===================   ");
//		System.out.println("=============searchby id is called===================   ");
//		System.out.println("=============searchby id is called===================   ");
//		System.out.println("=============searchby id is called===================   ");

//	dao.findAllClaims().forEach(System.out::println);
	
//		  List<ClaimHistory> claimHistoryList=dao.findAllClaimsHistory();
//		  System.out.println(claimHistoryList);
//	 
		controller.findAllClaimHistorycontroller().forEach(System.out::println);
	}

}

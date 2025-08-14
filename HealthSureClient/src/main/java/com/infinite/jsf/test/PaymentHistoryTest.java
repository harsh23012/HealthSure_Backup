package com.infinite.jsf.test;

import com.infinite.jsf.provider.dao.PaymentHistoryDao;
import com.infinite.jsf.provider.daoImpl.PaymentHistoryDaoImpl;

public class PaymentHistoryTest {
	public static void main(String[] args) {
		
		System.out.println("Hello world");
		
//      PaymentHistoryController controller=new PaymentHistoryController();
//      
//      controller.showAllPaymentHistory().forEach(System.out::println);
		
		PaymentHistoryDao dao=new PaymentHistoryDaoImpl();
       dao.showPaymentHistory().forEach(System.out::println);

	}

}

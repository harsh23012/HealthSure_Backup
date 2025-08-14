package com.infinite.jsf.provider.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.infinite.jsf.provider.dao.ClaimHistorySearchDao;
import com.infinite.jsf.provider.model.ClaimHistory;
import com.infinite.jsf.util.SessionHelper;

public class ClaimHistorySearchDaoImpl implements ClaimHistorySearchDao {

	static SessionFactory factory;
	static Session session;
	static {
		factory = SessionHelper.getSessionFactory();

	}

	public List<ClaimHistory> findAllClaimsHistory() {

		List<ClaimHistory> claimHistoryList = null;

		Session session = null;
		Transaction trans = null;

		session = factory.openSession();
		trans = session.beginTransaction();

		String hql = "FROM ClaimHistory ch JOIN FETCH ch.claim";
		Query query = session.createQuery(hql);
		claimHistoryList = query.list();

		trans.commit();

		session.close();

		return claimHistoryList;
	}

}

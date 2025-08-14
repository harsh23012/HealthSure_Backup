package com.infinite.jsf.provider.daoImpl;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.infinite.jsf.provider.dao.PaymentHistoryDao;
import com.infinite.jsf.provider.model.PaymentHistory;
import com.infinite.jsf.util.SessionHelper;

/**
 * Implementation class for PaymentHistoryDao interface.
 * Handles database operations related to PaymentHistory using Hibernate.
 */
public class PaymentHistoryDaoImpl implements PaymentHistoryDao {

    // Static reference to Hibernate SessionFactory for creating sessions
    static SessionFactory factory;

    // Static reference to Hibernate Session used for database operations
    static Session session;

    // Logger instance for logging information and debugging
    private static final Logger logger = Logger.getLogger(PaymentHistoryDaoImpl.class);

    // Static block to initialize the SessionFactory using a helper class
    static {
        factory = SessionHelper.getSessionFactory();
    }

    /**
     * Fetches all payment history records from the database.
     * 
     *
     */
    @Override
    public List<PaymentHistory> showPaymentHistory() {
        // Logging the start of the session
        logger.info("Zainab Opening session to fetch payment history");

        // Declare a list to hold the fetched payment history records
        List<PaymentHistory> history = null;

        // Open a new session from the factory
        session = factory.openSession();

        // Begin a transaction to ensure data consistency
        Transaction trans = session.beginTransaction();

        // Log the query being executed
        logger.info("Zainab Executing query: from PaymentHistory");

        // Execute HQL query to fetch all records from PaymentHistory table
        history = session.createQuery("from PaymentHistory").list();

        // Commit the transaction after successful query execution
        trans.commit();

        // Log successful transaction commit
        logger.info("Zainab Transaction committed successfully");

        // Close the session to release database resources
        session.close();

        // Return the list of payment history records
        return history;
    }
}

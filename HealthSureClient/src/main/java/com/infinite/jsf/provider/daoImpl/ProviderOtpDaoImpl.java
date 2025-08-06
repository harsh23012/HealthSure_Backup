package com.infinite.jsf.provider.daoImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
//import java.util.Date;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.infinite.jsf.provider.dao.ProviderOtpDao;
import com.infinite.jsf.util.MailSend;
import com.infinite.jsf.util.SessionHelper;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.provider.model.ProviderOtp;
import com.infinite.jsf.provider.model.Reason;

public class ProviderOtpDaoImpl implements ProviderOtpDao{
	Session session;
	SessionFactory sf;
	
//	@Override
//	public String insertOtp(ProviderOtp otp) throws ClassNotFoundException, SQLException {
//		 session = SessionHelper.getSessionFactory().openSession();
//	        Transaction tx = session.beginTransaction();
//
//	        // Send OTP Email
//	        String subject = "Your OTP for HealthSure Registration";
//	        String body = "Your OTP is: " + otp.getOtpCode() + ". It is valid for 10 minutes.";
//	        MailSend.sendInfo(otp.getProviderId(), subject, body);
//
//	        return "OTP inserted and email sent successfully.";
//	}

	@Override
	public String verifyOtp(String email, String otpCode, Reason reason) throws ClassNotFoundException, SQLException {
		session = SessionHelper.getSessionFactory().openSession();
        
		
        String hql = "FROM ProviderOtp WHERE email = :email AND otpCode = :otpCode AND isVerified = false";
        Query query = session.createQuery(hql);
        query.setParameter("email", email);
        query.setParameter("otpCode", otpCode);

        ProviderOtp otp = (ProviderOtp) query.uniqueResult();

        if (otp != null) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            System.out.println("OTP Found. Expires at: " + otp.getExpiresAt());
            System.out.println("Current Time: " + now);

            if (now.after(otp.getExpiresAt())) {
                return "OTP expired. Please request a new one.";
            }
            else {
            Transaction tx = session.beginTransaction();
            otp.setIsVerified(true);
            session.update(otp);
            tx.commit();
            session.close();

            return "OTP verified successfully.";
            }
        } else {
            session.close();
            return "Invalid OTP or already verified.";
        }
	}

	@Override
	public ProviderOtp getLatestOtp(String email) throws ClassNotFoundException, SQLException {
		 session = SessionHelper.getSessionFactory().openSession();

	        String hql = "FROM ProviderOtp o WHERE o.email = :email ORDER BY o.createdAt DESC";
	        Query query = session.createQuery(hql);
	        query.setParameter("email", email);
	        query.setMaxResults(1);

	        ProviderOtp latestOtp = (ProviderOtp) query.uniqueResult();
	        session.close();

	        return latestOtp;
	}
	
	@Override
	public String updateOtp(ProviderOtp otp) throws ClassNotFoundException, SQLException {
		Session session = SessionHelper.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		
		try {
			tx = session.beginTransaction();
			session.save(otp);
			tx.commit();
			return "OTP save successfully.";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "OTP update failed."; 
		}
	}

	@Override
	public String markOtpAsVerified(int otpId) throws ClassNotFoundException, SQLException {
		session = SessionHelper.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        String hql = "UPDATE ProviderOtp SET isVerified = true WHERE otpId = :otpId";
        int updated = session.createQuery(hql).setParameter("otpId", otpId).executeUpdate();

        tx.commit();
        session.close();

        if (updated > 0) {
            return "OTP verified.";
        } else {
            return "OTP not found or already verified.";
	}
	
	}

	@Override
	public String generateOtp(Provider provider, Reason reason) throws ClassNotFoundException, SQLException {

		    // Generate a 6-digit OTP
		    int code = new Random().nextInt(900000) + 100000;
		    String otpCode = String.valueOf(code);

		    // Hibernate session and transaction
		    Session session = SessionHelper.getSessionFactory().openSession();
		    Transaction tx = null;

		    try {
		        tx = session.beginTransaction();

		        // Timestamp
		        Timestamp now = new Timestamp(System.currentTimeMillis());
		        Timestamp expiry = new Timestamp(now.getTime() + 2 * 60 * 1000); // 2 minutes

			    // Create OTP entity
		        ProviderOtp otp = new ProviderOtp();
		        otp.setEmail(provider.getEmail());
		        System.out.println("my providerId is................"+provider.getProviderId());
		        System.out.println(provider.getEmail());
		        otp.setOtpCode(otpCode);
		        otp.setCreatedAt(now);
		        otp.setExpiresAt(expiry);
		        otp.setIsVerified(false);
		        otp.setReason(reason);
		        System.out.println("my otp is................");
		        System.out.println(otp);
		        session.save(otp);
		        session.flush();
		        System.out.println("OTP saved" +otp.getOtpCode());
		        tx.commit();
		        
		     // Send OTP Email
		        String subject = "Your OTP for HealthSure Registration";
		        String body = "Your OTP is: " + otp.getOtpCode() + ". It is valid for 2 minutes. Don't share this OTP with anyone.";
		        MailSend.sendInfo(provider.getEmail(), subject, body);
			    
		    } catch (Exception e) {
		        if (tx != null) tx.rollback();
		        e.printStackTrace();
		    } finally {
		        session.close();
		    }
		    return otpCode;
		}
	
	public boolean otpValid(String email, String otp) throws Exception {
        Session session = SessionHelper.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = session.createQuery("FROM ProviderOtp o WHERE o.email = :email");
            query.setParameter("email", email);
            ProviderOtp po = (ProviderOtp) query.uniqueResult();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            if (po == null) {
                tx.commit();
                return false;
            }
            if (now.after(po.getExpiresAt()) || !po.getOtpCode().equals(otp)) {
                session.update(po);
                tx.commit();
                return false;
            }
            session.delete(po);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
	
	public String resendOtp(String email) {
		SessionFactory sf;
		Session session;
		
	    if (email == null || email.trim().isEmpty()) {
	      return "Please enter your email.";
	    }

	    String otpCode = String.valueOf(new Random().nextInt(900_000) + 100_000);
	    Timestamp now    = new Timestamp(System.currentTimeMillis());
	    Timestamp expiry = new Timestamp(now.getTime() + 2 * 60 * 1000);

	    Transaction tx = null;
	    sf = SessionHelper.getSessionFactory();
	    session = sf.openSession();
	    tx = session.beginTransaction();
	    try {

	      ProviderOtp otp = (ProviderOtp) session
	        .createQuery("FROM ProviderOtp WHERE email = :e ORDER BY createdAt DESC")
	        .setParameter("e", email)
	        .setMaxResults(1)
	        .uniqueResult();

	      if (otp == null) {
	        otp = new ProviderOtp();
	        otp.setEmail(email);
	      }

	      otp.setOtpCode(otpCode);
	      otp.setCreatedAt(now);
	      otp.setExpiresAt(expiry);
	      otp.setIsVerified(false);

	      session.saveOrUpdate(otp);
	      tx.commit();
	    } catch (Exception ex) {
	      if (tx != null && tx.isActive()) tx.rollback();
	      ex.printStackTrace();
	      return "Database error saving OTP.";
	    }

	    try {
	      MailSend.sendInfo(
	        email,
	        "Your New OTP for HealthSure Registration",
	        "Your new OTP is: " + otpCode + ". Valid for 2 minutes. Don't share this OTP with anyone"
	      );
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      return "Failed to send email.";
	    }

	    return "New OTP sent to " + email;
	  }

	
	public ProviderOtp getValidOtp(String email, Reason reason, String otpCode) {
	    Session session = SessionHelper.getSessionFactory().openSession();
	    Query query = session.createQuery(
	        "FROM ProviderOtp WHERE email = :email AND reason = :reason AND otpCode = :otpCode AND isVerified = false AND expiresAt > :now");
	    query.setParameter("email", email);
	    query.setParameter("reason", reason); // enum directly
	    query.setParameter("otpCode", otpCode);
	    query.setParameter("now", LocalDateTime.now());

	    ProviderOtp otp = (ProviderOtp) query.uniqueResult();
	    session.close();
	    return otp;

}
}
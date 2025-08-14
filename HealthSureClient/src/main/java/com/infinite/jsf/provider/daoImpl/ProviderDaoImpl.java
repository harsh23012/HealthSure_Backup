package com.infinite.jsf.provider.daoImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.infinite.jsf.provider.dao.ProviderDao;
import com.infinite.jsf.provider.model.PasswordHistory;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.provider.model.ProviderDashboardDto;
import com.infinite.jsf.util.EncryptPassword;
import com.infinite.jsf.util.SessionHelper;

public class ProviderDaoImpl implements ProviderDao {

	SessionFactory sf;
	Session session;

	@Override
	public void addProvider(Provider provider) throws Exception {
		sf = SessionHelper.getSessionFactory();
		session = sf.openSession();
		Transaction trans = null;

		try {
			trans = session.beginTransaction();

			// Generate and set unique provider ID
			String newId = generateProviderId();
			provider.setProviderId(newId);

			session.save(provider);
			trans.commit();
			System.out.println("Provider saved with ID: " + newId);
		} catch (Exception e) {
			if (trans != null) {
				trans.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	public String generateProviderId() {
		Session session = null;
		try {
			sf = SessionHelper.getSessionFactory();
			session = sf.openSession();

			Query query = session.getNamedQuery("ProviderId");
			String latestId = (String) query.uniqueResult();

			if (latestId == null) {
				return "PROV001";
			} else {
				int num = Integer.parseInt(latestId.substring(4));
				return "PROV" + String.format("%03d", num + 1);
			}
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public Provider login(String email, String encryptedPassword) throws Exception {
		SessionFactory sf = SessionHelper.getSessionFactory();
		Session session = sf.openSession();
		Provider provider = null;

		try {
			Query query = session.createQuery("FROM Provider WHERE email = :email AND password = :password");
			query.setParameter("email", email);
			query.setParameter("password", encryptedPassword);

			provider = (Provider) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}

		return provider;
	}
	
	public ProviderDashboardDto getDashboardMetrics(String providerId) {
	    Session session = null;
	    ProviderDashboardDto dashboard = new ProviderDashboardDto();

	    try {
	        session = SessionHelper.getSessionFactory().openSession();

	        // 🗓️ Total Appointments booked this week
	        Query q1 = session.createQuery(
	            "SELECT COUNT(a) FROM Appointment a WHERE a.provider.providerId = :pid AND WEEK(a.bookedAt) = WEEK(CURRENT_DATE)");
	        q1.setParameter("pid", providerId);
	        dashboard.setTotalAppointments(((Long) q1.uniqueResult()).intValue());

	        // 🧑‍⚕️ Total Patients (distinct recipients with appointments under this provider)
	        Query q2 = session.createQuery(
	            "SELECT COUNT(DISTINCT a.recipient.hId) FROM Appointment a WHERE a.provider.providerId = :pid");
	        q2.setParameter("pid", providerId);
	        dashboard.setTotalPatients(((Long) q2.uniqueResult()).intValue());

	        // 📄 Total Claims filed this month
	        Query q3 = session.createQuery(
	            "SELECT COUNT(c) FROM Claims c WHERE c.provider.providerId = :pid AND MONTH(c.claimDate) = MONTH(CURRENT_DATE)");
	        q3.setParameter("pid", providerId);
	        dashboard.setTotalClaims(((Long) q3.uniqueResult()).intValue());

	        // 💰 Total Payments received
	        Query q4 = session.createQuery(
	            "SELECT SUM(p.amount) FROM PaymentHistory p WHERE p.provider.providerId = :pid AND p.paymentStatus = 'completed'");
	        q4.setParameter("pid", providerId);
	        Double total = (Double) q4.uniqueResult();
	        dashboard.setTotalAmounts(total != null ? total : 0.0);
	        System.err.println("total appontments : " + dashboard.getTotalAppointments());
	        System.err.println("total patients : " + dashboard.getTotalPatients());
	        System.err.println("total claims : " + dashboard.getTotalClaims());
	        System.err.println("total amounts : " + dashboard.getTotalAmounts());

	    } catch (Exception e) {
	        System.err.println("❌ Error fetching dashboard metrics: " + e.getMessage());
	        e.printStackTrace();
	    } finally {
	        if (session != null) session.close();
	    }

	    return dashboard;
	}

	@Override
	public boolean emailExists(String email) throws Exception {
		Session session = SessionHelper.getSessionFactory().openSession();

		try {
			Query query = session.createQuery("SELECT COUNT(p) FROM Provider p WHERE p.email = :email");
			query.setParameter("email", email);
			Long count = (Long) query.uniqueResult();
			return count > 0;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean phoneExists(String phone) throws Exception {
		Session session = SessionHelper.getSessionFactory().openSession();

		try {
			Query query = session.createQuery("SELECT COUNT(p) FROM Provider p WHERE p.telephone = :phone");
			query.setParameter("phone", phone);
			Long count = (Long) query.uniqueResult();
			return count > 0;
		} finally {
			session.close();
		}

	}

	@Override
	public boolean zipcodeExists(String zipcode) throws Exception {
		Session session = SessionHelper.getSessionFactory().openSession();

		try {
			Query query = session.createQuery("SELECT COUNT(p) FROM Provider p WHERE p.zipcode = :zipcode");
			query.setParameter("zipcode", zipcode);
			Long count = (Long) query.uniqueResult();
			return count > 0;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean updatePasswordByEmail(String email, String newPassword) {
		Session session = null;
		Transaction tx = null;

		try {
			session = new Configuration().configure().buildSessionFactory().openSession();
			tx = session.beginTransaction();

			String hql = "FROM Provider WHERE email = :email";
			Provider provider = (Provider) session.createQuery(hql).setParameter("email", email).uniqueResult();

			if (provider != null) {
				provider.setPassword(newPassword);
				session.update(provider);
				tx.commit();
				return true;
			}
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			if (session != null)
				session.close();
		}
		return false;
	}

	@Override
	public Provider getProviderId(String providerId) throws Exception {
		System.out.println("ProviderDao Step 1: Opening session for getProviderId()");
		SessionFactory sf = SessionHelper.getSessionFactory();
		if (sf == null) {
			throw new IllegalStateException("SessionFactory is null");
		}

		Session session = sf.openSession();
		try {
			System.out.println("ProviderDao Step 2: Retrieving Provider with ID=" + providerId);
			Provider provider = (Provider) session.get(Provider.class, providerId);
			if (provider == null || providerId.trim().isEmpty()) {
				System.out.println("ProviderDao Step 3: No Provider found for ID=" + providerId);
			} else {
				System.out.println("ProviderDao Step 3: Found Provider: " + provider);
			}
			return provider;
		} finally {
			session.close();
			System.out.println("ProviderDao Step 4: Session closed");
		}
	}

	@Override
	public List<Provider> getAllProvider() throws Exception {
		List<Provider> providers = new ArrayList<>();
		Session session = null;
		Transaction tx = null;

		try {
			session = SessionHelper.getSessionFactory().openSession();
			tx = session.beginTransaction();

			// HQL: load only registered providers
			String hql = "FROM Provider p WHERE p.registered = true ORDER BY p.name";
			Query query = session.createQuery(hql);
			providers = query.list();

			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			throw new Exception("Error fetching registered providers", e);
		} finally {
			if (session != null)
				session.close();
		}

		return providers;
	}

	@Override
	public Provider findByEmail(String email) {
		Session session = null;
		Provider provider = null;

		try {
			session = SessionHelper.getSessionFactory().openSession();
			String hql = "FROM Provider WHERE (email) = :email";
			Query query = session.createQuery(hql);
			query.setParameter("email", email);

			provider = (Provider) query.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}

		return provider;
	}

	private static final String PASS_REGEX = "^(?=.{8,12}$)(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).*$";

	@Override
	public String resetPassword(String providerId, String oldPassword, String newPassword) {
//		System.err.println("daoImpl called");
		FacesContext context = FacesContext.getCurrentInstance();

		// new Password should be in proper format

		if (newPassword == null || !newPassword.matches(PASS_REGEX)) {
//			System.err.println("new Password wrong formate");
			context.addMessage("changePwdForm:newPwd", new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Password must be 8–12 characters long, include letters, digits and a special character.", null));
			return null;
		}
//		System.err.println("oldPassword :" + oldPassword);
		
		session = SessionHelper.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();

//		Session session = sf.openSession();
//		Transaction tx = session.beginTransaction();
//		System.err.println("session :" + session);
		Provider p = (Provider) session.get(Provider.class, providerId);

		if (p == null) {
//			System.err.println("provider not found");
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Provider not found.", null));
			return null;
		}
//		System.err.println("provider id : " + p.getProviderId());
//		System.err.println("provider pass : " + p.getPassword());

		String oldEnc = EncryptPassword.getCode(oldPassword);

		if (!p.getPassword().equals(oldEnc)) {
			context.addMessage("changePwdForm:oldPwd",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "old Password is Incorrect", null));
			return null;
		}

		if (isAmongLastPasswords(session, p, newPassword)) {
			context.addMessage("changePwdForm:newPwd",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot reuse any of the last 3 passwords..", null));
			return null;
		}
//		System.err.println("provider found : " + p.getProviderId());
		String newEnc = EncryptPassword.getCode(newPassword);
//		System.err.println("new enc pass : " + newEnc);
		p.setPassword(newEnc);
		session.update(p);
		session.save(new PasswordHistory(p, newEnc, new Date()));

		tx.commit();
		session.close();

		return "success";
	}

	private boolean isAmongLastPasswords(Session session, Provider p, String newPassword) {
		String newEnc = EncryptPassword.getCode(newPassword);

		System.out.println("isAmongLastPasswords is called: new Password is " + newPassword);

		Query q = session.createQuery("FROM PasswordHistory WHERE provider = :p ORDER BY createdAt DESC");
		q.setParameter("p", p);
		q.setMaxResults(3);

		@SuppressWarnings("unchecked")
		List<PasswordHistory> last = q.list();

		if (last != null) {
			last.forEach(System.out::println);
		}

		boolean isMatch = last.stream().anyMatch(ph -> ph.getPasswordHash().equals(newEnc));

		System.out.println("Match with last 3 passwords: " + isMatch);

		return isMatch;
	}

}

package com.infinite.jsf.provider.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.infinite.jsf.provider.dao.ProviderDao;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.util.SessionHelper;

public class ProviderDaoImpl implements ProviderDao{

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
	        Query query = session.createQuery(
	            "FROM Provider WHERE email = :email AND password = :password");
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

	@Override
	public boolean emailExists(String email) throws Exception {
		Session session = SessionHelper.getSessionFactory().openSession();
		
		try {
			Query query = session.createQuery("SELECT COUNT(p) FROM Provider p WHERE p.email = :email");
			query.setParameter("email", email);
			Long count = (Long)query.uniqueResult();
			return count > 0;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean phoneExists(String phone) throws Exception {
		 Session session = SessionHelper.getSessionFactory().openSession();
	     
	        try {
	            Query query = session.createQuery(
	                "SELECT COUNT(p) FROM Provider p WHERE p.telephone = :phone");
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
            Query query = session.createQuery(
                "SELECT COUNT(p) FROM Provider p WHERE p.zipcode = :zipcode");
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
	            Provider provider = (Provider) session.createQuery(hql)
	                                                  .setParameter("email", email)
	                                                  .uniqueResult();

	            if (provider != null) {
	                provider.setPassword(newPassword);
	                session.update(provider);
	                tx.commit();
	                return true;
	            }
	        } catch (Exception e) {
	            if (tx != null) tx.rollback();
	            e.printStackTrace();
	        } finally {
	            if (session != null) session.close();
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
            if (tx != null) tx.rollback();
            throw new Exception("Error fetching registered providers", e);
        } finally {
            if (session != null) session.close();
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


}



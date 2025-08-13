package com.infinite.jsf.provider.daoImpl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.infinite.jsf.provider.dao.DoctorsDao;
import com.infinite.jsf.util.SessionHelper;
import com.infinite.jsf.provider.model.Doctor;

public class DoctorsDaoImpl implements DoctorsDao{
	SessionFactory sf;
	Session session;

	@Override
	 public void addDoctors(Doctor doctors) throws Exception {
        System.out.println("DAO Step 1: Entering addDoctors()");

        // 2) Obtain SessionFactory
        sf = SessionHelper.getSessionFactory();
        if (sf == null) {
            throw new IllegalStateException("SessionFactory is null");
        }
        System.out.println("DAO Step 2: SessionFactory obtained");

        // 3) Open session & begin transaction
        session = sf.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            System.out.println("DAO Step 3: Transaction started");

            // 4) Persist the entity
            System.out.println("DAO Step 4: Saving Doctors entity: " + doctors);
            session.save(doctors);

            // 5) Commit
            tx.commit();
            System.out.println("DAO Step 5: Transaction committed");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("DAO Step 6: Exception during save: " + e.getMessage());
            throw e;  // propagate so controller can handle it
        } finally {
            // 7) Close session
            session.close();
            System.out.println("DAO Step 7: Session closed");
        }

        System.out.println("DAO Step 8: Exiting addDoctors()");
    }


//	@Override
//	public void saveDoctor(Doctors doctor) {
//	    System.out.println("Starting saveDoctor method...");
//
//	    sf = SessionHelper.getSessionFactory();
//	    session = sf.openSession();
//	    System.out.println("Hibernate session opened.");
//
//	    try {
//	        Transaction tx = session.beginTransaction();
//	        System.out.println("Transaction started.");
//
//	        session.save(doctor);
//	        System.out.println("Doctor object saved to database.");
//
//	        tx.commit();
//	        System.out.println("Transaction committed successfully.");
//	    } catch (Exception e) {
//	        System.out.println("Exception occurred during saveDoctor: " + e.getMessage());
//	        e.printStackTrace();
//	    } finally {
//	        session.close();
//	        System.out.println("Hibernate session closed.");
//	    }
//
//	    System.out.println("saveDoctor method completed.");
//	}
    
	@Override
    public String generateDoctorId() {
    	Session session = null;
        try {
        	sf = SessionHelper.getSessionFactory();
            session = sf.openSession();
            Query query = session.getNamedQuery("DoctorId");
            String latestId = (String) query.uniqueResult();
 
            if (latestId == null) {
                return "DOC001";
            } else {
                int num = Integer.parseInt(latestId.substring(5));
                return "DOC" + String.format("%03d", num + 1);
            }
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
	

    @Override
    public Doctor searchByDoctorsId(int doctorId) throws Exception {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        try {
            Query query = session.getNamedQuery("Doctor.findById");
            query.setParameter("doctorId", doctorId);
            return (Doctor) query.uniqueResult();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Doctor> getAllDoctors() throws Exception {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        List<Doctor>doctorsList = null;
        try {
            Query query = session.createQuery("FROM Doctors");
            doctorsList = query.list();
        }catch(Exception e) {
        	e.printStackTrace();
        	throw new Exception("Doctors List not found",e);
        } finally {
            session.close();
        }
		return doctorsList;
    }

    @Override
    public List<Doctor> searchByProviderId(String providerId) throws Exception {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        try {
            Query query = session.getNamedQuery("Doctor.findByProviderId");
            query.setParameter("providerId", providerId);
            return query.list();
        } finally {
            session.close();
        }
    }

    @Override
    public void updateDoctors(Doctor doctor) throws Exception {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        try {
            Transaction tx = session.beginTransaction();
            session.update(doctor);
            tx.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public void deleteDoctors(int doctorId) throws Exception {
        sf = SessionHelper.getSessionFactory();
        session = sf.openSession();
        try {
            Doctor doctor = (Doctor) session.get(Doctor.class, doctorId);
            Transaction tx = session.beginTransaction();
            session.delete(doctor);
            tx.commit();
        } finally {
            session.close();
        }
    }

   

}

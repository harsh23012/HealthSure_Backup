package com.java.ejb.provider.bean;

import java.sql.SQLException;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.java.ejb.provider.dao.ProviderDao;
import com.java.ejb.provider.daoImpl.ProviderDaoImpl;
import com.java.ejb.provider.model.ClaimHistory;
import com.java.ejb.provider.model.Claims;
import com.java.ejb.provider.model.MedicalProcedure;
import com.java.ejb.provider.model.PrescribedMedicines;
import com.java.ejb.provider.model.Prescription;
import com.java.ejb.provider.model.ProcedureTest;

/**
 * Session Bean implementation class ProviderBean
 */
@Stateless
@Remote(ProviderBeanRemote.class)
public class ProviderBean implements ProviderBeanRemote {

    /**
     * Default constructor. 
     */
	static ProviderDao providerDao;
	static
	{
		providerDao=new ProviderDaoImpl();
	}
    public ProviderBean() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public String addMedicalProcedure(MedicalProcedure medicalProcedure) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return providerDao.addMedicalProcedure(medicalProcedure);
	}

	@Override
	public String addPrescription(Prescription prescription) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return providerDao.addPrescription(prescription);
	}

	@Override
	public String addTest(ProcedureTest procedureTest) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return providerDao.addTest(procedureTest);
	}

	@Override
	public String addPrescribedMedicines(PrescribedMedicines prescribedMedicines) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		return providerDao.addPrescribedMedicines(prescribedMedicines);
	}

}

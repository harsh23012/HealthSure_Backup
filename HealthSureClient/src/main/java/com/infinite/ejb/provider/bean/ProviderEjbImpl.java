package com.infinite.ejb.provider.bean;

import java.sql.SQLException;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.naming.NamingException;

import com.infinite.ejb.provider.model.Doctor;
import com.infinite.ejb.provider.model.MedicalProcedure;
import com.infinite.ejb.provider.model.PrescribedMedicines;
import com.infinite.ejb.provider.model.Prescription;
import com.infinite.ejb.provider.model.ProcedureTest;
import com.infinite.ejb.provider.model.Provider;
import com.infinite.ejb.recipient.model.Recipient;

public class ProviderEjbImpl {
	static ProviderBeanRemote remote;
	static {
		try {
			remote = ProviderRemoteHelper.lookupRemoteStatelessProvider();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String addMedicalProcedure(MedicalProcedure medicalProcedure) throws ClassNotFoundException, SQLException {
		// Store key fields in session map
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("procedureId", medicalProcedure.getProcedureId());
		sessionMap.put("providerId", medicalProcedure.getProvider().getProviderId());
		sessionMap.put("doctorId", medicalProcedure.getDoctor().getDoctorId());
		sessionMap.put("recipientHid", medicalProcedure.getRecipient().gethId());
		remote.addMedicalProcedure(medicalProcedure);
		return "ProcedureDashboard?faces-redirect=true";
	}

	public String addPrescription(Prescription prescription) throws ClassNotFoundException, SQLException {
	    // Save via remote EJB
		System.out.println("calling remote prescription");
	    remote.addPrescription(prescription);
	    System.out.println("prescription added");
	    return "PrescriptionDashboard?faces-redirect=true";
	}


	public String addTest(ProcedureTest test) throws ClassNotFoundException, SQLException {
		remote.addTest(test);
		return "PrescriptionDashboard?faces-redirect=true";
	}

	public String addPrescribedMedicines(PrescribedMedicines prescribedMedicine)
			throws ClassNotFoundException, SQLException {
		remote.addPrescribedMedicines(prescribedMedicine);
		return "PrescriptionDashboard?faces-redirect=true";
	}
}

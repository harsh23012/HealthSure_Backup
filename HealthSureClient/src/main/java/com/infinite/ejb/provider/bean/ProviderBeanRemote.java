package com.infinite.ejb.provider.bean;

import java.sql.SQLException;

import javax.ejb.Remote;

import com.infinite.ejb.provider.model.ClaimHistory;
import com.infinite.ejb.provider.model.Claims;
import com.infinite.ejb.provider.model.MedicalProcedure;
import com.infinite.ejb.provider.model.PrescribedMedicines;
import com.infinite.ejb.provider.model.Prescription;
import com.infinite.ejb.provider.model.ProcedureTest;

@Remote
public interface ProviderBeanRemote {
	public String addMedicalProcedure(MedicalProcedure medicalProcedure) throws ClassNotFoundException, SQLException;
	public String addPrescription(Prescription prescription) throws ClassNotFoundException, SQLException;
	public String addPrescribedMedicines(PrescribedMedicines prescribedMedicines) throws ClassNotFoundException, SQLException;
	public String addTest(ProcedureTest procedureTest) throws ClassNotFoundException, SQLException;
}

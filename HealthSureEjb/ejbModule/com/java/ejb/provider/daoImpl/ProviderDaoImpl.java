package com.java.ejb.provider.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.java.ejb.provider.dao.ProviderDao;
import com.java.ejb.provider.model.MedicalProcedure;
import com.java.ejb.provider.model.PrescribedMedicines;
import com.java.ejb.provider.model.Prescription;
import com.java.ejb.provider.model.ProcedureTest;
import com.java.ejb.util.ConnectionHelper;

public class ProviderDaoImpl implements ProviderDao{

	@Override
	public String addMedicalProcedure(MedicalProcedure medicalProcedure) throws ClassNotFoundException, SQLException {
	    Connection con = ConnectionHelper.getConnection();
	    String sql = "INSERT INTO medical_procedure (procedure_id, appointment_id, h_id, provider_id, doctor_id, procedure_date, diagnosis, recommendations, from_date, to_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


	    PreparedStatement pst = con.prepareStatement(sql);

	    pst.setString(1, medicalProcedure.getProcedureId());
	    pst.setString(2, medicalProcedure.getAppointment().getAppointmentId());
	    pst.setString(3, medicalProcedure.getRecipient().gethId());
	    pst.setString(4, medicalProcedure.getProvider().getProviderId());
	    pst.setString(5, medicalProcedure.getDoctor().getDoctorId());

	    java.sql.Date procDate = new java.sql.Date(medicalProcedure.getProcedureDate().getTime());
	    pst.setDate(6, procDate);

	    pst.setString(7, medicalProcedure.getDiagnosis());
	    pst.setString(8, medicalProcedure.getRecommendations());

	    if (medicalProcedure.getFromDate() != null) {
	        pst.setTimestamp(9, new java.sql.Timestamp(medicalProcedure.getFromDate().getTime()));
	    } else {
	        pst.setTimestamp(9, null);
	    }

	    if (medicalProcedure.getToDate() != null) {
	        pst.setTimestamp(10, new java.sql.Timestamp(medicalProcedure.getToDate().getTime()));
	    } else {
	        pst.setTimestamp(10, null);
	    }
	    System.out.println(medicalProcedure);
	    pst.executeUpdate();
	    pst.close();
	    con.close();

	    return "inserted";
	}

	@Override
	public String addPrescription(Prescription prescription) throws SQLException, ClassNotFoundException {
		System.out.println("remote prescription called");
	    Connection con = ConnectionHelper.getConnection();
	    String sql = "INSERT INTO prescription (" +
	                 "prescription_id, procedure_id, h_id, provider_id, doctor_id, " +
	                 "written_on, start_date, end_date, created_at) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    PreparedStatement pst = con.prepareStatement(sql);

	    pst.setString(1, prescription.getPrescriptionId());
	    pst.setString(2, prescription.getProcedure().getProcedureId());
	    pst.setString(3, prescription.getRecipient().gethId());
	    pst.setString(4, prescription.getProvider().getProviderId());
	    pst.setString(5, prescription.getDoctor().getDoctorId());

	    pst.setTimestamp(6, prescription.getWrittenOn() != null
	            ? new java.sql.Timestamp(prescription.getWrittenOn().getTime())
	            : new java.sql.Timestamp(System.currentTimeMillis()));

	    pst.setTimestamp(7, prescription.getStartDate() != null
	            ? new java.sql.Timestamp(prescription.getStartDate().getTime())
	            : null);

	    pst.setTimestamp(8, prescription.getEndDate() != null
	            ? new java.sql.Timestamp(prescription.getEndDate().getTime())
	            : null);

	    pst.setTimestamp(9, prescription.getCreatedAt() != null
	            ? new java.sql.Timestamp(prescription.getCreatedAt().getTime())
	            : new java.sql.Timestamp(System.currentTimeMillis()));

	    pst.executeUpdate();
	    pst.close();
	    con.close();
	    System.out.println("added and returning from remote prescription");
	    return "inserted";
	}

	@Override
	public String addPrescribedMedicines(PrescribedMedicines prescribedMedicines) throws ClassNotFoundException, SQLException {
	    Connection con = ConnectionHelper.getConnection();

	    String sql = "INSERT INTO prescribed_medicines (" +
	                 "prescribed_id, prescription_id, medicine_name, dosage, duration, notes, " +
	                 "start_date, end_date, created_at) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    PreparedStatement pst = con.prepareStatement(sql);

	    pst.setString(1, prescribedMedicines.getPrescribedId());
	    pst.setString(2, prescribedMedicines.getPrescription().getPrescriptionId());
	    pst.setString(3, prescribedMedicines.getMedicineName());
	    pst.setString(4, prescribedMedicines.getDosage());
	    pst.setString(5, prescribedMedicines.getDuration());
	    pst.setString(6, prescribedMedicines.getNotes());

	    pst.setTimestamp(7, prescribedMedicines.getStartDate() != null
	            ? new java.sql.Timestamp(prescribedMedicines.getStartDate().getTime())
	            : null);

	    pst.setTimestamp(8, prescribedMedicines.getEndDate() != null
	            ? new java.sql.Timestamp(prescribedMedicines.getEndDate().getTime())
	            : null);

	    pst.setTimestamp(9, prescribedMedicines.getCreatedAt() != null
	            ? new java.sql.Timestamp(prescribedMedicines.getCreatedAt().getTime())
	            : new java.sql.Timestamp(System.currentTimeMillis()));

	    pst.executeUpdate();
	    pst.close();
	    con.close();

	    return "inserted";
	}

	



	@Override
	public String addTest(ProcedureTest procedureTest) throws ClassNotFoundException, SQLException {
	    Connection con = ConnectionHelper.getConnection();

	    String sql = "INSERT INTO prescribed_tests (" +
	                 "test_id, prescription_id, test_name, test_date, result_summary, created_at) " +
	                 "VALUES (?, ?, ?, ?, ?, ?)";

	    PreparedStatement pst = con.prepareStatement(sql);

	    pst.setString(1, procedureTest.getTestId());
	    pst.setString(2, procedureTest.getPrescription().getPrescriptionId());
	    pst.setString(3, procedureTest.getTestName());

	    // Convert testDate (java.util.Date) to java.sql.Date
	    if (procedureTest.getTestDate() != null) {
	        pst.setDate(4, new java.sql.Date(procedureTest.getTestDate().getTime()));
	    } else {
	        pst.setDate(4, new java.sql.Date(System.currentTimeMillis())); // fallback
	    }

	    pst.setString(5, procedureTest.getResultSummary());

	    if (procedureTest.getCreatedAt() != null) {
	        pst.setTimestamp(6, new java.sql.Timestamp(procedureTest.getCreatedAt().getTime()));
	    } else {
	        pst.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
	    }

	    pst.executeUpdate();
	    pst.close();
	    con.close();

	    return "inserted";
	}

	



}

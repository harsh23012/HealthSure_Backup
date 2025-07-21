package com.infinite.jsf.provider.controller;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.infinite.ejb.provider.bean.ProviderEjbImpl;
import com.infinite.ejb.provider.model.Doctor;
import com.infinite.ejb.provider.model.MedicalProcedure;
import com.infinite.ejb.provider.model.PrescribedMedicines;
import com.infinite.ejb.provider.model.Prescription;
import com.infinite.ejb.provider.model.ProcedureTest;
import com.infinite.ejb.provider.model.Provider;
import com.infinite.ejb.recipient.model.Recipient;
import com.infinite.jsf.insurance.model.SubscribedMember;
import com.infinite.jsf.provider.daoImpl.InsuranceDaoImpl;
import com.infinite.jsf.provider.daoImpl.ProviderDaoImpl;
import com.infinite.ejb.provider.model.Appointment;
import com.infinite.jsf.recipient.model.PatientInsuranceDetails;

public class ProviderController {
ProviderEjbImpl providerEjb;
ProviderDaoImpl providerDao;
String doctorId;
String healthId;
String patientName;
String matchType="startsWith";
private String sortField;
private String selectedPatientId;
private boolean showInsuranceTable;
private boolean showPatientsFlag = false;
private boolean showInsuranceFlag = false;
private List<SubscribedMember> subscribedMembers;
boolean cameFromPatientSearch;
public boolean isCameFromPatientSearch() {
	return cameFromPatientSearch;
}

public void setCameFromPatientSearch(boolean cameFromPatientSearch) {
	this.cameFromPatientSearch = cameFromPatientSearch;
}

public void setSubscribedMembers(List<SubscribedMember> subscribedMembers) {
	this.subscribedMembers = subscribedMembers;
}

public List<SubscribedMember> getSubscribedMembers() {
   
        subscribedMembers = loadSubscribedMembers().getSubscribedMembers();
    
    return subscribedMembers;
}

public boolean isShowPatientsFlag() {
	return showPatientsFlag;
}




public void setShowPatientsFlag(boolean showPatientsFlag) {
	this.showPatientsFlag = showPatientsFlag;
}




public boolean isShowInsuranceFlag() {
	return showInsuranceFlag;
}




public void setShowInsuranceFlag(boolean showInsuranceFlag) {
	this.showInsuranceFlag = showInsuranceFlag;
}




public String getSelectedPatientId() {
	return selectedPatientId;
}




public void setSelectedPatientId(String selectedPatientId) {
	this.selectedPatientId = selectedPatientId;
}




public boolean isShowInsuranceTable() {
	return showInsuranceTable;
}




public void setShowInsuranceTable(boolean showInsuranceTable) {
	this.showInsuranceTable = showInsuranceTable;
}
private boolean ascending = true;

public void sortBy(String listType, String field) {
    if (field.equals(sortField)) {
        ascending = !ascending; // Toggle direction
    } else {
        sortField = field;
        ascending = true;
    }

    switch (listType) {
        case "insurance":
            sortInsuranceList();
            break;
        case "patients":
            patientInsuranceList = null;
            selectedPatientId = null;
            showInsuranceTable = false;
            sortAssociatedPatients();
            break;
        case "members":
            sortSubscribedMembers();
            break;
    }
}

public String getSortField() {
	return sortField;
}



public void setSortField(String sortField) {
	this.sortField = sortField;
}



public boolean isAscending() {
	return ascending;
}



public void setAscending(boolean ascending) {
	this.ascending = ascending;
}


public String backToPatients() {
    // Clear insurance-related data
    patientInsuranceList = null;
    showInsuranceFlag = false;

    // Keep patients list visible
    showPatientsFlag = true;

    // Optionally, clear messages
    topMessage = null;

    // Stay on the same page
    return null;
}
private void sortSubscribedMembers() {
    if (subscribedMembers == null || sortField == null) return;

    Collections.sort(subscribedMembers, (m1, m2) -> {
        try {
            Field f = m1.getClass().getDeclaredField(sortField);
            f.setAccessible(true);
            Comparable v1 = (Comparable) f.get(m1);
            Comparable v2 = (Comparable) f.get(m2);
            return ascending ? v1.compareTo(v2) : v2.compareTo(v1);
        } catch (Exception e) {
            return 0;
        }
    });
}

private void sortAssociatedPatients() {
    if (associatedPatients == null || sortField == null) return;

    Collections.sort(associatedPatients, (p1, p2) -> {
        try {
            Field f = p1.getClass().getDeclaredField(sortField);
            f.setAccessible(true);
            Comparable v1 = (Comparable) f.get(p1);
            Comparable v2 = (Comparable) f.get(p2);
            return ascending ? v1.compareTo(v2) : v2.compareTo(v1);
        } catch (Exception e) {
            return 0;
        }
    });
}

private void sortInsuranceList() {
    if (patientInsuranceList == null || sortField == null) return;

    Collections.sort(patientInsuranceList, (i1, i2) -> {
        try {
            Field f = i1.getClass().getDeclaredField(sortField);
            f.setAccessible(true);
            Object v1 = f.get(i1);
            Object v2 = f.get(i2);

            if (v1 == null || v2 == null) return 0;

            if (v1 instanceof Date && v2 instanceof Date) {
                return ascending ? ((Date) v1).compareTo((Date) v2) : ((Date) v2).compareTo((Date) v1);
            } else if (v1 instanceof Comparable && v2 instanceof Comparable) {
                return ascending ? ((Comparable) v1).compareTo(v2) : ((Comparable) v2).compareTo(v1);
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    });
}



public String getMatchType() {
	return matchType;
}

public void setMatchType(String matchType) {
	this.matchType = matchType;
}

public String getPatientName() {
	return patientName;
}

public void setPatientName(String patientName) {
	this.patientName = patientName;
}
List<Recipient> associatedPatients;
MedicalProcedure medicalProcedure;
List<PatientInsuranceDetails> patientInsuranceList;
PatientInsuranceDetails selectedItem;
private String topMessage;

public String getTopMessage() {
    return topMessage;
}

public void setTopMessage(String topMessage) {
    this.topMessage = topMessage;
}

public PatientInsuranceDetails getSelectedItem() {
	return selectedItem;
}
public void setSelectedItem(PatientInsuranceDetails selectedItem) {
	this.selectedItem = selectedItem;
}
public List<PatientInsuranceDetails> getPatientInsuranceList() {
	return patientInsuranceList;
}
public void setPatientInsuranceList(List<PatientInsuranceDetails> patientInsuranceList) {
	this.patientInsuranceList = patientInsuranceList;
}
public InsuranceDaoImpl getInsuranceDaoImpl() {
	return insuranceDaoImpl;
}
public void setInsuranceDaoImpl(InsuranceDaoImpl insuranceDaoImpl) {
	this.insuranceDaoImpl = insuranceDaoImpl;
}
InsuranceDaoImpl insuranceDaoImpl;
public MedicalProcedure getMedicalProcedure() {
	return medicalProcedure;
}
public void setMedicalProcedure(MedicalProcedure medicalProcedure) {
	this.medicalProcedure = medicalProcedure;
}
public ProviderEjbImpl getProviderEjb() {
	return providerEjb;
}
public void setProviderEjb(ProviderEjbImpl providerEjb) {
	this.providerEjb = providerEjb;
}
public ProviderController() {
	super();
	// TODO Auto-generated constructor stub
}
public ProviderDaoImpl getProviderDao() {
	return providerDao;
}
public void setProviderDao(ProviderDaoImpl providerDao) {
	this.providerDao = providerDao;
}
public String addMedicalProcedureController(MedicalProcedure medicalProcedure) throws ClassNotFoundException, SQLException {
	providerDao=new ProviderDaoImpl();
    FacesContext context = FacesContext.getCurrentInstance();
    boolean isValid = true;
    System.out.println(providerDao);
    System.out.println(medicalProcedure.getRecipient().gethId());
    // --- Validate Recipient Existence ---
    Recipient recipient = providerDao.searchRecipientByHealthId(medicalProcedure.getRecipient().gethId());
    if (recipient == null) {
        context.addMessage("recipientId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Invalid Patient", "Recipient with given Health ID not found."));
        context.validationFailed();
        isValid = false;
    }

    // --- Validate Provider Existence ---
    Provider provider = providerDao.searchProviderById(medicalProcedure.getProvider().getProviderId());
    if (provider == null) {
        context.addMessage("providerId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Invalid Provider", "Provider with given ID not found."));
        context.validationFailed();
        isValid = false;
    }

    // --- Validate Doctor Existence ---
    Doctor doctor = providerDao.searchDoctorById(medicalProcedure.getDoctor().getDoctorId());
    if (doctor==null) {
        context.addMessage("doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Invalid Doctor", "Doctor with given ID not found."));
        context.validationFailed();
        isValid = false;
    }

    // --- Validate Appointment Existence and Association ---
    Appointment appointment = providerDao.searchAppointmentById(medicalProcedure.getAppointment().getAppointmentId());
    if (appointment == null) {
        context.addMessage("appointmentId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Invalid Appointment", "Appointment with given ID not found."));
        context.validationFailed();
        isValid = false;
    } else {
        // Validate if appointment is linked with given doctor/provider/recipient
        if (!appointment.getProvider().getProviderId().equals(medicalProcedure.getProvider().getProviderId())) {
            context.addMessage("providerId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Mismatch", "This appointment does not belong to the selected provider."));
            context.validationFailed();
            isValid = false;
        }

        if (!appointment.getDoctor().getDoctorId().equals(medicalProcedure.getDoctor().getDoctorId())) {
            context.addMessage("doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Mismatch", "This appointment does not involve the selected doctor."));
            context.validationFailed();
            isValid = false;
        }

        if (!appointment.getRecipient().gethId().equals(medicalProcedure.getRecipient().gethId())) {
            context.addMessage("recipientId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Mismatch", "This appointment is not for the selected patient."));
            context.validationFailed();
            isValid = false;
        }
    }
    // --- Date Validations ---
    Date fromDate = medicalProcedure.getFromDate();
    Date toDate = medicalProcedure.getToDate();
    Date procedureDate=medicalProcedure.getProcedureDate();
    Date today = new Date();
    if (fromDate != null && toDate != null) {
        if (fromDate.after(today)) {
            context.addMessage("fromDate", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Date", "From Date cannot be in the future."));
            context.validationFailed();
            isValid = false;
        }
        if(fromDate.after(procedureDate))
        {
        	 context.addMessage("fromDate", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                     "Invalid Date", "from Date cannot be after procedure date."));
             context.validationFailed();
             isValid = false;
        }
        if (toDate.before(fromDate)) {
            context.addMessage("toDate", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Date", "To Date cannot be before from date."));
            context.validationFailed();
            isValid = false;
        }
        if (toDate.after(today)) {
            context.addMessage("toDate", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Invalid Date", "To Date cannot be in the future."));
            context.validationFailed();
            isValid = false;
        }
    }
    // --- If any error found ---
    if (!isValid) {
        return null;
    }
    // --- Save via EJB ---
    
    return providerEjb.addMedicalProcedure(medicalProcedure);
}
public String addPresribedMedicinesController(PrescribedMedicines prescribedMedicines) throws ClassNotFoundException, SQLException
{
	
	return providerEjb.addPrescribedMedicines(prescribedMedicines);
}
public String addPrescriptionController(Prescription prescription) throws ClassNotFoundException, SQLException {
    providerDao = new ProviderDaoImpl();
    FacesContext context = FacesContext.getCurrentInstance();
    Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

    // Retrieve IDs from session and set reference objects
    MedicalProcedure procedure = new MedicalProcedure();
    procedure.setProcedureId((String) sessionMap.get("procedureId"));
    prescription.setProcedure(procedure);

    Provider provider = new Provider();
    provider.setProviderId((String) sessionMap.get("providerId"));
    prescription.setProvider(provider);

    Doctor doctor = new Doctor();
    doctor.setDoctorId((String) sessionMap.get("doctorId"));
    prescription.setDoctor(doctor);

    Recipient recipient = new Recipient();
    recipient.sethId((String) sessionMap.get("recipientHid"));
    prescription.setRecipient(recipient);

    // Validate writtenOn field
    if (prescription.getWrittenOn() == null) {
        context.addMessage("writtenOn", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Please enter the Written On date.", null));
        return null;
    }

    // Fetch Procedure Date
    Date startDate = providerDao.getProcedureStartDate(prescription.getProcedure().getProcedureId());

    if (startDate == null) {
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Missing Procedure Date for Procedure ID: " + prescription.getProcedure().getProcedureId(), null));
        return null;
    }

    // Validate: writtenOn must not be before procedureDate
    if (prescription.getWrittenOn().before(startDate)) {
        context.addMessage("writtenOn", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Written On date (" + prescription.getWrittenOn() +
                ") cannot be before the Procedure start Date (" + startDate + ").", null));
        return null;
    }
    if (prescription.getStartDate().before(prescription.getWrittenOn())) {
        context.addMessage("startDate", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "prescription start date(" + prescription.getStartDate() +
                ") cannot be before the prescription written Date (" + prescription.getWrittenOn() + ").", null));
        return null;
    }
    if (prescription.getEndDate().before(prescription.getStartDate())) {
        context.addMessage("endDate", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "prescription end date(" + prescription.getEndDate() +
                ") cannot be before the prescription start Date (" + prescription.getStartDate() + ").", null));
        return null;
    }
    System.out.println("all validations passed");
    return providerEjb.addPrescription(prescription);  // Proceed if all validations pass
}


public String addTestController(ProcedureTest procedureTest) throws ClassNotFoundException, SQLException
{
	return providerEjb.addTest(procedureTest);
}
public String procedureSubmit()
{
	return "ProviderDashboard?faces-redirect=true";
}
public String prescriptionDetailsSubmit()
{
	return "ProcedureDashboard?faces-redirect=true";
}
public String handleSearch() {
    cameFromPatientSearch = true;
    insuranceDaoImpl = new InsuranceDaoImpl();
    providerDao = new ProviderDaoImpl();
    FacesContext context = FacesContext.getCurrentInstance();

    topMessage = null;
    patientInsuranceList = null;
    associatedPatients = null;
    showPatientsFlag = false;
    showInsuranceFlag = false;

    if (doctorId == null || doctorId.trim().isEmpty()) {
        context.addMessage("doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Doctor ID is required.", null));
        return null;
    }

    Doctor doctor = providerDao.searchDoctorById(doctorId);
    if (doctor == null) {
        context.addMessage("doctorId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Doctor with ID " + doctorId + " does not exist.", null));
        return null;
    }

    if (healthId != null && !healthId.trim().isEmpty()) {
        cameFromPatientSearch = false;
        Recipient recipient = providerDao.searchRecipientByHealthId(healthId);
        if (recipient == null) {
            context.addMessage("recipientId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Patient with Health ID " + healthId + " does not exist.", null));
            return null;
        }

        if (patientName != null && !patientName.trim().isEmpty()) {
            String cleaned = patientName.replaceAll("\\s+", "");
            // Length validation
            if (cleaned.length() < 2) {
                context.addMessage("patientName", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Please enter at least 2 characters in the patient name.", null));
                return null;
            }
            // Character validation
            if (!patientName.matches("^[a-zA-Z0-9\\s]+$")) {
                context.addMessage("patientName", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Patient name can only contain letters, digits, and spaces. Special characters are not allowed.", null));
                return null;
            }

            String fullName = (recipient.getFirstName() + recipient.getLastName()).toLowerCase().replaceAll("\\s+", "");
            String reverseName = (recipient.getLastName() + recipient.getFirstName()).toLowerCase().replaceAll("\\s+", "");
            String inputName = cleaned.toLowerCase();

            boolean match = false;
            switch (matchType.toLowerCase()) {
                case "startswith":
                    match = fullName.startsWith(inputName);
                    break;
                case "endswith":
                    match = fullName.endsWith(inputName);
                    break;
                case "contains":
                    match = fullName.contains(inputName) || reverseName.contains(inputName);
                    break;
                case "exact":
                default:
                    match = fullName.equals(inputName);
                    break;
            }

            if (!match) {
                context.addMessage("recipientId", new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Patient with ID " + healthId + " does not have a name that " + matchType + " '" + patientName + "'.", null));
                return null;
            }
        }

        if (!providerDao.isDoctorPatientAssociatedByAppointment(doctorId, healthId)) {
            context.addMessage("recipientId", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Access denied: The doctor is not associated with this patient via an appointment.", null));
            return null;
        }

        patientInsuranceList = insuranceDaoImpl.showInsuranceOfRecipient(healthId);
        if (patientInsuranceList == null || patientInsuranceList.isEmpty()) {
            context.addMessage("recipientId", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "No insurance found for patient ID: " + healthId, null));
        } else {
            showInsuranceFlag = true;
        }

    } else if (patientName != null && !patientName.trim().isEmpty()) {
        String cleaned = patientName.replaceAll("\\s+", "");
        // Length validation
        if (cleaned.length() < 2) {
            context.addMessage("patientName", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please enter at least 2 characters in the patient name.", null));
            return null;
        }
        // Character validation
        if (!patientName.matches("^[a-zA-Z0-9\\s]+$")) {
            context.addMessage("patientName", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Patient name can only contain letters, digits, and spaces. Special characters are not allowed.", null));
            return null;
        }

        associatedPatients = providerDao.searchPatientsByName(doctorId, patientName, matchType);
        if (associatedPatients == null || associatedPatients.isEmpty()) {
            String readableMatch = matchType.equalsIgnoreCase("startswith") ? "start with"
                                  : matchType.equalsIgnoreCase("endswith") ? "end with"
                                  : matchType.equalsIgnoreCase("exact") ? "exactly match"
                                  : "contain";
            context.addMessage("patientName", new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "No patients found under Doctor ID " + doctorId +
                    " whose name " + readableMatch + " '" + patientName + "'", null));
        } else {
            showPatientsFlag = true;
        }

    } else {
        associatedPatients = providerDao.getPatientListByDoctorId(doctorId);
        if (associatedPatients == null || associatedPatients.isEmpty()) {
            context.addMessage("doctorId", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "No patients found for Doctor ID: " + doctorId, null));
        } else {
            showPatientsFlag = true;
        }
    }

    return null;
}





public String showInsuranceForPatient(String hId) {
    System.out.println("view members called from nested table for hid " + hId);
    patientInsuranceList = insuranceDaoImpl.showInsuranceOfRecipient(hId);
    
    if (patientInsuranceList == null || patientInsuranceList.isEmpty()) {
        topMessage = "No insurance found for patient ID: " + hId;
        showInsuranceFlag = false;
        showPatientsFlag=true;
    } else {
        topMessage = null;
        showInsuranceFlag = true;
        showPatientsFlag=false;
    }

    return null;
}




public String getDoctorId() {
	return doctorId;
}
public void setDoctorId(String doctorId) {
	this.doctorId = doctorId;
}
public String getHealthId() {
	return healthId;
}
public void setHealthId(String healthId) {
	this.healthId = healthId;
}
public List<Recipient> getAssociatedPatients() {
	return associatedPatients;
}
public void setAssociatedPatients(List<Recipient> associatedPatients) {
	this.associatedPatients = associatedPatients;
}
public String redirect(PatientInsuranceDetails insurance) {
	FacesContext ctx = FacesContext.getCurrentInstance();
	System.out.println("_________________________redirecting to view members "+insurance);
	Map<String, Object> session = ctx.getExternalContext().getSessionMap();
	session.remove("selectedInsurance"); // force clear
	session.put("selectedInsurance", insurance);
	System.out.println("passed insurance"+session.get("selectedInsurance"));
    return "viewMembers?faces-redirect=true&ts=" + System.currentTimeMillis(); // timestamp tricks JSF into fresh redirect
}

public PatientInsuranceDetails loadSubscribedMembers() {
        Object obj = FacesContext.getCurrentInstance()
                                 .getExternalContext()
                                 .getSessionMap()
                                 .get("selectedInsurance");
    return (PatientInsuranceDetails)obj;
}

}

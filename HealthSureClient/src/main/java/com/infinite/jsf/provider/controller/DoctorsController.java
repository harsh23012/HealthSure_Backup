package com.infinite.jsf.provider.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.Transaction;

import com.infinite.jsf.provider.dao.DoctorsDao;
import com.infinite.jsf.provider.dao.ProviderDao;
import com.infinite.jsf.provider.daoImpl.DoctorsDaoImpl;
import com.infinite.jsf.provider.daoImpl.ProviderDaoImpl;
//import com.infinite.jsf.Util.SessionHelper;
import com.infinite.jsf.provider.model.DoctorStatus;
import com.infinite.jsf.provider.model.Doctor;
import com.infinite.jsf.provider.model.Provider;

public class DoctorsController implements Serializable{
	private static final long serialVersionUID = 1L;
	
	    private String providerId; 
	    private Doctor doctors = new Doctor();
	    private List<Doctor> doctorsList;
	    private DoctorsDao doctorDao = new DoctorsDaoImpl();
	    private ProviderDao providerDao = new ProviderDaoImpl();
	    
	    public DoctorsController() {
	    	 this.doctors = new Doctor();
	    	 //Fetch providerId from session
		        this.providerId= (String)FacesContext.getCurrentInstance()
		        .getExternalContext()
		        .getSessionMap()
		        .get("providerId");
		        
		        System.out.println("providerid is "+providerId);
		        
		        if(providerId != null) {
		        	Provider provider = new Provider();
		        	provider.setProviderId(providerId);
		        this.doctors.setProviders(new Provider());
		      }
	    }
	   

	    // === Add/register new doctor ===
	    public String doctors() {
	        this.doctors = new Doctor();
	        return "AddDoctors?faces-redirect=true";
	    }
	    public String addDoctors() { 
	    	System.out.println("Step 1: Entering addDoctors()");
	        FacesContext facesContext = FacesContext.getCurrentInstance();
	        boolean hasErrors = false;

	    try {
	        // 1) Validation
	        System.out.println("Step 2: Starting validation");

	        // Provider ID Validation
	        System.out.println("Step 3: Validating provider ID");
	        if (providerId == null || providerId.trim().isEmpty()) {
	            facesContext.addMessage("doctorForm:providerId",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Provider ID is required", null));
	            hasErrors = true;
	        } else if (!providerId.matches("^PROV\\d{3}$")) {
	            facesContext.addMessage("doctorForm:providerId",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Provider ID format (e.g., PROV001)", null));
	            hasErrors = true;
	        }

	        // Doctor Name
	        System.out.println("Step 4: Validating doctor name");
	        if (doctors.getDoctorName() == null ||
	            !doctors.getDoctorName().matches("^[A-Za-z\\s]+$")) {
	            facesContext.addMessage("doctorForm:doctorName",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor name must contain only letters and spaces", null));
	            hasErrors = true;
	        }

	        // Qualification
	        System.out.println("Step 5: Validating qualification");
	        String[] validQualifications = {
	            "MBBS", "BDS", "BAMS", "BHMS", "BUMS", "MD", "MS", "DNB"
	        };
	        boolean qualOk = Arrays.stream(validQualifications)
	            .anyMatch(q -> q.equalsIgnoreCase(doctors.getQualification()));
	        if (!qualOk) {
	            facesContext.addMessage("doctorForm:qualification",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid qualification", null));
	            hasErrors = true;
	        }

	        // Specialization
	        System.out.println("Step 6: Validating specialization");
	        String[] validSpecs = {
	            "Cardiologist", "Dermatologist", "Neurologist", "Oncologist",
	            "Orthopedic", "Psychiatrist", "General Surgeon", "Neurosurgeon"
	        };
	        boolean specOk = Arrays.stream(validSpecs)
	            .anyMatch(s -> s.equalsIgnoreCase(doctors.getSpecialization()));
	        if (!specOk) {
	            facesContext.addMessage("doctorForm:specialization",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid specialization", null));
	            hasErrors = true;
	        }

	        // License Number
	        System.out.println("Step 7: Validating license format");
	        if (doctors.getLicenseNo() == null ||
	            !doctors.getLicenseNo().matches("^[A-Z]{2}/\\d{4}/\\d{5}/[A-Z]$") ||
	            doctors.getLicenseNo().length() != 15) {
	            facesContext.addMessage("doctorForm:licenseNo",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid license number format", null));
	            hasErrors = true;
	        }

	        // Email
	        System.out.println("Step 8: Validating email");
	        if (doctors.getEmail() == null ||
	            !doctors.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
	            facesContext.addMessage("doctorForm:email",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email format", null));
	            hasErrors = true;
	        }

	        // Phone Number
	        System.out.println("Step 9: Validating phone number");
	        if (doctors.getPhoneNumber() == null ||
	            !doctors.getPhoneNumber().matches("^\\d{10}$")) {
	            facesContext.addMessage("doctorForm:phoneNumber",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number must be 10 digits", null));
	            hasErrors = true;
	        }
	        
	     // Address
	        System.out.println("Step 10: Validating address");
	        if (doctors.getAddress() == null || doctors.getAddress().trim().isEmpty()) {
	            facesContext.addMessage("doctorForm:address",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Address is required", null));
	            hasErrors = true;
	        }

	        // Gender
	        System.out.println("Step 11: Validating gender");
	        if (doctors.getGender() == null) {
	            facesContext.addMessage("doctorForm:gender",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Gender is required", null));
	            hasErrors = true;
	        }

	        // Doctor Type
	        System.out.println("Step 12: Validating doctor type");
	        if (doctors.getDoctorType() == null) {
	            facesContext.addMessage("doctorForm:type",
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Doctor Type is required", null));
	            hasErrors = true;
	        }

	        // If any errors, stop processing
	        if (hasErrors) {
	            System.out.println("Invalid Input.");
	            return null;
	        }

	        // 2) Prepare entity
	        System.out.println("Step 10: Setting default status and generating ID");
	        doctors.setDoctorStatus(DoctorStatus.INACTIVE);

	        Provider provider = providerDao.getProviderId(providerId);
	        doctors.setProvider(provider);
	        doctors.setDoctorId(doctorDao.generateDoctorId());
	        doctorDao.addDoctors(doctors);

	        // 3) Success message
	        System.out.println("Step 11: Adding success message");
	        facesContext.addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_INFO, "Doctor added successfully", null));

	        return "Success.jsp?faces-redirect=true";

	    } catch (Exception e) {
	        System.out.println("Exception in addDoctors(): " + e.getMessage());
	        facesContext.addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add Doctor", null));
	        return null;
	    }

}
	  

	    // === Get all doctors ===
	    public List<Doctor> getDoctorsList() {
	        if (doctorsList == null) {
	            try {
	                doctorsList = doctorDao.getAllDoctors();
	                if(doctorsList == null) {
	                	doctorsList = new DoctorsDaoImpl().getAllDoctors();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                doctorsList = new ArrayList<>();
	            }
	        }
	        return doctorsList;
	    }

	    // === Get doctors by provider ID ===
	    public List<Doctor> getDoctorsByProvider(String providerId) {
	        try {
	            return doctorDao.searchByProviderId(providerId);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }

	    // === Update doctor details ===
	    public String updateDoctor() {
	        try {
	            doctorDao.updateDoctors(doctors);
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Doctor Updated Successfully", ""));
	            return "doctor_updated.xhtml?faces-redirect=true";
	        } catch (Exception e) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to Update Doctor: " + e.getMessage(), ""));
	            return null;
	        }
	    }

	    // === Delete doctor by ID ===
	    public String deleteDoctor(int doctorId) {
	        try {
	            doctorDao.deleteDoctors(doctorId);
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_INFO, "Doctor Deleted Successfully", ""));
	            return "doctor_deleted.xhtml?faces-redirect=true";
	        } catch (Exception e) {
	            FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to Delete Doctor: " + e.getMessage(), ""));
	            return null;
	        }
	    }

	    // === Getters and Setters ===
	    public Doctor getDoctors() {
	        return doctors;
	    }

	    public void setDoctors(Doctor doctor) {
	        this.doctors = doctor;
	    }

		public String getProviderId() {
			return providerId;
		}

		public void setProviderId(String providerId) {
			this.providerId = providerId;
		}

		public ProviderDao getProviderDao() {
			return providerDao;
		}

		public void setProviderDao(ProviderDao providerDao) {
			this.providerDao = providerDao;
		}


		public void setDoctorsList(List<Doctor> doctorsList) {
			this.doctorsList = doctorsList;
		}
}

package com.infinite.jsf.util;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

import com.infinite.ejb.insurance.model.InsuranceCoverageOption;
import com.infinite.ejb.provider.model.Appointment;
import com.infinite.ejb.provider.model.LoginStatus;
import com.infinite.ejb.provider.model.ProviderStatus;
import com.infinite.ejb.recipient.model.Gender;
import com.infinite.ejb.recipient.model.Status;
import com.infinite.jsf.insurance.model.Subscribe;
import com.infinite.jsf.provider.model.Doctor;
import com.infinite.jsf.provider.model.MedicalProcedure;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.recipient.model.Recipient;

public class ModelMapperUtil {

    public static com.infinite.ejb.recipient.model.Recipient mapToEjbRecipient(Recipient jsfRecipient) {
        if (jsfRecipient == null) return null;

        com.infinite.ejb.recipient.model.Recipient ejbRecipient = new com.infinite.ejb.recipient.model.Recipient();
        ejbRecipient.sethId(jsfRecipient.gethId());
        ejbRecipient.setFirstName(jsfRecipient.getFirstName());
        ejbRecipient.setLastName(jsfRecipient.getLastName());
        ejbRecipient.setMobile(jsfRecipient.getMobile());
        ejbRecipient.setUserName(jsfRecipient.getUserName());
        ejbRecipient.setGender(Gender.valueOf(jsfRecipient.getGender().name()));
        ejbRecipient.setDob(jsfRecipient.getDob());
        ejbRecipient.setAddress(jsfRecipient.getAddress());
        ejbRecipient.setCreatedAt(jsfRecipient.getCreatedAt());
        ejbRecipient.setPassword(jsfRecipient.getPassword());
        ejbRecipient.setEmail(jsfRecipient.getEmail());
        ejbRecipient.setStatus(Status.valueOf(jsfRecipient.getStatus().name()));
        ejbRecipient.setLoginAttempts(jsfRecipient.getLoginAttempts());
        ejbRecipient.setLockedUntil(jsfRecipient.getLockedUntil());
        ejbRecipient.setLastLogin(jsfRecipient.getLastLogin());
        ejbRecipient.setPasswordUpdatedAt(jsfRecipient.getPasswordUpdatedAt());
        return ejbRecipient;
    }

    public static com.infinite.ejb.provider.model.Provider mapToEjbProvider(Provider jsfProvider) {
        if (jsfProvider == null) return null;

        com.infinite.ejb.provider.model.Provider ejbProvider = new com.infinite.ejb.provider.model.Provider();
        ejbProvider.setProviderId(jsfProvider.getProviderId());
        ejbProvider.setProviderName(jsfProvider.getProviderName());
        ejbProvider.setHospitalName(jsfProvider.getHospitalName());
        ejbProvider.setEmail(jsfProvider.getEmail());
        ejbProvider.setAddress(jsfProvider.getAddress());
        ejbProvider.setCity(jsfProvider.getCity());
        ejbProvider.setState(jsfProvider.getState());
        ejbProvider.setZipCode(jsfProvider.getZipCode());
        ejbProvider.setStatus(ProviderStatus.valueOf(jsfProvider.getStatus().name()));
        ejbProvider.setCreatedAt(jsfProvider.getCreatedAt());
        return ejbProvider;
    }

    public static com.infinite.ejb.provider.model.Doctor mapToEjbDoctor(Doctor jsfDoctor) {
        if (jsfDoctor == null) return null;

        com.infinite.ejb.provider.model.Doctor ejbDoctor = new com.infinite.ejb.provider.model.Doctor();
        ejbDoctor.setDoctorId(jsfDoctor.getDoctorId());
        ejbDoctor.setProviderId(jsfDoctor.getProvider().getProviderId());
        ejbDoctor.setDoctorName(jsfDoctor.getDoctorName());
        ejbDoctor.setQualification(jsfDoctor.getQualification());
        ejbDoctor.setSpecialization(jsfDoctor.getSpecialization());
        ejbDoctor.setLicenseNo(jsfDoctor.getLicenseNo());
        ejbDoctor.setEmail(jsfDoctor.getEmail());
        ejbDoctor.setAddress(jsfDoctor.getAddress());
        ejbDoctor.setGender(jsfDoctor.getGender());
        ejbDoctor.setPassword(jsfDoctor.getPassword());
        ejbDoctor.setLoginStatus(jsfDoctor.getLoginStatus().name());
        ejbDoctor.setDoctorStatus(jsfDoctor.getDoctorStatus().name());

        // Optional: link provider if available
        if (jsfDoctor.getProvider() != null) {
            ejbDoctor.setProvider(mapToEjbProvider(jsfDoctor.getProvider()));
        }

        return ejbDoctor;
    }

    public static com.infinite.ejb.provider.model.MedicalProcedure mapToEjbProcedure(MedicalProcedure jsfProc) {
        if (jsfProc == null) return null;

        com.infinite.ejb.provider.model.MedicalProcedure ejbProc = new com.infinite.ejb.provider.model.MedicalProcedure();
        ejbProc.setProcedureId(jsfProc.getProcedureId());
        ejbProc.setProcedureDate(jsfProc.getProcedureDate());
        ejbProc.setDiagnosis(jsfProc.getDiagnosis());
        ejbProc.setRecommendations(jsfProc.getRecommendations());
        ejbProc.setFromDate(jsfProc.getFromDate());
        ejbProc.setToDate(jsfProc.getToDate());
        ejbProc.setCreatedAt(jsfProc.getCreatedAt());

        // Set associations
        ejbProc.setRecipient(mapToEjbRecipient(jsfProc.getRecipient()));
        ejbProc.setProvider(mapToEjbProvider(jsfProc.getProvider()));
        ejbProc.setDoctor(mapToEjbDoctor(jsfProc.getDoctor()));

        if (jsfProc.getAppointment() != null) {
        	com.infinite.ejb.provider.model.Appointment ap=new Appointment();
        	ap.setAppointmentId(jsfProc.getAppointment().getAppointmentId());
            ejbProc.setAppointment(ap); // Assuming compatible or pre-mapped
        }

        return ejbProc;
    }

    public static com.infinite.ejb.insurance.model.Subscribe mapToEjbSubscribe(Subscribe jsfSub) {
        if (jsfSub == null) return null;

        com.infinite.ejb.insurance.model.Subscribe ejbSub = new com.infinite.ejb.insurance.model.Subscribe();
        ejbSub.setSubscribeDate(jsfSub.getSubscribeDate());
        ejbSub.setExpiryDate(jsfSub.getExpiryDate());
        ejbSub.setStatus(jsfSub.getStatus().name());
        ejbSub.setTotalPremium(jsfSub.getTotalPremium());
        ejbSub.setAmountPaid(jsfSub.getAmountPaid());

        if (jsfSub.getCoverage() != null) {
        	com.infinite.ejb.insurance.model.InsuranceCoverageOption co = new InsuranceCoverageOption();
        	co.setCoverageId(jsfSub.getCoverage().getCoverageId());
            ejbSub.setCoverageOption(co); // Assuming types align
        }

        return ejbSub;
    }
}

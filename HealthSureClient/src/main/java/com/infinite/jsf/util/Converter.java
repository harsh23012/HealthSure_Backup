package com.infinite.jsf.util;

import com.infinite.ejb.provider.model.Appointment;
import com.infinite.ejb.provider.model.Doctor;
import com.infinite.ejb.provider.model.DoctorAvailability;
import com.infinite.ejb.provider.model.Provider;
import com.infinite.ejb.recipient.model.Recipient;

public class Converter {
	public static Appointment convertToEJBAppointment(com.infinite.jsf.provider.model.Appointment jsfAppointment) {
	    if (jsfAppointment == null) return null;
	    Appointment ejbAppointment = new Appointment();
	    ejbAppointment.setAppointmentId(jsfAppointment.getAppointmentId());
	    ejbAppointment.setDoctor(convertToEJBDoctor(jsfAppointment.getDoctor()));
	    ejbAppointment.setRecipient(convertToEJBRecipient(jsfAppointment.getRecipient()));
	    ejbAppointment.setProvider(convertToEJBProvider(jsfAppointment.getProvider()));
	    if (jsfAppointment.getAvailability() != null) {
	        DoctorAvailability availability = new DoctorAvailability();
	        availability.setAvailabilityId(jsfAppointment.getAvailability().getAvailabilityId());
	        ejbAppointment.setAvailability(availability);
	    }
	    return ejbAppointment;
	}
	public static com.infinite.ejb.provider.model.Doctor convertToEJBDoctor(
	        com.infinite.jsf.provider.model.Doctor jsfDoctor) {
	    if (jsfDoctor == null || jsfDoctor.getDoctorId() == null) return null;

	    com.infinite.ejb.provider.model.Doctor ejbDoctor = new com.infinite.ejb.provider.model.Doctor();
	    ejbDoctor.setDoctorId(jsfDoctor.getDoctorId());
	    return ejbDoctor;
	}
	public static com.infinite.ejb.provider.model.Provider convertToEJBProvider(
	        com.infinite.jsf.provider.model.Provider jsfProvider) {
	    if (jsfProvider == null || jsfProvider.getProviderId() == null) return null;

	    com.infinite.ejb.provider.model.Provider ejbProvider = new com.infinite.ejb.provider.model.Provider();
	    ejbProvider.setProviderId(jsfProvider.getProviderId());
	    return ejbProvider;
	}
	public static com.infinite.ejb.recipient.model.Recipient convertToEJBRecipient(
	        com.infinite.jsf.recipient.model.Recipient jsfRecipient) {

	    if (jsfRecipient == null || jsfRecipient.gethId() == null) return null;

	    com.infinite.ejb.recipient.model.Recipient ejbRecipient = new com.infinite.ejb.recipient.model.Recipient();

	    ejbRecipient.sethId(jsfRecipient.gethId());
	    ejbRecipient.setFirstName(jsfRecipient.getFirstName());
	    ejbRecipient.setLastName(jsfRecipient.getLastName());
	    ejbRecipient.setMobile(jsfRecipient.getMobile());
	    ejbRecipient.setUserName(jsfRecipient.getUserName());
	    ejbRecipient.setDob(jsfRecipient.getDob());
	    ejbRecipient.setAddress(jsfRecipient.getAddress());
	    ejbRecipient.setCreatedAt(jsfRecipient.getCreatedAt());
	    ejbRecipient.setPassword(jsfRecipient.getPassword());
	    ejbRecipient.setEmail(jsfRecipient.getEmail());
	    ejbRecipient.setGender(
	    	    jsfRecipient.getGender() != null 
	    	    ? com.infinite.ejb.recipient.model.Gender.valueOf(jsfRecipient.getGender().name()) 
	    	    : null
	    	);

	    	ejbRecipient.setStatus(
	    	    jsfRecipient.getStatus() != null 
	    	    ? com.infinite.ejb.recipient.model.Status.valueOf(jsfRecipient.getStatus().name()) 
	    	    : null
	    	);

	    ejbRecipient.setLoginAttempts(jsfRecipient.getLoginAttempts());
	    ejbRecipient.setLockedUntil(jsfRecipient.getLockedUntil());
	    ejbRecipient.setLastLogin(jsfRecipient.getLastLogin());
	    ejbRecipient.setPasswordUpdatedAt(jsfRecipient.getPasswordUpdatedAt());

	    return ejbRecipient;
	}


}

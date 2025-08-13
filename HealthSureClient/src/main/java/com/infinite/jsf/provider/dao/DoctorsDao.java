package com.infinite.jsf.provider.dao;

import java.util.List;

import com.infinite.jsf.provider.model.Doctor;

public interface DoctorsDao {
    void addDoctors(Doctor doctors) throws Exception;
    Doctor searchByDoctorsId(int doctorId) throws Exception;
    List<Doctor> getAllDoctors() throws Exception;
    List<Doctor> searchByProviderId(String providerId) throws Exception;
    void updateDoctors(Doctor doctor) throws Exception;
    void deleteDoctors(int doctorId) throws Exception;
//	void saveDoctor(Doctors doctor);
	String generateDoctorId();

}

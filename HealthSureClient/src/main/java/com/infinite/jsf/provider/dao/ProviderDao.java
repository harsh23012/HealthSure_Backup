package com.infinite.jsf.provider.dao;

import java.util.List;

import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.provider.model.ProviderDashboardDto;

public interface ProviderDao {
	void addProvider(Provider provider) throws Exception;
	List<Provider> getAllProvider() throws Exception;
    Provider login(String email, String encryptedPassword) throws Exception;
    boolean emailExists(String email) throws Exception;
    boolean phoneExists(String phone) throws Exception;
    boolean zipcodeExists(String zipcode) throws Exception;
	boolean updatePasswordByEmail(String emsil, String newPassword) throws Exception;
	Provider getProviderId(String providerId) throws Exception;
	Provider findByEmail(String email);
	String resetPassword(String providerId, String oldPassword, String newPassword);
	ProviderDashboardDto getDashboardMetrics(String providerId);

}
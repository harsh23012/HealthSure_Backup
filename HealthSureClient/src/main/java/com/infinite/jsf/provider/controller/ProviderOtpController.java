package com.infinite.jsf.provider.controller;

import java.sql.SQLException;

import com.infinite.jsf.provider.dao.ProviderOtpDao;
import com.infinite.jsf.provider.daoImpl.ProviderOtpDaoImpl;
import com.infinite.jsf.provider.model.ProviderOtp;
import com.infinite.jsf.provider.model.Reason;

public class ProviderOtpController {
	private String providerId;
    private String otpCode;
    private String message;
    private ProviderOtp latestOtp;

    private ProviderOtpDao providerOtpDao = new ProviderOtpDaoImpl();

    private ProviderOtp otp = new ProviderOtp();

    // ✅ Method to Verify OTP
    public String verifyOtp(String email, Reason reason) {
        try {
            message = providerOtpDao.verifyOtp(email, otpCode, reason);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            message = "Error verifying OTP: " + e.getMessage();
        }
        return "OtpVerified";  
    }

    // ✅ Method to Get Latest OTP for Provider
    public String getLatestOtp() {
        try {
            latestOtp = providerOtpDao.getLatestOtp(providerId);
            if (latestOtp != null) {
                message = "Latest OTP: " + latestOtp.getOtpCode();
            } else {
                message = "No OTP found.";
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            message = "Error fetching OTP: " + e.getMessage();
        }
        return "LatestOtp";  
    }

    // ✅ Method to Mark OTP as Verified Manually
    public String markOtpAsVerified(int otpId) {
        try {
            message = providerOtpDao.markOtpAsVerified(otpId);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            message = "Error marking OTP: " + e.getMessage();
        }
        return "OtpMarked";  
    }

    // ===== Getters and Setters =====

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getMessage() {
        return message;
    }

    public ProviderOtp getOtp() {
        return otp;
    }

    public void setOtp(ProviderOtp otp) {
        this.otp = otp;
    }

    public ProviderOtp getLatestOtpDetails() {
        return latestOtp;
    }
}

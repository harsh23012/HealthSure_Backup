package com.infinite.jsf.provider.dao;

import java.sql.SQLException;

import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.provider.model.ProviderOtp;
import com.infinite.jsf.provider.model.Reason;


public interface ProviderOtpDao {

	 // ✅ Generate and insert OTP by email directly
//    String generateOtp(Provider provider ) throws ClassNotFoundException, SQLException;

    // ✅ Verify if OTP is correct for a given provider
    String verifyOtp(String providerEmail, String otpCode, Reason reason) throws ClassNotFoundException, SQLException;

    // ✅ Get the most recent OTP for a provider (for verification or resend)
    ProviderOtp getLatestOtp(String providerEmail) throws ClassNotFoundException, SQLException;

    // ✅ Mark an OTP record as verified
    String markOtpAsVerified(int otpId) throws ClassNotFoundException, SQLException;

    // ✅ Update an existing OTP record (e.g., after verification)
    String updateOtp(ProviderOtp otp) throws ClassNotFoundException, SQLException;

	String resendOtp(String email) throws ClassNotFoundException, SQLException;

	String generateOtp(Provider provider, Reason reason) throws ClassNotFoundException, SQLException;
}

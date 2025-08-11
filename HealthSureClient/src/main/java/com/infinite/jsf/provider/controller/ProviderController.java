package com.infinite.jsf.provider.controller;

import java.io.Serializable;
import java.sql.Timestamp;
//import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.infinite.jsf.provider.dao.ProviderDao;
import com.infinite.jsf.provider.dao.ProviderOtpDao;
import com.infinite.jsf.provider.daoImpl.ProviderDaoImpl;
import com.infinite.jsf.provider.daoImpl.ProviderOtpDaoImpl;
import com.infinite.jsf.util.EncryptPassword;
import com.infinite.jsf.provider.model.LoginStatus;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.provider.model.ProviderOtp;
import com.infinite.jsf.provider.model.Reason;



public class ProviderController implements Serializable{

	 Timestamp now = new Timestamp(System.currentTimeMillis());
	    Timestamp expiry = new Timestamp(now.getTime() + 2 * 60 * 1000); // 2 minutes
	
    private static final long serialVersionUID = 1L;
    
	private Provider provider;
	private String email;
	private String otpCode;
	private boolean otpSent = false;
	private List<Provider> providerList;
	private ProviderDao providerDaoImpl = new ProviderDaoImpl();
    private ProviderOtpDao providerOtpDaoImpl = new ProviderOtpDaoImpl();
    
    public ProviderController() {
        // Manually initialize DAO implementations
    	   provider = new Provider();
       providerDaoImpl = new ProviderDaoImpl();
       providerOtpDaoImpl = new ProviderOtpDaoImpl();
    }


    // ✅ Register a new provider with password confirmation
    public String signUp()
    {
    	this.provider=new Provider();
    	return "SignUp?faces-redirect=true";
    }
    public String register() throws Exception {
    	System.out.println("controller called...");
        System.out.println("Registering provider...");
        boolean a=false;

        if (provider == null) {
            System.out.println("Provider object is null!");
            return null;
        } 

        if (providerDaoImpl == null || providerOtpDaoImpl == null) {
            System.out.println("DAO is not initialized!");
            return null;
        }
        
     // ✅ Validate Provider Name: Only letters and spaces
        if (!provider.getProviderName().matches("^[A-Za-z\\s]+$")) {
        	System.out.println(1);
            FacesContext.getCurrentInstance().addMessage("providerName",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name must contain only alphabets.", null));
            a=true;
        }

        // ✅ Validate Hospital Name: Optional but must be non-numeric if provided
        String hospital = provider.getHospitalName();
        if (hospital != null && !hospital.trim().isEmpty() && !hospital.matches("^[A-Za-z\\s]+$")) {
        	System.out.println(12);
        	FacesContext.getCurrentInstance().addMessage("hospitalName",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hospital name must contain only alphabets.", null));
        	a=true;
        }

        // ✅ Email Format Validation
        if (!provider.getEmail().matches("^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,4}$")) {
        	System.out.println(13);
        	FacesContext.getCurrentInstance().addMessage("email",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email format.", null));
            a=true;
        }

        // ✅ Email Uniqueness Check
        if (providerDaoImpl.emailExists(provider.getEmail())) {
        	System.out.println(14);
        	FacesContext.getCurrentInstance().addMessage("email",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email already exists.", null));
        	a=true;
        }

        // ✅ Phone Number Format Validation
        if (!provider.getTelephone().matches("^[0-9]{10}$")) {
        	System.out.println(15);
        	FacesContext.getCurrentInstance().addMessage("telephone",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number must be exactly 10 digits.", null));
        	a=true;
        }

        // ✅ Phone Number Uniqueness Check
        if (providerDaoImpl.phoneExists(provider.getTelephone())) {
        	System.out.println(16);
        	FacesContext.getCurrentInstance().addMessage("telephone",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Phone number already exists.",null));
        	a=true;
        }

        // ✅ Zipcode Format Validation
        if (!provider.getZipcode().matches("^[0-9]{6}$")) {
        	System.out.println(17);
        	FacesContext.getCurrentInstance().addMessage("zipcode",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Zipcode must be 6 digits.", null));
        	a=true;
        }

//        // ✅ Zipcode Uniqueness Check
//        if (providerDaoImpl.zipcodeExists(provider.getZipcode())) {
//        	System.out.println(18);
//        	FacesContext.getCurrentInstance().addMessage("zipcode",
//                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Zipcode already exists.", null));
//        	a=true;
//        }
//        if(a) {
//        	return null;
//        }
        provider.setStatus(LoginStatus.PENDING);

        // Save provider to database
        providerDaoImpl.addProvider(provider);
        System.out.println("Provider added successfully.");
        FacesContext.getCurrentInstance().addMessage("null",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Success", "Provider added Successfully"));
        
        // Generate OTP
        String otp = providerOtpDaoImpl.generateOtp(provider, Reason.SIGNUP);

        // Simulate sending OTP (replace with actual email/SMS logic)
        System.out.println("OTP sent to: " + provider.getEmail() + " | OTP: " + otp);

        // Store email in session for OTP verification
        FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().put("providerEmail", provider.getEmail());
        

        // Redirect to OTP verification page
        return "VerifyOtp.jsf?faces-redirect=true";
    }
    
 // ✅ Submit OTP
    
    private boolean otpVerified = false;

    public boolean isOtpVerified() {
        return otpVerified;
    }
    
    public String verifyOtp() throws Exception {
        System.out.println("verifyOtp triggered");
        
        String email = provider.getEmail();
        System.out.println("Step 1: Retrieved provider email = " + email);
        
        String inputOtp = this.otpCode;
        System.out.println("Step 2: User entered OTP = " + inputOtp);
        
        ProviderOtp latestOtp = providerOtpDaoImpl.getLatestOtp(email);
        System.out.println("Step 3: Retrieved latestOtp = " + (latestOtp != null ? latestOtp.getOtpCode() : "null"));
        
        if (latestOtp == null) {
            System.out.println("Step 4: No OTP record found for this email");
            FacesContext.getCurrentInstance().addMessage(
                "otp",
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Invalid OTP",
                    "Please check the code and try again"
                )
            );
            otpVerified = false;
            System.out.println("Step 5: Returning null due to missing OTP");
            return null;
        }
        
        if (!latestOtp.getOtpCode().equals(inputOtp)) {
            System.out.println("Step 6: OTP mismatch (stored=" + latestOtp.getOtpCode() + ")");
            FacesContext.getCurrentInstance().addMessage(
                "otp",
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Invalid OTP",
                    "Please check the code and try again"
                )
            );
            otpVerified = false;
            System.out.println("Step 7: Returning null due to invalid OTP");
            return null;
        }
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        System.out.println("Step 8: Current timestamp = " + now);
        
        Timestamp expiresAt = latestOtp.getExpiresAt();
        System.out.println("Step 9: existing expiresAt = " + expiresAt);
        
        if (expiresAt == null) {
            expiresAt = new Timestamp(latestOtp.getCreatedAt().getTime() + 2 * 60 * 1000);
            latestOtp.setExpiresAt(expiresAt);
            providerOtpDaoImpl.updateOtp(latestOtp);   
            System.out.println("Step 10: Set new expiresAt = " + expiresAt);
        }
        
        if (now.after(expiresAt)) {
            System.out.println("Step 11: OTP has expired (expiresAt=" + expiresAt + ")");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_WARN,"OTP has expired.","Please request a new code"));
            otpVerified = false;
            System.out.println("Step 12: Returning null due to expired OTP");
            return null;
        }
        
        System.out.println("Step 13: OTP is valid and not expired");
        
        latestOtp.setIsVerified(true);
        System.out.println("Step 14: Marking OTP as verified");
        providerOtpDaoImpl.updateOtp(latestOtp);
        System.out.println("Step 15: Updated OTP record in database");
        
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,"OTP Verified ","OTP Verified Successfully"));
        otpVerified = true;
        System.out.println("Step 16: otpVerified set to true");
        
        System.out.println("Step 17: Redirecting to GeneratePassword page");
        return "GeneratePassword?faces-redirect=true";
    }
    
    
    // ✅ Resend OTP
    	 public String resendOtp() throws Exception {
    		    String result = providerOtpDaoImpl.resendOtp(provider.getEmail());
    		    FacesContext.getCurrentInstance()
    		      .addMessage(null,
    		        new FacesMessage(FacesMessage.SEVERITY_INFO, result, null));
    		    return null;
    		  }
    
    
    // ✅ Update password method (NEW)
    public String updatePassword() throws Exception {
        System.out.println("=== updatePassword() invoked ===");

        String email      = provider.getEmail();
        String newPwd     = provider.getNewPassword();
        String confirmPwd = provider.getConfirmPassword();

        System.out.println("Inputs → email: " + email
            + " | newPwd length: " + (newPwd  != null ? newPwd.length()  : "null")
            + " | confirmPwd length: " + (confirmPwd != null ? confirmPwd.length() : "null"));

        // Step 1: Required‐fields check
        System.out.println("Step 1: Checking required fields…");
        if (email == null || email.trim().isEmpty()
         || newPwd == null || newPwd.trim().isEmpty()
         || confirmPwd == null || confirmPwd.trim().isEmpty()) {
            System.out.println("❌ Validation failed: one or more fields are empty");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "All fields are required.", null)
            );
            return null;
        }
        System.out.println("✔ Required fields passed");

        // Step 2: Email format check
        System.out.println("Step 2: Validating email format…");
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!email.matches(emailPattern)) {
            System.out.println("❌ Validation failed: invalid email format → " + email);
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email address.", null)
            );
            return null;
        }
        System.out.println("✔ Email format valid");

        // Step 3: Password strength check
        System.out.println("Step 3: Checking password strength…");
        boolean lengthOK  = newPwd.length() >= 8;
        boolean upperOK   = newPwd.matches(".*[A-Z].*");
        boolean digitOK   = newPwd.matches(".*\\d.*");
        boolean specialOK = newPwd.matches(".*[!@#$%^&*()].*");

        if (!lengthOK || !upperOK || !digitOK || !specialOK) {
            System.out.println("❌ Validation failed: weak password"
                + " | lengthOK=" + lengthOK
                + " | upperOK=" + upperOK
                + " | digitOK=" + digitOK
                + " | specialOK=" + specialOK);
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Password must be ≥8 chars, include uppercase, number & special char.",
                    null
                )
            );
            return null;
        }
        System.out.println("✔ Password strength OK");

        // Step 4: Match confirmation
        System.out.println("Step 4: Checking password match…");
        if (!newPwd.equals(confirmPwd)) {
            System.out.println("❌ Validation failed: passwords do not match");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match.", null)
            );
            return null;
        }
        System.out.println("✔ Passwords match");

        // Step 5: Encrypt & persist
        System.out.println("Step 5: Encrypting password…");
        String encrypted = EncryptPassword.getCode(newPwd);
        System.out.println("Encrypted password: " + encrypted);

        System.out.println("Step 6: Updating database…");
        boolean updated = providerDaoImpl.updatePasswordByEmail(email.trim(), encrypted);
        if (updated) {
            System.out.println("✔ Password updated successfully for: " + email);
            provider.setNewPassword(null);
            provider.setConfirmPassword(null);
            provider.setPassword(null);

            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,"Password updated successfully. Please login.",null));
            return "Success.jsp?faces-redirect=true";
        }

        System.out.println("❌ Update failed: no provider found with email → " + email);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "No provider found with this email.", null)
        );
        return null;
    }

    
 // ✅ Login existing provider with detailed step-by-step logging
    public String login() {
        System.out.println("=== login() invoked ===");

        // 1. Null‐check backing Provider bean
        if (provider == null) {
            System.out.println("❌ login aborted: provider bean is null");
            return null;
        }
        System.out.println("✔ provider bean present: " + provider);

        // 2. Normalize inputs
        String email = (provider.getEmail() == null) 
            ? "" 
            : provider.getEmail().trim().toLowerCase();
        String plainPassword = (provider.getPassword() == null) 
            ? "" 
            : provider.getPassword().trim();
        System.out.println("Normalized → email: '" + email 
            + "' | password present? " + !plainPassword.isEmpty());

        // 3. Validate
        if (email.isEmpty() || plainPassword.isEmpty()) {
            System.out.println("❌ validation failed: missing email or password");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, 
                    "Email and password are required.", 
                    null
                )
            );
            return null;
        }
        System.out.println("✔ input validation passed");

        // 4. Encrypt password
        String encryptedPassword = EncryptPassword.getCode(plainPassword);
        System.out.println("Encrypted password generated");

        // 5. Call DAO inside try/catch
        Provider dbProvider;
        System.out.println("Calling DAO.login(email, encryptedPassword)");
        try {
            dbProvider = providerDaoImpl.login(email, encryptedPassword);
            System.out.println("DAO.login returned: " + dbProvider);
        } catch (Exception e) {
            System.err.println("❌ DAO.login threw exception: " + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_FATAL, 
                    "System error. Please try again later.", 
                    null
                )
            );
            return null;
        }

        // 6. Check credentials
        if (dbProvider == null) {
            System.out.println("❌ authentication failed: no matching provider");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Invalid email or password.",
                    null
                )
            );
            return null;
        }
        System.out.println("✔ authentication successful for: " 
            + dbProvider.getProviderName());

        // 7. Handle account status
        LoginStatus status = dbProvider.getStatus();
        System.out.println("Provider status: " + status);

        if (status == LoginStatus.APPROVED) {
            // store in session
            FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("loggedInProvider", dbProvider);
            System.out.println("✔ status APPROVED: session updated");
	        System.out.println("provider id is "+dbProvider.getProviderId());
            FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().put("providerId", dbProvider.getProviderId());
            System.out.println("dbprovider : " + dbProvider);
            this.provider = null;
            FacesContext.getCurrentInstance().getViewRoot().getChildren().clear();
            return "Provider.jsp?faces-redirect=true";

        } else if (status == LoginStatus.PENDING) {
            System.out.println("⚠️ status PENDING: account not approved yet");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_WARN, 
                    "Your account is not approved yet.", 
                    null
                )
            );
            return null;

        } else {
            System.out.println("❌ status INACTIVE/LOCKED: login blocked");
            FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Your account is locked or inactive.",
                    null
                )
            );
            FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().put("providerId", provider.getProviderId());
            
            this.provider = null;
            FacesContext.getCurrentInstance().getViewRoot().getChildren().clear();
            return null;
        }
        
//        public List<Provider> getProviderList() {
//            if (providerList == null) {
//                try {
//                    providerList = providerDaoImpl.getAllProvider();
//                } catch (Exception e) {
//                    System.err.println("Failed to fetch provider list: " + e.getMessage());
//                    e.printStackTrace();
//                    providerList = new ArrayList<>(); // Return empty list on failure
//                }
//            }
//            return providerList;
//        }
    }
    
    public String sendOtpForLogin() throws Exception {
        System.out.println("Step 1: Validating email input...");
        if (email == null || email.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required.", null));
            System.out.println("Step 1.1: Email is missing.");
            otpSent = false;
            return null;
        }

        System.out.println("Step 2: Checking if provider exists for email: " + email);
        email = email.trim().toLowerCase(); // Normalize email
        Provider existingProvider = providerDaoImpl.findByEmail(email);
        if (existingProvider == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not SignUp Yet !!", null));
            System.out.println("Step 2.1: No provider found.");
            otpSent = false;
            return null;
        }

        System.out.println("Step 3: Generating OTP for login...");
        String otp = providerOtpDaoImpl.generateOtp(existingProvider, Reason.LOGIN);
        System.out.println("Step 3.1: OTP generated: " + otp);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("providerEmail", email);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "OTP sent to your email.", null));
        System.out.println("Step 4: OTP sent and email stored in session.");

        otpSent = true; // 
        return null; // stay on same page and re-render
    }


    public String verifyLoginOtp() throws Exception {
        System.out.println("Step 1: Getting session email and OTP input...");
        String sessionEmail = (String) FacesContext.getCurrentInstance().getExternalContext()
                .getSessionMap().get("providerEmail");

        if (sessionEmail == null || otpCode == null || otpCode.trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing email or OTP.", null));
            System.out.println("Step 1.1: Missing data - email or OTP code.");
            otpSent = true;
            return null;
        }

        System.out.println("Step 2: Retrieving latest OTP from DB for email: " + sessionEmail);
        ProviderOtp latestOtp = providerOtpDaoImpl.getLatestOtp(sessionEmail);
        if (latestOtp == null || !latestOtp.getOtpCode().equals(otpCode)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid OTP.", null));
            System.out.println("Step 2.3: Invalid OTP.");
            otpSent = true;
            return null;
        }

        System.out.println("Step 3: Validating OTP expiration...");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp expiry = latestOtp.getExpiresAt();

        if (expiry == null) {
            expiry = new Timestamp(latestOtp.getCreatedAt().getTime() + 2 * 60 * 1000);
            latestOtp.setExpiresAt(expiry);
            providerOtpDaoImpl.updateOtp(latestOtp);
            System.out.println("Step 3.1: Expiry timestamp was null. Set new expiry: " + expiry);
        }

        if (now.after(expiry)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "OTP has expired.", null));
            System.out.println("Step 3.2: OTP expired. Current time: " + now);
            otpSent = true;
            return null;
        }

        System.out.println("Step 4: OTP verified successfully. Updating DB...");
        latestOtp.setIsVerified(true);
        providerOtpDaoImpl.updateOtp(latestOtp);

        Provider provider = providerDaoImpl.findByEmail(sessionEmail);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedInProvider", provider);

        System.out.println("Step 5: OTP verified and provider logged in.");

        // Send JS success redirect via JSF page
        FacesContext.getCurrentInstance().getExternalContext()
            .getSessionMap().put("otpVerified", true); 
        otpSent = false; // reset
        return "ResetPassword.jsp?faces-redirect=true";
    }
        
    
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session != null) {
            session.invalidate(); // Ends the session
        }

        // Redirect to login page
        return "Login.jsf?faces-redirect=true";
    }
    

       
  
    // ✅ Getters and Setters
    public Provider getProvider() {
    	 if (provider == null) {
             provider = new Provider();
         }
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }


	public ProviderOtpDao getProviderOtpDao() {
		return providerOtpDaoImpl;
	}

	public void setProviderOtpDao(ProviderOtpDao providerOtpDao) {
		this.providerOtpDaoImpl = providerOtpDao;
	}
	
	public String getOtpCode() {
		return otpCode;
	}
	 public void setOtpCode(String otpCode) {
		 this.otpCode = otpCode;
	 }


	 public ProviderDao getProviderDaoImpl() {
		return providerDaoImpl;
	 }


	 public void setProviderDaoImpl(ProviderDao providerDaoImpl) {
		this.providerDaoImpl = providerDaoImpl;
	 }

	 public ProviderOtpDao getProviderOtpDaoImpl() {
		return providerOtpDaoImpl;
	 }

	 public void setProviderOtpDaoImpl(ProviderOtpDao providerOtpDaoImpl) {
		this.providerOtpDaoImpl = providerOtpDaoImpl;
	 }
	 
	 public String getEmail() {
		return email;
	 }
	 public void setEmail(String email) {
		this.email = email;
	 }
	 
	 public List<Provider> getProviderList() {
		return providerList;
	 }
	 public void setProviderList(List<Provider> providerList) {
		this.providerList = providerList;
	 }


	 public boolean isOtpSent() {
		return otpSent;
	 }


	 public void setOtpSent(boolean otpSent) {
		this.otpSent = otpSent;
	 }
}

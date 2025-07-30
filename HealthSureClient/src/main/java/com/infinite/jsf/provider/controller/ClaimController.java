package com.infinite.jsf.provider.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.infinite.jsf.insurance.model.Subscribe;
import com.infinite.jsf.provider.dao.ClaimDao;
import com.infinite.jsf.provider.dto.PendingOrDeniedClaimDTO;
import com.infinite.jsf.provider.model.ClaimHistory;
import com.infinite.jsf.provider.model.ClaimStatus;
import com.infinite.jsf.provider.model.Claims;
import com.infinite.jsf.provider.model.MedicalProcedure;
import com.infinite.jsf.provider.model.PrescribedMedicines;
import com.infinite.jsf.provider.model.Prescription;
import com.infinite.jsf.provider.model.ProcedureTest;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.recipient.model.Recipient;
import com.infinite.jsf.util.MailSend;

public class ClaimController {
	
	private static final Logger log = Logger.getLogger("com.infinite.jsf.provider.controller");
	
	private ClaimDao claimDao;
	
	private String recepientId;
	private Recipient recipient;
	private String message;
	private List<Subscribe> subscriptions;
	private List<MedicalProcedure> unclaimedProcedures;
	private List<MedicalProcedure> paginatedUnclaimedProcedures;
	private String amountClaimed;
	private List<PendingOrDeniedClaimDTO> pendingOrDeclinedClaim;
	private List<PendingOrDeniedClaimDTO> paginatedPendingOrDeclinedClaim;
	private int page = 0;
	private int pageSize = 5;
	private String sortField = "procedureId"; // default sort field
	private boolean ascending = true;
	
	//============Getter & Setter================//
	
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

	
	public void setPaginatedUnclaimedProcedures(List<MedicalProcedure> paginatedUnclaimedProcedures) {
		this.paginatedUnclaimedProcedures = paginatedUnclaimedProcedures;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<PendingOrDeniedClaimDTO> getPendingOrDeclinedClaim() {
		return pendingOrDeclinedClaim;
	}
	public void setPendingOrDeclinedClaim(List<PendingOrDeniedClaimDTO> pendingOrDeclinedClaim) {
		this.pendingOrDeclinedClaim = pendingOrDeclinedClaim;
	}
	public List<MedicalProcedure> getUnclaimedProcedures() {
		return unclaimedProcedures;
	}
	public void setUnclaimedProcedures(List<MedicalProcedure> unclaimedProcedures) {
		this.unclaimedProcedures = unclaimedProcedures;
	}
	public String getAmountClaimed() {
		return amountClaimed;
	}
	public void setAmountClaimed(String amountClaimed) {
		this.amountClaimed = amountClaimed;
	}
	public List<Subscribe> getSubscriptions() {
		return subscriptions;
	}
	public void setSubscriptions(List<Subscribe> subscriptions) {
		this.subscriptions = subscriptions;
	}
	public String getRecepientId() {
		return recepientId;
	}
	public void setRecepientId(String recepientId) {
		this.recepientId = recepientId;
	}
	public Recipient getRecipient() {
		return recipient;
	}
	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ClaimDao getClaimDao() {
		return claimDao;
	}
	public void setClaimDao(ClaimDao claimDao) {
		this.claimDao = claimDao;
	}

	
	public void sortBy(String field) {
	    if (field.equals(this.sortField)) {
	        this.ascending = !this.ascending; // toggle sort direction
	    } else {
	        this.sortField = field;
	        this.ascending = true; // default to ascending
	    }
	    page = 0; // reset to first page
	}

	//Searching unclaimed procedures
	
	public String searchUnclaimedProcedure() {
		unclaimedProcedures = claimDao.showUnclaimedProcedure();
		System.out.println("list of unclaimed procedure : " + unclaimedProcedures.size());
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		 httpSession.setAttribute("unclaimedProcedures", unclaimedProcedures);
		 log.info("Unclaimed Procedure Fetched.");
		return "ShowMedicalProcedureToClaim.jsp?faces-redirect=true";
	}
	
	//Getting paginated and shorted unclaimed procedure 
	
	public List<MedicalProcedure> getPaginatedUnclaimedProcedures() {
	    if (unclaimedProcedures == null) {
	        searchUnclaimedProcedure(); // lazy load
	    }

	    if (unclaimedProcedures == null || unclaimedProcedures.isEmpty()) {
	        return Collections.emptyList();
	    }

	    // Apply sorting using if-else
	    Comparator<MedicalProcedure> comparator;

	    if ("procedureId".equals(sortField)) {
	        comparator = Comparator.comparing(MedicalProcedure::getProcedureId);
	    } else if ("diagnosis".equals(sortField)) {
	        comparator = Comparator.comparing(MedicalProcedure::getDiagnosis, String.CASE_INSENSITIVE_ORDER);
	    } else if ("procedureDate".equals(sortField)) {
	        comparator = Comparator.comparing(MedicalProcedure::getProcedureDate);
	    } else if ("doctorName".equals(sortField)) {
	        comparator = Comparator.comparing(p -> p.getDoctor().getDoctorName(), String.CASE_INSENSITIVE_ORDER);
	    } else if ("hospitalName".equals(sortField)) {
	        comparator = Comparator.comparing(p -> p.getProvider().getHospitalName(), String.CASE_INSENSITIVE_ORDER);
	    } else {
	        comparator = Comparator.comparing(MedicalProcedure::getProcedureId); // default
	    }

	    if (!ascending) {
	        comparator = comparator.reversed();
	    }

	    List<MedicalProcedure> sortedList = new java.util.ArrayList<>(unclaimedProcedures);
	    Collections.sort(sortedList, comparator);

	    // Apply pagination
	    int fromIndex = page * pageSize;
	    if (fromIndex >= sortedList.size()) {
	        page = 0;
	        fromIndex = 0;
	    }

	    int toIndex = Math.min(fromIndex + pageSize, sortedList.size());
	    paginatedUnclaimedProcedures = sortedList.subList(fromIndex, toIndex);
	    return paginatedUnclaimedProcedures;
	}

	
	//=========================Pagination of searchUnclaimedProcedure ===========================
	
	
 
	public void nextPage() {
		
		if ((page + 1) * pageSize < unclaimedProcedures.size()) {		
			page++;
		}
	}
 
	public void previousPage() {
		if (page > 0) {
			page--;
		}
	}
 
	public boolean isHasNextPage() {
		
		return unclaimedProcedures != null && (page + 1) * pageSize < unclaimedProcedures.size();
	}
 
	public boolean isHasPreviousPage() {
		return page > 0;
	}
 
	public int getCurrentPage() {
		return page + 1;
	}
 
	public int getTotalPages() {
		
		if (unclaimedProcedures == null || unclaimedProcedures.isEmpty())
			return 0;
 
		return (int) Math.ceil((double) unclaimedProcedures.size() / pageSize);
 
	}
	
	//======================= Pagination End ==============================
	
	
	
	// Searching and viewing all the details of that procedure details like recipient, medical procedure, prescription, medicines and tests details all
	
	public String searchRecipient(MedicalProcedure procedure) {
		System.out.println("Recipient id : " + procedure.getRecipient().gethId());
		String recId = procedure.getRecipient().gethId();
	    recipient = claimDao.findByHid(recId);
	    if (recipient != null) {
//	        MedicalProcedure procedure = claimDao.getLatestProcedureByHid(recipient.gethId());

	        Prescription prescription = null;
	        List<PrescribedMedicines> medicines = null;
	        List<ProcedureTest> tests = null;

	        if (procedure != null) {
	            prescription = claimDao.getPrescriptionByProcedureId(procedure);

	            if (prescription != null) {
	                medicines = claimDao.getMedicinesByPrescription(prescription);
	                tests = claimDao.getTestsByPrescription(prescription);
	            }
	        }

	        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
	            .getExternalContext().getSession(true);

	        httpSession.setAttribute("recipient", recipient);
	        httpSession.setAttribute("procedure", procedure);
	        httpSession.setAttribute("prescription", prescription);
	        httpSession.setAttribute("medicines", medicines);
	        httpSession.setAttribute("tests", tests);
	        httpSession.setAttribute("recepientId", recId);
	        log.info("All the details of selected procedure  fetched and stored in session.");
	        return "searchRecipient.jsp?faces-redirect=true";
	    } else {
	    	log.error("Recipient Not found.");
	        message = "Recipient Not Found";
	        return null;
	    }
	}
	
	// fetching all the plans in which recipient is enrolled
	public String showPlans() {
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		this.recepientId = (String) httpSession.getAttribute("recepientId");
		this.subscriptions = claimDao.getActiveSubscriptionsByRecipient(recepientId);
		
		if(subscriptions != null && !subscriptions.isEmpty()) {
			System.out.println("Subscriptions found: " + subscriptions.size());
		httpSession.setAttribute("subscriptions", subscriptions);
		log.info("All Subscribe plans are fetched..");
		return "selectInsurancePlan.jsp?faces-redirect=true";
		}
		else {
			log.error("No active insurance found of recipient with id : " + recepientId + ".");
			message = "No active insurance plans found for this recipient.";
			return "selectInsurancePlan.jsp?faces-redirect=true";
		}
	}
	
	// after selecting plan redirected to initiate claim with some amount
	
	public String selectPlan(String subscribeId) {
//		System.out.println("Requested subscribeId: " + subscribeId);

		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
	    @SuppressWarnings("unchecked")
		List<Subscribe> allSubscriptions = (List<Subscribe>) httpSession.getAttribute("subscriptions");
	    
//	    Subscribe selectedSub = null;
//	    for (Subscribe s : allSubscriptions) {
//	        if (s.getSubscribeId().equals(subscribeId)) {
//	            selectedSub = s;
//	            break;
//	        }
//	    }

	    Subscribe selectedSub = allSubscriptions.stream()
	        .filter(s -> s.getSubscribeId().equals(subscribeId))
	        .findFirst()
	        .orElse(null);
	    
	    if (selectedSub != null) {
	    	httpSession.setAttribute("selectedSubscribe", selectedSub);
	    	log.info("Plan is selected for claim.");
	        return "InitiateClaim.jsp?faces-redirect=true";
	    } else {
	    	log.error("Plan not found.");
	        message = "Plan not found.";
	        return null;
	    }
	}
	
	// For filing claim application to get the insurance claimed by the provider and all validations are here for claiming
	public String submitClaim() {
	    HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
	            .getExternalContext().getSession(true);
	    FacesContext context = FacesContext.getCurrentInstance();
	    Recipient recipient = (Recipient) httpSession.getAttribute("recipient");
	    MedicalProcedure procedure = (MedicalProcedure) httpSession.getAttribute("procedure");
	    Subscribe subscribe = (Subscribe) httpSession.getAttribute("selectedSubscribe");
//	    Provider provider = (Provider) httpSession.getAttribute("provider");
	    Provider provider = new Provider();
	    provider.setProviderId("PROV001");
	    System.out.println("Provider is set");
	    System.out.println("Amount claimed : " + amountClaimed);
	    BigDecimal coverageAmount =BigDecimal.valueOf(subscribe.getRemainingCoverageAmount());
	    // Validate required data
	    if (recipient == null || procedure == null || subscribe == null || provider == null) {
	        message = "Missing data for claim submission. Please ensure all steps are completed.";
	        return null;
	    }
	    if (!amountClaimed.matches("^-?\\d+(\\.\\d+)?$")) {
	        context.addMessage("application:amountClaimed", new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Invalid Amount", "Please Enter Valid Amount to Claim."));
	        return null;
	    }
	    BigDecimal amount = new BigDecimal(amountClaimed); // inputValue is the user-provided value
	    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 ) {
	    	context.addMessage("application:amountClaimed", new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Invalid Amount", "Claimed amount must be positive number."));
	        return null;
	    }
	
	    if (amount.compareTo(coverageAmount) > 0 ) {
	    	context.addMessage("application:amountClaimed", new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Invalid Amount", "Claim Amount must be less than or equal to Remaining Coverage Amount."));

	    	return null;
	    }

	    try {
	        // Prepare Claims object
	        Claims claim = new Claims();
	        claim.setRecipient(recipient);
	        claim.setProcedure(procedure);
	        claim.setCoverage(subscribe);
	        claim.setProvider(provider);
	        claim.setAmountClaimed(amount);
	        claim.setAmountApproved(BigDecimal.ZERO);
	        claim.setClaimStatus(ClaimStatus.PENDING);
	        claim.setClaimDate(new java.util.Date());

	        // Prepare initial ClaimHistory
	        ClaimHistory history = new ClaimHistory();
	        history.setClaim(claim);
	        history.setDescription("Claim submitted by provider.");
	        history.setActionDate(new java.util.Date());

	        // Submit through EJB
	        Claims insertedClaim = claimDao.fileClaim(claim, history);
//	        System.out.println("Status" + insertedClaim);
	        if (insertedClaim != null) {
	        	httpSession.setAttribute("insertedClaims", insertedClaim);
	        	String subject = "Hi " +claim.getRecipient().getFirstName() + " " + claim.getRecipient().getLastName() + " Your Insurance claim is initialised ";
	        	String body = "Dear " + claim.getRecipient().getFirstName() + " " + claim.getRecipient().getLastName() + ",\n\n"
	                    + "We’re pleased to inform you that your insurance claim has been successfully initiated.\n\n"
	                    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
	                    + "📄 CLAIM SUMMARY\n"
	                    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
	                    + "• Claim ID         : " + insertedClaim.getClaimId() + "\n"
	                    + "• Procedure        : " + claim.getProcedure().getDiagnosis() + "\n"
	                    + "• Claimed Amount   : ₹" + claim.getAmountClaimed().toPlainString() + "\n"
	                    + "• Status           : " + claim.getClaimStatus().name() + "\n"
	                    + "• Submission Date  : " + claim.getClaimDate().toString() + "\n"
	                    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n"
	                    + "You will receive further updates via email once your claim has been reviewed and a decision is made.\n\n"
	                    + "If you have any questions or need assistance, feel free to contact our support team.\n\n"
	                    + "Warm regards,\n"
	                    + "HealthSure Claims Department";


	        	MailSend.sendInfo(claim.getRecipient().getEmail(), subject, body);
	        	log.info("Claim filed successfully for recipeint : " + claim.getRecipient().getFirstName());
	            message = "Claim filed successfully.";
	            return "ClaimSuccess.jsp?faces-redirect=true";
	        } else {
	        	log.error("Insurance claim failed for " + claim.getRecipient().getFirstName() + ".");
	            message = "Claim filing failed. Please try again.";
	            return null;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException
	                || (e.getMessage() != null && e.getMessage().contains("Duplicate entry"))) {
	        		log.error("A claim for this procedure " + procedure.getProcedureId() +" has already been submitted.");
	                message = "A claim for this procedure has already been submitted.";
	            } else {
	            	log.error("Error while submitting claim.");
	                message = "Error occurred while submitting claim.";
	            }
	        return null;
	    }
	}
	
	
	public String showPendingClaims() {
		pendingOrDeclinedClaim = claimDao.showPendingClaimsDao();
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		System.out.println("size : " + pendingOrDeclinedClaim.size());
		if(pendingOrDeclinedClaim != null && !pendingOrDeclinedClaim.isEmpty()) {
			
		 httpSession.setAttribute("pendingOrDeclinedClaim", pendingOrDeclinedClaim);
		 log.info("Pending or declined claims are fetched for updation.");
		 return "ShowPendingOrDeclinedClaims.jsp?faces-redirect=true";
		}
		else {
			log.error("No Pending or Declined insurance plans found.");
			message = "No Pending or Declined insurance plans found.";
			return "ShowPendingOrDeclinedClaims.jsp?faces-redirect=true";
		}
		 		
	}
	
	//=========================Pagination of searchUnclaimedProcedure ===========================
	private String sortField1 = "claimId"; // default sort field
	private boolean ascending1 = true;
	
	public String getSortField1() {
	    return sortField1;
	}

	public void setSortField1(String sortField1) {
	    this.sortField1 = sortField1;
	}

	public boolean isAscending1() {
	    return ascending1;
	}

	public void setAscending1(boolean ascending1) {
	    this.ascending1 = ascending1;
	}

	public void sortByPendingClaim(String field) {
	    if (field.equals(this.sortField1)) {
	        this.ascending1 = !this.ascending1;
	    } else {
	        this.sortField1 = field;
	        this.ascending1 = true;
	    }
	    page = 0;
	}

	public List<PendingOrDeniedClaimDTO> getPaginatedPendingOrDeclinedClaim() {
	    if (pendingOrDeclinedClaim == null) {
	        showPendingClaims(); // lazy load
	    }

	    if (pendingOrDeclinedClaim == null || pendingOrDeclinedClaim.isEmpty()) {
	        return Collections.emptyList();
	    }

	    Comparator<PendingOrDeniedClaimDTO> comparator;

	    if ("claimId".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getClaimId);
	    } else if ("procedureId".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getProcedureId);
	    } else if ("amountClaimed".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getAmountClaimed);
	    } else if ("amountApproved".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getAmountApproved);
	    } else if ("claimStatus".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getClaimStatus, String.CASE_INSENSITIVE_ORDER);
	    } else if ("description".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getDescription, String.CASE_INSENSITIVE_ORDER);
	    } else if ("actionDate".equals(sortField1)) {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getActionDate);
	    } else {
	        comparator = Comparator.comparing(PendingOrDeniedClaimDTO::getClaimId);
	    }

	    if (!ascending1) {
	        comparator = comparator.reversed();
	    }

	    List<PendingOrDeniedClaimDTO> sortedList = new ArrayList<>(pendingOrDeclinedClaim);
	    Collections.sort(sortedList, comparator);

	    int fromIndex = page * pageSize;
	    if (fromIndex >= sortedList.size()) {
	        page = 0;
	        fromIndex = 0;
	    }

	    int toIndex = Math.min(fromIndex + pageSize, sortedList.size());
	    paginatedPendingOrDeclinedClaim = sortedList.subList(fromIndex, toIndex);
	    return paginatedPendingOrDeclinedClaim;
	}

	 
		public void nextPage1() {
			
			if ((page + 1) * pageSize < pendingOrDeclinedClaim.size()) {		
				page++;
			}
		}
	 
		public void previousPage1() {
			if (page > 0) {
				page--;
			}
		}
	 
		public boolean isHasNextPage1() {
			
			return pendingOrDeclinedClaim != null && (page + 1) * pageSize < pendingOrDeclinedClaim.size();
		}
	 
		public boolean isHasPreviousPage1() {
			return page > 0;
		}
	 
		public int getCurrentPage1() {
			return page + 1;
		}
	 
		public int getTotalPages1() {
			
			if (pendingOrDeclinedClaim == null || pendingOrDeclinedClaim.isEmpty())
				return 0;
	 
			return (int) Math.ceil((double) pendingOrDeclinedClaim.size() / pageSize);
	 
		}
		
		//======================= Pagination End ==============================
	
	
	public String editClaim(String claimId) {
		Claims claim = claimDao.findByClaimId(claimId);
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);	
		httpSession.setAttribute("recipient", claim.getRecipient());
        httpSession.setAttribute("procedure", claim.getProcedure());
        httpSession.setAttribute("prescription", claim.getProcedure().getPrescriptions());
        httpSession.setAttribute("selectedSubscribe", claim.getCoverage());
        httpSession.setAttribute("claimId", claimId);
        
        
//        httpSession.setAttribute("medicines", claim.getProcedure);
        httpSession.setAttribute("tests", claim.getProcedure().getTests());
        log.info("All details are fetched for update claim.");
		 return "UpdateClaim.jsp?faces-redirect=true";
		
	}
	
	public String updateClaim() {
		
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);	
		FacesContext context = FacesContext.getCurrentInstance();
		 Recipient recipient = (Recipient) httpSession.getAttribute("recipient");
		    MedicalProcedure procedure = (MedicalProcedure) httpSession.getAttribute("procedure");
		    Subscribe subscribe = (Subscribe) httpSession.getAttribute("selectedSubscribe");
		    Provider provider = new Provider();
		    provider.setProviderId("PROV001");
		String claimId = (String) httpSession.getAttribute("claimId");
		Claims claim = claimDao.findByClaimId(claimId);
		if(claim != null) {
			log.info("Claim is found for updation");
		}
		ClaimHistory history = new ClaimHistory();
        history.setClaim(claim);
        history.setDescription("Claim updated by provider.");
        history.setActionDate(new java.util.Date());
        
        BigDecimal coverageAmount =BigDecimal.valueOf(subscribe.getRemainingCoverageAmount());
	    // Validate required data
	    if (recipient == null || procedure == null || subscribe == null || provider == null) {
	        message = "Missing data for claim submission. Please ensure all steps are completed.";
	        return null;
	    }
	    if (!amountClaimed.matches("^-?\\d+(\\.\\d+)?$")) {
	        context.addMessage("updation:amountUpdated", new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Invalid Amount", "Please Enter Valid Amount to Claim."));

	        return null;
	    }
	    BigDecimal amount = new BigDecimal(amountClaimed); // inputValue is the user-provided value
	    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0 ) {
	    	context.addMessage("updation:amountUpdated", new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Invalid Amount", "Claimed amount must be positive number."));
	        return null;
	    }
	
	    if (amount.compareTo(coverageAmount) > 0 ) {
	    	context.addMessage("updation:amountUpdated", new FacesMessage(FacesMessage.SEVERITY_ERROR,
	                "Invalid Amount", "Claim Amount must be less than or equal to Remaining Coverage Amount."));

	    	return null;
	    }

	    
		Claims updatedClaim = claimDao.updateClaim(claimId, history, amount);
		if(updatedClaim != null) {
			httpSession.setAttribute("updatedClaim", updatedClaim);
			httpSession.setAttribute("history", history);
	    	String subject = "Hi " +claim.getRecipient().getFirstName() + " " + claim.getRecipient().getLastName() + " Your Insurance claim is updated ";
	    	String body = "Dear " + claim.getRecipient().getFirstName() + " " + claim.getRecipient().getLastName() + ",\n\n"
	    		    + "We are pleased to inform you that your insurance claim has been successfully updated.\n\n"
	    		    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
                    + "📄 CLAIM SUMMARY\n"
                    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n"
	    		    + "• Claim ID: " + updatedClaim.getClaimId() + "\n"
	    		    + "• Procedure: " + claim.getProcedure().getDiagnosis() + "\n"
	    		    + "• Claimed Amount: ₹" + amount + "\n"
	    		    + "• Status: " + claim.getClaimStatus().name() + "\n"
	    		    + "• Re-claim Date & Time: " + history.getActionDate().toString() + "\n"
	    		    + "• Submission Date & Time: " + claim.getClaimDate().toString() + "\n"
	    		    + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n"
	    		    + "You will be notified via email once your claim is reviewed and a decision has been made.\n\n"
	    		    + "For any queries or updates, please reach out to our support team.\n\n"
	    		    + "Warm regards,\n"
	    		    + "HealthSure Claims Department";
	
	    	MailSend.sendInfo(claim.getRecipient().getEmail(), subject, body);
	    	log.info("Claim updated successfully.");
	        message = "Claim updated successfully.";
	        return "UpdationComplete.jsp?faces-redirect=true";
	    } else {
	    	log.error("Claim updation failed for claim id : " + claim.getClaimId());
	        message = "Claim updation failed. Please try again.";
	        return null;
	    }
	}
}

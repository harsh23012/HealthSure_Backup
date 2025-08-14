
/**
* Copyright © 2025 Infinite Computer Solution. All rights reserved.
*/

/**
 * Package: com.infinite.jsf.provider.controller
 *
 * This package contains JSF controller classes that manage provider-side functionalities
 * within the application.
 * 
 * Specifically, it includes logic for searching, filtering, sorting, and paginating
 * payment history records based on criteria such as ID type, amount, and date range.
 * These controllers interact with the DAO layer to fetch and process data, and
 * communicate with JSF pages to render dynamic and user-driven views.
 */

package com.infinite.jsf.provider.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.infinite.jsf.provider.dao.PaymentHistoryDao;
import com.infinite.jsf.provider.daoImpl.PaymentHistoryDaoImpl;
import com.infinite.jsf.provider.model.PaymentHistory;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.recipient.model.Recipient;

/**
 * Controller for managing payment history search and display in JSF.
 * 
 * Features:
 * - Supports filtering by provider ID, recipient ID, payment ID, amount, and date range.
 * - Includes validation for search inputs and formats.
 * - Handles pagination and sorting of results.
 * - Controls visibility of the results table based on user actions.
 * 
 * Used in the JSF Payment History page to provide dynamic, user-driven search functionality.
 */

public class PaymentHistoryController {

	private PaymentHistory paymentHistory;
	private PaymentHistoryDao payHistoryDao;
	private List<PaymentHistory> paymentHistories;
	private Provider providers;
	private Recipient recipient;

	// Search fields
	private String searchType = null;
	private String searchValue = null;
	private Double searchAmount;
	private Date searchDateFrom;
	private Date searchDateTo;

	// Validation message
	private String validationMessage;

	// Pagination and sorting
	private int pageSize = 5;
	private int currentPage = 1;
	private String sortField = "paymentDate";
	private boolean sortAscending = true;
	private boolean showTable = false;

	public String searchCombined() {

		validationMessage = null;
		showTable = false;

		String valueUpper;
		if (searchValue != null) {
			valueUpper = searchValue.toUpperCase().trim();
		} else {
			valueUpper = null;
		}

		if (searchDateFrom != null && searchDateTo == null) {
			validationMessage = "Please select toDate.";
			addFacesMessage(validationMessage);
			return null;
		}
		if (searchDateFrom == null && searchDateTo != null) {
			validationMessage = "Please select fromDate";
			addFacesMessage(validationMessage);
			return null;
		}
		// Validation block
		if (searchAmount != null && searchAmount < 0) {
			validationMessage = "Amount must be non-negative.";
		} else if (searchDateFrom != null && searchDateTo != null && searchDateFrom.after(searchDateTo)) {
			validationMessage = "From Date must be before or equal to To Date.";
		} else if ((searchType == null || searchType.isEmpty()) && valueUpper != null && !valueUpper.isEmpty()) {
			validationMessage = "Please select a Search Type for the entered ID.";
		} else if (!valueUpper.isEmpty() && valueUpper != null && !valueUpper.matches("^[a-zA-Z0-9]+$")) {
			validationMessage = "ID must be alphanumeric.";
		} else if (searchType != null && !searchType.isEmpty() && (valueUpper == null || valueUpper.isEmpty())) {
			validationMessage = "Please enter an ID value for the selected Search Type.";

		} else if (searchType != null && !searchType.isEmpty() && valueUpper != null && !valueUpper.isEmpty()) {
			switch (searchType) {
			case "providerId":
				if (!valueUpper.startsWith("PROV")) {
					validationMessage = "Provider ID must start with 'PROV'.";
				}
				break;
			case "recipientId":
				if (!valueUpper.startsWith("HID")) {
					validationMessage = "Recipient ID must start with 'HID'.";
				}
				break;
			case "paymentId":
				if (!valueUpper.startsWith("PAY")) {
					validationMessage = "Payment ID must start with 'PAY'.";
				}
				break;
			}
		}

		if (validationMessage != null) {
			addFacesMessage(validationMessage);
			return null;
		}

		// Check if no filters are selected
		boolean noFilters = (searchType == null || searchType.isEmpty() || valueUpper == null || valueUpper.isEmpty())
				&& searchAmount == null && searchDateFrom == null && searchDateTo == null;

		if (noFilters) {
			validationMessage = "Please select at least one filter to search.";
			addFacesMessage(validationMessage);
			return null;
		}

		try {
			// Fetch all payment history records from the DAO
			List<PaymentHistory> allPayments = payHistoryDao.showPaymentHistory();

			// Apply filters using Java Streams
			paymentHistories = allPayments.stream()

					// Filter based on search type and value (providerId, recipientId, or paymentId)
					.filter(p -> {
						if (searchType != null && valueUpper != null && !valueUpper.isEmpty()) {
							switch (searchType) {
							case "providerId":
								// Check if provider exists and matches the search value
								return p.getProvider() != null
										&& p.getProvider().getProviderId().equalsIgnoreCase(valueUpper);
							case "recipientId":
								// Check if recipient exists and matches the search value
								return p.getRecipient() != null
										&& p.getRecipient().gethId().equalsIgnoreCase(valueUpper);
							case "paymentId":
								// Check if payment ID matches the search value
								return p.getPaymentId().equalsIgnoreCase(valueUpper);
							}
						}
						// If no search type or value is provided, include all records
						return true;
					})

					// Filter by payment amount if specified
					.filter(p -> searchAmount == null || p.getAmount().equals(searchAmount))

					// Filter by payment date range
					.filter(p -> {
						// Remove time part from payment date for accurate comparison
						Date paymentDate = stripTime(p.getPaymentDate());

						if (searchDateFrom != null && searchDateTo != null) {
							// Normalize date range by stripping time
							searchDateFrom = stripTime(searchDateFrom);
							searchDateTo = stripTime(searchDateTo);

							// Include payments within the date range
							return !paymentDate.before(searchDateFrom) && !paymentDate.after(searchDateTo);
						} else if (searchDateFrom != null) {
							// Include payments on or after the start date
							return !paymentDate.before(searchDateFrom);
						} else if (searchDateTo != null) {
							// Include payments on or before the end date
							return !paymentDate.after(searchDateTo);
						}

						// If no date filters are provided, include all records
						return true;
					})

					// Collect the filtered results into a list
					.collect(Collectors.toList());

			currentPage = 1;
			showTable = true;

			validationMessage = paymentHistories.isEmpty() ? "No records found for the selected filters."
					: "Search completed successfully.";
			addFacesMessage(validationMessage);

		} catch (Exception e) {
			e.printStackTrace();
			paymentHistories = new ArrayList<>();
			validationMessage = "An error occurred while searching.";
			addFacesMessage(validationMessage);
		}

		return null;
	}

	private Date stripTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// Reset filters and reload full list
	public String resetFilters() {
		searchType = null;
		searchValue = null;
		searchAmount = null;
		searchDateFrom = null;
		searchDateTo = null;
		validationMessage = null;
		showTable = false; // Hide the table

		try {
			paymentHistories = new ArrayList<>(); // Clear the list
			currentPage = 1;
			validationMessage = "Filters reset. Please apply filters and click Search.";
			addFacesMessage(validationMessage);
		} catch (Exception e) {
			e.printStackTrace();
			paymentHistories = new ArrayList<>();
			validationMessage = "Failed to reset filters.";
			addFacesMessage(validationMessage);
		}
		return null;
	}

	// sortBy arrow
	/**
	 * Sorts the list of entities (e.g., pharmacies, insurance plans, etc.) in
	 * ascending order based on the specified field. This method is typically used
	 * to organize data in UI tables for better readability and user experience.
	 *
	 * @param field The name of the field by which the list should be sorted (e.g.,
	 *              "name", "createdDate").
	 */

	public void sortByAsc(String field) {
		if (!field.equals(sortField) || !sortAscending) {
			// If this is a new field or the current order is not ascending, update sort
			sortField = field;
			sortAscending = true;

//			updatePaginatedList();
		}
		// If already sorting ascending on this field, you may skip or re-apply
	}

	/**
	 * Sorts the list of entities (e.g., pharmacies, insurance plans, etc.) in
	 * decending order based on the specified field. This method is typically used
	 * to organize data in UI tables for better readability and user experience.
	 *
	 * @param field The name of the field by which the list should be sorted (e.g.,
	 *              "name", "createdDate").
	 */

	public void sortByDesc(String field) {
		if (!field.equals(sortField) || sortAscending) {
			// If this is a new field or the current order is ascending, update sort
			sortField = field;
			sortAscending = false;

//			updatePaginatedList();
		}
	}

	private boolean isSameDay(Date d1, Date d2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(d1).equals(sdf.format(d2));
	}

	private void addFacesMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, message, null));
	}

	// Pagination
	public List<PaymentHistory> getPagedPaymentHistories() {
		try {
			// Step 1: Sort the full list of payment histories using a custom sorting method
			List<PaymentHistory> sortedList = sortPaymentHistories(paymentHistories);

			// Step 2: Calculate the starting index of the current page
			int fromIndex = (currentPage - 1) * pageSize;

			// Step 3: Calculate the ending index, ensuring it doesn't exceed the list size
			int toIndex = Math.min(fromIndex + pageSize, sortedList.size());

			// Step 4: Return the sublist representing the current page
			return sortedList.subList(fromIndex, toIndex);
		} catch (Exception e) {
			// If any error occurs (e.g., index out of bounds), print the stack trace
			e.printStackTrace();

			// Return an empty list to avoid breaking the UI
			return new ArrayList<>();
		}
	}

	public void nextPage() {
		// Check if there are more records beyond the current page
		if (currentPage * pageSize < paymentHistories.size()) {
			// Increment the page number to move forward
			currentPage++;
		}
	}

	public void previousPage() {
		// Ensure we don't go below page 1
		if (currentPage > 1) {
			// Decrement the page number to move backward
			currentPage--;
		}
	}

	public int getTotalPages() {
		// Divide total records by page size and round up to get total pages
		return (int) Math.ceil((double) paymentHistories.size() / pageSize);
	}

	// Sorting
	public void sortBy(String field) {
		// Check if the field passed in is the same as the current sort field
		if (sortField.equals(field)) {
			// If yes, toggle the sort direction (ascending ↔ descending)
			// This allows users to click the same column header to reverse the sort order
			sortAscending = !sortAscending;
		} else {
			// If a new field is selected for sorting:
			// Update the sort field to the newly selected one
			sortField = field;

			// Reset sort direction to ascending by default
			// This ensures consistent behavior when switching columns
			sortAscending = true;
		}
	}

	private List<PaymentHistory> sortPaymentHistories(List<PaymentHistory> list) {
		// Check if the input list is null or empty
		// If so, return an empty list to avoid errors
		if (list == null || list.isEmpty()) {
			return new ArrayList<>();
		}

		// Sort the list using a lambda comparator
		list.sort((p1, p2) -> {
			// Retrieve the value of the selected sort field from the first object
			Comparable val1 = getFieldValue(p1, sortField);

			// Retrieve the value of the selected sort field from the second object
			Comparable val2 = getFieldValue(p2, sortField);

			// If either value is null, treat them as equal (no sorting)
			if (val1 == null || val2 == null)
				return 0;

			// Compare the two values based on the sort direction
			// If ascending, compare normally: val1.compareTo(val2)
			// If descending, reverse the comparison: val2.compareTo(val1)
			return sortAscending ? val1.compareTo(val2) : val2.compareTo(val1);
		});

		// Return the sorted list
		return list;
	}

	private Comparable getFieldValue(PaymentHistory p, String field) {
		// Use a switch-case to determine which field to extract based on the field name
		switch (field) {

		// Case 1: Sort by payment date
		case "paymentDate":
			// Return the payment date (assumed to be a Comparable like java.util.Date)
			return p.getPaymentDate();

		// Case 2: Sort by payment ID
		case "paymentId":
			// Return the unique payment ID (e.g., String or Long)
			return p.getPaymentId();

		// Case 3: Sort by payment amount
		case "amount":
			// Return the amount paid (e.g., BigDecimal or Double)
			return p.getAmount();

		// Case 4: Sort by payment status
		case "paymentStatus":
			// Return the status of the payment (e.g., "Completed", "Pending")
			return p.getPaymentStatus();

		// Case 5: Sort by recipient's HId (nested object access)
		case "payment.recipient.hId":
			// Check if recipient object is not null, then return its HId
			return p.getRecipient() != null ? p.getRecipient().gethId() : null;

		// Case 6: Sort by provider's ID (nested object access)
		case "payment.providers.provider_id":
			// Check if providers object is not null, then return its provider ID
			return p.getProvider() != null ? p.getProvider().getProviderId() : null;

		// Default case: If field name doesn't match any known case, return null
		default:
			return null;
		}
	}

	// Getters and Setters
	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public Date getSearchDateFrom() {
		return searchDateFrom;
	}

	public void setSearchDateFrom(Date searchDateFrom) {
		this.searchDateFrom = searchDateFrom;
	}

	public Date getSearchDateTo() {
		return searchDateTo;
	}

	public void setSearchDateTo(Date searchDateTo) {
		this.searchDateTo = searchDateTo;
	}

	public List<PaymentHistory> getPaymentHistories() {
		return paymentHistories;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public String getSortField() {
		return sortField;
	}

	public boolean isSortAscending() {
		return sortAscending;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}

	public PaymentHistory getPaymentHistory() {
		return paymentHistory;
	}

	public void setPaymentHistory(PaymentHistory paymentHistory) {
		this.paymentHistory = paymentHistory;
	}

	public PaymentHistoryDao getPayHistoryDao() {
		return payHistoryDao;
	}

	public void setPayHistoryDao(PaymentHistoryDao payHistoryDao) {
		this.payHistoryDao = payHistoryDao;
	}

	public Provider getProviders() {
		return providers;
	}

	public void setProviders(Provider providers) {
		this.providers = providers;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}

	public void setPaymentHistories(List<PaymentHistory> paymentHistories) {
		this.paymentHistories = paymentHistories;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}

	public boolean isShowTable() {
		return showTable;
	}

	public Double getSearchAmount() {
		return searchAmount;
	}

	public void setSearchAmount(Double searchAmount) {
		this.searchAmount = searchAmount;
	}

	public void setShowTable(boolean showTable) {
		this.showTable = showTable;
	}

}
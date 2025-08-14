
/**
* Copyright © 2025 Infinite Computer Solution. All rights reserved.
*/


/**
 * Package: com.infinite.jsf.provider.controller
 *
 * This package contains JSF managed beans and controller classes responsible for
 * handling provider-side operations in the application.
 * 
 * Specifically, it includes logic for searching, filtering, sorting, and paginating
 * claim history records based on various criteria such as ID type, status, and date range.
 * These controllers serve as the bridge between the JSF UI and the backend service or DAO layer.
 */



package com.infinite.jsf.provider.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.infinite.jsf.provider.dao.ClaimHistorySearchDao;
import com.infinite.jsf.provider.daoImpl.ClaimHistorySearchDaoImpl;
import com.infinite.jsf.provider.model.ClaimHistory;
import com.infinite.jsf.provider.model.ClaimStatus;
import com.infinite.jsf.provider.model.Claims;
import com.infinite.jsf.provider.model.Provider;
import com.infinite.jsf.recipient.model.Recipient;


/**
 * Controller for managing claim history search functionality.
 * Handles filtering by ID type, status, and date range.
 * Supports pagination, sorting, and dynamic table visibility.
 * Used in JSF pages to display and interact with claim history records.
 */

public class ClaimHistorySearchController {

	private ClaimHistory claimHistory;
	private ClaimHistorySearchDao cleamsHistorySearchDao = new ClaimHistorySearchDaoImpl();

	private List<ClaimHistory> claimHistoryList = null;;
	private List<ClaimHistory> filteredList;
	private List<ClaimHistory> paginatedList;

	private Provider providers;
	private Recipient recipient;
	private Claims claim;
	
	private String searchType;
	private String searchValue;
	private String status;
	private String hid;
	private String claimId;
	private Date startDate;
	private Date endDate;
	private Date fromDate;
	private Date toDate;
	private int pageSize = 5;
	private int currentPage = 0;
	
	
	//private boolean showTable = false;

	//public boolean isShowTable() {
	    //return showTable;
	//}

	//public void setShowTable(boolean showTable) {
	    //this.showTable = showTable;
	//}

	
	// Sorting fields

	// This variable stores the name of the field by which data should be sorted
	// (e.g., "pharmacyId", "date", etc.)
	private String sortField;

	// This boolean flag indicates the sorting order: true for ascending, false for
	// descending
	private boolean ascending = true;

	public ClaimHistorySearchController() {
		// Fetches all claim history records from the database using the DAO (Data
		// Access Object)
		//claimHistoryList = cleamsHistorySearchDao.findAllClaimsHistory();

		// Initializes the filtered list to null; this will later hold search results
		// based on user filters
		filteredList = null;

		// Updates the paginated list to reflect the current state of claimHistoryList
		//updatePaginatedList();
	}

	public void nextPage() {
		// Retrieves the currently sorted list of claim history records
		List<ClaimHistory> sourceList = getSortedList();

		// Checks if there is another page available beyond the current one
		// (i.e., if moving to the next page won't exceed the total number of records)
		if ((currentPage + 1) * pageSize < sourceList.size()) {
			// Increments the current page number to move to the next page
			currentPage++;

			// Updates the paginated list to reflect the new page's data
			updatePaginatedList();
		}
	}

	public void previousPage() {
		// Checks if the current page is greater than 0 (i.e., not already on the first
		// page)
		if (currentPage > 0) {
			// Decrements the current page number to move to the previous page
			currentPage--;

			// Updates the paginated list to reflect the new page's data
			updatePaginatedList();
		}
	}

	private void updatePaginatedList() {
		// Retrieves the sorted list of claim history records based on current sort
		// field and order
		List<ClaimHistory> sourceList = getSortedList();

		// Calculates the starting index for the current page
		int fromIndex = currentPage * pageSize;

		// Calculates the ending index for the current page, ensuring it doesn't exceed
		// the list size
		int toIndex = Math.min(fromIndex + pageSize, sourceList.size());

		// Extracts the sublist of records for the current page and assigns it to
		// paginatedList
		paginatedList = sourceList.subList(fromIndex, toIndex);
	}

	private List<ClaimHistory> getSortedList() {
		// Use filteredList if it's available (i.e., user applied filters), otherwise
		// use the full list
		List<ClaimHistory> sourceList = (filteredList != null) ? filteredList : claimHistoryList;

		// If a sort field is selected (like "date" or "status"), then sort the list
		if (sortField != null && !sortField.isEmpty()) {
			// Make a copy of the list so we don't change the original one
			sourceList = new ArrayList<>(sourceList);

			// Sort the list using the selected field and order (ascending or descending)
			sourceList.sort((a, b) -> {
				// Get the values of the field to compare from both records
				Comparable valA = extractValue(a, sortField);
				Comparable valB = extractValue(b, sortField);

				// If either value is missing, treat them as equal
				if (valA == null || valB == null)
					return 0;

				// Compare values based on ascending or descending order
				return ascending ? valA.compareTo(valB) : valB.compareTo(valA);
			});
		}

		// Return the sorted (or original) list
		return sourceList;
	}

	public void sortBy(String field) {
		// If the user clicked the same column again, reverse the sort order (toggle
		// ascending/descending)
		if (field.equals(this.sortField)) {
			ascending = !ascending; // true becomes false, false becomes true
		} else {
			// If the user clicked a new column, update the sort field and reset to
			// ascending order
			sortField = field;
			ascending = true;
		}

		// Reset to the first page whenever sorting changes
		currentPage = 0;

		// Refresh the data to reflect the new sort order and page
		updatePaginatedList();
	}

	private Comparable extractValue(Object obj, String fieldPath) {
		try {
			// Split the field path by "." to handle nested fields like "provider.name"
			String[] fields = fieldPath.split("\\.");

			// Start with the original object (e.g., a ClaimHistory record)
			Object value = obj;

			// Loop through each field name in the path
			for (String field : fields) {
				// Get the field from the current object's class using reflection
				Field f = value.getClass().getDeclaredField(field);

				// Allow access to private fields
				f.setAccessible(true);

				// Get the value of the field from the current object
				value = f.get(value);
			}

			// Return the final value as a Comparable (so it can be sorted)
			return (Comparable) value;
		} catch (Exception e) {
			// If anything goes wrong (like field not found), return null
			return null;
		}
	}

	public boolean isHasNextPage() {
		// Step 1: Get the full list of claim history records that are already sorted
		List<ClaimHistory> sourceList = getSortedList();

		// Step 2: Calculate the starting index of the next page
		// Example: If currentPage = 1 and pageSize = 10, then (1 + 1) * 10 = 20
		// This means the next page would start at index 20

		// Step 3: Compare the next page's starting index with the total number of
		// records
		// If the next page's index is less than the total size, it means there are more
		// records to show
		// Example: If sourceList.size() = 23, then 20 < 23 → true (next page exists)
		return (currentPage + 1) * pageSize < sourceList.size();
	}

	public boolean isHasPreviousPage() {
		// Check if the current page number is greater than 0
		// If yes, it means there is a previous page to go back to
		return currentPage > 0;
	}

	public int getTotalPages() {
		// Step 1: Get the full list of claim history records that are already sorted
		List<ClaimHistory> sourceList = getSortedList();

		// Step 2: Calculate total pages using ceiling division
		// Convert list size to double and divide by pageSize to get total pages
		// Use Math.ceil to round up, because even a partial page counts as a full page
		// Cast the result to int since the method returns an integer
		return (int) Math.ceil((double) sourceList.size() / pageSize);
	}

	public List<ClaimHistory> findAllClaimHistorycontroller() {
		// Step 1: Fetch all claim history records from the DAO
		// This retrieves the complete list of claims from the database
		claimHistoryList = cleamsHistorySearchDao.findAllClaimsHistory();

		// Step 2: Clear any previously applied filters
		// This ensures we're working with the full list, not a filtered subset
		filteredList = null;

		// Step 3: Reset pagination to the first page
		// This is important because we're loading fresh data
		currentPage = 0;

		// Step 4: Update the paginated list based on the current page and page size
		// This prepares the subset of data to be displayed on the UI
		updatePaginatedList();

		// Step 5: Return the paginated list to be shown in the JSF page
		return paginatedList;
	}

	public void searchClaimHistoryByStatus(String status) {
		// Step 1: Reset all filters, pagination, and lists to their initial state
		resetmethod();

		// Step 2: Fetch all claim history records from the database using DAO
		claimHistoryList = cleamsHistorySearchDao.findAllClaimsHistory();

		// Step 3: Declare a variable to hold the status to filter by
		ClaimStatus statusToFilter;

		// Step 4: Check if the input status is not null
		if (status != null) {
			// Step 5: Convert the input status string to uppercase and match it with the
			// ClaimStatus enum
			statusToFilter = ClaimStatus.valueOf(status.toUpperCase());

			// Step 6: Filter the full claim history list based on the selected status
			// Only include records where the claim's status matches the input status
			filteredList = claimHistoryList.stream().filter(c -> c.getClaim().getClaimStatus() == statusToFilter)
					.collect(Collectors.toList());
		} else {
			// Step 7: If no status is provided, set the filtered list to an empty list
			filteredList = new ArrayList<>();
		}

		// Step 8: Reset pagination to the first page
		currentPage = 0;

		// Step 9: Update the paginated list based on the filtered results
		updatePaginatedList();
	}

	public void resetmethod() {
		paginatedList = null;
		searchType = null;
		searchValue = null;
		fromDate = null;
		toDate = null;
	}

	public void searchByIdType() {
		// Step 1: Load all claim history records from the DAO
		claimHistoryList = cleamsHistorySearchDao.findAllClaimsHistory();
		System.out.println(searchType);
		System.out.println(searchType.isEmpty());
		
		if ((searchType == null || searchType.isEmpty() )&& (searchValue == null || searchValue.isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Please select a search type and enter a valid ID.", null));
			filteredList = new ArrayList<>();
			updatePaginatedList();
			return;
		}

		// Step 2: If search value is selected but searchType is missing
		if (searchType == null && searchType.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a search type", null));
			filteredList = new ArrayList<>();
			updatePaginatedList();
			return;
		}

		

		// Step 3: If search type is selected but value is missing
		if ((searchType != null || !searchType.isEmpty()) && (searchValue == null || searchValue.isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter a valid ID.", null));
			filteredList = new ArrayList<>();
			updatePaginatedList();
			return;
		}

		// Step 4: If value is entered but search type is missing
		if ((searchType == null || searchType.isEmpty()) && (searchValue != null || !searchValue.isEmpty())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select a search type", null));
			filteredList = new ArrayList<>();
			updatePaginatedList();
			return;
		}

		// Step 5: Validate format of search value
		// Must be alphanumeric and match one of the expected patterns: PROV001, HID001,
		// CL002
		if (!searchValue.matches("(?i)^[a-z0-9]+$") || !(searchValue.matches("(?i)^prov\\d+$")
				|| searchValue.matches("(?i)^h\\d+$") || searchValue.matches("(?i)^CLAIM\\d+$"))) {

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"ID must be alphanumeric and follow a valid format like PROV001 or HID001 or CL002.", null));
			filteredList = new ArrayList<>();
			updatePaginatedList();
			return;
		}
		
		// Step 5.1: Ensure the ID format matches the selected search type
		boolean formatMismatch =
		    (searchType.equals("providerId") && !searchValue.matches("(?i)^prov\\d+$")) ||
		    (searchType.equals("claimId") && !searchValue.matches("(?i)^CLAIM\\d+$")) ||
		    (searchType.equals("recipientId") && !searchValue.matches("(?i)^h\\d+$"));

		if (formatMismatch) {
		    FacesContext.getCurrentInstance().addMessage(null,
		        new FacesMessage(FacesMessage.SEVERITY_ERROR,
		            "The entered ID does not match the selected search type. Please check the format.", null));
		    filteredList = new ArrayList<>();
		    updatePaginatedList();
		    return;
		}

		// Step 6: Filter the claim history list based on selected ID type
		filteredList = claimHistoryList.stream().filter(c -> {
			switch (searchType) {
			case "providerId":
				// Match provider ID if provider object is not null
				return c.getClaim().getProvider() != null
						&& c.getClaim().getProvider().getProviderId().equalsIgnoreCase(searchValue);

			case "claimId":
				// Match claim ID if it's not null
				return c.getClaim().getClaimId() != null && c.getClaim().getClaimId().equalsIgnoreCase(searchValue);

			case "recipientId":
				// Match recipient HId if recipient object is not null
				return c.getClaim().getRecipient() != null
						&& c.getClaim().getRecipient().gethId().equalsIgnoreCase(searchValue);

			default:
				// If search type is unknown, exclude the record
				return false;
			}
		}).collect(Collectors.toList());

		// Step 7: Show appropriate message based on result
		if (filteredList.isEmpty()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No data found for " + searchType + " with ID: " + searchValue, null));
		} else {
			FacesContext.getCurrentInstance().addMessage("claimForm:successmessage",
				    new FacesMessage(FacesMessage.SEVERITY_INFO, "Search completed successfully.", null));


		}

		// Step 8: Reset pagination and update the paginated list
		currentPage = 0;
		updatePaginatedList();
	}

	public String filterByDateRange() {

		// Debugging output to verify selected dates
		System.out.println("=============");
		System.out.println("todate" + toDate);
		System.out.println("formdote" + fromDate);

		// Case 1: Both dates are not selected
		if (fromDate == null && toDate == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select both From and To date.", null));
			return null;
		}

		// Case 2: From date is selected but To date is missing
		if (fromDate != null && toDate == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select To date.", null));
			return null;
		}

		// Case 3: To date is selected but From date is missing
		if (fromDate == null && toDate != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select from date.", null));
			return null;
		}

		// Case 4: From date is after To date — invalid range
		if (fromDate.after(toDate)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "From date cannot be after To date.", null));
			return null;
		}

		// ✅ Valid date range — proceed to filter the list
		filteredList = claimHistoryList.stream().filter(ch -> {
			// Skip entries with null action dates or null range bounds
			if (ch.getActionDate() == null || fromDate == null || toDate == null)
				return false;

			// Strip time from all dates to compare only the date part
			Date actionDateOnly = stripTime(ch.getActionDate());
			Date fromDateOnly = stripTime(fromDate);
			Date toDateOnly = stripTime(toDate);

			// Include only those entries where actionDate is within the range [fromDate,
			// toDate]
			return !actionDateOnly.before(fromDateOnly) && !actionDateOnly.after(toDateOnly);
		}).collect(Collectors.toList());

		// Reset pagination to the first page after filtering
		currentPage = 0;

		// Refresh the paginated list to reflect the filtered results
		updatePaginatedList();

		// Return null to stay on the same page (JSF navigation)
		return null;
	}

	private Date stripTime(Date date) {
		// Create a Calendar instance to manipulate the date
		Calendar cal = Calendar.getInstance();

		// Set the calendar's time to the input date
		cal.setTime(date);

		// Reset the hour of the day to 0 (midnight)
		cal.set(Calendar.HOUR_OF_DAY, 0);

		// Reset the minutes to 0
		cal.set(Calendar.MINUTE, 0);

		// Reset the seconds to 0
		cal.set(Calendar.SECOND, 0);

		// Reset the milliseconds to 0
		cal.set(Calendar.MILLISECOND, 0);

		// Return the updated date with time stripped (set to 00:00:00.000)
		return cal.getTime();
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
		if (!field.equals(sortField) || !ascending) {
			// If this is a new field or the current order is not ascending, update sort
			sortField = field;
			ascending = true;

			updatePaginatedList();
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
		// Check if the field is different from the current sort field
		// OR if the current sort direction is ascending
		if (!field.equals(sortField) || ascending) {

			// Update the sort field to the newly selected one
			sortField = field;

			// Set the sort direction to descending
			ascending = false;

			// Refresh the paginated list to reflect the new sort order
			updatePaginatedList();
		}
	}

	// Getters and Setters for all fields including sortField and ascending

	public String getSortField() {
		return sortField;
	}

	public boolean isAscending() {
		return ascending;
	}

	public List<ClaimHistory> getPaginatedList() {
		return paginatedList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public ClaimHistory getClaimHistory() {
		return claimHistory;
	}

	public void setClaimHistory(ClaimHistory claimHistory) {
		this.claimHistory = claimHistory;
	}

	public ClaimHistorySearchDao getCleamsHistorySearchDao() {
		return cleamsHistorySearchDao;
	}

	public void setCleamsHistorySearchDao(ClaimHistorySearchDao cleamsHistorySearchDao) {
		this.cleamsHistorySearchDao = cleamsHistorySearchDao;
	}

	public List<ClaimHistory> getClaimHistoryList() {
		return claimHistoryList;
	}

	public void setClaimHistoryList(List<ClaimHistory> claimHistoryList) {
		this.claimHistoryList = claimHistoryList;
	}

	public List<ClaimHistory> getFilteredList() {
		return filteredList;
	}

	public void setFilteredList(List<ClaimHistory> filteredList) {
		this.filteredList = filteredList;
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

	public Claims getClaim() {
		return claim;
	}

	public void setClaim(Claims claim) {
		this.claim = claim;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHid() {
		return hid;
	}

	public void setHid(String hid) {
		this.hid = hid;
	}

	public String getClaimId() {
		return claimId;
	}

	public void setClaimId(String claimId) {
		this.claimId = claimId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPaginatedList(List<ClaimHistory> paginatedList) {
		this.paginatedList = paginatedList;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	// Add other getters/setters as needed
}

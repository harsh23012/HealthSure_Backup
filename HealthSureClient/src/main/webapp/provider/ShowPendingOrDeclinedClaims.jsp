<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<!DOCTYPE html>
<html>
<head>
    <title>Pending & Declined Claims</title>
     <link rel="stylesheet" type="text/css" href="/HealthSureClient/resources/css/ShowPendingClaims.css" />
    
</head>
<f:view>
<body>

    <!-- Navbar -->
    <div class="mb-6">
        <jsp:include page="/navbar/NavProvider.jsp" />
    </div>

    <!-- Claims Panel -->
        <div class="page-wrapper">
            <h2 class="page-title">Pending & Declined Claims</h2>
            <h:form>
			    <div class="search-card">
			        <h3 class="search-title">🔍 Filter Claims</h3>
			
			        <div class="search-fields">
			            <div class="search-field">
			                <label for="claimId">Claim ID</label>
			                <h:inputText id="claimId" value="#{claimController.searchClaimId}" />
			            </div>
			
			            <div class="search-field">
			                <label for="procedureId">Procedure ID</label>
			                <h:inputText id="procedureId" value="#{claimController.searchProcedureId}" />
			            </div>
			
			            <div class="search-field">
			                <label for="claimStatus">Claim Status</label>
			                <h:selectOneMenu id="claimStatus" value="#{claimController.searchClaimStatus}">
			                    <f:selectItem itemLabel="-- Select --" itemValue="" />
			                    <f:selectItem itemLabel="Pending" itemValue="Pending" />
			                    <f:selectItem itemLabel="Declined" itemValue="Declined" />
			                </h:selectOneMenu>
			            </div>
			

			        </div>
			
			        <div class="search-actions">
			            <h:commandButton value="Search" action="#{claimController.searchClaims}" styleClass="search-btn" />
			            <h:commandButton value="Clear" action="#{claimController.clearSearchClaims}" styleClass="clear-btn" />
			        </div>
			    </div>
			</h:form>
   

		    <h:panelGroup rendered="#{not empty claimController.paginatedPendingOrDeclinedClaim}">
            <h:form>
                <h:dataTable value="#{claimController.paginatedPendingOrDeclinedClaim}" var="claim"
                             styleClass="data-table">
                    <h:column>
                        <f:facet name="header">
						    <h:commandLink action="#{claimController.sortByPendingClaim('claimId')}">
						        <h:outputText value="Claim ID" />
						        <h:outputText value="#{claimController.sortField1 eq 'claimId' ? (claimController.ascending1 ? ' ▲' : ' ▼') : ''}" />
						    </h:commandLink>
						</f:facet>

                        <h:outputText value="#{claim.claimId}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header">
						    <h:commandLink action="#{claimController.sortByPendingClaim('procedureId')}">
						        <h:outputText value="Procedure ID" />
						        <h:outputText value="#{claimController.sortField1 eq 'procedureId' ? (claimController.ascending1 ? ' ▲' : ' ▼') : ''}" />
						    </h:commandLink>
						</f:facet>

                        <h:outputText value="#{claim.procedureId}" />
                    </h:column>
                    <h:column>
						<f:facet name="header">
						    <h:commandLink action="#{claimController.sortByPendingClaim('amountClaimed')}" >
						        <h:outputText value="₹ Claimed" />
						        <h:outputText value="#{claimController.sortField1 eq 'amountClaimed' ? (claimController.ascending1 ? ' ▲' : ' ▼') : ''}" />
						    </h:commandLink>
						</f:facet>
                        <h:outputText value="#{claim.amountClaimed}" />
                    </h:column>
                    <h:column>
						<f:facet name="header">
						    <h:commandLink action="#{claimController.sortByPendingClaim('amountApproved')}" >
						        <h:outputText value="₹ Approved" />
						        <h:outputText value="#{claimController.sortField1 eq 'amountApproved' ? (claimController.ascending1 ? ' ▲' : ' ▼') : ''}" />
						    </h:commandLink>
						</f:facet>
                        <h:outputText value="#{claim.amountApproved}" />
                    </h:column>
                    <h:column>
						<f:facet name="header">
						    <h:commandLink action="#{claimController.sortByPendingClaim('claimStatus')}">
						        <h:outputText value="Status" />
						        <h:outputText value="#{claimController.sortField1 eq 'claimStatus' ? (claimController.ascending1 ? ' ▲' : ' ▼') : ''}" />
						    </h:commandLink>
						</f:facet>
                        <h:outputText value="#{claim.claimStatus}" />
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Remarks" /></f:facet>
                        <h:outputText value="#{claim.description}" />
                    </h:column>
                    <h:column>
						<f:facet name="header">
						    <h:commandLink action="#{claimController.sortByPendingClaim('actionDate')}">
						        <h:outputText value="Action Date" />
						        <h:outputText value="#{claimController.sortField1 eq 'actionDate' ? (claimController.ascending1 ? ' ▲' : ' ▼') : ''}" />
						    </h:commandLink>
						</f:facet>
                        <h:outputText value="#{claim.actionDate}">
						    <f:convertDateTime pattern="yyyy-MM-dd" />
						</h:outputText>
                    </h:column>
                    <h:column>
                        <f:facet name="header"><h:outputText value="Action" /></f:facet>
                        <h:commandButton value="Update Claim"
                                         action="#{claimController.editClaim(claim.claimId)}"
                                         styleClass="update-btn" />
                    </h:column>
                </h:dataTable>
                
                <div class="simple-pagination">
		        <h:commandButton value="Previous"
		                         action="#{claimController.previousPage1}"
		                         disabled="#{!claimController.hasPreviousPage1}"
		                         styleClass="nav-btn" />
		
		        <h:outputText value="Page #{claimController.currentPage1} of #{claimController.totalPages1}" 
		                      styleClass="page-status" />
		
		        <h:commandButton value="Next"
		                         action="#{claimController.nextPage1}"
		                         disabled="#{!claimController.hasNextPage1}"
		                         styleClass="nav-btn" />
		    </div>
            </h:form>
        </div>
    </h:panelGroup>

    <!-- No Data Message -->
    <h:panelGroup rendered="#{empty claimController.paginatedPendingOrDeclinedClaim}">
        <p class="empty-message">No Pending or Denied Claim is available for update.</p>
    </h:panelGroup>
	    <!-- 📎 Footer -->
    <jsp:include page="/footer/Footer.jsp" />
</body>
</f:view>
</html>

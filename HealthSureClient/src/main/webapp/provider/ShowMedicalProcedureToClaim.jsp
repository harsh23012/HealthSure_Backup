<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Available Procedures for Claim</title>
    <link rel="stylesheet" type="text/css" href="/HealthSureClient/resources/css/ShowMedicalProcedure.css" />
</head>
<body>
<f:view>

    <!-- 🔗 Navbar -->
    <jsp:include page="/navbar/NavProvider.jsp" />

    <!-- 📋 Page Container -->
    <div class="page-container">

        <h1 class="page-title">Unclaimed Medical Procedures</h1>
        <!-- 🔍 Search Section -->
        <h:form>
         <h3 class="search-title">🔍 Filter Procedures</h3>
		<div class="search-container">
		    <h:outputLabel for="searchProcId" value="Search by Procedure ID:" styleClass="search-label" />
		    <h:inputText id="searchProcId" value="#{claimController.searchProcedureId}" styleClass="search-input" />
		    <h:commandButton value="Search" action="#{claimController.searchByProcedureId}" styleClass="search-btn"/>
		    <h:commandButton value="Clear" action="#{claimController.clearSearch}" styleClass="clear-btn"/>
		</div>
		</h:form>
        <h:form>
            <h:dataTable value="#{claimController.paginatedUnclaimedProcedures}" var="proc"
                         styleClass="table-style"
                         headerClass=""
                         rowClasses="">
                <h:column>
                        <f:facet name="header">
    						<h:commandLink action="#{claimController.sortBy('procedureId')}" styleClass="sortable-header">
					        <h:outputText value="Procedure ID" />
					        <h:outputText value="#{claimController.sortField eq 'procedureId' ? (claimController.ascending ? ' ▲' : ' ▼') : ''}" />
					    </h:commandLink>
					</f:facet>
                    <h:outputText value="#{proc.procedureId}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
					    <h:commandLink action="#{claimController.sortBy('diagnosis')}" styleClass="sortable-header">
					        <h:outputText value="Diagnosis" />
					        <h:outputText value="#{claimController.sortField eq 'diagnosis' ? (claimController.ascending ? ' ▲' : ' ▼') : ''}" />
					    </h:commandLink>
					</f:facet>

                    <h:outputText value="#{proc.diagnosis}" />
                </h:column>

                <h:column>
					<f:facet name="header">
					    <h:commandLink action="#{claimController.sortBy('procedureDate')}" styleClass="sortable-header">
					        <h:outputText value="Procedure Date" />
					        <h:outputText value="#{claimController.sortField eq 'procedureDate' ? (claimController.ascending ? ' ▲' : ' ▼') : ''}" />
					    </h:commandLink>
					</f:facet>
					<h:outputText value="#{proc.procedureDate}">
						    <f:convertDateTime pattern="yyyy-MM-dd" />
					</h:outputText>
                </h:column>

                <h:column>
                     <f:facet name="header">
					    <h:commandLink action="#{claimController.sortBy('doctorName')}" styleClass="sortable-header">
					        <h:outputText value="Doctor Name" />
					        <h:outputText value="#{claimController.sortField eq 'doctorName' ? (claimController.ascending ? ' ▲' : ' ▼') : ''}" />
					    </h:commandLink>
					</f:facet>

    				<h:outputText value="#{proc.doctor.doctorName}" />
                </h:column>

                <h:column>
                    <f:facet name="header">
					    <h:commandLink action="#{claimController.sortBy('hospitalName')}" styleClass="sortable-header">
					        <h:outputText value="Hospital Name" />
					        <h:outputText value="#{claimController.sortField eq 'hospitalName' ? (claimController.ascending ? ' ▲' : ' ▼') : ''}" />
					    </h:commandLink>
					</f:facet>

                    <h:outputText value="#{proc.provider.hospitalName}" />
                </h:column>

                <h:column>
                    <f:facet name="header"><h:outputText value="Action" /></f:facet>
                    <h:commandButton value="View Details"
                                     action="#{claimController.searchRecipient(proc)}"
                                     styleClass="view-btn">
                        <f:param name="recipientId" value="#{proc.recipient.hId}" />
                    </h:commandButton>
                </h:column>
            </h:dataTable>
        
		    <div class="simple-pagination">
		        <h:commandButton value="Previous"
		                         action="#{claimController.previousPage}"
		                         disabled="#{!claimController.hasPreviousPage}"
		                         styleClass="nav-btn" />
		
		        <h:outputText value="Page #{claimController.currentPage} of #{claimController.totalPages}" 
		                      styleClass="page-status" />
		
		        <h:commandButton value="Next"
		                         action="#{claimController.nextPage}"
		                         disabled="#{!claimController.hasNextPage}"
		                         styleClass="nav-btn" />
		    </div>
        </h:form>


        <!-- No Data Message -->
        <h:panelGroup rendered="#{empty claimController.paginatedUnclaimedProcedures}">
            <p class="no-procedures">No procedures available for claim submission.</p>
        </h:panelGroup>

    </div>

    <!-- 📎 Footer -->
    <jsp:include page="/footer/Footer.jsp" />

</f:view>
</body>
</html>

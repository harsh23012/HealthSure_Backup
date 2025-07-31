<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Available Procedures for Claim</title>
    <link rel="stylesheet" type="text/css" href="/HealthSureClient/resources/css/ShowMedicalProcedures.css" />
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
            <div class="search-container">
                <h:outputLabel for="searchProcId" value="Search by Procedure ID:" styleClass="search-label" />
                <h:inputText id="searchProcId" value="#{claimController.searchProcedureId}" styleClass="search-input" />
                <h:commandButton value="Search" action="#{claimController.searchByProcedureId}" styleClass="search-btn"/>
                <h:commandButton value="Clear" action="#{claimController.clearSearch}" styleClass="clear-btn"/>
            </div>
        </h:form>

        <!-- 📊 Data Table -->
        <h:form>
            <h:panelGroup rendered="#{not empty claimController.paginatedUnclaimedProcedures}">
                <div class="table-container">
                    <h:dataTable value="#{claimController.paginatedUnclaimedProcedures}" var="proc"
                                 styleClass="table-style">

                        <!-- Procedure ID Column -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup styleClass="h-panelgroup">
                                    <h:outputText value="Procedure Id" />
                                    <h:panelGroup layout="block" styleClass="sort-icons-container">
                                        <h:commandLink action="#{claimController.sortByAsc('proc.procedureId')}"
                                                       rendered="#{claimController.renderSortButton('proc.procedureId','asc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/up-arrow.png"
                                                            width="10" height="10" title="sort-ascending"/>
                                        </h:commandLink>
                                        <h:commandLink action="#{claimController.sortByDesc('proc.procedureId')}"
                                                       rendered="#{claimController.renderSortButton('proc.procedureId','desc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/down-arrow.png"
                                                            width="10" height="10" title="sort-descending"/>
                                        </h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{proc.procedureId}" />
                        </h:column>

                        <!-- Diagnosis Column -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup styleClass="h-panelgroup">
                                    <h:outputText value="Diagnosis" />
                                    <h:panelGroup layout="block" styleClass="sort-icons-container">
                                        <h:commandLink action="#{claimController.sortByAsc('proc.diagnosis')}"
                                                       rendered="#{claimController.renderSortButton('proc.diagnosis','asc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/up-arrow.png"
                                                            width="10" height="10" title="sort-ascending"/>
                                        </h:commandLink>
                                        <h:commandLink action="#{claimController.sortByDesc('proc.diagnosis')}"
                                                       rendered="#{claimController.renderSortButton('proc.diagnosis','desc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/down-arrow.png"
                                                            width="10" height="10" title="sort-descending"/>
                                        </h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{proc.diagnosis}" />
                        </h:column>

                        <!-- Procedure Date Column -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup styleClass="h-panelgroup">
                                    <h:outputText value="Procedure Date" />
                                    <h:panelGroup layout="block" styleClass="sort-icons-container">
                                        <h:commandLink action="#{claimController.sortByAsc('proc.procedureDate')}"
                                                       rendered="#{claimController.renderSortButton('proc.procedureDate','asc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/up-arrow.png"
                                                            width="10" height="10" title="sort-ascending"/>
                                        </h:commandLink>
                                        <h:commandLink action="#{claimController.sortByDesc('proc.procedureDate')}"
                                                       rendered="#{claimController.renderSortButton('proc.procedureDate','desc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/down-arrow.png"
                                                            width="10" height="10" title="sort-descending"/>
                                        </h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{proc.procedureDate}">
                                <f:convertDateTime pattern="yyyy-MM-dd" />
                            </h:outputText>
                        </h:column>

                        <!-- Doctor Name Column -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup styleClass="h-panelgroup">
                                    <h:outputText value="Doctor Name" />
                                    <h:panelGroup layout="block" styleClass="sort-icons-container">
                                        <h:commandLink action="#{claimController.sortByAsc('proc.doctor.doctorName')}"
                                                       rendered="#{claimController.renderSortButton('proc.doctor.doctorName','asc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/up-arrow.png"
                                                            width="10" height="10" title="sort-ascending"/>
                                        </h:commandLink>
                                        <h:commandLink action="#{claimController.sortByDesc('proc.doctor.doctorName')}"
                                                       rendered="#{claimController.renderSortButton('proc.doctor.doctorName','desc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/down-arrow.png"
                                                            width="10" height="10" title="sort-descending"/>
                                        </h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{proc.doctor.doctorName}" />
                        </h:column>

                        <!-- Hospital Name Column -->
                        <h:column>
                            <f:facet name="header">
                                <h:panelGroup styleClass="h-panelgroup">
                                    <h:outputText value="Hospital Name" />
                                    <h:panelGroup layout="block" styleClass="sort-icons-container">
                                        <h:commandLink action="#{claimController.sortByAsc('proc.provider.hospitalName')}"
                                                       rendered="#{claimController.renderSortButton('proc.provider.hospitalName','asc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/up-arrow.png"
                                                            width="10" height="10" title="sort-ascending"/>
                                        </h:commandLink>
                                        <h:commandLink action="#{claimController.sortByDesc('proc.provider.hospitalName')}"
                                                       rendered="#{claimController.renderSortButton('proc.provider.hospitalName','desc')}"
                                                       styleClass="sort-icons">
                                            <h:graphicImage value="/resources/media/images/down-arrow.png"
                                                            width="10" height="10" title="sort-descending"/>
                                        </h:commandLink>
                                    </h:panelGroup>
                                </h:panelGroup>
                            </f:facet>
                            <h:outputText value="#{proc.provider.hospitalName}" />
                        </h:column>

                        <!-- Action Column -->
                        <h:column>
                            <f:facet name="header"><h:outputText value="Action" /></f:facet>
                            <h:commandButton value="View Details"
                                             action="#{claimController.searchRecipient(proc)}"
                                             styleClass="view-btn">
                                <f:param name="recipientId" value="#{proc.recipient.hId}" />
                            </h:commandButton>
                        </h:column>
                    </h:dataTable>
                </div>
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
		    </h:panelGroup>
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

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Patient Insurance Details</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8fcff;
            color: #003366;
            margin: 0;
            padding: 20px;
        }
        h2 {
            color: #0077b6;
            text-align: center;
            margin-bottom: 25px;
        }
        .data-table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
            background-color: #ffffff;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        }
        .data-table th, .data-table td {
            border: 1px solid #bcd9ea;
            padding: 10px;
            text-align: left;
        }
        .data-table th {
            background-color: #d0f0f3;
            color: #003c58;
        }
        .data-table tr:nth-child(even) {
            background-color: #f1faff;
        }
        .data-table tr:hover {
            background-color: #e0f7ff;
        }
        .error-message {
            color: red;
            font-size: 0.9em;
        }
    </style>
</head>
<body>
<h2>Patient insurance details</h2>

<h:form prependId="false">
    <h:panelGrid columns="4" cellpadding="5">
        <h:outputLabel for="doctorId" value="Enter Doctor ID:" />
        <h:inputText id="doctorId" value="#{providerController.doctorId}" />
        <h:message for="doctorId" styleClass="error-message" />
        <h:outputLabel />

        <h:outputLabel for="recipientId" value="Enter Patient ID (optional):" />
        <h:inputText id="recipientId" value="#{providerController.healthId}" />
        <h:message for="recipientId" styleClass="error-message" />
        <h:outputLabel />

        <h:outputLabel for="patientName" value="Patient Name (optional):" />
        <h:inputText id="patientName" value="#{providerController.patientName}" />
        <h:message for="patientName" styleClass="error-message" />
        <h:outputLabel />

        <h:outputLabel for="matchType" value="Name Match Type:" />
        <h:selectOneMenu id="matchType" value="#{providerController.matchType}">
            <f:selectItem itemLabel="Starts With" itemValue="startsWith" />
            <f:selectItem itemLabel="Ends With" itemValue="endsWith" />
            <f:selectItem itemLabel="Contains" itemValue="contains" />
            <f:selectItem itemLabel="Exact" itemValue="exact" />
        </h:selectOneMenu>
        <h:message for="matchType" styleClass="error-message" />

        <h:outputLabel />
        <h:commandButton value="Search" action="#{providerController.handleSearch}" />
    </h:panelGrid>
</h:form>

<!-- TOP MESSAGE -->
<h:panelGroup rendered="#{not empty providerController.topMessage}">
    <h:outputText value="#{providerController.topMessage}" style="color:red; font-weight:bold;" />
    <br/><br/>
</h:panelGroup>
<h:panelGroup rendered="#{providerController.showPatientsFlag}">
    <!-- Table for associatedPatients -->
<!-- PATIENT TABLE -->
<h:form rendered="#{providerController.showPatientsFlag}">
    <h:dataTable value="#{providerController.associatedPatients}" var="patient" styleClass="data-table">

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Health Id#{providerController.sortField eq 'hId' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                               action="#{providerController.sortBy('patients', 'hId')}" />
            </f:facet>
            <h:outputText value="#{patient.hId}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="User Name#{providerController.sortField eq 'userName' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                               action="#{providerController.sortBy('patients', 'userName')}" />
            </f:facet>
            <h:outputText value="#{patient.userName}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="First Name#{providerController.sortField eq 'firstName' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                               action="#{providerController.sortBy('patients', 'firstName')}" />
            </f:facet>
            <h:outputText value="#{patient.firstName}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Last Name#{providerController.sortField eq 'lastName' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                               action="#{providerController.sortBy('patients', 'lastName')}" />
            </f:facet>
            <h:outputText value="#{patient.lastName}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:outputText value="Show Insurance" />
            </f:facet>
            <h:commandButton value="Show Insurance"
                             action="#{providerController.showInsuranceForPatient(patient.hId)}" />
        </h:column>

    </h:dataTable>
</h:form>
</h:panelGroup>
<h:panelGroup rendered="#{providerController.showInsuranceFlag}">
    <!-- Table for patientInsuranceList -->

<!-- INSURANCE TABLE -->
<h:form rendered="#{providerController.showInsuranceFlag}">
    <h:dataTable value="#{providerController.patientInsuranceList}" var="insurance" styleClass="data-table">
 <h:column>
        <f:facet name="header">
            <h:commandLink value="Patient Name#{providerController.sortField eq 'patientName' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'patientName')}" />
        </f:facet>
        <h:outputText value="#{insurance.patientName}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Company Name#{providerController.sortField eq 'companyName' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'companyName')}" />
        </f:facet>
        <h:outputText value="#{insurance.companyName}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Plan Name#{providerController.sortField eq 'planName' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'planName')}" />
        </f:facet>
        <h:outputText value="#{insurance.planName}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Enrollment Date#{providerController.sortField eq 'enrollmentDate' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'enrollmentDate')}" />
        </f:facet>
        <h:outputText value="#{insurance.enrollmentDate}">
            <f:convertDateTime pattern="yyyy-MM-dd" />
        </h:outputText>
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Coverage Start#{providerController.sortField eq 'coverageStartDate' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'coverageStartDate')}" />
        </f:facet>
        <h:outputText value="#{insurance.coverageStartDate}">
            <f:convertDateTime pattern="yyyy-MM-dd" />
        </h:outputText>
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Coverage End#{providerController.sortField eq 'coverageEndDate' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'coverageEndDate')}" />
        </f:facet>
        <h:outputText value="#{insurance.coverageEndDate}">
            <f:convertDateTime pattern="yyyy-MM-dd" />
        </h:outputText>
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Coverage Type#{providerController.sortField eq 'coverageType' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'coverageType')}" />
        </f:facet>
        <h:outputText value="#{insurance.coverageType}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Status#{providerController.sortField eq 'coverageStatus' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'coverageStatus')}" />
        </f:facet>
        <h:outputText value="#{insurance.coverageStatus}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Coverage Limit#{providerController.sortField eq 'coverageLimit' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'coverageLimit')}" />
        </f:facet>
        <h:outputText value="#{insurance.coverageLimit}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Remaining Amount#{providerController.sortField eq 'remaining' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'remaining')}" />
        </f:facet>
        <h:outputText value="#{insurance.remaining}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Claimed Amount#{providerController.sortField eq 'claimed' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'claimed')}" />
        </f:facet>
        <h:outputText value="#{insurance.claimed}" />
    </h:column>

    <h:column>
        <f:facet name="header">
            <h:commandLink value="Last Claim Date#{providerController.sortField eq 'lastClaimDate' ? (providerController.ascending ? ' ↑' : ' ↓') : ''}"
                           action="#{providerController.sortBy('insurance', 'lastClaimDate')}" />
        </f:facet>
        <h:outputText value="#{insurance.lastClaimDate}">
            <f:convertDateTime pattern="yyyy-MM-dd" />
        </h:outputText>
    </h:column>

    <h:column>
        <f:facet name="header"><h:outputText value="Action" /></f:facet>
        <h:panelGroup rendered="#{insurance.coverageType eq 'FAMILY'}">
            <h:commandButton value="View Members" action="#{providerController.redirect(insurance)}" />
        </h:panelGroup>
    </h:column>

    </h:dataTable>

    <h:panelGroup rendered="#{providerController.cameFromPatientSearch and providerController.showInsuranceFlag}">
        <h:commandButton value="Back to PatientLists" action="#{providerController.backToPatients}"/>
    </h:panelGroup>
</h:form>
</h:panelGroup>
</body>
</html>
</f:view>


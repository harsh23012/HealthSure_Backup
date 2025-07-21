<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<f:view>
<html>
<head>
    <meta charset="UTF-8">
    <title>Subscribed Family Members</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f2f8fc;
            margin: 0;
            padding: 20px;
        }
        h2 {
            text-align: center;
            color: #005b96;
            margin-bottom: 20px;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            background-color: #fff;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        .data-table th, .data-table td {
            padding: 10px;
            border: 1px solid #cce0ff;
            text-align: left;
        }
        .data-table th {
            background-color: #dbeeff;
            color: #003366;
        }
        .data-table tr:nth-child(even) {
            background-color: #f3faff;
        }
        .data-table tr:hover {
            background-color: #e6f7ff;
        }
        .back-button {
            margin-top: 20px;
            display: flex;
            justify-content: center;
        }
    </style>
</head>
<body>

<h2>Subscribed Family Members</h2>

<h:form>
    <h:dataTable value="#{providerController.subscribedMembers}" var="member" styleClass="data-table">

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Member ID" action="#{providerController.sortBy('members', 'memberId')}" />
            </f:facet>
            <h:outputText value="#{member.memberId}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Full Name" action="#{providerController.sortBy('members', 'fullName')}" />
            </f:facet>
            <h:outputText value="#{member.fullName}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Age" action="#{providerController.sortBy('members', 'age')}" />
            </f:facet>
            <h:outputText value="#{member.age}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Gender" action="#{providerController.sortBy('members', 'gender')}" />
            </f:facet>
            <h:outputText value="#{member.gender}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Relation" action="#{providerController.sortBy('members', 'relationWithProposer')}" />
            </f:facet>
            <h:outputText value="#{member.relationWithProposer}" />
        </h:column>

        <h:column>
            <f:facet name="header">
                <h:commandLink value="Aadhar No" action="#{providerController.sortBy('members', 'aadharNo')}" />
            </f:facet>
            <h:outputText value="#{member.aadharNo}" />
        </h:column>

    </h:dataTable>

    <!-- Back Button -->
    <div class="back-button">
        <h:commandButton value="Back to Insurance Details" action="showInsuranceDetails" />
    </div>
</h:form>

</body>
</html>
</f:view>

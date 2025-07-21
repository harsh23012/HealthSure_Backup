<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view>
<html>
<head>
    <title>Add Prescribed Medicine</title>
    <link rel="stylesheet" href="css/healthsure-style.css" />
    <style>
        .form-container {
            max-width: 600px;
            margin: 40px auto;
            padding: 30px;
            background-color: #f4f8fb;
            border-radius: 10px;
            box-shadow: 0px 0px 10px #ccc;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }
        .form-control {
            width: 100%;
            padding: 8px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        .btn-submit {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
        }
        .btn-submit:hover {
            background-color: #0056b3;
        }
        .form-title {
            text-align: center;
            font-size: 22px;
            margin-bottom: 25px;
            color: #2a3f54;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <div class="form-title">Add Prescribed Medicine</div>

        <h:form id="addMedicineForm">
            <div class="form-group">
                <label for="prescribedId">Prescribed ID</label>
                <h:inputText id="prescribedId" value="#{prescribedMedicine.prescribedId}" 
                             required="true" styleClass="form-control"/>
            </div>

            <div class="form-group">
                <label for="prescriptionId">Prescription ID</label>
                <h:inputText id="prescriptionId" value="#{prescribedMedicine.prescription.prescriptionId}" 
                             required="true" styleClass="form-control"/>
            </div>

            <div class="form-group">
                <label for="medicineName">Medicine Name</label>
                <h:inputText id="medicineName" value="#{prescribedMedicine.medicineName}" 
                             required="true" styleClass="form-control"/>
            </div>

            <div class="form-group">
                <label for="dosage">Dosage</label>
                <h:inputText id="dosage" value="#{prescribedMedicine.dosage}" 
                             required="true" styleClass="form-control"/>
            </div>

            <div class="form-group">
                <label for="duration">Duration</label>
                <h:inputText id="duration" value="#{prescribedMedicine.duration}" 
                             required="true" styleClass="form-control"/>
            </div>
			<div class="form-group">
    <label for="startDate">Start Date</label>
    <h:inputText id="startDate" value="#{prescribedMedicine.startDate}" styleClass="form-control">
        <f:convertDateTime pattern="yyyy-MM-dd" />
    </h:inputText>
</div>

<div class="form-group">
    <label for="endDate">End Date</label>
    <h:inputText id="endDate" value="#{prescribedMedicine.endDate}" styleClass="form-control">
        <f:convertDateTime pattern="yyyy-MM-dd" />
    </h:inputText>
</div>
			
            <div class="form-group">
                <label for="notes">Notes</label>
                <h:inputTextarea id="notes" value="#{prescribedMedicine.notes}" 
                                 cols="30" rows="4" styleClass="form-control"/>
            </div>

            <div class="form-group" style="text-align: center;">
                <h:commandButton value="Add Medicine" action="#{providerController.addPresribedMedicinesController(prescribedMedicine)}" 
                                 styleClass="btn-submit"/>
            </div>
        </h:form>
    </div>
</body>
</html>
</f:view>

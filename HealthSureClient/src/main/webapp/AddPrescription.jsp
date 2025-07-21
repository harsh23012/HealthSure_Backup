<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Prescription</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right, #f0f4c3, #c8e6c9);
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 850px;
            margin: 40px auto;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
            padding: 25px 30px;
        }

        h2 {
            text-align: center;
            color: #33691e;
            font-size: 22px;
            margin-bottom: 20px;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 18px 30px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
        }

        label {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 5px;
            font-size: 14px;
        }

        input[type="text"] {
            padding: 8px 10px;
            font-size: 13px;
            border: 1px solid #ccc;
            border-radius: 6px;
            width: 100%;
            box-sizing: border-box;
        }

        .button-group {
            display: flex;
            justify-content: center;
            gap: 25px;
            margin-top: 25px;
        }

        .custom-button {
            background-color: #388e3c;
            color: white;
            padding: 10px 20px;
            font-size: 13px;
            font-weight: bold;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .custom-button:hover {
            background-color: #2e7d32;
        }

        .blue-button {
            background-color: #0288d1;
        }

        .blue-button:hover {
            background-color: #0277bd;
        }
    </style>
</head>
<body>
<f:view>
    <div class="container">
        <h:form prependId="false">
            <h2>Add Prescription Details</h2>

            <div class="form-grid">
                <div class="form-group">
                    <h:outputLabel for="prescriptionId" value="Prescription ID:" />
                    <h:inputText id="prescriptionId" value="#{prescription.prescriptionId}" required="true" />
                </div>

                <div class="form-group">
                    <h:outputLabel for="procedureId" value="Procedure ID:" />
                  <h:inputText id="procedureId" value="#{sessionScope.procedureId}" readonly="true" />
                </div>

                <div class="form-group">
                    <h:outputLabel for="recipientId" value="Recipient ID:" />
                    <h:inputText id="recipientId" value="#{sessionScope.recipientHid}" readonly="true" />
                </div>

                <div class="form-group">
                    <h:outputLabel for="providerId" value="Provider ID:" />
                   <h:inputText id="providerId" value="#{sessionScope.providerId}" readonly="true" />
                </div>

                <div class="form-group">
                    <h:outputLabel for="doctorId" value="Doctor ID:" />
                   <h:inputText id="doctorId" value="#{sessionScope.doctorId}" readonly="true" />
                </div>
            </div>
			<div class="form-group" style="grid-column: span 2;">
    <h:outputLabel for="writtenOn" value="Written On (Date):" />
    <h:inputText id="writtenOn" value="#{prescription.writtenOn}" required="true" requiredMessage="Written On date is required.">
        <f:convertDateTime pattern="yyyy-MM-dd" />
    </h:inputText>
    <h:message for="writtenOn" style="color:red; font-size:12px;" />
</div>
<div class="form-group" style="grid-column: span 2;">
    <h:outputLabel for="startDate" value="Start Date:" />
    <h:inputText id="startDate" value="#{prescription.startDate}">
        <f:convertDateTime pattern="yyyy-MM-dd" />
    </h:inputText>
     <h:message for="startDate" style="color:red; font-size:12px;" />
</div>

<div class="form-group" style="grid-column: span 2;">
    <h:outputLabel for="endDate" value="End Date:" />
    <h:inputText id="endDate" value="#{prescription.endDate}">
        <f:convertDateTime pattern="yyyy-MM-dd" />
    </h:inputText>
     <h:message for="endDate" style="color:red; font-size:12px;" />
</div>			
            <div class="button-group">
                <h:commandButton value="Save Prescription"
                                 styleClass="custom-button"
                                 action="#{providerController.addPrescriptionController(prescription)}" />
            </div>
        </h:form>
    </div>
</f:view>
</body>
</html>

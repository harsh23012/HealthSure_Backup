<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Procedure Test</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(to right, #fce4ec, #e1f5fe);
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
            color: #00796b;
            font-size: 22px;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 18px;
            display: flex;
            flex-direction: column;
        }

        label, h\\:outputLabel {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 5px;
            font-size: 14px;
        }

        input[type="text"], textarea {
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
            margin-top: 25px;
        }

        .custom-button {
            background-color: #00796b;
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
            background-color: #004d40;
        }
    </style>
</head>
<body>
<f:view>
    <div class="container">
        <h:form>
            <h2>Add Procedure Test</h2>

            <!-- Procedure ID -->
            <div class="form-group">
                <h:outputLabel for="prescriptionId" value="Prescription ID:" />
                <h:inputText id="procedureId" value="#{procedureTest.prescription.prescriptionId}" required="true" />
            </div>

            <!-- Test ID -->
            <div class="form-group">
                <h:outputLabel for="testId" value="Test ID:" />
                <h:inputText id="testId" value="#{procedureTest.testId}" required="true" />
            </div>

            <!-- Test Name -->
            <div class="form-group">
                <h:outputLabel for="testName" value="Test Name:" />
                <h:inputText id="testName" value="#{procedureTest.testName}" required="true" />
            </div>

            <!-- Test Date -->
            <div class="form-group">
                <h:outputLabel for="testDate" value="Test Date (yyyy-MM-dd):" />
                <h:inputText id="testDate" value="#{procedureTest.testDate}">
                    <f:convertDateTime pattern="yyyy-MM-dd" />
                </h:inputText>
                <h:message for="testDate" style="color:red; font-size:12px;" />
            </div>

            <!-- Result Summary -->
            <div class="form-group">
                <h:outputLabel for="resultSummary" value="Result Summary:" />
                <h:inputTextarea id="resultSummary" value="#{procedureTest.resultSummary}" rows="4" cols="50" />
            </div>

            <!-- Submit Button -->
            <div class="button-group">
                <h:commandButton value="Save Test" 
                                 styleClass="custom-button"
                                 action="#{providerController.addTestController(procedureTest)}" />
            </div>

        </h:form>
    </div>
</f:view>
</body>
</html>

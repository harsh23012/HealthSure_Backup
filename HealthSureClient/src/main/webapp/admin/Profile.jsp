<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Profile</title>
    <link rel="stylesheet" href="/HealthSureClient/resources/css/profile.css" />
</head>

<body>

<f:view>
    <!-- 🌐 Navigation -->
    <jsp:include page="/navbar/NavAdmin.jsp" />

    <div class="container">
        <!-- 👋 Greeting -->
        <h1 class="heading-primary">
            Welcome, 
            <h:outputText value="#{sessionScope.first_name}" /> 
            <h:outputText value="#{sessionScope.last_name}" />
        </h1>

        <!-- 🧾 Details -->
        <div class="details-box">
            <p><strong>Admin ID:</strong> <h:outputText value="#{sessionScope.user_id}" /></p>
            <p><strong>Username:</strong> <h:outputText value="#{sessionScope.user_name}" /></p>
            <p><strong>Email:</strong> <h:outputText value="#{sessionScope.email}" /></p>
            <p><strong>Created At:</strong> <h:outputText value="#{sessionScope.created_at}" /></p>
            <p><strong>Last Updated:</strong> <h:outputText value="#{sessionScope.updated_at}" /></p>
        </div>

        <!-- 🚪 Controls -->
        <div class="button-group">
                <h:form>
                    <h:commandButton value="Logout" action="#{authBean.logout}" styleClass="button-red" />
                </h:form>
                <h:form>
                    <h:commandButton value="Reset Password" action="#{authBean.resetPassword}" styleClass="button-yellow" />
                </h:form>
            </div>
    </div>

    <!-- 📎 Footer -->
    <jsp:include page="/footer/Footer.jsp" />
</f:view>

</body>
</html>

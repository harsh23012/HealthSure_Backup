<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<!DOCTYPE html>
<html>
<head>
    <title>Claim Submitted</title>
    <link rel="stylesheet" type="text/css" href="/HealthSureClient/resources/css/ClaimSuccess.css" />
</head>
<body>
<f:view>

    <!-- 🔗 Navbar -->
    <jsp:include page="/navbar/NavProvider.jsp" />

    <!-- 🎉 Confirmation Card -->
    <div class="confirmation-card">
        <h1 class="confirm-title">✅ Claim Submitted Successfully!</h1>
        <p class="confirm-description">
            Your insurance claim has been filed and is now being processed.
        </p>

        <!-- 📝 Status Overview -->
        <div class="status-box">
            <p>
                Reference ID: <strong><h:outputText value="#{sessionScope.insertedClaims.claimId}" /></strong><br/>
                Submission Date: <strong><h:outputText value="#{sessionScope.insertedClaims.claimDate}" /></strong><br/>
                Status: <span class="pending-status">Pending Review</span>
            </p>
        </div>

        <!-- 🚀 Navigation Options -->
        <div style="margin-top: 2rem;">
            <h:form>
                <h:commandButton value="🏠 Go to Dashboard"
                                 action="Provider.jsp?faces-redirect=true"
                                 styleClass="dashboard-btn" />
            </h:form>
        </div>

        <!-- 🙏 Footer -->
        <p class="footer-text">
            Need help? Contact support or visit your claim summary page for more details.
        </p>
    </div>
	    <!-- 📎 Footer -->
    <jsp:include page="/footer/Footer.jsp" />
</f:view>
</body>
</html>

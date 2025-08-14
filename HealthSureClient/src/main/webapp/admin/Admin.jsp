<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="/HealthSureClient/resources/css/admin.css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>

<body class="admin-body">
<f:view>

    <!-- 🌐 Navigation -->
    <jsp:include page="/navbar/NavAdmin.jsp" />

    <!-- 👋 Welcome -->
    <div class="container">
        <h2 class="page-heading">Welcome Admin 👋</h2>
        <p class="page-subtext">Here are your core administrative controls.</p>
    </div>

    <!-- 🎛️ Control Panel -->
    <div class="container">
        <h3 class="section-heading">Admin Actions</h3>

        <div class="grid-panel">
            <h:form>
                <h:commandButton value="✅ Provider Review" action="#{adminBean.reviewProviders}" styleClass="admin-btn green-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="🔍 Provider Inquiry" action="#{adminBean.searchProviders}" styleClass="admin-btn blue-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="💊 Pharmacy Review" action="#{adminBean.reviewPharmacies}" styleClass="admin-btn purple-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="👥 Member Inquiry" action="#{adminBean.searchMembers}" styleClass="admin-btn teal-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="✏️ Update Provider" action="#{adminBean.updateProvider}" styleClass="admin-btn yellow-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="✏️ Update Member" action="#{adminBean.updateMember}" styleClass="admin-btn orange-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="📃 Create Insurance Plan" action="#{adminBean.createInsurancePlan}" styleClass="admin-btn indigo-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="🔎 Search & Process Claims" action="#{adminBean.searchClaims}" styleClass="admin-btn cyan-btn" />
            </h:form>

            <h:form>
                <h:commandButton value="💰 Pay Hospitals" action="#{adminBean.makePayments}" styleClass="admin-btn fuchsia-btn" />
            </h:form>
        </div>
    </div>

    <!-- 💡 Motivational Quote -->
    <div class="container">
        <div class="quote-box">
            <p class="quote-text">
                "Efficiency is doing things right; effectiveness is doing the right things." — Peter Drucker
            </p>
        </div>
    </div>

    <!-- 📘 Footer -->
    <jsp:include page="/footer/Footer.jsp" />

</f:view>
</body>
</html>

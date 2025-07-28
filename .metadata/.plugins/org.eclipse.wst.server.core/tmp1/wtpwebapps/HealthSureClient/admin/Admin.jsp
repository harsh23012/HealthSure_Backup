<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Admin Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100 pt-20 font-sans">
<f:view>

    <!-- 🌐 Navigation -->
    <jsp:include page="/navbar/NavAdmin.jsp" />

    <!-- 👋 Welcome -->
    <div class="max-w-5xl mx-auto px-4 pt-6 pb-2">
        <h2 class="text-2xl font-bold text-blue-800">
            Welcome Admin 👋
        </h2>
        <p class="text-gray-600 mt-1">Here are your core administrative controls.</p>
    </div>

    <!-- 🎛️ Control Panel -->
    <div class="max-w-6xl mx-auto px-4 py-6 mb-10">
        <h3 class="text-2xl font-bold text-gray-800 mb-6 text-center">Admin Actions</h3>

        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">

            <h:form>
                <h:commandButton value="✅ Provider Review"
                    action="#{adminBean.reviewProviders}"
                    styleClass="w-full py-3 px-5 bg-green-600 hover:bg-green-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="🔍 Provider Inquiry"
                    action="#{adminBean.searchProviders}"
                    styleClass="w-full py-3 px-5 bg-blue-600 hover:bg-blue-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="💊 Pharmacy Review"
                    action="#{adminBean.reviewPharmacies}"
                    styleClass="w-full py-3 px-5 bg-purple-600 hover:bg-purple-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="👥 Member Inquiry"
                    action="#{adminBean.searchMembers}"
                    styleClass="w-full py-3 px-5 bg-teal-600 hover:bg-teal-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="✏️ Update Provider"
                    action="#{adminBean.updateProvider}"
                    styleClass="w-full py-3 px-5 bg-yellow-600 hover:bg-yellow-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="✏️ Update Member"
                    action="#{adminBean.updateMember}"
                    styleClass="w-full py-3 px-5 bg-orange-600 hover:bg-orange-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="📃 Create Insurance Plan"
                    action="#{adminBean.createInsurancePlan}"
                    styleClass="w-full py-3 px-5 bg-indigo-600 hover:bg-indigo-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="🔎 Search Claims"
                    action="#{adminBean.searchClaims}"
                    styleClass="w-full py-3 px-5 bg-cyan-600 hover:bg-cyan-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="⚖️ Process Claims"
                    action="#{adminBean.processClaims}"
                    styleClass="w-full py-3 px-5 bg-lime-600 hover:bg-lime-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

            <h:form>
                <h:commandButton value="💰 Pay Hospitals"
                    action="#{adminBean.makePayments}"
                    styleClass="w-full py-3 px-5 bg-fuchsia-600 hover:bg-fuchsia-700 text-white rounded-xl font-medium shadow transition" />
            </h:form>

        </div>
    </div>
    
    <!-- 💡 Motivational Quote -->
    <div class="max-w-4xl mx-auto px-4 mb-10">
        <div class="bg-gray-200 border-l-4 border-gray-500 p-4 rounded-lg shadow-sm">
            <p class="text-sm text-gray-700 italic">
                "Efficiency is doing things right; effectiveness is doing the right things." — Peter Drucker
            </p>
        </div>
    </div>
    

    <!-- 📘 Footer -->
    <jsp:include page="/footer/Footer.jsp" />

</f:view>
</body>
</html>

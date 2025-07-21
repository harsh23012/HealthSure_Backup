<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsf/core" %>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Recipient Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100 pt-20 font-sans">

<f:view>

    <!-- 🌐 Navigation -->
    <jsp:include page="/navbar/NavRecepient.jsp" />

    <!-- 👋 Welcome Message -->
    <div class="max-w-5xl mx-auto px-4 pt-6 pb-2">
        <h2 class="text-2xl font-bold text-blue-800">
            Welcome back, <h:outputText value="#" /> 🌿
        </h2>
        <p class="text-gray-600 mt-1">Your personal health snapshot is just below.</p>
    </div>

    <!-- 📊 Recipient Overview -->
    <div class="max-w-5xl mx-auto px-4 mt-4 mb-12">
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="bg-white p-5 rounded-xl shadow-md text-center hover:shadow-xl transition">
                <h3 class="text-md font-semibold text-blue-500 uppercase tracking-wide">Appointments</h3>
                <p class="mt-2 text-4xl font-bold text-gray-800">
                    <h:outputText value="#" />
                </p>
                <p class="text-xs text-gray-500 mt-1">Upcoming visits</p>
            </div>

            <div class="bg-white p-5 rounded-xl shadow-md text-center hover:shadow-xl transition">
                <h3 class="text-md font-semibold text-purple-600 uppercase tracking-wide">Prescriptions</h3>
                <p class="mt-2 text-4xl font-bold text-gray-800">
                    <h:outputText value="#" />
                </p>
                <p class="text-xs text-gray-500 mt-1">Your active medications</p>
            </div>

            <div class="bg-white p-5 rounded-xl shadow-md text-center hover:shadow-xl transition">
                <h3 class="text-md font-semibold text-green-600 uppercase tracking-wide">Insurance Plans</h3>
                <p class="mt-2 text-4xl font-bold text-gray-800">
                    <h:outputText value="#" />
                </p>
                <p class="text-xs text-gray-500 mt-1">Active coverage</p>
            </div>

            <div class="bg-white p-5 rounded-xl shadow-md text-center hover:shadow-xl transition">
                <h3 class="text-md font-semibold text-red-500 uppercase tracking-wide">Test Reports</h3>
                <p class="mt-2 text-4xl font-bold text-gray-800">
                    <h:outputText value="#" />
                </p>
                <p class="text-xs text-gray-500 mt-1">Viewed recently</p>
            </div>
        </div>
    </div>

    <!-- 💡 Health Tip or Quote -->
    <div class="max-w-4xl mx-auto mb-10 px-4">
        <div class="bg-green-50 border-l-4 border-green-400 p-4 rounded-lg shadow-sm">
            <p class="text-sm text-green-900 italic">
                “Health is a state of body. Wellness is a state of being.” — J. Stanford
            </p>
        </div>
    </div>

    <!-- 🚀 Quick Actions -->
    <div class="max-w-4xl mx-auto px-4 text-center mb-20">
        <h3 class="text-xl font-semibold text-gray-700 mb-4">Quick Access</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <h:form>
                <h:commandButton value="📖 My Medical History" action="#"
                    styleClass="w-full py-3 bg-gradient-to-r from-blue-500 to-blue-700 text-white rounded-xl font-semibold shadow-md hover:shadow-lg transition" />
            </h:form>
            <h:form>
                <h:commandButton value="🧾 Claim Insurance" action="#"
                    styleClass="w-full py-3 bg-gradient-to-r from-purple-500 to-purple-700 text-white rounded-xl font-semibold shadow-md hover:shadow-lg transition" />
            </h:form>
            <h:form>
                <h:commandButton value="💊 My Prescriptions" action="#"
                    styleClass="w-full py-3 bg-gradient-to-r from-teal-500 to-teal-700 text-white rounded-xl font-semibold shadow-md hover:shadow-lg transition" />
            </h:form>
            <h:form>
                <h:commandButton value="📞 Contact Provider" action="#"
                    styleClass="w-full py-3 bg-gradient-to-r from-pink-500 to-pink-700 text-white rounded-xl font-semibold shadow-md hover:shadow-lg transition" />
            </h:form>
        </div>
    </div>

    <!-- 📎 Footer -->
    <jsp:include page="/footer/Footer.jsp" />

</f:view>
</body>
</html>

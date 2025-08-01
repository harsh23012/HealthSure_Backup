/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.5.42
 * Generated at: 2025-07-25 13:14:11 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.footer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class Footer_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("<link rel=\"stylesheet\" href=\"/HealthSureClient/resources/css/footer.css\" />\n");
      out.write("\n");
      out.write("<footer class=\"footer\">\n");
      out.write("    <div class=\"footer-container\">\n");
      out.write("\n");
      out.write("        <!-- Brand Info -->\n");
      out.write("        <div class=\"footer-brand\">\n");
      out.write("            <img src=\"/HealthSureClient/resources/media/images/Logo.jpg\" alt=\"HealthSure Logo\" class=\"footer-logo-img\" />\n");
      out.write("            <h2 class=\"footer-logo\">HealthSure</h2>\n");
      out.write("            <p class=\"footer-tagline\">Your Health, Our Priority.</p>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <!-- Quick Links -->\n");
      out.write("        <div class=\"footer-links\">\n");
      out.write("            <b>Quick Links</b>\n");
      out.write("            <ul>\n");
      out.write("                <li><a href=\"/HealthSureClient/home/Home.jsf\">Home</a></li>\n");
      out.write("                <li><a href=\"/HealthSureClient/home/Home.jsf#aboutus\">About Us</a></li>\n");
      out.write("                <li><a href=\"/HealthSureClient/home/Home.jsf#contact\">Contact</a></li>\n");
      out.write("            </ul>\n");
      out.write("        </div>\n");
      out.write("        <div class=\"footer-links\">\n");
      out.write("            <b>Login As</b>\n");
      out.write("            <ul>\n");
      out.write("                <li><a href=\"#\">Provider</a></li>\n");
      out.write("                <li><a href=\"#\">Recipient</a></li>\n");
      out.write("                <li><a href=\"/HealthSureClient/pharmacy/Login.jsf\">Pharmacy</a></li>\n");
      out.write("                <li><a href=\"#\">Admin</a></li>\n");
      out.write("            </ul>\n");
      out.write("        </div>\n");
      out.write("        <div class=\"footer-links\">\n");
      out.write("            <b>Signup As</b>\n");
      out.write("            <ul>\n");
      out.write("                <li><a href=\"#\">Provider</a></li>\n");
      out.write("                <li><a href=\"#\">Recipient</a></li>\n");
      out.write("                <li><a href=\"/HealthSureClient/pharmacy/AddOwner.jsf\">Pharmacy</a></li>\n");
      out.write("                <li><a href=\"#\">Admin</a></li>\n");
      out.write("            </ul>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <!-- Role-Based Menus -->\n");
      out.write("        <div class=\"footer-links\">\n");
      out.write("            <b>For Users</b>\n");
      out.write("            <ul>\n");
      out.write("                <li><a href=\"providerDashboard.jsf\">Providers</a></li>\n");
      out.write("                <li><a href=\"recipientDashboard.jsf\">Recipients</a></li>\n");
      out.write("                <li><a href=\"insuranceHome.jsf\">Insurance</a></li>\n");
      out.write("                <li><a href=\"pharmacyHome.jsf\">Pharmacy</a></li>\n");
      out.write("                <li><a href=\"adminDashboard.jsf\">Admin</a></li>\n");
      out.write("            </ul>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <!-- Contact Info -->\n");
      out.write("        <div class=\"footer-contact\">\n");
      out.write("            <b>Contact Us</b>\n");
      out.write("            <p><i class=\"fas fa-map-marker-alt\"></i> 123 HealthSure Street, Bengaluru, India</p>\n");
      out.write("            <p><i class=\"fas fa-envelope\"></i> <a href=\"mailto:support@healthsure.com\">support@healthsure.com</a></p>\n");
      out.write("            <p><i class=\"fas fa-phone-alt\"></i> +91 99999 99999</p>\n");
      out.write("            <p><i class=\"fas fa-clock\"></i> Mon–Sat: 9AM – 8PM</p>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <!-- Social Icons -->\n");
      out.write("        <div class=\"footer-social\">\n");
      out.write("            <b>Follow Us</b>\n");
      out.write("            <div class=\"social-icons\">\n");
      out.write("                <a href=\"#\"><img src=\"/HealthSureClient/resources/media/images/icons/facebook.png\" alt=\"Facebook\" title=\"Facebook\" /></a>\n");
      out.write("                <a href=\"#\"><img src=\"/HealthSureClient/resources/media/images/icons/twitter.png\" alt=\"Twitter\" title=\"Twitter\" /></a>\n");
      out.write("                <a href=\"#\"><img src=\"/HealthSureClient/resources/media/images/icons/instagram.png\" alt=\"Instagram\" title=\"Instagram\" /></a>\n");
      out.write("                <a href=\"#\"><img src=\"/HealthSureClient/resources/media/images/icons/linkedin.png\" alt=\"LinkedIn\" title=\"LinkedIn\" /></a>\n");
      out.write("                <a href=\"#\"><img src=\"/HealthSureClient/resources/media/images/icons/youtube.png\" alt=\"YouTube\" title=\"YouTube\" /></a>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("    </div>\n");
      out.write("\n");
      out.write("    <!-- Bottom Bar -->\n");
      out.write("    <div class=\"footer-bottom\">\n");
      out.write("        <p>&copy; 2025 HEALTHSURE( INFINITE COMPUTER SOLUTIONS ). All Rights Reserved.</p>\n");
      out.write("    </div>\n");
      out.write("</footer>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}

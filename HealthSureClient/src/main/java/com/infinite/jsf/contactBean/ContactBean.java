package com.infinite.jsf.contactBean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.infinite.jsf.util.MailSend;


public class ContactBean {

    private String name;
    private String email;
    private String subject;
    private String message;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String sendEmail() {
        FacesContext context = FacesContext.getCurrentInstance();

        // ✅ Manual Validation
        if (name == null || name.trim().isEmpty()) {
            context.addMessage("name", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Name is required", null));
            return null;
        }

        if (email == null || email.trim().isEmpty()) {
            context.addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email is required", null));
            return null;
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            context.addMessage("email", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid email format", null));
            return null;
        }

        if (subject == null || subject.trim().isEmpty()) {
            context.addMessage("subject", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Subject is required", null));
            return null;
        }

        if (message == null || message.trim().isEmpty()) {
            context.addMessage("message", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message is required", null));
            return null;
        }

        // ✅ Compose Message
        String fullMessage = 
            "📩 New Contact Form Submission\n" +
            "----------------------------------------\n" +
            "👤 Name       : " + name + "\n" +
            "📧 Email      : " + email + "\n" +
            "📝 Subject    : " + subject + "\n" +
            "💬 Message    :\n\n" +
            message + "\n" +
            "----------------------------------------\n" +
            "📅 Submitted on: " + new java.util.Date();

        // ✅ Send Email
        String result = MailSend.sendInfo("infinitehealthsure@gmail.com", subject, fullMessage);

        if (result.contains("Successfully")) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your message has been sent."));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to send message: " + result));
        }

        return "contactConfirm.jsp?faces-redirect=true";
    }
}

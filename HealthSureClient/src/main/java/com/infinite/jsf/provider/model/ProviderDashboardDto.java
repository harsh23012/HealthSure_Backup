package com.infinite.jsf.provider.model;

public class ProviderDashboardDto {
    private int totalAppointments;
    private int totalPatients;
    private int totalClaims;
    private double totalAmounts;

    // Getters and setters
    public int getTotalAppointments() { return totalAppointments; }
    public void setTotalAppointments(int totalAppointments) { this.totalAppointments = totalAppointments; }

    public int getTotalPatients() { return totalPatients; }
    public void setTotalPatients(int totalPatients) { this.totalPatients = totalPatients; }

    public int getTotalClaims() { return totalClaims; }
    public void setTotalClaims(int totalClaims) { this.totalClaims = totalClaims; }

    public double getTotalAmounts() { return totalAmounts; }
    public void setTotalAmounts(double totalAmounts) { this.totalAmounts = totalAmounts; }
}

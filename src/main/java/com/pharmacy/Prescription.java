package com.pharmacy;

public class Prescription {
    private String prescriptionId;
    private String patientName;
    private int patientAge;
    private double patientWeightKg;
    private String drugName;
    private double dosageMg;
    private int refillsRemaining;
    private boolean paid;

    public Prescription(String prescriptionId, String patientName,
                        int patientAge, double patientWeightKg,
                        String drugName, double dosageMg, int refillsRemaining) {
        this.prescriptionId   = prescriptionId;
        this.patientName      = patientName;
        this.patientAge       = patientAge;
        this.patientWeightKg  = patientWeightKg;
        this.drugName         = drugName;
        this.dosageMg         = dosageMg;
        this.refillsRemaining = refillsRemaining;
        this.paid             = false;
    }

    public String getPrescriptionId()       { return prescriptionId; }
    public String getPatientName()          { return patientName; }
    public int getPatientAge()              { return patientAge; }
    public double getPatientWeightKg()      { return patientWeightKg; }
    public String getDrugName()             { return drugName; }
    public double getDosageMg()             { return dosageMg; }
    public int getRefillsRemaining()        { return refillsRemaining; }
    public boolean isPaid()                 { return paid; }

    public void setRefillsRemaining(int r)  { this.refillsRemaining = r; }
    public void setPaid(boolean paid)       { this.paid = paid; }
}
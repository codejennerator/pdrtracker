package rmg.pdrtracker.job.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JobDetailsModel implements Serializable {

    // Job
    String jobCreatorName;

    String jobName;

    Date jobDate;

    String jobDescription;

    // Insurance
    String insuranceCompanyName;

    String insuranceClaimNumber;

    // Customer
    String customerName;

    String customerPhone;

    // Vehicle
    String vin;

    String vehicleYear;

    String vehicleMake;

    String vehicleModel;

    String vehicleColor;

    public String getJobCreatorName() {
        return jobCreatorName;
    }

    public void setJobCreatorName(String jobCreatorName) {
        this.jobCreatorName = jobCreatorName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getJobDate() {
        if (jobDate ==  null) {
            jobDate = new Date();
        }
        return jobDate;
    }

    public void setJobDate(Date jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getInsuranceCompanyName() {
        return insuranceCompanyName;
    }

    public void setInsuranceCompanyName(String insuranceCompanyName) {
        this.insuranceCompanyName = insuranceCompanyName;
    }

    public String getInsuranceClaimNumber() {
        return insuranceClaimNumber;
    }

    public void setInsuranceClaimNumber(String insuranceClaimNumber) {
        this.insuranceClaimNumber = insuranceClaimNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVehicleYear() {
        return vehicleYear;
    }

    public void setVehicleYear(String vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

}

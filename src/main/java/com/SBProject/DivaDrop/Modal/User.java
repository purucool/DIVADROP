package com.SBProject.DivaDrop.Modal;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "User", uniqueConstraints = @UniqueConstraint(columnNames = "EmailId"))
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="Full Name",length=500)
    private String userName;

    @Column(name="EmailId",unique = true, length=500)
    private String emailAddress;

    @Column(name="Password", length=500)
    private String password;

    @Column(name="ConfirmPassword", length=500)
    private String confirmPassword;

    @Column(name="Address", length=1500)
    private String address;

    @Column(name="Pincode", length=10)
    private int pincode;


    @Column(name="PhoneNo", length=10)
    private String mobileNo;

    @Column(name="City",length=50)
    private String city;

    @Column(name="State",length=100)
    private String state;

    private String role;
    private String imgName;

    private Boolean isEnable;

    private Boolean accountNonLocked;
    private Integer failedAttempts;

    private Date lockTime;

    private String reset_token;

    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImgName() {
        return imgName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean enable) {
        isEnable = enable;
    }


    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public String getReset_token() {
        return reset_token;
    }

    public void setReset_token(String reset_token) {
        this.reset_token = reset_token;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", address='" + address + '\'' +
                ", pincode=" + pincode +
                ", mobileNo='" + mobileNo + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", role='" + role + '\'' +
                ", imgName='" + imgName + '\'' +
                ", isEnable=" + isEnable +
                ", accountNonLocked=" + accountNonLocked +
                ", failedAttempts=" + failedAttempts +
                '}';
    }
}

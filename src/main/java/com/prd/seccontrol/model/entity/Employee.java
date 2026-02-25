package com.prd.seccontrol.model.entity;

import com.prd.seccontrol.model.types.Country;
import com.prd.seccontrol.model.types.EmployeeType;
import com.prd.seccontrol.model.types.Gender;
import com.prd.seccontrol.model.types.IdentificationType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;

@Entity
public class Employee {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long userId;
  @OneToOne
  @JoinColumn(name = "userId",referencedColumnName = "id", insertable = false, updatable = false)
  private User user;
  private String firstName;
  private String lastName;
  private String mobilePhone;
  private String email;
  private String address;
  private Gender gender;
  private String documentNumber;
  private Country country;
  private LocalDate birthDate;
  private EmployeeType employeeType = EmployeeType.NONE;

  private IdentificationType identificationType;
  public Employee() {
  }

  public String getDocumentNumber() {
    return documentNumber;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  public void setDocumentNumber(String documentNumber) {
    this.documentNumber = documentNumber;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getMobilePhone() {
    return mobilePhone;
  }

  public void setMobilePhone(String mobilePhone) {
    this.mobilePhone = mobilePhone;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public IdentificationType getIdentificationType() {
    return identificationType;
  }

  public void setIdentificationType(IdentificationType identificationType) {
    this.identificationType = identificationType;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public EmployeeType getEmployeeType() {
    return employeeType;
  }

  public void setEmployeeType(EmployeeType employeeType) {
    this.employeeType = employeeType;
  }
}

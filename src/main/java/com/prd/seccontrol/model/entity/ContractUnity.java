package com.prd.seccontrol.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class ContractUnity {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;
  private Long clientContractId;
  @OneToOne
  @JoinColumn(name = "clientContractId", referencedColumnName = "id", updatable = false,
      insertable = false)
  private ClientContract clientContract;
  private Long unityId;
  @OneToOne
  @JoinColumn(name = "unityId", referencedColumnName = "id", updatable
      = false, insertable = false)
  private Unity unity;

  public ContractUnity() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ClientContract getClientContract() {
    return clientContract;
  }

  public void setClientContract(ClientContract clientContract) {
    this.clientContract = clientContract;
  }

  public Long getClientContractId() {
    return clientContractId;
  }

  public void setClientContractId(Long clientContractId) {
    this.clientContractId = clientContractId;
  }

  public Unity getUnity() {
    return unity;
  }

  public void setUnity(Unity unity) {
    this.unity = unity;
  }

  public Long getUnityId() {
    return unityId;
  }

  public void setUnityId(Long unityId) {
    this.unityId = unityId;
  }
}

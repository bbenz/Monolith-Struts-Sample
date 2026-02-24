package com.skishop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequest {
    @NotBlank private String userId;
    @NotBlank @Size(max = 50) private String label;
    @NotBlank @Size(max = 100) private String recipientName;
    @NotBlank @Size(max = 20) private String postalCode;
    @NotBlank @Size(max = 50) private String prefecture;
    @NotBlank @Size(max = 200) private String address1;
    @Size(max = 200) private String address2;
    @Size(max = 20) private String phone;
    private Boolean isDefault = Boolean.FALSE;

    public AddressRequest() {}
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getPrefecture() { return prefecture; }
    public void setPrefecture(String prefecture) { this.prefecture = prefecture; }
    public String getAddress1() { return address1; }
    public void setAddress1(String address1) { this.address1 = address1; }
    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
}

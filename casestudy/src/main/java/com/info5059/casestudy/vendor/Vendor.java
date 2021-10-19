package com.info5059.casestudy.vendor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
@Data
@RequiredArgsConstructor
public class Vendor {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private long Id;
 private String name;
 private String address1;
 private String city;
 private String province;
 private String postalcode;
 private String phone;
 private String type;
 private String email;
}
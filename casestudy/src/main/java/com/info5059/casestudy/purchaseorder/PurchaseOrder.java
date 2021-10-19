package com.info5059.casestudy.purchaseorder;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import javax.persistence.GenerationType;

@Entity
@Data
@RequiredArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Long vendorid;
    private BigDecimal amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime podate;
    @OneToMany(mappedBy = "poid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseOrderLineitem> items = new ArrayList<PurchaseOrderLineitem>();
}

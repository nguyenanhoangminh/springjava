package com.info5059.casestudy.purchaseorder;

import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
public class PurchaseOrderDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Long create(PurchaseOrder clientpurchaseorder) {
        PurchaseOrder realPurchaseOrder = new PurchaseOrder();
        realPurchaseOrder.setPodate(LocalDateTime.now());
        realPurchaseOrder.setVendorid(clientpurchaseorder.getVendorid());
        realPurchaseOrder.setAmount(clientpurchaseorder.getAmount());
        entityManager.persist(realPurchaseOrder);
        for (PurchaseOrderLineitem item : clientpurchaseorder.getItems()) {
            PurchaseOrderLineitem realItem = new PurchaseOrderLineitem();
            realItem.setPoid(realPurchaseOrder.getId());
            realItem.setProductid(item.getProductid());
            realItem.setQty(item.getQty());
            realItem.setPrice(item.getPrice());
            entityManager.persist(realItem);
        }
        return realPurchaseOrder.getId();
    }

    public PurchaseOrder findOne(Long id) {
        PurchaseOrder report = entityManager.find(PurchaseOrder.class, id);
        if (report == null) {
            throw new EntityNotFoundException("Can't find purchase order for ID " + id);
        }
        return report;
    }
}
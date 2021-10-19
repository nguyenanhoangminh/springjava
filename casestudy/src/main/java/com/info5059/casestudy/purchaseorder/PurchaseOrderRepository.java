package com.info5059.casestudy.purchaseorder;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
//@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "pos", path = "pos")
public interface PurchaseOrderRepository extends CrudRepository<PurchaseOrder, Long> {
}

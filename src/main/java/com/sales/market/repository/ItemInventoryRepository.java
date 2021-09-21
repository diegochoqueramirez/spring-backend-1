
package com.sales.market.repository;

import com.sales.market.model.ItemInventory;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemInventoryRepository extends GenericRepository<ItemInventory> {
    @Query("FROM ItemInventory i WHERE i.stockQuantity < i.lowerBoundThreshold")
    Optional<List<ItemInventory>> getMinItemInventories();

}

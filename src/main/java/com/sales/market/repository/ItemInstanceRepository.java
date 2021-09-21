/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.repository;


import com.sales.market.model.Item;
import com.sales.market.model.ItemInstance;
import com.sales.market.model.ItemInstanceStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ItemInstanceRepository extends GenericRepository<ItemInstance> {
    BigDecimal countAllByItemAndItemInstanceStatus(Item item, ItemInstanceStatus itemInstanceStatus);

    @Query("SELECT SUM(i.price) FROM ItemInstance i WHERE i.item = :item and i.itemInstanceStatus = :itemInstanceStatus ")
    BigDecimal sumPricesByItemAndItemInstanceStatus(@Param("item") Item item, @Param("itemInstanceStatus") ItemInstanceStatus itemInstanceStatus);

    ItemInstance findByIdentifier(String identifier);
}

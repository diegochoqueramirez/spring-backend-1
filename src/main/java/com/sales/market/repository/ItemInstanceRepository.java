/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.repository;


import com.sales.market.model.Item;
import com.sales.market.model.ItemInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ItemInstanceRepository extends GenericRepository<ItemInstance> {
    BigDecimal countAllByItem(Item item);

    @Query("SELECT SUM(i.price) FROM ItemInstance i WHERE i.item = :item")
    BigDecimal sumPricesByItem(@Param("item") Item item);

    ItemInstance findByIdentifier(String identifier);
}

/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.service;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInstance;

import java.math.BigDecimal;

public interface ItemInstanceService extends GenericService<ItemInstance> {
    BigDecimal countAllByItem(Item item);
    BigDecimal sumPricesByItem(Item item);
    ItemInstance findByIdentifier(String identifier);
}

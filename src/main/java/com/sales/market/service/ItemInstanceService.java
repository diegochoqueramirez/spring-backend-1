/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.service;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInstance;
import com.sales.market.model.ItemInstanceStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ItemInstanceService extends GenericService<ItemInstance> {
    BigDecimal countAllByItemAndItemInstanceStatus(Item item, ItemInstanceStatus itemInstanceStatus);
    BigDecimal sumPricesByItemAndItemInstanceStatus(Item item, ItemInstanceStatus itemInstanceStatus);
    ItemInstance findByIdentifier(String identifier);
    void saveAllByIdentifiers(String[] identifiers, Item item);
    void setState(String[] identifiers, ItemInstanceStatus itemInstanceStatus);
}

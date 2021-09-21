/**
 * @author: Diego Marcelo Choque Ramirez
 */

package com.sales.market.service;

import com.sales.market.model.ItemInventory;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.model.MovementType;

import java.util.List;
import java.util.Optional;

public interface ItemInventoryService extends GenericService<ItemInventory> {
    Optional<List<ItemInventory>> checkInventory();
    ItemInventory updateInventory(ItemInventoryEntry itemInventoryEntry);
}
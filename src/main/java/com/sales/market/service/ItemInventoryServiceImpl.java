/**
 * @author: Diego Marcelo Choque Ramirez
 */

package com.sales.market.service;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInventory;
import com.sales.market.model.ItemInventoryEntry;
import com.sales.market.model.MovementType;
import com.sales.market.repository.GenericRepository;
import com.sales.market.repository.ItemInventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemInventoryServiceImpl extends GenericServiceImpl<ItemInventory> implements ItemInventoryService {
    private final ItemInventoryRepository repository;
    private ItemInstanceServiceImpl itemInstanceService;
    private ItemService itemService;

    public ItemInventoryServiceImpl(ItemInventoryRepository repository, ItemService itemService, ItemInstanceServiceImpl itemInstanceService) {
        this.repository = repository;
        this.itemService = itemService;
        this.itemInstanceService = itemInstanceService;
    }

    @Override
    protected GenericRepository<ItemInventory> getRepository() {
        return repository;
    }

    @Override
    public Optional<List<ItemInventory>> checkInventory() {
        return repository.getMinItemInventories();
    }

    @Override
    public ItemInventory updateInventory(ItemInventoryEntry itemInventoryEntry, MovementType movementType) {
        return null;
    }

}
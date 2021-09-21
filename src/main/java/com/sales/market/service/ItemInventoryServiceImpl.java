/**
 * @author: Diego Marcelo Choque Ramirez
 */

package com.sales.market.service;

import com.sales.market.model.*;
import com.sales.market.repository.GenericRepository;
import com.sales.market.repository.ItemInventoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public ItemInventory save(ItemInventory itemInventory) {
        itemInventory.setStockQuantity(itemInstanceService.countAllByItemAndItemInstanceStatus(itemInventory.getItem(), ItemInstanceStatus.AVAILABLE));
        itemInventory.setTotalPrice(itemInstanceService.countAllByItemAndItemInstanceStatus(itemInventory.getItem(),
                ItemInstanceStatus.AVAILABLE).multiply(itemInstanceService.sumPricesByItemAndItemInstanceStatus(itemInventory.getItem(), ItemInstanceStatus.AVAILABLE)));

        return super.save(itemInventory);
    }

    @Override
    public ItemInventory updateInventory(ItemInventoryEntry itemInventoryEntry) {
        ItemInventory itemInventory = itemInventoryEntry.getItemInventory();
        MovementType movementType = itemInventoryEntry.getMovementType();
        BigDecimal quantity = itemInventoryEntry.getQuantity();
        String [] identifiers = itemInventoryEntry.getItemInstanceSkus().split(",");

        switch (movementType) {
            case BUY:
                itemInventory.setStockQuantity(itemInventory.getStockQuantity().add(quantity));
                itemInstanceService.saveAllByIdentifiers(identifiers, itemInventory.getItem());
                break;
            case REMOVED:
                itemInventory.setStockQuantity(itemInventory.getStockQuantity().subtract(quantity));
                itemInstanceService.setState(identifiers, ItemInstanceStatus.SCREWED);
                break;
            case SALE:
                itemInventory.setStockQuantity(itemInventory.getStockQuantity().subtract(quantity));
                itemInstanceService.setState(identifiers, ItemInstanceStatus.SOLD);
                break;
        }
        //verificar si es menor o igual
        return save(itemInventory);
    }
}
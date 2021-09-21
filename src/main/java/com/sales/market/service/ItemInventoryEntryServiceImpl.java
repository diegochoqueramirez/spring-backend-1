/**
 * @author: Diego Marcelo Choque Ramirez
 */

package com.sales.market.service;

import com.sales.market.model.*;
import com.sales.market.repository.GenericRepository;
import com.sales.market.repository.ItemInventoryEntryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemInventoryEntryServiceImpl extends GenericServiceImpl<ItemInventoryEntry> implements ItemInventoryEntryService {
    private final ItemInventoryEntryRepository repository;
    private final ItemInventoryService itemInventoryService;
    private final ItemInstanceService itemInstanceService;

    public ItemInventoryEntryServiceImpl(ItemInventoryEntryRepository repository, ItemInventoryService itemInventoryService, ItemInstanceService itemInstanceService) {
        this.repository = repository;
        this.itemInventoryService = itemInventoryService;
        this.itemInstanceService = itemInstanceService;
    }

    @Override
    protected GenericRepository<ItemInventoryEntry> getRepository() {
        return repository;
    }

//    @Override
//    public ItemInventoryEntry save(ItemInventoryEntry model) {
//        validateSave(model);
//        ItemInventoryEntry itemInventoryEntry = getRepository().save(model);
//
//        ItemInventory itemInventory = itemInventoryEntry.getItemInventory();
//        String[] skus = itemInventoryEntry.getItemInstanceSkus().split(",");
//
//        if (itemInventoryEntry.getMovementType() == MovementType.BUY) {
//
//            for (String sku: skus) {
//                ItemInstance itemInstance = new ItemInstance();
//                itemInstance.setItem(itemInventory.getItem());
//                itemInstance.setFeatured(true);
//                itemInstance.setPrice(5D);
//                itemInstance.setIdentifier(sku);
//                itemInstance.setItemInstanceStatus(ItemInstanceStatus.AVAILABLE);
//                itemInstanceService.save(itemInstance);
//            }
//
//            itemInventory.setItem(itemInventory.getItem());
//            itemInventory.setLowerBoundThreshold(itemInventory.getLowerBoundThreshold());
//            itemInventory.setUpperBoundThreshold(itemInventory.getUpperBoundThreshold());
//            itemInventory.setStockQuantity(itemInstanceService.countAllByItem(itemInventory.getItem()));
//            itemInventory.setTotalPrice(itemInstanceService.countAllByItem(itemInventory.getItem()).multiply(itemInstanceService.sumPricesByItem(itemInventory.getItem())));
//            itemInventoryService.save(itemInventory);
//        }
//
//        if (itemInventoryEntry.getMovementType() == MovementType.SALE) {
//
//            for (String sku: skus) {
//                ItemInstance itemInstance = itemInstanceService.findByIdentifier(sku);
//                itemInstance.setItemInstanceStatus(ItemInstanceStatus.SOLD);
//            }
//
//            itemInventory.setItem(itemInventory.getItem());
//            itemInventory.setLowerBoundThreshold(itemInventory.getLowerBoundThreshold());
//            itemInventory.setUpperBoundThreshold(itemInventory.getUpperBoundThreshold());
//            itemInventory.setStockQuantity(itemInstanceService.countAllByItem(itemInventory.getItem()));
//            itemInventory.setTotalPrice(itemInstanceService.countAllByItem(itemInventory.getItem()).multiply(itemInstanceService.sumPricesByItem(itemInventory.getItem())));
//            itemInventoryService.save(itemInventory);
//        }
//
//        return findById(itemInventoryEntry.getId());
//    }

    @Override
    public ItemInventoryEntry save(ItemInventoryEntry itemInventoryEntry) {
        super.save(itemInventoryEntry);
        itemInventoryService.updateInventory(itemInventoryEntry);
        return repository.save(itemInventoryEntry);
    }
}
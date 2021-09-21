/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market.service;

import com.sales.market.model.Item;
import com.sales.market.model.ItemInstance;
import com.sales.market.repository.GenericRepository;
import com.sales.market.repository.ItemInstanceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItemInstanceServiceImpl extends GenericServiceImpl<ItemInstance> implements ItemInstanceService {
    private final ItemInstanceRepository repository;
    private final ItemService itemService;

    public ItemInstanceServiceImpl(ItemInstanceRepository repository, ItemService itemService) {
        this.repository = repository;
        this.itemService = itemService;
    }

    @Override
    protected GenericRepository<ItemInstance> getRepository() {
        return repository;
    }

    @Override
    public ItemInstance bunchSave(ItemInstance itemInstance) {
        // here make all objects save other than this resource
        if (itemInstance.getItem() != null) {
            // todo habria que distinguir si permitiremos guardar y  actualizar o ambos mitando el campo id
            itemService.save(itemInstance.getItem());
        }
        return super.bunchSave(itemInstance);
    }

    @Override
    public BigDecimal countAllByItem(Item item) {
        return repository.countAllByItem(item);
    }

    @Override
    public BigDecimal sumPricesByItem(Item item) {
        return repository.sumPricesByItem(item);
    }

    @Override
    public ItemInstance findByIdentifier(String identifier) {
        return repository.findByIdentifier(identifier);
    }
}

/**
 * @author: Diego Marcelo Choque Ramirez
 */

package com.sales.market.model;

import com.sales.market.dto.ItemInventoryEntryDto;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class ItemInventoryEntry extends ModelBase<ItemInventoryEntryDto> {
    @ManyToOne
    private ItemInventory itemInventory;
    private MovementType movementType;
    private BigDecimal quantity;
    private String itemInstanceSkus; //skus

    public ItemInventory getItemInventory() {
        return itemInventory;
    }

    public void setItemInventory(ItemInventory itemInventory) {
        this.itemInventory = itemInventory;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getItemInstanceSkus() {
        return itemInstanceSkus;
    }

    public void setItemInstanceSkus(String itemInstanceSkus) {
        this.itemInstanceSkus = itemInstanceSkus;
    }
}

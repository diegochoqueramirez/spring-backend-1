/**
 * @author: Edson A. Terceros T.
 */

package com.sales.market;

import com.sales.market.model.*;
import com.sales.market.repository.BuyRepository;
import com.sales.market.repository.EmployeeRepository;
import com.sales.market.repository.ItemInstanceRepository;
import com.sales.market.service.*;
import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Component
public class DevelopmentBootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private final BuyRepository buyRepository;
    private final CategoryService categoryService;
    private final SubCategoryService subCategoryService;
    private final ItemService itemService;
    private final ItemInstanceService itemInstanceService;
    private final ItemInventoryService itemInventoryService;
    private final ItemInventoryEntryService itemInventoryEntryService;
    private EmployeeRepository employeeRepository;
    private UserService userService;
    private RoleService roleService;
    private ItemInstanceRepository itemInstanceRepository;

    SubCategory beverageSubCat = null;

    // injeccion evita hacer instancia   = new Clase();
    // bean pueden tener muchos campos y otros beans asociados


    public DevelopmentBootstrap(BuyRepository buyRepository, CategoryService categoryService,
                                SubCategoryService subCategoryService, ItemInstanceRepository itemInstanceRepository, ItemService itemService, ItemInstanceService itemInstanceService,
                                ItemInventoryService itemInventoryService, ItemInventoryEntryService itemInventoryEntryService, EmployeeRepository employeeRepository, UserService userService, RoleService roleService) {
        this.buyRepository = buyRepository;
        this.categoryService = categoryService;
        this.subCategoryService = subCategoryService;
        this.itemService = itemService;
        this.itemInstanceService = itemInstanceService;
        this.itemInventoryService = itemInventoryService;
        this.itemInventoryEntryService = itemInventoryEntryService;
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.itemInstanceRepository = itemInstanceRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("evento de spring");
        /*   duplicacion de codigo
        Buy buy = new Buy();
        buy.setValue(BigDecimal.TEN);
        buyRespository.save(buy);
        Buy buy2 = new Buy();
        buy2.setValue(BigDecimal.ONE);
        buyRespository.save(buy);*/

        persistBuy(BigDecimal.TEN);
        persistBuy(BigDecimal.ONE);
        persistCategoriesAndSubCategories();
        Item maltinItem = persistItems(beverageSubCat, "B-MALTIN", "Maltin");
        Item cocaItem = persistItems(beverageSubCat, "C-COCACOLA", "Cocacola");
        Item maltaItem = persistItems(beverageSubCat, "M-MALTA", "Malta");
        Item coronaItem = persistItems(beverageSubCat, "C-CORONA", "Corona");
        Item spriteItem = persistItems(beverageSubCat, "S-SPRITE", "Sprite");
        Item orientalItem = persistItems(beverageSubCat, "O-ORIENTAL", "Oriental");
        Item pepsiItem = persistItems(beverageSubCat, "P-PEPSI", "Pepsi");

        persistItemInstances(maltinItem);
        persistItemInstances(cocaItem);
        persistItemInstances(maltaItem);
        persistItemInstances(coronaItem);
        persistItemInstances(spriteItem);
        persistItemInstances(orientalItem);
        persistItemInstances(pepsiItem);

        ItemInventory itemInventory = createItemInventory(maltinItem, "10", "30", "8", "40");
        ItemInventory itemInventory1 = createItemInventory(cocaItem, "10", "30", "15", "40");
        ItemInventory itemInventory2 = createItemInventory(maltaItem, "10", "30", "5", "40");
        ItemInventory itemInventory3 = createItemInventory(coronaItem, "10", "30", "1", "40");
        ItemInventory itemInventory4 = createItemInventory(spriteItem, "10", "30", "4", "40");
        ItemInventory itemInventory5 = createItemInventory(orientalItem, "10", "30", "20", "40");
        ItemInventory itemInventory6 = createItemInventory(pepsiItem, "10", "30", "8", "40");

//        craeteItemInventoryEntry(itemInventory, "40", MovementType.BUY, "SKU-1231234, SKU-123421");

        initializeRoles();
        initializeEmployees();
    }

    private void craeteItemInventoryEntry(ItemInventory itemInventory, String quantity, MovementType movementType, String skus) {
        ItemInventoryEntry itemInventoryEntry = new ItemInventoryEntry();
        itemInventoryEntry.setItemInventory(itemInventory);
        itemInventoryEntry.setQuantity(new BigDecimal(quantity));
        itemInventoryEntry.setMovementType(movementType);
        itemInventoryEntry.setItemInstanceSkus(skus);
        itemInventoryEntryService.save(itemInventoryEntry);
    }

    private void initializeRoles() {
        createRole(RoleType.ADMIN.getId(), RoleType.ADMIN.getType());
        createRole(RoleType.GENERAL.getId(), RoleType.GENERAL.getType());
        createRole(RoleType.SUPERVISOR.getId(), RoleType.SUPERVISOR.getType());
    }



    private ItemInventory createItemInventory(Item item, String lower, String upper, String quantity, String price) {
        ItemInventory itemInventory = new ItemInventory();
        itemInventory.setItem(item);
        itemInventory.setLowerBoundThreshold(new BigDecimal(lower));
        itemInventory.setUpperBoundThreshold(new BigDecimal(upper));
        itemInventory.setStockQuantity(itemInstanceService.countAllByItemAndItemInstanceStatus(item, ItemInstanceStatus.AVAILABLE));
        itemInventory.setTotalPrice(itemInstanceService.countAllByItemAndItemInstanceStatus(item, ItemInstanceStatus.AVAILABLE).multiply(itemInstanceService.sumPricesByItemAndItemInstanceStatus(item, ItemInstanceStatus.AVAILABLE )));
        itemInventoryService.save(itemInventory);
        return itemInventory;
    }

    private Role createRole(long id, String roleName) {
        Role role = new Role();
        role.setId(id);
        role.setName(roleName);
        roleService.save(role);
        return role;
    }

    private void initializeEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            createEmployee("Edson", "Terceros", "edsonariel@gmail.com", false);
            createEmployee("Ariel", "Terceros", "ariel@gmail.com", false);
            createEmployee("System", "", "edson@gmail.com", true);
        }
    }

    private void createEmployee(String firstName, String lastName, String email, boolean system) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employeeRepository.save(employee);
        createUser(email, employee, system);
    }

    private void createUser(String email, Employee employee, boolean system) {
        User user = new User();
        Role role = new Role();
        HashSet<Role> roles = new HashSet<>();

        user.setEmail(email);
        user.setEnabled(true);
        user.setSystem(system);
        user.setPassword("$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.");
        user.setEmployee(employee);

        role.setId(1L);
        roles.add(role);
        user.setRoles(roles);
        userService.save(user);
    }


    private void persistItemInstances(Item maltinItem) {
        Random rdn = new Random();
        ItemInstance maltinItem1 = createItem(maltinItem, "SKU-" + String.valueOf(rdn.nextInt(10000000)), 5D);
        ItemInstance maltinItem2 = createItem(maltinItem, "SKU-" + String.valueOf(rdn.nextInt(10000000)), 5D);
        ItemInstance maltinItem3 = createItem(maltinItem, "SKU-"+ String.valueOf(rdn.nextInt(10000000)), 5D);
        ItemInstance maltinItem4 = createItem(maltinItem, "SKU-" + String.valueOf(rdn.nextInt(10000000)), 5D);
        itemInstanceService.save(maltinItem1);
        itemInstanceService.save(maltinItem2);
        itemInstanceService.save(maltinItem3);
        itemInstanceService.save(maltinItem4);
    }

    private ItemInstance createItem(Item maltinItem, String sku, double price) {
        ItemInstance itemInstance = new ItemInstance();
        itemInstance.setItem(maltinItem);
        itemInstance.setFeatured(true);
        itemInstance.setItemInstanceStatus(ItemInstanceStatus.AVAILABLE);
        itemInstance.setPrice(price);
        itemInstance.setIdentifier(sku);
        return itemInstance;
    }

    private Item persistItems(SubCategory subCategory, String code, String name) {
        Item item = new Item();
        item.setCode(code);
        item.setName(name);
        item.setSubCategory(subCategory);
        /*try {
            item.setImage(ImageUtils.inputStreamToByteArray(getResourceAsStream("/images/maltin.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return itemService.save(item);
    }

    private String getResourceAsString(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private InputStream getResourceAsStream(String resourceName) {
        try (InputStream inputStream = this.getClass().getResourceAsStream(resourceName)) {
            return inputStream;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void persistCategoriesAndSubCategories() {
        Category category = persistCategory();
        persistSubCategory("SUBCAT1-NAME", "SUBCAT1-CODE", category);
        beverageSubCat = persistSubCategory("BEVERAGE", "BEVERAGE-CODE", category);
    }

    private Category persistCategory() {
        Category category = new Category();
        category.setName("CAT1-NAME");
        category.setCode("CAT1-CODE");
        return categoryService.save(category);
    }

    private SubCategory persistSubCategory(String name, String code, Category category) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setCode(code);
        subCategory.setCategory(category);
        return subCategoryService.save(subCategory);
    }

    private void persistBuy(BigDecimal value) {
        Buy buy = new Buy();
        buy.setValue(value);
        buyRepository.save(buy);
    }


}

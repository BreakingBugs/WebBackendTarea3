package py.una.pol.web.tarea1.controller;

import py.una.pol.web.tarea1.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by codiumsa on 27/2/16.
 */
public class ItemController {
    private static ItemController instance = new ItemController();
    private Integer sequence = 1;
    private List<Item> items = new ArrayList<Item>();

    private ItemController() {
        //Mock product
        Item p = new Item();
        p.setName("Coke 500 ml");
        p.setStock(10);
        p.setPrice(5000.0);
        this.addItem(p);
    }

    public static ItemController getInstance() {
        return instance;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item p) {
        p.setId(sequence++);
        this.items.add(p);
    }

    public Item getItem(Integer id) {
        for(Item p : items) {
            if(p.getId() != null && p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Item updateItem(Integer id, Item itemWithChanges) {
        for(Item p : items) {
            if(p.getId() != null && p.getId().equals(id)) {
                if(itemWithChanges.getName() != null) {
                    p.setName(itemWithChanges.getName());
                }

                return p;
            }
        }
        return null;
    }

    public void removeItem(final Integer id) {
        items.removeIf(new Predicate<Item>() {
            public boolean test(Item item) {
                if(item.getId() != null && item.getId().equals(id)) {
                    return true;
                }
                return false;
            }
        });
    }

}

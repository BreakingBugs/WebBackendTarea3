package py.una.pol.web.tarea1.controller;

import py.una.pol.web.tarea1.model.Supplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by jordan on 2/28/16.
 */
public class SupplierController {
    private static SupplierController instance = new SupplierController();
    private Integer sequence = 1;
    private List<Supplier> suppliers = new ArrayList<Supplier>();

    private SupplierController() {
        Supplier s = new Supplier();
        s.setName("Coca Cola Company");
        this.addSupplier(s);
    }

    public static SupplierController getInstance() {
        return instance;
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void addSupplier(Supplier s) {
        s.setId(sequence++);
        this.suppliers.add(s);
    }

    public Supplier getSupplier(Integer id) {
        for (Supplier s : suppliers) {
            if (s.getId() != null && s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public Supplier updateSupplier(Integer id, Supplier supplierWithChanges) {
        Supplier s = this.getSupplier(id);
        if (s != null) {
            if (supplierWithChanges.getName() != null) {
                s.setName(supplierWithChanges.getName());
            }
        }
        return s;
    }

    public void removeSupplier(final Integer id) {
        suppliers.removeIf(new Predicate<Supplier>() {
            public boolean test(Supplier supplier) {
                return supplier.getId() != null && supplier.getId().equals(id);
            }
        });
    }
}

package py.una.pol.web.tarea1.controller;

import py.una.pol.web.tarea1.model.Item;
import py.una.pol.web.tarea1.model.Order;
import py.una.pol.web.tarea1.model.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by codiumsa on 27/2/16.
 */
public class ProviderController {
    private static ProviderController instance = new ProviderController();
    private Integer sequence = 1;
    private List<Provider> providers = new ArrayList<Provider>();

    private ProviderController() {
        //Mock provider
        Provider p = new Provider();
        p.setName("Distribuidora Gloria");
        this.addProvider(p);
    }

    public static ProviderController getInstance() {
        return instance;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void addProvider(Provider p) {
        p.setId(sequence++);

        checkItems(p);

        this.providers.add(p);
    }

    public boolean buyFromProvider(Integer providerId, List<Order> orders) {
        Provider p = this.getProvider(providerId);
        if(p == null) {
            return false;
        }

        for(Order o : orders) {
            Item i = ItemController.getInstance().getItem(o.getItem());
            if(i == null) {
                continue;
            }

            for(Integer itemId : p.getItems()) {
                if(i.getId().equals(itemId)) {
                    i.setStock(i.getStock() + o.getAmount());
                }
            }
        }

        return true;
    }

    private void checkItems(Provider p) {
        //comprobamos que todos los items asociados sean validos
        for(int i = 0; i < p.getItems().size(); i++) {
            Item item = ItemController.getInstance().getItem(p.getItems().get(i));
            if(item != null) {
                continue;
            }

            p.getItems().remove(i);
        }
    }

    public Provider getProvider(Integer id) {
        for(Provider p : providers) {
            if(p.getId() != null && p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public Provider updateProvider(Integer id, Provider providerWithChanges) {
        for(Provider p : providers) {
            if(p.getId() != null && p.getId().equals(id)) {
                if(providerWithChanges.getName() != null) {
                    p.setName(providerWithChanges.getName());
                }
                if(providerWithChanges.getItems() != null) {
                    p.setItems(providerWithChanges.getItems());
                    checkItems(p);
                }

                return p;
            }
        }
        return null;
    }

    public void removeProvider(final Integer id) {
        providers.removeIf(new Predicate<Provider>() {
            public boolean test(Provider provider) {
                return provider.getId() != null && provider.getId().equals(id);

            }
        });
    }

}

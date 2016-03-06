package py.una.pol.web.tarea2.controller;

import py.una.pol.web.tarea2.model.Item;
import py.una.pol.web.tarea2.model.Order;
import py.una.pol.web.tarea2.model.Provider;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by codiumsa on 27/2/16.
 */
@Stateless
public class ProviderController {
    @PersistenceContext(name = "Tarea2DS")
    EntityManager em;

    @Inject
    ItemController itemController;

    public List<Provider> getProviders() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Provider> cq = cb.createQuery(Provider.class);
        Root<Provider> root = cq.from(Provider.class);
        cq.select(root);
        TypedQuery<Provider> query = em.createQuery(cq);

        return query.getResultList();
    }

    public void addProvider(Provider p) {
        em.persist(p);
    }

    public boolean buyFromProvider(Integer providerId, List<Order> orders) {
        Provider p = this.getProvider(providerId);
        if(p == null) {
            return false;
        }

        for(Order o : orders) {
            Item i = itemController.getItem(o.getItem());
            if(i == null) {
                continue;
            }

            for(Item item : p.getItems()) {
                if(i.getId().equals(item.getId())) {
                    i.setStock(i.getStock() + o.getAmount());
                }
            }

        }

        return true;
    }

    private void checkItems(Provider p) {
        //comprobamos que todos los items asociados sean validos
        /*
        for(int i = 0; i < p.getItems().size(); i++) {
            Item item = itemController.getItem(p.getItems().get(i));
            if(item != null) {
                continue;
            }

            p.getItems().remove(i);
        }
        */
    }

    public Provider getProvider(Integer id) {
       return em.find(Provider.class, id);
    }

    public Provider updateProvider(Integer id, Provider providerWithChanges) {
        Provider p = getProvider(id);
        if(p!=null) {
            if(providerWithChanges.getName().compareTo(p.getName()) != 0) {
                p.setName(providerWithChanges.getName());
            }
        }
        return p;
    }

    public void removeProvider(final Integer id) {
        Provider p = getProvider(id);
        if(p!=null) {
            em.remove(p);
        }
    }

}

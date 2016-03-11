package py.una.pol.web.tarea3.controller;

import py.una.pol.web.tarea3.model.Item;
import py.una.pol.web.tarea3.model.Provider;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Stateless
public class ItemController {
    @PersistenceContext(name = "Tarea2DS")
    private EntityManager em;

    @Inject
    ProviderController providerController;

    public List<Item> getItems() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);
        cq.select(root);
        TypedQuery<Item> query = em.createQuery(cq);

        return query.getResultList();
    }

    public void addItem(Item p) {
        if (p.getProvider() != null) {
            Provider provider = providerController.getProvider(p.getProvider().getId());
            p.setProvider(provider);
        }
        em.persist(p);
    }

    public Item getItem(Integer id) {
        return em.find(Item.class, id);
    }

    public Item updateItem(Integer id, Item itemWithChanges) {
        Item c = getItem(id);
        if (c != null) {
            if (itemWithChanges.getName() != null && itemWithChanges.getName().compareTo(c.getName()) != 0) {
                c.setName(itemWithChanges.getName());
            }
            if (itemWithChanges.getPrice() != null && itemWithChanges.getPrice().compareTo(c.getPrice()) != 0) {
                c.setPrice(itemWithChanges.getPrice());
            }
            if (c.getProvider() == null ||
                    (itemWithChanges.getProvider() != null &&
                            itemWithChanges.getProvider().getId().compareTo(c.getProvider().getId()) != 0)) {
                c.setProvider(providerController.getProvider(itemWithChanges.getProvider().getId()));
            }
            em.merge(c);
        }
        return c;
    }

    public void removeItem(final Integer id) {
        Item c = getItem(id);
        if (c != null) {
            em.remove(c);
        }
    }

}

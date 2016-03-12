package py.una.pol.web.tarea3.controller;

import py.una.pol.web.tarea3.exceptions.DuplicateException;
import py.una.pol.web.tarea3.model.Item;
import py.una.pol.web.tarea3.model.Provider;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Stateless
public class ItemController {
    @PersistenceContext(name = "Tarea3DS")
    private EntityManager em;

    @Inject
    ProviderController providerController;

    @Inject
    DuplicateItemController duplicateItemController;

    @Inject
    private ItemController self;

    public List<Item> getItems() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);
        cq.select(root);
        TypedQuery<Item> query = em.createQuery(cq);

        return query.getResultList();
    }

    public Item getItemByName(String name) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);
        cq.select(root);
        cq.where(cb.equal(root.get("name"), name));
        TypedQuery<Item> query = em.createQuery(cq);
        Item i = query.getSingleResult();

        return i;
    }

    public void addItem(Item p) {
        if (p.getProvider() != null) {
            Provider provider = providerController.getProvider(p.getProvider().getId());
            p.setProvider(provider);
        }
        try {
            self.tryAddItem(p);
        } catch (DuplicateException e) {
            duplicateItemController.addDuplicate(p);
        }
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW) //BMT en una trx
    public void tryAddItem(Item p) throws DuplicateException {
        try {
            em.persist(p);
        } catch (PersistenceException e) {
            throw new DuplicateException();
        }
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

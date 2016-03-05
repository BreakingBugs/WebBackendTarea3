package py.una.pol.web.tarea2.controller;

import py.una.pol.web.tarea2.model.Customer;
import py.una.pol.web.tarea2.model.Item;
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
public class ItemController {
    @PersistenceContext(name = "Tarea2DS")
    EntityManager em;

    private Integer sequence = 1;
    private List<Item> items = new ArrayList<Item>();

    @PostConstruct
    public void init() {
        //Mock product
        Item p = new Item();
        p.setName("Coke 500 ml");
        p.setStock(10);
        p.setPrice(5000.0);
        this.addItem(p);
    }

    public List<Item> getItems() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Item> cq = cb.createQuery(Item.class);
        Root<Item> root = cq.from(Item.class);
        cq.select(root);
        TypedQuery<Item> query = em.createQuery(cq);

        return query.getResultList();
    }

    public void addItem(Item p) {
        em.persist(p);
    }

    public Item getItem(Integer id) {
        return em.find(Item.class, id);
    }

    public Item updateItem(Integer id, Item itemWithChanges) {
        Item c = getItem(id);
        if(c!= null) {
            if (itemWithChanges.getName().compareTo(c.getName()) != 0) {
                c.setName(itemWithChanges.getName());
            }
        }
        return c;
    }

    public void removeItem(final Integer id) {
        Item c = getItem(id);
        if(c!=null) {
            em.remove(c);
        }
    }

}

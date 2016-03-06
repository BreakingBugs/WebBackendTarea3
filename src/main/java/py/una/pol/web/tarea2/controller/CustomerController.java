package py.una.pol.web.tarea2.controller;

import py.una.pol.web.tarea2.model.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;


@Stateless
public class CustomerController {
    @PersistenceContext(name = "Tarea2DS")
    private EntityManager em;

    @Inject
    ItemController itemController;

    public List<Customer> getCustomers() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
        Root<Customer> root = cq.from(Customer.class);
        cq.select(root);
        TypedQuery<Customer> query = em.createQuery(cq);

        return query.getResultList();
    }

    public void addCustomer(Customer c) {
        em.persist(c);
    }

    public boolean sellToClient(Integer clientId, List<Order> orders) {
        Customer c = this.getCustomer(clientId);
        if (c == null) {
            return false;
        }

        for (Order o : orders) {
            Item i = itemController.getItem(o.getItem());
            if (i == null) {
                continue;
            }

            Integer amount = o.getAmount();
            if (i.getStock() < o.getAmount()) {
                amount = i.getStock();
            }

            Double total = i.getPrice() * amount;
            c.setAmountToPay(c.getAmountToPay() + total);
            em.merge(c);
            i.setStock(i.getStock() - amount);
            em.merge(i);
        }

        return true;
    }

    public boolean addPayment(Integer clientId, Payment payment) {
        Customer c = this.getCustomer(clientId);
        if (c == null) {
            return false;
        }

        Double monto = payment.getAmount();
        c.setAmountToPay(c.getAmountToPay() - monto);
        c.getPayments().add(payment);
        payment.setCustomer(c);
        em.persist(payment);
        return true;
    }

    public Customer getCustomer(Integer id) {
        return em.find(Customer.class, id);
    }

    public Customer updateCustomer(Integer id, Customer customerWithChanges) {
        Customer c = getCustomer(id);
        if (c != null) {
            if (customerWithChanges.getName().compareTo(c.getName()) != 0) {
                c.setName(customerWithChanges.getName());
            }
        }
        return c;
    }

    public void removeCustomer(final Integer id) {
        Customer c = getCustomer(id);
        if (c != null) {
            em.remove(c);
        }
    }
}

package py.una.pol.web.tarea2.controller;

import py.una.pol.web.tarea2.model.*;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Created by codiumsa on 28/2/16.
 */
@Stateless
public class CustomerController {
    @PersistenceContext(name = "Tarea2DS")
    EntityManager em;

    @EJB
    ItemController itemController;

    private static CustomerController instance = new CustomerController();
    private Integer sequence = 1;
    private List<Customer> customers = new ArrayList<Customer>();

    @PostConstruct
    public void init() {
        //Mock customer
        /*Customer c = new Customer();
        c.setName("John Doe");
        c.setAmountToPay(0.0);
        this.addCustomer(c);
        */
    }

    public static CustomerController getInstance() {
        return instance;
    }

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
        if(c == null) {
            return false;
        }

        for(Order o : orders) {
            Item i = itemController.getItem(o.getItem());
            if(i == null) {
                continue;
            }

            Integer amount = o.getAmount();
            if(i.getStock() < o.getAmount()) {
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
        if(c == null) {
            return false;
        }

        Double monto = payment.getAmount();
        c.setAmountToPay(c.getAmountToPay() - monto);
        //c.getPayments().add(payment);

        return true;
    }

    public Customer getCustomer(Integer id) {
        return em.find(Customer.class, id);
    }

    public Customer updateCustomer(Integer id, Customer customerWithChanges) {
        Customer c = getCustomer(id);
        if(c!= null) {
            if (customerWithChanges.getName().compareTo(c.getName()) != 0) {
                c.setName(customerWithChanges.getName());
            }
        }
        return c;
    }

    public void removeCustomer(final Integer id) {
        Customer c = getCustomer(id);
        if( c!= null) {
            em.remove(c);
        }
    }
}

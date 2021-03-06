package py.una.pol.web.tarea3.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.*;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.una.pol.web.tarea3.exceptions.DuplicateException;
import py.una.pol.web.tarea3.model.Item;
import py.una.pol.web.tarea3.model.Provider;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Stateless
public class ItemController {
    private static final int ITEMS_MAX = 100;

    private int batchSize = 20;


    @PersistenceContext(name = "Tarea3DS")
    private EntityManager em;

    @PersistenceUnit(unitName = "Tarea3DS")
    private SessionFactory sessionFactory;

    @Inject
    ProviderController providerController;

    @Inject
    DuplicateItemController duplicateItemController;

    @Inject
    private ItemController self;

    public StreamingOutput getItems() {
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                try {
                    fetchItemsAndStream(outputStream);
                } catch (Exception e) {
                    Logger logger = LoggerFactory.getLogger(ItemController.class);
                    logger.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }

    private void fetchItemsAndStream(OutputStream outputStream) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonGenerator jg = objectMapper.getFactory().createGenerator(outputStream);
        jg.writeStartArray();

        StatelessSession session = sessionFactory.openStatelessSession();
        try {
            ScrollableResults scrollableResults = session.createQuery("from Item order by id")
                    .setReadOnly(true).setCacheable(false).setFetchSize(ITEMS_MAX).scroll(ScrollMode.FORWARD_ONLY);
            while (scrollableResults.next()) {
                Item i = (Item) scrollableResults.get()[0];
                jg.writeObject(i);
            }
        } catch (Exception e) {
            Logger logger = LoggerFactory.getLogger(ItemController.class);
            logger.error(e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
        jg.writeEndArray();
        jg.close();
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

    public int batchAddItem(List<Item> items) {
        int i = 0;
        int duplicates = 0;
        for (Item item : items) {
            i++;
            addItem(item);
            if (i % batchSize == 0) {
                try {
                    em.flush();
                } catch (ConstraintViolationException e) {
                    Logger logger = LoggerFactory.getLogger(ItemController.class);
                    logger.error(e.getMessage());
                    e.printStackTrace();
                } finally {
                    em.clear();
                }
            }
        }
        return duplicates;
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

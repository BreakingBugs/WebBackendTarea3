package py.una.pol.web.tarea1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codiumsa on 28/2/16.
 */
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Double amountToPay;
    private List<Payment> payments = new ArrayList<Payment>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmountToPay() {
        return amountToPay;
    }

    public void setAmountToPay(Double amountToPay) {
        this.amountToPay = amountToPay;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}

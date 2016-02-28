package py.una.pol.web.tarea1.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by codiumsa on 28/2/16.
 */
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date date = new Date();
    private Double amount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

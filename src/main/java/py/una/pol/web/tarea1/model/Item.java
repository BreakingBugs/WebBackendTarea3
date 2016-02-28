package py.una.pol.web.tarea1.model;


import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

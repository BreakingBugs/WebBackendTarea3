package py.una.pol.web.tarea1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by codiumsa on 28/2/16.
 */
public class Provider implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private List<Integer> items = new ArrayList<Integer>();

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

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }
}

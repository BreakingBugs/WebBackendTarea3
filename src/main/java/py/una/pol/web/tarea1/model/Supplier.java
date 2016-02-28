package py.una.pol.web.tarea1.model;

import java.io.Serializable;

/**
 * Created by jordan on 2/28/16.
 */
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

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
}

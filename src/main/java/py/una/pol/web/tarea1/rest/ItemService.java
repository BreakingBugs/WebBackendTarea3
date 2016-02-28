package py.una.pol.web.tarea1.rest;

import py.una.pol.web.tarea1.controller.ItemController;
import py.una.pol.web.tarea1.model.Item;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/productos")
public class ItemService {


    @GET
    @Produces("application/json")
    public List<Item> getItems() {
        return ItemController.getInstance().getItems();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Item addItem(Item newItem) {
        ItemController.getInstance().addItem(newItem);
        return newItem;
    }

    @GET
    @Path("/{id: [0-9]*}")
    @Produces("application/json")
    public Response getItem(@PathParam("id") Integer id) {
        Item p = ItemController.getInstance().getItem(id);
        if(p != null) {
            return Response.ok(p).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id: [0-9]*}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateItem(@PathParam("id") Integer id, Item updatedItem) {
        Item p = ItemController.getInstance().updateItem(id, updatedItem);
        if(p != null) {
            return Response.ok(p).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id: [0-9]*}")
    public Response removeItem(@PathParam("id") Integer id) {
        ItemController.getInstance().removeItem(id);
        return Response.ok().build();
    }


}
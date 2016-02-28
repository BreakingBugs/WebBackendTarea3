package py.una.pol.web.tarea1.rest;

import py.una.pol.web.tarea1.controller.SupplierController;
import py.una.pol.web.tarea1.model.Supplier;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by jordan on 2/28/16.
 */
@Path("/proveedores")
public class SupplierService {

    @GET
    @Produces("application/json")
    public List<Supplier> getSuppliers() {
        return SupplierController.getInstance().getSuppliers();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Supplier addSupplier(Supplier s) {
        SupplierController.getInstance().addSupplier(s);
        return s;
    }

    @GET
    @Path("/{id: [0-9]+}")
    @Produces("application/json")
    public Response getSupplier(@PathParam("id") Integer id) {
        Supplier s = SupplierController.getInstance().getSupplier(id);
        if (s != null) {
            return Response.ok(s).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id: [0-9]+}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateSupplier(@PathParam("id") Integer id, Supplier updatedSupplier) {
        Supplier s = SupplierController.getInstance().updateSupplier(id, updatedSupplier);
        if (s != null)
            return Response.ok(s).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id: [0-9]+}")
    public Response removeSupplier(@PathParam("id") Integer id) {
        SupplierController.getInstance().removeSupplier(id);
        return Response.ok().build();
    }

}

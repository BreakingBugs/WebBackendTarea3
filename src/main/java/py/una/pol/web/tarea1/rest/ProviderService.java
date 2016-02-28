package py.una.pol.web.tarea1.rest;

import py.una.pol.web.tarea1.controller.ProviderController;
import py.una.pol.web.tarea1.model.Order;
import py.una.pol.web.tarea1.model.Provider;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/proveedores")
public class ProviderService {


    @GET
    @Produces("application/json")
    public List<Provider> getProviders() {
        return ProviderController.getInstance().getProviders();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Provider addProvider(Provider newProvider) {
        System.out.println("newProvider: ");
        System.out.println(newProvider.getName());
        ProviderController.getInstance().addProvider(newProvider);
        return newProvider;
    }

    @POST
    @Path("/{id: [0-9]*}/buy")
    @Consumes("application/json")
    @Produces("application/json")
    public Response sell(List<Order> orders, @PathParam("id") Integer providerId) {
        if(ProviderController.getInstance().buyFromProvider(providerId, orders)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id: [0-9]*}")
    @Produces("application/json")
    public Response getProvider(@PathParam("id") Integer id) {
        Provider p = ProviderController.getInstance().getProvider(id);
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
    public Response updateProvider(@PathParam("id") Integer id, Provider updatedProvider) {
        Provider p = ProviderController.getInstance().updateProvider(id, updatedProvider);
        if(p != null) {
            return Response.ok(p).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id: [0-9]*}")
    public Response removeProvider(@PathParam("id") Integer id) {
        ProviderController.getInstance().removeProvider(id);
        return Response.ok().build();
    }


}
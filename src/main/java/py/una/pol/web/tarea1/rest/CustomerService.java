package py.una.pol.web.tarea1.rest;

import py.una.pol.web.tarea1.controller.CustomerController;
import py.una.pol.web.tarea1.controller.ProviderController;
import py.una.pol.web.tarea1.model.Customer;
import py.una.pol.web.tarea1.model.Order;
import py.una.pol.web.tarea1.model.Provider;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by codiumsa on 28/2/16.
 */
@Path("/clientes")
public class CustomerService {
    @GET
    @Produces("application/json")
    public List<Customer> getCustomers() {
        return CustomerController.getInstance().getCustomers();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Customer addCustomer(Customer newCustomer) {
        CustomerController.getInstance().addCustomer(newCustomer);
        return newCustomer;
    }

    @POST
    @Path("/{id: [0-9]*}/sell")
    @Consumes("application/json")
    @Produces("application/json")
    public Response sell(List<Order> orders, @PathParam("id") Integer customerId) {
        if(CustomerController.getInstance().sellToClient(customerId, orders)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{id: [0-9]*}")
    @Produces("application/json")
    public Response getCustomer(@PathParam("id") Integer id) {
        Customer c = CustomerController.getInstance().getCustomer(id);
        if(c != null) {
            return Response.ok(c).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id: [0-9]*}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateCustomer(@PathParam("id") Integer id, Customer updatedCustomer) {
        Customer c = CustomerController.getInstance().updateCustomer(id, updatedCustomer);
        if(c != null) {
            return Response.ok(c).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id: [0-9]*}")
    public Response removeCustomer(@PathParam("id") Integer id) {
        CustomerController.getInstance().removeCustomer(id);
        return Response.ok().build();
    }
}

package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.objects.ErrorObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.core.Response;
import java.io.Serializable;

public abstract class Resource {

    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String MISSING_PARAMETER = "Parameter missing";

    /**
     * Returns a 200 OK Response with the Entity
     *
     * @param data Entity
     * @return Response
     */
    Response success(Object data) {
        Gson response = new GsonBuilder().setPrettyPrinting().create();
        return Response.status(Response.Status.OK).entity(response.toJson(data)).build();
    }

    /**
     * Returns a 201 Created Response with the created Entity
     *
     * @param data Entity that was created
     * @return Response
     */
    Response created(Object data) {
        Gson response = new GsonBuilder().setPrettyPrinting().create();
        return Response.status(Response.Status.CREATED).entity(response.toJson(data)).build();
    }

    /**
     * Returns a 204 No Content Response
     *
     * @return Response
     */
    Response deleted() {
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    /**
     * Returns a 404 Not Found Response
     *
     * @param entityName Name of the entity
     * @param id         Id that is being requested
     * @return Response
     */
    Response notFound(String entityName, long id) {
        String details = String.format("%s not found with id: %d", entityName, id);
        return error(Response.Status.NOT_FOUND, new ErrorObject(Resource.ENTITY_NOT_FOUND, details));
    }

    /**
     * Returns a formatted error response
     *
     * @param status the http response status type
     * @param errors what error(s) occurred
     * @return Response
     */
    Response error(Response.StatusType status, ErrorObject... errors) {
        Gson response = new GsonBuilder().setPrettyPrinting().create();
        if (errors.length > 0) {
            Object error = (errors.length > 1) ? new ErrorResponse(errors) : errors[0];
            return Response.status(status).entity(response.toJson(error)).build();
        }
        return Response.status(status).build();
    }

    /**
     * ErrorResponse class is used privately to properly format error responses
     */
    private class ErrorResponse implements Serializable {

        private static final long serialVersionUID = 493459904338441045L;

        private Object errors;

        /**
         * ErrorResponse constructor
         *
         * @param errors Object
         */
        ErrorResponse(Object errors) {
            this.errors = errors;
        }
    }

}

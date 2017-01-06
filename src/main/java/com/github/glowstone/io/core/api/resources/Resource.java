package com.github.glowstone.io.core.api.resources;

import com.github.glowstone.io.core.objects.ErrorObject;
import com.google.gson.Gson;

import javax.ws.rs.core.Response;
import java.io.Serializable;

public abstract class Resource {

    public static final String ENTITY_NOT_FOUND = "Entity not found";
    public static final String MISSING_PARAMETER = "Parameter missing";

    /**
     * Returns a formatted successful response
     *
     * @param data Response
     * @return Response
     */
    protected Response success(Object data) {
        Gson response = new Gson();
        return Response.status(Response.Status.OK).entity(response.toJson(new SuccessfulResponse(data))).build();
    }

    /**
     * Returns a formatted error response
     *
     * @param status the http response status type
     * @param errors what error(s) occurred
     * @return Response
     */
    protected Response error(Response.StatusType status, ErrorObject... errors) {
        Gson response = new Gson();
        return Response.status(status).entity(response.toJson(new ErrorResponse(errors))).build();
    }

    /**
     * SuccessfulResponse class is used privately to properly format successful responses
     */
    private class SuccessfulResponse implements Serializable {

        private static final long serialVersionUID = -3804910347266101942L;

        private Object data;

        /**
         * SuccessfulResponse constructor
         *
         * @param data Object
         */
        SuccessfulResponse(Object data) {
            this.data = data;
        }
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

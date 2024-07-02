package com.testquack.api.resources;

import com.testquack.dal.DokimionLogger;
import com.testquack.api.utils.FilterUtils;
import com.testquack.beans.Entity;
import com.testquack.beans.Event;
import com.testquack.beans.Filter;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Collection;

import static javax.ws.rs.core.Response.ok;

public abstract class BaseCrudResource<E extends Entity> extends BaseResource<E> {


    @Override
    protected Filter initFilter(HttpServletRequest hsr) {
        return FilterUtils.initFilter(request);
    }

    @GET
    @Path("/")
    public Collection<E> findFiltered(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId) {
        return getService().findFiltered(getUserSession(), projectId, initFilter(request));
    }

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Find entity by id", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Entity not found"),
            @ApiResponse(code = 403, message = "Access denied to the entity"),
            @ApiResponse(code = 200, message = "Successful operation")
    })
    public E findOne(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId,
            @ApiParam(value = "Entity Id", required = true) @PathParam("id") String id) {
DokimionLogger.info("BaseCrudResource::findOne() - projectId: " + projectId);
DokimionLogger.info("BaseCrudResource::findOne() - entity id: " + id);
        //return getService().findOne(getUserSession(), projectId, id);

        E entity = getService().findOne(getUserSession(), projectId, id);
DokimionLogger.info("BaseCrudResource::findOne - entity: " + entity);

        return entity;
    }

    @POST
    @Path("/")
    @ApiOperation(value = "Create entity", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Access denied to the entity"),
            @ApiResponse(code = 200, message = "Created entity")
    })
    public E create(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId,
            @ApiParam(value = "Entity", required = true) E entity) {
DokimionLogger.info("BaseCrudResource::create() - entity: " + entity);
        //return getService().save(getUserSession(), projectId, entity);
DokimionLogger.info("BaseCrudResource::create - user session: " + getUserSession());
        E new_entity = getService().save(getUserSession(), projectId, entity);
DokimionLogger.info("BaseCrudResource::create - new entity: " + new_entity);
        return new_entity;
    }

    @PUT
    @Path("/")
    @ApiOperation(value = "Update entity", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Access denied to the entity"),
            @ApiResponse(code = 200, message = "Updated entity")
    })
    public E update(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId,
            @ApiParam(value = "Entity", required = true) E entity) {
        return getService().save(getUserSession(), projectId, entity);
    }


    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "Delete entity", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Access denied to the entity"),
            @ApiResponse(code = 200, message = "Successful operation")
    })
    public Response delete(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId,
            @ApiParam(value = "Id", required = true) @PathParam("id") String id) {

DokimionLogger.info("BaseCrudResource:delete - projectId, launchId: " + projectId + ", " + id);

        getService().delete(getUserSession(), projectId, id);

DokimionLogger.info("BaseCrudResource:delete - after call to service.delete");

        return ok().build();
    }

    @GET
    @Path("/count")
    @ApiOperation(value = "Count", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = long.class)
    })
    public long count(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId){
        return getService().count(getUserSession(), projectId, initFilter(request));
    }

    @DELETE
    @Path("/")
    @ApiOperation(value = "Delete entity", notes = "")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Access denied to the entity"),
            @ApiResponse(code = 200, message = "Successful operation")
    })
    public Response delete(@ApiParam(value = "Project Id", required = true) @PathParam("projectId") String projectId) {
        getService().delete(getUserSession(), projectId, initFilter(request));
        return Response.ok().build();
    }

}

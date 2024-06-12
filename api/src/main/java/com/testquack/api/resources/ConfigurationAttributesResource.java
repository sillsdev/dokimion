package com.testquack.api.resources;

import com.testquack.beans.Filter;
import com.testquack.beans.ConfigurationAttributes;
import com.testquack.services.BaseService;
import com.testquack.services.UserService;
import com.testquack.services.ConfigurationAttributesService;
import com.testquack.api.utils.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.greatbit.whoru.auth.SessionProvider;
import ru.greatbit.whoru.auth.AuthProvider;
import ru.greatbit.whoru.auth.Session;
import ru.greatbit.whoru.auth.Person;
import ru.greatbit.whoru.jaxrs.Authenticable;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.json.*;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.core.Response.ok;

@Authenticable
@Path("/configurationattributes")
public class ConfigurationAttributesResource extends BaseResource<ConfigurationAttributes> {

  @Autowired
  private ConfigurationAttributesService service;

  @Override
  protected Filter initFilter(HttpServletRequest hsr) {
      return FilterUtils.initFilter(request);
  }

  @Override
  protected BaseService<ConfigurationAttributes> getService() {
    return service;
  }


  @GET
  @Path("/getall/{project}")
  @ApiOperation(value = "Find all entities", notes = "")
  @ApiResponses(value = {
          @ApiResponse(code = 400, message = "Entity not found"),
          @ApiResponse(code = 403, message = "Access denied to the Entity"),
          @ApiResponse(code = 200, message = "Successful operation")
  })
  public Collection<ConfigurationAttributes> getAllConfigurationAttributes(
         @PathParam("project") String project) {

    System.out.println("ConfigurationAttributesResource::getAllConfigurationAttributes");
    System.out.flush();

    ConfigurationAttributesService configurationAttributesService = (ConfigurationAttributesService)getService();
    List<ConfigurationAttributes> dpaList = configurationAttributesService.findFiltered(getUserSession(), project,  new Filter().withField("project", project));

    System.out.println("ConfigAttributesResource::getall - dpaList: " + dpaList);
    System.out.flush();

    
    return dpaList;

  }

}




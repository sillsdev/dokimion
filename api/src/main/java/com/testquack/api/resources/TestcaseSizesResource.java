package com.testquack.api.resources;

import com.testquack.dal.DokimionLogger;
import com.testquack.beans.Filter;
import com.testquack.beans.TestcaseSizes;
import com.testquack.services.BaseService;
import com.testquack.services.TestcaseSizesService;
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
import java.util.LinkedList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

import static javax.ws.rs.core.Response.ok;

@Authenticable
@Path("/testcasesizes")
public class TestcaseSizesResource extends BaseResource<TestcaseSizes> {

  @Autowired
  private TestcaseSizesService service;

  @Override
  protected Filter initFilter(HttpServletRequest hsr) {
      return FilterUtils.initFilter(request);
  }

  @Override
  protected BaseService<TestcaseSizes> getService() {
    return service;
  }

  @GET
  @Path("/getalltcsizes")
  @ApiOperation(value = "Find all entities", notes = "")
  @ApiResponses(value = {
          @ApiResponse(code = 400, message = "Entity not found"),
          @ApiResponse(code = 403, message = "Access denied to the entity"),
          @ApiResponse(code = 200, message = "Successful operation")
  })

  public Collection<TestcaseSizes> getAllTestcaseSizes() {

    DokimionLogger.info("TestcaseSizesResource::getAllTestcaseSizes");

DokimionLogger.info("getAllTestcaseSizes::filter - " + initFilter(request));

    Collection<TestcaseSizes> collTCSizes = getService().findFiltered(getUserSession(), null, new Filter());

    for (TestcaseSizes tcSize : collTCSizes) {
        DokimionLogger.info("TestcaseSizesResource.findFiltered - tcSize: " + tcSize);
     }
/*
   TestcaseSizesService tcSizesService = (TestcaseSizesService)getService();
   Set<String> tcSizesSet = tcSizesService.findAll().stream().map(
                            TestcaseSizes::getTcsName).collect(Collectors.toSet());

   Collection<TestcaseSizes> collTCSizes = new LinkedList<TestcaseSizes>();
   for (String tcSizeName : tcSizesSet) {
      DokimionLogger.info("TestcaseSizes::getallTestcaseSizes::tcSize: " + tcSizeName);
      System.out.flush();
      TestcaseSizes tcSize = new TestcaseSizes();
      tcSize.setName(tcSizeName);
      collTCSizes.add(tcSize);
   }
*/

    return collTCSizes;

  }

}





package com.pfc.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.pfc.handler.domain.ApiRequest;
import com.pfc.handler.domain.ApiResponse;
import com.pfc.service.IbanValidatorService;
import com.pfc.service.domain.IbanValidation;
import com.pfc.util.CountriesReader;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.*;

public class IbanValidatorHandler extends AbstractHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IbanValidatorHandler.class);
    private static final String IBAN_API = "/iban";
    private final Gson gson;
    private final IbanValidatorService ibanValidatorService;

    public IbanValidatorHandler() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.ibanValidatorService = new IbanValidatorService(new CountriesReader().getIbanLengths());
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        baseRequest.setHandled(true);
        response.setContentType("application/json;charset=UTF-8");
        if (!HttpMethod.POST.asString().equalsIgnoreCase(request.getMethod())) {
            response.setStatus(SC_METHOD_NOT_ALLOWED);
            response.getWriter().println(gson.toJson(new ApiResponse(request.getMethod() + " not allowed")));
        } else {
            if (IBAN_API.equals(target)) {
                try {
                    Optional<ApiRequest> apiRequestOpt = Optional.ofNullable(gson.fromJson(request.getReader(), ApiRequest.class));
                    if (apiRequestOpt.isPresent()) {
                        IbanValidation validation = ibanValidatorService.isValid(apiRequestOpt.get().getIban());
                        response.setStatus(validation.isValid() ? SC_OK : SC_BAD_REQUEST);
                        response.getWriter().println(gson.toJson(new ApiResponse(validation.getMessage())));
                    } else {
                        response.setStatus(SC_BAD_REQUEST);
                        response.getWriter().println(gson.toJson(new ApiResponse("Empty body")));
                    }
                } catch (JsonSyntaxException | JsonIOException jsonEx) {
                    LOG.error("Unable to process the request body", jsonEx);
                    response.setStatus(SC_BAD_REQUEST);
                    response.getWriter().println(gson.toJson(new ApiResponse(jsonEx.getMessage())));
                }
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println(gson.toJson(new ApiResponse("Endpoint not found")));
            }
        }
    }

}

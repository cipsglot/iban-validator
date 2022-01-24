package com.pfc.handler;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IbanValidatorHandlerTest {

    private static Server server;

    @BeforeAll
    static void beforeAll() throws Exception {
        server = new Server(8091);
        server.setHandler(new IbanValidatorHandler());
        server.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        server.stop();
    }

    @Test
    @DisplayName("Should return Method Not Allowed if is not a POST")
    public void methodNotAllowed() throws IOException {

        HttpUriRequest request = new HttpGet("http://localhost:8091");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, httpResponse.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("GET not allowed"));
    }

    @Test
    @DisplayName("Should return Endpoint Not found if is not a /iban ")
    public void notFoundEndpoint() throws IOException {

        HttpUriRequest request = new HttpPost("http://localhost:8091/fake");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, httpResponse.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("Endpoint not found"));
    }

    @Test
    @DisplayName("Should return bad request if the json is not parsable")
    public void badRequestJsonException() throws IOException {

        HttpPost httpPost = new HttpPost("http://localhost:8091/iban");

        String json = "{\"iban:1dsdas}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);

        assertEquals(HttpStatus.SC_BAD_REQUEST, httpResponse.getStatusLine().getStatusCode());
        assertTrue(EntityUtils.toString(httpResponse.getEntity()).contains("MalformedJsonException"));
    }


}
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
        // TODO: move to map and check the attribute
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        assertTrue(jsonResponse.contains("GET not allowed"));
        assertTrue(jsonResponse.contains("false"));
    }

    @Test
    @DisplayName("Should return Endpoint Not found if is not a /iban ")
    public void notFoundEndpoint() throws IOException {

        HttpUriRequest request = new HttpPost("http://localhost:8091/fake");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, httpResponse.getStatusLine().getStatusCode());
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        // TODO: move to map and check the attribute
        assertTrue(jsonResponse.contains("Endpoint not found"));
        assertTrue(jsonResponse.contains("false"));
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
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        // TODO: move to map and check the attribute
        assertTrue(jsonResponse.contains("MalformedJsonException"));
        assertTrue(jsonResponse.contains("false"));
    }

    @Test
    @DisplayName("Should return ok if the IBAN is not valid and the message")
    public void badRequestIbanNotValid() throws IOException {

        HttpPost httpPost = new HttpPost("http://localhost:8091/iban");

        String json = "{\"iban\": \"AT26 3621 8487 6967 5322\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);

        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        // TODO: move to map and check the attribute
        assertTrue(jsonResponse.contains("IBAN is not valid"));
        assertTrue(jsonResponse.contains("false"));
    }

    @Test
    @DisplayName("Should return ok if the IBAN is valid")
    public void okIbanValid() throws IOException {

        HttpPost httpPost = new HttpPost("http://localhost:8091/iban");

        String json = "{\"iban\": \"AT26 3621 8477 6967 5322\"}";
        StringEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(httpPost);

        assertEquals(HttpStatus.SC_OK, httpResponse.getStatusLine().getStatusCode());
        String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
        // TODO: move to map and check the attribute
        assertTrue(jsonResponse.contains("IBAN is valid"));
        assertTrue(jsonResponse.contains("true"));
    }

}
package com.pfc;

import com.pfc.handler.IbanValidatorHandler;
import org.eclipse.jetty.server.Server;

public class RestServer {

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8091);
        server.setHandler(new IbanValidatorHandler());
        server.start();
        server.join();
    }
}

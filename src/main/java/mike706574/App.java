package mike706574;

import java.io.IOException;
import java.io.OutputStream;

import java.net.InetSocketAddress;

import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class App {
    public static void main( String[] args ) {
        new App().run();
    }

    public HttpServer run() {
        try {
            HttpServer server = HttpServer.create( new InetSocketAddress( 8000 ), 0 );
            server.createContext( "/hello", new AppHandler() );
            server.start();
            return server;
        }
        catch( IOException ex ) {
            throw new RuntimeException( ex );
        }
    }

    static class AppHandler implements HttpHandler {
        @Override
        public void handle( HttpExchange exchange ) throws IOException {
            String response = "Hello, world!";
            exchange.sendResponseHeaders( 200, response.length() );
            OutputStream os = exchange.getResponseBody();
            os.write( response.getBytes() );
            os.close();
        }
    }
}
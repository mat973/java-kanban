package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class TasksHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().substring(1).split("/");
        switch (method){
            case "GET":
                exchange.sendResponseHeaders(200, 0);
                break;
            case "POST":
                break;
            case "DELETE":
                break;
            default:
                exchange.sendResponseHeaders(404, 0);
        }
    }
}

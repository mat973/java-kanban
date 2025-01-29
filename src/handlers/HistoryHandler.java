package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import typeTokens.GsonAdapters;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHandler implements HttpHandler {
    private TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().substring(1).split("/");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new GsonAdapters.LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new GsonAdapters.DurationAdapter())
                .create();
        int statusCode;
        String body;

        if (method.equals("GET") && splitPath.length == 1) {
            statusCode = 200;
            body = gson.toJson(manager.getHistoryManager().getHistory());
        } else {
            statusCode = 404;
            body = "Мы не знаем таких запросов.";
        }
        HandlersMessageSender.sendText(exchange, body, statusCode);
    }

}

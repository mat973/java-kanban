package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.EpicDto;
import exeptions.EpicNotExistException;
import managers.TaskManager;
import task.Epic;
import util.GsonAdapters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class EpicTasksHandler implements HttpHandler {
    private TaskManager manager;

    public EpicTasksHandler(TaskManager manager) {
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
        switch (method) {
            case "GET":
                if (splitPath.length == 1) {
                    statusCode = 200;
                    body = gson.toJson(manager.getEpicTasks());
                    break;
                } else if (splitPath.length == 2) {
                    int id;
                    try {
                        id = Integer.parseInt(splitPath[1]);
                    } catch (NumberFormatException e) {
                        statusCode = 404;
                        body = e.getMessage();
                        break;
                    }
                    Optional<Epic> epic = manager.getEpicById(id);
                    if (epic.isPresent()) {
                        statusCode = 200;
                        body = gson.toJson(epic.get());
                    } else {
                        statusCode = 404;
                        body = "Эпической задачи с таким c id: " + id + " не сущесвует.";
                    }
                } else if (splitPath.length == 3) {
                    if (splitPath[2].equals("subtasks")) {
                        int id;
                        try {
                            id = Integer.parseInt(splitPath[1]);
                        } catch (NumberFormatException e) {
                            statusCode = 404;
                            body = e.getMessage();
                            break;
                        }
                        Optional<Epic> epic = manager.getEpicById(id);
                        if (epic.isPresent()) {
                            statusCode = 200;
                            body = gson.toJson(epic.get().getSubtasks());
                        } else {
                            statusCode = 404;
                            body = "Эпической задачи с таким c id: " + id + " не сущесвует.";
                        }
                    } else {
                        statusCode = 404;
                        body = "По данному пути нет endpoint";
                        break;
                    }
                } else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            case "POST":
                if (splitPath.length == 1) {
                    EpicDto epicDto = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8), EpicDto.class);
                    if (epicDto.getId() == null) {
                        body = manager.createEpic(epicDto).toString();
                        statusCode = 200;
                        break;
                    } else {
                        try {
                            body = manager.changeEpic(epicDto).toString();
                            statusCode = 200;
                        } catch (EpicNotExistException e) {
                            statusCode = 404;
                            body = "Эпическая задача не была найдена";
                            break;
                        }
                    }
                } else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            case "DELETE":
                if (splitPath.length == 1) {
                    manager.removeAllEpics();
                    statusCode = 202;
                    body = "Список эпиков был отчищен";
                    break;
                } else if (splitPath.length == 2) {
                    int id;
                    try {
                        id = Integer.parseInt(splitPath[1]);
                    } catch (NumberFormatException e) {
                        statusCode = 404;
                        body = e.getMessage();
                        break;
                    }
                    try {
                        manager.removeEpicById(id);
                    } catch (EpicNotExistException e) {
                        body = "Эпическая задача c id: " + id + " не найдена.";
                        statusCode = 404;
                        break;
                    }
                    statusCode = 200;
                    body = "Эпическая задача c id: " + id + " была удалена.";
                } else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            default: {
                statusCode = 404;
                body = "Таких методов мы не знаем.";
            }

        }
        HandlersMessageSender.sendText(exchange, body, statusCode);
    }
}

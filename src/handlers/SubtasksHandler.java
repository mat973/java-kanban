package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.SubtaskDto;
import exeptions.EpicNotExistException;
import exeptions.SubtaskNotFoundException;
import exeptions.TaskIntersectionException;
import managers.TaskManager;
import task.Subtask;
import typeTokens.GsonAdapters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubtasksHandler implements HttpHandler {
    private TaskManager manager;

    public SubtasksHandler(TaskManager manager) {
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
                    body = gson.toJson(manager.getSubTasks());
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
                    Optional<Subtask> subtask = manager.getSubtaskById(id);
                    if (subtask.isPresent()) {
                        statusCode = 200;
                        body = gson.toJson(subtask.get());
                    } else {
                        statusCode = 404;
                        body = "Подзадачи с таким c id: " + id + " не сущесвует.";
                    }
                } else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            case "POST":
                if (splitPath.length == 1) {
                    SubtaskDto subtaskDto = gson.fromJson(new String(exchange.getRequestBody().readAllBytes(),
                            StandardCharsets.UTF_8), SubtaskDto.class);
                    if (subtaskDto.getId() == null) {
                        try {
                            body = manager.createSubTusk(subtaskDto).toString();
                        } catch (TaskIntersectionException e) {
                            statusCode = 400;
                            body = e.getMessage();
                            break;
                        } catch (EpicNotExistException e) {
                            statusCode = 404;
                            body = "У подзадачи должна быть существующая подзадача";
                            break;
                        }

                        statusCode = 200;
                        break;
                    } else {
                        try {
                            body = manager.changeSubTask(subtaskDto).toString();
                            statusCode = 200;
                        } catch (TaskIntersectionException e) {
                            statusCode = 406;
                            body = e.getMessage();
                            break;
                        } catch (SubtaskNotFoundException e) {
                            statusCode = 404;
                            body = "ползадачи с тайим id не найдено.";
                            break;
                        } catch (EpicNotExistException e) {
                            statusCode = 404;
                            body = "У подзадачи должна быть существующая подзадача";
                        }
                    }
                } else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            case "DELETE":
                if (splitPath.length == 1) {
                    manager.removeAllSubTasks();
                    statusCode = 202;
                    body = "Список задачь был отчищен.";
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
                        manager.removeSubtaskById(id);
                    } catch (SubtaskNotFoundException e) {
                        statusCode = 404;
                        body = "У подзадачи должна быть существующая подзадача";
                        break;
                    }
                    statusCode = 200;
                    body = "Задача c id: " + id + " была удалена.";
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

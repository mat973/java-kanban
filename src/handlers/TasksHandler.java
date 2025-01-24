package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dto.TaskDto;
import exeptions.TaskIntersectionException;
import exeptions.TaskNotFoundException;
import managers.TaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TasksHandler implements HttpHandler {
    private TaskManager manager;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
    }

        //Проверить еще раз логику и сдалсть что бы при Create возвращался обьект и при change тоже
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().substring(1).split("/");
        Gson gson = new Gson();
        int statusCode;
        String body;
        switch (method){
            case "GET":
                if (splitPath.length == 1){
                    statusCode = 202;
                    body = gson.toJson(manager.getTasks());
                    break;
                }else if (splitPath.length == 2){
                    int id;
                    try {
                        id = Integer.parseInt(splitPath[1]);
                    } catch (NumberFormatException e) {
                        statusCode = 404;
                        body = e.getMessage();
                        break;
                    }
                    Optional<Task> task = manager.getTaskById(id);
                    if (task.isPresent()){
                        statusCode = 200;
                        body = gson.toJson(task);
                    }else {
                        statusCode =404;
                        body = "Задачи с таким c id: " + id + " не сущесвует.";
                    }
                }else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            case "POST":
                if (splitPath.length == 1){
                    try {
                      body =  manager.createTask( gson.fromJson(new String(exchange.getRequestBody().readAllBytes()
                                , StandardCharsets.UTF_8), TaskDto.class)).toString();
                    } catch (TaskIntersectionException e) {
                        statusCode = 400;
                        body = e.getMessage();
                    }

                    statusCode = 200;
                    break;
                }else if (splitPath.length == 2){
                    int id;
                    try {
                        id = Integer.parseInt(splitPath[1]);
                    } catch (NumberFormatException e) {
                        statusCode = 404;
                        break;
                    }
                    try {
                        body = manager.changeTask(gson.fromJson(new String(exchange.getRequestBody().readAllBytes()
                                , StandardCharsets.UTF_8), TaskDto.class)).toString();
                    } catch (TaskNotFoundException e) {
                        statusCode = 404;
                        body = e.getMessage();
                    } catch (TaskIntersectionException e) {
                        statusCode = 406;
                        body = e.getMessage();
                    }  catch (IOException e) {
                        statusCode = 500;
                        body = e.getMessage();
                    }
                    statusCode = 200;
                }else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            case "DELETE":
                if (splitPath.length == 1){
                    manager.removeAllTasks();
                    statusCode = 202;
                    body = "Список задачь был отчищен.";
                    break;
                }else if (splitPath.length == 2){
                    int id;
                    try {
                        id = Integer.parseInt(splitPath[1]);
                    } catch (NumberFormatException e) {
                        statusCode = 404;
                        body = e.getMessage();
                        break;
                    }
                    try {
                        manager.removeTaskById(id);
                    } catch (TaskNotFoundException e) {
                        statusCode = 404;
                        body = "Задачи с таким c id: " + id + " не сущесвует.";
                        break;
                    }
                    statusCode = 200;
                    body = "Задача c id: " + id + " была удалена.";
                }else {
                    statusCode = 404;
                    body = "По данному пути нет endpoint";
                }
                break;
            default: {
                statusCode = 404;
                body = "Таких методов мы не знаем.";
            }
          HandlersMessageSender.sendText(exchange, body, statusCode);
        }
    }
}

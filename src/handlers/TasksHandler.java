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
ывфажывтажыщш
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
                        break;
                    }
                    Optional<Task> task = manager.getTaskById(id);
                    if (task.isPresent()){
                        statusCode = 200;
                        body = gson.toJson(task);
                    }else {
                        statusCode =404;
                    }
                }else {
                    statusCode = 404;
                }
                break;
            case "POST":
                if (splitPath.length == 1){
                    try {
                        manager.createTask( gson.fromJson(new String(exchange.getRequestBody().readAllBytes()
                                , StandardCharsets.UTF_8), TaskDto.class));
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
                        manager.changeTask(gson.fromJson(new String(exchange.getRequestBody().readAllBytes()
                                , StandardCharsets.UTF_8), TaskDto.class));
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
                }else {
                    statusCode = 404;
                }
                break;
            case "DELETE":
                break;
            default: {
                statusCode = 404;
            }
                //TODO вызвать какой то ментод для ответа method(HttpExchange exchange ,int statusCode,String body);
        }
    }
}

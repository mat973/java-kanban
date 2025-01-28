import com.sun.net.httpserver.HttpServer;
import handlers.*;
import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;
    private TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicTasksHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
        System.out.println("Я запустился");
        httpServer.start();
    }

    public static void main(String[] args) throws IOException {
    HistoryManager historyManager = Managers.getDefaultHistory();
    TaskManager manager = Managers.getFileTaskManager(historyManager);
    try {
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    }


    public void stop() {
        httpServer.stop(0);
    }
}

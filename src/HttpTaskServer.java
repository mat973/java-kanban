import com.sun.net.httpserver.HttpServer;
import handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.httpServer.createContext("/tasks", new TasksHandler());
        httpTaskServer.httpServer.createContext("/subtasks", new SubtasksHandler());
        httpTaskServer.httpServer.createContext("/epics", new EpicTasksHandler());
        httpTaskServer.httpServer.createContext("/history", new HistoryHandler());
        httpTaskServer.httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpTaskServer.httpServer.start();

    }



}

import com.google.gson.Gson;
import managers.HistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TasksHandlerTest {
    private HttpTaskServer server;
    private HttpClient client;
    private TaskManager manager;
    private HistoryManager historyManager;
    private final Gson gson = new Gson();

    @BeforeAll
    void init() throws IOException {
        historyManager = Managers.getDefaultHistory();
        manager = Managers.getFileTaskManager(historyManager); // Инициализируйте с вашим HistoryManager
        server = new HttpTaskServer(manager); // Запуск сервера
        client = HttpClient.newHttpClient(); // Инициализация клиента
    }

    @AfterAll
    void tearDown() {
        server.stop(); // Завершение сервера
    }

    @Test
    void testGetAllTasksEmpty() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(202, response.statusCode()); // Проверяем статус-код
        assertEquals("[]", response.body()); // Проверяем, что список задач пустой
    }

    @Test
    void testCreateTask() throws IOException, InterruptedException {
        String taskJson = """
                {
                    "name": "Test Task",
                    "description": "This is a test task",
                    "duration": 30,
                    "startTime": "2025-01-01T10:00:00"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode()); // Проверяем статус-код
        assertTrue(response.body().contains("Test Task")); // Проверяем, что задача добавлена
    }

    @Test
    void testDeleteAllTasks() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(202, response.statusCode()); // Проверяем статус-код
        assertEquals("Список задачь был отчищен.", response.body());
    }

    @Test
    void testGetTaskByIdNotFound() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode()); // Проверяем статус-код
        assertEquals("Задачи с таким c id: 1 не сущесвует.", response.body());
    }
}

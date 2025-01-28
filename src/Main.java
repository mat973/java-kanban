import com.google.gson.Gson;
import dto.EpicDto;
import dto.SubtaskDto;
import dto.TaskDto;
import managers.FileBackedTaskManager;
import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import task.Epic;
import task.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


public class Main {

    private static HttpClient client = HttpClient.newHttpClient();

    private static final Gson gson = new Gson();

//static {
//    HistoryManager historyManager = Managers.getDefaultHistory();
//    TaskManager manager = Managers.getFileTaskManager(historyManager);
//    try {
//        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
//    } catch (IOException e) {
//        throw new RuntimeException(e);
//    }
//}


    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("main");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();


 //       HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());



        //FileBackedTaskManager.deleteFile();
//        //dd MM yyyy HH:mm

        TaskDto taskDto = new TaskDto(0, "task", "Desc", Status.NEW, 150L, "27 02 2025 17:30");
        EpicDto epicDto1 = new EpicDto(1, "Epic1", "Desck1", Status.NEW, 150L, "26 02 2025 17:30");
        EpicDto epicDto2 = new EpicDto(2, "Epic2", "Desck3", Status.NEW, 150L, "25 02 2025 17:30");
        SubtaskDto subtaskDto1 = new SubtaskDto(3, "Sub1", "Description1", Status.NEW, 2, 150L, "26 02 2025 17:30");
//        SubtaskDto subtaskDto2 = new SubtaskDto(4, "Sub2", "Description2", Status.NEW, epicDto1, 150L, "25 02 2025 17:30");
//        SubtaskDto subtaskDto3 = new SubtaskDto(5, "Sub3", "Description3", Status.NEW, epicDto2, 150L, "24 02 2025 17:30");
//        SubtaskDto subtaskDto4 = new SubtaskDto(5, "Sub33", "Description33", Status.NEW, epicDto2, 150L, "24 02 2025 17:30");
        System.out.println(gson.toJson(subtaskDto1));
        System.out.println(gson.toJson(epicDto1));
        //System.out.println(gson.toJson(taskDto));
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(taskDto)))
                .build();

    //    response = client.send(request1, HttpResponse.BodyHandlers.ofString());



   //     response = client.send(request, HttpResponse.BodyHandlers.ofString());


//        response = client.send(request1, HttpResponse.BodyHandlers.ofString());
//
//        System.out.println(response.body());




    }
}


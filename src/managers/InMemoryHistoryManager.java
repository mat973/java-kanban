package managers;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    private static class Node{
        Task task;
        Node prev;
        Node next;

        public Node(Task task) {
            this.task = task;
        }

    }

    private final Map<Integer, Node> taskMap = new HashMap<>();

    private Node head;
    private Node tail;

    @Override
    public void addToHistory(Task task) {
        if (task == null) {
            System.out.println("Задача не может быть Null");
            return;
        }

        int taskId = task.getId();
        if (taskMap.containsKey(taskId)){
            remove(taskId);
        }

        linkLast(task);

        taskMap.put(taskId, tail);

    }

    private void linkLast(Task task) {
        Node node = new Node(task);
        if (tail == null){
            head = node;
        } else {
            tail.next = node;
            node.prev = tail;
        }
        tail = node;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null){
            tasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    @Override
    public void remove(int id) {
        Node node = taskMap.get(id);
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            } else {
                head = node.next;
            }

            if (node.next != null) {
                node.next.prev = node.prev;
            } else {
                tail = node.prev;
            }

            taskMap.remove(id);
        }
    }

}

package ru.yandex.practicum.agolykh.kanban.managers.history;

import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> nodeMap= new HashMap<>();
    public Node head;
    public Node tail;
    private int size = 0;

    class Node  {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    //Добавление элемент в конец
    public void linkLast(Task task) {
        final Node lastElement = tail;
        final Node newElement = new Node(lastElement, task, null);
        tail = newElement;
        if (lastElement == null) {
            head = newElement;
            nodeMap.put(head.data.getId(), head);
        } else {
            lastElement.next = newElement;
            nodeMap.put(newElement.data.getId(), newElement);
        }
        size++;
    }

    //Удаление ноды в связанном списке
    public void removeNode(Node node) {
        for(Node i = head; i != null; i = i.next) {
            if (i.data.equals(node.data)) {
                nodeMap.remove(node.data.getId());
                delLink(i);
            }
        }
    }

    public void delLink(Node i) {
        final Node next = i.next;
        final Node prev = i.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            i.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            i.next = null;
        }
        i.data = null;
        size--;
    }

    //Получить список задач
    public ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        for(Node i = tail; i != null; i = i.prev) {
            result.add(i.data);
        }
        return result;
    }

    // Добавление элемента в историю
    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
            linkLast(task);
        } else {
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(nodeMap.get(id));
    }

    // Вывод истории
    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    public int getSize() {
        return size;
    }
}

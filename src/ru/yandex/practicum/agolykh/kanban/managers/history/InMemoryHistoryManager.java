package ru.yandex.practicum.agolykh.kanban.managers.history;

import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList<Task> nodesList;

    public InMemoryHistoryManager() {
        nodesList = new CustomLinkedList<>();
    }

    // Получить количество элементов
    @Override
    public int countOfNodes() {
        return nodesList.getSize();
    }

    // Добавление элемента в историю
    @Override
    public void add(Task task) {
        if (nodesList.inSet(task.getId())) {
            remove(task.getId());
        }
        nodesList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (nodesList.inSet(id)) {
            nodesList.removeNode(nodesList.get(id));
        }
    }

    // Вывод истории
    @Override
    public ArrayList<Task> getHistory() {
        return nodesList.getTasks();
    }
}

class CustomLinkedList<T extends Task> {
    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    private class Node {
        private final T data;
        private Node next;
        private Node prev;

        private Node(Node prev, T data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    int getSize() {
        return size;
    }

    Node get(int id) {
        return nodeMap.get(id);
    }

    // Проверка наличия ID
    boolean inSet(int id) {
        return nodeMap.containsKey(id);
    }

    // Добавление ноды в конец списка
    void linkLast(T task) {
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

    // Удаление ноды в связанном списке
    void removeNode(Node node) {
        nodeMap.remove(node.data.getId());
        delLink(node);
    }

    // Изменение ссылок в соседних ячейках
    private void delLink(Node node) {
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }
        size--;
    }

    // Получить список задач
    ArrayList<T> getTasks() {
        ArrayList<T> result = new ArrayList<>();
        Node node = tail;

        while (node != null) {
            result.add(node.data);
            node = node.prev;
        }
        return result;
    }
}

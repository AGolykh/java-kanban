package ru.yandex.practicum.agolykh.kanban.managers.history;

import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> nodeMap= new HashMap<>();
    private Node head;
    private Node tail;
    private int size = 0;

    static class Node  {
        private Task data;
        private Node next;
        private Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    //Добавление элемент в конец
    private void linkLast(Task task) {
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
    private void removeNode(Node node) {
        nodeMap.remove(node.data.getId());
        delLink(node);
    }

    //Изменение ссылок в соседних нодах
    private void delLink(Node node) {
        final Node next = node.next;
        final Node prev = node.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            node.next = null;
        }
        node.data = null;
        size--;
    }

    //Получить список задач
    private ArrayList<Task> getTasks() {
        ArrayList<Task> result = new ArrayList<>();
        Node node = tail;

        while (node != null) {
            result.add(node.data);
            node = node.prev;
        }
        return result;
    }

    //Получить количество элементов
    @Override
    public int countOfNodes() {
        return size;
    }

    // Добавление элемента в историю
    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if(nodeMap.containsKey(id)) {
            removeNode(nodeMap.get(id));
        }
    }

    // Вывод истории
    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }


}

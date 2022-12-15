package ru.yandex.practicum.agolykh.kanban.tasks;

public enum Status {
    NEW, IN_PROGRESS, DONE;

    public static Status of(String value) {
        return switch(value) {
            case("DONE") -> DONE;
            case("IN_PROGRESS") -> IN_PROGRESS;
            case("NEW") -> NEW;
            default -> null;
        };
    }
}

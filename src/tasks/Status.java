package tasks;

public enum Status {
    NEW("Новая"),
    IN_PROGRESS("В процессе"),
    DONE("Выполнена");

    final private String title;

    Status(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

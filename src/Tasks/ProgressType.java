package Tasks;

public enum ProgressType {
    NEW ("Новая"),
    IN_PROGRESS ("В процессе"),
    DONE ("Выполнена"),
    CANCELLED("Отменена");

    final private String title;

    ProgressType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

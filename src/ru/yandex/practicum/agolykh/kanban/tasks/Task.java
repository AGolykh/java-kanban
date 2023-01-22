package ru.yandex.practicum.agolykh.kanban.tasks;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class Task {
    protected Integer id;
    protected TaskTypes type;
    protected Status status;
    protected String name;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public void setEndTime() {
        this.endTime = this.getEndTime();
    }

    public Task(String name, String description) {
        this.type = TaskTypes.TASK;
        this.status = Status.NEW;
        this.name = name;
        this.description = description;
    }

    public Task(Status status, String name, String description) {
        this(name, description);
        this.status = status;
    }

    public Task(String name, String description, int duration, String startTime) {
        this(name, description);
        this.duration = Duration.ofMinutes(duration);
        this.startTime = LocalDateTime.parse(startTime, Task.getFormatter());
        this.endTime = getEndTime();
    }

    public Task(Status status, String name, String description, int duration, String startTime) {
        this(name, description, duration, startTime);
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskTypes getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if ((this.startTime == null) || (this.duration == null)) {
            return null;
        }
        return startTime.plusMinutes(duration.toMinutes());
    }

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    @Override
    public String toString() {
        Long optionalDuration = Optional.ofNullable(duration)
                .map(Duration::toMinutes)
                .orElse(null);
        String optionalStartTime = Optional.ofNullable(startTime)
                .map((time) -> time.format(Task.getFormatter()))
                .orElse(null);
        String optionalEndTime = Optional.ofNullable(getEndTime())
                .map((time) -> time.format(Task.getFormatter()))
                .orElse(null);

        return "Task{"
                + "id=" + id
                + ", type=" + type
                + ", status=" + status
                + ", name='" + name
                + ", description='" + description
                + ", duration='" + optionalDuration
                + ", startTime='" + optionalStartTime
                + ", endTime='" + optionalEndTime
                + '}';
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id.equals(task.id)
                && type.equals(task.type)
                && status.equals(task.status)
                && name.equals(task.name)
                && description.equals(task.description);
    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(Task.getFormatter()));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), Task.getFormatter());
        }
    }

    public static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(Integer.parseInt(jsonReader.nextString()));
        }
    }

/*    public static class TasksAdapter extends TypeAdapter<Task> {
        @Override
        public void write(final JsonWriter jsonWriter, final Task task) throws IOException {
            Long optionalDuration = Optional.ofNullable(task.getDuration())
                    .map(Duration::toMinutes)
                    .orElse(null);
            String optionalStartTime = Optional.ofNullable(task.getStartTime())
                    .map((time) -> time.format(Task.getFormatter()))
                    .orElse(null);
            String optionalEndTime = Optional.ofNullable(task.getEndTime())
                    .map((time) -> time.format(Task.getFormatter()))
                    .orElse(null);

            jsonWriter.beginObject();
            jsonWriter.name("taskType");
            jsonWriter.value(String.valueOf(task.getType()));
            jsonWriter.name("id");
            jsonWriter.value(task.getId());
            jsonWriter.name("status");
            jsonWriter.value(String.valueOf(task.getStatus()));
            jsonWriter.name("name");
            jsonWriter.value(task.getName());
            jsonWriter.name("description");
            jsonWriter.value(task.getDescription());
            jsonWriter.name("duration");
            jsonWriter.value(optionalDuration);
            jsonWriter.name("startTime");
            jsonWriter.value(optionalStartTime);
            jsonWriter.name("endTime");
            jsonWriter.value(optionalEndTime);
            jsonWriter.endObject();
        }

        @Override
        public Task read(final JsonReader jsonReader) throws IOException {
            Task task = null;
            jsonReader.beginObject();
            String fieldName = null;
            String fieldValue = null;
            Integer fieldNumber = null;
            while (jsonReader.hasNext()) {
                JsonToken token = jsonReader.peek();
                if(JsonToken.NAME.equals(token)) {
                    token = jsonReader.peek();
                    fieldName = jsonReader.nextName();
                } else if(JsonToken.STRING.equals(token)) {
                    token = jsonReader.peek();
                    fieldValue = jsonReader.nextString();
                } else if(JsonToken.NUMBER.equals(token)) {
                    token = jsonReader.peek();
                    fieldNumber = jsonReader.nextInt();
                }

                if ("taskType".equals(fieldName) && "TASK".equals(fieldValue)) {
                    task = new Task(null, null);
                } else if ("id".equals(fieldName) && JsonToken.NUMBER.equals(token)) {
                    task.setId(fieldNumber);
                } else if ("status".equals(fieldName) && JsonToken.STRING.equals(token)) {
                    task.setStatus(Enum.valueOf(Status.class, fieldValue));
                } else if ("name".equals(fieldName) && JsonToken.STRING.equals(token)) {
                    task.setName(fieldValue);
                } else if ("description".equals(fieldName) && JsonToken.STRING.equals(token)) {
                    task.setDescription(fieldValue);
                } else if ("duration".equals(fieldName) && JsonToken.NUMBER.equals(token)) {
                    task.setDuration(Duration.ofMinutes(fieldNumber));
                } else if ("startTime".equals(fieldName) && JsonToken.STRING.equals(token)) {
                    task.setStartTime(LocalDateTime.parse(fieldValue, Task.getFormatter()));
                } else if ("endTime".equals(fieldName) && JsonToken.STRING.equals(token)) {
                    task.setEndTime();
                } else if (JsonToken.END_OBJECT.equals(jsonReader.peek())) {
                    jsonReader.endObject();
                }
            }
            return task;
        }
    }*/
}


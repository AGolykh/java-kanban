
package ru.yandex.practicum.agolykh.kanban.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import ru.yandex.practicum.agolykh.kanban.tasks.Epic;
import ru.yandex.practicum.agolykh.kanban.tasks.Status;
import ru.yandex.practicum.agolykh.kanban.tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class EpicAdapter extends TypeAdapter<Epic> {
    @Override
    public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {
        Long optionalDuration = Optional.ofNullable(epic.getDuration())
                .map(Duration::toMinutes)
                .orElse((long) 0);
        String optionalStartTime = Optional.ofNullable(epic.getStartTime())
                .map((time) -> time.format(Task.getFormatter()))
                .orElse("null");
        String optionalEndTime = Optional.ofNullable(epic.getEndTime())
                .map((time) -> time.format(Task.getFormatter()))
                .orElse("null");

        jsonWriter.beginObject()
            .name("id").value(epic.getId())
            .name("type").value(epic.getType().name())
            .name("status").value(epic.getStatus().name())
            .name("name").value(epic.getName())
            .name("description").value(epic.getDescription())
            .name("duration").value(optionalDuration)
            .name("startTime").value(optionalStartTime)
            .name("endTime").value(optionalEndTime)
            .name("listOfSubTasksId")
            .beginArray();
            for (int id: epic.getListSubTaskId()){
                jsonWriter.value(id);
            }
            jsonWriter.endArray().endObject();
    }

    @Override
    public Epic read(final JsonReader jsonReader) throws IOException {
        String nameToken = null;
        String stringToken;
        long numberToken;
        Epic epic = new Epic(null, null);
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            Set<Integer> setToken = new HashSet<>();
            JsonToken nextToken = jsonReader.peek();
            if (JsonToken.NAME.equals(nextToken)) {
                nameToken = jsonReader.nextName();
            } else if (JsonToken.STRING.equals(nextToken)) {
                stringToken = jsonReader.nextString();
                switch (Objects.requireNonNull(nameToken)) {
                    case "status" -> epic.setStatus(Enum.valueOf(Status.class, stringToken));
                    case "name" -> epic.setName(stringToken);
                    case "description" -> epic.setDescription(stringToken);
                }
            } else if (JsonToken.NUMBER.equals(nextToken)) {
                numberToken = jsonReader.nextLong();
                switch (Objects.requireNonNull(nameToken)) {
                    case "id" -> epic.setId(Math.toIntExact(numberToken));
                    case "duration" -> epic.setDuration(Duration.ofMinutes(numberToken));
                }
            } else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    setToken.add(jsonReader.nextInt());
                }
                epic.getListSubTaskId().addAll(setToken);
                jsonReader.endArray();
            }
        }
        jsonReader.endObject();
        return epic;
    }
}

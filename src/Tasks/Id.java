package Tasks;

public class Id {
    public static final Id instance = new Id();
    private static final int VALUE = 1;
    private int nextId;

    public int createId() {
        if (nextId == Integer.MAX_VALUE || nextId < VALUE) {
            reset();
        }
        return nextId++;
    }

    private void reset() {
        nextId = VALUE;
    }
}

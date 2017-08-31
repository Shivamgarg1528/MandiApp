package ig.com.digitalmandi.callback;

public interface EventCallback<T> {
    void onEvent(int pOperationType, T pObject);
}

package ig.com.digitalmandi.interfaces;

public interface EventListener<T> {
    void onEvent(int pOperationType, T pObject);
}

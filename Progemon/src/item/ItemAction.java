package item;

@FunctionalInterface
public interface ItemAction<T> {
	public void use(T target);
}

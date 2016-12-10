package item;

@FunctionalInterface
public interface ItemAction {
	public void use(Object target);
}

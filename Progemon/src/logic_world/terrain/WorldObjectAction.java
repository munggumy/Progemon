package logic_world.terrain;

@FunctionalInterface
public interface WorldObjectAction {

	public void execute(WorldObject target);

}

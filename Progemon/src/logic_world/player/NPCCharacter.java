package logic_world.player;

import graphic.DialogBox;
import logic_fight.character.pokemon.Pokemon;
import logic_world.terrain.WorldDirection;

public class NPCCharacter extends Character {

	public NPCCharacter(String imagepath, int blockX, int blockY, WorldDirection wd) {
		super(imagepath, 2, 3);
		// TODO Auto-generated constructor stub
		super.blockX = blockX;
		super.blockY = blockY;
		super.x = blockX * 32;
		super.y = blockY * 32;
		super.direction = wd;
	}

	@Override
	public void checkMove() {
		// TODO Auto-generated method stub
		
	}
	
	public void interacted() {
		direction = WorldDirection.getDirection(PlayerCharacter.instance.blockX - blockX
				, PlayerCharacter.instance.blockY - blockY);
		DialogBox.instance.show();
		DialogBox.instance.sentDialog("Hi " + PlayerCharacter.instance.getMe().getName() + "!@@@nLet me heal your pokemons");
		DialogBox.instance.hide();
		PlayerCharacter.instance.getMe().getPokemons().forEach(Pokemon::resetHP);
		System.err.println("NPCCharacter interacted --> do something");
	}

}

package item;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import logic_fight.character.pokemon.NonVolatileStatus;
import logic_fight.character.pokemon.Pokemon;

public class Items {

	public static final int IMAGE_OFFSET_X = 8, IMAGE_OFFSET_Y = 24;
	public static final int IMAGE_SIZE_X = 24, IMAGE_SIZE_Y = 24;

	private static final String ITEM_IMAGE_FILE_LOCATION = "load/img/item/PokemonEmerald-ItemIcons.png";
	private static Image spriteSheet;
	private static Map<String, Item> items = new HashMap<>();

	public static Item getItem(String itemName) {
		if (items == null) {
			throw new NullPointerException("items not loaded");
		}
		Item item = items.get(itemName);
		if (item != null) {
			return item;
		} else {
			throw new IllegalArgumentException("item \"" + itemName + "\" not found");
		}

	}

	public static void loadItems() {
		try {
			File spriteSheetFile = new File(ITEM_IMAGE_FILE_LOCATION);
			spriteSheet = new Image(spriteSheetFile.toURI().toString());
			while (spriteSheet.getPixelReader() == null) {

			}
			Item potion = new Item("Potion");
			potion.setOnPokemonUse(pokemon -> {
				pokemon.changeHP(20);
			});
			int xPos = IMAGE_OFFSET_X + 12 * IMAGE_SIZE_X;
			int yPos = IMAGE_OFFSET_Y + 0 * IMAGE_SIZE_Y;
			potion.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("potion", potion);

			Item sodaPop = new Item("Soda Pop");
			sodaPop.setOnPokemonUse(pokemon -> {
				pokemon.changeHP(60);
			});
			xPos = IMAGE_OFFSET_X + 6 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 1 * IMAGE_SIZE_Y;
			sodaPop.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("soda_pop", sodaPop);

			Item lemonade = new Item("Lemonade");
			lemonade.setOnPokemonUse(pokemon -> {
				pokemon.changeHP(80);
			});
			xPos = IMAGE_OFFSET_X + 7 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 1 * IMAGE_SIZE_Y;
			lemonade.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("lemonade", lemonade);

			Item superRepel = new Item("Super Repel");
			superRepel.setOnTrainerUse(trainer -> {
				trainer.setRepelTime(200);
			});
			xPos = IMAGE_OFFSET_X + 4 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 3 * IMAGE_SIZE_Y;
			superRepel.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("super_repel", superRepel);

			Item antidote = new Item("Antidote");
			antidote.setOnPokemonUse(pokemon -> {
				if (pokemon.getStatus().equals(NonVolatileStatus.POISON)) {
					System.out.println(pokemon.getName() + " cured from poison");
				} else {
					System.out.println("Antidote has no effect on " + pokemon.getName());
				}
			});
			xPos = IMAGE_OFFSET_X + 13 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 0 * IMAGE_SIZE_Y;
			antidote.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("antidote", antidote);

			Item iceHeal = new Item("Ice Heal");
			iceHeal.setOnPokemonUse(pokemon -> {
				if (pokemon.getStatus().equals(NonVolatileStatus.FREEZE)) {
					System.out.println(pokemon.getName() + " cured from freeze");
				} else {
					System.out.println("Ice Heal has no effect on " + pokemon.getName());
				}
			});
			xPos = IMAGE_OFFSET_X + 15 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 0 * IMAGE_SIZE_Y;
			iceHeal.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("ice_heal", iceHeal);

			Item rareCandy = new Item("Rare Candy");
			rareCandy.setOnPokemonUse(pokemon -> {
				if (pokemon.getLevel() < Pokemon.MAX_LEVEL) {
					pokemon.addExpAndTryLevelUp(pokemon.getNextExpRequired() - pokemon.getCurrentExp());
				}
			});
			xPos = IMAGE_OFFSET_X + 14 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 2 * IMAGE_SIZE_Y;
			rareCandy.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("rare_candy", rareCandy);

			Item pokeball = new Pokeball(PokeballType.POKEBALL);
			pokeball.setName("Pokeball");
			xPos = IMAGE_OFFSET_X + 3 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 0 * IMAGE_SIZE_Y;
			pokeball.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("pokeball", pokeball);

			Item greatball = new Pokeball(PokeballType.GREAT_BALL);
			greatball.setName("Great Ball");
			xPos = IMAGE_OFFSET_X + 2 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 0 * IMAGE_SIZE_Y;
			greatball.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("great_ball", greatball);

			Item ultraball = new Pokeball(PokeballType.ULTRA_BALL);
			ultraball.setName("Ultra Ball");
			xPos = IMAGE_OFFSET_X + 1 * IMAGE_SIZE_X;
			yPos = IMAGE_OFFSET_Y + 0 * IMAGE_SIZE_Y;
			ultraball.setIcon(new WritableImage(spriteSheet.getPixelReader(), xPos, yPos, IMAGE_SIZE_X, IMAGE_SIZE_Y));
			items.put("ultra_ball", ultraball);

		} catch (Exception ex) {
			System.err.println("Exception in Items.loadItems()");
			ex.printStackTrace();
		}
	}
}

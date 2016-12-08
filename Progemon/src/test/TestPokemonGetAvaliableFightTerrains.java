package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import logic_fight.character.pokemon.Pokemon;
import logic_fight.character.pokemon.Pokemon.MoveType;
import logic_fight.filters.MoveFilter;
import logic_fight.terrain.FightMap;
import utility.FileUtility;

public class TestPokemonGetAvaliableFightTerrains {

	FightMap fightMap;
	Pokemon fearow, pikachu;
	private int fearowMoveRange = 3;
	private int fearowAttackRange = 2;
	private int pikachuMoveRange = 3;
	private int pikachuAttackRange = 1;

	@Before
	public void setUp() throws Exception {
		FileUtility.loadAllDefaults();
		fightMap = new FightMap(FileUtility.loadFightMap());
		fearow = new Pokemon(22, 1.01, 1.11, 12.3, 18.5, fearowMoveRange, fearowAttackRange, MoveType.FLY);
		pikachu = new Pokemon(25, 0.98, 1.35, 10.9, 22.1, pikachuMoveRange, pikachuAttackRange, MoveType.WALK);
		fearow.setCurrentFightMap(fightMap);
		pikachu.setCurrentFightMap(fightMap);
		fearow.move(1, 1);
		pikachu.move(0, 0);
	}

	@Test
	public void testAvaliableFightTerrains() {
		
		fearow.move(2,2);
		
		fearow.findBlocksAround(1, new MoveFilter());
		assertEquals(5, fearow.getAvaliableFightTerrains().size());
		
	}

}

package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import utility.StringUtility;

public class TestStringUtility {
	String dog, logic, fun;

	@Before
	public void setUp() throws Exception {
		dog = "doge";
		logic = "logic arithmetic calculus discrete mathematics set graph";
		fun = "hEllO wORld";
	}

	@Test
	public void testDoge() {
		assertEquals("Doge", StringUtility.toTitleCase(dog));
	}

	@Test
	public void testLogic() {
		assertEquals("Logic Arithmetic Calculus Discrete Mathematics Set Graph", StringUtility.toTitleCase(logic));
	}

	@Test
	public void testFun() {
		assertEquals("Hello World", StringUtility.toTitleCase(fun));
	}
	
	@Test
	public void testHPBar(){
				
		// zero length
		
		assertEquals("[]", StringUtility.hpBar(0.1, 0));
		assertEquals("[]", StringUtility.hpBar(1, 0));
		
		//default length
		
		assertEquals("[                        ]",StringUtility.hpBar(0));
		assertEquals("[========================]", StringUtility.hpBar(1));
		assertEquals("[============            ]", StringUtility.hpBar(0.5));
		assertEquals("[======                  ]", StringUtility.hpBar(0.25));
		
		
		//length = 10
		
		assertEquals("[==        ]", StringUtility.hpBar(0.20, 10));
		assertEquals("[          ]", StringUtility.hpBar(0, 10));
		assertEquals("[=======   ]", StringUtility.hpBar(0.79, 10));
		
		// invalid percents 
		
		assertEquals("[          ]", StringUtility.hpBar(-0.7, 10));
		assertEquals("[==========]", StringUtility.hpBar(10000, 10));
		
		// invalid lengths
		
		assertEquals("[============            ]", StringUtility.hpBar(0.5, -1));
		assertEquals("[========                ]", StringUtility.hpBar(0.35, -10));
		
		
	}

}

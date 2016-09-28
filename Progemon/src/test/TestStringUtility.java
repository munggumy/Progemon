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
	
	@Test public void testLogic(){
		assertEquals("Logic Arithmetic Calculus Discrete Mathematics Set Graph", StringUtility.toTitleCase(logic));
	}
	@Test public void testFun(){
		assertEquals("Hello World", StringUtility.toTitleCase(fun));
	}

}

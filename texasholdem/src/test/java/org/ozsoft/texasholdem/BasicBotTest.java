package org.ozsoft.texasholdem;

import junit.framework.Assert;

import org.junit.Test;
import org.ozsoft.texasholdem.bots.BasicBot;

/**
 * Test suite for the BasicBot class.
 * 
 * @author Oscar Stigter
 */
public class BasicBotTest {
	
	/**
	 * Generic tests.
	 */
	@Test
	public void test() {
		BasicBot bot = new BasicBot();
		int value1, value2;
		
		value1 = bot.evaluateHoleCards(new Card[]{new Card("As"), new Card("Ah")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("As"), new Card("Ks")});
		Assert.assertTrue(value1 > value2);

		value1 = bot.evaluateHoleCards(new Card[]{new Card("As"), new Card("Ah")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("Ac"), new Card("Ad")});
		Assert.assertTrue(value1 == value2);

		value1 = bot.evaluateHoleCards(new Card[]{new Card("As"), new Card("Ah")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("Ks"), new Card("Kh")});
		Assert.assertTrue(value1 > value2);

//		value1 = bot.evaluateHoleCards(new Card[]{new Card("As"), new Card("Kh")});
//		value2 = bot.evaluateHoleCards(new Card[]{new Card("Kd"), new Card("Kc")});
//		Assert.assertTrue(value1 > value2);

		value1 = bot.evaluateHoleCards(new Card[]{new Card("Ks"), new Card("Kh")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("As"), new Card("Qs")});
		Assert.assertTrue(value1 > value2);

		value1 = bot.evaluateHoleCards(new Card[]{new Card("3d"), new Card("3c")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("2d"), new Card("2c")});
		Assert.assertTrue(value1 > value2);

		value1 = bot.evaluateHoleCards(new Card[]{new Card("4d"), new Card("3d")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("3c"), new Card("2c")});
		Assert.assertTrue(value1 > value2);

		value1 = bot.evaluateHoleCards(new Card[]{new Card("2d"), new Card("2c")});
		value2 = bot.evaluateHoleCards(new Card[]{new Card("3d"), new Card("2s")});
		Assert.assertTrue(value1 > value2);
		
	}

}

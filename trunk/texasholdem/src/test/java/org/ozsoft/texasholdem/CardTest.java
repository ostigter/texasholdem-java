package org.ozsoft.texasholdem;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Unit test for the Card class.
 * 
 * @author Oscar Stigter
 */
public class CardTest {
	
	/**
	 * Tests the card ordering.
	 */
	@Test
	public void sortOrder() {
		// Diamond is lower, Clubs is higher.
		Card _2d = new Card("2d");
		Card _3d = new Card("3d");
		Card _2c = new Card("2c");
		Card _3c = new Card("3c");
		Assert.assertEquals(_2d, _2d);
		Assert.assertFalse(_2d.equals(_3d));
		Assert.assertFalse(_2d.equals(_2c));
		Assert.assertEquals(0, _2d.hashCode());
		Assert.assertEquals(1, _2c.hashCode());
		Assert.assertEquals(4, _3d.hashCode());
		Assert.assertEquals(5, _3c.hashCode());
		Assert.assertTrue(_2d.compareTo(_2d) == 0);
		Assert.assertTrue(_2d.compareTo(_3d) < 0);
		Assert.assertTrue(_3d.compareTo(_2d) > 0);
		Assert.assertTrue(_2d.compareTo(_2c) < 0);
		Assert.assertTrue(_2c.compareTo(_2d) > 0);
	}

}

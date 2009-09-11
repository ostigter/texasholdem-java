package org.ozsoft.texasholdem;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test suite for the HandEvaluator class.
 * 
 * @author Oscar Stigter
 */
public class HandEvaluatorTest {
    
    /**
     * Tests the Flush.
     */
    @Test
    public void flush() {
        HandEvaluator evaluator = null;
        int value1, value2;
        
        // Flush.
        evaluator = new HandEvaluator(new Hand("Kh Jh Jd 8h 6h 5s 3h"));
        Assert.assertNotNull(evaluator);
        Assert.assertEquals(HandValueType.FLUSH, evaluator.getType());
        
        // Missing one card.
        evaluator = new HandEvaluator(new Hand("Kh Jh Jd 8h 6d 5s 3h"));
        Assert.assertNotNull(evaluator);
        Assert.assertFalse(evaluator.getType() == HandValueType.FLUSH);
        
        // Comparisons.
        evaluator = new HandEvaluator(new Hand("As Qs Ts 8s 6s 4d 2c"));
        Assert.assertEquals(HandValueType.FLUSH, evaluator.getType());
        value1 = evaluator.getValue();
        evaluator = new HandEvaluator(new Hand("Ks Qs Ts 8s 6s 4d 2c"));
        Assert.assertEquals(HandValueType.FLUSH, evaluator.getType());
        value2 = evaluator.getValue();
        Assert.assertTrue(value1 > value2);
        evaluator = new HandEvaluator(new Hand("As Qs Ts 8s 5s 4d 2c"));
        Assert.assertEquals(HandValueType.FLUSH, evaluator.getType());
        value2 = evaluator.getValue();
        Assert.assertTrue(value1 > value2);
    }

}

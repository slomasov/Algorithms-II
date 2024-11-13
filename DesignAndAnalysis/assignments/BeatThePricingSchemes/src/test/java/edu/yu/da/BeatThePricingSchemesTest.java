package edu.yu.da;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BeatThePricingSchemesTest{

	final double DELTA = 0.001;

	@Test
	public void profTest(){
		final double unitPrice = 2.0;
		final BeatThePricingSchemesBase btps = new BeatThePricingSchemes(unitPrice);
		final double price = 8.75;
		final int quantity = 5;
		btps.addPricingScheme(price, quantity);
		final int[] thresholds = {4, 6};
		final double[] expecteds = {8.0, 10.75};
		int testCase = 0;
		for ( int threshold : thresholds ) {
			final double cheapest = btps . cheapestPrice ( threshold ) ;
			assertEquals ( cheapest , expecteds [ testCase ++] , DELTA , "mismatch on cheapest price" ) ;
			final List<Integer> optimalDecisions = btps . optimalDecisions () ;
			assertNotNull( optimalDecisions, "optimalDecisions parameter");
		}
	}

	@Test
	public void unusialInputsTest(){
		assertThrows(IllegalArgumentException.class, () -> {
			final BeatThePricingSchemesBase btps = new BeatThePricingSchemes(0);
		});

		final BeatThePricingSchemesBase btps = new BeatThePricingSchemes(3);
		assertThrows(IllegalArgumentException.class, () -> {
			btps.addPricingScheme(0, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			btps.addPricingScheme(1, 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			btps.addPricingScheme(1, btps.MAX_MATZOS+1);
		});

		btps.addPricingScheme(1, 1);

		assertThrows(IllegalStateException.class, () -> {
			btps.optimalDecisions();
		});

		double price = btps.cheapestPrice(3);
		List<Integer> decisions = btps.optimalDecisions();
		assertEquals(price, 3.0);
		assertEquals(decisions, List.of(1,1,1));

		for(int i=0;i<19;i++){
			btps.addPricingScheme(1, 1);
		}
		assertThrows(IllegalArgumentException.class, () -> {
			btps.addPricingScheme(1, 1);
		});
	}

	@Test
	public void correctnessTest(){
		long start = System.currentTimeMillis();
		double unitPrice = 1.1;
		BeatThePricingSchemesBase btps = new BeatThePricingSchemes(unitPrice);
		final double[] prices = {12, 100, 7, 6, 4, 3.1, 2.1};
		final int[] quantities = {16, 11, 9, 7, 5, 3, 2};
		for(int i=0; i<quantities.length; i++){
			btps.addPricingScheme(prices[i], quantities[i]);
		}
		final int[] thresholds = {10, 12, 14, 17};
		final double[] expecteds = {8, 10, 11, 13.1};
		int testCase = 0;
		for ( int threshold : thresholds ) {
			final double cheapest = btps . cheapestPrice ( threshold ) ;
			assertEquals ( cheapest , expecteds [ testCase ++] , DELTA , "mismatch on cheapest price" ) ;
			final List<Integer> optimalDecisions = btps . optimalDecisions () ;
			assertNotNull( optimalDecisions, "optimalDecisions parameter");
		}
		long elapsed = System.currentTimeMillis()-start;
		//System.out.print("elapsed time: ");
		//System.out.println(elapsed);

		unitPrice = 2;
		btps = new BeatThePricingSchemes(unitPrice);
		btps.addPricingScheme(30,40);
		btps.addPricingScheme(30,57);
		btps.addPricingScheme(20,80);
		assertEquals (btps.cheapestPrice (97), 40, DELTA , "mismatch on cheapest price" ) ;
		assertEquals(btps.optimalDecisions(),List.of(3,3));

		btps = new BeatThePricingSchemes(unitPrice);
		btps.addPricingScheme(5,3);
		btps.addPricingScheme(100,5);
		assertEquals (btps.cheapestPrice (5), 9, DELTA , "mismatch on cheapest price" ) ;
		assertEquals(btps.optimalDecisions(),List.of(0,0,1));

		btps.addPricingScheme(1.7,1);
		assertEquals (btps.cheapestPrice (5), 8.4, DELTA , "mismatch on cheapest price" ) ;
		assertEquals(btps.optimalDecisions(),List.of(1,3,3));
	}

}//class
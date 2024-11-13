package edu.yu.da;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SpheresOfInfluenceTest{

	@Test
	public void profTest(){
		final int maxStrength = 2 ;
		final int maxRight = 10 ;
		final SpheresOfInfluenceBase soi = new SpheresOfInfluence(maxStrength, maxRight);
		soi.addInfluencer("A", 2, 3);
		soi.addInfluencer("B", 6, 5);
		final List<String> solution = soi.getMinimalCoverageInfluencers();
		final List<String> expected = List.of("A", "B");
		assertEquals(solution.size(), expected.size(),"Mismatch on solution size");
		assertEquals(solution, expected, "Incorrect minimal coverage");
	}

	@Test
	public void unusualInputTest(){
		final int maxStrength0 = 0;
		final int maxRight0 = 0;
		final int maxStrength = 2;
		final int maxRight = 10;

		assertThrows(IllegalArgumentException.class, () -> {
			final SpheresOfInfluenceBase soi = new SpheresOfInfluence(maxStrength0, maxRight);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final SpheresOfInfluenceBase soi = new SpheresOfInfluence(maxStrength, maxRight0);
		});

		final SpheresOfInfluenceBase soi = new SpheresOfInfluence(maxStrength, maxRight);
		final int x0 = -1;
		final int r0 = 0;
		final int x = 2;
		final int r = 10;
		final String id = "A";
		assertThrows(IllegalArgumentException.class, () -> {
			soi.addInfluencer(id, x0, r);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			soi.addInfluencer(id, x, r0);
		});
		soi.addInfluencer(id, x, r);
		// assertThrows(IllegalArgumentException.class, () -> {
		// 	soi.addInfluencer(id, 2, 3);
		// });
		// assertThrows(IllegalArgumentException.class, () -> {
		// 	soi.addInfluencer("B", x, r);
		// });


	}//inputs

	@Test
	public void correctnessTest(){
		/*
		** no solution
		** trivial with many spheres
		** large in the middle, two smaller (but correct) ones on the sides
		** two huge one on the sides, the smaller (but correct) one in the middle
		** complex case for each of the above (design first)
		*/
		final int maxRight = 10;
		final int maxStrength = 10;

		SpheresOfInfluenceBase soi = null;
		List<String> solution = null;
		List<String> expected = null;

		//no solution
		soi = new SpheresOfInfluence(maxStrength, maxRight);
		soi.addInfluencer("A", 0, 6);
		soi.addInfluencer("B", 5, 5);
		soi.addInfluencer("C", 9, 4);
		solution = soi.getMinimalCoverageInfluencers();
		expected = Collections.<String>emptyList();
		assertEquals(solution.size(), expected.size(),"Mismatch on solution size");
		assertEquals(solution, expected, "Incorrect minimal coverage");

		//trivial solution
		soi = new SpheresOfInfluence(maxStrength, maxRight);
		soi.addInfluencer("A", 2, 7);
		soi.addInfluencer("B", 6, 7);
		soi.addInfluencer("C", 0, 5);
		solution = soi.getMinimalCoverageInfluencers();
		expected = List.of("A","B");
		assertEquals(solution.size(), expected.size(),"Mismatch on solution size");
		assertEquals(solution, expected, "Incorrect minimal coverage");

		//large one in the middle, two small (and correct) ones on the sides
		soi = new SpheresOfInfluence(maxStrength, maxRight);
		soi.addInfluencer("A", 2, 6);
		soi.addInfluencer("B", 8, 6);
		soi.addInfluencer("C", 5, 7);
		solution = soi.getMinimalCoverageInfluencers();
		expected = List.of("A","B");
		assertEquals(solution.size(), expected.size(),"Mismatch on solution size");
		assertEquals(solution, expected, "Incorrect minimal coverage");

		//two large ones on the sides, one small (and correct) one in the middle
		soi = new SpheresOfInfluence(maxStrength, maxRight);
		soi.addInfluencer("A", 0, 11);
		soi.addInfluencer("B", 10, 11);
		soi.addInfluencer("C", 5, 8);
		solution = soi.getMinimalCoverageInfluencers();
		expected = List.of("C");
		assertEquals(solution.size(), expected.size(),"Mismatch on solution size");
		assertEquals(solution, expected, "Incorrect minimal coverage");


	}//correctness

	@Test
	public void performanceTest(){
		final int maxStrength = 10;
		final int maxRight = 8000;
		final SpheresOfInfluenceBase soi = new SpheresOfInfluence(maxStrength, maxRight);
		int size = 8000;
		int r = 5;
		long start = System.currentTimeMillis();
		for(int i=0; i<size; i++){
			soi.addInfluencer(((Integer)i).toString(),i,r);
		}
		long elapsed = System.currentTimeMillis()-start;
		System.out.println(elapsed);

	}//performance

}//class
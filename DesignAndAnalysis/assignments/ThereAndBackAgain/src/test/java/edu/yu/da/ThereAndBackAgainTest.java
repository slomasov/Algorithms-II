package edu.yu.da;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ThereAndBackAgainTest{

	double COST_DELTA = 0.001;
	
	@Test
	public void profTest(){
		final String startVertex = "a";
		final ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex);
		taba.addEdge(startVertex, "b", 1.0);
		taba.addEdge("b", "c", 2.0);
		taba.doIt();
		assertEquals(taba.goalVertex(), null, "goalVertex");
		assertEquals(taba.goalCost(), 0.0, COST_DELTA, "goalCost");
		assertEquals(taba.getOneLongestPath(), Collections.<String>emptyList(),"getOneLongestPath");
		assertEquals(taba.getOtherLongestPath(), Collections.<String>emptyList(),"getOtherLongestPath");
	}

	@Test
	public void creationTest(){
		final String startVertex1 = null;
		assertThrows(IllegalArgumentException.class, () -> {
			final ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex1);
		});
		String startVertex = "";
		assertThrows(IllegalArgumentException.class, () -> {
			final ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex1);
		});
		startVertex = "a";
		ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex);
		String v1="b";
		String v2="c";
		double weight = 1.0;
		taba.addEdge(v1,v2,weight);
		assertThrows(IllegalArgumentException.class, () -> {
			taba.addEdge(v1,v2,weight);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			taba.addEdge(v1,v1,weight);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			taba.addEdge(v1,v1,0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			taba.addEdge(v1,v1,-1);
		});
		taba.doIt();
		assertThrows(IllegalStateException.class, () -> {
			taba.doIt();
		});
		assertThrows(IllegalStateException.class, () -> {
			taba.addEdge(v1,"ddd",13);
		});
	}

	@Test
	public void correctnessTest(){
		final String startVertex = "a";
		List<String> onePath = new ArrayList<>();
		List<String> otherPath = new ArrayList<>();
		otherPath.add("a");
		otherPath.add("b");
		otherPath.add("c");
		onePath.add("a");
		onePath.add("c");
		assert(onePath.hashCode()<otherPath.hashCode());

		ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex);
		taba.addEdge(startVertex, "b", 1.0);
		taba.addEdge("b", "c", 2.0);
		taba.addEdge(startVertex, "c", 3.0);
		taba.doIt();
		assertEquals(taba.goalVertex(), "c", "goalVertex");
		assertEquals(taba.goalCost(), 3.0, COST_DELTA, "goalCost");
		assertEquals(taba.getOneLongestPath(), onePath,"getOneLongestPath");
		assertEquals(taba.getOtherLongestPath(), otherPath,"getOtherLongestPath");

		taba = new ThereAndBackAgain(startVertex);
		taba.addEdge(startVertex, "b", 1.0);
		taba.addEdge("b", "c", 2.0);
		taba.addEdge(startVertex, "c", 3.1);
		taba.doIt();
		assertEquals(taba.goalVertex(), null, "goalVertex");
		assertEquals(taba.goalCost(), 0.0, COST_DELTA, "goalCost");
		assertEquals(taba.getOneLongestPath(), Collections.<String>emptyList(),"getOneLongestPath");
		assertEquals(taba.getOtherLongestPath(), Collections.<String>emptyList(),"getOtherLongestPath");

		taba = new ThereAndBackAgain(startVertex);
		taba.addEdge(startVertex, "b", 1.0);
		taba.addEdge("b", "c", 2.0);
		taba.addEdge(startVertex, "c", 3.0);
		taba.addEdge("c", "d", 1.0);
		taba.doIt();
		onePath.add("d");
		otherPath.add("d");
		assert(onePath.hashCode()<otherPath.hashCode());
		assertEquals(taba.goalVertex(), "d", "goalVertex");
		assertEquals(taba.goalCost(), 4.0, COST_DELTA, "goalCost");
		assertEquals(taba.getOneLongestPath(), onePath,"getOneLongestPath");
		assertEquals(taba.getOtherLongestPath(), otherPath,"getOtherLongestPath");

		taba = new ThereAndBackAgain(startVertex);
		taba.addEdge(startVertex, "b", 1.0);
		taba.addEdge("b", "c", 2.0);
		taba.addEdge("c", "d", 1.0);
		taba.addEdge("b", "d", 3.0);

		taba.addEdge(startVertex, "e", 0.2);
		taba.addEdge("e", "f", 2.0);
		taba.addEdge("f", "g", 1.0);
		taba.addEdge("e", "g", 3.0);
		taba.doIt();
		onePath.set(1,"b");
		assert(onePath.hashCode()<otherPath.hashCode());
		assertEquals(taba.goalVertex(), "d", "goalVertex");
		assertEquals(taba.goalCost(), 4.0, COST_DELTA, "goalCost");
		assertEquals(taba.getOneLongestPath(), onePath,"getOneLongestPath");
		assertEquals(taba.getOtherLongestPath(), otherPath,"getOtherLongestPath");

	}

	@Test
	public void performanceTest(){
		long start = System.currentTimeMillis();
		String startVertex = "a";
		final ThereAndBackAgainBase taba = new ThereAndBackAgain(startVertex);
		taba.addEdge(startVertex, "b", 1.0);
		taba.addEdge("b", "c", 2.0);
		taba.addEdge("c", "d", 1.0);
		taba.addEdge("b", "d", 3.0);
		taba.addEdge(startVertex, "e", 0.2);
		taba.addEdge("e", "f", 2.0);
		taba.addEdge("f", "g", 1.0);
		taba.addEdge("e", "g", 3.0);
		taba.addEdge("c", "h", 15.0);
		byte[] array = new byte[20];
		Random rand = new Random();
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge(startVertex, new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("b", new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("c", new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("d", new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("e", new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("f", new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("g", new String(array), 2.1);
		}
		for(int i=0; i<25000; i++){
			rand.nextBytes(array);
			taba.addEdge("h", new String(array), 2.1);
		}
		taba.doIt();
		long elapsed = start - System.currentTimeMillis();
		//System.out.println(elapsed);
	}//performance

} //class
package edu.yu.da;

import edu.yu.da.MatzoDistribution;
import edu.yu.da.MatzoDistributionBase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MatzoDistributionTest{

	@Test
	public void profTest(){
		final int srcNodeConstraint = 4;
		final int MAX_CONSTRAINT = 3;
		final MatzoDistributionBase md = new MatzoDistribution ("s" , srcNodeConstraint , "t" ) ;
		md.addWarehouse("A", MAX_CONSTRAINT);
		md.roadExists("s", "A", MAX_CONSTRAINT);
		md.roadExists("A", "t", MAX_CONSTRAINT);
		final int max = md.max();
		assert(max==3);
	}//profTest

	@Test
	public void unusualInputsTest(){
		/*
		constructor(string, int, string) strings cannot be null or blank and must be different; int>0
		addWarehouse(string, int) string cannot be null or blank and cannot have been added; int>0
		roadExists(string, string, int) strings cannot be null or blank and must be different and must have been added; int>0
		*/

		//constructor
		assertThrows(IllegalArgumentException.class, () -> {
			final MatzoDistributionBase md = new MatzoDistribution ("s", 0, "t");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final MatzoDistributionBase md = new MatzoDistribution ("s", 1, "s");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final MatzoDistributionBase md = new MatzoDistribution ("", 1, "t");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final MatzoDistributionBase md = new MatzoDistribution ("s", 0, null);
		});

		//addWarehouse
		final MatzoDistributionBase md = new MatzoDistribution ("s", 1, "t");
		assertThrows(IllegalArgumentException.class, () -> {
			md.addWarehouse(null, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			md.addWarehouse("", 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			md.addWarehouse("A", 0);
		});
		md.addWarehouse("A", 1);
		assertThrows(IllegalArgumentException.class, () -> {
			md.addWarehouse("A", 1);
		});

		//roadExists
		assertThrows(IllegalArgumentException.class, () -> {
			md.roadExists("s", "A", 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			md.roadExists("", "A", 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			md.roadExists("s", null, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			md.roadExists("s", "B", 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			md.roadExists("s", "s", 1);
		});
		md.roadExists("s","A",1);
		assertThrows(IllegalArgumentException.class, () -> {
			md.roadExists("s", "A", 2);
		});
		int max = md.max();
		assert(max==0);
		md.roadExists("A","t",1);
		max = md.max();
		assert(max==1);
	}//unusualInputsTest

	@Test
	public void correctnessTest(){
		/*
		* small source: s cap 1, s-t 5, max 1
		* two-sided road: s-a-b-t with a-b and b-a
		* s - a - b - t: capacity a < capacity s-a and capacity b > capacity a-b. All capacities: 10,6,5,3,4,5. Max 3. With continious invocation.
		* triangle, cap of 2 roads is larger than cap of warehouse: s-a-b-t; s-b exists; cap s=10;a 10;s-a 10; a-b 2; s-b 3; b 4; b-t 10; max 4;
		* triangle, cap of 2 roads is smaller than cap of warehouse: s-a-b-t; s-b exists; cap s=10;a 10;s-a 10; a-b 2; s-b 3; b 6; b-t 10; max 5;
		* triangle, cap of warehouse is larger than cap of outgoing roads: s-a-b-t; a-t exists; cap s=10; a 10; s-a 10; a-b 2; b 10; a-t 3; b-t 10; max 5;
		* triangle, cap of warehouse is smaller than cap of outgoing roads: s-a-b-t; a-t exists; cap s=10; a 4; s-a 10; a-b 2; b 10; a-t 3; b-t 10; max 4;
		* large example: take a large network flow problem from lecture notes
		*/

		//long start = System.currentTimeMillis();

		String a = "a";
		String b = "b";
		String c = "c";
		String s = "s";
		String t = "t";
		MatzoDistributionBase md = null;
		int max = 0;

		md = new MatzoDistribution(s, 1, t);
		md.roadExists(s, t, 5);
		max = md.max();
		assertEquals(1, max, "small source test");

		md = new MatzoDistribution(s, 5, t);
		md.addWarehouse(a,5);
		md.addWarehouse(b,5);
		md.roadExists(s, a, 5);
		md.roadExists(a, b, 4);
		md.roadExists(b, a, 5);
		md.roadExists(b, t, 5);
		max = md.max();
		assertEquals(4, max, "two-sided road test");

		md = new MatzoDistribution(s, 10, t);
		md.addWarehouse(a,5);
		md.addWarehouse(b,4);
		md.roadExists(s, a, 6);
		md.roadExists(s, b, 3);
		md.roadExists(b, t, 5);
		max = md.max();
		assertEquals(3, max, "simple line test");
		md.addWarehouse(c, 10);
		md.roadExists(s, c, 10);
		md.roadExists(c, t, 10);
		max = md.max();
		assertEquals(10, max, "simple line test second invocation");

		md = new MatzoDistribution(s, 10, t);
		md.addWarehouse(a,10);
		md.addWarehouse(b,4);
		md.roadExists(s, a, 10);
		md.roadExists(a, b, 2);
		md.roadExists(s, b, 3);
		md.roadExists(b, t, 10);
		max = md.max();
		assertEquals(4, max, "triangle test 1");

		md = new MatzoDistribution(s, 10, t);
		md.addWarehouse(a,10);
		md.addWarehouse(b,6);
		md.roadExists(s, a, 10);
		md.roadExists(a, b, 2);
		md.roadExists(s, b, 3);
		md.roadExists(b, t, 10);
		max = md.max();
		assertEquals(5, max, "triangle test 2");

		md = new MatzoDistribution(s, 10, t);
		md.addWarehouse(a,10);
		md.addWarehouse(b,10);
		md.roadExists(s, a, 10);
		md.roadExists(a, b, 2);
		md.roadExists(a, t, 3);
		md.roadExists(b, t, 10);
		max = md.max();
		assertEquals(5, max, "triangle test 3");

		md = new MatzoDistribution(s, 10, t);
		md.addWarehouse(a,4);
		md.addWarehouse(b,10);
		md.roadExists(s, a, 10);
		md.roadExists(a, b, 2);
		md.roadExists(a, t, 3);
		md.roadExists(b, t, 10);
		max = md.max();
		assertEquals(4, max, "triangle test 4");

		//long elapsed1 = System.currentTimeMillis()-start;

		md = new MatzoDistribution(s, 100, t);
		md.addWarehouse("A", 100);
		md.addWarehouse("B", 100);
		md.addWarehouse("C", 100);
		md.addWarehouse("D", 100);
		md.addWarehouse("E", 100);
		md.addWarehouse("F", 100);
		md.roadExists(s,"A",10);
		md.roadExists(s,"B",5);
		md.roadExists(s,"C",15);
		md.roadExists("A","B",4);
		md.roadExists("A","D",9);
		md.roadExists("A","E",15);
		md.roadExists("B","C",4);
		md.roadExists("B","E",8);
		md.roadExists("C","F",16);
		md.roadExists("D","E",15);
		md.roadExists("D",t,10);
		md.roadExists("E","F",15);
		md.roadExists("E",t,10);
		md.roadExists("F","B",6);
		md.roadExists("F",t,10);
		max = md.max();
		assertEquals(28, max, "lecture test");

		//long elapsed2 = System.currentTimeMillis()-start;

		//System.out.println(elapsed1);
		//System.out.println(elapsed2-elapsed1);
		//performance
		// md = new MatzoDistribution(s,100,t);
		// int max_wh = 1000;
		// String[] out-going = new String[max_wh];
		// String[] in-going = new String[max_wh];
		// out-going[0]=s;
		// for(int i=0; i<max_wh; i++){

		// }

	}//correctnessTest


}//class
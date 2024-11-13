package edu.yu.da;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class WaterPressureTest{

	@Test
	public void profTest(){
		final String initialInputPump = "0";
		final String secondInputPump = "2";
		final String[] V = new String[]{"0","1"};
		final String[] W = new String[]{"1","2"};
		final double[] weight = new double[]{1.0,2.0};
		final double expectedMinAmount1=2.0;
		final double expectedMinAmount2=1.0;
		final WaterPressureBase wp = new WaterPressure(initialInputPump);
		wp.addBlockage(V[0],W[0],weight[0]);
		wp.addBlockage(V[1],W[1],weight[1]);
		final double actualMinAmount1 = wp.minAmount();
		assertEquals(expectedMinAmount1,actualMinAmount1,"one pump");
		wp.addSecondInputPump(secondInputPump);
		final double actualMinAmount2 = wp.minAmount(); 
		assertEquals(expectedMinAmount2,actualMinAmount2,"two pumps");
	}

	@Test
	public void unusualInputsTest(){
		/*
		* initialStartPump, length must be greater than 0.
		* The second input pump must already be in the channel system, and length must be greater than 0. Can't be invoked more than once.
		* blockage: The two pump stations must differ from one another, and no channel can already exist between the two pump stations.
		length of pumps and weight must be greater than 0. throws IllegalStateException if minAmount() has previously been invoked.
		*/

		//initial input pump
		final String initialInputPump0 = null;
		assertThrows(IllegalArgumentException.class, () -> {
			final WaterPressureBase wp = new WaterPressure(initialInputPump0);
		});
		final String initialInputPump1 = "";
		assertThrows(IllegalArgumentException.class, () -> {
			final WaterPressureBase wp = new WaterPressure(initialInputPump1);
		});
		final String initialInputPump = "a";
		final WaterPressureBase wp = new WaterPressure(initialInputPump);

		//second input pump
		final String secondInputPump0 = null;
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addSecondInputPump(secondInputPump0);
		});
		final String secondInputPump1 = "";
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addSecondInputPump(secondInputPump1);
		});
		final String secondInputPump = "b";
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addSecondInputPump(secondInputPump);
		});
		wp.addBlockage(initialInputPump, secondInputPump, 1.0);
		wp.addSecondInputPump(secondInputPump);
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addSecondInputPump(secondInputPump);
		});

		//blockage
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addBlockage(initialInputPump, secondInputPump, 2.0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addBlockage("x", "x", 2.0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addBlockage("", secondInputPump, 2.0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addBlockage(initialInputPump,"",2.0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addBlockage(initialInputPump, "c", 0);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			wp.addBlockage("d", "c", -1);
		});
		wp.minAmount();
		wp.minAmount();
		assertThrows(IllegalStateException.class, () -> {
			wp.addBlockage("x", "y", 1.0);
		});
		assertThrows(IllegalStateException.class, () -> {
			wp.minAmount();
		});
	}

	@Test
	public void onePumpTest(){

		//straight simple
		final String initialInputPump = "a";
		final String[] V = new String[]{"a","b","c"};
		final String[] W = new String[]{"b","c","d"};
		final double[] weight = new double[]{1.0,2.0,1.0};
		final WaterPressureBase wp1 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp1.addBlockage(V[i],W[i],weight[i]);
		}
		double expectedAmount = 2.0;
		assertEquals(expectedAmount, wp1.minAmount(), "straight simple test one pump");
		assertEquals(expectedAmount, wp1.minAmount(), "straight simple test one pump second invocation");

		//bi-directional
		final WaterPressureBase wp11 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp11.addBlockage(V[i],W[i],weight[i]);
			wp11.addBlockage(W[i],V[i],weight[i]);
		}
		assertEquals(expectedAmount, wp11.minAmount(), "bi-directional simple test one pump");
		assertEquals(expectedAmount, wp11.minAmount(), "bo-directional simple test one pump second invocation");

		//with cycle
		final WaterPressureBase wp2 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp2.addBlockage(V[i],W[i],weight[i]);
		}
		wp2.addBlockage("b","x",1.0);
		wp2.addBlockage("x","c",1.0);
		expectedAmount=1.0;
		assertEquals(expectedAmount, wp2.minAmount(), "simple test with cycle one pump");
		assertEquals(expectedAmount, wp2.minAmount(), "simple test with cycle one pump second invocation");

		//non-reachable
		final WaterPressureBase wp3 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp3.addBlockage(V[i],W[i],weight[i]);
		}
		wp3.addBlockage("e","d",10.0);
		expectedAmount=-1;
		assertEquals(expectedAmount, wp3.minAmount(), "simple test non-reachable one pump");
		assertEquals(expectedAmount, wp3.minAmount(), "simple test non-reachable one pump second invocation");
	}

	@Test
	public void twoPumpsTest(){
		//straight simple
		final String initialInputPump = "a";
		final String[] V = new String[]{"a","b","c"};
		final String[] W = new String[]{"b","c","d"};
		final double[] weight = new double[]{1.0,2.0,1.0};
		final WaterPressureBase wp1 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp1.addBlockage(V[i],W[i],weight[i]);
		}
		double expectedAmount = 2.0;
		assertEquals(expectedAmount, wp1.minAmount(), "straight simple test one pump");
		wp1.addSecondInputPump("c");
		double expectedAmount2 = 1.0;
		assertEquals(expectedAmount2, wp1.minAmount(), "straight simple test two pumps");

		//bi-directional
		final WaterPressureBase wp11 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp11.addBlockage(V[i],W[i],weight[i]);
			wp11.addBlockage(W[i],V[i],weight[i]);
		}
		assertEquals(expectedAmount, wp11.minAmount(), "bi-directional simple test two pumps");
		wp11.addSecondInputPump("c");
		assertEquals(expectedAmount2, wp11.minAmount(), "bi-directional simple test two pumps");

		//with cycle
		final WaterPressureBase wp2 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp2.addBlockage(V[i],W[i],weight[i]);
		}
		wp2.addBlockage("b","x",1.0);
		wp2.addBlockage("x","c",1.0);
		expectedAmount=1.0;
		wp2.addSecondInputPump("b");
		assertEquals(expectedAmount, wp2.minAmount(), "simple test with cycle two pumps");
		assertEquals(expectedAmount, wp2.minAmount(), "simple test with cycle two pumps second invocation");

		//non-reachable
		final WaterPressureBase wp3 = new WaterPressure(initialInputPump);
		for(int i=0; i<3; i++){
			wp3.addBlockage(V[i],W[i],weight[i]);
		}
		wp3.addBlockage("e","d",10.0);
		expectedAmount=-1;
		assertEquals(expectedAmount, wp3.minAmount(), "simple test non-reachable one pump");
		wp3.addSecondInputPump("e");
		expectedAmount2=2.0;
		assertEquals(expectedAmount2, wp3.minAmount(), "simple test non-reachable with one pump but reachable with two pumps");
	}

	@Test
	public void ComplexGraphCorrectnessTest(){
		final String initialInputPump = "a";
		final String[] V = new String[]{"a","a","a","a","b","c","d","e","e","f","f","g","h","i"};
		final String[] W = new String[]{"b","h","i","j","c","d","e","b","f","a","g","f","g","h"};
		final double[] weight = new double[]{3.0,10.0,6.0,1.0,3.0,2.0,5.0,3.0,1.0,15.0,1.0,1.0,20.0,4.0};
		final WaterPressureBase wp = new WaterPressure(initialInputPump);
		int tempLength = weight.length;
		for(int i=0; i<tempLength; i++){
			wp.addBlockage(V[i],W[i],weight[i]);
		}
		double expectedAmount = 6.0;
		assertEquals(expectedAmount, wp.minAmount(), "complex test one pump");
		wp.addSecondInputPump("i");
		double expectedAmount2 = 5.0;
		assertEquals(expectedAmount2, wp.minAmount(), "complex test two pumps");
	}

	@Test
	public void performanceTest(){

		long start = System.currentTimeMillis();

		final String InitialInputPump = "a";
		int size = (int)Math.pow(2,22);
		final String[] V = new String[size];
		final String[] W = new String[size];
		final double[] weight = new double[size];
		size--;
		for(int i=0; i<size; i++){
			weight[i]=1.0;
			String temp = ((Integer)i).toString();
			W[i]=temp;
			V[i+1]=temp;
		}

		String initialInputPump = "a";
		String secondInputPump = "b";
		V[0]=initialInputPump;
		W[size]=secondInputPump;
		weight[size]=1.0;

		final WaterPressureBase wp = new WaterPressure(initialInputPump);
		size++;
		for(int i=0; i<size; i++){
			wp.addBlockage(V[i],W[i],weight[i]);
		}
		double expectedAmount = 1.0;
		double actualAmount1 = wp.minAmount();
		assertEquals(expectedAmount, actualAmount1, "performance test one pump");
		wp.addSecondInputPump(secondInputPump);
		double actualAmount2 = wp.minAmount();
		assertEquals(expectedAmount, actualAmount2, "performance test two pumps");

		long elapsed = System.currentTimeMillis()-start;
		//System.out.println(elapsed);
	}

}//class
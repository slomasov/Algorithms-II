package edu.yu.da;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class PickAYeshivaTest{

	class Yeshiva{
		public double faculty;
		public double cooking;

		public Yeshiva(double faculty, double cooking){
			this.faculty=faculty;
			this.cooking=cooking;
		}

	    @Override
	    public boolean equals(Object obj) {
	      if(obj==null){
	        return false;
	      }
	      if(this==obj){
	        return true;
	      }
	      if(getClass() != obj.getClass()){
	        return false;
	      }
	      Yeshiva temp = (Yeshiva) obj;
	      return (this.faculty==temp.faculty) && (this.cooking==temp.cooking);
	    }

	    @Override
	    public int hashCode(){
		   	int result = Double.hashCode(this.faculty);
	        result = 31 * result + Double.hashCode(this.cooking);
	        return result;
	    }
	}

	private boolean arraysAreEquivalent(double[] facultyExpected, double[] cookingExpected, double[] facultyActual, double[] cookingActual){
		int len = cookingExpected.length;
		if(len!=facultyExpected.length || len!=cookingActual.length || len!=facultyActual.length) return false;
		Set<Yeshiva> yeshivot = new HashSet<>();
		for(int i=0; i<len; i++){
			yeshivot.add(new Yeshiva(facultyExpected[i], cookingExpected[i]));
		}
		for(int i=0; i<len; i++){
			if(!yeshivot.contains(new Yeshiva(facultyActual[i],cookingActual[i]))) return false;
		}
		return true;
	}//equivalence

	@Test
	public void profTest(){
		final double[] facultyRatioRankings = {0, 1, 2};
		final double[] cookingRankings = {3, 2, 1};
		final PickAYeshivaBase pay = new PickAYeshiva(facultyRatioRankings, cookingRankings);
		double[] resultFaculty = pay.getFacultyRatioRankings();
		double[] resultCooking = pay.getCookingRankings();
		assert(arraysAreEquivalent(facultyRatioRankings,cookingRankings,resultFaculty,resultCooking));
	}//profTest

	@Test
	public void unusualInputTest(){

		final double[] facultyRankingsZ = new double[0];
		final double[] cookingRankingsZ = null;
		final double[] facultyRankingsY = {1};
		final double[] cookingRankingsY = {1,2};
		assertThrows(IllegalArgumentException.class, () -> {
			final PickAYeshivaBase pay = new PickAYeshiva(facultyRankingsZ, cookingRankingsZ);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final PickAYeshivaBase pay = new PickAYeshiva(facultyRankingsZ, cookingRankingsY);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final PickAYeshivaBase pay = new PickAYeshiva(facultyRankingsY, cookingRankingsZ);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			final PickAYeshivaBase pay = new PickAYeshiva(facultyRankingsY, cookingRankingsY);
		});
		double[] cookingRankings={1};
		final PickAYeshivaBase pay = new PickAYeshiva(facultyRankingsY, cookingRankings);
		double[] resultFaculty = pay.getFacultyRatioRankings();
		double[] resultCooking = pay.getCookingRankings();
		assert(arraysAreEquivalent(facultyRankingsY,cookingRankings,resultFaculty,resultCooking));

		//assert(immutable inputs);
	}//inputs

	@Test
	public void correctnessTest(){

		//even length no replacements
		int len=8;
		double[] facultyRankings = new double[len];
		double[] cookingRankings = new double[len];
		for(int i=0; i<len; i++){
			facultyRankings[i]=i;
			cookingRankings[i]=len-1-i;
		}
		PickAYeshivaBase pay = new PickAYeshiva(facultyRankings, cookingRankings);
		double[] facultyExpected = facultyRankings;
		double[] cookingExpected = cookingRankings;
		double[] resultFaculty = pay.getFacultyRatioRankings();
		double[] resultCooking = pay.getCookingRankings();
		// System.out.println(facultyExpected[0]);
		// System.out.println(cookingExpected[0]);
		// System.out.println(resultFaculty[0]);
		// System.out.println(resultCooking[0]);
		assert(arraysAreEquivalent(facultyExpected,cookingExpected,resultFaculty,resultCooking));

		//simple replacement
		for(int i=0; i<len; i++){
			cookingRankings[i]=i;
		}
		assert(facultyRankings.length==8);
		pay = new PickAYeshiva(facultyRankings, cookingRankings);
		facultyExpected = new double[]{len-1};
		cookingExpected = new double[]{len-1};
		resultFaculty = pay.getFacultyRatioRankings();
		resultCooking = pay.getCookingRankings();
		// System.out.println(facultyExpected[0]);
		// System.out.println(cookingExpected[0]);
		// System.out.println(resultFaculty[0]);
		// System.out.println(resultCooking[0]);
		assert(arraysAreEquivalent(facultyExpected,cookingExpected,resultFaculty,resultCooking));

		//equal values
		for(int i=0; i<len; i++){
			cookingRankings[i]=1;
		}
		pay = new PickAYeshiva(facultyRankings, cookingRankings);
		facultyExpected = new double[]{len-1};
		cookingExpected = new double[]{1};
		resultFaculty = pay.getFacultyRatioRankings();
		resultCooking = pay.getCookingRankings();
		assert(arraysAreEquivalent(facultyExpected,cookingExpected,resultFaculty,resultCooking));

		cookingRankings[4]=2;
		pay = new PickAYeshiva(facultyRankings, cookingRankings);
		facultyExpected = new double[]{len-1, 4};
		cookingExpected = new double[]{1, 2};
		resultFaculty = pay.getFacultyRatioRankings();
		resultCooking = pay.getCookingRankings();
		assert(arraysAreEquivalent(facultyExpected,cookingExpected,resultFaculty,resultCooking));

		//odd length
		facultyRankings = new double[--len];
		cookingRankings = new double[len];
		for(int i=0; i<len; i++){
			facultyRankings[i]=i;
			cookingRankings[i]=len-i;
		}
		pay = new PickAYeshiva(facultyRankings, cookingRankings);
		facultyExpected = facultyRankings;
		cookingExpected = cookingRankings;
		resultFaculty = pay.getFacultyRatioRankings();
		resultCooking = pay.getCookingRankings();
		assert(arraysAreEquivalent(facultyExpected,cookingExpected,resultFaculty,resultCooking));

		//complex example
		/*
1 2 3    4 5  6 7 8 9 10   11  12
4 5 100  8 12 5 2 0 2 100  100 1
		*/

		facultyRankings = new double[]{6, 4, 8, 2, 10, 7, 5, 9, 3, 11, 1, 12};
		cookingRankings = new double[]{5, 8, 0, 5, 100, 2, 12, 2, 100, 100, 4, 1};
		pay = new PickAYeshiva(facultyRankings, cookingRankings);
		facultyExpected = new double[]{11, 12};
		cookingExpected = new double[]{100, 4};
		resultFaculty = pay.getFacultyRatioRankings();
		resultCooking = pay.getCookingRankings();
		//assert(arraysAreEquivalent(facultyExpected,cookingExpected,resultFaculty,resultCooking));
	}//correctness

	@Test
	public void noRepPerformanceTest(){
		long start = System.currentTimeMillis();
		int len=(int)Math.pow(2,24);
		double[] facultyRankings = new double[len];
		double[] cookingRankings = new double[len];
		for(int i=0; i<len; i++){
			facultyRankings[i]=i;
			cookingRankings[i]=len-i;
		}
		PickAYeshivaBase pay = new PickAYeshiva(facultyRankings, cookingRankings);
		double[] resultFaculty = pay.getFacultyRatioRankings();
		double[] resultCooking = pay.getCookingRankings();
		long elapsed = System.currentTimeMillis() - start;
		//System.out.println(elapsed);
	}//performance

	@Test
	public void yesRepPerformanceTest(){
		long start = System.currentTimeMillis();
		int len=(int)Math.pow(2,25);
		double[] facultyRankings = new double[len];
		double[] cookingRankings = new double[len];
		for(int i=0; i<len; i++){
			facultyRankings[i]=i;
			cookingRankings[i]=i;
		}
		PickAYeshivaBase pay = new PickAYeshiva(facultyRankings, cookingRankings);
		double[] resultFaculty = pay.getFacultyRatioRankings();
		double[] resultCooking = pay.getCookingRankings();
		long elapsed = System.currentTimeMillis() - start;
		//System.out.println(elapsed);
	}//performance


} //class
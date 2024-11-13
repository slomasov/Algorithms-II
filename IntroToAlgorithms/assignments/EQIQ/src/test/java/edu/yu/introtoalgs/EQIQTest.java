package edu.yu.introtoalgs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.yu.introtoalgs.EQIQBase;
import edu.yu.introtoalgs.EQIQ;
import org.testng.asserts.SoftAssert;
// import org.apache.logging.log4j.LogManager;
// import org.apache.logging.log4j.Logger;

public class EQIQTest{

	//private static final Logger logger = LogManager.getLogger(EQIQTest.class);
	private SoftAssert softAssert = new SoftAssert();
	private double DELTA = 0.0001;
	private int totalQuestions = 2;
	private double[] eqSuccessRate = {1,2};
	private double[] iqSuccessRate = {2,1};
	private int nepotismIndex = 1;

	@Test
	public void profTest(){
		final int tempTotalQuestions = 2;
		final int tempMCandidates = 2 ;
		final double[] tempEQSuccessRate = {10.0, 20.0}; 
		final double[] tempIQSuccessRate = {40.0, 40.0};
		final int tempNepotismIndex = 1 ;
		final EQIQBase eqiq = new EQIQ(tempTotalQuestions, tempEQSuccessRate, tempIQSuccessRate,tempNepotismIndex);
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , 2.0, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , 0.0, DELTA, "getNumberIQQuestions()");
		double myIqSeconds = 3600.0/tempEQSuccessRate[1];
		double otherIqSeconds = 3600.0/tempEQSuccessRate[0];
		double secondsDelta = 2.0*(otherIqSeconds-myIqSeconds);
		softAssert.assertEquals(360 , secondsDelta, DELTA, "myDeltaCalculation");
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), 360, DELTA, "getNumberOfSecondsSuccess()") ;
		softAssert.assertAll();
	}

	@Test
	public void allDifferentTest(){
		totalQuestions = 1000;
		eqSuccessRate = new double[]{1,2,3,4,5,6,7,8,9,10,11};
		iqSuccessRate = new double[]{11,10,9,8,7,6,5,4,3,2,1};
		nepotismIndex = 0;
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		double myIqSeconds = 3600.0/iqSuccessRate[0];
		double otherIqSeconds = 3600.0/iqSuccessRate[1];
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , 0.0, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , 1000.0, DELTA, "getNumberIQQuestions()");
		double secondsDelta = 1000.0*(otherIqSeconds-myIqSeconds);
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), secondsDelta, DELTA, "getNumberOfSecondsSuccess()");

		nepotismIndex = 1;
		double eqNumber = 500.0/17.0;
		final EQIQBase eqiq2 = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		softAssert.assertTrue(eqiq2.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq2.getNumberEQQuestions() , eqNumber, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq2.getNumberIQQuestions() , 1000.0-eqNumber, DELTA, "getNumberIQQuestions()");
		secondsDelta = eqNumber*(3600/eqSuccessRate[0]-3600/eqSuccessRate[1]) +(1000-eqNumber)*(3600/iqSuccessRate[0]-3600/iqSuccessRate[1]);
		//logger.info(secondsDelta);
		//logger.info(eqiq2.getNumberOfSecondsSuccess());
		softAssert.assertEquals(eqiq2.getNumberOfSecondsSuccess(), secondsDelta, DELTA, "getNumberOfSecondsSuccess()");

		softAssert.assertAll();
	}

	@Test
	public void cannotWinTest(){
		eqSuccessRate = new double[]{2,1};
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		softAssert.assertFalse(eqiq.canNepotismSucceed(), "Nepotism should not have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , -1, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , -1, DELTA, "getNumberIQQuestions()");
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), -1, DELTA, "getNumberOfSecondsSuccess()");
		softAssert.assertAll();
	}

	@Test
	public void performanceTest(){
		totalQuestions = 1000000000;
		eqSuccessRate = new double[100];
		iqSuccessRate = new double[100];
		for(int i=0; i<eqSuccessRate.length; i++){
			eqSuccessRate[i]=(double)(i+1);
			iqSuccessRate[i]=(double)(eqSuccessRate.length-i);
		}
		long start = System.currentTimeMillis();
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		long elapsed = System.currentTimeMillis()-start;
		assert(elapsed<10);
		softAssert.assertAll();
	}

	@Test
	public void zeroNepotismTest(){
		totalQuestions = 5;
		eqSuccessRate = new double[]{0,1,1};
		iqSuccessRate = new double[]{2,1,1};
		nepotismIndex = 0;

		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , 0, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , 5, DELTA, "getNumberIQQuestions()");
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), 5*1800, DELTA, "getNumberOfSecondsSuccess()");

		eqSuccessRate = new double[]{2,1,1};
		iqSuccessRate = new double[]{0,1,1};
		final EQIQBase eqiq2 = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		//logger.info(eqiq2.getNumberOfSecondsSuccess());
		softAssert.assertTrue(eqiq2.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq2.getNumberEQQuestions() , 5, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq2.getNumberIQQuestions() , 0, DELTA, "getNumberIQQuestions()");
		softAssert.assertEquals(eqiq2.getNumberOfSecondsSuccess(), 5*1800, DELTA, "getNumberOfSecondsSuccess()");
		softAssert.assertAll();
	}

	@Test
	public void zeroCandidatesTest(){
		eqSuccessRate[0]=0;
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		//logger.info(3600.0/eqSuccessRate[0]);
	}

	@Test
	public void differentNepotismIndexTest(){
		totalQuestions = 1000;
		eqSuccessRate = new double[]{6,2,3,4,5,1,7,8,9,10,11};
		iqSuccessRate = new double[]{6,10,9,8,7,11,5,4,3,2,1};
		nepotismIndex = 5;
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		double myIqSeconds = 3600.0/iqSuccessRate[5];
		double otherIqSeconds = 3600.0/iqSuccessRate[1];
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , 0.0, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , 1000.0, DELTA, "getNumberIQQuestions()");
		double secondsDelta = 1000.0*(otherIqSeconds-myIqSeconds);
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), secondsDelta, DELTA, "getNumberOfSecondsSuccess()");
		softAssert.assertAll();
	}

	@Test
	public void eqAndOrIqIsZero(){
		totalQuestions = 1000;
		eqSuccessRate = new double[]{1,2,3,0,1,0,7,8,9,10,11};
		iqSuccessRate = new double[]{11,10,1,0,0,6,5,4,3,2,1};
		nepotismIndex = 0;
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , 0.0, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , 1000.0, DELTA, "getNumberIQQuestions()");
		double myIqSeconds = 3600.0/iqSuccessRate[0];
		double otherIqSeconds = 3600.0/iqSuccessRate[1];
		double secondsDelta = 1000.0*(otherIqSeconds-myIqSeconds);
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), secondsDelta, DELTA, "getNumberOfSecondsSuccess()");
		softAssert.assertAll();
	}

	@Test
	public void allCandidatesAreDumb(){
		eqSuccessRate = new double[]{0,1};
		iqSuccessRate = new double[]{1,2};
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		//softAssert.assertEquals(eqiq.getNumberEQQuestions() , 2.0, DELTA, "getNumberEQQuestions()");
		//softAssert.assertEquals(eqiq.getNumberIQQuestions() , 0.0, DELTA, "getNumberIQQuestions()");
		softAssert.assertTrue(Double.isInfinite(eqiq.getNumberOfSecondsSuccess()), "should beat by infinity");
		softAssert.assertAll();
	}

	@Test
	public void preconditionsTest(){
		totalQuestions = 1;
		assertThrows(IllegalArgumentException.class, () -> {
			EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		});
		totalQuestions = 2;
		nepotismIndex=2;
		assertThrows(IllegalArgumentException.class, () -> {
			EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		});
		nepotismIndex=-1;
		assertThrows(IllegalArgumentException.class, () -> {
			EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		});
		nepotismIndex=0;
		eqSuccessRate=new double[]{1,1,1};
		assertThrows(IllegalArgumentException.class, () -> {
			EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		});
		eqSuccessRate=new double[]{1,-1};
		assertThrows(IllegalArgumentException.class, () -> {
			EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		});
		eqSuccessRate=new double[]{1};
		iqSuccessRate=new double[]{1};
		assertThrows(IllegalArgumentException.class, () -> {
			EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		});
		nepotismIndex = 1;
		eqSuccessRate = new double[]{10.0, 20.0};
		iqSuccessRate = new double[]{40.0, 40.0};
		final EQIQBase eqiq = new EQIQ(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		softAssert.assertTrue(eqiq.canNepotismSucceed(), "Nepotism should have succeeded");
		softAssert.assertEquals(eqiq.getNumberEQQuestions() , 2.0, DELTA, "getNumberEQQuestions()");
		softAssert.assertEquals(eqiq.getNumberIQQuestions() , 0.0, DELTA, "getNumberIQQuestions()");
		softAssert.assertEquals(eqiq.getNumberOfSecondsSuccess(), 360, DELTA, "getNumberOfSecondsSuccess()") ;
		softAssert.assertAll();
	}

}// end of class
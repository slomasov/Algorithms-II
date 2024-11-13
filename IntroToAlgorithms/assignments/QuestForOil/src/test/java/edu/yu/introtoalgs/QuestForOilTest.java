package edu.yu.introtoalgs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import edu.yu.introtoalgs.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

public class QuestForOilTest{

	private static final Logger logger = LogManager.getLogger(QuestForOilTest.class);
	private final char s = 'S';
	private final char u = 'U';

	@Test
	public void demo ( ) {
		final char [ ] [ ] map = new char [ 2 ] [ 2 ] ;
		int i = 0;
		map[0][0]=s;
		map[0][1]=s;
		map[1][0]=s;
		map[1][1]=s;
		final QuestForOilBase qfo = new QuestForOil (map) ;
		final int retval = qfo.nContiguous(0 , 1 ) ;
		assertEquals( retval , 4 , "Mismatch on nContiguous" ) ;
	}

	@Test
	public void correctnessTest(){
		assertThrows(IllegalArgumentException.class, () -> {
			final QuestForOilBase qfo = new QuestForOil (null) ;
		});

	char[][] map = new char[3][5];
	map[0][0]=s;
	map[1][0]=u;
	map[2][0]=s;

	map[0][1]=u;
	map[1][1]=s;
	map[2][1]=u;

	map[0][2]=u;
	map[1][2]=u;
	map[2][2]=u;

	map[0][3]=s;
	map[1][3]=s;
	map[2][3]=s;

	map[0][4]=u;
	map[1][4]=u;
	map[2][4]=s;

	final QuestForOilBase qfo1 = new QuestForOil (map) ;
	assert(qfo1.nContiguous(1,0)==0);
	assert(qfo1.nContiguous(1,2)==0);
	assertEquals(3, qfo1.nContiguous(0,0));
	assertEquals(3,qfo1.nContiguous(2,0));
	assertEquals(4,qfo1.nContiguous(0,3));
	assert(qfo1.nContiguous(2,4)==4);
	assertThrows(IllegalArgumentException.class, () -> {
		int temp = qfo1.nContiguous(3,5) ;
	});

	map[0][2]=s;
	final QuestForOilBase qfo2 = new QuestForOil (map) ;
	assert(qfo2.nContiguous(0,0)==8);
	assert(qfo2.nContiguous(2,4)==8);

	map[1][2]=s;
	final QuestForOilBase qfo3 = new QuestForOil (map) ;
	assert(qfo3.nContiguous(0,0)==9);
	assert(qfo3.nContiguous(2,4)==9);

	map[2][2]=s;
	final QuestForOilBase qfo4 = new QuestForOil (map) ;
	assert(qfo4.nContiguous(0,0)==10);
	assert(qfo4.nContiguous(2,4)==10);

	}//end of correctnessTest;

	@Test
	public void performanceTest(){
		int size = 5000;
		char[][] map = new char[size][size];
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				map[i][j]=s;
			}
		}

		//long start = System.currentTimeMillis();
		final QuestForOilBase qfo1 = new QuestForOil (map) ;
		final int retval1 = qfo1.nContiguous(0 , 0 );
		final int retval2 = qfo1.nContiguous(0 , size-1 );
		final int retval3 = qfo1.nContiguous(size/2 , size/2 );
		final int retval4 = qfo1.nContiguous(size-1, 0 );
		//long elapsed = System.currentTimeMillis()-start;
		//System.out.println(elapsed);
		//System.out.println();
		assert(retval1==size*size);
		assert(retval2==size*size);
		assert(retval3==size*size);
		assert(retval4==size*size);

		int interval = size/10;
		for(int i=interval-1; i<size; i+=interval){
			for(int j=0; j<size; j++){
				map[i][j]=u;
			}
		}

		//start = System.currentTimeMillis();
		final QuestForOilBase qfo2 = new QuestForOil (map) ;
		int val1 = qfo2.nContiguous(0 , 0 );
		int val2 = qfo2.nContiguous(interval , 0 );
		int val3 = qfo2.nContiguous(2*interval , 0 );
		int val4 = qfo2.nContiguous(3*interval , 0 );		
		//elapsed = System.currentTimeMillis()-start;
		int ans = size*(interval-1);
		//System.out.println(val1);
		assert(val1==ans);
		assert(val2==ans);
		assert(val3==ans);
		assert(val4==ans);
		//System.out.println(elapsed);

	}//end of performanceTest

}//end of class
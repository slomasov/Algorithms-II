package edu.yu.introtoalgs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BigOTest{

	BigOIt2 oTest = new BigOIt2();

	@Test
	public void mysteryTest(){
		//Mystery myMystery = new Mystery();
		double ratio = oTest.doublingRatio(Mystery.class.getName(), 100000000);
		//System.out.println(ratio);
		assert(!Double.isNaN(ratio));
		assert(ratio==2);
		//assert(ratio<=1.1);
	}
}
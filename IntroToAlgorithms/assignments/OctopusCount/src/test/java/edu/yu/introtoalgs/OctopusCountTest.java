package edu.yu.introtoalgs;

import org.junit.jupiter.api.Test;
import edu.yu.introtoalgs.OctopusCountI.ArmColor;
import edu.yu.introtoalgs.OctopusCountI.ArmTexture;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class OctopusCountTest{
	OctopusCount ocTest = new OctopusCount();
	//ArmTexture[] enTest = {ArmTexture.SMOOTH, ArmTexture.SMOOTH};
	//final ArmColor[] enTests = {ArmColor.GRAY, ArmColor.GRAY};

	@Test
	public void duplicatesTest(){

	final ArmColor [ ] colors1 = { ArmColor.GRAY, ArmColor.GRAY, ArmColor.GRAY, ArmColor.RED, ArmColor.RED, ArmColor.RED, ArmColor.BLACK, ArmColor.BLACK };
	final int [ ] lengthInCM1 = { 1,2,3,4,5,6,7,8};
	final ArmTexture [] textures1 = { ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SLIMY , ArmTexture.SLIMY , ArmTexture.SLIMY , ArmTexture.STICKY , ArmTexture.STICKY };

		//Testing invalid inputs
		assertThrows(IllegalArgumentException.class, () -> {
			this.ocTest.addObservation(-1, colors1, lengthInCM1, textures1);
		});

		assertThrows(IllegalArgumentException.class, () -> {
			ocTest.addObservation(10, colors1, lengthInCM1, null);
		});

		final ArmTexture[] temp = {null, null, null, null, null, null, null, null};
		assertThrows(IllegalArgumentException.class, () -> {
			ocTest.addObservation(10, colors1, lengthInCM1, temp);
		});


		//Adding observations
		ocTest.addObservation(0, colors1, lengthInCM1, textures1);
		assertEquals(1, ocTest.countThem(), "should be one unique observation");
		ocTest.addObservation(1, colors1, lengthInCM1, textures1);
		assertEquals(1, ocTest.countThem(), "should be one unique observation");

		ArmColor[] colors2 = Arrays.copyOf(colors1,8);
		int [ ] lengthInCM2 = { 8,2,3,4,5,6,7,1};
		ArmTexture[] textures2 = Arrays.copyOf(textures1,8);

		//Switching last and first element in colors2
		ArmColor tempC = colors2[0];
		colors2[0] = colors2[7];
		colors2[7] = tempC;

		//Switching last and first element in textures2
		ArmTexture tempT = textures2[0];
		textures2[0] = textures2[7];
		textures2[7] = tempT;

		//making sure the swap was correct
		assert(colors2[0]==(colors1[7]));
		assert(colors2[7]==(colors1[0]));
		assert(textures2[0]==(textures1[7]));
		assert(textures2[7]==(textures1[0]));
		assert(lengthInCM2[0]==lengthInCM1[7]);
		assert(lengthInCM2[7]==lengthInCM1[0]);


		//Adding switched observation
		ocTest.addObservation(2, colors2, lengthInCM2, textures2);
		assertEquals(1, ocTest.countThem(), "should be one unique observation");

		//Changing 2nd octopus so it's unique
		colors2[2]=ArmColor.RED;
		ocTest.addObservation(3, colors2, lengthInCM2, textures2);
		assertEquals(2, ocTest.countThem(), "should be two unique observations as the new octopus is now unique");

		//Changing 2nd octopus so it's no longer unique
		colors2[3] = ArmColor.GRAY;
		textures2[2] = ArmTexture.SLIMY;
		textures2[3] = ArmTexture.SMOOTH;
		lengthInCM2[2]=4;
		lengthInCM2[3]=3;

		ocTest.addObservation(4, colors2, lengthInCM2, textures2);
		assertEquals(2, ocTest.countThem(), "should be two unique observations as the new octopus is no longer unique");
	}



	@Test
	public void speedTest(){
		ArmColor [ ] colors1 = { ArmColor.GRAY, ArmColor.GRAY, ArmColor.GRAY, ArmColor.RED, ArmColor.RED, ArmColor.RED, ArmColor.BLACK, ArmColor.BLACK };
		int [ ] lengthInCM1 = { 1,2,3,4,5,6,7,8};
		ArmTexture [] textures1 = { ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SMOOTH, ArmTexture.SLIMY , ArmTexture.SLIMY , ArmTexture.SLIMY , ArmTexture.STICKY , ArmTexture.STICKY };

		ArmColor[] colors2 = Arrays.copyOf(colors1,8);
		int [ ] lengthInCM2 = { 8,2,3,4,5,6,7,1};
		ArmTexture[] textures2 = Arrays.copyOf(textures1,8);

		//Switching last and first element in colors2
		ArmColor tempC = colors2[0];
		colors2[0] = colors2[7];
		colors2[7] = tempC;

		//Switching last and first element in textures2
		ArmTexture tempT = textures2[0];
		textures2[0] = textures2[7];
		textures2[7] = tempT;

		//making sure the swap was correct
		assert(colors2[0]==(colors1[7]));
		assert(colors2[7]==(colors1[0]));
		assert(textures2[0]==(textures1[7]));
		assert(textures2[7]==(textures1[0]));
		assert(lengthInCM2[0]==lengthInCM1[7]);
		assert(lengthInCM2[7]==lengthInCM1[0]);

		ArmColor[] colors3 = Arrays.copyOf(colors1,8);
		int [ ] lengthInCM3 = { 8,2,3,4,5,6,7,1};
		ArmTexture[] textures3 = Arrays.copyOf(textures1,8);

		OctopusCount ocSpeed = new OctopusCount();

		//HashSet<Integer> numberSet = new HashSet<>();
		//for(int i = 9; i<25; i++){
		//	numberSet.add()
		//}
		int iter=512;
		int count=0;
		long[] times = new long[30];

		long start = System.nanoTime();
		int max = 9999999;

		for(int i=0;i<max;i++){
			if(i%3==2){
				ocSpeed.addObservation(i, colors1, lengthInCM1, textures1);
			}
			else if(i%3==1){
				ocSpeed.addObservation(i, colors2, lengthInCM2, textures2);
			}
			else if(i%3==0){
				ocSpeed.addObservation(i, colors3, lengthInCM3, textures3);
			}
			if(i+1==iter){
				iter*=2;
				times[count]=System.nanoTime()-start;
				//System.out.println(System.nanoTime()-start);
				count++;
			}
		}

		//long finish = System.nanoTime();
		//long timeElapsed = finish - start;

		for (int i=1; i<times.length;i++){
			try{
				System.out.println((double)times[i]/(double)times[i-1]);
			}
			catch(NullPointerException e){
				return;
			}
		}
		//System.out.println(times);
	}

	private ArmColor[] generateColors(){
		ArmColor[] colors = new ArmColor[8];
		Random rand = new Random();
		int temp = 0;

		for(int i=0; i<8; i++){
			temp = rand.nextInt(3);
			colors[i]=numToColor(temp);
		}

		return colors;
	}

	private ArmTexture[] generateTextures(){
		ArmTexture[] textures = new ArmTexture[8];
		Random rand = new Random();
		int temp = 0;

		for(int i=0; i<8; i++){
			temp = rand.nextInt(3);
			textures[i]=numToTexture(temp);
		}

		return textures;
	}

	private ArmColor numToColor(int i){
		if(i==0){
			return ArmColor.GRAY;
		}
		else if(i==1){
			return ArmColor.RED;
		}
		else{
			return ArmColor.BLACK;
		}
	}

	private ArmTexture numToTexture(int i){
		if(i==0){
			return ArmTexture.SMOOTH;
		}
		else if(i==1){
			return ArmTexture.SLIMY;
		}
		else{
			return ArmTexture.STICKY;
		}
	}
}
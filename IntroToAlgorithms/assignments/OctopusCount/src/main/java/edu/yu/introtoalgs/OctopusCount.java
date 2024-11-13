package edu.yu.introtoalgs;

import java.util.*;

public class OctopusCount implements OctopusCountI{

	private HashSet<HashSet> uniqueObs;

	class Arm{
		ArmColor color;
		int length;
		ArmTexture texture;

		Arm(ArmColor color, int length, ArmTexture texture){
			this.color = color;
			this.length = length;
			this.texture = texture;
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
			Arm temp = (Arm) obj;
			return this.hashCode()==temp.hashCode();
		}

	    @Override
	    public int hashCode(){
	    	int result = this.color.name().hashCode();
	    	result = 31*result + this.texture.name().hashCode();
	    	result = 31*result + this.length;
	    	return result;
	    }

	}

	public OctopusCount(){
		this.uniqueObs = new HashSet<>();
	}
	
	/** A single octopus observation, consisting of a set of arrays (each of size
   	* exactly N_ARMS), such that the ith element of each array describes the
   	* characteristics of the ith arm of the observed octopus.
   	*
   	* @param observationId non-negative integer, uniquely labels the observation
   	* (multiple observations can map to the same octopus),
   	* @param colors the color of the ith arm, not null, elements can't be null
   	* @param lengthInCM the length of the ith arm, not null, elements must be
   	* positive integers
   	* @param textures the texture of the ith arm, not null, elements can't be
   	* null
   	* @throws IllegalArgumentException if any of the parameter conditions are
   	* violated: e.g., there aren't exactly N_ARMS values for each arm
   	* characteristic or if a lengthInCM value is not a positive integer.
   	*/
	@Override
  	public void addObservation(int observationId, ArmColor[] colors, int[] lengthInCM, ArmTexture[] textures){
  		//check correctness
  		if(observationId<0 || colors==null || textures==null || lengthInCM==null || arrayInvalid(colors) || arrayInvalid(textures) || intsInvalid(lengthInCM)){
  			throw new IllegalArgumentException("invalid observation");
  		}

  		//set of arms to be compared
  		HashSet<Arm> arms = new HashSet<>();
  		for(int i=0; i<8; i++){
  			arms.add(new Arm(colors[i],lengthInCM[i],textures[i]));
  		}
  		uniqueObs.add(arms);
  		return;
  	}

  	private boolean arrayInvalid(Object[] objs){
  		if(objs.length!=8){
  			return true;
  		}
  		for(int i=0; i<8; i++){
  			if(objs[i]==null){
  				return true;
  			}
  		}
  		return false;
  	}

  	private boolean intsInvalid(int[] ints){
  		if(ints.length!=8){
  			return true;
  		}
  		for(int i=0; i<8; i++){
  			if(ints[i]<=0){
  				return true;
  			}
  		}
  		return false;
  	}
                     
  	/** Returns the number of unique octopus instances from the set of current
   	* observations.
   	*
   	* @return the number of unique instances.
  	*/
  	@Override
  	public int countThem(){
  		return this.uniqueObs.size();
  	}
}
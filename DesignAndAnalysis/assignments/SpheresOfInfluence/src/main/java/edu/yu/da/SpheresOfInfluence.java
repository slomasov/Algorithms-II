package edu.yu.da;

import java.util.*;

//Greedy structure: at each step, among all the elements covering the left-most uncovered corner,
//choose the one with max (x+r), i.e. right-most corner.

public class SpheresOfInfluence extends SpheresOfInfluenceBase {

  class Segment implements Comparable<Segment>{
  	public String id;
  	public double xMin;
  	public double xMax;

  	public Segment(String id, double xMin, double xMax){
      this.id = id;
      this.xMin=xMin;
      this.xMax=xMax;
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
      Segment temp = (Segment) obj;
      return ((this.xMin==temp.xMin) && (this.xMax==temp.xMax));
    }

    @Override
    public int hashCode(){
	   	int result = Double.hashCode(xMin);
        result = 31 * result + Double.hashCode(xMax);
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public int compareTo(Segment o){
    	double temp = this.xMin-o.xMin;
      if(temp<0) return -1;
      if(temp==0) return 0;
      return 1;
    }
  }
  
  private int maxStrength;
  private int maxRight;
  private List<Segment> segments;

  public SpheresOfInfluence(final int maxStrength, final int maxRight) {
  	super(maxStrength, maxRight);
  	if(maxStrength<=0 || maxRight<=0){
  		throw new IllegalArgumentException("maxStrength and maxRight must be greater than 0");
  	}
  	this.maxStrength=maxStrength;
  	this.maxRight=maxRight;
    this.segments = new ArrayList<>();
  }//constructor
  
  @Override
  public void addInfluencer(final String id, final int xValue, final int radius){
  	if(id==null||id.isEmpty()||xValue<0 ||radius<=0){
  		throw new IllegalArgumentException();
  	}
  	double halfS = maxStrength/2.0;
    //System.out.println(halfS);
    //System.out.println(radius);
  	if(radius<halfS){
  		this.segments.add(new Segment(id,-1,-1));
  	}
  	else if(radius==halfS){
  		this.segments.add(new Segment(id,xValue,xValue));
  	}
  	else{
  		double temp = Math.sqrt( (radius-halfS)*(radius+halfS) );
      //System.out.println(temp);
  		this.segments.add(new Segment(id,xValue-temp,xValue+temp));
  	}
    //System.out.println();
  }//addInfluencer

  @Override
  public List<String> getMinimalCoverageInfluencers(){
  	Collections.sort(segments);
    //System.out.println(segments.get(0).xMin);
    //System.out.println(segments.get(0).xMax);

    //System.out.println(segments.get(1).xMin);
    //System.out.println(segments.get(1).xMax);
  	List<String> result = new ArrayList<>();
  	double u = 0;
  	int index = 0;
  	int pointer = 0;
  	double maxR = -1;
  	int maxIndex = -1;
  	while(u<this.maxRight){
  		index = findIndex(pointer, u);
      //System.out.println(index);
  		if(index==-1) return Collections.<String>emptyList();
  		for(int i=pointer; i<index; i++){
  			if(segments.get(i).xMax>maxR){
  				maxR=segments.get(i).xMax;
  				maxIndex=i;
  			}
  		}
  		pointer = index;
  		if(pointer==segments.size() &&maxR<this.maxRight) return Collections.<String>emptyList();
  		//	if(u<this.maxRight) return Collections.<String>emptyList();
  		//}
  		if(maxR<u) return Collections.<String>emptyList();
  		result.add(segments.get(maxIndex).id);
  		u = maxR;
      //System.out.println(result.get(0));
  	}
    Collections.sort(result);
  	return result;
  }//getMinimalCoverage

  private int findIndex(int pointer, double u){

    /*
    u = target
    left = pointer
    right = list.size()-1
    
    while(left<=right):
      mid = left + (right-left)/2
      if(segments.get(mid).xMin<u) left = mid+1;
      else{
        result = mid;
        right = mid-1;
      }
    */

  	int left = pointer;
  	int right = segments.size()-1;
  	int result = -1;

    if(segments.get(pointer).xMin>u) return -1;
    if(segments.get(right).xMin<=u) return right+1;

  	while(left<=right){
  		int mid = left + (right-left)/2;
  		if(segments.get(mid).xMin>u){
        result = mid;
  			right = mid-1;
  		}
  		else{
        //result = mid;
  			left = mid+1;
  		}
  	}
    // if(left==pointer){
    //   if(segments.get(left).xMin>=u) return -1;
    //   else return left+1;
    // }
    // if(right==result){
    //   if(segments.get(right).xMin>=u) return right+1;
    //   else return right;
    // }
  	return result;
  }//findIndex

}//class
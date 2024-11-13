package edu.yu.da;

import java.util.Arrays;

public class PickAYeshiva extends PickAYeshivaBase {

  class Yeshiva{
    public double faculty;
    public double cooking;

    public Yeshiva(double faculty, double cooking){
      this.faculty=faculty;
      this.cooking=cooking;
    }
  }

  private Yeshiva[] aux;
  private Yeshiva[] yeshivot;
  private double[] resultFaculty;
  private double[] resultCooking;

  public PickAYeshiva(double[] facultyRatioRankings, double[] cookingRankings){
    super(facultyRatioRankings, cookingRankings);
    if(facultyRatioRankings==null || facultyRatioRankings.length==0 || cookingRankings==null || cookingRankings.length==0 || cookingRankings.length!=facultyRatioRankings.length){
      throw new IllegalArgumentException("invalid parameters in constructor");
    }
    int len = facultyRatioRankings.length;
    this.yeshivot = new Yeshiva[len];
    this.aux = new Yeshiva[len];
    for(int i=0; i<len; i++){
      this.yeshivot[i]=new Yeshiva(facultyRatioRankings[i],cookingRankings[i]);
      //this.aux[i]=this.yeshivot[i];
    }
    this.split(0,len-1);
    int yeshivaCount=len;
    this.aux=null;
    //this.resultFaculty=new double[]{1};
    //this.resultCooking=new double[]{1};
    for(int i=0;i<len;i++){
      if(this.yeshivot[i]==null){
        yeshivaCount--;
      }
    }
    this.resultFaculty=new double[yeshivaCount];
    this.resultCooking=new double[yeshivaCount];
    yeshivaCount=0;
    for(int i=0; i<len;i++){
      if(this.yeshivot[i]!=null){
        this.resultFaculty[yeshivaCount]=yeshivot[i].faculty;
        this.resultCooking[yeshivaCount]=yeshivot[i].cooking;
        yeshivaCount++;
      }
    }

  }//constructor

  @Override
  public double[] getFacultyRatioRankings(){
    return this.resultFaculty;
  }

  @Override
  public double[] getCookingRankings(){
    return this.resultCooking;
  }

  private void split(int lo, int hi){
    //Sedgewick
    if(hi<=lo) return;
    int mid = lo+(hi-lo)/2;
    split(lo,mid);
    split(mid+1,hi);
    merge(lo,mid,hi);
  }

  private void merge(int lo, int mid, int hi){
    //Sedgewick
    //assert(this.aux.length==this.yeshivot.length);
    int i=lo;
    int j=mid+1;
    for(int k=lo;k<=hi; k++){
      this.aux[k]=this.yeshivot[k];
    }
    for(int k=lo; k<=hi; k++){
      if(i>mid) this.yeshivot[k]=aux[j++];
      else if(j>hi) this.yeshivot[k]=aux[i++];
      else if(aux[i]==null) this.yeshivot[k]=aux[i++];
      else if(aux[j]==null) this.yeshivot[k]=aux[j++];
      else if(aux[i].faculty==aux[j].faculty){
        yeshivot[k]=null;
        if(aux[i].cooking<aux[j].cooking) i++;
        else j++;
      }
      else if(aux[i].faculty>aux[j].faculty){
        if(aux[i].cooking>=aux[j].cooking){
          this.yeshivot[k]=null;
          //improve the algorithm by NOT comparing subsequent cooking values?
          j++;
        }
        else this.yeshivot[k]=aux[j++];
      }
      else if(aux[i].faculty<aux[j].faculty){
        if(aux[i].cooking<=aux[j].cooking){
          this.yeshivot[k]=null;
          //improve the algorithm by NOT comparing subsequent cooking values?
          i++;
        }
        else this.yeshivot[k]=aux[i++];
      }
    }//initial for loop
  }//merge
  

} // class

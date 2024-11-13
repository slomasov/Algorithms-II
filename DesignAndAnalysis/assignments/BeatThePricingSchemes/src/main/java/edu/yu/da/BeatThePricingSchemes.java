package edu.yu.da;

import java.util.List;
import java.util.ArrayList;

public class BeatThePricingSchemes extends BeatThePricingSchemesBase{

  class PricingScheme{
    public int id;
    public double price;
    public int quantity;

    public PricingScheme(int id, double price, int quantity){
      this.id=id;
      this.price=price;
      this.quantity=quantity;
    }
  }

  private boolean newSchemeAdded;
  private boolean cheapestPriceCalled;
  private int id;
  private List<PricingScheme> schemes;
  private double[] minPrices;
  private List<List<Integer>> decisions;
  private int minIndex;

  public BeatThePricingSchemes(double unitPrice) {
    super(unitPrice);
    if(unitPrice<=0){
    	throw new IllegalArgumentException();
    }
    this.id=1;
    this.schemes = new ArrayList<>(21);
    this.schemes.add(new PricingScheme(UNIT_PRICE_DECISION, unitPrice, 1));
    this.minPrices = new double[201];
    this.minPrices[0]=0;
    this.decisions = new ArrayList<>(201);
    for(int i=0; i<201; i++){
      this.decisions.add(new ArrayList<>());
    }
  }

  @Override
  public void addPricingScheme(double price, int quantity){
  	if(price<=0 || quantity<=0 ||quantity>MAX_MATZOS || this.id>MAX_SCHEMES){
  		throw new IllegalArgumentException();
  	}
    this.newSchemeAdded=true;
    this.schemes.add(new PricingScheme(id++, price, quantity));
  }

  /*
  double minDP=Double.POSITIVE_INFINITY;
  int tempIndex = 0;
  for(int j=0; j<this.schemes.size(); j++){
    int remainingMatzos=i-this.schemes.get(j).quantity;
    if(remainingMatzos>=0){
      if(this.schemes.get(j).price+this.minPrices[remainingMatzos]<minDP){
        minDP=this.schemes.get(j).price+this.minPrices[remainingMatzos];
        tempIndex=j;
      }
    }
  }
  this.minPrices[i]=minDP;
  this.decisions.get(i)
  */

  @Override
  public double cheapestPrice(int threshold){
    if(threshold<=0 || threshold>MAX_MATZOS){
      throw new IllegalArgumentException();
    }
    if(this.newSchemeAdded){
      for(int i=0; i<201; i++){
        this.decisions.set(i,new ArrayList<>());
      }
      for(int i=1; i<201; i++){
          //PricingScheme minPS = new PricingScheme(-1,Double.POSITIVE_INFINITY,-1);
          double minDP = Double.POSITIVE_INFINITY;
          int tempIndex = 0;
          int previous = 0;
          for(int j=0; j<this.schemes.size(); j++){
            int remainingMatzos=i-this.schemes.get(j).quantity;
            if(remainingMatzos>=0){
              if(this.schemes.get(j).price+this.minPrices[remainingMatzos]<minDP){
                minDP=this.schemes.get(j).price+this.minPrices[remainingMatzos];
                previous=remainingMatzos;
                tempIndex=j;
              }
            }
          }
          // for(PricingScheme ps : this.schemes){
          //     //if(i==5 && ps)
          //     int remainingMatzos = i-ps.quantity;
          //     if(remainingMatzos>=0){
          //         if(ps.price+this.minPrices[remainingMatzos]<minPS.price){
          //          minPS=ps;
          //          previous=remainingMatzos;
          //         }
          //     }
          // }
          this.minPrices[i]=minDP;
          //System.out.println(minPrices[i]);
          this.decisions.get(i).addAll(this.decisions.get(previous)); //optimize?
          this.decisions.get(i).add(this.schemes.get(tempIndex).id);
      }
      // System.out.println(minPrices[0]);
      // System.out.println(minPrices[1]);
      // System.out.println(minPrices[2]);
      // System.out.println(minPrices[3]);
      // System.out.println(minPrices[4]);
      // System.out.println(minPrices[5]);
      // System.out.println(minPrices[6]);
      //System.out.println(this.schemes.get(1).price);
      //System.out.println(this.schemes.get(1).quantity);
    }
    this.cheapestPriceCalled=true;
    this.newSchemeAdded=false;

    //see if there's a cheaper combination of more items
    double tempMin = this.minPrices[threshold];
    this.minIndex = threshold;
    for(int i = threshold+1; i<this.minPrices.length;i++){
      if(this.minPrices[i]<tempMin){
        tempMin=this.minPrices[i];
        minIndex=i;
      }
    }
    return tempMin;
  }//getCheapestPrice

  @Override
  public List<Integer> optimalDecisions(){
    if(!this.cheapestPriceCalled){
      throw new IllegalStateException();
    }
    List<Integer> result = new ArrayList();
    result.addAll(this.decisions.get(this.minIndex));
    java.util.Collections.sort(result);
    return result;
  }//optimalDecisions

}//class
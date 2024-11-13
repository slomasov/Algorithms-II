package edu.yu.da;

import java.util.*;

public class WaterPressure extends WaterPressureBase{

  class Edge implements Comparable<Edge>{
    public String from;
    public String to;
    public double weight;

    public Edge(String v1, String v2, double weight){
      this.from=v1;
      this.to=v2;
      this.weight=weight;
    }

    @Override
    public int compareTo(Edge o){
      double temp = this.weight-o.weight;
      if(temp>0) return 1;
      if(temp==0) return 0;
      return -1;
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
      Edge temp = (Edge) obj;
      if(temp.weight==0||this.weight==0){
        return false;
      }
      return ((this.from.equals(temp.from))&&(this.to.equals(temp.to)));
    }

    @Override
    public int hashCode(){
      int result=17;
      result=31*result+this.from.hashCode();
      result=31*result+this.to.hashCode();
      return result;
    }
  }
  
  private boolean secondPumpAdded;
  private int minAmountInvocationCount=0;
  private int vertices=-1;
  private String initialInputPump;
  private String secondInputPump;

  //private Set<String> verticeSet;
  private Map<String, Set<Edge>> graph;
  //private Set<Edge> edgeSet;
  private Map<String,Boolean> visited;
  //private Set<String> visited;
  private PriorityQueue<Edge> pq;

  public WaterPressure(String initialInputPump) {
    super(initialInputPump);
    if(initialInputPump==null || initialInputPump.isEmpty()){
      throw new IllegalArgumentException();
    }
    this.graph = new HashMap<>();
    this.graph.put(initialInputPump,new HashSet<>());
    this.visited = new HashMap<>();
    //this.pq = new PriorityQueue<>();
    this.initialInputPump = initialInputPump;
    //this.verticeSet = new HashSet<>();
    this.visited.put(initialInputPump,false);
    //this.edgeSet = new HashSet<>();
    // sub-classed implementation may want to add code here
  }

  /** Adds a second input pump, differing from the initial input pump, to the
   * channel system.
   *
   * The second input pump must already be in the channel system (via
   * addBlockage): this method only designates the pump as also being an input
   * pump.
   *
   * @param secondInputPump, length must be greater than 0.
   * @throws IllegalArgumentException if the pre-conditions are violated.
   */   
  @Override
  public void addSecondInputPump(String secondInputPump){
    if(secondInputPump==null || secondInputPump.isEmpty() || !this.visited.containsKey(secondInputPump) || this.secondPumpAdded){
      throw new IllegalArgumentException();
    }
    this.secondPumpAdded=true;
    this.graph.get(this.initialInputPump).add(new Edge(initialInputPump, secondInputPump, 0));
    this.secondInputPump = secondInputPump;
  }

  /** Specifies a blockage on a channel running from pump station v to pump
   * station w.  The presence of a blockage implies that water can only flow on
   * the channel if a quantity of water greater or equal to "blockage" is
   * pumped by pump station v to w.
   *
   * The two pump stations must differ from one another, and no channel can
   * already exist between the two pump stations.
   *
   * @param v specifies a pump station, length must be > 0.
   * @param w specifies a pump station, length must be > 0.
   * @param blockage the magnitude of the blockage on the channel, must be > 0.
   * @throws IllegalStateException if minAmount() has previously been invoked.
   * @throws IllegalArgumentException if the other pre-conditions are violated.
   */
  @Override
  public void addBlockage(String v, String w, double blockage){
    if(v==null || w==null || v.isEmpty() || w.isEmpty() ||v.equals(w) || blockage<=0){
      throw new IllegalArgumentException();
    }
    if(minAmountInvocationCount!=0){
      throw new IllegalStateException("min amount has already been invoked");
    }
    Edge tempEdge = new Edge(v,w,blockage);
    //if(this.edgeSet.contains(tempEdge)){
    //  throw new IllegalArgumentException("edge has already been added");
    //}
    Set<Edge> tempSet = this.graph.get(v);
    if(tempSet==null){
      this.graph.put(v, new HashSet<>());
    }
    else{
      if(tempSet.contains(tempEdge)){
        throw new IllegalArgumentException("edge has already been added");
      }
    }
    this.graph.get(v).add(tempEdge);
    //this.edgeSet.add(tempEdge);
    this.visited.put(v,false);
    this.visited.put(w,false);
  } //addBlockage
  
  /** Client asks implementation to determine the minimum amount of water that
   * must be supplied to the initial input pump to ensure that water reaches
   * every pump station in the existing channel system.  If a second input pump
   * has been added to the channel system, the sematics of "minimum amount" is
   * the "minimum amount of water that must be supplied to BOTH input pump
   * stations".
   *
   * @return the minimum amount of water that must be supplied to the input
   * pump(s) to ensure that water reaches every pump station.  If the channel
   * system has been misconfigured such that no amount of water pumped from the
   * input pump stations can get water to all the pump stations, returns -1.0
   * as as sentinel value.
   */
  @Override
  public double minAmount(){
    if(minAmountInvocationCount>1){
      throw new IllegalStateException("minAmount has been invoked twice");
    }
    if(this.vertices==-1){
      this.vertices = this.visited.size();
    }
    //this.visited = new HashSet<>();
    this.pq = new PriorityQueue<>();
    minAmountInvocationCount++;
    //manage second invocation which is the same as first (i.e both times first pump or second pump); also manage second invocation (clear? etc.)
    this.visited.put(this.initialInputPump,true);
    for(Edge e : this.graph.get(this.initialInputPump)){
      this.pq.add(e);
    }
    if(this.pq.isEmpty()) return 0;
    //int vertices = this.verticeSet.size();
    //System.out.print(vertices);
    //System.out.print(" ");
    int vertexCount=1;
    double minWeight=-1;
    while(!this.pq.isEmpty() && vertexCount!=this.vertices){
      Edge e = this.pq.poll();
      //System.out.println(e.from);
      String v = e.to;
      if(visited(v)) continue;
      vertexCount++;
      if(e.weight>minWeight) minWeight = e.weight;
      this.visited.put(v,true);
      Set<Edge> tempList = this.graph.get(v);
      if(tempList!=null){
        for(Edge edg : tempList){
          //if(!visited(edg.to)){
            this.pq.add(edg);
          //}
        }
      }
    }
    //System.out.println(vertexCount);
    double temp = minWeight;
    if(vertexCount!=vertices){
      temp = -1;
    }

    for(String s :this.visited.keySet()){
      this.visited.put(s,false);
    }

    //clear values for 2nd invocation
    //this.graph = new HashMap<>();
    //this.graph.put(initialInputPump,new ArrayList<>());
    //if(this.secondPumpAdded){
      //this.graph.get(this.initialInputPump).add(new Edge(this.initialInputPump, this.secondInputPump, 0));
    //}

    return temp;
    
  } //minAmount

  private boolean visited(String v){
    return this.visited.get(v);
  }

} // class

package edu.yu.da;

import java.util.*;

// idea: whenever warehouse capacity (w,c) is added, model it as a small road w-w_aux after it with capacity c.
// All edges coming out of w must instead come ouf of w_aux.

/*
When calling max, what's my input? rGraph.
add edge: //
*/

//credit for Ford-Fulkerson: https://www.geeksforgeeks.org/ford-fulkerson-algorithm-for-maximum-flow-problem/

public class MatzoDistribution extends MatzoDistributionBase {

  class Edge{
    public String from;
    public String to; // To store destination vertex
    public int capacity; // To store capacity of the edge

    public Edge(String from, String to, int capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
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
      return ((this.from.equals(temp.from))&&(this.to.equals(temp.to)));
    }

    @Override
    public int hashCode(){
      int result=17;
      result=31*result+this.from.hashCode();
      result=31*result+this.to.hashCode();
      return result;
    }
  }//edge

  private String source;
  private String sink;
  private int vertices;
  private Map<String,String> warehouses = new HashMap<>();
  private Set<Edge> roads = new HashSet<>();
  private Map<String, List<Edge>> rGraph = new HashMap<>();

  public MatzoDistribution(final String sourceWarehouse, final int sourceConstraint, final String destinationWarehouse){
    super(sourceWarehouse, sourceConstraint, destinationWarehouse);
    if(sourceWarehouse==null || sourceWarehouse.isEmpty() || destinationWarehouse==null || destinationWarehouse.isEmpty()
    || sourceWarehouse.equals(destinationWarehouse) || sourceConstraint<=0){
      throw new IllegalArgumentException("invalid constructor parameters");
    }
    String alias = getAlias(sourceWarehouse);
    this.source = sourceWarehouse;
    this.sink = destinationWarehouse;
    this.warehouses.put(sourceWarehouse, alias);
    this.warehouses.put(destinationWarehouse, null);
    this.rGraph.put(sourceWarehouse, new ArrayList<>());
    this.rGraph.put(alias, new ArrayList<>());
    this.rGraph.put(destinationWarehouse, new ArrayList<>());
    this.rGraph.get(sourceWarehouse).add(new Edge(sourceWarehouse, alias,sourceConstraint));
  }

  private String getAlias(String s){
    byte[] array = new byte[10];
    new Random().nextBytes(array);
    String tempString = new String(array);
    return s + tempString;
  }

  @Override 
  public void addWarehouse(String warehouseId, int constraint){
    if(warehouseId==null || warehouseId.isEmpty() || constraint<=0 || this.warehouses.containsKey(warehouseId)){
      throw new IllegalArgumentException("invalid addWarehouse parameters");
    }
    String alias = getAlias(warehouseId);
    this.warehouses.put(warehouseId, alias);
    this.rGraph.put(warehouseId, new ArrayList<>());
    this.rGraph.put(alias, new ArrayList<>());
    this.rGraph.get(warehouseId).add(new Edge(warehouseId, alias, constraint));
  }

  @Override
  public void roadExists(String w1, String w2, int constraint){
    if(w1==null || w1.isEmpty() || w2==null || w2.isEmpty()|| w1.equals(w2) || constraint<=0
    || !this.warehouses.containsKey(w1) || !this.warehouses.containsKey(w2) || this.roads.contains(new Edge(w1,w2,constraint))
    || w1.equals(this.sink) || w2.equals(this.source)){
      throw new IllegalArgumentException("invalid roadExists parameters");
    }
    //All edges coming out of w must instead come ouf of w_aux.
    String alias = this.warehouses.get(w1);
    //System.out.println(this.rGraph.get(alias));
    this.rGraph.get(alias).add(new Edge(alias, w2, constraint));
    this.rGraph.get(w2).add(new Edge(w2, alias, 0));
    // if(!this.roads.containsKey(new Edge(w2, w1))){  //if two-sided road
    //   this.rGraph.get(w2)
    // }
    this.roads.add(new Edge(w1, w2, constraint));
  }

  @Override
  public int max(){
    return fordFulkerson(this.source, this.sink);
  }

  private int fordFulkerson(String s, String t) {
    int max_flow = 0;
    Map<String, String> parent = new HashMap<>();
    Map<String, List<Edge>> rGraphCopy = new HashMap<>();
    for(String str : this.rGraph.keySet()){
      rGraphCopy.put(str, new ArrayList<>());
      List<Edge> tempList = this.rGraph.get(str);
      for(Edge edg : tempList){
        rGraphCopy.get(str).add(new Edge(edg.from, edg.to, edg.capacity));
      }
    }
    // Map<String, List<Edge>> rGraph = new HashMap<>();
    // for (Map.Entry<String, List<Edge>> entry : graph.entrySet()) {
    //     String u = entry.getKey();
    //     rGraph.put(u, new ArrayList<>());
    //     for (Edge edge : entry.getValue()) {
    //         rGraph.get(u).add(new Edge(edge.to, edge.capacity));
    //     }
    // }
    while (bfs(rGraphCopy, s, t, parent)) {
        // Find minimum residual capacity of the edges along the path filled by BFS
        int path_flow = Integer.MAX_VALUE;
        for (String v = t; !v.equals(s); v = parent.get(v)) {
            String u = parent.get(v);
            //for (Edge edge : this.rGraph.get(u)) {
            for (Edge edge : rGraphCopy.get(u)) {
                if (edge.to.equals(v)) {
                    path_flow = Math.min(path_flow, edge.capacity);
                    break;
                }
            }
        }

        // Update residual capacities of the edges and reverse edges along the path
        for (String v = t; !v.equals(s); v = parent.get(v)) {
            String u = parent.get(v);
            //for (Edge edge : this.rGraph.get(u)) {
            for (Edge edge : rGraphCopy.get(u)) {
                if (edge.to.equals(v)) {
                    edge.capacity -= path_flow;
                    //break;
                }
            }
            //this.rGraph.get(v).add(new Edge(v, u, path_flow));
            for (Edge edge : rGraphCopy.get(v)) {
                if (edge.to.equals(u)) {
                  //System.out.print("edge found ");
                    edge.capacity += path_flow;
                    //break;
                }
            }
            //rGraphCopy.get(v).add(new Edge(v, u, path_flow));
        }

        // Add path flow to overall flow
        max_flow += path_flow;
    }

    // Return the overall flow
    return max_flow;
  }

  private boolean bfs(Map<String, List<Edge>> graph, String s, String t, Map<String, String> parent) {
    Set<String> visited = new HashSet<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(s);
    visited.add(s);
    parent.put(s, null);

    while (!queue.isEmpty()) {
        String u = queue.poll();

        for (Edge edge : graph.getOrDefault(u, new ArrayList<>())) {
            String v = edge.to;
            if (!visited.contains(v) && edge.capacity > 0) {
                if (v.equals(t)) {
                    parent.put(v, u);
                    return true;
                }
                queue.add(v);
                visited.add(v);
                parent.put(v, u);
            }
        }
    }
    return false;
  }


} // class

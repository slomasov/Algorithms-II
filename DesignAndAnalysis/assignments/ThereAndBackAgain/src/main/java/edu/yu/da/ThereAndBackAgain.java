package edu.yu.da;

import java.util.*;

public class ThereAndBackAgain extends ThereAndBackAgainBase{

  class IndexedMinPQ { //Sedgewick
    private int maxSize;
    private int size;
    private String[] keys;
    private double[] values;
    private int[] pq;
    private int[] qp;

    public IndexedMinPQ(int maxSize) {
        this.maxSize = maxSize;
        size = 0;
        keys = new String[maxSize + 1];
        values = new double[maxSize + 1];
        pq = new int[maxSize + 1];
        qp = new int[maxSize + 1];
        Arrays.fill(qp, -1);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(String key) {
        int temp = index(key);
        if(temp==-1){
            return false;
        }
        return qp[temp] != -1;
    }

    public void insert(String key, double value) {
        size++;
        keys[size] = key;
        values[size] = value;
        qp[index(key)] = size;
        pq[size] = size;
        swim(size);
    }

    public String delMin() {
        String minKey = keys[pq[1]];
        exchange(1, size--);
        sink(1);
        qp[index(minKey)] = -1;
        keys[pq[size + 1]] = null;
        return minKey;
    }

    public void change(String key, double value) {
        int i = index(key);
        values[i] = value;
        swim(qp[i]);
        sink(qp[i]);
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exchange(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && greater(j, j + 1))
                j++;
            if (!greater(k, j))
                break;
            exchange(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
        return values[pq[i]] > values[pq[j]];
    }

    private void exchange(int i, int j) {
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private int index(String key) {
        return Arrays.asList(keys).indexOf(key);
    }

  } //indexedMinPQ

  class Edge{
    public String from;
    public String to;
    public double weight;

    public Edge(String v1, String v2, double weight){
      this.from=v1;
      this.to=v2;
      this.weight=weight;
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
      return ((this.from.equals(temp.from))&&(this.to.equals(temp.to))) || ((this.from.equals(temp.to))&&(this.to.equals(temp.from)));
    }

    @Override
    public int hashCode(){
      return this.from.hashCode()^this.to.hashCode();
    }
  } //edge

  private String startVertex;
  private boolean done;
  private Map<String, List<Edge>> graph;
  private Set<String> visited;
  private Map<String, Edge> edgeTo;
  private Map<String, Edge> edgeToOther;
  private Map<String, Double> distTo;
  private Set<String> onPath;
  private String goal;
  private List<String> one = null;
  private List<String> other = null;
  private IndexedMinPQ pq;
  private Set<Edge> edgeSet;

  /** Constructor which supplies the start vertex
   *
   * @param startVertex, length must be > 0.
   * @throws IllegalArgumentException if the pre-condiitions are
   * violated
   */
  public ThereAndBackAgain(String startVertex) {
    super(startVertex);
    if(startVertex==null || startVertex.isEmpty()){
      throw new IllegalArgumentException("start vertex is null or empty");
    }
    this.startVertex = startVertex;
    this.graph = new HashMap<>();
    this.graph.put(startVertex, new ArrayList<>());
    this.edgeSet = new HashSet<>();
    // sub-class may want to add more function!
  }

  /** Adds an weighted undirected edge between vertex v and vertex w.  The two
   * vertices must differ from one another, and an edge between the two
   * vertices cannot have been added previously.
   *
   * @param v specifies a vertex, length must be > 0.
   * @param w specifies a vertex, length must be > 0.
   * @param weight the edge's weight, must be > 0.
   * @throws IllegalStateException if doIt() has previously been invoked.
   * @throws IllegalArgumentException if the other pre-conditions are violated.
   */
  @Override
  public void addEdge(String v, String w, double weight){
    if(this.done){
      throw new IllegalStateException();
    }
    if(v==null || v.isEmpty() || w==null || w.isEmpty()){
      throw new IllegalArgumentException("can't add edge: vertex is null or empty");
    }
    if(v.equals(w)){
      throw new IllegalArgumentException("can't add edge: vertices are equal");
    }
    if(weight<=0){
      throw new IllegalArgumentException("can't add edge: weight is not positive");
    }
    boolean hasV = this.graph.containsKey(v);
    boolean hasW = this.graph.containsKey(w);
    Edge temp1 = new Edge(v,w,weight);
    if(this.edgeSet.contains(temp1)){
      throw new IllegalArgumentException("can't add edge: the edge already exists");
    }
    this.edgeSet.add(temp1);
    // if(hasV && hasW){
    //   throw new IllegalArgumentException("can't add edge: the edge already exists");
    // }

    //adding
    if(!hasV){
      this.graph.put(v, new ArrayList<>());
    }
    //assert(this.graph.containsKey(v));
    this.graph.get(v).add(temp1);
    if(!hasW){
      this.graph.put(w, new ArrayList<>());
    }
    //assert(this.graph.containsKey(w));
    this.graph.get(w).add(new Edge(w, v, weight));
  } //addEdge
  
  /** Client informs implementation that the graph is fully constructed and
   * that the ThereAndBackAgainBase algorithm should be run on the graph.
   * After the method completes, the client is permitted to invoke the
   * solution's getters.
   *
   * Note: once invoked, the implementation must ignore subsequent calls to
   * this method.
   * @throws IllegalStateException if doIt() has previously been invoked.
   */
  @Override
  public void doIt(){
    if(this.done){
      throw new IllegalStateException();
    }
    this.done=true;

    //Sedgewick
    this.edgeTo = new HashMap<>();
    this.edgeToOther = new HashMap<>();
    this.distTo = new HashMap<>();
    this.visited = new HashSet<>();
    this.onPath = new HashSet<>();
    this.pq = new IndexedMinPQ(250000);
    for(String s : this.graph.keySet()){
      this.distTo.put(s, Double.POSITIVE_INFINITY);
      this.distTo.put(this.startVertex, 0.0);
    }
    this.pq.insert(this.startVertex, 0.0);
    this.edgeTo.put(this.startVertex, null);
    this.edgeToOther.put(this.startVertex, null);
    while(!this.pq.isEmpty()){
      String temp = this.pq.delMin();
      if(temp==null){
        break;
      }
      //assert(this.graph.containsKey(temp));
      if(!visited(temp)){
        this.visited.add(temp);
        relax(temp);
      }
    }

    //assert(!this.onPath.isEmpty());
    if(this.onPath.isEmpty()){
      this.goal = null;
      this.one = Collections.<String>emptyList();
      this.other = Collections.<String>emptyList();
      // this.one = Collections.EMPTY_LIST;
      // this.other = Collections.EMPTY_LIST;
    }
    else{
      this.goal=this.startVertex;
      for(String s : this.onPath){
        if(this.distTo.get(goal)<this.distTo.get(s)){
          this.goal = s;
        }
      }
      this.one = new ArrayList<>();
      this.other = new ArrayList<>();
      this.one.add(this.goal);
      this.other.add(this.goal);
      Edge e = this.edgeTo.get(this.goal);
      while(e!=null){
        this.one.add(e.from);
        e = this.edgeTo.get(e.from);
      }

      Edge eOther = this.edgeToOther.get(this.goal);
      while(eOther!=null){
        this.other.add(eOther.from);
        eOther = this.edgeToOther.get(eOther.from);
      }

      Collections.reverse(this.one);
      Collections.reverse(this.other);
      if(this.other.hashCode()<this.one.hashCode()){
        List<String> temp = this.one;
        this.one = this.other;
        this.other = temp;
      }
    }

  } //doIt

  private void relax(String v){
    //Sedgewick
    boolean isOnPath = this.onPath.contains(v);
    //assert(this.graph.containsKey(v));
    for(Edge e : this.graph.get(v)){
      if(!visited(e.to)){
        double distInitial = this.distTo.get(e.to);
        double distNew = this.distTo.get(v)+e.weight;
        //System.out.println(distInitial);
        //System.out.println(distNew);
        if(distInitial>distNew){
          this.distTo.put(e.to, distNew);
          this.edgeTo.put(e.to, e);
          this.edgeToOther.put(e.to, e);
          if(this.pq.contains(e.to)){
            //int aaaa=0;
            this.pq.change(e.to, distNew);
          }
          else{
            this.pq.insert(e.to, distNew);
          }
        }
        if(!isOnPath){
          if(distInitial==distNew){
            this.onPath.add(e.to);
            this.edgeToOther.put(e.to,e);
          }
        }
        if(isOnPath){
          this.onPath.add(e.to);
        }
      }
    }
  } //relax

  private boolean visited(String s){
    return this.visited.contains(s);
  } //visited

  /** If the graph contains a "goal vertex of the longest valid path" (as
   * defined by the requirements document), returns it.  Else returns null.
   *
   * @return goal vertex of the longest valid path if one exists, null
   * otherwise.
   */
  @Override
  public String goalVertex(){
    if(!this.done){
      throw new IllegalStateException();
    }
    // if(this.onPath.isEmpty()){
    //   return null;
    // }
    // this.goal=this.startVertex;
    // for(String s : this.onPath){
    //   if(this.distTo.get(goal)<this.distTo.get(s)){
    //     this.goal = s;
    //   }
    // }
    return this.goal;
  } //goalVertex

  /** Returns the cost (sum of the edge weights) of the longest valid path if
   * one exists, 0.0 otherwise.
   *
   * @return the cost if the graph contains a longest valid path, 0.0
   * otherwise.
   */
  @Override
  public double goalCost(){
    if(!this.done){
      throw new IllegalStateException();
    }
    if(this.onPath.isEmpty()){
      return 0.0;
    }
    return this.distTo.get(this.goal);
  }

  /** If a longest valid path exists, returns a ordered sequence of vertices
   * (beginning with the start vertex, and ending with the goal vertex)
   * representing that path.
   *
   * IMPORTANT: given the existence of (by definition) two longest valid paths,
   * this method returns the List with the LESSER of the two List.hashCode()
   * instances.
   *
   * @return one of the two longest paths, Collections.EMPTY_LIST if the graph
   * doesn't contain a longest valid path.
   */
  @Override
  public List<String> getOneLongestPath(){
    if(!this.done){
      throw new IllegalStateException();
    }
    return this.one;

  } //getOneLongestPath

  /** If a longest valid path exists, returns the OTHER ordered sequence of
   * vertices (beginning with the start vertex, and ending with the goal
   * vertex) representing that path.
   *
   * IMPORTANT: given the existence of (by definition) two longest valid paths,
   * this method returns the List with the GREATER of the two List.hashCode()
   * instances.
   *
   * @return the other of the two longest paths, Collections.EMPTY_LIST if the
   * graph doesn't contain a longest valid path.
   */
  @Override
  public List<String> getOtherLongestPath(){
    if(!this.done){
      throw new IllegalStateException();
    }
    return this.other;
  } //getOtherLongestPath

} // class
package edu.yu.introtoalgs;

import java.util.Queue;
import java.util.LinkedList;

public class QuestForOil extends QuestForOilBase{

  private char[][] map;
  private boolean[][] marked;
  private int count = 0;

  public QuestForOil(char[][] map) {
    super(map);
    if(map==null){
      throw new IllegalArgumentException("map is null");
    }
    this.map = map;
  }

  public int nContiguous(int row, int column){
    int n = this.map.length;
    int m = this.map[0].length;
    if(row<0 || row>=n || column<0 || column>=m){
      throw new IllegalArgumentException("initial square is outside the map");
    }
    if(this.map[row][column]!='S'){
      return 0;
    }
    this.marked = new boolean[n][m];
    //dfs(row, column);
    bfs(row, column);
    int temp = this.count;
    this.count = 0;
    this.marked = new boolean[n][m];
    return temp;
  }

  private void dfs(int row, int col){
    this.marked[row][col]=true;
    count++;
    if(isValid(row, col-1)){
      dfs(row,col-1);
    }
    if(isValid(row, col+1)){
      dfs(row,col+1);
    }
    if(isValid(row-1, col-1)){
      dfs(row-1,col-1);
    }
    if(isValid(row-1, col)){
      dfs(row-1,col);
    }
    if(isValid(row-1, col+1)){
      dfs(row-1,col+1);
    }
    if(isValid(row+1, col-1)){
      dfs(row+1,col-1);
    }
    if(isValid(row+1, col)){
      dfs(row+1,col);
    }
    if(isValid(row+1, col+1)){
      dfs(row+1,col+1);
    }
  }

  private void bfs(int row, int col){
    //Sedgewick
    Queue<int[]> q = new LinkedList<>();
    q.offer(new int[]{row, col});
    count++;
    this.marked[row][col]=true;
    while(!q.isEmpty()){
      int[] v = q.poll();
      if(isValid(v[0], v[1]-1)){
        q.offer(new int[]{v[0], v[1]-1});
        this.marked[v[0]][v[1]-1]=true;
        count++;
      }
      if(isValid(v[0], v[1]+1)){
        q.offer(new int[]{v[0], v[1]+1});
        this.marked[v[0]][v[1]+1]=true;
        count++;
      }
      if(isValid(v[0]-1, v[1]-1)){
        q.offer(new int[]{v[0]-1, v[1]-1});
        this.marked[v[0]-1][v[1]-1]=true;
        count++;
      }
      if(isValid(v[0]-1, v[1])){
        q.offer(new int[]{v[0]-1, v[1]});
        this.marked[v[0]-1][v[1]]=true;
        count++;
      }
      if(isValid(v[0]-1, v[1]+1)){
        q.offer(new int[]{v[0]-1, v[1]+1});
        this.marked[v[0]-1][v[1]+1]=true;
        count++;
      }
      if(isValid(v[0]+1, v[1]-1)){
        q.offer(new int[]{v[0]+1, v[1]-1});
        this.marked[v[0]+1][v[1]-1]=true;
        count++;
      }
      if(isValid(v[0]+1, v[1])){
        q.offer(new int[]{v[0]+1, v[1]});
        this.marked[v[0]+1][v[1]]=true;
        count++;
      }
      if(isValid(v[0]+1, v[1]+1)){
        q.offer(new int[]{v[0]+1, v[1]+1});
        this.marked[v[0]+1][v[1]+1]=true;
        count++;
      }
    }
  }

  private boolean isValid(int row, int column){
    if(row<0 || row>=this.map.length || column<0 || column>=this.map[0].length || this.marked[row][column] || this.map[row][column]!='S'){
      return false;
    }
    return true;
  }

} // class QuestForOil

package edu.yu.introtoalgs;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.RecursiveAction;

//idea: merge into the ForkJoinSort instance, but sort using tx instance?

public class TxSortFJ extends TxSortFJBase {

  private TxBase[] txs;
  //static TxBase[] aux;

  class ForkJoinSort extends RecursiveAction {
    TxBase[] transactions;
    //TxBase[] aux;
    int low;
    int high;
    int threshold;

    ForkJoinSort(TxBase[] transactions, int low, int high, int threshold){
      //TxBase[] transactions,
      this.transactions = transactions;
      //this.aux = new TxBase[transactions.length];
      this.low = low;
      this.high = high;
      this.threshold = threshold;
    }

    @Override
    public void compute(){
      if((this.high-this.low)<=this.threshold){
        //this.sequentialSort(low, high);
        Arrays.sort(this.transactions, this.low, this.high);
      }
      else{
        int mid = (low+high)/2;
        ForkJoinSort left = new ForkJoinSort(this.transactions, low, mid, this.threshold);
        ForkJoinSort right = new ForkJoinSort(this.transactions, mid, high, this.threshold);
        left.fork();
        right.compute();
        left.join();
        this.merge(mid);
      }
      //return null;
    }

    private void sequentialSort(int low, int high){

      Arrays.sort(this.transactions, low, high);

      // TxBase[] temp = Arrays.copyOfRange(this.transactions, low, high);
      // Arrays.sort(temp);
      // for(int i=0; i<temp.length; i++){
      //   this.transactions[low+i]=temp[i];
      // }

    }

    private void merge(int mid){
      // int highNew = high-1;
      // int mid = (low+highNew)/2;
      // for(int i=low; i<=highNew; i++){
      //   TxSortFJ.aux[i]=this.transactions[i];
      // }

      // //Sedgewick
      // int i = low;
      // int j = mid+1;
      // for(int k = low; k<=highNew; k++){
      //   if(i>mid) this.transactions[k]=TxSortFJ.aux[j++];
      //   else if(j>highNew) this.transactions[k] = TxSortFJ.aux[i++];
      //   else if( ( ((Tx)TxSortFJ.aux[j]).compareTo( (Tx)TxSortFJ.aux[i]) ) <0) this.transactions[k]=TxSortFJ.aux[j++];
      //   else this.transactions[k]=TxSortFJ.aux[i++];
      // }

      TxBase[] temp = Arrays.copyOfRange(this.transactions, this.low, this.high);
      int i = this.low;
      int j = mid - this.low;
      int k = this.low;

      while (i < mid && j < high - low) { //  ( (Tx) (temp[i-low]) ).compareTo( (Tx) (temp[j]) );
          //if (temp[i - low] < temp[j]) {
          if( ( (Tx) (temp[i-low]) ).compareTo( (Tx) (temp[j]) ) < 0){
              this.transactions[k++] = temp[i++ - low];
          } else {
              this.transactions[k++] = temp[j++];
          }
      }
      while (i < mid) {
          this.transactions[k++] = temp[i++ - low];
      }
      while (j < high - low) {
          this.transactions[k++] = temp[j++];
      }
    }
  }//end of inner class

  public TxSortFJ(List<TxBase> transactions) {
    super(transactions);
    if(transactions==null){
      throw new IllegalArgumentException();
    }
    this.txs = transactions.toArray(new TxBase[0]);
    //this.aux = new TxBase[this.txs.length];
    //this.txs = new TxBase[transactions.size()];
    // Iterator<TxBase> iter = transactions.iterator();
    // int count = 0;
    // while(iter.hasNext()){
    //   if(iter.next()==null){
    //     throw new IllegalArgumentException();
    //   }
    //   this.txs[count++]=(Tx)iter.next();
    //   //this.txsCopy[count]=(Tx)iter.next();
    // }
  }

  /** Returns an array of transactions, sorted in ascending order of
   * TxBase.time() values: any instances with null TxBase.time() values precede
   * all other transaction instances in the sort results.
   *
   * @return the transaction instances passed to the constructor, returned as
   * an array, and sorted as specified above.  Students MAY ONLY use the
   * ForkJoin and their own code in their implementation.
   */
  @Override
  public TxBase[] sort(){
    int parallelism = Runtime.getRuntime().availableProcessors() * 1;
    int threshold = 12000;
    ForkJoinTask<Void> task = new ForkJoinSort(this.txs, 0, this.txs.length, threshold);
    ForkJoinPool fjPool = new ForkJoinPool(parallelism);
    fjPool.invoke(task);
    fjPool.shutdown();
    return this.txs;
  }


} // abstract base class
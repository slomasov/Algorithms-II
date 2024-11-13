package edu.yu.introtoalgs;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

public class Tx extends TxBase{

  private static final AtomicLong idGenerator = new AtomicLong(1);
  private Account receiver;
  private Account sender;
  private int amount;
  private long id;
  private LocalDateTime time;

  /** Constructor.
   *
   * @param sender non-null initiator of the transaction
   * @param receiver non-null recipient
   * @param amount positive-integer-valued amount transfered in the
   * transaction.
   */
  public Tx(Account sender, Account receiver, int amount) {
    super(sender, receiver, amount);
    if(sender==null || receiver==null || amount<=0){
      throw new IllegalArgumentException("can't create transaction: invalid inputs");
    }
    this.sender = sender;
    this.receiver = receiver;
    this.amount = amount;
    this.time = LocalDateTime.now();
    this.id = this.idGenerator.getAndIncrement();
  }

  @Override
  public Account receiver(){
    return this.receiver;
  }

  @Override
  public Account sender(){
    return this.sender;
  }

  @Override
  public int amount(){
    return this.amount;
  }

  @Override
  public long id(){
    return this.id;
  }

  @Override
  public LocalDateTime time(){
    return this.time;
  }

  @Override
  public void setTimeToNull(){
    this.time=null;
  }
  
  @Override
  public String toString() {
    return "Tx{" +
      "sender=" + sender() +
      ", receiver=" + receiver() +
      ", amount=" + amount() +
      ", id=" + id() +
      ", time=" + time() +
      '}';
  }

  @Override
  public int compareTo(TxBase other) {
    if(other==null){
      throw new NullPointerException();
    }
    if(this.time==null){
      if(other.time()==null){
        return 0;
      }
      return -1;
    }
    if(other.time()==null){
      return 1;
    }
    return this.time.compareTo(other.time());
  }

  @Override
  public boolean equals(Object obj){
    if(obj==null){
      return false;
    }
    if(this==obj){
      return true;
    }
    if(getClass() != obj.getClass()){
      return false;
    }
    Tx other = (Tx) obj;
    if(this.time==null){
      if(other.time()==null){
        return true;
      }
      return false;
    }
    if(other.time()==null){
      return false;
    }
    return other.time().equals(this.time);
  }
}//end of class
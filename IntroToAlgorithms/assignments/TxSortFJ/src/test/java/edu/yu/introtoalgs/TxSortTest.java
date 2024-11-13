package edu.yu.introtoalgs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import edu.yu.introtoalgs.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;
import org.testng.asserts.SoftAssert;

public class TxSortTest{

	private static final Logger logger = LogManager.getLogger(TxSortTest.class);
	Account acc1 = new Account();
	Account acc2 = new Account();

	public boolean isSorted(TxBase[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void creationTest(){
    	final int nAccounts = 2 ;
		final int nTxs = 5;
		final List<TxBase> txs = new ArrayList<>();
    }

	@Test
	public void profTest(){
		Random random = new Random();
		final SoftAssert softAssert = new SoftAssert () ;
		final int nAccounts = 2 ;
		final int nTxs = 5;
		final List<TxBase> txs = new ArrayList<>();
		final Account[] accounts = new Account [ nAccounts ] ;
		for (int i=0; i<nAccounts; i++) {
			accounts[i] = new Account();
		}
		logger.info("Created {} Accounts", nAccounts);
		for (int i=0; i<nTxs; i++) {
		// being silly here: no point in making this look more real
			final Account account1 = accounts[random.nextInt(0, nAccounts)];
			final Account account2 = accounts[random.nextInt(0, nAccounts)];
			txs.add(new Tx(account1 , account2 , 1) ) ;
		}
		assert(txs.size()>0);
		Collections.shuffle(txs) ;
		logger.info ("Created {} Txs", txs.size() ) ;
		//try {
			final TxSortFJBase txSortFJ = new TxSortFJ(txs) ;
			final TxBase[] fjTxs = txSortFJ.sort();
			final boolean isSorted =this.isSorted(fjTxs) ; 
			assert(isSorted);
			softAssert.assertTrue ( isSorted ,"*** Txs should have been (but are not) sorted") ;
		//}
		//catch(Exception e){
		//	final String msg = "Unexpected exception running test: "; logger . error (msg, e) ;
		//	softAssert.fail(msg+e. toString () ) ;
		//}
		//finally {
			softAssert.assertAll();
		//}

	}

	@Test
	public void invalidInputsTest(){
		assertThrows(IllegalArgumentException.class, () -> {
			Tx badAmount = new Tx(acc1, acc2, 0);
		});
		acc1 = null;
		assertThrows(IllegalArgumentException.class, () -> {
			Tx nullSender = new Tx(acc1, acc2, 1);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			Tx nullReceiver = new Tx(acc2, acc1, 1);
		});
	}

	@Test
	public void txCompleteTest(){
		int amount = 1;
		TxBase txTest1 = new Tx(acc1, acc2, amount);
		assert(txTest1.sender().equals(acc1));
		assert(txTest1.receiver().equals(acc2));
		assert(txTest1.amount()==amount);
		assert(txTest1.time()!=null);

		TxBase txTest2 = new Tx(acc2, acc1, ++amount);
		assert(txTest2.time()!=null);
		assert(txTest1.id()!=txTest2.id());

		assertFalse(txTest1.equals(txTest2));
		assert(txTest1.compareTo(txTest2)<0);
		txTest2.setTimeToNull();
		assert(txTest2.time()==null);
		assertFalse(txTest1.equals(txTest2));
		assert(txTest1.compareTo(txTest2)>0);
		assert(txTest2.compareTo(txTest1)<0);
		txTest1.setTimeToNull();
		assert(txTest1.time()==null);
		assert(txTest1.compareTo(txTest2)==0);
		assertTrue(txTest1.equals(txTest2));
	}

	@Test
	public void txSortCorrectnessTest(){
		int capacity = 100000;
		List<TxBase> transactions = new ArrayList<>(capacity);
		for(int i=0; i<capacity;i++){
			transactions.add(new Tx(new Account(), new Account(), 2));
		}
		Collections.shuffle(transactions);
		final TxSortFJBase txSortFJ = new TxSortFJ(transactions);
		//Arrays.sort(txS)
		//System.c
		TxBase[] fjTxs = txSortFJ.sort();
		assert(isSorted(fjTxs));
		Arrays.sort(fjTxs);
		assert(isSorted(fjTxs));
		/*
		sort with java library sort
		sort with my code
		for i = 0 to n:
			if javaSorted[i] == null, then mySorted[i]==null
			else javaSorted[i] == mySorted[i]
		*/
	}

	@Test
	public void txSortPerformanceTest(){
		int capacity = 9000000;
		List<TxBase> transactions = new ArrayList<>(capacity);
		for(int i=0; i<capacity;i++){
			transactions.add(new Tx(new Account(), new Account(), 2));
		}
		Collections.shuffle(transactions);
		TxBase[] txs = transactions.toArray(new TxBase[0]);
		for(int i=0; i<10; i++){
			txs[i].setTimeToNull();
		}
		//long sequentialStart = System.currentTimeMillis();
		Arrays.sort(txs);
		//long sequentialTime = System.currentTimeMillis()-sequentialStart;
		//logger.info(sequentialTime);
		//System.out.println(sequentialTime);
		assert(isSorted(txs));

		// TxBase[] txs1 = transactions.toArray(new TxBase[0]);
		// long parallelStartJava = System.currentTimeMillis();
		// Arrays.parallelSort(txs1);
		// long parallelTimeJava = System.currentTimeMillis()-parallelStartJava;
		// System.out.println(parallelTimeJava);
		// assert(isSorted(txs1));

		TxSortFJBase txSortFJ = null;
		//long totalTime = 0;
		//for(int i=0; i<1; i++){
			txSortFJ = new TxSortFJ(transactions);
			long parallelStart = System.currentTimeMillis();
			TxBase[] fjTxs = txSortFJ.sort();
			long parallelTime = System.currentTimeMillis()-parallelStart;
			//totalTime+=parallelTime;
			//logger.info(parallelTime);
			//System.out.println(parallelTime);
			assert(isSorted(fjTxs));
			//assert(parallelTime<sequentialTime);
		//}
		//double meanTime = ((double)totalTime)/30;
		//System.out.println(meanTime);
	}
}
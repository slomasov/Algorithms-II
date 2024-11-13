package edu.yu.introtoalgs;

import java.util.concurrent.*;
import java.util.Arrays;

public class BigOIt2 extends BigOIt2Base{

	public BigOIt2(){
	}

	/** Given the name of a class that implements the BigOMeasurable API, creates
	* and executes instances of the class, such that by measuring the resulting
	* performance, can return the "doubling ratio" for that algorithm's
	* performance.
	*
	* See extended discussion in Sedgewick, Chapter 1.4, on the topic of
	* doubling ratio experiments.
	*
	* @param bigOMeasurable name of the class for which we want to compute a
	* doubling ratio.  The client claims that the named class implements the
	* BigOMeasurable API, and can be constructed with a no-argument constructor.
	* This method is therefore able to (1) construct instances of the named
	* class, (2) invoke "setup(n)" for whatever values of "n" are desired, and
	* (3) then invoke "execute()" to measure the performance of a single
	* invocation of the algorithm.  The client is responsible for ensuring that
	* invocation of setup(n) produces a suitably populated (perhaps randomized)
	* set of state scaled as a function of n.
	* @param timeOutInMs number of milliseconds allowed for the computation.  If
	* the implementation has not computed an answer by this time, it should
	* return NaN.
	* @return the doubling ratio for the specified algorithm if one can be
	* calculated, NaN otherwise.
	* @throws IllegalArgumentException if bigOMeasurable parameter doesn't
	* fulfil the contract specified above or if some characteristic of the
	* algorithm is at odds with the doubling ratio assumptions.
	*/
	@Override
	public double doublingRatio(String bigOMeasurable, long timeOutInMs){
		/**
		* timer start 
		* while timer < timeOutInMS do that, else return Double.NaN
		* parallelize iterations using threads, executors and futures.
		* try the following, if any part of the API doesn't work then throw IAE
		* BigOMeasurable BlackBox = new bigOMeasurable()
		* for n = nMin to nMax:
		* 	BlackBox.setup(n)
		* 	t_initial
		* 	BlackBox.execute()
		* 	t_final
		* 	record time
		* end for
		* 
		* discard small values, calculate doubling ratios
		*/

		try{


			// Class tempClass = Class.forName(bigOMeasurable); //getting the class type of bigOMeasurable
			// Class<? extends BigOMeasurable> tempClassExtends = tempClass.asSubclass(BigOMeasurable.class); //converting so that it extends BigOMeasurable
			// BigOMeasurable blackBox = tempClassExtends.newInstance(); //creating a new instance

			double[] ratios = new double[16];
			
			//credit for the timer part - https://stackoverflow.com/questions/4044726/how-to-set-a-timer-in-java

			ExecutorService service = Executors.newSingleThreadExecutor();
	
    		Runnable r = new Runnable() {
        		@Override
        		public void run() {

        			try{

        			Class tempClass = Class.forName(bigOMeasurable); //getting the class type of bigOMeasurable
					Class<? extends BigOMeasurable> tempClassExtends = tempClass.asSubclass(BigOMeasurable.class); //converting so that it extends BigOMeasurable
					BigOMeasurable blackBox = tempClassExtends.newInstance(); //creating a new instance

					long t_start = 0;
					long t_end = 0;
					long t_elapsed;
					int count = 0;
					int n = 8;
					int tooBig = 0;

					blackBox.setup(n);
					t_start = System.nanoTime();
					blackBox.execute();
					ratios[0]=(double) (System.nanoTime()-t_start);
					count++;
					n*=2;

					while(count<ratios.length){
						blackBox.setup(n);
						t_start = System.nanoTime();
						blackBox.execute();
						t_end = System.nanoTime();
						t_elapsed = t_end-t_start;
						ratios[count-1]=(double)t_elapsed / ratios[count-1];
						ratios[count]=(double) t_elapsed;
						if(ratios[count-1]>5){
							tooBig++;
						}
						//System.out.println(t_elapsed);
						n*=2;
						count++;
						if(tooBig>3){
							break;
						}
					}
					//System.out.println();
					//for(int i=0; i<ratios.length-1; i++){
					//	System.out.println(ratios[i]);
					//}
					//System.out.println();

					}//end of try
					catch(OutOfMemoryError e){
						ratios[0]=0;
					}
					catch(Exception e){
						throw new IllegalArgumentException();
					}   	

		        }
    		};

    		Future<?> f = service.submit(r);
    		f.get(timeOutInMs, TimeUnit.MILLISECONDS);

    		//return ((double)ratios[ratios.length-1])/((double)ratios[ratios.length-1]);

    		if(ratios[0]==0){
    			return Double.NaN;
    		}

    		boolean zeros=false;

    		//inspect for 0's: if there are, and at least 2 elements > 12, then return 16; if at least 2 > 20 then return 32;
    		for (int i=0; i<15; i++){
    			if(ratios[i]==0){
    				zeros = true;
    				ratios[i-1]=-1;
    				break;
    			}
    		}

    		if(zeros){
    			int big22 = 0;
    			int big12 = 0;
    			for(int i=0; i<15; i++){
    				if(ratios[i]>22){
    					big22++;
    					big12++;
    				}
    				else if(ratios[i]>12){
    					big12++;
    				}
    			}
    			if(big22>1){
    				return 32;
    			}
    			if(big12>1){
    				return 16;
    			}
    			else{
    				return 8;
    			}
    		}

    		ratios[0]=-1;
    		ratios[1]=-1;
    		ratios[2]=-1;
    		ratios[3]=-1;
    		ratios[15]=-1;

    		Arrays.sort(ratios);

    		double median = ratios[10];
    		return Math.round(median);


		}
		catch(final TimeoutException e){
			return Double.NaN;
		}

		catch(Exception e){
			throw new IllegalArgumentException();
		} 

	}//end of doublingratio


}
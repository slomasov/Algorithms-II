package edu.yu.introtoalgs;

public class EQIQ extends EQIQBase{

	private boolean isEveryoneDumb = true;
	private int total;
	private double eqNumber;
	private double secondsDelta = (double) Integer.MIN_VALUE;
	private double[][] transformed;

	public EQIQ(int totalQuestions, double[] eqSuccessRate, double[] iqSuccessRate, int nepotismIndex){
		super(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
		int candidates = eqSuccessRate.length;
		this.total = totalQuestions;
		if(this.total<=1 || candidates!=iqSuccessRate.length || candidates<=1 || nepotismIndex<0 || nepotismIndex>=candidates){
			throw new IllegalArgumentException();
		}

		/*
		auxiliary 2D-array, where first element is 3600/IQ*total, second element is 3600*(1/IQ-1/EQ)
		Once the array is created, swap the 0's row with nepotism's row
		do while double loop:
			calculate arrays' intersection using the formula x = (a2-a1)/(b1-b2)
			record secondsDelta for this intersection point
			if it's larger than max, update seconds delta
		return
		*/

		if(eqSuccessRate[nepotismIndex]==0){
			if(iqSuccessRate[nepotismIndex]==0){
				return;
			}
			this.eqNumber = 0;
			this.secondsDelta = this.total* (3600/findMax(iqSuccessRate, nepotismIndex)-3600/iqSuccessRate[nepotismIndex]);
			return;
		}

		if(iqSuccessRate[nepotismIndex]==0){
			if(eqSuccessRate[nepotismIndex]==0){
				return;
			}
			this.eqNumber = this.total;
			this.secondsDelta = this.total* (3600/findMax(eqSuccessRate, nepotismIndex)-3600/eqSuccessRate[nepotismIndex]);
			return;
		}

		//create auxiliary array, populate it
		this.transformed = new double[candidates][2];
		for(int i=0; i<candidates; i++){
			this.transformed[i] = transformEqIq(eqSuccessRate[i], iqSuccessRate[i], nepotismIndex==i);
		}

		if(this.isEveryoneDumb){
			this.eqNumber = 0.5;
			this.secondsDelta = Double.POSITIVE_INFINITY;
			return;
		}

		//Swap nepotism candidate with 0's index
		if(nepotismIndex!=0){
			double[] temp = this.transformed[0];
			this.transformed[0] = this.transformed[nepotismIndex];
			this.transformed[nepotismIndex] = temp;
		}

		solveWithIntersections();

		//if(this.total<1001){
		//	solveWithBruteForce();
		//}

		//solveWithIntersections();
		
	}

	private void solveWithIntersections(){
		//calculate corners
		calculateSecondsDelta(0);
		calculateSecondsDelta(this.total);

		//loop!
		for(int i=1; i<this.transformed.length; i++){
			if(this.transformed[i]!=null){
				for(int j=i+1; j<this.transformed.length; j++){
					if(this.transformed[j]!=null){
						double x1 = calculateIntersection(this.transformed[i][0], this.transformed[i][1], this.transformed[j][0], this.transformed[j][1]);
						//System.out.println(x1);
						if(!(x1<=0||x1>=this.total)){
							calculateSecondsDelta(x1);
						}
					}
				}
			}
		}
	}

	private void solveWithBruteForce(){
		for(double i=0; i<=this.total; i=i+0.001){
			calculateSecondsDelta(i);
		}
	}

	private double calculateIntersection(double a1, double b1, double a2, double b2){
		if(b2==b1 || a2==a1){
			return -1;
		}
		return (a1-a2)/(b2-b1);
	}

	private void calculateSecondsDelta(double x1){
		double myTime = this.transformed[0][0]+x1*this.transformed[0][1];
		double minTime = (double) Integer.MAX_VALUE;
		for(int i=1; i<this.transformed.length; i++){
			if(this.transformed[i]!=null){
				double otherTime = this.transformed[i][0]+x1*this.transformed[i][1];
				if(otherTime<minTime){
					minTime=otherTime;
				}
			}
		}
		if(minTime<=myTime){
			return;
		}
		double tempSecondsDelta = minTime-myTime;
		if(tempSecondsDelta>this.secondsDelta){
			this.secondsDelta=tempSecondsDelta;
			this.eqNumber=x1;
		}
		return;
	}

	private double[] transformEqIq(double eq, double iq, boolean nepot){
		if(eq<0 || iq<0){
			throw new IllegalArgumentException();
		}
		if(eq==0 || iq==0){
			return null;
		}
		else{
			if(!nepot){
				isEveryoneDumb = false;
			}
			double a = 3600.0*this.total/iq;
			double b = 3600.0*(1/eq-1/iq);
			return new double[]{a,b};
		}
	}

	private double findMax(double[] arr, int nepotismIndex){
		double max = Integer.MIN_VALUE;
		for(int i=0; i<arr.length; i++){
			if(i!=nepotismIndex && arr[i]>max){
				max = arr[i];
			}
		}
		return max;
	}

	@Override
	public boolean canNepotismSucceed(){
		return this.secondsDelta>0;
	}

	@Override
	public double getNumberEQQuestions(){
		if(!canNepotismSucceed()){
			return -1;
		}
		return this.eqNumber;
	}

	@Override
	public double getNumberIQQuestions(){
		if(!canNepotismSucceed()){
			return -1;
		}
		return (double)this.total - this.eqNumber;
	}

	@Override
	public double getNumberOfSecondsSuccess(){
		if(!canNepotismSucceed()){
			return -1;
		}
		return this.secondsDelta;
	}

}
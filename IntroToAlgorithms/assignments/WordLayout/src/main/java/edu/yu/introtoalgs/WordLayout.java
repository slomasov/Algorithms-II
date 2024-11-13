package edu.yu.introtoalgs;

import static edu.yu.introtoalgs.WordLayoutBase.LocationBase;
import static edu.yu.introtoalgs.WordLayoutBase.Grid;
import java.util.*;

public class WordLayout extends WordLayoutBase{
	Map<String,List<LocationBase>> locationMap = new HashMap<>();
	Grid grid;
	int rows;
	int columns;

	class ByLength implements Comparator<String>{
		@Override
		public int compare(String o1, String o2){
			return o2.length() - o1.length();
		}
	}

	public WordLayout(final int nRows, final int nColumns, final List<String> words){
		
		super(nRows, nColumns, words);

		if(nRows<0 || nColumns<0){
			throw new IllegalArgumentException("grid's rows and columns cannot be negative");
		}

	    if(words==null || words.isEmpty()){
			throw new IllegalArgumentException("list of words is empty");
		}

		try{

		this.grid = new Grid(nRows, nColumns){
			@Override
			public int hashCode(){
				int temp = 0;
				for(int i=0; i<nRows; i++){
					for(int j=0; j<nColumns; j++){
						temp+=(int) this.grid[i][j];
					}
				}
				return temp;
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
				Grid other = (Grid) obj;
				return other.hashCode()==this.hashCode();
			}
		};

		this.rows = nRows;
		this.columns = nColumns;

		List<String> words_copy = new ArrayList<String>(words);
		
		words_copy.sort(new ByLength());

		boolean horizontal = false;
		if(words_copy.get(0).length()<=nColumns){
			horizontal=true;
		}
		else{
			if(words_copy.get(0).length()>nRows){
				throw new IllegalArgumentException("given words can't fit into given grid");
			}
		}

		int[] pointers = null;
		int[] last = null;
		if(horizontal){
			pointers = new int[nRows];
			last = new int[nRows];
			for(int i=0; i<nRows; i++){
				last[i]=nColumns;
			}
		}
		else{
			pointers = new int[nColumns];
			last = new int[nColumns];
			for(int i=0; i<nColumns; i++){
				last[i]=nRows;
			}
		}

		this.populateGrid(words_copy, horizontal, pointers, last);

		} //end of try

		catch(NullPointerException e){
			throw new IllegalArgumentException("list contains null words");
		}

	}

	private void populateGrid(List<String> words, boolean isHorizontal, int[] pointers, int[] last){
		if(isHorizontal){
			int currentRow = 0;
			for(String currentWord : words){
				while( (currentRow < this.rows-1) && pointers[currentRow]+currentWord.length() > last[currentRow]){
					currentRow++;
				}
				//System.out.println(currentRow);
				
				if(currentRow == this.rows - 1 && pointers[currentRow]+currentWord.length() > last[currentRow]){
					//System.out.println(currentWord);
					int spaces = 0;
					int lastCol = last[0];
					boolean lastUpdate = false;
					//this.putWord(currentWord, false, 0, 2);

					for(int i=0; i<this.rows; i++){
						if(lastUpdate){
							lastCol = last[i];
						}
						//System.out.println(currentWord);
						if(pointers[i]>=lastCol){
							spaces = 0;
							lastUpdate = true;
						}
						else{
							//System.out.println("+1 space");
							spaces++;
							//lastUpdate = false; i-currentWord.length()+1; lastCol
							if(spaces==currentWord.length()){
								this.putWord(currentWord, false, i-currentWord.length()+1, lastCol-1);
								for(int j=i-currentWord.length()+1; j<i+1; j++){
									last[j]--;
								}
								//spaces = 0;
								//System.out.println(currentWord);
								break;
							}
						}
					}
					if(spaces!=currentWord.length()){
						throw new IllegalArgumentException("given words can't fit into given grid");
					}
				}
				
				//System.out.println(currentWord + ", row: " + currentRow + ", column: " + pointers[currentRow]);
				else{
					//System.out.println(currentRow);
					this.putWord(currentWord, isHorizontal, currentRow, pointers[currentRow]);
					pointers[currentRow]+=currentWord.length();
					currentRow=0;
				}
			}
		}
		else{
			int currentColumn = 0;
			for(String currentWord : words){
				while( (currentColumn < this.columns-1) && pointers[currentColumn]+currentWord.length() > last[currentColumn]){
					currentColumn++;
				}
				//System.out.println(currentRow);
				
				if(currentColumn == this.columns - 1 && pointers[currentColumn]+currentWord.length() > last[currentColumn]){
					//System.out.println(currentWord);
					int spaces = 0;
					int lastCol = last[0];
					boolean lastUpdate = false;
					//this.putWord(currentWord, false, 0, 2);

					for(int i=0; i<this.columns; i++){
						if(lastUpdate){
							lastCol = last[i];
						}
						//System.out.println(currentWord);
						if(pointers[i]>=lastCol){
							spaces = 0;
							lastUpdate = true;
						}
						else{
							//System.out.println("+1 space");
							spaces++;
							//lastUpdate = false; lastCol-1; i-currentWord.length()+1;
							if(spaces==currentWord.length()){
								this.putWord(currentWord, true, lastCol-1, i-currentWord.length()+1);
								for(int j=i-currentWord.length()+1; j<i+1; j++){
									last[j]--;
								}
								//spaces = 0;
								//System.out.println(currentWord);
								break;
							}
						}
					}
					if(spaces!=currentWord.length()){
						throw new IllegalArgumentException("given words can't fit into given grid");
					}
				}
				
				//System.out.println(currentWord + ", row: " + currentRow + ", column: " + pointers[currentRow]);
				else{
					//System.out.println(currentColumn);
					this.putWord(currentWord, isHorizontal, pointers[currentColumn], currentColumn);
					pointers[currentColumn]+=currentWord.length();
					currentColumn=0;
				}
			}

		}
	}

	private void putWord(String word, boolean horizontal, int rowIndex, int colIndex){
		List<LocationBase> bases= new ArrayList<>(word.length());
		for(int i=0; i<word.length(); i++){
			this.grid.grid[rowIndex][colIndex]=word.charAt(i);
			LocationBase temp = new LocationBase(rowIndex, colIndex){
				@Override
				public int hashCode(){
					return 13*this.row + 31*this.column;
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
					LocationBase other = (LocationBase) obj;
					return (other.row==this.row && other.column==this.column);
				}
			};
			bases.add(temp);
			if(horizontal){
				colIndex++;
			}
			else{
				rowIndex++;
			}
		}
		this.locationMap.put(word, bases);
	}

	@Override
	public List<LocationBase> locations(final String word){
		List<LocationBase> temp = this.locationMap.get(word);
		if(temp==null){
			throw new IllegalArgumentException("supplied word is not in the grid");
		}
		else{
			return temp;
		}
	}

	@Override
	public Grid getGrid(){
		return this.grid;
	}
}
package edu.yu.introtoalgs;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import edu.yu.introtoalgs.WordLayout;
import edu.yu.introtoalgs.WordLayoutBase;
import static edu.yu.introtoalgs.WordLayoutBase.Grid;
import static edu.yu.introtoalgs.WordLayoutBase.LocationBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

public class WordPuzzleTest{

	private static final Logger logger = LogManager.getLogger(WordPuzzleTest.class);

	@Test
	public void IAETest(){
		assertThrows(IllegalArgumentException.class, () -> {
			WordLayoutBase testEmpty = new WordLayout(10, 10, new ArrayList<String>());
		});
		assertThrows(IllegalArgumentException.class, () -> {
			WordLayoutBase testNullList = new WordLayout(10, 10, null);
		});
		List<String> nullList = new ArrayList<>(2);
		nullList.add(null);
		nullList.add("hi");
		assertThrows(IllegalArgumentException.class, () -> {
			WordLayoutBase testNullWords = new WordLayout(10, 10, nullList);
		});
		nullList.add(0,"");
		assertThrows(IllegalArgumentException.class, () -> {
			WordLayoutBase testNullWords = new WordLayout(1, 1, nullList);
		});
	}

	@Test
	public void wideTest(){
		final int nrows = 2;
		final int ncolumns = 8;
		final List<String> words = List.of("ANN", "BELL", "CELL", "DOLL");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		for (String word: words) {
			final List<LocationBase> locations = layout.locations(word);
			//logger.info("Locations for word {}: {}", word, locations);
		}
		final Grid grid = layout.getGrid();
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void tallTest(){
		final int nrows = 8;
		final int ncolumns = 2;
		final List<String> words = List.of("ANN", "BELL", "CELL", "DOLL");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		for (String word: words) {
			final List<LocationBase> locations = layout.locations(word);
			//logger.info("Locations for word {}: {}", word, locations);
		}
		final Grid grid = layout.getGrid();
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void profTest(){
		final int nrows = 3;
		final int ncolumns = 3;
		final List<String> words = List.of("CAT", "DOG", "BOB");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		for (String word: words) {
			final List<LocationBase> locations = layout.locations(word);
			//logger.info("Locations for word {}: {}", word, locations);
		}
		final List<LocationBase> locationTest1 = layout.locations("CAT");
		final List<LocationBase> locationTest2 = layout.locations("CAT");
		assert(locationTest1.equals(locationTest2));
		final Grid grid1 = layout.getGrid();
		final Grid grid2 = layout.getGrid();
		assert(grid1.equals(grid2));
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void directionChangeTest(){
		final int nrows = 4;
		final int ncolumns = 3;
		final List<String> words = List.of("AA", "BB", "CC", "DD", "EE", "FF");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		final Grid grid = layout.getGrid();
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void directionChangeFromTallTest(){
		final int nrows = 6;
		final int ncolumns = 2;
		final List<String> words = List.of("AAAAA", "BBB", "CC", "BB");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		final Grid grid = layout.getGrid();
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void OOBtest(){
		final int nrows = 3;
		final int ncolumns = 5;
		final List<String> words = List.of("AAAA", "BBBB", "CCCC", "DDD");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		final Grid grid = layout.getGrid();
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void tallOOBtest(){
		final int nrows = 5;
		final int ncolumns = 3;
		final List<String> words = List.of("AAAA", "BBBB", "CCCC", "DDD");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		final Grid grid = layout.getGrid();
		//logger.info("The filled in grid: {}",grid);
	}

	@Test
	public void hugeWideTest(){
		final int nrows = 2;
		final int ncolumns = 7000;

		Random random = new Random();

		final List<String> words = new ArrayList<>(416);
		char[] start = new char[32];
		for(int i=0; i<start.length; i++){
			start[i]='A';
		}
		words.add(new String(start));
		for(int i=0; i<416; i++){
			start[i/31]= (char) ('B' + random.nextInt(24)) ;
			start[i/31 + 1] = (char) ('B' + random.nextInt(24) );
			words.add(new String(start));
		}
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		for (String word: words) {
			final List<LocationBase> locations = layout.locations(word);
			//logger.info("First location : {}", locations.get(0));
		}
	}
	
	@Test
	public void profTest3(){
		final int nrows=8;
		final int ncolumns=6;
		final List<String> words= List.of("DIAMENT", "LEFF", "WYMORE", "JEKYL", "HYDE", "ALGS", "MDM", "DBIMPL", "DS", "INTRO");
		WordLayoutBase layout = new WordLayout(nrows, ncolumns, words);
		final Grid grid = layout.getGrid();
		logger.info("The filled in grid: {}",grid);
	}
}
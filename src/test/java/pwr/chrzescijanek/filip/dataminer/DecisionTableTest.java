package pwr.chrzescijanek.filip.dataminer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DecisionTableTest {

	private List<List<Double>> data;
	private List<String> decisions;

	@Before
	public void setUp() throws Exception {
		data = Arrays.asList(
				Arrays.asList(   85.13, 93.0,    12.3),
				Arrays.asList(  110.1,  31.3,    14.0),
				Arrays.asList(  120.4,  55.4,    13.0),
				Arrays.asList(  100.0,  40.0,    30.1),
				Arrays.asList(   79.1,  91.5,   100.1),
				Arrays.asList(  140.4,  78.1,    50.5),
				Arrays.asList(   50.1,  54.4,   180.0),
				Arrays.asList(   99.9,  20.1,   130.5),
				Arrays.asList(  100.1,  50.8,    45.1),
				Arrays.asList(   84.0,  39.4,    88.3)
		);
		decisions = Arrays.asList("1", "0", "2", "1", "3", "3", "2", "1", "1", "0");
	}

	@Test
	public void create() throws Exception {
		final DecisionTable decisionTable = new DecisionTable(data, decisions);
		decisionTable.subjects.stream().map(s -> s.attributes).forEach(System.out::println);
	}

}
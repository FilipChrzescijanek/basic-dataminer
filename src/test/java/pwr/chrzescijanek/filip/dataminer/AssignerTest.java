package pwr.chrzescijanek.filip.dataminer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class AssignerTest {

	private List<List<Double>> data;
	private List<List<Double>> testData;
	private List<String> decisions;

	private static final double EPSILON = 1.0;
	
	@Before
	public void setUp() throws Exception {
		data = Arrays.asList(
				Arrays.asList(  4.3,    921.6,  4.0,    20.7,   1799.0),
				Arrays.asList(  4.95,   2073.6, 4.0,    8.0,    1549.0),
				Arrays.asList(  5.5,    921.6,  4.0,    13.0,   1399.0),
				Arrays.asList(  4.5,    983.0,  2.0,    8.7,    1349.0),
				Arrays.asList(  4.6,    921.6,  2.0,    8.0,    1099.0),
				Arrays.asList(  3.1,    518.4,  2.0,    5.0,    999.0),
				Arrays.asList(  5.0,    921.6,  4.0,    13.0,   899.0),
				Arrays.asList(  4.3,    409.9,  2.0,    8.0,    749.0),
				Arrays.asList(  3.0,    76.8,   1.0,    5.0,    383.0),
				Arrays.asList(  2.4,    76.8,   1.0,    1.3,    208.0)
		);
//		data = Arrays.asList(
//				Arrays.asList(  5.5,    921.6,  4.0,    13.0,   1399.0),
//				Arrays.asList(  4.7,    921.6,  4.0,    8.0,    1399.0),
//				Arrays.asList(  4.5,    983.0,  2.0,    8.7,    1349.0),
//				Arrays.asList(  4.6,    921.6,  2.0,    8.0,    1099.0),
//				Arrays.asList(  3.1,    518.4,  2.0,    5.0,    999.0),
//				Arrays.asList(  4.2,    983.0,  2.0,    8.0,    949.0),
//				Arrays.asList(  5.0,    921.6,  4.0,    13.0,   899.0),
//				Arrays.asList(  4.3,    409.9,  2.0,    8.0,    749.0),
//				Arrays.asList(  3.0,    76.8,   1.0,    5.0,    383.0),
//				Arrays.asList(  2.4,    76.8,   1.0,    1.3,    208.0)
//		);
		testData = Arrays.asList(
				Arrays.asList(  3.5,    614.4,  2.0,    8.0,    1599.0),
				Arrays.asList(  5.2,    2073.6, 4.0,    13.0,   1549.0),
				Arrays.asList(  5.5,    921.6,  6.0,    8.0,    1499.0),
				Arrays.asList(  4.7,    921.6,  4.0,    8.0,    1399.0),
				Arrays.asList(  4.2,    983.0,  2.0,    8.0,    949.0)
		);
		decisions = Arrays.asList("2", "1", "0", "2", "3", "1",  "0", "1", "3", "2");
//		decisions = Arrays.asList("0", "0", "2", "3", "1", "1", "0", "1", "3", "2");
	}

	@Test
	public void assignDecision() throws Exception {
		final DecisionTable table = new Reductor().reduce(
				new ConflictResolver().resolve(
						new DecisionTable(deepCopy(data), decisions)));
		final Assigner assigner = new Assigner(table, new RuleCreator().makeRules(table, new Implicator().createImplications(table)));
		final List<List<Double>> deepCopy = deepCopy(data);
		for (int i = 0; i < deepCopy.size(); i++) {
			final Subject subject = table.createSubject(deepCopy.get(i));
			assigner.assignDecision(subject);
			System.out.print(String.format("%.2f ~ %s; ", subject.value, subject.decision));
			if (!table.indices.keySet().contains(i))
				assertEquals(Double.parseDouble(decisions.get(i)),
				             Double.parseDouble(subject.decision),
				             EPSILON);
			else
				assertEquals(Double.parseDouble(table.indices.get(i)),
				             Double.parseDouble(subject.decision),
				             EPSILON);
		}
		System.out.println();
		final List<List<Double>> test = deepCopy(testData);
		for (int i = 0; i < test.size(); i++) {
			final Subject subject = table.createSubject(test.get(i));
			assigner.assignDecision(subject);
			// "2","1","0","0","1"
			System.out.print(String.format("%.2f ~ %s; ", subject.value, subject.decision));
		}
	}

	private <E> List<List<E>> deepCopy(final List<List<E>> list) {
		return list
				.stream()
				.map(ArrayList::new)
				.collect(Collectors.toList());
	}

}
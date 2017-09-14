package pwr.chrzescijanek.filip.dataminer;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class RuleEvaluatorTest {

	private static final double EPSILON = 0.0000000001;

	private List<List<Double>> data;
	private List<String> decisions;
	@Before
	public void setUp() throws Exception {
		data = new ArrayList<>(Arrays.asList(
				new ArrayList<>(Arrays.asList(5.5, 921.6, 4.0, 13.0, 1399.0)),
				new ArrayList<>(Arrays.asList(  4.7,    921.6,  4.0,    8.0,    1399.0)),
				new ArrayList<>(Arrays.asList(  4.5,    983.0,  2.0,    8.7,    1349.0)),
				new ArrayList<>(Arrays.asList(  4.6,    921.6,  2.0,    8.0,    1099.0)),
				new ArrayList<>(Arrays.asList(  3.1,    518.4,  2.0,    5.0,    999.0)),
				new ArrayList<>(Arrays.asList(  4.2,    983.0,  2.0,    8.0,    949.0)),
				new ArrayList<>(Arrays.asList(  5.0,    921.6,  4.0,    13.0,   899.0)),
				new ArrayList<>(Arrays.asList(  4.3,    409.9,  2.0,    8.0,    749.0)),
				new ArrayList<>(Arrays.asList(  3.0,    76.8,   1.0,    5.0,    383.0)),
				new ArrayList<>(Arrays.asList(  2.4,    76.8,   1.0,    1.3,    208.0))
		));
		decisions = Arrays.asList("0", "0", "2", "3", "1", "1", "0", "1", "3", "2");
	}

	@Test
	public void evaluate() throws Exception {
		final DecisionTable table = new ConflictResolver().resolve(new Reductor().reduce(new DecisionTable(data, decisions)));
		final Subject subject = table.createSubject(new ArrayList<>(Arrays.asList(  2.4,    510.6,   1.0,    4.0,   590.0)));
		final List<Pair<String, Expression<String>>> rules = new RuleCreator().makeRules(table, new Implicator().createImplications(table));
		final List<Pair<Double, Double>> pairs = rules
				.stream()
				.map(r -> new Pair<>(Double.parseDouble(r.getKey()),
				                     new RuleEvaluatorFactory(table.stats).getEvaluator(r.getValue()).evaluate(subject)))
				.collect(Collectors.toList());
		pairs.forEach(System.out::println);
		//final String decision = pairs.stream().sorted((r1, r2) -> r2.getValue().compareTo(r1.getValue())).findFirst().get().getKey();
		//assertResults(pairs, decision);
	}

	private void assertResults(final List<Pair<String, Double>> pairs, final String decision) {
		//assertArrayEquals(pairs.stream().mapToDouble(p -> p.getValue()).toArray(), probabilities, EPSILON);
		//assertEquals(this.decision, decision);
	}
}
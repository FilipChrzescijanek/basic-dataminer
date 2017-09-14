package pwr.chrzescijanek.filip.dataminer;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ImplicatorTest {

	private List<Subject> before;
	private List<Expression<String>> expressions;

	@Before
	public void setUp() throws Exception {
		final Subject x1 = new Subject("x1", Arrays.asList("1", "0", "1"), "0");
		final Subject x2 = new Subject("x2", Arrays.asList("0", "0", "0"), "1");
		final Subject x3 = new Subject("x3", Arrays.asList("2", "0", "1"), "0");
		final Subject x4 = new Subject("x4", Arrays.asList("0", "0", "1"), "2");
		final Subject x5 = new Subject("x5", Arrays.asList("1", "1", "1"), "0");

		before = new ArrayList<>(Arrays.asList(x1, x2, x3, x4, x5));

		expressions = new ArrayList<>();
		expressions.add(Variable.of("f<a, 1>"));
		expressions.add(Variable.of("f<c, 0>"));
		expressions.add(Variable.of("f<a, 2>"));
		expressions.add(And.of(Variable.of("f<a, 0>"), Variable.of("f<c, 1>")));
		expressions.add(Or.of(Variable.of("f<a, 1>"), Variable.of("f<b, 1>")));
	}
	
	@Test
	public void createImplications() throws Exception {
		final DecisionTable table = new DecisionTable(before);
		final List<Expression<String>> implications = new Implicator().createImplications(table);
		assertExpressionsEquals(expressions, implications);
	}

	private void assertExpressionsEquals(final List<Expression<String>> expected, final List<Expression<String>> actual) {
		for (int i = 0; i < expected.size(); i++)
			assertEquals(expected.get(i), actual.get(i));
	}
}
package pwr.chrzescijanek.filip.dataminer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ReductorTest {

	private List<Subject> before;
	private List<Subject> after;
	
	@Before
	public void setUp() throws Exception {
		final Subject x1 = new Subject("x1", Arrays.asList("a", "d", "e"), "d1");
		final Subject x2 = new Subject("x2", Arrays.asList("b", "c", "e"), "d1");
		final Subject x3 = new Subject("x3", Arrays.asList("b", "d", "f"), "d2");
		final Subject x4 = new Subject("x4", Arrays.asList("a", "d", "f"), "d1");
		final Subject x6 = new Subject("x6", Arrays.asList("a", "d", "g"), "d1");

		before = new ArrayList<>(Arrays.asList(x1, x2, x3, x4, x6));

		final Subject sx1 = new Subject("x1", Arrays.asList("a", "e"), "d1");
		final Subject sx2 = new Subject("x2", Arrays.asList("b", "e"), "d1");
		final Subject sx3 = new Subject("x3", Arrays.asList("b", "f"), "d2");
		final Subject sx4 = new Subject("x4", Arrays.asList("a", "f"), "d1");
		final Subject sx6 = new Subject("x6", Arrays.asList("a", "g"), "d1");

		after = new ArrayList<>(Arrays.asList(sx1, sx2, sx3, sx4, sx6));
	}

	@Test
	public void reduce() throws Exception {
		final DecisionTable table = new DecisionTable(before);
		new Reductor().reduce(table);
		assertSubjectsEquals(after, table.subjects);
	}

	private void assertSubjectsEquals(final List<Subject> expected, final List<Subject> subjects) {
		for (int i = 0; i < expected.size(); i++)
			assertEquals(expected.get(i).attributes, subjects.get(i).attributes);
	}

}
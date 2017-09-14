package pwr.chrzescijanek.filip.dataminer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ConflictResolverTest {

	private List<Subject> before;
	private List<Subject> after;

	@Before
	public void setUp() {
		final Subject x1 = new Subject("x1", Arrays.asList("a", "d", "e"), "d1");
		final Subject x2 = new Subject("x2", Arrays.asList("b", "c", "e"), "d1");
		final Subject x3 = new Subject("x3", Arrays.asList("b", "d", "f"), "d2");
		final Subject x4 = new Subject("x4", Arrays.asList("a", "d", "f"), "d1");
		final Subject x5 = new Subject("x5", Arrays.asList("b", "c", "e"), "d2");
		final Subject x6 = new Subject("x6", Arrays.asList("a", "d", "g"), "d1");

		before = new ArrayList<>(Arrays.asList(x1, x2, x3, x4, x5, x6));
		after = new ArrayList<>(Arrays.asList(x1, x2, x3, x4, x6));
	}

	@Test
	public void resolve() throws Exception {
		final DecisionTable table = new DecisionTable(before);
		new ConflictResolver().resolve(table);
		assertEquals(after, table.subjects);
	}

}
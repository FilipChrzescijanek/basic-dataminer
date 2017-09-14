package pwr.chrzescijanek.filip.dataminer;

import com.bpodgursky.jbool_expressions.And;
import com.bpodgursky.jbool_expressions.ExprUtil;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.Variable;
import com.bpodgursky.jbool_expressions.rules.RuleSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Implicator {

	public List<Expression<String>> createImplications(final DecisionTable table) {
		final List<Integer>[][] indicesTable = new List[table.subjects.size()][table.subjects.size()];
		for (int i = 0; i < table.subjects.size(); i++) {
			for (int j = i + 1; j < table.subjects.size(); j++) {
				final Subject first = table.subjects.get(i);
				final Subject second = table.subjects.get(j);
				final List<Integer> attributes = new ArrayList<>();
				for (int k = 0; k < first.attributes.size(); k++ )
					if (!first.attributes.get(k).equals(second.attributes.get(k)))
						attributes.add(k);
				indicesTable[i][j] = attributes;
				indicesTable[j][i] = attributes;
			}
		}

		final List<List<List<Integer>>> matrices = new ArrayList<>();
		for (int i = 0; i < table.subjects.size(); i++) {
			final List<List<Integer>> matrix = new ArrayList<>();
			final Subject current = table.subjects.get(i);
			for (int j = 0; j < table.subjects.size(); j++) {
				final Subject s = table.subjects.get(j);
				if (!s.decision.equals(current.decision)) {
					matrix.add(indicesTable[i][j]);
				}
			}
			matrices.add(matrix);
		}

		int counter = 0;
		final List<Expression<String>> expressions = new ArrayList<>();
		for (final List<List<Integer>> list : matrices) {
			final int finalCounter = counter;
			final List<Expression<String>> collect = list.stream().filter(l -> !l.isEmpty()).map(attributes ->Or.of(
					attributes.stream().map(i -> Variable.of(
							"f<" + Character.toString((char) (i.intValue() + 'a')) + ", "
							+ table.subjects.get(finalCounter).attributes.get(i) + ">")).collect(Collectors.toList())))
			                                     .collect(Collectors.toList());
			expressions.add(And.of(collect));
			counter++;
		}

		return expressions.stream().map(RuleSet::simplify).collect(Collectors.toList());
	}
}

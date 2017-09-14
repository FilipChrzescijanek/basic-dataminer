package pwr.chrzescijanek.filip.dataminer;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Or;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RuleCreator {

	public  List<Pair<String, Expression<String>>> makeRules(final DecisionTable table, final List<Expression<String>> implications) {
		final List<Pair<String, Expression<String>>> rules = new ArrayList<>();
		final Map<String, List<Subject>> byDecisions = table.subjects.stream().collect(Collectors.groupingBy(s -> s.decision));
		byDecisions.forEach((k, v) -> {
			rules.add(new Pair<>(k, RuleSet.simplify(Or.of(v.stream()
			                                       .map(s -> implications.get(table.subjects.indexOf(s)))
			                                       .collect(Collectors.toList())))));
		});
		return rules;
	}
}

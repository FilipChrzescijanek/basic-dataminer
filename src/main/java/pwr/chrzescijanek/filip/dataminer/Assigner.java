package pwr.chrzescijanek.filip.dataminer;

import com.bpodgursky.jbool_expressions.Expression;
import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class Assigner {

	private final DecisionTable table;
	private final List<Pair<String, Expression<String>>> rules;

	public Assigner(final DecisionTable table, final List<Pair<String, Expression<String>>> rules) {
		this.table = table;
		this.rules = rules;
	}

	public void assignDecision(final Subject subject) {
		final List<Pair<Double, Double>> pairs = rules
				.stream()
				.map(r -> new Pair<>(Double.parseDouble(r.getKey()),
				                     new RuleEvaluatorFactory(table.stats).getEvaluator(r.getValue()).evaluate(subject)))
				.collect(Collectors.toList());
		final double sum = pairs.stream()
		                        .mapToDouble(Pair::getValue).sum();
		subject.value = pairs.stream()
		                      .mapToDouble(pair -> pair.getKey() * pair.getValue()).sum() / sum;
		subject.decision = "" + Math.round(subject.value);
	}
}

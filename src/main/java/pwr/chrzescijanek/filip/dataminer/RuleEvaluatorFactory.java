package pwr.chrzescijanek.filip.dataminer;

import com.bpodgursky.jbool_expressions.Expression;

import java.util.List;

public class RuleEvaluatorFactory {

	private final List<Stats> statistics;

	public RuleEvaluatorFactory(final List<Stats> statistics) {this.statistics = statistics;}

	public RuleEvaluator getEvaluator(final Expression<String> rule) {
		return new RuleEvaluator(statistics, rule);
	}
}

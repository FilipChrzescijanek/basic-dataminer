package pwr.chrzescijanek.filip.dataminer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bpodgursky.jbool_expressions.Expression;
import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.fathzer.soft.javaluator.BracketPair;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;

public class RuleEvaluator extends AbstractEvaluator<String> {

	private final static Operator AND = new Operator("&", 2, Operator.Associativity.LEFT, 2);
	private final static Operator OR = new Operator("|", 2, Operator.Associativity.LEFT, 1);

	private static final Parameters PARAMETERS;

	static {
		PARAMETERS = new Parameters();
		PARAMETERS.add(AND);
		PARAMETERS.add(OR);
		PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
	}

	private final List<Stats> statistics;
	private final Expression<String> rule;
	private Subject currentSubject;

	RuleEvaluator(final List<Stats> statistics, final Expression<String> rule) {
		super(PARAMETERS);
		this.statistics = statistics;
		this.rule = rule;
	}

	public double evaluate(final Subject subject) {
		currentSubject = subject;
		return Double.parseDouble(this.evaluate(rule.toString(), new ArrayList<>()));
	}

	@Override
	protected String toValue(final String literal, final Object evaluationContext) {
		return "" + getValue(literal);
	}

	private double getValue(final String literal) {
		if (literal.contains("f")) {
			final String attribute = literal.substring(literal.indexOf('<') + 1, literal.indexOf(',')).trim();
			final int descriptorFunction = Integer.parseInt(literal.substring(literal.indexOf(',') + 1, literal.indexOf('>')).trim());
			final int index = attribute.charAt(0) - 'a';
			final Stats stats = statistics.get(index);
			final double attrValue = currentSubject.attributeValues.get(index);
			return EvaluatorUtils.getResultForFunction(descriptorFunction, attrValue, stats);
		} else {
			return Double.parseDouble(literal);
		}
	}

	@Override
	protected String evaluate(final Operator operator, final Iterator<String> operands,
	                          final Object evaluationContext) {
		final List<String> tree = (List<String>) evaluationContext;
		final String o1 = operands.next();
		final String o2 = operands.next();
		final Double result;
		if (operator == OR) {
			result = Math.max(getValue(o1), getValue(o2));
		} else if (operator == AND) {
			result = Math.min(getValue(o1),getValue(o2));
		} else {
			throw new IllegalArgumentException();
		}
		final String eval = "" + result;
		tree.add(eval);
		return eval;
	}
}

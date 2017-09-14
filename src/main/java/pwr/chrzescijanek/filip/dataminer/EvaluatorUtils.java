package pwr.chrzescijanek.filip.dataminer;

public class EvaluatorUtils {

	public static double getResultForFunction(final int index, final double value, final Stats stats) {
		switch(index) {
			case 0: return value > stats.average ? 0.0 : 1 - pdf(stats, value);
			case 1: return pdf(stats, value);
			case 2: return value < stats.average ? 0.0 : 1 - pdf(stats, value);
			default: throw new IllegalArgumentException("Wrong decision! " + index);
		}
	}

	public static double pdf(final Stats stats, final double value) {
		return Math.exp(-(Math.pow(value - stats.average, 2)) / (2 * Math.pow(stats.stdDeviation, 2)));
	}

}

package pwr.chrzescijanek.filip.dataminer;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DecisionTable {

	public List<List<Double>> data = Collections.emptyList();
	public final List<Subject> subjects;
	public List<Stats> stats = Collections.emptyList();
	public List<Integer> meaningfulIndices = Collections.emptyList();
	public Map<Integer, String> indices =  Collections.emptyMap();

	public DecisionTable(final List<List<Double>> data, final List<String> decisions) {
		checkData(data, decisions);
		this.data = data;
		updateAttributeStats();
		subjects = getSubjects(data, decisions);
	}

	private void checkData(final List<List<Double>> data, final List<String> decisions) {
		if (data.isEmpty())
			throw new IllegalArgumentException("There has to be at least one data row!");
		if (!data.stream().allMatch(row -> row.size() == data.get(0).size()))
			throw new IllegalArgumentException("All data rows must have the same number of columns!");
		if (data.size() != decisions.size())
			throw new IllegalArgumentException("Data rows have to have corresponding decisions!");
	}

	public DecisionTable(final List<Subject> subjects) {
		checkSubjects(subjects);
		this.subjects = subjects;
		data = new ArrayList<>();
		subjects.forEach(s -> data.add(new ArrayList<>()));
	}

	public void updateAttributeStats() {
		stats = new ArrayList<>();
		if (!data.isEmpty()) {
			for (int i = 0; i < data.get(0).size(); i++) {
				final int index = i;
				final double average = data.stream()
				                           .mapToDouble(row -> row.get(index)).sum() / data.size();
				final double stdDeviation =
						Math.sqrt(data.stream()
						              .mapToDouble(row -> Math.pow(row.get(index) - average, 2)).sum() / (data.size() - 1));
				stats.add(new Stats(average, stdDeviation));
			}
		}
	}

	private void checkSubjects(final List<Subject> subjects) {
		if (subjects.isEmpty())
			throw new IllegalArgumentException("There has to be at least one subject!");
		if (!subjects.stream().allMatch(s -> s.attributes.size() == subjects.get(0).attributes.size()))
			throw new IllegalArgumentException("All subjects have to have the same number of attributes!");
	}

	public Subject createSubject(final List<Double> attributeValues) {
		removeAttributes(attributeValues);
		final List<String> attributes = assignAttributes(attributeValues);
		final Subject subject = new Subject("x" + subjects.size(), attributes);
		subject.attributeValues = attributeValues;
		return subject;
	}

	private void removeAttributes(final List<Double> attributeValues) {
		IntStream.range(0, attributeValues.size())
		         .filter(i -> !meaningfulIndices.contains(i))
		         .mapToObj(Integer::valueOf)
		         .sorted(Comparator.reverseOrder())
		         .mapToInt(Integer::intValue)
		         .forEach(attributeValues::remove);
	}

	private List<String> assignAttributes(final List<Double> attributeValues) {
		final List<String> attributes = new ArrayList<>();
		for (int j = 0; j < attributeValues.size(); j++) {
			final int finalJ = j;
			final List<Pair<Integer, Double>> pairs =
					IntStream.range(0, 3)
					         .mapToObj(n -> new Pair<>(n, EvaluatorUtils
							         .getResultForFunction(n, attributeValues.get(finalJ), stats.get(finalJ))))
					         .collect(Collectors.toList());
			attributes.add("" + pairs.stream().max((p1, p2) -> p1.getValue().compareTo(p2.getValue())).get().getKey());
		}
		return attributes;
	}

	private List<Subject> getSubjects(final List<List<Double>> data, final List<String> decisions) {
		final List<Subject> subjects = new ArrayList<>();
		for (int i = 0; i < data.size(); i++) {
			final List<String> attributes = new ArrayList<>();
			for (int j = 0; j < data.get(i).size(); j++) {
				final int finalI = i;
				final int finalJ = j;
				final List<Pair<Integer, Double>> pairs =
						IntStream.range(0, 3)
						         .mapToObj(n -> new Pair<>(n, EvaluatorUtils
								         .getResultForFunction(n, data.get(finalI).get(finalJ), stats.get(finalJ))))
						.collect(Collectors.toList());
				attributes.add("" + pairs.stream().max((p1, p2) -> p1.getValue().compareTo(p2.getValue())).get().getKey());
			}
			subjects.add(new Subject("x" + subjects.size(), attributes, decisions.get(i)));
		}
		return subjects;
	}
}

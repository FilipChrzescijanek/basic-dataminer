package pwr.chrzescijanek.filip.dataminer;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConflictResolver {

	public DecisionTable resolve(final DecisionTable table) {
		final Map<List<String>, List<Subject>> conflicts = table.subjects.stream().filter(
				s1 -> table.subjects.stream().anyMatch(
						s2 -> s1.attributes.equals(s2.attributes) && !s1.decision.equals(s2.decision)))
		                                                               .collect(Collectors.groupingBy(s -> s.attributes));
		final Map<Integer, String> indices = new HashMap<>();
		conflicts.forEach((k, v) -> {
			final List<Subject> subjectsOfConflict = table.subjects.stream().filter(s1 -> v.stream().anyMatch(s2 -> s1.decision.equals(s2.decision))).collect(Collectors.toList());
			final Map<List<String>, List<Subject>> byAttributes = subjectsOfConflict.stream().collect(Collectors.groupingBy(s -> s.attributes));
			final Map<String, List<Subject>> byDecision = subjectsOfConflict.stream().collect(Collectors.groupingBy(s -> s.decision));
			final String key = byDecision.entrySet().stream().map(
					e -> new Pair<>(e.getKey(), byAttributes.values().stream().filter(l -> e.getValue().containsAll(l)).count()))
			                             .max((p1, p2) -> p1.getValue().compareTo(p2.getValue())).get().getKey();
			v.stream()
			 .filter(s -> !s.decision.equals(key))
			 .map(table.subjects::indexOf)
			 .collect(Collectors.toList())
			 .forEach(i -> indices.put(i, key));
		});
		final Set<Integer> toRemove = indices.keySet();
		IntStream.range(0, table.subjects.size())
		         .filter(toRemove::contains)
		         .mapToObj(Integer::valueOf)
		         .sorted(Comparator.reverseOrder())
		         .mapToInt(Integer::intValue)
		         .forEach(i -> {
			         table.data.remove(i);
			         table.subjects.remove(i);
		         });
		table.indices = indices;
//		table.updateAttributeStats();

		return table;
	}
}

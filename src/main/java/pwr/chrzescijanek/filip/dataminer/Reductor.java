package pwr.chrzescijanek.filip.dataminer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Reductor {

	public DecisionTable reduce(final DecisionTable table) {
		List<List<Integer>> differences = new ArrayList<>();
		final List<Integer> indices = new ArrayList<>();
		for (int i = 0; i < table.subjects.size(); i++) {
			for (int j = i + 1; j < table.subjects.size(); j++) {
				final Subject first = table.subjects.get(i);
				final Subject second = table.subjects.get(j);
				final List<Integer> attributes = new ArrayList<>();
				for (int k = 0; k < first.attributes.size(); k++ )
					if (!first.attributes.get(k).equals(second.attributes.get(k)))
						attributes.add(k);
				if (!attributes.isEmpty())
					differences.add(attributes);
			}
		}
		while (!differences.isEmpty()) {
			final Integer index = differences.stream().flatMap(Collection::stream).collect(Collectors.groupingBy(Integer::intValue))
			                                 .entrySet().stream().max((e1, e2) -> Integer.compare(e1.getValue().size(), e2.getValue().size()))
			                                 .map(Map.Entry::getKey).get();
			indices.add(index);
			differences = differences.stream().filter(l -> !l.contains(index)).collect(Collectors.toList());
		}
		table.subjects.forEach(s ->
			IntStream.range(0, s.attributes.size())
			         .filter(i -> !indices.contains(i))
			         .mapToObj(Integer::valueOf)
			         .sorted(Comparator.reverseOrder())
			         .mapToInt(Integer::intValue)
			         .forEach(s.attributes::remove)
		);
		table.data.forEach(l -> IntStream.range(0, l.size())
		                                 .filter(i -> !indices.contains(i))
		                                 .mapToObj(Integer::valueOf)
		                                 .sorted(Comparator.reverseOrder())
		                                 .mapToInt(Integer::intValue)
		                                 .forEach(l::remove)
		);
		IntStream.range(0, table.stats.size())
		                                 .filter(i -> !indices.contains(i))
		                                 .mapToObj(Integer::valueOf)
		                                 .sorted(Comparator.reverseOrder())
		                                 .mapToInt(Integer::intValue)
		                                 .forEach(table.stats::remove);
//		table.updateAttributeStats();
		table.meaningfulIndices = indices;
		return table;
	}
}

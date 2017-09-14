package pwr.chrzescijanek.filip.dataminer;

import java.util.ArrayList;
import java.util.List;

public class Subject {

	public final String label;
	public final List<String> attributes;
	public List<Double> attributeValues;
	public String decision;
	public double value;

	public Subject(final String label, final List<String> attributes) {
		this.label = label;
		this.attributes = new ArrayList<>(attributes);
	}

	public Subject(final String label, final List<String> attributes, final String decision) {
		this.label = label;
		this.attributes = new ArrayList<>(attributes);
		this.decision = decision;
	}

}

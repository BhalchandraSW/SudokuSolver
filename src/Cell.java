import java.util.ArrayList;
import java.util.List;

public class Cell {

	private int value;
	private List<Integer> probabilities = new ArrayList<Integer>();

	public Cell() {
		for (int i = 1; i <= 9; i++) {
			probabilities.add(i);
		}
	}

	public int getValue() {
		return value;
	}

	public boolean setValue(int value) {
		if (value != 0 && value != this.value) {
			this.value = value;
			this.probabilities.clear();
			return true;
		}
		return false;
	}

	public List<Integer> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(List<Integer> probabilities) {
		this.probabilities = probabilities;
	}

	public String toString() {
		return Integer.toString(value);
	}
}

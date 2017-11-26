package model;
import java.util.List;

public class SudokuGrid {

	private Cell[][] cells = new Cell[9][9];

	public SudokuGrid() {
		for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
			for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
				cells[columnIndex][rowIndex] = new Cell();
			}
		}
	}

	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public int getValue(int columnIndex, int rowIndex) {
		return cells[columnIndex][rowIndex].getValue();
	}

	public boolean setValue(Integer value, int columnIndex, int rowIndex) {

		boolean solved = cells[columnIndex][rowIndex].setValue(value);

		// Remove probabilities from column and rows
		for (int i = 0; i < 9; i++) {

			// Remove from current column
			this.removeProbability(value, columnIndex, i);

			// Remove from current row
			this.removeProbability(value, i, rowIndex);
		}

		// Remove probabilities from grid
		int currentGridColumn = columnIndex - (columnIndex % 3);
		int currentGridRow = rowIndex - (rowIndex % 3);

		for (int i = currentGridColumn; i < (currentGridColumn + 3); i++) {
			for (int j = currentGridRow; j < (currentGridRow + 3); j++) {
				this.removeProbability(value, i, j);
			}
		}

		return solved;
	}

	private void removeProbability(Integer probability, int columnIndex, int rowIndex) {

		List<Integer> probabilities = cells[columnIndex][rowIndex].getProbabilities();
		probabilities.remove(probability);

		if (probabilities.size() == 1) {
			this.setValue(probabilities.get(0), columnIndex, rowIndex);
		}
	}

	@Override
	public String toString() {

		String string = "";

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				string += cells[i][j] + "\t";
			}
			string += "\n";
		}

		return string;
	}
}

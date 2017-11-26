package controller;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import model.SudokuGrid;

public class SudokuGridController {

	private SudokuGrid sudokuGrid = new SudokuGrid();

	@FXML
	private GridPane grid;

	public SudokuGridController() {
	}

	@FXML
	protected void solve(ActionEvent event) {

		boolean solved = false;

		while (true) {

			for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
				for (int rowIndex = 0; rowIndex < 9; rowIndex++) {

					// Check in column

					Integer aloneProbability = 0;

					for (Integer probability : sudokuGrid.getCells()[columnIndex][rowIndex].getProbabilities()) {

						boolean found = false;

						for (int i = 0; i < 9; i++) {

							if (i == rowIndex) {
								continue;
							}

							if (sudokuGrid.getCells()[columnIndex][i].getProbabilities().contains(probability)) {
								found = true;
								break;
							}
						}

						if (!found) {
							aloneProbability = probability;
							break;
						}
					}

					if (!aloneProbability.equals(0)) {
						solved = sudokuGrid.setValue(aloneProbability, columnIndex, rowIndex);
						System.out.println("Solved the row loner");
						System.out.println(
								"column:\t" + columnIndex + "\trow:\t" + rowIndex + "\tvalue:\t" + aloneProbability);
					}

					// Check in row

					aloneProbability = 0;

					for (Integer probability : sudokuGrid.getCells()[columnIndex][rowIndex].getProbabilities()) {

						boolean found = false;

						for (int i = 0; i < 9; i++) {

							if (i == columnIndex) {
								continue;
							}

							if (sudokuGrid.getCells()[i][rowIndex].getProbabilities().contains(probability)) {
								found = true;
								break;
							}
						}

						if (!found) {
							aloneProbability = probability;
							break;
						}
					}

					if (!aloneProbability.equals(0)) {
						solved = sudokuGrid.setValue(aloneProbability, columnIndex, rowIndex);
						System.out.println("Solved the column loner");
						System.out.println(
								"column:\t" + columnIndex + "\trow:\t" + rowIndex + "\tvalue:\t" + aloneProbability);
					}

					// Check in grid

					aloneProbability = 0;

					for (Integer probability : sudokuGrid.getCells()[columnIndex][rowIndex].getProbabilities()) {

						boolean found = false;

						int column = columnIndex - (columnIndex % 3);
						int row = rowIndex - (rowIndex % 3);

						for (int c = column; c < (column + 3); c++) {
							for (int r = row; r < (row + 3); r++) {

								if (c == columnIndex && r == rowIndex) {
									continue;
								}

								if (sudokuGrid.getCells()[c][r].getProbabilities().contains(probability)) {
									found = true;
									break;
								}
							}
						}

						if (!found) {
							aloneProbability = probability;
							break;
						}
					}

					if (!aloneProbability.equals(0)) {
						solved = sudokuGrid.setValue(aloneProbability, columnIndex, rowIndex);
					}
				}
			}

			if (solved) {
				solved = false;
			} else {
				break;
			}
		}

		for (Node node : grid.getChildren()) {

			int columnIndex = GridPane.getColumnIndex(node);
			int rowIndex = GridPane.getRowIndex(node);

			TextField textField = (TextField) node;

			int value = sudokuGrid.getValue(columnIndex, rowIndex);
			if (value != 0) {
				textField.setText(Integer.toString(value));
				textField.setFont(Font.font(36));
				if (!textField.getStyle().equals("-fx-text-inner-color: red;")) {
					textField.setStyle("-fx-text-inner-color: blue;");
				}
			} else {

				String sValue = "";

				for (int i = 1; i <= 9; i++) {
					if (sudokuGrid.getCells()[columnIndex][rowIndex].getProbabilities().contains(i)) {
						sValue += i;
					} else {
						sValue += " ";
					}
				}

				textField.setText(sValue);
				textField.setFont(Font.font(18));
				textField.setStyle("-fx-text-inner-color: grey;");
			}
		}
	}

	@FXML
	protected void textChanged(ActionEvent event) {

		System.out.println("textChanged");

		TextField textField = (TextField) event.getSource();
		String text = textField.getText();

		int columnIndex = GridPane.getColumnIndex(textField);
		int rowIndex = GridPane.getRowIndex(textField);

		if (text.length() == 1) {
			sudokuGrid.setValue(Integer.parseInt(text), GridPane.getColumnIndex(textField),
					GridPane.getRowIndex(textField));
		}

		List<Integer> probabilities = sudokuGrid.getCells()[columnIndex][rowIndex].getProbabilities();
		probabilities.clear();

		for (Character ch : text.toCharArray()) {
			if (!ch.equals(' ')) {
				probabilities.add(Integer.parseInt(ch.toString()));
			}
		}
	}
}

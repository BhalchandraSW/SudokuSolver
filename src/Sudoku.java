import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Sudoku extends Application {

	private SudokuGrid sudokuGrid = new SudokuGrid();

	public static void main(String[] args) {
		launch(args);
	}

	private static String[][] values = { { "", "7", "", "", "", "", "2", "", "9" },
			{ "8", "", "1", "", "", "7", "", "", "" }, { "", "5", "", "1", "", "", "", "", "6" },
			{ "", "", "5", "", "8", "", "", "2", "" }, { "", "", "", "9", "", "1", "", "", "" },
			{ "", "1", "", "", "4", "", "5", "", "" }, { "1", "", "", "", "", "3", "", "9", "" },
			{ "", "", "", "4", "", "", "6", "", "2" }, { "6", "", "8", "", "", "", "", "3", "" } };

	@Override
	public void start(Stage primaryStage) throws Exception {

		BorderPane border = new BorderPane();

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);

		for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
			for (int rowIndex = 0; rowIndex < 9; rowIndex++) {

				TextField t = new TextField(values[rowIndex][columnIndex]);
				t.setAlignment(Pos.CENTER);

				grid.add(t, columnIndex, rowIndex);
			}
		}

		border.setCenter(grid);

		Button b = new Button("Get Answer");
		b.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				for (Node node : grid.getChildren()) {

					TextField text = (TextField) node;

					int columnIndex = GridPane.getColumnIndex(node);
					int rowIndex = GridPane.getRowIndex(node);

					int value = 0;

					if (!text.getText().equals("")) {
						value = Integer.parseInt(text.getText());
						text.setStyle("-fx-text-inner-color: red;");
						// text.setFont(clueFont);
					}

					sudokuGrid.setValue(value, columnIndex, rowIndex);
				}

				// Loner Test

				int count = 0;

				boolean solved = false;

				while (true) {

					count++;

					for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
						for (int rowIndex = 0; rowIndex < 9; rowIndex++) {

							// Check in column

							Integer aloneProbability = 0;

							for (Integer probability : sudokuGrid.getCells()[columnIndex][rowIndex]
									.getProbabilities()) {

								boolean found = false;

								for (int i = 0; i < 9; i++) {

									if (i == rowIndex) {
										continue;
									}

									if (sudokuGrid.getCells()[columnIndex][i].getProbabilities()
											.contains(probability)) {
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
								System.out.println("column:\t" + columnIndex + "\trow:\t" + rowIndex + "\tvalue:\t"
										+ aloneProbability);
							}

							// Check in row

							aloneProbability = 0;

							for (Integer probability : sudokuGrid.getCells()[columnIndex][rowIndex]
									.getProbabilities()) {

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
								System.out.println("column:\t" + columnIndex + "\trow:\t" + rowIndex + "\tvalue:\t"
										+ aloneProbability);
							}

							// Check in grid

							aloneProbability = 0;

							for (Integer probability : sudokuGrid.getCells()[columnIndex][rowIndex]
									.getProbabilities()) {

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
								System.out.println("Solved the column loner");
								System.out.println("column:\t" + columnIndex + "\trow:\t" + rowIndex + "\tvalue:\t"
										+ aloneProbability);
							}
						}
					}

					if (!solved) {
						break;
					} else {
						solved = false;
					}
				}

				for (Node node : grid.getChildren()) {

					int columnIndex = GridPane.getColumnIndex(node);
					int rowIndex = GridPane.getRowIndex(node);

					TextField textField = (TextField) node;

					int value = sudokuGrid.getValue(columnIndex, rowIndex);
					if (value != 0) {
						textField.setText(Integer.toString(value));
						if (!textField.getStyle().equals("-fx-text-inner-color: red;")) {
							textField.setStyle("-fx-text-inner-color: blue;");
						}
					} else {
						String sValue = "";
						for (int i : sudokuGrid.getCells()[columnIndex][rowIndex].getProbabilities()) {
							sValue += i + ",\t";
						}
						textField.setText(sValue.substring(0, sValue.length() - 2));
						textField.setStyle("-fx-text-inner-color: grey;");
					}
				}

				System.out.println("Ran " + count + " loops");

			}
		});

		border.setBottom(b);

		Scene scene = new Scene(border);

		primaryStage.setTitle("Sudoku Solver");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
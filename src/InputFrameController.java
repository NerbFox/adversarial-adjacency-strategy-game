import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

/**
 * The InputFrameController class. It controls input from the users and
 * validates it.
 * If validation is successful, the Adjacency game screen will pop up in a
 * different window.
 *
 * @author Jedid Ahn
 *
 */
public class InputFrameController {

    public CheckBox isBotFirst;
    @FXML
    private TextField player1;

    @FXML
    private TextField player2;

    @FXML
    private ComboBox<String> player1Type;
    @FXML
    private ComboBox<String> player2Type;
    @FXML
    private ComboBox<String> numberOfRounds;

    /**
     * Initialize the dropdown ComboBox with a list of items that are allowed to be
     * selected.
     * Select the first item in the list as the default value of the dropdown.
     *
     */
    @FXML
    private void initialize() {
        ObservableList<String> numberOfRoundsDropdown = FXCollections.observableArrayList(
                "", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
        ObservableList<String> player1TypeDropdown = FXCollections.observableArrayList(
                "", "human", "minimax", "genetic algorithm", "simulated annealing", "stochastic hill climbing",
                "steepest hill climbing");

        ObservableList<String> player2TypeDropdown = FXCollections.observableArrayList(
                "", "minimax", "genetic algorithm", "simulated annealing", "stochastic hill climbing", "steepest hill climbing");

        this.numberOfRounds.setItems(numberOfRoundsDropdown);
        this.numberOfRounds.getSelectionModel().select(0);
        this.player1Type.setItems(player1TypeDropdown);
        this.player1Type.getSelectionModel().select(0);
        this.player2Type.setItems(player2TypeDropdown);
        this.player2Type.getSelectionModel().select(0);
    }

    /**
     * Reset player1 and player2 text fields and reset numberOfRounds dropdown to
     * default value
     * if reset button is clicked.
     *
     */
    @FXML
    private void reset() {
        this.player1.setText("");
        this.player2.setText("");
        this.numberOfRounds.getSelectionModel().select(0);
    }

    /**
     * Open OutputFrame controlled by OutputFrameController if play button is
     * clicked and
     * all input have been successfully validated.
     *
     * @exception IOException To load the FXMLLoader to open the Adjacency game
     *                        screen (output screen).
     *
     */
    @FXML
    private void play() throws IOException {
        if (this.isInputFieldValidated()) {
            // Close primary stage/input frame.
            Stage primaryStage = (Stage) this.player1.getScene().getWindow();
            primaryStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("OutputFrame.fxml"));
            Parent root = loader.load();

            // Get controller of output frame and pass input including player names and
            // number of rounds chosen.
            OutputFrameController outputFC = loader.getController();
            outputFC.getInput(this.player1.getText(), this.player2.getText(), this.numberOfRounds.getValue(),
                    this.isBotFirst.isSelected(),
                    this.player1Type.getValue(), this.player2Type.getValue());

            // Open the new frame.
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Game Board Display");
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setResizable(true);
            secondaryStage.show();
        }
    }

    /**
     * Return whether all input fields have been successfully validated or not.
     *
     * @return boolean
     *
     */
    private boolean isInputFieldValidated() {
        String playerX = this.player1.getText();
        String playerO = this.player2.getText();
        String roundNumber = this.numberOfRounds.getValue();
        String player1Type = this.player1Type.getValue();
        String player2Type = this.player2Type.getValue();

        if (playerX.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Player 1 name is blank.").showAndWait();
            return false;
        }

        if (playerO.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Player 2 name is blank.").showAndWait();
            return false;
        }

        if (playerX.equals(playerO)) {
            new Alert(Alert.AlertType.ERROR, "Player 1 and Player 2 cannot have the same name.").showAndWait();
            return false;
        }

        if (player1Type.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Minimal Player 1 dipilih ya sayang").showAndWait();
            return false;
        }

        if (player2Type.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Minimal Pkayer 2 dipilih ya sayang").showAndWait();
            return false;
        }

        if (roundNumber.length() == 0) {
            new Alert(Alert.AlertType.ERROR, "Number of rounds dropdown menu is blank.").showAndWait();
            return false;
        }

        return true;
    }
}
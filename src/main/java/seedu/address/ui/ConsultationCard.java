package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Consultation}.
 */
public class ConsultationCard extends UiPart<Region> {

    private static final String FXML = "ConsultationListCard.fxml";

    public final Consultation consultation;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label consultationTime;
    @FXML
    private Label studentName;
    @FXML
    private Label studentNusNetId;
    // Add other fields as needed

    /**
     * Creates a {@code ConsultationCard} with the given {@code Consultation} and index to display.
     */
    public ConsultationCard(Consultation consultation, int displayedIndex, Model model) {
        super(FXML);
        this.consultation = consultation;
        id.setText(displayedIndex + ". ");
        consultationTime.setText(consultation.showConsultationTime());

        Person student = model.findPerson(consultation.getNusnetid());
        if (student != null) {
            studentName.setText(student.getName().fullName);
            studentNusNetId.setText(consultation.getNusnetid().value);
        } else {
            studentName.setText("(Unknown student)");
        }
        // Set other fields as needed
    }
}

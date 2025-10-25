package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;

/**
 * Panel containing the list of consultations.
 */
public class ConsultationListPanel extends UiPart<Region> {
    private static final String FXML = "ConsultationListPanel.fxml";

    @FXML
    private ListView<Consultation> consultationListView;

    private Model model;

    /**
     * Creates a {@code ConsultationListPanel} with the given {@code ObservableList}.
     */
    public ConsultationListPanel(ObservableList<Consultation> consultationList, Model model) {
        super(FXML);
        consultationListView.setItems(consultationList);
        consultationListView.setCellFactory(listView -> new ConsultationListViewCell());
        this.model = model;
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Consultation} using a {@code ConsultationCard}.
     */
    class ConsultationListViewCell extends ListCell<Consultation> {
        @Override
        protected void updateItem(Consultation consultation, boolean empty) {
            super.updateItem(consultation, empty);

            if (empty || consultation == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ConsultationCard(consultation, getIndex() + 1, model).getRoot());
            }
        }
    }
}

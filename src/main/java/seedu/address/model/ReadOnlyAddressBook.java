package seedu.address.model;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Person;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {
    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    List<Person> getUniquePersonList();

    /**
     * Returns an unmodifiable view of the consultations list.
     * This list will not contain any duplicate consultations.
     */
    ObservableList<Consultation> getConsultationList();
    /**
     * Returns an unmodifiable view of the groups list.
     * This list will not contain any duplicate groups.
     */
    ObservableList<Group> getGroupList();
    /**
     * Updates groups when a person is added.
     * @param person the person that is added
     */
    void updateGroupWhenAddPerson(Person person);
}

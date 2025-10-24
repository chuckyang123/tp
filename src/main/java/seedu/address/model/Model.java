package seedu.address.model;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /** {@code Predicate} that evaluates to true if a person has a consultation scheduled */
    Predicate<Person> PREDICATE_SHOW_PERSONS_WITH_CONSULTATIONS = person -> person.getConsultation().isPresent();

    /** {@code Predicate} that always evaluate to true */
    Predicate<Consultation> PREDICATE_SHOW_ALL_CONSULTATIONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    boolean hasPerson(Person person);

    /**
     * Returns true if a person with the same nusnetid as {@code nusnetid} exists in the address book.
     */
    boolean hasPerson(Nusnetid nusnetid);

    /**
     * Returns the person with the given nusnetid.
     * Returns null if no such person exists.
     */
    Person findPerson(Nusnetid nusnetid);

    /**
     * Return true if a group with the same groupId as {@code groupId} exists in the address book.
     */
    boolean hasGroup(GroupId groupId);

    /**
     * Deletes the given person.
     * The person must exist in the address book.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the address book.
     */
    void addPerson(Person person);

    /**
     * Gets a person by their nusnetId.
     * @param nusnetId the nusnetId of the person to be retrieved
     * @return the person with the specified nusnetId
     * @throws CommandException if no person with the given nusnetId exists in the address book
     */
    Person getPersonByNusnetId(Nusnetid nusnetId) throws CommandException;

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    void setPerson(Person target, Person editedPerson);
    /**
     * Updates the groups to include the newly added person.
     * @param person the person that was added
     */
    void updateGroupWhenAddPerson(Person person) throws CommandException;

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Returns true if a consultation equivalent to {@code consultation} exists in the address book.
     */
    boolean hasConsultation(Consultation consultation);

    /**
     * Returns true if a consultation that overlaps with {@code consultation} exists in the address book.
     */
    boolean hasOverlappingConsultation(Consultation consultation);

    /**
     * Adds the given consultation.
     * {@code consultation} must not already exist in the address book.
     */
    void addConsultation(Consultation consultation);

    /**
     * Deletes the given consultation.
     * The {@code consultation} must exist in the address book.
     */
    void deleteConsultation(Consultation consultation);

    /** Returns an unmodifiable view of the filtered consultation list */
    ObservableList<Consultation> getFilteredConsultationList();

    /**
     * Updates the filter of the filtered consultation list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredConsultationList(Predicate<Consultation> predicate);

    /**
     * Adds the given consultation to the person identified by the given nusnetid.
     */
    void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation);

    /**
     * Deletes the consultation from the person identified by the given nusnetid.
     * @return the consultation that was deleted
     */
    Consultation deleteConsultationFromPerson(Nusnetid nusnetid);

    /**
     * Adds a group to the model.
     */
    void addGroup(Group group);

    /**
     * Returns the list of groups in the model.
     */
    List<Group> getGroupList();

    /**
     * Gets a group by its groupId.
     * @param groupId the groupId of the group to be retrieved
     * @return the group with the specified groupId
     */
    Group getGroup(GroupId groupId);
}

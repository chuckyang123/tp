package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    private static final String MESSAGE_STUDENT_NOT_FOUND = "Student not found.";
    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Consultation> filteredConsultations;
    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        filteredConsultations = new FilteredList<>(this.addressBook.getConsultationList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasPerson(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        return addressBook.hasPerson(nusnetid);
    }

    @Override
    public boolean hasGroup(GroupId groupId) {
        requireNonNull(groupId);
        return addressBook.hasGroup(groupId);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }
    /**
     * Updates the group information when a person is added.
     * @param person the person that was added
     * @throws CommandException if duplicate person exception occurs
     */
    @Override
    public void updateGroupWhenAddPerson(Person person) {
        requireNonNull(person);
        this.addressBook.updateGroupWhenAddPerson(person);
    }
    /**
     * Retrieves a person by their nusnetId.
     * @param nusnetId the nusnetId of the person to be retrieved
     * @return the person with the specified nusnetId
     * @throws CommandException if no person with the given nusnetId is found
     */
    @Override
    public Person getPersonByNusnetId(Nusnetid nusnetId) throws CommandException {
        requireNonNull(nusnetId);
        assert hasPerson(nusnetId) : "Person with given nusnetId should exist in the address book.";
        Person target = this.getFilteredPersonList()
                .stream().filter(p -> p.getNusnetid().equals(nusnetId))
                .findFirst().orElseThrow(() -> new CommandException(MESSAGE_STUDENT_NOT_FOUND));
        return target;
    }

    @Override
    public boolean hasConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return addressBook.hasConsultation(consultation);
    }

    @Override
    public boolean hasOverlappingConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return addressBook.hasOverlappingConsultation(consultation);
    }

    @Override
    public void addConsultation(Consultation consultation) {
        addressBook.addConsultation(consultation);
        updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
    }

    @Override
    public void deleteConsultation(Consultation consultation) {
        addressBook.deleteConsultation(consultation);
        updateFilteredConsultationList(PREDICATE_SHOW_ALL_CONSULTATIONS);
    }

    @Override
    public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
        requireAllNonNull(nusnetid, consultation);
        addressBook.addConsultationToPerson(nusnetid, consultation);
        Predicate<Person> predicate = person -> person.hasSameNusnetId(nusnetid);
        updateFilteredPersonList(predicate);
    }

    @Override
    public Consultation deleteConsultationFromPerson(Nusnetid nusnetid) {
        requireAllNonNull(nusnetid);
        Consultation deletedConsultation = addressBook.deleteConsultationFromPerson(nusnetid);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return deletedConsultation;
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    /**
     * Adds a group to the model.
     * @param group the group to be added
     */
    @Override
    public void addGroup(Group group) {
        requireNonNull(group);
        addressBook.addGroup(group);
    }

    @Override
    public List<Group> getGroupList() {
        return addressBook.getGroupList();
    }

    //=========== Filtered Consultation List Accessors =============================================================
    /**
     * Returns an unmodifiable view of the list of {@code Consultation} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Consultation> getFilteredConsultationList() {
        return filteredConsultations;
    }

    @Override
    public void updateFilteredConsultationList(Predicate<Consultation> predicate) {
        requireNonNull(predicate);
        filteredConsultations.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    @Override
    public Group getGroup(GroupId groupId) {
        requireNonNull(groupId);
        return addressBook.getGroup(groupId);
    }
}

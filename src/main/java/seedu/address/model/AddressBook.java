package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.event.Consultation;
import seedu.address.model.event.UniqueConsultationList;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueConsultationList consultations;
    private final UniqueGroupList groups;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        consultations = new UniqueConsultationList();
        groups = new UniqueGroupList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    /**
     * Replaces the contents of the consultation list with {@code consultations}.
     * {@code consultations} must not contain duplicate consultations.
     */
    public void setConsultations(List<Consultation> consultations) {
        this.consultations.setConsultations(consultations);
    }
    /**
     * Returns true if a group with the same identity as {@code groupId} exists in the address book.
     */
    public boolean hasGroup(GroupId groupId) {
        requireNonNull(groupId);
        return groups.contains(groupId);
    }
    /**
     * Replaces the contents of the group list with {@code groups}.
     * {@code groups} must not contain duplicate groups.
     */
    public void setGroups(List<Group> groups) {
        this.groups.setGroups(groups);
    }

    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group g) {
        requireNonNull(g);
        this.groups.add(g);
    }

    /**
     * Gets a group by GroupId, or null if not present.
     */
    public Group getGroup(GroupId groupId) {
        requireNonNull(groupId);
        return groups.getGroup(groupId);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setPersons(newData.getPersonList());
        setConsultations(newData.getConsultationList());
        setGroups(newData.getGroupList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Returns true if a person with the same nusnetid as {@code nusnetid} exists in the address book.
     */
    public boolean hasPerson(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        return persons.contains(nusnetid);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    /**
     * Adds the given {@code consultation} to the person identified by {@code nusnetid}.
     * The person must exist in the address book.
     */
    public void addConsultationToPerson(Nusnetid nusnetid, Consultation consultation) {
        persons.addConsultationToPerson(nusnetid, consultation);
    }

    /**
     * Deletes the consultation from the person identified by {@code nusnetid}.
     * The person must exist in the address book.
     * @return the deleted Consultation.
     */
    public Consultation deleteConsultationFromPerson(Nusnetid nusnetid) {
        return persons.deleteConsultationFromPerson(nusnetid);
    }

    //// consultation-level operations

    /**
     * Returns true if a consultations equivalent to {@code consultation} exists in the address book.
     */
    public boolean hasConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return consultations.contains(consultation);
    }

    /**
     * Returns true if a consultation overlapping with {@code consultation} exists in the address book.
     */
    public boolean hasOverlappingConsultation(Consultation consultation) {
        requireNonNull(consultation);
        return consultations.hasOverlappingConsultation(consultation);
    }

    /**
     * Adds a consultation to the address book.
     * The consultation must not already exist in the address book.
     */
    public void addConsultation(Consultation c) {
        consultations.add(c);
    }

    /**
     * Deletes the given consultation from the address book.
     * The consultation must exist in the address book.
     */
    public void deleteConsultation(Consultation c) {
        consultations.remove(c);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("consultations", consultations)
                .add("groups", groups)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }
    public List<Person> getUniquePersonList() {
        return persons.toList();
    }

    @Override
    public ObservableList<Consultation> getConsultationList() {
        return consultations.asUnmodifiableObservableList();
    }
    @Override
    public ObservableList<Group> getGroupList() {
        return groups.asUnmodifiableObservableList();
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return persons.equals(otherAddressBook.persons)
                && consultations.equals(otherAddressBook.consultations)
                && groups.equals(otherAddressBook.groups);
    }

    @Override
    public int hashCode() {
        int result = persons.hashCode();
        result = 31 * result + consultations.hashCode();
        result = 31 * result + groups.hashCode();
        return result;
    }
}

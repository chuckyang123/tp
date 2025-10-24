package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.Group;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_CONSULTATION =
            "Consultations list contains duplicate consultation(s).";
    public static final String MESSAGE_DUPLICATE_GROUP = "Groups list contains duplicate group(s).";
    public static final String MESSAGE_GROUP_REFERENCES_UNKNOWN_PERSON =
            "Group references a person that does not exist in persons list.";
    public static final String MESSAGE_GROUP_CONTAINS_INVALID_NUSNETID =
            "Group contains invalid nusnetid(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedConsultation> consultations = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("consultations") List<JsonAdaptedConsultation> consultations,
                                       @JsonProperty("groups") List<JsonAdaptedGroup> groups) {
        if (persons != null) {
            this.persons.addAll(persons);
        }
        if (consultations != null) {
            this.consultations.addAll(consultations);
        }
        if (groups != null) {
            this.groups.addAll(groups);
        }
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        consultations.addAll(source.getConsultationList().stream()
                .map(JsonAdaptedConsultation::new).collect(Collectors.toList()));
        groups.addAll(source.getGroupList().stream().map(JsonAdaptedGroup::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @return AddressBook object
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        // Convert and add all students so that groups can refer to existing students
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (addressBook.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addPerson(person);
        }
        // Convert and add all consultations
        for (JsonAdaptedConsultation jsonAdaptedConsultation : consultations) {
            Consultation consultation = jsonAdaptedConsultation.toModelType();
            if (addressBook.hasConsultation(consultation)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CONSULTATION);
            }
            addressBook.addConsultation(consultation);
        }
        // Convert and add all groups after students have been added so that
        // we can validate that each nus net id in group refers to an existing student
        List<Group> modelGroups = new ArrayList<>();
        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            // validate and build Group object from one JsonAdaptedGroup
            GroupId modelGroupId = jsonAdaptedGroup.toModelGroupId();
            List<String> nusnetidsInGroup = jsonAdaptedGroup.getStudentNusnetids();
            ArrayList<Person> studentsInGroup = new ArrayList<>();
            // For each stored nus net id in group,
            // find the corresponding Person in addressBook and add to the group
            for (String nusIdStr : nusnetidsInGroup) {
                if (nusIdStr == null) {
                    throw new IllegalValueException(MESSAGE_GROUP_CONTAINS_INVALID_NUSNETID);
                }
                if (!Nusnetid.isValidNusnetid(nusIdStr)) {
                    throw new IllegalValueException(Nusnetid.MESSAGE_CONSTRAINTS);
                }
                Person student = addressBook.getPersonByNusnetId(new Nusnetid(nusIdStr));
                // person not found in address book
                if (student == null) {
                    throw new IllegalValueException(MESSAGE_GROUP_REFERENCES_UNKNOWN_PERSON);
                }
                studentsInGroup.add(student);
            }
            Group modelGroup = new Group(modelGroupId, studentsInGroup);
            modelGroups.add(modelGroup);
        }

        // check for duplicate groups
        long distinctCount = modelGroups.stream().distinct().count();
        if (distinctCount != modelGroups.size()) {
            throw new IllegalValueException(MESSAGE_DUPLICATE_GROUP);
        }

        addressBook.setGroups(modelGroups);
        return addressBook;
    }

}

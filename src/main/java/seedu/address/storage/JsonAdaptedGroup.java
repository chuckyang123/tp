package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.Group;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;

/**
 * Jackson-friendly version of {@link Group}.
 * Two fields: groupId {@code String} and list of student nusnetids {@code List<String>}.
 */
class JsonAdaptedGroup {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Group's %s field is missing!";

    private final String groupId;
    private final List<String> studentNusnetids = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedGroup} with the given group details.
     */
    @JsonCreator
    public JsonAdaptedGroup(@JsonProperty("groupId") String groupId,
                            @JsonProperty("studentNusnetids") List<String> studentNusnetids) {
        this.groupId = groupId;
        if (studentNusnetids != null) {
            this.studentNusnetids.addAll(studentNusnetids);
        }
    }

    /**
     * Converts a given {@code Group} into this class for Jackson use.
     */
    public JsonAdaptedGroup(Group source) {
        groupId = source.getGroupId().value;
        studentNusnetids.addAll(source.getAllPersons().stream()
                .map(p -> p.getNusnetid().value).collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted group object into the model's {@code GroupId} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public GroupId toModelGroupId() throws IllegalValueException {
        if (groupId == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    GroupId.class.getSimpleName()));
        }
        if (!GroupId.isValidGroupId(groupId)) {
            throw new IllegalValueException(GroupId.MESSAGE_CONSTRAINTS);
        }
        return new GroupId(groupId);
    }

    public List<Nusnetid> getStudentNusnetidsAsIds() {
        return studentNusnetids.stream().map(Nusnetid::new).collect(Collectors.toList());
    }
    public List<String> getStudentNusnetids() {
        return studentNusnetids;
    }
    public String getGroupId() {
        return groupId;
    }
}

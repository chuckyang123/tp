package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a student's group identifier in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidGroupId(String)}
 */
public class GroupId {

    public static final String MESSAGE_CONSTRAINTS =
            "Group IDs should start with T or B (case-insensitive) and be followed by exactly 2 digits";
    public static final String VALIDATION_REGEX = "[TtBb]\\d{2}";

    public final String value;

    /**
     * Constructs a {@code GroupId}.
     *
     * @param groupId A valid group id.
     */
    public GroupId(String groupId) {
        requireNonNull(groupId);
        checkArgument(isValidGroupId(groupId), MESSAGE_CONSTRAINTS);
        char prefix = Character.toUpperCase(groupId.charAt(0));
        value = prefix + groupId.substring(1);
    }

    /**
     * Returns true if a given string is a valid group id.
     */
    public static boolean isValidGroupId(String test) {
        return test.matches(VALIDATION_REGEX);
    }
    /**
     * Check if the given group id is valid.
     * @param test group id to be checked
     * @return boolean valid or not
     */
    public static boolean isValidGroupId(GroupId test) {
        requireNonNull(test);
        return test.value.matches(VALIDATION_REGEX);
    }
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof GroupId)) {
            return false;
        }
        GroupId otherId = (GroupId) other;
        return value.equals(otherId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}


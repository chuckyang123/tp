package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;

import seedu.address.model.Group;
import seedu.address.model.Model;
import seedu.address.model.person.GroupId;

/**
 * Creates a group with a given groupId (used as group identifier).
 */
public class CreateGroupCommand extends Command {
    public static final String COMMAND_WORD = "create_group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Creates a new group identified by the given group id. "
            + "Parameters: " + PREFIX_GROUP + "GROUPID "
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP + "T12";
    public static final String MESSAGE_DUPLICATE_GROUP = "Group %s already exists!";
    public static final String MESSAGE_SUCCESS = "New group %s created";
    private final GroupId groupId;

    /**
     * Creates a CreateGroupCommand to add the specified {@code groupId}
     */
    public CreateGroupCommand(GroupId groupId) {
        requireNonNull(groupId);
        this.groupId = groupId;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        // Check for duplicate group id
        boolean duplicate = model.getGroupList().stream()
            .anyMatch(g -> g.getGroupId().equals(groupId));
        if (duplicate) {
            return new CommandResult(String.format(MESSAGE_DUPLICATE_GROUP, groupId));
        }
        // Create a new group with the given id
        Group newGroup = new Group(groupId);
        model.addGroup(newGroup);
        return new CommandResult(String.format(MESSAGE_SUCCESS, groupId));
    }
}

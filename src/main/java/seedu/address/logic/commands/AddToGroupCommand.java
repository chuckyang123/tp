package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Group;
import seedu.address.model.Model;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * Adds a person to a group.
 */
public class AddToGroupCommand extends Command {
    public static final String COMMAND_WORD = "add_to_group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a existing student to a group.\n"
            + "Parameters: " + PREFIX_GROUP + "GROUPID " + PREFIX_NUSNETID + "NETID \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP + "T01 " + PREFIX_NUSNETID + "E1234567 \n";

    private static final String MESSAGE_SUCCESS = "Student %s added to Group %s.";
    private final GroupId groupId;
    private final Nusnetid nusnetId;
    /**
     * Creates an {@code AddToGroupCommand} to add a person to a group.
     *
     * @param nusnetId the nus netId of the target student
     * @param groupId the ID of the group to add the student to
     */
    public AddToGroupCommand(Nusnetid nusnetId, GroupId groupId) {
        requireNonNull(nusnetId);
        requireNonNull(groupId);
        this.nusnetId = nusnetId;
        this.groupId = groupId;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Person target = model.getPersonByNusnetId(nusnetId);
        Person updatedStudent = target.withUpdatedGroup(groupId);
        try {
            model.setPerson(target, updatedStudent);
        } catch (DuplicatePersonException e) {
            throw new CommandException(e.getMessage());
        }
        // if group does not exist, create it
        if (!model.hasGroup(groupId)) {
            Group newGroup = new Group(groupId);
            model.addGroup(newGroup);
            newGroup.addStudent(updatedStudent);
        } else { // group exists
            Group group = model.getGroup(groupId);
            group.setPerson(target, updatedStudent);
        }
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, nusnetId, groupId));
    }
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AddToGroupCommand otherCommand)) {
            return false;
        }
        return nusnetId.equals(otherCommand.nusnetId)
                && groupId.equals(otherCommand.groupId);
    }
}

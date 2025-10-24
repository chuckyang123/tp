package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;

import java.util.function.Predicate;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Person;

/**
 * Finds and lists all persons in address book whose group ID matches the input group id.
 * Example: find_group g/T01
 */
public class FindGroupCommand extends Command {
    public static final String COMMAND_WORD = "find_group";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all students whose group IDs match the input group id \n"
            + "Parameters: GROUP ID\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_GROUP + " T01";
    private final GroupId groupId;
    private final Predicate<Person> predicate;
    /**
     * Creates a FindGroupCommand to find persons by their group ID.
     * @param groupId the group ID to search for.
     */
    public FindGroupCommand(GroupId groupId) {
        this.groupId = groupId;
        this.predicate = person -> person.getGroupId().equals(this.groupId);
    }
    /**
     * Executes the command and returns the result message.
     * @param model {@code Model} which the command should operate on.
     * @return the command result message.
     */
    @Override
    public CommandResult execute(Model model) {
        model.updateFilteredPersonList(predicate);
        return new CommandResult(String.format(
                Messages.MESSAGE_PERSONS_LISTED_OVERVIEW,
                model.getFilteredPersonList().size()));
    }
    /**
     * Checks equality between this FindGroupCommand and another object.
     * @param other the other object to compare with.
     * @return true if both are FindGroupCommand instances with the same groupId, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindGroupCommand otherFindGroupCommand)) {
            return false;
        }
        return groupId.equals(otherFindGroupCommand.groupId);
    }
    @Override
    public String toString() {
        return String.format("FindGroupCommand{groupId=%s}", groupId.toString());
    }
}

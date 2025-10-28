package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.GroupId;

public class CreateGroupCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    @Test
    public void execute_validGroupId_groupCreated() {
        GroupId groupId = new GroupId("B90");
        CreateGroupCommand command = new CreateGroupCommand(groupId);
        try {
            command.execute(model);
        } catch (CommandException e) {
            throw new AssertionError("CommandException not expected");
        }
        assert(model.hasGroup(groupId));
    }
    @Test
    public void execute_duplicateGroupId_throwsException() {
        GroupId groupId = new GroupId("T01"); // Assuming T01 already exists
        CreateGroupCommand command = new CreateGroupCommand(groupId);
        try {
            command.execute(model);
            throw new AssertionError("Expected CommandException Thrown By Duplicate Group Id");
        } catch (CommandException e) {
            assertEquals(String.format(CreateGroupCommand.MESSAGE_DUPLICATE_GROUP, groupId), e.getMessage());
        }
    }
}

package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.GroupId;

public class FindGroupCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    @Test
    public void equals() {
        FindGroupCommand findFirstCommand = new FindGroupCommand(new GroupId("T01"));
        FindGroupCommand findSecondCommand = new FindGroupCommand(new GroupId("T01"));
        // same object -> returns true
        assertEquals(findFirstCommand, findFirstCommand);
        assertEquals(findFirstCommand, findSecondCommand);

        // same values -> returns true
        FindGroupCommand findFirstCommandCopy = new FindGroupCommand(new GroupId("B010"));
        assertNotEquals(findFirstCommand, findFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(1, findFirstCommand);

        // null -> returns false
        assertNotEquals(null, findFirstCommand);
    }
    @Test
    public void execute_validGroupId_personFound() {
        FindGroupCommand command = new FindGroupCommand(new GroupId("T01"));
        // 1 person in T01 group
        command.execute(model);
        command.execute(expectedModel);
        assertEquals(2, model.getFilteredPersonList().size());
        assertTrue(expectedModel.hasPerson(model.getFilteredPersonList().get(0)));
    }
    @Test
    public void execute_invalidGroupId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> new GroupId("X01"));
        assertEquals(GroupId.MESSAGE_CONSTRAINTS, ex.getMessage());
    }
    @Test
    public void execute_nonExistentGroupId_noPersonFound() {
        FindGroupCommand command = new FindGroupCommand(new GroupId("T99"));
        // no person in T99 group
        command.execute(model);
        command.execute(expectedModel);
        assertEquals(0, model.getFilteredPersonList().size());
    }
    @Test
    public void toStringMethod() {
        FindGroupCommand command = new FindGroupCommand(new GroupId("T01"));
        String expectedString = "FindGroupCommand{groupId=T01}";
        assertEquals(expectedString, command.toString());
    }
}

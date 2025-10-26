package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddHomeworkCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        // Add sample students
        Person alice = new PersonBuilder().withNusnetid("E1234567").withName("Alice").build();
        Person bob = new PersonBuilder().withNusnetid("E1234568").withName("Bob").build();
        model.addPerson(alice);
        model.addPerson(bob);
    }

    @Test
    public void execute_addSingleHomework_success() throws Exception {
        AddHomeworkCommand command = new AddHomeworkCommand(new Nusnetid("E1234567"), 1, false);
        String expectedMessage = String.format(AddHomeworkCommand.MESSAGE_SUCCESS_ONE, 1, "Alice");
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // check that Alice now has homework 1
        Person updatedAlice = model.getFilteredPersonList().stream()
                .filter(p -> p.getNusnetid().value.equals("E1234567")).findFirst().orElseThrow();
        assertEquals(true, updatedAlice.getHomeworkTracker().hasAssignment(1));
        assertEquals("incomplete", updatedAlice.getHomeworkTracker().getStatus(1));
    }

    @Test
    public void execute_addHomeworkToAll_success() throws Exception {
        AddHomeworkCommand command = new AddHomeworkCommand(null, 2, true);
        String expectedMessage = String.format(AddHomeworkCommand.MESSAGE_SUCCESS_ALL, 2);
        assertEquals(expectedMessage, command.execute(model).getFeedbackToUser());

        // all students should have homework 2
        for (Person p : model.getFilteredPersonList()) {
            assertEquals(true, p.getHomeworkTracker().hasAssignment(2));
            assertEquals("incomplete", p.getHomeworkTracker().getStatus(2));
        }
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        AddHomeworkCommand command = new AddHomeworkCommand(new Nusnetid("E0000000"), 1, false);
        assertThrows(Exception.class, () -> command.execute(model));
    }
}

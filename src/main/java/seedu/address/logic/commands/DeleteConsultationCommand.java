package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Nusnetid;

/**
 * Deletes a consultation from the address book.
 */
public class DeleteConsultationCommand extends Command {

    public static final String COMMAND_WORD = "delete_consult";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a consultation from the address book. "
            + "Parameters: "
            + PREFIX_NUSNETID + "NUSNETID \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NUSNETID + "E1234567";

    public static final String MESSAGE_SUCCESS = "Deleted consultation: %1$s";
    public static final String MESSAGE_STUDENT_DOES_NOT_EXIST = "Student does not exist";
    public static final String MESSAGE_STUDENT_DOES_NOT_HAVE_CONSULTATION =
            "Student does not have an existing consultation";

    private final Nusnetid toDelete;

    /**
     * Creates a DeleteConsultationCommand to delete consultation from the Person with the specified {@code nusnetid}
     */
    public DeleteConsultationCommand(Nusnetid nusnetid) {
        requireNonNull(nusnetid);
        toDelete = nusnetid;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.hasPerson(toDelete)) {
            throw new CommandException(MESSAGE_STUDENT_DOES_NOT_EXIST);
        }

        Consultation deletedConsultation;
        try {
            deletedConsultation = model.deleteConsultationFromPerson(toDelete);
        } catch (IllegalArgumentException e) { // handle error when student does not have a consultation
            throw new CommandException(e.getMessage());
        }

        model.deleteConsultation(deletedConsultation);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(deletedConsultation)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteConsultationCommand)) {
            return false;
        }

        DeleteConsultationCommand otherDeleteConsultationCommand = (DeleteConsultationCommand) other;
        return toDelete.equals(otherDeleteConsultationCommand.toDelete);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toDelete", toDelete)
                .toString();
    }
}

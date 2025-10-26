package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ASSIGNMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;

import seedu.address.logic.commands.DeleteHomeworkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new {@link DeleteHomeworkCommand} object.
 * <p>
 * Expected formats:
 * <ul>
 *     <li>{@code i/<nusnetId> a/<assignmentId>} – delete homework for one student</li>
 *     <li>{@code i/all a/<assignmentId>} – delete homework for all students</li>
 * </ul>
 * Assignment IDs must be integers between 1 and 3.
 */
public class DeleteHomeworkCommandParser implements Parser<DeleteHomeworkCommand> {

    @Override
    public DeleteHomeworkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String normalized = " " + args.trim();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(normalized, PREFIX_NUSNETID, PREFIX_ASSIGNMENT);
        try {
            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NUSNETID, PREFIX_ASSIGNMENT);
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
        }

        String assignmentRaw = argMultimap.getValue(PREFIX_ASSIGNMENT)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE)))
                .trim();
        // Reject extra tokens after a/ by requiring digits only
        if (!assignmentRaw.matches("\\d+")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE));
        }

        int assignmentId;
        try {
            assignmentId = Integer.parseInt(assignmentRaw);
        } catch (NumberFormatException e) {
            throw new ParseException("Assignment id must be an integer between 1 and 3.");
        }
        if (assignmentId < 1 || assignmentId > 3) {
            throw new ParseException("Assignment id must be between 1 and 3.");
        }

        String nusnetIdRaw = argMultimap.getValue(PREFIX_NUSNETID)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteHomeworkCommand.MESSAGE_USAGE)))
                .trim();

        if (nusnetIdRaw.equalsIgnoreCase("all")) {
            return new DeleteHomeworkCommand(null, assignmentId, true);
        }
        try {
            Nusnetid nusnetId = new Nusnetid(nusnetIdRaw);
            return new DeleteHomeworkCommand(nusnetId, assignmentId, false);
        } catch (IllegalArgumentException e) {
            throw new ParseException(Nusnetid.MESSAGE_CONSTRAINTS);
        }
    }
}

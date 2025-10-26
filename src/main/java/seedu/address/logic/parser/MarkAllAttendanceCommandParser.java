package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.Locale;

import seedu.address.logic.commands.MarkAllAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link MarkAllAttendanceCommand} object.
 * <p>
 * The expected input format is:
 * <pre>{@code
 * g/<group> w/<week> status/<present|absent|excused>
 * }</pre>
 * Week must be an integer between 2 and 13, and the status must be one of
 * "present", "absent", or "excused".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_all_attendance g/T02 w/3 status/present  // marks week 3 attendance for all students in group T02 as present
 * }</pre>
 */
public class MarkAllAttendanceCommandParser implements Parser<MarkAllAttendanceCommand> {

    @Override
    public MarkAllAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP, PREFIX_WEEK,
                PREFIX_STATUS);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_GROUP, PREFIX_WEEK, PREFIX_STATUS);

        String groupId = argMultimap.getValue(PREFIX_GROUP)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAllAttendanceCommand.MESSAGE_USAGE)))
                .trim();

        String weekRaw = argMultimap.getValue(PREFIX_WEEK)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAllAttendanceCommand.MESSAGE_USAGE)))
                .trim();

        int week;
        try {
            week = Integer.parseInt(weekRaw);
        } catch (NumberFormatException e) {
            throw new ParseException(MarkAllAttendanceCommand.MESSAGE_INVALID_WEEK);
        }
        if (week < 2 || week > 13) {
            throw new ParseException(MarkAllAttendanceCommand.MESSAGE_INVALID_WEEK);
        }

        String statusRaw = argMultimap.getValue(CliSyntax.PREFIX_STATUS)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAllAttendanceCommand.MESSAGE_USAGE)))
                .trim();

        String normalizedStatus = normalizeStatus(statusRaw);
        if (normalizedStatus == null) {
            throw new ParseException(MarkAllAttendanceCommand.MESSAGE_INVALID_STATUS);
        }

        return new MarkAllAttendanceCommand(groupId, week, normalizedStatus);
    }

    private String normalizeStatus(String statusRaw) {
        if (statusRaw == null) {
            return null;
        }
        String s = statusRaw.toLowerCase(Locale.ROOT).trim();
        // direct matches
        if (s.equals("present") || s.equals("absent") || s.equals("excused")) {
            return s;
        }
        // common shorthands
        switch (s) {
        case "p":
        case "pres":
            return "present";
        case "a":
        case "abs":
            return "absent";
        case "e":
        case "ex":
        case "exc":
            return "excused";
        default:
            return null;
        }
    }
}

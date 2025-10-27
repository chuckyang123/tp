package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WEEK;

import java.util.Locale;

import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new {@link MarkAttendanceCommand} object.
 * <p>
 * The expected input format is:
 * <pre>{@code
 * i/<nusnetId> w/<week> status/<present|absent|excused>
 * }</pre>
 * Week must be an integer between 2 and 13, and the status must be one of
 * "present", "absent", or "excused".
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * mark_attendance i/E1234567 w/3 status/present  // marks week 3 attendance for student E1234567 as present
 * }</pre>
 */
public class MarkAttendanceCommandParser implements Parser<MarkAttendanceCommand> {

    @Override
    public MarkAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_NUSNETID, PREFIX_WEEK, PREFIX_STATUS);
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NUSNETID, PREFIX_WEEK, PREFIX_STATUS);

        String nusnetId = argMultimap.getValue(PREFIX_NUSNETID)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAttendanceCommand.MESSAGE_USAGE)))
                .trim();

        String weekRaw = argMultimap.getValue(PREFIX_WEEK)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAttendanceCommand.MESSAGE_USAGE)))
                .trim();

        int week;
        try {
            week = Integer.parseInt(weekRaw);
        } catch (NumberFormatException e) {
            throw new ParseException(MarkAttendanceCommand.MESSAGE_INVALID_WEEK);
        }
        String statusRaw = argMultimap.getValue(CliSyntax.PREFIX_STATUS)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAttendanceCommand.MESSAGE_USAGE)))
                .trim();

        String normalizedStatus = normalizeStatus(statusRaw);
        if (normalizedStatus == null) {
            throw new ParseException(MarkAttendanceCommand.MESSAGE_INVALID_STATUS);
        }

        return new MarkAttendanceCommand(new Nusnetid(nusnetId), week, normalizedStatus);
    }

    private String normalizeStatus(String statusRaw) {
        if (statusRaw == null) {
            return null;
        }
        String s = statusRaw.toLowerCase(Locale.ROOT).trim();
        if (s.equals("present") || s.equals("absent") || s.equals("excused")) {
            return s;
        }
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

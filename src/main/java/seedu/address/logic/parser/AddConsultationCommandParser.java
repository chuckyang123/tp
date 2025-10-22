package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TO;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddConsultationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new AddConsultationCommand object
 */
public class AddConsultationCommandParser implements Parser<AddConsultationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ConsultationCommand
     * and returns a ConsultationCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddConsultationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NUSNETID, PREFIX_FROM, PREFIX_TO);

        if (!arePrefixesPresent(argMultimap, PREFIX_NUSNETID, PREFIX_FROM, PREFIX_TO)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddConsultationCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NUSNETID, PREFIX_FROM, PREFIX_TO);
        Nusnetid nusnetid = ParserUtil.parseNusnetid(argMultimap.getValue(PREFIX_NUSNETID).get());
        LocalDateTime from = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_FROM).get());
        LocalDateTime to = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_TO).get());

        if (!Consultation.isValidConsultation(from, to)) {
            throw new ParseException(Consultation.MESSAGE_CONSTRAINTS);
        }
        Consultation consultation = new Consultation(nusnetid, from, to);

        return new AddConsultationCommand(consultation);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

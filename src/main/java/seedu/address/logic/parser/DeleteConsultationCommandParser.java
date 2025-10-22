package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;

import java.util.stream.Stream;

import seedu.address.logic.commands.DeleteConsultationCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Nusnetid;

/**
 * Parses input arguments and creates a new DeleteConsultationCommand object
 */
public class DeleteConsultationCommandParser implements Parser<DeleteConsultationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteConsultationCommand
     * and returns a DeleteConsultationCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteConsultationCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NUSNETID);

        if (!arePrefixesPresent(argMultimap, PREFIX_NUSNETID)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteConsultationCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NUSNETID);
        Nusnetid nusnetid = ParserUtil.parseNusnetid(argMultimap.getValue(PREFIX_NUSNETID).get());

        return new DeleteConsultationCommand(nusnetid);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}

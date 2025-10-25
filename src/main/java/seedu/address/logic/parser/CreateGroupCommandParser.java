package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;

import seedu.address.logic.commands.CreateGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.GroupId;

/**
 * Parses input arguments and creates a new CreateGroupCommand object
 */
public class CreateGroupCommandParser implements Parser<CreateGroupCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the CreateGroupCommand
     * and returns a CreateGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CreateGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_GROUP);
        if (!argPrefixesPresent(argMultimap, PREFIX_GROUP)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    CreateGroupCommand.MESSAGE_USAGE));
        }
        String value = argMultimap.getValue(PREFIX_GROUP).get().trim();
        GroupId id = ParserUtil.parseGroupId(value);
        return new CreateGroupCommand(id);
    }

    private static boolean argPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return java.util.stream.Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

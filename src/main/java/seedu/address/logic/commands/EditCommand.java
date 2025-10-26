package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NUSNETID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Consultation;
import seedu.address.model.person.AttendanceSheet;
import seedu.address.model.person.Email;
import seedu.address.model.person.GroupId;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nusnetid;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit_student";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_NUSNETID + "NUSNETID] "
            + "[" + PREFIX_TELEGRAM + "TELEGRAM] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] " + "\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@u.nus.edu";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Person: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private static final Logger logger = LogsCenter.getLogger(EditCommand.class);

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        // First remove the old person's membership from their existing group (handles nusnetid or group changes)
        model.updateGroupWhenEditPersonId(personToEdit);

        // If the nusnetid changed, update any consultations that reference the old nusnetid
        if (!personToEdit.getNusnetid().equals(editedPerson.getNusnetid())) {
            Nusnetid oldId = personToEdit.getNusnetid();
            Nusnetid newId = editedPerson.getNusnetid();
            logger.info(String.format("Detected NUSNETID change: %s -> %s. Updating consultations.",
                    oldId.value, newId.value));
            model.updateConsultationsForEditedPerson(oldId, newId);
        }

        model.setPerson(personToEdit, editedPerson);
        logger.fine(() -> "Updated person in model: " + editedPerson.getNusnetid().value);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.updateGroupWhenAddPerson(editedPerson);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.phone == null
                ? null : editPersonDescriptor.getPhone().orElse(personToEdit.getPhone().orElse(null));
        Email updatedEmail = editPersonDescriptor.email == null
                ? null : editPersonDescriptor.getEmail().orElse(personToEdit.getEmail().orElse(null));
        Nusnetid updatedNusnetid = editPersonDescriptor.getNusnetid().orElse(personToEdit.getNusnetid());
        Telegram updatedTelegram = editPersonDescriptor.getTelegram().orElse(personToEdit.getTelegram());
        GroupId updatedGroupId = personToEdit.getGroupId();
        AttendanceSheet attendanceSheet = personToEdit.getAttendanceSheet();

        // Build Optional<Phone> and Optional<Email> following existing semantics:
        java.util.Optional<Phone> phoneOptional;
        if (updatedPhone == null) {
            phoneOptional = Optional.ofNullable(updatedPhone);
        } else {
            phoneOptional = java.util.Optional.ofNullable(updatedPhone);
        }
        java.util.Optional<Email> emailOptional;
        if (updatedEmail == null) {
            emailOptional = Optional.ofNullable(updatedEmail);
        } else {
            emailOptional = java.util.Optional.ofNullable(updatedEmail);
        }

        // Preserve and, if necessary, update the person's consultation to use the new nusnetid
        java.util.Optional<Consultation> consultationOptional = personToEdit.getConsultation()
                .map(oldConsult -> {
                    if (oldConsult.getNusnetid().equals(updatedNusnetid)) {
                        return oldConsult;
                    } else {
                        return new Consultation(updatedNusnetid, oldConsult.getFrom(), oldConsult.getTo());
                    }
                });

        return new Person(updatedName, phoneOptional, emailOptional, updatedNusnetid, updatedTelegram, updatedGroupId,
                personToEdit.getHomeworkTracker(), attendanceSheet, consultationOptional);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Nusnetid nusnetid;
        private Telegram telegram;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setNusnetid(toCopy.nusnetid);
            setTelegram(toCopy.telegram);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, nusnetid, telegram, phone, email)
                    || phone == null || email == null;
        }

        public void setName(Name name) {
            this.name = name;
        }
        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }
        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }
        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setNusnetid(Nusnetid nusnetid) {
            this.nusnetid = nusnetid;
        }
        public Optional<Nusnetid> getNusnetid() {
            return Optional.ofNullable(nusnetid);
        }

        public Optional<Telegram> getTelegram() {
            return Optional.ofNullable(telegram);
        }
        public void setTelegram(Telegram telegram) {
            this.telegram = telegram;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }
            EditPersonDescriptor o = (EditPersonDescriptor) other;
            return Objects.equals(name, o.name)
                    && Objects.equals(phone, o.phone)
                    && Objects.equals(email, o.email)
                    && Objects.equals(nusnetid, o.nusnetid)
                    && Objects.equals(telegram, o.telegram);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("nusnetid", nusnetid)
                    .add("telegram", telegram)
                    .toString();
        }
    }
}

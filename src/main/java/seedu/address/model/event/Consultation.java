package seedu.address.model.event;

import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Nusnetid;

/**
 * Represents a Consultation in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Consultation {
    public static final String MESSAGE_CONSTRAINTS = "Consultation end time must be after start time";
    private final Nusnetid nusnetid;
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Every field must be present and not null.
     */
    public Consultation(Nusnetid nusnetid, LocalDateTime from, LocalDateTime to) {
        requireAllNonNull(nusnetid, from, to);
        checkArgument(isValidConsultation(from, to), MESSAGE_CONSTRAINTS);
        this.nusnetid = nusnetid;
        this.from = from;
        this.to = to;
    }

    public Nusnetid getNusnetid() {
        return nusnetid;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromInString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
        return from.format(formatter);
    }

    public String getToInString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmm");
        return to.format(formatter);
    }

    /**
     * Returns true if the given start and end time constitutes a valid consultation.
     */
    public static boolean isValidConsultation(LocalDateTime from, LocalDateTime to) {
        return to.isAfter(from);
    }

    /**
     * Returns true if both consultations have the same start and end times.
     * This defines a weaker notion of equality between two consultations.
     */
    public boolean isSameConsultation(seedu.address.model.event.Consultation otherConsultation) {
        if (otherConsultation == this) {
            return true;
        }

        return otherConsultation != null
                && otherConsultation.getFrom().equals(getFrom())
                && otherConsultation.getTo().equals(getTo());
    }

    /**
     * Returns true if this consultation's timing overlaps with the other consultation's timing.
     */
    public boolean isOverlappingConsultation(Consultation otherConsultation) {
        return this.from.isBefore(otherConsultation.to) && otherConsultation.from.isBefore(this.to);
    }

    /**
     * Returns a string representation of the consultation time in the format
     * "Consultation: yyyy-MM-dd HH:mm to yyyy-MM-dd HH:mm".
     */
    public String showConsultationTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        final StringBuilder builder = new StringBuilder();
        builder.append("Consultation: ")
                .append(from.format(formatter))
                .append(" to ")
                .append(to.format(formatter));
        return builder.toString();
    }

    /**
     * Returns true if both consultations have the same nusnetid and start & end times.
     * This defines a stronger notion of equality between two consultations.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof seedu.address.model.event.Consultation)) {
            return false;
        }

        seedu.address.model.event.Consultation otherConsultation = (seedu.address.model.event.Consultation) other;
        return nusnetid.equals(otherConsultation.nusnetid)
                && from.equals(otherConsultation.from)
                && to.equals(otherConsultation.to);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(nusnetid, from, to);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("NUSnetid", nusnetid)
                .add("from", from)
                .add("to", to)
                .toString();
    }
}

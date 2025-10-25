package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.GroupId;

/**
 * A list of groups that enforces uniqueness between its elements and does not allow nulls.
 * A group is considered unique by comparing using {@code Group#isSameGroup(Group)}.
 * As such, adding and updating of groups uses Group#isSameGroup(Group) for equality
 * to ensure that the group being added or updated is unique in terms of identity
 * in the UniqueGroupList. However, the removal of a group uses Group#equals(Object
 */
public class UniqueGroupList implements Iterable<Group> {
    private final ObservableList<Group> internalList = FXCollections.observableArrayList();
    private final ObservableList<Group> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);
    /**
     * Returns true if the list contains an equivalent group as the given argument.
     * @param toCheck group to check
     * @return boolean
     */
    public boolean contains(GroupId toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(g -> g.isSameGroup(toCheck));
    }
    /**
     * Adds a group to the list.
     * @param toAdd group to add
     */
    public void add(Group toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd.getGroupId())) {
            throw new IllegalArgumentException("Duplicate group");
        }
        internalList.add(toAdd);
    }
    /**
     * Returns the Group with the given GroupId, or null if not found.
     * @param groupId to look for
     * @return Group or null
     */
    public Group getGroup(GroupId groupId) {
        requireNonNull(groupId);
        for (Group g : internalList) {
            if (g.getGroupId().equals(groupId)) {
                return g;
            }
        }
        return null;
    }
    /**
     * Sets the groups to the groups in the given list.
     * @param groups updated list of groups
     */
    public void setGroups(List<Group> groups) {
        requireNonNull(groups);
        long distinctCount = groups.stream().distinct().count();
        if (distinctCount != groups.size()) {
            throw new IllegalArgumentException("Groups list contains duplicate groups");
        }
        internalList.setAll(groups);
    }
    @Override
    public Iterator<Group> iterator() {
        return internalList.iterator();
    }
    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Group> asUnmodifiableObservableList() {
        return this.internalUnmodifiableList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof UniqueGroupList)) {
            return false;
        }
        UniqueGroupList otherList = (UniqueGroupList) other;
        return new HashSet<>(this.internalList)
                .equals(new HashSet<>(otherList.internalList));
    }

    @Override
    public int hashCode() {
        return new HashSet<>(internalList).hashCode();
    }
    @Override
    public String toString() {
        return internalList.toString();
    }
}

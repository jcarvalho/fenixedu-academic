package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import dml.runtime.RelationAdapter;

public class MeasurementTestShift extends MeasurementTestShift_Base {

    public static Comparator<MeasurementTestShift> COMPARATOR_BY_NAME = new Comparator<MeasurementTestShift>() {
	public int compare(MeasurementTestShift leftMeasurementTestShift, MeasurementTestShift rightMeasurementTestShift) {
	    int comparationResult = leftMeasurementTestShift.getName().compareTo(rightMeasurementTestShift.getName());
	    return (comparationResult == 0) ? leftMeasurementTestShift.getIdInternal().compareTo(
		    rightMeasurementTestShift.getIdInternal()) : comparationResult;
	}
    };

    static {
	MeasurementTestShiftMeasurementTest.addListener(new RelationAdapter<MeasurementTestShift, MeasurementTest>() {

	    @Override
	    public void beforeAdd(MeasurementTestShift toAdd, MeasurementTest test) {

		if (toAdd != null && test != null) {
		    if (test.getShiftByName(toAdd.getName()) != null) {
			throw new DomainException(
				"error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.already.contains.shift.with.same.name");

		    }
		}

	    }

	});
    }

    protected MeasurementTestShift() {
	super();

	super.setRootDomainObject(RootDomainObject.getInstance());
    }

    public MeasurementTestShift(String name, String date, MeasurementTest test) {
	this();

	check(name, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.name.cannot.be.null");
	check(date, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.date.cannot.be.null");
	check(test, "error.net.sourceforge.fenixedu.domain.candidacy.MeasurementTestShift.test.cannot.be.null");

	setDate(date);
	setName(name);
	setTest(test);
    }

    public MeasurementTestRoom getAvailableRoom() {
	for (final MeasurementTestRoom room : getSortedRooms()) {
	    if (room.isAvailable()) {
		return room;
	    }
	}

	return null;
    }

    public boolean hasAvailableRoom() {
	return getAvailableRoom() != null;
    }

    public SortedSet<MeasurementTestRoom> getSortedRooms() {
	final SortedSet<MeasurementTestRoom> result = new TreeSet<MeasurementTestRoom>(
		MeasurementTestRoom.COMPARATOR_BY_ROOM_ORDER);

	result.addAll(getRooms());

	return result;
    }

    public MeasurementTestRoom getRoomByName(String name) {
	for (final MeasurementTestRoom room : getRooms()) {
	    if (room.getName().equals(name)) {
		return room;
	    }
	}

	return null;
    }
}

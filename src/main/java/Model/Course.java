package Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class is responsbile for a class itself like CMPT.
 *
 */

public class Course implements Subject{
    private long courseId;
    private String catalogNumber;

    private List<CourseOfferings> courseOfferingsList;
    private List<Observer> observerList;

    public Course(long courseId, CourseData data) {
        this.courseId = courseId;
        this.catalogNumber = data.getCatalogNumber();
        this.courseOfferingsList = new ArrayList<>();
        this.observerList = new ArrayList<>();
    }


    // Adding
    /*
       We want to check if the course exists.
       If it already does exsists this means we just want to add a new section!

       If it does not exsists this means we want to create a whole new course create a section for that course then add it to the list of courses we offer.
     */

    public void addCourseOffering(CourseData courseData) {
        boolean courseExists = false;

        for (CourseOfferings currentOffering : courseOfferingsList) {
            if (currentOffering.getSemesterId() == courseData.getSemesterId()) {
                courseExists = true;
                currentOffering.addSection(courseData);
                break;
            }
        }

        if (!courseExists) {
            int id = courseOfferingsList.size() + 1;
            CourseOfferings newOfferedCourse = new CourseOfferings(courseId, id, courseData);
            newOfferedCourse.addSection(courseData);
            courseOfferingsList.add(newOfferedCourse);
            // Sort
        }
        // Create an CourseOffering object for the observer
        // NOTIFY OBSERVERS HERE
    }

    public CourseOfferings findCourseOfferings(CourseData courseData) {
        for (CourseOfferings currentCourseOfferings : courseOfferingsList) {
            if (currentCourseOfferings.getSemesterId() == courseData.getSemesterId()) {
                return currentCourseOfferings;
            }
        }
        // Throw error here
        return null;
    }

    private void sortCourseOfferings() {
        courseOfferingsList.sort(Comparator.comparing(o -> o.getSemesterId() + o.getLocation()));
    }

    public int getTotalEnrollmentInSemester(int semesterId) {
        int totalEnrollmentInSemester = 0;
        for (CourseOfferings currentCourseOfferings : courseOfferingsList) {
            if (currentCourseOfferings.getSemesterId() == semesterId) {
                if (currentCourseOfferings.getSemesterId() == semesterId) {
                    totalEnrollmentInSemester += currentCourseOfferings.getTotalEnrollments();
                }
            }
        }
        return totalEnrollmentInSemester;
    }

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {

    }

    @Override
    public void notifyObservers(CourseOfferings offerings, CourseData data) {
        observerList.forEach(obs -> obs.changedState(offerings, data));
    }

    public long getCourseId() {
        return courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public List<CourseOfferings> getCourseOfferingsList() {
        return courseOfferingsList;
    }

    public List<Observer> getObserverList() {
        return observerList;
    }
}

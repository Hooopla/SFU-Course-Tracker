package Model;

import java.util.Observer;

/**
 * Used so we can observe the classes that implements this
*/

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(CourseOfferings offerings, CourseData data);

}
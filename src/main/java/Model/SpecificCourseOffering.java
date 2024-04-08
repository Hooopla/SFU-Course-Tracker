package Model;

public class SpecificCourseOffering {
    private String catalogNumber;
    private long courseID;

    public SpecificCourseOffering(String catalogNumber, long courseID) {
        this.catalogNumber = catalogNumber;
        this.courseID = courseID;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public long getCourseID() {
        return courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }
}

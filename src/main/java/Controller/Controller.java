package Controller;

import Model.*;
import Model.Exception.CourseNotFound;
import Model.Exception.DepartmentNotFound;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {
    private List<Department> departmentList = new ArrayList<>();
    private Authors authors = new Authors();
    ModelLoader loader = new ModelLoader("data/course_data_2018.csv", departmentList);

    // About Us Page | @GetMapping
    @GetMapping("/about")
    public Authors getAboutPage() {
        return authors;
    }


    // Get List of Departments | @GetMapping
    // Comments : Works!
    @GetMapping("/departments")
    public List<Department> getDepartmentList() {
        return departmentList;
    }
    // Get One Specific Department | @GetMapping
    // Comment: Works!
    @GetMapping("/departments/{departmentName}/courses")
    public List<Course> getDepartment(@PathVariable("departmentName") String departmentName) {
        for (Department department : departmentList) {
            if (department.getDepartmentName().trim().equals(departmentName)) {
                return department.getCourseList();
            }
        }
        throw new DepartmentNotFound("Department: " + departmentName + " was not retrieved.");
    }

    // Get Total Amount of Students in that department each Semester | @GetMapping
    // Comment: I need to watch the video again as I am not sure if this is going to be used in the graph or not.

    // Get List of Course | @GetMapping
    // Comment: Works!
    @GetMapping("/departments/{departmentName}/courses/{courseId}/offerings")
    public List<CourseOfferings> getCourseOfferings(
            @PathVariable("departmentName") String departmentName,
            @PathVariable("courseId") long courseId) {
        boolean DepartmentFound = false;
        for (Department department : departmentList) {
            if (department.getDepartmentName().trim().equals(departmentName)) {
                DepartmentFound = true;
                for (Course course : department.getCourseList()) {
                    if (course.getCourseId() == courseId) {
                        return course.getCourseOfferingsList();
                    }
                }
            }
        }
        if (DepartmentFound == false) {
            throw new DepartmentNotFound("Department: " + departmentName + " was not retrieved.");
        }
        throw new CourseNotFound("Department: " + departmentName + "Course: " + courseId + " was not retrieved.");
    }

    // Get One Specific Course | @GetMapping
    // Comment:
    @GetMapping("/departments/{departmentName}/courses/{courseId}/offerings/{offeringId}/Sections")
    public List<Section> getSection(
            @PathVariable("departmentName") String departmentName,
            @PathVariable("courseId") long courseId,
            @PathVariable("offeringId") long courseOfferingId) {
        boolean DepartmentFound = false;
        boolean CourseFound = false;
        for (Department department : departmentList) {
            if (department.getDepartmentName().trim().equals(departmentName)) {
                DepartmentFound = true;
                for (Course course : department.getCourseList()) {
                    if (course.getCourseId() == courseId) {
                        CourseFound = true;
                    }
                    for (CourseOfferings courseOfferings :  course.getCourseOfferingsList()) {
                        if (courseOfferings.getCourseOfferingsId() == courseOfferingId) {
                            return courseOfferings.getSectionList();
                        }
                    }
                }
            }
        }
        return null;
    }

    // Get List of Course Offering | @GetMapping
    // Comment:

    // Get One Specific Course Offering | @GetMapping
    // Comment:


    // Get List of Sections @GetMapping | @GetMapping
    // Comment:

    // Get One Specific Section @GetMapping | @GetMapping
    // Comment:

    // HELLO ALEX IF U SEE THIS ANYTHING BELOW THIS IS FUNCTION THAT WE MAY NEED TO MAKE? I just need to rewatch the video or if u know u can get rid of it as needed.
    // AddCourseOffering??
    // AddCourse??
    // AddSection??

    // Get Dump Model | @GetMapping
    // Comment: Works Great!
    @GetMapping("dump-model")
    private void printToConsole() {
        DumpWriter.dumpModel(departmentList);
    }
}

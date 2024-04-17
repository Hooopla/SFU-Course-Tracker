package Controller;

import Model.*;
import Model.DTO.APICourseOfferingDTO;
import Model.DTO.WatcherDTO;
import Model.Exception.CourseNotFound;
import Model.Exception.CourseOfferingsNotFound;
import Model.Exception.DepartmentNotFound;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {
    private List<Department> departmentList = new ArrayList<>();
    private Authors authors = new Authors();
    ModelLoader loader = new ModelLoader("data/course_data_2018.csv", departmentList);
    List<WatcherDTO> watcherDTOList = new ArrayList<>();

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
    // Department -> Get CourseList | @GetMapping
    // Comment: Works!
    @GetMapping("/departments/{departmentName}/courses")
    public List<Course> getDepartment(@PathVariable("departmentName") String departmentName) {
        for (Department department : departmentList) {
            if (department.getName().trim().equals(departmentName)) {
                return department.getCourseList();
            }
        }
        throw new DepartmentNotFound("Department: " + departmentName + " was not retrieved.");
    }

    // Get Total Amount of Students in that department each Semester | @GetMapping
    // Comment: I need to watch the video again as I am not sure if this is going to be used in the graph or not.

    // Department -> Course -> Get CourseOfferingsList | @GetMapping
    // Comment: Works!
    @GetMapping("/departments/{departmentName}/courses/{courseId}/offerings")
    public List<CourseOfferings> getCourseOfferings(
            @PathVariable("departmentName") String departmentName,
            @PathVariable("courseId") long courseId) {
        boolean DepartmentFound = false;
        for (Department department : departmentList) {
            if (department.getName().trim().equals(departmentName)) {
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
        throw new CourseNotFound("Department: " + departmentName + " CourseId: " + courseId + " was not retrieved. ");
    }

    // Department -> Course -> CourseOfferings -> Get SectionList | @GetMapping
    // Comment: Works!!
    @GetMapping("/departments/{departmentName}/courses/{courseId}/offerings/{courseOfferingsId}")
    public List<Section> getSections(
            @PathVariable("departmentName") String departmentName,
            @PathVariable("courseId") long courseId,
            @PathVariable("courseOfferingsId") long offeringId) {
        boolean DepartmentFound = false;
        boolean CourseFound = false;
        for (Department department : departmentList) {
            System.out.println("uhmm");
            if (department.getName().trim().equals(departmentName)) {
                DepartmentFound = true;
                System.out.println("yoo 1");
                for (Course course : department.getCourseList()) {
                    if (course.getCourseId() == courseId) {
                        CourseFound = true;
                        System.out.println("yoo 2");
                    }
                    for (CourseOfferings courseOfferings :  course.getCourseOfferingsList()) {
                        if (courseOfferings.getCourseOfferingId() == offeringId) {
                            System.out.println("yooo 3");
                            return courseOfferings.getSectionList();
                        }
                    }
                }
            }
        }
        if (DepartmentFound == false) {
            throw new DepartmentNotFound("Department: " + departmentName + " was not retrieved.");
        }
        if (CourseFound == false) {
            throw new CourseNotFound("Department: " + departmentName + " CourseId: " + courseId + " was not retrieved.");
        }
        throw new CourseOfferingsNotFound("Department: " + departmentName + "CourseId: " + courseId + "CourseOfferings: " + offeringId + " was not retrieved.");
    }

    // HELLO ALEX IF U SEE THIS ANYTHING BELOW THIS IS FUNCTION THAT WE MAY NEED TO MAKE? I just need to rewatch the video or if u know u can get rid of it as needed.
    // AddCourseOffering??
    // AddCourse??
    // AddSection??

    @PostMapping("addoffering")
    private void addCourseOffering(@RequestBody APICourseOfferingDTO dto) {
        CourseData data = dto.getCourseData();

        for (Department department: departmentList) {
            if (department.getName().equals(dto.getSubjectName())) {
                department.addCourse(data);
            }
        }
    }

    @GetMapping("watchers")
    private List<WatcherDTO> getWatchers() {
        watcherDTOList.clear();

        for (Department department: departmentList) {
            for (Course course: department.getCourseList()) {
                for (Observer watcher: course.getObserverList()) {
                    WatcherDTO dto = new WatcherDTO(
                            watcherDTOList.size() + 1,
                            department,
                            course,
                            watcher.getEvents()
                    );
                    watcherDTOList.add(dto);
                }
            }
        }
        return watcherDTOList;
    }

    @PostMapping("watchers")
    private void createWatcher(@RequestBody Map<String, Object> body) {
        String bodyDep = (String) body.get("deptId");
        long bodyCourseId = Long.parseLong(body.get("courseId").toString());
        WatcherInformation watcher = new WatcherInformation(
                bodyDep,
                bodyCourseId
        );

//        Course course = getCourseFromDepartmentCourseId(bodyDep, bodyCourseId);
//        course.addObserver(watcher);
        for (Department department: departmentList) {
            if (department.getName().equals(bodyDep)) {
                for (Course course: department.getCourseList()) {
                    if (course.getCourseId() == bodyCourseId) {
                        System.out.println("Adding Observer for " + course.getCourseId() + " " + bodyCourseId);
                        course.addObserver(watcher);
                    }
                }
            }
        }
    }

    // Get Dump Model | @GetMapping
    // Comment: Works Great!
    @GetMapping("dump-model")
    private void printToConsole() {
        DumpWriter.dumpModel(departmentList);
    }

    private Course getCourseFromDepartmentCourseId(String departmentName, long courseId) {
        for (Department department: departmentList) {
            if (department.getName().equals(departmentName)) {
                for (Course course: department.getCourseList()) {
                    if (course.getCourseId() == courseId) {
                        return course;
                    }
                }
            }
        }
        throw new CourseNotFound();
    }
}

package com.demo;

import com.demo.domain.entity.college.College;
import com.demo.domain.entity.college.CollegeCourse;
import com.demo.domain.entity.college.CollegeDepartment;
import com.demo.domain.entity.college.CollegeDivision;
import com.demo.domain.entity.course.Course;
import com.demo.domain.entity.user.User;
import com.demo.domain.payload.request.*;
import com.demo.domain.service.college.CollegeService;
import com.demo.domain.service.course.CourseService;
import com.demo.domain.service.user.UserService;
import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppDataSetter {
    private final UserService userService;
    private final CourseService courseService;
    private final CollegeService collegeService;

    @Order(value = 1)
    @EventListener(value = ApplicationReadyEvent.class)
    public void read() {
        readCollegeFile("src/main/resources/data/college.csv");
        readCourseFile("src/main/resources/data/course.csv");
        readUserFile("src/main/resources/data/user.csv");
    }

    private void readCollegeFile(String filePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            reader.readNext();
            String[] data;

            while ((data = reader.readNext()) != null) {
                College college = null;
                CollegeDivision collegeDivision = null;
                CollegeDepartment collegeDepartment = null;
                CollegeCourse collegeCourse = null;

                System.out.println(Arrays.toString(data));

                if (!data[0].isBlank() && !data[1].isBlank()) {
                    college = getOrCreateCollege(CollegeDto.of(data[1], data[0]));
                }
                if (!data[2].isBlank() && !data[3].isBlank()) {
                    collegeDivision = getOrCreateCollegeDivision(college, CollegeDivisionDto.of(data[3], data[2]));
                }
                if (!data[4].isBlank() && !data[5].isBlank()) {
                    collegeDepartment = getOrCreateCollegeDepartment(college, CollegeDepartmentDto.of(data[5], data[4]));
                }
                if (!data[6].isBlank() && !data[7].isBlank()) {
                    collegeCourse = getOrCreateCollegeCourse(college, collegeDivision, collegeDepartment, CollegeCourseDto.of(data[7], data[6]));
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.info(e.getMessage());
        }
    }

    private void readCourseFile(String filePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            reader.readNext();
            String[] data;

            while ((data = reader.readNext()) != null) {
                College college = null;
                CollegeDivision collegeDivision = null;
                CollegeDepartment collegeDepartment = null;
                CollegeCourse collegeCourse = null;
                Course course = null;

                System.out.println(Arrays.toString(data));

                if (!data[2].isBlank() && !data[3].isBlank()) {
                    college = getCollege(CollegeDto.of(data[3], data[2]));
                }
                if (!data[4].isBlank() && !data[5].isBlank()) {
                    collegeDivision = getCollegeDivision(CollegeDivisionDto.of(data[5], data[4]));
                }
                if (!data[6].isBlank() && !data[7].isBlank()) {
                    collegeDepartment = getCollegeDepartment(CollegeDepartmentDto.of(data[7], data[6]));
                }
                if (!data[8].isBlank() && !data[9].isBlank()) {
                    collegeCourse = getCollegeCourse(CollegeCourseDto.of(data[9], data[8]));
                }
                if (!data[10].isBlank() && !data[11].isBlank() && !data[12].isBlank() && !data[13].isBlank() && !data[15].isBlank()) {
                    course = getOrCreateCourse(college, collegeDivision, collegeDepartment, collegeCourse, CourseDto.of(data[10], data[12], data[13], data[0], data[1], Integer.valueOf(data[11]), Integer.valueOf(data[15])));
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.info(e.getMessage());
        }
    }

    private void readUserFile(String filePath) {
        try {
            CSVReader reader = new CSVReader(new FileReader(filePath));
            reader.readNext();
            String[] data;

            while ((data = reader.readNext()) != null) {
                User user = null;

                System.out.println(Arrays.toString(data));

                if (!data[0].isBlank() && !data[1].isBlank() && !data[2].isBlank() && !data[3].isBlank() && !data[4].isBlank()) {
                    user = getOrCreateUser(UserDto.of(data[1], data[0], data[2], Integer.valueOf(data[3]), Integer.valueOf(data[4])));
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.info(e.getMessage());
        }
    }

    private College getOrCreateCollege(CollegeDto collegeDto) {
        return collegeService.getCollege(collegeDto.getCollegeName(), collegeDto.getCollegeNumber()).orElseGet(() -> collegeService.createCollege(collegeDto));
    }

    private College getCollege(CollegeDto collegeDto) {
        return collegeService.getCollege(collegeDto.getCollegeName(), collegeDto.getCollegeNumber()).orElseThrow(() -> ApiException.of404(ApiExceptionType.COLLEGE_NOT_EXISTED));
    }

    private CollegeDivision getOrCreateCollegeDivision(College college, CollegeDivisionDto collegeDivisionDto) {
        return collegeService.getCollegeDivision(collegeDivisionDto.getCollegeDivisionName(), collegeDivisionDto.getCollegeDivisionNumber()).orElseGet(() -> collegeService.createCollegeDivision(college, collegeDivisionDto));
    }

    private CollegeDivision getCollegeDivision(CollegeDivisionDto collegeDivisionDto) {
        return collegeService.getCollegeDivision(collegeDivisionDto.getCollegeDivisionName(), collegeDivisionDto.getCollegeDivisionNumber()).orElseThrow(() -> ApiException.of404(ApiExceptionType.COLLEGE_DIVISION_NOT_EXISTED));
    }

    private CollegeDepartment getOrCreateCollegeDepartment(College college, CollegeDepartmentDto collegeDepartmentDto) {
        return collegeService.getCollegeDepartment(collegeDepartmentDto.getCollegeDepartmentName(), collegeDepartmentDto.getCollegeDepartmentNumber()).orElseGet(() -> collegeService.createCollegeDepartment(college, collegeDepartmentDto));
    }

    private CollegeDepartment getCollegeDepartment(CollegeDepartmentDto collegeDepartmentDto) {
        return collegeService.getCollegeDepartment(collegeDepartmentDto.getCollegeDepartmentName(), collegeDepartmentDto.getCollegeDepartmentNumber()).orElseThrow(() -> ApiException.of404(ApiExceptionType.COLLEGE_DEPARTMENT_NOT_EXISTED));
    }

    private CollegeCourse getOrCreateCollegeCourse(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourseDto collegeCourseDto) {
        return collegeService.getCollegeCourse(collegeCourseDto.getCollegeCourseName(), collegeCourseDto.getCollegeCourseNumber()).orElseGet(() -> collegeService.createCollegeCourse(college, collegeDivision, collegeDepartment, collegeCourseDto));
    }

    private CollegeCourse getCollegeCourse(CollegeCourseDto collegeCourseDto) {
        return collegeService.getCollegeCourse(collegeCourseDto.getCollegeCourseName(), collegeCourseDto.getCollegeCourseNumber()).orElseThrow(() -> ApiException.of404(ApiExceptionType.COLLEGE_COURSE_NOT_EXISTED));
    }

    private Course getOrCreateCourse(College college, CollegeDivision collegeDivision, CollegeDepartment collegeDepartment, CollegeCourse collegeCourse, CourseDto courseDto) {
        return courseService.getCourse(courseDto.getCourseNumber()).orElseGet(() -> courseService.createCourse(college, collegeDivision, collegeDepartment, collegeCourse, courseDto));
    }

    private User getOrCreateUser(UserDto userDto) {
        return userService.getUser(userDto.getUserNumber()).orElseGet(() -> userService.createUser(userDto));
    }
}
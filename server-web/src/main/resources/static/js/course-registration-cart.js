const userCourseIds = [];

document.addEventListener("DOMContentLoaded", function (event) {

  /**
   * 사용자 조회
   */
  course.onGetUser().then();

  /**
   * 예비 수강 신청 조회
   */
  course.onGetCourseRegistrationCarts().then();

  /**
   * 이벤트 등록
   */
  document.addEventListener("click", function (event) {

    /**
     * 예비 수강 신청 이벤트 등록
     */
    if (event.target.classList.contains("course-registration-cart-create-button")) {
      const courseId = event.target.closest("tr").getAttribute("data-key");
      course.onCreateCourseRegistrationCart(courseId).then(() => location.reload());
    }

    /**
     * 예비 수강 신청 취소 이벤트 등록
     */
    if (event.target.classList.contains("course-registration-cart-delete-button")) {
      const courseId = event.target.closest("tr").getAttribute("data-key");
      course.onDeleteCourseRegistrationCart(courseId).then(() => location.reload());
    }
  });

  /**
   * 강의 페이지 이동 이벤트 등록
   */
  document.querySelector(".js.course-button")?.addEventListener("click", function (event) {
    course.onRouteCoursePage();
  });

  /**
   * 수강 신청 페이지 이동 이벤트 등록
   */
  document.querySelector(".js.course-registration-button")?.addEventListener("click", function (event) {
    course.onRouteCourseRegistrationPage();
  });

  /**
   * 예비 수강 신청 페이지 이동 이벤트 등록
   */
  document.querySelector(".js.course-registration-cart-button")?.addEventListener("click", function (event) {
    course.onRouteCourseRegistrationCartPage();
  });

  /**
   * 사용자 로그인 페이지 이동 이벤트 등록
   */
  document.querySelector(".js.user-logout-button")?.addEventListener("click", function (event) {
    course.onRouteUserLoginPage();
  });

  /**
   * 담당 대학 선택 이벤트 등록
   */
  document.querySelector(".js.college-select")?.addEventListener("change", function (event) {
    const collegeId = document.querySelector(".js.college-select").options[document.querySelector(".js.college-select").selectedIndex].value;

    course.onClearSelectCollegeDivision();
    course.onClearSelectCollegeDepartment();
    course.onSelectCollegeDivision(collegeId);
  });

  /**
   * 담당 학부 선택 이벤트 등록
   */
  document.querySelector(".js.college-division-select")?.addEventListener("change", function (event) {
    const collegeId = document.querySelector(".js.college-select").options[document.querySelector(".js.college-select").selectedIndex].value;
    const collegeDivisionId = document.querySelector(".js.college-division-select").options[document.querySelector(".js.college-division-select").selectedIndex].value;

    course.onClearSelectCollegeDepartment();
    course.onSelectCollegeDepartment(collegeId, collegeDivisionId);
  });

  /**
   * 담당 학과 선택 이벤트 등록
   */
  document.querySelector(".js.college-department-select")?.addEventListener("change", function (event) {
    // ...
  });

  /**
   * 강의 선택 검색 이벤트 등록
   */
  document.querySelector(".js.search-button")?.addEventListener("click", function (event) {
    course.onClearCourses();
    course.onSearchCourse().then();
  });

  /**
   * 강의 다시 검색 이벤트 등록
   */
  document.querySelector(".js.search-clear-button")?.addEventListener("click", function () {
    // location.replace("/course");
    course.onClearCourses();
    course.onClearSelectCourseYear();
    course.onClearSelectCourseSemester();
    course.onClearSelectCourseType();
    course.onClearSelectCollege();
    course.onClearSelectCollegeDivision();
    course.onClearSelectCollegeDepartment();
    course.onClearSelectOptionEmpty(document.querySelector(".js.college-division-select"));
    course.onClearSelectOptionEmpty(document.querySelector(".js.college-department-select"));
  });
});

const course = {

  /**
   * 강의 페이지 이동
   */
  onRouteCoursePage: function () {
    location.replace("/course");
  },

  /**
   * 수강 신청 페이지 이동
   */
  onRouteCourseRegistrationPage: function () {
    location.replace("/course/registration");
  },

  /**
   * 예비 수강 신청 페이지 이동
   */
  onRouteCourseRegistrationCartPage: function () {
    location.replace("/course/registration/cart");
  },

  /**
   * 사용자 로그인 페이지 이동
   */
  onRouteUserLoginPage: function () {
    client.usePostByUserLogout();
    location.replace("/");
  },

  /**
   * 강의 검색 결과 테이블 초기화 처리
   */
  onClearCourses: function () {
    document.querySelector(".js.course-search-details-table-content").replaceChildren();
  },

  /**
   * 수강 신청 테이블 초기화 처리
   */
  onClearCourseRegistrations: function () {
    document.querySelector(".js.course-registration-details-table-content").replaceChildren();
  },

  /**
   * 예비 수강 신청 테이블 초기화 처리
   */
  onClearCourseRegistrationCarts: function () {
    document.querySelector(".js.course-registration-cart-details-table-content").replaceChildren();
  },

  /**
   * 개설 연도 선택 초기화
   */
  onClearSelectCourseYear: function () {
    document.querySelector(".js.course-year-select").options[1].selected = true;
  },

  /**
   * 개설 학기 선택 초기화
   */
  onClearSelectCourseSemester: function () {
    document.querySelector(".js.course-semester-select").options[1].selected = true;
  },

  /**
   * 강의 구분 선택 초기화
   */
  onClearSelectCourseType: function () {
    document.querySelector(".js.course-type-select").options[0].selected = true;
  },

  /**
   * 담당 대학 선택 초기화
   */
  onClearSelectCollege: function () {
    document.querySelector(".js.college-select").options[0].selected = true;
  },

  /**
   * 담당 학부 선택 초기화
   */
  onClearSelectCollegeDivision: function () {
    document.querySelector(".js.college-division-select").options.length = 0;
  },

  /**
   * 담당 학과 선택 초기화
   */
  onClearSelectCollegeDepartment: function () {
    document.querySelector(".js.college-department-select").options.length = 0;
  },

  /**
   * 선택 옵션 초기화
   */
  onClearSelectOption: function (map, target) {
    if (map?.size === 0) {
      target.insertAdjacentHTML("beforeend", `<option value="">-</option>`);
    } else if (map?.size > 0) {
      target.insertAdjacentHTML("beforeend", `<option value="">선택</option>`);
      map.forEach((value, key) => {
        target.insertAdjacentHTML("beforeend", `<option value="${key}">${value}</option>`);
      });
    }
  },

  /**
   * 선택 옵션 초기화
   */
  onClearSelectOptionEmpty: function (target) {
    target.insertAdjacentHTML("beforeend", `<option value="">-</option>`);
  },

  /**
   * 담당 학부 선택 처리
   */
  onSelectCollegeDivision: function (collegeId) {
    const collegeDivisionMap = new Map();
    const collegeDepartmentMap = new Map();

    if (collegeId === "1") {
      // ...
    } else if (collegeId === "2") {
      collegeDivisionMap.set("1", "어문학부");
    } else if (collegeId === "3") {
      collegeDivisionMap.set("2", "경영학부");
    } else if (collegeId === "4") {
      collegeDepartmentMap.set("5", "경제학과");
      collegeDepartmentMap.set("6", "행정학과");
      collegeDepartmentMap.set("7", "디지털미디어학과");
    } else if (collegeId === "5") {
      collegeDivisionMap.set("3", "소프트웨어학부");
    }

    course.onClearSelectOption(collegeDivisionMap, document.querySelector(".js.college-division-select"));
    course.onClearSelectOption(collegeDepartmentMap, document.querySelector(".js.college-department-select"));
  },

  /**
   * 담당 학과 선택 처리
   */
  onSelectCollegeDepartment: function (collegeId, collegeDivisionId) {
    const collegeDepartmentMap = new Map();

    if (collegeId === "1" && collegeDivisionId === "") {
      // ...
    } else if (collegeId === "2" && collegeDivisionId === "1") {
      collegeDepartmentMap.set("1", "국어국문학과");
      collegeDepartmentMap.set("2", "영어국문학과");
    } else if (collegeId === "3" && collegeDivisionId === "2") {
      collegeDepartmentMap.set("3", "경영학과");
      collegeDepartmentMap.set("4", "경영정보학과");
    } else if (collegeId === "4" && collegeDivisionId === "") {
      // ...
    } else if (collegeId === "5" && collegeDivisionId === "3") {
      collegeDepartmentMap.set("8", "소프트웨어학과");
      collegeDepartmentMap.set("9", "데이터사이언스학과");
    }

    course.onClearSelectOption(collegeDepartmentMap, document.querySelector(".js.college-department-select"));
  },

  /**
   * 강의 선택 검색 처리
   */
  onSearchCourse: async function () {
    const courseYear = document.querySelector(".js.course-year-select").options[document.querySelector(".js.course-year-select").selectedIndex].value;
    const courseSemester = document.querySelector(".js.course-semester-select").options[document.querySelector(".js.course-semester-select").selectedIndex].value;
    const courseType = document.querySelector(".js.course-type-select").options[document.querySelector(".js.course-type-select").selectedIndex].value;
    const collegeId = document.querySelector(".js.college-select").options[document.querySelector(".js.college-select").selectedIndex].value;
    const collegeDivisionId = document.querySelector(".js.college-division-select").options[document.querySelector(".js.college-division-select").selectedIndex].value;
    const collegeDepartmentId = document.querySelector(".js.college-department-select").options[document.querySelector(".js.college-department-select").selectedIndex].value;

    if (courseYear === "" || courseSemester === "" || courseType === "" || collegeId === "") {
      alert("선택 조건을 확인하세요.");
      return;
    }

    const { code, result } = await client.useGetByCourses(courseYear, courseSemester, courseType, collegeId, collegeDivisionId, collegeDepartmentId);

    if (code !== "+100") {
      alert("강의를 조회하지 못하였습니다.");
      return;
    }

    if (result.length === 0) {
      alert("조회된 강의가 없습니다.");
      return;
    }

    result.forEach((item, i) => {
      const isUserCourseId = userCourseIds.indexOf(item.courseId);
      document.querySelector(".js.course-search-details-table-content").insertAdjacentHTML("beforeend", `
        <tr data-key="${item.courseId}">
          <td>${item.collegeName}</td>
          <td>${item.collegeDivisionName}</td>
          <td>${item.collegeDepartmentName}</td>
          <td>${item.collegeCourseName}</td>
          <td>${item.courseType}</td>
          <td>${item.courseCredit}</td>
          <td>${item.courseNumber}</td>
          <td>${item.courseTimetable}</td>
          <td>${item.courseRegistrationCountLimit}</td>
          <td>${item.courseRegistrationCountCart}</td>
          ${isUserCourseId === -1
            ? `<td><button class="course-registration-cart-create-button js" type="button">신청</button></td>`
            : `<td><button class="course-registration-cart-create-button js" type="button" disabled>신청완료</button></td>`
          }
        </tr>
      `);
    });
  },

  /**
   * 사용자 조회 처리
   */
  onGetUser: async function () {
    const {code, result } = await client.useGetByUser();

    if (code !== "+100") {
      alert("사용자를 조회하지 못하였습니다.");
      return;
    }

    localStorage.setItem("user_id", result.userId);
    document.querySelector(".js.course-registration-user-table-content").insertAdjacentHTML("beforeend", `
      <tr>
        <td>${result.userNumber}</td>
        <td>${result.userName}</td>
        <td>${result.userRegistrationCredit}</td>
        <td>${result.userRegistrationCreditLeft}</td>
      </tr>
    `);
  },

  /**
   * 예비 수강 신청 조회 처리
   */
  onGetCourseRegistrationCarts: async function () {
    course.onClearCourseRegistrationCarts();

    const { code, result } = await client.useGetByCourseRegistrationCarts();

    if (code !== "+100") {
      alert("예비 수강 신청 내역을 조회하지 못하였습니다.");
      return;
    }

    if (result.length !== 0) {
      result.forEach((item, i) => {
        userCourseIds.push(item.courseId);
        document.querySelector(".js.course-registration-cart-details-table-content").insertAdjacentHTML("beforeend", `
          <tr data-key="${item.courseId}">
            <td>${item.collegeName}</td>
            <td>${item.collegeDivisionName}</td>
            <td>${item.collegeDepartmentName}</td>
            <td>${item.collegeCourseName}</td>
            <td>${item.courseType}</td>
            <td>${item.courseCredit}</td>
            <td>${item.courseNumber}</td>
            <td>${item.courseTimetable}</td>
            <td>${item.courseRegistrationCountLimit}</td>
            <td>${item.courseRegistrationCountCart}</td>
            <td><button class="course-registration-cart-delete-button js" type="button">신청취소</button></td>
          </tr>
        `);
      });
    }
  },

  /**
   * 예비 수강 신청 처리
   */
  onCreateCourseRegistrationCart: async function (courseId) {
    const { code } = await client.usePostByCourseRegistrationCart(courseId);

    if (code === "+100") {
      alert("신청되었습니다.");
    } else if (code === "---") {
      alert("이미 신청된 강의입니다.");
    } else {
      alert("오류가 발생하였습니다.");
    }
  },

  /**
   * 예비 수강 신청 취소 처리
   */
  onDeleteCourseRegistrationCart: async function (courseId) {
    const { code } = await client.useDeleteByCourseRegistrationCart(courseId);

    if (code === "+100") {
      alert("신청 취소되었습니다.");
    } else {
      alert("오류가 발생하였습니다.");
    }
  },
}
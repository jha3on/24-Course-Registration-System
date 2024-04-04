const api = {

  /**
   * 사용자 조회
   */
  useGetByUser: async function () {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`http://localhost:8000/api/users/me`, {
        headers: {
          Authorization: token,
        },
      });
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  /**
   * 사용자 로그인
   */
  usePostByUserLogin: async function (userNumber, userPassword) {
    try {
      const response = await axios.post(`http://localhost:8000/api/users/login`, {
        userNumber: userNumber,
        userPassword: userPassword
      });
      localStorage.setItem("token", response.headers.get("Authorization"));
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  /**
   * 사용자 로그아웃
   */
  usePostByUserLogout: function () {
    localStorage.removeItem("token");
  },








  /**
   * 수강 신청 목록 조회
   */
  useGetByCourseRegistration: async function () {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`http://localhost:8000/api/courses/registrations`, {
        headers: {
          Authorization: token,
        },
      });
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  /**
   * 수강 신청
   */
  usePostByCourseRegistration: async function (courseId) {
    const token = localStorage.getItem("token");
  },

  /**
   * 수강 신청 취소
   */
  useDeleteByCourseRegistrationCancel: async function (courseId) {
    const token = localStorage.getItem("token");
  },

  /**
   * 예비 수강 신청 조회
   */
  useGetByCourseRegistrationCart: async function () {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`http://localhost:8000/api/courses/registrations/carts`, {
        headers: {
          Authorization: token,
        },
      });
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  /**
   * 예비 수강 신청
   */
  usePostByCourseRegistrationCart: async function (courseId) {
    try {
      const token = localStorage.getItem("token");
      console.log(token);
      const response = await axios.post(`http://localhost:8000/api/courses/registrations/carts`, null, {
        params: {
          courseId: courseId
        },
        headers: {
          Authorization: token,
        },
      });
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  /**
   * 예비 수강 신청 취소
   */
  useDeleteByCourseRegistrationCartCancel: async function (courseId) {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.delete(`http://localhost:8000/api/courses/registrations/carts`, {
        params: {
          courseId: courseId
        },
        headers: {
          Authorization: token,
        },
      });
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  /**
   * 강의 목록 조회
   */
  useGetByCourseList: async function (
    courseYear, courseSemester, courseType,
    collegeId, collegeDivisionId, collegeDepartmentId
  ) {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get(`http://localhost:8000/api/courses`, {
        params: {
          courseYear: courseYear,
          courseSemester: courseSemester,
          courseType: courseType,
          collegeId: collegeId,
          collegeDivisionId: collegeDivisionId,
          collegeDepartmentId: collegeDepartmentId
        },
        headers: {
          Authorization: token,
        },
      });
      return api.setResponse(response);
    } catch (error) {
      return api.setResponseError(error.response);
    }
  },

  // set
  // setError



  /**
   * API 응답 처리
   * @param response
   */
  setResponse: function (response) {
    return api.toObject(response.data);
  },

  /**
   * API 응답 오류 처리
   * @param response (error.response)
   */
  setResponseError: function (response) {
    if (response === undefined) {
      return api.toObject({ code: "-100", message: "", result: null});
      // return {
      //   code: "-100",
      //   message: "",
      //   result: null,
      // };
    } else {
      return api.toObject(response.data);
    }
  },

  /**
   * API 응답 데이터 처리
   * @param data (response.data)
   */
  toObject: function (data) {
    console.log(data);
    return {
      code: data.code,
      message: data.message,
      result: data.result,
    };
  },
}
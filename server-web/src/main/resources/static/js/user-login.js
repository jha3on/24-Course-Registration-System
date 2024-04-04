document.addEventListener("DOMContentLoaded", function (e) {

  /**
   * 로그인 이벤트 등록
   */
  document.querySelector(".js.user-login-button")?.addEventListener("click", function (event) {
    userPage.onLogin().then(() => userPage.onRouteCoursePage());
  });
});

const userPage = {

  /**
   * 사용자 로그인 처리
   */
  onLogin: async function () {
    const userNumber = document.querySelector(".js.user-login-number");
    const userPassword = document.querySelector(".js.user-login-password");
    const { code, message } = await client.usePostByUserLogin(userNumber.value, userPassword.value);

    if (code !== "+100") {
      alert(message);
    }
  },

  /**
   * 강의 페이지 이동
   */
  onRouteCoursePage: function () {
    location.replace("/course");
  },
};
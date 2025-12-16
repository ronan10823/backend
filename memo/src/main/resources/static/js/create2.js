const url = `http://localhost:8080/memo`;
const form = document.querySelector("#insert-form");

// fetch() : window 함수 기본 사용 가능
// post

form.addEventListener("submit", (e) => {
  e.preventDefault();

  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ text: e.target.text.value }), // 여기에서 e.target은 form 요소
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error(`error! ${res.status}`);
      }
      // json body 추출
      return res.json();
    })
    .then((data) => {
      console.log(data);
      if (data) {
        Swal.fire({
          title: "데이터 생성 완료",
          icon: "success",
          draggable: true,
        });
      }
      // 새로 고침
      // location.reload();
    })
    .catch((err) => console.log(err));
});

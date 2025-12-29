// 등록 클릭 시(form submit이 일어나면)
document.querySelector("#createForm").addEventListener("submit", (e) => {
  // submit 기능 중지
  e.preventDefault();
  // .. submit 기능을 -> 왜 중지시켜야 만 하는가?
  // uploadResult  안 li 정보를 수집해야 하기 때문이다. 그 정보 수집 이후 form hidden tag로 append하려고 하기 때문.

  const attachInfos = document.querySelectorAll(".uploadResult li");
  // li를 전체로 찾았기 때문에 반드시 배열로 들어온다. 그래서 forEach로 해야한다.

  let result = "";

  attachInfos.forEach((obj, idx) => {
    result += `<input type="hidden" name="movieImages[${idx}].imgName" value="${obj.dataset.name}">`;
    result += `<input type="hidden" name="movieImages[${idx}].uuid" value="${obj.dataset.uuid}">`;
    result += `<input type="hidden" name="movieImages[${idx}].path" value="${obj.dataset.path}">`;
  });

  e.target.insertAdjacentHTML("beforeend", result);

  console.log(e.target.innerHTML);

  e.target.submit();
});

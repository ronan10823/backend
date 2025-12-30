document.querySelectorAll(".uploadResult i").forEach((item) => {
  item.addEventListener("click", (e) => {
    e.preventDefault();

    const li = e.target.closest("li");

    if (confirm("정말로 삭제하시겠습니까?")) {
      // 화면에서 이미지 제거
      li.remove();
    }
  });
});

document.querySelector(".delete").addEventListener("click", () => {
  const removeForm = document.querySelector("#removeForm");
  removeForm.submit();
});

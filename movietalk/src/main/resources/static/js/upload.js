const fileInput = document.querySelector("[name='file']");

const showUploadImages = (files) => {
  const output = document.querySelector(".uploadResult ul");

  let tags = "";

  files.forEach((file) => {
    tags += `<li data-name="${file.imgName}" data-path="${file.path}" data-uuid="${file.uuid}">`;
    tags += `<a href="${file.imageURL}">`;
    tags += `<img src="/upload/display?fileName=${file.thumbnailURL}" class="block">`;
    tags += "</a>";
    tags += `<span class="text-sm d-inline-block mx-1">${file.imgName}</span>`;
    tags += `<a href="${file.imageURL}" data-file=""><i class="fa-solid fa-xmark"></i></a>`;
    tags += "</li>";
  });
  output.insertAdjacentHTML("beforeend", tags);
};

fileInput.addEventListener("change", (e) => {
  const files = fileInput.files;

  const formData = new FormData();
  for (let idx = 0; idx < files.length; idx++) {
    formData.append("uploadFiles", files[idx]);
  }
  fetch("/upload/upload", {
    method: "post",
    body: formData,
  })
    .then((res) => res.json())
    .then((data) => {
      console.log(data);
      showUploadImages(data);
    });
});

// 등록 클릭 시(form submit이 일어나면)
document.querySelector("#createForm").addEventListener("submit", (e) => {
  // submit 기능 중지
  e.preventDefault();

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

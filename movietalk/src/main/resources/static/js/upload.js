const fileInput = document.querySelector("[name='file']");

document.querySelector(".uploadResult").addEventListener("click", (e) => {
  e.preventDefault();

  const aTag = e.target.closest("a");
  const li = e.target.closest("li");

  // href 값 가져오기
  const href = aTag.getAttribute("href");

  // controller로 요청 보내기
  const formData = new FormData();
  formData.append("fileName", href);

  fetch("/upload/remove", {
    method: "post",
    body: formData,
  })
    .then((res) => res.text())
    .then((data) => {
      console.log(data);
      // 화면에서 이미지 제거
      li.remove();
    });
});

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

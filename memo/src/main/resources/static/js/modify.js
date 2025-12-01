// 삭제 버튼 클릭 시
// submit 기능 중지
// form.action = "가야할 곳"

document.querySelector(".btn-outline-danger").addEventListener("click", (e)=>{
    
    const form = document.querySelector("#modify-form")

    form.action = "/memo/remove"
    // why the action has to be /memo/remove? why not /memo/modify?
    form.submit();
})



// const btn = document.querySelector("button");

// form.addEventlistner("button", (e)=>{
//     e.preventDefault();
//     const 
// })

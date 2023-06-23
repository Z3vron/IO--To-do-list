
document.addEventListener('DOMContentLoaded', () => {

    loginLogic();
})


const loginLogic = () => {
    const loginButton = document.querySelector('#login_button')

    loginButton.addEventListener('click', () => {
        
        const formData = {
            "email" : document.querySelector("#email").value,
            "password" : document.querySelector("#password").value
        }

        requestBody = validateCredentials(formData)
        sendLoginRequest(requestBody)
    })
}


const sendLoginRequest = (body_object) => {
    const req = new XMLHttpRequest
    req.open('POST','http://localhost:8080/api/v1/auth/authenticate',false)
    req.setRequestHeader('Content-Type','application/json')
    req.send(JSON.stringify(body_object))
    console.log(req)

    if (req.status == 200) {
        sessionStorage.setItem('AuthResponse',req.responseText)
        sessionStorage.setItem('Subjects',JSON.stringify(JSON.parse(req.responseText)['user']['actualSemester']['subjects']))

        if (JSON.parse(req.responseText)['user']['role'] == 'STUDENT')
            window.location.replace('./panel_studenta.html')
        else if (JSON.parse(req.responseText)['user']['role'] == 'CLASS_PRESIDENT')
            window.location.replace('./panel_starosty.html')
    } else {
        setTimeout(function() { alert("Coś poszło nie tak..."); }, 10);
    }
    
}


const validateCredentials = (data) => {
    return data;
}
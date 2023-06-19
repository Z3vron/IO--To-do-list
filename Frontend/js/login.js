
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
        window.location.replace('./panel_studenta.html')
    } else {
        setTimeout(function() { alert("Coś poszło nie tak..."); }, 10);
    }
    
}


const validateCredentials = (data) => {
    return data;
}
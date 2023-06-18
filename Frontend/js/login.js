const getSemesters = () => {
    const req = new XMLHttpRequest;
    req.open('GET','http://localhost:8080/api/v1/init/semesters',false)
    req.send()
    console.log(req.responseText)
}

const getStudent = (id = 1) => {
    const req = new XMLHttpRequest
    req.open('GET',"http://localhost:8080/api/v1/students/{1}",false)
    req.send()
    console.log(req)
}

const checkDemoController = () => {
    const req = new XMLHttpRequest
    req.open('GET','http://localhost:8080/api/v1/demo-controller',false)
    // req.setRequestHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGFzc19wcmVzaWRlbnRfMjAyMF8xQHBvY3p0YS5wbCIsImlhdCI6MTY4NjU5OTA1MCwiZXhwIjoxNjg2NjAwMDUwfQ.vsmAHr7KiDzbKEx1uCyBH7YXZXwV_GNtkfverYN6UzI")
    // req.setRequestHeader('Access-Control-Allow-Origin','*');
    req.send()
    console.log(req)
}

const checkCredentials = () => {
    const req = new XMLHttpRequest;
    req.open('POST','http://localhost:8080/api/v1/auth/authenticate',false)
    req.setRequestHeader('Content-Type','application/json')
    req.send(JSON.stringify(
                {
                    "email":"student6@interia.pl",
                    "password":"password"
                })
            );

    console.log(req);

    // req.open('GET','http://localhost:8080/api/v1/init/semesters',false)
    // req.send()
    // console.log(req.responseText)
}



document.addEventListener('DOMContentLoaded', () => {

    // getSemesters();
    // checkDemoController();
    // // getStudent();
    // checkCredentials();
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
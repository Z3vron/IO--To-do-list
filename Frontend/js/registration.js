// const getSemesters = () => {
//     const req = new XMLHttpRequest;
//     req.open('GET','http://localhost:8080/api/v1/init/semesters',false)
//     req.send()
//     console.log(req.responseText)
// }

// const registerRequest = () => { 
//     const req = new XMLHttpRequest
//     req.open('POST','http://localhost:8080/api/v1/auth/register',false)
//     req.setRequestHeader('Content-Type','application/json')
//     req.send(
//         JSON.stringify( { 
//             "firstName" : "Jan",
//             "lastName" : "Nowak",
//             "email" : "student7@interia.pl",
//             "password" : "password",
//             "semesterId" : 1
//         })
//     )
//     console.log(req.responseText)
// }

// const checkCredentials = () => {
//     const req = new XMLHttpRequest;
//     req.open('POST','http://localhost:8080/api/v1/auth/authenticate',false)
//     req.setRequestHeader('Content-Type','application/json')
//     req.send(JSON.stringify(
//                     {
//                         "email":"student6@interia.pl",
//                         "password":"password"
//                     })
//             );

//     console.log(req);

//     // req.open('GET','http://localhost:8080/api/v1/init/semesters',false)
//     // req.send()
//     // console.log(req.responseText)
// }



document.addEventListener('DOMContentLoaded', () => {

    // getSemesters();
    // checkCredentials();
    // registerRequest();
    registrationLogic();
})

const registrationLogic = () => {
    const registrationButton = document.querySelector('#register_button');

    console.log(document.querySelector('fieldset [checked]').parentNode.id)

    registrationButton.addEventListener('click', () => {

        const formData = {
            "firstName" : document.querySelector('#first_name').value,
            "lastName" : document.querySelector('#last_name').value,
            "email" : document.querySelector('#email').value,
            "password" : document.querySelector('#password').value,
            "semesterId" : parseInt(document.querySelector('fieldset [checked]').parentNode.id)
        }

        requestBody = validateCredentials(formData)
        sendRegistrationRequest(requestBody)
    })
}


const sendRegistrationRequest = (body_object) => { 
    const req = new XMLHttpRequest
    req.open('POST','http://localhost:8080/api/v1/auth/register',false)
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
import { dropdownListHtml } from "./html_templates";


document.addEventListener('DOMContentLoaded', () => {
    console.log(sessionStorage.getItem('AuthResponse'))

    const authResponse = JSON.parse(sessionStorage.getItem('AuthResponse'));
    
    const subjectList = authResponse['user']['actualSemester']['subjects'];



    console.log(subjectList)
})

const proceedSubjectRecords = (records) => {
    const subjectList = document.querySelector('#subject_list')
    records.forEach(record => addSubjectRecord(subjectList,record))
}


const addSubjectRecord = (parent,subject_data) => {
    const record = document.createElement('tr');
    record.innerHTML = dropdownListHtml;
    parent.append(record)
}

document.addEventListener('DOMContentLoaded', () => {
    
    
    etapy = [...document.querySelectorAll('.etap')]

    etapy.forEach(element => {
        element.innerHTML = dropdownListHtml
    });

    button_stworz_ticket  = document.querySelector('#stworz_ticket_przycisk')
    button_stworz_ticket.addEventListener('click', (e) => {
      document.querySelector('#stworz_ticket_form').style.display = 'flex';
      e.target.style.display = 'none'
    })
})
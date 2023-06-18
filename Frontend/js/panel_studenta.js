import { dropdownListHtml } from "./html_templates.js";


document.addEventListener('DOMContentLoaded', () => {
    console.log(sessionStorage.getItem('AuthResponse'))

    const authResponse = JSON.parse(sessionStorage.getItem('AuthResponse'));
    
    const subjectList = authResponse['user']['actualSemester']['subjects'];

    proceedSubjectRecords(subjectList)

    console.log(subjectList)

    // const testList = [1,2,3,4]
    // testList.forEach((a,b,c) => console.log(a,b,c))
})

const proceedSubjectRecords = (records) => {
    const subjectList = document.querySelector('#subject_list tbody')
    records.forEach( (record,ind) => addSubjectRecord(subjectList,record,ind))
}


const addSubjectRecord = (parent,subjectData,subject_ind) => {
    const record = document.createElement('tr');

    record.innerHTML += `<th scope="row" class="subject_num">${subject_ind+1}</th>`;
    record.innerHTML += `<td class="subject_name">${subjectData['name']}</td>`;
    record.appendChild(createTaskProgressBar(subjectData['tasks']));
    record.appendChild(createTaskDropdown(subjectData['tasks']));

    parent.append(record);
}

const createTaskDropdown = (task_data) => {
    const dropDownMenu = document.createElement('td')

    return dropDownMenu;
}

const createTaskProgressBar = (task_data) => {
    const taskBarElem = document.createElement('td');
    taskBarElem.innerHTML += `<div class="progress" role="progressbar" aria-label="Example with label" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100">\
        <div class="progress-bar" style="width: 25%">25%</div>\
    </div>`
    return taskBarElem;
}





// document.addEventListener('DOMContentLoaded', () => {
    
    
//     const etapy = [...document.querySelectorAll('.etap')]

//     etapy.forEach(element => {
//         element.innerHTML = dropdownListHtml
//     });

//     button_stworz_ticket  = document.querySelector('#stworz_ticket_przycisk')
//     button_stworz_ticket.addEventListener('click', (e) => {
//       document.querySelector('#stworz_ticket_form').style.display = 'flex';
//       e.target.style.display = 'none'
//     })
// })
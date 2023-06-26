import {addTaskUpdateListener} from './panel_starosty.js'

const proceedSubjectCreditRecords = (records,isPresident=false) => {
    const subjectList = document.querySelector('#subject_credit_list tbody')
    subjectList.innerHTML = ''
    records.forEach( (record,ind) => addSubjectCreditRecord(subjectList,record,ind,isPresident))
}

const proceedSubjectManagementRecords = (records) => {
    const subjectList = document.querySelector('#subject_management_list tbody')
    records.forEach( (record,ind) => addSubjectManagementRecord(subjectList,record,ind))
}

const addSubjectCreditRecord = (parent,subjectData,subject_ind,isPresident) => {
    const record = document.createElement('tr');
    record.id = subjectData['id']
    record.className = 'credit_record'

    record.innerHTML += `<th scope="row" class="subject_num">${subject_ind+1}</th>`;
    record.innerHTML += `<td class="subject_name">${subjectData['name']}</td>`;
    record.appendChild(createTaskProgressBar(subjectData['tasks']));
    record.appendChild(createSubjectCreditDropdown(subjectData['tasks']));
    if (isPresident) {
        record.appendChild(addNewTaskButton(subjectData));
        record.appendChild(addRemoveTaskButton(subjectData));
    }


    parent.append(record);
}

const addNewTaskButton = (subjectData) => {
    const addTaskButton = document.createElement('td')
    addTaskButton.classList = 'add_task_button'// d-flex justify-content-center'
    addTaskButton.id = `add_task_button_${subjectData.id}`
    addTaskButton.innerHTML = '<button type="button" style="width: 50px;display:block;margin:0 auto;" class="btn btn-success">+</button>'
    return addTaskButton;
}

const addRemoveTaskButton = (subjectData) => {
    const removeTaskButton = document.createElement('td')
    removeTaskButton.classList = 'remove_task_button'//  d-flex justify-content-center'
    removeTaskButton.id = `remove_task_button_${subjectData.id}`
    removeTaskButton.innerHTML = '<button type="button" style="width: 50px;display:block;margin:0 auto;" class="btn btn-danger">-</button>'
    return removeTaskButton;
}

const addSubjectManagementRecord = (parent, subjectData, subject_ind) => {
    const record = document.createElement('tr');
    record.classList.add('subject_record')

    record.innerHTML += `<th scope="row" class="subject_num">${subject_ind+1}</th>`;
    record.innerHTML += `<td class="subject_name">${subjectData['name']}</td>`;
    
    initManagementButtons(record,subject_ind)
    
    parent.append(record);
}

const createSubjectCreditDropdown = (tasks) => {
    const dropDownWrapper = document.createElement('td')
    dropDownWrapper.classList.add('etap')

    const dropDownMenu = document.createElement('div')
    dropDownMenu.classList.add('dropdown')
    dropDownMenu.innerHTML += `<button class="btn btn-secondary dropdown-toggle" style="display:block;margin:0 auto;" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    Rozwin liste
                                </button>`;

    const dropDownMenuTaskList = document.createElement('ul')
    dropDownMenuTaskList.classList = 'dropdown-menu dropdown-menu-dark'

    tasks.forEach(task => {
        dropDownMenuTaskList.innerHTML +=   `<li>
                                        <div class="form-check">
                                            <input class="form-check-input" type="checkbox" value=" " id="${task.id}" ${task.done ? 'checked' : ''}>
                                            <label class="form-check-label" for="defaultCheck1">
                                                ${task.name}
                                            </label>
                                        </div>
                                    </li>`
    })

    dropDownMenuTaskList.innerHTML += `<li><hr class="dropdown-divider"></li>
                                <li>
                                    <div class="d-grid gap-2" dropdown-button>
                                        <button class="btn btn-primary col-10 mx-auto" type="button">Zapisz</button>
                                    </div>
                                </li>`;


    dropDownMenu.append(dropDownMenuTaskList)
    dropDownWrapper.append(dropDownMenu)
    return dropDownWrapper;
}


const createTaskProgressBar = (tasks_arr) => {
    const completedTasks = tasks_arr.filter(task => { return task.done }).length
    const percentage = tasks_arr.length > 0 ? Math.floor(completedTasks / tasks_arr.length*100) : 0
    const taskBarElem = document.createElement('td');
    
    taskBarElem.classList.add('credit_progress')
    taskBarElem.innerHTML += `<div class="progress" role="progressbar" aria-label="Example with label"  aria-valuemin="0" aria-valuemax="100">\
        <div class="progress-bar" style="width: ${percentage > 5 ? percentage : 5}%">${percentage}%</div>\
    </div>`
    return taskBarElem;
}

const initManagementButtons = (record,id) => {

    record.innerHTML += `<td class="button_wrapper usun">
                            <button type="button" class="btn btn-danger" id="${id}_usun">Usuń przedmiot</button>
                        </td>`;
    record.innerHTML += `<td class="button_wrapper zawies">
                            <button type="button" class="btn btn-warning" id="${id}_zawies">Zawieś przedmiot</button>
                        </td>`;
    record.innerHTML += `<td class="button_wrapper otworz d-none">
                            <button type="button" class="btn btn-success" id="${id}_otworz">Otwórz przedmiot</button>
                        </td>`
}

const refreshSubjectsStored = (studentId = 1) => {
    const req = new XMLHttpRequest
    req.open('GET',`http://localhost:8080/api/v1/students/${studentId}`,false)
    req.setRequestHeader('Content-Type','application/json')
    req.send(JSON.stringify({}))
  
    sessionStorage.setItem('Subjects',JSON.stringify(JSON.parse(req.responseText)['actualSemester']['subjects']))
    // console.log(JSON.parse(req.responseText)['actualSemester']['subjects'])
  
    var subjectsNewData = JSON.parse(req.responseText).actualSemester.subjects
  
    const subjectRecords = document.querySelectorAll('#subject_credit_list tr.credit_record')
  
    subjectRecords.forEach(record => {
        const newSubjectObj = subjectsNewData.find(subject => subject.id == +record.id)

        const progressBarWraper = record.querySelector('.credit_progress')
        const dropDownWrapper = record.querySelector('.etap')

        progressBarWraper.remove()
        dropDownWrapper.remove()

        record.insertBefore(createSubjectCreditDropdown(newSubjectObj.tasks),record.childNodes[2])
        record.insertBefore(createTaskProgressBar(newSubjectObj.tasks), record.childNodes[2])

        addTaskUpdateListener(record)
    })
  }

const sendTaskUpdateRequest = (taskStatus, taskInput) => {
    const req = new XMLHttpRequest
  
    if (taskStatus) {
      console.log('done request sent')
      req.open('PUT',`http://localhost:8080/api/v1/tasks/done/${taskInput.id}`,false)
    } else {
      console.log('undone request sent')
      req.open('PUT',`http://localhost:8080/api/v1/tasks/undone/${taskInput.id}`,false)
    }
    req.setRequestHeader('Content-Type','application/json')
    req.send(JSON.stringify({}))
  
    if (req.status != 200)  {
        setTimeout(function() { alert("Coś poszło nie tak..."); }, 10);
    }
  }
  
// const getSubjectTaksks

export {proceedSubjectCreditRecords, proceedSubjectManagementRecords, refreshSubjectsStored, sendTaskUpdateRequest}
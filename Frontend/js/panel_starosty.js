import { addAlert } from "./alerts.js";
import {proceedSubjectManagementRecords,proceedSubjectCreditRecords, refreshSubjectsStored, sendTaskUpdateRequest} from "./subject_management.js";

document.addEventListener('DOMContentLoaded', () => {
    const subjectList = JSON.parse(sessionStorage.getItem('Subjects'));

    proceedSubjectCreditRecords(subjectList,true)
    subjectCreditLogic();
})

const subjectManagementLogic = () => {
    const buttons = document.querySelectorAll('.subject_record button');
    
    buttons.forEach(button => button.addEventListener('click', e => {
        const subjectRecord = e.currentTarget.parentNode.parentNode;

        if (button.parentNode.classList.contains('usun')) {
            var messageStatus = 'danger';
            var message = 'Usunięto przedmiot: ' + subjectRecord.querySelector('.subject_name').innerHTML;
            subjectRecord.remove()
        }
        if (button.parentNode.classList.contains('zawies')) {var messageStatus = 'danger';
            var messageStatus = 'warning';
            var message = 'Zawieszono przedmiot: ' + subjectRecord.querySelector('.subject_name').innerHTML;
            button.parentNode.classList.toggle('d-none')
            subjectRecord.querySelector('.otworz').classList.toggle('d-none')
        }
        if (button.parentNode.classList.contains('otworz')) {
            var messageStatus = 'success';
            var message = 'Otworzono przedmiot: ' + subjectRecord.querySelector('.subject_name').innerHTML;
            button.parentNode.classList.toggle('d-none')
            subjectRecord.querySelector('.zawies').classList.toggle('d-none')
        }

        addAlert(messageStatus,message);
    }))

}


const subjectCreditLogic = () => {
  const creditButtons = [...document.querySelectorAll('[dropdown-button]')]
  

  creditButtons.forEach(button => {

    var subjectRecord = button.closest('tr')

    const subjectId = subjectRecord.id;
    const tasksToUpdate = getTasksBySubjectId(subjectId)
    
    addTaskLogic(subjectId);
    removeTaskLogic(subjectId);

    button.addEventListener('click',e => {
      const taskInputs = e.target.parentNode.parentNode.parentNode.querySelectorAll('.form-check input')

      addAlert('warning','Saving changes to database')
      setTimeout(() => {
        tasksToUpdate.forEach(task => {
          taskInputs.forEach(taskInput => {
            if (taskInput.parentNode.querySelector('label').innerText.trim() == task.name)
              sendTaskUpdateRequest(taskInput.checked,task)
          })
        })
        addAlert('success','Saved successfully')
        refreshSubjectsStored(JSON.parse(sessionStorage.getItem('AuthResponse'))['user']['id'])
      }, 50);
      
      })
  }
  )

}

const addTaskLogic = (subjectId) => {
  const addTaskButton = document.getElementById(`${subjectId}`).querySelector('td.add_task_button button')
  addTaskButton.addEventListener('click', () => {
      console.log(subjectId)
      const president = JSON.parse(sessionStorage.getItem('AuthResponse')).user
      performTaskAdding(subjectId,president)
  })
}

const removeTaskLogic = (subjectId) => {
  const removeTaskButton = document.getElementById(`${subjectId}`).querySelector('td.remove_task_button button')
  removeTaskButton.addEventListener('click', () => {
      performTaskDeletion(subjectId)
  })
}

const performTaskDeletion = (subjectId) => {
  const taskListForDeletion = document.createElement('div')
  taskListForDeletion.classList = 'modal d-flex justify-content-center'
  taskListForDeletion.role = 'dialog'

  taskListForDeletion.innerHTML += `<div class="modal-dialog" role="document" style='width:300px'>
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Wybierz które etapy chcesz usunąć</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
          
          </div>
          <div class="modal-footer">
            <button type="button" id="remove_tasks_btn" class="btn btn-danger">Usuń wybrane</button>
            <button type="button" id="cancel_deleting_task_btn" class="btn btn-secondary" data-dismiss="modal">Wróć</button>
          </div>
        </div>
      </div>>`

  taskListForDeletion.querySelector('.modal-body').appendChild(createTaskListForDeletion(subjectId))

  taskListForDeletion.id = 'task_data_input'
  taskListForDeletion.style.position = 'absolute'
  document.querySelector('body').prepend(taskListForDeletion)

  const manageAddingButtons = document.querySelectorAll('.modal-content button');
  manageAddingButtons.forEach(button => button.addEventListener('click', (e) => {
      if (e.target.id == 'remove_tasks_btn') {
        const taskRecords = taskListForDeletion.querySelectorAll('.form-check')
        taskRecords.forEach(record => {
          if (record.querySelector('input').checked) {
            removeTask(record.querySelector('label').innerText.trim(),subjectId)
          }
        })
        refreshSubjectsStored(JSON.parse(sessionStorage.getItem('AuthResponse')).user.id)
      }

      taskListForDeletion.remove()
  }))
}

const createTaskListForDeletion = (subjectId) => {
  const subject = JSON.parse(sessionStorage.getItem('Subjects')).filter(subject => subject.id == subjectId)[0]
  const list = document.createElement('ul')
  subject.tasks.forEach(task => {
    list.innerHTML +=   `<li>
                                    <div class="form-check">
                                        <input class="form-check-input" type="checkbox" value=" " id="${task.id}">
                                        <label class="form-check-label" for="defaultCheck1">
                                            ${task.name}
                                        </label>
                                    </div>
                                </li>`
  })
  return list
}

const performTaskAdding = (subjectId,president) => {
  const taskDataInputForm = document.createElement('div')
  taskDataInputForm.classList = 'modal d-flex justify-content-center'
  taskDataInputForm.role = 'dialog'

  taskDataInputForm.innerHTML += `<div class="modal-dialog" role="document" style='width:300px'>
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Dodaj etap zaliczenia</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <input class="form-control" id="task_name_input" type="text" placeholder="Nazwa etapu zaliczenia">
            <input class="form-control" id="task_description_input" type="text" placeholder="Opis">
          </div>
          <div class="modal-footer">
            <button type="button" id="save_task_btn" class="btn btn-primary">Zapisz</button>
            <button type="button" id="cancel_adding_task_btn" class="btn btn-secondary" data-dismiss="modal">Wróć</button>
          </div>
        </div>
      </div>>`

  taskDataInputForm.id = 'task_data_input'
  taskDataInputForm.style.position = 'absolute'
  document.querySelector('body').prepend(taskDataInputForm)

  const manageAddingButtons = document.querySelectorAll('.modal-content button');
  manageAddingButtons.forEach(button => button.addEventListener('click', (e) => {
      if (e.target.id == 'save_task_btn') {
        var name = taskDataInputForm.querySelector('#task_name_input').value
        var description = taskDataInputForm.querySelector('#task_description_input').value
        
        if (name.trim() != '')
          addNewTask(subjectId,president,name,description)

        refreshSubjectsStored(JSON.parse(sessionStorage.getItem('AuthResponse')).user.id)
      }

      taskDataInputForm.remove()
  }))
  
}

const addNewTask = (subjectId,president,name,description) => {
  // console.log(president.actualSemester.id, +subjectId)
  const req = new XMLHttpRequest
  req.open('POST',`http://localhost:8080/api/v1/tasks/${president.actualSemester.id}/${subjectId}`,false)
  req.setRequestHeader('Content-Type','application/json')
  req.send(JSON.stringify({
    "userId" : president.id,
    "subjectId" : subjectId,
    "name" : name,
    "description" : description
  }))

  // console.log(req)
}

const removeTask = (task_name,subjectId) => {
  console.log(task_name)
  const req = new XMLHttpRequest
  req.open('DELETE',`http://localhost:8080/api/v1/tasks/remove/${task_name}/${subjectId}`,false)
  req.setRequestHeader('Content-Type','application/json')
  req.send(JSON.stringify({}))
}

const getTasksBySubjectId = (subjectId) => {
  const req = new XMLHttpRequest
  req.open('GET',`http://localhost:8080/api/v1/tasks/${subjectId}`,false)
  req.setRequestHeader('Content-Type','application/json')
  req.send(JSON.stringify({}))

  if (req.status == 200) {
    return JSON.parse(req.responseText)
  } else {
      setTimeout(function() { alert("Coś poszło nie tak..."); }, 10);
  }
}

const ticketManagementLogic = () => {
    const buttons = document.querySelectorAll('.ticket_record button')
    
    buttons.forEach(button => button.addEventListener('click', e => {
        const ticketRecord = e.currentTarget.parentNode.parentNode;

        if (e.target.classList.contains('reject_btn')) {
            var messageStatus = 'danger';
            var message = 'Odrzucono ticket: ' + ticketRecord.querySelector('.ticket_name').innerHTML;
        }
        else if (e.target.classList.contains('accept_btn')){
            var messageStatus = 'success';
            console.log(e.target)
            var message = 'Zaakceptowano ticket: ' + ticketRecord.querySelector('.ticket_name').innerHTML;
        }

        ticketRecord.remove()
        addAlert(messageStatus,message);
    }))
}


const ticketManagementInit = () => {
    const akceptuj_list = document.querySelectorAll('.akceptuj')
    
    akceptuj_list.forEach(item => {
      item.innerHTML = '<button type="button" class="btn btn-success accept_btn">Akceptuj</button>'
    })

    const odrzuc_list = document.querySelectorAll('.odrzuc')
    
    odrzuc_list.forEach(item => {
      item.innerHTML = '<button type="button" class="btn btn-danger reject_btn">Odrzuć</button>'
    })
}


const subjectManagementInit = () => {



    const buttonWrappers = document.querySelectorAll('.subject_record .button_wrapper');
    buttonWrappers.forEach(wrapper => {
        if (wrapper.classList.contains('usun'))
            wrapper.innerHTML = '<button type="button" class="btn btn-danger">Usuń przedmiot</button>';
        if (wrapper.classList.contains('zawies'))
            wrapper.innerHTML = '<button type="button" class="btn btn-warning">Zawieś przedmiot</button>';
        if (wrapper.classList.contains('otworz'))
            wrapper.innerHTML = '<button type="button" class="btn btn-success">Otwórz przedmiot</button>';
    })
}


const studentManagementInit = () => {
    let avaliableListHtml = '<div class="dropdown">\
    <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">\
      Rozwin liste\
    </button>\
    <ul class="dropdown-menu dropdown-menu-dark">\
      <li><div class="form-check">\
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">\
        <label class="form-check-label" for="defaultCheck1">\
          Przedmiot pierwszy\
        </label>\
      </div></li>\
      <li><div class="form-check">\
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">\
        <label class="form-check-label" for="defaultCheck1">\
          Przedmiot drugi\
        </label>\
      </div></li>\
      <li><div class="form-check">\
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">\
        <label class="form-check-label" for="defaultCheck1">\
          Przedmiot trzeci\
        </label>\
      </div></li>\
      <li><hr class="dropdown-divider"></li>\
      <li>\
        <div class="d-grid gap-2">\
          <button class="btn btn-primary col-10 mx-auto" type="button">Wypisz</button>\
        </div>\
      </li>\
    </ul>\
  </div>'

  let assignedListHtml = '<div class="dropdown">\
    <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">\
      Rozwin liste\
    </button>\
    <ul class="dropdown-menu dropdown-menu-dark">\
      <li><div class="form-check">\
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">\
        <label class="form-check-label" for="defaultCheck1">\
          Przedmiot pierwszy\
        </label>\
      </div></li>\
      <li><div class="form-check">\
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">\
        <label class="form-check-label" for="defaultCheck1">\
          Przedmiot drugi\
        </label>\
      </div></li>\
      <li><div class="form-check">\
        <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">\
        <label class="form-check-label" for="defaultCheck1">\
          Przedmiot trzeci\
        </label>\
      </div></li>\
      <li><hr class="dropdown-divider"></li>\
      <li>\
        <div class="d-grid gap-2">\
          <button class="btn btn-primary col-10 mx-auto" type="button">Przydziel</button>\
        </div>\
      </li>\
    </ul>\
  </div>'

    const przydziel_list = document.querySelectorAll('.przydziel')
    const wypisz_list = document.querySelectorAll('.wypisz')

    przydziel_list.forEach(element => {
        element.innerHTML = avaliableListHtml
    });
    wypisz_list.forEach(element => {
        element.innerHTML = assignedListHtml
    });
}

const addTaskUpdateListener = (subjectRecord) => {
  
    const subjectId = subjectRecord.id;
    const tasksToUpdate = getTasksBySubjectId(subjectId)
    
    subjectRecord.querySelector('[dropdown-button]').addEventListener('click',e => {
      const taskInputs = e.target.parentNode.parentNode.parentNode.querySelectorAll('.form-check input')

      addAlert('warning','Saving changes to database')
      setTimeout(() => {
        tasksToUpdate.forEach(task => {
          taskInputs.forEach(taskInput => {
            if (taskInput.parentNode.querySelector('label').innerText.trim() == task.name)
              sendTaskUpdateRequest(taskInput.checked,task)
          })
        addAlert('success','Saved successfully')
        })
  
        refreshSubjectsStored(JSON.parse(sessionStorage.getItem('AuthResponse'))['user']['id'])
      }, 50);
      
    })
}


export {addTaskUpdateListener}
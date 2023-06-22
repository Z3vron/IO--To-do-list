import { addAlert } from "./alerts.js";
import {proceedSubjectManagementRecords,proceedSubjectCreditRecords} from "./subject_management.js";

document.addEventListener('DOMContentLoaded', () => {

    const authResponse = JSON.parse(sessionStorage.getItem('AuthResponse'));
    const subjectList = authResponse['user']['actualSemester']['subjects'];

    proceedSubjectCreditRecords(subjectList)
    proceedSubjectManagementRecords(subjectList)

    console.log(subjectList);
    
    subjectManagementLogic();
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

    
    const button_dodaj_przedmiot  = document.querySelector('#dodaj_przedmiot_przycisk')
    button_dodaj_przedmiot.addEventListener('click', (e) => {
      document.querySelector('#dodaj_przedmiot_form').style.display = 'flex';
      e.target.style.display = 'none'
    })
}


const subjectCreditLogic = () => {
  const creditButtons = document.querySelectorAll('[dropdown-button]')
  creditButtons.forEach(button => button.addEventListener('click',e => {
    const taskInputs = e.target.parentNode.parentNode.parentNode.querySelectorAll('.form-check input')
    
    taskInputs.forEach(taskInput => {
      // if (taskInput.checked) {
        
      // } else {
      //   console.log('unchecked')
      // }
      sendTaskUpdateRequest(taskInput)
    })
  }))
}


const sendTaskUpdateRequest = (taskInput) => {
  const req = new XMLHttpRequest

  if (taskInput.checked) {
    req.open('PUT',`http://localhost:8080/api/v1/tasks/undone/${taskInput.id}`,false)
  } else {
    req.open('PUT',`http://localhost:8080/api/v1/tasks/done/${taskInput.id}`,false)
  }
  req.setRequestHeader('Content-Type','application/json')
  req.send(JSON.stringify({}))
  console.log(req)

  if (req.status != 200)  {
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

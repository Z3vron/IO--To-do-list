import { dropdownListHtml } from "./html_templates.js";
import {proceedSubjectCreditRecords,refreshSubjectsStored, sendTaskUpdateRequest} from "./subject_management.js";


document.addEventListener('DOMContentLoaded', () => {
    console.log(sessionStorage.getItem('AuthResponse'))

    const authResponse = JSON.parse(sessionStorage.getItem('AuthResponse'));
    
    const subjectList = authResponse['user']['actualSemester']['subjects'];

    proceedSubjectCreditRecords(subjectList)
    subjectCreditLogic()

    console.log(subjectList)
})

const subjectCreditLogic = () => {
    const creditButtons = document.querySelectorAll('[dropdown-button]')
    creditButtons.forEach(button => button.addEventListener('click',e => {
      const taskInputs = e.target.parentNode.parentNode.parentNode.querySelectorAll('.form-check input')
      
      taskInputs.forEach(taskInput => {
        sendTaskUpdateRequest(taskInput.checked,taskInput)
      })
  
      refreshSubjectsStored(JSON.parse(sessionStorage.getItem('AuthResponse'))['user']['id'])
    }))
  }
  

  



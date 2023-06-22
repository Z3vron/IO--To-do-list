import { dropdownListHtml } from "./html_templates.js";
import {proceedSubjectCreditRecords} from "./subject_management.js";


document.addEventListener('DOMContentLoaded', () => {
    console.log(sessionStorage.getItem('AuthResponse'))

    const authResponse = JSON.parse(sessionStorage.getItem('AuthResponse'));
    
    const subjectList = authResponse['user']['actualSemester']['subjects'];

    proceedSubjectCreditRecords(subjectList)

    console.log(subjectList)
})


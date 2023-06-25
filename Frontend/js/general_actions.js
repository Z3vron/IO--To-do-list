document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.querySelector('#wyloguj')
    logoutButton.addEventListener('click', () => {
        sessionStorage.clear()
        window.location.replace('./login.html')
    })
})
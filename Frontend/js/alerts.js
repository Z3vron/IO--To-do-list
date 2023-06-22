export function addAlert(alertType,alertMessage) {
    var alertList = document.querySelector('#alert_list');

    if (alertList == null) {
        var alertList = addAlertList()
    }

    switch (alertType) {
        case 'warning':
            var alert = createAlert(alertMessage, 'alert alert-warning');
            break;
        case 'danger':
            var alert = createAlert(alertMessage, 'alert alert-danger');
            break;
        case 'success':
            var alert = createAlert(alertMessage, 'alert alert-success');
            break;
    }

    alertList.append(alert);
    
    setTimeout(() => {
        alert.style.opacity = '0';
    }, 3000);

    setTimeout(() => {
        alert.remove();
        if (alertList.children.length == 0)
            alertList.remove()
    }, 3500);
}

const createAlert = (alertMessage,alertClassList) => {
    const alert = document.createElement('div')
    alert.classList = alertClassList;
    alert.role = 'alert' ;
    alert.style.transition = '0.5s all'
    alert.style.opacity = '1';
    alert.innerHTML = alertMessage
    return alert;
}

const addAlertList = () => {
    var alertList = document.createElement('div')
    alertList.id = 'alert_list';
    alertList.style.position = 'absolute'
    alertList.style.width = '25%'
    alertList.style.right = 0
    document.querySelector('body').prepend(alertList)
    return alertList;
}

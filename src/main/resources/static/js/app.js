const url = 'api/users/';
let table = document.getElementById('usersTable');
let listener;

getTableWithUsers();

//РАБОТА С МОДАЛЬНЫМИ ОКНАМИ
$('#userDialog').on('shown.bs.modal', function (event) {
    let button = $(event.relatedTarget);
    let userId = button.data('userid');
    let modal = $(this);
    fillModalWithUserData(userId, modal);

    // ДЛЯ МОДАЛЬНОГО ОКНА EDIT
    if (button.data('action') === 'edit') {
        document.querySelector('#userDialogLabel').textContent = 'Edit user';
        document.querySelector('#userDialogButton').textContent = 'Edit';
        [].forEach.call(this.querySelectorAll('input'), function (el) {
            if (!(el.id === 'id')) {
                el.readOnly = false;
            }
        });
        this.querySelector('select').disabled = false;
        this.querySelector('#password-form').style.display = "";
        listener = function (e) {
            editUserRequest(userId);
        }
        document.getElementById("userDialogButton").addEventListener('click', listener)

    // ДЛЯ МОДАЛЬНОГО ОКНА DELETE
    } else if (button.data('action') === 'delete') {
        document.querySelector('#userDialogLabel').textContent = 'Delete user';
        document.querySelector('#userDialogButton').textContent = 'Delete';
        [].forEach.call(this.querySelectorAll('input'), function (el) {
            el.readOnly = true;
        });
        this.querySelector('select').disabled = true;
        this.querySelector('#password-form').style.display = "none";
        listener = function (e) {
            deleteRequest(userId);
        }
        document.getElementById("userDialogButton").addEventListener('click', listener)
    }
}).on('hidden.bs.modal', function (event) {
    clearForm(document.querySelector('#userDialog'));
    document.getElementById("userDialogButton").removeEventListener('click', listener);
})

//ДОБАВЛЕНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ
document.getElementById('addNewUserBtn').addEventListener('click', () => {
    createUserRequest();
    clearForm(document.querySelector('#newUserForm'));
    document.querySelector('#usersTablePanel').setAttribute('class', 'tab-pane fade show active');
    document.querySelector('#newUser').setAttribute('class', 'tab-pane fade');
    document.querySelector('#usersTableTabBtn').setAttribute('class', 'nav-link active');
    document.querySelector('#newUserTabBtn').setAttribute('class', 'nav-link');
})

function fillModalWithUserData(userId, modal) {
    fetch(url + userId)
        .then(res => res.json())
        .then(user => {
            modal.find('#id').val(user.id);
            modal.find('#username').val(user.username);
            modal.find('#age').val(user.age);
            modal.find('#roles').val(user.roles);
        })
}

function getTableWithUsers() {
    document.querySelector('#usersTable tbody').innerHTML = "";
    fetch(url)
        .then(result => result.json())
        .then(users => {
            users.forEach(user => {
                addUserToTable(user);
            })
        })
}

function addUserToTable(user) {
    let tableBody = document.querySelector('#usersTable tbody');
    let tableFilling = '';
    tableFilling += `<tr>
                        <td id="userID" class="align-middle">${user.id}</td>
                        <td class="align-middle">${user.username}</td>
                        <td class="align-middle">${user.age}</td>
                        <td class="align-middle">
                            <ul class="list-inline mt-3">`
    user.roles.forEach(role => {
        tableFilling += `<li class="list-inline-item align-middle">${role.name}</li>`
    })
    tableFilling +=
                            `</ul>
                         </td>
                         <td class="align-middle">
                            <button id="edit-user" type="button" data-userid="${user.id}" data-action="edit"
                                class="btn btn-primary border-0 py-1"
                                style="background-color: DarkCyan" data-toggle="modal"
                                data-target="#userDialog"> Edit
                            </button>
                         </td>
                         <td class="align-middle">
                             <button id="delete-user" type="button" data-userid="${user.id}" data-action="delete"
                                class="btn btn-primary border-0 py-1"
                                style="background-color: Crimson; color: white" data-toggle="modal"
                                data-target="#userDialog">Delete
                             </button>
                         </td>
                      </tr>`;
    tableBody.innerHTML += tableFilling;
}

function deleteRequest(userId) {
    fetch(url + userId, {method: 'DELETE'})
        .then(() => {
            table.querySelectorAll('tr').forEach(raw => {
                if ((!(raw.querySelectorAll('td').item(0) === null))
                    && (raw.querySelectorAll('td').item(0).textContent === userId.toString())) {
                    raw.remove()
                }
            })
        });
}

function createUserRequest() {
    let roles = $('[name="roles"]').val();
    let rolesList = [];
    roles.forEach(role => {
        rolesList.push({id: role});
    })
    fetch(url, {
        headers: {"Content-Type": "application/json; charset=utf-8"},
        method: 'POST',
        body: JSON.stringify({
            username: document.querySelector('#username-add').value,
            password: document.querySelector('#password-add').value,
            age: document.querySelector('#age-add').value,
            roles: rolesList
        })
    })
        .then(() => {
            getTableWithUsers()
        })
}

function editUserRequest(userId) {
    let rolesEdit = $('[name="rolesEdit"]').val();
    let rolesList = [];
    rolesEdit.forEach(role => {
        rolesList.push({id: role});
    })
    fetch(url + userId, {
        headers: {"Content-Type": "application/json; charset=utf-8"},
        method: 'PUT',
        body: JSON.stringify({
            username: document.querySelector('#username').value,
            password: document.querySelector('#password').value,
            age: document.querySelector('#age').value,
            roles: rolesList
        })
    })
        .then(() => {
            getTableWithUsers();
        })
}

function clearForm(form) {
    [].forEach.call(form.querySelectorAll('input'), function (el) {
            el.value = "";
    });
    form.querySelector('select').value = "";
}
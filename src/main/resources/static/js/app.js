const url = 'http://localhost:9090/users/';

$(async function () {
    await getTableWithUsers();
})


//РАБОТА С МОДАЛЬНЫМИ ОКНАМИ
$('#userDialog').on('shown.bs.modal', function (event) {
    let button = $(event.relatedTarget);
    let userId = button.data('userid');
    if (userId) {
        //ОТОБРАЖЕНИЕ ДАННЫХ ВЫБРАННОГО ПОЛЬЗОВАТЕЛЯ
        fetch(url + userId)
            .then(res => res.json())
            .then(user => {
                let modal = $(this);
                modal.find('#id').val(user.id);
                modal.find('#username').val(user.username);
                modal.find('#age').val(user.age);
                modal.find('#roles').val(user.roles);
            })
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
            document.getElementById("userDialogButton").addEventListener('click', (e) => {
                let rolesEdit = $('[name="rolesEdit"]').val();
                let rolesList = [];
                rolesEdit.forEach(role => {
                    rolesList.push({id: role});
                })
                fetch(url + userId, {
                    headers: {"Content-Type": "application/json; charset=utf-8"},
                    method: 'PUT',
                    body: JSON.stringify({
                        username: this.querySelector('#username').value,
                        password: this.querySelector('#password').value,
                        age: this.querySelector('#age').value,
                        roles: rolesList
                    })
                })
                    .then(response => response.json())
            })
            // ДЛЯ МОДАЛЬНОГО ОКНА DELETE
        } else if (button.data('action') === 'delete') {
            let table = document.getElementById('usersTable');
            alert("html до выпиливания строки с пользователем из таблицы" + table.innerText)

            let listener = function (e) {
                let  allTrWithUserID = table.querySelectorAll('tr')
                allTrWithUserID.forEach(trWithUser => {
                    trWithUser.querySelectorAll('td').forEach(tdUser =>{
                        //ДОБАВИТЬ ПРОВЕРКУ, ЧТО ЭТО ИМЕННО ID ПОЛЬЗОВАТЕЛЯ, А НЕ ВОЗРАСТ, НАПРИМЕР
                        if (tdUser.textContent === userId.toString()) {
                            trWithUser.remove()
                            alert("html после выпиливания строки с пользователем из таблицы" + table.innerText)
                        }
                    })
                })
                //fetch(url + userId, {method: 'DELETE'})
                //ПЕРЕНЕСТИ СЮДА кусок с выпиливанием из таблицы
                //.then(() =>
            }
            document.querySelector('#userDialogLabel').textContent = 'Delete user';
            document.querySelector('#userDialogButton').textContent = 'Delete';
            [].forEach.call(this.querySelectorAll('input'), function (el) {
                el.readOnly = true;
            });
            this.querySelector('select').disabled = true;
            this.querySelector('#password-form').style.display = "none";
            document.getElementById("userDialogButton").addEventListener('click', listener)
            //очистка данных пользователя при закрытии окна через кнопку в футтере
            document.getElementById("closeUserDialog").addEventListener('click', () => {
                document.getElementById("userDialogButton").removeEventListener('click', listener);
                [].forEach.call(this.querySelectorAll('input'), function (el) {
                    el.value = "";
                });
            })
            //очистка данных пользователя при закрытии окна через кнопку в хеддере
            document.getElementById("closeUserDialogInHeader").addEventListener('click', () => {
                document.getElementById("userDialogButton").removeEventListener('click', listener);
                [].forEach.call(this.querySelectorAll('input'), function (el) {
                    el.value = "";
                });
            })
        }
    }
})

//ДОБАВЛЕНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ
document.getElementById('addNewUserBtn').addEventListener('click', () => {
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
        .then(response => response.json())
})


//ОТОБРАЖЕНИЕ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ
function getTableWithUsers() {
    fetch(url)
        .then(result => result.json())
        .then(users => {
            users.forEach(user => {
                addUserToTable(user);
            })
        })
}

//ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ В HTML ТАБЛИЦУ
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
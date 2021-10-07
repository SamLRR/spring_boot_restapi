$(document).ready(function () {

    const url = "http://localhost:8080/";
    let tbody = document.querySelector('tbody');
    let result = '';

    const getAllUsers = (users) => {
        function getUserRoles(roles) {
            let stringRoles = '';
            roles.forEach(role => {
                stringRoles += role.name + " ";
            })
            return stringRoles;
        }

        users.forEach(user => {
            result += '<tr> ' +
                '<td>' + user.id + '</td>' +
                '<td>' + user.firstName + '</td>' +
                '<td>' + user.lastName + '</td>' +
                '<td>' + user.age + '</td>' +
                '<td>' + user.username + '</td>' +
                '<td>' + getUserRoles(user.roles) + '</td>' +
                '<td><a href="user/update/' + user.id + '" id="eBtn1" class="eBtn3 btn btn-info">Edit</a></td>' +
                '<td><a href="user/update/' + user.id + '" id="eBtn2" class="eBtn4 btn btn-danger delBtn">Delete</a></td>' +
                '</tr>'
        })
        tbody.innerHTML = result;
    }


    fetch(url+'admin')
        .then(res => res.json())
        .then(data => getAllUsers(data))
        .catch(error => console.log(error))

    const on = (element, event, selector, handler) => {
        element.addEventListener(event, e => {
            if (e.target.closest(selector)) {
                handler(e)
            }
        })
    }

    on(document, 'click', '#eBtn1', function (e) {
        e.preventDefault();
        let parentNode = e.target.parentNode.parentNode;
        let id = parentNode.firstElementChild.innerHTML;

        var href = url+'user/update/' + id;

        $.get(href, function (user, status) {
            debugger;
            $('.formEdit #id').val(user.id);
            $('.formEdit #firstName').val(user.firstName);
            $('.formEdit #lastName').val(user.lastName);
            $('.formEdit #age').val(user.age);
            $('.formEdit #userName').val(user.userName);
            $('.formEdit #password').val(user.password);
            $('.formEdit #roles').val(user.roles);

        });
        $('.formEdit #modalEdit').modal();

    });

    on(document, 'click', '#eBtn2', function (e) {
        e.preventDefault();
        let parentNode = e.target.parentNode.parentNode;
        let id = parentNode.firstElementChild.innerHTML;

        var href = url+'user/update/' + id;

        $.get(href, function (user, status) {
            debugger;
            $('.formDelete #id1').val(user.id);
            $('.formDelete #firstName1').val(user.firstName);
            $('.formDelete #lastName1').val(user.lastName);
            $('.formDelete #age1').val(user.age);
            $('.formDelete #userName1').val(user.userName);
            $('.formDelete #roles1').val(user.roles);

        });
        $('.formDelete #modalDelete').modal();

    });

});
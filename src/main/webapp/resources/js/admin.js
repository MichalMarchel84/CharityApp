location.href = "#institutions";
document.querySelector("#add").addEventListener("click", displayNew);
[...document.querySelectorAll("button[name='delete']")].forEach(btn => btn.addEventListener("click", deleteInstitution));
[...document.querySelectorAll("#users tr")].forEach(tr => tr.addEventListener("click", displayUserDetails));

function deleteInstitution(ev) {
    ev.preventDefault();
    if (confirm("Na pewno usunąć?")) {
        const form = ev.target.parentElement.parentElement;
        const id = form.querySelector("input[name='id']").value;
        fetch(`/admin/institutions/delete?id=${id}`,
            {}
        ).then(resp => {
            if (resp.ok) {
                form.parentElement.remove();
            } else console.log(resp.status);
        });
    }
}

function displayNew(ev) {
    const add = document.querySelector("#add").parentElement;
    add.remove();
    const cont = document.querySelector("div.address-cont");
    const div = document.createElement("div");
    div.className = "address";
    div.innerHTML = `<form method="post" action="/admin/institutions" id="new">
                    <input type="text" name="name" placeholder="Nazwa">
                    <textarea name="description" form="new" placeholder="Opis" rows="4"></textarea>
                    <div>
                        <input type="submit" value="Zapisz" class="btn btn--small">
                        <button name="delete" class="btn btn--small">Anuluj</button>
                    </div>
                    <input type="hidden" name="${cont.dataset.pn}" value="${cont.dataset.pt}"/>
                </form>`;
    div.querySelector("button").addEventListener("click", () => {
        div.remove();
        cont.append(add);
    });
    cont.append(div);
}

function displayUserDetails(ev) {
    const tr = ev.target.closest("tr");
    document.querySelector("div.dialog-box h2").innerText = "Edytuj użytkownika";
    const form = document.querySelector("div.dialog-box form");
    form.action = "/admin/user";
    const csrf = form.querySelector("#csrf");
    document.querySelector("div.dialog-box p").innerText = "";
    form.innerHTML =
        `<input type="hidden" name="id" value="${tr.dataset.id}">
            <input type="text" name="email" value="${tr.children[0].innerText}">
            <div>
                <select name="enabled">
                    <option value="1">Aktywny</option>
                    <option value="0">Zablokowany</option>
                </select>
                <select name="roles">
                    <option value="1">Administrator</option>
                    <option value="2">Użytkownik</option>
                </select>
                <button id="delete" class="btn" style="margin-left: 2em">Usuń</button>
            </div>
                <div>
                    <input type="submit" value="Zapisz" class="btn btn--large">
                    <button id="cancel" class="btn btn--large">Anuluj</button>
                </div>`;
    if(tr.children[1].innerText === "Aktywny"){
        form.querySelector("select[name='enabled']").children[0].selected = true;
    }else{
        form.querySelector("select[name='enabled']").children[1].selected = true;
    }
    if(tr.children[2].innerText === "Administrator"){
        form.querySelector("select[name='roles']").children[0].selected = true;
    }else{
        form.querySelector("select[name='roles']").children[1].selected = true;
    }
    form.append(csrf);
    form.querySelector("#delete").addEventListener("click", deleteUser);
    form.querySelector("#cancel").addEventListener("click", hide);
    form.addEventListener("submit", sendUserChange);
    document.querySelector("div.dialog-cont").style.display = "flex";
}

function sendUserChange(ev) {
    ev.preventDefault();
    const form = ev.target;
    fetch(form.action, {
        method: form.method,
        body: new URLSearchParams([...(new FormData(form))]),
    }).then(resp => {
        if(resp.ok) {
            document.querySelector("div.dialog-cont").style.display = "none";
            const id = form.querySelector("input[name='id']").value;
            const email = form.querySelector("input[name='email']").value;
            const status = form.querySelector("select[name='enabled']");
            const role = form.querySelector("select[name='roles']");
            const tr = document.querySelector(`#users tr[data-id='${id}']`);
            tr.children[0].innerText = email;
            tr.children[1].innerText = status.options[status.selectedIndex].innerText;
            tr.children[2].innerText = role.options[role.selectedIndex].innerText;
        }
        else {
            const msg = document.querySelector("div.dialog-box p");
            resp.text().then(txt => {
                msg.innerText = txt;
            });
        }
    });
}

function hide(event) {
    event.preventDefault();
    document.querySelector("div.dialog-cont").style.display = "none";
}

function deleteUser(ev) {
    ev.preventDefault();
    if(confirm("Na pewno usunąć?")) {
        const form = ev.target.closest("form");
        const id = form.querySelector("input[name='id']").value;
        fetch(`/admin/user/delete?id=${id}`,
            {}
        ).then(resp => {
            if (resp.ok) {
                document.querySelector("div.dialog-cont").style.display = "none";
                document.querySelector(`#users tr[data-id='${id}']`).remove();
            }
            else document.querySelector("div.dialog-box p").innerText = "Nie udało się usunąć użytkownika";
        });
    }
}
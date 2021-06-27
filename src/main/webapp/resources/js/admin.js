location.href = "#institutions";
document.querySelector("#add").addEventListener("click", displayNew);
[...document.querySelectorAll("button[name='delete']")].forEach(btn => btn.addEventListener("click", deleteInstitution));

function deleteInstitution(ev) {
    ev.preventDefault();
    if(confirm("Na pewno usunąć?")) {
        const form = ev.target.parentElement.parentElement;
        const id = form.querySelector("input[name='id']").value;
        fetch(`/admin/institutions/delete?id=${id}`,
            {}
        ).then(resp => {
            if (resp.ok) {
                form.parentElement.remove();
            }
            else console.log(resp.status);
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
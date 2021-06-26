location.href = "#address";
document.querySelector("#add").addEventListener("click", displayNew);
[...document.querySelectorAll("button[name='delete']")].forEach(btn => btn.addEventListener("click", deleteAddress));

function deleteAddress(ev) {
    ev.preventDefault();
    if(confirm("Na pewno usunąć?")) {
        const form = ev.target.parentElement.parentElement;
        const id = form.querySelector("input[name='id']").value;
        fetch(`/user/address/delete?id=${id}`,
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
    div.innerHTML = `<form method="post" action="/user/address">
                    <input type="text" name="street" placeholder="Ulica">
                    <input type="text" name="city" placeholder="Miasto">
                    <input type="text" name="postCode" placeholder="Kod pocztowy">
                    <input type="text" name="phone" placeholder="Telefon">
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
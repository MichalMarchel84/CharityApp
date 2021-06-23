document.querySelector("#changeEmail").addEventListener("click", displayEmailChange);
document.querySelector("#changePass").addEventListener("click", displayPassChange);

function displayEmailChange() {

    document.querySelector("div.dialog-box h2").innerText = "Zmień email";
    const form = document.querySelector("div.dialog-box form");
    form.action = "/user/change-email";
    const csrf = form.querySelector("input[type='hidden']");
    form.innerHTML =
        `<input type="text" name="email">
                <div>
                    <input type="submit" value="Zmień" class="btn btn--large">
                    <button class="btn btn--large">Anuluj</button>
                </div>`;
    form.append(csrf);
    form.querySelector("input[type='text']").value = document.querySelector("li.logged-user").innerText;
    form.querySelector("button").addEventListener("click", hide);
    form.addEventListener("submit", ev => sendChange(ev, true));
    document.querySelector("div.dialog-cont").style.display = "flex";
}

function displayPassChange() {
    document.querySelector("div.dialog-box h2").innerText = "Zmień hasło";
    const form = document.querySelector("div.dialog-box form");
    form.action = "/user/change-pass";
    const csrf = form.querySelector("input[type='hidden']");
    form.innerHTML =
        `<label>
            Nowe hasło
            <input type="password" name="pass">
        </label>
        <label>
            Powtórz hasło
        <input type="password" name="repeat">
        </label>
                <div>
                    <input type="submit" value="Zmień" class="btn btn--large">
                    <button class="btn btn--large">Anuluj</button>
                </div>`;
    form.append(csrf);
    form.querySelector("button").addEventListener("click", hide);
    form.addEventListener("submit", sendChange);
    document.querySelector("div.dialog-cont").style.display = "flex";
}

function sendChange(ev, changeName) {
    ev.preventDefault();
    const form = ev.target;
    fetch(form.action, {
        method: form.method,
        body: new URLSearchParams([...(new FormData(form))]),
    }).then(resp => {
        if(resp.ok) {
            document.querySelector("div.dialog-cont").style.display = "none";
            if(changeName){
                document.querySelector("li.logged-user").innerText = form.querySelector("input[type='text']").value;
            }
        }
        else {
            const msg = document.querySelector("div.dialog-box p");
            resp.text().then(txt => {
                msg.innerText = txt;
                document.querySelector("div.dialog-box").append(msg);
            });
        }
    });
}

function hide(event) {
    event.preventDefault();
    document.querySelector("div.dialog-cont").style.display = "none";
}
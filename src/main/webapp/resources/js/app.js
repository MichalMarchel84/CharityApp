document.addEventListener("DOMContentLoaded", function () {

    /**
     * Form Select
     */
    class FormSelect {
        constructor($el) {
            this.$el = $el;
            this.options = [...$el.children];
            this.init();
        }

        init() {
            this.createElements();
            this.addEvents();
            this.$el.parentElement.removeChild(this.$el);
        }

        createElements() {
            // Input for value
            this.valueInput = document.createElement("input");
            this.valueInput.type = "text";
            this.valueInput.name = this.$el.name;

            // Dropdown container
            this.dropdown = document.createElement("div");
            this.dropdown.classList.add("dropdown");

            // List container
            this.ul = document.createElement("ul");

            // All list options
            this.options.forEach((el, i) => {
                const li = document.createElement("li");
                li.dataset.value = el.value;
                li.innerText = el.innerText;

                if (i === 0) {
                    // First clickable option
                    this.current = document.createElement("div");
                    this.current.innerText = el.innerText;
                    this.dropdown.appendChild(this.current);
                    this.valueInput.value = el.value;
                    li.classList.add("selected");
                }

                this.ul.appendChild(li);
            });

            this.dropdown.appendChild(this.ul);
            this.dropdown.appendChild(this.valueInput);
            this.$el.parentElement.appendChild(this.dropdown);
        }

        addEvents() {
            this.dropdown.addEventListener("click", e => {
                const target = e.target;
                this.dropdown.classList.toggle("selecting");

                // Save new value only when clicked on li
                if (target.tagName === "LI") {
                    this.valueInput.value = target.dataset.value;
                    this.current.innerText = target.innerText;
                }
            });
        }
    }

    document.querySelectorAll(".form-group--dropdown select").forEach(el => {
        new FormSelect(el);
    });

    /**
     * Hide elements when clicked on document
     */
    document.addEventListener("click", function (e) {
        const target = e.target;
        const tagName = target.tagName;

        if (target.classList.contains("dropdown")) return false;

        if (tagName === "LI" && target.parentElement.parentElement.classList.contains("dropdown")) {
            return false;
        }

        if (tagName === "DIV" && target.parentElement.classList.contains("dropdown")) {
            return false;
        }

        document.querySelectorAll(".form-group--dropdown .dropdown").forEach(el => {
            el.classList.remove("selecting");
        });
    });

    /**
     * Switching between form steps
     */
    class FormSteps {
        constructor(form) {
            this.$form = form;
            this.$next = form.querySelectorAll(".next-step");
            this.$prev = form.querySelectorAll(".prev-step");
            this.$step = form.querySelector(".form--steps-counter span");
            this.currentStep = 1;

            this.$stepInstructions = form.querySelectorAll(".form--steps-instructions p");
            const $stepForms = form.querySelectorAll("form > div");
            this.slides = [...this.$stepInstructions, ...$stepForms];

            this.categories = [...document.querySelectorAll("div[data-step='1'] label")];

            this.init();
        }

        /**
         * Init all methods
         */
        init() {
            this.events();
            this.updateForm();

            this.categories.forEach(label => {
                const hidden = label.querySelector("input[type='hidden']");
                label.removeChild(hidden);
                label.appendChild(hidden);
            });

            const adr = document.querySelector("#adr");
            if(adr) {
                adr.addEventListener("change", evt => {
                    const inputs = document.querySelectorAll("div[data-step='4'] input[type='text']");
                    if (evt.target.value) {
                        document.querySelector("#adrSave").style.display = "none";
                        const vals = JSON.parse(evt.target.value);
                        document.querySelector('#adrId').value = vals.id;
                        inputs[0].value = vals.street;
                        inputs[1].value = vals.city;
                        inputs[2].value = vals.postCode;
                        inputs[3].value = vals.phone;

                    } else {
                        document.querySelector("#adrSave").style.display = "flex";
                        document.querySelector('#adrId').value = "";
                        for (let i = 0; i < 4; i++) {
                            inputs[i].value = "";
                        }
                    }
                });
            }
        }

        /**
         * All events that are happening in form
         */
        events() {
            // Next step
            this.$next.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    if (this.validate()) {
                        this.currentStep++;
                        this.updateForm();
                    }
                });
            });

            // Previous step
            this.$prev.forEach(btn => {
                btn.addEventListener("click", e => {
                    e.preventDefault();
                    this.currentStep--;
                    this.updateForm();
                });
            });

            // Form submit
            this.$form.querySelector("form").addEventListener("submit", e => this.submit(e));
        }

        /**
         * Checks if current step is error-free
         */
        validate() {
            let result = false;
            switch (this.currentStep) {
                case 1:
                    this.categories.forEach(cat => {
                        if (cat.querySelector("input[type='checkbox']").checked) {
                            document.querySelector("div[data-step='1'] p.errMsg").innerText = "";
                            result = true;
                        }
                    });
                    if(result) return true;
                    document.querySelector("div[data-step='1'] p.errMsg").innerText = "Zaznacz co najmniej jedną kategorię";
                    return false;
                case 2:
                    const bags = parseInt(document.querySelector("div[data-step='2'] input").value);
                    if(bags > 0){
                        document.querySelector("div[data-step='2'] p.errMsg").innerText = "";
                        result = true;
                    }
                    if(result) return true;
                    document.querySelector("div[data-step='2'] p.errMsg").innerText = "Musisz oddać co najmniej jeden worek";
                    return false;
                case 3:
                    [...document.querySelectorAll("div[data-step='3'] label")].forEach(label => {
                        if (label.querySelector("input[type=radio]").checked){
                            document.querySelector("div[data-step='3'] p.errMsg").innerText = "";
                            result = true;
                        }
                    });
                    if(result) return true;
                    document.querySelector("div[data-step='3'] p.errMsg").innerText = "Musisz wybrać instytucję";
                    return false;
                case 4:
                    result = true;
                    const inputs = document.querySelectorAll("div[data-step='4'] input[type='text'], div[data-step='4'] input[type='date'], div[data-step='4'] input[type='time']");
                    const errMsgs = document.querySelectorAll("div[data-step='4'] p.errMsg");
                    if(inputs[0].value.replaceAll(" ", "") === ""){
                        errMsgs[0].innerText = "Pole nie może być puste";
                        result = false;
                    }else errMsgs[0].innerText = "";
                    if(inputs[1].value.replaceAll(" ", "") === ""){
                        errMsgs[1].innerText = "Pole nie może być puste";
                        result = false;
                    }else errMsgs[1].innerText = "";
                    let regex = /^[0-9]{2}-[0-9]{3}$/;
                    if(!regex.test(inputs[2].value)){
                        errMsgs[2].innerText = "Nieprawidłowy kod pocztowy";
                        result = false;
                    }else errMsgs[2].innerText = "";
                    regex = /^ *$|^(\+[0-9]{2})?[0-9 ]{9,}$/;
                    if(!regex.test(inputs[3].value)){
                        errMsgs[3].innerText = "Nieprawidłowy numer telefonu";
                        result = false;
                    }else errMsgs[3].innerText = "";
                    regex = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                    if(!regex.test(inputs[4].value)){
                        errMsgs[4].innerText = "Nieprawidłowy email";
                        result = false;
                    }else errMsgs[4].innerText = "";
                    const dateForm = new Date(inputs[5].value);
                    const dateNow = new Date(Date.now());
                    if(!(dateForm > dateNow)){
                        errMsgs[5].innerText = "Nie możesz zamówić kuriera wcześniej niż na jutro";
                        result = false;
                    }else errMsgs[5].innerText = "";
                    const formHour = inputs[6].value.split(":")[0];
                    if((formHour < 8) || (formHour >= 20)){
                        errMsgs[6].innerText = "Kurier odbiera przesyłki od 8 do 20";
                        result = false;
                    }else errMsgs[6].innerText = "";
                    return result;
            }
        }

        /**
         * Update form front-end
         * Show next or previous section etc.
         */
        updateForm() {
            this.$step.innerText = this.currentStep;

            this.slides.forEach(slide => {
                slide.classList.remove("active");

                if (slide.dataset.step == this.currentStep) {
                    slide.classList.add("active");
                }
            });

            this.$stepInstructions[0].parentElement.parentElement.hidden = this.currentStep >= 5;
            this.$step.parentElement.hidden = this.currentStep >= 5;

            if (this.currentStep === 5) {
                const lists = document.querySelectorAll("div.summary ul");
                const donationDesc = lists[0].querySelectorAll("span.summary--text");
                const bags = parseInt(document.querySelector("div[data-step='2'] input").value);
                let wor_x = '';
                let zaw_x = '';
                const bagsLastNum = bags - Math.floor(bags / 10) * 10;
                if (bags === 1) {
                    wor_x = "worek";
                    zaw_x = "zawierający";
                } else if ((bagsLastNum === 0) || (bagsLastNum >= 5)) {
                    wor_x = "worków";
                    zaw_x = 'zawierających';
                } else if (bagsLastNum < 5) {
                    wor_x = "worki";
                    zaw_x = 'zawierające';
                }

                let cont = "";
                this.categories.forEach(cat => {
                    if (cat.querySelector("input[type='checkbox']").checked) {
                        cont += cat.querySelector('span.description').innerText + ", ";
                    }
                });
                cont = cont.slice(0, -2);
                donationDesc[0].innerText = `Oddajesz ${bags} ${wor_x} ${zaw_x} ${cont}`;
                [...document.querySelectorAll("div[data-step='3'] label")].forEach(label => {
                    if (label.querySelector("input[type=radio]").checked) {
                        donationDesc[1].innerText = 'Dla ' + label.querySelector('div.title').innerText;
                    }
                });

                const adrSummary = [...lists[1].querySelectorAll("li")];
                const adrForm = [...document.querySelectorAll("div[data-step='4'] input[type='text'], div[data-step='4'] input[type='date'], div[data-step='4'] input[type='time']")];
                for (let i = 0; i < adrSummary.length; i++){
                    adrSummary[i].innerText = adrForm[i].value;
                }

                const pickUpSummary = [...lists[2].querySelectorAll("li")];
                pickUpSummary[0].innerText = adrForm[5].value;
                pickUpSummary[1].innerText = adrForm[6].value;
                const msg = document.querySelector("div[data-step='4'] textarea").value;
                if(msg === "") pickUpSummary[2].innerText = "Brak uwag";
                else pickUpSummary[2].innerText = msg;
            }
        }

    }

    const form = document.querySelector(".form--steps");
    if (form !== null) {
        new FormSteps(form);
    }
});

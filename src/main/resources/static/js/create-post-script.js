    document.addEventListener("DOMContentLoaded", function () {
        // Markdown обработка
        const input = document.getElementById("markdownInput");
        const output = document.getElementById("output");

        if (input && output) {
            input.addEventListener("input", () => {
                output.innerHTML = marked.parse(input.value);
            });

            // Начальная инициализация
            output.innerHTML = marked.parse(input.value);
        }

        // Валидация формы и работа с паролем
        const togglePassword = document.getElementById("togglePassword");
        const passwordContainer = document.getElementById("password-container");
        const passwordInput = document.getElementById("password");
        const form = document.querySelector("form");

        if (togglePassword && passwordContainer && passwordInput) {
            togglePassword.addEventListener("change", function () {
                if (this.checked) {
                    passwordContainer.style.display = "block";
                    passwordInput.removeAttribute("disabled");
                    passwordInput.setAttribute("required", "required");
                } else {
                    passwordContainer.style.display = "none";
                    passwordInput.setAttribute("disabled", "disabled");
                    passwordInput.removeAttribute("required");
                    passwordInput.value = "";
                }
            });
        }

        if (form) {
            form.addEventListener("submit", function (e) {
                if (togglePassword.checked && passwordInput.value.trim() === "") {
                    e.preventDefault();
                    alert("Please enter a password when the checkbox is checked.");
                }
            });
        }
    });

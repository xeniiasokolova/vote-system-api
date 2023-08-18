const checkbox = document.getElementById("showPasswordCheckbox");
const inputFields = document.getElementById("inputFields");
const updateButton = document.getElementById("updateButton");
const form = document.forms[0];
const formElements = form.elements;

//Variable for storing the initial data of the form
let originalFormData = {};

function updateProfile(formId, method) {
    const errorMessageElement = document.getElementById("error-message");

    document.getElementById(formId).addEventListener("submit", function (event) {
        event.preventDefault(); // Stopping the form submission

        // Data preparation in JSON format
        const formData = new FormData(this);
        const jsonData = {};
        formData.forEach((value, key) => {
            // Convert "true" or "false" string to actual boolean
            if (key === "showPasswordCheckbox") {
                jsonData[key] = value === "true";
            } else {
                jsonData[key] = value;
            }
        });

        // Sending form data to the server
        fetch(this.action, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
            .then(response => {
                if (response.ok) {
                    // Redirect to the user list page
                    window.location.href = "/account";
                } else {
                    // Handling errors if update failed
                    response.text().then(errorMessage => {
                        if (errorMessage) {
                            errorMessageElement.textContent = errorMessage;
                            errorMessageElement.classList.remove("d-none"); // Show the element
                        } else {
                            errorMessageElement.textContent = "";
                            errorMessageElement.classList.add("d-none"); // Hide the element
                        }
                    });
                }
            });
    });
}

// Handle checkbox state change
checkbox.addEventListener("change", function () {
    if (checkbox.checked) {
        inputFields.style.display = "block";
    } else {
        inputFields.style.display = "none";
    }
    // Call the formChanged() function when the checkbox is changed
    formChanged();
});


// Function for filling originalFormData with data from form fields
function populateOriginalFormData() {
    for (let i = 0; i < formElements.length; i++) {
        const element = formElements[i];
        if (element.name) {
            originalFormData[element.name] = element.value;
        }
    }
}

// Function for comparing changes and activating/deactivating the "Refresh" button
function formChanged() {
    const currentFormData = {};

    // Updating currentFormData
    for (let i = 0; i < formElements.length; i++) {
        const element = formElements[i];
        if (element.name) {
            currentFormData[element.name] = element.value;
        }
    }

    // Check if there are any changes in the password input fields
    const passwordFieldsChanged = checkbox.checked && inputFields.style.display === "block";
    // Check if there are changes in other fields of the form
    const otherFieldsChanged = JSON.stringify(currentFormData) !== JSON.stringify(originalFormData);

    // If there are changes in the password input fields or other fields of the form, the "Update" button becomes active
    updateButton.disabled = !(passwordFieldsChanged || otherFieldsChanged);
}

// Проверяем localStorage при загрузке страницы
window.addEventListener("load", function () {
    const savedCheckboxValue = localStorage.getItem("showPasswordCheckbox");
    if (savedCheckboxValue === "true") {
        checkbox.checked = true;
        inputFields.style.display = "block";
    }

    // Заполняем originalFormData при загрузке страницы
    populateOriginalFormData();

    // Вызываем функцию formChanged() при загрузке страницы, чтобы актуализировать состояние кнопки "Обновить"
    formChanged();
});

function updateOrCreateUser(formId, method) {
    const errorMessageElement = document.getElementById("error-message");

    document.getElementById(formId).addEventListener("submit", function (event) {
        event.preventDefault(); // Stopping the form submission

        // Data preparation in JSON format
        const formData = new FormData(this);
        const jsonData = {};
        formData.forEach((value, key) => {
            jsonData[key] = value;
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
                    window.location.href = "/users";
                } else {
                    // Handling errors if update failed
                    response.text().then(errorMessage => {
                        if (errorMessage) {
                            errorMessageElement.textContent = errorMessage;
                            errorMessageElement.classList.remove("d-none"); // Показать элемент
                        } else {
                            errorMessageElement.textContent = "";
                            errorMessageElement.classList.add("d-none"); // Скрыть элемент
                        }
                    });
                }
            });
    });
}





//User delete function
function deleteUser(userId) {
    if (confirm("Are you sure you want to delete the user?")) {
        fetch(`/rest/users/${userId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    location.reload(); // Reload the page after successful deletion
                } else {
                    console.error("Error when deleting a user");
                }
            })
            .catch(error => {
                console.error("There's been an error:", error);
            });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-user-button");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const userId = button.getAttribute("data-user-id");
            if (userId) {
                deleteUser(userId); // Calling a function to delete
            }
        });
    });
});
// Function for dish update or create
function updateOrCreateDish(formId, method) {
    document.getElementById(formId).addEventListener("submit", function (event) {
        event.preventDefault(); // Stopping the form submission

        // Data preparation in JSON format
        const formData = new FormData(this);
        const jsonData = {};
        formData.forEach((value, key) => {
            jsonData[key] = value;
        });
        console.log(jsonData);
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
                    // Redirect to the restaurants list page
                    const restaurantId = document.getElementById("restaurantId").value;
                    location.href = `/restaurants/${restaurantId}/menu`;
                } else {
                    // Error handling if update failed
                    console.error("Failed to update or create dish");
                }
            });
    });
}


//Dish delete function
function deleteDish(restaurantId, dishId) {
    if (confirm("Are you sure you want to delete the dish?")) {
        fetch(`/rest/restaurants/${restaurantId}/menu/${dishId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    location.reload(); // Reload the page after successful deletion
                } else {
                    console.error("Error when deleting a dish");
                }
            })
            .catch(error => {
                console.error("There's been an error:", error);
            });
    }
}

// Function to handle voting for a dish
function voteForDish(data) {
    const { restaurantId, dishId, voteConfirmation, voteError } = data;

    if (!voteConfirmation && !voteError) {
        castVote(data);
    } else if (voteConfirmation && !voteError) {
        const userConfirmed = confirm("You've already voted today. Are you sure you want to change the vote?");
        if (userConfirmed) {
            castVote(data);
        }
    }
}

// Function to cast a vote for a dish
function castVote(data) {
    const formData = new FormData(); // Create a new FormData
    formData.append("restaurantId", data.restaurantId);
    formData.append("dishId", data.dishId);
    formData.append("voteConfirmation", data.voteConfirmation);
    formData.append("voteError", data.voteError);

    fetch(`/rest/restaurants/${data.restaurantId}/menu/${data.dishId}`, {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (response.ok) {
                window.location.href = `/restaurants/${data.restaurantId}/menu`;
            } else {
                response.text().then(errorMessage => {
                    console.error("Error when voting for the dish:", errorMessage);
                });
            }
        })
        .catch(error => {
            console.error("There's been an error:", error);
        });
}

/**
// Function to handle voting for a dish
function voteForDish(restaurantId, dishId, voteConfirmation, voteError) {

    if (!voteConfirmation && !voteError) {
        castVote(restaurantId, dishId, voteConfirmation, voteError);
    } else if (voteConfirmation && !voteError) {
        const userConfirmed = confirm("You've already voted today. Are you sure you want to change the vote?");
        if (userConfirmed) {
            castVote(restaurantId, dishId, voteConfirmation, voteError);
        }
    }
}

// Function to cast a vote for a dish
function castVote(restaurantId, dishId, voteConfirmation, voteError) {
    const formData = new FormData(); // Create a new FormData
    formData.append("restaurantId", restaurantId);
    formData.append("dishId", dishId);
    formData.append("voteConfirmation", voteConfirmation);
    formData.append("voteError", voteError);


    fetch(`/rest/restaurants/${restaurantId}/menu/${dishId}`, {
        method: "POST",
        body: formData
    })
        .then(response => {
            if (response.ok) {
                window.location.href = `/restaurants/${restaurantId}/menu`;
            } else {
                response.text().then(errorMessage => {
                    console.error("Error when voting for the dish:", errorMessage);
                });
            }
        })
        .catch(error => {
            console.error("There's been an error:", error);
        });
}
*/

document.addEventListener("DOMContentLoaded", function () {

    const deleteButtons = document.querySelectorAll(".delete-dish-button");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const dishId = button.getAttribute("data-dish-id");
            const restaurantId = button.getAttribute("data-restaurant-id");
            if (dishId) {
                deleteDish(restaurantId, dishId); // Calling a function to delete
            }
        });
    });

    const voteButtons = document.querySelectorAll(".vote-button");

    voteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const voteConfirmation = button.getAttribute("data-vote-confirmation") === "true";
            const voteError = button.getAttribute("data-vote-error") === "true";
            const restaurantId = button.getAttribute("data-restaurant-id");
            const dishId = button.getAttribute("data-dish-id");

            if (!voteError) {
                voteForDish({ restaurantId, dishId, voteConfirmation, voteError });
            }
        });
    });

   /** const voteButtons = document.querySelectorAll(".vote-button");

    voteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const voteConfirmation = button.getAttribute("data-vote-confirmation") === "true";
            const voteError = button.getAttribute("data-vote-error") === "true";
            const restaurantId = button.getAttribute("data-restaurant-id");
            const dishId = button.getAttribute("data-dish-id");

            if (!voteError) {
                voteForDish(restaurantId, dishId, voteConfirmation, voteError);
            }
        });
    });*/
});
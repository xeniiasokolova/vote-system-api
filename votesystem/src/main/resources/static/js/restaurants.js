// Function for restaurant update
function updateOrCreateRestaurant(formId, method) {

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
            method: method, // Используйте метод PUT
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
            .then(response => {
                if (response.ok) {
                    // Redirect to the restaurants list page
                    window.location.href = "/restaurants";
                } else {
                    // Error handling if update failed
                    console.error("Failed to update or create restaurant");
                }
            });
    });
 }

//Restaurant delete function
function deleteRestaurant(restaurantId) {
    if (confirm("Are you sure you want to delete the restaurant?")) {
        fetch(`/rest/restaurants/${restaurantId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    location.reload(); // Reload the page after successful deletion
                } else {
                    console.error("Error when deleting a restaurant");
                }
            })
            .catch(error => {
                console.error("There's been an error:", error);
            });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-restaurant-button");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const restaurantId = button.getAttribute("data-restaurant-id");
            if (restaurantId) {
                deleteRestaurant(restaurantId); // Calling a function to delete
            }
        });
    });
});



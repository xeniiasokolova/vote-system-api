//Vote delete function
function deleteVote(voteId) {
    if (confirm("Are you sure you want to delete the vote?")) {
        fetch(`/rest/votes/${voteId}`, {
            method: "DELETE"
        })
            .then(response => {
                if (response.ok) {
                    location.reload(); // Reload the page after successful deletion
                } else {
                    console.error("Error when deleting a vote");
                }
            })
            .catch(error => {
                console.error("There's been an error:", error);
            });
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const deleteButtons = document.querySelectorAll(".delete-vote-button");

    deleteButtons.forEach(button => {
        button.addEventListener("click", function (event) {
            event.preventDefault();

            const voteId = button.getAttribute("data-vote-id");
            if (voteId) {
                deleteVote(voteId); // Calling a function to delete
            }
        });
    });
});

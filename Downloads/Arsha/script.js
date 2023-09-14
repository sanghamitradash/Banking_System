document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("account-form");
    const submitButton = document.getElementById("submit-button");
    const successMessage = document.getElementById("success-message");

    submitButton.addEventListener("click", function () {
        // Client-side validation (simplified)
        const firstName = document.getElementById("first-name").value;
        const lastName = document.getElementById("last-name").value;
        const address = document.getElementById("address").value;
        const pinCode = document.getElementById("pin-code").value;

        if (firstName === "" || lastName === "" || address === "" || pinCode === "") {
            alert("Please fill in all fields.");
        } else {
            // Simulate a successful account creation
            successMessage.style.display = "block";
            form.reset(); // Clear the form
        }
    });
});

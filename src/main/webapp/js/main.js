// GlamBook front-end helpers (all real validation is done again on the server)

// Ask for confirmation before any form that has a data-confirm attribute is submitted.
// Used for every delete / cancel button.
document.addEventListener('submit', function (event) {
    const message = event.target.getAttribute('data-confirm');
    if (message && !confirm(message)) {
        event.preventDefault();
    }
});

// Date inputs with the class "no-past" cannot pick a date before today.
document.addEventListener('DOMContentLoaded', function () {
    const today = new Date();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const todayText = today.getFullYear() + '-' + month + '-' + day;

    document.querySelectorAll('input[type="date"].no-past').forEach(function (input) {
        input.min = todayText;
    });
});

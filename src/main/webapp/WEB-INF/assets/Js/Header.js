
        function updateDateTime() {
            var currentDate = new Date();
            var dateTimeString = currentDate.toLocaleString();
            document.getElementById("currentDateTime").textContent = dateTimeString;
        }

        // Update time every second
        setInterval(updateDateTime, 1000);

        // Initial call to display time immediately
        updateDateTime();

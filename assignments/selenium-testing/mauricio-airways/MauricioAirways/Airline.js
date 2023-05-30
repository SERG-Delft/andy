var darkMode = true;
var flights = {};
var reservedTickets = [];
var availableFlights = [
    {
        id: "TU-427",
        from: "TUD",
        to: "LAX",
        seatsAvailable: 15
    }, {
        id: "CSE-1110",
        from: "AMS",
        to: "GIG",
        seatsAvailable: 125
    }, {
        id: "OA-570",
        from: "ATH",
        to: "AMS",
        seatsAvailable: 1
    }, {
        id: "RO-D99",
        from: "OTP",
        to: "SOF",
        seatsAvailable: 327
    }
];
for (var _i = 0, availableFlights_1 = availableFlights; _i < availableFlights_1.length; _i++) {
    var flight = availableFlights_1[_i];
    flights[flight.id] = flight;
}
function bookCurrentlySelected() {
    var flightName = document.getElementById("selectedFlightName").textContent;
    var selectedSeats = parseInt(document.getElementById("selectedNumberOfSeats").value);
    if (isNaN(selectedSeats) || selectedSeats <= 0) {
        fancy_alert("Selected number of seats not possible.");
        return;
    }
    if (!flights.hasOwnProperty(flightName)) {
        fancy_alert("Flight does not exist / no flight selected.");
        return;
    }
    var flight = flights[flightName];
    if (flight.seatsAvailable < selectedSeats) {
        fancy_alert("Selected number of seats not possible.");
        return;
    }
    flight.seatsAvailable -= selectedSeats;
    document.getElementById("availableSeats").textContent = flight.seatsAvailable.toString();
    reservedTickets.push({ flight: flight, numberOfSeatsBooked: selectedSeats });
    visualizeTickets();
}
function visualizeTickets() {
    var ticketList = document.getElementById("ticketList");
    ticketList.innerHTML = "";
    for (var _i = 0, reservedTickets_1 = reservedTickets; _i < reservedTickets_1.length; _i++) {
        var ticket = reservedTickets_1[_i];
        var row = createTableRow([ticket.flight.id, ticket.flight.from, ticket.flight.to, ticket.numberOfSeatsBooked.toString()]);
        ticketList.appendChild(row);
    }
}
function fancy_alert(msg) {
    var alert = document.getElementById("alert");
    document.getElementById("alert-msg").textContent = msg;
    alert.style.display = "inherit";
    alert.style.opacity = "1";
}
function close_alert() {
    var alert = document.getElementById("alert");
    alert.style.opacity = "0";
    setTimeout(function () {
        alert.style.display = "none";
    }, 400);
}
function createTableRow(elements) {
    var parent = document.createElement("tr");
    for (var _i = 0, elements_1 = elements; _i < elements_1.length; _i++) {
        var element = elements_1[_i];
        var col = document.createElement("td");
        col.textContent = element;
        parent.appendChild(col);
    }
    return parent;
}
function flipDarkMode() {
    document.getElementsByTagName('link')[2].disabled = darkMode;
    darkMode = !darkMode;
}
function initialize() {
    var flightList = document.getElementById("flightList");
    var _loop_1 = function (flightId) {
        var flight = flights[flightId];
        var child = document.createElement("div");
        child.onclick = function () {
            document.getElementById("selectedFlightName").textContent = flight.id;
            document.getElementById("availableSeats").textContent = flight.seatsAvailable.toString();
        };
        // using textContent should hopefully not introduce xss
        // using innerHTML however might? might be good to be aware of
        child.textContent = "BOOK NOW!\r\nFlight: " + flightId + "\r\nFrom: " + flight.from + " To: " + flight.to;
        child.className = "selectFlight";
        flightList.appendChild(child);
    };
    for (var flightId in flights) {
        _loop_1(flightId);
    }
    document.getElementById("bookButton").onclick = bookCurrentlySelected;
    document.getElementById("closeErrorButton").onclick = close_alert;
    document.getElementById("alert").style.display = "none";
    document.getElementById("darkModeButton").onclick = flipDarkMode;
}
initialize();
//# sourceMappingURL=Airline.js.map
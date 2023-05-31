var events = [];
function addCalendarEvent() {
    var from = parseTime(document.getElementById('from').value);
    var to = parseTime(document.getElementById('to').value);
    var description = document.getElementById('description').value;
    if (from == null) {
        fancy_alert('Start time should be in the format HH:mm (e.g. 08:30)');
        return;
    }
    if (to == null) {
        fancy_alert('End time should be in the format HH:mm (e.g. 08:30)');
        return;
    }
    if (isAfterOrEqual(from, to)) {
        fancy_alert('The end time should be after the start time');
        return;
    }
    if (description == null || description.length == 0) {
        fancy_alert('Description should not be empty');
        return;
    }
    var event = {
        from: from,
        to: to,
        description: description
    };
    if (hasOverlap(event)) {
        fancy_alert('This event should not overlap with an existing event');
        return;
    }
    events.push(event);
    var child = document.createElement('div');
    child.className = 'event';
    var descriptionElement = document.createElement('h3');
    descriptionElement.textContent = description;
    var timeElement = document.createElement('p');
    timeElement.textContent = n(from.hour) + ":" + n(from.minute) + " - " + n(to.hour) + ":" + n(to.minute);
    child.appendChild(descriptionElement);
    child.appendChild(timeElement);
    document.getElementById('eventList').appendChild(child);
    document.getElementById('alert').style.display = 'none';
}
function n(i) {
    var s = i.toString();
    if (s.length == 1) {
        s = '0' + s;
    }
    return s;
}
function isBefore(t1, t2) {
    return t1.hour < t2.hour || t1.hour == t2.hour && t1.minute < t2.minute;
}
function isAfter(t1, t2) {
    return isBefore(t2, t1);
}
function isBeforeOrEqual(t1, t2) {
    return !isAfter(t1, t2);
}
function isAfterOrEqual(t1, t2) {
    return !isBefore(t1, t2);
}
function hasOverlap(e) {
    var overlap = false;
    events.forEach(function (c) {
        if (isBeforeOrEqual(e.from, c.to) && isAfterOrEqual(e.to, c.from)
            && !(isBefore(e.from, c.from) && isAfter(e.to, c.to))) {
            overlap = true;
        }
    });
    return overlap;
}
function parseTime(str) {
    var match = str.match(/^(\d{2}):(\d{2})$/);
    if (match === null) {
        return null;
    }
    var hour = parseInt(match[1]);
    var minute = parseInt(match[2]);
    if (hour > 23 || minute > 59) {
        return null;
    }
    return {
        hour: hour,
        minute: minute
    };
}
function fancy_alert(msg) {
    var alert = document.getElementById('alert');
    document.getElementById('alert-msg').textContent = msg;
    alert.style.display = 'inherit';
    alert.style.opacity = '1';
}
//# sourceMappingURL=TodaysEvents.js.map
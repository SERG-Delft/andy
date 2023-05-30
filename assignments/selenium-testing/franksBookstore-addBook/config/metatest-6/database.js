var books = {}
var authors = {}
authors[1] = "Mauricio Aniche";
authors[2] = "Dan Brown";
authors[3] = "J.R.R. Tolkien";
var ERROR_COLOR = "#f44336"
var SUCCESS_COLOR = "#6BF178"

function add_author(name) {
    if (name === "") {
        fancy_alert("Author name cannot be empty", ERROR_COLOR)
        return
    }

    var max = 0
    for (var id in authors) {
        var author_name = authors[id]
        if (name === author_name) {
            fancy_alert("Author with that name already exists!", ERROR_COLOR)
            return
        }
        max = id > max ? id : max
    }
    var newId = parseInt(max) + 1;
    authors[newId] = name
    fancy_alert("Author successfully added with id " + newId, SUCCESS_COLOR);
}

function add_book(id, name) {
    if (!name) {
        fancy_alert("Book name cannot be empty", ERROR_COLOR)
        return
    }

    if (!authors[id]) {
        fancy_alert("Author with that id does not exist", ERROR_COLOR)
        return
    }
    if (!books[id]) {
        books[id] = []
    }
    // might also want an error for duplicate protection
        for (var book_index in books[id]) {
        book_name = books[id][book_index]
        if (book_name === name) {
            fancy_alert("The author already has a book with that name!", ERROR_COLOR)
            return
        }
    }
    books[id] = [name]
    fancy_alert("Book was successfully added!", SUCCESS_COLOR)
}

function display_authors(query) {
    // clear current children divs
    var list_div = document.getElementById("author_list")
    list_div.innerHTML = ""  // garbage collection go brrrr

    var author_found = false
    for (var id in authors) {
        var name = authors[id]
        if (!name.includes(query)) {
            continue
        }

        var child = document.createElement("div");
        // using textContent should hopefully not introduce xss
        // using innerHTML however might? might be good to be aware of
        child.textContent = id + " : " + name

        list_div.appendChild(child)

        author_found = true
    }

    if (!author_found) {
        var child = document.createElement("div")
        child.textContent = "No matching authors were found!"

        list_div.appendChild(child)
    }
}

function display_books(query) {
    // clear current children divs
    var list_div = document.getElementById("book_list")
    list_div.innerHTML = ""  // garbage collection go brrrr

    var book_found = false
    for (var id in books) {
        var names = books[id]
                for (var index in names) {
            var name = names[index]
            if (!name.includes(query)) {
                continue
            }

            var child = document.createElement("div");
            child.textContent = "\"" + name + "\" by: " + authors[id]

            list_div.appendChild(child)

            book_found = true
        }
    }

    if (!book_found) {
        var child = document.createElement("div");

        child.textContent = "No matching books were found!"

        list_div.appendChild(child)
    }
}

function fancy_alert(msg, color) {
    var alert = document.getElementById("alert")

    if (color) alert.style.backgroundColor = color
    document.getElementById("alert-msg").textContent = msg
    alert.style.display = "inherit"
    alert.style.opacity = "1"
}

function close_alert() {
    var alert = document.getElementById("alert")

    alert.style.opacity = "0"
    setTimeout(function () {
        alert.style.display = "none"
        alert.style.backgroundColor = "#DCDCDC"
    }, 400)
}

document.getElementById('author_submit_button').onclick = function () {
    add_author(document.getElementById('author_name').value)
}
document.getElementById('book_submit_button').onclick = function () {
    add_book(parseInt(document.getElementById('author_id').value), document.getElementById('book_name').value)
}

document.getElementById("search_bar").addEventListener("keyup", function(event) {
    if (event.key === "Enter") {
        document.getElementById("search_results").style.visibility = "visible"

        var query = document.getElementById("search_bar").value
        display_books(query)
        display_authors(query)
    }
})

document.getElementById("close_icon").addEventListener("click", function() {
    document.getElementById("search_results").style.visibility = "hidden"
})

class ApiService {
    fetchListAuthor = () => fetch("/api/author")
        .then(response => response.json());

    fetchAuthor = authorId => fetch("/api/author/" + authorId)
        .then(response => {
            return this.responseHandle(response, "Автор не найден")
        });

    saveAuthor = author => fetch("/api/author", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify(author)
    })
        .then(response => {
            return this.responseHandle(response, "Ошибка при сохранении автора")
        });

    deleteAuthor = authorId => fetch("/api/author/" + authorId, {
        method: "DELETE"
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при удалении автора");
        });

    fetchListGenre = () => fetch("/api/genre")
        .then(response => response.json());

    fetchGenre = genreId => fetch("/api/genre/" + genreId)
        .then(response => {
            return this.responseHandle(response, "Жанр не найден");
        })

    saveGenre = genre => fetch("/api/genre", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify(genre)
    })
        .then(response => {
            return this.responseHandle(response, "Ошибка при сохранении жанра")
        });

    deleteGenre = genreId => fetch("/api/genre/" + genreId, {
        method: "DELETE"
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при удалении жанра")
        });

    fetchListBook = () => fetch("/api/book")
        .then(response => response.json());

    fetchBook = bookId => fetch("/api/book/" + bookId)
        .then(response => {
            return this.responseHandle(response, "Книга не найдена")
        });

    saveBook = book => fetch("/api/book", {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify(book)
    });

    deleteBook = bookId => fetch("/api/book/" + bookId, {
        method: "DELETE"
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при удалении книги.")
        });

    fetchListComment = bookId => fetch("/api/comment/list/" + bookId)
        .then(response => response.json());

    fetchComment = commentId => fetch("/api/comment/" + commentId)
        .then(response => response.json());

    saveComment = (bookId, commentBook) => fetch("/api/comment/" + bookId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8"
        },
        body: JSON.stringify(commentBook)
    });

    deleteComment = commentId => fetch("/api/comment/" + commentId, {
        method: "DELETE"
    });

    errorHandle = (response, messageError) => {
        if (!(response.ok)) {
            throw new Error(messageError);
        }
    };

    responseHandle = (response, messageError) => {
        if (response.ok) {
            return response.json()
        } else {
            throw new Error(messageError);
        }
    }

}

export default new ApiService();
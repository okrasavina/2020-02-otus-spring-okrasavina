class ApiService {
    fetchListAuthor = () => fetch('/api/author')
        .then(response => response.json());

    fetchAuthor = authorNumber => fetch('/api/author/' + authorNumber)
        .then(response => {
            return this.errorHandle(response, "Автор не найден")
        });

    saveAuthor = author => fetch('/api/author', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(author)
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при сохранении автора")
        });

    deleteAuthor = author => fetch('/api/author', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(author)
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при удалении автора");
        });

    fetchListGenre = () => fetch('/api/genre')
        .then(response => response.json());

    fetchGenre = genreNumber => fetch('/api/genre/' + genreNumber)
        .then(response => {
            return this.errorHandle(response, "Жанр не найден");
        })

    saveGenre = genre => fetch('/api/genre', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(genre)
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при сохранении жанра")
        });

    deleteGenre = genre => fetch('/api/genre', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(genre)
    })
        .then(response => {
            return this.errorHandle(response, "Ошибка при удалении жанра")
        });

    fetchListBook = () => fetch('/api/book')
        .then(response => response.json());

    fetchBook = bookNumber => fetch('/api/book/' + bookNumber)
        .then(response => {
            return this.errorHandle(response, "Книга не найдена")
        });

    saveBook = book => fetch('/api/book', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(book)
    });

    deleteBook = book => fetch('/api/book', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(book)
    });

    fetchListComment = bookNumber => fetch('/api/comment/' + bookNumber)
        .then(response => response.json());

    fetchComment = commentNumber => fetch('/api/comment/get/' + commentNumber)
        .then(response => response.json());

    saveComment = (bookNumber, commentBook) => fetch('/api/comment/' + bookNumber, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(commentBook)
    });

    deleteComment = (bookNumber, commentBook) => fetch('/api/comment/' + bookNumber, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify(commentBook)
    });

    errorHandle = (response, messageError) => {
        if (response.ok) {
            return response.json()
        } else {
            throw new Error(messageError);
        }
    };
}

export default new ApiService();
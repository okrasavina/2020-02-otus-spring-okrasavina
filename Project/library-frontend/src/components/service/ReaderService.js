class ReaderService {
    fetchListBook = () => fetch("/api/shelf/book")
        .then(response => response.json());
}

export default new ReaderService();
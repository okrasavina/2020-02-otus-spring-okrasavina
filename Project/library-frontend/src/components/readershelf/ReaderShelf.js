import React from 'react';
import ReaderService from '../service/ReaderService';

export default class ReaderShelf extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            books: [],
            isEmpty: true
        };
    }

    componentDidMount() {
        ReaderService.fetchListBook()
            .then(books => {
                this.setState({books});
                this.setState({isEmpty: books.size === 0})
            })
    }

    returnBook = number => {
        console.log("return");
    };

    showComments = number => {
        console.log("showComments");
    };

    getBookForReading = () => {
        console.log("getBookForReading");
    };

    render() {
        return (
            <div>
                <h1 className={"hidden"}>Моя книжная полка</h1>
                {this.state.isEmpty && <p>Книжная полка пуста</p>}
                {!this.state.isEmpty && <table className={"object-list"}>
                    <caption>Моя книжная полка</caption>
                    <thead>
                    <tr>
                        <th>Номер</th>
                        <th>Наименование книги</th>
                        <th colSpan="2">Возможные действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.books.map((book, number) => (
                                <tr key={number}>
                                    <td className={"counter"}/>
                                    <td>{book.title}</td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.returnBook(book.number)}>Вернуть
                                        </button>
                                    </td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.showComments(book.number)}>Комментарии
                                        </button>
                                    </td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>}
                <form>
                    <button onClick={() => this.getBookForReading()}>Выбрать книгу</button>
                </form>
            </div>
        )
    }

}
import React from 'react';
import ApiService from '../ApiService';

export default class BookList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            books: [],
            isEmpty: true
        };
    }

    componentDidMount() {
        ApiService.fetchListBook()
            .then(books => {
                this.setState({books});
                this.setState({isEmpty: books.size === 0})
            });
    }

    editBook = number => {
        window.localStorage.setItem('bookNumber', number);
        this.props.history.push('/book/edit');
    };

    deleteBook = number => {
        window.localStorage.setItem('bookNumber', number);
        this.props.history.push('/book/delete');
    };

    addNewBook() {
        window.localStorage.removeItem('bookNumber');
        this.props.history.push('/book/new');
    }

    showComments = (number, title) => {
        window.localStorage.setItem('bookNumber', number);
        window.localStorage.setItem('bookTitle', title);
        this.props.history.push('/comment');
    };

    render() {
        return (
            <div>
                <h1 className={'hidden'}>Список книг</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={'object-list'}>
                    <caption>Список книг</caption>
                    <thead>
                    <tr>
                        <th>Номер</th>
                        <th>Наименование</th>
                        <th>Автор</th>
                        <th colSpan='3'>Возможные действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.books.map((book, number) => (
                                <tr key={number}>
                                    <td>{book.number}</td>
                                    <td>{book.title}</td>
                                    <td>{book.authors}</td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.editBook(book.number)}>Изменить
                                        </button>
                                    </td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.showComments(book.number, book.title)}>Комментарии
                                        </button>
                                    </td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.deleteBook(book.number)}>Удалить
                                        </button>
                                    </td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>}
                <form>
                    <button onClick={() => this.addNewBook()}>Добавить</button>
                </form>
            </div>
        );
    }
}
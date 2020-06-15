import React from "react";

export default class AuthorList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authors: [],
            isEmpty: true
        };
        this.editAuthor = this.editAuthor.bind(this);
        this.deleteAuthor = this.deleteAuthor.bind(this);
        this.addNewAuthor = this.addNewAuthor.bind(this);
    };

    componentDidMount() {
        fetch('/api/author')
            .then(response => response.json())
            .then(authors => {
                this.setState({authors});
                this.setState({isEmpty: authors.size === 0})
            });
    }

    editAuthor(number) {
        window.localStorage.setItem("authorNumber", number);
        this.props.history.push('/author/edit');
    }

    deleteAuthor(number) {
        window.localStorage.setItem("authorNumber", number);
        this.props.history.push('/author/delete');
    }

    addNewAuthor() {
        window.localStorage.removeItem("authorNumber");
        this.props.history.push('/author/new');
    }

    render() {
        return (
            <div>
                <h1 className={'hidden'}>Список авторов</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={'object-list'}>
                    <caption>Список авторов</caption>
                    <thead>
                    <tr>
                        <th>Номер</th>
                        <th>Полное имя</th>
                        <th colSpan="2">Возможные действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.authors.map((author, number) => (
                                <tr key={number}>
                                    <td>{author.number}</td>
                                    <td>{author.name}</td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.editAuthor(author.number)}>Изменить
                                        </button>
                                    </td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.deleteAuthor(author.number)}>Удалить
                                        </button>
                                    </td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>}
                <form>
                    <button onClick={() => this.addNewAuthor()}>Добавить</button>
                </form>
            </div>
        );
    }
}
import React from 'react';
import ApiService from '../ApiService';

export default class AuthorList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            authors: [],
            isEmpty: true
        };
    };

    componentDidMount() {
        ApiService.fetchListAuthor()
            .then(authors => {
                this.setState({authors});
                this.setState({isEmpty: authors.size === 0})
            });
    }

    editAuthor = id => {
        window.localStorage.setItem("authorId", id);
        this.props.history.push("/author/edit");
    };

    deleteAuthor = id => {
        window.localStorage.setItem("authorId", id);
        this.props.history.push("/author/delete");
    };

    addNewAuthor = () => {
        window.localStorage.removeItem("authorId");
        this.props.history.push("/author/new");
    };

    render() {
        return (
            <div>
                <h1 className={"hidden"}>Список авторов</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={"object-list"}>
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
                        this.state.authors.map((author, id) => (
                                <tr key={id}>
                                    <td className={"counter"}/>
                                    <td>{author.name}</td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.editAuthor(author.id)}>Изменить
                                        </button>
                                    </td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.deleteAuthor(author.id)}>Удалить
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
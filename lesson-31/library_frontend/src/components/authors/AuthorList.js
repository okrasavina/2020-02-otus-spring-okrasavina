import React from 'react';
import ApiService from '../ApiService';

export default class AuthorList extends React.Component {
    _embedded;
    _links;
    constructor(props) {
        super(props);
        this.state = {
            authors: [],
            isEmpty: true
        };
    };

    componentDidMount() {
        ApiService.fetchListAuthor()
            .then(lines => {
                let authors = lines._embedded.authors;
                this.setState({authors});
                this.setState({isEmpty: authors.size === 0})
            });
    }

    editAuthor = href => {
        href = href.replace("http://localhost:8080", "");
        window.localStorage.setItem("authorHref", href);
        this.props.history.push("/author/edit");
    };

    deleteAuthor = href => {
        href = href.replace("http://localhost:8080", "");
        window.localStorage.setItem("authorHref", href);
        this.props.history.push("/author/delete");
    };

    addNewAuthor = () => {
        window.localStorage.removeItem("authorHref");
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
                                                onClick={() => this.editAuthor(author._links.self.href)}>Изменить
                                        </button>
                                    </td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.deleteAuthor(author._links.self.href)}>Удалить
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
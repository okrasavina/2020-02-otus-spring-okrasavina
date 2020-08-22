import React from 'react';
import ApiService from '../ApiService';

export default class GenreList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            genres: [],
            isEmpty: true
        };
    }

    componentDidMount() {
        ApiService.fetchListGenre()
            .then(lines => {
                let genres = lines._embedded.genres;
                this.setState({genres});
                this.setState({isEmpty: genres.size === 0})
            })
    }

    editGenre = href => {
        href = href.replace("http://localhost:8080", "");
        window.localStorage.setItem("genreHref", href);
        this.props.history.push("/genre/edit");
    };

    deleteGenre = href => {
        href = href.replace("http://localhost:8080", "");
        window.localStorage.setItem("genreHref", href);
        this.props.history.push("/genre/delete");
    };

    addNewGenre = () => {
        window.localStorage.removeItem("genreHref");
        this.props.history.push("/genre/new");
    };

    render() {
        return (
            <div>
                <h1 className={"hidden"}>Список жанров</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={"object-list"}>
                    <caption>Список жанров</caption>
                    <thead>
                    <tr>
                        <th>Номер</th>
                        <th>Наименование</th>
                        <th colSpan="2">Возможные действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.genres.map((genre, id) => (
                                <tr key={id}>
                                    <td className={"counter"}/>
                                    <td>{genre.name}</td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.editGenre(genre._links.self.href)}>Изменить
                                        </button>
                                    </td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.deleteGenre(genre._links.self.href)}>Удалить
                                        </button>
                                    </td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>}
                <form>
                    <button onClick={() => this.addNewGenre()}>Добавить</button>
                </form>
            </div>
        );
    }
}
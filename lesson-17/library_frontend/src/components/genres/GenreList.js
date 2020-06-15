import React from "react";

export default class GenreList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            genres: [],
            isEmpty: true
        };
        this.editGenre = this.editGenre.bind(this);
        this.deleteGenre = this.deleteGenre.bind(this);
        this.addNewGenre = this.addNewGenre.bind(this);
    }

    componentDidMount() {
        fetch('/api/genre')
            .then(response => response.json())
            .then(genres => {
                this.setState({genres});
                this.setState({isEmpty: genres.size === 0})
            })
    }

    editGenre(number) {
        window.localStorage.setItem("genreNumber", number);
        this.props.history.push('/genre/edit');
    }

    deleteGenre(number) {
        window.localStorage.setItem("genreNumber", number);
        this.props.history.push('/genre/delete');
    }

    addNewGenre() {
        window.localStorage.removeItem("genreNumber");
        this.props.history.push('/genre/new');
    }

    render() {
        return (
            <div>
                <h1 className={'hidden'}>Список жанров</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={'object-list'}>
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
                        this.state.genres.map((genre, number) => (
                                <tr key={number}>
                                    <td>{genre.number}</td>
                                    <td>{genre.name}</td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.editGenre(genre.number)}>Изменить
                                        </button>
                                    </td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.deleteGenre(genre.number)}>Удалить
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
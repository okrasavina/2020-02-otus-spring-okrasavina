import React from 'react';
import ApiService from '../ApiService';

export default class Genre extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: "",
            name: "",
            description: ""
        };
    }

    componentDidMount() {
        if (this.props.modeOpen !== 1) {
            let genreId = window.localStorage.getItem("genreId");
            ApiService.fetchGenre(genreId)
                .then(genre => {
                    this.setState({
                        id: genre.id,
                        name: genre.name,
                        description: genre.description
                    });
                })
                .catch((e) => {
                    console.log(e);
                    window.localStorage.setItem("messageError", e);
                    this.props.history.push("/error")
                });
        }
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    cancel = () => {
        window.localStorage.removeItem("genreId");
        this.props.history.push("/genre");
    };

    submitGenreHandler = () => {
        window.localStorage.removeItem("genreId");
        if (this.props.modeOpen === 3) {
            this.deleteGenre();
        } else {
            this.saveGenre();
        }
    };

    saveGenre = () => {
        let genre = {id: this.state.id, name: this.state.name, description: this.state.description};
        ApiService.saveGenre(genre)
            .then(this.props.history.push("/genre"))
            .catch(e => {
                window.localStorage.setItem("messageError", e);
                this.props.history.push("/error")
            });
    };

    deleteGenre = () => {
        ApiService.deleteGenre(this.state.id)
            .then(this.props.history.push("/genre"))
            .catch(e => {
                window.localStorage.setItem("messageError", e);
                this.props.history.push("/error")
            });
    };

    render() {
        const readOnly = this.props.modeOpen === 3;
        return (
            <div>
                <form id={"edit-form"}>
                    <h1>Литературный жанр</h1>
                    <div className={"row"}>
                        <label htmlFor={"id-input"}>Идентификатор:&nbsp;</label>
                        <input className={"readonly"} id={"id-input"} name={"id"} type={"text"} readOnly={true}
                               value={this.state.id}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"name-input"}>Наименование:&nbsp;</label>
                        <input className={readOnly ? "readonly" : null} id={"name-input"} name={"name"} type={"text"}
                               readOnly={readOnly} onChange={this.onChange} value={this.state.name}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"description-input"}>Описание:&nbsp;</label>
                        <textarea cols={30} className={readOnly ? "readonly" : null} rows={5} id={"description-input"}
                                  name={"description"} readOnly={readOnly} onChange={this.onChange}
                                  value={this.state.description}/>
                    </div>

                    <div className={"row"}>
                        <button onClick={() => this.submitGenreHandler()}>{readOnly ? "Удалить" : "Сохранить"}</button>
                        <button className={"btn-right"} onClick={() => this.cancel()}>Отмена</button>
                    </div>
                </form>
            </div>
        );
    }
}
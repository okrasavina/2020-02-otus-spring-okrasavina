import React from 'react';
import ApiService from '../ApiService';

export default class Author extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: "",
            name: "",
            birthDay: ""
        };
    }

    componentDidMount() {
        if (this.props.modeOpen !== 1) {
            let authorId = window.localStorage.getItem("authorId");
            ApiService.fetchAuthor(authorId)
                .then(author => {
                    this.setState({
                        id: author.id,
                        name: author.name,
                        birthDay: author.birthDay
                    })
                })
                .catch(e => {
                    window.localStorage.setItem("messageError", e);
                    this.props.history.push("/error")
                });
        }
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    cancel = () => {
        window.localStorage.removeItem("authorId");
        this.props.history.push("/author");
    };

    submitHandler = () => {
        window.localStorage.removeItem("authorId");
        if (this.props.modeOpen === 3) {
            this.deleteAuthor();
        } else {
            this.saveAuthor();
        }
    };

    saveAuthor = () => {
        let author = {id: this.state.id, name: this.state.name, birthDay: this.state.birthDay};
        ApiService.saveAuthor(author)
            .then(this.props.history.push("/author"))
            .catch(e => {
                window.localStorage.setItem("messageError", e);
                this.props.history.push("/error")
            });
    };

    deleteAuthor = () => {
        ApiService.deleteAuthor(this.state.id)
            .then(this.props.history.push("/author"))
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
                    <h1>Автор</h1>
                    <div className={"row"}>
                        <label htmlFor={"id-input"}>Идентификатор:&nbsp;</label>
                        <input className={"readonly"} id={"id-input"} name={"id"} type={"text"} readOnly={true}
                               value={this.state.id}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"name-input"}>Полное имя:&nbsp;</label>
                        <input className={readOnly ? "readonly" : null} id={"name-input"} name={"name"} type={"text"}
                               readOnly={readOnly} onChange={this.onChange} value={this.state.name}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"birthDay-input"}>Дата рождения:&nbsp;</label>
                        <input className={readOnly ? "readonly" : null} id={"birthDay-input"} name={"birthDay"}
                               type={"text"} onChange={this.onChange} value={this.state.birthDay}/>
                    </div>

                    <div className={"row"}>
                        <button type={"submit"}
                                onClick={() => this.submitHandler()}>{readOnly ? "Удалить" : "Сохранить"}</button>
                        <button className={"btn-right"} onClick={() => this.cancel()}>Отмена</button>
                    </div>
                </form>
            </div>
        );
    }
}
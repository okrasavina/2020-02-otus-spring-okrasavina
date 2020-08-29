import React from 'react';
import ApiService from '../service/ApiService';

export default class Book extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            number: "0",
            title: "",
            authors: [],
            genres: "",
            description: ""
        };
    }

    componentDidMount() {
        if (this.props.modeOpen !== 1) {
            let bookNumber = window.localStorage.getItem("bookNumber");
            ApiService.fetchBook(bookNumber)
                .then(book => {
                    this.setState({
                        number: book.number,
                        title: book.title,
                        authors: book.authors,
                        genres: book.genres,
                        description: book.description
                    })
                });
        }
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    cancel = () => {
        window.localStorage.removeItem("bookNumber");
        this.props.history.push("/book");
    };

    submitBookHandler = () => {
        window.localStorage.removeItem("bookNumber");
        if (this.props.modeOpen === 3) {
            this.deleteBook();
        } else {
            this.saveBook();
        }
    };

    saveBook = () => {
        let book = {
            number: this.state.number, title: this.state.title, authors: this.state.authors,
            genres: this.state.genres, description: this.state.description
        };
        ApiService.saveBook(book)
            .then(this.props.history.push("/book"));
    };

    deleteBook = () => {
        let book = {
            number: this.state.number, title: this.state.title, authors: this.state.authors,
            genres: this.state.genres, description: this.state.description
        };
        ApiService.deleteBook(book)
            .then(this.props.history.push("/book"));
    };

    render() {
        const readOnly = this.props.modeOpen === 3;
        return (
            <div>
                <form id={"edit-form"}>
                    <h1>Книга</h1>
                    <div className={"row"}>
                        <label htmlFor={"id-input"}>Номер:&nbsp;</label>
                        <input className={"readonly"} id={"id-input"} name={"number"} type={"text"} readOnly={true}
                               value={this.state.number}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"title-input"}>Наименование:&nbsp;</label>
                        <input className={readOnly ? "readonly" : null} id={"title-input"} name={"title"}
                               type={"text"}
                               readOnly={readOnly} onChange={this.onChange} value={this.state.title}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"authors-input"}>Автор:&nbsp;</label>
                        <input className={readOnly ? "readonly" : null} id={"authors-input"} name={"authors"}
                               type={"text"}
                               readOnly={readOnly} onChange={this.onChange} value={this.state.authors}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"genres-input"}>Жанр:&nbsp;</label>
                        <input className={readOnly ? "readonly" : null} id={"genres-input"} name={"genres"}
                               type={"text"}
                               readOnly={readOnly} onChange={this.onChange} value={this.state.genres}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"description-input"}>Описание:&nbsp;</label>
                        <textarea cols={30} className={readOnly ? "readonly" : null} rows={5}
                                  id={"description-input"}
                                  name={"description"} readOnly={readOnly} onChange={this.onChange}
                                  value={this.state.description}/>
                    </div>

                    <div className={"row"}>
                        <button
                            onClick={() => this.submitBookHandler()}>{readOnly ? "Удалить" : "Сохранить"}</button>
                        <button className={"btn-right"} onClick={() => this.cancel()}>Отмена</button>
                    </div>
                </form>
            </div>
        );
    }
}
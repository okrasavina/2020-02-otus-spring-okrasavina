import React from 'react';
import ApiService from '../ApiService';

export default class CommentBook extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            id: "",
            textComment: ""
        };
    }

    componentDidMount() {
        if (this.props.modeOpen !== 1) {
            let commentId = window.localStorage.getItem("commentId");
            ApiService.fetchComment(commentId)
                .then(commentBook => {
                    this.setState({
                        id: commentBook.id,
                        textComment: commentBook.textComment
                    })
                });
        }
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    cancel = () => {
        window.localStorage.removeItem("commentId");
        this.props.history.push("/comment");
    };

    submitCommentHandler = () => {
        window.localStorage.removeItem("commentId");
        let bookId = window.localStorage.getItem("bookId");
        if (this.props.modeOpen === 3) {
            this.deleteComment();
        } else {
            this.saveComment(bookId);
        }
    };

    saveComment = bookId => {
        let commentBook = {id: this.state.id, textComment: this.state.textComment};
        ApiService.saveComment(bookId, commentBook)
            .then(this.props.history.push("/comment"));
    };

    deleteComment = () => {
        ApiService.deleteComment(this.state.id)
            .then(this.props.history.push("/comment"));
    };

    render() {
        const bookTitle = window.localStorage.getItem("bookTitle");
        const captionList = "Комментарий к книге \"" + bookTitle + "\"";
        const readOnly = this.props.modeOpen === 3;
        return (
            <div>
                <form id={"edit-form"}>
                    <h1>{captionList}</h1>
                    <div className={"row"}>
                        <label htmlFor={"id-input"}>Идентификатор:&nbsp;</label>
                        <input className={"readonly"} id={"id-input"} name={"id"} type={"text"} readOnly={true}
                               value={this.state.id}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"textComment-input"}>Текст комментария:&nbsp;</label>
                        <textarea cols={30} className={readOnly ? "readonly" : null} rows={5} id={"textComment-input"}
                                  name={"textComment"} readOnly={readOnly} onChange={this.onChange}
                                  value={this.state.textComment}/>
                    </div>

                    <div className={"row"}>
                        <button
                            onClick={() => this.submitCommentHandler()}>{readOnly ? "Удалить" : "Сохранить"}</button>
                        <button className={"btn-right"} onClick={() => this.cancel()}>Отмена</button>
                    </div>
                </form>
            </div>
        );
    }
}
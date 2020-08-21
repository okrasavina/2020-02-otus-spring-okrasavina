import React from 'react';
import ApiService from '../ApiService';

export default class CommentList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            commentsBook: [],
            isEmpty: true
        };
    }

    componentDidMount() {
        let bookNumber = window.localStorage.getItem("bookId");
        ApiService.fetchListComment(bookNumber)
            .then(commentsBook => {
                this.setState({commentsBook});
                this.setState({isEmpty: commentsBook.size === 0})
            });
    }

    editComment = commentId => {
        window.localStorage.setItem("commentId", commentId);
        this.props.history.push("/comment/edit");
    };

    deleteComment = commentId => {
        window.localStorage.setItem("commentId", commentId);
        this.props.history.push("/comment/delete");
    };

    addNewComment = () => {
        window.localStorage.removeItem("commentId");
        this.props.history.push("/comment/new");
    };

    returnToBookList = () => {
        window.localStorage.removeItem("bookId");
        window.localStorage.removeItem("bookTitle");
        this.props.history.push("/book");
    };

    render() {
        const bookTitle = window.localStorage.getItem("bookTitle");
        const captionList = "Комментарии к книге \"" + bookTitle + "\"";
        return (
            <div>
                <h1 className={"hidden"}>Список жанров</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={"object-list"}>
                    <caption>{captionList}</caption>
                    <thead>
                    <tr>
                        <th>Номер</th>
                        <th>Текст комментария</th>
                        <th colSpan="2">Возможные действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    {
                        this.state.commentsBook.map((commentBook, id) => (
                                <tr key={id}>
                                    <td className={"counter"}/>
                                    <td>{commentBook.textComment}</td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.editComment(commentBook.id)}>Изменить
                                        </button>
                                    </td>
                                    <td className={"row-action"}>
                                        <button className={"btn-action"}
                                                onClick={() => this.deleteComment(commentBook.id)}>Удалить
                                        </button>
                                    </td>
                                </tr>
                            )
                        )
                    }
                    </tbody>
                </table>}
                <form>
                    <button onClick={() => this.addNewComment()}>Добавить</button>
                    <button className={"btn-right"} onClick={() => this.returnToBookList()}>Вернуться к списку книг
                    </button>
                </form>
            </div>
        );
    }
}
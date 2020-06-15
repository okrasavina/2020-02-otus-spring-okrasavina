import React from "react";

export default class CommentList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            commentsBook: [],
            isEmpty: true
        };
        this.editComment = this.editComment.bind(this);
        this.deleteComment = this.deleteComment.bind(this);
        this.addNewComment = this.addNewComment.bind(this);
        this.returnToBookList = this.returnToBookList.bind(this);
    }

    componentDidMount() {
        let bookNumber = window.localStorage.getItem("bookNumber");
        fetch('/api/comment/' + bookNumber)
            .then(response => response.json())
            .then(commentsBook => {
                this.setState({commentsBook});
                this.setState({isEmpty: commentsBook.size === 0})
            });
    }

    editComment(number) {
        window.localStorage.setItem("commentNumber", number);
        this.props.history.push('/comment/edit');
    }

    deleteComment(number) {
        window.localStorage.setItem("commentNumber", number);
        this.props.history.push('/comment/delete');
    }

    addNewComment() {
        window.localStorage.removeItem("commentNumber");
        this.props.history.push('/comment/new');
    }

    returnToBookList() {
        window.localStorage.removeItem("bookNumber");
        window.localStorage.removeItem("bookTitle");
        this.props.history.push('/book');
    }

    render() {
        const bookTitle = window.localStorage.getItem('bookTitle');
        const captionList = 'Комментарии к книге \'' + bookTitle + '\'';
        console.log(this.state.commentsBook.length);
        console.log(this.state.commentsBook);
        return (
            <div>
                <h1 className={'hidden'}>Список жанров</h1>
                {this.state.isEmpty && <p>Список пуст</p>}
                {!this.state.isEmpty && <table className={'object-list'}>
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
                        this.state.commentsBook.map((commentBook, number) => (
                                <tr key={number}>
                                    <td>{commentBook.number}</td>
                                    <td>{commentBook.textComment}</td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.editComment(commentBook.number)}>Изменить
                                        </button>
                                    </td>
                                    <td className={'row-action'}>
                                        <button className={'btn-action'}
                                                onClick={() => this.deleteComment(commentBook.number)}>Удалить
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
                    <button className={'btn-right'} onClick={() => this.returnToBookList()}>Вернуться к списку книг
                    </button>
                </form>
            </div>
        );
    }
}
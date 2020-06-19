import React from 'react';
import ApiService from '../ApiService';

export default class CommentBook extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            number: '0',
            textComment: ''
        };
    }

    componentDidMount() {
        if (this.props.modeOpen !== 1) {
            let commentNumber = window.localStorage.getItem('commentNumber');
            ApiService.fetchComment(commentNumber)
                .then(commentBook => {
                    this.setState({
                        number: commentBook.number,
                        textComment: commentBook.textComment
                    })
                });
        }
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    cancel = () => {
        window.localStorage.removeItem('commentNumber');
        this.props.history.push('/comment');
    };

    submitCommentHandler = () => {
        window.localStorage.removeItem('commentNumber');
        let bookNumber = window.localStorage.getItem('bookNumber');
        if (this.props.modeOpen === 3) {
            this.deleteComment(bookNumber);
        } else {
            this.saveComment(bookNumber);
        }
    };

    saveComment = bookNumber => {
        let commentBook = {number: this.state.number, textComment: this.state.textComment};
        ApiService.saveComment(bookNumber, commentBook)
            .then(this.props.history.push('/comment'));
    };

    deleteComment = bookNumber => {
        let commentBook = {number: this.state.number, textComment: this.state.textComment};
        ApiService.deleteComment(bookNumber, commentBook)
            .then(this.props.history.push('/comment'));
    };

    render() {
        const bookTitle = window.localStorage.getItem('bookTitle');
        const captionList = 'Комментарий к книге \'' + bookTitle + '\'';
        const readOnly = this.props.modeOpen === 3;
        return (
            <div>
                <form id={'edit-form'}>
                    <h1>{captionList}</h1>
                    <div className={'row'}>
                        <label htmlFor={'id-input'}>Номер:&nbsp;</label>
                        <input className={'readonly'} id={'id-input'} name={'number'} type={'text'} readOnly={true}
                               value={this.state.number}/>
                    </div>
                    <div className={'row'}>
                        <label htmlFor={'textComment-input'}>Текст комментария:&nbsp;</label>
                        <textarea cols={30} className={readOnly ? 'readonly' : null} rows={5} id={'textComment-input'}
                                  name={'textComment'} readOnly={readOnly} onChange={this.onChange}
                                  value={this.state.textComment}/>
                    </div>

                    <div className={'row'}>
                        <button
                            onClick={() => this.submitCommentHandler()}>{readOnly ? 'Удалить' : 'Сохранить'}</button>
                        <button className={'btn-right'} onClick={() => this.cancel()}>Отмена</button>
                    </div>
                </form>
            </div>
        );
    }
}
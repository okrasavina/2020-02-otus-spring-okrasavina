import React from "react";

export default class Author extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            number: '0',
            name: '',
            birthDay: ''
        };
        this.cancel = this.cancel.bind(this);
        this.submitHandler = this.submitHandler.bind(this);
        this.saveAuthor = this.saveAuthor.bind(this);
        this.deleteAuthor = this.deleteAuthor.bind(this);
        this.onChange = this.onChange.bind(this);
    }

    componentDidMount() {
        if (this.props.modeOpen !== 1) {
            let authorNumber = window.localStorage.getItem('authorNumber');
            fetch('/api/author/' + authorNumber)
                .then(response => {
                    if (!response.ok) throw new Error("Автор не найден");
                    else return response.json()
                })
                .then(author => {
                    this.setState({number: author.number});
                    this.setState({name: author.name});
                    this.setState({birthDay: author.birthDay})
                })
                .catch(e => {
                    window.localStorage.setItem("messageError", e);
                    this.props.history.push('/error')
                });
        }
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    }

    cancel() {
        window.localStorage.removeItem("authorNumber");
        this.props.history.push('/author');
    }

    submitHandler() {
        window.localStorage.removeItem("authorNumber");
        if (this.props.modeOpen === 3) {
            this.deleteAuthor();
        } else {
            this.saveAuthor();
        }
    }

    saveAuthor() {
        let author = {number: this.state.number, name: this.state.name, birthDay: this.state.birthDay};
        fetch('/api/author/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(author)
        })
            .then(response => {
                if (!response.ok) throw new Error("Ошибка при сохранении автора");
                else return response.json()
            })
            .then(this.props.history.push('/author'))
            .catch(e => {
                window.localStorage.setItem("messageError", e);
                this.props.history.push('/error')
            });
    }

    deleteAuthor() {
        let author = {number: this.state.number, name: this.state.name, birthDay: this.state.birthDay};
        fetch('/api/author/delete', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(author)
        })
            .then(response => {
                if (!response.ok) throw new Error("Ошибка при удалении автора");
                else return response.json()
            })
            .then(this.props.history.push('/author'))
            .catch(e => {
                window.localStorage.setItem("messageError", e);
                this.props.history.push('/error')
            });
    }

    render() {
        const readOnly = this.props.modeOpen === 3;
        return (
            <div>
                <form id={'edit-form'}>
                    <h1>Автор</h1>
                    <div className={'row'}>
                        <label htmlFor={'id-input'}>Номер:&nbsp;</label>
                        <input className={'readonly'} id={'id-input'} name={'number'} type={'text'} readOnly={true}
                               value={this.state.number}/>
                    </div>
                    <div className={'row'}>
                        <label htmlFor={'name-input'}>Полное имя:&nbsp;</label>
                        <input className={readOnly ? 'readonly' : null} id={'name-input'} name={'name'} type={'text'}
                               readOnly={readOnly} onChange={this.onChange} value={this.state.name}/>
                    </div>
                    <div className={'row'}>
                        <label htmlFor={'birthDay-input'}>Дата рождения:&nbsp;</label>
                        <input className={readOnly ? 'readonly' : null} id={'birthDay-input'} name={'birthDay'}
                               type={'text'} onChange={this.onChange} value={this.state.birthDay}/>
                    </div>

                    <div className={'row'}>
                        <button type={'submit'}
                                onClick={() => this.submitHandler()}>{readOnly ? 'Удалить' : 'Сохранить'}</button>
                        <button className={'btn-right'} onClick={() => this.cancel()}>Отмена</button>
                    </div>
                </form>
            </div>
        );
    }
}
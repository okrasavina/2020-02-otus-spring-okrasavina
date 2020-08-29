import React from 'react';
import AuthService from '../service/AuthService';

export default class Register extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            email: "",
            successful: false,
            message: ""
        };
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    };

    registerHandle = e => {
        e.preventDefault();
        let user = {username: this.state.username, email: this.state.email, password: this.state.password};
        this.setState({
            successful: true,
            message: ""
        })
        AuthService.register(user).then(
            () => {
                this.props.history.push("/login");
            },
            error => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                this.setState({
                    successful: false,
                    message: resMessage
                });
            }
        );

    };

    render() {
        return (
            <div className={"login"}>
                <h2>Регистрация нового пользователя</h2>
                <form>
                    <div className={"row"}>
                        <label htmlFor={"username"}>Логин</label>
                        <input id={"username"} name={"username"} type={"text"} placeholder={"username"}
                               autoComplete={"false"}
                               onChange={this.onChange} value={this.state.username}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"password"}>Пароль</label>
                        <input id={"password"} name={"password"} type={"password"} placeholder={"password"}
                               autoComplete={"false"}
                               onChange={this.onChange} value={this.state.password}/>
                    </div>
                    <div className={"row"}>
                        <label htmlFor={"email"}>Email</label>
                        <input id={"email"} name={"email"} type={"email"} placeholder={"email"}
                               autoComplete={"false"}
                               onChange={this.onChange} value={this.state.email}/>
                    </div>
                    <div>
                        <button onClick={(e) => this.registerHandle(e)}>Зарегистрироваться</button>
                    </div>
                    {this.state.message && (
                        <div className="form-group">
                            <div className="alert alert-danger" role="alert">
                                {this.state.message}
                            </div>
                        </div>
                    )}
                </form>
            </div>
        )
    }
}
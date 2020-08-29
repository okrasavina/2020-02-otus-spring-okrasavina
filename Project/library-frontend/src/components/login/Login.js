import React from 'react';
import AuthService from '../service/AuthService';

export default class Login extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            username: "",
            password: "",
            loading: false,
            message: ""
        };
    }

    onChange = (e) => {
        this.setState({[e.target.name]: e.target.value});
    };

    loginHandler = e => {
        e.preventDefault()
        this.setState({
            message: "",
            loading: true
        });
        let user = {username: this.state.username, password: this.state.password};
        AuthService.login(user).then(
            () => {
                this.props.history.push("/");
                window.location.reload();
            },
            error => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                this.setState({
                    loading: false,
                    message: resMessage
                });
            }
        );

    };

    registerNewUser = (e) => {
        e.preventDefault();
        this.props.history.push("/register");
    };

    render() {
        return (
            <div className={"login"}>
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
                    <div>
                        <button onClick={(e) => this.loginHandler(e)}>Войти</button>
                        <button className={"btn-right"} onClick={(e) => this.registerNewUser(e)}>Заригистрироваться
                        </button>
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
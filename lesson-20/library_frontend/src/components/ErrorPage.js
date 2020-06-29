import React from 'react';

export default class ErrorPage extends React.Component {
    render() {
        const message = window.localStorage.getItem("messageError");
        window.localStorage.removeItem("messageError");
        return (
            <div className={"error"}>
                <p>{message}</p>
            </div>
        )
    }
}
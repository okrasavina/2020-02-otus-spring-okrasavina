import React from 'react';
import {Link} from 'react-router-dom';
import '../App.css';

export default class Navigation extends React.Component {
    render() {
        return (
            <main>
                <h1 className={"welcome-text"}>Добро пожаловать в библиотеку</h1>
                <p>Здесь вы можете отдохнуть от трудового дня и почитать книгу.</p>
                <ul>
                    <li><Link to={"/reader-shelf"}>Моя книжная полка</Link></li>
                    <li><Link to={"/wish"}>Мои пожелания</Link></li>
                </ul>
                <h2>Здесь вы можете скорректировать базу данных этой библиотеки.</h2>
                <p>Списки:</p>
                <ul>
                    <li><Link to={"/author"}>авторы</Link></li>
                    <li><Link to={"/genre"}>жанры</Link></li>
                    <li><Link to={"/book"}>книги</Link></li>
                </ul>
            </main>
        );
    }
}

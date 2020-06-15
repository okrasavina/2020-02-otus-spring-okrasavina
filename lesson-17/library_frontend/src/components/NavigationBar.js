import React from "react";
import LibraryImg from "../img/library.png";
import {Link} from "react-router-dom";

export default function NavigationBar() {
    return (
        <nav>
            <Link to={'/'}>
                <img src={LibraryImg} alt="Библиотека" title="На главную страницу" width="100%"/>
            </Link>
        </nav>
    );
}
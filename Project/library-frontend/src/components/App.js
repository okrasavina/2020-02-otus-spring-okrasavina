import React from "react";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import NavigationBar from './NavigationBar';
import Footer from './Footer';
import Navigation from './Navigation';
import AuthorList from './authors/AuthorList';
import GenreList from './genres/GenreList';
import Author from './authors/Author';
import BookList from './books/BookList';
import Genre from './genres/Genre';
import Book from './books/Book';
import CommentList from './comments/CommentList';
import CommentBook from './comments/CommentBook';
import ErrorPage from './ErrorPage';
import {PrivateRoute} from "./PrivateRoute";
import Login from "./login/Login";
import ReaderShelf from "./readershelf/ReaderShelf";
import Register from "./login/Register";

export default () => (
    <Router>
        <div>
            <NavigationBar/>
            <div className={"library-body"}>
                <Switch>
                    <Route exact path={"/"} component={Navigation}/>
                    <Route exact path={"/author"} component={AuthorList}/>
                    <PrivateRoute path={"/author/new"}
                           render={props => <Author modeOpen={1} {...props} />}/>
                    <PrivateRoute path={"/author/edit"}
                           render={props => <Author modeOpen={2} {...props} />}/>
                    <PrivateRoute path={"/author/delete"}
                           render={props => <Author modeOpen={3} {...props} />}/>
                    <PrivateRoute exact path={"/genre"} component={GenreList}/>
                    <PrivateRoute path={"/genre/new"}
                           render={props => <Genre modeOpen={1} {...props} />}/>
                    <PrivateRoute path={"/genre/edit"}
                           render={props => <Genre modeOpen={2} {...props} />}/>
                    <PrivateRoute path={"/genre/delete"}
                           render={props => <Genre modeOpen={3} {...props} />}/>
                    <PrivateRoute exact path={"/book"} component={BookList}/>
                    <PrivateRoute path={"/book/new"}
                           render={props => <Book modeOpen={1} {...props} />}/>
                    <PrivateRoute path={"/book/edit"}
                           render={props => <Book modeOpen={2} {...props} />}/>
                    <PrivateRoute path={"/book/delete"}
                           render={props => <Book modeOpen={3} {...props} />}/>
                    <PrivateRoute exact path={"/comment"} component={CommentList}/>
                    <PrivateRoute path={"/comment/new"}
                           render={props => <CommentBook modeOpen={1} {...props} />}/>
                    <PrivateRoute path={"/comment/edit"}
                           render={props => <CommentBook modeOpen={2} {...props} />}/>
                    <PrivateRoute path={"/comment/delete"}
                           render={props => <CommentBook modeOpen={3} {...props} />}/>
                    <PrivateRoute path={"/error"} component={ErrorPage}/>
                    <PrivateRoute path={"/reader-shelf"} component={ReaderShelf}/>
                    <Route path={"/login"} component={Login}/>
                    <Route path={"/register"} component={Register}/>
                </Switch>
            </div>
            <Footer/>
        </div>
    </Router>
)

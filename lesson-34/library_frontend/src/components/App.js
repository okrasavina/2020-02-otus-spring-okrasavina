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

export default () => (
    <Router>
        <div>
            <NavigationBar/>
            <div className={"library-body"}>
                <Switch>
                    <Route exact path={"/"} component={Navigation}/>
                    <Route exact path={"/author"} component={AuthorList}/>
                    <Route path={"/author/new"}
                           render={props => <Author modeOpen={1} {...props} />}/>
                    <Route path={"/author/edit"}
                           render={props => <Author modeOpen={2} {...props} />}/>
                    <Route path={"/author/delete"}
                           render={props => <Author modeOpen={3} {...props} />}/>
                    <Route exact path={"/genre"} component={GenreList}/>
                    <Route path={"/genre/new"}
                           render={props => <Genre modeOpen={1} {...props} />}/>
                    <Route path={"/genre/edit"}
                           render={props => <Genre modeOpen={2} {...props} />}/>
                    <Route path={"/genre/delete"}
                           render={props => <Genre modeOpen={3} {...props} />}/>
                    <Route exact path={"/book"} component={BookList}/>
                    <Route path={"/book/new"}
                           render={props => <Book modeOpen={1} {...props} />}/>
                    <Route path={"/book/edit"}
                           render={props => <Book modeOpen={2} {...props} />}/>
                    <Route path={"/book/delete"}
                           render={props => <Book modeOpen={3} {...props} />}/>
                    <Route exact path={"/comment"} component={CommentList}/>
                    <Route path={"/comment/new"}
                           render={props => <CommentBook modeOpen={1} {...props} />}/>
                    <Route path={"/comment/edit"}
                           render={props => <CommentBook modeOpen={2} {...props} />}/>
                    <Route path={"/comment/delete"}
                           render={props => <CommentBook modeOpen={3} {...props} />}/>
                    <Route path={"/error"} component={ErrorPage}/>
                </Switch>
            </div>
            <Footer/>
        </div>
    </Router>
)

import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.css';
import Header from "./component/Header";
import Home from "./page/Home";
import Login from "./page/Login";
import Signup from "./page/Signup";
import './App.css'

function App() {
    return (
        <Router>
            <Header/>
            <Routes>
                <Route path="/" element={<Home/>}/>
                <Route path="/login" element={<Login/>}/>
                <Route path="/signup" element={<Signup/>}/>
            </Routes>
        </Router>
    );
}

export default App;

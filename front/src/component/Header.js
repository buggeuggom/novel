import React from 'react';
import {Link, useNavigate} from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import {Button} from "react-bootstrap";


const Header = () => {
    const isLoggedIn = !!localStorage.getItem('token');
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/');
    };

    return (
        <Navbar bg="dark" data-bs-theme="dark">
            <Container>
                <Navbar.Brand href="/">노벨맨</Navbar.Brand>
                <Nav className="me-auto">
                    <Nav.Link href="/">Home</Nav.Link>
                    <Nav.Link href="/subscribe">구독</Nav.Link>
                    {isLoggedIn ? (
                        <Nav.Link href="/">소설 등록</Nav.Link>
                    ) : (<></>)}
                </Nav>
                {isLoggedIn ? (
                    <Button variant="outline-danger" onClick={handleLogout}>Log out</Button>
                ) : (
                    <Button href="/login" variant="outline-info">Log In</Button>
                )}
            </Container>
        </Navbar>
    );
};

export default Header;
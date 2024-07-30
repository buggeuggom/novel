import React, {useState} from 'react';
import {Button, Form} from "react-bootstrap";
import {loginUser} from "../api/AuthApi";
import {useNavigate} from "react-router-dom";

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin =  async (e) => {
        e.preventDefault();

        const data = {email: email, password: password}

        await loginUser(data);

        navigate('/');
        return null;
    }

    return (
        <Form className="w-50 h-50 align-middle">
            <Form.Group className="mb-3" controlId="formGroupEmail">
                <Form.Label>Email address</Form.Label>
                <Form.Control type="email" onChange={(e)=> setEmail(e.target.value)} placeholder="Enter email"/>
            </Form.Group>
            <Form.Group className="mb-3" controlId="formGroupPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" onChange={(e)=> setPassword(e.target.value)} placeholder="Password"/>
            </Form.Group>
            <Button variant="primary" onClick={handleLogin} type="submit">Login</Button>{" "}
            <Button variant="secondary"  href="/signup" type="submit">Signup</Button>
        </Form>
    );
};

export default Login;
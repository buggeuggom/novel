import React, {useState} from 'react';
import {Button, Form} from "react-bootstrap";
import {useNavigate} from "react-router-dom";
import {loginUser, signupUser} from "../api/AuthApi";

const Signup = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const navigate = useNavigate();

    const handleLogin =  async (e) => {
        e.preventDefault();

        const data = {email: email, password: password, name: name}

        const response = await signupUser(data);

        navigate('/login');
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
            <Form.Group className="mb-3" controlId="formGroupName">
                <Form.Label>Name</Form.Label>
                <Form.Control type="name" onChange={(e)=> setName(e.target.value)} placeholder="Name"/>
            </Form.Group>
            <Button variant="primary" onClick={handleLogin} type="submit">submit</Button>
        </Form>
    );
};

export default Signup;
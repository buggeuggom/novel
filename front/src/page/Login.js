import React from 'react';
import {Button, Form} from "react-bootstrap";

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');


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
            <Button variant="primary" onClick={} type="submit">Login</Button>{" "}
            <Button variant="secondary" type="submit">Signup</Button>
        </Form>
    );
};

export default Login;
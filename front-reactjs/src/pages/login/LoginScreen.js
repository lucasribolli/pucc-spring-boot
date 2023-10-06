import './LoginScreenStyle.css';
import React, { useEffect } from "react";
import { Button, Card, CardContent} from "@material-ui/core";
import Input from '@mui/joy/Input';
import ApiService from "../../api/ApiService"
import { useState } from 'react';

export default function LoginScreen() {
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")
    const apiInstance = ApiService();

    const login = async () => {
        const data = {
            username: username,
            password: password
        }

        const result = await apiInstance.userLogin(data);
        
        if (!result?.data?.email) {
            return alert("Usuário não cadastrado no sistema! Entre em contato com um adminstrador.");
        }
        localStorage.setItem("token", result?.data?.token)

        alert("Logado com sucesso.");

        return window.location.replace("/reserves-list")
    }

    useEffect(() => {
        localStorage.removeItem("token")
    }, [])

    return (
        <div className="login__main-login">
            <Card sx={{ width: 500 }}>
                <CardContent>
                    <div className="pageTitle">
                        <span className="titleText">Login</span>
                        <span>Faça o Login com sua conta para entrar.</span>
                        <div className="separator" />
                    </div>
                    <div className="login__input-row">
                        <Input placeholder="User" onChange={(e) => setUsername(e.target.value)} />
                    </div>
                    <div className="login__input-row">
                        <Input type="password" placeholder="Senha" onChange={(e) => setPassword(e.target.value)}/>
                    </div>
                    <div className="login__input-row">
                        <Button
                            onClick={() => login()}
                            variant="contained"
                        >
                            Entrar
                        </Button>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
};

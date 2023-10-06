import './UserScreenStyle.css';
import React, {useEffect, useState} from "react";
import ApiService from "../../api/ApiService";
import {
    Button,
    Card,
    CardContent,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    TextField,
} from "@material-ui/core";
import { Add, ArrowBack } from "@material-ui/icons";

import Modal from 'react-modal';
Modal.setAppElement('#root');


const customStyles = {
    content: {
      top: '50%',
      left: '50%',
      right: 'auto',
      bottom: 'auto',
      marginRight: '-50%',
      transform: 'translate(-50%, -50%)',
    },
  };

const roleMap = {
    ROLE_USER: "Padrão",
    ROLE_ADMIN: "Admin"
}

export default function UserScreen() {
    const [usersList, setUsersList] = useState([]);
    const [modalIsOpen, setIsOpen] = useState(false);

    const [userName, setUserName] = useState("");
    const [userEmail, setUserEmail] = useState("");
    const [userPassword, setUserPassword] = useState("");
    const [userRole, setUserRole] = useState("ROLE_USER");
    const apiInstance = ApiService();

    const getData = async () => {
        const result = await apiInstance.getUsers();
        console.log(result)

        if (!result.data) {
            alert("Houve um erro! Tente novamente mais tarde");
            return;
        }
        return setUsersList(result.data);
    };

    const createUser = async () => {
        const data = {
            username: userName,
            email: userEmail,
            role: [ userRole ],
            password: userPassword
        };

        const result = await apiInstance.createUser(data);

        console.log(result)
        if (result?.ok && result?.data.message) {
            alert("Usuário cadastrado com sucesso!")
            await getData().then()
            return setIsOpen(false)
        }
        
        alert("Tente novamente mais tarde!")
    }

    useEffect(() => {
        getData().then()
        // eslint-disable-next-line
    }, []);

    return (
        <div className="componentColumn">
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={() => setIsOpen(false)}
                style={customStyles}
                contentLabel="Example Modal"
            >
                <div className="modalColumn">
                    <div className="modalTitle">
                        <Button
                            onClick={() => setIsOpen(false)}
                            variant="text"
                            color="inherit"
                            startIcon={<ArrowBack />}>
                            Criação de Novo usuário
                        </Button>
                    </div>
                    <div className="separator" />
                    <div className="registerInputRow">
                        <TextField
                            className="registerInput"
                            label="Nome"
                            placeholder="Ex.: Fulano"
                            variant="outlined"
                            type="text"
                            onChange={e => setUserName(e.target.value)}
                            onBlur={e => setUserName(e.target.value)}
                            value={userName}
                            sx={{m: "10px"}}
                        />
                        <TextField
                            className="registerInput"
                            label="Email"
                            placeholder="Ex.: fulano@email.com"
                            variant="outlined"
                            type="email"
                            onChange={e => setUserEmail(e.target.value)}
                            onBlur={e => setUserEmail(e.target.value)}
                            value={userEmail}
                            sx={{m: "10px"}}
                        />
                    </div>
                    <div className="registerInputRow">
                        <TextField
                            className="registerInput"
                            label="Senha"
                            placeholder="Ex.: Teste123@"
                            variant="outlined"
                            type="text"
                            onChange={e => setUserPassword(e.target.value)}
                            onBlur={e => setUserPassword(e.target.value)}
                            value={userPassword}
                            sx={{m: "10px"}}
                        />
                        <FormControl sx={{ minWidth: 223 }}>
                            <InputLabel id="status" sx={{m: "10px"}}>Status</InputLabel>
                            <Select
                                className="registerInput"
                                labelId="status"
                                id="statusId"
                                value={userRole}
                                label="Status"
                                onChange={e => setUserRole(e.target.value)}
                                onBlur={e => setUserRole(e.target.value)}
                                sx={{m: "10px"}}
                            >
                                <MenuItem value={"ROLE_USER"}>Padrão</MenuItem>
                                <MenuItem value={"ROLE_ADMIN"}>Adminstrador</MenuItem>
                            </Select>
                        </FormControl>
                    </div>
                    <div className="registerInputRow">
                        <Button
                            disabled={
                                userName === "" ||
                                userEmail === "" ||
                                userPassword === "" ||
                                userRole === ""
                            }
                            onClick={() => createUser()}
                            variant="contained"
                            startIcon={<Add />}>
                            Criar Usuário
                        </Button>
                    </div>
                </div>
            </Modal>
            <div className="rowAtEnd">
                <Button
                    onClick={() => setIsOpen(true)}
                    variant="contained"
                    endIcon={<Add />}>
                    Novo Usuário
                </Button>
            </div>
            <Card sx={{ width: 1000 }}>
                <CardContent>
                    <div className="pageTitle">
                        <span className="titleText">Listagem de Usuários</span>
                        <span>Verifique aqui todas os usuários cadastrados.</span>
                        <div className="separator"/>
                    </div>
                    {usersList.length > 0 ? usersList.map((item, idx) => {
                        return (
                            <div key={idx}>
                                <div className="inputRow">
                        
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Usuário</span>
                                        <span>{item.username}</span>
                                    </div>
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Email</span>
                                        <span>{item.email}</span>
                                    </div>
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Função</span>
                                        <span>{roleMap[item.roles[0].name]}</span>
                                    </div>
                                </div>
                                {
                                    usersList.length - 1 !== idx && (
                                        <div className="separator" />
                                    )
                                }
                            </div>
                        );
                    }) : (
                        <div className="pageTitle">
                            <span><b>Nenhum Usuário cadastrado!</b></span>
                        </div>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}

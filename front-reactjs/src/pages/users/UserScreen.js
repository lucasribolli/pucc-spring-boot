import './UserScreenStyle.css';
import React, {useEffect, useState} from "react";
import {
    Button,
    Card,
    CardContent,
} from "@material-ui/core";
import {DeleteForever, Check} from "@material-ui/icons";
import ApiService from "../../api/ApiService";

export default function UserScreen() {
    const [reserveList, setReserveList] = useState([]);
    const apiInstance = ApiService();
    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [role, setRole] = useState("ROLE_ADMIN");
    const [password, setPassword] = useState("123456789");

    const getData = async () => {
        const result = await apiInstance.getReserves();
        console.log(result)

        if (!result.data) {
            alert("Houve um erro! Tente novamente mais tarde");
            return;
        }
        return setReserveList(result.data);
    };

    useEffect(() => {
        getData().then()
        // eslint-disable-next-line
    }, []);

    const updateReserveStatus = async (status, reserve_id) => {
        await apiInstance.editReserveStatus({
            reserve_id : reserve_id,
            approved: status
        });

        await getData().then()
    };

    const createUser = async () => {
        await apiInstance.createUser({
            username: username,
            email: email,
            role: role,
            password: password
        });

        await getData().then()
    };

    return (
        <div className="calculatorMain">
            <Card sx={{ width: 1000 }}>
                <CardContent>
                    <div className="pageTitle">
                        <span className="titleText">Listagem de Reservas</span>
                        <span>Verifique aqui todas as reservas para aprovação.</span>
                        <div className="separator"/>
                    </div>
                    {reserveList.length > 0 ? reserveList.map((item, idx) => {
                        return (
                            <div key={idx}>
                                <div className="inputRow">
                        
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Id Reserva</span>
                                        <span>{item.id}</span>
                                    </div>
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Id da Mesa</span>
                                        <span>{item.restaurant_table_id}</span>
                                    </div>
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Usuário Dono</span>
                                        <span>{item.user_id}</span>
                                    </div>
                                    <div className="plateItemCol">
                                        <span className="labelPadding">Status de aprovação</span>
                                        <span>{item.approved === true ? "Aprovado" : item.approved === false ? "Rejeitado" : "Pendente"}</span>
                                    </div>
                                    {
                                        item.approved === null && (
                                            <>
                                                <div className="plateButtonCol">
                                                    <Button
                                                        onClick={() => updateReserveStatus(true, item.id)}
                                                        variant="contained"
                                                        endIcon={<Check />}>
                                                        Aprovar
                                                    </Button>
                                                </div>
                                                <div className="plateButtonCol">
                                                    <Button
                                                        onClick={() => updateReserveStatus(false, item.id)}
                                                        color="error"
                                                        variant="contained"
                                                        endIcon={<DeleteForever />}>
                                                        Recusar
                                                    </Button>
                                                </div>
                                            </>
                                        )
                                    }
                                </div>
                                {
                                    reserveList.length - 1 !== idx && (
                                        <div className="separator" />
                                    )
                                }
                            </div>
                        );
                    }) : (
                        <div className="pageTitle">
                            <span><b>Nenhuma reserva cadastrada!</b></span>
                        </div>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}

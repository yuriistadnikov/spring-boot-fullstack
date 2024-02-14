import {Wrap, WrapItem, Spinner, Text} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import React, { useEffect, useState } from "react";
import {getCustomers} from "./services/client.js";
import Card from "./components/Card.jsx";
import CreateCustomerDrawerForm from "./components/CreateCustomerDrawerForm.jsx";
import {errorNotification} from "./services/notification.js";

function App() {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers()
            .then(res => {
                console.log(res.data);
                setCustomers(res.data);
            })
            .catch(error => {
                setError(error);
                console.log(error);
            })
            .finally(() => {
                setLoading(false);
            })
    }

    useEffect(() => {
        fetchCustomers();
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        );
    }

    if (error) {
        if (customers.length <= 0) {
            return (
                <SidebarWithHeader>
                    <CreateCustomerDrawerForm
                        fetchCustomers={fetchCustomers}
                    />
                    <Text mt={"5"}>Ooops, there was an error</Text>
                </SidebarWithHeader>
            );
        }
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawerForm
                    fetchCustomers={fetchCustomers}
                />
                <Text mt={"5"}>No customers available</Text>
            </SidebarWithHeader>
        );
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawerForm
                fetchCustomers={fetchCustomers}
            />
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <Card
                            {...customer}
                            fetchCustomers={fetchCustomers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
}

export default App

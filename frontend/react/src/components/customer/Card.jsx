'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Button,
    useColorModeValue, Tag,
} from '@chakra-ui/react'
import CustomerDeleteButton from "./CustomerDeleteButton.jsx";
import EditCustomerDrawerForm from "./EditCustomerDrawerForm.jsx";

export default function Card({ id, name, age, email, gender, fetchCustomers }) {
    const properties = {id, name, age, email, gender, fetchCustomers}
    const genderType = gender.toUpperCase() === "MALE" ? "men" : "women";
    return (
        <Center py={6}>
            <Box
                maxW={'300px'}
                minW={'300px'}
                m={2}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'lg'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/${genderType}/${id}.jpg`
                        }
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>
                <Stack>
                    <Box p={6} alignContent={"bottom"}>
                        <Stack spacing={2} align={'center'} mb={5}>
                            <Tag borderRadius={"full"}>{id}</Tag>
                            <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                                {name}
                            </Heading>
                            <Text color={'gray.500'}>{email}</Text>
                            <Text color={'gray.500'}>{`Age ${age} | ${gender.toUpperCase()}`}</Text>
                        </Stack>
                    </Box>
                    <Stack direction={"row"} justifyContent={"center"} spacing={10} mb={1} minWidth={200}>
                        <EditCustomerDrawerForm
                            {...properties}
                        />
                        <CustomerDeleteButton
                            fetchCustomers={fetchCustomers}
                            customerId={id}
                            customerName={name}
                        />
                    </Stack>

                </Stack>
            </Box>
        </Center>
    )
}
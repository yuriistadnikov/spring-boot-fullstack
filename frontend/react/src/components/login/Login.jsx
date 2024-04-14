import {
    Alert,
    AlertIcon,
    Box,
    Button,
    Flex,
    FormLabel,
    Heading,
    Image,
    Input,
    Link,
    Stack,
    Text,
} from '@chakra-ui/react';
import {Form, Formik, useField} from "formik";
import * as Yup from 'yup'
import {useAuth} from "../context/AuthContext.jsx";
import {errorNotification} from "../../services/notification.js";
import {useNavigate} from "react-router-dom";
import {useEffect} from "react";

const Login = () => {
    const { customer } = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        if (customer) {
            navigate("/dashboard");
        }
    })

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'}>
                    <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
                    <LoginForm />
                    <Link color={"blue.500"} href={"/signup"}>
                        Dont have an account? Signup now.
                    </Link>
                </Stack>
            </Flex>
            <Flex flex={1} p={10} flexDirection={"column"} alignItems={"center"} justifyContent={"center"} bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}>
                <Text fontSize={"6xl"} color={"white"} fontWeight={"bold"} mb={5}>
                    <Link href={"https://google.com"}>
                        Enroll now
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://user-images.githubusercontent.com/40702606/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png'
                    }
                />
            </Flex>
        </Stack>
    );
}

const LoginForm = () => {
    const { login } = useAuth();
    const navigate = useNavigate();

    return (
        <Formik
            initialValues={{username: '', password: ''}}
            validateOnMount={true}
            validateOnChange={Yup.object({
                username: Yup.string()
                    .email("Must be valid email")
                    .required("Email is required"),
                password: Yup.string()
                    .max(20, "Password  can't be more than 20 characters")
                    .required("Password is required")
            })}
            onSubmit={(values, {setSubmitting}) => {
                setSubmitting(true);
                login(values).then(res => {
                    navigate("/dashboard");
                    console.log("Successfully logged in");
                }).catch(err => {
                    errorNotification(err.code, err.response.data.message);
                }).finally(() => {
                    setSubmitting(false);
                })
            }}>

            {({isValid, isSubmitting}) => {
                return (
                    <Form>
                        <Stack spacing={6}>
                            <MyTextInput label={"Email"} name={"username"} type={"email"} placeholder={"mail@yuriistadnikov.com"}/>
                            <MyTextInput label={"Password"} name={"password"} type={"Password"} placeholder={"Type your password"}/>
                            <Button type={"submit"} disabled={!isValid || isSubmitting}>
                                Login
                            </Button>
                        </Stack>
                    </Form>
                );
            }}

        </Formik>
    );
}

const MyTextInput = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert status={"error"} className="error">
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

export default Login;
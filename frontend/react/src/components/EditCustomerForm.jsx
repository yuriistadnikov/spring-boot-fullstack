import React from 'react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack} from "@chakra-ui/react";
import {editCustomer, saveCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";

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

const MySelect = ({ label, ...props }) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Select {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert status={"error"} className="error">
                    <AlertIcon/>
                    {meta.error}
                </Alert>
            ) : null}
        </Box>
    );
};

// And now we can use these
const CreateCustomerForm = ({ id, name, age, email, gender, fetchCustomers }) => {
    return (
        <>
            <Formik
                initialValues={{
                    id: id,
                    name: name,
                    email: email,
                    age: age,
                    gender: gender,
                }}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    age: Yup.number()
                        .min(16, "Must be at least 16 years of age")
                        .max(100, "Must be less than 100 years of age")
                        .required('Required'),
                    gender: Yup.string()
                        .oneOf(
                            ['MALE', 'FEMALE'],
                            'Invalid gender'
                        )
                        .required('Required'),
                })}
                onSubmit={(customer, { setSubmitting }) => {
                    setSubmitting(true);
                    editCustomer(customer)
                        .then(res => {
                            console.log(res);
                            successNotification(
                                "Customer changed",
                                "Changes was successfully saved"
                            );
                            fetchCustomers();
                        })
                        .catch(error => {
                            console.log(error);
                            errorNotification(
                                error.code,
                                error.response.data.message
                            );
                        })
                        .finally(() => {
                            setSubmitting(false);
                        });
                }}
            >
                {({ isValid, isSubmitting , dirty}) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Name"
                            />

                            <MyTextInput
                                label="Email"
                                name="email"
                                type="email"
                                placeholder="Email"
                            />

                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                                placeholder="Age"
                            />

                            <MySelect label="Gender" name="gender">
                                <option value="">Select gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </MySelect>


                            <Button isDisabled={!isValid || isSubmitting || !dirty} type="submit">Update customer</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default CreateCustomerForm;

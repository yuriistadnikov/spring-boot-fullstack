import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogContent,
    AlertDialogOverlay,
    useDisclosure,
    Button,
} from '@chakra-ui/react'
import { useRef } from "react";
import {errorNotification, successNotification} from "../../services/notification.js";
import {deleteCustomer} from "../../services/client.js";

export default function CustomerDeleteButton({ fetchCustomers, customerId, customerName }) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = useRef()

    const deleteAction = () => {
        deleteCustomer(customerId)
            .then(response => {
                successNotification(
                    `Customer ${customerName} successfully deleted`
                )
                onClose();
                fetchCustomers();
            })
            .catch(error => {
                console.log(error);
                onClose();
                errorNotification(
                    error.code,
                    error.response.data.message
                );
            });
    }

    return (
        <>
            <Button bg={'red.400'}
                    color={'white'}
                    rounded={'full'}
                    _hover={{
                        transform: 'translateY(-2px)',
                        boxShadow: 'lg'
                    }}
                    _focus={{
                        bg: 'green.500'
                    }} onClick={onOpen}>
                Delete
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Customer
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure you want to delete {customerName}? You can't undo this action afterwards.
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button colorScheme='red' onClick={deleteAction} ml={3}>
                                Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}
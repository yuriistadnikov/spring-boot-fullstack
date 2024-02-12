import {
    Button,
    Drawer, DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    Input, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";
import EditCustomerForm from "./EditCustomerForm.jsx";

export default function EditCustomerDrawerForm(properties) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return (
        <>
            <Button onClick={onOpen} colorScheme={"teal"} bg={'green.400'}
                    color={'white'}
                    rounded={'full'}
                    _hover={{
                        transform: 'translateY(-2px)',
                        boxShadow: 'lg'
                    }}
                    _focus={{
                        bg: 'green.500'
                    }}>Edit</Button>
            <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Edit</DrawerHeader>

                    <DrawerBody>
                        <EditCustomerForm
                            {...properties}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button type='submit' form='my-form' colorScheme={"teal"} onClick={onClose}>
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    );
}
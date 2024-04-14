import { createContext, useContext, useEffect, useSta, useState } from "react";
import {login as performLogin} from "../../services/client.js";
import { jwtDecode } from "jwt-decode";


const AuthContext = createContext({});

const AuthProvider = ({ children }) => {
    const [customer, setCustomer] = useState(null);

    const setCustomerFromToken = () => {
        let token = localStorage.getItem("token");
        if (token) {
            token = jwtDecode(token);
            console.log(token);
            setCustomer({
                username: token.sub,
                roles: token.scopes
            })
        }
    }

    useEffect(() => {
        setCustomerFromToken()
    }, [])

    const login = async (loginData) => {
        return new Promise((resolve, reject) => {
            performLogin(loginData).then(res => {
                    const jwtToken = res.headers["authorization"];
                    localStorage.setItem("token", jwtToken);

                    const decodedToken = jwtDecode(jwtToken);
                    setCustomer({
                        username: decodedToken.sub,
                        roles: decodedToken.scopes
                    })
                    resolve(res);
            }).catch(err => {
                reject(err);
            })
        })
    }

    const logOut = () => {
        localStorage.removeItem("token");
        setCustomer(null);
    }

    const isCustomerAuthenticated = () => {
        const token = localStorage.getItem("token");
        if (!token) {
            return false;
        }
        const { exp: expiration } = jwtDecode(token);
        if (Date.now() > expiration * 1000) {
            logOut()
            return false;
        }
        return true;
    }

    return (
        <AuthContext.Provider value={{
            customer,
            login,
            logOut,
            isCustomerAuthenticated,
            setCustomerFromToken
        }}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => useContext(AuthContext);

export default AuthProvider;
import axios from "axios";
import { api, API_URL } from '../../config/Api'
import { LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS, LOGOUT_FAILURE, LOGOUT_REQUEST, LOGOUT_SUCCESS, REGISTER_FAILURE, REGISTER_REQUEST, REGISTER_SUCCESS } from "./ActionType";

export const registerUser = ({ requestData, navigate }) => async (dispatch) => {
    dispatch({ type: REGISTER_REQUEST })
    try {
        const { data } = await api.post('/api/auth/signup', requestData);

        dispatch({ type: REGISTER_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Registration failed. Please try again.';
        dispatch({ type: REGISTER_FAILURE, payload: errorMessage });
    }
}

export const login = (requestData) => async (dispatch) => {
    dispatch({ type: LOGIN_REQUEST })
    try {
        const { data } = await api.post('/api/auth/signin', requestData);
        if (data !== null) {
            localStorage.setItem('token', data.token);
            localStorage.setItem('refreshToken', data.refreshToken);
        }
        dispatch({ type: LOGIN_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Login failed. Please try again.';
        dispatch({ type: LOGIN_FAILURE, payload: errorMessage });
    }
}

export const logout = (token) => async (dispatch) => {
    dispatch({ type: LOGOUT_REQUEST })
    try {
        const { data } = await api.get('/api/auth/logout', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
        if (data !== null) {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
        }
        dispatch({ type: LOGOUT_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Logout failed. Please try again.';
        dispatch({ type: LOGOUT_FAILURE, payload: errorMessage });
    }
}
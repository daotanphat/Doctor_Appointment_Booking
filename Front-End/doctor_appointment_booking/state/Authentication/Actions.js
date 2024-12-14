import axios from "axios";
import { api, API_URL } from '../../config/Api'
import { REGISTER_FAILURE, REGISTER_REQUEST, REGISTER_SUCCESS } from "./ActionType";

export const registerUser = ({ requestData, navigate }) => async (dispatch) => {
    dispatch({ type: REGISTER_REQUEST })
    try {
        const { data } = await api.post('/api/auth/signup', requestData);
        //navigate('/login');
        console.log(data);

        dispatch({ type: REGISTER_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Registration failed. Please try again.';
        dispatch({ type: REGISTER_FAILURE, payload: errorMessage });
    }
}
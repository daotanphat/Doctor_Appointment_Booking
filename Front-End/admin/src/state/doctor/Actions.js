import { api } from "../../config/Api";
import { REFRESH_ERROR, REFRESH_MESSAGE } from "../speciality/ActionTypes";
import { ADD_DOCTOR_FAILURE, ADD_DOCTOR_REQUEST, ADD_DOCTOR_SUCCESS } from "./ActionsTypes";

export const addDoctor = (formData) => async (dispatch) => {
    dispatch({ type: ADD_DOCTOR_REQUEST })
    try {
        const { data } = await api.post('/api/admin/add-doctor', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        console.log(data);
        dispatch({ type: ADD_DOCTOR_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Add doctor failed. Please try again.';
        dispatch({ type: ADD_DOCTOR_FAILURE, payload: errorMessage });
    }
}

export const refreshMessage = () => async (dispatch) => {
    dispatch({ type: REFRESH_MESSAGE })
}

export const refreshError = () => async (dispatch) => {
    dispatch({ type: REFRESH_ERROR })
}
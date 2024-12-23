import { api } from "../../config/Api";
import { REFRESH_ERROR, REFRESH_MESSAGE } from "../speciality/ActionTypes";
import { GET_DOCTOR_BY_ID_FAILURE, GET_DOCTOR_BY_ID_REQUEST, GET_DOCTOR_BY_ID_SUCCESS, GET_DOCTOR_BY_SPECIALITY_FAILURE, GET_DOCTOR_BY_SPECIALITY_REQUEST, GET_DOCTOR_BY_SPECIALITY_SUCCESS, GET_DOCTORS_FAILURE, GET_DOCTORS_REQUEST, GET_DOCTORS_SUCCESS } from "./ActionsTypes";

export const getDoctors = ({ search, speciality, page }) => async (dispatch) => {
    dispatch({ type: GET_DOCTORS_REQUEST })
    try {
        const params = { page }; // Always include the page

        if (search) params.search = search; // Add `search` if it has a value
        if (speciality) params.speciality = speciality; // Add `speciality` if it has a value

        const { data } = await api.get('/api/user/doctor-list', { params });

        dispatch({ type: GET_DOCTORS_SUCCESS, payload: data })
    } catch (error) {
        console.log(error);

        const errorMessage = error.response?.data?.message || 'Load doctors failure!';
        dispatch({ type: GET_DOCTORS_FAILURE, payload: errorMessage });
    }
}

export const getDoctorById = (doctorId) => async (dispatch) => {
    dispatch({ type: GET_DOCTOR_BY_ID_REQUEST })
    try {
        const { data } = await api.get(`/api/user/doctor/${doctorId}`);

        dispatch({ type: GET_DOCTOR_BY_ID_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Load doctor failure!';
        dispatch({ type: GET_DOCTOR_BY_ID_FAILURE, payload: errorMessage });
    }
}

export const getDoctorBySpeciality = (speciality) => async (dispatch) => {
    dispatch({ type: GET_DOCTOR_BY_SPECIALITY_REQUEST })
    try {
        const { data } = await api.get(`/api/user/doctor/speciality/${speciality}`);

        dispatch({ type: GET_DOCTOR_BY_SPECIALITY_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Load doctor failure!';
        dispatch({ type: GET_DOCTOR_BY_SPECIALITY_FAILURE, payload: errorMessage });
    }
}

export const refreshMessage = () => async (dispatch) => {
    dispatch({ type: REFRESH_MESSAGE })
}

export const refreshError = () => async (dispatch) => {
    dispatch({ type: REFRESH_ERROR })
}
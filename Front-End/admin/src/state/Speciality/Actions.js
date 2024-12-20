import { api } from '../../config/Api'
import { ADD_SPECIALITY_FAILURE, ADD_SPECIALITY_REQUEST, ADD_SPECIALITY_SUCCESS, GET_SPECIALITY_FAILURE, GET_SPECIALITY_REQUEST, GET_SPECIALITY_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE } from './ActionTypes'

export const addSpecialty = (formData) => async (dispatch) => {
    dispatch({ type: ADD_SPECIALITY_REQUEST })
    try {
        const { data } = await api.post('/api/admin/add-speciality', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        dispatch({ type: ADD_SPECIALITY_SUCCESS, payload: data })
    } catch (error) {
        console.log(error);

        const errorMessage = error.response?.data?.message || 'Add Speciality failed. Please try again.';
        dispatch({ type: ADD_SPECIALITY_FAILURE, payload: errorMessage });
    }
}

export const getAllSpecialties = () => async (dispatch) => {
    dispatch({ type: GET_SPECIALITY_REQUEST })
    try {
        const { data } = await api.get('/api/admin/specialities');

        dispatch({ type: GET_SPECIALITY_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Get specialities failed!';
        dispatch({ type: GET_SPECIALITY_FAILURE, payload: errorMessage });
    }
}

export const refreshMessage = () => async (dispatch) => {
    dispatch({ type: REFRESH_MESSAGE })
}

export const refreshError = () => async (dispatch) => {
    console.log('error refresh');

    dispatch({ type: REFRESH_ERROR })
}
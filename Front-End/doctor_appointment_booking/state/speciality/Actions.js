import { api } from '../../config/Api'
import { GET_SPECIALITY_FAILURE, GET_SPECIALITY_REQUEST, GET_SPECIALITY_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE } from './ActionTypes'

export const getAllSpecialties = () => async (dispatch) => {
    dispatch({ type: GET_SPECIALITY_REQUEST })
    try {
        const { data } = await api.get('/api/user/specialities');
        dispatch({ type: GET_SPECIALITY_SUCCESS, payload: data })
    } catch (error) {
        console.log(error);
        
        const errorMessage = error.response?.data?.message || 'Load Specialities failed!';
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
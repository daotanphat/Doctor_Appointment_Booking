import { api } from '../../config/Api'
import { GET_USER_INFO_FAILURE, GET_USER_INFO_REQUEST, GET_USER_INFO_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE, UPDATE_USER_INFO_FAILURE, UPDATE_USER_INFO_REQUEST, UPDATE_USER_INFO_SUCCESS } from './ActionTypes';

export const getUserInfo = () => async (dispatch) => {
    dispatch({ type: GET_USER_INFO_REQUEST })
    try {
        const { data } = await api.get('/api/user/my-info');
        dispatch({ type: GET_USER_INFO_SUCCESS, payload: data })
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Load user info failed!';
        dispatch({ type: GET_USER_INFO_FAILURE, payload: errorMessage });
    }
}

export const updateUserInfo = (formData) => async (dispatch) => {
    dispatch({ type: UPDATE_USER_INFO_REQUEST })
    try {
        console.log('hallo');

        const { data } = await api.post('/api/user/update-info', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        console.log(data);

        dispatch({ type: UPDATE_USER_INFO_SUCCESS, payload: data })
    } catch (error) {
        console.log(error);

        const errorMessage = error.response?.data?.message || 'Update user info failed!';
        dispatch({ type: UPDATE_USER_INFO_FAILURE, payload: errorMessage });
    }
}

export const refreshMessage = () => async (dispatch) => {
    dispatch({ type: REFRESH_MESSAGE })
}

export const refreshError = () => async (dispatch) => {
    dispatch({ type: REFRESH_ERROR })
}
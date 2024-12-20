import { ADD_SPECIALITY_FAILURE, ADD_SPECIALITY_REQUEST, ADD_SPECIALITY_SUCCESS, GET_SPECIALITY_FAILURE, GET_SPECIALITY_REQUEST, GET_SPECIALITY_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE } from './ActionTypes'

const initialState = {
    specialities: [],
    isLoading: null,
    error: null,
    message: null
}

export const specialityReducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_SPECIALITY_REQUEST:
        case GET_SPECIALITY_REQUEST:
            return {
                ...state,
                isLoading: true,
                error: null
            }
        case ADD_SPECIALITY_SUCCESS:
            return {
                ...state,
                isLoading: false,
                message: action.payload.message,
                error: null
            }
        case GET_SPECIALITY_SUCCESS:
            return {
                ...state,
                isLoading: false,
                specialities: action.payload,
            }
        case GET_SPECIALITY_FAILURE:
        case ADD_SPECIALITY_FAILURE:
            return {
                ...state,
                isLoading: false,
                error: action.payload,
                message: null
            }
        case REFRESH_MESSAGE:
            return {
                ...state,
                message: null
            }
        case REFRESH_ERROR:
            return {
                ...state,
                error: null
            }
        default:
            return state;
    }
}
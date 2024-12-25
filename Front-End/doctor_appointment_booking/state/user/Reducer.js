import { GET_USER_INFO_FAILURE, GET_USER_INFO_REQUEST, GET_USER_INFO_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE, UPDATE_USER_INFO_FAILURE, UPDATE_USER_INFO_REQUEST, UPDATE_USER_INFO_SUCCESS } from "./ActionTypes"

const initialState = {
    user: null,
    isLoading: null,
    error: null,
    message: null
}

export const userReducer = (state = initialState, action) => {
    switch (action.type) {
        case GET_USER_INFO_REQUEST:
        case UPDATE_USER_INFO_REQUEST:
            return {
                ...state,
                isLoading: true,
                error: null
            }
        case GET_USER_INFO_SUCCESS:
            return {
                ...state,
                isLoading: false,
                user: action.payload
            }
        case UPDATE_USER_INFO_SUCCESS:
            return {
                ...state,
                isLoading: false,
                message: action.payload
            }
        case UPDATE_USER_INFO_FAILURE:
        case GET_USER_INFO_FAILURE:
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
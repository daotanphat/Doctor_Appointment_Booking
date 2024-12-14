import { LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS, REGISTER_FAILURE, REGISTER_REQUEST, REGISTER_SUCCESS } from "./ActionType"

const initialState = {
    user: null,
    isLoading: null,
    error: null,
    jwt: null,
    refreshToken: null,
    message: null
}

export const authReducer = (state = initialState, action) => {
    switch (action.type) {
        case REGISTER_REQUEST:
        case LOGIN_REQUEST:
            return {
                ...state,
                isLoading: true,
                error: null
            }
        case REGISTER_SUCCESS:
            return {
                ...state,
                isLoading: false,
                message: action.payload.message
            }
        case LOGIN_SUCCESS:
            return {
                ...state,
                isLoading: false,
                jwt: action.payload.token,
                refreshToken: action.payload.refreshToken,
                message: null
            }
        case LOGIN_FAILURE:
        case REGISTER_FAILURE:
            return {
                ...state,
                isLoading: false,
                error: action.payload,
                message: null
            }
        default:
            return state;
    }
}
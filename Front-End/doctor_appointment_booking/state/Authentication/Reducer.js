import { LOGIN_FAILURE, LOGIN_REQUEST, LOGIN_SUCCESS, LOGOUT_FAILURE, LOGOUT_REQUEST, LOGOUT_SUCCESS, REFRESH_TOKEN_FAILURE, REFRESH_TOKEN_REQUEST, REFRESH_TOKEN_SUCCESS, REGISTER_FAILURE, REGISTER_REQUEST, REGISTER_SUCCESS } from "./ActionType"

const initialState = {
    user: null,
    isLoading: null,
    error: null,
    jwt: null,
    refreshToken: null,
    roles: [],
    message: null
}

export const authReducer = (state = initialState, action) => {
    switch (action.type) {
        case REGISTER_REQUEST:
        case LOGIN_REQUEST:
        case LOGOUT_REQUEST:
        case REFRESH_TOKEN_REQUEST:
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
                user: action.payload.email,
                isLoading: false,
                jwt: action.payload.token,
                refreshToken: action.payload.refreshToken,
                roles: action.payload.roles,
                message: null
            }
        case LOGOUT_SUCCESS:
            return {
                ...state,
                isLoading: false,
                message: action.payload.message
            }
        case REFRESH_TOKEN_SUCCESS:
            return {
                ...state,
                isLoading: false,
                jwt: action.payload.accessToken,
                refreshToken: action.payload.refreshToken,
            }
        case REFRESH_TOKEN_FAILURE:
        case LOGOUT_FAILURE:
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
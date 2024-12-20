import { ADD_DOCTOR_FAILURE, ADD_DOCTOR_REQUEST, ADD_DOCTOR_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE } from "./ActionsTypes";

const initialState = {
    doctors: [],
    isLoading: null,
    error: null,
    message: null
}

export const doctorReducer = (state = initialState, action) => {
    switch (action.type) {
        case ADD_DOCTOR_REQUEST:
            return {
                ...state,
                isLoading: true,
                error: null,
            }
        case ADD_DOCTOR_SUCCESS:
            return {
                ...state,
                isLoading: false,
                message: action.payload.message
            }
        case ADD_DOCTOR_FAILURE:
            return {
                ...state,
                isLoading: false,
                error: action.payload
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
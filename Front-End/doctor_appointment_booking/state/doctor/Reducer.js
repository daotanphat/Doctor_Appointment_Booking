import { GET_DOCTORS_FAILURE, GET_DOCTORS_REQUEST, GET_DOCTORS_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE } from "./ActionsTypes";

const initialState = {
    doctors: [],
    totalPages: null,
    isLoading: null,
    error: null,
    message: null
}

export const doctorReducer = (state = initialState, action) => {
    switch (action.type) {
        case GET_DOCTORS_REQUEST:
            return {
                ...state,
                isLoading: true,
                error: null,
            }
        case GET_DOCTORS_SUCCESS:
            return {
                ...state,
                isLoading: false,
                doctors: action.payload.objects,
                totalPages: action.payload.totalPages
            }
        case GET_DOCTORS_FAILURE:
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
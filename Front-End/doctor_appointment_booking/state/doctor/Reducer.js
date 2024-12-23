import { GET_DOCTOR_BY_ID_FAILURE, GET_DOCTOR_BY_ID_REQUEST, GET_DOCTOR_BY_ID_SUCCESS, GET_DOCTOR_BY_SPECIALITY_FAILURE, GET_DOCTOR_BY_SPECIALITY_REQUEST, GET_DOCTOR_BY_SPECIALITY_SUCCESS, GET_DOCTORS_FAILURE, GET_DOCTORS_REQUEST, GET_DOCTORS_SUCCESS, REFRESH_ERROR, REFRESH_MESSAGE } from "./ActionsTypes";

const initialState = {
    doctors: [],
    doctor: null,
    totalPages: null,
    isLoading: null,
    error: null,
    message: null
}

export const doctorReducer = (state = initialState, action) => {
    switch (action.type) {
        case GET_DOCTORS_REQUEST:
        case GET_DOCTOR_BY_ID_REQUEST:
        case GET_DOCTOR_BY_SPECIALITY_REQUEST:
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
        case GET_DOCTOR_BY_ID_SUCCESS:
            return {
                ...state,
                isLoading: false,
                doctor: action.payload
            }
        case GET_DOCTOR_BY_SPECIALITY_SUCCESS:
            return {
                ...state,
                isLoading: false,
                doctors: action.payload
            }
        case GET_DOCTOR_BY_SPECIALITY_FAILURE:
        case GET_DOCTOR_BY_ID_FAILURE:
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
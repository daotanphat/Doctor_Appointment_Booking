import axios from "axios";
import { logout, refreshTokenRequest } from "../state/Authentication/Actions";
import { useSelector } from "react-redux";

export const API_URL = 'http://localhost:8080';

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    }
});

// Request Interceptor to add the access token to headers if it exists
api.interceptors.request.use(
    (config) => {

        if (!config.url.includes('/api/auth/')) {
            const accessToken = useSelector((state) => state.auth.accessToken); // Access token from Redux store
            console.log(accessToken + ' token - api.jsx');

            if (accessToken) {
                config.headers['Authorization'] = `Bearer ${accessToken}`;
            }
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response Interceptor to handle token refresh
api.interceptors.response.use(
    (response) => response, // Return the response if successful
    async (error) => {
        const originalRequest = error.config;

        // If the error response status is 401 (Unauthorized) and the request has not been retried
        if (error.response && error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            console.log('error 3');

            // Get the refresh token from Redux store
            const refreshTokenFromStore = store.getState().auth.refreshToken;

            try {
                // Call the refresh token API to get a new access token
                const response = await refreshTokenRequest(refreshTokenFromStore);

                // Retry the original request with the new access token
                originalRequest.headers['Authorization'] = `Bearer ${response.data.accessToken}`;
                return api(originalRequest); // Retry the original request with the new token
            } catch (refreshError) {
                // If refresh token fails, logout the user
                const accessToken = store.getState().auth.jwt;
                store.dispatch(logout(accessToken));
                window.location.href = '/login'; // Redirect to login page
                return Promise.reject(refreshError);
            }
        }

        return Promise.reject(error);
    }
);
import axios from "axios";
import { logout, refreshTokenRequest } from "../state/Authentication/Actions";
import { store } from '../state/Store';

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
        // Define the list of API paths that require the Authorization token
        const pathsRequiringToken = [
            '/api/user/my-info',
            '/api/user/my-appointments',
        ];

        // Check if the URL matches any of the paths requiring a token
        if (pathsRequiringToken.some(path => config.url.includes(path))) {
            const accessToken = localStorage.getItem('token');
            if (!accessToken && !window.location.pathname.includes('/login')) {
                window.location.href = '/login'; // Redirect to login page
                return Promise.reject(new Error('No access token found, redirecting to login'));
            } else {
                config.headers.Authorization = `Bearer ${accessToken}`
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

            // Get the refresh token from Redux store
            const refreshTokenFromStore = localStorage.getItem('refreshToken');

            if (refreshTokenFromStore !== null) {
                try {
                    // Call the refresh token API to get a new access token
                    const response = await refreshTokenRequest(refreshTokenFromStore);

                    // Retry the original request with the new access token
                    originalRequest.headers['Authorization'] = `Bearer ${response.data.accessToken}`;
                    return api(originalRequest); // Retry the original request with the new token
                } catch (refreshError) {
                    // If refresh token fails, logout the user
                    const email = store.getState().auth.user;
                    store.dispatch(logout(email));
                    window.location.href = '/login'; // Redirect to login page
                    return Promise.reject(refreshError);
                }
            }
        }

        return Promise.reject(error);
    }
);
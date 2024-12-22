import { applyMiddleware, combineReducers, legacy_createStore } from "redux";
import { authReducer } from "./Authentication/Reducer";
import { thunk } from "redux-thunk";
import storage from "redux-persist/lib/storage";
import { persistReducer, persistStore } from "redux-persist";
import { doctorReducer } from "./doctor/Reducer";
import { specialityReducer } from './speciality/Reducer';

const rooteReducer = combineReducers({
    auth: authReducer,
    speciality: specialityReducer,
    doctor: doctorReducer
})

const rootReducer = (state, action) => {
    if (action.type === 'LOGOUT_SUCCESS') {
        state = undefined;
    }
    return rooteReducer(state, action);
}

const persitConfig = {
    key: 'root',
    storage,
    whitelist: ['auth', 'speciality', 'doctor']
}

const persitedReducer = persistReducer(persitConfig, rootReducer);

export const store = legacy_createStore(persitedReducer, applyMiddleware(thunk));
export const persistor = persistStore(store);
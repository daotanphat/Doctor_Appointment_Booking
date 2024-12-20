import { applyMiddleware, combineReducers, legacy_createStore } from "redux";
import { specialityReducer } from './speciality/Reducer';
import storage from "redux-persist/lib/storage";
import { persistReducer, persistStore } from "redux-persist";
import { thunk } from "redux-thunk";
import { doctorReducer } from "./doctor/Reducer";

const rooteReducer = combineReducers({
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
    storage
}

const persitedReducer = persistReducer(persitConfig, rootReducer);

export const store = legacy_createStore(persitedReducer, applyMiddleware(thunk));
export const persistor = persistStore(store);
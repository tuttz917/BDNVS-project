// authService.js
let memoryToken = null;

export const setAccessToken = (token) => {
    memoryToken = token;
};

export const getAccessToken = () => {
    return memoryToken;
};

export const logout = () => {
    memoryToken = null;
    // Opțional: apel către backend pentru ștergerea cookie-ului de refresh
};
import api from './Api.js'

/**
 *  json 형태의 데이터가 원칙임 예외시 표기
 */

/**
 *
 * @param data email, password, name
 * @returns {Promise<*>} email, name
 */
export async function signupUser(data) {
    const response = await api.post('/signup', data);
    return response.data;
}

/**
 *
 * @param data email, password
 * @returns {Promise<void>}
 */
export async function loginUser(data) {
    const response = await api.post('/login', data);
    const token  = response.headers.get('authentication');

    if (token) {
        localStorage.setItem('token', token);
    }
}
